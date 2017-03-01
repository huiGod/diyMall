package cn._51app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import cn._51app.service.INewOrderService;
import cn._51app.service.IUserService;

@Controller
public class NewUserController extends BaseController{
	@Autowired
	private INewOrderService iNewOrderService;
	
	@Autowired
	private IUserService userService;
	
	/**
	 * tengh 2016年6月7日 下午1:38:34
	 * @param app
	 * @param deviceNo
	 * @param version
	 * @return
	 * TODO 统计diy用户信息
	 */
	@RequestMapping(value ="infoUser",method = { RequestMethod.GET })
	public ResponseEntity<String> infoUser(@RequestParam(value="app") String app,@RequestParam(value="deviceNo") String deviceNo,@RequestParam(value="version") String version,@RequestParam(value="deviceToken",required=false)String deviceToken){
		String data =null;
		String msg=null;
		int code =SUCESS;
		try {
			boolean result =iNewOrderService.infoUser(deviceNo,app,version,deviceToken);
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
	
	
	@RequestMapping("getUser")
	public ResponseEntity<String> getUser(@RequestParam(value="app") String app,
			@RequestParam(value="deviceNo") String deviceNo){
		String data =null;
		String msg=null;
		int code =SUCESS;
		try {
			String json = userService.getUser(deviceNo, app);
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
	
	
	
}
