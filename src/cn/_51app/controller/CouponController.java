package cn._51app.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import cn._51app.service.ICouponService;

@Controller
@RequestMapping("/coupon/")
public class CouponController extends BaseController{
	
	@Autowired
	private ICouponService service;
	
	@RequestMapping(value ="couponTo",method = { RequestMethod.GET })
	public ResponseEntity<String> couponTo(){
		String data =null;
		String msg=null;
		int code =SUCESS;
		try {
			String json =this.service.couponTo();
			if(json==null||json.equals("")){
				code =EMPTY;
			}else{
				data=json;
			}
		} catch (Exception e) {
			e.printStackTrace();
			code =SERVER_ERR;
		}
		return super.resultInfo(data, code, msg);
	}
	
	//是否弹出领取优惠券h5
	@RequestMapping(value ="drawSwitch/{deviceNo}/{app}",method = { RequestMethod.GET })
	public ResponseEntity<String> drawSwitch(
			@PathVariable(value = "deviceNo") String deviceNo,
			@PathVariable(value = "app") String app){
		String data =null;
		String msg=null;
		int code =SUCESS;
		try {
			String json =this.service.drawSwitch(deviceNo,app);
			if(json==null||json.equals("")){
				code =EMPTY;
			}else{
				data=json;
			}
		} catch (Exception e) {
			e.printStackTrace();
			code =SERVER_ERR;
		}
		return super.resultInfo(data, code, msg);
	}
	
	
	//进入抽奖页面
	@RequestMapping(value ="getDrawLAndV",method = { RequestMethod.GET })
	public String getDrawLAndV(Model model,String mobile){
		try {
			String dlvStr =this.service.getDrawLevelAndValid();
			if(dlvStr!=null&&!dlvStr.equals("")){
				Map<String,Object> m=new ObjectMapper().readValue(dlvStr, new TypeReference<HashMap<String,Object>>(){});
				m.put("mobile", mobile);
				model.addAttribute("dlvM", m);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "draw";
	}
	
	//记录设备领取的优惠券
	@RequestMapping(value ="addCouponUser",method = { RequestMethod.GET })
	public ResponseEntity<String> addCouponUser(
			@RequestParam(value = "valid") String valid,
			@RequestParam(value = "item") String item,
			@RequestParam(value = "mobile") String mobile
			){
		
		String data =null;
		String msg=null;
		int code =FAIL;
		
		try {
			boolean b =service.addCouponUser(valid,item,mobile);
			if(b) code=SUCESS;
		} catch (Exception e) {
			e.printStackTrace();
			code=SERVER_ERR;
		}
		return super.resultInfo(data, code, msg);
	}
	
	
	/**
	 * tengh 2016年5月17日 下午9:23:37
	 * @param deviceNo
	 * @param app
	 * @return
	 * TODO 获取账号期限内的可用优惠券
	 */
	@RequestMapping(value ="getCoupont",method = { RequestMethod.GET })
	public ResponseEntity<String> getCoupon(
			@RequestParam(value = "deviceNo") String deviceNo,
			@RequestParam(value = "app") String app){
		String data =null;
		String msg=null;
		int code =SUCESS;
		try {
			System.out.println(deviceNo+"|"+app);
			data =service.getCoupon(deviceNo,app);
			if(StringUtils.isBlank(data)){
				code=EMPTY;
			}
		} catch (Exception e) {
			e.printStackTrace();
			code =SERVER_ERR;
		}
		return super.resultInfo(data, code, msg);
	}

}
