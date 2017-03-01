package cn._51app.entity;

import java.io.Serializable;

public class CutPriceCoupon implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2926966234506798409L;

	private String id;
	private String orgPrice;//原价
	private String nowPrice;//现价
	private String name;
	private String info;
	private String valid;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getOrgPrice() {
		return orgPrice==null?"":orgPrice;
	}
	public void setOrgPrice(String orgPrice) {
		this.orgPrice = orgPrice;
	}
	public String getNowPrice() {
		return nowPrice==null?"":nowPrice;
	}
	public void setNowPrice(String nowPrice) {
		this.nowPrice = nowPrice;
	}
	public String getName() {
		return name==null?"":name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getInfo() {
		return info==null?"":info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public String getValid() {
		return valid==null?"":valid;
	}
	public void setValid(String valid) {
		this.valid = valid;
	}
}
