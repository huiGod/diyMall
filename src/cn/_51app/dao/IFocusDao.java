package cn._51app.dao;

import java.util.List;
import java.util.Map;

public interface IFocusDao {

	String getPrivilege(Map<String,Object> paramMap) throws Exception;

	String queryMaterialByType(Map<String, Object> paramMap)throws Exception;
	
	List<Map<String,Object>> getShopCartC(String orderNo) throws Exception;
	
	int saveOrUpdateSCT(Map<String,Object> paramMap) throws Exception;
	
	int saveOrUpdateSCC(Map<String,Object> paramMap) throws Exception;
	
	String getCoupon(Map<String,Object> paramMap) throws Exception;
	
	Map<String,Object> getPrivilegeId(double feeTotal) throws Exception;
	
	String drawSwitch(Map<String,Object> paramMap) throws Exception;
	
	String getDrawLevelAndValid(String cacheKey,int cacheTime) throws Exception;
	
	int getCouponIdByLevel(String levelStr) throws Exception;
	
	String versionListI(String cacheKey,int cacheTime) throws Exception;
	
	int getLevelByVersion(String v) throws Exception;
	
	String getLatestVersion(String cacheKey, int cacheTime) throws Exception;
	
	int getShopCartNum(String dn,String app) throws Exception;
	
	int insertDevice(String dn,String app) throws Exception;

	String materialList(Map<String, Object> paramMap) throws Exception;
}
