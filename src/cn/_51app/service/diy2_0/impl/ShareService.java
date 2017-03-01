package cn._51app.service.diy2_0.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn._51app.dao.diy2_0.IShareDao;
import cn._51app.service.BaseService;
import cn._51app.service.diy2_0.IShareService;

@Service
public class ShareService extends BaseService implements IShareService {

	@Autowired
	private IShareDao iShareDao;
	private String defaultApp="com.shua.CustomMall";
	
	public int getMemberId(String openid) {
		return this.iShareDao.getMemberId(openid,defaultApp);
	}
	
}
