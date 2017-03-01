package cn._51app.service.diy2_0;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;

public interface IShopCartService {

	/**
	 * tengh 2016年5月19日 下午4:00:36
	 * @param deviceNo
	 * @param app
	 * @param page
	 * @return
	 * TODO 查询购物车
	 */
	String shopList(String deviceNo, String app, String page) throws Exception;

	/**
	 * tengh 2016年8月17日 下午3:44:03
	 * @param imgFile
	 * @param imgBackFile
	 * @param previewFile
	 * @param previewBackFile
	 * @param infoId
	 * @param textureIds
	 * @param deviceNo
	 * @param num
	 * @param app
	 * @return
	 * TODO 添加到购物车
	 */
	boolean addShop(MultipartFile imgFile, MultipartFile imgBackFile, MultipartFile previewFile,
			MultipartFile previewBackFile, String infoId, String textureIds, String deviceNo, String num, String app,HttpServletRequest req,String wallpaper,String isBoutique,String lettering,String modId);

	/**
	 * tengh 2016年8月17日 下午4:13:16
	 * @param deviceNo
	 * @param app
	 * @param shopNos
	 * @return
	 * TODO 删除购物车
	 */
	boolean deleteShop(String deviceNo, String app, String shopNos);

	/**
	 * tengh 2016年8月17日 下午4:31:46
	 * @param deviceNo
	 * @param app
	 * @param shopNos
	 * @param nums
	 * @return
	 * TODO 编辑多个购物车数量
	 */
	boolean updateShop(String deviceNo, String app, String shopNos, String nums);

	/**
	 * tengh 2016年8月17日 下午4:40:39
	 * @param deviceNo
	 * @param app
	 * @return
	 * TODO 查询账户购物车和订单数量
	 */
	String getOrderShopNum(String deviceNo, String app) throws Exception;

	/**
	 * tengh 2016年8月22日 下午3:25:12
	 * @param deviceNo
	 * @param app
	 * @param shopNos
	 * @param payId
	 * @param addressId
	 * @param couponId
	 * @return
	 * TODO 购物车生成订单
	 */
	String createOrderByShops(String deviceNo, String app, String shopNos, String payId, String addressId,
			String couponId) throws Exception;
	
	/**
	 * tengh 2016年5月20日 下午5:08:39
	 * @param shopNos
	 * @param deviceNo
	 * @param app
	 * TODO 购物车无效
	 */
	void invalidShops(String shopNos, String deviceNo, String app);

	/**
	 * tengh 2016年8月22日 下午7:15:40
	 * @param deviceNo
	 * @param app
	 * @param shopNo
	 * @return
	 * TODO 查询购物车编号激活的数量
	 */
	int countActiveShop(String deviceNo, String app, String shopNo);

	/**
	 * tengh 2016年5月20日 下午8:18:12
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
	 * @param textureIdss 
	 * @return
	 * TODO 从订单添加到购物车
	 */
	int addShopByOrder(String prices, String imgUrls, String fileTypes, String nums, String textureNamess, String infoIdss,
			String userIdss, String shopNo, String deviceNo, String app, String textureIdss);

	/**
	 * TODO 处理购物车优惠券
	 * @param deviceNo
	 * @param app
	 * @param couponIds
	 * @param totalFee
	 * @return
	 */
	Double privilege(String deviceNo, String app, String couponIds, Double totalFee);

	/**
	 * TODO 查询购物车编号激活的数量(id版)
	 * @param userId
	 * @param shopNo
	 * @return
	 */
	int countActiveShopForId(String userId, String shopNo);

	/**
	 * TODO 从订单添加到购物车(id版)
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
	 * TODO 查询购物车(id版)
	 * @param userId
	 * @param page
	 * @return
	 * @throws Exception
	 */
	Map<String,Object> shopListForId(String userId) throws Exception;

	/**
	 * TODO 添加到购物车(id版)
	 * @param imgFile
	 * @param imgBackFile
	 * @param previewFile
	 * @param previewBackFile
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
	 */
	boolean addShopForId(MultipartFile imgFile, MultipartFile imgBackFile, MultipartFile previewFile,
			MultipartFile previewBackFile, String infoId, String textureIds, String userId, String num,
			HttpServletRequest req, String wallpaper, String isBoutique,String lettering,String modId, String keycode, String isSave);

	/**
	 * TODO 删除购物车(id版)
	 * @param userId
	 * @param shopNos
	 * @return
	 */
	boolean deleteShopForId(String userId, String shopNos);

	/**
	 * TODO 编辑多个购物车数量(id版)
	 * @param userId
	 * @param shopNos
	 * @param nums
	 * @return
	 */
	boolean updateShopForId(String userId, String shopNos, String nums);

	/**
	 * TODO 查询账户购物车和订单数量(id版)
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	String getOrderShopNumForId(String userId) throws Exception;

	/**
	 * TODO 处理购物车优惠券
	 * @param userId
	 * @param couponIds
	 * @param totalFee
	 * @return
	 */
	Double privilegeForId(String userId, String couponIds, Double totalFee);

	/**
	 * TODO 购物车失效(id版)
	 * @param shopNos
	 * @param userId
	 */
	void invalidShopsForId(String shopNos, String userId);

	/**
	 * TODO 购物车生成订单(id版)
	 * @param userId
	 * @param shopNos
	 * @param payId
	 * @param addressId
	 * @param couponId
	 * @param app
	 * @param message 
	 * @return
	 * @throws Exception
	 */
	String createOrderByShopsForId(String userId, String shopNos, String payId, String addressId, String couponId,
			String app, String message) throws Exception;

	/**
	 * tengh 2016年11月30日 上午11:53:40
	 * @param id
	 * @param page
	 * @return 
	 * TODO 商家凑单
	 */
	String togetherGoods(String id, String page,String activityId);

	/**
	 * tengh 2016年11月30日 下午2:46:34
	 * @param id
	 * @param shopNos
	 * @param type
	 * @return
	 * TODO 购物车选中操作
	 */
	boolean carshopSelect(String id, String shopNos, String type);

	/**
	 * tengh 2016年12月2日 下午2:21:18
	 * @param userId
	 * @param id
	 * @return
	 * TODO 直接将商品添加到购物车
	 */
	boolean addShopByGood(String userId, String id);

}
