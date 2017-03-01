package cn._51app.controller.diy2_0;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import cn._51app.controller.BaseController;
import cn._51app.entity.CutPriceCoupon;
import cn._51app.service.diy2_0.ICutPriceService;
import cn._51app.util.JSONUtil;

@Controller
@RequestMapping("/cutPrice")
public class CutPriceController extends BaseController{
	
	@Autowired
	private ICutPriceService iCutPriceService;
	
	/**
	 * tengh 2016年11月23日 下午8:25:59
	 * @param cutPriceCoupon
	 * @return
	 * TODO 添加砍价券信息
	 */
	@RequestMapping(value="/add",method=RequestMethod.POST)
	public ResponseEntity<String> addCutCoupon(@ModelAttribute CutPriceCoupon cutPriceCoupon){
		String data =null;
		String msg=null;
		int code =SUCESS;
		try {
			boolean check=this.iCutPriceService.addCutCoupon(cutPriceCoupon);
			if(!check){
				code=FAIL;
			}
		} catch (Exception e) {
			e.printStackTrace();
			code =SERVER_ERR;
		}
		return super.resultInfo(data, code, msg);
	}
	
	/**
	 * tengh 2016年11月25日 上午10:46:12
	 * @param cutPriceCoupon
	 * @return
	 * TODO 修改砍价券信息
	 */
	@RequestMapping(value="/edit",method=RequestMethod.POST)
	public ResponseEntity<String> editCutCoupon(@ModelAttribute CutPriceCoupon cutPriceCoupon){
		String data =null;
		String msg=null;
		int code =SUCESS;
		try {
			boolean check=this.iCutPriceService.editCutCoupon(cutPriceCoupon);
			if(!check){
				code=FAIL;
			}
		} catch (Exception e) {
			e.printStackTrace();
			code =SERVER_ERR;
		}
		return super.resultInfo(data, code, msg);
	}
	
	/**
	 * tengh 2016年11月25日 上午10:57:12
	 * @param cutPriceCoupon
	 * @return
	 * TODO 删除砍价券
	 */
	@RequestMapping(value="/del",method=RequestMethod.POST)
	public ResponseEntity<String> delCutCoupon(@RequestParam(value="id")String id){
		String data =null;
		String msg=null;
		int code =SUCESS;
		try {
			boolean check=this.iCutPriceService.delCutCoupon(id);
			if(!check){
				code=FAIL;
			}
		} catch (Exception e) {
			e.printStackTrace();
			code =SERVER_ERR;
		}
		return super.resultInfo(data, code, msg);
	}
	
	/**
	 * tengh 2016年11月24日 下午1:44:38
	 * @param userId
	 * @param mobile
	 * @return
	 * TODO 我的砍价券列表
	 */
	@RequestMapping(value="/myCutCoupon")
	public ResponseEntity<String> myCutCoupon(@RequestParam(value="userId") String userId){
		String data =null;
		String msg=null;
		int code =SUCESS;
		try {
			data=this.iCutPriceService.myCutCoupon(userId);
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
	 * tengh 2016年11月24日 下午3:03:14
	 * @param userId
	 * @return
	 * TODO 抽中奖
	 */
	@RequestMapping(value="/getCutCoupon")
	public ResponseEntity<String> getCutCoupon(@RequestParam(value="userId")String userId){
		String data =null;
		String msg=null;
		int code =SUCESS;
		try {
			Map<String, Object> result=this.iCutPriceService.getCutCoupon(userId);
			boolean flag=(Boolean)result.get("flag");
			if(!flag){
				msg=(String)result.get("msg");
				code=FAIL;
			}else{
				data=JSONUtil.convertObjectToJSON(result);
			}
		} catch (Exception e) {
			e.printStackTrace();
			code =SERVER_ERR;
		}
		return super.resultInfo(data, code, msg);
	}
	
	/**
	 * tengh 2016年12月23日 下午3:31:22
	 * @param userId
	 * @return
	 * TODO 查用户是否能够抽奖
	 */
	@RequestMapping(value="/checkCutCoupon")
	public ResponseEntity<String> checkCutCoupon(@RequestParam(value="userId")String userId){
		String data =null;
		String msg=null;
		int code =SUCESS;
		try {
			Map<String, Object> result=this.iCutPriceService.checkCutCoupon(userId);
			boolean flag=(Boolean)result.get("flag");
			if(!flag){
				msg=(String)result.get("msg");
				code=FAIL;
			}else{
				data=JSONUtil.convertObjectToJSON(result);
			}
		} catch (Exception e) {
			e.printStackTrace();
			code =SERVER_ERR;
		}
		return super.resultInfo(data, code, msg);
	}
	
	/**
	 * tengh 2016年11月24日 下午4:53:27
	 * @return
	 * TODO 砍价券信息
	 */
	@RequestMapping(value="/coupons")
	public ResponseEntity<String> coupons(){
		String data =null;
		String msg =null;
		int code =SUCESS;
		try {
			data=this.iCutPriceService.coupons();
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
