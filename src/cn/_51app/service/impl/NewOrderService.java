package cn._51app.service.impl;
import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import cn._51app.dao.INewOrderDao;
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
public class NewOrderService extends BaseService implements INewOrderService {
	
	private final String updownloadRootDir =PropertiesUtil.getValue("uploadUrl.sys");
	private Logger logger = Logger.getLogger(NewOrderService.class);
	private final String diyRootPath =PropertiesUtil.getValue("diy.root.path");
	private final String orderNumer =PropertiesUtil.getValue("diy.order.page.size");
	private String expressMsg="待付款";
	private DecimalFormat df= new DecimalFormat("######0.00");
	
	@Autowired
	private INewOrderDao iNewOrderDao;
	@Autowired
	private INewShopCartService iNewShopCartService;
	@Autowired
	private INewAddressService inewAddressService;
	@Autowired
	private OCSDao ocsDao;
	private ObjectMapper mapper=new ObjectMapper();
	
	@Override
	public String createOrder(MultipartFile imgFile, MultipartFile imgBackFile, MultipartFile previewFile,MultipartFile previewBackFile,
			String infoId, String textureIds, String deviceNo, String app,String num) throws Exception{
		Map<String, Object> resultMap=new HashMap<>();
		String orderNo=CommonUtil.createOrderNo("D", 5);
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
//				String boutique[]=resultPath.split(",");

					sufFormat="xxx";

			}
			//插入到订单表
			Map<String, Object> returnMap=this.iNewOrderDao.createOrder(resultPath,infoId,textureIds,deviceNo,app,orderNo,sufFormat,num);
			if(resultMap!=null){
				resultMap.put("orderNo", orderNo);
				resultMap.put("sufFormat", sufFormat);
				resultMap.put("imgUrl", diyRootPath+resultPath);
				resultMap.put("num", num);
				resultMap.put("name", returnMap.get("name"));
				resultMap.put("textureName", returnMap.get("textureNames"));
				resultMap.put("nowPrice", returnMap.get("orgPrice"));
				return mapper.writeValueAsString(resultMap);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public String getPrivilege() throws Exception {
		String json=this.ocsDao.query(OCSKey.DIY_PRIVILEGE);
		if(StringUtils.isBlank(json)){
			List<Map<String, Object>> list=this.iNewOrderDao.queryPrivilege();
			if(list==null){
				return null;
			}
			json=mapper.writeValueAsString(list);
			if (!ocsDao.insert(OCSKey.DIY_PRIVILEGE, json, 0) && logger.isInfoEnabled()) {
				logger.info("NewOrderService getPrivilege 我的优惠券初始化失败");
			}
		}
		return json;
	}
	
	@Override
	public String activeOrder(String deviceNo, String app, String couponId, String orderNo, String addressId,
			String num, String payId) throws Exception {
		/*********/
		//验证订单商品的数量是够满足
		boolean flag=true;
		Map<String, Object> orderGoodNumAndPay=this.iNewOrderDao.confirmOrderNumAndPay(orderNo,deviceNo,app);
		double totalFee=0;
		String[] infoIdss=((String)orderGoodNumAndPay.get("infoIds")).split(",");
		String[] nums=((String)orderGoodNumAndPay.get("num")).split(",");
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
		if(flag){ //商品数量充足继续处理
			/*********/
			Map<String, Object> resultMap=new HashMap<>();
			//查询订单总价  单价*数量 +运费     price    transportfee
			Map<String, Object> orderInfo=this.iNewOrderDao.queryOrderInfo(orderNo,deviceNo,app);
			if(orderInfo==null){
				return null;
			}
			double price=Double.valueOf((String)orderInfo.get("price"));
			double transportfee=Double.valueOf((String)orderInfo.get("transportfee"));
			totalFee=price*Integer.parseInt(num)+transportfee;
			double orgPrice=0.00,desPrice=0.00;
			//处理满减优惠
			String json=this.getPrivilege();
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
			try {
			String addressJson=inewAddressService.getAdress(deviceNo, app);
			List<Map<String, Object>> addressList=mapper.readValue(addressJson, new TypeReference<List<Map<String,Object>>>() {});
			Map<String, Object> addressMap=new HashMap<>();
			for (Map<String, Object> map : addressList) {
				Integer id=(Integer)map.get("id");
				if(id.equals(Integer.parseInt(addressId))){
					addressMap=map;
					break;
				}
			}
			
				flag=true;
				//绑定订单需要的信息
				if(flag){
					double totalFee_=this.iNewOrderDao.activeOrder(orderNo,couponId,addressId,num,payId,totalFee,orgPrice,desPrice,deviceNo,app,addressMap);
					if("2".equals(payId)){//微信
						//请求微信生成prepayId
						Map<String, Object> paraMap= WxPayUtil.unifiedorder(totalFee_, orderNo,getWxPay(app,"key"),getWxPay(app,"appid"),getWxPay(app, "appIDS"),getWxPay(app, "mchId"));
						String prepayId=(String)paraMap.get("prepayId");
						String payNo=(String)paraMap.get("payNo");
						if(StringUtils.isBlank(prepayId)){
							return null;
						}
						//将prepayid payNo绑定到订单  到支付信息表
						int result=this.iNewOrderDao.boundOrderPrepay(orderNo,prepayId,payNo);
						if(result>0){
							resultMap.put("prepayId", prepayId);
						}
					}
					if(StringUtils.isNotBlank(orderNo)){
						//更新订单数
						this.iNewOrderDao.updateShopOrderNum(deviceNo,app,"order",1);
					}
					resultMap.put("orderNo", orderNo);
					resultMap.put("totalFee", df.format(totalFee_));
					resultMap.put("creatTime", DateUtil.date2String(new Date(), DateUtil.FORMAT_DATETIME));
				}else{
					return null;
				}
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
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
			if(teMap!=null){
				json=mapper.writeValueAsString(teMap);
				ocsDao.insert(OCSKey.DIY_WX_APP_+app.split("V")[0], json, 0);
			}
		}
		if(StringUtils.isNotBlank(json)){
			Map<String, Object> map=mapper.readValue(json, HashMap.class);
			return (String)map.get(key);
		}
		return null;
	}

	@Override
	public String getOrderInfo(String orderNo,String deviceNo,String app) throws Exception{
		Map<String, Object> result=new HashMap<>();
		Map<String, Object> map=this.iNewOrderDao.queryOrderInfo(orderNo, deviceNo, app);
		if(map!=null){
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
			String creatTime=(String)map.get("creatTime");
			String[] prices=((String)map.get("price")).split(",");
			String[] imgUrls=((String)map.get("imgUrl")).split(",");
			String[] fileTypes=((String)map.get("fileType")).split(",");
			String[] nums=((String)map.get("num")).split(",");
			String names=(String)map.get("name");
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
				tempMap.put("imgUrl", diyRootPath+imgUrls[i]);
				tempMap.put("fileType", fileTypes[i]==null?"":fileTypes[i]);
				tempMap.put("num", nums[i]);
				tempMap.put("name", names);
				tempMap.put("textureName", textureNamess[i]);
				tempMap.put("goodsId", infoIds[i]);
				goodinfos.add(tempMap);
			}
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
		List<Map<String, Object>> list=this.iNewOrderDao.getOrderList(deviceNo,app,Integer.parseInt(page),Integer.parseInt(orderNumer),Integer.parseInt(state));
		if(list!=null){
			for (Map<String, Object> map : list) {
				tempMap=new HashMap<>();
				Integer status=(Integer)map.get("status");
				String code=(String)map.get("code");
				String orderNo=(String)map.get("orderNo");
				String payType=(String)map.get("payType");
				String names=(String)map.get("name");
				double feeTotal=(Double)map.get("feeTotal");
				String expressNo=(String)map.get("expressNo");
				String[] prices=((String)map.get("price")).split(",");
				String imgUrl=map.get("imgUrl")==null?"":map.get("imgUrl").toString();
				String[] imgUrls=imgUrl.split(",");
				String[] fileTypes=((String)map.get("fileType")).split(",");
				String[] nums=((String)map.get("num")).split(",");
				String[] textureNamess=((String)map.get("textureNames")).split("\\|");
				String[] infoIds=((String)map.get("infoIds")).split(",");
				List<Map<String, Object>> goodinfos=new ArrayList<>();
				Map<String, Object> tempMap2=null;
				for (int i = 0; i < prices.length; i++) {
					tempMap2=new HashMap<>();
					tempMap2.put("nowPrice", prices[i]);
					if(!imgUrl.equals("")){
					tempMap2.put("imgUrl", diyRootPath+imgUrls[i]);
					}else{
						tempMap2.put("imgUrl", null);
					}
					tempMap2.put("fileType", fileTypes[i]==null?"":fileTypes[i]);
					tempMap2.put("num", nums[i]);
					tempMap2.put("name", names);
					tempMap2.put("textureName", textureNamess[i]);
					tempMap2.put("goodsId", infoIds[i]);
					goodinfos.add(tempMap2);
				}
				tempMap.put("goodinfo", goodinfos);
				tempMap.put("status", status);
				tempMap.put("code", code==null?"":code);
				tempMap.put("orderNo", orderNo);
				tempMap.put("payType", payType);
				tempMap.put("feeTotal", feeTotal);
				tempMap.put("expressNo", expressNo==null?"":expressNo.trim());
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
		return this.iNewOrderDao.checkOrder(orderNo,deviceNo,app);
	}
	
	@Override
	public String confirmOrder(String orderNo, String deviceNo, String app) throws Exception{
		//验证订单的商品是否都充足
		Map<String, Object> orderGoodNumAndPay=this.iNewOrderDao.confirmOrderNumAndPay(orderNo,deviceNo,app);
		String payType=(String)orderGoodNumAndPay.get("payType");
		String prepayId=(String)orderGoodNumAndPay.get("prepayId");
		double feeTotal=(Double)orderGoodNumAndPay.get("feeTotal");
		Long time=(Long)orderGoodNumAndPay.get("time");
		String[] infoIdss=((String)orderGoodNumAndPay.get("infoIds")).split(",");
		String[] nums=((String)orderGoodNumAndPay.get("num")).split(",");
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
		boolean flag=true;
		for (String key:infoNum.keySet()) {
			flag=this.iNewOrderDao.checkOrderNum(infoNum.get(key),key);
			if(!flag){
				break;
			}
		}
		//如果是微信  验证prepayId是否过期  过期重新生成
		if("2".equals(payType)){
			if(time>118 || time==null){
				Map<String,Object> mapres=WxPayUtil.unifiedorder(feeTotal, orderNo,getWxPay(app, "key"),getWxPay(app, "appid"),getWxPay(app, "appIDS"),getWxPay(app, "mchId"));
				prepayId=(String)mapres.get("prepayId");
				int result=this.iNewOrderDao.boundPrepayNo(orderNo,(String)mapres.get("prepayId"),(String)mapres.get("payNo"));
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
	public boolean updateOrder(String orderNo, String deviceNo, String app, String flag) {
		if("delete".equalsIgnoreCase(flag) || "confirm".equalsIgnoreCase(flag) || "cancel".equalsIgnoreCase(flag) || "padiCancel".equals(flag)){
			return this.iNewOrderDao.updateOrder(orderNo,deviceNo,app,flag);
		}else if("addShop".equalsIgnoreCase(flag)){
			//订单添加到购物车
			Map<String, Object> map=this.iNewOrderDao.queryOrderInfo(orderNo, deviceNo, app);
			if(map!=null){
				String[] prices=((String)map.get("price")).split(",");
				String[] imgUrls=((String)map.get("imgUrl")).split(",");
				String[] fileTypes=((String)map.get("fileType")).split(",");
				String[] nums=((String)map.get("num")).split(",");
				String[] textureNamess=((String)map.get("textureNames")).split("\\|");
				String[] infoIdss=((String)map.get("infoIds")).split(",");
				String[] userIdss=((String)map.get("userIds")).split(",");
				String[] textureIdss=((String)map.get("textureIds")).split("\\|");
				String temShopNo= (String)map.get("shopNo");
				String[] shopNos=new String[]{};
				int index=0;
				if(StringUtils.isNotBlank(temShopNo)){
					shopNos=temShopNo.split(",");
					String shopNo="(";
					for (int i = 0; i < shopNos.length; i++) {
						shopNo+=("'"+shopNos[i]+"',");
					}
					shopNo=shopNo.substring(0,shopNo.length()-1);
					shopNo+=")";
					index=this.iNewShopCartService.countActiveShop(deviceNo,app,shopNo);
				}else{
					shopNos=new String[prices.length];
					index=prices.length;
				}
				for (int i = 0; i < prices.length; i++) {
					this.iNewShopCartService.addShopByOrder(prices[i],imgUrls[i],fileTypes[i],nums[i],textureNamess[i],infoIdss[i],userIdss[i],((shopNos[i]==null)?CommonUtil.createOrderNo("G", 3):shopNos[i]),deviceNo,app,textureIdss[i]);
				}
				//更新购物车数量
				this.iNewOrderDao.updateShopOrderNum(deviceNo, app, "shop", index);
			}
			return true;
		}else{
			return false;
		}
	}
	
	@Override
	public boolean recordUser(String deviceNo, String app, String deviceToken) {
		return this.iNewOrderDao.recordUser(deviceNo,app,deviceNo);
	}
	
	@Override
	public boolean infoUser(String deviceNo, String app, String version,String deviceToken) {
		return this.iNewOrderDao.infoUser(deviceNo,app,version,deviceToken);
	}
}
