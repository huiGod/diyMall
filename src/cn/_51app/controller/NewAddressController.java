package cn._51app.controller;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import cn._51app.service.INewAddressService;

/**
 * @author Administrator
 * 地址管理
 */
@Controller
@RequestMapping("/newAddress/")
public class NewAddressController extends BaseController{

	
	@Autowired
	private INewAddressService iNewAddressService;
	
	//地址列表
	/**
	 * tengh 2016年5月17日 下午5:26:01
	 * @param deviceNo
	 * @param app
	 * @return
	 * TODO 查询地址
	 */
	@RequestMapping(value ="getAdress",method = { RequestMethod.GET })
	public ResponseEntity<String> getAdress(
			@RequestParam(value = "deviceNo") String deviceNo,
			@RequestParam(value = "app") String app){//软件标识及版本号
		String data =null;
		String msg=null;
		int code =SUCESS;
		try {
			data =iNewAddressService.getAdress(deviceNo,app);
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
	 * tengh 2016年5月17日 下午6:12:09
	 * @param deviceNo
	 * @param app
	 * @param addressId
	 * @return  addressId不存在是添加
	 * TODO 保存、修改地址
	 */
	@RequestMapping(value ="updateAdress",method = { RequestMethod.POST })
	public ResponseEntity<String> updateAdress(
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
			data =iNewAddressService.updateAdress(deviceNo,app,addressId,name,mobile,province,area,isDefault);
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
	 * tengh 2016年5月17日 下午7:55:39
	 * @param deviceNo
	 * @param app
	 * @return
	 * TODO 删除地址
	 */
	@RequestMapping(value ="deleteAdress",method = { RequestMethod.GET })
	public ResponseEntity<String> deleteAdress(@RequestParam(value="deviceNo") String deviceNo,@RequestParam(value="app")String app,@RequestParam(value="addressId")String addressId){
		String data =null;
		String msg=null;
		int code =SUCESS;
		try {
			boolean flag =iNewAddressService.deleteAdress(deviceNo,app,addressId);
			if(flag){
				code=SUCESS;
			}else{
				code =FAIL;
				msg="删除地址";
			}
		} catch (Exception e) {
			e.printStackTrace();
			code =SERVER_ERR;
		}
		return super.resultInfo(data, code, msg);
	}
}
