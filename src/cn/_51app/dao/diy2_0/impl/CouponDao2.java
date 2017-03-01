package cn._51app.dao.diy2_0.impl;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import cn._51app.dao.BaseDao;
import cn._51app.dao.diy2_0.ICouponDao2;
import cn._51app.entity.User;

@Repository
public class CouponDao2 extends BaseDao implements ICouponDao2 {
	
	private DecimalFormat df=new DecimalFormat("######0.00");
	
	private String couponHttp="http://api.diy.51app.cn/diyMall/coupon2/getCenterCoupon.do";
	private String couponHttpType="http://api.diy.51app.cn/diyMall/coupon2/getCenterCouponType.do";
	private String couponHttp2="http://api.diy.51app.cn/diyMall/coupon2/getCenterCouponForId.do";
	//id版本优惠券中心
	private String couponHttpForId="http://api.diy.51app.cn/diyMall/coupon2/getCenterCouponForId.do";

	@Override
	public Map<String,Object> couponList(String deviceNo, String app,String dgurl) throws Exception{
		Map<String,Object>map=new HashMap<String,Object>();
		String validSql="SELECT `dcu`.`id`,`dc`.`img`,`dc`.`store_id` AS `storeId`,`dcu`.`valid`,`dc`.`title`,`dc`.`about`,`dc`.`des_price` AS `desPrice`,`dc`.`org_price` AS `orgPrice`,dc.level FROM `diy_coupon_user` `dcu` INNER JOIN `diy_coupon` `dc` ON `dcu`.`coupon_id`=`dc`.`id` WHERE `dcu`.`status`=1 AND `dc`.`status`=1 AND `dcu`.`coupon_id`!=0 AND `dcu`.`deviceNo`=? AND `dcu`.`app`=? AND `dcu`.`valid`>=CURDATE() ORDER BY `dcu`.`creatime` DESC";
		String failureSql="SELECT `dcu`.`id`,`dc`.`img`,`dc`.`store_id` AS `storeId`,`dcu`.`valid`,`dc`.`title`,`dc`.`about`,`dc`.`des_price` AS `desPrice`,`dc`.`org_price` AS `orgPrice`,dc.level FROM `diy_coupon_user` `dcu` INNER JOIN `diy_coupon` `dc` ON `dcu`.`coupon_id`=`dc`.`id` WHERE `dcu`.`status`=1 AND `dc`.`status`=1 AND `dcu`.`coupon_id`!=0 AND `dcu`.`deviceNo`=? AND `dcu`.`app`=? AND `dcu`.`valid`<CURDATE() ORDER BY `dcu`.`creatime` DESC";
		List<Map<String, Object>> validList=this.jt.queryForList(validSql,new Object[]{deviceNo,app});
		//有效券
		for(Map<String,Object>vMap : validList){
			vMap.remove("level");
			vMap.put("couponImg",dgurl+vMap.get("img"));
			vMap.remove("img");
		}
		List<Map<String, Object>> failureList=this.jt.queryForList(failureSql,new Object[]{deviceNo,app});
		//失效券
		for(Map<String,Object>fMap : failureList){
			fMap.remove("level");
			fMap.put("couponImg",dgurl+fMap.get("img"));
			fMap.remove("img");
		}
		map.put("valid", validList);
		//没有失效券不显示
		if(!failureList.isEmpty())
		map.put("failure",failureList);
		return map;
	}
	
	@Override
	public Map<String,Object> couponListForId(String userId,String dgurl) throws Exception{
		Map<String,Object>map=new HashMap<String,Object>();
		String validSql="SELECT `dcu`.`id`,`dc`.`img`,`dc`.`store_id` AS `storeId`,`dcu`.`valid`,`dc`.`title`,`dc`.`about`,`dc`.`des_price` AS `desPrice`,`dc`.`org_price` AS `orgPrice`,dc.level FROM `diy_coupon_user` `dcu` INNER JOIN `diy_coupon` `dc` ON `dcu`.`coupon_id`=`dc`.`id` WHERE `dcu`.`status`=1 AND `dc`.`status`=1 AND `dcu`.`coupon_id`!=0 AND `dcu`.`userId`=? AND `dcu`.`valid`>=CURDATE() ORDER BY `dcu`.`creatime` DESC";
		String failureSql="SELECT `dcu`.`id`,`dc`.`img`,`dc`.`store_id` AS `storeId`,`dcu`.`valid`,`dc`.`title`,`dc`.`about`,`dc`.`des_price` AS `desPrice`,`dc`.`org_price` AS `orgPrice`,dc.level FROM `diy_coupon_user` `dcu` INNER JOIN `diy_coupon` `dc` ON `dcu`.`coupon_id`=`dc`.`id` WHERE `dcu`.`status`=1 AND `dc`.`status`=1 AND `dcu`.`coupon_id`!=0 AND `dcu`.`userId`=? AND `dcu`.`valid`<CURDATE() ORDER BY `dcu`.`creatime` DESC";
		List<Map<String, Object>> validList=this.jt.queryForList(validSql,new Object[]{userId});
		//有效券
		for(Map<String,Object>vMap : validList){
			vMap.remove("level");
			vMap.put("couponImg",dgurl+vMap.get("img"));
			vMap.remove("img");
		}
		List<Map<String, Object>> failureList=this.jt.queryForList(failureSql,new Object[]{userId});
		//失效券
		for(Map<String,Object>fMap : failureList){
			fMap.remove("level");
			fMap.put("couponImg",dgurl+fMap.get("img"));
			fMap.remove("img");
		}
		map.put("valid", validList);
		//没有失效券不显示
		if(!failureList.isEmpty())
		map.put("failure",failureList);
		return map;
	}
	
	@Override
	public String couponCenter(Map<String,Object>paramMap) throws Exception{
		String deviceNo=paramMap.get("deviceNo")==null?"":paramMap.get("deviceNo").toString();
		String app=paramMap.get("app")==null?"":paramMap.get("app").toString();
		String dgurl=paramMap.get("dgurl").toString();
		String sql="SELECT id,valid,title,about,des_price AS desPrice,org_price AS orgPrice,count,img FROM diy_coupon WHERE type=2 AND status=1";
		List<Map<String,Object>>list=this.jt.queryForList(sql);
		for(Map<String,Object>m : list){
			String valid=m.get("valid").toString();
			String id=m.get("id").toString();
			//封装查询用户是否领过券
			paramMap.put("id",id);
			if(isCachCoupon(paramMap)){
				m.put("isGet",1);
			}else{
				m.put("isGet",0);
			}			
			m.put("couponImg",dgurl+m.get("img"));
			//领取中心优惠券接口
			String http=couponHttp+"?deviceNo="+deviceNo+"&app="+app+"&valid="+valid+"&id="+id;
			m.put("getCenterCoupon",http);
			m.put("desPrice",df.format(m.get("desPrice")));
			m.put("orgPrice",df.format(m.get("orgPrice")));
			m.put("deviceNo", deviceNo);
			m.put("app",app);
		}
		return super.toJson(list);
	}
	
	@Override
	public String couponCenterType(Map<String,Object>paramMap) throws Exception{
		String deviceNo=paramMap.get("deviceNo")==null?"":paramMap.get("deviceNo").toString();
		String app=paramMap.get("app")==null?"":paramMap.get("app").toString();
		String dgurl=paramMap.get("dgurl").toString();
		String type=paramMap.get("type").toString();
		String sql="SELECT id,valid,title,about,des_price AS desPrice,org_price AS orgPrice,count,img FROM diy_coupon WHERE type="+type+" AND status=1";
		List<Map<String,Object>>list=this.jt.queryForList(sql);
		for(Map<String,Object>m : list){
			String valid=m.get("valid").toString();
			String id=m.get("id").toString();
			//封装查询用户是否领过券
			paramMap.put("id",id);
			if(isCachCouponType(paramMap)){
				m.put("isGet",1);
			}else{
				m.put("isGet",0);
			}			
			m.put("couponImg",dgurl+m.get("img"));
			//领取中心优惠券接口
			String http=couponHttpType+"?deviceNo="+deviceNo+"&app="+app+"&valid="+valid+"&id="+id+"&type="+type;
			m.put("getCenterCoupon",http);
			m.put("desPrice",df.format(m.get("desPrice")));
			m.put("orgPrice",df.format(m.get("orgPrice")));
			m.put("deviceNo", deviceNo);
			m.put("app",app);
		}
		return super.toJson(list);
	}
	
	@Override
	public String couponCenterForId(Map<String,Object>paramMap) throws Exception{
		String userId=paramMap.get("userId")==null?"":paramMap.get("userId").toString();
		String dgurl=paramMap.get("dgurl").toString();
		String sql="SELECT `dc`.`id`,`dc`.`valid`,`dc`.`title`,`dc`.`store_id`,`dc`.`about`,`dc`.`des_price` AS desPrice,`dc`.`org_price` AS orgPrice,`dc`.`count`,`dc`.`img`,`dsu`.`name` AS `storeName` FROM `diy_coupon` `dc` LEFT JOIN `diy_sys_user` `dsu` ON `dsu`.`id`=`dc`.`store_id` WHERE type=2 AND status=1 AND valid>=CURDATE()";
		List<Map<String,Object>>list=this.jt.queryForList(sql);
		for(Map<String,Object>m : list){
			String valid=m.get("valid").toString();
			String id=m.get("id").toString();
			//封装查询用户是否领过券
			paramMap.put("id",id);
			if(isCachCouponForId(paramMap)){
				m.put("isGet",1);
			}else{
				m.put("isGet",0);
			}			
			m.put("couponImg",dgurl+m.get("img"));
			//领取中心优惠券接口,id=优惠券id
			String http=couponHttp2+"?userId="+userId+"&valid="+valid+"&id="+id;
			m.put("getCenterCoupon",http);
			m.put("desPrice",df.format(m.get("desPrice")));
			m.put("orgPrice",df.format(m.get("orgPrice")));
			m.put("userId", userId);
		}
		return super.toJson(list);
	}
	
	@Override
	public String getAiSiAndOldCouponList(Map<String,Object>paramMap) throws Exception{
		String userId=paramMap.get("userId")==null?"":paramMap.get("userId").toString();
		String dgurl=paramMap.get("dgurl").toString();
		String sql="SELECT `dc`.`id`,`dc`.`valid`,`dc`.`title`,`dc`.`store_id`,`dc`.`about`,`dc`.`des_price` AS desPrice,`dc`.`org_price` AS orgPrice,`dc`.`count`,`dc`.`img`,`dsu`.`name` AS `storeName` FROM `diy_coupon` `dc` LEFT JOIN `diy_sys_user` `dsu` ON `dsu`.`id`=`dc`.`store_id` WHERE type=:type AND status=1 AND valid>=CURDATE()";
		List<Map<String,Object>>list=this.npjt.queryForList(sql,paramMap);
		for(Map<String,Object>m : list){
			String valid=m.get("valid").toString();
			String id=m.get("id").toString();
			//封装查询用户是否领过券
			paramMap.put("id",id);
			if(isCachCouponForId(paramMap)){
				m.put("isGet",1);
			}else{
				m.put("isGet",0);
			}			
			m.put("couponImg",dgurl+m.get("img"));
			//领取中心优惠券接口,id=优惠券id
			String http=couponHttp2+"?userId="+userId+"&valid="+valid+"&id="+id;
			m.put("getCenterCoupon",http);
			m.put("desPrice",df.format(m.get("desPrice")));
			m.put("orgPrice",df.format(m.get("orgPrice")));
			m.put("userId", userId);
		}
		return super.toJson(list);
	}
	
	@Override
	public List<Map<String,Object>> getCouponListByType(Map<String,Object>paramMap) throws Exception{
		String userId=paramMap.get("userId")==null?"":paramMap.get("userId").toString();
		String dgurl=paramMap.get("dgurl").toString();
		String sql="SELECT `dc`.`id`,`dc`.`valid`,`dc`.`title`,`dc`.`store_id`,`dc`.`about`,`dc`.`des_price` AS desPrice,`dc`.`org_price` AS orgPrice,`dc`.`count`,`dc`.`img`,`dsu`.`name` AS `storeName` FROM `diy_coupon` `dc` LEFT JOIN `diy_sys_user` `dsu` ON `dsu`.`id`=`dc`.`store_id` WHERE type=:type AND status=1 AND valid>=CURDATE()";
		List<Map<String,Object>>list=this.npjt.queryForList(sql,paramMap);
		for(Map<String,Object>m : list){
			String valid=m.get("valid").toString();
			String id=m.get("id").toString();
			//封装查询用户是否领过券
			paramMap.put("id",id);
			if(isCachCouponForId(paramMap)){
				m.put("isGet",1);
			}else{
				m.put("isGet",0);
			}			
			m.put("couponImg",dgurl+m.get("img"));
			//领取中心优惠券接口,id=优惠券id
			String http=couponHttp2+"?userId="+userId+"&valid="+valid+"&id="+id;
			m.put("getCenterCoupon",http);
			m.put("desPrice",df.format(m.get("desPrice")));
			m.put("orgPrice",df.format(m.get("orgPrice")));
			m.put("userId", userId);
		}
		return list;
	}
	
	@Override
	public int addCoupon(Map<String,Object>paramMap)throws Exception{
		try {
			return this.jt.update("INSERT INTO `diy_coupon_user` (`deviceNo`,`valid`,`coupon_id`,`app`,`creatime`,`status`,`org_price`,`des_price`) VALUES(?,?,?,?,CURDATE(),1,(SELECT `org_price` FROM `diy_coupon` WHERE `id`=?),(SELECT `des_price` FROM `diy_coupon` WHERE `id`=?))",new Object[]{paramMap.get("deviceNo"),paramMap.get("valid"),paramMap.get("id"),paramMap.get("app"),paramMap.get("id"),paramMap.get("id")});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	@Override
	public int addCouponForId(Map<String,Object>paramMap)throws Exception{
		try {
			return this.jt.update("INSERT INTO `diy_coupon_user` (`userId`,`valid`,`coupon_id`,`creatime`,`status`,`org_price`,`des_price`) VALUES(?,?,?,CURDATE(),1,(SELECT `org_price` FROM `diy_coupon` WHERE `id`=?),(SELECT `des_price` FROM `diy_coupon` WHERE `id`=?))",new Object[]{paramMap.get("userId"),paramMap.get("valid"),paramMap.get("id"),paramMap.get("id"),paramMap.get("id")});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	@Override
	public List<Map<String,Object>> getCodeCoupon(Map<String,Object>paramMap){
		String sql="SELECT * FROM diy_coupon WHERE code=:code AND status=1 AND type=1";
		List<Map<String,Object>> result=this.npjt.queryForList(sql, paramMap);
		return result;
	}
	
	@Override
	public boolean isCodeCoupon(Map<String,Object>paramMap)throws Exception{
		String sql="SELECT count(`dcu`.`id`) FROM `diy_coupon_user` `dcu` LEFT JOIN `diy_coupon` `dc` ON `dc`.`id`=`dcu`.`coupon_id` WHERE `dc`.`type`=1 AND `dcu`.`status`=1 AND `dcu`.`deviceNo`=:deviceNo AND `dcu`.`app`=:app AND `dc`.`code`=:code";
			int result=this.npjt.queryForObject(sql, paramMap,Integer.class);
			if(result>0){
				return true;
			}
			return false;
	}
	
	@Override
	public boolean isCodeCouponForId(Map<String,Object>paramMap)throws Exception{
		String sql="SELECT count(`dcu`.`id`) FROM `diy_coupon_user` `dcu` LEFT JOIN `diy_coupon` `dc` ON `dc`.`id`=`dcu`.`coupon_id` WHERE `dc`.`type`=1 AND `dcu`.`status`=1 AND `dcu`.`userId`=:userId AND `dc`.`code`=:code";
			int result=this.npjt.queryForObject(sql, paramMap,Integer.class);
			if(result>0){
				return true;
			}
			return false;

	}
	
	@Override
	public String getCoupon(Map<String,Object>paramMap) throws Exception{
		String dgurl=paramMap.get("dgurl").toString();
		String sql="SELECT `id`,`store_id` AS `storeId`,`title`,`about`,`des_price` AS `desPrice`,`org_price` AS `orgPrice`,`valid`,`img` FROM `diy_coupon` WHERE `status`=1 AND `id`=:id";
		List<Map<String,Object>>list=this.npjt.queryForList(sql,paramMap);
		for(Map<String,Object>map : list){
			map.put("img",dgurl+(map.get("img")==null?"":map.get("img")));
		}
		return super.toJson(list);
	}
	
	@Override
	public boolean isCachCoupon(Map<String,Object>paramMap){
		String sql="SELECT `dcu`.`id` FROM `diy_coupon_user` `dcu` LEFT JOIN `diy_coupon` `dc` ON `dc`.`id`=`dcu`.`coupon_id` WHERE `dc`.`type`=2 AND `dcu`.`deviceNo`=:deviceNo AND `dcu`.`app`=:app AND dc.`id`=:id";
			List<Map<String,Object>>list=this.npjt.queryForList(sql, paramMap);
			if(!list.isEmpty()){
				return true;
			}
			return false;
	}
	
	@Override
	public boolean isCachCouponType(Map<String,Object>paramMap){
		String sql="SELECT `dcu`.`id` FROM `diy_coupon_user` `dcu` LEFT JOIN `diy_coupon` `dc` ON `dc`.`id`=`dcu`.`coupon_id` WHERE `dc`.`type`=:type AND `dcu`.`deviceNo`=:deviceNo AND `dcu`.`app`=:app AND dc.`id`=:id";
			List<Map<String,Object>>list=this.npjt.queryForList(sql, paramMap);
			if(!list.isEmpty()){
				return true;
			}
			return false;
	}
	
	@Override
	public boolean isCachCouponForId(Map<String,Object>paramMap){
		String sql="SELECT `dcu`.`id` FROM `diy_coupon_user` `dcu` LEFT JOIN `diy_coupon` `dc` ON `dc`.`id`=`dcu`.`coupon_id` WHERE `dcu`.`userId`=:userId AND dc.`id`=:id";
			List<Map<String,Object>>list=this.npjt.queryForList(sql, paramMap);
			if(!list.isEmpty()){
				return true;
			}
			return false;
	}
	
	@Override
	public int getCouponCount(String couponId){
		String sql="SELECT `count` FROM `diy_coupon` WHERE `id`=?";
		try{
			return this.jt.queryForObject(sql,Integer.class,couponId);
		}catch(Exception e){
			return 0;
		}
	}
	
	@Override
	public int updateCouponCount(int count,String couponId){
		String sql="UPDATE `diy_coupon` SET `count`=? WHERE `id`=?";
		return this.jt.update(sql, count,couponId);
	}
	
	@Override
	public int addCouponByUserId(Map<String,Object>paramMap)throws Exception{
		try {
			return this.jt.update("INSERT INTO `diy_coupon_user` (`deviceNo`,`valid`,`coupon_id`,`app`,`creatime`,`status`,`org_price`,`des_price`,`userId`) VALUES( (SELECT `device_no` FROM diy_device_user WHERE `id`=?),(SELECT `valid` FROM `diy_coupon` WHERE `id`=?),?,(SELECT `app` FROM diy_device_user WHERE `id`=?),CURDATE(),1,(SELECT `org_price` FROM `diy_coupon` WHERE `id`=?),(SELECT `des_price` FROM `diy_coupon` WHERE `id`=?),?)",new Object[]{paramMap.get("userId"),paramMap.get("id"),paramMap.get("id"),paramMap.get("userId"),paramMap.get("id"),paramMap.get("id"),paramMap.get("id")});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	@Override
	public List<Map<String,Object>>getCouponListNotLogin(Map<String,Object>paramMap){
		String sql="SELECT * FROM diy_coupon_user WHERE userId=(SELECT id FROM diy_device_user WHERE device_no=:deviceNo AND app=:capp AND openid is null)";
		try{
			return super.npjt.queryForList(sql, paramMap);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public List<Map<String, Object>> getCouponListNotLogin2(User user) {
		String sql="SELECT * FROM diy_coupon_user WHERE userId=(SELECT id FROM diy_device_user WHERE device_no=? AND app=? ORDER BY `ctime` DESC LIMIT 1)";
		try{
			return super.jt.queryForList(sql, new Object[]{user.getDeviceNo(),user.getApp()});
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public List<Map<String,Object>>getCouponListlogined(Map<String,Object>paramMap){
		String sql="SELECT * FROM diy_coupon_user WHERE userId=(SELECT id FROM diy_device_user WHERE openid=:openid AND app=:app AND device_no=:deviceNo)";
		return super.npjt.queryForList(sql, paramMap);
	}
	
	@Override
	public List<Map<String, Object>> getCouponListlogined2(User user) {
		String sql="SELECT * FROM diy_coupon_user WHERE userId=?";
		return super.jt.queryForList(sql, user.getId());
	}

	public boolean isGetCoupon(String id, String sys, String userId) {
		try {
			this.jt.queryForObject("SELECT `id` FROM `diy_coupon_user` WHERE `coupon_id`=? AND `userId`=? AND `sys`=? LIMIT 1",new Object[]{id,userId,sys}, Integer.class);
			return true;
		} catch (Exception e) {
		}
		return false;
	}

	public List<Map<String, Object>> getCouponList(String userId, String sys) {
		return this.jt.queryForList("SELECT 1 AS `type`,`dcu`.`id`,`dcu`.`org_price` AS `orgPrice`,`dcu`.`des_price` AS `desPrice`,`dcu`.`creatime`,`dcu`.`status`,`dcu`.`valid`,`dsu`.`id` AS `companyId`,`dsu`.`name` AS `companyName` FROM `diy_coupon_user` `dcu` LEFT JOIN `diy_coupon` `dc` ON `dc`.`id`=`dcu`.`coupon_id` LEFT JOIN `diy_sys_user` `dsu` ON `dsu`.`id`=`dc`.`store_id` WHERE `dcu`.`userId`=? AND `dcu`.`sys`=? AND `dcu`.`status`=1 AND `dcu`.`valid`>=CURDATE() UNION ALL SELECT 2 AS `type`,`dcu`.`id`,`dcu`.`org_price` AS `orgPrice`,`dcu`.`des_price` AS `desPrice`,`dcu`.`creatime`,`dcu`.`status`,`dcu`.`valid`,`dsu`.`id` AS `companyId`,`dsu`.`name` AS `companyName` FROM `diy_coupon_user` `dcu` LEFT JOIN `diy_coupon` `dc` ON `dc`.`id`=`dcu`.`coupon_id` LEFT JOIN `diy_sys_user` `dsu` ON `dsu`.`id`=`dc`.`store_id` WHERE `dcu`.`userId`=? AND `dcu`.`sys`=? AND (`dcu`.`status`=0 OR `dcu`.`valid`<CURDATE()) AND `dcu`.`valid`>DATE_SUB(CURDATE(), INTERVAL 1 MONTH) ORDER BY `creatime` DESC",new Object[]{userId,sys,userId,sys});
	}

	public List<Map<String, Object>> checkCouponList(String userId, String sys) {
		return this.jt.queryForList("SELECT DISTINCT(`dsu`.`id`) AS `companyId`,`dsu`.`name` AS `companyName` FROM `diy_coupon_user` `dcu` LEFT JOIN `diy_coupon` `dc` ON `dc`.`id`=`dcu`.`coupon_id` LEFT JOIN `diy_sys_user` `dsu` ON `dsu`.`id`=`dc`.`store_id` WHERE `dcu`.`userId`=? AND `dcu`.`sys`=? AND `dcu`.`status`=1 AND `dcu`.`valid`>=CURDATE()",new Object[]{userId,sys});
	}

	public List<Map<String, Object>> getAvailableCoupon(String userId, String sys, Integer companyId) {
		return this.jt.queryForList("SELECT `dcu`.`id`,`dcu`.`org_price` AS `orgPrice`,`dcu`.`des_price` AS `desPrice`,`dcu`.`creatime`,`dcu`.`status`,`dcu`.`valid` FROM `diy_coupon_user` `dcu` LEFT JOIN `diy_coupon` `dc` ON `dc`.`id`=`dcu`.`coupon_id` LEFT JOIN `diy_sys_user` `dsu` ON `dsu`.`id`=`dc`.`store_id` WHERE `dcu`.`userId`=? AND `dcu`.`sys`=? AND `dcu`.`status`=1 AND `dsu`.`id`=? AND `dcu`.`valid`>=CURDATE()",new Object[]{userId,sys,companyId});
	}

	public List<Map<String, Object>> getAvailableCoupon2(String userId, String sys) {
		return this.jt.queryForList("SELECT `dcu`.`id`,`dcu`.`org_price` AS `orgPrice`,`dcu`.`des_price` AS `desPrice`,`dcu`.`creatime`,`dcu`.`status`,`dcu`.`valid`,-1 AS companyId FROM `diy_coupon_user` `dcu` LEFT JOIN `diy_coupon` `dc` ON `dc`.`id`=`dcu`.`coupon_id`  WHERE `dcu`.`userId`=? AND `dcu`.`sys`=? AND `dcu`.`status`=1 AND `dc`.`store_id`=-1 AND `dcu`.`valid`>=CURDATE()",new Object[]{userId,sys});
	}

	public boolean getCoupon(String userId, String couponId, String sys) {
		try {
			this.jt.queryForObject("SELECT `id` FROM `diy_coupon_user` WHERE `userId`=? AND `coupon_id`=? AND `sys`=? LIMIT 1",new Object[]{userId,couponId,sys}, Integer.class);
		} catch (Exception e) {
			Map<String, Object> couponMap=this.jt.queryForMap("SELECT `store_id`,`org_price`,`des_price`,`valid` FROM `diy_coupon` WHERE `id`=? AND `status`=1",new Object[]{couponId});
			int result=this.jt.update("INSERT INTO `diy_coupon_user` (`coupon_id`,`valid`,`sys`,`org_price`,`des_price`,`creatime`,`userId`,`status`) VALUES(?,?,?,?,?,NOW(),?,1)",new Object[]{couponId,couponMap.get("valid"),sys,couponMap.get("org_price"),couponMap.get("des_price"),userId});
			if(result>0){
				return true;
			}
		}
		return false;
		
	}
	
}
