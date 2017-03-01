package cn._51app.service.diy2_0;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;

public interface IndentService {

	/**
	 * 创建-1的订单
	 * @param imgFile
	 * @param imgOrderFile
	 * @param imgFile2
	 * @param imgOrderFile2
	 * @param infoId
	 * @param textureIds
	 * @param deviceNo
	 * @param app
	 * @param num
	 * @param req
	 * @param wallpaper
	 * @param isBoutique
	 * @return
	 * @throws Exception
	 */
	String createOrder(MultipartFile imgFile, MultipartFile imgOrderFile, MultipartFile imgFile2,
			MultipartFile imgOrderFile2, String infoId, String textureIds, String deviceNo, String app, String num,
			HttpServletRequest req,String wallpaper,String isBoutique) throws Exception;

	/**
	 *TODO  获取优惠券信息
	 * @return
	 * @throws Exception
	 */
	String getPrivilege() throws Exception;

	/**
	 * TODO 激活状态-1的订单 绑定支付信息 优惠 数量 地址
	 * @param deviceNo
	 * @param app
	 * @param couponId
	 * @param orderNo
	 * @param addressId
	 * @param num
	 * @param payId  
	 * @param activityId   活动id
	 * @param merchant
	 * @return
	 * @throws Exception
	 */
	String activeOrder(String deviceNo, String app, String couponId, String orderNo, String addressId, String num,
			String payId) throws Exception;

	/**
	 * TODO 查询单个订单详情
	 * @param orderNo
	 * @param deviceNo
	 * @param app
	 * @return
	 * @throws Exception
	 */
	String getOrderInfo(String orderNo, String deviceNo, String app) throws Exception;

	/**
	 * tengh 2016年5月18日 下午8:28:39
	 * @param deviceNo
	 * @param app
	 * @param page 
	 * @return
	 * TODO 查询订单列表
	 */
	String getOrderList(String deviceNo, String app, String page,String state) throws Exception;

	/**
	 * tengh 2016年5月18日 下午6:12:48
	 * @param orderNo
	 * @param deviceNo
	 * @param app
	 * @return
	 * TODO 查询订单是否支付成功
	 */
	boolean checkOrder(String orderNo, String deviceNo, String app);

	/**
	 * tengh 2016年5月19日 上午9:38:59
	 * @param orderNo
	 * @param deviceNo
	 * @param app
	 * @param appid 
	 * @return
	 * TODO 立即支付 (验证库存    微信支付看是prepayId否过期)
	 */
	String confirmOrder(String orderNo, String deviceNo, String app) throws Exception;

	/**
	 * tengh 2016年5月19日 下午1:52:09
	 * @param orderNo
	 * @param deviceNo
	 * @param app
	 * @param flag
	 * @return
	 * TODO 订单状态改变
	 */
	boolean updateOrder(String orderNo, String deviceNo, String app, String flag);

	/**
	 * tengh 2016年5月20日 下午6:58:25
	 * @param deviceNo
	 * @param app
	 * @param deviceToken
	 * @return
	 * TODO 插入新用户记录
	 */
	boolean recordUser(String deviceNo, String app, String deviceToken);

	/**
	 * TODO 计算优惠券
	 * @param deviceNo
	 * @param app
	 * @param couponIds  优惠券ids多个
	 * @param totalFee  实际价格，不算运费
	 * @return
	 */
	Double privilege(String deviceNo, String app, String couponIds, Double totalFee);

	/**
	 * 创建-1的订单（id版）
	 * @param imgFile
	 * @param imgOrderFile
	 * @param imgFile2
	 * @param imgOrderFile2
	 * @param infoId
	 * @param textureIds
	 * @param userId
	 * @param num
	 * @param req
	 * @param wallpaper
	 * @param isBoutique
	 * @param keycode 
	 * @param isSave 
	 * @return
	 * @throws Exception
	 */
	String createOrderForId(MultipartFile imgFile, MultipartFile imgOrderFile, MultipartFile imgFile2,
			MultipartFile imgOrderFile2, String infoId, String textureIds, String userId, String num,
			HttpServletRequest req, String wallpaper, String isBoutique,String lettering,String modId, String keycode, String isSave) throws Exception;

	/**
	 * TODO 计算优惠券(id版)
	 * @param userId
	 * @param couponIds
	 * @param totalFee
	 * @return
	 */
	Double privilegeForId(String userId, String couponIds, Double totalFee);

	/**
	 * TODO 激活状态-1的订单 绑定支付信息 优惠 数量 地址（id版）
	 * @param userId
	 * @param couponIds
	 * @param orderNo
	 * @param addressId
	 * @param num
	 * @param payId
	 * @param app
	 * @param message 
	 * @return
	 * @throws Exception
	 */
	String activeOrderForId(String userId, String couponIds, String orderNo, String addressId, String num, String payId,
			String app, String message) throws Exception;

	/**
	 * TODO 查询单个订单详情(id版)
	 * @param orderNo
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	String getOrderInfoForId(String orderNo, String userId) throws Exception;

	/**
	 * TODO getOrderList(id版)
	 * @param userId
	 * @param page
	 * @param state
	 * @return
	 * @throws Exception
	 */
	String getOrderListForId(String userId, String page, String state) throws Exception;

	/**
	 * TODO 查询订单是否支付成功(id版)
	 * @param orderNo
	 * @param userId
	 * @return
	 */
	boolean checkOrderForId(String orderNo, String userId);

	/**
	 * TODO 立即支付 (验证库存    微信支付看是prepayId否过期)(id版)
	 * @param orderNo
	 * @param userId
	 * @param app
	 * @return
	 * @throws Exception
	 */
	String confirmOrderForId(String orderNo, String userId, String app) throws Exception;

	/**
	 * TODO 订单状态改变(id版)
	 * @param orderNo
	 * @param userId
	 * @param flag
	 * @return
	 */
	boolean updateOrderForId(String orderNo, String userId, String flag);

	/**
	 * TODO 插入新用户记录(id版)
	 * @param userId
	 * @param deviceToken
	 * @return
	 */
	boolean recordUserForId(String userId, String deviceToken);

	/**
	 * tengh 2016年12月6日 下午8:54:58
	 * @param shopNo
	 * @param userId
	 * @param textureIds
	 * @return
	 * TODO 修改材质
	 */
	boolean changeTexture(String shopNo, String userId, String textureIds);

	/**
	 * tengh 2016年12月23日 上午11:55:52
	 * @param keycode
	 * @param imgFile
	 * @return
	 * TODO 单个文件上传
	 */
	boolean uploadFile(String keycode, MultipartFile imgFile);
}
