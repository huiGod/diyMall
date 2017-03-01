package cn._51app.dao.diy2_0;

import java.util.List;
import java.util.Map;

public interface ISearchGoodsDao {
	
	/**
	 * 
	 * TODO 查找关键字 
	 * @param name
	 * @return
	 * @author yuanqi 2017年2月17日 下午4:11:01
	 */
	List<Map<String, Object>> getKeywords(String name);
	
	/**
	 * 
	 * TODO 根据关键字查找商品 
	 * @param name 商品关键字
	 * @param title 商品标签
	 * @return
	 * @author yuanqi 2017年2月17日 下午4:09:35
	 */
	List<Map<String, Object>> getGoodsInfo(int page,String name,Integer goods_id,Integer keyword_id);

	/**
	 * 
	 * TODO 获取用户的搜索记录 
	 * @param userId
	 * @return
	 * @author yuanqi 2017年2月20日 下午7:43:00
	 */
	List<Map<String, Object>> getSearchLog(String userId);
	
	/**
	 * 
	 * TODO 添加用户搜索记录 
	 * @param userId 用户id
	 * @param content 搜索值
	 * @author yuanqi 2017年2月20日 下午7:42:31
	 */
	void addSearchLog(String userId,String content);

	/**
	 * 
	 * TODO 清除用户的搜索记录 
	 * @param userId
	 * @return
	 * @author yuanqi 2017年2月20日 下午7:42:14
	 */
	int clearLogs(String userId);

	/**
	 * 
	 * TODO 获取热门推荐 
	 * @return
	 * @author yuanqi 2017年2月20日 下午8:25:28
	 */
	List<Map<String, Object>> getRecommand();

	List<Map<String, Object>> getGoodsInfo(Map<String, Object> paramMap);

}
