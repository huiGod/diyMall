package cn._51app.controller;

import java.nio.charset.Charset;
import java.util.Date;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import cn._51app.util.PropertiesUtil;


public abstract class BaseController {
	
	 protected final int SUCESS = 200;    // 操作成功 	
	 protected final int FAIL = 300;      // 操作失败
	 protected final int EMPTY = 400;     // 空数据
	 protected final int SERVER_ERR = 500;// 服务器错误
	 protected final int NO_LOGIN = 401;  //未登录
	 protected final int REGISTER = 402;  //未登录
	 protected final int NOOPENID = 403;  //未登录
	 private final String DEV =PropertiesUtil.getValue("project.dev"); //0 正式 1 开发
	 
	 
	 /**>>Faster
	  * 
	  * @param data 数据
	  * @param code 错误码
	  * @param msg 错误信息
	  * @return json数据格式
	  */
	 protected ResponseEntity<String> resultInfo(String data, int code, String msg) {
		 	//设置httpHeaders请求头信息
			HttpHeaders responseHeaders = new HttpHeaders();
			responseHeaders.add("Access-Control-Allow-Origin", "*");//ajax跨域
			//设置MediaType格式，请求头配置
			MediaType mediaType = new MediaType("text", "html",Charset.forName("UTF-8"));
			//设置content-type请求头
			responseHeaders.setContentType(mediaType);
			
			//使用stringbuffer设置字符窜
			StringBuffer responseJson =new StringBuffer();
			responseJson.append("{\"code\":"+code);
			if(DEV.equals("1")){
				if(msg==null){
					switch(code){
						case 200:msg="操作成功";
						break;
						case 300:msg="操作失败";
						break;
						case 400:msg="无数据";
						break;
						case 401:msg="未登录";
						break;
						case 500:msg="服务器错误";
						break;
						default:msg="未定义";
					}
				}
				responseJson.append(",\"message\":\""+msg+ "\"");
			}
			//有数据返回，无数据就返回'}'
			if(data!=null){
				responseJson.append(",\"data\":"+data+ "}");
			}else{
				responseJson.append("}");
			}
			//1-任何数据，2-请求头对象。3-httpStatus
			ResponseEntity<String> responseEntity = new ResponseEntity<String>(responseJson.toString(), responseHeaders, HttpStatus.CREATED);
			return responseEntity;
		}
	 
	 /**>>Faster
	  * 
	  * @param data 数据
	  * @param code 错误码
	  * @param msg 错误信息
	  * @return json数据格式
	  */
	 protected ResponseEntity<String> resultInfo2(String data, int code, String msg,String timestamp) {
		 	//设置httpHeaders请求头信息
			HttpHeaders responseHeaders = new HttpHeaders();
			responseHeaders.add("Access-Control-Allow-Origin", "*");//ajax跨域
			//设置MediaType格式，请求头配置
			MediaType mediaType = new MediaType("text", "html",Charset.forName("UTF-8"));
			//设置content-type请求头
			responseHeaders.setContentType(mediaType);
			
			//使用stringbuffer设置字符窜
			StringBuffer responseJson =new StringBuffer();
			responseJson.append("{\"code\":"+code);
			if(DEV.equals("1")){
				if(msg==null){
					switch(code){
						case 200:msg="操作成功";
						break;
						case 300:msg="操作失败";
						break;
						case 400:msg="无数据";
						break;
						case 401:msg="未登录";
						break;
						case 500:msg="服务器错误";
						break;
						default:msg="未定义";
					}
				}
				responseJson.append(",\"message\":\""+msg+ "\"");
			}
			if(timestamp!=null){
				responseJson.append(",\"timestamp\":"+timestamp);
			}
			//有数据返回，无数据就返回'}'
			if(data!=null){
				responseJson.append(",\"data\":"+data+ "}");
			}else{
				responseJson.append("}");
			}
			//1-任何数据，2-请求头对象。3-httpStatus
			ResponseEntity<String> responseEntity = new ResponseEntity<String>(responseJson.toString(), responseHeaders, HttpStatus.CREATED);
			return responseEntity;
		}

}
