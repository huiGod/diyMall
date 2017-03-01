package cn._51app.controller.diy2_0;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import cn._51app.controller.BaseController;
import cn._51app.service.diy2_0.HikActiveService;
import cn._51app.util.WxLoginUtil;

@Controller
@RequestMapping("hikActive")
public class HikActiveController extends BaseController{

	@Autowired
	private HikActiveService hikActiveService;
	
	@RequestMapping(value="ranking",method={RequestMethod.GET})
	public ResponseEntity<String> getRanking(@RequestParam("userId")String userId){
		String data =null;
		String msg=null;
		int code =SUCESS;
		try {
			//查询用户是否有作品
			if(hikActiveService.getUserInfo(userId)){
				Map<String,Object> paramMap = new HashMap<String,Object>();
				paramMap.put("user_id", userId);
				String json = hikActiveService.getInfo(paramMap);
				if(json==null||json.equals("")){
					code =EMPTY;
				}else{
					data=json;
				}
			}else{
				code=FAIL;
				msg="你还没有设计";
			}
		} catch (Exception e) {
			e.printStackTrace();
			code =SERVER_ERR;
		}
		return super.resultInfo(data, code, msg);
	}
	
	
	/**
	 * TODO 微信登陆,只有openid
	 * @param code  
	 * @param state 
	 * @return
	 */
	@RequestMapping(value="wxLogin",method={RequestMethod.GET})
	public ResponseEntity<String> wxLogin(String code,String state){
		String data =null;
		String msg=null;
		int code1 =SUCESS;
		try {
			Map<String,Object> paramMap = WxLoginUtil.getAccessToken(code);
			String wxId = null==paramMap.get("openid")?"":paramMap.get("openid").toString();
			String info[]=state.split(",");
			System.out.println(info[0]);
			System.out.println(info[1]);
			paramMap.put("openid", wxId);
			paramMap.put("goods_id", state);
			String json = hikActiveService.wxLogin(paramMap);
			if(json==null||json.equals("")){
				code1 =EMPTY;
			}else{
				data=json;
			}
		} catch (Exception e) {
			code1=FAIL;
			msg="服务器错误";
		}
		return super.resultInfo(data, code1, msg);
		
	}
	
	/**
	 * TODO 微信点赞功能
	 * @param goodsId
	 * @param openid
	 * @return
	 */
	@RequestMapping(value="wxHik",method={RequestMethod.GET})
	public ResponseEntity<String> wxHik(@RequestParam("goodsId")String goodsId,@RequestParam("openid") String openid){
		String data =null;
		String msg=null;
		int code =SUCESS;
		try {

			Map<String,Object> paramMap = new HashMap<String,Object>();
			paramMap.put("goods_id", goodsId);
			paramMap.put("openid", openid);
			String json = hikActiveService.wxHik(paramMap);
			if(json==null||json.equals("")){
				code =EMPTY;
			}else{
				data=json;
			}
			if(data!=null){
				if(data.indexOf("0")!=-1){
					if(data.replaceAll("\"", "").equals("0")){
						code=FAIL;
						msg="你已经投过票";
					}
				}
			}
		} catch (Exception e) {
			code=FAIL;
			msg="服务器错误";
		}
		
		return super.resultInfo(data, code, msg);
	}
	
	
	@RequestMapping(value="payGoods",method={RequestMethod.GET})
	public ResponseEntity<String> payGoods(@RequestParam("goodsId")String goodsId,@RequestParam("userId")String userId,@RequestParam("number")Integer number,@RequestParam("price")String price){
		String data =null;
		String msg=null;
		int code =SUCESS;
		try {
			Map<String,Object> paramMap = new HashMap<String,Object>();
			paramMap.put("goods_id", goodsId);
			paramMap.put("user_id", userId);
			paramMap.put("number", number);
			paramMap.put("price", price);
			
			String json = hikActiveService.payGoods(paramMap);
			if(json==null||json.equals("")){
				code =EMPTY;
			}else{
				data=json;
			}
		} catch (Exception e) {
			code=FAIL;
			msg="服务器错误";
		}
		return super.resultInfo(data, code, msg);
	}
	
	
	@RequestMapping(value="wxPayGoods",method={RequestMethod.GET})
	public ResponseEntity<String> wxPayGoods(@RequestParam("goodsId")String goodsId,@RequestParam("openid")String openid,@RequestParam("number")Integer number,@RequestParam("price")String price){
		String data =null;
		String msg=null;
		int code =SUCESS;
		try {
			Map<String,Object> paramMap = new HashMap<String,Object>();
			paramMap.put("goods_id", goodsId);
			paramMap.put("user_id", openid);
			paramMap.put("number", number);
			paramMap.put("price", price);
			String json = hikActiveService.payGoods(paramMap);
			if(json==null||json.equals("")){
				code =EMPTY;
			}else{
				data=json;
			}
		} catch (Exception e) {
			code=FAIL;
			msg="服务器错误";
		}
		return super.resultInfo(data, code, msg);
	}
	
	/**
	 * TODO 投票页面
	 * @param userId   用户id==openid
	 * @param goodsId   定制商品id new_goods_info
	 * @return
	 */
	@RequestMapping(value="appHik",method={RequestMethod.GET})
	public ResponseEntity<String> appHik(@RequestParam("userId")String userId,@RequestParam("goodsId")String goodsId){
		String data =null;
		String msg=null;
		int code =SUCESS;
		try {
			Map<String,Object> paramMap = new HashMap<String,Object>();
			
			paramMap.put("user_id", userId);
			paramMap.put("goods_id", goodsId);
			
			String json = hikActiveService.appHik(paramMap);
			if(json==null||json.equals("")){
				code =EMPTY;
			}else{
				data=json;
			}
			//data==0时候表示已经投过票
			if(data!=null){
				if(data.indexOf("0")!=-1){
					if(data.replaceAll("\"", "").equals("0")){
						code=FAIL;
						msg="你已经投过票";
					}
				}
			}
			
		} catch (Exception e) {
			code=FAIL;
			msg="服务器错误";
		}
		return super.resultInfo(data, code, msg);
	}
}
