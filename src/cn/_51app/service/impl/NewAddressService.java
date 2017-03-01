package cn._51app.service.impl;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import cn._51app.dao.INewAddressDao;
import cn._51app.dao.ocs.OCSDao;
import cn._51app.service.BaseService;
import cn._51app.service.INewAddressService;
import cn._51app.util.OCSKey;

@Service
public class NewAddressService extends BaseService implements INewAddressService{
	@Autowired
	private OCSDao oCSDao;
	@Autowired
	private INewAddressDao iNewAddressDao;
	
	private Logger logger = Logger.getLogger(NewAddressService.class);
	private ObjectMapper mapper=new ObjectMapper();

	@Override
	public String getAdress(String deviceNo, String app) throws Exception{
		String json=this.oCSDao.query(OCSKey.DIY_USER_ADDRESS_+deviceNo+":"+app);
		if(StringUtils.isBlank(json)){
			List<Map<String, Object>> map=this.iNewAddressDao.queryAddress(deviceNo,app);
			if(map!=null){
				json=mapper.writeValueAsString(map);
				if (!oCSDao.insert(OCSKey.DIY_USER_ADDRESS_ + deviceNo+":"+app, json, 0) && logger.isInfoEnabled()) {
					logger.info("NewAddressService getAdress 我的地址初始化失败");
				}
			}
		}
		return json;
	}
	
	@Override
	public String updateAdress(String deviceNo, String app, String addressId, String name, String mobile,
			String province, String area, String isDefault) throws Exception {
			int id=0;
			try {
				id=this.iNewAddressDao.updateAdress(deviceNo,app,addressId,name,mobile,province,area,isDefault);
				if(id>0){
					//删除缓存
					oCSDao.del(OCSKey.DIY_USER_ADDRESS_+deviceNo+":"+app);
					//缓存初始化设置
					getAdress(deviceNo,app);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return "{\"addressId\":"+id+"}";
	}
	
	@Override
	public boolean deleteAdress(String deviceNo, String app, String addressId) throws Exception{
			boolean flag=false;
			try {
				flag=this.iNewAddressDao.deleteAdress(deviceNo,app,addressId);
				if(flag){
					//删除缓存
					oCSDao.del(OCSKey.DIY_USER_ADDRESS_+deviceNo+":"+app);
					//缓存初始化设置
					getAdress(deviceNo,app);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return flag;
	}
	
}
