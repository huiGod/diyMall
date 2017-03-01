package cn._51app.service.diy2_0.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn._51app.dao.diy2_0.ActiveDao;
import cn._51app.service.diy2_0.ActiveService;
import cn._51app.util.HttpUtils;

@Service
public class ActiveServiceImpl implements ActiveService {
	
	@Autowired
	private ActiveDao activeDao;
	
	@Override
	public boolean recordActiveInfo(String appstoreid, String eventime, String activeip, String appkey, String devicetype,
			String osversion, String tdid, String idfa, String spreadurl, String adnetname, String spreadname, String ua,
			String clicktime,String clickip,String channelpackageid) {
		try {
			this.activeDao.recordActivceInfo(appstoreid,eventime,activeip,appkey,devicetype,osversion,tdid,idfa,spreadurl,adnetname,spreadname,ua,clicktime,clickip,channelpackageid);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	@Override
	public boolean recordAndroidActiveInfo(String eventime, String ip, String appkey, String devicetype,
			String osversion, String tdid, String spreadurl, String adnetname, String spreadname, String ua,
			String clicktime, String clickip, String channelpackageid, String mac, String imei, String androidid,
			String advertisingid) {
		try {
			this.activeDao.recordAndroidActivceInfo(eventime,ip,appkey,devicetype,osversion,tdid,spreadurl,adnetname,spreadname,ua,clicktime,clickip,channelpackageid,mac,imei,androidid,advertisingid);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	@Override
	public boolean login(String appstoreid, String eventime, String ip, String appkey, String devicetype,
			String osversion, String tdid, String idfa, String spreadurl, String adnetname, String spreadname, String ua,
			String clicktime, String clickip, String accountid,String channelpackageid) {
		try {
			this.activeDao.login(appstoreid,eventime,ip,appkey,devicetype,osversion,tdid,idfa,spreadurl,adnetname,spreadname,ua,clicktime,clickip,accountid,channelpackageid);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	@Override
	public boolean loginAndroid(String eventime, String ip, String appkey, String devicetype, String osversion, String tdid,
			String spreadurl, String adnetname, String spreadname, String ua, String clicktime, String clickip,
			String accountid, String channelpackageid, String deeplink) {
		try {
			this.activeDao.loginAndroid(eventime,ip,appkey,devicetype,osversion,tdid,spreadurl,adnetname,spreadname,ua,clicktime,clickip,accountid,channelpackageid,deeplink);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	@Override
	public void callbackIdfa(String idfa, String appstoreid) {
		//是否是首次
		long num=this.activeDao.queryNumIdfa(idfa,appstoreid);
		if(1==num){
			try {
				HttpUtils.get("http://ios.api.51app.cn/ios_appActive.action?appid=1105250240&idfa="+idfa, null, 5, "utf-8");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
