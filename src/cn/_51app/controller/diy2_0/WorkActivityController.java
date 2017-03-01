package cn._51app.controller.diy2_0;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn._51app.controller.BaseController;
import cn._51app.service.diy2_0.IWorkActivityService;
import cn._51app.util.WxLoginUtil;

@Controller
@RequestMapping("/workActivity")
public class WorkActivityController extends BaseController {
	
	public static String redirect_uri = "http://test.diy.51app.cn/diyMall2/workActivity/wxLogin.do";
	
	@Autowired
	public IWorkActivityService iWorkActivityService;

	/**
	 * TODO 排名查询(openid版)
	 * @param userId 用户id===diy_device_user
	 * @return
	 */
	@RequestMapping("/workList/{openid}")
	public ResponseEntity<String>getWorkList(@PathVariable(value="openid")String openid){
		String data=null;
		String msg=null;
		int code=FAIL;
		try {
			data=iWorkActivityService.getWorkList(openid);
			if(!StringUtils.isBlank(data)){
				code=SUCESS;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return super.resultInfo(data, code, msg);
	}
	
	@RequestMapping("/workListForApp")
	public ResponseEntity<String>getWorkListForApp(String userId,String page){
		String data=null;
		String msg=null;
		int code=FAIL;
			try {
				data=iWorkActivityService.getWorkListForApp(userId,page);
				if(!StringUtils.isBlank(data)){
					code=SUCESS;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		return super.resultInfo(data, code, msg);
	}
	
	/**
	 * TODO 分享，跳转活动投票页
	 * @param model
	 * @return
	 */
	@RequestMapping("/initActivity/{workId}/{author}")
	public String initActivity(Model model,@PathVariable("workId")String workId,@PathVariable("author")String author){
		String wxUrl = WxLoginUtil.codeUrl(redirect_uri, workId+"_"+author);
		model.addAttribute("wxUrl", wxUrl);
		return "diy2_0/jumpActivity";
	}
	
	/**
	 * TODO 微信登陆，投票页面,获取openid
	 * @param code
	 * @param state
	 * @return
	 */
	@RequestMapping(value="/wxLogin",method={RequestMethod.GET})
	public String  wxLogin(String code,String state,HttpSession session){
		String openid="",author="",workId="";
		String s_arr[]=state.split("_");
		try {
			if(s_arr.length>=2){
				workId=s_arr[0];
				author=s_arr[1];
			}
			Map<String,Object> paramMap = WxLoginUtil.getAccessToken(code);
			openid = null==paramMap.get("openid")?"":paramMap.get("openid").toString();
			paramMap.put("openid", openid);
			iWorkActivityService.wxLogin(paramMap);
			session.setAttribute("openid",openid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:http://api.diy.51app.cn/diyMall/activitePage/activityVote.do?workId="+workId+"&openid="+openid+"&authorId="+author;
	}
	
	/**
	 * TODO 作品投票页面(h5)
	 * @param openid
	 * @param workId
	 * @return
	 */
	@RequestMapping("/workInfo")
	public ResponseEntity<String>workInfo(String openid,String workId){
		String data=null;
		String msg=null;
		int code=FAIL;
		try {
			data=iWorkActivityService.workInfo(openid,workId);
			code=SUCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return super.resultInfo(data, code, msg);
	}
	
	/**
	 * TODO 作品投票页面(app)
	 * @param openid
	 * @param workId
	 * @return
	 */
	@RequestMapping("/workInforApp")
	public ResponseEntity<String>workInforApp(String userId,String workId){
		String data=null;
		String msg=null;
		int code=FAIL;
		try {
			data=iWorkActivityService.workInfoApp(userId,workId);
			code=SUCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return super.resultInfo(data, code, msg);
	}
	
	/**
	 * TODO 用户投票
	 * @param openid       
	 * @param workId     作品id
	 * @param workUser   作品的用户id
	 * @return
	 */
	@RequestMapping("/userVote")
	public ResponseEntity<String>userVote(String openid,String workId,String workUser){
		String data=null;
		String msg=null;
		int code=FAIL;
		boolean result;
		try {
			result = iWorkActivityService.userVote2(openid,workId,workUser);
			if(result){
				code=SUCESS;
			}else{
				msg="您已给该作品投过票";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}	
		return super.resultInfo(data, code, msg);
	}
	
	/**
	 * TODO 用户投票userId
	 * @param userId        投票的用户
	 * @param workId
	 * @param workUser    作品的用户
	 * @return
	 */
	@RequestMapping("/userVoteforApp")
	public ResponseEntity<String>userVoteforApp(String userId, String workId, String workUser){
		String data=null;
		String msg=null;
		int code=FAIL;
		boolean result;
		try {
			result = iWorkActivityService.userVoteforApp2(userId,workId,workUser);
			if(result){
				code=SUCESS;
			}else{
				msg="您已给该作品投过票";
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}	
		return super.resultInfo(data, code, msg);
	}
	
	
	
	@RequestMapping("/vote")
	public String vote(){
		return "diy2_0/vote";
	}
	
	@RequestMapping("/customize")
	public String customize(){
		return "diy2_0/customize";
	}
	
	@RequestMapping("/rank")
	public String rank(){
		return "diy2_0/rank";
	}	
	
}
