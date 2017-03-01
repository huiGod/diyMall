package cn._51app.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class QQLoginUtil {
	
	private static String appId = "101325751";
	
	private static String appkey = "02a069d4385cc04a84e7a25b62aa9e30";
	
	public static String qq_user_app = "com.shua.h5";
	
	
	private static String code_api = "https://graph.qq.com/oauth2.0/authorize";
	
	private static String access_token_api = "https://graph.qq.com/oauth2.0/token";
	
	private static String openid_api = "https://graph.qq.com/oauth2.0/me";
	
	private static String userinfo_api = "https://graph.qq.com/user/get_user_info";
	
	
	
	final private static String redirect_uri = "http://test.diy.51app.cn/diyMall2/UMallUser/QQLogin.do";
	
	public static String codeUrl(String state){
		//String redirectUri = URLEncoder.encode(redirect_uri, "utf-8");
		String res = code_api+"?client_id="+appId+"&redirect_uri="+redirect_uri+"&response_type=code&scope=snsapi_userinfo&state="+state+"&display=mobile";
		return res;
	}
	
	public static String shareUrl(String redirectUrl){
		String res = code_api+"?client_id="+appId+"&redirect_uri="+redirectUrl+"&response_type=code&scope=snsapi_userinfo&state=shareQQ&display=mobile";
		return res;
	}
	
	/**
	 * 通过code换取网页授权access_token
	 * @param code
	 * @return
	 * @throws Exception
	 */
	public static String getAccessToken(String code) throws Exception{
		String url = access_token_api+"?client_id="+appId+"&client_secret="+appkey+"&code="+code+"&grant_type=authorization_code&redirect_uri="+redirect_uri;
		String str = HttpClientUtil.get(url, null);
		String[] strArr = str.split("&");
		String[] accessArr = strArr[0].split("=");
		if("access_token".equals(accessArr[0])){
			return accessArr[1];
		}
		return null;
	}
	
	/**
	 * 获取用户OpenID
	 * @param code
	 * @return
	 * @throws Exception
	 */
	public static String getOpenId(String access_token) throws Exception{
		String url = openid_api+"?access_token="+access_token;
		String json = HttpClientUtil.get(url, null);
		json = json.substring(json.indexOf("{"),json.indexOf("}")+1);
		Map<String,Object> m=new ObjectMapper().readValue(json, new TypeReference<HashMap<String,Object>>(){});
		return null==m.get("openid")?null:m.get("openid").toString();
	}
	
	/**
	 * 获取用户基本信息
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Object> getUserInfo(String access_token,String openid) throws Exception{
		String url = userinfo_api+"?access_token="+access_token+"&oauth_consumer_key="+appId+"&openid="+openid;
		String json = HttpClientUtil.get(url, null);
		Map<String,Object> m=new ObjectMapper().readValue(json, new TypeReference<HashMap<String,Object>>(){});
		return m;
	}
	
	public static void main(String[] args) throws Exception{
		String state = "kennen";
		System.out.println(codeUrl(state));
		//String redirectUri = URLEncoder.encode(redirect_uri, "utf-8");
		//System.out.println(redirectUri);
		//String url = access_token_api+"?client_id="+appId+"&client_secret="+appkey+"&code="+"E7F87C6000DB96CB2726C5BAB2220D3C"+"&grant_type=authorization_code&redirect_uri="+redirectUri;
		//ystem.out.println(url);
//		String url = access_token_api+"?client_id="+appId+"&client_secret="+appkey+"&code="+"2C366AB26D2E681FDDC26FDF3334110F"+"&grant_type=authorization_code&redirect_uri="+redirect_uri;
//		String json = HttpClientUtil.get(url, null);
//		System.out.println(json);
//		String str = "access_token=3FCB12095ED6DF87F98506F6C90F131B&expires_in=7776000&refresh_token=D41910DE3AF8D6BED37909443D86D168";
//		String[] strArr = str.split("&");
//		String[] accessArr = strArr[0].split("=");
//		if("access_token".equals(accessArr[0])){
//			System.out.println(accessArr[1]);
//		}
//		String url = openid_api+"?access_token="+"3FCB12095ED6DF87F98506F6C90F131B";
//		System.out.println(url);
//		System.out.println(getOpenId("3FCB12095ED6DF87F98506F6C90F131B"));
		
	}
	
}
