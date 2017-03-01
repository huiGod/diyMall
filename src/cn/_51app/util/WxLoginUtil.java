package cn._51app.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;


public class WxLoginUtil {
	
	public static String appId = "wx204459d3f9148e3b";
	
	private static String appSecret = "ca10547faa3f0c2e140fafc847add0d9";
	
	public static String wx_user_app = "com.shua.h5";
	
	/* 微信API */
	private static String code_api = "https://open.weixin.qq.com/connect/oauth2/authorize";
	
	private static String access_token_api = "https://api.weixin.qq.com/sns/oauth2/access_token";
	
	private static String refresh_token_api = "https://api.weixin.qq.com/sns/oauth2/refresh_token";
	
	private static String userinfo_api = "https://api.weixin.qq.com/sns/userinfo";
	
	private static String auth_access_token_api = "https://api.weixin.qq.com/sns/auth";
	
	public static String redirect_uri = "http://test.diy.51app.cn/diyMall2/UGoods/WxLogin.do";
	
	public static String redirect_uri_good = "http://test.diy.51app.cn/diyMall2/UGoods/WxLoginGoods.do";
	
	public static String lottery_url = "http://test.diy.51app.cn/diyMall2/UCoupon/WxLogin.do";
	
	public static String codeUrl(String redirect_uri,String state){
		try {
			redirect_uri = URLEncoder.encode(redirect_uri, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String res = code_api+"?appid="+appId+"&redirect_uri="+redirect_uri+"&response_type=code&scope=snsapi_userinfo&state="+state+"#wechat_redirect";
		return res;
	}
	
	public static String lotteryUrl(String state){
		String lotteryUrl = lottery_url;
		try {
			lotteryUrl = URLEncoder.encode(lottery_url, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String res = code_api+"?appid="+appId+"&redirect_uri="+lotteryUrl+"&response_type=code&scope=snsapi_userinfo&state="+state+"#wechat_redirect";
		return res;
	}
	
	public static String shareUrl(String url){
		String redirectUrl="";
		try {
			redirectUrl = URLEncoder.encode(url, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String res = code_api+"?appid="+appId+"&redirect_uri="+redirectUrl+"&response_type=code&scope=snsapi_userinfo&state=shareWX#wechat_redirect";
		return res;
	}
	
	/**
	 * 通过code换取网页授权access_token
	 * @param code
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Object> getAccessToken(String code) throws Exception{
		String url = access_token_api+"?appid="+appId+"&secret="+appSecret+"&code="+code+"&grant_type=authorization_code";
		String json = HttpClientUtil.get(url, null);
		Map<String,Object> m=new ObjectMapper().readValue(json, new TypeReference<HashMap<String,Object>>(){});
		return m;
	}
	
	/**
	 * 刷新access_token
	 * @param refresh_token
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Object> getRefreshToken(String refresh_token) throws Exception{
		String url = refresh_token_api+"?appid="+appId+"&grant_type=refresh_token&refresh_token="+refresh_token;
		String json = HttpClientUtil.get(url, null);
		Map<String,Object> m=new ObjectMapper().readValue(json, new TypeReference<HashMap<String,Object>>(){});
		return m;
	}
	
	/**
	 * 拉取用户信息
	 * @param access_token
	 * @param openid
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Object> getUserinfo(String access_token,String openid) throws Exception{
		String url = userinfo_api+"?access_token="+access_token+"&openid="+openid;
		String json = HttpClientUtil.get(url, null);
		Map<String,Object> m=new ObjectMapper().readValue(json, new TypeReference<HashMap<String,Object>>(){});
		return m;
	}
	
	/**
	 * 检验授权凭证（access_token）是否有效
	 * @param access_token
	 * @param openid
	 * @return
	 */
	public static boolean authAccessToken(String access_token,String openid){
		String url = auth_access_token_api+"?access_token="+access_token+"&openid="+openid;
		String json = HttpClientUtil.get(url, null);
		Map<String, Object> m;
		try {
			m = new ObjectMapper().readValue(json, new TypeReference<HashMap<String,Object>>(){});
			String errcode = m.get("errcode").toString();
			if("0".equals(errcode))
				return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	
	
	/* 分享api */
	public static String access_token_share = "https://api.weixin.qq.com/cgi-bin/token";
	public static String jsapi_ticket = "https://api.weixin.qq.com/cgi-bin/ticket/getticket";
	
	/**
	 * 获取微信access_token
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Object> shareAccessToken() throws Exception{
		String url = access_token_share+"?grant_type=client_credential&appid="+appId+"&secret="+appSecret;
		String json = HttpClientUtil.get(url, null);
		Map<String,Object> m=new ObjectMapper().readValue(json, new TypeReference<HashMap<String,Object>>(){});
		return m;
	}
	
	/**
	 * 获得jsapi_ticket
	 * @param access_token
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Object> shareTicket(String access_token) throws Exception{
		String url = jsapi_ticket+"?access_token="+access_token+"&type=jsapi";
		String json = HttpClientUtil.get(url, null);
		Map<String,Object> m=new ObjectMapper().readValue(json, new TypeReference<HashMap<String,Object>>(){});
		return m;
	}
	
	public static void main(String[] args) {
		try {
			System.out.println(codeUrl("http://test.diy.51app.cn/diyMall2/UGoods/WxLogin.do", "kennen"));
			String wxpay = "weixin://wap/pay?appid%3Dwx2421b1c4370ec43b%26noncestr%3DqZRDzbKq6zsgFof9%26package%3DWAP%26prepayid%3Dwx2016062811583977756452680845630255%26timestamp%3D1467086319%26sign%3DF13A88201DA330213E22E51F10339A27";
			System.out.println(URLDecoder.decode(wxpay));
			//weixin://wap/pay?appid=wx2421b1c4370ec43b&noncestr=qZRDzbKq6zsgFof9&package=WAP&prepayid=wx2016062811583977756452680845630255&timestamp=1467086319&sign=F13A88201DA330213E22E51F10339A27
			//Map<String, Object> map = getAccessToken("021qFDsD18cI8g01O3rD1HDKsD1qFDsu");
			//https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx204459d3f9148e3b&redirect_uri=http%3A%2F%2Ftest.diy.51app.cn%2FdiyMall2%2FyouGoods%2Ftonice.do&response_type=code&scope=snsapi_userinfo&state=kennen#wechat_redirect
			//Map<String, Object> map1 = getUserinfo("SlHgvKidOFrW349z8qgDQ1A_UnLT5HcXUWQZ8ztxuws5PN_NYpr5FPMALzI7RC1YHcMuqGBu0jcUcEZZNAHJQX6CBHx3v62SUrCmczVkCDw", "oWu8buKtZhVe0fHIK3V8JQy7XLig");
			//boolean b = authAccessToken("r2VUnNGsbjEPKd_-DKom43KfNZhIF9amBgv3ygPJKYxdO3vAGV1dki72hx949rgN-aw9Ht_ab40OnAqB4lNBbPhtVblBRVjOy-_OeyHBK6Y", "oWu8buKtZhVe0fHIK3V8JQy7XLig");
			//Map<String, Object> map2 = getRefreshToken("s3GrX0XCdcymfKQGOp3hvFdm1cDpfzgka9trYyLcsqPL7QOotcY1cberCi6IHhIIX_RQ0xoZrHPD9rLHq4VhGOo7egV0TL31-n4InySCBeA");
			//System.out.println(map);
			
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
}
