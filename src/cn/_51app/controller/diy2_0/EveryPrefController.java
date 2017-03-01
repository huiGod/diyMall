package cn._51app.controller.diy2_0;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn._51app.controller.BaseController;
import cn._51app.service.diy2_0.IEveryPrefService;

/**
 * 每日优选
 * @author yuanqi
 *
 */

@Controller
@RequestMapping("/everyPref")
public class EveryPrefController extends BaseController {
	
	@Autowired 
	private IEveryPrefService everyPrefService;

	@RequestMapping("/load")
	public ResponseEntity<String> load() {

		String data = null;
		String msg = null;
		int code = SUCESS;
		
		try {
			data = everyPrefService.load();
		} catch (Exception e) {
			code =SERVER_ERR;
		}
		
		return super.resultInfo(data, code, msg);
	}
	
	@RequestMapping("/getActivities")
	public ResponseEntity<String> getActivities(){
		String data = null;
		String msg = null;
		int code = SUCESS;
		
		try {
			data = everyPrefService.getActivities();
		} catch (Exception e) {
			code =SERVER_ERR;
		}
		return super.resultInfo(data, code, msg);
	}
}
