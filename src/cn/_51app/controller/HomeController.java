package cn._51app.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import cn._51app.service.IGoodsInfoService;
import cn._51app.service.IFocusService;
import cn._51app.service.INewOrderService;

@Controller
@RequestMapping("/home/")
public class HomeController extends BaseController {

	//获取订单号
	//((((img_url.replace(".", "#")).split("#"))[0]).split("/")[1])
	
	
	// 0 false ;1 true
	
	
	
	@Autowired
	private IGoodsInfoService service;
	
	@Autowired
	private IFocusService fService;
	
	@Autowired
	private INewOrderService iNewOrderService;
	
	/**>>Faster
	 * 
	 * 首页h5及设备注册
	 * @param dn 设备号
	 * @param app
	 * @param model 返回的视图对象
	 * @return index页面
	 */
	@RequestMapping(value ="initHome/{dn}/{app}",method = { RequestMethod.GET })
	public String getGoodsPageInit(
			//获取url动态参数
			@PathVariable(value = "dn") String dn,//设备号
			@PathVariable(value = "app") String app,
			Model model){
		try {
			//插入数据到diy_device_user表
			this.fService.insertDevice(dn,app.split("&")[0]);
			//设置页码查询
			Map<String,Object> paramMap =new HashMap<String,Object>();
			//设置以0为起始页码
			paramMap.put("startIndex", 0);
			//分页查询首页商品数据
			String goodsListStr = service.getGoodsInfoList(paramMap);
			//转换json数据
			List<Map<String,Object>> lm=new ObjectMapper().readValue(goodsListStr, new TypeReference<ArrayList<HashMap<String,Object>>>(){});
			//设置跳转页面数据
			model.addAttribute("list", lm);
			if(app.contains("&"))
			model.addAttribute("vapp",app.split("&")[1]);
			model.addAttribute("deviceNo",dn);
			model.addAttribute("app",app.split("&")[0]);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		//跳转到index页面
		return "index";
	}
	
	/**
	 * @author zhanglz
	 * @return 首页跳转
	 */
	@RequestMapping("tohome")
	public String tohome(){
		return "diy-page";
	}
	
	/**
	 * @author zhanglz
	 * @param page:当前页
	 * @return 首页
	 */
	@RequestMapping(value="home/{dn}/{app}",method = { RequestMethod.GET })
	public ResponseEntity<String> home(//获取url动态参数
			@PathVariable(value = "dn") String dn,//设备号
			@PathVariable(value = "app") String app
			){
		
		//返回数据默认为null
		String data =null;
		//返回错误信息，默认为null
		String msg=null;
		//设置错误码
		int code =SUCESS;
	try {	
		app=app.split("-")[0];
		//插入数据到diy_device_user表
		this.fService.insertDevice(dn,app);
		//设置页码查询
		Map<String,Object> paramMap =new HashMap<String,Object>();
		//设置以0为起始页码
		paramMap.put("startIndex", 0);
		//分页查询首页商品数据
		String json = service.getGoodsInfoList(paramMap);
		//json为空错误码
		if(json==null||json.equals("")){
			code =EMPTY;
		//正常返回数据
		}else{
			data=json;
		}
		
	} catch (Exception e) {
		e.printStackTrace();
		code =SERVER_ERR;
	}
	return super.resultInfo(data, code, msg);
}


	
	
	/**
	 * @author zhanglz
	 * @return 精品汇跳转
	 */

	@RequestMapping("tonice")
	public String tonice(){
		return "nice-together";
	}
	
	/**>>Faster
	 * 
	 * @author zhanglz
	 * @param page:当前页
	 * @return 精品汇
	 */
	@RequestMapping("nice")
	public ResponseEntity<String> nice(Integer page){
		//返回数据默认为null
		String data =null;
		//返回错误信息，默认为null
		String msg=null;
		//设置错误码
		int code =SUCESS;
		//设置页码为空默认为1
		if(page==null)
			page=1;
		try {
			//查询json数据
			String json =this.service.nice(page);
			//json为空错误码
			if(json==null||json.equals("")){
				code =EMPTY;
			//正常返回数据
			}else{
				data=json;
			}
		//数据异常错误码
		} catch (Exception e) {
			e.printStackTrace();
			code =SERVER_ERR;
		}
		//返回可跨域json数据格式
		return super.resultInfo(data, code, msg);
	}
	
	@RequestMapping("tohomeV2")
	public String tohomeV2(Model model){
		List<Map<String, Object>> plm;
		try {
			//满减滚动条
			String privilege = iNewOrderService.getPrivilege();
			plm = new ObjectMapper().readValue(privilege, new TypeReference<ArrayList<Map<String,Object>>>(){});
			model.addAttribute("privilege", plm);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return "indexV2";
	}
	
	@RequestMapping("homeV2")
	public ResponseEntity<String> homeV2(Model model){
		String data =null;
		String msg=null;
		int code =SUCESS;
		try {
			//查询json数据
			String json =this.service.homeV2();
			//json为空错误码
			if(json==null||json.equals("")){
				code =EMPTY;
			//正常返回数据
			}else{
				data=json;
			}
		//数据异常错误码
		} catch (Exception e) {
			e.printStackTrace();
			code =SERVER_ERR;
		}
		return super.resultInfo(data, code, msg);
	}
}
