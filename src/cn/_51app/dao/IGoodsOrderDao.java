
package cn._51app.dao;

import java.util.Map;

public interface IGoodsOrderDao {
	
	int insert(Map<String, Object> paramMap) throws Exception;
	
	String getGOMap(Map<String, Object> paramMap) throws Exception;

}
