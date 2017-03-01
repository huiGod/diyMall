package cn._51app.entity;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

public class User implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer id;
	
	private String headUrl;
	
	private String app;
	
	private String deviceNo;
	
	private String name;
	
	private String openid;
	
	private String mobile;
	
	private String sex;
	
	private String qqid;
	
	//授权登录是完整的地址
	private String imgurl;
	
	private String hxUserName;
	
	private String hxPassWord;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getHeadUrl() {
		return headUrl;
	}

	public void setHeadUrl(String headUrl) {
		this.headUrl = headUrl;
	}

	public String getApp() {
		return app;
	}

	public void setApp(String app) {
		this.app = app;
	}

	public String getDeviceNo() {
		return deviceNo;
	}

	public void setDeviceNo(String deviceNo) {
		this.deviceNo = deviceNo;
	}

	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOpenid() {
		return openid==null?"":openid;
	}

	public void setOpenid(String openid) {
		if(StringUtils.isBlank(openid) || "null".equals(openid) || "(null)".equals(openid) || "<null>".equals(openid)){
			openid=null;
		}
		this.openid = openid;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getImgurl() {
		return imgurl;
	}

	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getQqid() {
		return qqid==null?"":qqid;
	}

	public void setQqid(String qqid) {
		this.qqid = qqid;
	}

	public String getHxUserName() {
		return hxUserName==null?"":hxUserName;
	}

	public void setHxUserName(String hxUserName) {
		this.hxUserName = hxUserName;
	}

	public String getHxPassWord() {
		return hxPassWord==null?"":hxPassWord;
	}

	public void setHxPassWord(String hxPassWord) {
		this.hxPassWord = hxPassWord;
	}
	
	
	
}
