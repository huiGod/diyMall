package cn._51app.service.diy2_0.impl;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn._51app.dao.diy2_0.IZeroGoodDao;
import cn._51app.dao.ocs.OCSDao;
import cn._51app.service.diy2_0.IZeroGoodsService;
import cn._51app.util.CreateBarcodeByZxing;
import cn._51app.util.DateUtil;
import cn._51app.util.JSONUtil;
import cn._51app.util.OCSKey;
import cn._51app.util.PropertiesUtil;
import cn._51app.util.RedisUtil;

@Service
public class ZeroGoodsServiceImpl implements IZeroGoodsService {	

	@Autowired
	private IZeroGoodDao iZeroGoodDao;
	@Autowired
	private OCSDao ocsDao;
	private String preImgUrl=PropertiesUtil.getValue("diy.goods.url");
	private DecimalFormat df=new DecimalFormat("#####0.00");
	
	@Override
	public String zeroGoods() {
		String json=ocsDao.query(OCSKey.DIY_ZERO_GOODS);
		if(StringUtils.isBlank(json)){
			List<Map<String, Object>> zeroGoodsList=this.iZeroGoodDao.zeroGoods();
			if(zeroGoodsList==null || zeroGoodsList.isEmpty()){
				return null;
			}
			for (int i = 0; i < zeroGoodsList.size(); i++) {
				String icoUrl=(String)zeroGoodsList.get(i).get("icoUrl");
				double orgPrice=(Double)zeroGoodsList.get(i).get("orgPrice");
				String pre_url=(String)zeroGoodsList.get(i).get("pre_url");
				String coverImg=(String)zeroGoodsList.get(i).get("coverImg");
				zeroGoodsList.get(i).put("pre_url", preImgUrl+pre_url);
				zeroGoodsList.get(i).put("coverImg", preImgUrl+coverImg);
				zeroGoodsList.get(i).put("icoUrl", preImgUrl+icoUrl);
				zeroGoodsList.get(i).put("orgPrice", df.format(orgPrice));
				zeroGoodsList.get(i).remove("sort");
			}
			json=JSONUtil.convertArrayToJSON(zeroGoodsList);
			ocsDao.insert(OCSKey.DIY_ZERO_GOODS, json,0);
		}
		return json;
	}
	
	@Override
	public String workListByPage(int page, String type, Integer userId) {
		List<Map<String, Object>> result=new ArrayList<>();
		Map<String, Object> temMap=null;
		Set<String> paraTemp=null;
		if("new".equals(type) || "hot".equals(type)){
			if("hot".equals(type)){
				paraTemp=RedisUtil.zrevrangeByScore(OCSKey.DIY_WORKLIST_LIKE,1000000,0,page,20);
				System.err.println("page:"+page+",paraTemp:"+paraTemp.toString());
			}
		}else if("friend".equals(type)){
			paraTemp=RedisUtil.smembers(OCSKey.DIY_USER_FRIENDIDS_+userId);
		}
			List<Map<String, Object>> list=this.iZeroGoodDao.workListByPage(type,page,paraTemp,userId);
			if(list==null || list.size()<=0){
				return null;
			}
			for (int i = 0; i < list.size(); i++) {
				temMap=new HashMap<>();
				String imgurl=(String)list.get(i).get("imgurl");
				Integer id=(Integer)list.get(i).get("id");
				String name=(String)list.get(i).get("name");
				String cont=(String)list.get(i).get("cont");
				String suffix=(String)list.get(i).get("suffix");
				Integer goodsId=(Integer)list.get(i).get("goodsId");
				String mobile=(String)list.get(i).get("mobile");
				String headUrl=(String)list.get(i).get("headUrl");
				double money=(Double)list.get(i).get("money");
				double orgPrice=(Double)list.get(i).get("orgPrice");
//				Integer heart=(Integer)list.get(i).get("heart");
				String userName=(String)list.get(i).get("userName");
				Integer type_=(Integer)list.get(i).get("type");
				Date ctime=(Date)list.get(i).get("ctime");
				Date endtime=(Date)list.get(i).get("endtime");
				Date nowtime=(Date)list.get(i).get("nowtime");
				Integer cutStatus=(Integer)list.get(i).get("cutStatus");
				temMap.put("id", id);
				temMap.put("name", StringUtils.isBlank(name)?"未命名作品":name);
				temMap.put("money", df.format(money));
				temMap.put("orgPrice", df.format(orgPrice));
				temMap.put("cont", StringUtils.isBlank(cont)?"这家伙很懒，什么都没写":cont);
				temMap.put("imgUrl", preImgUrl+imgurl+"@b");
				temMap.put("suffix", suffix);
				temMap.put("goodsId", goodsId);
				temMap.put("mobile", (StringUtils.isBlank(mobile) || "0".equals(mobile))?"":mobile.subSequence(0, 3)+"****"+mobile.substring(7));
				temMap.put("headUrl", headUrl==null?"":preImgUrl+headUrl);
//				temMap.put("heart", heart);
				temMap.put("userName", userName==null?"":userName);
				temMap.put("ctime", DateUtil.date2String(ctime,DateUtil.FORMAT_DATETIME));
				temMap.put("nowtime", nowtime.getTime());
				temMap.put("endtime", endtime==null?"":endtime.getTime());
				if(type_==3){
					temMap.put("isZeroGood", true);
					if(cutStatus!=3 && nowtime.getTime()>endtime.getTime()){
						cutStatus=2;
					}
					temMap.put("cutStatus", cutStatus);
				}else{
					temMap.put("cutStatus", 0);
					temMap.put("isZeroGood", false);
				}
				if(userId!=null && RedisUtil.sismember(OCSKey.DIY_CUTPRICE_USREIDS_+id, userId+"")){
					temMap.put("hadCut", true);
				}else{
					temMap.put("hadCut", false);
				}
				result.add(temMap);
			}
		//加上点赞数  和是否点过赞
		for (int i = 0; i < result.size(); i++) {
			Integer id=(Integer)result.get(i).get("id");
			result.get(i).put("heart", RedisUtil.zscore(OCSKey.DIY_WORKLIST_LIKE, id+""));
			boolean isLike=false;
			if(userId!=null){
				isLike=RedisUtil.sismember(OCSKey.DIY_WORKLIST_USREIDS_+id, userId+"");
			}
			result.get(i).put("isLike", isLike);
			boolean isZeroGood=(Boolean)result.get(i).get("isZeroGood");
			if(isZeroGood){
				result.get(i).put("friends", RedisUtil.lrange(OCSKey.DIY_CUTPRICE_USRELIST_+id, 0, -1));
			}
		}
		return JSONUtil.convertArrayToJSON(result);
	}
	
	@Override
	public String workList(Integer userId) {
		String newList= workListByPage(0, "new", userId);
		String hotList= workListByPage(0, "hot", userId);
		String friendList= workListByPage(0, "friend", userId);
		Map<String, Object> result=new HashMap<>();
		result.put("newList",JSONUtil.convertJSONToList(newList));
		result.put("hotList", JSONUtil.convertJSONToList(hotList));
		result.put("friendList", JSONUtil.convertJSONToList(friendList));
		return JSONUtil.convertObjectToJSON(result);
	}
	
	@Override
	public boolean doLike(Integer id, Integer userId,String type) {
		if("add".equals(type)){
			RedisUtil.zincrby(OCSKey.DIY_WORKLIST_LIKE, 1, id+"");
			RedisUtil.sadd(OCSKey.DIY_WORKLIST_USREIDS_+id, userId+"");
		}else if("delete".equals(type)){
			RedisUtil.zincrby(OCSKey.DIY_WORKLIST_LIKE, -1, id+"");
			RedisUtil.srem(OCSKey.DIY_WORKLIST_USREIDS_+id, userId+"");
		}else{
			return false;
		}
		return true;
	}
	
	@Override
	public boolean doFriend(String userId, String friendId, String type) {
		if("add".equals(type) && !userId.equals(friendId)){
			boolean flag=RedisUtil.sismember(OCSKey.DIY_USER_FRIENDIDS_+userId, friendId);
			if(flag){
				return false;
			}
			RedisUtil.sadd(OCSKey.DIY_USER_FRIENDIDS_+userId, friendId);
			RedisUtil.sadd(OCSKey.DIY_USER_FRIENDIDS_+friendId, userId);
		}else if("delete".equals(type)){
			RedisUtil.srem(OCSKey.DIY_USER_FRIENDIDS_+userId, friendId);
			RedisUtil.srem(OCSKey.DIY_USER_FRIENDIDS_+friendId, userId);
		}else{
			return false;
		}
		return true;
	}
	
	@Override
	public boolean doFriendByWork(String userId, String id) {
		Integer friendId=(Integer)this.iZeroGoodDao.getFriendIdByWorkId(Integer.parseInt(id));
		RedisUtil.sadd(OCSKey.DIY_USER_FRIENDIDS_+userId, friendId+"");
		RedisUtil.sadd(OCSKey.DIY_USER_FRIENDIDS_+friendId, userId);
		return true;
	}
	
	@Override
	public Map<String, Object> goodsInfo(String id,String userId) {
		Map<String, Object> result=new HashMap<>();
		String msg="";
		boolean flag=false;
		Map<String, Object> map=this.iZeroGoodDao.goodsInfo(id);
		if(map==null || map.isEmpty()){
			msg="没有找到该作品，请确认作品信息正确";
		}else{
			Integer status=(Integer)map.get("status");
			if(status==0){
				msg="您来晚了，作品已被发布者删除";
			}else{
				String name=(String)map.get("name");
				map.put("name", StringUtils.isBlank(name)?"未命名作品":name);
				String cont=(String)map.get("cont");
				map.put("cont", StringUtils.isBlank(cont)?"这家伙很懒，什么都没写":cont);
				Integer cutStatus=(Integer)map.get("cutStatus");
				Integer type=(Integer)map.get("type");
				double money=(Double)map.get("money");
				map.put("money", df.format(money));
				double orgPrice=(Double)map.get("orgPrice");
				map.put("orgPrice", df.format(orgPrice));
				map.put("ctime", DateUtil.date2String((Date)map.get("ctime"),DateUtil.FORMAT_DATETIME));
				Date endtime=(Date)map.get("endtime");
				map.put("endtime", endtime.getTime());
				String imgUrl=(String)map.get("imgUrl");
				map.put("imgUrl", preImgUrl+imgUrl+"@b");
				Date nowtime=(Date)map.get("nowtime");
				map.put("nowtime", nowtime.getTime());
				String userName=(String)map.get("userName");
				map.put("userName", userName==null?"":userName);
				String headUrl=(String)map.get("headUrl");
				map.put("headUrl",headUrl==null?"":preImgUrl+headUrl);
				String mobile=(String)map.get("mobile");
				map.put("mobile",(StringUtils.isBlank(mobile)||"0".equals(mobile))?"":mobile.substring(0,3)+"****"+mobile.substring(7));
				if(type==3){
					if(cutStatus!=3 && nowtime.getTime()>endtime.getTime()){
						cutStatus=2;
					}
				}else{
					cutStatus=0;
				}
				if(userId!=null && RedisUtil.sismember(OCSKey.DIY_CUTPRICE_USREIDS_+id, userId+"")){
					map.put("hadCut", true);
				}else{
					map.put("hadCut", false);
				}
				map.put("cutStatus", cutStatus);
				if(cutStatus==3){
					money=orgPrice;
				}
				map.put("friends", RedisUtil.lrange(OCSKey.DIY_CUTPRICE_USRELIST_+id, 0, -1));
				flag=true;
			}
		}
		result.put("msg", msg);
		result.put("flag", flag);
		result.put("data", JSONUtil.convertObjectToJSON(map));
		return result;
	}
	
	@Override
	public String userWorkById(String id) throws Exception {
		Map<String, Object> info=this.iZeroGoodDao.userWorkById(id);
		String headUrl=(String)info.get("headUrl");
		String mobile=(String)info.get("mobile");
		String name=(String)info.get("name");
		String workName=(String)info.get("workName");
		String cont=(String)info.get("cont");
		double orgPrice=(Double)info.get("orgPrice");
		double money=(Double)info.get("money");
		String imgurl=(String)info.get("imgurl");
		info.put("imgurl", preImgUrl+imgurl+"@b");
		info.put("orgPrice", df.format(orgPrice));
		info.put("money", df.format(money));
		info.put("headUrl", headUrl==null?"":preImgUrl+headUrl);
		info.put("name",name==null?"":name);
		info.put("mobile", (StringUtils.isBlank(mobile)||"0".equals(mobile))?"":mobile.substring(0,3)+"****"+mobile.substring(7));
		info.put("workName", StringUtils.isBlank(workName)?"未命名作品":workName);
		info.put("cont", StringUtils.isBlank(cont)?"这家伙很懒，什么都没写":cont);
		String urlPath="http://api.diy.51app.cn/diyMall/zero/scanGood.do?id="+id;
		String path="/data/resource/diymall/barCode/"+id+".png";
		if(!new File(path).exists()){
			 CreateBarcodeByZxing.CreateBarcode(urlPath, path);
		 }
		info.put("barCodeUrl", preImgUrl+"barCode/"+id+".png");
		return JSONUtil.convertObjectToJSON(info);
	}
	
	@Override
	public String myWorkList(Integer userId) {
		String normalList=myWorkListByPage(userId, "normal", 0);
		String allList=myWorkListByPage(userId, "all", 0);
		String zeroList=myWorkListByPage(userId, "zero", 0);
		Map<String, Object> result=new HashMap<>();
		result.put("normalList", JSONUtil.convertJSONToList(normalList));
		result.put("allList", JSONUtil.convertJSONToList(allList));
		result.put("zeroList", JSONUtil.convertJSONToList(zeroList));
		return JSONUtil.convertObjectToJSON(result);
	}
	
	@Override
	public String myWorkListByPage(Integer userId, String type,int page) {
		List<Map<String, Object>> list=this.iZeroGoodDao.myWorkListByPage(userId,type,page);
		if(list==null || list.size()<=0){
			return null;
		}
		for (int i = 0; i < list.size(); i++) {
			Integer type_=(Integer)list.get(i).get("type");
			Integer isopen=(Integer)list.get(i).get("isopen");
			Integer id=(Integer)list.get(i).get("id");
			String name=(String)list.get(i).get("name");
			String mobile=(String)list.get(i).get("mobile");
			String cont=(String)list.get(i).get("cont");
//			Integer heart=(Integer)list.get(i).get("heart");
			String userName=(String)list.get(i).get("userName");
			String imgUrl=(String)list.get(i).get("imgUrl");
			String suffix=(String)list.get(i).get("suffix");
			Date ctime=(Date)list.get(i).get("ctime");
			Date endtime=(Date)list.get(i).get("endtime");
			Date nowtime=(Date)list.get(i).get("nowtime");
			Integer cutStatus=(Integer)list.get(i).get("cutStatus");
			double orgPrice=(Double)list.get(i).get("orgPrice");
			double money=(Double)list.get(i).get("money");
			list.get(i).put("orgPrice", df.format(orgPrice));
			list.get(i).put("money", df.format(money));
			list.get(i).put("mobile", (StringUtils.isBlank(mobile) || "0".equals(mobile))?"":mobile.subSequence(0, 3)+"****"+mobile.substring(7));
			list.get(i).put("imgUrl", preImgUrl+imgUrl+"@b");
			list.get(i).put("suffix", suffix);
			list.get(i).put("userName", userName==null?"":userName);
			list.get(i).put("name", StringUtils.isBlank(name)?"未命名作品":name);
			list.get(i).put("cont", StringUtils.isBlank(name)?"这家伙很懒，什么都没写":cont);
			list.get(i).put("ctime", DateUtil.date2String(ctime,DateUtil.FORMAT_DATETIME));
			list.get(i).put("endtime", endtime.getTime());
			list.get(i).put("heart", RedisUtil.zscore(OCSKey.DIY_WORKLIST_LIKE, id+""));
			list.get(i).put("friends", RedisUtil.lrange(OCSKey.DIY_CUTPRICE_USRELIST_+id, 0, -1));
			list.get(i).put("nowtime", nowtime.getTime());
			list.get(i).put("isopen", isopen);
			boolean isZeroGood=false;
			if(type_==3){
				isZeroGood=true;
				if(cutStatus!=3 && nowtime.getTime()>endtime.getTime()){
					cutStatus=2;
				}
			}else{
				cutStatus=0;
			}
			list.get(i).put("cutStatus", cutStatus); //1. 砍价中 2.砍价结束 3.已购买
			list.get(i).put("isZeroGood", isZeroGood);
			boolean isLike=false;
			if(userId!=null){
				isLike=RedisUtil.sismember(OCSKey.DIY_WORKLIST_USREIDS_+id, userId+"");
			}
			list.get(i).put("isLike", isLike);
		}
		List<Map<String, Object>> result=new ArrayList<>();
		Map<String, List<Map<String, Object>>> daysMap=new HashMap<>();
		for (Map<String, Object> map : list) {
			long daydiff=DateUtil.dateDiff((String)map.get("ctime"),DateUtil.date2String(new Date(), DateUtil.FORMAT_DATE), DateUtil.FORMAT_DATE, "day");
			List<Map<String, Object>> temList=daysMap.get(daydiff+"");
			if(daydiff==1){//今日
				if(temList==null || temList.isEmpty()){
					temList=new ArrayList<>();
				}
				temList.add(map);
				daysMap.put("1", temList);
			}else if(daydiff==0){//昨日
				if(temList==null || temList.isEmpty()){
					temList=new ArrayList<>();
				}
				temList.add(map);
				daysMap.put("0", temList);
			}else{
				String monthNum=DateUtil.date2String(DateUtil.string2Date((String)map.get("ctime"), DateUtil.FORMAT_DATE), DateUtil.FORMAT_DIRMONTH);
				List<Map<String, Object>> monthList=daysMap.get(monthNum);
				if(monthList==null || monthList.isEmpty()){
					monthList=new ArrayList<>();
				}
				monthList.add(map);
				daysMap.put(monthNum, monthList);
			}
		}
		Map<String, Object> temMap=new TreeMap<>(new MapKeyComparator());
		temMap.putAll(daysMap);
		Map<String, Object> tem=null;
		for (Entry<String, Object> entry:temMap.entrySet()) {
			tem=new HashMap<>();
			String key=entry.getKey();
			if("1".equals(key)){
				tem.put("date", "昨日");
			}else if("0".equals(key)){
				tem.put("date", "今日");
			}else{
				tem.put("date", key+"月");
			}
			tem.put("data", entry.getValue());
			result.add(tem);
		}
		return JSONUtil.convertArrayToJSON(result);
	}
	
	class MapKeyComparator implements Comparator<String>{

		@Override
		public int compare(String str1, String str2) {
			
			return str1.compareTo(str2);
		}
	}
	
	@Override
	public boolean delWorkList(int id, Integer userId) {
		int result=this.iZeroGoodDao.delWorkList(id,userId);
		if(result>0){
			//删除点赞数
			RedisUtil.zrem(OCSKey.DIY_WORKLIST_LIKE,id+"");
			//删除跟此作品集点过赞的用户
//			RedisUtil.del(OCSKey.DIY_WORKLIST_USREIDS_+id);
			//删除砍过价的用户
//			RedisUtil.srem(OCSKey.DIY_CUTPRICE_USREIDS_+id, userId+"");
			//删除砍过价的用户列表信息
//			RedisUtil.del(OCSKey.DIY_CUTPRICE_USRELIST_+id);
			return true;
		}
		return false;
	}
	
	@Override
	public Map<String, Object> cutPrice(int id, Integer userId, String ip,String mobile,String cutCouponId) {
		Integer friendId=this.iZeroGoodDao.getFriendIdByWorkId(id);
		Map<String, Object> result=new HashMap<>();
		String msg="砍价失败";
		boolean flag=false;
		//是否曾经砍价过
		if("0".equals(mobile)){
			msg="请先绑定手机号,才能进行砍价~";
		}else if(!this.iZeroGoodDao.checkCutTime(id)){
			msg="砍价时间已过哦~";
		}else if(this.iZeroGoodDao.checkWork(id,userId)){
			msg="您不能自己给自己砍价哦~";
		}else if(!RedisUtil.sismember(OCSKey.DIY_USER_FRIENDIDS_+friendId, userId+"")){
			msg="您还不是他/她的好友~";
		}else if(RedisUtil.sismember(OCSKey.DIY_CUTPRICE_USREIDS_+id, userId+"")){
			msg="您已经帮忙砍过价啦~";
		}else{
			//删除用户砍价券id列表中的id  (不能删除  要查看过去的砍价券)
//			long remResult=RedisUtil.lrem(OCSKey.DIY_COUPONLIST_USERID_+userId, 0, cutCouponId);
			String cutCouponJson= RedisUtil.hget(OCSKey.DIY_CUTPRICE_COUPONLIST, cutCouponId);
			Map<String, String> cutCouponMap=(Map<String, String>)JSONUtil.convertJSONToObject(cutCouponJson, HashMap.class);
			if("true".equals(cutCouponMap.get("status")) && (userId+"").equals(cutCouponMap.get("userId"))){
				//将砍价券状态改成已使用
				cutCouponMap.put("status", "false");
				RedisUtil.hset(OCSKey.DIY_CUTPRICE_COUPONLIST,cutCouponId+"", JSONUtil.convertObjectToJSON(cutCouponMap));
					if(DateUtil.dateDiff(cutCouponMap.get("valid"), DateUtil.date2String(new Date(), DateUtil.FORMAT_DATE), DateUtil.FORMAT_DATE, "day")>0){
						//优惠券 过期失效
						msg="优惠券已失效~";
					}else{ 
						double randMoney=Double.valueOf(cutCouponMap.get("nowPrice"));
						int resultCut= this.iZeroGoodDao.cutPrice(id,randMoney);
						if(resultCut>0){
							flag=true;
							msg="您成功帮忙砍"+randMoney+"元";
							//砍价用户id
							RedisUtil.sadd(OCSKey.DIY_CUTPRICE_USREIDS_+id, userId+"");
							Map<String, Object> personInfo=this.iZeroGoodDao.personInfo(userId);
							String name=(String)personInfo.get("name");
							String headUrl=(String)personInfo.get("headUrl");
							Date ctime=(Date)personInfo.get("ctime");
							personInfo.put("headUrl", headUrl==null?"":preImgUrl+headUrl);
							personInfo.put("name", name==null?"":name);
							personInfo.put("ctime", DateUtil.date2String(ctime,DateUtil.FORMAT_DATEHOUR_MINUTE));
							personInfo.put("money",randMoney);
							//砍价用户信息列表
							RedisUtil.rpush(OCSKey.DIY_CUTPRICE_USRELIST_+id,JSONUtil.convertObjectToJSON(personInfo));
						}
					}
				}else{
					msg="数据异常";
				}
		}
		result.put("flag", flag);
		result.put("msg", msg);
		return result;
	}
	
	@Override
	public String friends(Integer userId) {
		Set<String> ids=RedisUtil.smembers(OCSKey.DIY_USER_FRIENDIDS_+userId);
		if(ids.isEmpty()){
			return null;
		}
		List<Map<String, Object>> list=this.iZeroGoodDao.friends(ids);
		for (int i = 0; i < list.size(); i++) {
			String headUrl=(String)list.get(i).get("headUrl");
			String name=(String)list.get(i).get("name");
			list.get(i).put("headUrl", headUrl==null?"":preImgUrl+headUrl);
			list.get(i).put("name", name==null?"":name);
		}
		return JSONUtil.convertArrayToJSON(list);
	}
	
	@Override
	public Map<String, Object> getPersonInfo(String id) {
		Map<String, Object> info=this.iZeroGoodDao.getPersonInfo(id);
		String name=(String)info.get("name");
		String headUrl=(String)info.get("headUrl");
		info.put("name", name==null?"":name);
		info.put("headUrl", headUrl==null?"":preImgUrl+headUrl);
		return info;
	}
	
	@Override
	public Map<String, Object> endCutprice(String id, String userId) {
		Map<String, Object> result=new HashMap<>();
		double money=0;
		boolean flag=false;
		Map<String, Object> map=this.iZeroGoodDao.getWorkInfo(id,userId);
		boolean checkResult=this.iZeroGoodDao.endCutprice(id);
		if(checkResult){
			flag=true;
			money=(Double)map.get("money");
		}
		result.put("flag", flag);
		result.put("money", df.format(money));
		return result;
	}
	
	@Override
	public boolean editWorkList(String userId, String id, String type) {
		if(!"0".equals(type) && !"1".equals(type) && !"2".equals(type) && !"3".equals(type)){
			return false;
		}
		return this.iZeroGoodDao.editWorkList(id,userId,type);
	}
	
	public static void main(String[] args) {
		List<Map<String, Object>> list=new ArrayList<>();
		
	}
}
