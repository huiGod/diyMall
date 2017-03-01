package cn._51app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import cn._51app.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import cn._51app.service.IFocusService;

@Controller
@RequestMapping("/focus/")
public class FocusController extends BaseController{
	
	@Autowired
	private IFocusService fService;
	//清缓存
	@RequestMapping(value ="flushAll",method = { RequestMethod.GET })
	public ResponseEntity<String> flushAll(){
		String data =null;
		String msg=null;
		int code =SUCESS;
		try {
			this.fService.flushAll();
		} catch (Exception e) {
			e.printStackTrace();
			code =SERVER_ERR;
		}
		return super.resultInfo(data, code, msg);
	}
	
	
	/*//模版列表
	@RequestMapping(value ="material/list",method = { RequestMethod.GET })
	public ResponseEntity<String> list(){
		String data =null;
		String msg=null;
		int code =SUCESS;
		try {
			String json =this.fService.queryMaterialByType();
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
	}*/
	
	/**
	 * @author zhanglz
	 *  新的模版统一接口
	 */
	@RequestMapping(value ="material/newlist",method = { RequestMethod.GET })
	public ResponseEntity<String> materialList(){
		String data =null;
		String msg=null;
		int code =SUCESS;
		try {
			String json =this.fService.materialList();
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
	

	@RequestMapping(value ="enter/ios/{v}",method = {RequestMethod.GET})
    public ResponseEntity<String> enterIOS(
    		@PathVariable(value = "v") String v){
		String data =null;
		String msg=null;
		int code =SUCESS;
		try {
			String json =this.fService.isVersionI(v);
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
	
	//版本检查
	@RequestMapping(value = "checkUpdate/{v}", method = RequestMethod.GET)
	public ResponseEntity<String> checkUpdate(
			@PathVariable(value = "v") String v){
		String data =null;
		String msg=null;
		int code =SUCESS;
		try {
			String json =this.fService.checkVersion(v);
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
	
	
	
	//分享页面跳转
	@RequestMapping("toShopSuccess")
	public String toShopSuccess(){
		return "shopSuccess";
	}
	
	@RequestMapping("toshare")
	public String share(){
		return "DIY-share";
	}
	
}
