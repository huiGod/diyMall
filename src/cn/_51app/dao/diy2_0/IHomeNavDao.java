package cn._51app.dao.diy2_0;

import java.util.Map;

public interface IHomeNavDao {

	/**
	 * 首页数据
	 * @param paramMap 参数
	 * @return
	 * @throws Exception 
	 */
	public String HomeNav(Map<String,Object>paramMap) throws Exception;

	/**
	 * 查询专题
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public String toSpecial(Map<String, Object> paramMap) throws Exception;

	/**
	 * 专题
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	String special(Map<String, Object> paramMap) throws Exception;
}
