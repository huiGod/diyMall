package cn._51app.controller.diy2_0;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import cn._51app.controller.BaseController;
import cn._51app.service.diy2_0.ISearchGoodsService;
import cn._51app.util.CommonUtil;

/**
 * 商品搜索
 * 
 * @author yuanqi
 * 
 */

@Controller
@RequestMapping("/searchGoods")
public class SearchGoodsController extends BaseController {

	@Autowired
	private ISearchGoodsService searchGoodsService;

	/**
	 * 
	 * TODO 根据名称所搜商品的关键字 
	 * @param name
	 * @return
	 * @throws Exception
	 * @author yuanqi 2017年2月17日 下午4:06:46
	 */
	@RequestMapping(value = "/getKeyword",method = { RequestMethod.POST }) 
	public ResponseEntity<String> getKeyword(
			@RequestParam(value = "name") String name) throws Exception {
		String data = null;
		String msg = null;
		int code = SUCESS;
		

		try {
			if (name != null && !name.equals("")) {
				name = CommonUtil.encodeStr(name.trim());
				data = this.searchGoodsService.getKeywords(name);
			} 
			
			if(StringUtils.isBlank(data) || data.length()<=2){
				code=EMPTY;
				msg="没有数据";
			}
		} catch (Exception e) {
			e.printStackTrace();
			code = SERVER_ERR;
		}
		return super.resultInfo(data, code, msg);
	}

	/**
	 * 
	 * TODO 查询商品信息
	 * @param userId
	 * @param page
	 * @param name 所搜值
	 * @param goodsName 商品名称
	 * @param keyword 关键字名称
	 * @return
	 * @throws Exception
	 * @author yuanqi 2017年2月22日 上午10:22:07
	 */
//	@RequestMapping(value = "/getGoodsInfo")
//	public ResponseEntity<String> getGoodsInfo(
//			@RequestParam(value = "userId") String userId,
//			@RequestParam(value = "logName")String logName,
//			Integer page,String name,
//			Integer goods_id,Integer keyword_id) throws Exception {
//		
//		String data = null;
//		String msg = null;
//		int code = SUCESS;
//		
//		
//		if (name != null && !name.equals("")) {
//			name = new String(name.getBytes("iso-8859-1"),"utf-8");
//		}
//		
//		if (logName != null && !logName.equals("")) {
//			logName = new String(logName.getBytes("iso-8859-1"),"utf-8");
//		}else {
//			return  super.resultInfo(null, FAIL, null);
//		}
//		
//		if (page == null || page <=0) {
//			page = 1;
//		}
//		page --;
//		try {
//			this.searchGoodsService.addSearchLog(userId, logName);
//			data = this.searchGoodsService.getGoodsInfo(page,name,goods_id,keyword_id);
//			
//			if(StringUtils.isBlank(data) || data.length()<=2){
//				code=EMPTY;
//				msg="没有数据";
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			code = SERVER_ERR;
//		}
//		return super.resultInfo(data, code, msg);
//	}
	
	@RequestMapping(value = "/getGoodsInfo",method = { RequestMethod.POST })
	public ResponseEntity<String> getGoodsInfo(
			@RequestParam(value = "userId") String userId,
			@RequestParam(value = "page") Integer page,
			@RequestParam(value = "name")String name) throws Exception{
		String data = null;
		String msg = null;
		int code = SUCESS;
		
		
		
		if (name == null || name.trim().equals("")) {
			return super.resultInfo(null, FAIL, null);
		}else {
			System.out.println("getGoodsInfo : " + name);
			name = CommonUtil.encodeStr(name);
		}
		System.out.println("getGoodsInfo ::: "+ name);
		
		if (page == null) {
			page = 0;
		}
		
		try {
			this.searchGoodsService.addSearchLog(userId, name.split(" ")[0]);			
			data = this.searchGoodsService.getGoodsInfo(page,name);
			
			if(StringUtils.isBlank(data) || data.length()<=2){
				code=EMPTY;
				msg="没有数据";
			}
		} catch (Exception e) {
			e.printStackTrace();
			code = SERVER_ERR;
		}
		
		return super.resultInfo(data, code, msg);
	}
	
	
	@RequestMapping(value = "/getRecommand")
	public ResponseEntity<String> getRecommand(){
		String data = null;
		String msg = null;
		int code = SUCESS;

		try {
			data = searchGoodsService.getRecommand();
			if(StringUtils.isBlank(data)){
				code=EMPTY;
				msg="没有查询记录";
			}
		} catch (Exception e) {
			e.printStackTrace();
			code = SERVER_ERR;
		}
		return super.resultInfo(data, code, msg);
	}
	
	
	/**
	 * 
	 * TODO 获取搜索记录与热门推荐 
	 * @param userId
	 * @return
	 * @author yuanqi 2017年2月20日 下午8:23:10
	 */
	@RequestMapping(value = "/getLogs")
	public ResponseEntity<String> getLogs(String userId){
		String data = null;
		String msg = null;
		int code = SUCESS;

		try {
			data = searchGoodsService.getLogs(userId);
			if(StringUtils.isBlank(data)){
				code=EMPTY;
				msg="没有查询记录";
			}
		} catch (Exception e) {
			e.printStackTrace();
			code = SERVER_ERR;
		}
		return super.resultInfo(data, code, msg);
	}
	
	@RequestMapping(value = "/clearLogs")
	public ResponseEntity<String> clearLogs(String userId){
		String data = null;
		String msg = null;
		int code = SUCESS;

		try {
			
			if (searchGoodsService.clearLogs(userId)) {
				data = "true";
			}else {
				data = "false";
				msg = "没有搜索记录";
			}
		} catch (Exception e) {
			e.printStackTrace();
			code = SERVER_ERR;
		}
		return super.resultInfo(data, code, msg);
	}
	
	@RequestMapping("/searchResult")
	public String searchResult(){
		return "diy2_5/search-result";
	}
}
