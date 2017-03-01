package cn._51app.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import cn._51app.service.ICouponService;
import cn._51app.service.INewAddressService;
import cn._51app.service.INewOrderService;
import cn._51app.service.INewShopCartService;
import cn._51app.service.IUUMallService;
import cn._51app.util.PropertiesUtil;

@Controller
@RequestMapping("/UOrder")  
public class UOrderController extends BaseController{

	@Autowired
	private IUUMallService iuuMallService;
	
	@Autowired
	private INewShopCartService inewShopCartService;
	
	@Autowired
	private INewOrderService iNewOrderService;
	
	@Autowired
	private INewAddressService iNewAddressService;
	
	@Autowired
	private ICouponService iCouponService;
	
	private ObjectMapper mapper=new ObjectMapper();
	
	private final String diy_pro_url =PropertiesUtil.getValue("diy.pro.url");
	
	/**
	 * 购物车数量
	 * @author zhanglz
	 */
	@RequestMapping("/shopNum")
	public ResponseEntity<String> shopNum(HttpSession session){
		String msg=null;
		int code =SERVER_ERR;
		String data = null;
		try {
			Map<String, Object> map = (Map)session.getAttribute("user");
			//String deviceNo = "A4AD9E6F-77AF-4EDD-8538-1FC8FCC05595";//(String)map.get("device_no");
			//String app = "com.shua.h5";//(String)map.get("app");
			String deviceNo = (String)map.get("device_no");
			String app = (String)map.get("app");
			data = iuuMallService.getShopNum(deviceNo, app)+"";
			code=SUCESS;
		} catch (Exception e) {
		}
		return super.resultInfo(data, code, msg);
	}
	
	@RequestMapping("/toShop")
	public String toShopCar(HttpSession session,Model model){
		try {
			Map<String, Object> map = (Map)session.getAttribute("user");
			if(map==null){
				return "redirect:/UMallUser/toLogin.do?isGoodsPage=1";
			}
			String deviceNo = (String)map.get("device_no");
			String app = (String)map.get("app");
			List<Map<String, Object>> list = iuuMallService.shopList(deviceNo, app);
			model.addAttribute("shop", list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "UUMall/shoppingCar_index";
	}
	
	@RequestMapping("/ushop")
	public String ushop(HttpSession session,Model model){
		Map<String, Object> map = (Map)session.getAttribute("user");
		String deviceNo = (String)map.get("device_no");
		String app = (String)map.get("app");
		List<Map<String, Object>> list;
		try {
			list = iuuMallService.shopList(deviceNo, app);
			model.addAttribute("shop", list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "UUMall/shoppingCar_index";
	}
	
	/**
	 * 购物车列表
	 * @author zhanglz
	 */
	@RequestMapping("/shopList")
	public ResponseEntity<String> shopList(HttpSession session,String page){
		String msg=null;
		int code =SERVER_ERR;
		String data = null;
		if(page==null){
			page="0";
		}
		try {
			Map<String, Object> map = (Map)session.getAttribute("user");
			String deviceNo = (String)map.get("device_no");
			String app = (String)map.get("app");
			data = inewShopCartService.shopList(deviceNo, app, page);
			code=SUCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return super.resultInfo(data, code, msg);
	}
	
	/**
	 * 加入购物车
	 * @author zhanglz
	 */
	@RequestMapping("/addShop")
	public ResponseEntity<String> addShop(HttpSession session,
			@RequestParam(value = "infoId") String infoId,
			@RequestParam(value = "textureIds" )String textureIds,
			@RequestParam(value = "num") String num){
		String msg=null;
		int code =SUCESS;
		String data = null;
		try {
			Map<String, Object> map = (Map)session.getAttribute("user");
			if(map==null){
				code = NO_LOGIN;
				return super.resultInfo(data, code, msg);
			}
			String deviceNo = (String)map.get("device_no");
			String app = (String)map.get("app");
			boolean flag = iuuMallService.addShop(infoId, textureIds, deviceNo, num, app);//(infoId, textureIds, deviceNo, num, app);
			if(!flag){
				code =FAIL;
				msg="添加到购物车失败";
			}
		} catch (Exception e) {
			e.printStackTrace();
			code=SERVER_ERR;
		}
		return super.resultInfo(data, code, msg);
	}
	
	/**
	 * 购物车操作(编辑数量、删除)
	 * @author zhanglz
	 */
	@RequestMapping("/editShop")
	public ResponseEntity<String> editShop(HttpSession session,
			@RequestParam(value = "shopNo")String shopNo,
			@RequestParam(value = "operation")String operation,
			@RequestParam(value = "total_number")String total_number){
		String data =null;
		String msg=null;
		int code =SUCESS;
		try {
			Map<String, Object> map = (Map)session.getAttribute("user");
			String deviceNo = (String)map.get("device_no");
			String app = (String)map.get("app");
			boolean flag = false;
			if(operation.contains("delete")){
				flag =this.inewShopCartService.deleteShop(deviceNo,app,shopNo);
			}else if(operation.contains("edit")){
				flag =this.inewShopCartService.updateShop(deviceNo,app,shopNo,total_number);
			}
			if(!flag){
				code =FAIL;
				msg="编辑购物车失败";
			}
		} catch (Exception e) {
			e.printStackTrace();
			code =SERVER_ERR;
		}
		return super.resultInfo(data, code, msg);
	}
	
	/**
	 * 购物车结算
	 * @author zhanglz
	 */
	@RequestMapping("/shopBuy")
	public String shopBuy(HttpSession session,Model model,String shopNo){
		try {
			Map<String, Object> map = (Map)session.getAttribute("user");
			String deviceNo = (String)map.get("device_no");
			String app = (String)map.get("app");
			List<Map<String, Object>> list = iuuMallService.shopBuy(shopNo,deviceNo,app);
			model.addAttribute("shop", list);
			System.out.println(shopNo.split(",").length);
			model.addAttribute("shopSize", list.size());
			model.addAttribute("shopNos", shopNo);
			//地址信息
			List<Map<String, Object>> adress = iuuMallService.getAdress(deviceNo, app);
			model.addAttribute("adress", adress);
			//优惠卷信息
			List<Map<String, Object>> coupon = iuuMallService.getCoupon(deviceNo, app);
			model.addAttribute("coupon", coupon);
			model.addAttribute("isOne", 2);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "UUMall/shoppingCar_confirm";
	}
	
	
	/**
	 * 立即购买
	 * @author zhanglz
	 */
	@RequestMapping("/createOrder")
	public String createOrder(HttpSession session,Model model,
			@RequestParam(value = "infoId") String infoId,
			@RequestParam(value = "textureIds" )String textureIds,
			@RequestParam(value = "num") String num){
		try {
			Map<String, Object> map = (Map)session.getAttribute("user");
			if(map==null){
				return "redirect:/UMallUser/toLogin.do?isGoodsPage=1&infoId="+infoId+"&textureIds="+textureIds+"&num="+num+"&state=2";
			}
			String deviceNo = (String)map.get("device_no");
			String app = (String)map.get("app");
			String data=this.iuuMallService.createOrder(infoId,textureIds,deviceNo,app,num);
			Map<String,Object> m=new ObjectMapper().readValue(data, new TypeReference<Map<String,Object>>(){});
			model.addAttribute("goods", m);
			//地址信息
			List<Map<String, Object>> adress = iuuMallService.getAdress(deviceNo, app);
			model.addAttribute("adress", adress);
			//优惠卷信息
			List<Map<String, Object>> coupon = iuuMallService.getCoupon(deviceNo, app);
			model.addAttribute("coupon", coupon);
			model.addAttribute("isOne", 1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "UUMall/shoppingCar_confirm";
	}
	
	/**
	 * 立即购买提交订单
	 * @author zhanglz
	 */
	@RequestMapping(value ="/formOrderOne",method = { RequestMethod.POST })
	public String formOrderOne(HttpSession session,Model model,HttpServletRequest request,
			@RequestParam(value = "orderNo")String orderNo,
			@RequestParam(value = "couponId",required=false)String couponId,
			@RequestParam(value = "addressId")String addressId,
			@RequestParam(value = "num")String num,
			@RequestParam(value = "payId")String payId,
			@RequestParam(value = "remark",required=false)String remark){
		try {
			Map<String, Object> map = (Map)session.getAttribute("user");
			String deviceNo = (String)map.get("device_no");
			String app = (String)map.get("app");
			String json = iuuMallService.formOrderOne(deviceNo, app, couponId, orderNo, addressId, num, payId,remark);
			Map<String, Object> payMap=mapper.readValue(json, HashMap.class);
			if(payMap!=null){
				if("1".equals(payId)){
					request.setAttribute("WIDout_trade_no", orderNo);
					request.setAttribute("WIDtotal_fee", payMap.get("totalFee"));
					request.setAttribute("WIDsubject", "唯优品");
					request.setAttribute("WIDshow_url", diy_pro_url+"UOrder/toOrderList.do");
					return "UUMall/pay/alipayapi";
				}else if("2".equals(payId)){
					model.addAttribute("pay", payMap);
					return "UUMall/pay/WeixinPay";
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "UUMall/";
	}
	
	/**
	 * 购物车生成订单
	 * @author zhanglz
	 */
	@RequestMapping(value ="/createOrderByShops",method = { RequestMethod.POST })
	public String createOrderByShops(HttpSession session,Model model,HttpServletRequest request,
			@RequestParam(value = "shopNos") String shopNos,
			@RequestParam(value = "payId") String payId,
			@RequestParam(value = "addressId") String addressId,
			@RequestParam(value = "couponId",required=false) String couponId,
			@RequestParam(value = "remark",required=false) String remark){
		try {
			Map<String, Object> map = (Map)session.getAttribute("user");
			String deviceNo = (String)map.get("device_no");
			String app = (String)map.get("app");
			String json = iuuMallService.createOrderByShops(deviceNo,app,shopNos,payId,addressId,couponId,remark);
			Map<String, Object> payMap=mapper.readValue(json, HashMap.class);
			if(payMap!=null){
				if("1".equals(payId)){
					request.setAttribute("WIDout_trade_no", payMap.get("orderNo"));
					request.setAttribute("WIDtotal_fee", payMap.get("totalFee"));
					request.setAttribute("WIDsubject", "唯优品");
					request.setAttribute("WIDshow_url", diy_pro_url+"UOrder/toOrderList.do");
					return "UUMall/pay/alipayapi";
				}else if("2".equals(payId)){
					model.addAttribute("pay", payMap);
					return "UUMall/pay/WeixinPay";
				}
			}
		} catch (Exception e) {
			
		}
		return "UUMall/";
	}
	
	/**
	 * 订单列表立即支付
	 * @author zhanglz
	 */
	@RequestMapping("/orderPay")
	public String orderPay(HttpSession session,Model model,HttpServletRequest request,
			@RequestParam(value = "orderNo")String orderNo){
		try {
			Map<String, Object> map = (Map)session.getAttribute("user");
			String deviceNo = (String)map.get("device_no");
			String app = (String)map.get("app");
			Map<String, Object> payMap = iuuMallService.confirmOrder(orderNo,deviceNo, app);
			if(payMap!=null){
				String payId = (String)payMap.get("payType");
				if("1".equals(payId)){
					request.setAttribute("WIDout_trade_no", orderNo);
					request.setAttribute("WIDtotal_fee", payMap.get("totalFee"));
					request.setAttribute("WIDsubject", "唯优品");
					request.setAttribute("WIDshow_url", diy_pro_url+"UOrder/toOrderList.do");
					return "UUMall/pay/alipayapi";
				}else if("2".equals(payId)){
					model.addAttribute("pay", payMap);
					return "UUMall/pay/WeixinPay";
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "UUMall/";
	}
	
	@RequestMapping("toOrderList")
	public String toOrderList(HttpSession session){
		Map<String, Object> map = (Map)session.getAttribute("user");
		if(map==null){
			return "redirect:/UMallUser/toLogin.do?isGoodsPage=1";
		}
		return "UUMall/order_list";
	}
	
	/**
	 * 订单列表
	 * @author zhanglz
	 */
	@RequestMapping("/getOrderList")
	public ResponseEntity<String> getOrderList(HttpSession session,
			@RequestParam(value="page")String page,
			@RequestParam(value="status")String status){
		String data =null;
		String msg=null;
		int code =SUCESS;
		try {
			Map<String, Object> map = (Map)session.getAttribute("user");
			String deviceNo = (String)map.get("device_no");
			String app = (String)map.get("app");
			data =iuuMallService.getOrderList(deviceNo,app,page,status);
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
	 * 订单状态的改变
	 * @param operation  delete删除   confirm确认收货  cancel取消  addShop加入购物车
	 * @author zhanglz
	 */
	@RequestMapping("updateOrder")
	public ResponseEntity<String> updateOrder(HttpSession session,
			@RequestParam(value="orderNo") String orderNo,
			@RequestParam(value="operation") String operation){
		String data =null;
		String msg=null;
		int code =SUCESS;
		try {
			Map<String, Object> map = (Map)session.getAttribute("user");
			String deviceNo = (String)map.get("device_no");
			String app = (String)map.get("app");
			boolean result =iNewOrderService.updateOrder(orderNo,deviceNo,app,operation);
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
	 * 订单详情
	 * @author zhanglz
	 */
	@RequestMapping("/orderDetail")
	public String orderDetail(HttpSession session,Model model,String orderNo){
		try {
			Map<String, Object> map = (Map)session.getAttribute("user");
			String deviceNo = (String)map.get("device_no");
			String app = (String)map.get("app");
			String data =iNewOrderService.getOrderInfo(orderNo,deviceNo,app);
			Map<String, Object> result=mapper.readValue(data, HashMap.class);
			model.addAttribute("order", result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "UUMall/order_detail";
	}
	
	/**
	 * 订单详情取消订单
	 * @param 
	 * @author zhanglz
	 */
	@RequestMapping("cancel4Detail")
	public String cancel4Detail(HttpSession session,
			@RequestParam(value="orderNo") String orderNo){
		try {
			String operation = "cancel";
			Map<String, Object> map = (Map)session.getAttribute("user");
			String deviceNo = (String)map.get("device_no");
			String app = (String)map.get("app");
			boolean result =iNewOrderService.updateOrder(orderNo,deviceNo,app,operation);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "UUMall/order_list";
	}
	
	
	/*==========================================地址接口================================================*/
	/**
	 * 查询地址
	 * @author zhanglz
	 */
	@RequestMapping("/getAdress")
	public ResponseEntity<String> getAdress(HttpSession session){//软件标识及版本号
		String data =null;
		String msg=null;
		int code =SUCESS;
		try {
			Map<String, Object> map = (Map)session.getAttribute("user");
			String deviceNo = (String)map.get("device_no");
			String app = (String)map.get("app");
			data =iNewAddressService.getAdress(deviceNo,app);
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
	 * 保存修改地址
	 * @author zhanglz
	 */
	@RequestMapping(value ="/updateAdress")
	public ResponseEntity<String> updateAdress(HttpSession session,
			@RequestParam(value = "addressId",required=false) String addressId,
			@RequestParam(value = "name") String name,
			@RequestParam(value = "mobile") String mobile,
			@RequestParam(value = "province") String province,
			@RequestParam(value = "area") String area,
			@RequestParam(value = "isDefault",required=false) String isDefault){
		String data =null;
		String msg=null;
		int code =SUCESS;
		try {
			if("new".equals(addressId))
				addressId = "";
			if(isDefault==null||!"1".equals(isDefault))
				isDefault="0";
			Map<String, Object> map = (Map)session.getAttribute("user");
			String deviceNo = (String)map.get("device_no");
			String app = (String)map.get("app");
			data =iNewAddressService.updateAdress(deviceNo,app,addressId,name,mobile,province,area,isDefault);
			if(StringUtils.isNotBlank(data)){
				code=SUCESS;
			}else{
				code =FAIL;
				msg="修改地址失败";
			}
		} catch (Exception e) {
			e.printStackTrace();
			code =SERVER_ERR;
		}
		return super.resultInfo(data, code, msg);
	}
	
	/**
	 * 删除地址
	 * @author zhanglz
	 */
	@RequestMapping("/deleteAdress")
	public ResponseEntity<String> deleteAdress(HttpSession session,
			@RequestParam(value="addressId")String addressId){
		String data =null;
		String msg=null;
		int code =SUCESS;
		try {
			Map<String, Object> map = (Map)session.getAttribute("user");
			String deviceNo = (String)map.get("device_no");
			String app = (String)map.get("app");
			boolean flag =iNewAddressService.deleteAdress(deviceNo,app,addressId);
			if(flag){
				code=SUCESS;
			}else{
				code =FAIL;
				msg="删除地址";
			}
		} catch (Exception e) {
			e.printStackTrace();
			code =SERVER_ERR;
		}
		return super.resultInfo(data, code, msg);
	}
	/*==========================================地址接口end==============================================*/
	
	
	
}
