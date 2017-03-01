package cn._51app.dao.diy2_0.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import cn._51app.dao.BaseDao;
import cn._51app.dao.diy2_0.IAdressDao;

@Repository
public class AdressDao extends BaseDao implements IAdressDao{
	
	@Override
	public List<Map<String, Object>> queryAddress(String deviceNo, String app) {
		List<Map<String, Object>> list= this.jt.queryForList("SELECT `id`,`name`,`mobile`,`province`,`area` FROM `diy_user_address` WHERE `status`=1 AND `device_no`=? AND `app`=? ORDER BY `is_default` DESC,`ctime` DESC",new Object[]{deviceNo,app});
		return (list==null || list.size()==0)?null:list;
	}
	
	@Override
	public List<Map<String, Object>> queryAddressForId(String userId) {
		List<Map<String, Object>> list= this.jt.queryForList("SELECT `id`,`name`,`mobile`,`province`,`area` FROM `diy_user_address` WHERE `status`=1 AND `userId`=? ORDER BY `is_default` DESC,`ctime` DESC",new Object[]{userId});
		return (list==null || list.size()==0)?null:list;
	}
	
	@Override
	public int updateAdress(final String deviceNo, final String app, String addressId, final String name, final String mobile, final String province,
			final String area, final String isDefault) {
		if(StringUtils.isBlank(addressId)){
			if("1".equalsIgnoreCase(isDefault)){
				this.jt.update("UPDATE `diy_user_address` SET `is_default`=0 WHERE `device_no`=? AND `app`=? AND `status`=1",new Object[]{deviceNo,app});
			}
			
			final String sql="INSERT INTO `diy_user_address` (`name`,`mobile`,`province`,`area`,`is_default`,`ctime`,`status`,`device_no`,`app`) VALUES(?,?,?,?,?,NOW(),1,?,?)";
			int lastNum = 0;
			KeyHolder keyHolder = new GeneratedKeyHolder(); // 创建一个主健拥有者
			PreparedStatementCreator p = new PreparedStatementCreator() {			
				public PreparedStatement createPreparedStatement(Connection conn) {
					try {
						PreparedStatement ps = conn.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS);
						ps.setString(1, name);
						ps.setString(2, mobile);
						ps.setString(3, province);
						ps.setString(4, area);
						ps.setString(5, isDefault);
						ps.setString(6, deviceNo);
						ps.setString(7, app);
						return ps;
					} catch (SQLException e) {
						e.printStackTrace();
					};
					return null;
				}
			};
			
			this.jt.update(p, keyHolder);
			lastNum = keyHolder.getKey().intValue();
			return lastNum;
		}else{
			this.jt.update("UPDATE `diy_user_address` SET `name`=?,`mobile`=?,`province`=?,`area`=?,`is_default`=? WHERE `id`=? AND `device_no`=? AND `app`=? AND `status`=1 LIMIT 1",new Object[]{name,mobile,province,area,isDefault,addressId,deviceNo,app});
			this.jt.update("UPDATE `diy_user_address` SET `is_default`=0 WHERE `id`!=? AND `device_no`=? AND `app`=? AND `status`=1",new Object[]{addressId,deviceNo,app});
		}
		return Integer.parseInt(addressId);
	}
	
	@Override
	public int updateAdressForId(final String userId, String addressId, final String name, final String mobile, final String province,
			final String area, final String isDefault) {
		if(StringUtils.isBlank(addressId)){
			if("1".equalsIgnoreCase(isDefault)){
				this.jt.update("UPDATE `diy_user_address` SET `is_default`=0 WHERE `userId`=? AND `status`=1",new Object[]{userId});
			}
			
			final String sql="INSERT INTO `diy_user_address` (`name`,`mobile`,`province`,`area`,`is_default`,`ctime`,`status`,`userId`) VALUES(?,?,?,?,?,NOW(),1,?)";
			int lastNum = 0;
			KeyHolder keyHolder = new GeneratedKeyHolder(); // 创建一个主健拥有者
			PreparedStatementCreator p = new PreparedStatementCreator() {			
				public PreparedStatement createPreparedStatement(Connection conn) {
					try {
						PreparedStatement ps = conn.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS);
						ps.setString(1, name);
						ps.setString(2, mobile);
						ps.setString(3, province);
						ps.setString(4, area);
						ps.setString(5, isDefault);
						ps.setString(6, userId);
						return ps;
					} catch (SQLException e) {
						e.printStackTrace();
					};
					return null;
				}
			};
			
			this.jt.update(p, keyHolder);
			lastNum = keyHolder.getKey().intValue();
			return lastNum;
		}else{
			this.jt.update("UPDATE `diy_user_address` SET `name`=?,`mobile`=?,`province`=?,`area`=?,`is_default`=? WHERE `id`=? AND `userId`=? AND `status`=1 LIMIT 1",new Object[]{name,mobile,province,area,isDefault,addressId,userId});
			this.jt.update("UPDATE `diy_user_address` SET `is_default`=0 WHERE `id`!=? AND `userId`=? AND `status`=1",new Object[]{addressId,userId});
		}
		return Integer.parseInt(addressId);
	}
	
	@Override
	public boolean deleteAdress(String deviceNo, String app, String addressId) {
		int result=this.jt.update("UPDATE `diy_user_address` SET `status`=0 WHERE `id`=?  AND `device_no`=? AND `app`=?",new Object[]{addressId,deviceNo,app});
		if(result>0){
			return true;
		}
		return false;
	}
	
	@Override
	public boolean deleteAdressForId(String userId, String addressId) {
		int result=this.jt.update("UPDATE `diy_user_address` SET `status`=0 WHERE `id`=?  AND `userId`=?",new Object[]{addressId,userId});
		if(result>0){
			return true;
		}
		return false;
	}
	
	@Override
	public boolean isDefault(String deviceNo, String app, String addressId) {
		int result=this.jt.update("UPDATE `diy_user_address` SET `is_default`='0' WHERE `device_no`=? AND `app`=?",new Object[]{deviceNo,app});
		result=this.jt.update("UPDATE `diy_user_address` SET `is_default`=1 WHERE `id`=?  AND `device_no`=? AND `app`=?",new Object[]{addressId,deviceNo,app});
		if(result>0){
			return true;
		}
		return false;
	}
	
	@Override
	public boolean isDefaultForId(String userId, String addressId) {
		int result=this.jt.update("UPDATE `diy_user_address` SET `is_default`='0' WHERE `userId`=?",new Object[]{userId});
		result=this.jt.update("UPDATE `diy_user_address` SET `is_default`=1 WHERE `id`=?  AND `userId`=?",new Object[]{addressId,userId});
		if(result>0){
			return true;
		}
		return false;
	}

	@Override
	public boolean isRemote(String id) {
		try {
			String area=this.jt.queryForObject("SELECT `province` FROM `diy_user_address` WHERE `id`=?",new Object[]{id},String.class);
			if(area.contains("新疆") ||area.contains("内蒙古")|| area.contains("西藏")|| area.contains("宁夏")|| area.contains("甘肃")|| area.contains("青海")){
				return true;
			}
		} catch (Exception e) {
		}
		return false;
	}
}
