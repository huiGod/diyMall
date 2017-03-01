package cn._51app.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import cn._51app.dao.BaseDao;
import cn._51app.dao.IUCouponDao;

@Repository
public class UCouponDao extends BaseDao implements IUCouponDao {

	@Override
	public Map<String,Object> getLottery(Map<String,Object>paramMap) throws Exception {
		String sql ="SELECT valid FROM diy_draw ORDER BY creatime DESC LIMIT 1,2";
		String sql2="SELECT count FROM diy_coupon WHERE level=?";
		String sql3="UPDATE diy_coupon SET count=? WHERE level=?";
		Map<String,Object> m=null;
				m=super.jt.queryForMap(sql);
				//鲜花
				int count=Integer.parseInt(super.jt.queryForMap(sql2,8).get("count").toString());
				//diy
				int count2=Integer.parseInt(super.jt.queryForMap(sql2,13).get("count").toString());
				//diy壳
				int count3=Integer.parseInt(super.jt.queryForMap(sql2,12).get("count").toString());
				//30元
				int count4=Integer.parseInt(super.jt.queryForMap(sql2,11).get("count").toString());
				//10元
				int count5=Integer.parseInt(super.jt.queryForMap(sql2,10).get("count").toString());
				//无限循环
				while(true){
					double math = Math.random();
					if(math<0){
						if(count==0){
							continue;
						}
						m.put("level", 1);
						count--;
						super.jt.update(sql3,count,8);
						break;
					}else if(math<0.11){
						if(count2==0){
							continue;
						}
						m.put("level", 5);	
						count2--;
						super.jt.update(sql3,count2,13);
						break;
					}else if(math<0.18){
						if(count3==0){
							continue;
						}
						count3--;
						m.put("level", 7);
						super.jt.update(sql3,count3,12);
						break;
					}else if(math<0.30){
						if(count4==0){
							continue;
						}
						count4--;
						m.put("level", 4);
						super.jt.update(sql3,count4,11);
						break;
					//5-1
					}else if(math<0.50){
						if(count5==0){
							continue;
						}
						count5--;
						m.put("level", 2);
						super.jt.update(sql3,count5,10);
						break;
					}else if(math<0.70){
						m.put("level", 0);	
						break;
					}else if(math<0.85){
						m.put("level", 3);	
						break;
					}else{
						m.put("level", 6);	
						break;
					}
				}
				//用户抽奖剩余
				m.put("hasDraw", verifyUserCount(paramMap));
				return m;
	}
	
	public boolean isDraw() {
		String sql = "SELECT id FROM diy_draw WHERE starttime<NOW() AND endtime>NOW()";
		return super.jt.queryForList(sql).isEmpty();
	}
	
	@Override
	public boolean userDraw(Map<String,Object> paramMap) throws Exception{
			String sql = "SELECT * FROM diy_coupon_user WHERE deviceNo=:deviceNo AND app=:app";
			String sql2 = "SELECT COUNT(id) FROM diy_coupon_user WHERE deviceNo=:deviceNo AND app=:app";
			if(super.npjt.queryForList(sql, paramMap).isEmpty()){
				return true;
			}else{
				int count=super.npjt.queryForObject(sql2, paramMap, Integer.class);
				if(count<3){
						return true;
				}else{
					return false;
				}
			}
	}
	
	@Override
	public int addCouponUser(Map<String, Object> paramMap) throws Exception {
		try {
			return this.jt.update("INSERT INTO `diy_coupon_user` (`deviceNo`,`valid`,`coupon_id`,`app`,`creatime`,`status`,`org_price`,`des_price`) SELECT ?,?,?,?,CURDATE(),1,(SELECT `org_price` FROM `diy_coupon` WHERE `id`=?),(SELECT `des_price` FROM `diy_coupon` WHERE `id`=?)",new Object[]{paramMap.get("deviceNo"),paramMap.get("valid"),paramMap.get("cid"),paramMap.get("app"),paramMap.get("cid"),paramMap.get("cid")});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	@Override
	public int getCouponIdByLevel(String levelStr) throws Exception {
		int level =new Integer(levelStr);
		String sql="SELECT id FROM diy_coupon WHERE level="+level;
		return super.jt.queryForObject(sql, Integer.class);
	}
	
	@Override
	public int addMobileUser(Map<String,Object>paramMap)throws Exception{
		return this.npjt.update("UPDATE diy_coupon_user SET phone=:phone WHERE app=:app,deviceNo=:deviceNo",paramMap);
	}
	
	@Override
	public int verifyUserCount(Map<String,Object>paramMap) throws DataAccessException, Exception{
		String sql="SELECT COUNT(id) FROM diy_coupon_user WHERE app=:app AND deviceNo=:deviceNo";
		int result=0;
		try {
			result = super.npjt.queryForObject(sql,paramMap,Integer.class);
		} catch (DataAccessException e) {
			return 0;
		}
		return result;
		
	}
	
	@Override
	public List<Map<String,Object>> winnerList(String app) throws DataAccessException, Exception{
		String sql="SELECT ddu.name,dc.title as about FROM diy_coupon_user dcu LEFT JOIN diy_device_user ddu ON dcu.deviceNo=ddu.device_no LEFT JOIN diy_coupon dc ON dc.id=dcu.coupon_id"
				+ " WHERE dc.level IN(8,9,10,11,12,13) ORDER BY dcu.creatime DESC,dcu.id LIMIT 0,40 ";
		return super.jt.queryForList(sql);
	}
	
	@Override
	public int getAwards(Map<String,Object>paramMap){
		if(paramMap==null){
			return 0;
		}
		String sql="SELECT dc.level FROM diy_coupon_user dcu LEFT JOIN diy_coupon dc ON dcu.coupon_id=dc.id WHERE dcu.app=:app AND dcu.deviceNo=:deviceNo ORDER BY dcu.creatime ASC,dcu.id DESC LIMIT 1";
		int result=0;
		
		try {
			result = super.npjt.queryForObject(sql, paramMap,Integer.class);
		} catch (DataAccessException e) {
			return 0;
		}
		switch(result){
		case 8 : return 1;
		case 13 : return 5;
		case 12 : return 7;
		case 11 : return 4;
		case 10 : return 2;
		case 9 : return 0;
		default:return 0;
		}
	}
	
	@Override
	public int verifyCount(String level){
		String sql="SELECT count FROM diy_coupon WHERE level="+level;
		return super.jt.queryForObject(sql,Integer.class);
	}
	
	@Override
	public int getCouponId(Map<String,Object>paramMap){
		String sql="SELECT id FROM diy_coupon_user WHERE deviceNo=:deviceNo AND app=:app ORDER BY id DESC limit 1";
		return super.npjt.queryForObject(sql, paramMap, Integer.class);
	}
	
	@Override
	public int upUserMobile(int id,String mobile)throws Exception{
		String sql="UPDATE diy_coupon_user SET phone=? WHERE id=?";
		return super.jt.update(sql,mobile,id);
	}
	
	@Override 
	public int delCoupon()throws Exception{
		String sql="DELETE FROM diy_coupon_user WHERE app='com.shua.h5'";
		return super.jt.update(sql);
	}
}
