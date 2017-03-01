package cn._51app.controller.diy2_0;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import cn._51app.controller.BaseController;
import cn._51app.service.diy2_0.IZeroGoodsService;
import cn._51app.util.IpUtil;
import cn._51app.util.JSONUtil;
import cn._51app.util.JpushUtil;
import cn._51app.util.PropertiesUtil;
import cn.jpush.api.JPushClient;

@Controller
@RequestMapping("/zero")
public class ZeroController extends BaseController{
	
	private String preImgUrl=PropertiesUtil.getValue("diy.goods.url");
	
	@Autowired
	private IZeroGoodsService iZeroGoodsService;

	/**
	 * tengh 2016年11月17日 下午6:30:25
	 * @return
	 * TODO 获取0元购商品列表
	 */
	@RequestMapping(value="/zeroGoods",method=RequestMethod.GET)
	public ResponseEntity<String> zeroGoods(){
		String data =null;
		String msg=null;
		int code =SUCESS;
		try {
			data=this.iZeroGoodsService.zeroGoods();
			if(StringUtils.isBlank(data)){
				code=EMPTY;
				msg="暂无数据";
			}
		} catch (Exception e) {
			e.printStackTrace();
			code =SERVER_ERR;
		}
		return super.resultInfo(data, code, msg);
	}
	
	/**
	 * tengh 2016年11月23日 下午1:44:02
	 * @param userId
	 * @return
	 * TODO 作品集
	 */
	@RequestMapping(value="/workList",method=RequestMethod.GET)
	public ResponseEntity<String> workList(@RequestParam(value="userId",required=false)Integer userId){
		String data =null;
		String msg=null;
		int code =SUCESS;
		try {
			data=this.iZeroGoodsService.workList(userId);
			if(StringUtils.isBlank(data)){
				code=EMPTY;
				msg="暂无数据";
			}
		} catch (Exception e) {
			e.printStackTrace();
			code =SERVER_ERR;
		}
		return super.resultInfo(data, code, msg);
	}
	
	/**
	 * tengh 2016年11月28日 下午5:36:07
	 * @param userId
	 * @param id
	 * @param type  0.仅自己可见 1.好友可见 2.公开
	 * @return
	 * TODO 作品权限的修改
	 */
	@RequestMapping(value="/editWorkList",method=RequestMethod.GET)
	public ResponseEntity<String> editWorkList(@RequestParam(value="userId")String userId,@RequestParam(value="id")String id,@RequestParam(value="type")String type){
		String data =null;
		String msg=null;
		int code =SUCESS;
		try {
			boolean flag=this.iZeroGoodsService.editWorkList(userId,id,type);
			if(!flag){
				code=FAIL;
			}
		} catch (Exception e) {
			e.printStackTrace();
			code =SERVER_ERR;
		}
		return super.resultInfo(data, code, msg);
	}
	
	/**
	 * tengh 2016年11月17日 下午7:22:11
	 * @param page
	 * @return
	 * TODO 用户作品集 （最新new 最热hot 好友friend）
	 */
	@RequestMapping(value="/workListByPage",method=RequestMethod.GET)
	public ResponseEntity<String> workListByPage(@RequestParam(value="page",defaultValue="0")int page,@RequestParam(value="type")String type,@RequestParam(value="userId",required=false)Integer userId){
		String data =null;
		String msg=null;
		int code =SUCESS;
		try {
			data=this.iZeroGoodsService.workListByPage(page,type,userId);
			if(StringUtils.isBlank(data)){
				code=EMPTY;
				msg="到底了";
			}
		} catch (Exception e) {
			e.printStackTrace();
			code =SERVER_ERR;
		}
		return super.resultInfo(data, code, msg);
	}
	
	/**
	 * tengh 2016年11月19日 上午10:00:20
	 * @param id
	 * @param userId
	 * @param type  add点赞 delte取消
	 * @return
	 * TODO 给作品点赞/取消赞
	 */
	@RequestMapping(value="/doLike",method=RequestMethod.GET)
	public ResponseEntity<String> doLike(@RequestParam(value="id")Integer id,@RequestParam(value="userId")Integer userId,@RequestParam(value="type")String type){
		String data =null;
		String msg=null;
		int code =SUCESS;
		try {
			boolean result=this.iZeroGoodsService.doLike(id,userId,type);
			if(!result){
				code=FAIL;
			}
		} catch (Exception e) {
			e.printStackTrace();
			code =SERVER_ERR;
		}
		return super.resultInfo(data, code, msg);
	}
	
	/**
	 * tengh 2016年11月19日 下午1:45:10
	 * @param userId
	 * @param friendid
	 * @param type
	 * @return
	 * TODO 好友操作   （添加好友add  删除好友delete）
	 */
	@RequestMapping(value="/doFriend",method=RequestMethod.GET)
	public ResponseEntity<String> doFriend(@RequestParam(value="userId")String userId,@RequestParam(value="friendId")String friendId,@RequestParam(value="type")String type){
		String data =null;
		String msg=null;
		int code =SUCESS;
		try {
			boolean result=this.iZeroGoodsService.doFriend(userId,friendId,type);
			if(!result){
				code=FAIL;
				msg="他/她已经是你的好友哦~";
			}
		} catch (Exception e) {
			e.printStackTrace();
			code =SERVER_ERR;
		}
		return super.resultInfo(data, code, msg);
	}
	
	/**
	 * tengh 2016年12月1日 下午8:53:45
	 * @param userId
	 * @param id
	 * @return
	 * TODO 通过作品id添加好友
	 */
	@RequestMapping(value="/doFriendByWork",method=RequestMethod.GET)
	public ResponseEntity<String> doFriendByWork(@RequestParam(value="userId")String userId,@RequestParam(value="id")String id){
		String data =null;
		String msg=null;
		int code =SUCESS;
		try {
			boolean result=this.iZeroGoodsService.doFriendByWork(userId,id);
			if(!result){
				code=FAIL;
			}
		} catch (Exception e) {
			e.printStackTrace();
			code =SERVER_ERR;
		}
		return super.resultInfo(data, code, msg);
	}
	
	/**
	 * tengh 2016年11月21日 下午6:15:32
	 * @param id
	 * @return
	 * TODO 作品详情
	 */
	@RequestMapping(value="/goodsInfo",method=RequestMethod.GET)
	public ResponseEntity<String> goodsInfo(@RequestParam(value="id")String id,@RequestParam(value="userId",required=false)String userId){
		String data =null;
		String msg =null;
		int code =SUCESS;
		try {
			Map<String, Object> result=this.iZeroGoodsService.goodsInfo(id,userId);
			boolean flag=(Boolean)result.get("flag");
			if(!flag){
				code=FAIL;
				msg=(String)result.get("msg");
			}else{
				data=(String)result.get("data");
			}
		} catch (Exception e) {
			e.printStackTrace();
			code =SERVER_ERR;
		}
		return super.resultInfo(data, code, msg);
	}
	
	/**
	 * tengh 2016年11月23日 下午2:04:43
	 * @param userId
	 * @return
	 * TODO 我的作品首页
	 */
	@RequestMapping(value="/myWorkList",method=RequestMethod.GET)
	public ResponseEntity<String> myWorkList(@RequestParam(value="userId")Integer userId){
		String data =null;
		String msg=null;
		int code =SUCESS;
		try {
			data=this.iZeroGoodsService.myWorkList(userId);
			if(StringUtils.isBlank(data)){
				code=EMPTY;
				msg="到底了";
			}
		} catch (Exception e) {
			e.printStackTrace();
			code =SERVER_ERR;
		}
		return super.resultInfo(data, code, msg);
	}
	
	/**
	 * tengh 2016年11月22日 下午2:18:09
	 * @param userId
	 * @param type all normal zero
	 * @return
	 * TODO 我的作品集
	 */
	@RequestMapping(value="/myWorkListByPage",method=RequestMethod.GET)
	public ResponseEntity<String> myWorkListByPage(@RequestParam(value="userId")Integer userId,@RequestParam(value="type")String type,@RequestParam(value="page",defaultValue="0")int page){
		String data =null;
		String msg=null;
		int code =SUCESS;
		try {
			data=this.iZeroGoodsService.myWorkListByPage(userId,type,page);
			if(StringUtils.isBlank(data)){
				code=EMPTY;
			}
		} catch (Exception e) {
			e.printStackTrace();
			code =SERVER_ERR;
		}
		return super.resultInfo(data, code, msg);
	}
	
	/**
	 * tengh 2016年11月22日 下午3:25:12
	 * @param userId
	 * @param id
	 * @return
	 * TODO 作品集的删除
	 */
	@RequestMapping("/delWorkList")
	public ResponseEntity<String> delWorkList(@RequestParam(value="userId")Integer userId,@RequestParam(value="id")int id){
		String data =null;
		String msg=null;
		int code =SUCESS;
		try {
			boolean flag=this.iZeroGoodsService.delWorkList(id,userId);
			if(!flag){
				code=FAIL;
			}
		} catch (Exception e) {
			e.printStackTrace();
			code =SERVER_ERR;
		}
		return super.resultInfo(data, code, msg);
	}
	
	/**
	 * tengh 2016年11月28日 下午12:02:15
	 * @param userId
	 * @param id
	 * @return
	 * TODO 加入购物车,购买
	 */
	@RequestMapping("/endCutprice")
	public ResponseEntity<String> endCutprice(@RequestParam(value="userId")String userId,@RequestParam(value="id")String id){
		String data =null;
		String msg=null;
		int code =SUCESS;
		try {
			Map<String, Object> result=this.iZeroGoodsService.endCutprice(id,userId);
			boolean flag=(Boolean)result.get("flag");
			if(flag){
				data=JSONUtil.convertObjectToJSON(result);
			}else{
				code=FAIL;
				msg="数据异常";
			}
		} catch (Exception e) {
			e.printStackTrace();
			code =SERVER_ERR;
		}
		return super.resultInfo(data, code, msg);
	}
	
	/**
	 * tengh 2016年11月22日 下午3:59:18
	 * @param userId
	 * @param id
	 * @return
	 * TODO 对作品砍价
	 */
	@RequestMapping("/cutPrice")
	public ResponseEntity<String> cutPrice(@RequestParam(value="userId")Integer userId,@RequestParam(value="id")int id,@RequestParam(value="mobile")String mobile,@RequestParam(value="cutCouponId")String cutCouponId,HttpServletRequest request){
		String data =null;
		String msg=null;
		int code =SUCESS;
		try {
			Map<String, Object> map=this.iZeroGoodsService.cutPrice(id,userId,IpUtil.getIp(request),mobile,cutCouponId);
			boolean flag=(Boolean)map.get("flag");
			msg=(String)map.get("msg");
			if(!flag){
				code=FAIL;
			}
		} catch (Exception e) {
			e.printStackTrace();
			code =SERVER_ERR;
		}
		return super.resultInfo(data, code, msg);
	}
	
	/**
	 * tengh 2016年11月23日 下午2:20:54
	 * @param userId
	 * @return
	 * TODO 好友列表
	 */
	@RequestMapping("/friends")
	public ResponseEntity<String> friends(@RequestParam(value="userId")Integer userId){
		String data =null;
		String msg=null;
		int code =SUCESS;
		try {
			data=this.iZeroGoodsService.friends(userId);
			if(StringUtils.isBlank(data)){
				code=EMPTY;
			}
		} catch (Exception e) {
			e.printStackTrace();
			code =SERVER_ERR;
		}
		return super.resultInfo(data, code, msg);
	}
	
	/**
	 * tengh 2016年11月25日 下午4:12:19
	 * @param id
	 * @param channel app应用内
	 * @param userId
	 * @return
	 * TODO 扫描添加好友二维码
	 */
	@RequestMapping("/scanFriend")
	public String scanFriend(Model model,@RequestParam(value="id")String id,@RequestParam(value="channel",required=false)String channel){
		if(!"app".equals(channel)){
			//应用外扫描
			model.addAttribute("location", "http://a.app.qq.com/o/simple.jsp?pkgname=com.wcl.market");
			return "diy2_5/direct";
		}
		Map<String, Object> info=this.iZeroGoodsService.getPersonInfo(id);
		//应用内扫描
		model.addAttribute("location", "friend://?id="+info.get("id")+"&name="+info.get("name")+"&headUrl="+info.get("headUrl")+"&islogin=true&isback=true");
		return "diy2_5/direct";
	}
	
	/**
	 * tengh 2016年12月1日 下午2:10:58
	 * @param id
	 * @return
	 * TODO 分享出去打开详情
	 */
	@RequestMapping("/userWorkById")
	public ResponseEntity<String> userWorkById(@RequestParam(value="id")String id){
		String data =null;
		String msg=null;
		int code =SUCESS;
		try {
			data=this.iZeroGoodsService.userWorkById(id);
			if(StringUtils.isBlank(data)){
				code=EMPTY;
			}
		} catch (Exception e) {
			e.printStackTrace();
			code =SERVER_ERR;
		}
		return super.resultInfo(data, code, msg);
	}
	
	
	/**
	 * tengh 2016年11月25日 下午6:15:42
	 * @param model
	 * @param id
	 * @param channel
	 * @return
	 * TODO 分享商品
	 */
	@RequestMapping("/scanGood")
	public String scanGood(Model model,@RequestParam(value="id")String id,@RequestParam(value="channel",required=false)String channel){
		if(!"app".equals(channel)){
			//应用外
			return "diy2_5/free-buy-share";
		}
		//应用内扫描
		model.addAttribute("location", "goodsInfo://?id="+id+"&islogin=true&isback=true");
		return "diy2_5/direct";
	}
	
	/**
	 * tengh 2016年12月5日 下午5:15:18
	 * @return
	 * TODO 分享成功后图片的更换
	 */
	@RequestMapping("/successImg")
	public ResponseEntity<String> successImg(){
		String data =null;
		String msg=null;
		int code =SUCESS;
		try {
			Map<String, Object> result=new HashMap<>();
			result.put("1", preImgUrl+"zero_yaoqinghaoyou_360x288_1.jpg");
			result.put("2", preImgUrl+"zero_yaoqinghaoyou_360x288_2.jpg");
			data=JSONUtil.convertObjectToJSON(result);
			if(StringUtils.isBlank(data)){
				code=EMPTY;
			}
		} catch (Exception e) {
			e.printStackTrace();
			code =SERVER_ERR;
		}
		return super.resultInfo(data, code, msg);
	}
	
}
