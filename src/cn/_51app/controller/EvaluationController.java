package cn._51app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import cn._51app.entity.Evaluations;
import cn._51app.service.IEvaluationService;
import cn._51app.util.BASE64;

@Controller
@RequestMapping("/evaluation/")
public class EvaluationController extends BaseController {
	
	@Autowired
	private IEvaluationService service;
	
	
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
			if(service.saveEvaluation(evaluations))
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
			String json =this.service.getEvalTypeNum(goodsId);
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
			String json =this.service.findEvalPic(page, goodsId);
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
	 * @author zhanglz
	 * @return 分页查询评论
	 */
	@RequestMapping("evalPage")
	public ResponseEntity<String> findAllEval(Integer page,Integer goodsId,Integer evalType){
		if(page==null)
			page=1;
		if(goodsId==null)
			return super.resultInfo(null, FAIL, null);
		String data =null;
		String msg=null;
		int code =SUCESS;
		try {
			String json =this.service.findAllEval(page, goodsId,evalType);
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
	 * @author zhanglz
	 * @return 保存图片
	 */
	@RequestMapping(value ="saveImg",method = { RequestMethod.POST })
	public ResponseEntity<String> saveImg(@RequestParam(value = "imgFile",required=false) MultipartFile imgFile){
		String data =null;
		String msg=null;
		int code =SUCESS;
		try {
			String json =this.service.saveImg(imgFile);
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
	public ResponseEntity<String> saveEvaluation2(String json){
		String data =null;
		String msg=null;
		int code =SUCESS;
		try {
			Gson gson=new GsonBuilder().create();
			Evaluations evaluations = gson.fromJson(json,Evaluations.class);
			if(service.saveEvaluation2(evaluations))
				code=SUCESS;
			else
				code=FAIL;
		} catch (JsonSyntaxException e) {
			code =SERVER_ERR;
			e.printStackTrace();
		}
		return super.resultInfo(data, code, msg);
	}
	
}
