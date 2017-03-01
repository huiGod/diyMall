package cn._51app.service.diy2_0.impl;

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

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import cn._51app.dao.diy2_0.IAdressDao;
import cn._51app.dao.diy2_0.IShopCartDao;
import cn._51app.dao.diy2_0.IUserWorksDao;
import cn._51app.dao.diy2_0.IZeroGoodDao;
import cn._51app.dao.diy2_0.IndentDao;
import cn._51app.dao.ocs.OCSDao;
import cn._51app.service.BaseService;
import cn._51app.service.diy2_0.IAdressService;
import cn._51app.service.diy2_0.IShopCartService;
import cn._51app.util.CommonUtil;
import cn._51app.util.DateUtil;
import cn._51app.util.JSONUtil;
import cn._51app.util.OCSKey;
import cn._51app.util.PropertiesUtil;
import cn._51app.util.RedisUtil;
import cn._51app.util.WxPayUtil;

@Service
public class ShopCartService extends BaseService implements IShopCartService {
	
	@Autowired
	private IShopCartDao iShopCartDao;
	@Autowired
	private IndentDao indentDao;
	@Autowired
	private IAdressService iAdressService;
	@Autowired
	private OCSDao ocsDao;
	@Autowired
	private IZeroGoodDao iZeroGoodDao;
	@Autowired
	private IUserWorksDao iUserWorksDao;
	@Autowired
	private IAdressDao iAdressDao;
	private final String shopNumer =PropertiesUtil.getValue("diy.goods.page.size");
	private final String diyRootPath =PropertiesUtil.getValue("diy.root.path");
	private final String updownloadRootDir =PropertiesUtil.getValue("uploadUrl.sys");
	//裁剪图片根路径
	private static final String cutPath=PropertiesUtil.getValue("diy.cut.path");
	private ObjectMapper mapper=new ObjectMapper();
	private DecimalFormat df= new DecimalFormat("######0.00");
	private final String dgurl=PropertiesUtil.getValue("diy.goods.url");
	private SimpleDateFormat dateformat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@Override
	public String shopList(String deviceNo, String app, String page) throws Exception{
		if(StringUtils.isBlank(page)){
			page="0";
		}
		List<Map<String, Object>> result=new ArrayList<>();
		//按商家查询出来
		List<Map<String, Object>> companyList=this.iShopCartDao.getShopByUserIds(deviceNo,app,Integer.parseInt(page),Integer.parseInt(shopNumer));
		Map<String, Object> teMap=null;
		Map<String,Object> activityMap=null;
		for (int i = 0; companyList!=null && i< companyList.size(); i++) {
			teMap=new HashMap<>();
			activityMap=new HashMap<>();
			Integer companyId=(Integer)companyList.get(i).get("companyId");
			String companyName=(String)companyList.get(i).get("companyName");
			//根据商家查询商品
			List<Map<String, Object>> shopList=this.iShopCartDao.getShopInfoByUserId(companyId,deviceNo,app);
			//根据商家id查询用户（商家的商品）购物车的所有活动
			List<Map<String,Object>>activityList=this.iShopCartDao.getShopActivityByUserId(companyId,deviceNo,app);
			//运费合计（算最小的商品）
			double postage=0.00;
			//是否包邮
			boolean isPostage=false;
			//处理商品
			for(Map<String,Object>m : shopList){
				String type=m.get("type")==null?"":m.get("type").toString();
				String imgUrl=m.get("imgUrl")!=null?m.get("imgUrl").toString():"";
				
				String num=activityMap.get("Num"+type)==null?"":activityMap.get("Num"+type).toString();
				String price=activityMap.get("Price"+type)==null?"":activityMap.get("Price"+type).toString();
				//把相同活动类型的商品合并一起
				if(num.equals("") || price.equals("")){
					activityMap.put("Num"+type,m.get("num"));
					activityMap.put("Price"+type, m.get("nowPrice"));
				}else{
					activityMap.put("Num"+type,num+","+m.get("num"));
					activityMap.put("Price"+type,price+","+m.get("nowPrice"));
				}
				
				//处理图片（照片书的需要额外处理）
				if(imgUrl.contains("_")){
					String imgs[]=imgUrl.split("_");
					m.put("imgUrl", diyRootPath+imgs[0]+"_1.jpg");
					//处理照片书商品数量
					m.put("num",imgs[1]);
				}else
				m.put("imgUrl", diyRootPath+imgUrl);
				//条件运费
				double transportfee=m.get("postage")==null?0.00:Double.valueOf(m.get("postage").toString());
				if(transportfee<postage || postage==0){
					postage=transportfee;
				}
				//限定数量
				int freeCount=m.get("freeCount")==null?0:Integer.parseInt(m.get("freeCount").toString());
				int nn=m.get("num")==null?0:Integer.parseInt(m.get("num").toString());
				//计算邮费
				if(imgUrl.contains("_")){
					String photoNum[]=imgUrl.split("_");
					Integer pN=Integer.parseInt(photoNum[1]);
					m.put("feeNum",photoNum[1]);
					m.put("feePostage", freeCount*nn);
					//包邮
					if(pN>=freeCount){
						isPostage=true;
					}
				}else{
					//商品数量合格包邮
					if(freeCount<=nn){
						isPostage=true;
					}
				}
				m.put("feeNum", 1);
				m.put("feePostage",nn);
			}
			//处理活动
//			List<Map<String,Object>> resultList=CommonUtil.getActivityMethod(activityMap,activityList);
			if(shopList.isEmpty()){
				teMap.put("companyId", "");
				teMap.put("companyName", "");
				teMap.put("shopList", "");
				teMap.put("activityList", "");
				result.add(teMap);
			}else{
				teMap.put("companyId", companyId);
				teMap.put("companyName", companyName);
				teMap.put("shopList", shopList);
//				teMap.put("activityList", resultList);
				result.add(teMap);
			}	
		}
		if(result!=null && result.size()>0){
			return mapper.writeValueAsString(result);
		}
		return null;
	}
	
	@Override
	public Map<String,Object> shopListForId(String userId) throws Exception{
		List<Map<String, Object>> result=new ArrayList<>();
		//按商家查询出来
		List<Map<String, Object>> companyList=this.iShopCartDao.getShopByUserIdsForId(userId);
		Map<String, Object> teMap=null;
		Map<String,Object> activityMap=null;
		Map<String,Object>companyMap=null;
		Date date=null;
		Map<String,Object>resultMap=new HashMap<String,Object>();
		
		//检查更新用户购物车总量
		checkShopNum(userId);
		
		for (int i = 0; companyList!=null && i< companyList.size(); i++) {
			teMap=new HashMap<>();
			activityMap=new HashMap<>();
			companyMap=new HashMap<>();
			Integer companyId=(Integer)companyList.get(i).get("companyId");
			//商家优惠券
			List<Map<String, Object>> couponList=this.iShopCartDao.getCouponByGoodId(companyId);
			if(couponList!=null && couponList.size()>0){
				for (int j = 0; j < couponList.size(); j++) {
					Integer temcouponId=(Integer)couponList.get(j).get("id");
					boolean isGetCoupon=this.iShopCartDao.checkIsGetCoupon(temcouponId,userId);
					if(isGetCoupon){
						couponList.get(j).put("isGet", true);
					}else{
						couponList.get(j).put("isGet", false);
					}
				}
			}
			String companyName=(String)companyList.get(i).get("companyName");
			//商家需要满足多少金额可以免运费
			double enoughMoney=(Double)companyList.get(i).get("enoughMoney");
			double transportfee=(Double)companyList.get(i).get("transportfee");
			double remotefee=(Double)companyList.get(i).get("remotefee");
			//根据商家查询商品
			List<Map<String, Object>> shopList=this.iShopCartDao.getShopInfoByUserIdForId(companyId,userId);
			//根据商家id查询用户（商家的商品）购物车的所有活动
//			List<Map<String,Object>>activityList=this.iShopCartDao.getShopActivityByUserIdForId(companyId,userId);
			//处理商品
			for(Map<String,Object>m : shopList){
				String creat_time=m.get("creat_time")==null?"":m.get("creat_time").toString();
				DateFormat simpleDate=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date shopDate=simpleDate.parse(creat_time);
				if(date==null)
				date=shopDate;
				else if(date.getTime()<shopDate.getTime())
				date=shopDate;
				String type=m.get("type")==null?"":m.get("type").toString();
				String actId=m.get("actId")==null?"":m.get("actId").toString();
				String imgUrl=m.get("imgUrl")!=null?m.get("imgUrl").toString():"";
				String fileType=m.get("fileType")==null?"":m.get("fileType").toString();
				String textureName=m.get("textureName")==null?"":m.get("textureName").toString();
				String user_id=m.get("user_id")==null?"":m.get("user_id").toString();
				String coverImg=m.get("coverImg")!=null?m.get("coverImg").toString():"";
				String pre_url=m.get("pre_url")!=null?m.get("pre_url").toString():"";
				String about=m.get("about")!=null?m.get("about").toString():"";
				String sortName=m.get("sortName")!=null?m.get("sortName").toString():"";
				String isUnicom=m.get("isUnicom")!=null?m.get("isUnicom").toString():"";
				long typeId=(Long)m.get("typeId");
				Integer flagId=(Integer)m.get("flagId");
				
				if(!about.equals("") && !about.equals("0")){
					m.put("about",1);
				}else{
					m.put("about",0);
				}
				
				//处理材质预览图和盖图
				m.put("coverImg",diyRootPath+coverImg);
				m.put("pre_url",diyRootPath+pre_url);
				
				//处理图片（照片书的需要额外处理）
				if(imgUrl.contains("_")){
					String imgs[]=imgUrl.split("_");
					m.put("imgUrl", imgs[0]+"_1.jpg");
					//处理照片书的数量
					int count=Integer.parseInt(imgs[1]);
					List<String>imgsList=new ArrayList<String>();
					//所有预览图
					for(int j=0;j<count;j++){
						imgsList.add(diyRootPath+imgs[0]+"_"+(j+1)+"@cut.jpg");
					}
					//冲印才显示
					if(flagId==90 && typeId==2){
						m.put("textureName", textureName+"(1份)X"+count);
					}else{
						m.put("textureName", textureName);
					}
					
					m.put("preImgNum",imgsList);
					double nP=(double)m.get("nowPrice");
					//处理照片书的价格
					if(fileType.equals("xxx")){
						m.put("nowPrice", nP*count);
					}
				}else{
					if(fileType.equals("xxx")){
						List<String>imgsList=new ArrayList<String>();
						imgsList.add(diyRootPath+imgUrl);
						m.put("imgUrl",imgUrl);
						m.put("preImgNum",imgsList);
					}	
					else{ 
						List<String>imgsList=new ArrayList<String>();
						m.put("imgUrl", imgUrl+"@b"+fileType);
						imgsList.add(diyRootPath+imgUrl+"@b"+fileType);
						File file=new File(diyRootPath+imgUrl+"@pb"+fileType);
						if(file.exists()){
							imgsList.add(diyRootPath+imgUrl+"@p"+fileType);
							imgsList.add(diyRootPath+imgUrl+"@pb"+fileType);
						}
						m.put("preImgNum", imgsList);
					}
				}
				
				//联通送礼品的变0元
				if(isUnicom.equals("1")){
					m.put("nowPrice", 0);
				}
				
				//按商家把活动分开
				String act= companyMap.get(user_id)==null?"":companyMap.get(user_id).toString();
				if(act.equals("")){
					//查询活动的详情
					Map<String,Object>typeInfo=indentDao.getTypeMap(actId);
					if(typeInfo==null)
						continue;
					typeInfo.put("companyId",user_id);
					companyMap.put(user_id+"_"+type,typeInfo);
				}
				
				String num=activityMap.get("Num"+type+"_"+user_id)==null?"":activityMap.get("Num"+type+"_"+user_id).toString();
				String price=activityMap.get("Price"+type+"_"+user_id)==null?"":activityMap.get("Price"+type+"_"+user_id).toString();
				if(num.equals("") || price.equals("")){
					activityMap.put("Num"+type+"_"+user_id,m.get("num"));
					activityMap.put("Price"+type+"_"+user_id, m.get("nowPrice"));
				}else{
					activityMap.put("Num"+type+"_"+user_id,num+","+m.get("num"));
					activityMap.put("Price"+type+"_"+user_id,price+","+m.get("nowPrice"));
				}
				
				//台历的材质图的去掉(写死)
				if(sortName.contains("台历")){
					m.put("origin", "");
					m.put("wh_size", "");
					m.put("cover_size", "");
					m.put("coverImg", "");
					m.put("pre_url", "");
				}
			
				
				//处理条件运费
//				double transportfee=m.get("postage")==null?0.00:Double.valueOf(m.get("postage").toString());
//				if(transportfee<postage || postage==0){
//					postage=transportfee;
//				}
//				//限定数量
//				int freeCount=m.get("freeCount")==null?0:Integer.parseInt(m.get("freeCount").toString());
//				int nn=m.get("num")==null?0:Integer.parseInt(m.get("num").toString());
//				//计算邮费
//				if(imgUrl.contains("_")){
//					String photoNum[]=imgUrl.split("_");
//					Integer pN=Integer.parseInt(photoNum[1]);
//					m.put("feeNum",photoNum[1]);
//					m.put("feePostage", freeCount*nn);
//					//包邮
//					if(pN>=freeCount){
//						isPostage=true;
//					}
//				}else{
//					//商品数量合格包邮
//					if(freeCount<=nn){
//						isPostage=true;
//					}
//					m.put("feeNum", 1);
//					m.put("feePostage",nn);
//				}
			}
			
			if(shopList!=null && shopList.size()>0){
				for (int j = 0; j < shopList.size(); j++) {
					Integer userwork=(Integer)shopList.get(j).get("userwork");
					Integer isUnicom=(Integer)shopList.get(j).get("isUnicom");
					Map<String, Object> temUserWorkMap=iZeroGoodDao.getWorkInfo(userwork+"", userId);
					if(temUserWorkMap!=null && userwork!=null && 0!=userwork){
						Integer type=(Integer)temUserWorkMap.get("type");
						if(type==3){
							//判断是不是0元购
							shopList.get(j).put("nowPrice", temUserWorkMap.get("money"));
							shopList.get(j).put("isZero", true);
						}
					}else{
						//联通送礼品的变0元
						if(isUnicom==1){
							shopList.get(j).put("isZero", true);
						}else
						shopList.get(j).put("isZero", false);
					}
					shopList.get(j).remove("userwork");
					shopList.get(j).put("imgUrl", diyRootPath+shopList.get(j).get("imgUrl"));
				}
			}
			
			//处理活动
			List<Map<String,Object>> resultList=CommonUtil.getActivityMethod(activityMap,companyMap);
			if(!shopList.isEmpty()){
				teMap.put("companyId", companyId);
				teMap.put("enoughMoney", enoughMoney);
				teMap.put("transportfee", transportfee);
				teMap.put("companyName", companyName);
				teMap.put("remotefee", remotefee);
				teMap.put("shopList", shopList);
				teMap.put("activityList", resultList);
				teMap.put("couponList", couponList);
			}
			if(teMap!=null && !teMap.isEmpty()){
				result.add(teMap);
			}
		}
		if(result!=null && result.size()>0){
			resultMap.put("result",result);
			resultMap.put("timestamp",date.getTime());
			return resultMap;
//			return mapper.writeValueAsString(result);
		}
		return null;
	}

	@Override
	public boolean addShop(MultipartFile imgFile, MultipartFile imgBackFile, MultipartFile previewFile, MultipartFile previewBackFile,String infoId,
			String textureIds, String deviceNo, String num, String app,HttpServletRequest req,String wallpaper,String isBoutique,String lettering,String modId){
		String orderNo=CommonUtil.createOrderNo("G", 3);
		String resultPath=null;
		String sufFormat=".jpg";
		//是否定制
		boolean customize=false;
		
		try {
			//判断是否精品汇商品
			if(imgFile!=null && wallpaper==null){
				customize=true;
				resultPath=CommonUtil.orderBF("2", imgFile, imgBackFile, previewFile, previewBackFile,orderNo).split("#")[0];
			//照片书
			}else if(CommonUtil.isFile(req) && wallpaper==null){
				if(isBoutique!=null || !isBoutique.equals("")){
					customize=false;
					sufFormat="xxx";
				}else
				customize=true;
				resultPath=CommonUtil.orderBatchImg(req, orderNo).split("#")[0];
			}else if(previewFile!=null && wallpaper!=null){
				String pathName=new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());
				String suffix=wallpaper.substring(wallpaper.lastIndexOf("."));
				File file=new File(updownloadRootDir+cutPath+pathName);
				if(!file.exists()){
					file.mkdirs();
				}
				//原图
				if(previewFile!=null && !previewFile.isEmpty()){
					File imgFilePath=new File(updownloadRootDir+cutPath+pathName+"/"+orderNo+suffix);
					if(!imgFilePath.exists()){
						imgFilePath.createNewFile();
					}
					previewFile.transferTo(imgFilePath);
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
				//相同商品相同材质合并商品
				num=String.valueOf(isSameGoods(deviceNo, app, textureIds, num, infoId));
				sufFormat="xxx";
			}
			
			//刻字的就用精品的材质id
			if(lettering!=null && lettering.equals("")){
				customize=false;
			}
			
			//插入到购物车
			int result=this.iShopCartDao.createShop(resultPath,infoId,textureIds,deviceNo,app,orderNo,sufFormat,num,0,lettering,modId);
			if(result>0){
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	@Override
	public boolean addShopForId(MultipartFile imgFile, MultipartFile imgBackFile, MultipartFile previewFile, MultipartFile previewBackFile,String infoId,
			String textureIds, String userId, String num,HttpServletRequest req,String wallpaper,String isBoutique,String lettering,String modId,String keycode,String isSave){
		String orderNo=CommonUtil.createOrderNo("G", 3);
		String resultPath=null;
		String sufFormat=".jpg";
		//是否定制
		boolean customize=false;
		try {
			if(StringUtils.isBlank(keycode)){
				//判断是否精品汇商品
				if(imgFile!=null && wallpaper==null){
					customize=true;
					resultPath=CommonUtil.orderBF("2", imgFile, imgBackFile, previewFile, previewBackFile,orderNo).split("#")[0];
				//照片书
				}else if(previewFile!=null && wallpaper==null){
					customize=true;
					resultPath=CommonUtil.orderBF("2", imgFile, imgBackFile, previewFile, previewBackFile,orderNo).split("#")[0];
				}else if(CommonUtil.isFile(req) && wallpaper==null){
					if(StringUtils.isNotBlank(isBoutique)){
						customize=false;
						sufFormat="xxx";
						num="1";
					}else
					customize=true;
					resultPath=CommonUtil.orderBatchImg(req, orderNo).split("#")[0];
				}else if(previewFile!=null && wallpaper!=null){
					String pathName=new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());
					String suffix=wallpaper.substring(wallpaper.lastIndexOf("."));
					File file=new File(updownloadRootDir+cutPath+pathName);
					if(!file.exists()){
						file.mkdirs();
					}
					//原图
					if(previewFile!=null && !previewFile.isEmpty()){
						File imgFilePath=new File(updownloadRootDir+cutPath+pathName+"/"+orderNo+suffix);
						if(!imgFilePath.exists()){
							imgFilePath.createNewFile();
						}
						previewFile.transferTo(imgFilePath);
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
					num=String.valueOf(isSameGoodsForId(userId, textureIds, num, infoId));
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
			
			//刻字的就用精品的材质id
			if(lettering!=null && StringUtils.isNotBlank(lettering)){
				customize=false;
			}
			
			//插入到购物车
			int result=this.iShopCartDao.createShopForId(resultPath,infoId,textureIds,userId,orderNo,sufFormat,num,null,lettering,modId);
			if(result>0){
				//定制保存作品
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
						paramMap.put("goodId", infoId);
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
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	@Override
	public boolean deleteShop(String deviceNo, String app, String shopNos) {
		String[] shopNoss=shopNos.split(",");
		String shopNo="(";
		for (int i = 0; i < shopNoss.length; i++) {
			shopNo+=("'"+shopNoss[i]+"',");
		}
		shopNo=shopNo.substring(0,shopNo.length()-1);
		shopNo+=")";
		int result= this.indentDao.deleteShop(deviceNo,app,shopNo);
		if(result>0){
			//更新购物车数量	
			this.indentDao.updateShopOrderNum(deviceNo, app, "shop", -result);
			return true;
		}
		return false;
	}
	
	@Override
	public boolean deleteShopForId(String userId, String shopNos) {
		String[] shopNoss=shopNos.split(",");
		String shopNo="(";
		for (int i = 0; i < shopNoss.length; i++) {
			shopNo+=("'"+shopNoss[i]+"',");
		}
		shopNo=shopNo.substring(0,shopNo.length()-1);
		shopNo+=")";
		int result= this.indentDao.deleteShopForId(userId,shopNo);
		if(result>0){
			//更新购物车数量,-删除数量
			this.indentDao.updateShopOrderNumForId(userId, "shop",-result);
			return true;
		}
		return false;
	}
	
	@Override
	public boolean updateShop(String deviceNo, String app, String shopNos, String nums) {
		int result= this.iShopCartDao.updateShop(deviceNo,app,shopNos,nums);
		if(result>0){
			return true;
		}
		return false;
	}
	
	@Override
	public boolean updateShopForId(String userId, String shopNos, String nums) {
		int result= this.iShopCartDao.updateShopForId(userId,shopNos,nums);
		if(result>0){
			return true;
		}
		return false;
	}
	
	@Override
	public String getOrderShopNum(String deviceNo, String app) throws Exception {
		Map<String, Object> map=this.iShopCartDao.getOrderShopNum(deviceNo,app);
		if(map==null){
			return null;
		}
		return mapper.writeValueAsString(map);
	}
	
	@Override
	public String getOrderShopNumForId(String userId) throws Exception {
		Map<String, Object> map=this.iShopCartDao.getOrderShopNumForId(userId);
		if(map==null){
			return null;
		}
		return mapper.writeValueAsString(map);
	}
	
	@Override
	public String createOrderByShops(String deviceNo, String app, String shopNos,String payId,String addressId,String couponId) throws Exception {
		String orderNo=CommonUtil.createOrderNo("B", 5);
		//查询多个购物车信息
		String[] shopNoss=shopNos.split(",");
		String shopNo="(";
		for (int i = 0; i < shopNoss.length; i++) {
			shopNo+=("'"+shopNoss[i]+"',");
		}
		shopNo=shopNo.substring(0,shopNo.length()-1);
		shopNo+=")";
		List<Map<String, Object>> list=this.iShopCartDao.getShopInfoByShopNos(shopNo);
		//查出即将生成订单的所有活动
		List<Map<String,Object>> list2=this.iShopCartDao.getActivityList(shopNo);
		String shopNo_="",infoId_="",textureIds_="",temTextureName_="",userId_="",imgUrl_="",fileType_="",num_="",name_="",nowPrice_="",transportfee_="",sortName_="",userwork_="";
		double totalFee=0.00,orgPrice=0.00,desPrice=0.00,transport=0.00;
		Map<String,Object>activityMap=new HashMap<>();
		for (Map<String, Object> map : list) {
			userwork_+=map.get("userwork")!=null?map.get("userwork")+",":"0,";
			shopNo_+=(String)map.get("shopNo")+",";
			infoId_+=String.valueOf(map.get("infoId"))+",";
			textureIds_+=(String)map.get("textureIds")+"|";
			temTextureName_+=(String)map.get("textureName")+"|";
			userId_+=String.valueOf(map.get("userId"))+",";
			imgUrl_+=(String)map.get("imgUrl")+",";
			fileType_+=(String)map.get("fileType")+",";
			sortName_+=(String)map.get("sortName")+",";
			String temNum=String.valueOf(map.get("num"));
			num_+=temNum+",";
			name_+=(String)map.get("name")+",";
			String temNowPrice=String.valueOf(map.get("nowPrice"));
			nowPrice_+=temNowPrice+",";
			String temTransportfee=String.valueOf(map.get("transportfee"));
			transportfee_=temTransportfee+",";
			transport+=Double.valueOf(temTransportfee);
			totalFee+=Double.valueOf(temNowPrice)*Integer.parseInt(temNum);
			
			//获取活动数据
			String type=map.get("type")==null?"0":map.get("type").toString();;
			String num=activityMap.get("Num"+type)==null?"":activityMap.get("Num"+type).toString();
			String price=activityMap.get("Price"+type)==null?"":activityMap.get("Price"+type).toString();
			if(num.equals("") || price.equals("")){
				activityMap.put("Num"+type,map.get("num"));
				activityMap.put("Price"+type, map.get("nowPrice"));
			}else{
				activityMap.put("Num"+type,num+","+map.get("num"));
				activityMap.put("Price"+type,price+","+map.get("nowPrice"));
			}
			
		}

		String types="";
		//格式化获取参与的活动type
		for(Map<String,Object>mm : list2){
			types=mm.get("type").toString()+",";
		}
		if(!types.equals("")){
			types=types.substring(0,types.length()-1);
		}
		
		//处理活动
//		List<Map<String,Object>> resultList=CommonUtil.getActivityMethod(activityMap,list2);
		List<Map<String,Object>> resultList=null;
		double nowPrice=0.00;
		if(resultList!=null){
			for(Map<String,Object>m : resultList){
				nowPrice+=Double.valueOf(m.get("nowPrice").toString());
			}
		}else{
			nowPrice=totalFee;
		}
		
		/*********/
		//验证订单商品的数量是够满足
		boolean flag=true;
		String[] infoIdss=CommonUtil.subStr(infoId_).split(",");
		String[] nums=CommonUtil.subStr(num_).split(",");
		String []fileType=CommonUtil.subStr(fileType_).split(",");
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
		
		for (String key:infoNum.keySet()) {
			flag=this.indentDao.checkOrderNum(infoNum.get(key),key);
			if(!flag){
				break;
			}
		}
		/*********/
		if(flag){ //商品数量充足继续处理
			//检验地址信息
			String addressJson=iAdressService.getAdress(deviceNo, app);
			List<Map<String, Object>> addressList=mapper.readValue(addressJson, new TypeReference<List<Map<String,Object>>>() {});
			Map<String, Object> addressMap=new HashMap<>();
			for (Map<String, Object> map : addressList) {
				Integer id=(Integer)map.get("id");
				if(id.equals(Integer.parseInt(addressId))){
					addressMap=map;
					break;
				}
			}
			
			//处理优惠券
			double couponPrice=0.00;
			if(couponId!=null){
				couponPrice= this.privilege(deviceNo, app, couponId, totalFee);
				if(couponPrice!=0.00)
				totalFee-=couponPrice;
			}
			
			//生成订单
			double totalFee_=this.iShopCartDao.createOrderByShops(orderNo,CommonUtil.subStr(shopNo_),CommonUtil.subStr(infoId_),CommonUtil.subStr(textureIds_),CommonUtil.subStr(temTextureName_),CommonUtil.subStr(userId_),CommonUtil.subStr(imgUrl_),CommonUtil.subStr(fileType_),CommonUtil.subStr(num_),CommonUtil.subStr(name_),CommonUtil.subStr(nowPrice_),payId,orgPrice,desPrice,couponId,nowPrice-couponPrice+transport,addressMap,deviceNo,app,CommonUtil.subStr(transportfee_),CommonUtil.subStr(sortName_),couponPrice,types,CommonUtil.subStr(userwork_));
			Map<String, Object> resultMap=new HashMap<>();
			this.indentDao.updateShopOrderNum(deviceNo, app, "order", 1);
			if("2".equals(payId)){//微信
				//请求微信生成prepayId
				Map<String, Object> paraMap= WxPayUtil.unifiedorder(totalFee_, orderNo,getWxPay(app,"key"),getWxPay(app,"appid"),getWxPay(app, "appIDS"),getWxPay(app, "mchId"));
				String prepayId=(String)paraMap.get("prepayId");
				String payNo=(String)paraMap.get("payNo");
				if(StringUtils.isBlank(prepayId)){
					return null;
				}
				//将prepayid payNo绑定到订单  到支付信息表
				int result3=this.indentDao.boundOrderPrepay(orderNo,prepayId,payNo);
				if(result3>0){
					resultMap.put("prepayId", prepayId);
				}
			}
			resultMap.put("orderNo", orderNo);
			resultMap.put("totalFee", df.format(totalFee_));
			resultMap.put("creatTime", DateUtil.date2String(new Date(), DateUtil.FORMAT_DATETIME));
			this.invalidShops(shopNos,deviceNo,app);
			return mapper.writeValueAsString(resultMap);
		}else{
			return null;
		}
	}
	
	@Override
	public String createOrderByShopsForId(String userId, String shopNos,String payId,String addressId,String couponId,String app,String message) throws Exception {
		String orderNo=CommonUtil.createOrderNo("B", 5);
		//查询多个购物车信息
		String[] shopNoss=shopNos.split(",");
		String shopNo="(";
		for (int i = 0; i < shopNoss.length; i++) {
			shopNo+=("'"+shopNoss[i]+"',");
		}
		shopNo=shopNo.substring(0,shopNo.length()-1);
		shopNo+=")";
		List<Map<String, Object>> list=this.iShopCartDao.getShopInfoByShopNos(shopNo);
		//查出即将生成订单的所有活动
		List<Map<String,Object>> list2=this.iShopCartDao.getActivityList(shopNo);
		String shopNo_="",infoId_="",textureIds_="",temTextureName_="",userId_="",imgUrl_="",fileType_="",num_="",name_="",nowPrice_="",transportfee_="",sortName_="",userwork_="",comId="",param1_="",paramType_="",isUnicom_="";
		double totalFee=0.00,orgPrice=0.00,desPrice=0.00,transport=0.00,allFee=0.00,noActiveFee=0.00;
		Map<String,Object>activityMap=new HashMap<>();
		Map<String,Object>companyMap=new HashMap<>();
		//运费map
		Map<String,Object>transportMap=new HashMap<>();
		//是否偏远标识
		boolean isRemote=false;
		
		
		//判断是否偏远地区,只有一个优启作为商家
		if(iAdressDao.isRemote(addressId)){
			isRemote=true;
		}
		
		
		//---------------------  		所有选中需要购买的购物车商品循环开始			--------------------------
		for (Map<String, Object> map : list) {
			userwork_+=map.get("userwork")!=null?map.get("userwork")+",":"0,";
			shopNo_+=(String)map.get("shopNo")+",";
			infoId_+=String.valueOf(map.get("infoId"))+",";
			textureIds_+=(String)map.get("textureIds")+"|";
			//材质名字组合
			temTextureName_+=(String)map.get("textureName")+"|";
			comId=map.get("userId").toString();
			userId_+=comId+",";
			imgUrl_+=(String)map.get("imgUrl")+",";
			fileType_+=(String)map.get("fileType")+",";
			sortName_+=(String)map.get("sortName")+",";
			//单类商品数量
			String temNum=String.valueOf(map.get("num"));
			num_+=temNum+",";
			name_+=(String)map.get("name")+",";
			param1_+=(map.get("param1")==null?"":map.get("param1").toString())+",";
			paramType_+=(map.get("paramType")==null?"":map.get("paramType").toString())+",";
			String goodsId=map.get("infoId")==null?"":map.get("infoId").toString();
			String fileSuffix=map.get("fileType")==null?"":map.get("fileType").toString();
			String workId=map.get("userwork")==null?"":map.get("userwork").toString();
			
			String isUnicom=String.valueOf(map.get("isUnicom"));
			isUnicom_ += isUnicom+",";
			
//			String imgs=map.get("imgUrl").toString();
//			//处理照片书的价格
//			if(imgs.contains("_")){
//				String img[]=imgs.split("_");
//				double price=Double.valueOf(map.get("nowPrice").toString());
//				int pNum=Integer.parseInt(img[1]);
//				//重整当前价格
//				map.put("nowPrice",price*pNum);
//			}
			
			String temNowPrice=String.valueOf(map.get("nowPrice"));
			//合计原价,不参与0元购
			nowPrice_+=temNowPrice+",";
			
			//获取活动类型
			String type=map.get("type")==null?"0":map.get("type").toString();
			//获取活动id
			String actId=map.get("actId")==null?"0":map.get("actId").toString();
			
			Map<String,Object> goods=this.indentDao.queryForGoods(goodsId,fileSuffix);
			int ispostage=(Integer)goods.get("ispostage");
			
			String photoImg=map.get("imgUrl")!=null?map.get("imgUrl").toString():"";
			//计算照片书价格
			if(photoImg.contains("_")){
				String photoNum[]=photoImg.split("_");
				if(photoNum.length>=2){
					String fx=(String)map.get("fileType");
					if(fx.equals("xxx")){
						Integer pN=Integer.parseInt(photoNum[1]);
						temNowPrice=String.valueOf(Double.valueOf(temNowPrice)*pN);
					}
				}
			}
			
			//先算商品的原价格
			totalFee=Double.valueOf(temNowPrice)*Integer.parseInt(temNum);		
			
			//偏远&邮费
			if(isRemote){
				if(transportMap.get("merchant_isRemote"+comId)==null){
					Map<String,Object>userMap=indentDao.queryForMerchant(comId);
					if(userMap==null)
						break;
					//偏远地区的邮费
					double rfree=Double.valueOf((userMap.get("remotefee")==null?"0.00":userMap.get("remotefee").toString()));
					transportMap.put("merchant_isRemote"+comId,rfree);
				}	
			}
				//不包邮就查运费
				if(ispostage==2){
					Map<String,Object>userMap=indentDao.queryForMerchant(comId);
					if(userMap==null)
						break;
					//包邮需要满足的金额
					double tfree=Double.valueOf((userMap.get("enough_money")==null?"0.00":userMap.get("enough_money").toString()));
					//不包邮时候的邮费
					double nfree=Double.valueOf((userMap.get("transportfee")==null?"0.00":userMap.get("transportfee").toString()));
					
					//记录商家下的价格
					if(transportMap.get("merchant_count_price"+comId)==null){
						transportMap.put("merchant_count_price"+comId,totalFee);
					}else{
						double tscP=(Double)transportMap.get("merchant_count_price"+comId);
						transportMap.put("merchant_count_price"+comId,totalFee+tscP);
					}
					
					//记录不免邮邮费
					if(transportMap.get("merchant"+comId)==null){
						transportMap.put("merchant_ispostage"+comId,nfree);
					}
					
					//满足条件设定运费0.00
					double merchantCountPrice=(Double)transportMap.get("merchant_count_price"+comId);
					if(merchantCountPrice>=tfree){
						transportMap.put("merchant_ispostage"+comId,0.00);
					}
				}else{
					transportMap.put("merchant_ispostage"+comId,0.00);
				}

			//判断是否是0元购商品
			if(StringUtils.isNotBlank(workId) && !"0".equals(workId)){
				if(iUserWorksDao.checkIsZeroGood(workId)){
					Double money=0.00;
					Map<String,Object>userWork=iUserWorksDao.queryUserWorkDetailInfo(workId);
					money=(Double)userWork.get("money");
					//0元购商品价格,作为当前这个商品价格
					temNowPrice=String.valueOf(money);
				}
			}
			
			//联通送礼品的变0元
			if(isUnicom.equals("1")){
				temNowPrice="0";
			}
	
			//按商家把活动分开,分商家算商家可以进行的活动
			String act= companyMap.get(comId)==null?"":companyMap.get(comId).toString();
			if(act.equals("")){
				//查询活动的详情
				Map<String,Object>typeInfo=indentDao.getTypeMap(actId);
				if(typeInfo!=null)
				typeInfo.put("companyId",comId);
				else
					noActiveFee+=Double.valueOf(temNowPrice)*Integer.parseInt(temNum);
				//可能一个商家下面多个活动
				companyMap.put(comId+"_"+type,typeInfo);
			}
			
			//封装活动数据，相同类型的活动放一起，分商家
			String num=activityMap.get("Num"+type+"_"+comId)==null?"":activityMap.get("Num"+type+"_"+comId).toString();
			String price=activityMap.get("Price"+type+"_"+comId)==null?"":activityMap.get("Price"+type+"_"+comId).toString();
			if(num.equals("") || price.equals("")){
				activityMap.put("Num"+type+"_"+comId,map.get("num"));
				activityMap.put("Price"+type+"_"+comId, temNowPrice);
			}else{
				activityMap.put("Num"+type+"_"+comId,num+","+map.get("num"));
				activityMap.put("Price"+type+"_"+comId,price+","+temNowPrice);
			}
			// 计算总数据
			String temTransportfee=String.valueOf(map.get("transportfee"));
			transportfee_=temTransportfee+",";
//			transport+=Double.valueOf(temTransportfee);
			allFee+=Double.valueOf(temNowPrice)*Integer.parseInt(temNum);
		}
		//---------				循环体结束			-------------------
		
		String types="";
		//格式化获取参与的活动type
		for(Map<String,Object>mm : list2){
			types=mm.get("type").toString()+",";
		}
		if(!types.equals("")){
			types=types.substring(0,types.length()-1);
		}
		
		
		//处理活动,合计活动以后的总价
		List<Map<String,Object>> resultList=CommonUtil.getActivityMethod(activityMap,companyMap);
		double nowPrice=0.00;
		if(resultList!=null){
			for(Map<String,Object>m : resultList){
				nowPrice+=Double.valueOf(m.get("nowPrice").toString());
				desPrice+=Double.valueOf(m.get("discount").toString());
			}
			//如果有没关联活动的商品算原价并和优惠的商品合并
			nowPrice+=noActiveFee;
		}else{
			//无活动商品用原价
			nowPrice=allFee;
		}
		
		for(String key : transportMap.keySet()){
			if(!key.contains("merchant_count")){
				transport+=(Double)transportMap.get(key);
			}
		}
		
		/*********/
		//验证订单商品的数量是够满足
		boolean flag=true;
		String[] infoIdss=CommonUtil.subStr(infoId_).split(",");
		String[] nums=CommonUtil.subStr(num_).split(",");
		String []fileType=CommonUtil.subStr(fileType_).split(","); 
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
		
		//验证商品库存
		for (String key:infoNum.keySet()) {
			flag=this.indentDao.checkOrderNum(infoNum.get(key),key);
			if(!flag){
				break;
			}
		}
		/*********/
		if(flag){ //商品数量充足继续处理
			//检验地址信息
			String addressJson=iAdressService.getAdressForId(userId);
			List<Map<String, Object>> addressList=mapper.readValue(addressJson, new TypeReference<List<Map<String,Object>>>() {});
			Map<String, Object> addressMap=new HashMap<>();
			for (Map<String, Object> map : addressList) {
				Integer id=(Integer)map.get("id");
				if(id.equals(Integer.parseInt(addressId))){
					addressMap=map;
					break;
				}
			}
			
			//处理优惠券
			double couponPrice=0.00;
			if(couponId!=null){
				couponPrice= this.privilegeForId(userId, couponId, nowPrice);
			}
			//生成订单
			double totalFee_=this.iShopCartDao.createOrderByShopsForId(orderNo,CommonUtil.subStr(shopNo_),CommonUtil.subStr(infoId_),CommonUtil.subStr(textureIds_),CommonUtil.subStr(temTextureName_),CommonUtil.subStr(userId_),CommonUtil.subStr(imgUrl_),CommonUtil.subStr(fileType_),CommonUtil.subStr(num_),CommonUtil.subStr(name_),CommonUtil.subStr(nowPrice_),payId,orgPrice,desPrice,couponId,nowPrice-couponPrice+transport,addressMap,userId,transport,CommonUtil.subStr(sortName_),couponPrice,types,CommonUtil.subStr(userwork_),message,CommonUtil.subStr(param1_),CommonUtil.subStr(paramType_),CommonUtil.subStr(isUnicom_));
			Map<String, Object> resultMap=new HashMap<>();
			this.indentDao.updateShopOrderNumForId(userId, "order", 1);
			if("2".equals(payId)){//微信
				//请求微信生成prepayId
				Map<String, Object> paraMap= WxPayUtil.unifiedorder(totalFee_, orderNo,getWxPay(app,"key"),getWxPay(app,"appid"),getWxPay(app, "appIDS"),getWxPay(app, "mchId"));
				String prepayId=(String)paraMap.get("prepayId");
				String payNo=(String)paraMap.get("payNo");
				if(StringUtils.isBlank(prepayId)){
					return null;
				}
				//将prepayid payNo绑定到订单  到支付信息表
				int result3=this.indentDao.boundOrderPrepay(orderNo,prepayId,payNo);
				if(result3>0){
					resultMap.put("prepayId", prepayId);
				}
			}
			resultMap.put("orderNo", orderNo);
			resultMap.put("totalFee", df.format(totalFee_));
			resultMap.put("creatTime", DateUtil.date2String(new Date(), DateUtil.FORMAT_DATETIME));
			this.invalidShopsForId(shopNos,userId);
			return mapper.writeValueAsString(resultMap);
		}else{
			return null;
		}
	}
	
	/**
	 * tengh 2016年6月7日 下午3:11:17
	 * @param appid
	 * @param key
	 * @return
	 * TODO 获取微信配置
	 */
	private String getWxPay(String app, String key) throws Exception{
		String json=this.ocsDao.query(OCSKey.DIY_WX_APP_+app.split("V")[0]);
		if(StringUtils.isBlank(json)){
			Map<String, Object> teMap=this.indentDao.getWxPay(app.split("V")[0]);
			json=mapper.writeValueAsString(teMap);
			ocsDao.insert(OCSKey.DIY_WX_APP_+app.split("V")[0], json, 0);
		}
		Map<String, Object> map=mapper.readValue(json, HashMap.class);
		return (String)map.get(key);
	}
	
	@Override
	public void invalidShops(String shopNos,String deviceNo,String app){
		//查询多个购物车信息
		String[] shopNoss=shopNos.split(",");
		String shopNo="(";
		for (int i = 0; i < shopNoss.length; i++) {
			shopNo+=("'"+shopNoss[i]+"',");
		}
		shopNo=shopNo.substring(0,shopNo.length()-1);
		shopNo+=")";
		//购物车失效
		this.iShopCartDao.invalidShop(shopNo,deviceNo,app);
		//购物车数量减少
		this.indentDao.updateShopOrderNum(deviceNo, app, "shop", -(shopNoss.length));
	}
	
	@Override
	public void invalidShopsForId(String shopNos,String userId){
		//查询多个购物车信息
		String[] shopNoss=shopNos.split(",");
		String shopNo="(";
		for (int i = 0; i < shopNoss.length; i++) {
			shopNo+=("'"+shopNoss[i]+"',");
		}
		shopNo=shopNo.substring(0,shopNo.length()-1);
		shopNo+=")";
		//购物车失效
		this.iShopCartDao.invalidShopForId(shopNo,userId);
		//购物车数量减少
		this.indentDao.updateShopOrderNumForId(userId, "shop", -(shopNoss.length));
	}
	
	@Override
	public int countActiveShop(String deviceNo, String app, String shopNo) {
		return this.iShopCartDao.countActiveShop(deviceNo,app,shopNo);
	}
	
	@Override
	public int countActiveShopForId(String userId, String shopNo) {
		return this.iShopCartDao.countActiveShopForId(userId,shopNo);
	}
	
	@Override
	public int addShopByOrder(String prices, String imgUrls, String fileTypes, String nums, String textureNamess,
			String infoIdss, String userIdss ,String shopNo,String deviceNo,String app,String textureId) {
		return this.iShopCartDao.addShopByOrder(prices,imgUrls,fileTypes,nums,textureNamess,infoIdss,userIdss,shopNo,deviceNo,app,textureId);
	}
	
	@Override
	public int addShopByOrderForId(String prices, String imgUrls, String fileTypes, String nums, String textureNamess,
			String infoIdss, String userIdss ,String shopNo,String userId,String textureId,String workId) {
		return this.iShopCartDao.addShopByOrderForId(prices,imgUrls,fileTypes,nums,textureNamess,infoIdss,userIdss,shopNo,userId,textureId,workId);
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
	
	public int isSameGoods(String deviceNo,String app,String textureIds,String num,String infoId){
		Integer nn=Integer.parseInt(num);
		List<Map<String,Object>>list=iShopCartDao.queryShopList(deviceNo,app);
		if(list.isEmpty()){
			return nn;
		}
		for(Map<String,Object>map:list){
			String texture=map.get("texture_ids")==null?"":map.get("texture_ids").toString();
			String info_id=map.get("info_id")==null?"":map.get("info_id").toString();
			if(textureIds.equals(texture) && infoId.equals(info_id)){
				int n=Integer.parseInt(map.get("num").toString());
				//购物车-1
				int result=this.indentDao.updateShopOrderNum(deviceNo, app, "shop", -1);
				//删除购物车
			    result= this.indentDao.deleteShop(deviceNo,app,"('"+map.get("shop_no").toString()+"')");
				if(result<=0){
					throw new RuntimeException();
				}
				return nn+n;
			}
		}
		return nn;
	}
	
	public int isSameGoodsForId(String userId,String textureIds,String num,String infoId){
		Integer nn=Integer.parseInt(num);
		List<Map<String,Object>>list=iShopCartDao.queryShopListForId(userId);
		for(Map<String,Object>map:list){
			String texture=map.get("texture_ids")==null?"":map.get("texture_ids").toString();
			String info_id=map.get("info_id")==null?"":map.get("info_id").toString();
			if(textureIds.equals(texture) && infoId.equals(info_id)){
				int n=Integer.parseInt(map.get("num").toString());
				//购物车-1
				int result=this.indentDao.updateShopOrderNumForId(userId, "shop", -1);
				//删除购物车
				result= this.indentDao.deleteShopForId(userId,"('"+map.get("shop_no").toString()+"')");
				if(result<=0){
					throw new RuntimeException();
				}
				return nn+n;
			}
		}
		return nn;
	}
	
	@Override
	public String togetherGoods(String id, String page,String activityId) {
		List<Map<String, Object>> goodlist=null;
		if( activityId.isEmpty() || "0".equals(activityId)){
			goodlist=this.iShopCartDao.togetherGoods(id,page);
		}else{
			goodlist=this.iShopCartDao.togetherForActivity(id,activityId, page);
		}
		if(goodlist==null || goodlist.size()<=0){
			return null;
		}
		for (int i = 0; i < goodlist.size(); i++) {
			String icoUrl = null==goodlist.get(i).get("icoUrl")?"":goodlist.get(i).get("icoUrl").toString();
			if(StringUtils.isNotEmpty(icoUrl)){
				goodlist.get(i).put("icoUrl", dgurl+icoUrl);
			}
			//格式化价格
			goodlist.get(i).put("nowPrice",df.format(goodlist.get(i).get("nowPrice")));
		}
		return JSONUtil.convertArrayToJSON(goodlist);
	}
	
	@Override
	public boolean carshopSelect(String id, String shopNos, String type) {
		String shopNo="";
		if(StringUtils.isNotBlank(shopNos)){
			String[] shopNoss=shopNos.split(",");
			shopNo=shopNo+"(";
			for (int i = 0; i < shopNoss.length; i++) {
				shopNo+=("'"+shopNoss[i]+"',");
			}
			shopNo=shopNo.substring(0,shopNo.length()-1);
			shopNo+=")";
		}
		return this.iShopCartDao.carshopSelect(id,shopNo,type);
	}
	
	@Override
	public boolean addShopByGood(String userId, String id) {
		Map<String, Object> jphGoodInfo=this.indentDao.queryjphGoodInfo(id);
		Integer info_id=(Integer)jphGoodInfo.get("id");
		String texture_ids=(String)jphGoodInfo.get("texture_ids");
		String shopNo=this.iShopCartDao.getShopNo(userId,info_id,texture_ids);
		if(StringUtils.isBlank(shopNo)){
			jphGoodInfo.put("userId", userId);
			jphGoodInfo.put("num", 1);
			jphGoodInfo.put("shop_no", CommonUtil.createOrderNo("G", 3));
			jphGoodInfo.put("file_type", "xxx");
			jphGoodInfo.put("texture_name", indentDao.queryTextureById(((String)jphGoodInfo.get("texture_ids")).split("_")));
			return this.iShopCartDao.addShopByGood(jphGoodInfo);
		}else{
			this.iShopCartDao.updateNum(userId,shopNo);
			return true;
		}
		
	}
	
	private void checkShopNum(String userId){
		int num=this.iShopCartDao.getUserRealShopNum(userId);
		String failShop=this.iShopCartDao.getUserShopFailNo(userId);
		if(!StringUtils.isBlank(failShop))
		this.indentDao.deleteShopForId(userId,"("+failShop+")");
		this.indentDao.updateShopOrderNumForId2(userId,"shop",num);
	}

}
