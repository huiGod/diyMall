package cn._51app.dao.diy2_0;

import java.util.List;
import java.util.Map;

public interface IndentDao {

	/**
	 * TODO 创建立即付款订单
	 * @param resultPath
	 * @param infoId
	 * @param textureIds
	 * @param deviceNo
	 * @param app
	 * @param orderNo
	 * @param sufFormat
	 * @param num
	 * @param customize
	 * @param transportfee
	 * @return
	 */
	Map<String,Object>createOrder(String resultPath,String infoId,String textureIds,String deviceNo,String app,String orderNo,String sufFormat,String num,boolean customize,String workId);

	/**
	 * TODO 查询材质
	 * @param tempTextureId
	 * @return
	 */
	String queryTextureById(String[] tempTextureId);

	/**
	 * TODO 查询精品汇商品图片
	 * @param infoId
	 * @param textureIds
	 * @return
	 */
	String queryPreUrl(String infoId, String textureIds);

	/**
	 * TODO 查询订单信息以验证
	 * @param orderNo
	 * @param deviceNo
	 * @param app
	 * @return
	 */
	Map<String, Object> confirmOrderNumAndPay(String orderNo, String deviceNo, String app);

	/**
	 * TODO 查看库存是否充足，分表
	 * @return
	 */
	boolean checkOrderNum(Integer num,String infoid);

	/**
	 *  TODO 查询价格
	 * @param orderNo
	 * @param deviceNo
	 * @param app
	 * @return
	 */
	Map<String, Object> queryOrderInfo(String orderNo, String deviceNo, String app);

	/**
	 * TODO 查询优惠券
	 * @return
	 */
	List<Map<String, Object>> queryPrivilege();

	/**
	 * TODO 绑定订单信息并支付
	 * @param orderNo
	 * @param addressId
	 * @param num
	 * @param payId
	 * @param totalFee
	 * @param couponMoney
	 * @param couponIds
	 * @param orgPrice
	 * @param desPrice
	 * @param deviceNo
	 * @param app
	 * @param addressMap
	 * @return
	 */
	double activeOrder(String orderNo, String addressId, String num, String payId, double totalFee,double couponMoney,String couponIds,
			double orgPrice, double desPrice, String deviceNo, String app, Map<String, Object> addressMap);

	/**
	 * TODO 获取微信标识
	 * @param app
	 * @return
	 */
	Map<String, Object> getWxPay(String app);

	/**
	 * 绑定支付信息到支付表
	 * @param orderNo
	 * @param prepayId
	 * @param payNo
	 * @return
	 */
	int boundOrderPrepay(String orderNo, String prepayId, String payNo);

	/**
	 * tengh 2016年8月17日 下午4:02:49
	 * @param deviceNo
	 * @param app
	 * @param flag
	 * @param num
	 * @return
	 * TODO 更新购物车或者订单数目
	 */
	int updateShopOrderNum(String deviceNumber, String app, String flag, int num);

	/**
	 * tengh 2016年8月17日 下午4:30:33
	 * @param deviceNo
	 * @param app
	 * @param shopNo
	 * @return
	 * TODO 删除购物车
	 */
	int deleteShop(String deviceNo, String app, String shopNo);

	/**
	 * TODO 查询单个订单详情，区分定制非定制
	 * @param orderNo
	 * @param deviceNo
	 * @param app
	 * @return
	 */
	Map<String, Object> queryOrderInfo2(String orderNo, String deviceNo, String app);

	/**
	 * tengh 2016年8月22日 下午2:09:16
	 * @param deviceNo
	 * @param app
	 * @param parseInt
	 * @param parseInt2
	 * @param parseInt3
	 * @return
	 * TODO  查询订单列表

	 */
	List<Map<String, Object>> getOrderList(String deviceNo, String app, int page, int number, Integer state);

	/**
	 * tengh 2016年8月22日 下午3:08:55
	 * @param orderNo
	 * @param deviceNo
	 * @param app
	 * @return
	 * TODO 查询订单是否支付成功
	 */
	boolean checkOrder(String orderNo, String deviceNo, String app);

	/**
	 * tengh 2016年8月22日 下午3:17:07
	 * @param orderNo
	 * @param string
	 * @param string2
	 * @return
	 * TODO
	 */
	int boundPrepayNo(String orderNo, String prepayId, String payNo);

	/**
	 * tengh 2016年8月22日 下午7:14:44
	 * @param orderNo
	 * @param deviceNo
	 * @param app
	 * @param flag
	 * @return
	 * TODO 修改购物车状态
	 */
	boolean updateOrder(String orderNo, String deviceNo, String app, String flag);

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
	 * TODO 获取用户优惠券
	 * @param deviceNo
	 * @param app
	 * @param couponId
	 * @return
	 */
	Map<String, Object> getUserCoupon(String deviceNo, String app, String couponId);

	/**
	 * TODO 查出商家名称
	 * @param companyId
	 * @return
	 */
	String queryOrderCompanyName(String companyId);

	/**
	 * TODO 根据订单号查询活动
	 * @param orderNo
	 * @param deviceNo
	 * @param app
	 * @return
	 */
	List<Map<String, Object>> getActivityByOrderNo(String orderNo, String deviceNo, String app);

	/**
	 * TODO 获取商品类型
	 * @param info
	 * @param fileType
	 * @return
	 */
	String getType(String info, String fileType);

	/**
	 * TODO 获取所有活动
	 * @param activities
	 * @return
	 */
	List<Map<String, Object>> getAcitvityList(String activities);

	/**
	 * TODO 查询用户的订单
	 * @param orderNo
	 * @return
	 */
	Map<String, Object> queryByOrderNo(String orderNo);

	/**
	 * TODO 创建立即购买订单（id版）
	 * @param resultPath
	 * @param infoId
	 * @param textureIds
	 * @param deviceNumber
	 * @param userId
	 * @param sufFormat
	 * @param num
	 * @param customize
	 * @param workId
	 * @return
	 */
	Map<String, Object> createOrderForId(String resultPath, String infoId, String textureIds, String deviceNumber,
			String userId, String sufFormat, String num, boolean customize, String workId,String lettering,String modId);

	/**
	 * TODO 查询订单信息以验证（id版 ）
	 * @param orderNo
	 * @param userId
	 * @return
	 */
	Map<String, Object> confirmOrderNumAndPayForId(String orderNo, String userId);

	/**
	 * TODO 查询订单价格数量（id版）
	 * @param orderNo
	 * @param userId
	 * @return
	 */
	Map<String, Object> queryOrderInfoForId(String orderNo, String userId);

	/**
	 * TODO 获取用户优惠券(id版)
	 * @param userId
	 * @param couponId
	 * @return
	 */
	Map<String, Object> getUserCouponForId(String userId, String couponId);

	/**
	 * TODO 绑定订单信息并支付(id版)
	 * @param orderNo
	 * @param addressId
	 * @param num
	 * @param payId
	 * @param totalFee
	 * @param couponMoney
	 * @param couponIds
	 * @param orgPrice
	 * @param desPrice
	 * @param userId
	 * @param addressMap
	 * @param message 
	 * @return
	 */
	double activeOrderForId(String orderNo, String addressId, String num, String payId, double totalFee,
			double couponMoney, String couponIds, double orgPrice, double desPrice, String userId,
			Map<String, Object> addressMap, String message,double realTransport);

	/**
	 * TODO  更新购物车或者订单数目(id版)
	 * @param userId
	 * @param flag
	 * @param num
	 * @return
	 */
	int updateShopOrderNumForId(String userId, String flag, int num);

	/**
	 * TODO 查询单个订单详情，区分定制非定制(id版)
	 * @param orderNo
	 * @param userId
	 * @return
	 */
	Map<String, Object> queryOrderInfo2ForId(String orderNo, String userId);

	/**
	 * TODO 查询订单列表(id版)
	 * @param userId
	 * @param page
	 * @param number
	 * @param state
	 * @return
	 */
	List<Map<String, Object>> getOrderListForId(String userId, int page, int number, Integer state);

	/**
	 * TODO 查询订单是否支付成功(id版)
	 * @param orderNo
	 * @param userId
	 * @return
	 */
	boolean checkOrderForId(String orderNo, String userId);

	/**
	 * TODO 修改购物车状态(id版)
	 * @param orderNo
	 * @param userId
	 * @param flag
	 * @return
	 */
	boolean updateOrderForId(String orderNo, String userId, String flag);

	/**
	 * TODO 插入用户(id版)
	 * @param userId
	 * @param deviceToken
	 * @return
	 */
	boolean recordUserForId(String userId, String deviceToken);

	/**
	 * TODO 删除购物车(id版)
	 * @param userId
	 * @param shopNo
	 * @return
	 */
	int deleteShopForId(String userId, String shopNo);

	/**
	 * TODO 获取活动信息表
	 * @param activities
	 * @return
	 */
	Map<String, Object> getTypeMap(String activities);

	/**
	 * tengh 2016年12月1日 下午8:02:31
	 * @param workId
	 * @return
	 * TODO 查询作品信息
	 */
	Map<String, Object> getWorkInfo(String workId);

	/**
	 * tengh 2016年12月2日 下午2:24:33
	 * @param id
	 * @return
	 * TODO 查询精品汇商品信息
	 */
	Map<String, Object> queryjphGoodInfo(String id);

	/**
	 * TODO 查询全商品,根据fileType来区分
	 * @param id
	 * @param fileType
	 * @return
	 */
	Map<String,Object>queryForGoods(String id,String fileType);
	
	/**
	 * TODO 查询商家的邮费
	 * @param id
	 * @return
	 */
	Map<String,Object>queryForMerchant(String id);

	/**
	 * tengh 2016年12月6日 下午8:56:08
	 * @param shopNo
	 * @param userId
	 * @param textureIds
	 * @return
	 * TODO 修改材质
	 */
	boolean changeTexture(String shopNo, String userId, String textureIds);

	/**
	 * tengh 2016年12月7日 下午5:49:56
	 * @param infoId
	 * @param textureIds 
	 * @return
	 * TODO 查询定制商品信息
	 */
	Map<String, Object> queryJphInfoByInfoId(String infoId, String textureIds);

	/**
	 * TODO 直接决定购物车数量
	 * @param userId
	 * @param flag
	 * @param num
	 * @return
	 */
	int updateShopOrderNumForId2(String userId, String flag, int num);

	/**
	 * TODO 删除购物车并返回真实数量
	 * @param userId
	 * @param shopNo
	 * @return
	 */
	int deleteShopForIdReal(String userId, String shopNo);

	/**
	 * TODO 查看精品汇是否有这个商品
	 * @param infoId
	 * @param texture_ids
	 * @return
	 */
	Map<String, Object> queryJphInfoByInfoId2(String infoId, String texture_ids);

	/**
	 * TODO 查询订单需要的材质参数
	 * @param infoId
	 * @param texutreIds
	 * @param fileType
	 * @return
	 */
	Map<String, Object> queryInfoTexture(String infoId, String texutreIds, String fileType);

	/**
	 * 查询商家名称
	 * @param companyId
	 * @return
	 */
	String getComponyName(String companyId);

	/**
	 * TODO 删除过时的订单
	 * @param userId
	 */
	void toDeleteOrderOutTime(String userId);

}
