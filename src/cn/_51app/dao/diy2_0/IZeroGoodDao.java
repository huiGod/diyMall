package cn._51app.dao.diy2_0;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IZeroGoodDao {

	/**
	 * tengh 2016年11月17日 下午6:43:15
	 * @return
	 * TODO 查询0元购商品
	 */
	List<Map<String, Object>> zeroGoods();

	/**
	 * tengh 2016年11月18日 下午4:38:19
	 * @param type
	 * @param page
	 * @param paraTemp 
	 * @param userId 
	 * @return
	 * TODO 分页查询作品集
	 */
	List<Map<String, Object>> workListByPage(String type, int page, Set<String> paraTemp, Integer userId);

	/**
	 * tengh 2016年11月21日 下午6:26:51
	 * @param id
	 * @return
	 * TODO 分享的详情
	 */
	Map<String, Object> goodsInfo(String id);

	/**
	 * tengh 2016年11月22日 下午2:23:15
	 * @param userId
	 * @param type
	 * @param page 
	 * @return
	 * TODO 查看我的作品集
	 */
	List<Map<String, Object>> myWorkListByPage(Integer userId, String type, int page);

	/**
	 * tengh 2016年11月22日 下午3:26:50
	 * @param id
	 * @param userId
	 * @return
	 * TODO 作品集的删除
	 */
	int delWorkList(int id, Integer userId);

	/**
	 * tengh 2016年11月22日 下午4:25:48
	 * @param id
	 * @param randMoney
	 * TODO 砍价
	 */
	int cutPrice(int id, double randMoney);

	/**
	 * tengh 2016年11月22日 下午4:38:19
	 * @param userId
	 * @return
	 * TODO 查询砍价用户的信息
	 */
	Map<String, Object> personInfo(Integer userId);

	/**
	 * tengh 2016年11月22日 下午7:17:46
	 * @param id
	 * @return
	 * TODO 检查砍价时间是否已过
	 */
	boolean checkCutTime(int id);

	/**
	 * tengh 2016年11月23日 上午10:39:08
	 * @param ids
	 * @return
	 * TODO 好友列表数据
	 */
	List<Map<String, Object>> friends(Set<String> ids);

	/**
	 * tengh 2016年11月23日 下午2:42:31
	 * @param id
	 * @param userId
	 * @return
	 * TODO 是否是自己的产品
	 */
	boolean checkWork(int id, Integer userId);

	/**
	 * tengh 2016年11月23日 下午2:59:32
	 * @param userId
	 * @param mobile
	 * @return
	 * TODO 验证手机号
	 */
	boolean checkMobile(Integer userId, String mobile);

	/**
	 * tengh 2016年11月23日 下午3:41:46
	 * @param goodId
	 * @return
	 * TODO 查询0元购的商品原价
	 */
	double getOrgPrice(String goodId);

	/**
	 * tengh 2016年11月25日 下午7:01:59
	 * @param id
	 * @return
	 * TODO 查询个人信息
	 */
	Map<String, Object> getPersonInfo(String id);

	/**
	 * tengh 2016年11月28日 下午3:53:16
	 * @param id
	 * @param userId
	 * @return 提前结束砍价
	 * TODO
	 */
	boolean endCutprice(String id);

	/**
	 * tengh 2016年11月28日 下午3:57:35
	 * @param id
	 * @param userId 
	 * @return
	 * TODO 查询作品信息
	 */
	Map<String, Object> getWorkInfo(String id, String userId);

	/**
	 * tengh 2016年11月28日 下午5:42:05
	 * @param id
	 * @param userId
	 * @param type
	 * @return
	 * TODO 修改作品权限
	 */
	boolean editWorkList(String id, String userId, String type);

	/**
	 * tengh 2016年11月28日 下午7:31:13
	 * @return
	 * TODO 查询正在砍价中过期的作品
	 */
	List<Map<String, Object>> queryEndList();

	/**
	 * tengh 2016年11月28日 下午7:59:13
	 * @param ids
	 * TODO 扫描砍价结束
	 */
	void endCutPriceStatus(String ids);

	/**
	 * tengh 2016年12月1日 下午2:13:56
	 * @param id
	 * @return
	 * TODO 分享出去的作品详情
	 */
	Map<String, Object> userWorkById(String id);

	/**
	 * tengh 2016年12月1日 下午6:01:04
	 * @param workId
	 * @param string
	 * TODO 更新作品状态
	 */
	void updateCutStatus(String workId, String string);

	/**
	 * tengh 2016年12月1日 下午8:23:42
	 * @param id
	 * @return
	 * TODO 通过作品id获取用户id
	 */
	Integer getFriendIdByWorkId(int id);
}
