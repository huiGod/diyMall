package cn._51app.dao.diy2_0;

import java.util.Map;

public interface IWorkActivityDao {

	/**
	 * TODO 获取作品排名数据
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public String getWorkList(String userId,String dgurl) throws Exception;

	/**
	 * TODO 查询拥有优惠券的用户
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> openIdUser(Map<String, Object> paramMap) throws Exception;

	/**
	 * TODO 插入新用户（openid）
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	int insertUser(Map<String, Object> paramMap) throws Exception;

	/**
	 * TODO 获取作品详情
	 * @param paramMap
	 * @return
	 */
	Map<String, Object> workInfo(Map<String, Object> paramMap);

	/**
	 * TODO 查询用户投票剩余次数
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	int userActivite(Map<String, Object> paramMap) throws Exception;

	/**
	 * TODO 修改用户点赞次数
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	int userVote(Map<String, Object> paramMap) throws Exception;

	/**
	 * TODO 用户作品添加一个心
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	int upHeart(Map<String, Object> paramMap) throws Exception;

	/**
	 * TODO 查询用户的心数量
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	int workHeart(Map<String, Object> paramMap) throws Exception;

	/**
	 * TODO 更新用户心数量
	 * @param workId
	 * @return
	 * @throws Exception
	 */
	int setUserHeart(String workId) throws Exception;

	/**
	 * TODO app投票次数查询
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	int userActiviteApp(Map<String, Object> paramMap) throws Exception;

	/**
	 * TODO 查询用户id的投票剩余次数
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	int userActiviteforUserId(Map<String, Object> paramMap) throws Exception;

	/**
	 * TODO 用户id的投票次数-1
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	int userVoteforUserId(Map<String, Object> paramMap) throws Exception;

	/**
	 * TODO 获取作品排名数据(userId版)
	 * @param userId
	 * @param dgurl
	 * @return
	 * @throws Exception
	 */
	String getWorkListForApp(String userId, String dgurl,String page) throws Exception;
	
	/**
	 * 
	 * TODO 用户给作品投票
	 * @param paramMap
	 * @return
	 * @author yuanqi 2016年12月13日 下午6:48:27
	 */
	int insertVoteWorks(Map<String, Object> paramMap) throws Exception;
	
	/**
	 * TODO  获取用户对某一作品投票数
	 * @param paramMap
	 * @return
	 * @author yuanqi 2016年12月13日 下午6:47:58
	 */
	int getWorkVoteCount(Map<String, Object> paramMap) throws Exception;
	
}
