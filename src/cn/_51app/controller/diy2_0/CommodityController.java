package cn._51app.controller.diy2_0;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import cn._51app.controller.BaseController;
import cn._51app.service.diy2_0.ICommodityService;
import cn._51app.util.OCSKey;
import cn._51app.util.RedisUtil;

/**@author Faster
 * 
 * diy商城2.0（商品部分）
 * createtime 2016-8-9 14:23:37
 */
@Controller
@RequestMapping("/commodity")
public class CommodityController extends BaseController {
	
	@Autowired
	private ICommodityService iCommodityService;

	/**
	 * 商品分类列表（非定制）
	 * @return
	 */
	@RequestMapping("/goodsList")
	public ResponseEntity<String> goodsList(){
		String data=null;
		int code=FAIL;
		String msg="操作失败";
		try {
			 data=iCommodityService.getGoodsSortCat();
			code=SUCESS;
			msg="操作成功";
		} catch (Exception e) {
			e.printStackTrace();
			code=SERVER_ERR;
			msg="服务器错误";
		}
		if(data==null || data.equals("")){
			code=EMPTY;
			msg="没有数据";
		}
		return super.resultInfo(data, code, msg);
	}
	
	/**
	 * 定制商品
	 * @return
	 */
	@RequestMapping("/customize")
	public ResponseEntity<String>customize(@RequestParam(value="id")String id){
		String data=null;
		int code=FAIL;
		String msg="操作失败";
		try {
			data=iCommodityService.customize(id);
			code=SUCESS;
			msg="操作成功";
		} catch (Exception e) {
			e.printStackTrace();
			code=SERVER_ERR;
			msg="服务器错误";
		}
		if(data==null || data.equals("")){
			code=EMPTY;
			msg="没有数据";
		}
		return super.resultInfo(data, code, msg);
	}
	
	/**
	 * 定制商品（临时用）
	 * @return
	 */
	@RequestMapping("/customizeTest")
	public ResponseEntity<String>customizeTest(){
		String data=null;
		int code=FAIL;
		String msg="操作失败";
		try {
			data=iCommodityService.customizeTest();
			code=SUCESS;
			msg="操作成功";
		} catch (Exception e) {
			e.printStackTrace();
			code=SERVER_ERR;
			msg="服务器错误";
		}
		if(data==null || data.equals("")){
			code=EMPTY;
			msg="没有数据";
		}
		return super.resultInfo(data, code, msg);
	}
	
	/**>>Faster
	 * 
	 * 进入商品需要的相关参数
	 * @param id  diy_goods_info   id
	 * @return
	 */
	@RequestMapping(value ="chartParam/{id}",method = { RequestMethod.GET })
	public ResponseEntity<String> goodsChart(
			@PathVariable(value = "id") String id){
		String data =null;
		String msg=null;
		int code =SUCESS;
		try {
			Map<String,Object> paramMap =new HashMap<String,Object>();
			paramMap.put("id", id);
			String json =this.iCommodityService.getGoodsChartParamById(paramMap);
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
	 * 商品标题
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/title/{id}",method={RequestMethod.GET})
	public String getGoodsTitleById(@PathVariable(value="id")String id,
		Model model){
	try {
		//根据商品id查询title
		String goodsTitleStr =this.iCommodityService.getGoodsTitleById(id);
		//设置返回页面对象
		Map<String,Object> m=null;
		//如果查出数据为空，不转换json,避免错误
		if(!"".equals(goodsTitleStr)){
			m=new ObjectMapper().readValue(goodsTitleStr, new TypeReference<HashMap<String,Object>>(){});
		}
		//设置标题对象
		model.addAttribute("goodsTitle", m);
	} catch (Exception e) {
		e.printStackTrace();
	}
	//返回标题页面
	return "goods-title";
	}
	
	/**
	 * 根据分类id查出模板
	 * @param id diy_good的id
	 * @return
	 */
	@RequestMapping("/material/{module}/{id}")
	public ResponseEntity<String>getMaterial(@PathVariable(value="id") String id,@PathVariable(value="module")String module){
		String data=null;
		int code=FAIL;
		String msg="操作失败";
		try {
			data=iCommodityService.materialList(id,module);
		} catch (Exception e) {
			e.printStackTrace();
			code=SERVER_ERR;
			msg="服务器错误";
		}
		code=SUCESS;
		msg="操作成功";
		if(data.isEmpty()){
			code=EMPTY;
			msg="没有数据";
		}
		return super.resultInfo(data, code, msg);
	}
	
	/**
	 * 分类id查询图片和标签
	 * @param id
	 * @return
	 */
	@RequestMapping("/photoList/{id}")
	public ResponseEntity<String>getPhotoList(@PathVariable(value="id")String id){
		String data=null;
		int code=FAIL;
		String msg="操作失败";
		try {
			data=iCommodityService.photoList(id);
		} catch (Exception e) {
			e.printStackTrace();
			code=SERVER_ERR;
			msg="服务器错误";
		}
		code=SUCESS;
		msg="操作成功";
		if(data==null || data.equals("") || data.equals("[]")){
			code=EMPTY;
			msg="没有数据";
		}
		return super.resultInfo(data, code, msg);
	}
	
	/**
	 * 分类id查询图片和标签
	 * @param id
	 * @return
	 */
	@RequestMapping("/tags/{id}")
	public ResponseEntity<String>getTags(@PathVariable(value="id")String id){
		String data=null;
		int code=FAIL;
		String msg="操作失败";
		try {
			data=iCommodityService.tagsList(id);
		} catch (Exception e) {
			e.printStackTrace();
			code=SERVER_ERR;
			msg="服务器错误";
		}
		code=SUCESS;
		msg="操作成功";
		if(data==null || data.equals("") || data.equals("[]")){
			code=EMPTY;
			msg="没有数据";
		}
		return super.resultInfo(data, code, msg);
	}
	
	/**
	 * 发现首页列表
	 * @return
	 */
	@RequestMapping("/findList")
	public ResponseEntity<String>getFindList(String page){
		String data=null;
		int code=FAIL;
		String msg="操作失败";
		try {
			 data=iCommodityService.findList(page);
			code=SUCESS;
			msg="操作成功";
			if(data.equals("")){
				code=EMPTY;
				msg="没有数据";
				data=null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			code=SERVER_ERR;
			msg="服务器错误";
		}
		return super.resultInfo(data, code, msg);
	}
	
	/**
	 * 发现详情列表
	 * @return
	 */
	@RequestMapping("/findDetail/{id}")
	public ResponseEntity<String>findDetail(@PathVariable(value="id")String id){
		String data=null;
		int code=FAIL;
		String msg="操作失败";
		try {
			data=iCommodityService.findDetail(id);
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
	 * 属性选择
	 * @param id 商品id
	 * @return
	 * type类型 1精品2定制商品
	 */
	@RequestMapping("/goodsProperty/{type}/{id}")
	public ResponseEntity<String>goodsProperty(
			@PathVariable(value="id")String id,
			@PathVariable(value="type")String type
			){
		String data=null;
		int code=FAIL;
		String msg="操作失败";
		try {
			data=iCommodityService.goodsProperty(id,type);
			code=SUCESS;
			msg="操作成功";
			if(data==null || data.equals("")){
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
	 * 属性选择 横版
	 * @param id 商品id
	 * @return
	 * type类型 1精品2定制商品
	 */
	@RequestMapping("/goodsProperty2/{type}/{id}")
	public ResponseEntity<String>goodsProperty2(
			@PathVariable(value="id")String id,
			@PathVariable(value="type")String type
			){
		String data=null;
		int code=FAIL;
		String msg="操作失败";
		try {
			data=iCommodityService.goodsProperty2(id,type);
			code=SUCESS;
			msg="操作成功";
			if(data==null || data.equals("")){
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
	 * 属性选择 竖版
	 * @param id 商品id
	 * @return
	 * type类型 1精品2定制商品
	 */
	@RequestMapping("/goodsProperty3/{type}/{id}")
	public ResponseEntity<String>goodsProperty3(
			@PathVariable(value="id")String id,
			@PathVariable(value="type")String type
			){
		String data=null;
		int code=FAIL;
		String msg="操作失败";
		try {
			data=iCommodityService.goodsProperty3(id,type);
			code=SUCESS;
			msg="操作成功";
			if(data==null || data.equals("")){
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
	 * TODO 跳转到property
	 * @param id
	 * @param type   1精品2定制商品
	 * @param model
	 * @return
	 */
	@RequestMapping("/property/{type}/{id}")
	public String Property(
			@PathVariable(value="id")String id,
			@PathVariable(value="type")String type,
			Model model){
		String http="http://api.diy.51app.cn/diyMall/commodity/goodsProperty/"+type+"/"+id+".do";
		model.addAttribute("url",http);
		return "diy2_0/goods-pop";
	}
	
	@RequestMapping("/getSortPage")
	public String getSortPage(){
		return "diy2_0/classify";
	}
	
	/**
	 * TODO　获取字体列表
	 * @return
	 */
	@RequestMapping("/getFontList")
	public ResponseEntity<String>getFontList(){
		String msg=null;
		int code=SUCESS;
		String data=null;
		try {
			data=iCommodityService.getFontList();
		} catch (Exception e) {
			e.printStackTrace();
			code=SERVER_ERR;
		}
		return super.resultInfo(data, code, msg);
		
	}
	
	//获取商品详细信息，以及默认选择的属性名称，每次点击商品调用
		@RequestMapping("/goods")
		public ResponseEntity<String> goods(Integer id,String type){
			String data =null;
			String msg=null;
			int code =SUCESS;
			try {
				String json =this.iCommodityService.goods(id);
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
		 * TODO 跳转商品下面介绍
		 * @param id
		 * @param model
		 * @return
		 */
		@RequestMapping("/goods/{id}")
		public String getGoods(@PathVariable("id")String id,Model model){
			String url="http://api.diy.51app.cn/diyMall/commodity/goods.do?id="+id;
			model.addAttribute("url",url);
			return "diy2_0/goodsBottom";
		}
		
		/**
		 * TODO 定制页面图标
		 * @param place
		 * @return
		 */
		@RequestMapping("/production")
		public ResponseEntity<String> production(String place,String page){
			String data =null;
			String msg=null;
			int code =SUCESS;
			try {
				String json =this.iCommodityService.production(place,page);
				if(json==null||json.isEmpty()){
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
		 * TODO 定制分类的商品
		 * @param sortId
		 * @return
		 */
		@RequestMapping("/getMakeSort")
		public ResponseEntity<String>getMakeSort(String sortId){
			String data =null;
			String msg=null;
			int code =SUCESS;
			try {
				String json =this.iCommodityService.getMakeSort(sortId);
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
		 * TODO 商品详情
		 * @param id
		 * @param type  1精品/2定制
		 * @param model
		 * @return
		 */
		@RequestMapping(value ="details",method = { RequestMethod.GET })
		public ResponseEntity<String> getGoodsDetailsById(
				@RequestParam(value = "id") String id,
				@RequestParam(value = "type") String type){
			String data=null;
			String msg=null;
			int code=super.SUCESS;
			try {
				//数据库or缓存获取数据
				data =this.iCommodityService.getGoodsDetails(id,type);
				if(data==null || data.isEmpty()){
					code=super.EMPTY;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return super.resultInfo(data, code, msg);

		}
		
		/**
		 * TODO 产品页面
		 * @return
		 */
		@RequestMapping("/productionPage")
		public String productionPage(){
			return "diy2_5/order";
		}
		
		/**
		 * TODO定制专题页面
		 * @return
		 */
		@RequestMapping("/orderSpecial")
		public String orderSpecial(){
			return "diy2_5/order-special";
		}
		
		/**
		 * TODO定制页面介绍
		 * @return
		 */
		@RequestMapping("/goodsDetail")
		public String goodsDetail(){
			return "diy2_5/diyDetails";
		}
		
		/**
		 * TODO商品弹窗
		 * @return
		 */
		@RequestMapping("/orderPopup")
		public String orderPopup(){
			return "diy2_5/order-popup";
		}
		
		/**
		 * TODO 拆分1
		 * @return
		 */
		@RequestMapping("/orderImpression")
		public String orderImpression(){
			return "diy2_5/order-impression";
		}
		
		
		/**
		 * TODO 拆分2
		 * @return
		 */
		@RequestMapping("/ordeInscribe")
		public String ordeInscribe(){
			return "diy2_5/order-inscribe";
		}
		
		/**
		 * TODO 商品评论
		 * @return
		 */
		@RequestMapping("/goodsComment")
		public String goodsComment(){
			return "diy2_5/goodsComment";
		}
		
		/**
		 * TODO 商品介绍
		 * @return
		 */
		@RequestMapping("/presentation")
		public String presentation(){
			return "diy2_5/goodsPresentation";
		}
		
		/**
		 * TODO 商品详情
		 * @return
		 */
		@RequestMapping("/goodsDetails")
		public String goodsDetails(){
			return "diy2_5/goodsDetails";
		}
		
		/**
		 * TODO 分享页面
		 * @return
		 */
		@RequestMapping("/orderShare")
		public String shareOrder(){
			return "diy2_5/order-share";
		}
		
		/**
		 * TODO 0元购分享页面
		 * @return
		 */
		@RequestMapping("/freeBuyShare")
		public String freeBuyShare(){
			return "diy2_5/free-buy-share";
		}
		
		/**
		 * TODO 商务合作
		 * @return
		 */
		@RequestMapping("/businessCooperation")
		public String businessCooperation(){
			return "diy2_5/businessCooperation";
		}
		
		/**
		 * TODO 邮费
		 * @return
		 */
		@RequestMapping("/postage")
		public String postage(){
			return "diy2_5/postage";
		}
		
		/**
		 * TODO 
		 * @return
		 */
		@RequestMapping("/makeUp")
		public String makeUp(){
			return "diy2_5/makeUp";
		}
		
		/**
		 * TODO 领取砍价券
		 * @return
		 */
		@RequestMapping("/getBargainTicket")
		public String getBargainTicket(){
			return "diy2_5/getBargainTicket";
		}
		
		/**
		 * TODO 查询文章阅读量
		 */
		@RequestMapping("/getView/{id}")
		public ResponseEntity<String> getView(@PathVariable("id")String id){
			String data=String.valueOf(RedisUtil.zscore(OCSKey.DIY_FIND_NUM, id+""));
			int code=SUCESS;
			String msg=null;
			return super.resultInfo(data, code, msg);
		}
		
		/**
		 * TODO 设置文章阅读量
		 */
		@RequestMapping("/setView/{id}/{num}")
		public ResponseEntity<String> getView(@PathVariable("id")String id,@PathVariable("num")Double num){
			String data=null;
			int code=SUCESS;
			String msg=null;
			try{
				RedisUtil.zadd(OCSKey.DIY_FIND_NUM, num, id+"");
			}catch(Exception e){
				code=FAIL;
			}
			return super.resultInfo(data, code, msg);
		}
		
		/**
		 * TODO 文章阅读量+1
		 */
		@RequestMapping("/addView/{id}")
		public ResponseEntity<String> addView(@PathVariable("id")String id){
			String data=null;
			int code=SUCESS;
			String msg=null;
			try{
				int num=(int) (Math.random()*10+1);
				RedisUtil.zincrby(OCSKey.DIY_FIND_NUM,num, id+"");
			}catch(Exception e){
				code=FAIL;
			}
			return super.resultInfo(data, code, msg);
		}
		
		/**
		 * TODO 购买成功以后的页面
		 * @return
		 */
		@RequestMapping("/shopSuccess")
		public String shopSuccess(){
			return "diy2_5/shopSuccess";
		}
		
		
		/**
		 * TODO 临时活动的页面
		 * @return
		 */
		@RequestMapping("/temporaryActivityPage")
		public String temporaryActivityPage(){
			return "diy2_5/temporaryActivityPage";
		}
		
		/**
		 * TODO 情人节活动
		 * @return
		 */
		@RequestMapping("/ValentineDay")
		public String ValentineDay(){
			return "diy2_5/ValentineDay";
		}
	
}
