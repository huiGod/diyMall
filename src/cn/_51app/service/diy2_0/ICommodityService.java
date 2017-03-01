package cn._51app.service.diy2_0;

import java.util.Map;

public interface ICommodityService {

	//获取商品分类标题
	public String getGoodsSortCat() throws Exception;
	
	//获取定制商品
	public String customize(String id) throws Exception;

	//根据id查询模板
	String materialList(String id,String module) throws Exception;
	
	//根据id查询图片
	String photoList(String id) throws Exception;

	//发现列表
	String findList(String page) throws Exception;

	//发现详情
	String findDetail(String id) throws Exception;

	//商品属性
	String goodsProperty(String id,String type) throws Exception;

	//精品商品参数
	String getGoodsChartParamById(Map<String, Object> paramMap) throws Exception;

	//商品标题
	String getGoodsTitleById(String id) throws Exception;

	//获取下载文字
	String getFontList() throws Exception;

	//商品界面
	String goods(Integer id) throws Exception;

	//贴纸页面
	String tagsList(String id) throws Exception;

	/**
	 * TODO定制商品（临时用）
	 * @return
	 * @throws Exception
	 */
	String customizeTest() throws Exception;

	/**
	 * TODO定制页面图标
	 * @param type
	 * @return
	 * @throws Exception
	 */
	String production(String type, String page) throws Exception;

	/**
	 * TODO 定制分类的商品
	 * @param sortId
	 * @return
	 * @throws Exception
	 */
	String getMakeSort(String sortId) throws Exception;

	/**
	 * TODO 商品详情
	 * @param id
	 * @return
	 * @throws Exception
	 */
	String getGoodsDetails(String id, String isBoutique) throws Exception;

	/**
	 * TODO 材质选择台历分横竖版
	 * @param id
	 * @param type
	 * @return
	 * @throws Exception
	 */
	String goodsProperty2(String id, String type) throws Exception;

	/**
	 * TODO 材质选择台历分横竖版
	 * @param id
	 * @param type
	 * @return
	 * @throws Exception
	 */
	String goodsProperty3(String id, String type) throws Exception;
	
}
