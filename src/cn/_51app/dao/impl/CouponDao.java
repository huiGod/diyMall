package cn._51app.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import cn._51app.dao.BaseDao;
import cn._51app.dao.ICouponDao;

@Repository
public class CouponDao extends BaseDao implements ICouponDao{

	@Override
	public String drawSwitch(Map<String,Object> paramMap) throws Exception {
		String cacheKey =paramMap.get("cacheKey").toString();
		String sql ="SELECT switch,valid FROM diy_draw ORDER BY creatime DESC LIMIT 1";
		Map<String,Object> m=null;
		boolean b =false;
		if(super.isCacheNull(cacheKey).equals("a")){
			//获取有效期
			try {
				m=super.jt.queryForMap(sql);
				String valid =m.get("valid").toString();
				paramMap.put("valid", valid);
				sql="SELECT creatime FROM diy_coupon_user WHERE valid=:valid AND deviceNo=:deviceNo AND app=:app";
				//查询设备有没有抽奖，没有返回true
				b =super.npjt.queryForList(sql, paramMap).isEmpty();
				//查询后台设置有没有抽奖
				b =new Boolean(m.get("switch").toString());
			} catch (EmptyResultDataAccessException e) {
				return null;
			}
			int cacheTime =new Integer(paramMap.get("cacheTime").toString());
			return super.saveAndGet(b, cacheKey, cacheTime);
			
		}else if(isCacheNull(cacheKey).equals("b")){
			return super.q(cacheKey);
		}else{
			try {
				m=super.jt.queryForMap(sql);
				String valid =m.get("valid").toString();
				paramMap.put("valid", valid);
				sql="SELECT creatime FROM diy_coupon_user WHERE valid=:valid AND deviceNo=:deviceNo AND app=:app";
				//查询设备有没有抽奖，没有返回true
				b =super.npjt.queryForList(sql, paramMap).isEmpty();
				//查询后台设置有没有抽奖
				b =new Boolean(m.get("switch").toString());
			} catch (EmptyResultDataAccessException e) {
				return "";
			}
			return super.toJson(b);
		}
		
	}
	
	
	public boolean isDraw() {
		String sql = "SELECT id FROM diy_draw WHERE starttime<NOW() AND endtime>NOW()";
		return super.jt.queryForList(sql).isEmpty();
	}
	
	@Override
	public boolean userDraw(Map<String,Object> paramMap) throws Exception{
		if(!isDraw()){
			String sql = "SELECT * FROM diy_coupon_user WHERE TO_DAYS(creatime) = TO_DAYS(NOW()) AND deviceNo=:deviceNo AND app=:app";
			return super.npjt.queryForList(sql, paramMap).isEmpty();
		}else{
			return false;
		}
	}

	@Override
	public String getDrawLevelAndValid(String cacheKey, int cacheTime) throws Exception {
		String sql ="SELECT valid FROM diy_draw ORDER BY creatime DESC LIMIT 1";
		Map<String,Object> m=null;
		if(super.isCacheNull(cacheKey).equals("a")||isCacheNull(cacheKey).equals("c")){
			try {
				m=super.jt.queryForMap(sql);
				double math = Math.random();
				if(math<0.15)
					m.put("level", 4);
				else if(math<0.35)
					m.put("level", 5);
				else if(math<0.85)
					m.put("level", 6);
				else
					m.put("level", 0);
			} catch (EmptyResultDataAccessException e) {
				return "";
			}
			if(super.isCacheNull(cacheKey).equals("a"))
				return super.saveAndGet(m, cacheKey, cacheTime);
			else
				return super.toJson(m);
		}else if(isCacheNull(cacheKey).equals("b")){
			return super.q(cacheKey);
		}else{
			return "";
		}
	}

	@Override
	public int getCouponIdByLevel(String levelStr) throws Exception {
		int level =new Integer(levelStr);
		String sql="SELECT id FROM diy_coupon WHERE level="+level;
		return super.jt.queryForObject(sql, Integer.class);
	}

	
	
	@Override
	public int addCouponUser(Map<String, Object> paramMap) throws Exception {
//		try {
//			String sql="INSERT INTO diy_coupon_user (deviceNo,valid,coupon_id,app,sys,creatime,status)"
//					+ "VALUES(:deviceNo,:valid,:cid,:app,:sys,CURDATE(),1)";
//			return super.npjt.update(sql, paramMap);
//		} catch (DuplicateKeyException e) {
//			return 0;
//		}
		try {
			return this.jt.update("INSERT INTO `diy_coupon_user` (`deviceNo`,`valid`,`coupon_id`,`app`,`creatime`,`status`,`org_price`,`des_price`) SELECT ?,?,?,?,CURDATE(),1,(SELECT `org_price` FROM `diy_coupon` WHERE `id`=?),(SELECT `des_price` FROM `diy_coupon` WHERE `id`=?)",new Object[]{paramMap.get("deviceNo"),paramMap.get("valid"),paramMap.get("cid"),paramMap.get("app"),paramMap.get("cid"),paramMap.get("cid")});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public String getCoupon(Map<String, Object> paramMap) throws Exception {
		String cacheKey =paramMap.get("cacheKey").toString();
		
		String sql="SELECT dc.id AS dcId, dc.title,dc.about AS content,dcu.valid AS expirtDate,dc.des_price AS value,dc.org_price AS orgPirce"
				+ " FROM diy_coupon_user dcu INNER JOIN diy_coupon dc"
				+ " ON dcu.coupon_id =dc.id"
				+ " WHERE dcu.valid>=CURDATE()"
				+ " AND dcu.status=0"
				+ " AND dcu.deviceNo=:deviceNo"
				+ " AND dcu.valid=:valid"
				+ " AND dcu.app LIKE CONCAT('%',:app,'%')";
		
		if(super.isCacheNull(cacheKey).equals("a")){
			
			List<Map<String,Object>> lm =super.npjt.queryForList(sql, paramMap);
			if(!lm.isEmpty()){
				int cacheTime =new Integer(paramMap.get("cacheTime").toString());
				 return super.saveAndGet(lm, cacheKey, cacheTime);
				
			}
		}else if(isCacheNull(cacheKey).equals("b")){
			return super.q(cacheKey);
		}else{
			List<Map<String,Object>> lm =super.npjt.queryForList(sql, paramMap);
			if(!lm.isEmpty()){
				 return super.toJson(lm);
			}
		}
		return "";
		
	}

	@Override
	public List<Map<String, Object>> queryAvaiCoupon(String deviceNo, String app) {
		List<Map<String, Object>> list=this.jt.queryForList("SELECT `dcu`.`id`,`dcu`.`valid`,`dc`.`title`,`dc`.`about`,`dc`.`des_price` AS `desPrice`,`dc`.`org_price` AS `orgPrice`,dc.level FROM `diy_coupon_user` `dcu` INNER JOIN `diy_coupon` `dc` ON `dcu`.`coupon_id`=`dc`.`id` WHERE `dcu`.`status`=1 AND `dcu`.`coupon_id`!=0 AND `dcu`.`deviceNo`=? AND `dcu`.`app`=? AND `dcu`.`valid`>=CURDATE() ORDER BY `dcu`.`creatime` DESC",new Object[]{deviceNo,app});
		return (list==null || list.size()<=0)?null:list;
	}
	
	@Override
	public List<Map<String, Object>> getUsers() {
		return this.jt.queryForList("SELECT `device_no` AS `deviceNo`,`app` FROM `diy_device_user` `dcu` WHERE EXISTS(SELECT 1 FROM diy_orders WHERE `dcu`.`device_no`=`device_no` AND `app`=`dcu`.`app` AND( `status`=1 OR `status`=2 OR `status`=3 OR `status`=4 OR `status`=7))");
	}	
	
	@Override
	public int insertCoupon(String deviceNo, String app) {
		this.jt.update("INSERT INTO `diy_coupon_user`(`deviceNo`,`valid` ,`coupon_id` ,`app` ,`sys` ,`org_price` ,`des_price` ,`creatime` ,`status` ) VALUES (?,'2016-11-30',26,?,1,10,5,NOW(),1) ",new Object[]{deviceNo,app});
		this.jt.update("INSERT INTO `diy_coupon_user`(`deviceNo`,`valid` ,`coupon_id` ,`app` ,`sys` ,`org_price` ,`des_price` ,`creatime` ,`status` ) VALUES (?,'2016-11-30',27,?,1,99,10,NOW(),1) ",new Object[]{deviceNo,app});
		return this.jt.update("INSERT INTO `diy_coupon_user`(`deviceNo`,`valid` ,`coupon_id` ,`app` ,`sys` ,`org_price` ,`des_price` ,`creatime` ,`status` ) VALUES (?,'2016-11-30',28,?,1,199,20,NOW(),1) ",new Object[]{deviceNo,app});
	}
}
