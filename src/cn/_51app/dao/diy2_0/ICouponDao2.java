package cn._51app.dao.diy2_0;

import java.util.List;
import java.util.Map;

import cn._51app.entity.User;

public interface ICouponDao2 {

	/**
	 * TODO 查询个人优惠券
	 * @param deviceNo
	 * @param app
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> couponList(String deviceNo, String app,String dgurl) throws Exception;

	/**
	 * TODO 领券中心
	 * @param paramMap
	 * @return
	 * @throws Exception 
	 */
	public String couponCenter(Map<String, Object> paramMap) throws Exception;

	/**
	 * TODO 用户添加优惠券
	 * @param paramMap
	 * @return
	 * @throws Exception 
	 */
	public int addCoupon(Map<String, Object> paramMap) throws Exception;

	/**
	 * TODO 查询用户输入码是否正确
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	List<Map<String,Object>> getCodeCoupon(Map<String, Object> paramMap);

	/**
	 * TODO 验证是否领过码券
	 * @param paramMap
	 * @return
	 * @throws Exception 
	 */
	boolean isCodeCoupon(Map<String, Object> paramMap) throws Exception;

	/**
	 * TODO 获取单个优惠券
	 * @param paramMap
	 * @return
	 * @throws Exception 
	 */
	String getCoupon(Map<String, Object> paramMap) throws Exception;

	/**
	 * TODO 判断用户是否领取过用户中心的奖券
	 * @param paramMap
	 * @return
	 */
	boolean isCachCoupon(Map<String, Object> paramMap);

	/**
	 * TODO 获取优惠券数量
	 * @param couponId
	 * @return
	 */
	int getCouponCount(String couponId);

	/**
	 * TODO 更新优惠券数量
	 * @param count
	 * @param couponId
	 * @return
	 */
	int updateCouponCount(int count, String couponId);

	/**
	 * TODO 投票达到要求赠送优惠券
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	int addCouponByUserId(Map<String, Object> paramMap) throws Exception;

	/**
	 * TODO 获取个人优惠券（id版）
	 * @param userId
	 * @param dgurl
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> couponListForId(String userId, String dgurl) throws Exception;

	/**
	 * TODO 领券中心（id版）
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	String couponCenterForId(Map<String, Object> paramMap) throws Exception;

	/**
	 * 判断用户是否领过中心优惠券（id版）
	 * @param paramMap
	 * @return
	 */
	boolean isCachCouponForId(Map<String, Object> paramMap);

	/**
	 * TODO 添加优惠券（id版）
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	int addCouponForId(Map<String, Object> paramMap) throws Exception;

	/**
	 * TODO 验证是否领过领码券（id版）
	 * @param paramMap
	 * @return
	 * @throws Exception 
	 */
	boolean isCodeCouponForId(Map<String, Object> paramMap) throws Exception;

	/**
	 * TODO 未登录时候的优惠券
	 * @param paramMap
	 * @return
	 */
	List<Map<String, Object>> getCouponListNotLogin(Map<String, Object> paramMap);

	/**
	 * TODO 登陆以后的优惠券
	 * @param paramMap
	 * @return
	 */
	List<Map<String, Object>> getCouponListlogined(Map<String, Object> paramMap);

	/**
	 * TODO 领券中心（type版）
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	String couponCenterType(Map<String, Object> paramMap) throws Exception;

	/**
	 * TODO 是否领取（优惠券type版）
	 * @param paramMap
	 * @return
	 */
	boolean isCachCouponType(Map<String, Object> paramMap);

	/**
	 * tengh 2016年12月31日 上午3:46:13
	 * @param user
	 * @return
	 * TODO 查询没有登录的设备信息
	 */
	public List<Map<String, Object>> getCouponListNotLogin2(User user);

	/**
	 * tengh 2016年12月31日 上午3:53:56
	 * @param user
	 * @return
	 * TODO 查询现在的优惠券
	 */
	public List<Map<String, Object>> getCouponListlogined2(User user);

	/**
	 * TODO 根据type请求用户优惠券列表
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	String getAiSiAndOldCouponList(Map<String, Object> paramMap) throws Exception;

	/**
	 * TODO 获取优惠券列表byType
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> getCouponListByType(Map<String, Object> paramMap) throws Exception;

}
