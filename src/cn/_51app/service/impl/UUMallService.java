package cn._51app.service.impl;

import java.io.File;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import cn._51app.dao.ICouponDao;
import cn._51app.dao.IEvaluationDao;
import cn._51app.dao.INewOrderDao;
import cn._51app.dao.INewShopCartDao;
import cn._51app.dao.IUUMallDao;
import cn._51app.dao.ocs.OCSDao;
import cn._51app.service.BaseService;
import cn._51app.service.INewAddressService;
import cn._51app.service.INewOrderService;
import cn._51app.service.IUUMallService;
import cn._51app.util.AliSMSUtil;
import cn._51app.util.CommonUtil;
import cn._51app.util.DateUtil;
import cn._51app.util.OCSKey;
import cn._51app.util.PropertiesUtil;
import cn._51app.util.QQLoginUtil;
import cn._51app.util.WxLoginUtil;
import cn._51app.util.WxPayUtil;

@Service
public class UUMallService extends BaseService implements IUUMallService {
	
	private static Logger log=Logger.getLogger(UUMallService.class);
	
	@Autowired
	private IUUMallDao iuuMallDao;
	@Autowired
	private INewShopCartDao iNewShopCartDao;
	@Autowired
	private IEvaluationDao iEvaluationDao;
	@Autowired
	private ICouponDao iCouponDao;
	@Autowired
	private INewOrderDao iNewOrderDao;
	@Autowired
	private INewAddressService inewAddressService;
	@Autowired
	private INewOrderService iNewOrderService;
	
	@Autowired
	private OCSDao ocsDao;
	
	private ObjectMapper mapper=new ObjectMapper();
	private DecimalFormat df= new DecimalFormat("######0.00");
	
	private final String diyRootPath =PropertiesUtil.getValue("diy.root.path");
	private final String updownloadRootDir =PropertiesUtil.getValue("uploadUrl.sys");
	private final String dgurl =PropertiesUtil.getValue("diy.goods.url");
	private final String orderNumer =PropertiesUtil.getValue("diy.order.page.size");
	private String expressMsg="待付款";
	
	@Override
	public boolean haveOpenid(String openid){
		return iuuMallDao.haveOpenid(openid)==1;
	}
	
	@Override
	public boolean haveMobile(String mobile){
		return iuuMallDao.haveMobile(mobile)==1;
	}
	
	/**
	 * 发送验证码
	 */
	@Override
	public boolean sendMsg(String phone,String smscode) throws Exception{
		return AliSMSUtil.sendMsg(phone, smscode);
	}
	
	/**
	 * 插入微信用户
	 * @author zhanglz
	 * @return 
	 */
	public boolean insertWx(String openid,String name,String head_url){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("app", WxLoginUtil.wx_user_app);
		map.put("device_no", CommonUtil.createOrderNo("U", 5)+"-"+CommonUtil.createOrderNo("U", 5));
		map.put("openid", openid);
		map.put("name", name);
		map.put("head_url", head_url);
		map.put("mobile", 0);
		return iuuMallDao.insertOpenid(map)==1;
	}
	
	/**
	 * 微信用户登录
	 * @author zhanglz
	 * @return 用户map
	 */
	@Override
	public Map<String, Object> WxLogin(String code){
		Map<String, Object> result = null;
		try {
			Map<String, Object> map = WxLoginUtil.getAccessToken(code);
			String openid = null==map.get("openid")?"":map.get("openid").toString();
			if(StringUtils.isNotEmpty(openid)){
				if(!haveOpenid(openid)){
					String access_token = null==map.get("access_token")?"":map.get("access_token").toString();
					Map<String, Object> userMap = WxLoginUtil.getUserinfo(access_token, openid);
					String nickname = null==userMap.get("nickname")?"":userMap.get("nickname").toString();
					String headimgurl = null==userMap.get("headimgurl")?"":userMap.get("headimgurl").toString();
//					nickname = nickname.replaceAll("\\", " ");
					String re="";
			    	for (int i = 0; i < nickname.length(); i++) {
			            char  item =  nickname.charAt(i);
			            if(isMessyCode(String.valueOf(item))){//如果是乱码
			            	continue;
			            }else{
			            	re+=item;
			            }
			            
			        }
			    	nickname=re;
					insertWx(openid, nickname, headimgurl);
				}
				result = iuuMallDao.findUser4Openid(openid);
			}else{
				log.info("WxLogin openid=null");
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.info("WxLogin error!!!");
		}
		return result;
	}
	
	public static boolean isMessyCode(String strName){  
        Pattern p = Pattern.compile("\\s*|\t*|\r*|\n*");  
        Matcher m = p.matcher(strName);  
        String after = m.replaceAll("");  
        String temp = after.replaceAll("\\p{P}", "");  
        char[] ch = temp.trim().toCharArray();  
        float chLength = 0 ;  
        float count = 0;  
        for (int i = 0; i < ch.length; i++) {  
            char c = ch[i];  
            if (!Character.isLetterOrDigit(c)) {  
                if (!isChinese(c)) {  
                    count = count + 1;  
                }  
                chLength++;   
            }  
        }  
        float result = count / chLength ;  
        if (result > 0.4) {  
            return true;  
        } else {  
            return false;  
        }  
    }
	
	private static boolean isChinese(char c) {  
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);  
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS  
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS  
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A  
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION  
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION  
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {  
            return true;  
        }  
        return false;  
    }  
	
	/**
	 * 插入QQ用户
	 * @author zhanglz
	 * @return 
	 */
	public boolean insertQQ(String openid,String name,String head_url){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("app", QQLoginUtil.qq_user_app);
		map.put("device_no", CommonUtil.createOrderNo("U", 5)+"-"+CommonUtil.createOrderNo("U", 5));
		map.put("openid", openid);
		map.put("name", name);
		map.put("head_url", head_url);
		map.put("mobile", 0);
		return iuuMallDao.insertOpenid(map)==1;
	}
	
	/**
	 * QQ用户登录
	 * @author zhanglz
	 * @return 用户map
	 */
	@Override
	public Map<String, Object> QQLogin(String code){
		Map<String, Object> result = null;
		try {
			String access_token = QQLoginUtil.getAccessToken(code);
			if(access_token!=null){
				String openId = QQLoginUtil.getOpenId(access_token);
				if(openId!=null){
					if(!haveOpenid(openId)){
						Map<String, Object> userMap = QQLoginUtil.getUserInfo(access_token, openId);
						String nickname = null==userMap.get("nickname")?"":userMap.get("nickname").toString();
						String headimgurl = null==userMap.get("figureurl_qq_1")?"":userMap.get("figureurl_qq_1").toString();
						insertQQ(openId, nickname, headimgurl);
					}
					result = iuuMallDao.findUser4Openid(openId);
				}else
					log.info("QQLogin openid=null");
			}else
				log.info("QQLogin access_token=null");
		} catch (Exception e) {
			e.printStackTrace();
			log.info("QQLogin error!!!");
		}
		return result;
	}
	
	/**
	 * 手机登录
	 * @author zhanglz
	 * @return 用户map
	 */
	@Override
	public Map<String, Object> mobileLogin(String mobile){
		Map<String, Object> map = null;
		if(haveMobile(mobile)){
			map = iuuMallDao.findUser4Mobile(mobile);
		}else{
			Map<String, Object> temp = new HashMap<String, Object>();
			temp.put("app", QQLoginUtil.qq_user_app);
			temp.put("device_no", CommonUtil.createOrderNo("U", 5)+"-"+CommonUtil.createOrderNo("U", 5));
			temp.put("openid", "");
			temp.put("name", "用户"+CommonUtil.createOrderNo("U", 3));
			temp.put("head_url", "");
			temp.put("mobile", mobile);
			if(iuuMallDao.insertOpenid(temp)==1){
				map = iuuMallDao.findUser4Mobile(mobile);
			}
		}
		return map;
	}
	
	/**
	 * 绑定手机
	 * @author zhanglz
	 */
	@Override
	public boolean binding(Integer userId,String mobile){
		if(userId==null||mobile.length()!=11){
			return false;
		}
		return iuuMallDao.binding(mobile, userId)==1;
	}
	
	
	@Override
	public boolean addShop(String infoId,String textureIds, String deviceNo, String num, String app){
		if(iuuMallDao.mergeShop(infoId, textureIds, deviceNo, app, Integer.parseInt(num))>0){
			return true;
		}else{
			String orderNo=CommonUtil.createOrderNo("U", 3);
			try {
				String sufFormat="xxx";
				//查询出精品会商品图片 
				String resultPath=this.iuuMallDao.queryPreUrl(infoId,textureIds);
				System.err.println("resultPath:"+resultPath);
				int result=this.iNewShopCartDao.createShop(resultPath,infoId,textureIds,deviceNo,app,orderNo,sufFormat,num);
				if(result>0){
					return true;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	
	@Override
	public int getShopNum(String deviceNo, String app){
		return iuuMallDao.getShopNum(deviceNo, app);
	}
	
	@Override
	public String youHomeNav()throws Exception {
		String cacheKey=OCSKey.YOU_NAV;
		Map<String,Object>paramMap = new HashMap<String,Object>();
		paramMap.put("cacheKey",cacheKey);
		paramMap.put("cacheTime",super.FOREVER);
		return this.iuuMallDao.youNav(paramMap);
	}

	@Override
	public String youSpecial(int nav_id) throws Exception {
		String key=OCSKey.YOU_SPECIAL;
		String cacheKey=key+nav_id;
		Map<String,Object>paramMap = new HashMap<String,Object>();
		paramMap.put("cacheKey",cacheKey);
		paramMap.put("cacheTime",super.FOREVER);
		paramMap.put("nav_id",nav_id);
		paramMap.put("dgurl", dgurl);
		return this.iuuMallDao.youSpecial(paramMap);
	}
	
	@Override
	public String youStrategy(String specialId)throws Exception{
		String key=OCSKey.YOU_STRATEGY;
		String cacheKey=key+specialId;
		Map<String,Object>paramMap = new HashMap<String,Object>();
		paramMap.put("cacheTime",super.FOREVER);
		paramMap.put("cacheKey",cacheKey);
		paramMap.put("specialId",specialId);
		paramMap.put("dgurl", dgurl);
		return this.iuuMallDao.youStrategy(paramMap);
	}
	
	@Override
	public int homeOrderNum(String deviceNo, String app){
		return iuuMallDao.homeOrderNum(deviceNo, app);
	}
	
	@Override
	public String homeIcoNum(String deviceNo, String app) throws Exception{
		int shopNum = getShopNum(deviceNo, app);
		int orderNum = homeOrderNum(deviceNo, app);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("shopNum", shopNum);
		map.put("orderNum", orderNum);
		String json = new ObjectMapper().writeValueAsString(map);
		return json;
	}
	
	@Override
	public Map<String, Object> getOrder4No(String order_no){
		return iuuMallDao.getOrder4No(order_no);
	}
	
	@Override
	public List<Map<String, Object>> shopList(String deviceNo, String app) throws Exception{
		List<Map<String, Object>> list=this.iuuMallDao.queryShopList(deviceNo,app);
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
			return list;
		}
		return null;
	}
	
	@Override
	public List<Map<String, Object>> getOrderComm(String order_no){
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		Map<String, Object> map = iuuMallDao.getOrderComm(order_no);
		if(map.isEmpty()){
			return null;
		}
		String info_ids = null==map.get("info_ids")?"":map.get("info_ids").toString();
		String img_url = null==map.get("img_url")?"":map.get("img_url").toString();
		String file_type = null==map.get("file_type")?"":map.get("file_type").toString();
		String infoArr[] = info_ids.split(",");
		String imgArr[] = img_url.split(",");
		String fileArr[] = file_type.split(",");
		for (int i = 0; i < infoArr.length; i++) {
			Map<String, Object> temp = new HashMap<String, Object>();
			temp.put("info_id", infoArr[i]);
			temp.put("img_url", dgurl+imgArr[i]);
			if(fileArr[i].equals("xxx"))
				temp.put("file_type", "");
			else
				temp.put("file_type", fileArr[i]);
			list.add(temp);
		}
		return list;
	}
	
	/**
	 * 购物车结算
	 * @author zhanglz
	 */
	@Override
	public List<Map<String, Object>> shopBuy(String shopNo,String deviceNo,String app){
		String shopArr[] = shopNo.split(",");
		String shopNos = "(";
		for (int i = 0; i < shopArr.length; i++) {
			shopNos+="'"+shopArr[i]+"',";
		}
		shopNos = shopNos.substring(0,shopNos.length()-1)+")";
		List<Map<String, Object>> list = iuuMallDao.shopBuy(shopNos,deviceNo,app);
		System.err.println("size:"+list.size());
		for (Map<String, Object> map : list) {
			map.put("img_url", dgurl+map.get("img_url"));
		}
		return list;
	}
	
	/**
	 * 保存评价
	 * @author zhanglz
	 */
	@Override
	public boolean saveComment(String orderNo,String info_id,String commentArea,String starNum,String deviceNo){
		try {
			Map<String,Object> map = iEvaluationDao.getMobile4Order(orderNo);
			String mobile = null==map.get("mobile")?"":map.get("mobile").toString();
			String texture_names = null==map.get("texture_names")?"":map.get("texture_names").toString();
			String[] textureArr = texture_names.split("\\|");
			
			String[] infoArr = info_id.split(",");
			String[] contentArr = commentArea.split(",");
			String[] starArr = starNum.split(",");
			for (int i = 0; i < infoArr.length; i++) {
				int star = Integer.parseInt(starArr[i]);
				int evalType = 1;
				if(star==1) evalType=3;
				else if(star==2||star==3) evalType=2;
				else evalType=1;
				String text = "";
				if(textureArr.length>i){
					text = textureArr[i];
				}
				iuuMallDao.saveComment(infoArr[i], evalType, contentArr[i], deviceNo, mobile, text);
			}
			iEvaluationDao.updateEvaStatus(orderNo);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	@Override
	public List<Map<String, Object>> getAdress(String deviceNo, String app){
		return iuuMallDao.queryAddress(deviceNo, app);
	}
	
	@Override
	public List<Map<String, Object>> getCoupon(String deviceNo, String app){
		List<Map<String, Object>> list = iCouponDao.queryAvaiCoupon(deviceNo,app);
		if(list==null)
			return null;
		for (Map<String, Object> map : list) {
			BigDecimal des = (BigDecimal)map.get("desPrice");
			BigDecimal org = (BigDecimal)map.get("orgPrice");
			map.put("desPrice", des.intValue());
			map.put("orgPrice", org.intValue());
		}
		List<Map<String, Object>> result = new ArrayList<Map<String,Object>>();
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> map = list.get(i);
			int level = (int)map.get("level");
			if(level==8||level==12||level==13){
				continue;
			}
			result.add(map);
		}
		return result;
	}
	
	/**
	 * 立即购买
	 * @author zhanglz
	 */
	@Override
	public String createOrder(String infoId, String textureIds, String deviceNo, String app,String num) throws Exception{
		Map<String, Object> resultMap=new HashMap<>();
		String orderNo=CommonUtil.createOrderNo("U", 5);
		String resultPath=null;
		try {
			String sufFormat="xxx";
			//查询出精品会商品图片  (预览图足够)
			resultPath=this.iNewOrderDao.queryPreUrl(infoId,textureIds);
			//插入到订单表
			Map<String, Object> returnMap=this.iNewOrderDao.createOrder(resultPath,infoId,textureIds,deviceNo,app,orderNo,sufFormat,num);
			Integer isBoutique = iuuMallDao.isBoutique(infoId);
			if(resultMap!=null){
				resultMap.put("orderNo", orderNo);
				resultMap.put("sufFormat", sufFormat);
				resultMap.put("imgUrl", diyRootPath+resultPath);
				resultMap.put("num", num);
				resultMap.put("name", returnMap.get("name"));
				resultMap.put("textureName", returnMap.get("textureNames"));
				resultMap.put("nowPrice", returnMap.get("orgPrice"));
				resultMap.put("isBoutique", isBoutique);
				return mapper.writeValueAsString(resultMap);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 立即购买付款
	 * @author zhanglz
	 */
	@Override
	public String formOrderOne(String deviceNo, String app, String couponId, String orderNo, String addressId,
			String num, String payId,String remark) throws Exception {
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
			
//			/** 定制商品第二件半价  **/
//			int isBoutique = iuuMallDao.isBoutique(infoIdss[0]);
//			int n = Integer.parseInt(num);
//			if(isBoutique==2&&n/2>=1){
//				totalFee-=((price/2)*(n/2));
//			}
			
			//处理满减优惠
			/*String json=this.iNewOrderService.getPrivilege();
			List<Map<String, Object>> privilege=mapper.readValue(json, new TypeReference<List<Map<String,Object>>>() {});
			for (Map<String, Object> map : privilege) {
				orgPrice=(Double)map.get("orgPrice");
				desPrice=(Double)map.get("desPrice");
				if(totalFee>=orgPrice){
					totalFee=totalFee-desPrice;
					break;
				}
			}*/
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
				double totalFee_=this.iuuMallDao.activeOrder(orderNo,couponId,addressId,num,payId,totalFee,orgPrice,desPrice,deviceNo,app,addressMap,remark);
				if("2".equals(payId)){//微信
					//请求微信生成prepayId
					app="com.shua.h5";
					//String openid = iuuMallDao.getOpenId(deviceNo, app);
					Map<String, Object> paraMap= WxPayUtil.H5unifiedorder(totalFee_, orderNo,getWxPay(app,"key"),getWxPay(app,"appid"),getWxPay(app, "appIDS"),getWxPay(app, "mchId"),iuuMallDao.getOpenId(deviceNo, app));
					//WxPayUtil.H5unifiedorder(totalFee_, orderNo,getWxPay(app,"key"),getWxPay(app,"appid"),getWxPay(app, "appIDS"),getWxPay(app, "mchId"),iuuMallDao.getOpenId(deviceNo, app));
					String prepayId=(String)paraMap.get("prepayId");
					String payNo=(String)paraMap.get("payNo");
					if(StringUtils.isBlank(prepayId)){
						return null;
					}
					//将prepayid payNo绑定到订单  到支付信息表
					int result=this.iNewOrderDao.boundOrderPrepay(orderNo,prepayId,payNo);
					if(result>0){
						Map<String, Object> h5Map = WxPayUtil.returnH5Pay("wx204459d3f9148e3b", "54dfs2u32016hrfhasklijfhgdfgsdkl", prepayId);//H5支付返回给页面的参数
						resultMap.put("prepayId", prepayId);
						resultMap.putAll(h5Map);
					}
				}else if("1".equals(payId)){
					
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
	 * 购物车生成订单
	 * @author zhanglz
	 */
	@Override
	public String createOrderByShops(String deviceNo, String app, String shopNos,String payId,String addressId,String couponId,String remark) throws Exception {
		String orderNo=CommonUtil.createOrderNo("U", 5);
		//查询多个购物车信息
		String[] shopNoss=shopNos.split(",");
		String shopNo="(";
		for (int i = 0; i < shopNoss.length; i++) {
			shopNo+=("'"+shopNoss[i]+"',");
		}
		shopNo=shopNo.substring(0,shopNo.length()-1);
		shopNo+=")";
		List<Map<String, Object>> list=this.iuuMallDao.getShopInfoByShopNos(shopNo);
		String shopNo_="",infoId_="",textureIds_="",temTextureName_="",userId_="",imgUrl_="",fileType_="",num_="",name_="",nowPrice_="",transportfee_="",isBoutique_="";
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
			
			isBoutique_+=String.valueOf(map.get("isBoutique"))+",";
		}
		
//		/** 定制商品第二件半价  **/
//		Map<String, Integer> tempNum = new HashMap<>();
//		Map<String, String> tempPrice = new HashMap<>();
//		String[] infoArr = CommonUtil.subStr(infoId_).split(",");
//		String[] numArr = CommonUtil.subStr(num_).split(",");
//		String[] isBoutiqueArr = CommonUtil.subStr(isBoutique_).split(",");
//		String[] nowPriceArr = CommonUtil.subStr(nowPrice_).split(",");
//		for (int i = 0; i < infoArr.length; i++) {
//			if("2".equals(isBoutiqueArr[i])){
//				Integer value=tempNum.get(infoArr[i]);
//				if(value==null || value==0){
//					tempNum.put(infoArr[i], Integer.parseInt(numArr[i]));
//					tempPrice.put(infoArr[i], nowPriceArr[i]);
//				}else
//					tempNum.put(infoArr[i], value+Integer.parseInt(numArr[i]));
//			}
//		}
//		if(!tempNum.isEmpty()){
//			for (String key : tempNum.keySet()) {
//				Integer num = tempNum.get(key);
//				if(num/2>=1){
//					double price = Double.parseDouble(tempPrice.get(key));
//					totalFee-=((price/2)*(num/2));
//				}
//			}
//		}
		
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
			/*String json=this.iNewOrderService.getPrivilege();
			List<Map<String, Object>> privilege=mapper.readValue(json, new TypeReference<List<Map<String,Object>>>() {});
			for (Map<String, Object> map : privilege) {
				orgPrice=(Double)map.get("orgPrice");
				desPrice=(Double)map.get("desPrice");
				if(totalFee>=orgPrice){
					totalFee=totalFee-desPrice;
					break;
				}
			}*/
			//检验地址信息
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
			//生成订单
			double totalFee_=this.iuuMallDao.createOrderByShops(orderNo,CommonUtil.subStr(shopNo_),CommonUtil.subStr(infoId_),CommonUtil.subStr(textureIds_),CommonUtil.subStr(temTextureName_),CommonUtil.subStr(userId_),CommonUtil.subStr(imgUrl_),CommonUtil.subStr(fileType_),CommonUtil.subStr(num_),CommonUtil.subStr(name_),CommonUtil.subStr(nowPrice_),payId,orgPrice,desPrice,couponId,totalFee,addressMap,deviceNo,app,CommonUtil.subStr(transportfee_),remark);
			Map<String, Object> resultMap=new HashMap<>();
			this.iNewOrderDao.updateShopOrderNum(deviceNo, app, "order", 1);
			if("2".equals(payId)){//微信
				//请求微信生成prepayId
				app="com.shua.h5";
				Map<String, Object> paraMap= WxPayUtil.H5unifiedorder(totalFee_, orderNo,getWxPay(app,"key"),getWxPay(app,"appid"),getWxPay(app, "appIDS"),getWxPay(app, "mchId"),iuuMallDao.getOpenId(deviceNo, app));
				String prepayId=(String)paraMap.get("prepayId");
				String payNo=(String)paraMap.get("payNo");
				if(StringUtils.isBlank(prepayId)){
					return null;
				}
				//将prepayid payNo绑定到订单  到支付信息表
				int result3=this.iNewOrderDao.boundOrderPrepay(orderNo,prepayId,payNo);
				if(result3>0){
					Map<String, Object> h5Map = WxPayUtil.returnH5Pay(getWxPay(app,"appIDS"), getWxPay(app,"key"), prepayId);//H5支付返回给页面的参数
					resultMap.put("prepayId", prepayId);
					resultMap.putAll(h5Map);
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
	 * 订单列表立即支付
	 * @author zhanglz
	 */
	@Override
	public Map<String, Object> confirmOrder(String orderNo, String deviceNo, String app) throws Exception{
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
		Map<String, Object> map = new HashMap<String, Object>();
		//如果是微信  验证prepayId是否过期  过期重新生成
		if("2".equals(payType)){
			app="com.shua.h5";
			if(time==null||time>118){
				Map<String,Object> mapres=WxPayUtil.H5unifiedorder(feeTotal, orderNo,getWxPay(app, "key"),getWxPay(app, "appid"),getWxPay(app, "appIDS"),getWxPay(app, "mchId"), iuuMallDao.getOpenId(deviceNo, app));
				prepayId=(String)mapres.get("prepayId");
				int result=this.iNewOrderDao.boundPrepayNo(orderNo,(String)mapres.get("prepayId"),(String)mapres.get("payNo"));
				if(result>0){
					Map<String, Object> h5Map = WxPayUtil.returnH5Pay(getWxPay(app,"appIDS"), getWxPay(app,"key"), prepayId);//H5支付返回给页面的参数
					map.put("prepayId", prepayId);
					map.putAll(h5Map);
				}
			}else{
				Map<String, Object> h5Map = WxPayUtil.returnH5Pay(getWxPay(app,"appIDS"), getWxPay(app,"key"), prepayId);//H5支付返回给页面的参数
				map.put("prepayId", prepayId);
				map.putAll(h5Map);
			}
		}
		map.put("orderNo", orderNo);
		map.put("totalFee", df.format(feeTotal));
		map.put("payType", payType);
		if(flag){
			return map;
		}else{
			return null;
		}
	}
	
	
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
	
	/*
	 * 获取微信配置
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
	
	
	/**
	 * 获取微信jsapi_ticket
	 * @return
	 * @throws Exception
	 */
	public String getJsapi_ticket() throws Exception{
		String access_token_ocs = OCSKey.YOU_ACCESSTOKEN;
		String jsapi_ticket_ocs = OCSKey.YOU_JSAPITICKET;
		String access_token=this.ocsDao.query(access_token_ocs);
		if(StringUtils.isBlank(access_token)){
			Map<String, Object> aMap = WxLoginUtil.shareAccessToken();
			access_token = aMap.get("access_token")==null?"":aMap.get("access_token").toString();
			ocsDao.insert(access_token_ocs,access_token,110);
		}
		String jsapi_ticket=this.ocsDao.query(jsapi_ticket_ocs);
		if(StringUtils.isBlank(jsapi_ticket)){
			Map<String, Object> aMap = WxLoginUtil.shareTicket(access_token);
			jsapi_ticket = aMap.get("ticket")==null?"":aMap.get("ticket").toString();
			ocsDao.insert(jsapi_ticket_ocs,jsapi_ticket,110);
		}
		return jsapi_ticket;
	}
	
	@Override
	public Map<String, Object> goodsShare(String id) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("noncestr", CommonUtil.createOrderNo("0",18));
		map.put("jsapi_ticket", getJsapi_ticket());
		map.put("timestamp", Long.toString(System.currentTimeMillis()));
		map.put("url", "http://test.diy.51app.cn/diyMall2/UGoods/goodsInfo.do?id="+id);
		String sign = getSign(map);
		map.put("sign", sign);
		map.put("appId", WxLoginUtil.appId);
		return map;
	}
	
	@Override
	public Map<String, Object> homeShare(String url) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("noncestr", CommonUtil.createOrderNo("0",18));
		map.put("jsapi_ticket", getJsapi_ticket());
		map.put("timestamp", Long.toString(System.currentTimeMillis()));
		map.put("url", "http://test.diy.51app.cn"+url);
		String sign = getSign(map);
		map.put("sign", sign);
		map.put("appId", WxLoginUtil.appId);
		return map;
	}
	
	@Override
	public Map<String, Object> returnShare() throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("noncestr", CommonUtil.createOrderNo("0",18));
		map.put("jsapi_ticket", getJsapi_ticket());
		map.put("timestamp", Long.toString(System.currentTimeMillis()));
		map.put("url", "http://test.diy.51app.cn/diyMall2/UGoods/toShare.do");
		String sign = getSign(map);
		map.put("sign", sign);
		map.put("appId", WxLoginUtil.appId);
		return map;
	}
	
	public static String getSign(Map<String,Object> map){
        ArrayList<String> list = new ArrayList<String>();
        for(Map.Entry<String,Object> entry:map.entrySet()){
            if(entry.getValue()!=""){
                list.add(entry.getKey() + "=" + entry.getValue() + "&");
            }
        }
        int size = list.size();
        String [] arrayToSort = list.toArray(new String[size]);
        Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < size; i ++) {
            sb.append(arrayToSort[i]);
        }
        String result = sb.toString();
        result = result.substring(0, result.length()-1);
        result = DigestUtils.sha1Hex(result);
        return result;
    }
	
	@Override
	public String getOrderList(String deviceNo, String app,String page,String state) throws Exception{
		List<Map<String, Object>> result=new ArrayList<>();
		Map<String, Object> tempMap=null;
		if(StringUtils.isBlank(page)){
			page="0";
		}
		List<Map<String, Object>> list=this.iuuMallDao.getOrderList(deviceNo,app,Integer.parseInt(page),Integer.parseInt(orderNumer),Integer.parseInt(state));
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
}
