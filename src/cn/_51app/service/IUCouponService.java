package cn._51app.service;

import org.springframework.dao.DataAccessException;

public interface IUCouponService {

	boolean addMobileUser(String mobile) throws Exception;

//	String verifyUserCount(String mobile) throws DataAccessException, Exception;

//	boolean addCouponUser(String valid, String item, String mobile, String phone) throws Exception;

	String getLottery(String derviceNo,String app) throws Exception;

	String winnerList(String app, String deviceNo) throws DataAccessException, Exception;

	boolean addCouponUser(String valid, String item, String deviceNo,String app) throws Exception;

	boolean upUserMobile(int id, String mobile) throws Exception;

	boolean delCoupon() throws Exception;
}
