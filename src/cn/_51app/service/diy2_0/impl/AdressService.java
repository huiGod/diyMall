package cn._51app.service.diy2_0.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import cn._51app.dao.diy2_0.impl.AdressDao;
import cn._51app.dao.ocs.OCSDao;
import cn._51app.service.diy2_0.IAdressService;
import cn._51app.util.OCSKey;

@Service
public class AdressService implements IAdressService {
	
	@Autowired
	private OCSDao oCSDao;
	
	@Autowired
	private AdressDao adressDao;
	
	private ObjectMapper mapper=new ObjectMapper();
	
	private Logger logger=Logger.getLogger(AdressService.class);

	@Override
	public String getAdress(String deviceNo, String app) throws Exception{
		String json=this.oCSDao.query(OCSKey.DIY_USER_ADDRESS2+deviceNo+":"+app);
		if(StringUtils.isBlank(json)){
			List<Map<String, Object>> map=this.adressDao.queryAddress(deviceNo,app);
			if(map!=null){
				json=mapper.writeValueAsString(map);
				if (!oCSDao.insert(OCSKey.DIY_USER_ADDRESS2 + deviceNo+":"+app, json, 0) && logger.isInfoEnabled()) {
					logger.info("NewAddressService getAdress 我的地址初始化失败");
				}
			}
		}
		return json;
	}
	
	@Override
	public String getAdressForId(String userId) throws Exception{
		String json=this.oCSDao.query(OCSKey.DIY_USER_ADDRESS2+"_"+userId);
		if(StringUtils.isBlank(json)){
			List<Map<String, Object>> map=this.adressDao.queryAddressForId(userId);
			if(map!=null){
				json=mapper.writeValueAsString(map);
				if (!oCSDao.insert(OCSKey.DIY_USER_ADDRESS2 +"_"+userId, json, 0) && logger.isInfoEnabled()) {
					logger.info("NewAddressService getAdress 我的地址初始化失败");
				}
			}
		}
		return json;
	}
	
}
