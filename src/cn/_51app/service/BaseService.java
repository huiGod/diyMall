package cn._51app.service;

import org.springframework.beans.factory.annotation.Autowired;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.rubyeye.xmemcached.MemcachedClient;

public abstract class BaseService {

	@Autowired
	private MemcachedClient mc;
	
	/** 过期时间 默认30分钟 */
	protected final int TIMEOUT = 30 * 60;
	/** 永久 */
	protected final int FOREVER = 0;
	/** 12小时 */
	protected final int HALFDAY = (12*60)*60;
	
	protected String q(String cacheKey) throws Exception{
		return mc.get(cacheKey);
	};
	//flushAll
	protected void flushAll() throws Exception{
		mc.flushAll();
	};
	
	protected boolean del(String k) throws Exception{
		return mc.delete(k);
	};
	
	protected String toJson(Object data) throws Exception{
		if(data==null){
			return "";
		}
		String json = new ObjectMapper().writeValueAsString(data);
		return json;
	}
}
