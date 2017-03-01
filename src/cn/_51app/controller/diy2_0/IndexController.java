package cn._51app.controller.diy2_0;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn._51app.controller.BaseController;
import cn._51app.service.diy2_0.IIndexService;
import cn._51app.util.JSONUtil;

@Controller
@RequestMapping("/index")
public class IndexController extends BaseController {
	
	@Autowired
	private IIndexService iIndexService;
	
	@RequestMapping("/data")
	public String index(){
		return "/diy2_5/index";
	}
	
	@RequestMapping("/storeGoods")
	public String storeGoods(){
		return "/diy2_5/storeGoods";
	}
	
	/**
	 * TODO 首页数据
	 * @return
	 */
	@RequestMapping("/home")
	public ResponseEntity<String> home(){
		String data=null;
		int code=SUCESS;
		String msg=null;
		try {
			data=iIndexService.home();
			if(StringUtils.isBlank(data)){
				code=EMPTY;
				msg="没有首页数据";
			}
		} catch (Exception e) {
			e.printStackTrace();
			code=SERVER_ERR;
		}
		return super.resultInfo(data, code, msg);
	}
	
	/**
	 * tengh 2016年12月27日 下午10:58:11
	 * @param userId
	 * @param mobile
	 * @return
	 * TODO 绑定手机号
	 */
	@RequestMapping("/boundMobile")
	public ResponseEntity<String> boundMobile(@RequestParam(value="userId")String userId,@RequestParam(value="mobile")String mobile){
		String data=null;
		int code=SUCESS;
		String msg=null;
		try {
			Map<String, Object> result=iIndexService.boundMobile(userId,mobile);
			boolean flag=(boolean)result.get("flag");
			if(!flag){
				code=FAIL;
			}
			data=JSONUtil.convertObjectToJSON(result);
		} catch (Exception e) {
			e.printStackTrace();
			code=SERVER_ERR;
		}
		return super.resultInfo(data, code, msg);
	}
	
	/**
	 * TODO 首页商品推荐
	 * @return
	 */
	@RequestMapping("/homeRecommend")
	public ResponseEntity<String> homeRecommend(@RequestParam(value="page")int page){
		String data=null;
		int code=SUCESS;
		String msg=null;
		try {
			data=iIndexService.homeRecommendByPage(page);
			if(StringUtils.isBlank(data)){
				code=EMPTY;
				msg="已经没有数据了";
			}
		} catch (Exception e) {
			e.printStackTrace();
			code=SERVER_ERR;
		}
		return super.resultInfo(data, code, msg);
	}
	
	/**
	 * TODO 店铺商品推荐
	 * @return
	 */
	@RequestMapping("/goodRecommend")
	public ResponseEntity<String> goodRecommendBypage(@RequestParam(value="page")int page,@RequestParam(value="storeId")String storeId){
		String data=null;
		int code=SUCESS;
		String msg=null;
		try {
			data=iIndexService.goodRecommendBypage(page,storeId);
			if(StringUtils.isBlank(data)){
				code=EMPTY;
				msg="已经没有数据了";
			}
		} catch (Exception e) {
			e.printStackTrace();
			code=SERVER_ERR;
		}
		return super.resultInfo(data, code, msg);
	}
	
	/**
	 * TODO 首页顶部导航栏
	 * @return
	 */
	@RequestMapping("/topNavInfo")
	public ResponseEntity<String> topNavInfo(@RequestParam(value="id")int id){
		String data=null;
		int code=SUCESS;
		String msg=null;
		try {
			data=iIndexService.getTopNavInfoById(id);
			if(StringUtils.isBlank(data)){
				code=EMPTY;
				msg="已经没有数据了";
			}
		} catch (Exception e) {
			e.printStackTrace();
			code=SERVER_ERR;
		}
		return super.resultInfo(data, code, msg);
	}
	
	/**
	 * TODO 首页顶部导航栏下拉分页
	 * @return
	 */
	@RequestMapping("/topNavInfoByPage")
	public ResponseEntity<String> topNavInfoByPage(@RequestParam(value="id")int id,@RequestParam(value="page")int page){
		String data=null;
		int code=SUCESS;
		String msg=null;
		try {
			data=iIndexService.topNavInfoByPage(id,page);
			if(StringUtils.isBlank(data)){
				code=EMPTY;
				msg="已经没有数据了";
			}
		} catch (Exception e) {
			e.printStackTrace();
			code=SERVER_ERR;
		}
		return super.resultInfo(data, code, msg);
	}
	
	/**
	 * TODO 专题详情
	 * @return
	 */
	@RequestMapping("/specialInfo")
	public ResponseEntity<String> specialInfo(@RequestParam(value="type")int type,@RequestParam(value="id") int id){
		String data=null;
		int code=SUCESS;
		String msg=null;
		try {
			data=iIndexService.specialInfoById(type,id);
			if(StringUtils.isBlank(data)){
				code=EMPTY;
				msg="数据为空";
			}
		} catch (Exception e) {
			e.printStackTrace();
			code=SERVER_ERR;
		}
		return super.resultInfo(data, code, msg);
	}
	
	/**
	 * TODO 分类首页
	 * @return
	 */
	@RequestMapping("/sortHome")
	public ResponseEntity<String>sortHome(String type){
		String data=null;
		int code=SUCESS;
		String msg=null;
		try{
			data=iIndexService.sortHome(type);
			if(StringUtils.isBlank(data)){
				code=super.EMPTY;
				msg="数据为空";
			}
		}catch(Exception e){
			code=super.SERVER_ERR;
		}
		return super.resultInfo(data, code, msg);
	}
	
	/**
	 * TODO 专题
	 * @return
	 */
	@RequestMapping("/toSpecial")
	public String toSpecial(){
		return "/diy2_5/special";
	}
	
	/**
	 * TODO 定制全部下拉框
	 * @return
	 */
	@RequestMapping("/orderInscribeCategory")
	public String orderInscribeCategory(){
		return "/diy2_5/order-inscribe-category";
	}
	
	/**
	 * TODO 发现列表
	 */
	@RequestMapping("/discovery")
	public String discovery(){
		return "/diy2_5/discovery";
	}
	
	/**
	 * TODO 发现详情
	 * @return
	 */
	@RequestMapping("/discoveryDetail")
	public String discoveryDetail(){
		return "/diy2_5/discovery-detail";
	}
	
	/**
	 * TODO 分类的页面
	 * @return
	 */
	@RequestMapping("/sortPage")
	public String sortPage(){
		return "/diy2_5/classify";
	}
	
	/**
	 * TODO 0元购
	 * @return
	 */
	@RequestMapping("/freeToBuy")
	public String freeToBuy(){
		return "/diy2_5/freeToBuy";
	}
	
	/**
	 * TODO index02
	 */
	@RequestMapping("/index02")
	public String index02(){
		return "/diy2_5/index02";
	}
	
	/**
	 * tengh 2017年1月17日 下午8:57:18
	 * @return
	 * TODO 安卓最新应用列表
	 */
	@RequestMapping("/updateVersion")
	public ResponseEntity<String>updateVersion(){
		String data=null;
		int code=SUCESS;
		String msg=null;
		try{
			data=iIndexService.updateVersion();
			if(StringUtils.isBlank(data)){
				code=super.EMPTY;
			}
		}catch(Exception e){
			code=super.SERVER_ERR;
		}
		return super.resultInfo(data, code, msg);
	}
	
	@RequestMapping("/homeRevision")
	public ResponseEntity<String>homeRevision(){
		String data=null;
		int code=SUCESS;
		String msg=null;
		try{
			data=iIndexService.homeRevision();
			if(StringUtils.isBlank(data)){
				code=super.EMPTY;
			}
		}catch(Exception e){
			code=super.SERVER_ERR;
			e.printStackTrace();
		}
		return super.resultInfo(data, code, msg);
	}
	
}
