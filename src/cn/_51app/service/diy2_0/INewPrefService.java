package cn._51app.service.diy2_0;


public interface INewPrefService {
	
	public String getLoad(Integer userId);
	
	public boolean isGetCoupon(Integer userId);
	
	public boolean getCouponByUser(Integer userId) throws Exception;
	
	
	public boolean isLogin(Integer userId);
	
}
