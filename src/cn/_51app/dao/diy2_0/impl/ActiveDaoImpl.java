package cn._51app.dao.diy2_0.impl;

import org.springframework.stereotype.Repository;

import cn._51app.dao.BaseDao;
import cn._51app.dao.diy2_0.ActiveDao;

@Repository
public class ActiveDaoImpl extends BaseDao implements ActiveDao{

	@Override
	public void recordActivceInfo(String appstoreid, String eventime, String activeip, String appkey, String devicetype,
			String osversion, String tdid, String idfa, String spreadurl, String adnetname, String spreadname, String ua,
			String clicktime,String clickip,String channelpackageid) {
		this.jt.update("INSERT INTO `td_active` (`appstoreid`,`eventime`,`activeip`,`appkey`,`devicetype`,`osversion`,`tdid`,`idfa`,`clicktime`,`clickip`,`spreadurl`,`adnetname`,`spreadname`,`ua`,`channelpackageid`) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ",
				new Object[]{appstoreid,eventime,activeip,appkey,devicetype,osversion,tdid,idfa,clicktime,clickip,spreadurl,adnetname,spreadname,ua,channelpackageid});
	}
	
	@Override
	public void recordAndroidActivceInfo(String eventime, String ip, String appkey, String devicetype,
			String osversion, String tdid, String spreadurl, String adnetname, String spreadname, String ua,
			String clicktime, String clickip, String channelpackageid, String mac, String imei, String androidid,
			String advertisingid) {
		this.jt.update("INSERT INTO `td_android_active` (`eventime`,`ip`,`appkey`,`devicetype`,`osversion`,`tdid`,`spreadurl`,`adnetname`,`spreadname`,`ua`,`clicktime`,`clickip`,`channelpackageid`,`mac`,`imei`,`androidid`,`advertisingid`) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ",
				new Object[]{eventime,ip,appkey,devicetype,osversion,tdid,spreadurl,adnetname,spreadname,ua,clicktime,clickip,channelpackageid,mac,imei,androidid,advertisingid});
	}
	
	@Override
	public void login(String appstoreid, String eventime, String ip, String appkey, String devicetype, String osversion,
			String tdid, String idfa, String spreadurl, String adnetname, String spreadname, String ua,
			String clicktime, String clickip, String accountid,String channelpackageid) {
		this.jt.update("INSERT INTO `td_login` (`appstoreid`,`eventime`,`ip`,`appkey`,`devicetype`,`osversion`,`tdid`,`idfa`,`clicktime`,`clickip`,`spreadurl`,`adnetname`,`spreadname`,`ua`,`accountid`,`channelpackageid`) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ON DUPLICATE KEY UPDATE `num`=`num`+1",
				new Object[]{appstoreid,eventime,ip,appkey,devicetype,osversion,tdid,idfa,clicktime,clickip,spreadurl,adnetname,spreadname,ua,accountid,channelpackageid});
	}
	
	@Override
	public void loginAndroid(String eventime, String ip, String appkey, String devicetype, String osversion, String tdid,
			String spreadurl, String adnetname, String spreadname, String ua, String clicktime, String clickip,
			String accountid, String channelpackageid, String deeplink) {
		this.jt.update("INSERT INTO `td_android_login` (`eventime`,`ip`,`appkey`,`devicetype`,`osversion`,`tdid`,`clicktime`,`clickip`,`spreadurl`,`adnetname`,`spreadname`,`ua`,`accountid`,`channelpackageid`,`deeplink`) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ON DUPLICATE KEY UPDATE `num`=`num`+1",
				new Object[]{eventime,ip,appkey,devicetype,osversion,tdid,clicktime,clickip,spreadurl,adnetname,spreadname,ua,accountid,channelpackageid,deeplink});
	}
	
	@Override
	public long queryNumIdfa(String idfa, String appstoreid) {
		return this.jt.queryForObject("SELECT COUNT(*) FROM `td_active` WHERE `idfa`=? AND `appstoreid`=?",new Object[]{idfa,appstoreid}, Long.class);
	}
}
