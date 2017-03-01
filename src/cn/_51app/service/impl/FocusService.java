package cn._51app.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import cn._51app.service.BaseService;
import cn._51app.service.IFocusService;
import cn._51app.util.OCSKey;
import cn._51app.util.PropertiesUtil;
import cn._51app.util.WxPayUtil;
import cn._51app.dao.IFocusDao;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class FocusService extends BaseService implements IFocusService {
	
	@Autowired
	private IFocusDao fDao;
	
	private final String dmRootUrl =PropertiesUtil.getValue("diy.material.url");
	private final String orderPageSize =PropertiesUtil.getValue("diy.order.page.size");
	
	private final String ONLINE_API =PropertiesUtil.getValue("VERSION_ONLINE_API");
	private final String ONLINE_DATA =PropertiesUtil.getValue("VERSION_ONLINE_DATA");
	private final String VERIFY_API =PropertiesUtil.getValue("VERSION_VERIFY_API");
	private final String VERIFY_DATA =PropertiesUtil.getValue("VERSION_VERIFY_DATA");
	
	@Override
	public String getPrivilege() throws Exception {
		Map<String,Object> paramMap =new HashMap<String,Object>();
		String cacheKey =OCSKey.DIY_PRIVILEGE;
		paramMap.put("cacheKey", cacheKey);
		paramMap.put("cacheTime", super.FOREVER);
		return this.fDao.getPrivilege(paramMap);
	}

	

	@Override
	public String queryMaterialByType() throws Exception {
		String cacheKey =OCSKey.DIY_MATERIAL;
		Map<String,Object> paramMap =new HashMap<String,Object>();
		String http ="http://www.51app.cn/diyMaterial/";
		paramMap.put("cacheKey", cacheKey);
		paramMap.put("cacheTime", super.FOREVER);
		paramMap.put("dmRootUrl", dmRootUrl);
		
		paramMap.put("glassName", "定制变色马克杯");//定制精美手机壳
		paramMap.put("glassPrice", "￥28.00元起");//￥39.00元起
		paramMap.put("glassColor", "#5DB8C1,#50D5C2");
		paramMap.put("glassTmp", "glass");
		paramMap.put("glassItemSize", "{58,58}");
		paramMap.put("glassIco", http+"mkbTmp.jpg");
		
		paramMap.put("pillowName", "定制DIY抱枕");//定制个性马克杯
		paramMap.put("pillowPrice", "￥28.00元起");//￥28.00元起
		paramMap.put("pillowColor", "#5DB8C1,#50D5C2");
		paramMap.put("pillowTmp", "pillow");
		paramMap.put("pillowItemSize", "{58, 58}");
		paramMap.put("pillowIco", http+"bzTmp.jpg");
		
		paramMap.put("mpsName", "定制精美手机壳");//定制抱枕
		paramMap.put("mpsPrice", "￥39.00元起");//￥28.00元起
		paramMap.put("mpsColor", "#718EC1,#63AAC2");
		paramMap.put("mpsTmp", "mps");
		paramMap.put("mpsItemSize", "{58,58}");
		paramMap.put("mpsIco", http+"sjkTmp.jpg");
		
		paramMap.put("TName", "定制纯棉T恤");//定制T恤
		paramMap.put("TPrice", "￥50.00元起");//￥28.00元起 
		paramMap.put("TColor", "#7D6FC1,#7284BE");
		paramMap.put("TTmp", "tShirt");
		paramMap.put("TItemSize", "{58,58}");
		paramMap.put("TIco", http+"tsTmp.jpg");
		
		paramMap.put("typeArr", "1,2,3,4");
		return fDao.queryMaterialByType(paramMap);
	}

	@Override
	public void flushAll() throws Exception {
		super.flushAll();
	}	

	

	

	

	

	

	

	

	@Override
	public String drawSwitch(String deviceNo,String app) throws Exception {
		String cacheKey =OCSKey.DIY_DRAW_SWITCH+deviceNo+app;
		int cacheTime =super.FOREVER;
		Map<String,Object> paramMap =new HashMap<String,Object>();
		paramMap.put("deviceNo", deviceNo);
		paramMap.put("app", app);
		paramMap.put("cacheKey", cacheKey);
		paramMap.put("cacheTime", cacheTime);
		this.fDao.drawSwitch(paramMap);
		return super.q(cacheKey);
	}

	@Override
	public String getDrawLevelAndValid() throws Exception {
		String cacheKey =OCSKey.DIY_DRAW_L_V;
		int cacheTime =super.FOREVER;
		this.fDao.getDrawLevelAndValid(cacheKey,cacheTime);
		return super.q(cacheKey);
	}

	@Override
	public String isVersionI(String v) throws Exception {
		String cacheKey =OCSKey.DIY_VERSION_I;
		int cacheTime =super.FOREVER;
		fDao.versionListI(cacheKey,cacheTime);
		String json =super.q(cacheKey);
		
		boolean flag=false;
		if(json==null||json.equals("")) flag=false;
		flag=json.indexOf(v)!=-1;
		Map<String, Object> m = new HashMap<String, Object>();
		if(flag){
			//表示正常
			m.put("apple", false);
	        m.put("apiUrl", ONLINE_API);//api调用地址
	        m.put("dataUrl", ONLINE_DATA);//静态资源地址
			return super.toJson(m);
		}else{
			//表示审核
			m.put("apple", true);
	        m.put("apiUrl", VERIFY_API);
	        m.put("dataUrl", VERIFY_DATA);
	        return super.toJson(m);
		}
	}

	@Override
	public String checkVersion(String v) throws Exception {
		String cacheKey =OCSKey.DIY_LATEST_VERSION_I;
		int cacheTime =super.FOREVER;
		fDao.getLatestVersion(cacheKey,cacheTime);
		String json =super.q(cacheKey);
		if(json==null){
			return null;
		}
		int userVersion = fDao.getLevelByVersion(v);
		Map<String,Object> versionM=new ObjectMapper().readValue(json, new TypeReference<HashMap<String,Object>>(){});
		String[] c = v.split("\\.");
		String[] s = versionM.get("version").toString().split("\\.");
		int length = s.length;
		for (int i = 0; i < length; i++) {
			if (s[i].compareTo(c[i]) > 0) {
				versionM.put("level", userVersion);
				return super.toJson(versionM);
			} else {
				continue;
			}
		}
		return json;
	}

	@Override
	public String getShopCartNum(String dn, String app) throws Exception {
		int scNum =fDao.getShopCartNum(dn, app);
		return super.toJson(scNum);
	}

	/**
	 * 往diy_device_user插入一条用户数据
	 */
	@Override
	public boolean insertDevice(String dn, String app) throws Exception {
		int result =this.fDao.insertDevice(dn,app);
		if(result==0){
			return false;
		}
		return true;
	}


	@Override
	public String materialList() throws Exception {
		String cacheKey =OCSKey.DIY_NEWMATERIAL;
		Map<String,Object> paramMap =new HashMap<String,Object>();
		paramMap.put("cacheKey", cacheKey);
		paramMap.put("cacheTime", super.FOREVER);
		paramMap.put("dmRootUrl", dmRootUrl);
		
		return fDao.materialList(paramMap);
	}
	

}
