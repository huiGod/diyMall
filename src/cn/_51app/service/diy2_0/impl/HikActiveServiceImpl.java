package cn._51app.service.diy2_0.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn._51app.dao.diy2_0.HikActiveDao;
import cn._51app.service.BaseService;
import cn._51app.service.diy2_0.HikActiveService;
import cn._51app.util.OCSKey;

@Service
public class HikActiveServiceImpl extends BaseService implements HikActiveService{

	@Autowired
	private HikActiveDao hikActiveDao;
	
	@Override
	public boolean getUserInfo(String id) throws Exception {
		int result = hikActiveDao.getUserInfo(id);
		if(result!=0){
			return true;
		}
		return false;
		
	}

	@Override
	public String getInfo(Map<String, Object> paramMap) throws Exception {
		
		return hikActiveDao.getInfo(paramMap);
	}

	@Override
	public String appHik(Map<String, Object> paramMap) throws Exception {
		
		return hikActiveDao.appHik(paramMap);
	}

	@Override
	public String wxHik(Map<String, Object> paramMap) throws Exception {
		
		return hikActiveDao.wxHik(paramMap);
	}

	@Override
	public String payGoods(Map<String, Object> paramMap) throws Exception {
		return hikActiveDao.payGoods(paramMap);
	}

	@Override
	public String wxLogin(Map<String, Object> paramMap) throws Exception {
		
		return hikActiveDao.wxLogin(paramMap);
	}



}
