package cn._51app.dao.diy2_0;

public interface IShareDao {

	/**
	 * tengh 2016年10月11日 下午9:19:02
	 * @param openid
	 * @param defaultApp 
	 * @return
	 * TODO 查询出账号id
	 */
	int getMemberId(String openid, String defaultApp);

}
