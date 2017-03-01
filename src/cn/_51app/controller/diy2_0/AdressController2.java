package cn._51app.controller.diy2_0;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import cn._51app.controller.BaseController;
import cn._51app.service.diy2_0.impl.AdressService2;

@Controller
@RequestMapping("address2")
public class AdressController2 extends BaseController {
	
	@Autowired
	private AdressService2 adressService2;
	
	//地址列表
		/**
		 * @param deviceNo
		 * @param app
		 * @return
		 * TODO 查询地址
		 */
		@RequestMapping(value ="getAddress",method = { RequestMethod.GET })
		public ResponseEntity<String> getAddress(
				@RequestParam(value = "deviceNo") String deviceNo,
				@RequestParam(value = "app") String app){//软件标识及版本号
			String data =null;
			String msg=null;
			int code =SUCESS;
			try {
				data =adressService2.getAddress(deviceNo,app);
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
		 * TODO 查询地址(id版本)
		 * @param userId
		 * @return
		 */
		@RequestMapping(value ="getAddressForId",method = { RequestMethod.GET })
		public ResponseEntity<String> getAddressForId(@RequestParam(value = "userId") String userId){//软件标识及版本号
			String data =null;
			String msg=null;
			int code =SUCESS;
			try {
				data =adressService2.getAddressForId(userId);
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
		 * TODO 更新用户地址
		 * @param deviceNo
		 * @param app
		 * @param addressId
		 * @param name
		 * @param mobile
		 * @param province
		 * @param area
		 * @param isDefault
		 * @return
		 */
		@RequestMapping(value ="updateAddress",method = { RequestMethod.POST })
		public ResponseEntity<String> updateAddress(
				@RequestParam(value = "deviceNo") String deviceNo,
				@RequestParam(value = "app") String app,
				@RequestParam(value = "addressId",required=false) String addressId,
				@RequestParam(value = "name") String name,
				@RequestParam(value = "mobile") String mobile,
				@RequestParam(value = "province") String province,
				@RequestParam(value = "area") String area,
				@RequestParam(value = "isDefault") String isDefault){//软件标识及版本号
			String data =null;
			String msg=null;
			int code =SUCESS;
			try {
				data =adressService2.updateAddress(deviceNo,app,addressId,name,mobile,province,area,isDefault);
				if(StringUtils.isNotBlank(data)){
					code=SUCESS;
				}else{
					code =FAIL;
					msg="修改地址失败";
				}
			} catch (Exception e) {
				e.printStackTrace();
				code =SERVER_ERR;
			}
			return super.resultInfo(data, code, msg);
		}
		
		/**
		 * TODO 更新用户地址id版本
		 * @param userId
		 * @param addressId
		 * @param name
		 * @param mobile
		 * @param province
		 * @param area
		 * @param isDefault
		 * @return
		 */
		@RequestMapping(value ="updateAddressForId",method = { RequestMethod.POST })
		public ResponseEntity<String> updateAddressForId(
				@RequestParam(value = "userId") String userId,
				@RequestParam(value = "addressId",required=false) String addressId,
				@RequestParam(value = "name") String name,
				@RequestParam(value = "mobile") String mobile,
				@RequestParam(value = "province") String province,
				@RequestParam(value = "area") String area,
				@RequestParam(value = "isDefault") String isDefault){//软件标识及版本号
			String data =null;
			String msg=null;
			int code =SUCESS;
			try {
				data =adressService2.updateAddressForId(userId,addressId,name,mobile,province,area,isDefault);
				if(StringUtils.isNotBlank(data)){
					code=SUCESS;
				}else{
					code =FAIL;
					msg="修改地址失败";
				}
			} catch (Exception e) {
				e.printStackTrace();
				code =SERVER_ERR;
			}
			return super.resultInfo(data, code, msg);
		}
		
		/**
		 * @param deviceNo
		 * @param app
		 * @return
		 * TODO 删除地址
		 */
		@RequestMapping(value ="deleteAddress",method = { RequestMethod.GET })
		public ResponseEntity<String> deleteAddress(@RequestParam(value="deviceNo") String deviceNo,@RequestParam(value="app")String app,@RequestParam(value="addressId")String addressId){
			String data =null;
			String msg=null;
			int code =SUCESS;
			try {
				boolean flag =adressService2.deleteAddress(deviceNo,app,addressId);
				if(flag){
					code=SUCESS;
				}else{
					code =FAIL;
					msg="删除地址失败";
				}
			} catch (Exception e) {
				e.printStackTrace();
				code =SERVER_ERR;
			}
			return super.resultInfo(data, code, msg);
		}
		
		/**
		 * TODO 删除地址（id版）
		 * @param userId
		 * @param addressId
		 * @return
		 */
		@RequestMapping(value ="deleteAddressForId",method = { RequestMethod.GET })
		public ResponseEntity<String> deleteAddressForId(@RequestParam(value="userId")String userId,@RequestParam(value="addressId")String addressId){
			String data =null;
			String msg=null;
			int code =SUCESS;
			try {
				boolean flag =adressService2.deleteAddressForId(userId,addressId);
				if(flag){
					code=SUCESS;
				}else{
					code =FAIL;
					msg="删除地址失败";
				}
			} catch (Exception e) {
				e.printStackTrace();
				code =SERVER_ERR;
			}
			return super.resultInfo(data, code, msg);
		}
		
		/**
		 * TODO 修改为默认地址
		 * @param deviceNo
		 * @param app
		 * @param addressId
		 * @return
		 */
		@RequestMapping(value ="isDefault",method = { RequestMethod.GET })
		public ResponseEntity<String>isDeafult(@RequestParam(value="deviceNo") String deviceNo,@RequestParam(value="app")String app,@RequestParam(value="addressId")String addressId){
			String data =null;
			String msg=null;
			int code =SUCESS;
			try {
				boolean flag =adressService2.isDefault(deviceNo,app,addressId);
				if(flag){
					code=SUCESS;
				}else{
					code =FAIL;
				}
			} catch (Exception e) {
				e.printStackTrace();
				code =SERVER_ERR;
			}
			return super.resultInfo(data, code, msg);
		}
		
		/**
		 * TODO 修改为默认地址
		 * @param app
		 * @param addressId
		 * @return
		 */
		@RequestMapping(value ="isDefaultForId",method = { RequestMethod.GET })
		public ResponseEntity<String>isDeafultForId(@RequestParam(value="userId")String userId,@RequestParam(value="addressId")String addressId){
			String data =null;
			String msg=null;
			int code =SUCESS;
			try {
				boolean flag =adressService2.isDefaultForId(userId,addressId);
				if(flag){
					code=SUCESS;
				}else{
					code =FAIL;
				}
			} catch (Exception e) {
				e.printStackTrace();
				code =SERVER_ERR;
			}
			return super.resultInfo(data, code, msg);
		}
		
		/**
		 * tengh 2016年12月5日 下午5:28:37
		 * @param id
		 * @return
		 * TODO 判断地址是不是偏远
		 */
		@RequestMapping(value ="isRemote",method = { RequestMethod.GET })
		public ResponseEntity<String>isRemote(@RequestParam(value="id") String id){
			String data =null;
			String msg=null;
			int code =SUCESS;
			try {
				boolean flag =adressService2.isRemote(id);
				if(!flag){
					code=FAIL;
				}
			} catch (Exception e) {
				e.printStackTrace();
				code =SERVER_ERR;
			}
			return super.resultInfo(data, code, msg);
		}
}
