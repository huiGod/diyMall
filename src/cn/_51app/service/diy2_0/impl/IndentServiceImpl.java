package cn._51app.service.diy2_0.impl;


import java.awt.image.BufferedImage;
import java.io.File;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import cn._51app.dao.diy2_0.IActiviteDao;
import cn._51app.dao.diy2_0.IAdressDao;
import cn._51app.dao.diy2_0.IShopCartDao;
import cn._51app.dao.diy2_0.IUserWorksDao;
import cn._51app.dao.diy2_0.IndentDao;
import cn._51app.dao.ocs.OCSDao;
import cn._51app.service.BaseService;
import cn._51app.service.diy2_0.IAdressService;
import cn._51app.service.diy2_0.IShopCartService;
import cn._51app.service.diy2_0.IndentService;
import cn._51app.util.CommonUtil;
import cn._51app.util.DateUtil;
import cn._51app.util.JSONUtil;
import cn._51app.util.OCSKey;
import cn._51app.util.PropertiesUtil;
import cn._51app.util.RedisUtil;
import cn._51app.util.WxPayUtil;

@Service
public class IndentServiceImpl extends BaseService implements IndentService {
	
	private final String diyRootPath =PropertiesUtil.getValue("diy.root.path");
	private final String orderNumer =PropertiesUtil.getValue("diy.order.page.size");
	private final String cutPath=PropertiesUtil.getValue("diy.cut.path");
	private final  String updownloadRootDir =PropertiesUtil.getValue("uploadUrl.sys");
	private Logger logger=Logger.getLogger(IndentServiceImpl.class);
	private ObjectMapper mapper=new ObjectMapper();
	DecimalFormat df=new DecimalFormat("######0.00");
	private String expressMsg="待付款";
	
	@Autowired
	private IndentDao indentDao;
	
	@Autowired
	private IAdressService iAdressService;
	
	@Autowired
	private OCSDao ocsDao;
	
	@Autowired
	private IShopCartService iShopCartService;
	
	@Autowired
	private IActiviteDao iActiviteDao;
	
	@Autowired
	private IShopCartDao iShopCartDao;
	
	@Autowired
	private IUserWorksDao iUserWorksDao;
	
	@Autowired
	private IAdressDao iAdressDao;

	@Override
	public String createOrder(MultipartFile imgFile, MultipartFile imgOrderFile, MultipartFile imgFile2,MultipartFile imgOrderFile2, String infoId,
			String textureIds, String deviceNo, String app, String num,HttpServletRequest req,String wallpaper,String isBoutique) throws Exception {
		Map<String, Object> resultMap=new HashMap<>();
		Map<String,Object> activityMap=null;
		List<Map<String,Object>> list=null;
		String orderNo=CommonUtil.createOrderNo("A", 5);
		String resultPath=null;
		String sufFormat=".jpg";
		//是否定制
		boolean customize=false;
		//判断是否精品汇商品,除去手机壳方式wallpaper
		if(imgFile!=null && wallpaper==null){
			customize=true;
			resultPath=CommonUtil.orderBF("2", imgFile, imgOrderFile, imgFile2, imgOrderFile2,orderNo).split("#")[0];
		//照片书
		}else if(CommonUtil.isFile(req) && wallpaper==null){
			if(isBoutique!=null || !isBoutique.equals("")){
				customize=false;
				sufFormat="xxx";
			}
			else
			customize=true;
			resultPath=CommonUtil.orderBatchImg(req, orderNo).split("#")[0];
		//壁纸手机壳
		}else if(imgFile2!=null && wallpaper!=null){
			String pathName=new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());
			String suffix=wallpaper.substring(wallpaper.lastIndexOf("."));
			File file=new File(updownloadRootDir+cutPath+pathName);
			if(!file.exists()){
				file.mkdirs();
			}
			//原图
			if(imgFile2!=null && !imgFile2.isEmpty()){
				File imgFilePath=new File(updownloadRootDir+cutPath+pathName+"/"+orderNo+suffix);
				if(!imgFilePath.exists()){
					imgFilePath.createNewFile();
				}
				imgFile2.transferTo(imgFilePath);
			}
			//下载url图片到本地
			byte[] btImg = CommonUtil.getImageFromNetByUrl(wallpaper);  
			//原图路径
			String fileName =updownloadRootDir+cutPath+pathName+"/"+orderNo+"@cutBefore"+suffix;  
		    if(null != btImg && btImg.length > 0){  
		         CommonUtil.writeImageToDisk(btImg, fileName);  
		    }
		    //裁剪图片
		    CommonUtil.cutImage(643, 1143, fileName,updownloadRootDir+cutPath +pathName+"/"+orderNo+"@cut"+suffix);
			resultPath=cutPath+pathName+"/"+orderNo+suffix;
			sufFormat="xxx";
		}else{
			//查询出精品会商品图片  (预览图足够)
			resultPath=this.indentDao.queryPreUrl(infoId,textureIds); 
			sufFormat="xxx";
		}
		//插入到订单表
		Map<String, Object> returnMap=this.indentDao.createOrder(resultPath,infoId,textureIds,deviceNo,app,orderNo,sufFormat,num,customize,"0");
		if(returnMap!=null){
			activityMap=new HashMap<String,Object>();
			list=new ArrayList<Map<String,Object>>();
			List<Map<String,Object>> shopList=new ArrayList<Map<String,Object>>();
			Map<String,Object> map=new HashMap<String,Object>();
			String type=returnMap.get("type")==null?"0":returnMap.get("type").toString();
			activityMap.put("Num"+type,num);
			activityMap.put("Price"+type,returnMap.get("orgPrice"));
			list.add(returnMap);
			String feeNum=(String)resultMap.get("feeNum");
			map.put("feeNum", feeNum==null?"":feeNum);
			String feePostage=(String)resultMap.get("feePostage");
			map.put("feePostage",feePostage==null ?"":feePostage);
			
			map.put("orderNo", orderNo);
			map.put("sortName",returnMap.get("sortName"));
			map.put("textureName", returnMap.get("textureNames"));
			//格式化照片书的图片
			if(isBoutique==null || isBoutique.equals(""))
			map.put("imgUrl", diyRootPath+resultPath);
			else{
				if(resultPath.contains("_")){
					String imgs[]=resultPath.split("_");
					map.put("imgUrl", diyRootPath+imgs[0]+"_1.jpg");
				}
			}
			map.put("fileType", sufFormat);
			map.put("num", num);
			map.put("name", returnMap.get("name"));
			map.put("transportfee",resultMap.get("transportfee"));
			map.put("nowPrice", returnMap.get("orgPrice"));
			map.put("type", returnMap.get("type"));
			map.put("isselect", returnMap.get("isselect"));
			
//			List<Map<String,Object>> resultList=CommonUtil.getActivityMethod(activityMap,list);
			shopList.add(map);
//			resultMap.put("activityList", resultList);
			resultMap.put("shopList",shopList);
			resultMap.put("companyName",returnMap.get("companyName"));
			resultMap.put("companyId",returnMap.get("user_id"));
			return mapper.writeValueAsString(resultMap);
		}
		return null;
	}
	
	@Override
	public String createOrderForId(MultipartFile imgFile, MultipartFile imgOrderFile, MultipartFile imgFile2,MultipartFile imgOrderFile2, String infoId,
			String textureIds, String userId, String num,HttpServletRequest req,String wallpaper,String isBoutique,String lettering,String modId,String keycode,String isSave) throws Exception {
		Map<String, Object> resultMap=new HashMap<>();
		Map<String,Object> activityMap=null;
		String orderNo=CommonUtil.createOrderNo("A", 5);
		String resultPath=null;
		String sufFormat=".jpg";
		//是否定制
		boolean customize=false;
		//判断是否精品汇商品,除去手机壳方式wallpaper
		if(StringUtils.isBlank(keycode)){
			if(imgFile!=null && wallpaper==null){
				customize=true;
				resultPath=CommonUtil.orderBF("2", imgFile, imgOrderFile, imgFile2, imgOrderFile2,orderNo).split("#")[0];
			}else if(imgFile2!=null && wallpaper==null){
				customize=true;
				resultPath=CommonUtil.orderBF("2", imgFile, imgOrderFile, imgFile2, imgOrderFile2,orderNo).split("#")[0];
			//照片书
			}else if(CommonUtil.isFile(req) && wallpaper==null){
				if(isBoutique!=null && !isBoutique.equals("")){
					customize=false;
					sufFormat="xxx";
				}else{
					customize=true;
				}
				resultPath=CommonUtil.orderBatchImg(req, orderNo).split("#")[0];
			//壁纸手机壳 
			}else if(imgFile2!=null && wallpaper!=null){
				String pathName=new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());
				String suffix=wallpaper.substring(wallpaper.lastIndexOf("."));
				File file=new File(updownloadRootDir+cutPath+pathName);
				if(!file.exists()){
					file.mkdirs();
				}
				//原图
				if(imgFile2!=null && !imgFile2.isEmpty()){
					File imgFilePath=new File(updownloadRootDir+cutPath+pathName+"/"+orderNo+suffix);
					if(!imgFilePath.exists()){
						imgFilePath.createNewFile();
					}
					imgFile2.transferTo(imgFilePath);
				}
				//下载url图片到本地
				byte[] btImg = CommonUtil.getImageFromNetByUrl(wallpaper);  
				//原图路径
				String fileName =updownloadRootDir+cutPath+pathName+"/"+orderNo+"@cutBefore"+suffix;  
			    if(null != btImg && btImg.length > 0){  
			         CommonUtil.writeImageToDisk(btImg, fileName);  
			    }
			    //裁剪图片
			    CommonUtil.cutImage(643, 1143, fileName,updownloadRootDir+cutPath +pathName+"/"+orderNo+"@cut"+suffix);
				resultPath=cutPath+pathName+"/"+orderNo+suffix;
				sufFormat="xxx";
			}else{
				//查询出精品会商品图片  (预览图足够)
				resultPath=this.indentDao.queryPreUrl(infoId,textureIds);
				sufFormat="xxx";
			}
		}else{
			//照片冲印
			String tmonth=DateUtil.date2String(new Date(), DateUtil.FORMAT_DIRMONTH);
			String path="/data/resource/diymall/photos/"+tmonth+"/"+keycode;
			File paraPath=new File(path);
			File[] paraFiles=paraPath.listFiles();
			resultPath="photos/"+tmonth+"/"+keycode+"_"+paraFiles.length/2;
			sufFormat="xxx";
		}
		if("xxx".equals(sufFormat)){
			customize=false;
		}else{
			customize=true;
		}
		//刻字的就用精品的材质id
		if(lettering!=null && StringUtils.isNotBlank(lettering)){
			customize=true;
		}
		//插入到订单表
		Map<String, Object> returnMap=this.indentDao.createOrderForId(resultPath,infoId,textureIds,userId,orderNo,sufFormat,num,customize,"0",lettering,modId);

		if(returnMap!=null){
			//加入作品,刻字的材质和定制材质是公用的哦
			if(!"xxx".equals(sufFormat) && (modId==null || modId.isEmpty()) && "1".equals(isSave)){
				Map<String, Object> jphInfo=null;	
					jphInfo=this.indentDao.queryJphInfoByInfoId(infoId,textureIds);
				if(jphInfo==null || jphInfo.isEmpty())
					jphInfo=this.indentDao.queryJphInfoByInfoId2(infoId,textureIds);
				if(jphInfo!=null && !jphInfo.isEmpty()){
					Map<String, Object> paramMap=new HashMap<>();
					paramMap.put("name", "");
					paramMap.put("cont", "");
					paramMap.put("isopen", 0);
					paramMap.put("saveImg", resultPath);
					paramMap.put("suffix", sufFormat);
					paramMap.put("goodId", jphInfo.get("make_id"));
					paramMap.put("userId", userId);
					paramMap.put("textureIds", textureIds);
					paramMap.put("type", "1");
					paramMap.put("orgPrice", jphInfo.get("orgPrice"));
					paramMap.put("textureNames", indentDao.queryTextureById(textureIds.split("_")));
					paramMap.put("money", jphInfo.get("nowPrice"));
					String workId=this.iUserWorksDao.saveWorks(paramMap);
					Map<String, Object> temMap=(Map<String, Object>)JSONUtil.convertJSONToObject(workId, HashMap.class);
					RedisUtil.zadd(OCSKey.DIY_WORKLIST_LIKE, 0, String.valueOf(temMap.get("workId")));
				}
			}
			activityMap=new HashMap<String,Object>();
//			list=new ArrayList<Map<String,Object>>();
			List<Map<String,Object>> shopList=new ArrayList<Map<String,Object>>();
			Map<String,Object> map=new HashMap<String,Object>();
			Map<String,Object> companyMap=new HashMap<String,Object>();
			String type=returnMap.get("type")==null?"0":returnMap.get("type").toString();
			String actId=returnMap.get("actId")==null?"0":returnMap.get("actId").toString();
			String userIds=returnMap.get("merchant").toString();
			
			activityMap.put("Num"+type+"_"+userIds,num);
			activityMap.put("Price"+type+"_"+userIds,returnMap.get("orgPrice"));
			
//			list.add(returnMap);
		
			//按商家把活动分开
			String act= companyMap.get(userIds)==null?"":companyMap.get(userIds).toString();
			if(act.equals("")){
				//查询活动的详情
				Map<String,Object>typeInfo=indentDao.getTypeMap(actId);
				if(typeInfo!=null){
					typeInfo.put("companyId",userIds);
					companyMap.put(userIds,typeInfo);
				}
			}
			
			map.put("feeNum", returnMap.get("feeNum"));
			map.put("feePostage",returnMap.get("feePostage"));
			map.put("postage",returnMap.get("postage"));
			
			map.put("orderNo", orderNo);
			map.put("sortName",returnMap.get("sortName"));
			map.put("textureName", returnMap.get("textureNames"));
			map.put("imgUrl", diyRootPath+resultPath+".jpg");
			map.put("ispostage", returnMap.get("ispostage"));
			//格式化照片书的图片
			if(isBoutique==null || isBoutique.equals(""))
			if(sufFormat.equals("xxx"))
			map.put("imgUrl", diyRootPath+resultPath);
			else
			map.put("imgUrl", diyRootPath+resultPath+"@b"+sufFormat);
			else{
				if(resultPath.contains("_")){
					String imgs[]=resultPath.split("_");
					map.put("imgUrl", diyRootPath+imgs[0]+"_1.jpg");
				}
			}
			map.put("fileType", sufFormat);
			map.put("num", returnMap.get("num"));
			map.put("name", returnMap.get("name"));
			map.put("nowPrice", (double)returnMap.get("orgPrice"));
			map.put("type", returnMap.get("type"));
			map.put("isselect", 1);
			map.put("cover_size", returnMap.get("cover_size"));
			map.put("wh_size", returnMap.get("wh_size"));
			map.put("origin", returnMap.get("origin"));
			String pre_url=(String) returnMap.get("pre_url");
			String coverImg=(String) returnMap.get("coverImg");
			map.put("pre_url", pre_url==null?"":diyRootPath+pre_url);
			map.put("coverImg", coverImg==null?"":diyRootPath+coverImg);
			List<Map<String,Object>> resultList=CommonUtil.getActivityMethod(activityMap,companyMap);
			shopList.add(map);
			resultMap.put("activityList", resultList);
			resultMap.put("shopList",shopList);
			resultMap.put("companyId",returnMap.get("user_id"));
			resultMap.put("transportfee", returnMap.get("transportfee"));
			resultMap.put("remotefee", returnMap.get("remotefee"));
			resultMap.put("enoughMoney", returnMap.get("enoughMoney"));
			resultMap.put("companyName", returnMap.get("companyName"));
			return mapper.writeValueAsString(resultMap);
		}
		return null;
	}
	
	@Override
	public String getPrivilege() throws Exception {
		String json=this.ocsDao.query(OCSKey.DIY_PRIVILEGE2);
		if(StringUtils.isBlank(json)){
			List<Map<String, Object>> list=this.indentDao.queryPrivilege();
			if(list==null){
				return null;
			}
			json=mapper.writeValueAsString(list);
			if (!ocsDao.insert(OCSKey.DIY_PRIVILEGE2, json, 0) && logger.isInfoEnabled()) {
				logger.info("NewOrderService getPrivilege 我的优惠券初始化失败");
			}
		}
		return json;
	}
	
	/**
	 * tengh 2016年6月7日 下午3:11:17
	 * @param appid
	 * @param key
	 * @return
	 * TODO 获取微信配置
	 */
	private String getWxPay(String app, String key) throws Exception{
		String json=this.ocsDao.query(OCSKey.DIY_WX_APP2+app.split("V")[0]);
		if(StringUtils.isBlank(json)){
			Map<String, Object> teMap=this.indentDao.getWxPay(app.split("V")[0]);
			if(teMap!=null){
				json=mapper.writeValueAsString(teMap);
				ocsDao.insert(OCSKey.DIY_WX_APP2+app.split("V")[0], json, 0);
			}
		}
		if(StringUtils.isNotBlank(json)){
			Map<String, Object> map=mapper.readValue(json, HashMap.class);
			return (String)map.get(key);
		}
		return null;
	}
	
	@Override
	public String activeOrder(String deviceNo,String app,String couponIds,String orderNo,String addressId,String num,String payId)throws Exception{
		//验证订单数量是否满足
		boolean flag=true;
		Map<String,Object>orderGoodsNumAndPay=this.indentDao.confirmOrderNumAndPay(orderNo, deviceNo, app);
		double totalFee=0;
		String []infoIdss=((String)orderGoodsNumAndPay.get("infoIds")).split(",");
		String []nums=((String)orderGoodsNumAndPay.get("num")).split(",");
		String []fileType=((String)orderGoodsNumAndPay.get("fileType")).split(",");
		String []prices=((String)orderGoodsNumAndPay.get("price")).split(",");
		String activities=orderGoodsNumAndPay.get("activities")!=null?orderGoodsNumAndPay.get("activities").toString():"";
		//查询所有活动
		List<Map<String,Object>>getAcitvityList=this.indentDao.getAcitvityList(activities);
		//商品 数量和并然后拿去检测 每种数量是否充足
		Map<String,Integer>infoNum=new HashMap<>();
		Map<String,Object>activityMap=new HashMap<>();
		//加入文件后缀，用来判断是否精品汇，分表查询商品
		for(int i=0;i<infoIdss.length;i++){
			Integer value=infoNum.get(infoIdss[i]+"_"+fileType[i]);
			if(value==null || value==0){
				infoNum.put(infoIdss[i]+"_"+fileType[i],Integer.parseInt(nums[i]));
			}else{
				infoNum.put(infoIdss[i]+"_"+fileType[i],value+Integer.parseInt(nums[i]));
			}
			
			//计算活动
			String type=this.indentDao.getType(infoIdss[i],fileType[i]);
			String nn=activityMap.get("Num"+type)==null?"":activityMap.get("Num"+type).toString();
			String price=activityMap.get("Price"+type)==null?"":activityMap.get("Price"+type).toString();
			if(num.equals("") || price.equals("")){
				activityMap.put("Num"+type,nums[i]);
				activityMap.put("Price"+type, prices[i]);
			}else{
				activityMap.put("Num"+type,nn+","+nums[i]);
				activityMap.put("Price"+type,price+","+prices[i]);
			}
		}
		//验证商品库存
		for(String key:infoNum.keySet()){
			flag=this.indentDao.checkOrderNum(infoNum.get(key),key);
			if(!flag){
				break;
			}
		}
		//商品数量充足
		if(flag){
			Map<String,Object>resultMap=new HashMap<>();
			//查询订单总价 单价*数量+运费   price transportfee
			Map<String,Object> orderInfo=this.indentDao.queryOrderInfo(orderNo, deviceNo, app);
			if(orderInfo==null){
				return null;
			}
			String transportfee_[]=orderInfo.get("transportfee").toString().split(",");
			double price=Double.valueOf((String)orderInfo.get("price"));
			double transportfee=0.00;
			for(int i=0;i<transportfee_.length;i++){
				transportfee+=Double.valueOf(transportfee_[i]);
			}
			totalFee=price*Integer.parseInt(num);
			double orgPrice=0.00,desPrice=0.00;
			//处理活动
//			List<Map<String,Object>> resultList=CommonUtil.getActivityMethod(activityMap,getAcitvityList);
			List<Map<String,Object>> resultList=null;
			double nowPrice=0.00;
			if(resultList!=null){
				for(Map<String,Object>m : resultList){
					nowPrice+=Double.valueOf(m.get("nowPrice").toString());
					desPrice+=Double.valueOf(m.get("discount").toString());
				}
			}else{
				nowPrice=totalFee;
			}
			
//			//处理全场优惠券
//			String json=this.getPrivilege();
//			List<Map<String,Object>> privilege=mapper.readValue(json,new TypeReference<List<Map<String,Object>>>(){});
//			for(Map<String,Object>map : privilege){
//				orgPrice=(Double)map.get("orgPrice");
//				desPrice=(Double)map.get("desPrice"); 
//				if(totalFee>=orgPrice){
//					totalFee=totalFee-desPrice;
//					break;
//				}
//			}
			
			//处理优惠券
			double couponPrice=0.00;
			if(couponIds!=null){
				couponPrice= this.privilege(deviceNo, app, couponIds, totalFee);
				if(couponPrice!=0.00)
				totalFee-=couponPrice;
			}
			try{
				//检验地址信息
				String addressJson=iAdressService.getAdress(deviceNo, app);
				List<Map<String,Object>>addressList=mapper.readValue(addressJson,new TypeReference<List<Map<String,Object>>>(){});
				Map<String,Object>addressMap=new HashMap<>();
				for(Map<String,Object>map : addressList){
					Integer id=(Integer)map.get("id");
					if(id.equals(Integer.parseInt(addressId))){
						addressMap=map;
						break;
					}
				}
				
				flag=true;
				//绑定订单需要的信息
				if(flag){
					double totalFee_=this.indentDao.activeOrder(orderNo,addressId, num, payId, nowPrice+transportfee-couponPrice,couponPrice,couponIds, orgPrice, desPrice, deviceNo, app, addressMap);
					//微信
					if("2".equals(payId)){
						//请求微信生成prepayId
						Map<String,Object>paraMap=WxPayUtil.unifiedorder(totalFee_,orderNo,getWxPay(app,"key"),getWxPay(app,"appid"),getWxPay(app, "appIDS"),getWxPay(app, "mchId"));
						String prepayId=(String)paraMap.get("prepayId");
						String payNo=(String)paraMap.get("payNo");
						if(StringUtils.isBlank(prepayId)){
							return null;
						}
						//将prepayid payNo绑定到订单 到支付信息表
						int result=this.indentDao.boundOrderPrepay(orderNo, prepayId, payNo);
						if(result>0){
							resultMap.put("prepayId",prepayId);
						}
					}
					if(StringUtils.isNoneBlank(orderNo)){
						//更新订单数
						this.indentDao.updateShopOrderNum(deviceNo, app,"order", 1);
					}
					resultMap.put("orderNo",orderNo);
					resultMap.put("totalFee",df.format(totalFee_));
					resultMap.put("creatTime",DateUtil.date2String(new Date(),DateUtil.FORMAT_DATETIME));
				}else{
					return null;
				}
			}catch(Exception e){
				e.printStackTrace();
				return null;
			}
			return mapper.writeValueAsString(resultMap);
		}else{
			return null;
		}
	}
	
	@Override
	public String activeOrderForId(String userId,String couponIds,String orderNo,String addressId,String num,String payId,String app,String message)throws Exception{
		//验证订单数量是否满足
		boolean flag=true;
		Map<String,Object>orderGoodsNumAndPay=this.indentDao.confirmOrderNumAndPayForId(orderNo,userId);
		double totalFee=0;
		String infoIdss=(String)orderGoodsNumAndPay.get("infoIds");
//		String nums=(String)orderGoodsNumAndPay.get("num");
		String fileType=(String)orderGoodsNumAndPay.get("fileType");
		String prices=(String)orderGoodsNumAndPay.get("price");
		String userIds=(String)orderGoodsNumAndPay.get("userIds");
		String workId=(String)orderGoodsNumAndPay.get("userwork");
		String img_url=(String)orderGoodsNumAndPay.get("img_url");
		//活动的分类type
		String activities=orderGoodsNumAndPay.get("activities")!=null?orderGoodsNumAndPay.get("activities").toString():"";
		String type="";
		//活动数组
//		String types=activities;
		//查询所有活动
//		List<Map<String,Object>>getAcitvityList=this.indentDao.getAcitvityList(activities);
		//商品 数量和并然后拿去检测 每种数量是否充足
		Map<String,Object>activityMap=new HashMap<>();
		Map<String,Object>companyMap=new HashMap<>();

			
			//查询活动：按商家来查询活动活动分开，不重复查询活动
			String act= companyMap.get(userIds)==null?"":companyMap.get(userIds).toString();
			if(act.equals("")){
				//查询活动的详情
				Map<String,Object>typeInfo=indentDao.getTypeMap(activities);
				if(typeInfo!=null)
				type=typeInfo.get("type")==null?"":typeInfo.get("type").toString();
				if(typeInfo!=null){
					typeInfo.put("companyId",userIds);
					//以商家id分类
					companyMap.put(userIds,typeInfo);
				}
			}
		
		//验证商品库存
		flag=this.indentDao.checkOrderNum(Integer.parseInt(num),infoIdss+"_"+fileType);

		//商品数量充足
		if(flag){
			Map<String,Object>resultMap=new HashMap<>();
			Map<String,Object> goods=this.indentDao.queryForGoods(infoIdss, fileType);
			int ispostage=(Integer)goods.get("ispostage");
			//运费，需要面运满足的条件，只要一个商家下免，其他都免
			double transportfee=0.00,enough_money=0.00;
			
			//查询订单总价 单价*数量+运费   price transportfee
			Map<String,Object> orderInfo=this.indentDao.queryOrderInfoForId(orderNo,userId);
			if(orderInfo==null){
				return null;
			}
			double price=Double.valueOf((String)orderInfo.get("price"));
			//先算商品的原价格
			totalFee=price*Integer.parseInt(num);
			
			//照片书图片价格计算
			if(img_url.contains("_")){
				if(fileType.equals("xxx")){
					String imgs[]=img_url.split("_");
					double photoPrice=price*Integer.parseInt(imgs[1]);
					totalFee=photoPrice;
				}
			}
			
			//判断是否偏远地区,只有一个优启作为商家
			Map<String,Object>userMap=indentDao.queryForMerchant(userIds);
			if(iAdressDao.isRemote(addressId)){
				transportfee=(Double)userMap.get("remotefee");
			}
			//不包邮就查运费
			if(ispostage==2){
				enough_money=(Double)userMap.get("enough_money");
				if(totalFee<enough_money){
					transportfee+=(Double)userMap.get("transportfee");
				}
			}
			
			//判断是否是0元购商品
			if(StringUtils.isNotBlank(workId) && !"0".equals(workId)){
				if(iUserWorksDao.checkIsZeroGood(workId)){
					Double money=0.00;
					Map<String,Object>userWork=iUserWorksDao.queryUserWorkDetailInfo(workId);
					money=(Double)userWork.get("money");
					//0元购商品价格,作为当前这个商品价格
					prices=String.valueOf(money);
					//总价用0元购价格
					if(img_url.contains("_") && fileType.equals("xxx")){
						String imgs[]=img_url.split("_");
						totalFee=money*Integer.parseInt(imgs[1]);
					}else
						totalFee=money*Integer.parseInt(num);
				}
			}
			//计算活动
//			String type=this.indentDao.getType(infoIdss[i],fileType[i]);
			//数量和价格
			//封装对应活动和商家的价格和数量，为查询活动准备
			activityMap.put("Num"+type+"_"+userIds,num);
			if(img_url.contains("_") && fileType.equals("xxx")){
				String imgs[]=img_url.split("_");
				activityMap.put("Price"+type+"_"+userIds, Double.valueOf(prices)*Integer.parseInt(imgs[1]));
			}else{
				activityMap.put("Price"+type+"_"+userIds, prices);
			}
				
			
			double orgPrice=0.00,desPrice=0.00;
			//处理活动
			List<Map<String,Object>> resultList=CommonUtil.getActivityMethod(activityMap,companyMap);
			double nowPrice=0.00;
			if(resultList!=null){
				for(Map<String,Object>m : resultList){
					//最终价格
					nowPrice+=Double.valueOf(m.get("nowPrice").toString());
					//优惠的价格
					desPrice+=Double.valueOf(m.get("discount").toString());
				}
			}else{
				nowPrice=totalFee;
			}
//			//处理全场优惠券
//			String json=this.getPrivilege();
//			List<Map<String,Object>> privilege=mapper.readValue(json,new TypeReference<List<Map<String,Object>>>(){});
//			for(Map<String,Object>map : privilege){
//				orgPrice=(Double)map.get("orgPrice");
//				desPrice=(Double)map.get("desPrice"); 
//				if(totalFee>=orgPrice){
//					totalFee=totalFee-desPrice;
//					break;
//				}
//			}
			
			//处理优惠券
			double couponPrice=0.00;
			if(couponIds!=null){
				couponPrice= this.privilegeForId(userId, couponIds, totalFee);
				if(couponPrice!=0.00)
				totalFee-=couponPrice;
			}
			try{
				//检验地址信息
				String addressJson=iAdressService.getAdressForId(userId);
				List<Map<String,Object>>addressList=mapper.readValue(addressJson,new TypeReference<List<Map<String,Object>>>(){});
				Map<String,Object>addressMap=new HashMap<>();
				for(Map<String,Object>map : addressList){
					Integer id=(Integer)map.get("id");
					if(id.equals(Integer.parseInt(addressId))){
						addressMap=map;
						break;
					}
				}
				
				flag=true;
				//绑定订单需要的信息
				if(flag){
					double totalFee_=this.indentDao.activeOrderForId(orderNo,addressId, num, payId, nowPrice-couponPrice+transportfee,couponPrice,couponIds, orgPrice, desPrice,userId, addressMap,message,transportfee);
					//微信
					if("2".equals(payId)){
						//请求微信生成prepayId
						Map<String,Object>paraMap=WxPayUtil.unifiedorder(totalFee_,orderNo,getWxPay(app,"key"),getWxPay(app,"appid"),getWxPay(app, "appIDS"),getWxPay(app, "mchId"));
						String prepayId=(String)paraMap.get("prepayId");
						String payNo=(String)paraMap.get("payNo");
						if(StringUtils.isBlank(prepayId)){
							return null;
						}
						//将prepayid payNo绑定到订单 到支付信息表
						int result=this.indentDao.boundOrderPrepay(orderNo, prepayId, payNo);
						if(result>0){
							resultMap.put("prepayId",prepayId);
						}
					}else{
						if(totalFee_==0){
							totalFee_=0.01;
						}
					}
					if(StringUtils.isNoneBlank(orderNo)){
						//更新订单数
						this.indentDao.updateShopOrderNumForId(userId,"order", 1);
					}
					resultMap.put("orderNo",orderNo);
					resultMap.put("totalFee",df.format(totalFee_));
					resultMap.put("creatTime",DateUtil.date2String(new Date(),DateUtil.FORMAT_DATETIME));
				}else{
					return null;
				}
			}catch(Exception e){
				e.printStackTrace();
				return null;
			}
			return mapper.writeValueAsString(resultMap);
		}else{
			return null;
		}
	}
	
	@Override
	public String getOrderInfo(String orderNo,String deviceNo,String app)throws Exception{
		Map<String, Object> result=new HashMap<>();
		Map<String, Object> map=this.indentDao.queryOrderInfo2(orderNo, deviceNo, app);
		if(map!=null){
			double original=0.00;
			String transportfee=(String)map.get("transportfee");
			String province=(String)map.get("province");
			String area=(String)map.get("area");
			String mobile=(String)map.get("mobile");
			String payType=(String)map.get("payType");
			String code=(String)map.get("code");
			String consignee=(String)map.get("consignee");
			Integer status=(Integer)map.get("status");
			String expressNo=(String)map.get("expressNo");
			double feeTotal=(Double)map.get("feeTotal");
			double desPrivilege=Double.valueOf(map.get("desPrivilege").toString());
			String creatTime=(String)map.get("creatTime");
			String companyId=(String)map.get("companyId");
			String componyName=(String)map.get("componyName");
			String[]sortName=((String)map.get("sortName")).split(",");
			String[] prices=((String)map.get("price")).split(",");
			String[] imgUrls=((String)map.get("imgUrl")).split(",");
			String[] fileTypes=((String)map.get("fileType")).split(",");
			String[] nums=((String)map.get("num")).split(",");
			String[] names=((String)map.get("name")).split(",");
			String[] textureNamess=((String)map.get("textureNames")).split("\\|");
			String[] infoIds=((String)map.get("infoIds")).split(",");
			result.put("transportfee", transportfee==null?"":transportfee);
			result.put("province", province==null?"":province);
			result.put("area", area==null?"":area);
			result.put("mobile", mobile==null?"":mobile);
			result.put("payType", payType==null?"":payType);
			result.put("consignee", consignee==null?"":consignee);
			result.put("feeTotal", feeTotal);
			result.put("creatTime", creatTime==null?"":creatTime);
			result.put("orderNo", orderNo);
			result.put("status", status);
			result.put("code", code==null?"":code);
			result.put("expressNo", expressNo==null?"":expressNo.trim());
			result.put("desPrivilege",df.format(desPrivilege));
			result.put("companyId", companyId);
			result.put("componyName", componyName);
			if(status==1){
				expressMsg="待付款";
			}else if(status==2 || status==8){
				expressMsg="待发货";
			}else if(status==3){
				expressMsg="卖家已发货";
			}else if(status==4){
				expressMsg="交易成功";
			}else if(status==5){
				expressMsg="交易关闭";
			}else if(status==6){
				expressMsg="交易关闭";
			}else if(status==7){
				expressMsg="交易成功";
			}
			result.put("expressMsg", expressMsg);
			List<Map<String, Object>> goodinfos=new ArrayList<>();
			Map<String, Object> tempMap=null;
			for (int i = 0; i < prices.length; i++) {
				//材质名称
				tempMap.put("textureName", textureNamess[i]);
				tempMap=new HashMap<>();
				tempMap.put("nowPrice", prices[i]);
				//处理照片书的图片显示问题
				if(imgUrls[i].contains("_")){
					String imgs[]=imgUrls[i].split("_");
					tempMap.put("imgUrl", diyRootPath+imgs[0]+"_1.jpg");
					//处理照片书张数问题
					tempMap.put("num",imgs[1]);
					if(fileTypes[i].equals("xxx")){
						tempMap.put("nowPrice", Double.valueOf(prices[i])*Integer.parseInt(imgs[1]));
					}
					tempMap.put("textureName", textureNamess[i]+"(1份)X"+imgs[1]);
				}else{
					tempMap.put("imgUrl", diyRootPath+imgUrls[i]+"@b"+fileTypes[i]);
					tempMap.put("num", nums[i]);
				}
				
				tempMap.put("fileType", fileTypes[i]==null?"":fileTypes[i]);
				tempMap.put("name", names[i]);
				tempMap.put("goodsId", infoIds[i]);
				tempMap.put("sortName",sortName[i]);
				goodinfos.add(tempMap);
				original+=Integer.parseInt(nums[i])*Double.valueOf(prices[i]);
			}
			result.put("originalPrice",original);
			result.put("goodinfos", goodinfos);
			if(result!=null && result.size()>0){
				return mapper.writeValueAsString(result);
			}
		}
		return null;
	}
	
	@Override
	public String getOrderInfoForId(String orderNo,String userId)throws Exception{
		Map<String, Object> result=new HashMap<>();
		Map<String, Object> map=this.indentDao.queryOrderInfo2ForId(orderNo,userId);
		if(map!=null){
			double original=0.00;
			String transportfee=(String)map.get("transportfee");
			String province=(String)map.get("province");
			String area=(String)map.get("area");
			String mobile=(String)map.get("mobile");
			String payType=(String)map.get("payType");
			String code=(String)map.get("code");
			String consignee=(String)map.get("consignee");
			Integer status=(Integer)map.get("status");
			String expressNo=(String)map.get("expressNo");
			double feeTotal=(Double)map.get("feeTotal");
			double desPrivilege=Double.valueOf(map.get("desPrivilege").toString());
			double coupon=Double.valueOf(map.get("coupon").toString());
			String creatTime=(String)map.get("creatTime");
			String companyId=(String)map.get("companyId");
			String message=(String)map.get("message");
			
			String[]shopNo=(map.get("shopNo")==null?"":map.get("shopNo").toString()).split(",");
			String[]sortName=(map.get("sortName")==null?"":map.get("sortName").toString()).split(",");
			String[] prices=((String)map.get("price")==null?"":map.get("price").toString()).split(",");
			String[] imgUrls=((String)map.get("imgUrl")==null?"":map.get("imgUrl").toString()).split(",");
			String[] fileTypes=((String)map.get("fileType")==null?"":map.get("fileType").toString()).split(",");
			String[] nums=((String)map.get("num")==null?"":map.get("num").toString()).split(",");
			String[] names=((String)map.get("name")==null?"":map.get("name").toString()).split(",");
			String[] textureNamess=((String)map.get("textureNames")==null?"":map.get("textureNames").toString()).split("\\|");
			String[] infoIds=((String)map.get("infoIds")==null?"":map.get("infoIds").toString()).split(",");
			String[] textureIds=((String)map.get("textureIds")==null?"":map.get("textureIds").toString()).split("\\|");
			String[]uid=companyId.split(",");
			result.put("message", message==null?"":message);
			result.put("transportfee", transportfee==null?"":transportfee);
			result.put("province", province==null?"":province);
			result.put("area", area==null?"":area);
			result.put("mobile", mobile==null?"":mobile);
			result.put("payType", payType==null?"":payType);
			result.put("consignee", consignee==null?"":consignee);
			result.put("feeTotal", feeTotal);
			result.put("creatTime", creatTime==null?"":creatTime);
			//计算7天内需要付款的剩余时间
			DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date=dateFormat.parse(creatTime);
			
			String remain = "11分";
			if (status == 1) {//不是待付款的 remian 都为“11分”，袁琪临时添加
				remain=DateUtil.dateDiff2StringForOrderInfo(new Date(date.getTime()+1000*60*60*24*7),null);
			}
			result.put("remain",remain);
			result.put("orderNo", orderNo);
			result.put("status", status);
			result.put("code", code==null?"":code);
			result.put("expressNo", expressNo==null?"":expressNo.trim());
			result.put("desPrivilege",df.format(desPrivilege+coupon));
			result.put("companyId", companyId);
			
			//判读是否多个商家
			boolean flag=false;
			String temp="";
			for(int i=0;i<uid.length;i++){
				if(!temp.equals("") && !temp.equals(uid[i])){
					flag=true;
				}
				temp=uid[i];
			}

			//多个商家显示唯优品
			if(flag)
				result.put("componyName", "唯乐购");
			else
				result.put("componyName", indentDao.getComponyName(companyId));
			
			if(status==1){
				expressMsg="待付款";
			}else if(status==2 || status==8){
				expressMsg="待发货";
			}else if(status==3){
				expressMsg="卖家已发货";
			}else if(status==4){
				expressMsg="交易成功";
			}else if(status==5){
				expressMsg="交易关闭";
			}else if(status==6){
				expressMsg="交易关闭";
			}else if(status==7){
				expressMsg="交易成功";
			}
			result.put("expressMsg", expressMsg);
			List<Map<String, Object>> goodinfos=new ArrayList<>();
			Map<String, Object> tempMap=null;
			for (int i = 0; i < prices.length; i++) {
				tempMap=new HashMap<>();
				tempMap.put("nowPrice", prices[i]);
				//处理照片书的图片显示问题
				if(imgUrls[i].contains("_")){
					String imgs[]=imgUrls[i].split("_");
					tempMap.put("imgUrl", diyRootPath+imgs[0]+"_1.jpg");
					//处理照片书材质显示的问题
					textureNamess[i]=textureNamess[i]+"(1份)X"+imgs[1];
					if(fileTypes[i].equals("xxx")){
						double photoPrice=Double.valueOf(prices[i])*Integer.parseInt(imgs[1]);
						tempMap.put("nowPrice", photoPrice);
						prices[i]=String.valueOf(photoPrice);
					}
				}else{
					if(fileTypes[i].equals("xxx"))
					tempMap.put("imgUrl", diyRootPath+imgUrls[i]);
					else
					tempMap.put("imgUrl", diyRootPath+imgUrls[i]+"@b"+fileTypes[i]);
					
				}
				
				//获取isUnicom
				List<Map<String, Object>> list=this.iShopCartDao.getShopInfoByShopNosByAllStatus("('"+shopNo[i]+"')");
				String isUnicom="0";
				if(!list.isEmpty())
				 isUnicom=list.get(0).get("isUnicom").toString();
				
				//联通送礼品的变0元
				if(isUnicom.equals("1")){
					tempMap.put("nowPrice", 0);
				}
				
				tempMap.put("fileType", fileTypes[i]==null?"":fileTypes[i]);
				tempMap.put("name", (names.length-1)>=i?names[i]:"");
				tempMap.put("textureName",(textureNamess.length-1)>=i?textureNamess[i]:"");
				tempMap.put("goodsId", (infoIds.length-1)>=i?infoIds[i]:"");
				tempMap.put("sortName",(sortName.length-1)>=i?sortName[i]:"");
				tempMap.put("num",(nums.length-1)>=i?nums[i]:"");
				Map<String,Object>textureInfo=this.indentDao.queryInfoTexture(infoIds[i], textureIds[i], fileTypes[i]);
				if(textureInfo!=null){
					tempMap.put("coverImg",textureInfo.get("diy25_img"));
					tempMap.put("origin", textureInfo.get("origin"));
					tempMap.put("wh_size", textureInfo.get("wh_size"));
					tempMap.put("cover_size", textureInfo.get("cover_size"));
					tempMap.put("pre_url", textureInfo.get("pre_url"));
				}
				goodinfos.add(tempMap);
				original+=Integer.parseInt(nums[i])*Double.valueOf(prices[i]);
			}
			result.put("originalPrice",original);
			result.put("goodinfos", goodinfos);
			if(result!=null && result.size()>0){
				return mapper.writeValueAsString(result);
			}
		}
		return null;
	}

	@Override
	public String getOrderList(String deviceNo, String app,String page,String state) throws Exception{
		List<Map<String, Object>> result=new ArrayList<>();
		Map<String, Object> tempMap=null;
		if(StringUtils.isBlank(page)){
			page="0";
		}
		
		List<Map<String,Object>> list=this.indentDao.getOrderList(deviceNo,app,Integer.parseInt(page),Integer.parseInt(orderNumer),Integer.parseInt(state));
		if(list!=null){
			for (Map<String, Object> map : list) {
				tempMap=new HashMap<>();
				Integer status=(Integer)map.get("status");
				String code=(String)map.get("code");
				String orderNo=(String)map.get("orderNo");
				String payType=(String)map.get("payType");
				double feeTotal=(Double)map.get("feeTotal");
				String expressNo=(String)map.get("expressNo");
				String transportfee=(String)map.get("transportfee");
				String[] sortName=((String)map.get("sortName")).split(",");
				String[] prices=((String)map.get("price")).split(",");
				String[] names=((String)map.get("name")).split(",");
				String imgUrl=map.get("imgUrl")==null?"":map.get("imgUrl").toString();
				String[] imgUrls=imgUrl.split(",");
				String[] fileTypes=((String)map.get("fileType")).split(",");
				String[] nums=((String)map.get("num")).split(",");
				String[] textureNamess=((String)map.get("textureNames")).split("\\|");
				String[] infoIds=((String)map.get("infoIds")).split(",");
				String[] companyIds=((String)map.get("companyId")).split(",");
				List<Map<String, Object>> goodinfos=new ArrayList<>();
				Map<String, Object> tempMap2=null;
				for (int i = 0; i < prices.length; i++) {
					tempMap2=new HashMap<>();
					tempMap2.put("nowPrice", prices[i]);
					if(!imgUrl.equals("")){
						if(imgUrls[i].contains("_")){
							String imgs[]=imgUrls[i].split("_");
							tempMap2.put("imgUrl", diyRootPath+imgs[0]+"_1.jpg");
							tempMap2.put("num", imgs[1]);
						}else{
							tempMap2.put("imgUrl", diyRootPath+imgUrls[i]);
							tempMap2.put("num", nums[i]);
						}
					}else{
						tempMap2.put("imgUrl", null);
						tempMap2.put("num", nums[i]);
					}
					tempMap2.put("fileType", fileTypes[i]==null?"":fileTypes[i]);
					tempMap2.put("name", names[i]);
					tempMap2.put("textureName", textureNamess[i]);
					tempMap2.put("goodsId", infoIds[i]);
					tempMap2.put("sortName", sortName[i]);
					goodinfos.add(tempMap2);
				}
				String merchant=this.indentDao.queryOrderCompanyName(companyIds[0]);
				tempMap.put("companyName",merchant);
				tempMap.put("companyId",companyIds[0]);
				tempMap.put("goodinfo", goodinfos);
				tempMap.put("status", status);
				tempMap.put("code", code==null?"":code);
				tempMap.put("orderNo", orderNo);
				tempMap.put("payType", payType);
				tempMap.put("feeTotal", feeTotal);
				tempMap.put("expressNo", expressNo==null?"":expressNo.trim());
				tempMap.put("transportfee",transportfee);
				if(status==1){
					expressMsg="待付款";
				}else if(status==2 || status==8){
					expressMsg="待发货";
				}else if(status==3){
					expressMsg="卖家已发货";
				}else if(status==4){
					expressMsg="交易成功";
				}else if(status==5){
					expressMsg="交易关闭";
				}else if(status==6){
					expressMsg="交易关闭";
				}else if(status==7){
					expressMsg="交易成功";
				}
				tempMap.put("expressMsg", expressMsg);
				result.add(tempMap);
			}
		}
		if(result!=null && result.size()>0){
			return mapper.writeValueAsString(result);
		}
		return null;
	}
	
	@Override
	public String getOrderListForId(String userId,String page,String state) throws Exception{
		List<Map<String, Object>> result=new ArrayList<>();
		Map<String, Object> tempMap=null;
		if(StringUtils.isBlank(page)){
			page="0";
		}
		
		//删除掉过时的订单,7天后的
		indentDao.toDeleteOrderOutTime(userId);
		
		List<Map<String,Object>> list=this.indentDao.getOrderListForId(userId,Integer.parseInt(page),Integer.parseInt(orderNumer),Integer.parseInt(state));
		if(list!=null){
			for (Map<String, Object> map : list) {
				tempMap=new HashMap<>();
				Integer status=(Integer)map.get("status");
				String code=(String)map.get("code");
				String orderNo=(String)map.get("orderNo");
				String payType=(String)map.get("payType");
				double feeTotal=(Double)map.get("feeTotal");
				String expressNo=(String)map.get("expressNo");
				String transportfee=(String)map.get("transportfee");
				String[] shopNo=(map.get("shopNo")==null?"":map.get("shopNo").toString()).split(",");
				String[] sortName=(map.get("sortName")==null?"":map.get("sortName").toString()).split(",");
				String[] prices=(map.get("price")==null?"":map.get("price").toString()).split(",");
				String[] names=(map.get("name")==null?"":map.get("name").toString()).split(",");
				String imgUrl=map.get("imgUrl")==null?"":map.get("imgUrl").toString();
				String[] imgUrls=imgUrl.split(",");
				String[] fileTypes=(map.get("fileType")==null?"":map.get("fileType").toString()).split(",");
				String[] nums=(map.get("num")==null?"":map.get("num").toString()).split(",");
				String[] textureNamess=(map.get("textureNames")==null?"":map.get("textureNames").toString()).split("\\|");
				String[] infoIds=(map.get("infoIds")==null?"":map.get("infoIds").toString()).split(",");
				String[] companyIds=(map.get("companyId")==null?"":map.get("companyId").toString()).split(",");
				String[] textureIds=(map.get("texture_ids")==null?"":map.get("texture_ids").toString()).split("\\|");
				List<Map<String, Object>> goodinfos=new ArrayList<>();
				Map<String, Object> tempMap2=null;
				for (int i = 0; i < prices.length; i++) {
					tempMap2=new HashMap<>();
					tempMap2.put("nowPrice", prices[i]);
					if(!imgUrl.equals("")){
						if(imgUrls[i].contains("_")){
							String imgs[]=imgUrls[i].split("_");
							tempMap2.put("imgUrl", diyRootPath+imgs[0]+"_1@cut.jpg");
							//处理照片书材质显示的问题
							textureNamess[i]=textureNamess[i]+"(1份)X"+imgs[1];
							if(fileTypes[i].equals("xxx")){
								tempMap2.put("nowPrice", Double.valueOf(prices[i])*Integer.parseInt(imgs[1]));
							}
						}else{
							if(fileTypes[i].equals("xxx"))
							tempMap2.put("imgUrl", diyRootPath+imgUrls[i]);
							else
							tempMap2.put("imgUrl", diyRootPath+imgUrls[i]+"@b"+fileTypes[i]);
						}
					}else{
						tempMap2.put("imgUrl", null);
					}
					
					//获取isUnicom
					List<Map<String, Object>> shopList=this.iShopCartDao.getShopInfoByShopNosByAllStatus("('"+shopNo[i]+"')");
					String isUnicom="0";
					if(!shopList.isEmpty())
					 isUnicom=shopList.get(0).get("isUnicom").toString();
					
					//联通送礼品的变0元
					if(isUnicom.equals("1")){
						tempMap2.put("nowPrice", 0);
					}
					
					tempMap2.put("num",nums[i]);
					tempMap2.put("fileType", fileTypes[i]==null?"":fileTypes[i]);
					tempMap2.put("name", (names.length-1)>=i?names[i]:"");
					tempMap2.put("textureName", (textureNamess.length-1)>=i?textureNamess[i]:"");
					tempMap2.put("goodsId",(infoIds.length-1)>=i?infoIds[i]:"");
					tempMap2.put("sortName", (sortName.length-1)>=i?sortName[i]:"");
					Map<String,Object>textureInfo=this.indentDao.queryInfoTexture(infoIds[i], textureIds[i], fileTypes[i]);
					if(textureInfo!=null){
						tempMap2.put("coverImg",textureInfo.get("diy25_img"));
						tempMap2.put("origin", textureInfo.get("origin"));
						tempMap2.put("wh_size", textureInfo.get("wh_size"));
						tempMap2.put("cover_size", textureInfo.get("cover_size"));
						tempMap2.put("pre_url", textureInfo.get("pre_url"));
					}
					goodinfos.add(tempMap2);
				}
				String merchant=this.indentDao.queryOrderCompanyName(companyIds[0]);
				tempMap.put("companyName",merchant);
				tempMap.put("companyId",companyIds[0]);
				tempMap.put("goodinfo", goodinfos);
				tempMap.put("status", status);
				tempMap.put("code", code==null?"":code);
				tempMap.put("orderNo", orderNo);
				tempMap.put("payType", payType);
				tempMap.put("feeTotal", feeTotal);
				tempMap.put("expressNo", expressNo==null?"":expressNo.trim());
				tempMap.put("transportfee",transportfee);
				if(status==1){
					expressMsg="待付款";
				}else if(status==2 || status==8){
					expressMsg="待发货";
				}else if(status==3){
					expressMsg="卖家已发货";
				}else if(status==4){
					expressMsg="交易成功";
				}else if(status==5){
					expressMsg="交易关闭";
				}else if(status==6){
					expressMsg="交易关闭";
				}else if(status==7){
					expressMsg="交易成功";
				}
				tempMap.put("expressMsg", expressMsg);
				result.add(tempMap);
			}
		}
		if(result!=null && result.size()>0){
			return mapper.writeValueAsString(result);
		}
		return null;
	}

	@Override
	public boolean checkOrder(String orderNo, String deviceNo, String app) {
		return this.indentDao.checkOrder(orderNo,deviceNo,app);
	}
	
	@Override
	public boolean checkOrderForId(String orderNo, String userId) {
		return this.indentDao.checkOrderForId(orderNo,userId);
	}

	/**
	 * tengh 2016年8月22日 下午3:13:07
	 * @param orderNo
	 * @param deviceNo
	 * @param app
	 * @return
	 * TODO 立即支付
	 * @throws Exception 
	 */
	@Override
	public String confirmOrder(String orderNo, String deviceNo, String app) throws Exception {
		//验证订单的商品是否都充足
		Map<String, Object> orderGoodNumAndPay=this.indentDao.confirmOrderNumAndPay(orderNo,deviceNo,app);
		String payType=(String)orderGoodNumAndPay.get("payType");
		String prepayId=(String)orderGoodNumAndPay.get("prepayId");
		double feeTotal=(Double)orderGoodNumAndPay.get("feeTotal");
		Long time=(Long)orderGoodNumAndPay.get("time");
		String[] infoIdss=((String)orderGoodNumAndPay.get("infoIds")).split(",");
		String[] nums=((String)orderGoodNumAndPay.get("num")).split(",");
		String []fileType=((String)orderGoodNumAndPay.get("fileType")).split(",");
		//商品 数量合并然后去检测 每种数量是否充足
		Map<String, Integer> infoNum=new HashMap<>();
		//加入文件后缀，用来判断是否精品汇，分表查询商品
				for(int i=0;i<infoIdss.length;i++){
					Integer value=infoNum.get(infoIdss[i]+"_"+fileType[i]);
					if(value==null || value==0){
						infoNum.put(infoIdss[i]+"_"+fileType[i],Integer.parseInt(nums[i]));
					}else{
						infoNum.put(infoIdss[i]+"_"+fileType[i],value+Integer.parseInt(nums[i]));
					}
				}
		boolean flag=true;
		for (String key:infoNum.keySet()) {
			flag=this.indentDao.checkOrderNum(infoNum.get(key),key);
			if(!flag){
				break;
			}
		}
		//如果是微信  验证prepayId是否过期  过期重新生成
		if("2".equals(payType)){
			if(time>118 || time==null){
				Map<String,Object> mapres=WxPayUtil.unifiedorder(feeTotal, orderNo,getWxPay(app, "key"),getWxPay(app, "appid"),getWxPay(app, "appIDS"),getWxPay(app, "mchId"));
				prepayId=(String)mapres.get("prepayId");
				int result=this.indentDao.boundPrepayNo(orderNo,(String)mapres.get("prepayId"),(String)mapres.get("payNo"));
				if(result<=0){
					flag=false;
				}
			}
		}
		if(flag){
			return prepayId;
		}else{
			return "-1";
		}
	}
	
	@Override
	public String confirmOrderForId(String orderNo, String userId,String app) throws Exception {
		//验证订单的商品是否都充足
		Map<String, Object> orderGoodNumAndPay=this.indentDao.confirmOrderNumAndPayForId(orderNo,userId);
		String payType=(String)orderGoodNumAndPay.get("payType");
		String prepayId=(String)orderGoodNumAndPay.get("prepayId");
		double feeTotal=(Double)orderGoodNumAndPay.get("feeTotal");
		Long time=(Long)orderGoodNumAndPay.get("time");
		String[] infoIdss=((String)orderGoodNumAndPay.get("infoIds")).split(",");
		String[] nums=((String)orderGoodNumAndPay.get("num")).split(",");
		String []fileType=((String)orderGoodNumAndPay.get("fileType")).split(",");
		//商品 数量合并然后去检测 每种数量是否充足
		Map<String, Integer> infoNum=new HashMap<>();
		//加入文件后缀，用来判断是否精品汇，分表查询商品
				for(int i=0;i<infoIdss.length;i++){
					Integer value=infoNum.get(infoIdss[i]+"_"+fileType[i]);
					if(value==null || value==0){
						infoNum.put(infoIdss[i]+"_"+fileType[i],Integer.parseInt(nums[i]));
					}else{
						infoNum.put(infoIdss[i]+"_"+fileType[i],value+Integer.parseInt(nums[i]));
					}
				}
		boolean flag=true;
		for (String key:infoNum.keySet()) {
			flag=this.indentDao.checkOrderNum(infoNum.get(key),key);
			if(!flag){
				break;
			}
		}
		//如果是微信  验证prepayId是否过期  过期重新生成
		if("2".equals(payType)){
			if(time>118 || time==null){
				Map<String,Object> mapres=WxPayUtil.unifiedorder(feeTotal, orderNo,getWxPay(app, "key"),getWxPay(app, "appid"),getWxPay(app, "appIDS"),getWxPay(app, "mchId"));
				prepayId=(String)mapres.get("prepayId");
				int result=this.indentDao.boundPrepayNo(orderNo,(String)mapres.get("prepayId"),(String)mapres.get("payNo"));
				if(result<=0){
					flag=false;
				}
			}
		}
		if(flag){
			return prepayId;
		}else{
			return "-1";
		}
	}

	/**
	 * tengh 2016年8月22日 下午7:13:17
	 * @param orderNo
	 * @param deviceNo
	 * @param app
	 * @param flag
	 * @return
	 * TODO 订单状态改变
	 */
	@Override
	public boolean updateOrder(String orderNo, String deviceNo, String app, String flag) {
		if("delete".equalsIgnoreCase(flag) || "confirm".equalsIgnoreCase(flag) || "cancel".equalsIgnoreCase(flag)){
			return this.indentDao.updateOrder(orderNo,deviceNo,app,flag);
		}else if("addShop".equalsIgnoreCase(flag)){
			//订单添加到购物车
			Map<String, Object> map=this.indentDao.queryOrderInfo2(orderNo, deviceNo, app);
			if(map!=null){
				String[] prices=((String)map.get("price")).split(",");
				String[] imgUrls=((String)map.get("imgUrl")).split(",");
				String[] fileTypes=((String)map.get("fileType")).split(",");
				String[] nums=((String)map.get("num")).split(",");
				String[] textureNamess=((String)map.get("textureNames")).split("\\|");
				String[] infoIdss=((String)map.get("infoIds")).split(",");
				String[] userIdss=((String)map.get("companyId")).split(",");
				String[] textureIdss=((String)map.get("textureIds")).split("\\|");
				String temShopNo= (String)map.get("shopNo");
				String[] shopNos=new String[]{};
				String userwork=map.get("userwork")==null?"":map.get("userwork").toString();
				String[] workId=userwork.split(",");
				int index=0;
				if(StringUtils.isNotBlank(temShopNo)){
					shopNos=temShopNo.split(",");
					String shopNo="(";
					for (int i = 0; i < shopNos.length; i++) {
						shopNo+=("'"+shopNos[i]+"',");
					}
					shopNo=shopNo.substring(0,shopNo.length()-1);
					shopNo+=")";
					index=this.iShopCartService.countActiveShop(deviceNo,app,shopNo);
				}else{
					shopNos=new String[prices.length];
					index=prices.length;
				}
				for (int i = 0; i < prices.length; i++) {
					this.iShopCartService.addShopByOrder(prices[i],imgUrls[i],fileTypes[i],nums[i],textureNamess[i],infoIdss[i],userIdss[i],((shopNos[i]==null)?CommonUtil.createOrderNo("G", 3):shopNos[i]),deviceNo,app,textureIdss[i]);
				}
				//更新购物车数量
				this.indentDao.updateShopOrderNum(deviceNo, app, "shop", index);
			}
			return true;
		}else{
			return false;
		}
	}
	
	@Override
	public boolean updateOrderForId(String orderNo, String userId, String flag) {
		if("delete".equalsIgnoreCase(flag) || "confirm".equalsIgnoreCase(flag) || "cancel".equalsIgnoreCase(flag)){
			return this.indentDao.updateOrderForId(orderNo,userId,flag);
		}else if("addShop".equalsIgnoreCase(flag)){
			//订单添加到购物车
			Map<String, Object> map=this.indentDao.queryOrderInfo2ForId(orderNo,userId);
			if(map!=null){
				String[] prices=((String)map.get("price")).split(",");
				String[] imgUrls=((String)map.get("imgUrl")).split(",");
				String[] fileTypes=((String)map.get("fileType")).split(",");
				String[] nums=((String)map.get("num")).split(",");
				String[] textureNamess=((String)map.get("textureNames")).split("\\|");
				String[] infoIdss=((String)map.get("infoIds")).split(",");
				String[] userIdss=((String)map.get("companyId")).split(",");
				String[] textureIdss=((String)map.get("textureIds")).split("\\|");
//				String temShopNo= (String)map.get("shopNo");
//				String[] shopNos=new String[]{};
//				String userwork=map.get("userwork")==null?"":map.get("userwork").toString();
//				String[] workId=userwork.split(",");
				int index=0;
//				if(StringUtils.isNotBlank(temShopNo)){
//					shopNos=temShopNo.split(",");
//					String shopNo="(";
//					for (int i = 0; i < shopNos.length; i++) {
//						shopNo+=("'"+shopNos[i]+"',");
//					}
//					shopNo=shopNo.substring(0,shopNo.length()-1);
//					shopNo+=")";
//					index=this.iShopCartService.countActiveShopForId(userId,shopNo);
//				}else{
//					shopNos=new String[prices.length];
//					index=prices.length;
//				}
				//查询用户购物车
				List<Map<String,Object>>shopList=iShopCartDao.queryShopListForId(userId);

				for (int i = 0; i < prices.length; i++) {
					//相同属性的商品不加入购物车，只修改购物车相同商品的数量
					if(isSameGoodsForId(shopList, userId, textureIdss[i],nums[i],infoIdss[i],fileTypes[i])){
						//不同商品，新加入购物车商品
						this.iShopCartService.addShopByOrderForId(prices[i],imgUrls[i],fileTypes[i],nums[i],textureNamess[i],infoIdss[i],userIdss[i],CommonUtil.createOrderNo("G", 3),userId,textureIdss[i],null);
						index++;
					}
				}
				//更新购物车数量
				this.indentDao.updateShopOrderNumForId(userId, "shop", index);
			}
			return true;
		}else{
			return false;
		}
	}

	@Override
	public boolean recordUser(String deviceNo, String app, String deviceToken) {
		return this.indentDao.recordUser(deviceNo,app,deviceToken);
	}
	
	@Override
	public boolean recordUserForId(String userId, String deviceToken) {
		return this.indentDao.recordUserForId(userId,deviceToken);
	}
	
	@Override
	public Double privilege(String deviceNo,String app,String couponIds,Double totalFee){
		String coupon[]=couponIds.split(",");
		Double result=0.00;
		Double orgPrice=0.00;
		Double desPrice=0.00;
		for(int i=0;i<coupon.length;i++){
			//查询出优惠券
			Map<String,Object>cpMap=indentDao.getUserCoupon(deviceNo,app,coupon[i]);
			//查询不到优惠券不操作
			if(cpMap==null){
				continue;
			}
			orgPrice=Double.valueOf(cpMap.get("orgPrice").toString());
			desPrice=Double.valueOf(cpMap.get("desPrice").toString());
			//查询不到优惠券条件不操作
			if(totalFee<orgPrice){
				continue;
			}
			result+=desPrice;
		}
		return result;
	}
	
	@Override
	public Double privilegeForId(String userId,String couponIds,Double totalFee){
		String coupon[]=couponIds.split(",");
		Double result=0.00;
		Double orgPrice=0.00;
		Double desPrice=0.00;
		for(int i=0;i<coupon.length;i++){
			//查询出优惠券
			Map<String,Object>cpMap=indentDao.getUserCouponForId(userId,coupon[i]);
			//查询不到优惠券不操作
			if(cpMap==null){
				continue;
			}
			orgPrice=Double.valueOf(cpMap.get("orgPrice").toString());
			desPrice=Double.valueOf(cpMap.get("desPrice").toString());
			//查询不到优惠券条件不操作
			if(totalFee<orgPrice){
				continue;
			}
			result+=desPrice;
		}
		return result;
	}

//	public boolean recordUser(String deviceNo, String app, String deviceToken) {
//		return this.indentDao.recordUser(deviceNo,app,deviceNo);
//	}
//	
//	/**
//	 * TODO 处理活动
//	 * @param totalFee
//	 * @return
//	 */
//	public Double activityPrice(String priceNum,String merchant){
//		String pn[]=priceNum.split("_");
//		int num=0;
//		for(int i=0;i<pn.length;i++){
//			String list[]=pn[i].split(",");
//			String price=list[0];
//			//累计数量
//			 num+=Integer.parseInt(list[1]);
//			 
//			 String type="",companyId="",money="",count="",param1="";
////			 Double result=price*num,orgPrice=0.00,desPrice=0.00;
////			 Double totalPrice=price*num;
//			 //验证活动商家是否合法,不合法返回空
//			 Map<String,Object>map=iActiviteDao.activiterList2(merchant);
//			 //无活动
//			 if(map==null)
//				 return result;
//		
//			 type=map.get("type").toString();
//			 companyId=map.get("company_id").toString();
//			 money=map.get("money").toString();
//			 orgPrice=Double.valueOf(map.get("org_price").toString());
//			 desPrice=Double.valueOf(map.get("des_price").toString());
//			 count=map.get("num").toString();
//			 param1=map.get("param1").toString();
//			 switch(type){
//			 //满减
//			 case "1" : if(totalPrice>orgPrice)result=totalPrice-desPrice;break;
//			 //第二件半价或免费
//			 case "2" : if(param1.equals("half") & num>=2)result=price*0.5*(num/2)+price*(num-num/2);
//						else if(param1.equals("avoid") & num>=2)result=price*(num-num/2);
//			 break;
//			 //打折活动
//			 case "6" : result=Double.valueOf(param1)*price*num;break;
//			 }
//		}
//		return result;
//	}
	
	public boolean isSameGoodsForId(List<Map<String,Object>>shopList,String userId,String textureIds,String num,String infoId,String fileTypes){
		Integer nn=Integer.parseInt(num);
		if(!fileTypes.equals("xxx")){
			return true;
		}
		for(Map<String,Object>map:shopList){
			String texture=map.get("texture_ids")==null?"":map.get("texture_ids").toString();
			String info_id=map.get("info_id")==null?"":map.get("info_id").toString();
			String shopNos=map.get("shop_no")==null?"":map.get("shop_no").toString();
			//判断购物车和加入购物车的商品是否相同
			if(textureIds.equals(texture) && infoId.equals(info_id)){
				int n=Integer.parseInt(map.get("num").toString());
				//更改购物车数量
				int result=this.iShopCartDao.updateShopForId(userId, shopNos, String.valueOf(nn+n));
				if(result<=0){
					throw new RuntimeException();
				}
				return false;
			}
		}
		return true;
	}
	
	@Override
	public boolean changeTexture(String shopNo, String userId, String textureIds) {
		return this.indentDao.changeTexture(shopNo,userId,textureIds);
	}
	
	@Override
	public boolean uploadFile(String keycode, MultipartFile imgFile) {
		try {
			String tmonth=DateUtil.date2String(new Date(), DateUtil.FORMAT_DIRMONTH);
			String path="/data/resource/diymall/photos/"+tmonth+"/"+keycode;
//			String path="E:/filedown/photos/"+tmonth+"/"+keycode;
			if(imgFile!=null && !imgFile.isEmpty()){
				//创建目录
				File orgPath=new File(path);
				if(!orgPath.exists()){
					orgPath.mkdirs();
				}
				File[] files= orgPath.listFiles();
				int fileNum=files.length;
				fileNum=fileNum/2+1;
				String paraPath=path+"/_"+fileNum+".jpg";
				File result=new File(paraPath);
				result.createNewFile();
				//原图
				imgFile.transferTo(result);
				BufferedImage bufferedImage=ImageIO.read(result);
				int width=bufferedImage.getWidth()/2;
				int height=bufferedImage.getHeight()/2;
				//压缩
				CommonUtil.cutImage2(width, height, paraPath, path+"/_"+fileNum+"@cut.jpg", 50.0);
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	 public static void main(String[] args) {  
	        String url = "http://120.26.112.213:8083/file/wp/IntegralMall/a03ffa9c.jpg";  
	        System.out.println(url.substring(url.lastIndexOf(".")));
//	        byte[] btImg = getImageFromNetByUrl(url);  
//	        if(null != btImg && btImg.length > 0){  
//	            System.out.println("读取到：" + btImg.length + " 字节");  
//	            String fileName = "百度.gif";  
//	            writeImageToDisk(btImg, fileName);  
//	        }else{  
//	            System.out.println("没有从该连接获得内容");  
//	        }  
	    }    

}
