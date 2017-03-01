package cn._51app.service.diy2_0;

public interface IHomeNavService {

	/**
	 * 获取2.0首页数据
	 * @return
	 * @throws Exception 
	 */
	public String getHomeNav() throws Exception;

	String toSpecial(String navId) throws Exception;

	/**
	 * 跳专题
	 * @param nav_id
	 * @return
	 * @throws Exception
	 */
	String special(int nav_id) throws Exception;
}
