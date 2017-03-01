package cn._51app.dao.diy2_0;

import java.util.List;
import java.util.Map;

public interface IIdexDao {

	/**
	 * tengh 2016年11月9日 下午3:30:56
	 * @return
	 * TODO 首页菜单
	 */
	List<Map<String, Object>> getHomeButton(int version);

	/**
	 * tengh 2016年11月9日 下午3:31:02
	 * @param source
	 * @param bannerId
	 * @return
	 * TODO 查询banner轮播图
	 */
	List<Map<String, Object>> getBannerBySource(int source, Integer bannerId,int version);

	/**
	 * tengh 2016年11月9日 下午3:31:16
	 * @return
	 * TODO 首页今日特价商品
	 */
	List<Map<String, Object>> getTodayGoods();

	/**
	 * tengh 2016年11月9日 下午4:47:49
	 * @return
	 * TODO 首页专题
	 */
	List<Map<String, Object>> getHomeSpecials();

	/**
	 * tengh 2016年11月9日 下午5:00:28
	 * @param page 
	 * @return
	 * TODO 首页推荐的商品
	 */
	List<Map<String, Object>> getRecommendGoods(int page);

	/**
	 * tengh 2016年11月10日 下午3:18:59
	 * @param id
	 * @return
	 * TODO 通过顶部导航菜单查询商品
	 */
	List<Map<String, Object>> getGoodsByTopNavIdByPage(int id,int page);

	/**
	 * tengh 2016年11月14日 下午3:36:49
	 * @param id
	 * @param type 
	 * @return
	 * TODO 查询专题下的商品
	 */
	List<Map<String, Object>> getSpecialOfGoods(int id, int type);

	/**
	 * tengh 2016年12月5日 下午9:25:26
	 * @param page
	 * @param storeId
	 * @return
	 * TODO 按商家推荐
	 */
	List<Map<String, Object>> goodRecommendBypage(int page, String storeId);

	/**
	 * tengh 2016年12月27日 下午11:03:20
	 * @param userId
	 * @param mobile
	 * TODO 绑定手机号
	 * @return 
	 */
	int updateMobile(String userId, String mobile);

	List<Map<String, Object>> getVerionList();
	
	/**
	 * TODO 首页页面改版的数据
	 * @param module
	 * @return
	 */
	List<Map<String,Object>>handpick(int module);

	List<Map<String, Object>> getSubGoods(Integer id);
	
}
