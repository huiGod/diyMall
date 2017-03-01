package cn._51app.service.diy2_0.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.jni.OS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn._51app.dao.diy2_0.INewPrefDao;
import cn._51app.dao.diy2_0.impl.CouponDao2;
import cn._51app.dao.ocs.OCSDao;
import cn._51app.service.BaseService;
import cn._51app.service.diy2_0.INewPrefService;
import cn._51app.util.JSONUtil;
import cn._51app.util.OCSKey;
import cn._51app.util.PropertiesUtil;

@Service
public class NewPrefService extends BaseService implements INewPrefService {
	
	@Autowired
	private INewPrefDao newPrefDao;
	
	@Autowired
	private OCSDao ocsDao;
	
	@Autowired
	private CouponDao2 couponDao2;
	
	private String preImgUrl=PropertiesUtil.getValue("diy.goods.url");

	@Override
	public String getLoad(Integer userId) {
		String cacheKey = OCSKey.DIY_NEWPREF_LOAD;
		String cacheValue = this.ocsDao.query(cacheKey);
		if (StringUtils.isBlank(cacheValue)) {
			Map<String, Object> mapLoad = new HashMap<String, Object>();
			
			List<Map<String, Object>> goodsInfos = newPrefDao.getNewPrefGoods();
			if (goodsInfos != null) {
				for (Map<String, Object> map : goodsInfos) {
					String iconUrl = (String) map.get("iconUrl");
					if (StringUtils.isNotBlank(iconUrl)) {
						map.put("iconUrl", preImgUrl+iconUrl);
					}
				}
			}
			
			mapLoad.put("goodsInfo",goodsInfos);
			mapLoad.put("coupon", true);
			
			if (isLogin(userId)) {
				if (newPrefDao.isGetCoupon(userId)) {
					mapLoad.put("coupon",false);
				}
			}
			
			cacheValue = JSONUtil.convertObjectToJSON(mapLoad);
			ocsDao.insert(cacheKey, cacheValue, 0);
			System.out.println("从数据库获取");
		}else {
			System.out.println("从缓存获取");
		}
		return cacheValue;
	}
	
	@Override
	public boolean isLogin(Integer userId) {
		return newPrefDao.isLogin(userId);
	}

	@Override
	public boolean isGetCoupon(Integer userId) {
		return newPrefDao.isGetCoupon(userId);
	}

	@Override
	@Transactional(rollbackFor=Exception.class)
	public boolean getCouponByUser(Integer userId) throws Exception {

		List<Map<String, Object>> list = this.newPrefDao.getNewPrefCoupon();
		int result = 0;
		for (Map<String, Object> paramMap : list) {
			// 优惠券id
			String id = paramMap.get("id") == null ? "0" : paramMap.get("id").toString();
			paramMap.put("userId", userId);
			paramMap.put("id", id);
			result += this.newPrefDao.addCouponForId(paramMap);
		}
		if (result>0) {
			ocsDao.del(OCSKey.DIY_NEWPREF_LOAD);
		}
		return result > 0;
	}

	
}
