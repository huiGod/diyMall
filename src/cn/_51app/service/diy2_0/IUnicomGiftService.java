package cn._51app.service.diy2_0;


public interface IUnicomGiftService {

	/**
	 * 
	 * TODO 领取奖品 
	 * @param mobile 手机号码
	 * @return 领取成功返回true，否则返回false
	 * @author yuanqi 2017年2月5日 上午9:58:46
	 */
	boolean addGift(String mobile);
	
	
	/**
	 * 
	 * TODO 获取领奖总数 
	 * @return
	 * @author yuanqi 2017年2月5日 上午9:59:26
	 */
	int receiveNum();

	/**
	 * TODO 判断是否登陆
	 * @param userId
	 * @return
	 */
	boolean isLogin(String userId);
	
	/**
	 * 
	 * TODO 判断联通用户是否成功领取奖品
	 * @param userId
	 * @param goodsId
	 * @param textureIds
	 * @param textureName
	 * @return
	 * @author yuanqi 2017年2月5日 下午1:35:06
	 */
	boolean addToShop(String userId,String goodsId,String textureIds,String textureName);
	
	/**
	 * 
	 * TODO 判断联通用户是否已经领取奖品与优惠券
	 * @param userId
	 * @return
	 * @author yuanqi 2017年2月5日 下午4:57:14
	 */
	String isReceiveByUser(String userId)  throws Exception;
	
	/**
	 * 
	 * TODO 通过账号获取手机号码 
	 * @param userId
	 * @return
	 * @author yuanqi 2017年2月5日 下午1:40:35
	 */
	String getMobileByUser(String userId) throws Exception;

	/**
	 * 
	 * TODO 获取联通专区奖品信息 
	 * @return
	 * @throws Exception
	 * @author yuanqi 2017年2月6日 上午11:39:40
	 */
	String getUnicomGoods() throws Exception;


	/**
	 * TODO 领取并且获取联通优惠券
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	boolean getUnicomCoupon(String userId) throws Exception;


	void getUnicomCouponByWeixin(String userId, String mobile);
		
}
