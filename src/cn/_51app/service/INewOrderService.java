package cn._51app.service;

import org.springframework.web.multipart.MultipartFile;

public interface INewOrderService {

	/**
	 * tengh 2016年5月16日 下午8:45:46
	 * @param imgFile
	 * @param imgBackFile
	 * @param previewFile
	 * @param previewBackFile 
	 * @param paramStr
	 * @param deviceNumber
	 * @param app
	 * @param sys
	 * @param num 
	 * @param sys2 
	 * @return
	 * TODO 直接生成订单  （未激活状态 -1）
	 */
	String createOrder(MultipartFile imgFile, MultipartFile imgBackFile, MultipartFile previewFile, MultipartFile previewBackFile, String infoId,
			String textureIds,String deviceNumber, String app, String num) throws Exception;

	/**
	 * tengh 2016年5月17日 下午9:10:18
	 * @return
	 * TODO 查询满减优惠信息
	 */
	String getPrivilege() throws Exception;

	/**
	 * tengh 2016年5月18日 上午10:19:41
	 * @param deviceNo
	 * @param app
	 * @param couponId
	 * @param orderNo
	 * @param addressId
	 * @param num
	 * @param payId
	 * @return
	 * TODO 激活订单
	 */
	String activeOrder(String deviceNo, String app, String couponId, String orderNo, String addressId, String num,
			String payId) throws Exception;

	/**
	 * tengh 2016年5月18日 下午4:56:41
	 * @param orderNo
	 * @param app 
	 * @param deviceNo 
	 * @return
	 * TODO 查询单个订单详细信息
	 */
	String getOrderInfo(String orderNo, String deviceNo, String app) throws Exception;

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
	 * tengh 2016年5月18日 下午8:28:39
	 * @param deviceNo
	 * @param app
	 * @param page 
	 * @return
	 * TODO 查询订单列表
	 */
	String getOrderList(String deviceNo, String app, String page,String state) throws Exception;

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
	 * tengh 2016年6月7日 下午1:44:26
	 * @param deviceNo
	 * @param app
	 * @param version
	 * @param deviceToken 
	 * @return
	 * TODO 统计用户信息
	 */
	boolean infoUser(String deviceNo, String app, String version, String deviceToken);
	
}
