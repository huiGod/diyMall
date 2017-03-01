package cn._51app.dao.diy2_0;

import java.util.List;
import java.util.Map;

import cn._51app.entity.User;

public interface IUserWorksDao {

	String userWorks(Map<String, Object> paramMap) throws Exception;

	String saveWorks(Map<String, Object> paramMap) throws Exception;

	int delWorks(String id) throws Exception;

	int editUserInfo(Map<String, Object> paramMap) throws Exception;

	/**
	 *TODO 读取用户信息
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	Map<String,Object> loadUserInfo(Map<String, Object> paramMap) throws Exception;

	/**
	 * TODO 判断用户openid
	 * @param openid
	 * @return
	 */
	Integer haveOpenid(String openid);

	/**
	 * TODO 插入微信用户
	 * @param paramMap
	 * @return
	 */
	int insertOpenid(Map<String, Object> paramMap);

	/**
	 * TODO 根据openid获取用户信息
	 * @param openid
	 * @return
	 */
	Map<String, Object> findUser4Openid(String openid);

	/**
	 * TODO 插入一条用户数据
	 * @param deviceNo
	 * @param app
	 * @return
	 */
	int insertUserHome(String deviceNo, String app);
	
	/**
	 * TODO 环信返回一个账号密码
	 * @param id
	 * @return
	 */
	Map<String,Object> returnPassword(int id);
	
	/**
	 * 环信插入一个密码
	 * @param id
	 * @param password
	 * @return
	 */
	int insertPassword(Map<String, Object> paramMap);

	/**
	 * TODO 根据用户id获取用户信息
	 * @param paramMap
	 * @return
	 */
	Map<String, Object> loadUserInfoById(Map<String, Object> paramMap);

	/**
	 * tengh 2016年9月13日 上午11:31:17
	 * @param deviceNo
	 * @param app
	 * @param version
	 * @param deviceToken
	 * @return
	 * TODO 保存用户信息
	 */
	boolean infoUser(String deviceNo, String app, String version, String deviceToken);

	/**
	 * TODO 根据作品id查询作品
	 * @param workId
	 * @return
	 */
	Map<String, Object> queryUserWork(String workId);

	/**
	 * TODO判断用户是否登陆过
	 * @param paramMap
	 * @return
	 * @throws Exception 
	 */
	int isOpenUser(Map<String, Object> paramMap) throws Exception;

	/**
	 * TODO 插入一条新用户
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	int insertUser(Map<String, Object> paramMap) throws Exception;

	/**
	 * TODO 绑定优惠券userId,具体优惠券coupon_id
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	int bindingCoupon(Map<String, Object> paramMap) throws Exception;

	/**
	 * TODO 绑定购物车
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	int bindingShop(Map<String, Object> paramMap) throws Exception;

	/**
	 * TODO 绑定订单
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	int bindingOrders(Map<String, Object> paramMap) throws Exception;

	/**
	 * TODO 绑定地址
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	int bindingAddress(Map<String, Object> paramMap) throws Exception;

	/**
	 * TODO 绑定评论
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	int bindingEvaluation(Map<String, Object> paramMap) throws Exception;

	/**
	 * TODO 绑定用户作品
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	int bindingWork(Map<String, Object> paramMap) throws Exception;

	/**
	 * TODO 编辑用户作品
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	int editWorkds(Map<String, Object> paramMap) throws Exception;

	/**
	 * TODO 查询作品详情
	 * @param workId 作品id
	 * @return
	 */
	Map<String, Object> queryWork(String workId);

	/**
	 * TODO deviceNo,app,openid验证修改用户
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	int editUserInfoAndOpenid(Map<String, Object> paramMap) throws Exception;

	/**
	 * TODO 插入意见反馈
	 * @param contract
	 * @param content
	 * @return
	 * @throws Exception
	 */
	int retroaction(String contact, String content) throws Exception;

	/**
	 * tengh 2016年11月30日 下午8:08:33
	 * @param goodId
	 * @param textureIds 
	 * @return
	 * TODO 查询精品会默认材质
	 */
	Map<String, Object> queryDefaultTextureIds(String goodId, String textureIds);

	/**
	 * tengh 2016年12月1日 下午5:52:58
	 * @param workId
	 * @return
	 * TODO 判断作品是否是0元购
	 */
	boolean checkIsZeroGood(String workId);

	/**
	 * tengh 2016年12月2日 下午5:36:05
	 * @param userId
	 * @param workId
	 * @return
	 * TODO 查看订单购物车是否有作品
	 */
	boolean isShopOrOrder(String userId, String workId);

	/**
	 * TODO 查询用户作品详细
	 * @param workId
	 * @return
	 */
	Map<String, Object> queryUserWorkDetailInfo(String workId);

	/**
	 * tengh 2016年12月4日 下午6:07:59
	 * @param orderNo
	 * TODO 更新0元购商品订单
	 */
	void updateZeroStatus(String orderNo);

	/**
	 * TODO 查看是否已经有这个环信账号的用户
	 * @param userName
	 * @return
	 */
	boolean checkHxUserName(String userName);

	/**
	 * TODO 查询用户信息使用状态
	 * @param paramMap
	 * @return
	 */
	List<Map<String, Object>> loadUserInfoByIdWithState(Map<String, Object> paramMap);

	/**
	 * TODO 编辑用户信息state版
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	int editUserInfoWithState(Map<String, Object> paramMap) throws Exception;

	/**
	 * tengh 2016年12月31日 上午1:10:20
	 * @param user
	 * @return
	 * TODO 默认注册/登录新账号
	 */
	Map<String, Object> createNewUser(User user);

	/**
	 * tengh 2016年12月31日 上午2:00:07
	 * @param user
	 * @return
	 * TODO 手机号登录/注册
	 */
	Map<String, Object> loginUserByPhone(User user);

	/**
	 * tengh 2016年12月31日 上午2:09:53
	 * @param user
	 * @return
	 * TODO 授权登录/注册
	 */
	Map<String, Object> loginUserByOpenid(User user);

	/**
	 * tengh 2016年12月31日 上午3:07:04
	 * @param user
	 * @return
	 * TODO 查询个人信息
	 */ 
	Map<String, Object> getUserInfo(User user);

	/**
	 * tengh 2016年12月31日 上午3:28:09
	 * @param id
	 * @param userName
	 * @param password
	 */
	void updateInfo(Integer id, String userNameAndPass, String imgurl);

	/**
	 * tengh 2016年12月31日 上午3:54:56
	 * @param user
	 * @param couponId
	 * @return
	 * TODO 绑定优惠券
	 */
	int bindingCoupon2(User user, String couponId);

	/**
	 * tengh 2016年12月31日 上午4:03:55
	 * @param user
	 * @return
	 * TODO 绑定购物
	 */
	int bindingShop2(User user);

	/**
	 * tengh 2016年12月31日 上午4:11:54
	 * @param user
	 * @return
	 * TODO 绑定地址
	 */
	int bindingAddress2(User user);

	/**
	 * tengh 2016年12月31日 上午4:15:47
	 * @param user
	 * @return
	 * TODO
	 */
	int bindingOrders2(User user);

	int bindingWork2(User user);

	void updateNewInfo(String userId, String nickName, String sex, String resultpath);

}
