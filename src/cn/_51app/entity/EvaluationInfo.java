package cn._51app.entity;

import java.io.Serializable;

/**
 * @author yuanqi 订单评价详情实体类，对应数据库diy_evaluation表
 */
public class EvaluationInfo implements Serializable {
	private Integer id;
	private Integer goodsId;// 商品id
	
	private String deviceNo;//设备号
	private Integer evalType;// 评价类型(1好评2中评3差评)
	private String content;// 评价内容
	private String cime;// 创建时间
	private String imgUrl;// 图片
	private Integer imgNull;// 0 表示有图 1 表示没图
	private Integer isTop;// 精选评论
	private Integer state;// 是否有效
	private Integer userId;// 用户id
	private Integer goodsType;//商品类型：0非定制、1定制

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Integer goodsId) {
		this.goodsId = goodsId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getEvalType() {
		return evalType;
	}
	
	

	public String getDeviceNo() {
		return deviceNo;
	}

	public void setDeviceNo(String deviceNo) {
		this.deviceNo = deviceNo;
	}

	public void setEvalType(Integer evalType) {
		this.evalType = evalType;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCime() {
		return cime;
	}

	public void setCime(String cime) {
		this.cime = cime;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public Integer getImgNull() {
		return imgNull;
	}

	public void setImgNull(Integer imgNull) {
		this.imgNull = imgNull;
	}

	public Integer getIsTop() {
		return isTop;
	}

	public void setIsTop(Integer isTop) {
		this.isTop = isTop;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Integer getGoodsType() {
		return goodsType;
	}

	public void setGoodsType(Integer goodsType) {
		this.goodsType = goodsType;
	}
	
	

}
