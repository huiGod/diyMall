package cn._51app.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import cn._51app.service.IGoodsInfoService;
import cn._51app.controller.BaseController;
/*
 * 购物下单区分 系统 软件 版本
 * 
 */

@Controller
@RequestMapping("/goods/")
public class GoodsInfoController extends BaseController{
	
	
	
	@Autowired
	private IGoodsInfoService service;
	
	/******************************* 进入商品购买及详情h5 *******************************/
	
	/**>>Faster
	 * 
	 * @param id 商品id
	 * @param model 追加页面数据
	 * @return
	 */
	@RequestMapping(value ="title/{id}",method = { RequestMethod.GET })
	public String getGoodsTitleById(
			@PathVariable(value = "id") String id,
			Model model){
		try {
			//根据商品id查询title
			String goodsTitleStr =this.service.getGoodsTitleById(id);
			//设置返回页面对象
			Map<String,Object> m=null;
			//如果查出数据为空，不转换json,避免错误
//			if(!"".equals(goodsTitleStr)){
//				m=new ObjectMapper().readValue(goodsTitleStr, new TypeReference<HashMap<String,Object>>(){});
//			}
			if(StringUtils.isNotBlank(goodsTitleStr)){
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
	
	@RequestMapping(value ="titleV2/{id}",method = { RequestMethod.GET })
	public String getGoodsTitleByIdV2(
			@PathVariable(value = "id") String id,
			Model model){
		try {
			//根据商品id查询title
			String goodsTitleStr =this.service.getGoodsTitleById(id);
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
		return "goods-titleV2";
	}
	
	/**>>Faster
	 * 
	 * 查询详情and介绍
	 * @param id 商品id
	 * @param isBoutique 1精品汇
	 * @param model 返回spring视图request级别数据
	 * @return
	 */
	@RequestMapping(value ="details/{isBoutique}/{id}",method = { RequestMethod.GET })
	public String getGoodsDetailsById(
			@PathVariable(value = "id") String id,
			@PathVariable(value = "isBoutique") String isBoutique,
			Model model){
		try {
			//数据库or缓存获取数据
			String goodsDetailsStr =this.service.getGoodsDetails(id);
			//转换json数据为map对象
			Map<String,Object> m=new ObjectMapper().readValue(goodsDetailsStr, new TypeReference<HashMap<String,Object>>(){});
			//设置返回数据isBoutique
			m.put("isBoutique", isBoutique);
			//设置返回title
			if(isBoutique.equals("1")){
				m.put("title", "图文详情");
			}else if(isBoutique.equals("2")){
				m.put("title", "定制介绍");
			}
			//视图数据为map类型的goodsDetails
			model.addAttribute("goodsDetails", m);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//跳转到一个视图页面
		return "goods-details";
	}
	
	/**>>Faster
	 * 
	 * @param id 商品id
	 * @param model 返回视图数据
	 * @return
	 */
	@RequestMapping(value ="show/{id}",method = { RequestMethod.GET })
	public String getGoodsShowById(
			@PathVariable(value = "id") String id,
			Model model){
		try {
			
			String goodsBuyParamStr =this.service.getGoodsBuyParam(id);
			//如果数据为空会报json转换错误
			Map<String,Object> m=new ObjectMapper().readValue(goodsBuyParamStr, new TypeReference<HashMap<String,Object>>(){});
			model.addAttribute("json", goodsBuyParamStr);
			model.addAttribute("goodsBuyParam", m);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "goods-show-new";
	}
	
	/******************************* 进入商品购买及详情h5 *******************************/
	
	
	
	
	//返回点击图片数组
	/*
	@RequestMapping(value="showImg/{id}",method={ RequestMethod.GET })
	public ResponseEntity<String> getGoodsShowImgById(@PathVariable(value = "id")String id){
		String data=null;
		String msg=null;
		int code=SUCESS;
		try{
			Map<String,Object> paramMap = new HashMap<String,Object>();
			paramMap.put("id",id);
			String json=this.service.getGoodsShowImgById(paramMap);
			if(json==null || json.equals("")){
				code=EMPTY;
			}else{
				data=json;
			}
		}catch(Exception e){
			e.printStackTrace();
			code=SERVER_ERR;
		}
		return super.resultInfo(data, code, msg);
	}
	*/
	
	/**>>Faster
	 * 
	 * 进入商品定制需要的相关参数及h5链接
	 * @param id
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
			String json =this.service.getGoodsChartParamById(paramMap);
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
	
	@RequestMapping(value ="chartParamV2/{id}",method = { RequestMethod.GET })
	public ResponseEntity<String> goodsChartV2(
			@PathVariable(value = "id") String id){
		String data =null;
		String msg=null;
		int code =SUCESS;
		try {
			Map<String,Object> paramMap =new HashMap<String,Object>();
			paramMap.put("id", id);
			String json =this.service.getGoodsChartParamByIdV2(paramMap);
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
	 * @author yuanqi
	 * @param param  商品id,商品参数1,商品参数2
	 *   定制商品页面预览图
	 */
	@RequestMapping(value ="cfImgList",method = { RequestMethod.POST})
	public ResponseEntity<String> getCfImgUrlList(
			@RequestParam(value="param") String param){
		String data =null;
		String msg=null;
		int code =SUCESS;
		try {
			
			String json =this.service.getCfImgUrlList(param);
			if(json==null){
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
	 * @modify yuanqi
	 * @param param  商品id,商品参数1,商品参数2
	 *   定制页面预览图
	 */
	@RequestMapping(value ="cfImgList2",method = { RequestMethod.POST })
	public ResponseEntity<String> getCfAllImgList(
			@RequestParam(value="param") String param){
		String data =null;
		String msg=null;
		int code =SUCESS;
		try {
			
			String json =this.service.getCfImgUrlList2(param);
			if(json==null){
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
	
	/**>>Faster
	 * 
	 * 首页广告页面
	 * @return
	 */
	@RequestMapping(value="adImgList",method = {RequestMethod.GET})
	public ResponseEntity<String> getADImgList(){
		//数据、错误信息默认为空
		String data=null;
		String msg=null;
		//获取错误码
		int code=SUCESS;
		try{
			//查询广告数据
			String json=this.service.getADImgList();
			if(json==null || json.equals("")){
				code=EMPTY;
			}else{
				data=json;
			}
		//返回错误码
		}catch(Exception e){
			e.printStackTrace();
			code=SERVER_ERR;
		}
		//返回json数据
		return super.resultInfo(data, code, msg);
	}
	
	//广告跳转页面
	@RequestMapping(value="adImgJump/{id}",method = {RequestMethod.GET})
	public String getADImgJump(@PathVariable(value="id")String id,
			Model model){
		try {
			List<String> result=null;
			String imgList = this.service.getADImgList(id);
			if(!imgList.equals("")){
				result=new ObjectMapper().readValue(imgList, new TypeReference<List<String>>(){});
			}
			model.addAttribute("imgList",result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "ad-jumpPage";
	}

	/**
	 * TODO1.0广告条
	 * @return
	 */
	@RequestMapping(value="newADImgList",method = {RequestMethod.GET})
	public ResponseEntity<String> newADImgList(){
		//数据、错误信息默认为空
		String data=null;
		String msg=null;
		//获取错误码
		int code=SUCESS;
		try{
			//查询广告数据
			String json=this.service.newADImgList();
			if(json==null || json.equals("")){
				code=EMPTY;
			}else{
				data=json;
			}
		//返回错误码
		}catch(Exception e){
			e.printStackTrace();
			code=SERVER_ERR;
		}
		//返回json数据
		return super.resultInfo(data, code, msg);
	}
	
	/*===================安卓新增接口====================*/
	@RequestMapping("getGoodsTitle")
	public ResponseEntity<String> getGoodsTitle(String id){
		String data=null;
		String msg=null;
		int code=SUCESS;
		try {
			String json =this.service.getGoodsTitleById(id);
			if(json==null || json.equals("")){
				code=EMPTY;
			}else{
				data=json;
			}
		} catch (Exception e) {
			e.printStackTrace();
			code=SERVER_ERR;
		}
		return super.resultInfo(data, code, msg);
	}
	
	@RequestMapping("getGoodsBuyParam")
	public ResponseEntity<String> getGoodsBuyParam(String id){
		String data=null;
		String msg=null;
		int code=SUCESS;
		try {
			String json =this.service.getGoodsBuyParam(id);
			if(json==null || json.equals("")){
				code=EMPTY;
			}else{
				data=json;
			}
		} catch (Exception e) {
			e.printStackTrace();
			code=SERVER_ERR;
		}
		return super.resultInfo(data, code, msg);
	}
	/*======================================================*/
	
	
}
