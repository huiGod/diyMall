package cn._51app.controller.diy2_0;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import cn._51app.controller.BaseController;
import cn._51app.service.diy2_0.IActiviteService;
@Controller
@RequestMapping("/activite/")
public class ActiviteCartController extends BaseController{
	
	@Autowired
	private IActiviteService iActiviteService;
	
	/**
	 * tengh 2016年5月19日 下午5:06:57
	 * @param deviceNo
	 * @param app
	 * @param page
	 * @return
	 * TODO 活动列表   type给测试使用  正式的活动只可能有一个
	 */
	@RequestMapping(value ="list",method = { RequestMethod.GET })
	public ResponseEntity<String> list(
			@RequestParam(value = "type",required=false,defaultValue="0") String type
			){
		String data =null;
		String msg=null;
		int code =SUCESS;
		try { 
			data =this.iActiviteService.activiteList(type);
			if(StringUtils.isBlank(data)){
				code =EMPTY;
				msg="暂时没有优惠活动";
			}
		} catch (Exception e) {
			e.printStackTrace();
			code =SERVER_ERR;
		}
		return super.resultInfo(data, code, msg);
	}
	
}
