package cn._51app.dao;

import java.util.List;
import java.util.Map;

public interface ICouponDao {
	
	String drawSwitch(Map<String,Object> paramMap) throws Exception;
	
	String getDrawLevelAndValid(String cacheKey,int cacheTime) throws Exception;
	
	int getCouponIdByLevel(String levelStr) throws Exception;
	
	int addCouponUser(Map<String,Object> paramMap) throws Exception;
	
	String getCoupon(Map<String,Object> paramMap) throws Exception;

	boolean userDraw(Map<String, Object> paramMap) throws Exception;

	/**
	 * tengh 2016年5月17日 下午9:33:57
	 * @param deviceNo
	 * @param app
	 * @return
	 * TODO 查询个人可用优惠券
	 */
	List<Map<String, Object>> queryAvaiCoupon(String deviceNo, String app);

	List<Map<String, Object>> getUsers();

	int insertCoupon(String deviceNo, String app);
}
