package cn._51app.dao.diy2_0;

import java.util.List;
import java.util.Map;

import cn._51app.entity.EvaluationInfo;

public interface IEvaluationDao2 {

	Integer saveEvaluation(EvaluationInfo info,String mobile,String texture) throws Exception;

	void updateEvaStatus(String orderNo);

	String getEvalTypeNum(Map<String,Object> paramMap) throws Exception;

	String findEvalPic(Map<String, Object> paramMap) throws Exception;

	String findAllEval(Map<String, Object> paramMap) throws Exception;

	Map<String, Object> getMobile4Order(String order);


}
