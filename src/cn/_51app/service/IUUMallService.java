package cn._51app.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

public interface IUUMallService {

	boolean haveOpenid(String openid);

	boolean haveMobile(String mobile);
	
	Map<String, Object> WxLogin(String code);

	Map<String, Object> mobileLogin(String mobile);

	boolean sendMsg(String phone, String smscode) throws Exception;

	boolean binding(Integer userId, String mobile);

	boolean addShop(String infoId, String textureIds, String deviceNo,
			String num, String app);

	int getShopNum(String deviceNo, String app);

	Map<String, Object> QQLogin(String code);

	String youHomeNav()throws Exception;
	
	String youSpecial(int id)throws Exception;

	int homeOrderNum(String deviceNo, String app);

	String homeIcoNum(String deviceNo, String app) throws Exception;

	Map<String, Object> getOrder4No(String order_no);

	List<Map<String, Object>> shopList(String deviceNo, String app)
			throws Exception;

	List<Map<String, Object>> getOrderComm(String order_no);

	List<Map<String, Object>> shopBuy(String shopNo, String deviceNo, String app);

	boolean saveComment(String orderNo, String info_id, String commentArea,
			String starNum, String deviceNo);

	List<Map<String, Object>> getAdress(String deviceNo, String app);

	List<Map<String, Object>> getCoupon(String deviceNo, String app);

	String formOrderOne(String deviceNo, String app, String couponId,
			String orderNo, String addressId, String num, String payId,String remark)
			throws Exception;

	String youStrategy(String specialId) throws Exception;

	String createOrderByShops(String deviceNo, String app, String shopNos,
			String payId, String addressId, String couponId, String remark) throws Exception;

	Map<String, Object> confirmOrder(String orderNo, String deviceNo, String app)
			throws Exception;

	Map<String, Object> returnShare() throws Exception;

	Map<String, Object> goodsShare(String id) throws Exception;

	String getOrderList(String deviceNo, String app, String page, String state)
			throws Exception;

	String createOrder(String infoId, String textureIds, String deviceNo,
			String app, String num) throws Exception;

	Map<String, Object> homeShare(String url) throws Exception;

}
