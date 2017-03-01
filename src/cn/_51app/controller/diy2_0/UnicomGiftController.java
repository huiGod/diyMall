package cn._51app.controller.diy2_0;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import cn._51app.controller.BaseController;
import cn._51app.service.diy2_0.IUnicomGiftService;
import cn._51app.util.CheckPhoneUtil;

@Controller
@RequestMapping("/unicom/")
public class UnicomGiftController extends BaseController {
	
	private static final String NOT_LOGIN = "0"; //没有登录
	private static final String NOT_UNICOM = "1";// 不是联通手机号码
	private static final String RECEIVE_SUCCESS = "2"; // 领取成功
	private static final String RECEIVED= "3"; // 已经领取（再次领取会失败）
	private static final String UNRECEIVED = "4"; // 还没领取(可以领取)

	@Autowired
	private IUnicomGiftService iUnicomGiftService;

	/**
	 * 
	 * TODO 联通手机号码领取奖品 (通过微信公众号操作的)
	 * 
	 * @param mobile
	 *            手机号码
	 * @return
	 * @author yuanqi 2017年2月5日 上午9:22:29
	 */
	@RequestMapping(value = "addGift", method = { RequestMethod.GET })
	public ResponseEntity<String> addGift(
			@RequestParam(value = "mobile") String mobile) {

		String data = null;
		String msg = null;
		int code = SUCESS;
		try {
			if (CheckPhoneUtil.isCompanyPhone(mobile) || CheckPhoneUtil.isChinaUnicomPhoneNum(mobile)) {
				boolean success = this.iUnicomGiftService.addGift(mobile);
				if (success) {
					data = RECEIVE_SUCCESS;
					msg = "领取成功";
				}else {//领取失败，说明已经领取了
					data = RECEIVED;
					msg = "已经领取了";
				}
			} else {
				data = NOT_UNICOM;
				msg = "不是联通手机号码";
			}
		} catch (Exception e) {
			code = SERVER_ERR;
		}
		return super.resultInfo(data, code, msg);
	}
	
	/**
	 * 
	 * TODO 获取领取奖品总数 
	 * @return
	 * @author yuanqi 2017年2月5日 上午10:44:45
	 */
	@RequestMapping(value = "receiveNum", method = { RequestMethod.GET })
	public ResponseEntity<String> receiveNum() {
		String data = null;
		String msg = null;
		int code = SUCESS;
		try {
			int count = iUnicomGiftService.receiveNum();
			data = String.valueOf(count);
		} catch (Exception e) {
			e.printStackTrace();
			code = SERVER_ERR;
		}
		return super.resultInfo(data, code, msg);
	}

	/**
	 * 
	 * TODO 判断是否已经领取奖品 
	 * @return
	 * @author yuanqi 2017年2月5日 上午10:44:45
	 */
	@RequestMapping(value = "isAddToshop", method = { RequestMethod.GET })
	public ResponseEntity<String> isAddToshop(@RequestParam(value = "userId") String userId){
		String data = null;
		String msg = null;
		int code = SUCESS;
	
		try {
			String json =this.iUnicomGiftService.isReceiveByUser(userId);
			if(json==null||json.equals("")||json.length()<=2){
				code =EMPTY;
			}else{
				data=json;
			}
		}catch (Exception e) {
			code = SERVER_ERR;
		}
		
		return super.resultInfo(data, code, msg);
	}
	
	/**
	 * 
	 * TODO  用户领取奖品(通过唯乐购应用领取)
	 * @param userId 用户id
	 * @param goodsId 奖品id
	 * @return
	 * @author yuanqi017年2月5日 上午10:47:10
	 */
	@RequestMapping(value = "addToShop", method = { RequestMethod.GET })
	public ResponseEntity<String> addToShop(
			@RequestParam(value = "userId") String userId,
			@RequestParam(value = "goodsId") String goodsId,
			@RequestParam(value = "textureIds") String textureIds,
			@RequestParam(value = "textureName") String textureName){
		
		String data = null;
		String msg = null;
		int code = SUCESS;
		
		try {
			if (iUnicomGiftService.isLogin(userId)) {
				String mobile = iUnicomGiftService.getMobileByUser(userId);
				if (CheckPhoneUtil.isCompanyPhone(mobile) || CheckPhoneUtil.isChinaUnicomPhoneNum(mobile)) {
					if(StringUtils.isNotBlank(textureName)){
						textureName = new String(textureName.getBytes("iso-8859-1"),"utf-8");
					}				
					boolean success = this.iUnicomGiftService.addToShop(userId, goodsId, textureIds, textureName);
					if (success) {
						data = RECEIVE_SUCCESS;
						msg = "领取成功";
					}else {//领取失败，说明已经领取了
						data = RECEIVED;
						msg = "已经领取了";
					}
				} else {
					data = NOT_UNICOM;
					msg = "不是联通手机号码";
				}
			}else {
				data = NOT_LOGIN;
				msg = "没有登录";
			}
		} catch (Exception e) {
			code = SERVER_ERR;
		}
		
		return super.resultInfo(data, code, msg);
	}
	
	/**
	 * TODO 联通活动页面
	 * @return
	 */
	@RequestMapping("/liantongActivity")
	public String liantongActivity(){
		return "diy2_5/liantongActivity";
	}
	
	/**
	 * TODO 联通微信页面
	 * @return
	 */
	@RequestMapping("/liantongActivityWeixin")
	public String liantongActivityWeixin(){
		return "diy2_5/liantongActivityWeixin";
	}

	/**
	 * TODO 联通详情页面
	 * @return
	 */
	@RequestMapping("/liantongDiyDetails")
	public String liantongDiyDetails(){
		return "diy2_5/liantong-diyDetails";
	}
	
	/**
	 * 
	 * TODO 获取联通奖品的商品信息
	 * @return
	 * @author yuanqi 2017年2月5日 下午3:31:56
	 */
	@RequestMapping(value = "getUnicomGoods", method = { RequestMethod.GET })
	public ResponseEntity<String> getUnicomGoods(){
		String data = null;
		String msg = null;
		int code = SUCESS;
		
		try {
			String json = iUnicomGiftService.getUnicomGoods();
			if(json==null||json.equals("")||json.length()<=2){
				code =EMPTY;
			}else{
				data=json;
			}
		} catch (Exception e) {
			e.printStackTrace();
			code = SERVER_ERR;
		}
		return super.resultInfo(data, code, msg);
	}
	

	/**
	 * 
	 * TODO 联通领取用户优惠券
	 * @return
	 * @author yuanqi 2017年2月5日 下午3:31:56
	 */
	@RequestMapping(value = "getUnicomCoupon", method = { RequestMethod.GET })
	public ResponseEntity<String> getUnicomCoupon(String userId){
		String data = null;
		String msg = null;
		int code = SUCESS;
		try {
			//判断是否登陆过
			if (iUnicomGiftService.isLogin(userId)){
				//获取用户电话
				String mobile = iUnicomGiftService.getMobileByUser(userId);
				//判断是否是联通用户
				if (CheckPhoneUtil.isCompanyPhone(mobile) || CheckPhoneUtil.isChinaUnicomPhoneNum(mobile)) {
					if (iUnicomGiftService.getUnicomCoupon(userId)) {
						data = RECEIVE_SUCCESS;
						msg = "领取成功";
					} else {
						data = RECEIVED;
						msg = "领取失败";
					}
				}else {
					data = NOT_UNICOM;
					msg = "不是联通手机号码";
				}
			}else {
				data = NOT_LOGIN;
				msg = "没有登录";
			}
		} catch (Exception e) {
			code = SERVER_ERR;
		}
		return super.resultInfo(data, code, msg);
	}
	
//	/**
//	 * 
//	 * TODO 通过微信公众号领取优惠管
//	 * @param userId
//	 * @return
//	 * @author yuanqi 2017年2月8日 上午11:06:38
//	 */
//	@RequestMapping(value = "getUnicomCouponByWeixin", method = { RequestMethod.GET })
//	public ResponseEntity<String> getUnicomCouponByWeixin(String userId){
//		String data = null;
//		String msg = null;
//		int code = SUCESS;
//		try {
//			//判断是否登陆过
//			if (iUnicomGiftService.isLogin(userId)){
//				//获取用户电话
//				String mobile = iUnicomGiftService.getMobileByUser(userId);
//				if (CheckPhoneUtil.isCompanyPhone(mobile) || CheckPhoneUtil.isChinaUnicomPhoneNum(mobile)) {
//					iUnicomGiftService.getUnicomCouponByWeixin(userId,mobile);
//				}
//			}
//		}catch (Exception e) {
//			code = SERVER_ERR;
//		}
//		return super.resultInfo(data, code, msg);
//	}
//	
	
}
