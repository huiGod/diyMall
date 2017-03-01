package cn._51app.service.diy2_0;

import java.util.Map;

public interface ICouponService2 {

	/**
	 * TODO 获取可用优惠券
	 * @param deviceNo
	 * @param app
	 * @return
	 * @throws Exception
	 */
	String couponList(String deviceNo, String app) throws Exception;

	/**
	 * TODO 领券中心优惠券
	 * @return
	 * @throws Exception
	 */
	String couponCenter(String deviceNo,String app) throws Exception;

	/**
	 * TODO 添加优惠券
	 * @param valid
	 * @param deviceNo
	 * @param app
	 * @param sys
	 * @param id
	 * @return
	 * @throws Exception
	 */
	boolean addCoupon(String valid, String deviceNo, String app, String sys, String id)throws Exception;

	/**
	 * TODO 获取并获取用户优惠券
	 * @param code
	 * @param deviceNo
	 * @param app
	 * @return
	 */
	String getCodeCoupon(String code, String deviceNo, String app);

	/**
	 * TODO 领取领券中心优惠券
	 * @param deviceNo
	 * @param app
	 * @param couponId
	 * @return
	 */
	String getCenterCoupon(String deviceNo, String app, String couponId,String valid);

	/**
	 * TODO 获取优惠券列表（id版）
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	String couponListForId(String userId) throws Exception;

	/**
	 * TODO领券中心（id版）
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	String couponCenterForId(String userId) throws Exception;

	/**
	 * TODO 领取领券中心优惠券（id版）
	 * @param userId
	 * @param couponId
	 * @param valid
	 * @return
	 */
	String getCenterCouponForId(String userId, String couponId, String valid);

	/**
	 * TODO 领取并获取用户优惠券（id版）
	 * @param code
	 * @param userId
	 * @return
	 */
	String getCodeCouponForId(String code, String userId);

	/**
	 * TODO 添加用户优惠券（id版)
	 * @param valid
	 * @param userId
	 * @param sys
	 * @param id
	 * @return
	 * @throws Exception
	 */
	boolean addCouponForId(String valid, String userId, String sys, String id) throws Exception;

	/**
	 * TODO 中心优惠券（type版本）
	 * @param deviceNo
	 * @param app
	 * @param type
	 * @return
	 * @throws Exception
	 */
	String couponCenterType(String deviceNo, String app, String type) throws Exception;

	/**
	 * TODO 领取中心优惠券（type版本）
	 * @param deviceNo
	 * @param app
	 * @param couponId
	 * @param valid
	 * @param type
	 * @return
	 */
	String getCenterCouponType(String deviceNo, String app, String couponId, String valid, String type);

	/**
	 * TODO 根据type获取优惠券的列表
	 * @param type
	 * @return
	 * @throws Exception 
	 */
	String getAiSiAndOldCouponList(String type,String userId) throws Exception;

}
