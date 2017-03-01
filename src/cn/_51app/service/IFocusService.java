package cn._51app.service;

import java.util.Map;

public interface IFocusService {

	void flushAll() throws Exception;
	
	String  getPrivilege() throws Exception;
	
	String queryMaterialByType() throws Exception;
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	String drawSwitch(String deviceNo,String app) throws Exception;
	
	String getDrawLevelAndValid() throws Exception;
	
	String isVersionI(String v) throws Exception;
	
	String checkVersion(String v) throws Exception;
	
	String getShopCartNum(String dn,String app) throws Exception;
	
	boolean insertDevice(String dn, String app) throws Exception;

	String materialList() throws Exception;
	
	
	
	
}
