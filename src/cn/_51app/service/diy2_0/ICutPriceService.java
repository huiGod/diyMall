package cn._51app.service.diy2_0;

import java.util.Map;

import cn._51app.entity.CutPriceCoupon;

public interface ICutPriceService{

	/**
	 * tengh 2016年11月23日 下午8:27:12
	 * @param cutPriceCoupon
	 * @return
	 * TODO 添加一种砍价券
	 */
	boolean addCutCoupon(CutPriceCoupon cutPriceCoupon);

	/**
	 * tengh 2016年11月24日 下午1:47:01
	 * @param userId
	 * @return
	 * TODO 我的砍价券
	 */
	String myCutCoupon(String userId);

	/**
	 * tengh 2016年11月24日 下午2:04:21
	 * @param userId
	 * @return
	 * TODO 用户抽奖
	 */
	Map<String, Object> getCutCoupon(String userId);

	/**
	 * tengh 2016年11月24日 下午4:58:41
	 * @return
	 * TODO 砍价券信息
	 */
	String coupons();

	/**
	 * tengh 2016年11月25日 上午10:46:40
	 * @param cutPriceCoupon
	 * @return
	 * TODO 添加砍价券信息
	 */
	boolean editCutCoupon(CutPriceCoupon cutPriceCoupon);

	/**
	 * tengh 2016年11月25日 上午10:57:29
	 * @param id
	 * @return
	 * TODO 删除砍价券
	 */
	boolean delCutCoupon(String id);

	/**
	 * tengh 2016年12月23日 下午3:31:35
	 * @param userId
	 * @return
	 * TODO 查用户是否能够抽奖
	 */
	Map<String, Object> checkCutCoupon(String userId);

}
