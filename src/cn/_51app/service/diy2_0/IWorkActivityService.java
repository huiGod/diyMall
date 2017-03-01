package cn._51app.service.diy2_0;

import java.util.Map;

public interface IWorkActivityService {

	public String getWorkList(String userId)throws Exception;

	String wxLogin(Map<String, Object> paramMap) throws Exception;

	String workInfo(String openid, String workId) throws Exception;

	/**
	 * TODO 用户投票的一系列操作
	 * @param openid
	 * @param workId
	 * @param workUser
	 * @return
	 * @throws Exception
	 */
//	boolean userVote(String openid, String workId, String workUser) throws Exception;
	
	/**
	 * TODO 用户投票的一系列操作
	 * @param openid
	 * @param workId
	 * @param workUser
	 * @return
	 * @throws Exception
	 */
	boolean userVote2(String openid, String workId, String workUser) throws Exception;

	/**
	 * TODO 用户投票页面（app）
	 * @param userId
	 * @param workId
	 * @return
	 * @throws Exception
	 */
	String workInfoApp(String userId, String workId) throws Exception;
	
	
	/**
	 * TODO 用户投票的一系列操作userId
	 * @param userId
	 * @param workId
	 * @param workUser
	 * @return
	 * @throws Exception
	 */
//	boolean userVoteforApp(String userId, String workId, String workUser) throws Exception;
	
	/**
	 * TODO 用户投票的一系列操作userId
	 * @param userId
	 * @param workId
	 * @param workUser
	 * @return
	 * @throws Exception
	 */
	boolean userVoteforApp2(String userId, String workId, String workUser) throws Exception;
	
	

	/**
	 * TODO 用户作品排名（userid）
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	String getWorkListForApp(String userId,String page) throws Exception;
}
