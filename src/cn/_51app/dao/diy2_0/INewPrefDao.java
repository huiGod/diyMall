package cn._51app.dao.diy2_0;

import java.util.List;
import java.util.Map;



public interface INewPrefDao {

	/**
	 * 
	 * TODO 获取新人专享商品信息 
	 * @param page 页码
	 * @return
	 * @author yuanqi 2017年2月25日 下午1:03:49
	 */
	public List<Map<String,Object>> getNewPrefGoods();

	/**
	 * 
	 * TODO  判断用户是否领取新人专享优惠券
	 * @param userId
	 * @return 
	 * @author yuanqi 2017年2月25日 下午1:04:05
	 */
	public boolean isGetCoupon(Integer userId);

	/**
	 * 
	 * TODO 用户领取优惠券 
	 * @param userId
	 * @return
	 * @author yuanqi 2017年2月25日 下午1:05:04
	 */
	public int getCouponByUser(Integer userId) throws Exception;

	/**
	 * 
	 * TODO 用户是否登录 
	 * @param userId
	 * @return
	 * @author yuanqi 2017年2月25日 下午1:49:35
	 */
	public boolean isLogin(Integer userId);

	/**
	 * 
	 * TODO 获取新人专享优惠券 
	 * @return
	 * @author yuanqi 2017年2月25日 下午3:31:22
	 */
	public List<Map<String, Object>> getNewPrefCoupon();
	
	/**
	 * 
	 * TODO 添加优惠券 
	 * @param paramMap
	 * @return
	 * @throws Exception
	 * @author yuanqi 2017年2月25日 下午3:55:44
	 */
	public int addCouponForId(Map<String, Object> paramMap) throws Exception;


}
