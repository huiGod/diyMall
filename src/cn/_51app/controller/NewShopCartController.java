package cn._51app.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import cn._51app.service.INewShopCartService;
@Controller
@RequestMapping("/newShopCart/")
public class NewShopCartController extends BaseController{
	
	@Autowired
	private INewShopCartService inewShopCartService;
	
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
			data =this.inewShopCartService.shopList(deviceNo,app,page);
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
			@RequestParam(value = "app") String app){
		String data =null;
		String msg=null;
		int code =SUCESS;
		try {
			boolean flag =this.inewShopCartService.addShop(imgFile,imgBackFile,previewFile,previewBackFile,infoId,textureIds,deviceNo,num,app);
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
			boolean flag =this.inewShopCartService.deleteShop(deviceNo,app,shopNos);
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
			boolean flag =this.inewShopCartService.updateShop(deviceNo,app,shopNos,nums);
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
	public ResponseEntity<String> getOrderShopNum(
			@RequestParam(value = "deviceNo") String deviceNo,
			@RequestParam(value = "app") String app){
		String data =null;
		String msg=null;
		int code =SUCESS;
		try {
			data =this.inewShopCartService.getOrderShopNum(deviceNo,app);
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
			data =this.inewShopCartService.createOrderByShops(deviceNo,app,shopNos,payId,addressId,couponId);
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
