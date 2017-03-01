package cn._51app.controller.diy2_0;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn._51app.controller.BaseController;
import cn._51app.service.diy2_0.impl.CouponService2;

@Controller
@RequestMapping("/coupon2")
public class CounponController2 extends BaseController {
	
	@Autowired
	private CouponService2 couponService2;
	
	/**
	 * TODO 个人优惠券
	 * @param deviceNo
	 * @param app
	 * @return
	 */
	@RequestMapping("/couponList")
	public ResponseEntity<String> couponList(@RequestParam(value="deviceNo")String deviceNo,@RequestParam(value="app")String app){
		int code=SUCESS;
		String data=null;
		String msg=null;
		try {
			System.out.println(deviceNo+"|"+app);
			data=couponService2.couponList(deviceNo, app);                                                                     
		} catch (Exception e) {
			e.printStackTrace();
			code=SERVER_ERR;
		}
		return super.resultInfo(data, code, msg);
	}
	
	/**
	 * tengh 2016年12月2日 上午11:25:52
	 * @param userId
	 * @param couponId
	 * @return
	 * TODO 领取商家优惠券
	 */
	@RequestMapping("/getCoupon")
	public ResponseEntity<String> getCoupon(@RequestParam(value="userId")String userId,@RequestParam(value="couponId")String couponId,@RequestParam(value="sys")String sys){
		int code=SUCESS;
		String data=null;
		String msg=null;
		try {
			boolean check=couponService2.getCoupon(userId,couponId,sys);
			if(!check){
				code=FAIL;
				msg="您已经领取改优惠券~";
			}
		} catch (Exception e) {
			e.printStackTrace();
			code=SERVER_ERR;
		}
		return super.resultInfo(data, code, msg);
	}
	
	/**
	 * TODO 个人优惠券（id版）
	 * @param userId
	 * @return
	 */
	@RequestMapping("/couponListForId")
	public ResponseEntity<String> couponListForId(@RequestParam(value="userId")String userId){
		int code=SUCESS;
		String data=null;
		String msg=null;
		try {
			data=couponService2.couponListForId(userId);                                                                     
		} catch (Exception e) {
			e.printStackTrace();
			code=SERVER_ERR;
		}
		return super.resultInfo(data, code, msg);
	}
	
	/**
	 * tengh 2016年11月30日 下午6:14:06
	 * @param userId
	 * @return
	 * TODO 查看所有优惠券
	 */
	@RequestMapping("/getCouponList")
	public ResponseEntity<String> getCouponList(@RequestParam(value="userId")String userId,@RequestParam(value="sys")String sys){
		int code=SUCESS;
		String data=null;
		String msg=null;
		try {
			data=couponService2.getCouponList(userId,sys);                                                                     
		} catch (Exception e) {
			e.printStackTrace();
			code=SERVER_ERR;
		}
		return super.resultInfo(data, code, msg);
	}
	
	/**
	 * tengh 2016年11月30日 下午7:28:38
	 * @param userId
	 * @param sys
	 * @return
	 * TODO 查询出可用优惠券
	 */
	@RequestMapping("/checkCouponList")
	public ResponseEntity<String> checkCouponList(@RequestParam(value="userId")String userId,@RequestParam(value="sys")String sys){
		int code=SUCESS;
		String data=null;
		String msg=null;
		try {
			data=couponService2.checkCouponList(userId,sys);                                                                     
		} catch (Exception e) {
			e.printStackTrace();
			code=SERVER_ERR;
		}
		return super.resultInfo(data, code, msg);
	}
	
	
	/**
	 * TODO 领券中心优惠券
	 * @return
	 */
	@RequestMapping("/center")
	public ResponseEntity<String>couponCenter(@RequestParam("deviceNo")String deviceNo,@RequestParam("app")String app){
		int code=SUCESS;
		String data=null;
		String msg=null;
		try {
			data=couponService2.couponCenterType(deviceNo,app,"2");
		} catch (Exception e) {
			e.printStackTrace();
			code=SERVER_ERR;
		}
		return super.resultInfo(data, code, msg);
	}
	
	/**
	 * TODO 领券中心优惠券(type版本)
	 * @return
	 */
	@RequestMapping("/centerType")
	public ResponseEntity<String>couponCenterType(@RequestParam("deviceNo")String deviceNo,@RequestParam("app")String app,@RequestParam("type")String type){
		int code=SUCESS;
		String data=null;
		String msg=null;
		try {
			System.out.println(deviceNo+"|||"+app);
			data=couponService2.couponCenterType(deviceNo,app,type);
		} catch (Exception e) {
			e.printStackTrace();
			code=SERVER_ERR;
		}
		return super.resultInfo(data, code, msg);
	}
	
	/**
	 * TODO 领券中心优惠券(id版）
	 * @param userId  用户id
	 * @return
	 */
	@RequestMapping("/centerForId")
	public ResponseEntity<String>couponCenterForId(@RequestParam("userId")String userId){
		int code=SUCESS;
		String data=null;
		String msg=null;
		try {
			data=couponService2.couponCenterForId(userId);
		} catch (Exception e) {
			e.printStackTrace();
			code=SERVER_ERR;
		}
		return super.resultInfo(data, code, msg);
	}
	
	
	
	/**
	 * TODO 用户优惠券添加
	 * @param valid  优惠券有效期
	 * @param deviceNo
	 * @param app  
	 * @param sys   1 IOS 2 Android
	 * @param id   优惠券id
	 * @return
	 */
	@RequestMapping("/addCoupon")
	public ResponseEntity<String>addCoupon(String valid,String deviceNo,String app,String sys,String id){
		int code=SUCESS;
		String data=null;
		String msg=null;
		try {
			if(couponService2.addCoupon(valid, deviceNo, app, sys, id)){
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			code=SERVER_ERR;
		}
		return super.resultInfo(data, code, msg);
	}
	
	/**
	 * TODO 用户添加优惠券（id版）
	 * @param valid
	 * @param userId
	 * @param sys
	 * @param id
	 * @return
	 */
	@RequestMapping("/addCouponForId")
	public ResponseEntity<String>addCouponForId(String valid,String userId,String sys,String id){
		int code=SUCESS;
		String data=null;
		String msg=null;
		try {
			if(couponService2.addCouponForId(valid,userId, sys, id)){
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			code=SERVER_ERR;
		}
		return super.resultInfo(data, code, msg);
	}
	
	/**
	 * TODO 验证码获取优惠券
	 * @param code
	 * @return
	 */
	@RequestMapping("/codeCoupon")
	public ResponseEntity<String>CodeCoupon(@RequestParam("code")String code,@RequestParam("deviceNo")String deviceNo,@RequestParam("app")String app){
		int sue=SUCESS;
		String data=null;
		String msg=null;
		try {
			data=couponService2.getCodeCoupon(code, deviceNo, app);          
			if(data==null){
				sue=FAIL;
			}else if(data.equals("isGet")){
				data=null;
				sue=301;
			}else if(data.equals("error")){
				data=null;
				sue=302;
			}else if(data.equals("had")){
				data=null;
				sue=303;
			}
		} catch (Exception e) {
			e.printStackTrace();
			sue=SERVER_ERR;
		}
		return super.resultInfo(data, sue, msg);
	}
	
	@RequestMapping("/codeCouponForId")
	public ResponseEntity<String>CodeCoupon(@RequestParam("code")String code,@RequestParam("userId")String userId){
		int sue=SUCESS;
		String data=null;
		String msg=null;
		try {
			data=couponService2.getCodeCouponForId(code,userId);          
			if(data==null){
				sue=FAIL;
			}else if(data.equals("isGet")){
				data=null;
				sue=301;
			}else if(data.equals("error")){
				data=null;
				sue=302;
			}else if(data.equals("had")){
				data=null;
				sue=303;
			}
		} catch (Exception e) {
			e.printStackTrace();
			sue=SERVER_ERR;
		}
		return super.resultInfo(data, sue, msg);
	}
	
	/**
	 * TODO 获取领券中心优惠券
	 * @param deviceNo
	 * @param app
	 * @param id   优惠券id
	 * @param valid
	 * @return
	 */
	@RequestMapping("/getCenterCoupon")
	public ResponseEntity<String>getCenterCoupon(@RequestParam("deviceNo")String deviceNo,@RequestParam("app")String app,@RequestParam("id")String id,@RequestParam("valid")String valid){
		int sue=SUCESS;
		String data=null;
		String msg=null;
		try {
			data=couponService2.getCenterCoupon(deviceNo,app,id,valid);
			if(data==null){
				sue=FAIL;
			}else{
				//成功屏蔽data
				data=null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			sue=SERVER_ERR;
		}
		return super.resultInfo(data, sue, msg);
	}
	
	/**
	 * TODO 获取领券中心优惠券
	 * @param deviceNo
	 * @param app
	 * @param id   优惠券id
	 * @param valid
	 * @return
	 */
	@RequestMapping("/getCenterCouponType")
	public ResponseEntity<String>getCenterCouponType(@RequestParam("deviceNo")String deviceNo,@RequestParam("app")String app,@RequestParam("id")String id,@RequestParam("valid")String valid,@RequestParam("type")String type){
		int sue=SUCESS;
		String data=null;
		String msg=null;
		try {
			data=couponService2.getCenterCouponType(deviceNo,app,id,valid,type);
			if(data==null){
				sue=FAIL;
			}else{
				//成功屏蔽data
				data=null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			sue=SERVER_ERR;
		}
		return super.resultInfo(data, sue, msg);
	}
	
	/**
	 * TODO 领券中心（id版）
	 * @param userId
	 * @param id
	 * @param valid
	 * @return
	 */
	@RequestMapping("/getCenterCouponForId")
	public ResponseEntity<String>getCenterCouponForId(@RequestParam("userId")String userId,@RequestParam("id")String id,@RequestParam("valid")String valid){
		int sue=SUCESS;
		String data=null;
		String msg=null;
		try {
			data=couponService2.getCenterCouponForId(userId,id,valid);
			if(data==null){
				sue=FAIL;
			}else{
				//成功屏蔽data
				data=null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			sue=SERVER_ERR;
		}
		return super.resultInfo(data, sue, msg);
	}
	
	/**
	 * TODO 根据type获取优惠券的列表
	 * @param type
	 * @param userId
	 * @return
	 */
	@RequestMapping("/getAiSiAndOldCouponList")
	public ResponseEntity<String> getAiSiAndOldCouponList(String type,String userId){
		String data=null;
		String msg=null;
		int code=SUCESS;
		try {
			data=couponService2.getAiSiAndOldCouponList(type,userId);
			if(data==null){
				code=FAIL;
			}
		} catch (Exception e) {
			e.printStackTrace();
			code=SERVER_ERR;
		}
		return super.resultInfo(data, code, msg);
	}
	
	/**
	 * TODO 领券中心页面
	 * @param deviceNo
	 * @param app
	 * @param model
	 * @return
	 */
	@RequestMapping("/getCenterPage")
	public String getCenterPage(@RequestParam("deviceNo")String deviceNo,@RequestParam("app")String app,Model model){
		String prefix="http://api.diy.51app.cn/diyMall/";
		String html=prefix+"coupon2/center.do?deviceNo="+deviceNo+"&app="+app;
		String html2=prefix+"coupon2/codeCoupon.do?deviceNo="+deviceNo+"&app="+app;
		model.addAttribute("url1", html);
		model.addAttribute("url2", html2);
		return "diy2_0/coupons";
	}
	
	/**
	 * TODO领券中心页面（id版）
	 * @param userId
	 * @param model
	 * @return
	 */
	@RequestMapping("/getCenterPageForId")
	public String getCenterPage(@RequestParam("userId")String userId,Model model){
		String prefix="http://api.diy.51app.cn/diyMall/";
		String html=prefix+"coupon2/centerForId.do?userId="+userId;
		String html2=prefix+"coupon2/codeCouponForId.do?userId="+userId;
		model.addAttribute("url1", html);
		model.addAttribute("url2", html2);
		return "diy2_0/coupons";
	}
	
	
	/**
	 * TODO领券中心页面（type版）
	 * @param userId
	 * @param model
	 * @return
	 */
	@RequestMapping("/getCenterTypePage")
	public String getCenterTypePage(Model model,@RequestParam("app")String app,@RequestParam("deviceNo")String deviceNo){
		model.addAttribute("deviceNo", deviceNo);
		model.addAttribute("app", app);
		return "hd-both11";
	}
	
	/**
	 * TODO hd-act1115.jsp
	 * @return
	 */
	@RequestMapping("/actPage")
	public String actPage(){
		return "hd-act1115";
	}
	
	/**
	 * TODO 优惠券页面
	 * @return
	 */
	@RequestMapping("/getcoupon")
	public String getcoupon(){
		return "diy2_5/getcoupon";
	}
	
	/**
	 * TODO 爱思助手和老用户优惠券
	 * @return
	 */
	@RequestMapping("/getAiSiAndOld/{id}")
	public String getAiSiAndOld(@PathVariable(value="id")String id){
		return "diy2_5/getcoupon-exc";
	}
		
	/**
	 * TODO  获取用户id跳优惠券页面
	 */
	@RequestMapping("/getAiSiAndOldUser")
	public String getAiSiAndOldUser(Model model,String t){
		model.addAttribute("location", "goodsInfo://?islogin=true&url=http://api.diy.51app.cn/diyMall/coupon2/getAiSiAndOld/"+t+".do?");
		return "diy2_5/direct";
	}
		
}
 