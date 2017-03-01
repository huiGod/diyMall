package cn._51app.service.diy2_0.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn._51app.dao.diy2_0.IUnicomGiftDao;
import cn._51app.dao.diy2_0.impl.CouponDao2;
import cn._51app.service.BaseService;
import cn._51app.service.diy2_0.IUnicomGiftService;
import cn._51app.util.CheckPhoneUtil;
import cn._51app.util.OCSKey;
import cn._51app.util.PropertiesUtil;

@Service
public class UnicomGiftService extends BaseService implements
		IUnicomGiftService {
	
	private final static int LIMIT_COUNT = 1000000;//100万

	@Autowired
	private IUnicomGiftDao iUnicomGiftDao;

	@Autowired
	private CouponDao2 couponDao2;

	// 图片前缀
	private String dgurl = PropertiesUtil.getValue("diy.goods.url");

	
	@Override
	public boolean addGift(String mobile) {
		if (!iUnicomGiftDao.isReceive(mobile)) {// 如果没有领取
			try {
				int count = 1;
				if (receiveNum() < LIMIT_COUNT) {
					//如果领取次数操过1000000，每次加1，否则每次加 100~150的随机数
					count = new Random().nextInt(50)+100;
				}
				return iUnicomGiftDao.addGift(mobile,count);
			} catch (Exception e) {// 领取失败
				e.printStackTrace();
			}
		}
		return false;
	}

	@Override
	public int receiveNum() {
		return iUnicomGiftDao.receiveNum();
	}

	@Override
	public boolean isLogin(String userId) {
		try {
			return iUnicomGiftDao.isLogin(userId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean addToShop(String userId, String goodsId, String textureIds,
			String textureName) {
		
		String id = "";
		try {
			id = iUnicomGiftDao.getReceiveGoodsId(userId);
		} catch (Exception e) {}
		
		if (StringUtils.isBlank(id)) {
			try {
				return iUnicomGiftDao.addToShop(userId, goodsId, textureIds,textureName);
			} catch (Exception e) {}
		}
		return false;
	}

	@Override
	public String getMobileByUser(String userId) throws Exception {
		return iUnicomGiftDao.getMobileByUser(userId);
	}

	@Override
	public String getUnicomGoods() throws Exception {
		String cacheKey = OCSKey.DIY_UNICOM_GOODS;
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("cacheKey", cacheKey);// 缓存对应的键
		paramMap.put("cacheTime", TIMEOUT);// 缓存时间
		return iUnicomGiftDao.getUnicomGoods(paramMap);
	}

	
	@Override
	@Transactional(rollbackFor=Exception.class)
	public boolean getUnicomCoupon(String userId) throws Exception {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("userId", userId);
		paramMap.put("type", 8);
		paramMap.put("dgurl", dgurl);

		if (!iUnicomGiftDao.isReceiveCoupon(userId)) {
			List<Map<String, Object>> list = this.couponDao2.getCouponListByType(paramMap);
			int result = 0;
			for (Map<String, Object> m : list) {
				// 优惠券id
				String id = m.get("id") == null ? "0" : m.get("id").toString();
				String valid = m.get("valid") == null ? "0" : m.get("valid").toString();
				paramMap.put("valid", valid);
				paramMap.put("userId", userId);
				paramMap.put("sys", "1");
				paramMap.put("id", id);
				result += this.couponDao2.addCouponForId(paramMap);
			}
			return result > 0;
		}
		return false;
	}

	@Override
	public String isReceiveByUser(String userId) throws Exception{
		//默认可以领取
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("goodsId", "");
		map.put("coupon", true);
		
		try {
			if (isLogin(userId)) {
				String mobile = getMobileByUser(userId);
				getUnicomCouponByWeixin(userId, mobile);
				if (CheckPhoneUtil.isCompanyPhone(mobile) || CheckPhoneUtil.isChinaUnicomPhoneNum(mobile)) {
	
					try {
						String goodsId = String.valueOf(iUnicomGiftDao.getReceiveGoodsId(userId));
						map.put("goodsId", goodsId);
					} catch (Exception e) {
						
					}
	
					if (iUnicomGiftDao.isReceiveCoupon(userId)) {
						map.put("coupon",false);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return super.toJson(map);
	}

	@Override
	public void getUnicomCouponByWeixin(String userId, String mobile) {
		if (iUnicomGiftDao.isReceive(mobile)) {
			try {
				if (!iUnicomGiftDao.isReceiveCoupon(userId)) {	
					getUnicomCoupon(userId);//领取优惠券
				}			
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
