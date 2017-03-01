package cn._51app.service.diy2_0.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn._51app.dao.diy2_0.IWorkActivityDao;
import cn._51app.dao.diy2_0.impl.CouponDao2;
import cn._51app.service.BaseService;
import cn._51app.service.diy2_0.IWorkActivityService;
import cn._51app.util.OCSKey;
import cn._51app.util.PropertiesUtil;
import cn._51app.util.RedisUtil;

@Service
public class WorkActivityService extends BaseService implements
		IWorkActivityService {

	@Autowired
	private IWorkActivityDao iworkActivityDao;

	@Autowired
	private CouponDao2 couponDao2;

	// 图片前缀
	private String dgurl = PropertiesUtil.getValue("diy.goods.url");

	@Override
	public String getWorkList(String openid) throws Exception {
		return iworkActivityDao.getWorkList(openid, dgurl);
	}

	@Override
	public String getWorkListForApp(String userId,String page) throws Exception {
		if(page.isEmpty()){
			page="0";
		}
		return iworkActivityDao.getWorkListForApp(userId, dgurl,page);
	}

	@Override
	public String wxLogin(Map<String, Object> paramMap) throws Exception {
		Map<String, Object> map = iworkActivityDao.openIdUser(paramMap);
		if (map == null) {
			// 默认用户点赞次数为1
			int result = iworkActivityDao.insertUser(paramMap);
			if (result > 0) {
				map = iworkActivityDao.openIdUser(paramMap);
			}
		}
		return super.toJson(map);
	}

	@Override
	public String workInfo(String openid, String workId) throws Exception {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("userId", openid);
		paramMap.put("workId", workId);
		paramMap.put("dgurl", dgurl);
		paramMap.put("type", 0);
		// 查询作品信息
		Map<String, Object> map = iworkActivityDao.workInfo(paramMap);
		boolean islike=RedisUtil.sismember(OCSKey.DIY_WORKLIST_USREIDS_+workId, openid+"");
		if(islike){
			map.put("isVote", 1);
		}else{
			map.put("isVote", 0);
		}
		return super.toJson(map);
	}

	@Override
	public String workInfoApp(String userId, String workId) throws Exception {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("userId", userId);
		paramMap.put("workId", workId);
		paramMap.put("dgurl", dgurl);
		// 查询作品信息
		Map<String, Object> map = iworkActivityDao.workInfo(paramMap);
		boolean islike=RedisUtil.sismember(OCSKey.DIY_WORKLIST_USREIDS_+workId, userId+"");
		if(islike){
			map.put("isVote", 1);
		}else{
			map.put("isVote", 0);
		}
		
		return super.toJson(map);
	}

//	@Override
//	@Transactional(rollbackFor = Exception.class)
//	public boolean userVote(String openid, String workId, String workUser)
//			throws Exception {
//		Map<String, Object> paramMap = new HashMap<String, Object>();
////		paramMap.put("openid", openid);
////		paramMap.put("workId", workId);
////		// 10元-5元优惠券
////		paramMap.put("id", 7);
////		paramMap.put("userId", workUser);
//		
//		paramMap.put("userId", openid);
//		paramMap.put("workId", workId);
//		paramMap.put("type", 1);//用户投票
//
//		// 查询用户剩余投票次数
//		int activite = iworkActivityDao.userActivite(paramMap);
//		if (activite > 0) {
//			// 投票次数-1
//			int result = iworkActivityDao.userVote(paramMap);
//			if (result <= 0) {
//				throw new RuntimeException();
//			}
//			// 用户作品+心
//			result = iworkActivityDao.upHeart(paramMap);
//			if (result <= 0) {
//				throw new RuntimeException();
//			}
//			// 用户心数量
//			int heart = iworkActivityDao.workHeart(paramMap);
//			// 能被30整除的心是送优惠券的时候
//			if (heart % 30 == 0 && heart >= 30) {
//				result = couponDao2.addCouponByUserId(paramMap);
//			}
//			if (result <= 0) {
//				throw new RuntimeException();
//			}
//			return true;
//		}
//		return false;
//	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean userVote2(String openid, String workId, String workUser)
			throws Exception {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("userId", openid);
		paramMap.put("workId", workId);
		paramMap.put("type", 0);//用户投票
		
		boolean islike=RedisUtil.sismember(OCSKey.DIY_WORKLIST_USREIDS_+workId, openid+"");
		if (!islike) {
			
		// 查询用户给该作品的投票次数
//		int count = iworkActivityDao.getWorkVoteCount(paramMap);
//		if (count <= 0) {
//			int result = iworkActivityDao.insertVoteWorks(paramMap);
//			if (result <= 0) {
//				throw new RuntimeException();
//			}
			// 用户作品+心
//			result = iworkActivityDao.upHeart(paramMap);
			RedisUtil.zincrby(OCSKey.DIY_WORKLIST_LIKE, 1, workId+"");
			RedisUtil.sadd(OCSKey.DIY_WORKLIST_USREIDS_+workId, openid+"");
			// 用户心数量
			double heart = RedisUtil.zscore(OCSKey.DIY_WORKLIST_LIKE, workId+"");
			// 能被30整除的心是送优惠券的时候
			if (heart % 30 == 0 && heart >= 30) {
				// 10元-5元优惠券
				paramMap.put("id", 7);
				paramMap.put("userId", workUser);
				couponDao2.addCouponByUserId(paramMap);
			}
			return true;
		}
		return false;
	}

//	@Override
//	@Transactional(rollbackFor = Exception.class)
//	public boolean userVoteforApp(String userId, String workId, String workUser)
//			throws Exception {
//		Map<String, Object> paramMap = new HashMap<String, Object>();
//		paramMap.put("user", userId);
//		paramMap.put("workId", workId);
//		// 10元-5元优惠券
//		paramMap.put("id", 7);
//		paramMap.put("userId", workUser);
//
//		// 查询用户剩余投票次数
//		int activite = iworkActivityDao.userActiviteforUserId(paramMap);
//		if (activite > 0) {
//			// 投票次数-1
//			int result = iworkActivityDao.userVoteforUserId(paramMap);
//			if (result <= 0) {
//				throw new RuntimeException();
//			}
//			// 作品+心
//			result = iworkActivityDao.upHeart(paramMap);
//			if (result <= 0) {
//				throw new RuntimeException();
//			}
//			// 用户心数量
//			int heart = iworkActivityDao.workHeart(paramMap);
//			// 能被30整除的心是送优惠券的时候
//			if (heart % 30 == 0 && heart > 30) {
//				result = couponDao2.addCouponByUserId(paramMap);
//			}
//			if (result <= 0) {
//				throw new RuntimeException();
//			}
//			return true;
//		}
//		return false;
//	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean userVoteforApp2(String userId, String workId, String workUser)
			throws Exception {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("userId", userId);
		paramMap.put("workId", workId);
		paramMap.put("type", 0);//微信投票
		

		// 查询用户给该作品的投票次数
//		int count = iworkActivityDao.getWorkVoteCount(paramMap);
		boolean islike=RedisUtil.sismember(OCSKey.DIY_WORKLIST_USREIDS_+workId, userId+"");
		if (!islike) {
//			int result = iworkActivityDao.insertVoteWorks(paramMap);
//			if (result <= 0) {
//				throw new RuntimeException();
//			}
			RedisUtil.zincrby(OCSKey.DIY_WORKLIST_LIKE, 1, workId+"");
			RedisUtil.sadd(OCSKey.DIY_WORKLIST_USREIDS_+workId, userId+"");
			// 作品+心
//			result = iworkActivityDao.upHeart(paramMap);
//			if (result <= 0) {
//				throw new RuntimeException();
//			}
			// 用户心数量
//			int heart = iworkActivityDao.workHeart(paramMap);
			double heart = RedisUtil.zscore(OCSKey.DIY_WORKLIST_LIKE, workId+"");
			// 能被30整除的心是送优惠券的时候
			if (heart % 30 == 0 && heart > 30) {
				// 10元-5元优惠券
				paramMap.put("id", 7);
				paramMap.put("userId", workUser);
				couponDao2.addCouponByUserId(paramMap);
			}
			return true;
		}
		return false;
	}

}
