package cn._51app.service.diy2_0;

import java.util.Map;

public interface IIndexService {

	/**
	 * tengh 2016年11月10日 上午11:54:05
	 * @return
	 * TODO 首页数据
	 */
	String home();

	/**
	 * tengh 2016年11月10日 上午11:54:10
	 * @param page
	 * @return
	 * TODO 首页推荐商品分页
	 */
	String homeRecommendByPage(int page);

	/**
	 * tengh 2016年11月10日 下午3:11:37
	 * @param id
	 * @return
	 * TODO 顶部导航跳转信息
	 */
	String getTopNavInfoById(int id);

	/**
	 * tengh 2016年11月10日 下午4:12:03
	 * @param id
	 * @param page
	 * @return
	 * TODO 下拉刷新顶部导航页下面商品信息
	 */
	String topNavInfoByPage(int id, int page);

	/**
	 * tengh 2016年11月10日 下午6:38:22
	 * @param type
	 * @param id
	 * @return 专题详情
	 * TODO
	 */
	String specialInfoById(int type, int id);

	/**
	 * TODO分类页面
	 * @return
	 */
	String sortHome(String topType);

	/**
	 * tengh 2016年12月5日 下午9:22:39
	 * @param page
	 * @param storeId 
	 * @return
	 * TODO 店铺分页
	 */
	String goodRecommendBypage(int page, String storeId);

	/**
	 * tengh 2016年12月27日 下午10:59:04
	 * @param userId
	 * @param mobile
	 * @return
	 * TODO 绑定手机号
	 */
	Map<String, Object> boundMobile(String userId, String mobile);

	/**
	 * tengh 2017年1月17日 下午8:57:28
	 * @return
	 * TODO 安卓最新应用列表
	 */
	String updateVersion();
	
	/**
	 * TODO 首页改版页面数据
	 * @return
	 */
	public String homeRevision();

}
