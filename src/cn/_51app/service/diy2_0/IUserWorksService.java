package cn._51app.service.diy2_0;

import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import cn._51app.entity.User;

public interface IUserWorksService {

	//用户作品
	String userWorks(String id)throws Exception;

	/**
	 * TODO 保存用户作品
	 * @param name
	 * @param cont
	 * @param isopen
	 * @param saveImg
	 * @param goodId
	 * @param userId
	 * @param textureIds
	 * @return
	 * @throws Exception
	 */
	String saveWorks(String name, String cont, String isopen, String saveImg, String goodId, String userId,String textureIds,String type)throws Exception;

	boolean delWorks(String id)throws Exception;

	/**
	 * TODO 手机发送验证码
	 * @param phone
	 * @param smscode
	 * @return
	 * @throws Exception
	 */
	boolean sendMsg(String phone, String smscode) throws Exception;

	/**
	 * 修改用户信息
	 * @param imgurl  头像
	 * @param nickName  昵称
	 * @param phone 手机号
	 * @param sex  性别1男2女
	 * @param userId  用户id
	 * @return
	 * @throws Exception
	 */
	String editUserInfo(MultipartFile imgurl, String nickName, String phone, String sex,String openid,String app,String deviceNo,String token)
			throws Exception;

	/**
	 * TODO 读取用户信息
	 * @param deviceNo
	 * @param app
	 * @return
	 * @throws Exception
	 */
	String loadUserInfo(String deviceNo, String app,String token) throws Exception;

	/**
	 * TOOD 判断用户openid决定是否绑定微信
	 * @param openid
	 * @return
	 */
	boolean haveOpenid(String openid);

	/**
	 * 
	 * @param code
	 * @return
	 * @throws Exception
	 */
	String weixinLogin(String code) throws Exception;

	/**
	 * TODO 插入一条用户
	 * @param deviceNo
	 * @param app
	 * @return
	 */
	boolean insertUser(String deviceNo, String app);
	
	/**
	 * TODO 环信返回用户名密码
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> returnOne(int id,String token) throws Exception;

	/**
	 * tengh 2016年9月13日 上午11:30:22
	 * @param deviceNo
	 * @param app
	 * @param version
	 * @param deviceToken
	 * @return
	 * TODO 保存用户统计信息
	 */
	boolean infoUser(String deviceNo, String app, String version, String deviceToken);
	
	/**
	 * TODO 生成订单-1
	 * @param workId
	 * @param userId
	 * @param num
	 * @return
	 */
	String createOrder(String workId,String num,String textureIds,String app,String deviceNo)throws Exception;
	
	/**
	 * TODO 加入购物车
	 * @param workId
	 * @param userId
	 * @param num
	 * @return
	 */
	boolean addShops(String workId,String num,String textureIds,String app,String deviceNo)throws Exception;

	/**
	 * TODO 编辑用户作品
	 * @param workId
	 * @param cont
	 * @param name
	 * @param isopen
	 * @param textureIds
	 * @return
	 * @throws Exception
	 */
	boolean editWorks(String workId, String cont, String name, String isopen, String textureIds) throws Exception;

	/**
	 * TODO 查询作品详情
	 * @param workId
	 * @return
	 * @throws Exception 
	 */
	String worksInfo(String workId) throws Exception;

	/**
	 * TODO 用户作品加入购物车（id版）
	 * @param workId
	 * @param num
	 * @param textureIds
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	boolean addShopsForId(String workId, String num, String textureIds, String userId,String lettering,String modId) throws Exception;

	/**
	 * TODO 立即购买作品（id版）
	 * @param workId
	 * @param num
	 * @param textureIds
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	String createOrderForId(String workId, String num, String textureIds, String userId,String lettering,String modId) throws Exception;

	/**
	 * TODO 修复所有用户的购物车
	 * @return
	 * @throws Exception
	 */
	boolean amendShop() throws Exception;

	/**
	 * TODO 意见反馈
	 * @param contract
	 * @param content
	 * @return
	 * @throws Exception
	 */
	boolean retroaction(String contract, String content) throws Exception;

	/**
	 * tengh 2016年12月4日 下午6:06:58
	 * @param orderNo
	 * TODO 更新0元购订单的状态
	 */
	void updateZeroStatus(String orderNo);

	/**
	 * TODO 新版用户登陆
	 * @param imgurl
	 * @param nickName
	 * @param phone
	 * @param sex
	 * @param openid
	 * @param app
	 * @param deviceNo
	 * @param state
	 * @return
	 * @throws Exception 
	 */
	String editUserInfoForAndroid(MultipartFile imgurl, String nickName, String phone, String sex, String openid,
			String app, String deviceNo, String state) throws Exception;

	/**
	 * tengh 2016年12月31日 上午1:02:19
	 * @param user
	 * @return
	 * TODO 登录或者注册
	 */
	String login(User user);

	/**
	 * tengh 2016年12月31日 上午4:23:40
	 * @param imgurl
	 * @param nickName
	 * @param phone
	 * @param sex
	 * @param userId
	 * @return
	 * TODO 修改个人信息
	 */
	boolean editUser(MultipartFile imgurl, String nickName, String sex, String userId);

}
