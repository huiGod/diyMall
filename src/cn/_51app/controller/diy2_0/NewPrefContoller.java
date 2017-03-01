package cn._51app.controller.diy2_0;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn._51app.controller.BaseController;
import cn._51app.service.diy2_0.INewPrefService;

/**
 * 
 * 新人特惠
 *
 */
@Controller
@RequestMapping("/newPref")
public class NewPrefContoller extends BaseController {
	
	private static final String NOT_LOGIN = "0"; //没有登录
	private static final String RECEIVED= "1"; // 领取成功
	private static final String SUCCESSED = "2"; // 领取成功
	private static final String FAILED = "3"; // 领取失败
	
	@Autowired 
	private INewPrefService newPrefService;

	
	@RequestMapping("/getLoad")
	public ResponseEntity<String> getLoad(Integer userId) {

		String data = null;
		String msg = null;
		int code = SUCESS;
		
		try {
			String json = newPrefService.getLoad(userId);
			if(json==null||json.equals("")||json.length()<=2){
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
	
	@RequestMapping("/receivedByUser")
	public ResponseEntity<String> receivedByUser(@RequestParam(value = "userId") Integer userId) {
		String data = null;
		String msg = null;
		int code = SUCESS;
		
		try {
			if (newPrefService.isLogin(userId)) {
				if (newPrefService.isGetCoupon(userId)) {
					data = RECEIVED;
					msg = "您已经领取了";
				}else {
					if (newPrefService.getCouponByUser(userId)) {
						data = SUCCESSED;
						msg = "领取成功";
					}else {
						data = FAILED;
						msg = "领取失败";
					}
				}
			}else {
				data = NOT_LOGIN;
				msg = "未登录";
			}
		} catch (Exception e) {
			e.printStackTrace();
			code =SERVER_ERR;
		}
		
		return super.resultInfo(data, code, msg);
	}
}
