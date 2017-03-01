package cn._51app.service.diy2_0;


public interface ISearchGoodsService {
	
	
	public String getKeywords(String name);

	
	public String getGoodsInfo(int page, String name,Integer goods_id,Integer keyword_id);
	
	public void addSearchLog(String userId,String content);

	/**
	 * 
	 * TODO 获取用户搜索记录 
	 * @param userId
	 * @return
	 * @author yuanqi 2017年2月21日 下午2:46:00
	 */
	public String getLogs(String userId);

	/**
	 * 
	 * TODO 清空用户搜索记录 
	 * @param userId
	 * @return
	 * @author yuanqi 2017年2月21日 下午2:46:33
	 */
	public boolean clearLogs(String userId);

	/**
	 * 
	 * TODO 热门推荐的关键字
	 * @return
	 * @author yuanqi 2017年2月21日 下午2:40:14
	 */
	public String getRecommand();


	public String getGoodsInfo(int page, String name);
}
