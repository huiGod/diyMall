package cn._51app.service.diy2_0.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn._51app.dao.diy2_0.IEveryPrefDao;
import cn._51app.dao.ocs.OCSDao;
import cn._51app.service.BaseService;
import cn._51app.service.diy2_0.IEveryPrefService;
import cn._51app.util.JSONUtil;
import cn._51app.util.OCSKey;
import cn._51app.util.PropertiesUtil;

@Service
public class EveryPrefService extends BaseService implements IEveryPrefService {
	
	@Autowired
	private IEveryPrefDao everyPrefDao;
	
	@Autowired
	private OCSDao ocsDao;
	
	private String preImgUrl=PropertiesUtil.getValue("diy.goods.url");

	@Override
	public String load() {
		String cacheKey = OCSKey.DIY_EVERY_PREF;
		String cacheValue = this.ocsDao.query(cacheKey);
		if (StringUtils.isBlank(cacheValue)) {
			Map<String, Object> result = everyPrefDao.load();
			if (result != null) {
				String imgTop = (String) result.get("imgTop");
				if (StringUtils.isNotBlank(imgTop)) {
					result.put("imgTop", preImgUrl+imgTop);
				}
				
				String imgTail = (String) result.get("imgTail");
				if (StringUtils.isNotBlank(imgTail)) {
					result.put("imgTail", preImgUrl+imgTail);
				}
			}
			cacheValue = JSONUtil.convertObjectToJSON(result);
			ocsDao.insert(cacheKey, cacheValue, 60*30);//保存三十分钟
		}
		return cacheValue;
	}

	@Override
	public String getActivities() {
		String cacheKey = OCSKey.DIY_ACTIVITIES;
		String cacheValue = this.ocsDao.query(cacheKey);
		if (StringUtils.isBlank(cacheValue)) {
			List<Map<String, Object>> result = everyPrefDao.getActivities();
			if (result != null) {
				for (Map<String, Object> map : result) {
					String imgUrl = (String) map.get("imgUrl");
					if (StringUtils.isNotBlank(imgUrl)) {
						map.put("imgUrl", preImgUrl+imgUrl);
					}
				}
			}
			cacheValue = JSONUtil.convertArrayToJSON(result);
			ocsDao.insert(cacheKey, cacheValue, 60*30);//保存三十分钟
			System.out.println("从数据库获取");
		}else {
			System.out.println("从缓存获取");
		}
		return cacheValue;
	}

	
	
	
}
