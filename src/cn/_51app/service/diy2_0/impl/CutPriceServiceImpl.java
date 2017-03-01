package cn._51app.service.diy2_0.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import cn._51app.entity.CutPriceCoupon;
import cn._51app.service.diy2_0.ICutPriceService;
import cn._51app.util.DateUtil;
import cn._51app.util.JSONUtil;
import cn._51app.util.OCSKey;
import cn._51app.util.RedisUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

@Service
public class CutPriceServiceImpl implements ICutPriceService {	

	@Override
	public boolean addCutCoupon(CutPriceCoupon cutPriceCoupon) {
		long cutPriceId=RedisUtil.scard(OCSKey.DIY_CUTPRICE_IDS);
		cutPriceId=cutPriceId+1;
		RedisUtil.sadd(OCSKey.DIY_CUTPRICE_IDS, cutPriceId+"");
		Map<String, String> hash=new HashMap<>();
		hash.put("id", cutPriceId+"");
		hash.put("nowPrice", cutPriceCoupon.getNowPrice());
		hash.put("name", cutPriceCoupon.getName());
		hash.put("info", cutPriceCoupon.getInfo());
		hash.put("valid", cutPriceCoupon.getValid());
		String result=RedisUtil.hmset(OCSKey.DIY_CUTPRICE_ID_+cutPriceId,hash);
		return (result==null || !"OK".equals(result))?false:true;
	}
	
	@Override
	public boolean editCutCoupon(CutPriceCoupon cutPriceCoupon) {
		Map<String, String> hash=new HashMap<>();
		hash.put("id", cutPriceCoupon.getId());
		hash.put("nowPrice", cutPriceCoupon.getNowPrice());
		hash.put("name", cutPriceCoupon.getName());
		hash.put("info", cutPriceCoupon.getInfo());
		hash.put("valid", cutPriceCoupon.getValid());
		String result=RedisUtil.hmset(OCSKey.DIY_CUTPRICE_ID_+cutPriceCoupon.getId(),hash);
		return (result==null || !"OK".equals(result))?false:true;
	}
	
	@Override
	public boolean delCutCoupon(String id) {
		long result=RedisUtil.srem(OCSKey.DIY_CUTPRICE_IDS, id);
		if(result>0){
			RedisUtil.del(OCSKey.DIY_CUTPRICE_ID_+id);
			return true;
		}
		return false;
	}
	
	@Override
	public String myCutCoupon(String userId) {
		List<String> list=RedisUtil.lrange(OCSKey.DIY_COUPONLIST_USERID_+userId,0,-1);
		if(list==null || list.size()<=0){
			return null;
		}
		String[] array = new String[list.size()];
		list.toArray(array);
		List<String> resultStr=RedisUtil.hmget(OCSKey.DIY_CUTPRICE_COUPONLIST, array);
		Map<String, Object> result=new HashMap<>();
		List<Map<String, Object>> useList=new ArrayList<>();
		List<Map<String, Object>> validList=new ArrayList<>();
		Map<String, Object> temp=null;
		for (String str : resultStr) {
			temp=new HashMap<>();
			temp=(Map<String, Object>)JSONUtil.convertJSONToObject(str, HashMap.class);
			temp.remove("userId");
			temp.remove("islottery");
			String status=(String)temp.get("status");
			String valid=(String)temp.get("valid");
			String now=DateUtil.date2String(DateUtil.getCurrentDate(), DateUtil.FORMAT_DATE);
			long diff=DateUtil.dateDiff(now, valid, DateUtil.FORMAT_DATE, "day");
			if("true".equals(status) && diff>=0){
				useList.add(temp);
			}else{
				validList.add(temp);
			}
		}
		result.put("useList", useList);
		result.put("validList", validList);
		return JSONUtil.convertObjectToJSON(result);
	}
	
	@Override
	public Map<String, Object> checkCutCoupon(String userId) {
		Map<String, Object> result=new HashMap<>();
		boolean flag=true;
		String msg="";
		String lastId=RedisUtil.lindex(OCSKey.DIY_COUPONLIST_USERID_+userId, 0);
		if(StringUtils.isNotBlank(lastId)){
			String lastCouponJson=RedisUtil.hget(OCSKey.DIY_CUTPRICE_COUPONLIST, lastId);
			Map<String, String> lastCoupon=(Map<String, String>)JSONUtil.convertJSONToObject(lastCouponJson, HashMap.class);
			String lastTime=lastCoupon.get("ctime");
			String islottery=lastCoupon.get("islottery");
			//判断是不是一周以内的时间
			long diffDay=DateUtil.dateDiff(lastTime, DateUtil.date2String(DateUtil.getCurrentDate(),DateUtil.FORMAT_DATE), DateUtil.FORMAT_DATE, "day");
			if("true".equals(islottery) && diffDay<=6){
				msg="您本周已经领取过砍价券了，请在"+DateUtil.cutDayHM(DateUtil.string2Date(lastTime, DateUtil.FORMAT_DATE), 7)+"后再次领取。";
				flag=false;
			}
		}
		result.put("flag", flag);
		result.put("msg", msg);
		return result;
	}
	
	@Override
	public Map<String, Object> getCutCoupon(String userId) {
		Map<String, Object> result=new HashMap<>();
		boolean flag=true;
		String msg="数据异常";
		String cutCouponId="";
		//判断是否一周内已经领取过
		String lastId=RedisUtil.lindex(OCSKey.DIY_COUPONLIST_USERID_+userId, 0);
		if(StringUtils.isNotBlank(lastId)){
			String lastCouponJson=RedisUtil.hget(OCSKey.DIY_CUTPRICE_COUPONLIST, lastId);
			Map<String, String> lastCoupon=(Map<String, String>)JSONUtil.convertJSONToObject(lastCouponJson, HashMap.class);
			String lastTime=lastCoupon.get("ctime");
			String islottery=lastCoupon.get("islottery");
			//判断是不是一周以内的时间
			long diffDay=DateUtil.dateDiff(lastTime, DateUtil.date2String(DateUtil.getCurrentDate(),DateUtil.FORMAT_DATE), DateUtil.FORMAT_DATE, "day");
			if("true".equals(islottery) && diffDay<=6){
				msg="您本周已经领取过砍价券了，请在"+DateUtil.cutDayHM(DateUtil.string2Date(lastTime, DateUtil.FORMAT_DATE), 7)+"后再次领取。";
				flag=false;
			}
		}
		if(flag){
			while (true) {
				cutCouponId=RedisUtil.srandmember(OCSKey.DIY_CUTPRICE_IDS);
				if(!"12".equals(cutCouponId)){
					break;
				}
			}
			Map<String, String> cutCoupon=RedisUtil.hgetAll(OCSKey.DIY_CUTPRICE_ID_+cutCouponId);
			//生成自己的砍价券id  用List有顺序存起来
			long myCouponId=RedisUtil.incr(OCSKey.DIY_CUTPRICE_COUPONID);
			//插入用户砍价券id列表
			RedisUtil.lpush(OCSKey.DIY_COUPONLIST_USERID_+userId, myCouponId+"");
			cutCoupon.put("id", myCouponId+"");
			cutCoupon.put("islottery", "true"); //砍价券是否是抽奖获得的
			cutCoupon.put("ctime", DateUtil.date2String(DateUtil.getCurrentDate(),DateUtil.FORMAT_DATE));
			cutCoupon.put("status", "true"); //砍价券未使用
			cutCoupon.put("userId", userId); //砍价券属于用户id
			//插入用户砍价券信息
			RedisUtil.hset(OCSKey.DIY_CUTPRICE_COUPONLIST,myCouponId+"", JSONUtil.convertObjectToJSON(cutCoupon));
			flag=true;
			result.put("money",cutCoupon.get("nowPrice"));
		}
		result.put("flag", flag);
		result.put("couponId", cutCouponId);
		result.put("msg", msg);
		return result;
	}
	
	@Override
	public String coupons() {
		Set<String> ids=RedisUtil.smembers(OCSKey.DIY_CUTPRICE_IDS);
		if(ids.isEmpty()){
			return null;
		}
		Jedis jedis=null;
		List<Map<String, String>> result=new ArrayList<>();
		try {
			jedis=RedisUtil.getJedis();
			Pipeline pipeline=jedis.pipelined();
				for (String id : ids) {
					result.add(RedisUtil.hgetAll(OCSKey.DIY_CUTPRICE_ID_+id));
				}
			pipeline.sync();
			return JSONUtil.convertArrayToJSON(result);
		} catch (Exception e) {
			RedisUtil.returnBrokenResource(jedis);
			e.printStackTrace();
		}finally {
			RedisUtil.returnResource(jedis);
		}
		return null;
	}
	
}
