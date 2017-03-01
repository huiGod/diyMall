package cn._51app.dao;

import java.util.Map;

public interface IUserDao {

	Map<String, Object> getUser(String deviceNo, String app) throws Exception;
	
}
