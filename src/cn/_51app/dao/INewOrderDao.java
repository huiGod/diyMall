package cn._51app.dao;

import java.util.List;
import java.util.Map;

public interface INewOrderDao {

	/**
	 * tengh 2016年5月17日 下午2:57:51
	 * @param resultPath
	 * @param infoId
	 * @param textureIds
	 * @param deviceNumber
	 * @param app
	 * @param sys
	 * @param orderNo 
	 * @param sufFormat 
	 * @param num 
	 * TODO 生成未激活的订单
	 */ 
	Map<String, Object> createOrder(String resultPath, String infoId, String textureIds, String deviceNumber, String app,  String orderNo, String sufFormat, String num) throws Exception;

	/**
	 * tengh 2016年5月17日 下午5:02:55
	 * @param deviceNumber
	 * @param app  
	 * @param flag   shop购物车  order订单
	 * @param num 数量 正负数
	 * TODO 更新购物车或者订单数量 
	 */
	int updateShopOrderNum(String deviceNumber, String app, String flag, int num);

	/**
	 * tengh 2016年5月17日 下午9:12:16
	 * @return
	 * TODO 获取满减优惠信息
	 */
	List<Map<String, Object>> queryPrivilege();

	/**
	 * tengh 2016年5月18日 上午11:01:49
	 * @param orderNo
	 * @param deviceNo
	 * @param app
	 * @return
	 * TODO 查询订单信息
	 */
	Map<String, Object> queryOrderInfo(String orderNo, String deviceNo, String app);

	/**
	 * tengh 2016年5月18日 上午11:39:13
	 * @param orderNo 
	 * @param couponId 
	 * @param addressId 
	 * @param num 
	 * @param payId 
	 * @param totalFee 
	 * @param orgPrice 
	 * @param desPrice 
	 * @param deviceNo 
	 * @param app 
	 * @param addressMap 
	 * @return
	 * TODO 绑定订单信息并支付
	 */
	double activeOrder(String orderNo, String couponId, String addressId, String num, String payId, double totalFee, double orgPrice, double desPrice, String deviceNo, String app, Map<String, Object> addressMap);

	/**
	 * tengh 2016年5月18日 下午4:33:14
	 * @param orderNo
	 * @param prepayId
	 * @param payNo
	 * TODO 将支付信息绑定到订单
	 * @return 
	 */
	int boundOrderPrepay(String orderNo, String prepayId, String payNo);

	/**
	 * tengh 2016年5月18日 下午6:13:25
	 * @param orderNo
	 * @param deviceNo
	 * @param app
	 * @return
	 * TODO 查询订单是否支付成功
	 */
	boolean checkOrder(String orderNo, String deviceNo, String app);

	/**
	 * tengh 2016年5月18日 下午7:29:45
	 * @param tempTextureId
	 * @return
	 * TODO 查询材质名称  {1,2}
	 */
	String queryTextureById(String[] tempTextureId);

	/**
	 * tengh 2016年5月18日 下午8:34:21
	 * @param deviceNo
	 * @param app
	 * @param number 
	 * @param page 
	 * @return
	 * TODO 查询订单列表
	 */
	List<Map<String, Object>> getOrderList(String deviceNo, String app, int page, int number,Integer state);

	/**
	 * tengh 2016年5月19日 上午9:49:09
	 * @param orderNo
	 * @param deviceNo
	 * @param app
	 * @return 查询订单信息   和 对应商品数量
	 * TODO
	 */
	Map<String, Object> confirmOrderNumAndPay(String orderNo, String deviceNo, String app);

	/**
	 * tengh 2016年5月19日 上午10:32:30
	 * @param num
	 * @param infoid
	 * @return
	 * TODO
	 */
	boolean checkOrderNum(Integer num, String infoid);

	/**
	 * tengh 2016年5月19日 上午11:18:00
	 * @param orderNo
	 * @param prepayId
	 * @param payNo
	 * TODO 
	 * @return 
	 */
	int boundPrepayNo(String orderNo, String prepayId, String payNo);

	/**
	 * tengh 2016年5月19日 下午1:58:17
	 * @param orderNo
	 * @param deviceNo
	 * @param app
	 * @param flag
	 * @return
	 * TODO
	 */
	boolean updateOrder(String orderNo, String deviceNo, String app, String flag);

	/**
	 * tengh 2016年5月19日 下午2:59:09
	 * @param infoId
	 * @return
	 * TODO 获取精品会预览图
	 */
	String queryImgUrl(String infoId);

	/**
	 * tengh 2016年5月19日 下午7:38:33
	 * @param deviceNo
	 * @param app
	 * @param shopNo
	 * @return
	 * TODO 删除购物车
	 */
	int deleteShop(String deviceNo, String app, String shopNo);

	/**
	 * tengh 2016年5月20日 下午7:00:04
	 * @param deviceNo
	 * @param app
	 * @param deviceToken
	 * @return
	 * TODO 插入用户
	 */
	boolean recordUser(String deviceNo, String app, String deviceToken);

	/**
	 * tengh 2016年5月25日 下午12:01:03
	 * @param out_trade_no
	 * @return
	 * TODO 查询订单详情
	 */
	Map<String, Object> queryOrderInfo(String out_trade_no);

	/**
	 * zhanglz 
	 * @param infoId
	 * @param textureIds
	 * @return
	 * TODO 获取精品会预览图
	 */
	String queryPreUrl(String infoId, String textureIds);

	Map<String, Object> queryOrders(String out_trade_no);

	void separateOrder(Map<String, Object> paramMap);

	void update0status(String order_no);

	/**
	 * tengh 2016年6月7日 下午1:45:14
	 * @param deviceNo
	 * @param app
	 * @param version
	 * @param deviceToken 
	 * @return
	 * TODO 绑定用户信息
	 */
	boolean infoUser(String deviceNo, String app, String version, String deviceToken);

	/**
	 * tengh 2016年6月7日 下午3:16:58
	 * @param appid
	 * @return
	 * TODO 获取微信配置
	 */
	Map<String, Object> getWxPay(String appid);

	/**
	 * tengh 2016年6月7日 下午5:41:01
	 * @param appid
	 * @return
	 * TODO 获取微信配置
	 */
	Map<String, Object> getWxPayByAppId(String appid);
}
