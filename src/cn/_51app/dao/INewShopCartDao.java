package cn._51app.dao;

import java.util.List;
import java.util.Map;

public interface INewShopCartDao {

	/**
	 * tengh 2016年5月19日 下午4:13:40
	 * @param deviceNo
	 * @param app
	 * @param page
	 * @param number
	 * @return
	 * TODO 分页查询购物车列表
	 */
	List<Map<String, Object>> queryShopList(String deviceNo, String app, int page, int number);

	/**
	 * tengh 2016年5月19日 下午5:51:48
	 * @param resultPath
	 * @param infoId
	 * @param textureIds
	 * @param deviceNo
	 * @param app
	 * @param orderNo
	 * @param sufFormat
	 * @param num
	 * TODO 添加到购物车
	 */
	int createShop(String resultPath, String infoId, String textureIds, String deviceNo, String app, String shopNo,
			String sufFormat, String num);

	/**
	 * tengh 2016年5月19日 下午8:00:58
	 * @param deviceNo
	 * @param app
	 * @param shopNos
	 * @param nums
	 * @return 编辑多个购物车数量
	 * TODO
	 */
	int updateShop(String deviceNo, String app, String shopNos, String nums);

	/**
	 * tengh 2016年5月19日 下午8:32:11
	 * @param deviceNo
	 * @param app
	 * @return
	 * TODO 查询购物车和订单数量
	 */
	Map<String, Object> getOrderShopNum(String deviceNo, String app);

	/**
	 * tengh 2016年5月19日 下午9:22:14
	 * @param shopNo
	 * @return
	 * TODO 查询多个购物车信息
	 */
	List<Map<String, Object>> getShopInfoByShopNos(String shopNo);

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
			String userId, String imgUrl, String fileType, String num, String name, String nowPrice, String payId, double orgPrice, double desPrice, String couponId, double totalFee, Map<String, Object> addressMap, String deviceNo, String app, String transportFee);

	/**
	 * tengh 2016年5月20日 下午5:02:46
	 * @param shopNo
	 * TODO 购物车失效
	 * @param app 
	 * @param deviceNo 
	 */
	void invalidShop(String shopNo, String deviceNo, String app);

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
	 * tengh 2016年5月24日 上午10:29:55
	 * @param deviceNo
	 * @param app
	 * @param shopNo
	 * @return
	 * TODO 查询购物车激活数
	 */
	int countActiveShop(String deviceNo, String app, String shopNo);
	
}
