package cn._51app.controller.diy2_0;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import cn._51app.controller.BaseController;
import cn._51app.service.diy2_0.IndentService;

@Controller
@RequestMapping("/indent")
public class OrderController extends BaseController {
	
	@Autowired
	private IndentService indentService;
	
	/**
	 * TODO 直接生成订单为-1
	 * @param imgFile  正面原图
	 * @param imgBackFile  反面原图
	 * @param previewFile  正面效果图
	 * @param previewBackFile  反面效果图
	 * @param infoId  商品id
	 * @param textureIds   材质id
	 * @param deviceNo  设备号
	 * @param num   数量
	 * @param app 平台号
	 * @param req   批量上传照片书时候时候，规定图片名称为（原图包含original名称,效果图包含effect名称）批量上传
	 * @return
	 */
	@RequestMapping(value="/createOrder",method=RequestMethod.POST)
	public ResponseEntity<String>createOrder(
			@RequestParam(value="imgFile",required=false)MultipartFile imgFile,
			@RequestParam(value="imgBackFile",required=false)MultipartFile imgBackFile,
			@RequestParam(value="previewFile",required=false)MultipartFile previewFile,
			@RequestParam(value="previewBackFile",required=false)MultipartFile previewBackFile,
			@RequestParam(value = "infoId") String infoId,
			@RequestParam(value = "textureIds" )String textureIds,
			@RequestParam(value = "deviceNo") String deviceNo,
			@RequestParam(value = "num") String num,
			@RequestParam(value = "app") String app,
			@RequestParam(value ="wallpara",required=false)String wallpaper,
			@RequestParam(value="isBoutique",required=false)String isBoutique,
			HttpServletRequest req
			){
		String data =null;
		String msg="订单生成失败";
		int code =FAIL;
		try {
			data=this. indentService.createOrder(imgFile, imgBackFile, previewFile, previewBackFile, infoId, textureIds, deviceNo, app, num,req,wallpaper,isBoutique);
			msg="订单生成成功";
			code=SUCESS;
			if(StringUtils.isBlank(data)){
				code=EMPTY;
				msg="空数据异常";
			}
		} catch (Exception e) {
			e.printStackTrace();
			code =SERVER_ERR;
		}
		return super.resultInfo(data, code, msg);
	}
	
	/**
	 * TODO 直接生成订单为-1(id版)
	 * @param imgFile
	 * @param imgBackFile
	 * @param previewFile
	 * @param previewBackFile
	 * @param infoId
	 * @param textureIds
	 * @param num
	 * @param userId
	 * @param wallpaper   壁纸库的url,否则传空
	 * @param isBoutique   是否定制的照片书，否则传空
	 * @param req
	 * @return
	 */
	@RequestMapping(value="/createOrderForId",method=RequestMethod.POST)
	public ResponseEntity<String>createOrderForId(
			@RequestParam(value="imgFile",required=false)MultipartFile imgFile,
			@RequestParam(value="imgBackFile",required=false)MultipartFile imgBackFile,
			@RequestParam(value="previewFile",required=false)MultipartFile previewFile,
			@RequestParam(value="previewBackFile",required=false)MultipartFile previewBackFile,
			@RequestParam(value = "infoId") String infoId,
			@RequestParam(value = "textureIds" )String textureIds,
			@RequestParam(value = "num") String num,
			@RequestParam(value = "userId") String userId,
			@RequestParam(value ="wallpara",required=false)String wallpaper,
			@RequestParam(value="isBoutique",required=false)String isBoutique,
			@RequestParam(value ="lettering",required=false)String lettering,
			@RequestParam(value="modId",required=false)String modId,
			@RequestParam(value="keycode",required=false)String keycode,
			@RequestParam(value="isSave",required=false,defaultValue="0")String isSave,
			HttpServletRequest req
			){
		String data =null;
		String msg="订单生成失败";
		int code =FAIL;
		try {
			data=this.indentService.createOrderForId(imgFile, imgBackFile, previewFile, previewBackFile, infoId, textureIds,userId, num,req,wallpaper,isBoutique,lettering,modId,keycode,isSave);
			msg="订单生成成功";
			code=SUCESS;
			if(StringUtils.isBlank(data)){
				code=EMPTY;
				msg="空数据异常";
			}
		} catch (Exception e) {
			e.printStackTrace();
			code =SERVER_ERR;
		}
		return super.resultInfo(data, code, msg);
	}
	
	/**
	 * 
	 * @param deviceNo 设备号
	 * @param app 平台
	 * @param couponId  优惠券id
	 * @param orderNo  订单号
	 * @param addressId  地址id
	 * @param num  数量
	 * @param payId  1.支付宝 2.微信  
	 * @return
	 * TODO 激活状态是-1的订单  并且 绑定 地址、数量、优惠信息
	 */
	@RequestMapping(value ="activeOrder",method = { RequestMethod.POST })
	public ResponseEntity<String> activeOrder(
			@RequestParam(value="deviceNo") String deviceNo,
			@RequestParam(value="app")String app,
			@RequestParam(value="couponIds",required=false) String couponIds,
			@RequestParam(value="orderNo")String orderNo,
			@RequestParam(value="addressId")String addressId,
			@RequestParam(value="num")String num,
			@RequestParam(value="payId")String payId
			){
		String data =null;
		String msg=null;
		int code =SUCESS;
		try {
			data=this.indentService.activeOrder(deviceNo,app,couponIds,orderNo,addressId,num,payId);
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
	 *  TODO 激活状态是-1的订单  并且 绑定 地址、数量、优惠信息(id版)
	 * @param userId  用户id
	 * @param couponId   优惠券id
	 * @param orderNo   订单号
	 * @param addressId  地址id
	 * @param num  订单数量
	 * @param payId  支付id  1支付宝2微信
	 * @param app   平台号
	 * @param message   留言
	 * @return
	 */
	@RequestMapping(value ="activeOrderForId",method = { RequestMethod.POST })
	public ResponseEntity<String> activeOrderForId(
			@RequestParam(value="userId") String userId,
			@RequestParam(value="couponId",required=false) String couponId,
			@RequestParam(value="orderNo")String orderNo,
			@RequestParam(value="addressId")String addressId,
			@RequestParam(value="num")String num,
			@RequestParam(value="payId")String payId,
			@RequestParam(value="app")String app,
			@RequestParam(value="message",defaultValue="")String message
			){
		String data =null;
		String msg=null;
		int code =SUCESS;
		try {
			data=this.indentService.activeOrderForId(userId,couponId,orderNo,addressId,num,payId,app,message);
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
	 * tengh 2016年5月18日 下午4:55:53
	 * @param orderNo
	 * @return
	 * TODO 查询单个订单详细信息
	 */
	@RequestMapping("/getOrderInfo")
	public ResponseEntity<String>getOrderInfo(@RequestParam(value="orderNo") String orderNo,@RequestParam(value="deviceNo")String deviceNo,@RequestParam(value="app")String app){
		String data =null;
		String msg=null;
		int code =SUCESS;
		try {
			data =indentService.getOrderInfo(orderNo,deviceNo,app);
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
	 * TODO  查询单个订单详细信息(id版)
	 * @param orderNo
	 * @param userId
	 * @return
	 */
	@RequestMapping("/getOrderInfoForId")
	public ResponseEntity<String>getOrderInfoForId(@RequestParam(value="orderNo") String orderNo,@RequestParam(value="userId")String userId){
		String data =null;
		String msg=null;
		int code =SUCESS;
		try {
			data =indentService.getOrderInfoForId(orderNo,userId);
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
			data =indentService.getOrderList(deviceNo,app,page,state);
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
	 * TODO 查询订单列表(id版)
	 * @param userId
	 * @param page
	 * @param state
	 * @return
	 */
	@RequestMapping(value ="getOrderListForId",method = { RequestMethod.GET })
	public ResponseEntity<String> getOrderListForId(
			@RequestParam(value="userId")String userId,
			@RequestParam(value="page")String page,
			@RequestParam(value="state")String state){
		String data =null;
		String msg=null;
		int code =SUCESS;
		try {
			data =indentService.getOrderListForId(userId,page,state);
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
			boolean flag =indentService.checkOrder(orderNo,deviceNo,app);
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
	 * TODO 核查订单是否支付成功(id版)
	 * @param orderNo
	 * @param userId
	 * @return
	 */
	@RequestMapping(value ="checkOrderForId",method = { RequestMethod.GET })
	public ResponseEntity<String> checkOrderForId(@RequestParam(value="orderNo") String orderNo,@RequestParam(value="userId")String userId){
		String data =null;
		String msg=null;
		int code =SUCESS;
		try {
			boolean flag =indentService.checkOrderForId(orderNo,userId);
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
			String flag =indentService.confirmOrder(orderNo,deviceNo,app);
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
	 * TODO 立即支付 (验证库存    微信支付看是prepayId否过期)(id版)
	 * @param orderNo
	 * @param userId
	 *   @param app
	 * @return
	 */
	@RequestMapping(value ="confirmOrderForId",method = { RequestMethod.GET })
	public ResponseEntity<String> confirmOrderForId(
			@RequestParam(value="orderNo") String orderNo,
			@RequestParam(value="userId")String userId,
			@RequestParam(value="app")String app){
		String data =null;
		String msg=null;
		int code =SUCESS;
		try {
			String flag =indentService.confirmOrderForId(orderNo,userId,app);
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
	 * @param flag  delete删除   confirm确认收货  cancel取消  addShop加入购物车
	 * @return
	 * TODO 订单状态的改变
	 */
	@RequestMapping(value ="updateOrder",method = { RequestMethod.GET })
	public ResponseEntity<String> updateOrder(@RequestParam(value="orderNo") String orderNo,@RequestParam(value="deviceNo")String deviceNo,@RequestParam(value="app")String app,@RequestParam(value="flag") String flag){
		String data =null;
		String msg=null;
		int code =SUCESS;
		try {
			boolean result =indentService.updateOrder(orderNo,deviceNo,app,flag);
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
	 * TODO  订单状态的改变(id版)
	 * @param orderNo
	 * @param userId
	 * @param flag
	 * @return
	 */
	@RequestMapping(value ="updateOrderForId",method = { RequestMethod.GET })
	public ResponseEntity<String> updateOrderForId(@RequestParam(value="orderNo") String orderNo,@RequestParam(value="userId")String userId,@RequestParam(value="flag") String flag){
		String data =null;
		String msg=null;
		int code =SUCESS;
		try {
			boolean result =indentService.updateOrderForId(orderNo,userId,flag);
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
			boolean result =indentService.recordUser(deviceNo,app,deviceToken);
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
	 * TODO 新用户插入(id版)
	 * @param userId
	 * @param deviceToken
	 * @return
	 */
	@RequestMapping(value ="recordUserForId",method = { RequestMethod.GET })
	public ResponseEntity<String> recordUserForId(@RequestParam(value="userId") String userId,@RequestParam(value="deviceToken") String deviceToken){
		String data =null;
		String msg=null;
		int code =SUCESS;
		try {
			boolean result =indentService.recordUserForId(userId,deviceToken);
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
	 * TODO h5直接生成订单为-1
	 * @param imgFile  正面原图
	 * @param imgBackFile  反面原图
	 * @param previewFile  正面效果图
	 * @param previewBackFile  反面效果图
	 * @param infoId  商品id
	 * @param textureIds   材质id
	 * @param deviceNo  设备号
	 * @param num   数量
	 * @param app 平台号
	 * @param req   批量上传照片书时候时候，规定图片名称为（原图包含original名称,效果图包含effect名称）批量上传
	 * @return
	 */
	@RequestMapping(value="/createOrderH5",method=RequestMethod.POST)
	public ResponseEntity<String>createOrderH5(
			@RequestParam(value="imgFile",required=false)MultipartFile imgFile,
			@RequestParam(value="imgBackFile",required=false)MultipartFile imgBackFile,
			@RequestParam(value="previewFile",required=false)MultipartFile previewFile,
			@RequestParam(value="previewBackFile",required=false)MultipartFile previewBackFile,
			@RequestParam(value = "infoId") String infoId,
			@RequestParam(value = "textureIds" )String textureIds,
			@RequestParam(value = "deviceNo") String deviceNo,
			@RequestParam(value = "num") String num,
			@RequestParam(value = "app") String app,
			HttpServletRequest req
			){
		String data =null;
		String msg="订单生成失败";
		int code =FAIL;
		try {
			data=this. indentService.createOrder(imgFile, imgBackFile, previewFile, previewBackFile, infoId, textureIds, deviceNo, app, num,req,null,null);
			msg="订单生成成功";
			code=SUCESS;
			if(StringUtils.isBlank(data)){
				code=EMPTY;
				msg="空数据异常";
			}
		} catch (Exception e) {
			e.printStackTrace();
			code =SERVER_ERR;
		}
		return super.resultInfo(data, code, msg);
	}
	
	/**
	 * tengh 2016年12月6日 下午8:53:55
	 * @param shopNo
	 * @param textureIds
	 * @param userId
	 * @return
	 * TODO 修改商品材质
	 */
	@RequestMapping(value ="changeTexture",method = { RequestMethod.GET })
	public ResponseEntity<String> changeTexture(@RequestParam(value="shopNo") String shopNo,@RequestParam(value="textureIds") String textureIds,@RequestParam(value="userId")String userId){
		String data =null;
		String msg=null;
		int code =SUCESS;
		try {
			boolean result =indentService.changeTexture(shopNo,userId,textureIds);
			if(!result){
				code=FAIL;
				msg="操作失败";
			}
		} catch (Exception e) {
			e.printStackTrace();
			code =SERVER_ERR;
		}
		return super.resultInfo(data, code, msg);
	}
	
	/**
	 * tengh 2016年12月23日 上午11:55:39
	 * @param keycode
	 * @param imgFile
	 * @return
	 * TODO 单个文件上传
	 */
	@RequestMapping(value ="uploadFile",method = { RequestMethod.POST })
	public ResponseEntity<String> uploadFile(@RequestParam(value="keycode") String keycode,@RequestParam(value="imgFile")MultipartFile imgFile){
		String data =null;
		String msg=null;
		int code =SUCESS;
		try {
			boolean result =indentService.uploadFile(keycode,imgFile);
			if(!result){
				code=FAIL;
				msg="操作失败";
			}
		} catch (Exception e) {
			e.printStackTrace();
			code =SERVER_ERR;
		}
		return super.resultInfo(data, code, msg);
	}
}
