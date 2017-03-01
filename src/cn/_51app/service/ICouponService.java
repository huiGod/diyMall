package cn._51app.service;

public interface ICouponService {
	
	String drawSwitch(String deviceNo,String app) throws Exception;
	
	String getDrawLevelAndValid() throws Exception;
	
	boolean addCouponUser(String valid, String item, String mobile)
			throws Exception;
	

	/**
	 * tengh 2016年5月17日 下午9:28:04
	 * @param deviceNo
	 * @param app
	 * @return
	 * TODO 获取可用优惠券
	 */
	String getCoupon(String deviceNo, String app) throws Exception;

	/**
	 * tengh 2016年11月4日 下午6:43:20
	 * @return
	 * TODO
	 */
	String couponTo();
}
