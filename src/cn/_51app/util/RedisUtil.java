package cn._51app.util;

import java.util.List;
import java.util.Map;
import java.util.Set;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public final class RedisUtil {
	
	private static String ADDR="fb3b881e32fb499e.m.cnhza.kvstore.aliyuncs.com";
//	private static String ADDR="192.168.1.249";
	
	private static int PORT=6379;
	
	private static String AUTH="Wcl20150312www51appcn";
//	private static String AUTH=null;
	
	private static int MAX_TOTAL=300;
	
	private static int MAX_IDLE=100;
	
	private static int MAX_WAITMILLIS=10000;
	
	private static int TIMEOUT=10000;
	
	private static boolean TEST_ONBORROW=true;
	
	private static JedisPool jedisPool=null;
	
	/**
	 * 获取redis连接池
	 */
	static {
		try {
			JedisPoolConfig jedisPoolConfig=new JedisPoolConfig();
			jedisPoolConfig.setMaxTotal(MAX_TOTAL);
			jedisPoolConfig.setMaxIdle(MAX_IDLE);
			jedisPoolConfig.setMaxWaitMillis(MAX_WAITMILLIS);
			jedisPoolConfig.setTestOnBorrow(TEST_ONBORROW);
			jedisPool=new JedisPool(jedisPoolConfig, ADDR, PORT, TIMEOUT, AUTH);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * tengh 2016年11月17日 下午4:29:59
	 * @return
	 * TODO获取Jedis实例
	 */
	public synchronized static Jedis getJedis(){
		try {
			if(jedisPool!=null){
				Jedis resource=jedisPool.getResource();
				return resource;
			}else{
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 
	 * tengh 2016年11月17日 下午4:32:22
	 * @param jedis
	 * TODO 返还到连接池
	 */
	public static void returnResource(final Jedis jedis){
		if(jedis!=null){
			jedisPool.returnResource(jedis);
		}
	}
	
	/**
	 * 
	 * tengh 2016年11月17日 下午4:53:40
	 * @param jedis
	 * TODO 释放redis对象
	 */
	public static void returnBrokenResource(final Jedis jedis){
		if(jedis!=null){
			jedisPool.returnBrokenResource(jedis);
		}
	}
	
	/**
	 * tengh 2016年11月18日 上午10:14:08
	 * @return
	 * TODO 清空当前reids
	 */
	public static String flushDb(){
		Jedis jedis=null;
		try {
			jedis=getJedis();
			return jedis.flushDB();
		} catch (Exception e) {
			returnBrokenResource(jedis);
			e.printStackTrace();
		}finally {
			returnResource(jedis);
		}
		return "清空缓存异常";
	}
	
	/**
	 * 
	 * tengh 2016年11月17日 下午4:45:23
	 * @param key
	 * @return 
	 * TODO 获取键值
	 */
	public static String get(String key){
		String value=null;
		Jedis jedis=null;
		try {
			jedis=getJedis();
			value=jedis.get(key);
		} catch (Exception e) {
			returnBrokenResource(jedis);
			e.printStackTrace();
		}finally {
			returnResource(jedis);
		}
		return value;
	}
	
	/**
	 * tengh 2016年11月17日 下午5:00:09
	 * @param key
	 * @param value
	 * @return
	 * TODO 设置缓存
	 */
	public static void set(String key,String value){
		Jedis jedis=null;
		try {
			jedis=getJedis();
			jedis.set(key, value);
		} catch (Exception e) {
			returnBrokenResource(jedis);
			e.printStackTrace();
		}finally {
			returnResource(jedis);
		}
	}
	
	/**
	 * tengh 2016年11月17日 下午5:06:46
	 * @param key
	 * TODO 删除键值
	 */
	public static long del(String key){
		Jedis jedis=null;
		try {
			jedis=getJedis();
			return jedis.del(key);
		} catch (Exception e) {
			returnBrokenResource(jedis);
			e.printStackTrace();
		}finally {
			returnResource(jedis);
		}
		return 0;
	}
	
	/**
	 * tengh 2016年11月19日 上午10:28:17
	 * @param key
	 * TODO 增加
	 */
	public static long incr(String key){
		Jedis jedis=null;
		try {
			jedis=getJedis();
			return jedis.incr(key);
		} catch (Exception e) {
			returnBrokenResource(jedis);
			e.printStackTrace();
		}finally {
			returnResource(jedis);
		}
		return 0;
	}
	
	/**
	 * tengh 2016年11月19日 上午10:28:34
	 * @param key
	 * TODO 减少
	 */
	public static long decr(String key){
		Jedis jedis=null;
		try {
			jedis=getJedis();
			return jedis.decr(key);
		} catch (Exception e) {
			returnBrokenResource(jedis);
			e.printStackTrace();
		}finally {
			returnResource(jedis);
		}
		return 0;
	}
//	********************************Hash*********************************************
	/**
	 * tengh 2016年11月24日 上午11:13:00
	 * @param key
	 * @param field
	 * @param value
	 * TODO 添加元素
	 */
	public static long hset(String key,String field,String value){
		Jedis jedis=null;
		try {
			jedis=getJedis();
			return jedis.hset(key, field, value);
		} catch (Exception e) {
			returnBrokenResource(jedis);
			e.printStackTrace();
		}finally {
			returnResource(jedis);
		}
		return 0;
	}
	
	/**
	 * tengh 2016年11月24日 下午2:55:11
	 * @param key
	 * @param field
	 * @return
	 * TODO 获取一个值
	 */
	public static String hget(String key,String field){
		Jedis jedis=null;
		try {
			jedis=getJedis();
			return jedis.hget(key, field);
		} catch (Exception e) {
			returnBrokenResource(jedis);
			e.printStackTrace();
		}finally {
			returnResource(jedis);
		}
		return null;
	}
	
	/**
	 * tengh 2016年11月24日 下午2:55:52
	 * @param key
	 * @param fields
	 * @return
	 * TODO 获取多个值
	 */
	public static List<String> hmget(String key,String...fields){
		Jedis jedis=null;
		try {
			jedis=getJedis();
			return jedis.hmget(key, fields);
		} catch (Exception e) {
			returnBrokenResource(jedis);
			e.printStackTrace();
		}finally {
			returnResource(jedis);
		}
		return null;
	}
	
	/**
	 * tengh 2016年11月24日 下午2:56:39
	 * @param key
	 * @return
	 * TODO 获取所有值
	 */
	public static Map<String, String> hgetAll(String key){
		Jedis jedis=null;
		try {
			jedis=getJedis();
			return jedis.hgetAll(key);
		} catch (Exception e) {
			returnBrokenResource(jedis);
			e.printStackTrace();
		}finally {
			returnResource(jedis);
		}
		return null;
	}
	
	/**
	 * tengh 2016年11月24日 上午11:13:53
	 * @param key
	 * @param hash
	 * TODO 添加多个元素
	 */
	public static String hmset(String key,Map<String, String> hash){
		Jedis jedis=null;
		try {
			jedis=getJedis();
			return jedis.hmset(key, hash);
		} catch (Exception e) {
			returnBrokenResource(jedis);
			e.printStackTrace();
		}finally {
			returnResource(jedis);
		}
		return null;
	}
//	********************************List*********************************************
	/**
	 * tengh 2016年11月22日 下午5:03:44
	 * @param key
	 * @param value
	 * TODO 往列表尾部添加元素
	 */
	public static void rpush(String key,String...value){
		Jedis jedis=null;
		try {
			jedis=getJedis();
			jedis.rpush(key, value);
		} catch (Exception e) {
			returnBrokenResource(jedis);
			e.printStackTrace();
		}finally {
			returnResource(jedis);
		}
	}
	
	/**
	 * tengh 2016年11月24日 下午3:15:01
	 * @param key
	 * @param value
	 * TODO 往列表头部添加元素
	 */
	public static void lpush(String key,String...value){
		Jedis jedis=null;
		try {
			jedis=getJedis();
			jedis.lpush(key, value);
		} catch (Exception e) {
			returnBrokenResource(jedis);
			e.printStackTrace();
		}finally {
			returnResource(jedis);
		}
	}
	
	/**
	 * tengh 2016年11月24日 下午3:55:11
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 * TODO 取从头到尾一段元素
	 */
	public static List<String> lrange(String key,long start,long end){
		Jedis jedis=null;
		try {
			jedis=getJedis();
			return jedis.lrange(key, start, end);
		} catch (Exception e) {
			returnBrokenResource(jedis);
			e.printStackTrace();
		}finally {
			returnResource(jedis);
		}
		return null;
	}
	
	/**
	 * tengh 2016年11月24日 下午3:57:35
	 * @param key
	 * @param index
	 * @return
	 * TODO 根据索引取列表的值
	 */
	public static String lindex(String key,long index){
		Jedis jedis=null;
		try {
			jedis=getJedis();
			return jedis.lindex(key, index);
		} catch (Exception e) {
			returnBrokenResource(jedis);
			e.printStackTrace();
		}finally {
			returnResource(jedis);
		}
		return null;
	}
	
	/**
	 * tengh 2016年11月25日 上午11:30:10
	 * @param key
	 * @param count
	 * @param value
	 * @return
	 * TODO 从列表中删除元素
	 */
	public static long lrem(String key,long count,String value){
		Jedis jedis=null;
		try {
			jedis=getJedis();
			return jedis.lrem(key, count, value);
		} catch (Exception e) {
			returnBrokenResource(jedis);
			e.printStackTrace();
		}finally {
			returnResource(jedis);
		}
		return 0;
	}
	
//	********************************Sets*********************************************
	/**
	 * tengh 2016年11月19日 上午10:59:16
	 * @param key
	 * @param value
	 * TODO 添加元素到 Sets
	 */
	public static long sadd(String key,String value){
		Jedis jedis=null;
		try {
			jedis=getJedis();
			return jedis.sadd(key, value);
		} catch (Exception e) {
			returnBrokenResource(jedis);
			e.printStackTrace();
		}finally {
			returnResource(jedis);
		}
		return 0;
	}
	
	/**
	 * tengh 2016年11月19日 上午11:00:35
	 * @param key
	 * @param value
	 * TODO 从Sets删除元素
	 */
	public static long srem(String key,String value){
		Jedis jedis=null;
		try {
			jedis=getJedis();
			return jedis.srem(key, value);
		} catch (Exception e) {
			returnBrokenResource(jedis);
			e.printStackTrace();
		}finally {
			returnResource(jedis);
		}
		return 0;
	}
	
	/**
	 * tengh 2016年11月19日 上午11:01:54
	 * @param key
	 * @param value
	 * @return
	 * TODO 判断元素是否在Sets中
	 */
	public static boolean sismember(String key,String value){
		Jedis jedis=null;
		try {
			jedis=getJedis();
			return jedis.sismember(key, value);
		} catch (Exception e) {
			returnBrokenResource(jedis);
			e.printStackTrace();
		}finally {
			returnResource(jedis);
		}
		return false;
	}
	
	/**
	 * tengh 2016年11月19日 下午2:15:57
	 * @param key
	 * @return
	 * TODO 查询Sets集合中所有元素
	 */
	public static Set<String> smembers(String key){
		Jedis jedis=null;
		try {
			jedis=getJedis();
			return jedis.smembers(key);
		} catch (Exception e) {
			returnBrokenResource(jedis);
			e.printStackTrace();
		}finally {
			returnResource(jedis);
		}
		return null;
	}
	
	/**
	 * tengh 2016年11月24日 下午2:38:45
	 * @param key
	 * @return
	 * TODO 返回长度
	 */
	public static long scard(String key){
		Jedis jedis=null;
		try {
			jedis=getJedis();
			return jedis.scard(key);
		} catch (Exception e) {
			returnBrokenResource(jedis);
			e.printStackTrace();
		}finally {
			returnResource(jedis);
		}
		return 0;
	}
	
	public static String srandmember(String key){
		Jedis jedis=null;
		try {
			jedis=getJedis();
			return jedis.srandmember(key);
		} catch (Exception e) {
			returnBrokenResource(jedis);
			e.printStackTrace();
		}finally {
			returnResource(jedis);
		}
		return null;
	}
	
//	********************************Sort Sets******************************************
	
	/**
	 * tengh 2016年11月24日 下午2:37:48
	 * @param key
	 * @param score
	 * @param member
	 * TODO 添加元素
	 */
	public static void zadd(String key,double score,String member){
		Jedis jedis=null;
		try {
			jedis=getJedis();
			jedis.zadd(key, score, member);
		} catch (Exception e) {
			returnBrokenResource(jedis);
			e.printStackTrace();
		}finally {
			returnResource(jedis);
		}
	}
	
	/**
	 * tengh 2016年11月19日 下午4:04:29
	 * @param key
	 * @param score
	 * @param member
	 * TODO sort sets增益
	 */
	public static void zincrby(String key,double score,String member){
		Jedis jedis=null;
		try {
			jedis=getJedis();
			jedis.zincrby(key, score, member);
		} catch (Exception e) {
			returnBrokenResource(jedis);
			e.printStackTrace();
		}finally {
			returnResource(jedis);
		}
	}
	
	/**
	 * tengh 2016年11月19日 下午4:16:53
	 * @param key
	 * @param member
	 * TODO 查询 sets 的score
	 */
	public static double zscore(String key,String member){
		Jedis jedis=null;
		try {
			jedis=getJedis();
			return jedis.zscore(key, member);
		} catch (Exception e) {
			try {
				jedis.zadd(key, 0, member);
			} catch (Exception e2) {
				returnBrokenResource(jedis);
				e.printStackTrace();
			}
		}finally {
			returnResource(jedis);
		}
		return 0;
	}
	
	/**
	 * tengh 2016年11月19日 下午5:20:32
	 * @param key
	 * @param member
	 * @return
	 * TODO 从高往低 按score排序
	 */
	public static Set<String> zrevrangeByScore(String key,double max,double min,int page,int length){
		Jedis jedis=null;
		try {
			jedis=getJedis();
			return jedis.zrevrangeByScore(key, max, min, page*length, length);
		} catch (Exception e) {
			returnBrokenResource(jedis);
			e.printStackTrace();
		}finally {
			returnResource(jedis);
		}
		return null;
	}
	
	/**
	 * tengh 2016年11月22日 下午3:33:27
	 * @param key
	 * @param members
	 * @return
	 * TODO 从Sort Sets中删除元素
	 */
	public static long zrem(String key,String...members){
		Jedis jedis=null;
		try {
			jedis=getJedis();
			return jedis.zrem(key, members);
		} catch (Exception e) {
			returnBrokenResource(jedis);
			e.printStackTrace();
		}finally {
			returnResource(jedis);
		}
		return 0;
	}
	
	public static void main(String[] args) {
		del("test");
		System.err.println(get("test"));
	}

}
