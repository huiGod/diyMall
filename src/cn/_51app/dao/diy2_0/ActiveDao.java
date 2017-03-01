package cn._51app.dao.diy2_0;

public interface ActiveDao {

	/**
	 * tengh 2016年12月25日 下午5:43:04
	 * @param appstoreid
	 * @param eventime
	 * @param ip
	 * @param appkey
	 * @param devicetype
	 * @param oversion
	 * @param tdid
	 * @param idfa
	 * @param spreadurl
	 * @param adnetname
	 * @param spreadname
	 * @param ua
	 * @param clicktime
	 * TODO 激活记录
	 * @param clickip 
	 * @param channelpackageid 
	 */
	void recordActivceInfo(String appstoreid, String eventime, String activeip, String appkey, String devicetype,
			String osversion, String tdid, String idfa, String spreadurl, String adnetname, String spreadname, String ua,
			String clicktime, String clickip, String channelpackageid);

	/**
	 * tengh 2016年12月25日 下午6:00:38
	 * @param appstoreid
	 * @param eventime
	 * @param ip
	 * @param appkey
	 * @param devicetype
	 * @param oversion
	 * @param tdid
	 * @param idfa
	 * @param spreadurl
	 * @param adnetname
	 * @param spreadname
	 * @param ua
	 * @param clicktime
	 * @param clickip
	 * @param accountid
	 * TODO 登录回调
	 * @param channelpackageid 
	 */
	void login(String appstoreid, String eventime, String ip, String appkey, String devicetype, String osversion,
			String tdid, String idfa, String spreadurl, String adnetname, String spreadname, String ua,
			String clicktime, String clickip, String accountid, String channelpackageid);

	/**
	 * tengh 2017年1月20日 上午11:29:56
	 * @param eventime
	 * @param ip
	 * @param appkey
	 * @param devicetype
	 * @param osversion
	 * @param tdid
	 * @param spreadurl
	 * @param adnetname
	 * @param spreadname
	 * @param ua
	 * @param clicktime
	 * @param clickip
	 * @param channelpackageid
	 * @param mac
	 * @param imei
	 * @param androidid
	 * @param advertisingid
	 * TODO 安卓激活回调
	 */
	void recordAndroidActivceInfo(String eventime, String ip, String appkey, String devicetype, String osversion,
			String tdid, String spreadurl, String adnetname, String spreadname, String ua, String clicktime,
			String clickip, String channelpackageid, String mac, String imei, String androidid, String advertisingid);

	/**
	 * tengh 2017年1月20日 上午11:47:15
	 * @param eventime
	 * @param ip
	 * @param appkey
	 * @param devicetype
	 * @param osversion
	 * @param tdid
	 * @param spreadurl
	 * @param adnetname
	 * @param spreadname
	 * @param ua
	 * @param clicktime
	 * @param clickip
	 * @param accountid
	 * @param channelpackageid
	 * @param deeplink
	 * TODO 安卓登录
	 */
	void loginAndroid(String eventime, String ip, String appkey, String devicetype, String osversion, String tdid,
			String spreadurl, String adnetname, String spreadname, String ua, String clicktime, String clickip,
			String accountid, String channelpackageid, String deeplink);

	/**
	 * tengh 2017年2月6日 下午4:11:30
	 * @param idfa
	 * @param appstoreid
	 * @return
	 * TODO 查询idfa的次数
	 */
	long queryNumIdfa(String idfa, String appstoreid);
	
	
}
