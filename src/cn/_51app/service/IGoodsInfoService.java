package cn._51app.service;

import java.util.Map;

public interface IGoodsInfoService {
	
	 /**>>Faster
	  * 
	  * @param paramMap
	  * @return 返回首页商品
	  * @throws Exception
	  */
	 String getGoodsInfoList(Map<String, Object> paramMap) throws Exception;
	 
	 /**>>Faster
	  * 
	  * @param id
	  * @return 获取选择标题属性
	  * @throws Exception
	  */
	 String getGoodsTitleById(String id)throws Exception;
	 
	 /**>>Faster
	  * 
	  * 商品介绍and详情
	  * @param id 商品id
	  * @return
	  * @throws Exception
	  */
	 String getGoodsDetails(String id)throws Exception;
	 
	 /**>>Faster
	  * 
	  * @param id 广告id
	  * @return 页面图片
	  * @throws Exception
	  */
	 String getADImgList(String id)throws Exception;
	 
	 String getGoodsBuyParam(String id) throws Exception;
	 
	 String home(int page) throws Exception;
	 
	 String getGoodsChartParamById(Map<String,Object> paramMap)throws Exception;

	 String nice(int page) throws Exception;
	 
	 String getCfImgUrlList(String param)throws Exception;
	 
	 String getCfImgUrlList2(String param)throws Exception;
	 
	 //String getGoodsShowImgById(Map<String,Object>paramMap)throws Exception;
	 
	 String getADImgList()throws Exception;

	String youHome() throws Exception;

	String youGoods(Integer id) throws Exception;
	
	String youHome2()throws Exception;

	String homeV2() throws Exception;

	String getGoodsChartParamByIdV2(Map<String, Object> paramMap)
			throws Exception;

	/**
	 * TODO 新修改1.0广告条
	 * @return
	 * @throws Exception
	 */
	String newADImgList() throws Exception;
	
}
