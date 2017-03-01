package cn._51app.service.diy2_0;

import java.util.Map;

public interface IZeroGoodsService{

	/**
	 * tengh 2016年11月17日 下午6:30:38
	 * @return
	 * TODO 获取0元购商品
	 */
	String zeroGoods();

	/**
	 * tengh 2016年11月17日 下午7:25:29
	 * @param page
	 * @param type
	 * @param userId
	 * @return
	 * TODO 用户作品集
	 */
	String workListByPage(int page, String type, Integer userId);

	/**
	 * tengh 2016年11月19日 上午10:10:08
	 * @param id
	 * @param userId
	 * @param type 
	 * @return
	 * TODO 给作品点赞/取消赞
	 */
	boolean doLike(Integer id, Integer userId, String type);

	/**
	 * tengh 2016年11月19日 下午1:46:17
	 * @param userId
	 * @param friendId
	 * @param type
	 * @return 
	 * TODO 好友操作
	 */
	boolean doFriend(String userId, String friendId, String type);

	/**
	 * tengh 2016年11月21日 下午6:24:40
	 * @param id
	 * @return
	 * TODO 分享出去的作品详情
	 */
	Map<String, Object> goodsInfo(String id,String userId);

	/**
	 * tengh 2016年11月22日 下午2:18:31
	 * @param userId
	 * @param type
	 * @param page 
	 * @return
	 * TODO 我的作品集
	 */
	String myWorkListByPage(Integer userId, String type, int page);

	/**
	 * tengh 2016年11月22日 下午3:26:00
	 * @param id
	 * @param userId
	 * @return
	 * TODO 作品集的删除
	 */
	boolean delWorkList(int id, Integer userId);

	/**
	 * tengh 2016年11月22日 下午4:00:13
	 * @param id
	 * @param userId
	 * @param ip 
	 * @param mobile 
	 * @param cutCouponId 
	 * @return
	 * TODO 使用砍价券砍价
	 */
	Map<String, Object> cutPrice(int id, Integer userId, String ip, String mobile, String cutCouponId);

	/**
	 * tengh 2016年11月23日 上午10:30:46
	 * @param userId
	 * @return
	 * TODO 好友列表
	 */
	String friends(Integer userId);

	/**
	 * tengh 2016年11月23日 下午1:44:15
	 * @param userId
	 * @return
	 * TODO 作品集
	 */
	String workList(Integer userId);

	/**
	 * tengh 2016年11月23日 下午2:04:56
	 * @param userId
	 * @return
	 * TODO 我的作品首页
	 */
	String myWorkList(Integer userId);

	/**
	 * tengh 2016年11月25日 下午7:00:55
	 * @param id
	 * @return
	 * TODO 查询个人信息
	 */
	Map<String, Object> getPersonInfo(String id);

	/**
	 * tengh 2016年11月28日 下午1:38:31
	 * @param id
	 * @param userId
	 * @return
	 * TODO 砍价放入购物车
	 */
	Map<String, Object> endCutprice(String id, String userId);

	/**
	 * tengh 2016年11月28日 下午5:36:56
	 * @param userId
	 * @param id
	 * @param type
	 * @return
	 * TODO 修改我的作品权限
	 */
	boolean editWorkList(String userId, String id, String type);

	/**
	 * tengh 2016年12月1日 下午2:11:28
	 * @param id
	 * @return
	 * TODO 分享详情
	 */
	String userWorkById(String id) throws Exception;

	/**
	 * tengh 2016年12月1日 下午8:54:13
	 * @param userId
	 * @param id
	 * @return
	 * TODO 通过作品id添加好友
	 */
	boolean doFriendByWork(String userId, String id);

}
