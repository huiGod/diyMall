package cn._51app.service.diy2_0;

public interface ActiveService {

	/**
	 * tengh 2016年12月25日 下午5:43:16
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
	 * @param channelpackageid 
	 * @return
	 * TODO 激活记录
	 */
	boolean recordActiveInfo(String appstoreid, String eventime, String activeip, String appkey, String devicetype,
			String oversion, String tdid, String idfa, String spreadurl, String adnetname, String spreadname, String ua,
			String clicktime, String clickip, String channelpackageid);

	/**
	 * tengh 2016年12月25日 下午5:59:34
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
	 * @param channelpackageid 
	 * @return
	 * TODO 登录回调
	 */
	boolean login(String appstoreid, String eventime, String ip, String appkey, String devicetype, String oversion,
			String tdid, String idfa, String spreadurl, String adnetname, String spreadname, String ua,
			String clicktime, String clickip, String accountid, String channelpackageid);

	/**
	 * tengh 2017年1月20日 上午11:29:01
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
	 * @return
	 * TODO 安卓激活回调
	 */
	boolean recordAndroidActiveInfo(String eventime, String ip, String appkey, String devicetype, String osversion,
			String tdid, String spreadurl, String adnetname, String spreadname, String ua, String clicktime,
			String clickip, String channelpackageid, String mac, String imei, String androidid, String advertisingid);

	/**
	 * tengh 2017年1月20日 上午11:46:17
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
	 * @return
	 * TODO 安卓登录
	 */
	boolean loginAndroid(String eventime, String ip, String appkey, String devicetype, String osversion, String tdid,
			String spreadurl, String adnetname, String spreadname, String ua, String clicktime, String clickip,
			String accountid, String channelpackageid, String deeplink);

	/**
	 * tengh 2017年2月6日 下午4:07:44
	 * @param idfa
	 * @param appstoreid
	 * TODO 首次回调
	 */
	void callbackIdfa(String idfa, String appstoreid);
	
}
