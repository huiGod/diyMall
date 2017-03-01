package cn._51app.entity;

import java.io.Serializable;
import java.util.List;
/**
 * 
 * @author  yuanqi
 * 评论列表实体类，一个订单号对应多个评论
 */
public class Evaluations implements Serializable {
	private List<EvaluationInfo> list;//评论列表
	private String orderNo;//订单编号
	public List<EvaluationInfo> getList() {
		return list;
	}
	public void setList(List<EvaluationInfo> list) {
		this.list = list;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	
}
