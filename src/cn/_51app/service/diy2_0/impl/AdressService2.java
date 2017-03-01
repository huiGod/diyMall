package cn._51app.service.diy2_0.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.databind.ObjectMapper;

import cn._51app.dao.diy2_0.IAdressDao;
import cn._51app.dao.ocs.OCSDao;
import cn._51app.service.INewAddressService;
import cn._51app.service.diy2_0.IAdressService2;
import cn._51app.service.impl.NewAddressService;
import cn._51app.util.OCSKey;

@Service
public class AdressService2 implements IAdressService2 {

	@Autowired
	private IAdressDao iAdressDao;
	
	@Autowired
	private OCSDao oCSDao;
	private Logger logger = Logger.getLogger(NewAddressService.class);
	private ObjectMapper mapper=new ObjectMapper();

	@Override
	public String getAddress(String deviceNo, String app) throws Exception{
		String json=this.oCSDao.query(OCSKey.DIY_USER_ADDRESS2+deviceNo+":"+app);
		if(StringUtils.isBlank(json)){
			List<Map<String, Object>> map=this.iAdressDao.queryAddress(deviceNo,app);
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
	public String getAddressForId(String userId) throws Exception{
		String json=this.oCSDao.query(OCSKey.DIY_USER_ADDRESS2+"_"+userId);
		if(StringUtils.isBlank(json)){
			List<Map<String, Object>> map=this.iAdressDao.queryAddressForId(userId);
			if(map!=null){
				json=mapper.writeValueAsString(map);
				if (!oCSDao.insert(OCSKey.DIY_USER_ADDRESS2 +"_"+userId, json, 0) && logger.isInfoEnabled()) {
					logger.info("NewAddressService getAdress 我的地址初始化失败");
				}
			}
		}
		return json;
	}
	
	@Override
	public String updateAddress(String deviceNo, String app, String addressId, String name, String mobile,
			String province, String area, String isDefault) throws Exception {
			int id=0;
			try {
				id=this.iAdressDao.updateAdress(deviceNo,app,addressId,name,mobile,province,area,isDefault);
				if(id>0){
					//删除缓存
					oCSDao.del(OCSKey.DIY_USER_ADDRESS2+deviceNo+":"+app);
					//缓存初始化设置
					getAddress(deviceNo,app);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return "{\"addressId\":"+id+"}";
	}
	
	@Override
	public String updateAddressForId(String userId, String addressId, String name, String mobile,
			String province, String area, String isDefault) throws Exception {
			int id=0;
			try {
				id=this.iAdressDao.updateAdressForId(userId,addressId,name,mobile,province,area,isDefault);
				if(id>0){
					//删除缓存
					oCSDao.del(OCSKey.DIY_USER_ADDRESS2+"_"+userId);
					//缓存初始化设置
					getAddressForId(userId);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return "{\"addressId\":"+id+"}";
	}
	
	@Override
	public boolean deleteAddress(String deviceNo, String app, String addressId) throws Exception{
			boolean flag=false;
			try {
				flag=this.iAdressDao.deleteAdress(deviceNo,app,addressId);
				if(flag){
					//删除缓存
					oCSDao.del(OCSKey.DIY_USER_ADDRESS2+deviceNo+":"+app);
					//缓存初始化设置
					getAddress(deviceNo,app);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return flag;
	}
	
	@Override
	public boolean deleteAddressForId(String userId, String addressId) throws Exception{
			boolean flag=false;
			try {
				flag=this.iAdressDao.deleteAdressForId(userId,addressId);
				if(flag){
					//删除缓存
					oCSDao.del(OCSKey.DIY_USER_ADDRESS2+"_"+userId);
					//缓存初始化设置
					getAddressForId(userId);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return flag;
	}
	
	@Override
	public boolean isDefault(String deviceNo, String app, String addressId) throws Exception{
			boolean flag=false;
			try {
				flag=this.iAdressDao.isDefault(deviceNo,app,addressId);
				if(flag){
					//删除缓存
					oCSDao.del(OCSKey.DIY_USER_ADDRESS2+deviceNo+":"+app);
					//缓存初始化设置
					getAddress(deviceNo,app);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return flag;
	}
	
	@Override
	public boolean isDefaultForId(String userId, String addressId) throws Exception{
			boolean flag=false;
			try {
				flag=this.iAdressDao.isDefaultForId(userId,addressId);
				if(flag){
					//删除缓存
					oCSDao.del(OCSKey.DIY_USER_ADDRESS2+"_"+userId);
					//缓存初始化设置
					getAddressForId(userId);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return flag;
	}

	/**
	 * tengh 2016年12月5日 下午5:29:10
	 * @param id
	 * @return
	 * TODO 判断地址是不是偏远地区
	 */
	public boolean isRemote(String id) {
		return this.iAdressDao.isRemote(id);
	}
	
}
