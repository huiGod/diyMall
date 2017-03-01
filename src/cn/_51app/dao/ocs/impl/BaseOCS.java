package cn._51app.dao.ocs.impl;

import org.springframework.beans.factory.annotation.Autowired;

import net.rubyeye.xmemcached.MemcachedClient;

public abstract class BaseOCS {

	@Autowired
	protected MemcachedClient ocsClient;

	public MemcachedClient getOcsClient() {
		return ocsClient;
	}

	public void setOcsClient(MemcachedClient ocsClient) {
		this.ocsClient = ocsClient;
	}

	/**
	 * 增加键值，如果key已经存在，再放入是失败的.
	 * 
	 * @param key
	 * @param value
	 * @param time
	 *            缓存时间
	 * @return
	 */
	protected boolean add(String key, Object value, int time) {
		try {
			return this.ocsClient.add(key, time, value);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 增加键值，如果key存在,就是更新, 如果key不存在，就是添加
	 * 
	 * @param key
	 * @param value
	 * @param time
	 *            缓存时间
	 * @return
	 */
	protected boolean set(String key, Object value, int time) {
		try {
			return this.ocsClient.set(key, time, value);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 原子操作，自增值
	 * 
	 * @param key
	 * @param margin
	 *            增加幅度
	 * @return
	 */
	protected long incr(String key, long margin) {
		try {
			return this.ocsClient.incr(key, margin);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * 原子操作，自增值
	 * 
	 * @param key
	 * @param margin
	 *            增加幅度
	 * @param initValue
	 *            初始值
	 * @return
	 */
	protected long incr(String key, long margin, long initValue) {
		try {
			return this.ocsClient.incr(key, margin, initValue);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * 原子操作，自减值
	 * 
	 * @param key
	 * @param margin
	 *            减少幅度
	 * @return
	 */
	protected long decr(String key, long margin) {
		try {
			return this.ocsClient.decr(key, margin);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * 原子操作，自减值
	 * 
	 * @param key
	 * @param margin
	 *            减少幅度
	 * @param initValue
	 *            初始值
	 * @return
	 */
	protected long decr(String key, long margin, long initValue) {
		try {
			return this.ocsClient.decr(key, margin, initValue);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * 得到对应键的值
	 * 
	 * @param key
	 * @return
	 */
	protected Object get(String key) {
		try {
			return this.ocsClient.get(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 得到对应键的值
	 * 
	 * @param key
	 * @return
	 */
	protected Object getAndSet(String key, int time) {
		try {
			Object mValue = this.ocsClient.get(key);
			if (mValue != null) {
				this.ocsClient.set(key, time, mValue);
			}
			return mValue;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	protected boolean delete(String key) {
		try {
			return this.ocsClient.delete(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	protected void flush() throws Exception{
		try {
			this.ocsClient.flushAll();
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}
}
