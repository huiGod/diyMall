package cn._51app.dao.ocs;

/**
 * OCSdao
 * @author Mr.yu
 *
 */
public interface OCSDao {
	/**
	 * 查询缓存
	 * @param key
	 * @return
	 */
	String query(String key);

	/**
	 * 插入缓存
	 * @param key
	 * @param value
	 * @param time
	 * @return
	 */
	boolean insert(String key, String value, int time);
	
	/**
	 * tengh 2016年5月18日 上午10:33:18
	 * @return
	 * TODO 删除缓存
	 */
	boolean del(String key);
	
	/**
	 * tengh 2016年5月18日 上午10:32:50
	 * @throws Exception
	 * TODO 清除所有缓存  慎用
	 */
	void flushAll() throws Exception;
}
