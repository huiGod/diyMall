package cn._51app.controller.diy2_0;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn._51app.controller.BaseController;
import cn._51app.service.diy2_0.ActiveService;

@Controller
@RequestMapping("/talkData")
public class ActiveController extends BaseController{
	
	private static Logger deviceLog=Logger.getLogger("deviceLog");
	private static Logger loginLog=Logger.getLogger("loginLog");
	private SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	@Autowired
	private ActiveService activeService;
	
	/**
	 * tengh 2016年12月25日 下午2:36:19
	 * @param request
	 * @return
	 * TODO 激活回调(渠道量自然量)
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping(value="/active")
	public ResponseEntity<String> active(HttpServletRequest request) throws UnsupportedEncodingException{
		String data =null;
		String msg=null;
		int code =SUCESS;
		deviceLog.info(request.getQueryString());
		String appstoreid=(String)request.getParameter("appstoreid");
		String eventime=(String)request.getParameter("eventime");
		if(StringUtils.isNotBlank(eventime)){
			eventime=df.format(new Date(new Long(eventime)));
		}
		String activeip=(String)request.getParameter("activeip");
		String appkey=(String)request.getParameter("appkey");
		String devicetype=(String)request.getParameter("devicetype");
		String osversion=(String)request.getParameter("osversion");
		String tdid=(String)request.getParameter("tdid");
		String idfa=(String)request.getParameter("idfa");
		String spreadurl=(String)request.getParameter("spreadurl");
		String adnetname=(String)request.getParameter("adnetname");
		String spreadname=(String)request.getParameter("spreadname");
		if(StringUtils.isNotBlank(spreadname)){
			spreadname = new String(spreadname.getBytes("iso-8859-1"), "utf-8");
		}
		String ua=(String)request.getParameter("ua");
		String clicktime=(String)request.getParameter("clicktime");
		if(StringUtils.isNotBlank(clicktime)){
			clicktime=df.format(new Date(new Long(clicktime)));
		}
		String clickip=(String)request.getParameter("clickip");
		String channelpackageid=(String)request.getParameter("channelpackageid");
		try {
			boolean check=this.activeService.recordActiveInfo(appstoreid,eventime,activeip,appkey,devicetype,osversion,tdid,idfa,spreadurl,adnetname,spreadname,ua,clicktime,clickip,channelpackageid);
			if(!check){
				code=FAIL;
				msg="暂无数据";
			}
			//首次的话给统计回调
			this.activeService.callbackIdfa(idfa,appstoreid);
		} catch (Exception e) {
			e.printStackTrace();
			code =SERVER_ERR;
		}
		return super.resultInfo(data, code, msg);
	}
	
	/**
	 * tengh 2017年1月20日 上午11:22:13
	 * @param request
	 * @return
	 * @throws UnsupportedEncodingException
	 * TODO 安卓激活回调
	 */
	@RequestMapping(value="/activeAndroid")
	public ResponseEntity<String> activeAndroid(HttpServletRequest request) throws UnsupportedEncodingException{
		String data =null;
		String msg=null;
		int code =SUCESS;
		deviceLog.info(request.getQueryString());
		String eventime=(String)request.getParameter("eventime");
		if(StringUtils.isNotBlank(eventime)){
			eventime=df.format(new Date(new Long(eventime)));
		}
		String appkey=(String)request.getParameter("appkey"); 
		String devicetype=(String)request.getParameter("devicetype");
		String osversion=(String)request.getParameter("osversion");
		String tdid=(String)request.getParameter("tdid");
		String spreadurl=(String)request.getParameter("spreadurl");
		String adnetname=(String)request.getParameter("adnetname");
		String spreadname=(String)request.getParameter("spreadname");
		if(StringUtils.isNotBlank(spreadname)){
			spreadname = new String(spreadname.getBytes("iso-8859-1"), "utf-8");
		}
		String ua=(String)request.getParameter("ua");
		String clicktime=(String)request.getParameter("clicktime");
		if(StringUtils.isNotBlank(clicktime)){
			clicktime=df.format(new Date(new Long(clicktime)));
		}
		String ip=(String)request.getParameter("ip");
		String channelpackageid=(String)request.getParameter("channelpackageid");
		String mac=(String)request.getParameter("mac");
		String advertisingid=(String)request.getParameter("advertisingid");
		String androidid=(String)request.getParameter("androidid");
		String imei=(String)request.getParameter("imei");
		String clickip=(String)request.getParameter("clickip");
		try {
			boolean check=this.activeService.recordAndroidActiveInfo(eventime,ip,appkey,devicetype,osversion,tdid,spreadurl,adnetname,spreadname,ua,clicktime,clickip,channelpackageid,mac,imei,androidid,advertisingid);
			if(!check){
				code=FAIL;
				msg="暂无数据";
			}
		} catch (Exception e) {
			e.printStackTrace();
			code =SERVER_ERR;
		}
		return super.resultInfo(data, code, msg);
	}
	
	/**
	 * tengh 2016年12月25日 下午2:39:53
	 * @param request
	 * @return
	 * TODO 登录回调
	 */
	@RequestMapping(value="/login")
	public ResponseEntity<String> login(HttpServletRequest request) throws Exception{
		String data =null;
		String msg=null;
		int code =SUCESS;
		loginLog.info(request.getQueryString());
		String appstoreid=(String)request.getParameter("appstoreid");
		String eventime=(String)request.getParameter("eventime");
		if(StringUtils.isNotBlank(eventime)){
			eventime=df.format(new Date(new Long(eventime)));
		}
		String ip=(String)request.getParameter("ip");
		String appkey=(String)request.getParameter("appkey");
		String devicetype=(String)request.getParameter("devicetype");
		String osversion=(String)request.getParameter("osversion");
		String tdid=(String)request.getParameter("tdid");
		String idfa=(String)request.getParameter("idfa");
		String spreadurl=(String)request.getParameter("spreadurl");
		String adnetname=(String)request.getParameter("adnetname");
		String spreadname=request.getParameter("spreadname");
		if(StringUtils.isNotBlank(spreadname)){
			spreadname = new String(spreadname.getBytes("iso-8859-1"), "utf-8");
			loginLog.info("spreadname.urldecode:"+spreadname);
		}
		String ua=(String)request.getParameter("ua");
		String clicktime=(String)request.getParameter("clicktime");
		if(StringUtils.isNotBlank(clicktime)){
			clicktime=df.format(new Date(new Long(clicktime)));
		}
		String clickip=(String)request.getParameter("clickip");
		String accountid=(String)request.getParameter("accountid");
		String channelpackageid=(String)request.getParameter("channelpackageid");
		try {
			boolean check=this.activeService.login(appstoreid,eventime,ip,appkey,devicetype,osversion,tdid,idfa,spreadurl,adnetname,spreadname,ua,clicktime,clickip,accountid,channelpackageid);
			if(!check){
				code=FAIL;
				msg="暂无数据";
			}
		} catch (Exception e) {
			e.printStackTrace();
			code =SERVER_ERR;
		}
		return super.resultInfo(data, code, msg);
	}
	
	/**
	 * tengh 2016年12月25日 下午2:39:53
	 * @param request
	 * @return
	 * TODO 登录回调
	 */
	@RequestMapping(value="/loginAndroid")
	public ResponseEntity<String> loginAndroid(HttpServletRequest request) throws Exception{
		String data =null;
		String msg=null;
		int code =SUCESS;
		loginLog.info(request.getQueryString());
		String eventime=(String)request.getParameter("eventime");
		if(StringUtils.isNotBlank(eventime)){
			eventime=df.format(new Date(new Long(eventime)));
		}
		String ip=(String)request.getParameter("ip");
		String appkey=(String)request.getParameter("appkey");
		String devicetype=(String)request.getParameter("devicetype");
		String osversion=(String)request.getParameter("osversion");
		String tdid=(String)request.getParameter("tdid");
		String spreadurl=(String)request.getParameter("spreadurl");
		String adnetname=(String)request.getParameter("adnetname");
		String spreadname=request.getParameter("spreadname");
		if(StringUtils.isNotBlank(spreadname)){
			spreadname = new String(spreadname.getBytes("iso-8859-1"), "utf-8");
			loginLog.info("spreadname.urldecode:"+spreadname);
		}
		String ua=(String)request.getParameter("ua");
		String clicktime=(String)request.getParameter("clicktime");
		if(StringUtils.isNotBlank(clicktime)){
			clicktime=df.format(new Date(new Long(clicktime)));
		}
		String clickip=(String)request.getParameter("clickip");
		String accountid=(String)request.getParameter("accountid");
		String channelpackageid=(String)request.getParameter("channelpackageid");
		String deeplink=(String)request.getParameter("deeplink");
		try {
			boolean check=this.activeService.loginAndroid(eventime,ip,appkey,devicetype,osversion,tdid,spreadurl,adnetname,spreadname,ua,clicktime,clickip,accountid,channelpackageid,deeplink);
			if(!check){
				code=FAIL;
				msg="暂无数据";
			}
		} catch (Exception e) {
			e.printStackTrace();
			code =SERVER_ERR;
		}
		return super.resultInfo(data, code, msg);
	}
}
