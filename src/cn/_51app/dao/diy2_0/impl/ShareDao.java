package cn._51app.dao.diy2_0.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import cn._51app.dao.BaseDao;
import cn._51app.dao.diy2_0.IShareDao;

@Repository
public class ShareDao extends BaseDao implements IShareDao{
	@Override
	public int getMemberId(final String openid,final String app) {
		Integer memberId=null;
		try {
			memberId=this.jt.queryForObject("SELECT `id` FROM `diy_device_user` WHERE `openid`=? OR `device_no`=? LIMIT 1",new Object[]{openid,openid}, Integer.class);
		} catch (Exception e) {
			KeyHolder keyHolder=new GeneratedKeyHolder();
			this.jt.update(new PreparedStatementCreator() {
				
				@Override
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
					PreparedStatement ps=connection.prepareStatement("INSERT INTO `diy_device_user`(`device_no`,`app`,`ctime`) VALUES(?,?,NOW())");
					ps.setString(1, openid);
					ps.setString(2, app);
					return ps;
				}
			}, keyHolder);
			return keyHolder.getKey().intValue();
		}
		return memberId;
	}
}
