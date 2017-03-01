package cn._51app.controller.diy2_0;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn._51app.controller.BaseController;
import cn._51app.service.diy2_0.IShareService;
import cn._51app.util.QQLoginUtil;
import cn._51app.util.WxLoginUtil;
@Controller
@RequestMapping("/share")
public class ShareController extends BaseController{
	
	@Autowired
	private IShareService ishareService;

	/**
	 * tengh 2016年10月11日 下午7:09:29
	 * @param flag
	 * @param aboutId
	 * @param model
	 * @return
	 * TODO 分享的地址
	 */
	@RequestMapping("/goods")
	public String goodsOfQQ(Model model,HttpServletRequest request){
		String type=request.getParameter("type");
		String flag=request.getParameter("flag");
		String aboutId=request.getParameter("aboutId");
		String url="http://test.diy.51app.cn/diyMall2/share/Login.do?type="+type+"&flag="+flag+"&aboutId="+aboutId;
		if("QQ".equals(type)){
			model.addAttribute("qqLoginUrl", QQLoginUtil.shareUrl(url));
			return "UUMall/login";
		}else if("WX".equals(type)){
			model.addAttribute("wxUrl", WxLoginUtil.shareUrl(url));
			return "UUMall/gzhJump";
		}else{
			return "";
		}
	}
	
	/**
	 * tengh 2016年10月11日 下午3:43:11
	 * @param flag
	 * @param aboutId
	 * @return
	 * TODO 微信静默授权之后获取到用户的openid
	 */
	@RequestMapping(value ="/Login")
	public String WxLogin(HttpServletRequest request,Model model){
		String type=request.getParameter("type");
		String flag=request.getParameter("flag");
		String aboutId=request.getParameter("aboutId");
		String code=request.getParameter("code");
		String openid="";
		try {
			if("QQ".equals(type)){
				String access_token = QQLoginUtil.getAccessToken(code);
				openid= QQLoginUtil.getOpenId(access_token);
			}else if("WX".equals(type)){
				Map<String, Object> map = WxLoginUtil.getAccessToken(code);
				openid=(String)map.get("openid");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//查询出用户id
		int memberId=this.ishareService.getMemberId(openid);
		if("wallpaper".equals(flag)){//分享的壁纸
			 
		}else if("jphGood".equals(flag)){//分享精品会
			
		}else if("custom".equals(flag)){//分享定制商品
			
		}
		return "test";
	}
}
