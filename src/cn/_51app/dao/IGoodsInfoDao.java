package cn._51app.dao;

import java.util.List;
import java.util.Map;

public interface IGoodsInfoDao {
	
	String getGoodsInfoList(Map<String,Object> paramMap) throws Exception;
	
	/**>>Faster
	 * 
	 * @param paramMap
	 * @return	返回选择标题属性
	 * @throws Exception
	 */
	String getGoodsTitleById(Map<String,Object> paramMap) throws Exception;
	
	String getGoodsEditChart(Map<String,Object> paramMap) throws Exception;
	
	/**>>Faster
	 * 
	 * @param paramMap
	 * @return 商品详情and介绍
	 * @throws Exception
	 */
	String getGoodsDetails(Map<String,Object> paramMap) throws Exception;
	
	/**>>Faster
	 * 
	 * @param id
	 * @return 选择材质属性
	 * @throws Exception
	 */
	String getSelectTexture(Map<String,Object>paramMap)throws Exception;
	
	String getGoodsBuyParam(Map<String,Object> paramMap) throws Exception;

	String home(Map<String, Object> paramMap) throws Exception;

	Map<String,Object> getGoodsTypeIdAndParam(Map<String,Object> paramMap) throws Exception;
	
	int insertShopCart(Map<String,Object> paramMap) throws Exception;
	
	int insertOrderGoods(Map<String,Object> paramMap) throws Exception;
	
	String getGoodsChartParamById(Map<String,Object> paramMap) throws Exception;

	String nice(Map<String, Object> paramMap) throws Exception;
	
	String getJPImgUrl(String name) throws Exception;
	
	List<Map<String,Object>> getCfImgUrlList(Map<String,Object> paramMap) throws Exception;
	
	void getGoodsShowImgList(Map<String,Object> paramMap) throws Exception;
	
	String getADImgList(Map<String,Object> paramMap)throws Exception;
	
	String getADJumpImg(Map<String,Object>paramMap)throws Exception;

	/**>>Faster
	 * 
	 * @param paramMap
	 * @return 返回首页商品
	 * @throws Exception
	 */
	String getHomeList(Map<String, Object> paramMap) throws Exception;

	String getGoodsShow(Map<String, Object> paramMap) throws Exception;

	String getPreviewImg(Map<String, Object> paramMap) throws Exception;
	
	/**
	 * TODO 获取商品预览图
	 * @param paramMap
	 * @return
	 * @throws Exception
	 * @author yuanqi 2016年12月12日 下午6:26:02
	 */
	String getPreviewImg2(Map<String, Object> paramMap) throws Exception;

	String youHome(Map<String, Object> paramMap) throws Exception;

	String youGoods(Map<String, Object> paramMap) throws Exception;
	
	String youHome2(Map<String,Object>paramMap) throws Exception;

	String homeV2(Map<String, Object> paramMap) throws Exception;

	String getGoodsChartParamByIdV2(Map<String, Object> paramMap)
			throws Exception;

	/**
	 * TODO 1.0改2.0版本首页导航条
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	String newADImgList(Map<String, Object> paramMap) throws Exception;
}
