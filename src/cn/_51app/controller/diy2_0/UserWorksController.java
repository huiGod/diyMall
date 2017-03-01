package cn._51app.controller.diy2_0;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

import cn._51app.controller.BaseController;
import cn._51app.entity.User;
import cn._51app.service.diy2_0.IUserWorksService;
import cn._51app.util.AliSMSUtil;
import cn._51app.util.CommonUtil;
import cn._51app.util.HttpClientUtil;

@Controller
@RequestMapping("/user")
public class UserWorksController extends BaseController {
	
	@Autowired
	private IUserWorksService iUserWorksService;
	
		
	/**
	 *查看用户作品
	 * @param id
	 * @return
	 */
	@RequestMapping("/userWorks/{id}")
	public ResponseEntity<String>userWorks(@PathVariable(value="id")String id){
		String data=null;
		int code=FAIL;
		String msg="操作失败";
		try {
			data=iUserWorksService.userWorks(id);
			code=SUCESS;
			msg="操作成功";
			if(data==null || data.equals("") || data.equals("[]")){
				code=EMPTY;
				msg="没有数据";
			}
		} catch (Exception e) {
			e.printStackTrace();
			code=SERVER_ERR;
			msg="服务器错误";
		}
		return super.resultInfo(data, code, msg);
	}
	
	/**
	 * 保存用户作品
	 * 
	 * @param req
	 * @param goodsType  商品类型
	 * @param goodId  商品id
	 * @param userId  用户id
	 * @param imgFile 原图
	 * @param imgOrderFile  效果图（定制图）
	 * @param imgFile2  原图2
	 * @param imgOrderFile2  效果图（定制图）2
	 * @param name  名称
	 * @param cont  内容
	 * @param isopen  是否公开
	 * @param type 1(app)2(作品活动) 3(0元购)
	 * @return
	 */
	@RequestMapping("/saveWorks")
	public ResponseEntity<String>saveWorks(
			HttpServletRequest req,
			String goodsType,
			String goodId,
			String userId,
			String name,
			String cont,
			String isopen,
			String type,
			@RequestParam(value="textureIds",required=false)String textureIds,
			@RequestParam(value="imgFile",required=false)MultipartFile imgFile,
			@RequestParam(value="imgOrderFile",required=false)MultipartFile imgOrderFile,
			@RequestParam(value="imgFile2",required=false)MultipartFile imgFile2,
			@RequestParam(value="imgOrderFile2",required=false)MultipartFile imgOrderFile2
			){
		String data=null;
		int code=SUCESS;
		String msg="操作成功";
        String saveImg="";
		try {
			if(goodsType.equals("3")){
				//上传图片，用户id文件夹
				saveImg=CommonUtil.batchImg(req,userId);
			}else{
				saveImg=CommonUtil.backANDFront(goodsType, imgFile, imgOrderFile, imgFile2, imgOrderFile2);
			}
			data=iUserWorksService.saveWorks(name,cont,isopen,saveImg, goodId, userId,textureIds,type);
		} catch (Exception e) {
			e.printStackTrace();
			code=FAIL;
			 msg="操作失败";
		}
		return super.resultInfo(data, code, msg);
	}
	
	/**
	 * 删除作品
	 * @param id  作品id
	 * @return
	 */
	@RequestMapping("/delWorks/{ids}")
	public ResponseEntity<String>delWorks(@PathVariable(value="ids")String ids){
		String data=null;
		int code=SUCESS;
		String msg="操作成功";
		try {
			iUserWorksService.delWorks(ids);
		} catch (Exception e) {
			e.printStackTrace();
			code=FAIL;
			 msg="操作失败";
		}
		return super.resultInfo(data, code, msg);
	}
	
	/**
	 * TODO 修改用户作品
	 * @param workId   作品id
	 * @param cont 		内容
	 * @param name     标题
	 * @param isopen    是否公开
	 * @param textureIds   材质
	 * @return
	 */
	@RequestMapping("/editWorks")
	public ResponseEntity<String>editWorks(String workId,String cont,String name,String isopen,String textureIds){
		String data=null;
		int code=SUCESS;
		String msg=null;
		try {
			iUserWorksService.editWorks(workId,cont,name,isopen,textureIds);
		} catch (Exception e) {
			e.printStackTrace();
			code=FAIL;
		}
		return super.resultInfo(data, code, msg);
	}
	
	@RequestMapping("/worksInfo/{workId}")
	public ResponseEntity<String>worksInfo(@PathVariable(value="workId")String workId){
		String data=null;
		int code=SUCESS;
		String msg=null;
		try {
			data=iUserWorksService.worksInfo(workId);
			if(data.isEmpty()){
				code=EMPTY;
			}
		} catch (Exception e) {
			e.printStackTrace();
			code=FAIL;
		}
		return super.resultInfo(data, code, msg);
	}
	
	/**
	 * 手机登录发送验证码
	 * @author
	 * @return 
	 */
	@RequestMapping("/mobileSendMsg")
	public ResponseEntity<String> sendMsg(String mobile){
		String msg=null; 
		int code =SERVER_ERR;
		String smscode = (AliSMSUtil.nextInt(100000, 999999))+"";
		String data="";
		try {
			if(iUserWorksService.sendMsg(mobile, smscode)){
				Map<String,Object>map=new HashMap<String,Object>();
				map.put("smscode", smscode);
				map.put("smscodeTime", System.currentTimeMillis() + 5 * 60 * 1000);
				data=new ObjectMapper().writeValueAsString(map);
				code =SUCESS;
			}
		} catch (Exception e) {
			e.printStackTrace();
			code=SERVER_ERR;
		}
		return super.resultInfo(data, code, msg);
	}
	
	/**
	 * tengh 2016年12月31日 上午1:02:09
	 * @param user
	 * @return
	 * TODO 登录。注册
	 */
	@RequestMapping("/login")
	public ResponseEntity<String>login(@ModelAttribute User user){
		String data=null;
		int code=SUCESS;
		String msg=null;
		try {
			//获取token
			data=iUserWorksService.login(user);
			if(StringUtils.isBlank(data)){
				code=EMPTY;
				msg="数据异常，联系管理员~";
			}
		} catch (Exception e) {
			e.printStackTrace();
			code=FAIL;
		}
		return super.resultInfo(data, code, msg);
	}
	
	/**
	 * tengh 2016年12月31日 上午4:22:26
	 * @param imgurl
	 * @param nickName
	 * @param phone
	 * @param sex
	 * @return
	 * TODO 修改账号信息
	 */
	@RequestMapping("/editUser")
	public ResponseEntity<String>editUser(@RequestParam(value="imgurl",required=false)MultipartFile imgurl,String name,String sex,String userId){
		String msg=null;
		int code=SUCESS;
		String data=null;
		try {
			boolean flag=iUserWorksService.editUser(imgurl, name,sex,userId);
			if(!flag){
				code=FAIL;
			}
		} catch (Exception e) {
			e.printStackTrace();
			code=SERVER_ERR;
		}
		return super.resultInfo(data, code, msg);
	}
	
	/**
	 *  修改用户信息
	 * @param imgurl 头像
	 * @param nickName 昵称
	 * @param phone 手机号
	 * @return
	 */
	@RequestMapping("/editUserInfo")
	public ResponseEntity<String>editUserInfo(@RequestParam(value="imgurl",required=false)MultipartFile imgurl,String nickName,String phone,String sex,String openid,String app,String deviceNo){
		String msg=null;
		int code=SUCESS;
		String data="";
		String tokenUrl="https://a1.easemob.com/1717/weiyoupin/token";
		String clientId = "YXA6TgFOcGm-EeaKO5-bd8iPGg";
		String clientSecret = "YXA6lmlcc-mVxoARnQByVWClU9fjFrs";
		String tokenData="";
		if("<null>".equals(openid)){
			openid="";
		}
		try {
			tokenData=HttpClientUtil.httpPostRequest(tokenUrl, "{\"grant_type\":\"client_credentials\",\"client_id\":\""+clientId+"\",\"client_secret\":\""+clientSecret+"\"}","json");
			//获取token
			String token = tokenData.substring(tokenData.indexOf(":")+2, tokenData.indexOf(",")-1);
//			String re="";
//			if(nickName!=null && !nickName.equals("")){
//				for (int i = 0; i < nickName.length(); i++) {
//		            char  item =  nickName.charAt(i);
//		            if(isMessyCode(String.valueOf(item))){//如果是乱码
//		            	continue;
//		            }else{
//		            	re+=item;
//		            }
//		            
//		        }
//			}
//	    	nickName=re;
	    	//生成账号
	    	iUserWorksService.infoUser(deviceNo,app,null,null);
			data=iUserWorksService.editUserInfo(imgurl, nickName, phone,sex,openid,app,deviceNo,token);
			if(data.equals("")){
				code=FAIL;
			}
		} catch (Exception e) {
			e.printStackTrace();
			code=SERVER_ERR;
		}
		return super.resultInfo(data, code, msg);
	}
	
	/**
	 *  修改用户信息
	 * @param imgurl 头像
	 * @param nickName 昵称
	 * @param phone 手机号
	 * @return
	 */
	@RequestMapping("/editUserForAndroid")
	public ResponseEntity<String>editUserForAndroid(@RequestParam(value="imgurl",required=false)MultipartFile imgurl,
			String nickName,
			String phone,
			String sex,
			String openid,
			String app,
			String deviceNo,
			String type,
			String state){
		String msg=null;
		int code=SUCESS;
		String data="";
		try {
	    	//生成账号
	    	iUserWorksService.infoUser(deviceNo,app,null,null);
	    	//处理账号
			data=iUserWorksService.editUserInfoForAndroid(imgurl, nickName, phone,sex,openid,app,deviceNo,state);
			if(data==null){
				msg="查无此用户";
				code=FAIL;
			}
			if(data!=null && data.equals("-1")){
				code=NO_LOGIN;
				msg="还未注册";
				data=null;
			}
			if(data!=null && data.equals("-2")){
				code=REGISTER;
				msg="注册新用户失败";
				data=null;
			}
			if(data!=null && data.equals("-3")){
				code=NOOPENID;
				msg="你未填openid";
				data=null;
			}
			if(data!=null && data.equals("")){
				code=EMPTY;
			}
		} catch (Exception e) {
			e.printStackTrace();
			code=SERVER_ERR;
		}
		return super.resultInfo(data, code, msg);
	}
	
	/**
	 * 查询用户信息
	 * @return
	 */
	@RequestMapping("loadUserInfo/{dn}/{app}")
	public ResponseEntity<String>loadUserInfo(@PathVariable("dn")String dn,@PathVariable("app")String app){
		String msg=null;
		int code=SUCESS;
		String data="";
		String tokenUrl="https://a1.easemob.com/1717/weiyoupin/token";
		String clientId = "YXA6TgFOcGm-EeaKO5-bd8iPGg";
		String clientSecret = "YXA6lmlcc-mVxoARnQByVWClU9fjFrs";
		String tokenData="";
		
		try {
			tokenData=HttpClientUtil.httpPostRequest(tokenUrl, "{\"grant_type\":\"client_credentials\",\"client_id\":\""+clientId+"\",\"client_secret\":\""+clientSecret+"\"}","json");
			//获取token
			String token = tokenData.substring(tokenData.indexOf(":")+2, tokenData.indexOf(",")-1);
			data=iUserWorksService.loadUserInfo(dn, app,token);
		} catch (Exception e) {
			e.printStackTrace();
			code=SERVER_ERR;
		}
		return super.resultInfo(data, code, msg);
	}
	
	/**
	 * TODO 微信登陆获取头像名称(失效)
	 * @return
	 */
	@RequestMapping("/weixinLogin")
	public ResponseEntity<String>weixinLogin(String code){
		String msg=null;
		int status=SUCESS;
		String data="";
		try {
			data=iUserWorksService.weixinLogin(code);
		} catch (Exception e) {
			e.printStackTrace();
			status=SERVER_ERR;
		}
		return super.resultInfo(data, status, msg);
	}
	
	/**
	 * 注册单个用户带上token（“授权注册”模式）
	 * 
	 */
	@RequestMapping("/huanxingToken/{id}")
	public ResponseEntity<String> huanxingToken(@PathVariable("id") int id){
		String tokenUrl="https://a1.easemob.com/1717/weiyoupin/token";
		String clientId = "YXA6TgFOcGm-EeaKO5-bd8iPGg";
		String clientSecret = "YXA6lmlcc-mVxoARnQByVWClU9fjFrs";
	
		String msg=null;
		int status=SUCESS;
		String data="";
		String tokenData="";
		
		
		try {
			
			tokenData=HttpClientUtil.httpPostRequest(tokenUrl, "{\"grant_type\":\"client_credentials\",\"client_id\":\""+clientId+"\",\"client_secret\":\""+clientSecret+"\"}","json");
			String token = tokenData.substring(tokenData.indexOf(":")+2, tokenData.indexOf(",")-1);
			Map<String,Object> map = iUserWorksService.returnOne(id,token);
			data=new ObjectMapper().writeValueAsString(map);
		} catch (Exception e) {
			e.printStackTrace();
			status=SERVER_ERR;
		}
		return super.resultInfo(data, status, msg);
	}
	
	/**
	 * tengh 2016年6月7日 下午1:38:34
	 * @param app
	 * @param deviceNo
	 * @param version
	 * @return
	 * TODO 统计diy用户信息
	 */
	@RequestMapping(value ="infoUser",method = { RequestMethod.GET })
	public ResponseEntity<String> infoUser(@RequestParam(value="app") String app,@RequestParam(value="deviceNo") String deviceNo,@RequestParam(value="version") String version,@RequestParam(value="deviceToken",required=false)String deviceToken){
		String data =null;
		String msg=null;
		int code =SUCESS;
		try {
			boolean result =iUserWorksService.infoUser(deviceNo,app,version,deviceToken);
			if(!result){
				code=FAIL;
				msg="操作失败";
			}
		} catch (Exception e) {
			e.printStackTrace();
			code =SERVER_ERR;
		}
		return super.resultInfo(data, code, msg);
	
	}
	
/**
 * TODO 用户作品直接生成订单为-1
 * @param workId   用户作品id
 * @param num    数量
 * @param deviceNo    设备号
 * @param app     平台号
 * @param textureIds     材质id
 * @return
 */
	@RequestMapping(value="/createOrder")
	public ResponseEntity<String>createOrder(
			@RequestParam(value = "workId") String workId,
			@RequestParam(value = "num") String num,
			@RequestParam(value = "deviceNo") String deviceNo,
			@RequestParam(value = "app") String app,
			@RequestParam(value = "textureIds" )String textureIds
			){
		String data =null;
		String msg="订单生成失败";
		int code =FAIL;
		try {
			data=this. iUserWorksService.createOrder(workId,num,textureIds,app,deviceNo);
			msg="订单生成成功";
			code=SUCESS; 
			if(StringUtils.isBlank(data)){
				code=EMPTY;
				msg="空数据异常";
			}
		} catch (Exception e) {
			e.printStackTrace();
			code =SERVER_ERR;
		}
		return super.resultInfo(data, code, msg);
	}
	
	/**
	 * TODO 用户作品直接生成订单为-1(id版)  考虑0元购商品
	 * @param workId   用户作品id
	 * @param num    数量
	 * @param userId   
	 * @param textureIds     材质id
	 * @return
	 */
		@RequestMapping(value="/createOrderForId")
		public ResponseEntity<String>createOrder(
				@RequestParam(value = "workId") String workId,
				@RequestParam(value = "num",defaultValue="1") String num,
				@RequestParam(value = "userId") String userId,
				@RequestParam(value = "textureIds" )String textureIds,
				@RequestParam(value = "lettering",required=false) String lettering,
				@RequestParam(value = "modId",required=false)String modId
				){
			String data =null;
			String msg="订单生成失败";
			int code =FAIL;
			try {
				data=this. iUserWorksService.createOrderForId(workId,num,textureIds,userId,lettering,modId);
				msg="订单生成成功";
				code=SUCESS; 
				if(StringUtils.isBlank(data)){
					code=EMPTY;
					msg="空数据异常";
				}
			} catch (Exception e) {
				e.printStackTrace();
				code =SERVER_ERR;
			}
			return super.resultInfo(data, code, msg);
		}
	
	/**
	 * TODO 用户作品保存购物车
	 * @param workId
	 * @param num
	 * @param deviceNo
	 * @param app
	 * @param textureIds
	 * @return
	 */
	@RequestMapping(value ="addShop")
	public ResponseEntity<String> addShop(
			@RequestParam(value = "workId") String workId,
			@RequestParam(value = "num") String num,
			@RequestParam(value = "deviceNo") String deviceNo,
			@RequestParam(value = "app") String app,
			@RequestParam(value = "textureIds" )String textureIds
			){
		String data =null;
		String msg=null;
		int code =SUCESS;
		try {
			boolean flag =this.iUserWorksService.addShops(workId,num,textureIds,app,deviceNo);
			if(!flag){
				code =FAIL;
				msg="添加到购物车失败";
			}
		} catch (Exception e) {
			e.printStackTrace();
			code =SERVER_ERR;
		}
		return super.resultInfo(data, code, msg);
	}
	
	/**
	 * TODO 用户作品保存购物车(id版) 考虑0元购商品
	 * @param workId 
	 * @param userId
	 * @param app
	 * @param textureIds
	 * @return
	 */
	@RequestMapping(value ="addShopForId")
	public ResponseEntity<String> addShopForId(
			@RequestParam(value = "workId") String workId,
			@RequestParam(value = "num",defaultValue="1") String num,
			@RequestParam(value = "userId") String userId,
			@RequestParam(value = "textureIds" )String textureIds,
			@RequestParam(value = "lettering",required=false) String lettering,
			@RequestParam(value = "modId",required=false)String modId
			){
		String data =null;
		String msg=null;
		int code =SUCESS;
		try {
			boolean flag =this.iUserWorksService.addShopsForId(workId,num,textureIds,userId,lettering,modId);
			if(!flag){
				code =FAIL;
				msg="添加到购物车失败";
			}
		} catch (Exception e) {
			e.printStackTrace();
			code =SERVER_ERR;
		}
		return super.resultInfo(data, code, msg);
	}
	
	/**
	 * TODO 修复所有用户与的购物车数量
	 * @return
	 */
	@RequestMapping("/amendShop")
	public ResponseEntity<String>amendShop(){
		String data=null;
		String msg=null;
		int code=SUCESS;
		try {
			this.iUserWorksService.amendShop();
		} catch (Exception e) {
			e.printStackTrace();
			code=SERVER_ERR;
		}
		return super.resultInfo(data, code, msg);
	}
	
	/**
	 * TODO 用户反馈
	 * @param contact 联系方式
	 * @param content	联系内容
	 * @return
	 */
	@RequestMapping(value="/retroaction",method={RequestMethod.POST})
	public ResponseEntity<String>retroaction(String contact,String content){
		String data=null;
		String msg=null;
		int code=SUCESS;
		try{
			if(!this.iUserWorksService.retroaction(contact,content)){
				code=super.FAIL;
			}
		}catch(Exception e){
			e.printStackTrace();
			code=SERVER_ERR;
		}
		return super.resultInfo(data, code, msg);
	}
}
