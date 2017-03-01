
package cn._51app.service.diy2_0.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn._51app.dao.diy2_0.ISearchGoodsDao;
import cn._51app.dao.ocs.OCSDao;
import cn._51app.service.diy2_0.ISearchGoodsService;
import cn._51app.util.JSONUtil;
import cn._51app.util.OCSKey;
import cn._51app.util.PropertiesUtil;

@Service
public class SearchGoodsService implements ISearchGoodsService {

	@Autowired
	private ISearchGoodsDao iSearchGoodsDao;

	@Autowired
	private OCSDao ocsDao;
	
	private String preImgUrl=PropertiesUtil.getValue("diy.goods.url");

	@Override
	public String getKeywords(String name) {
		String cacheKey = OCSKey.DIY_SEARCH_KEYWORD+name; 
		String keyword = this.ocsDao.query(cacheKey);
		if (StringUtils.isBlank(keyword)) {
			List<Map<String, Object>> result = iSearchGoodsDao.getKeywords(name);
			keyword = JSONUtil.convertArrayToJSON(result);
			ocsDao.insert(cacheKey, keyword, 0);
		}
		return keyword;
	}

	@Override
	public String getGoodsInfo(int page,String name,Integer goods_id,Integer keyword_id) {	
		String cacheKey = OCSKey.DIY_SERACH_GOODSINFO+":"+name+":"+goods_id+":"+keyword_id;
		String goodsInfos = this.ocsDao.query(cacheKey);
		if (StringUtils.isBlank(goodsInfos)) {
			List<Map<String, Object>> result = iSearchGoodsDao.getGoodsInfo(page,name,goods_id,keyword_id);
			for (Map<String, Object> map : result) {
				String iconUrl = (String) map.get("iconUrl");
				if (StringUtils.isNotBlank(iconUrl)) {
					map.put("iconUrl", preImgUrl+iconUrl);
				}
			}
			
			goodsInfos = JSONUtil.convertArrayToJSON(result);
			ocsDao.insert(cacheKey, goodsInfos, 10);
		}
		return goodsInfos;
	}

	@Override
	public String getLogs(String userId) {
	
		List<Map<String, Object>> list = iSearchGoodsDao.getSearchLog(userId);
		if (list != null && list.size()>0) {
			return JSONUtil.convertArrayToJSON(list);
		}
		return null;
	}

	@Override
	public void addSearchLog(String userId, String content) {
		if (StringUtils.isNoneBlank(userId)) {
			iSearchGoodsDao.addSearchLog(userId, content);
		}
	}

	@Override
	public boolean clearLogs(String userId) {
		if (userId != null) {
			return iSearchGoodsDao.clearLogs(userId) > 0;
		}
		return false;
	}

	@Override
	public String getRecommand() {
		String cacheKey = OCSKey.DIY_SEARCH_RECOMMAND;
		String recommandInfos = this.ocsDao.query(cacheKey);
		if (StringUtils.isBlank(recommandInfos)) {
			List<Map<String, Object>> result = iSearchGoodsDao.getRecommand();
			recommandInfos = JSONUtil.convertArrayToJSON(result);
			ocsDao.insert(cacheKey, recommandInfos, 10);
		}
		return recommandInfos;
	}

	@Override
	public String getGoodsInfo(int page, String name) {
		
		String[] names = name.split(" ");		
		String cacheKey = OCSKey.DIY_SERACH_GOODSINFO+":"+name+":"+page;
		String goodsInfos = this.ocsDao.query(cacheKey);
		if (StringUtils.isBlank(goodsInfos)) {
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("index", page*10);
			paramMap.put("rows", 10);
			paramMap.put("searchName", names[0]);
			if (names.length>=2) {
				paramMap.put("goodsName", names[1]);
			}
			if (names.length >=3) {
				paramMap.put("keywordName", names[2]);
			}

			List<Map<String, Object>> result = iSearchGoodsDao.getGoodsInfo(paramMap);
			for (Map<String, Object> map : result) {
				String iconUrl = (String) map.get("iconUrl");
				if (StringUtils.isNotBlank(iconUrl)) {
					map.put("iconUrl", preImgUrl+iconUrl);
				}
			}
			
			goodsInfos = JSONUtil.convertArrayToJSON(result);
			ocsDao.insert(cacheKey, goodsInfos, 10);
		}
		return goodsInfos;
	}
}
