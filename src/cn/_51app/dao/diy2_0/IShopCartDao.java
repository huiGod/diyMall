package cn._51app.dao.diy2_0;

import java.util.List;
import java.util.Map;

import cn._51app.entity.User;

public interface IShopCartDao {

	/**
	 * tengh 2016年8月17日 下午3:52:02
	 * @param deviceNo
	 * @param app
	 * @param parseInt
	 * @param parseInt2
	 * @return
	 * TODO 查询购物车
	 */
	List<Map<String, Object>> queryShopList(String deviceNo, String app, int page, int number);

	/**
	 * tengh 2016年8月17日 下午3:52:59
	 * @param infoId
	 * @param textureIds
	 * @return
	 * TODO 查询精品汇预览图
	 */
	String queryPreUrl(String infoId, String textureIds);

	/**
	 * tengh 2016年8月17日 下午3:55:46
	 * @param resultPath
	 * @param infoId
	 * @param textureIds
	 * @param deviceNo
	 * @param app
	 * @param orderNo
	 * @param sufFormat
	 * @param num
	 * @return
	 * TODO 添加到购物车
	 */
	int createShop(String resultPath, String infoId, String textureIds, String deviceNo, String app, String orderNo,
			String sufFormat, String num,int workId,String lettering,String modId);

	/**
	 * tengh 2016年8月17日 下午4:35:14
	 * @param deviceNo
	 * @param app
	 * @param shopNos
	 * @param nums
	 * @return
	 * TODO 编辑多个购物车数量
	 */
	int updateShop(String deviceNo, String app, String shopNos, String nums);

	/**
	 * tengh 2016年8月17日 下午4:41:19
	 * @param deviceNo
	 * @param app
	 * @return
	 * TODO 查询购物车和订单数量
	 */
	Map<String, Object> getOrderShopNum(String deviceNo, String app);

	/**
	 * tengh 2016年8月22日 下午3:29:04
	 * @param shopNo
	 * @param deviceNo
	 * @param app
	 * TODO 购物车失效
	 */
	void invalidShop(String shopNo, String deviceNo, String app);

	/**
	 * tengh 2016年5月20日 上午9:54:08
	 * @param orderNo
	 * @param shopNo
	 * @param infoId
	 * @param textureIds
	 * @param textureName
	 * @param userId
	 * @param imgUrl
	 * @param fileType
	 * @param num
	 * @param name
	 * @param nowPrice
	 * @param payId 
	 * @param addressMap 
	 * @param totalFee 
	 * @param couponId 
	 * @param desPrice 
	 * @param orgPrice 
	 * @param app 
	 * @param deviceNo 
	 * @param transportFee 
	 * @return
	 * TODO 购物车直接生成订单
	 */
	double createOrderByShops(String orderNo, String shopNo, String infoId, String textureIds, String textureName,
			String userId, String imgUrl, String fileType, String num, String name, String nowPrice, String payId, double orgPrice, double desPrice, String couponId, double totalFee, Map<String, Object> addressMap, String deviceNo, String app, String transportFee,String sortName,double couponMoney,String types,String userwork);

	/**
	 * tengh 2016年8月22日 下午3:42:36
	 * @param shopNo
	 * @return
	 * TODO  查询多个购物车信息
	 */
	List<Map<String, Object>> getShopInfoByShopNos(String shopNo);

	/**
	 * tengh 2016年8月22日 下午7:16:31
	 * @param deviceNo
	 * @param app
	 * @param shopNo
	 * @return
	 * TODO  查询购物车激活数
	 */
	int countActiveShop(String deviceNo, String app, String shopNo);

	/**
	 * tengh 2016年5月20日 下午8:21:17
	 * @param prices
	 * @param imgUrls
	 * @param fileTypes
	 * @param nums
	 * @param textureNamess
	 * @param infoIdss
	 * @param userIdss
	 * @param shopNo 
	 * @param app 
	 * @param deviceNo 
	 * @param textureId 
	 * @return
	 * TODO 订单添加到购物车
	 */
	int addShopByOrder(String prices, String imgUrls, String fileTypes, String nums, String textureNamess,
			String infoIdss, String userIdss, String shopNo, String deviceNo, String app, String textureId);

	/**
	 * tengh 2016年8月23日 下午2:19:39
	 * @param deviceNo
	 * @param app
	 * @return
	 * TODO 购物车按商家查询出来
	 */
	List<Map<String, Object>> getShopByUserIds(String deviceNo, String app,int page,int number);

	/**
	 * tengh 2016年8月23日 下午2:36:56
	 * @param companyId
	 * @param app 
	 * @param deviceNo 
	 * @return
	 * TODO 通过商家查询购物车
	 */
	List<Map<String, Object>> getShopInfoByUserId(Integer companyId, String deviceNo, String app);

	/**
	 * TODO 知道定制非定制和info_id获取sort_name
	 * @param infoId
	 * @param flag
	 * @return
	 */
	String getSortNameById(String infoId, boolean flag);

	/**
	 * TODO 购物车按商家查询活动
	 * @param companyId
	 * @param deviceNo
	 * @param app
	 * @return
	 */
	List<Map<String, Object>> getShopActivityByUserId(Integer companyId, String deviceNo, String app);

	/**
	 * TODO 根据商品shopno查询所有活动
	 * @param shopNo
	 * @return
	 */
	List<Map<String, Object>> getActivityList(String shopNo);

	/**
	 * TODO 查询购物车激活数(id版)
	 * @param userId
	 * @param shopNo
	 * @return
	 */
	int countActiveShopForId(String userId, String shopNo);

	/**
	 * TODO 订单添加到购物车(id版)
	 * @param prices
	 * @param imgUrls
	 * @param fileTypes
	 * @param nums
	 * @param textureNamess
	 * @param infoIdss
	 * @param userIdss
	 * @param shopNo
	 * @param userId
	 * @param textureId
	 * @return
	 */
	int addShopByOrderForId(String prices, String imgUrls, String fileTypes, String nums, String textureNamess,
			String infoIdss, String userIdss, String shopNo, String userId, String textureId,String workId);

	/**
	 * TODO 购物车按商家查询出来(id版)
	 * @param userId
	 * @param page
	 * @param number
	 * @return
	 */
	List<Map<String, Object>> getShopByUserIdsForId(String userId);

	/**
	 * TODO 通过商家查询购物车(id版)
	 * @param companyId
	 * @param userId
	 * @return
	 */
	List<Map<String, Object>> getShopInfoByUserIdForId(Integer companyId, String userId);

	/**
	 * TODO 购物车按商家查询活动(id版)
	 * @param companyId
	 * @param userId
	 * @return
	 */
	List<Map<String, Object>> getShopActivityByUserIdForId(Integer companyId, String userId);

	/**
	 * TODO 添加到购物车(id版)
	 * @param resultPath
	 * @param infoId
	 * @param textureIds
	 * @param userId
	 * @param shopNo
	 * @param sufFormat
	 * @param num
	 * @param workId
	 * @return
	 */
	int createShopForId(String resultPath, String infoId, String textureIds, String userId, String shopNo,
			String sufFormat, String num, String workId,String lettering,String modId);

	/**
	 * TODO 编辑多个购物车数量(id版)
	 * @param userId
	 * @param shopNos
	 * @param nums
	 * @return
	 */
	int updateShopForId(String userId, String shopNos, String nums);

	/**
	 * TODO 查询购物车和订单数量(id版)
	 * @param userId
	 * @return
	 */
	Map<String, Object> getOrderShopNumForId(String userId);

	/**
	 * TODO 购物车直接生成订单(id版)
	 * @param orderNo
	 * @param shopNo
	 * @param infoId
	 * @param textureIds
	 * @param textureName
	 * @param userId
	 * @param imgUrl
	 * @param fileType
	 * @param num
	 * @param name
	 * @param nowPrice
	 * @param payId
	 * @param orgPrice
	 * @param desPrice
	 * @param couponId
	 * @param totalFee
	 * @param addressMap
	 * @param userId2
	 * @param transportFee
	 * @param sortName
	 * @param couponMoney
	 * @param types
	 * @param userwork
	 * @param message 
	 * @return
	 */
	double createOrderByShopsForId(String orderNo, String shopNo, String infoId, String textureIds, String textureName,
			String userId, String imgUrl, String fileType, String num, String name, String nowPrice, String payId,
			double orgPrice, double desPrice, String couponId, double totalFee, Map<String, Object> addressMap,
			String userId2, double transportFee, String sortName, double couponMoney, String types, String userwork, 
			String message,String param1,String paramType,String isUnicom);

	/**
	 * TODO 购物车失效(id版)
	 * @param shopNo
	 * @param userId
	 */
	void invalidShopForId(String shopNo, String userId);

	/**
	 * TODO 查询用户购物车
	 * @param deviceNo
	 * @param app
	 * @return
	 */
	List<Map<String, Object>> queryShopList(String deviceNo, String app);

	/**
	 * TODO 查询用户购物车（id版）
	 * @param userId
	 * @return
	 */
	List<Map<String, Object>> queryShopListForId(String userId);

	/**
	 * TODO 修复所有的购物车数量
	 * @return
	 * @throws Exception 
	 */
	int amendShop() throws Exception;

	/**
	 * tengh 2016年11月30日 下午1:45:19
	 * @param id
	 * @param page
	 * @return
	 * TODO 商家下的商品拼单
	 */
	List<Map<String, Object>> togetherGoods(String id, String page);

	/**
	 * tengh 2016年11月30日 下午2:59:05
	 * @param id
	 * @param shopNoss
	 * @param type
	 * @return
	 * TODO 购物车选中状态的编辑
	 */
	boolean carshopSelect(String id, String shopNoss, String type);

	/**
	 * tengh 2016年11月30日 下午5:47:20
	 * @param companyId
	 * @return
	 * TODO 查询所有的商家券
	 */
	List<Map<String, Object>> getCouponByGoodId(Integer companyId);

	/**
	 * tengh 2016年12月2日 下午2:22:16
	 * @param userId
	 * @param id
	 * @param jphGoodInfo 
	 * @return
	 * TODO 直接将商品添加到购物车
	 */
	boolean addShopByGood(Map<String, Object> jphGoodInfo);

	/**
	 * tengh 2016年12月2日 下午5:02:42
	 * @param temcouponId
	 * @param userId
	 * @return
	 * TODO 判断是否领取过优惠券
	 */
	boolean checkIsGetCoupon(Integer temcouponId, String userId);

	/**
	 * TODO 活动凑单
	 * @param id
	 * @param page
	 * @return
	 */
	List<Map<String, Object>> togetherForActivity(String id, String activityId, String page);

	/**
	 * TODO 获取实际购物车数量
	 * @param userId
	 * @return
	 */
	int getUserRealShopNum(String userId);

	/**
	 * TODO 获取错误的购物车号
	 * @param userId
	 * @return
	 */
	String getUserShopFailNo(String userId);

	/**
	 * tengh 2016年12月29日 下午10:16:56
	 * @param userId 
	 * @param info_id
	 * @param texture_ids
	 * @return
	 * TODO 查购物车编号
	 */
	String getShopNo(String userId, Integer info_id, String texture_ids);

	/**
	 * tengh 2016年12月29日 下午10:22:39
	 * @param userId
	 * @param shopNo
	 * TODO 合并
	 */
	void updateNum(String userId, String shopNo);

	/**
	 * TODO 查询所有的购物车不分状态
	 * @param shopNo
	 * @return
	 */
	List<Map<String, Object>> getShopInfoByShopNosByAllStatus(String shopNo);

}
