package cn._51app.dao;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

public interface IUCouponDao {

	boolean userDraw(Map<String, Object> paramMap) throws Exception;

	int addCouponUser(Map<String, Object> paramMap) throws Exception;

	int getCouponIdByLevel(String levelStr) throws Exception;

	int addMobileUser(Map<String, Object> paramMap) throws Exception;

	int verifyUserCount(Map<String, Object> paramMap) throws DataAccessException, Exception;

	List<Map<String, Object>> winnerList(String app) throws DataAccessException, Exception;

	Map<String,Object> getLottery(Map<String, Object> paramMap) throws Exception;

	int getAwards(Map<String, Object> paramMap);

	int verifyCount(String level);

	int getCouponId(Map<String, Object> paramMap);

	int upUserMobile(int id, String mobile) throws Exception;

	int delCoupon() throws Exception;

}
