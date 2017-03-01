package cn._51app.service.diy2_0.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn._51app.dao.diy2_0.IHomeNavDao;
import cn._51app.service.BaseService;
import cn._51app.service.diy2_0.IHomeNavService;
import cn._51app.util.OCSKey;
import cn._51app.util.PropertiesUtil;

@Service
public class HomeNavService extends BaseService implements IHomeNavService {
	
	@Autowired
	private IHomeNavDao iHomeNavDao;
	
	//图片前缀
	private String dgurl=PropertiesUtil.getValue("diy.goods.url");

	@Override
	public String getHomeNav() throws Exception {
		String cacheKey=OCSKey.DIY_GOODS_HOME2;
		Map<String,Object>paramMap=new HashMap<String,Object>();
		paramMap.put("cacheKey",cacheKey);
		paramMap.put("cacheTime",FOREVER);
		paramMap.put("dgurl",dgurl);
		return iHomeNavDao.HomeNav(paramMap);
	}
	
	@Override
	public String toSpecial(String navId) throws Exception{
		String cacheKey=OCSKey.DIY_GOODS_SPECIAL2+"_"+navId;
		Map<String,Object>paramMap=new HashMap<String,Object>();
		paramMap.put("cacheKey",cacheKey);
		paramMap.put("cacheTime",FOREVER);
		paramMap.put("navId",navId);
		paramMap.put("dgurl",dgurl);
		return iHomeNavDao.toSpecial(paramMap);
	}
	
	@Override
	public String special(int nav_id) throws Exception {
		String key=OCSKey.YOU_SPECIAL2;
		String cacheKey=key+nav_id;
		Map<String,Object>paramMap = new HashMap<String,Object>();
		paramMap.put("cacheKey",cacheKey);
		paramMap.put("cacheTime",super.FOREVER);
		paramMap.put("nav_id",nav_id);
		paramMap.put("dgurl", dgurl);
		return this.iHomeNavDao.special(paramMap);
	}

}
