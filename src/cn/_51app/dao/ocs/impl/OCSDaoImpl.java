package cn._51app.dao.ocs.impl;

import org.springframework.stereotype.Component;

import cn._51app.dao.ocs.OCSDao;

/**
 * OCSDao
 * @author Mr.yu
 *
 */
@Component
public class OCSDaoImpl extends BaseOCS implements OCSDao{

	@Override
	public String query(String key) {
		return (String) get(key);
	}

	@Override
	public boolean insert(String key, String value, int time) {
		return set(key, value, time);
	}

	@Override
	public boolean del(String key) {
		return delete(key);
	}
	
	public void flushAll(){
		try {
			this.flush();
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}
}
