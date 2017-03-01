package cn._51app.controller.diy2_0;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

import cn._51app.controller.BaseController;
import cn._51app.service.diy2_0.IShopCartService;
@Controller
@RequestMapping("/shops/")
public class ShopCartController extends BaseController{
	
	@Autowired
	private IShopCartService iShopCartService;
	
	private ObjectMapper mapper=new ObjectMapper();
	
	/**
	 * tengh 2016年5月19日 下午5:06:57
	 * @param deviceNo
	 * @param app
	 * @param page
	 * @return
	 * TODO 查询购物车列表
	 */
	@RequestMapping(value ="shopList",method = { RequestMethod.GET })
	public ResponseEntity<String> list(
			@RequestParam(value = "deviceNo") String deviceNo,
			@RequestParam(value = "app") String app,
			@RequestParam(value = "page") String page
			){
		String data =null;
		String msg=null;
		int code =SUCESS;
		try {
			data =this.iShopCartService.shopList(deviceNo,app,page);
			if(StringUtils.isBlank(data)){
				code =EMPTY;
				msg="您的购物车没有商品";
			}
		} catch (Exception e) {
			e.printStackTrace();
			code =SERVER_ERR;
		}
		return super.resultInfo(data, code, msg);
	}
	
	/**
	 * TODO 查询购物车列表(id版)
	 * @param userId
	 * @param page
	 * @return
	 */
	@RequestMapping(value ="shopListForId",method = { RequestMethod.GET })
	public ResponseEntity<String> listForId(
			@RequestParam(value = "userId") String userId
			){
		String data =null;
		String timestamp=null;
		String msg=null;
		int code =SUCESS;
		try {
			Map<String,Object> map =this.iShopCartService.shopListForId(userId);
			if(map!=null){
				data=mapper.writeValueAsString(map.get("result"));
				timestamp=mapper.writeValueAsString(map.get("timestamp"));
			}
			if(StringUtils.isBlank(data)){
				code =EMPTY;
				msg="您的购物车没有商品";
			}
		} catch (Exception e) {
			e.printStackTrace();
			code =SERVER_ERR;
		}
		return super.resultInfo2(data, code, msg,timestamp);
	}
	
	/**
	 * tengh 2016年11月30日 下午2:44:19
	 * @param id
	 * @param shopNos
	 * @param type  add,del,alladd,alldel
	 * @return
	 * TODO 记录购物车选中状态
	 */
	@RequestMapping(value ="carshopSelect",method = { RequestMethod.GET })
	public ResponseEntity<String> carshopSelect(
			@RequestParam(value = "userId") String userId,
			@RequestParam(value = "shopNos",required=false) String shopNos,
			@RequestParam(value = "type")String type
			){
		String data =null;
		String timestamp=null;
		String msg=null;
		int code =SUCESS;
		try {
			boolean check =this.iShopCartService.carshopSelect(userId,shopNos,type);
			if(!check){
				code =FAIL;
			}
		} catch (Exception e) {
			e.printStackTrace();
			code =SERVER_ERR;
		}
		return super.resultInfo2(data, code, msg,timestamp);
	}
	
	/**
	 * tengh 2016年12月2日 下午2:20:08
	 * @param userId
	 * @param id
	 * @return
	 * TODO 将精品汇商品直接放进购物车
	 */
	@RequestMapping(value ="addShopByGood",method = { RequestMethod.GET })
	public ResponseEntity<String> addShopByWork(
			@RequestParam(value = "userId") String userId,
			@RequestParam(value = "id")String id
			){
		String data =null;
		String msg=null;
		int code =SUCESS;
		try {
			boolean check =this.iShopCartService.addShopByGood(userId,id);
			if(!check){
				code =FAIL;
			}
		} catch (Exception e) {
			e.printStackTrace();
			code =SERVER_ERR;
		}
		return super.resultInfo(data, code, msg);
	}
	
	/**
	 * tengh 2016年11月30日 上午11:53:27
	 * @param id
	 * @param page
	 * @param activityId
	 * @return
	 * TODO 商家凑单
	 */
	@RequestMapping(value ="togetherGoods",method = { RequestMethod.GET })
	public ResponseEntity<String> togetherGoods(
			@RequestParam(value = "id") String id,
			@RequestParam(value = "page",defaultValue="0") String page,
			@RequestParam(value = "activityId",required=false) String activityId
			){
		String data =null;
		String timestamp=null;
		String msg=null;
		int code =SUCESS;
		try {
			data =this.iShopCartService.togetherGoods(id,page,activityId);
			if(StringUtils.isBlank(data)){
				code =EMPTY;
			}
		} catch (Exception e) {
			e.printStackTrace();
			code =SERVER_ERR;
		}
		return super.resultInfo2(data, code, msg,timestamp);
	}
	
	/**
	 * tengh 2016年5月19日 下午7:27:59
	 * @param imgFile
	 * @param imgBackFile
	 * @param previewFile
	 * @param infoId
	 * @param textureIds
	 * @param deviceNo
	 * @param num
	 * @param app
	 * @return
	 * TODO 添加到购物车
	 */
	@RequestMapping(value ="addShop",method = { RequestMethod.POST })
	public ResponseEntity<String> addShop(
			@RequestParam(value = "imgFile",required=false) MultipartFile imgFile,
			@RequestParam(value = "imgBackFile",required=false) MultipartFile imgBackFile,//背面
			@RequestParam(value = "previewFile",required=false) MultipartFile previewFile,
			@RequestParam(value = "previewBackFile",required=false) MultipartFile previewBackFile,//预览图 反面
			@RequestParam(value = "infoId") String infoId,
			@RequestParam(value = "textureIds" )String textureIds,
			@RequestParam(value = "deviceNo") String deviceNo,
			@RequestParam(value = "num") String num,
			@RequestParam(value = "app") String app,
			@RequestParam(value = "wallpara",required=false)String wallpaper,
			@RequestParam(value="isBoutique",required=false)String isBoutique,
			@RequestParam(value = "lettering",required=false)String lettering,
			@RequestParam(value="modId",required=false)String modId,
			HttpServletRequest req
			){
		String data =null;
		String msg=null;
		int code =SUCESS;
		try {
			boolean flag =this.iShopCartService.addShop(imgFile,imgBackFile,previewFile,previewBackFile,infoId,textureIds,deviceNo,num,app,req,wallpaper,isBoutique,lettering,modId);
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
	 * TODO 添加到购物车(id版)
	 * @param imgFile
	 * @param imgBackFile
	 * @param previewFile
	 * @param previewBackFile
	 * @param infoId
	 * @param textureIds
	 * @param num
	 * @param userId
	 * @param wallpaper
	 * @param isBoutique
	 * @param req
	 * @return
	 */
	@RequestMapping(value ="addShopForId",method = { RequestMethod.POST })
	public ResponseEntity<String> addShopForId(
			@RequestParam(value = "imgFile",required=false) MultipartFile imgFile,
			@RequestParam(value = "imgBackFile",required=false) MultipartFile imgBackFile,//背面
			@RequestParam(value = "previewFile",required=false) MultipartFile previewFile,
			@RequestParam(value = "previewBackFile",required=false) MultipartFile previewBackFile,//预览图 反面
			@RequestParam(value = "infoId") String infoId,
			@RequestParam(value = "textureIds" )String textureIds,
			@RequestParam(value = "num") String num,
			@RequestParam(value = "userId") String userId,
			@RequestParam(value = "wallpara",required=false)String wallpaper,
			@RequestParam(value="isBoutique",required=false)String isBoutique,
			@RequestParam(value = "lettering",required=false)String lettering,
			@RequestParam(value="modId",required=false)String modId,
			@RequestParam(value="keycode",required=false)String keycode,
			@RequestParam(value="isSave",required=false,defaultValue="0")String isSave,
			HttpServletRequest req
			){
		String data =null;
		String msg=null;
		int code =SUCESS;
		try {
			boolean flag =this.iShopCartService.addShopForId(imgFile,imgBackFile,previewFile,previewBackFile,infoId,textureIds,userId,num,req,wallpaper,isBoutique,lettering,modId,keycode,isSave);
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
	 * tengh 2016年5月19日 下午7:29:03
	 * @param deviceNo
	 * @param app
	 * @param shopNos 用逗号隔开多个   为-1是删除全部购物车
	 * @return
	 * TODO 删除多个购物车  
	 */
	@RequestMapping(value ="deleteShop",method = { RequestMethod.GET })
	public ResponseEntity<String> deleteShop(
			@RequestParam(value = "deviceNo") String deviceNo,
			@RequestParam(value = "app") String app,
			@RequestParam(value = "shopNos")String shopNos){
		String data =null;
		String msg=null;
		int code =SUCESS;
		try {
			boolean flag =this.iShopCartService.deleteShop(deviceNo,app,shopNos);
			if(!flag){
				code =FAIL;
				msg="删除购物车失败";
			}
		} catch (Exception e) {
			e.printStackTrace();
			code =SERVER_ERR;
		}
		return super.resultInfo(data, code, msg);
	}
	
	/**
	 * TODO 删除多个购物车 (id版)
	 * @param userId
	 * @param shopNos
	 * @return
	 */
	@RequestMapping(value ="deleteShopForId",method = { RequestMethod.GET })
	public ResponseEntity<String> deleteShopForId(
			@RequestParam(value = "userId") String userId,
			@RequestParam(value = "shopNos")String shopNos){
		String data =null;
		String msg=null;
		int code =SUCESS;
		try {
			boolean flag =this.iShopCartService.deleteShopForId(userId,shopNos);
			if(!flag){
				code =FAIL;
				msg="删除购物车失败";
			}
		} catch (Exception e) {
			e.printStackTrace();
			code =SERVER_ERR;
		}
		return super.resultInfo(data, code, msg);
	}
	
	/**
	 * tengh 2016年5月19日 下午7:58:32
	 * @param deviceNo
	 * @param app
	 * @param shopNos 多个购物车编号用逗号隔开
	 * @param nums    需要改到的数量用逗号隔开
	 * @return
	 * TODO 编辑多个购物车数量
	 */
	@RequestMapping(value ="updateShop",method = { RequestMethod.POST })
	public ResponseEntity<String> updateShop(
			@RequestParam(value = "deviceNo") String deviceNo,
			@RequestParam(value = "app") String app,
			@RequestParam(value = "shopNos")String shopNos,
			@RequestParam(value = "nums")String nums){
		String data =null;
		String msg=null;
		int code =SUCESS;
		try {
			boolean flag =this.iShopCartService.updateShop(deviceNo,app,shopNos,nums);
			if(!flag){
				code =FAIL;
				msg="修改购物车数量失败";
			}
		} catch (Exception e) {
			e.printStackTrace();
			code =SERVER_ERR;
		}
		return super.resultInfo(data, code, msg);
	}
	
	/**
	 * TODO 编辑多个购物车数量(id版)
	 * @param userId
	 * @param shopNos
	 * @param nums
	 * @return
	 */
	@RequestMapping(value ="updateShopForId",method = { RequestMethod.POST })
	public ResponseEntity<String> updateShop(
			@RequestParam(value = "userId") String userId,
			@RequestParam(value = "shopNos")String shopNos,
			@RequestParam(value = "nums")String nums){
		String data =null;
		String msg=null;
		int code =SUCESS;
		try {
			boolean flag =this.iShopCartService.updateShopForId(userId,shopNos,nums);
			if(!flag){
				code =FAIL;
				msg="修改购物车数量失败";
			}
		} catch (Exception e) {
			e.printStackTrace();
			code =SERVER_ERR;
		}
		return super.resultInfo(data, code, msg);
	}
	
	/**
	 * tengh 2016年5月19日 下午8:26:43
	 * @param deviceNo
	 * @param app
	 * @param shopNos
	 * @return
	 * TODO 查询账户购物车和订单数量
	 */
	@RequestMapping(value ="getOrderShopNum",method = { RequestMethod.GET })
	public ResponseEntity<String> getOrderShopNum (
			@RequestParam(value = "deviceNo") String deviceNo,
			@RequestParam(value = "app") String app){
		String data =null;
		String msg=null;
		int code =SUCESS;
		try {
			data =this.iShopCartService.getOrderShopNum(deviceNo,app);
			if(StringUtils.isBlank(data)){
				code =EMPTY;
				msg="账户不存在";
			}
		} catch (Exception e) {
			e.printStackTrace();
			code =SERVER_ERR;
		}
		return super.resultInfo(data, code, msg);
	}
	
	/**
	 * TODO 查询账户购物车和订单数量(id版)
	 * @param userId
	 * @return
	 */
	@RequestMapping(value ="getOrderShopNumForId",method = { RequestMethod.GET })
	public ResponseEntity<String> getOrderShopNumForId (@RequestParam(value = "userId") String userId){
		String data =null;
		String msg=null;
		int code =SUCESS;
		try {
			data =this.iShopCartService.getOrderShopNumForId(userId);
			if(StringUtils.isBlank(data)){
				code =EMPTY;
				msg="账户不存在";
			}
		} catch (Exception e) {
			e.printStackTrace();
			code =SERVER_ERR;
		}
		return super.resultInfo(data, code, msg);
	}
	
	/**
	 * tengh 2016年5月19日 下午8:43:56
	 * @param deviceNo
	 * @param app
	 * @param shopNos
	 * @return
	 * TODO 购物车生成订单
	 */
	@RequestMapping(value ="createOrderByShops",method = { RequestMethod.POST })
	public ResponseEntity<String> createOrderByShops(
			@RequestParam(value = "deviceNo") String deviceNo,
			@RequestParam(value = "app") String app,
			@RequestParam(value = "shopNos") String shopNos,
			@RequestParam(value = "payId") String payId,
			@RequestParam(value = "addressId") String addressId,
			@RequestParam(value = "couponId",required=false) String couponId){
		String data =null;
		String msg=null;
		int code =SUCESS;
		try {
			data =this.iShopCartService.createOrderByShops(deviceNo,app,shopNos,payId,addressId,couponId);
			if(StringUtils.isBlank(data)){
				code =FAIL;
				msg="生成订单失败";
			}
		} catch (Exception e) {
			e.printStackTrace();
			code =SERVER_ERR;
		}
		return super.resultInfo(data, code, msg);
	}
	
	/**
	 * 
	 * TODO 通过购物车生成订单 
	 * @param userId 
	 * @param app 
	 * @param shopNos 
	 * @param payId
	 * @param addressId
	 * @param couponId
	 * @param message
	 * @return
	 * @author yuanqi 2017年2月8日 下午8:14:07
	 */
	@RequestMapping(value ="createOrderByShopsForId",method = { RequestMethod.POST })
	public ResponseEntity<String> createOrderByShopsForId(
			@RequestParam(value = "userId") String userId,
			@RequestParam(value = "app") String app,
			@RequestParam(value = "shopNos") String shopNos,
			@RequestParam(value = "payId") String payId,
			@RequestParam(value = "addressId") String addressId,
			@RequestParam(value = "couponId",required=false) String couponId,
			@RequestParam(value = "message",defaultValue="") String message){
		String data =null;
		String msg=null;
		int code =SUCESS;
		try {
			data =this.iShopCartService.createOrderByShopsForId(userId,shopNos,payId,addressId,couponId,app,message);
			if(StringUtils.isBlank(data)){
				code =FAIL;
				msg="生成订单失败";
			}
		} catch (Exception e) {
			e.printStackTrace();
			code =SERVER_ERR;
		}
		return super.resultInfo(data, code, msg);
	}

}
