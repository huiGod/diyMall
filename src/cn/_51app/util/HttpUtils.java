package cn._51app.util;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

/**
 * http请求
 * @author Zhu Jian
 *
 */
public class HttpUtils {
    /**
     * post请求 ，超时默认10秒, 默认utf-8
     * @param url
     * @param params
     * @return
     * @throws IOException
     */
    public String post(String url, Map<String, String> params) throws Exception {
        return this.post(url, params, 10, HTTP.UTF_8);
    }
    /**
     * post请求, 超时默认10秒
     * @param url
     * @param params
     * @param charset 编码方式
     * @return
     * @throws Exception
     */
    public String post(String url, Map<String, String> params, String charset) throws Exception {
        return this.post(url, params, 10, charset);
    }
    /**
     * post请求, 默认utf-8
     * @param url
     * @param params
     * @param timeout 超时时间，秒
     * @return
     * @throws Exception
     */
    public String post(String url, Map<String, String> params, int timeout) throws Exception {
        return this.post(url, params, timeout, HTTP.UTF_8);
    }
    /**
     * post请求
     * @param url
     * @param params
     * @param timeout 超时时间，秒
     * @return
     * @throws IOException
     */
    public static String post(String url, Map<String, String> params, int timeout, String charset) throws Exception {
        HttpClient httpclient = new DefaultHttpClient();
        httpclient.getParams().setIntParameter("http.socket.timeout", timeout * 1000);
        httpclient.getParams().setBooleanParameter("http.protocol.expect-continue", false);
        String retVal = "";
        try {
            List<NameValuePair> formparams = new ArrayList<NameValuePair>();
            if (params != null) {
                for (Map.Entry<String, String> param : params.entrySet()) {
                    formparams.add(new BasicNameValuePair(param.getKey(), param.getValue()));
                }
            }
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, charset);
            HttpPost httppost = new HttpPost(url);
            httppost.setEntity(entity);
            HttpResponse resp = httpclient.execute(httppost);
            retVal = EntityUtils.toString(resp.getEntity(), charset);
        } catch (IOException e) {
            throw e;
        } finally {
            httpclient.getConnectionManager().shutdown();
        }
        return retVal;
    }
    
    public static String post(String url, int timeout, String str) throws Exception {
        HttpClient httpclient = new DefaultHttpClient();
        httpclient.getParams().setIntParameter("http.socket.timeout", timeout * 1000);
        httpclient.getParams().setBooleanParameter("http.protocol.expect-continue", false);
        httpclient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, timeout * 1000);
        String retVal = "";
        try {
        	if(url.startsWith("https")){
    			verifierHostname();
    		}
            HttpPost httppost = new HttpPost(url);
            StringEntity reqEntity = new StringEntity(str, "UTF-8");
            httppost.setEntity(reqEntity);
            httppost.setHeader("Content-type", "text/xml; charset=\"utf-8\"");
            httppost.setHeader("User-Agent", "InetURL/1.0");
            HttpResponse resp = httpclient.execute(httppost);
            retVal = EntityUtils.toString(resp.getEntity(), "UTF-8");
        } catch (IOException e) {
            throw e;
        } finally {
            httpclient.getConnectionManager().shutdown();
        }
        return retVal;
    }
    
    private static void verifierHostname() throws NoSuchAlgorithmException, KeyManagementException {
		SSLContext sslContext = null;
		sslContext = SSLContext.getInstance("TLS");
		X509TrustManager xtm = new X509TrustManager() {
			public void checkClientTrusted(X509Certificate[] chain, String authType) {
			}

			public void checkServerTrusted(X509Certificate[] chain, String authType) {
			}

			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}
		};
		X509TrustManager[] xtmArray = new X509TrustManager[] { xtm };
		sslContext.init(null, xtmArray, new java.security.SecureRandom());
		if (sslContext != null) {
			HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
		}
		HostnameVerifier hnv = new HostnameVerifier() {
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		};
		HttpsURLConnection.setDefaultHostnameVerifier(hnv);
	}
    
    public static String postOut(String url, int timeout, String str) throws Exception {
        HttpClient httpclient = new DefaultHttpClient();
        httpclient.getParams().setIntParameter("http.socket.timeout", timeout * 1000);
        httpclient.getParams().setBooleanParameter("http.protocol.expect-continue", false);
        httpclient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, timeout * 1000);
        String retVal = "";
        try {
            HttpPost httppost = new HttpPost(url);
            StringEntity reqEntity = new StringEntity(str, "UTF-8");
            httppost.setEntity(reqEntity);
            httppost.setHeader("Content-type", "text/xml; charset=\"UTF-8\"");
            httppost.setHeader("User-Agent", "InetURL/1.0");
            HttpResponse resp = httpclient.execute(httppost);
            retVal = EntityUtils.toString(resp.getEntity(), "UTF-8");
        } catch (IOException e) {
            throw e;
        } finally {
            httpclient.getConnectionManager().shutdown();
        }
        return retVal;
    }
    
    public static String postOut(String url, int timeout, String str, String charset) throws Exception {
        HttpClient httpclient = new DefaultHttpClient();
        httpclient.getParams().setIntParameter("http.socket.timeout", timeout * 1000);
        httpclient.getParams().setBooleanParameter("http.protocol.expect-continue", false);
        httpclient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, timeout * 1000);
        String retVal = "";
        try {
            HttpPost httppost = new HttpPost(url);
            StringEntity reqEntity = new StringEntity(str, charset);
            httppost.setEntity(reqEntity);
            httppost.setHeader("Content-type", "text/xml; charset=\"UTF-8\"");
            httppost.setHeader("User-Agent", "InetURL/1.0");
            HttpResponse resp = httpclient.execute(httppost);
            retVal = EntityUtils.toString(resp.getEntity(), "UTF-8");
        } catch (IOException e) {
            throw e;
        } finally {
            httpclient.getConnectionManager().shutdown();
        }
        return retVal;
    }
    
    public static String postByLock(String url, int timeout, String str) throws Exception {
        HttpClient httpclient = new DefaultHttpClient();
        httpclient.getParams().setIntParameter("http.socket.timeout", timeout * 1000);
        httpclient.getParams().setBooleanParameter("http.protocol.expect-continue", false);
        httpclient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, timeout * 1000);
        String retVal = "";
        try {
            HttpPost httppost = new HttpPost(url);
            StringEntity reqEntity = new StringEntity(str);
            httppost.setEntity(reqEntity);

            httppost.setHeader("Accept", "image/gif, image/jpeg, image/pjpeg, image/pjpeg, " +
            		"application/x-shockwave-flash, application/vnd.ms-excel, " +
            		"application/vnd.ms-powerpoint, application/msword, application/xaml+xml, " +
            		"application/x-ms-xbap, application/x-ms-application, */*");
            httppost.setHeader("Accept-Language", "zh-cn");
            httppost.setHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.1; " +
            		"Trident/4.0; .NET CLR 2.0.50727; Zune 4.7; .NET4.0C; .NET4.0E)");
            httppost.setHeader("Content-type", "application/x-www-form-urlencoded");
//            httppost.setHeader("Accept-Encoding", "gzip, deflate");
            httppost.setHeader("Host", "iunlocker.net");
            httppost.setHeader("Connection", "Keep-Alive");
            httppost.setHeader("Cache-Control", "no-cache");
            httppost.setHeader("Referer", "http://iunlocker.net/check_imei_bulk.php");
            httppost.setHeader("Content-Encoding", "gzip");
            HttpResponse resp = httpclient.execute(httppost);
            retVal = EntityUtils.toString(resp.getEntity(), "GB2312");
        } catch (IOException e) {
            throw e;
        } finally {
            httpclient.getConnectionManager().shutdown();
        }
        return retVal;
    }
    
    /**
     * get请求
     * @param url
     * @param params
     * @param timeout 超时时间，秒
     * @param charset 编码方式
     * @return
     * @throws Exception
     */
    public static String get(String url, Map<String, String> params, int timeout, String charset) throws Exception {
        HttpClient httpclient = new DefaultHttpClient();
        httpclient.getParams().setIntParameter("http.socket.timeout", timeout * 1000);
        httpclient.getParams().setBooleanParameter("http.protocol.expect-continue", false);
        String retVal = "";
        try {
            List<NameValuePair> qparams = new ArrayList<NameValuePair>();
            if (params != null) {
                for (Map.Entry<String, String> param : params.entrySet()) {
                    qparams.add(new BasicNameValuePair(param.getKey(), param.getValue()));
                }
            }
            String paramstr = URLEncodedUtils.format(qparams, charset);
            if (StringUtils.isNotEmpty(paramstr)) {
                url = url + "?" + paramstr;
            }
            HttpGet httpget = new HttpGet(url);
            HttpResponse resp = httpclient.execute(httpget);
            retVal = EntityUtils.toString(resp.getEntity(), charset);
        } catch (IOException e) {
            throw e;
        } finally {
            httpclient.getConnectionManager().shutdown();
        }
        return retVal;
    }
    
    
    /**
     * get请求
     * @param url
     * @param params
     * @param timeout 超时时间，秒
     * @param charset 编码方式
     * @param fNameEndChar 方法名后结束字符 默认“?”
     * @return
     * @throws Exception
     */
    public static String get(String url, Map<String, String> params, int timeout, String charset,String fNameEndChar) throws Exception {
        HttpClient httpclient = new DefaultHttpClient();
//        org.apache.http.impl.client.ProxyClient proc = new org.apache.http.impl.client.ProxyClient();
        httpclient.getParams().setIntParameter("http.socket.timeout", timeout * 1000);
        httpclient.getParams().setBooleanParameter("http.protocol.expect-continue", false);
        //请求超时 
        //CoreConnectionPNames.CONNECTION_TIMEOUT  == "http.connection.timeout"
        httpclient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, timeout * 1000);  
//        //读取超时
//        //CoreConnectionPNames.SO_TIMEOUT  == "http.socket.timeout"
//        httpclient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, timeout * 1000);
//        httpclient.getParams().setBooleanParameter("http.protocol.expect-continue", false);
        String retVal = "";
        fNameEndChar=(fNameEndChar==null || "".endsWith(fNameEndChar))? "?":fNameEndChar;
        try {
            List<NameValuePair> qparams = new ArrayList<NameValuePair>();
            if (params != null) {
                for (Map.Entry<String, String> param : params.entrySet()) {
                    qparams.add(new BasicNameValuePair(param.getKey(), param.getValue()));
                }
            }
            String paramstr = URLEncodedUtils.format(qparams, charset);
            if (StringUtils.isNotEmpty(paramstr)) {
                url = url + fNameEndChar + paramstr;
            }
            HttpGet httpget = new HttpGet(url);
            
            HttpResponse resp = httpclient.execute(httpget);
//            System.out.println(httpclient.getParams().getParameter("port"));
            retVal = EntityUtils.toString(resp.getEntity(), charset);
        } catch (IOException e) {
            throw e;
        } finally {
            httpclient.getConnectionManager().shutdown();
            httpclient = null;
        }
        return retVal;
    }
    
    /**
     * get请求
     * @param url
     * @param params
     * @param timeout 超时时间，秒
     * @param charset 编码方式
     * @param fNameEndChar 方法名后结束字符 默认“?”
     * @return
     * @throws Exception
     */
    public static String getByIacqua(String url, Map<String, String> params, int timeout, String charset,String fNameEndChar) throws Exception {
        HttpClient httpclient = new DefaultHttpClient();
        httpclient.getParams().setIntParameter("http.socket.timeout", timeout * 1000);
        httpclient.getParams().setBooleanParameter("http.protocol.expect-continue", false);
        //请求超时 
        //CoreConnectionPNames.CONNECTION_TIMEOUT  == "http.connection.timeout"
        httpclient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, timeout * 1000);  
//        //读取超时
//        //CoreConnectionPNames.SO_TIMEOUT  == "http.socket.timeout"
//        httpclient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, timeout * 1000);
//        httpclient.getParams().setBooleanParameter("http.protocol.expect-continue", false);
        String retVal = "";
        fNameEndChar=(fNameEndChar==null || "".endsWith(fNameEndChar))? "?":fNameEndChar;
        try {
            List<NameValuePair> qparams = new ArrayList<NameValuePair>();
            if (params != null) {
                for (Map.Entry<String, String> param : params.entrySet()) {
                    qparams.add(new BasicNameValuePair(param.getKey(), param.getValue()));
                }
            }
            String paramstr = URLEncodedUtils.format(qparams, charset);
            if (StringUtils.isNotEmpty(paramstr)) {
                url = url + fNameEndChar + paramstr;
            }
            HttpGet httpget = new HttpGet(url);
            httpget.setHeader("User-Agent", "iacqua/1.1-405");
            
            HttpResponse resp = httpclient.execute(httpget);
            retVal = EntityUtils.toString(resp.getEntity(), charset);
        } catch (IOException e) {
            throw e;
        } finally {
            httpclient.getConnectionManager().shutdown();
            httpclient = null;
        }
        return retVal;
    }
    
    /**
     * get请求,超时默认10秒
     * @param url
     * @param params
     * @param charset 编码方式
     * @return
     * @throws IOException
     */
    public String get(String url, Map<String, String> params, String charset) throws Exception {
        return this.get(url, params, 10, charset);
    }
    /**
     * get请求,超时默认10秒, 默认utf-8
     * @param url
     * @param params
     * @return
     * @throws Exception
     */
    public String get(String url, Map<String, String> params) throws Exception {
        return this.get(url, params, 10, HTTP.UTF_8);
    }
    /**
     * get请求, 默认utf-8
     * @param url
     * @param params
     * @param timeout 超时时间，秒
     * @return
     * @throws Exception
     */
    public String get(String url, Map<String, String> params, int timeout) throws Exception {
        return this.get(url, params, timeout, HTTP.UTF_8);
    }
    
    public static Map<String, String> getHeaderFromUrl(String url){
    	Map<String, String> map = new HashMap<String, String>();
    	HttpGet httpget = new HttpGet(url);
		HttpClient httpclient = new DefaultHttpClient();
		httpclient.getParams().setIntParameter("http.socket.timeout", 5 * 1000);
		httpclient.getParams().setBooleanParameter("http.protocol.expect-continue", false);
		try {
			HttpResponse resp = httpclient.execute(httpget);
			Header[] h = resp.getAllHeaders();
			map.put("HttpStatus", resp.getStatusLine().toString().split(" ")[1]);
			map.put("reqUrl", url);
			String[] ar;
			for(int i=0; i<h.length; i++){
				ar = h[i].toString().split(":");
				map.put(ar[0], h[i].toString().replaceFirst(ar[0], "").trim().replaceFirst(":", "").trim());
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			map = null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			map = null;
		}
		return map;
    } 
    
    public static void main(String[] args){
    	try {
			System.err.println(get("http://melongame.winclick.net/index.php?r=app/sendDate/dosend&channel=1001&idfa=6FF98C21-C84F-44EB-BE08-AB6B62C345A5&ip=36.149.86.22", null, 5, "utf-8"));
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}