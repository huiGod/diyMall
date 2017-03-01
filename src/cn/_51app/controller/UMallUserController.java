package cn._51app.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.databind.ObjectMapper;

import cn._51app.service.IUUMallService;
import cn._51app.util.AliSMSUtil;
import cn._51app.util.QQLoginUtil;


@Controller  
@RequestMapping("/UMallUser")
public class UMallUserController extends BaseController{
	
	@Autowired
	private IUUMallService iuuMallService;
	
	
	/**
	 * 绑定手机号发送验证码
	 * @author zhanglz 
	 * @return 
	 */
	@RequestMapping("/bindingSendMsg")
	public ResponseEntity<String> bindingSendMsg(HttpSession session,String mobile){
		String msg=null; 
		int code =SERVER_ERR;
		if(iuuMallService.haveMobile(mobile)){
			code=FAIL;
			msg="该手机号已经绑定其他账号！";
			return super.resultInfo(null, code, msg);
		}
		String smscode = (AliSMSUtil.nextInt(100000, 999999))+"";
		try {
			if(iuuMallService.sendMsg(mobile, smscode)){
				session.setAttribute("smscode", smscode);
				session.setAttribute("smscodeTime", System.currentTimeMillis() + 5 * 60 * 1000);
				session.setAttribute("bindMobile", mobile);
				code =SUCESS;
			}
		} catch (Exception e) {
			e.printStackTrace();
			code=SERVER_ERR;
		}
		return super.resultInfo(null, code, msg);
	}
	
	/**
	 * 绑定手机号
	 * @author zhanglz 
	 * @return 
	 */
	@RequestMapping("/binding")
	public ResponseEntity<String> binding(HttpSession session,String mobile){
		String msg=null; 
		int code =SERVER_ERR;
		try {
			String bindMobile = (String)session.getAttribute("bindMobile");
			if(bindMobile.equals("mobile")){
				Map<String, Object> userMap = (Map)session.getAttribute("user");
				Integer userId = (Integer)userMap.get("id");
				iuuMallService.binding(userId, mobile);
				code=SUCESS;
			}else{
				code=FAIL;
			}
		} catch (Exception e) {
			e.printStackTrace();
			code=SERVER_ERR;
		}
		return super.resultInfo(null, code, msg);
	}
	
	
	/**
	 * 手机登录发送验证码
	 * @author zhanglz 
	 * @return 
	 */
	@RequestMapping("/mobileSendMsg")
	public ResponseEntity<String> sendMsg(HttpSession session,String mobile){
		String msg=null; 
		int code =SERVER_ERR;
//		if(!iuuMallService.haveMobile(mobile)){
//			code=FAIL;
//			msg="手机号不存在，请先绑定手机";
//			return super.resultInfo(null, code, msg);
//		}
		String smscode = (AliSMSUtil.nextInt(100000, 999999))+"";
		try {
			if(iuuMallService.sendMsg(mobile, smscode)){
				session.setAttribute("smscode", smscode);
				session.setAttribute("smscodeTime", System.currentTimeMillis() + 5 * 60 * 1000);
				code =SUCESS;
			}
		} catch (Exception e) {
			e.printStackTrace();
			code=SERVER_ERR;
		}
		return super.resultInfo(null, code, msg);
	}
	
	/**
	 * 手机登录
	 * @author zhanglz 
	 * @return 
	 */
	@RequestMapping("/mobileLogin")
	public ResponseEntity<String> phoneLogin(HttpSession session,String smscode,String mobile){
		String msg=null;
		int code =SERVER_ERR;
		String data = null;
		try {
			String sessioncode = session.getAttribute("smscode")==null?"":session.getAttribute("smscode").toString();
			long smscodeTime = (long)session.getAttribute("smscodeTime");
			long now = System.currentTimeMillis();
			if(smscode.equals(sessioncode)&&smscodeTime>now){
				Map<String, Object> map = iuuMallService.mobileLogin(mobile);
				if(map!=null){
					session.setMaxInactiveInterval(10800);
					session.setAttribute("user", map);
					data = new ObjectMapper().writeValueAsString(map);
					code=SUCESS;
				}else{
					code=FAIL;
				}
			}else{
				code=FAIL;
				msg="验证码错误或已失效";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return super.resultInfo(data, code, msg);
	}
	
	/**
	 * 个人中心
	 * @author zhanglz 
	 * @return 
	 */
	@RequestMapping("/personalCenter")
	public String personalCenter(HttpSession session,Model model){
		Map<String, Object> map = (Map)session.getAttribute("user");
		if(map==null)
			return "redirect:/UMallUser/toLogin.do";
		model.addAttribute("user", map);
		return "UUMall/personal-center";
	}
	
	@RequestMapping("/toBinding")
	public String toBinding(){
		return "UUMall/binding";
	}
	
	@RequestMapping("/toLogin")
	public String toLogin(HttpServletRequest request,HttpSession session,Model model,@RequestParam(value="isGoodsPage",required=false)String isGoodsPage,@RequestParam(value="textureIds",required=false)String textureIds,@RequestParam(value="infoId",required=false)String infoId,@RequestParam(value="num",required=false)String num,@RequestParam(value="state",required=false)String state){
		//跳转发送指定参数
		String http="";
		if(state!=null)
		if(state.equals("1"))
		http="http://test.diy.51app.cn/diyMall2/UOrder/addShop.do";
		else if(state.equals("2"))
		http="http://test.diy.51app.cn/diyMall2/UOrder/createOrder.do";
		
		if(isGoodsPage!=null&&"1".equals(isGoodsPage))
		session.setAttribute("Referer",http+"?infoId="+infoId+"&textureIds="+textureIds+"&num="+num);
//			session.setAttribute("Referer", request.getHeader("Referer")); //保存之前的页面路径
		String qqLoginUrl = QQLoginUtil.codeUrl("kennen");
		model.addAttribute("qqLoginUrl", qqLoginUrl);
		return "UUMall/login";
	}
	
	/**
	 * QQ登录链接
	 * @author zhanglz
	 */
	@RequestMapping("/QQLogin")
	public String QQLogin(HttpServletRequest request,HttpSession session,
			Model model,String code,String state){
		Map<String, Object> user = iuuMallService.QQLogin(code);
		session.setMaxInactiveInterval(10800);
		session.setAttribute("user", user);
		return returnBeforePage(request, session,model);
	}
	
	
	/**
	 * 登录后返回登录之前页面
	 * @author zhanglz
	 */
	@RequestMapping("/returnBeforePage")
	public String returnBeforePage(HttpServletRequest request,HttpSession session,Model model){
		Object obj = session.getAttribute("Referer");
		if(obj==null)
			return "redirect:/UMallUser/personalCenter.do";
		else{
			session.removeAttribute("Referer");
			String referer = (String)obj;
			if(referer.contains("addShop")){
				referer="http://test.diy.51app.cn/diyMall2/UOrder/ushop.do";
			}
			String url = referer.substring(referer.lastIndexOf(request.getContextPath())+request.getContextPath().length());
			return "redirect:"+url;
		}
	}
	
	
	@RequestMapping("/todayRecomment")
	public String todayRecomment(Model model,String token,String app){
		model.addAttribute("token", token);
		model.addAttribute("app", app);
		return "UUMall/todayRecomment";
	}
	
	@RequestMapping("/diyring")
	public String diyring(){
		return "UUMall/diyring";
	}
}
