package cn._51app.service.impl;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import cn._51app.dao.INewOrderDao;
import cn._51app.dao.INewShopCartDao;
import cn._51app.dao.ocs.OCSDao;
import cn._51app.service.BaseService;
import cn._51app.service.INewAddressService;
import cn._51app.service.INewOrderService;
import cn._51app.service.INewShopCartService;
import cn._51app.util.CommonUtil;
import cn._51app.util.DateUtil;
import cn._51app.util.OCSKey;
import cn._51app.util.PropertiesUtil;
import cn._51app.util.WxPayUtil;

@Service
public class NewShopCartService extends BaseService implements INewShopCartService {
	
	@Autowired
	private INewShopCartDao iNewShopCartDao;
	@Autowired
	private INewOrderDao iNewOrderDao;
	@Autowired
	private INewOrderService iNewOrderService;
	@Autowired
	private INewAddressService iNewAddressService;
	@Autowired
	private OCSDao ocsDao;
	private final String shopNumer =PropertiesUtil.getValue("diy.goods.page.size");
	private final String diyRootPath =PropertiesUtil.getValue("diy.root.path");
	private final String updownloadRootDir =PropertiesUtil.getValue("uploadUrl.sys");
	private ObjectMapper mapper=new ObjectMapper();
	private DecimalFormat df= new DecimalFormat("######0.00");
	
	public String shopList(String deviceNo, String app, String page) throws Exception{
		if(StringUtils.isBlank(page)){
			page="0";
		}
		List<Map<String, Object>> list=this.iNewShopCartDao.queryShopList(deviceNo,app,Integer.parseInt(page),Integer.parseInt(shopNumer));
		if(list!=null){
			for (int i = 0; i < list.size(); i++) {
				String fileType=(String)list.get(i).get("fileType");
				String imgUrl=(String)list.get(i).get("imgUrl");
				list.get(i).put("fileType", fileType==null?"":fileType);
				list.get(i).put("imgUrl", imgUrl==null?"":(diyRootPath+imgUrl));
				File file=new File(updownloadRootDir+imgUrl+"@pb"+fileType);
				if(file.exists()){
					list.get(i).put("previewBack", diyRootPath+imgUrl+"@pb"+fileType);
				}else{
					list.get(i).put("previewBack", "");
				}
			}
			return mapper.writeValueAsString(list);
		}
		return null;
	}

	public boolean addShop(MultipartFile imgFile, MultipartFile imgBackFile, MultipartFile previewFile, MultipartFile previewBackFile,String infoId,
			String textureIds, String deviceNo, String num, String app){
		String orderNo=CommonUtil.createOrderNo("G", 3);
		String resultPath=null;
		try {
			String pathName=new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());
			File file=new File(updownloadRootDir+"order/"+pathName);
			resultPath="order/"+pathName+"/"+orderNo;
			String sufFormat=".jpg";
			if(imgFile!=null && !imgFile.isEmpty()){  //非精品会商品
				if(!file.exists()){
					file.mkdirs();
				}
				if(imgFile!=null && !imgFile.isEmpty()){
					File imgFilePath=new File(updownloadRootDir+resultPath+sufFormat);
					if(!imgFilePath.exists()){
						imgFilePath.createNewFile();
					}
					imgFile.transferTo(imgFilePath);
				}
				if(imgBackFile!=null && !imgBackFile.isEmpty()){
					File imgBackFilePath=new File(updownloadRootDir+resultPath+"@b"+sufFormat);
					if(!imgBackFilePath.exists()){
						imgBackFilePath.createNewFile();
					}
					imgBackFile.transferTo(imgBackFilePath);
				}
				if(previewFile!=null && !previewFile.isEmpty()){
					File previewFilePath=new File(updownloadRootDir+resultPath+"@p"+sufFormat);
					if(!previewFilePath.exists()){
						previewFilePath.createNewFile();
					}
					previewFile.transferTo(previewFilePath);
				}
				if(previewBackFile!=null && !previewBackFile.isEmpty()){
					File previewBackFilePath=new File(updownloadRootDir+resultPath+"@pb"+sufFormat);
					if(!previewBackFilePath.exists()){
						previewBackFilePath.createNewFile();
					}
					previewBackFile.transferTo(previewBackFilePath);
				}
			}else{//精品会商品
				//查询出精品会商品图片  (预览图足够)
				resultPath=this.iNewOrderDao.queryPreUrl(infoId,textureIds);
				System.err.println("resultPath:"+resultPath);
//				String boutique[]=resultPath.split(",");
				sufFormat="xxx";
		
			}
			//插入到购物车
			int result=this.iNewShopCartDao.createShop(resultPath,infoId,textureIds,deviceNo,app,orderNo,sufFormat,num);
			if(result>0){
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean deleteShop(String deviceNo, String app, String shopNos) {
		String[] shopNoss=shopNos.split(",");
		String shopNo="(";
		for (int i = 0; i < shopNoss.length; i++) {
			shopNo+=("'"+shopNoss[i]+"',");
		}
		shopNo=shopNo.substring(0,shopNo.length()-1);
		shopNo+=")";
		int result= this.iNewOrderDao.deleteShop(deviceNo,app,shopNo);
		if(result>0){
			//更新购物车数量	
			this.iNewOrderDao.updateShopOrderNum(deviceNo, app, "shop", -result);
			return true;
		}
		return false;
	}
	
	@Override
	public boolean updateShop(String deviceNo, String app, String shopNos, String nums) {
		int result= this.iNewShopCartDao.updateShop(deviceNo,app,shopNos,nums);
		if(result>0){
			return true;
		}
		return false;
	}
	
	@Override
	public String getOrderShopNum(String deviceNo, String app) throws Exception{
		Map<String, Object> map=this.iNewShopCartDao.getOrderShopNum(deviceNo,app);
		if(map==null){
			return null;
		}
		return mapper.writeValueAsString(map);
	}
	
	@Override
	public String createOrderByShops(String deviceNo, String app, String shopNos,String payId,String addressId,String couponId) throws Exception {
		String orderNo=CommonUtil.createOrderNo("G", 5);
		//查询多个购物车信息
		String[] shopNoss=shopNos.split(",");
		String shopNo="(";
		for (int i = 0; i < shopNoss.length; i++) {
			shopNo+=("'"+shopNoss[i]+"',");
		}
		shopNo=shopNo.substring(0,shopNo.length()-1);
		shopNo+=")";
		List<Map<String, Object>> list=this.iNewShopCartDao.getShopInfoByShopNos(shopNo);
		String shopNo_="",infoId_="",textureIds_="",temTextureName_="",userId_="",imgUrl_="",fileType_="",num_="",name_="",nowPrice_="",transportfee_="";
		double totalFee=0.00,orgPrice=0.00,desPrice=0.00;
		for (Map<String, Object> map : list) {
			shopNo_+=(String)map.get("shopNo")+",";
			infoId_+=String.valueOf(map.get("infoId"))+",";
			textureIds_+=(String)map.get("textureIds")+"|";
			temTextureName_+=(String)map.get("textureName")+"|";
			userId_+=String.valueOf(map.get("userId"))+",";
			imgUrl_+=(String)map.get("imgUrl")+",";
			fileType_+=(String)map.get("fileType")+",";
			String temNum=String.valueOf(map.get("num"));
			num_+=temNum+",";
			name_+=(String)map.get("name")+",";
			String temNowPrice=String.valueOf(map.get("nowPrice"));
			nowPrice_+=temNowPrice+",";
			String temTransportfee=String.valueOf(map.get("transportfee"));
			transportfee_=temTransportfee+",";
			totalFee+=Double.valueOf(temNowPrice)*Integer.parseInt(temNum)+Double.valueOf(temTransportfee);
		}
		/*********/
		//验证订单商品的数量是够满足
		boolean flag=true;
		String[] infoIdss=CommonUtil.subStr(infoId_).split(",");
		String[] nums=CommonUtil.subStr(num_).split(",");
		//商品 数量合并然后去检测 每种数量是否充足
		Map<String, Integer> infoNum=new HashMap<>();
		for (int i = 0; i < infoIdss.length; i++) {
			Integer value=infoNum.get(infoIdss[i]);
			if(value==null || value==0){
				infoNum.put(infoIdss[i], Integer.parseInt(nums[i]));
			}else{
				infoNum.put(infoIdss[i], value+Integer.parseInt(nums[i]));
			}
		}
		for (String key:infoNum.keySet()) {
			flag=this.iNewOrderDao.checkOrderNum(infoNum.get(key),key);
			if(!flag){
				break;
			}
		}
		/*********/
		if(flag){ //商品数量充足继续处理
			//处理满减优惠
			String json=this.iNewOrderService.getPrivilege();
			if(StringUtils.isNotBlank(json)){
				List<Map<String, Object>> privilege=mapper.readValue(json, new TypeReference<List<Map<String,Object>>>() {});
				for (Map<String, Object> map : privilege) {
					orgPrice=(Double)map.get("orgPrice");
					desPrice=(Double)map.get("desPrice");
					if(totalFee>=orgPrice){
						totalFee=totalFee-desPrice;
						break;
					}
				}
			}
			//检验地址信息
			String addressJson=iNewAddressService.getAdress(deviceNo, app);
			List<Map<String, Object>> addressList=mapper.readValue(addressJson, new TypeReference<List<Map<String,Object>>>() {});
			Map<String, Object> addressMap=new HashMap<>();
			for (Map<String, Object> map : addressList) {
				Integer id=(Integer)map.get("id");
				if(id.equals(Integer.parseInt(addressId))){
					addressMap=map;
					break;
				}
			}
			//生成订单
			double totalFee_=this.iNewShopCartDao.createOrderByShops(orderNo,CommonUtil.subStr(shopNo_),CommonUtil.subStr(infoId_),CommonUtil.subStr(textureIds_),CommonUtil.subStr(temTextureName_),CommonUtil.subStr(userId_),CommonUtil.subStr(imgUrl_),CommonUtil.subStr(fileType_),CommonUtil.subStr(num_),CommonUtil.subStr(name_),CommonUtil.subStr(nowPrice_),payId,orgPrice,desPrice,couponId,totalFee,addressMap,deviceNo,app,CommonUtil.subStr(transportfee_));
			Map<String, Object> resultMap=new HashMap<>();
			this.iNewOrderDao.updateShopOrderNum(deviceNo, app, "order", 1);
			if("2".equals(payId)){//微信
				//请求微信生成prepayId
				Map<String, Object> paraMap= WxPayUtil.unifiedorder(totalFee_, orderNo,getWxPay(app,"key"),getWxPay(app,"appid"),getWxPay(app, "appIDS"),getWxPay(app, "mchId"));
				String prepayId=(String)paraMap.get("prepayId");
				String payNo=(String)paraMap.get("payNo");
				if(StringUtils.isBlank(prepayId)){
					return null;
				}
				//将prepayid payNo绑定到订单  到支付信息表
				int result3=this.iNewOrderDao.boundOrderPrepay(orderNo,prepayId,payNo);
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
			Map<String, Object> teMap=this.iNewOrderDao.getWxPay(app.split("V")[0]);
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
		this.iNewShopCartDao.invalidShop(shopNo,deviceNo,app);
		//购物车数量减少
		this.iNewOrderDao.updateShopOrderNum(deviceNo, app, "shop", -(shopNoss.length));
	}
	
	@Override
	public int addShopByOrder(String prices, String imgUrls, String fileTypes, String nums, String textureNamess,
			String infoIdss, String userIdss ,String shopNo,String deviceNo,String app,String textureId) {
		return this.iNewShopCartDao.addShopByOrder(prices,imgUrls,fileTypes,nums,textureNamess,infoIdss,userIdss,shopNo,deviceNo,app,textureId);
	}
	
	@Override
	public int countActiveShop(String deviceNo, String app, String shopNo) {
		return this.iNewShopCartDao.countActiveShop(deviceNo,app,shopNo);
	}
}
