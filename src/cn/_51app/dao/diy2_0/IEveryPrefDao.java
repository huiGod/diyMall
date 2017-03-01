package cn._51app.dao.diy2_0;

import java.util.List;
import java.util.Map;



public interface IEveryPrefDao {

	public Map<String, Object> load();
	
	public List<Map<String, Object>> getActivities();
}
