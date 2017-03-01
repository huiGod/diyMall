package cn._51app.service;

import org.springframework.web.multipart.MultipartFile;

public interface INewShopCartService {

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
	 * tengh 2016年5月19日 下午5:28:22
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
	boolean addShop(MultipartFile imgFile, MultipartFile imgBackFile, MultipartFile previewFile, MultipartFile previewBackFile, String infoId,
			String textureIds, String deviceNo, String num, String app);

	/**
	 * tengh 2016年5月19日 下午7:37:17
	 * @param deviceNo
	 * @param app
	 * @param shopNos
	 * @return 删除购物车
	 * TODO
	 */
	boolean deleteShop(String deviceNo, String app, String shopNos);

	/**
	 * tengh 2016年5月19日 下午7:59:39
	 * @param deviceNo
	 * @param app
	 * @param shopNos
	 * @param nums
	 * @return
	 * TODO 编辑购物车数量
	 */
	boolean updateShop(String deviceNo, String app, String shopNos, String nums);

	/**
	 * tengh 2016年5月19日 下午8:27:41
	 * @param deviceNo
	 * @param app
	 * @return 
	 * TODO 查询购物车和订单数量
	 */
	String getOrderShopNum(String deviceNo, String app) throws Exception;

	/**
	 * tengh 2016年5月19日 下午8:46:53
	 * @param deviceNo
	 * @param app
	 * @param shopNos
	 * @param payId 
	 * @param addressId
	 * @param  couponId 
	 * @return
	 * TODO 购物车生成订单
	 */
	String createOrderByShops(String deviceNo, String app, String shopNos, String payId, String addressId, String couponId) throws Exception;

	/**
	 * tengh 2016年5月20日 下午5:08:39
	 * @param shopNos
	 * @param deviceNo
	 * @param app
	 * TODO 购物车无效
	 */
	void invalidShops(String shopNos, String deviceNo, String app);

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
	 * tengh 2016年5月24日 上午10:27:19
	 * @param deviceNo
	 * @param app
	 * @param shopNo
	 * @return
	 * TODO 查询购物车编号激活的数量
	 */
	int countActiveShop(String deviceNo, String app, String shopNo);
}
