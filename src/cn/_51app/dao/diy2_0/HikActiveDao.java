package cn._51app.dao.diy2_0;

import java.util.Map;

public interface HikActiveDao {
	int getUserInfo(String id);

	String getInfo(Map<String, Object> paramMap) throws Exception;

	String appHik(Map<String, Object> paramMap) throws Exception;

	String wxHik(Map<String, Object> paramMap)throws Exception;

	String payGoods(Map<String, Object> paramMap)throws Exception;

	String wxLogin(Map<String, Object> paramMap)throws Exception;


}
