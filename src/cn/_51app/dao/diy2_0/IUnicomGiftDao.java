package cn._51app.dao.diy2_0;

import java.util.Map;

public interface IUnicomGiftDao {
	
	/**
	 * 
	 * TODO 领取奖品 
	 * @param mobile 手机号码
	 * @return 领取成功返回true，否则返回false
	 * @author yuanqi 2017年2月5日 上午9:58:46
	 */
	boolean addGift(String mobile,int count) throws Exception;
	
	/**
	 * 
	 * TODO 判断手机号是否领取奖品 
	 * @param mobile
	 * @return 领取就返回true，未领取返回false
	 * @author yuanqi 2017年2月5日 上午9:58:04
	 */
	boolean isReceive(String mobile);
	
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
	 * @throws Exception
	 */
	boolean isLogin(String userId) throws Exception;
	


	/**
	 * 
	 * TODO 判断是否成功领取奖品(加入购物车)
	 * @param userId
	 * @param goodsId
	 * @param textureIds
	 * @param textureName
	 * @return
	 * @throws Exception
	 * @author yuanqi 2017年2月5日 下午2:07:08
	 */
	boolean addToShop(String userId,String goodsId,String textureIds,String textureName) throws Exception;

	/**
	 * 
	 * TODO 通过账户获取手机号码
	 * @param userId
	 * @return
	 * @throws Exception
	 * @author yuanqi 2017年2月5日 下午2:08:10
	 */
	String getMobileByUser(String userId) throws Exception;

	/**
	 * 
	 * TODO 获取联通奖品的信息 
	 * @param paramMap
	 * @return
	 * @throws Exception
	 * @author yuanqi 2017年2月5日 下午4:16:10
	 */
	String getUnicomGoods(Map<String, Object> paramMap) throws Exception;
	
	/**
	 * 
	 * TODO 判断用户是否领取奖品 （购物车是否有记录）
	 * @param userId
	 * @return
	 * @author yuanqi 2017年2月5日 上午11:06:07
	 */
	boolean isReceiveGoods(String userId);
	
	/**
	 * 
	 * TODO 获取用户领取的联通商品的id 
	 * @param userId
	 * @return
	 * @throws Exception
	 * @author yuanqi 2017年2月8日 下午4:34:24
	 */
	String getReceiveGoodsId(String userId) throws Exception;
	
	/**
	 * 
	 * TODO 判断用户是否领取过联通优惠券 
	 * @param userId
	 * @return 
	 * @throws Exception
	 * @author yuanqi 2017年2月7日 下午8:23:19
	 */
	boolean isReceiveCoupon(String userId);
}
