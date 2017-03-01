package cn._51app.dao.diy2_0;

import java.util.Map;

public interface IActiviteDao {

	/**
	 * tengh 2016年8月18日 下午6:53:02
	 * @param type
	 * @return
	 * TODO 活动列表信息
	 */
	Map<String, Object> activiteList(String type);

	/**
	 * TODO 根据活动id获取活动
	 * @param activityId
	 * @return
	 */
	Map<String, Object> activiterList2(String activityId);

}
