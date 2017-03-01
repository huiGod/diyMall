package cn._51app.dao.diy2_0;

import java.util.List;
import java.util.Map;

public interface ICommodityDao {
	
	public String getGoodsSortCat(Map<String,Object>paramMap) throws Exception;
	
	public String getCustomize(Map<String,Object>paramMap) throws Exception;

	String getMaterial(Map<String, Object> paramMap) throws Exception;

	String photoList(Map<String, Object> paramMap) throws Exception;

	String findList(Map<String, Object> paramMap) throws Exception;

	String findDetail(Map<String, Object> paramMap) throws Exception;

	String goodsProperty(Map<String, Object> paramMap) throws Exception;

	String getGoodsChartParamById(Map<String, Object> paramMap) throws Exception;

	String getGoodsTitleById(Map<String, Object> paramMap) throws Exception;

	String getSelectTexture(Map<String, Object> paramMap) throws Exception;

	/**
	 * TODO 获取字体列表
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	String getFontList(Map<String, Object> paramMap) throws Exception;

	/**
	 * 商品页面
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	String goods(Map<String, Object> paramMap) throws Exception;

	/**
	 * TODO 获取标签列表
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	String tagList(Map<String, Object> paramMap) throws Exception;

	/**
	 * TODO 定制商品数据（测试版）
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	String getCustomizeTest(Map<String, Object> paramMap) throws Exception;

	/**
	 * TODO 定制页面图标
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	List<Map<String,Object>> production(Map<String, Object> paramMap) throws Exception;

	/**
	 * TODO 定制分类的商品
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	String getMakeSort(Map<String, Object> paramMap) throws Exception;

	/**
	 * TODO 定制商品详情
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	String getGoodsDetails(Map<String, Object> paramMap) throws Exception;

	/**
	 * TODO 材质选择横版
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	String goodsProperty2(Map<String, Object> paramMap) throws Exception;

	/**
	 * TODO 材质选择竖版
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	String goodsProperty3(Map<String, Object> paramMap) throws Exception;

}
