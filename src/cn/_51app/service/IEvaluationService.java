package cn._51app.service;


import org.springframework.web.multipart.MultipartFile;

import cn._51app.entity.Evaluations;

public interface IEvaluationService {

	boolean saveEvaluation(Evaluations evaluations);

	String getEvalTypeNum(int goodsId) throws Exception;
	
	String findEvalPic(int page, Integer goodsId) throws Exception;

	String findAllEval(int page, int goodsId, Integer evalType)
			throws Exception;

	String saveImg(MultipartFile imgFile);

	boolean saveEvaluation2(Evaluations evaluations);


}
