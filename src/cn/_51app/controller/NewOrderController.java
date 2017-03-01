package cn._51app.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import cn._51app.service.INewOrderService;

@Controller
@RequestMapping("/newOrder")
public class NewOrderController extends BaseController{
	
	@Autowired
	private INewOrderService iNewOrderService;
	
	/**
	 * tengh 2016年5月16日 下午8:45:00
	 * @param imgFile
	 * @param imgBackFile
	 * @param previewFile
	 * @param paramStr
	 * @param deviceNumber
	 * @param app
	 * @param sys
	 * @return
	 * TODO 直接生成订单信息   （为激活状态  -1）
	 */
	@RequestMapping(value ="createOrder",method = { RequestMethod.POST })
	public ResponseEntity<String> createOrder(
			//上传图片为空是表示精品汇
			@RequestParam(value = "imgFile",required=false) MultipartFile imgFile,//原图
			@RequestParam(value = "imgBackFile",required=false) MultipartFile imgBackFile,//背面
			@RequestParam(value = "previewFile",required=false) MultipartFile previewFile,//预览图
			@RequestParam(value = "previewBackFile",required=false) MultipartFile previewBackFile,//预览图 反面
			@RequestParam(value = "infoId") String infoId,
			@RequestParam(value = "textureIds" )String textureIds,
			@RequestParam(value = "deviceNo") String deviceNo,
			@RequestParam(value = "num") String num,
			@RequestParam(value = "app") String app){
		String data =null;
		String msg=null;
		int code =SUCESS;
		try {
			data=this.iNewOrderService.createOrder(imgFile,imgBackFile,previewFile,previewBackFile,infoId,textureIds,deviceNo,app,num);
			if(StringUtils.isBlank(data)){
				code=FAIL;
				msg="网络异常";
			}
		} catch (Exception e) {
			e.printStackTrace();
			code =SERVER_ERR;
		}
		return super.resultInfo(data, code, msg);
	}

	/**
	 * tengh 2016年5月18日 上午10:10:19
	 * @param deviceNo
	 * @param app
	 * @param couponId
	 * @param orderNo
	 * @param addressId
	 * @param num
	 * @param payId  1.支付宝 2.微信
	 * @return
	 * TODO 激活状态是-1的订单  并且 绑定 地址、数量、优惠信息
	 */
	@RequestMapping(value ="activeOrder",method = { RequestMethod.POST })
	public ResponseEntity<String> activeOrder(
			@RequestParam(value="deviceNo") String deviceNo,
			@RequestParam(value="app")String app,
			@RequestParam(value="couponId",required=false) String couponId,
			@RequestParam(value="orderNo")String orderNo,
			@RequestParam(value="addressId")String addressId,
			@RequestParam(value="num")String num,
			@RequestParam(value="payId")String payId){
		String data =null;
		String msg=null;
		int code =SUCESS;
		try {
			data=this.iNewOrderService.activeOrder(deviceNo,app,couponId,orderNo,addressId,num,payId);
			if(StringUtils.isBlank(data)){
				code=FAIL;
				msg="订单生成失败";
			}
		} catch (Exception e) {
			e.printStackTrace();
			code =SERVER_ERR;
		}
		return super.resultInfo(data, code, msg);
	}
	
	/**
	 * tengh 2016年5月17日 下午9:10:02
	 * @return
	 * TODO 查询满减优惠信息
	 */
	@RequestMapping(value ="getPrivilege",method = { RequestMethod.GET })
	public ResponseEntity<String> getPrivilege(){
		String data =null;
		String msg=null;
		int code =SUCESS;
		try {
			data =iNewOrderService.getPrivilege();
			if(StringUtils.isBlank(data)){
				code=EMPTY;
			}
		} catch (Exception e) {
			e.printStackTrace();
			code =SERVER_ERR;
		}
		return super.resultInfo(data, code, msg);
	}
	
	/**
	 * tengh 2016年5月18日 下午4:55:53
	 * @param orderNo
	 * @return
	 * TODO 查询单个订单详细信息
	 */
	@RequestMapping(value ="getOrderInfo",method = { RequestMethod.GET })
	public ResponseEntity<String> getOrderInfo(@RequestParam(value="orderNo") String orderNo,@RequestParam(value="deviceNo")String deviceNo,@RequestParam(value="app")String app){
		String data =null;
		String msg=null;
		int code =SUCESS;
		try {
			data =iNewOrderService.getOrderInfo(orderNo,deviceNo,app);
			if(StringUtils.isBlank(data)){
				code=EMPTY;
				msg="该订单不存在";
			}
		} catch (Exception e) {
			e.printStackTrace();
			code =SERVER_ERR;
		}
		return super.resultInfo(data, code, msg);
	}
	
	/**
	 * tengh 2016年5月18日 下午8:28:26
	 * @param deviceNo
	 * @param app
	 * @return
	 * TODO 查询订单列表
	 */
	@RequestMapping(value ="getOrderList",method = { RequestMethod.GET })
	public ResponseEntity<String> getOrderList(@RequestParam(value="deviceNo")String deviceNo,
			@RequestParam(value="app")String app,
			@RequestParam(value="page")String page,
			@RequestParam(value="state")String state){
		String data =null;
		String msg=null;
		int code =SUCESS;
		try {
			data =iNewOrderService.getOrderList(deviceNo,app,page,state);
			if(StringUtils.isBlank(data)){
				code=EMPTY;
				msg="您还没有订单";
			}
		} catch (Exception e) {
			e.printStackTrace();
			code =SERVER_ERR;
		}
		return super.resultInfo(data, code, msg);
	}
	
	/**
	 * tengh 2016年5月18日 下午6:11:43
	 * @param orderNo
	 * @param deviceNo
	 * @param app
	 * @return
	 * TODO 核查订单是否支付成功
	 */
	@RequestMapping(value ="checkOrder",method = { RequestMethod.GET })
	public ResponseEntity<String> checkOrder(@RequestParam(value="orderNo") String orderNo,@RequestParam(value="deviceNo")String deviceNo,@RequestParam(value="app")String app){
		String data =null;
		String msg=null;
		int code =SUCESS;
		try {
			boolean flag =iNewOrderService.checkOrder(orderNo,deviceNo,app);
			if(!flag){
				code=FAIL;
				msg="订单支付失败";
			}
		} catch (Exception e) {
//			e.printStackTrace();
			code =SERVER_ERR;
		}
		return super.resultInfo(data, code, msg);
	}
	
	/**
	 * tengh 2016年5月19日 上午9:38:09
	 * @param orderNo
	 * @param deviceNo
	 * @param app
	 * @return
	 * TODO 立即支付 (验证库存    微信支付看是prepayId否过期)
	 */
	@RequestMapping(value ="confirmOrder",method = { RequestMethod.GET })
	public ResponseEntity<String> confirmOrder(
			@RequestParam(value="orderNo") String orderNo,
			@RequestParam(value="deviceNo")String deviceNo,
			@RequestParam(value="app")String app){
		String data =null;
		String msg=null;
		int code =SUCESS;
		try {
			String flag =iNewOrderService.confirmOrder(orderNo,deviceNo,app);
			if("-1".equals(flag)){
				code=FAIL;
				msg="订单支付失败";
			}else{
				data="{\"prepayId\":\""+flag+"\",\"orderNo\":\""+orderNo+"\"}";
			}
		} catch (Exception e) {
			e.printStackTrace();
			code =SERVER_ERR;
		}
		return super.resultInfo(data, code, msg);
	}
	
	/**
	 * tengh 2016年5月19日 下午12:00:19
	 * @param orderNo
	 * @param deviceNo
	 * @param app
	 * @param flag  delete删除   confirm确认收货  cancel取消  addShop 加入购物车 paidCancel 已付款取消 
	 * @return
	 * TODO 订单状态的改变
	 */
	@RequestMapping(value ="updateOrder",method = { RequestMethod.GET })
	public ResponseEntity<String> updateOrder(@RequestParam(value="orderNo") String orderNo,@RequestParam(value="deviceNo")String deviceNo,@RequestParam(value="app")String app,@RequestParam(value="flag") String flag){
		String data =null;
		String msg=null;
		int code =SUCESS;
		try {
			boolean result =iNewOrderService.updateOrder(orderNo,deviceNo,app,flag);
			if(!result){
				code=FAIL;
				msg="订单操作失败";
			}
		} catch (Exception e) {
			e.printStackTrace();
			code =SERVER_ERR;
		}
		return super.resultInfo(data, code, msg);
	}
	
	/**
	 * tengh 2016年5月20日 下午6:58:15
	 * @param app
	 * @param deviceNo
	 * @param deviceToken
	 * @return
	 * TODO 新用户插入
	 */
	@RequestMapping(value ="recordUser",method = { RequestMethod.GET })
	public ResponseEntity<String> recordUser(@RequestParam(value="app") String app,@RequestParam(value="deviceNo") String deviceNo,@RequestParam(value="deviceToken") String deviceToken){
		String data =null;
		String msg=null;
		int code =SUCESS;
		try {
			boolean result =iNewOrderService.recordUser(deviceNo,app,deviceToken);
			if(!result){
				code=FAIL;
				msg="订单操作失败";
			}
		} catch (Exception e) {
			e.printStackTrace();
			code =SERVER_ERR;
		}
		return super.resultInfo(data, code, msg);
	}
	
}
