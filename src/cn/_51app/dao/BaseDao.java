package cn._51app.dao;

import net.rubyeye.xmemcached.MemcachedClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import cn._51app.util.SaveCacheException;

public abstract class BaseDao {

	@Autowired
	protected JdbcTemplate jt;
	
	@Autowired
	protected NamedParameterJdbcTemplate npjt;
	
	@Autowired
	private MemcachedClient mc;
	
	
	protected String q(String k) throws Exception{
		return mc.get(k);
	};
	
	protected boolean add(String k, String v, int time) throws Exception{
		return mc.set(k,time,v);
	};
	
	protected boolean del(String k) throws Exception{
		return mc.delete(k);
	};
	
	/**>>Faster
	 * 
	 * @param data
	 * @return 将对象转换成json数据
	 * @throws Exception
	 */
	protected String toJson(Object data) throws Exception{
		if(data==null){
			return "";
		}
		String json = new ObjectMapper().writeValueAsString(data);
		return json;
	}
	
	protected String saveAndGet(Object data,String cacheKey,int cacheTime) throws Exception{
		String json = toJson(data);
		if(!add(cacheKey, json,cacheTime)){
			throw new SaveCacheException();
		}
		return q(cacheKey);
	}
	
	/**>>Faster
	 * 
	 * @param cacheKey 缓存名称
	 * @return a为空或数据空，b正常获取缓存，c获取缓存异常
	 * @throws Exception
	 */
	protected String isCacheNull(String cacheKey) throws Exception{
		
		try {
			String json =q(cacheKey);
			if (json == null || json.equals("")){
				return "a";
			}else{
				return "b";
			}
		} catch (Exception e) {
			//缓存异常
			return "c";
		}
		
	}
}
