package cn._51app.controller.diy2_0;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/activitePage")
public class ActivitePageController {
	
	@RequestMapping("/activityDownload")
	public String activityDownload(){
		return "diy2_5/activity/activity-download";
	}
	
	@RequestMapping("/activityGoods")
	public String activityGoods(){
		return "diy2_5/activity/activity-goods";
	}
	
	@RequestMapping("/activityIndex")
	public String activityIndex(){
		return "diy2_5/activity/activity-index";
	}
	
	@RequestMapping("/activitySort")
	public String activitySort(){
		return "diy2_5/activity/activity-sort";
	}
	
	@RequestMapping("/activityVote")
	public String activityVote(){
		return "diy2_5/activity/activity-vote";
	}
	
	@RequestMapping("/customization")
	public String customization(){
		return "diy2_5/customization-2_20";
	}
	
	
	@RequestMapping("/activityArea")
	public String activityArea(){
		return "diy2_5/activity/activity-area";
	}
	
	@RequestMapping("/activityAiSiAssistant")
	public String activityAiSiAssistant(){
		return "diy2_5/activity/activity-AiSiAssistant";
	}
	
	@RequestMapping("/everydayPreferred")
	public String everydayPreferred(){
		return "diy2_5/activity/everydayPreferred";
	}
	
	@RequestMapping("/favorCustomDesign")
	public String favorCustomDesign(){
		return "diy2_5/activity/favorCustomDesign";
	}
	
	@RequestMapping("/newUserArrondi")
	public String newUserArrondi(){
		return "diy2_5/activity/newUserArrondi";
	}
}
