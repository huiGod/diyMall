package cn._51app.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import cn._51app.service.IUUMallService;
import cn._51app.service.impl.UCouponService;
import cn._51app.util.AliSMSUtil;
import cn._51app.util.WxLoginUtil;

//进入抽奖页面
@Controller
@RequestMapping("/UCoupon")
public class UCouponController extends BaseController {
	
	@Autowired
	private UCouponService uCouponService;
	
	@Autowired
	private IUUMallService iuuMallService;
	
	//随机给指定概率的奖项
	@RequestMapping(value="/getLottery")
	public ResponseEntity<String> getLottery(HttpSession session){
		String data =null;
		String msg=null;
		int code =FAIL;
		try {
			Map<String, Object> map = (Map)session.getAttribute("user");
			String deviceNo ="";
			String app = "";
			if(map!=null){
				deviceNo = map.get("device_no")==null?"":(String)map.get("device_no");
				 app =  map.get("app")==null?"":(String)map.get("app");
			}
			String json =this.uCouponService.getLottery(deviceNo,app);
			if(json==null||json.equals("")){
				code =EMPTY;
			}else{
				data=json;
				code=SUCESS;
			}
		} catch (Exception e) {
			e.printStackTrace();
			code =EMPTY;
		}
		return super.resultInfo(data, code, msg);
	}
	
	/**
	 * 记录设备领取的优惠券
	 * @param valid  有效期
	 * @param item 抽中的等级
	 * @param mobile  deviceNo,app,sys
	 * @return
	 */
		@RequestMapping(value ="addCouponUser",method = { RequestMethod.GET })
		public ResponseEntity<String> addCouponUser(
				@RequestParam(value = "valid") String valid,
				@RequestParam(value = "item") String item,
				@RequestParam(value = "mobile") String mobile,
				@RequestParam(value = "phone")String phone
				){
			String data =null;
			String msg=null;
			int code =FAIL;
			try {
//				boolean b =uCouponService.addCouponUser(valid,item,mobile,phone);
				if(true)code=SUCESS;
			} catch (Exception e) {
				e.printStackTrace();
				code=SERVER_ERR;
			}
			return super.resultInfo(data, code, msg);
		}
		
		//发送短信放入session
		@RequestMapping(value="sendMobileUser",method={RequestMethod.POST})
		public ResponseEntity<String> sendMobileUser(HttpSession session,String mobile){
			String msg=null; 
			int code =SERVER_ERR;
			String smscode = (AliSMSUtil.nextInt(100000, 999999))+"";
			try {
				if(AliSMSUtil.sendMsg(mobile, smscode)){
					session.setAttribute("sms", smscode);
					session.setAttribute("smstime", System.currentTimeMillis() + 5 * 60 * 1000);
					code =SUCESS;
				}
			} catch (Exception e) {
				e.printStackTrace();
				code=SERVER_ERR;
			}
			return super.resultInfo(null, code, msg);
		}
		
		/**
		 * 验证短信保存用户手机号
		 * @param session  
		 * @param smscode 验证输入的短信码
		 * @param mobile
		 * @return
		 */
		@RequestMapping(value="addMobileUser",method={RequestMethod.POST})
		public ResponseEntity<String> addMobileUser(HttpSession session,String smscode,String mobile,int id){
			String data =null;
			String msg=null;
			int code =FAIL;
			String sessioncode = session.getAttribute("sms")==null?"":session.getAttribute("sms").toString();
			long smscodeTime = (long)session.getAttribute("smstime");
			long now = System.currentTimeMillis();
			try {
				if(smscode.equals(sessioncode)&&smscodeTime>now){
						this.uCouponService.upUserMobile(id, mobile);
						code=SUCESS;
				}
			} catch (Exception e) {
				e.printStackTrace();
				code=SERVER_ERR;
			}
			return super.resultInfo(data, code, msg);
		}
		
		//查询已经抽的次数
//		@RequestMapping(value="verifyUserCount",method={RequestMethod.GET})
//		public ResponseEntity<String>verifyUserCount(String mobile){
//			String data =null;
//			String msg=null;
//			int code =FAIL;
//			try{
//				String json =this.uCouponService.verifyUserCount(mobile);
//				if(json==null||json.equals("")){
//					code =EMPTY;
//				}else{
//					data=json;
//					code=SUCESS;
//				}
//			}catch(Exception e){
//				e.printStackTrace();
//				code =SERVER_ERR;
//			}
//			return super.resultInfo(data, code, msg);
//		}
		
		//获取获奖名单
		@RequestMapping(value="winnerList")
		public ResponseEntity<String>winnerList(HttpSession session){
			String data =null;
			String msg=null;
			int code =FAIL;
			try{
				Map<String, Object> map = (Map)session.getAttribute("user");
				String deviceNo ="";
				String app = "";
				if(map!=null){
					deviceNo = map.get("device_no")==null?"":(String)map.get("device_no");
					 app =  map.get("app")==null?"":(String)map.get("app");
				}
				String json =this.uCouponService.winnerList(app,deviceNo);
				if(json==null||json.equals("")){
					code =EMPTY;
				}else{
					data=json;
					code=SUCESS;
				}
			}catch(Exception e){
				e.printStackTrace();
				code =EMPTY;
			}
			return super.resultInfo(data, code, msg);
		}
		
		@RequestMapping("/initCoupon")
		public String initCoupon(Model model){
			return "UUMall/lottery";
		}
		
		@RequestMapping("/delCoupon")
		public ResponseEntity<String>delCoupon(){
			String data =null;
			String msg=null;
			int code =FAIL;
			try {
				if(uCouponService.delCoupon()){
					code=SUCESS;
				}
			} catch (Exception e) {
				e.printStackTrace();
				code =SERVER_ERR;
			}
			return super.resultInfo(data, code, msg);
		}
		
		//跳转玫瑰花页面
		@RequestMapping("/toPhone")
		public String toPhone(Model model,String couponId){
			model.addAttribute("couponId", couponId);
			return "UUMall/phone-comfirm";
		}
		
		//未用到，微信跳转页
		@RequestMapping("/gzhCoupon")
		public String gzhCoupon(Model model){
			model.addAttribute("wxUrl", WxLoginUtil.lotteryUrl("kennen"));
			return "UUMall/gzhJump";
		}
		
		/**
		 * 微信登录链接
		 * @param code
		 * @return
		 */
		@RequestMapping("/WxLogin")
		public String WxLogin(HttpServletRequest request,HttpSession session,Model model,String code){
			Map<String, Object> map = iuuMallService.WxLogin(code);
			session.setMaxInactiveInterval(10800);
			session.setAttribute("user", map);
			model.addAttribute("user", map);
			return "redirect:/UCoupon/initCoupon.do";
		}
		
		
}
