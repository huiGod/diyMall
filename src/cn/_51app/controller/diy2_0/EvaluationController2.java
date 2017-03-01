package cn._51app.controller.diy2_0;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import cn._51app.controller.BaseController;
import cn._51app.entity.Evaluations;
import cn._51app.service.diy2_0.IEvaluationService2;
import cn._51app.util.BASE64;

@Controller
@RequestMapping("/evaluation2/")
public class EvaluationController2 extends BaseController {
	
	@Autowired
	private IEvaluationService2 service2;
	
	
	@RequestMapping("toevaluation")
	public String tonice(String id,
			Model model){
			model.addAttribute("id", new Integer(id));
		return "comment";
	}
	
	/**
	 * @author zhanglz
	 * @return 保存评论
	 */
	@RequestMapping(value = "saveEvaluation", method = { RequestMethod.POST })
	public ResponseEntity<String> saveEvaluation(@RequestParam("param") String param){
		String data =null;
		int code = SERVER_ERR;
		try {
			Gson gson=new GsonBuilder().create();
			Evaluations evaluations = gson.fromJson(new String(BASE64.decode(param)),Evaluations.class);
			if(service2.saveEvaluation(evaluations))
				code=SUCESS;
			else
				code=FAIL;
			return super.resultInfo(data,code, null);
		} catch (Exception e) {
			code=SERVER_ERR;
			return super.resultInfo(data,code, null);
		}
	}
	
	
	/**
	 * @author zhanglz
	 * @return 查询评论类型的数量
	 */
	@RequestMapping("evalNum")
	public ResponseEntity<String> getEvalTypeNum(Integer goodsId){
		
		String data =null;
		String msg=null;
		int code =SUCESS;
		try {
			String json =this.service2.getEvalTypeNum(goodsId);
			if(json==null||json.equals("")||json.length()<=2){
				code =EMPTY;
			}else{
				data=json;
			}
		} catch (Exception e) {
			code =SERVER_ERR;
		}
		return super.resultInfo(data, code, msg);
	}
	
	/**
	 * 
	 * @author yuanqi   
	 * @datetime 2016年12月6日  下午3:46:10
	 * @param goodsId 商品编号
	 * @param goodsType 商品类型： 0 非定制商品 ；1定制商品
	 * @return
	 * TODO
	 */
	@RequestMapping("evalNum2")
	public ResponseEntity<String> getEvalTypeNum(Integer goodsId,Integer goodsType){
		
		String data =null;
		String msg=null;
		int code =SUCESS;
		try {
			String json =this.service2.getEvalTypeNum(goodsId,goodsType);
			if(json==null||json.equals("")||json.length()<=2){
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
	 * @author zhanglz
	 * @return 查询评论的图片
	 */
	@RequestMapping("evalPic")
	public ResponseEntity<String> findEvalPic(Integer page,Integer goodsId){
		if(page==null)
			page=1;
		String data =null;
		String msg=null;
		int code =SUCESS;
		try {
			String json =this.service2.findEvalPic(page, goodsId);
			if(json==null||json.equals("")||json.length()<=2){
				code =EMPTY;
			}else{
				data=json;
			}
		} catch (Exception e) {
			code =SERVER_ERR;
		}
		return super.resultInfo(data, code, msg);
	}
	
	/**
	 * @author yuanqi
	 * @param page    
	 * @param goodsId
	 * @param goodsType
	 * @return
	 */
	@RequestMapping("evalPic2")
	public ResponseEntity<String> findEvalPic(Integer page,Integer goodsId,Integer goodsType){
		if(page==null)
			page=1;
		String data =null;
		String msg=null;
		int code =SUCESS;
		try {
			String json =this.service2.findEvalPic(page, goodsId,goodsType);
			if(json==null||json.equals("")||json.length()<=2){
				code =EMPTY;
			}else{
				data=json;
			}
		} catch (Exception e) {
			code =SERVER_ERR;
		}
		return super.resultInfo(data, code, msg);
	}
	
	
	
	
	
	/**
	 * 
	 * @author yuanqi   
	 * @datetime 2016年12月6日  下午3:19:49
	 * @param page 页码
	 * @param goodsId 商品id
	 * @param evalType 评论类型
	 * @param goodsType 0非定制商品 1定制商品
	 * @return
	 * TODO 分页查找评论
	 */
	@RequestMapping("evalPage")
	public ResponseEntity<String> findAllEval(Integer page,Integer goodsId,Integer evalType,Integer goodsType){
		if(page==null)
			page=1;
		if(goodsId==null || goodsType == null){
			return super.resultInfo(null, FAIL, null);
		}
		if (evalType == null) {
			evalType = 0;
		}
		
		String data =null;
		String msg=null;
		int code =SUCESS;
		try {
			String json =this.service2.findAllEval(page, goodsId,evalType,goodsType);
			if(json==null||json.equals("")||json.length()<=2){
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
	 * @author zhanglz
	 * @return 保存图片
	 */
	@RequestMapping(value ="saveImg",method = { RequestMethod.POST })
	public ResponseEntity<String> saveImg(@RequestParam(value = "imgFile",required=false) MultipartFile imgFile){
		String data =null;
		String msg=null;
		int code =SUCESS;
		try {
			String json =this.service2.saveImg(imgFile);
			if(json==null||json.equals("")){
				code =EMPTY;
			}else{
				data=json;
			}
		} catch (Exception e) {
			code =SERVER_ERR;
		}
		return super.resultInfo(data, code, msg);
	}
	
	/**
	 * @author zhanglz
	 * @return 保存评论(Android)
	 */
	@RequestMapping(value = "saveEvaluation2", method = { RequestMethod.POST })
	public ResponseEntity<String> saveEvaluation2(String param){
		String data =null;
		String msg=null;
		int code =SUCESS;
		try {
			Gson gson=new GsonBuilder().create();
			Evaluations evaluations = gson.fromJson(new String(BASE64.decode(param)),Evaluations.class);
			if(service2.saveEvaluation2(evaluations))
				code=SUCESS;
			else
				code=FAIL;
		} catch (JsonSyntaxException e) {
			code =SERVER_ERR;
			e.printStackTrace();
		}
		return super.resultInfo(data, code, msg);
	}
	
	/**
	 * TODO 领取砍价券
	 * @return
	 */
	@RequestMapping("/comment")
	public String comment(){
		return "diy2_5/comment";
	}
	
}
