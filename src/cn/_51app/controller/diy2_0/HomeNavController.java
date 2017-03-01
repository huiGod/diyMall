package cn._51app.controller.diy2_0;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import cn._51app.controller.BaseController;
import cn._51app.service.diy2_0.IHomeNavService;
import cn._51app.service.diy2_0.IndentService;
import cn._51app.service.diy2_0.impl.UserWorksService;

@Controller
@RequestMapping("/homeNav2")
public class HomeNavController extends BaseController {
	
	@Autowired
	private IHomeNavService iHomeNavService;

	@Autowired
	private IndentService indentService;
	
	@Autowired
	private UserWorksService userWorksService;

	/**
	 * TODO 2.0首页数据
	 * @return
	 */
	@RequestMapping("/home")
	public ResponseEntity<String>homeNav2(){
		String data=null;
		int code=FAIL;
		String msg="操作失败";
		try {
			data=iHomeNavService.getHomeNav();
			code=SUCESS;
			msg="操作成功";
		} catch (Exception e) {
			e.printStackTrace();
			code=SERVER_ERR;
			msg="服务器错误";
		}
		return super.resultInfo(data, code, msg);
	}
	
	@RequestMapping("/toSpecial")
	public ResponseEntity<String>toSpecial(@RequestParam(value="navId")String navId){
		String data=null;
		int code=FAIL;
		String msg="操作失败";
		try {
			data=iHomeNavService.toSpecial(navId);
			code=SUCESS;
			msg="操作成功";
			if(data==null || data.equals("")){
				code=EMPTY;
			}
		} catch (Exception e) {
			e.printStackTrace();
			code=SERVER_ERR;
			msg="服务器错误";
		}
		return super.resultInfo(data, code, msg);
	}
	
	/**
	 * 专题页面
	 * @param model
	 * @param nav_id 导航条id
	 * @param type
	 * @return
	 */
	@RequestMapping("/special")
	public String toSpecial(Model model,String navId){
		try {
			String json = ""; 
				json=iHomeNavService.special(Integer.parseInt(navId));
			List<Map<String,Object>> plm=null;
			if(json!=null)
			 plm=new ObjectMapper().readValue(json, new TypeReference<ArrayList<Map<String,Object>>>(){});
			model.addAttribute("special", plm);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "diy2_0/special";
	}
	
	/**
	 * TODO 发现列表
	 * @return
	 */
	@RequestMapping("/findList")
	public String findList(){
		return "diy2_0/findList";
	}
	
	/**
	 * TODO 发现文章
	 * @return
	 */
	@RequestMapping("/findArticle/{id}")
	public String findArticle(@PathVariable("id")String id,Model model){
		String prefix="http://api.diy.51app.cn/diyMall/commodity/";
		model.addAttribute("url",prefix+"findDetail/"+id+".do");
		return "diy2_0/findArticle";
	}
	
	/**
	 * TODO 首页
	 * @return
	 */
	@RequestMapping("/homePage/{dn}/{app}")
	public String homePage(Model model,@PathVariable("dn")String dn,@PathVariable("app")String app){
		try{
			app=app.split("-")[0];
			//插入数据到diy_device_user表
			this.userWorksService.insertUser(dn, app);
			//满减滚动条
			String privilege = indentService.getPrivilege();
			if(privilege!=null){
			List<Map<String,Object>> plm=new ObjectMapper().readValue(privilege, new TypeReference<ArrayList<Map<String,Object>>>(){});
			model.addAttribute("privilege", plm);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return "diy2_0/index";
	}
	
	/**
	 * TODO 版权声明
	 * @return
	 */
	@RequestMapping("/copyRight")
	public String copyRight(){
		return "diy2_0/copyRight";
	}
	
	/**
	 * TODO 介绍
	 * @return
	 */
	@RequestMapping("/intro")
	public String intro(){
		return "diy2_0/intro";
	}

}
