package cn._51app.service.diy2_0;


import org.springframework.web.multipart.MultipartFile;

import cn._51app.entity.Evaluations;


public interface IEvaluationService2 {

	boolean saveEvaluation(Evaluations evaluations);

	String getEvalTypeNum(int goodsId) throws Exception;
	
	/**
	 * @author yuanqi   
	 * @datetime 2016年12月6日  下午3:47:53
	 * @param goodsId 商品id
	 * @param goodsType 商品类型
	 * @return
	 * @throws Exception
	 * TODO
	 */
	String getEvalTypeNum(int goodsId,int goodsType) throws Exception;
	
	String findEvalPic(int page, Integer goodsId) throws Exception;
	
	String findEvalPic(int page, Integer goodsId, Integer goodsType) throws Exception;
	
	/**
	 * 
	 * @author yuanqi   
	 * @datetime 2016年12月3日  下午5:26:55
	 * @param page 页数
	 * @param goodsId 商品编号
	 * @param evalType  评论类型：好评、中评、差评
	 * @param goodsType 商品类型：0非定制商品(默认) 1定制商品  
	 * @return
	 * @throws Exception
	 * TODO
	 */
	String findAllEval(int page, int goodsId, Integer evalType,Integer goodsType)
			throws Exception;



	String saveImg(MultipartFile imgFile);

	boolean saveEvaluation2(Evaluations evaluations);


}
