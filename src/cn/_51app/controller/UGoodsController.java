package cn._51app.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import cn._51app.service.ICouponService;
import cn._51app.service.IFocusService;
import cn._51app.service.IGoodsInfoService;
import cn._51app.service.INewOrderService;
import cn._51app.service.INewShopCartService;
import cn._51app.service.IUUMallService;
import cn._51app.util.WxLoginUtil;

@Controller
@RequestMapping("/UGoods")
public class UGoodsController extends BaseController {
	
	@Autowired
	private IGoodsInfoService service;
	
	@Autowired
	private IUUMallService iuuMallService;
	
	@Autowired
	private IGoodsInfoService goodsInfoService;
	
	@Autowired
	private INewOrderService iNewOrderService;
	
	@Autowired
	private ICouponService couponService;
	
	@RequestMapping("/initHome")
	public String initHome(Model model){
		String wxUrl = WxLoginUtil.codeUrl(WxLoginUtil.redirect_uri, "kennen");
		model.addAttribute("wxUrl", wxUrl);
		return "UUMall/jump";
	}
	
	@RequestMapping("/shareGoods")
	public String initHome(Model model,String distUrl){
		String wxUrl = WxLoginUtil.codeUrl(WxLoginUtil.redirect_uri_good,distUrl);
		System.out.println("share");
		model.addAttribute("wxUrl", wxUrl);
		return "UUMall/jump";
	}
	
	/**
	 * 进入主页
	 * @author zhanglz
	 */
	@RequestMapping("/tohome")
	public String tohome(Model model,HttpServletRequest request){
		try {
			//主题
			String json = iuuMallService.youHomeNav();
			List<Map<String,Object>> lm=new ObjectMapper().readValue(json, new TypeReference<ArrayList<Map<String,Object>>>(){});
			model.addAttribute("nav", lm);
			
			//满减滚动条
			String privilege = iNewOrderService.getPrivilege();
			List<Map<String,Object>> plm=null;
			if(privilege!=null){
				plm=new ObjectMapper().readValue(privilege, new TypeReference<ArrayList<Map<String,Object>>>(){});
			}
			model.addAttribute("privilege", plm);
			
			//分享
			String param = "";
			Map<String, String[]> map = request.getParameterMap();
			String url = request.getRequestURI();
			if(!map.isEmpty()){
				for (String key : map.keySet()) {
					param+=key + "=" + (map.get(key))[0]+"&";
				}
				url = request.getRequestURI()+"?"+param.substring(0, param.length()-1);
			}
			Map<String, Object> share = iuuMallService.homeShare(url);
			model.addAttribute("share", share);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "UUMall/index";
	}
	
	/**
	 * 微信登录链接
	 * @param code
	 * @return
	 */
	@RequestMapping("/WxLogin")
	public String WxLogin(HttpServletRequest request,HttpSession session,Model model,String code,String state){
		Map<String, Object> map = iuuMallService.WxLogin(code);
		session.setMaxInactiveInterval(10800);
		session.setAttribute("user", map);
		model.addAttribute("user", map);
		System.out.println("state="+state);
		if(state!=null && !state.equals("kennen")){
			return "redirect:"+state;
		}
		return tohome(model, request);
	}
	
	/**
	 * 微信登录分享出去
	 * @param code
	 * @return
	 */
	@RequestMapping("/WxLoginGoods")
	public String WxLoginGoods(HttpServletRequest request,HttpSession session,Model model,String code,String state){
		Map<String, Object> map = iuuMallService.WxLogin(code);
		System.out.println(state+"|"+state);
		System.out.println(map.get("state"));
		session.setMaxInactiveInterval(10800);
		session.setAttribute("user", map);
		model.addAttribute("user", map);
		return "redirect:"+state;
	}
	
	@RequestMapping("/home")
	public ResponseEntity<String> home(){
		String data =null;
		String msg=null;
		int code =SUCESS;
		try {
			String json =this.service.youHome();
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
	
	//首页商品数据，包含分区显示
	@RequestMapping("/home2")
	public ResponseEntity<String>home2(){
		String data = null;
		String msg = null;
		int code = SUCESS;
		try {
			String json=this.service.youHome2();
			if(json==null||json.equals("")){
				code = EMPTY;
			}else{
				data=json;
			}
		} catch (Exception e) {
			e.printStackTrace();
			code = SERVER_ERR;
		}
		return super.resultInfo(data, code, msg);
	}
	
	//获取商品详细信息，以及默认选择的属性名称，每次点击商品调用
	@RequestMapping("/goods")
	public ResponseEntity<String> goods(Integer id){
		String data =null;
		String msg=null;
		int code =SUCESS;
		try {
			String json =this.service.youGoods(id);
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
	
	/**
	 * 进入商品详情
	 * @author zhanglz
	 */
	@RequestMapping("/goodsInfo")
	public String goodsInfo(Model model,String id){
		try {
			String json = this.service.youGoods(Integer.parseInt(id));
			Map<String,Object> lm=new ObjectMapper().readValue(json, new TypeReference<HashMap<String,Object>>(){});
			model.addAttribute("goods", lm);
			
			//商品详情数据
			String goodsDetailsStr = this.service.getGoodsDetails(id);
			Map<String,Object> m=new ObjectMapper().readValue(goodsDetailsStr, new TypeReference<HashMap<String,Object>>(){});
			model.addAttribute("goodsDetails", m);
			
			//材质数据
			String goodsBuyParamStr =this.goodsInfoService.getGoodsBuyParam(id);
			Map<String,Object> showMap=new ObjectMapper().readValue(goodsBuyParamStr, new TypeReference<HashMap<String,Object>>(){});
			model.addAttribute("json", goodsBuyParamStr);
			model.addAttribute("goodsBuyParam", showMap);
			
			//分享
			Map<String, Object> share = iuuMallService.goodsShare(id);
			model.addAttribute("share", share);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "UUMall/goods-info";
	}
	
	
	/**>>Faster
	 * 获取导航条数据
	 * @return
	 */
	@RequestMapping("/getNav")
	public ResponseEntity<String> getHomeNav(){
		int code=SUCESS;
		String msg=null;
		String data=null;
		try {
			data=this.iuuMallService.youHomeNav();
			if(StringUtils.isBlank(data)){
				code=FAIL;
				msg="无数据";
			}
		} catch (Exception e) {
			e.printStackTrace();
			code=SERVER_ERR;
			msg="异常处理";
		}
		return resultInfo(data, code, msg);
	}
	
	/**
	 * 专题页面
	 * @param model
	 * @param nav_id 导航条id
	 * @param type
	 * @return
	 */
	@RequestMapping("/toSpecial")
	public String toSpecial(Model model,String special_ids,Integer type){
		try {
			String json = ""; 
			if(type!=null&&type==2){
				json=iuuMallService.youStrategy(special_ids);
			}else{
				json=iuuMallService.youSpecial(Integer.parseInt(special_ids));
			}
			List<Map<String,Object>> plm=null;
			if(json!=null)
			 plm=new ObjectMapper().readValue(json, new TypeReference<ArrayList<Map<String,Object>>>(){});
			model.addAttribute("special", plm);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(type!=null&&type==2)
			return "UUMall/specialGoods";
		return "UUMall/special";
	}
	
	/**>>Faster
	 * 
	 * @param nav_id 导航条关联id
	 * @return 
	 */
	@RequestMapping(value="/getSpecial",method={RequestMethod.GET})
	public ResponseEntity<String> getSpecial(int nav_id){
		String data=null;
		String msg=null;
		int code=SUCESS;
		try {
			data=this.iuuMallService.youSpecial(nav_id);
			if(StringUtils.isBlank(data)){
				code=FAIL;
				msg="无数据";
			}
		} catch (Exception e) {
			e.printStackTrace();
			code=SERVER_ERR;
			msg="异常处理";
		}
		return resultInfo(data,code,msg);
	}
	
	
	/**
	 * 首页购物车和订单数量(订单只显示未付款数量)
	 * @author zhanglz
	 */
	@RequestMapping("/homeIcoNum")
	public ResponseEntity<String> homeIcoNum(HttpSession session){
		String data =null;
		String msg=null;
		int code =SUCESS;
		try {
			Map<String, Object> map = (Map)session.getAttribute("user");
			String deviceNo = (String)map.get("device_no");
			String app = (String)map.get("app");
			data=iuuMallService.homeIcoNum(deviceNo, app);
			if(StringUtils.isBlank(data)){
				code=FAIL;
			}
		} catch (Exception e) {
			e.printStackTrace();
			code =SERVER_ERR;
		}
		return super.resultInfo(data, code, msg);
	}
	
	
	/*=================================评价====================================*/
	/**
	 * 查看商品评价
	 * @author zhanglz
	 */
	@RequestMapping("/toevaluation")
	public String toevaluation(String id,
			Model model){
			model.addAttribute("id", new Integer(id));
		return "UUMall/comment";
	}
	
	/**
	 * 进入评价页面
	 * @author zhanglz
	 */
	@RequestMapping("/releaseComment")
	public String releaseComment(Model model,String orderNo){
		List<Map<String, Object>> list = iuuMallService.getOrderComm(orderNo);
		model.addAttribute("com", list);
		model.addAttribute("orderNo", orderNo);
		return "UUMall/release-Comment";
	}
	
	/**
	 * 保存评价
	 * @author zhanglz
	 */
	@RequestMapping(value="/saveComment",method={RequestMethod.POST})
	public String saveComment(HttpSession session,Model model,
			@RequestParam(value = "orderNo") String orderNo,
			@RequestParam(value = "info_id") String info_id,
			@RequestParam(value = "commentArea")String commentArea,
			@RequestParam(value = "starNum")String starNum){
		Map<String, Object> map = (Map)session.getAttribute("user");
		String deviceNo = (String)map.get("device_no");
		iuuMallService.saveComment(orderNo, info_id, commentArea, starNum, deviceNo);
		return "redirect:/UOrder/toOrderList.do";
	}
	/*==================================评价end=================================*/
	
	@RequestMapping("toShare")
	public String toShare(Model model){
		try {
			Map<String, Object> map = iuuMallService.returnShare();
			model.addAttribute("share", map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "UUMall/share";
	}
	
	@RequestMapping("toConnect")
	public String toConnect(){
		return "UUMall/connectServicer";
	}
}
