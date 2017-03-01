package cn._51app.dao.impl;

import java.util.Map;

import org.springframework.stereotype.Repository;

import cn._51app.dao.BaseDao;
import cn._51app.dao.IUserDao;


@Repository
public class UserDao extends BaseDao implements IUserDao{
	
	@Override
	public Map<String, Object> getUser(String deviceNo,String app) throws Exception{
		String sql = "SELECT id,`name`,sex,`mobile`,`head_url` FROM `diy_device_user` WHERE device_no=? AND app=?";
		return jt.queryForMap(sql,new Object[]{deviceNo,app});
	}
}
