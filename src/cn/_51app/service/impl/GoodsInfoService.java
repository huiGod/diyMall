package cn._51app.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn._51app.service.BaseService;
import cn._51app.service.IGoodsInfoService;
import cn._51app.util.PropertiesUtil;
import cn._51app.dao.IGoodsInfoDao;
import cn._51app.util.OCSKey;

@Service
public class GoodsInfoService extends BaseService implements IGoodsInfoService{

	@Autowired
	private IGoodsInfoDao dao;
	
	private final String dgurl =PropertiesUtil.getValue("diy.goods.url");
	private final String dgpsize =PropertiesUtil.getValue("diy.goods.page.size");
	private final String homesize =PropertiesUtil.getValue("diy.home.page.size");
	//图片详情and介绍url
	private final String detail=PropertiesUtil.getValue("diy.goods.details");
	//获取版本号
	private final String version1_0=PropertiesUtil.getValue("diy_version_1_0");
	private final String version2_0=PropertiesUtil.getValue("diy_version_2_0");
	
	/**>>Faster
	 * 
	 *  商品首页数据获取
	 */
	public String getGoodsInfoList(Map<String, Object> paramMap) throws Exception {
		//定义商品缓存名称
		String key =OCSKey.DIY_GOODS_PAGE;
		//获取商品初始页码
		Integer startIndex =new Integer(paramMap.get("startIndex").toString());
		//设置缓存名称（名称+页码）
		String cacheKey =key+startIndex;
		//设置每页显示数量
		paramMap.put("pageSize", new Integer(dgpsize));
		//设置前缀
		paramMap.put("dgurl", dgurl);
		//缓存名称封装
		paramMap.put("cacheKey", cacheKey);
		//缓存时间封装
		paramMap.put("cacheTime", super.FOREVER);
		//查询首页数据
		return this.dao.getHomeList(paramMap);
	}

	/**>>Faster
	 * 
	 * 商品选择标题参数
	 */
	public String getGoodsTitleById(String id) throws Exception {
		//获取缓存标签
		String key =OCSKey.DIY_GOODS_ID;
		//缓存+商品ID作为key
		String cacheKey=key+id;
		//传入缓存key、时间、查询id
		Map<String,Object> paramMap =new HashMap<String,Object>();
		paramMap.put("id", id);
		paramMap.put("dgurl", dgurl);
		paramMap.put("cacheKey", cacheKey);
		paramMap.put("cacheTime", super.FOREVER);
		//查询id对应的标题页面
		return this.dao.getGoodsTitleById(paramMap);
	}
	
	/**>>Faster
	 * 
	 * 商品详情and介绍
	 * @param id 商品id
	 */
	public String getGoodsDetails(String id) throws Exception {
		//获取缓存名称
		String key =OCSKey.DIY_GOODS_DETAILS_ID;
		//设置缓存格式
		String cacheKey=key+"_"+id;
		//设置传入Map
		Map<String,Object> paramMap =new HashMap<String,Object>();
		//设置图片前缀
		paramMap.put("dgurl", dgurl);
		//设置商品id
		paramMap.put("id", id);
		//设置缓存名称
		paramMap.put("cacheKey", cacheKey);
		//设置缓存时间
		paramMap.put("cacheTime", super.FOREVER);
		//查询数据库，传入Map
		return this.dao.getGoodsDetails(paramMap);
	}

	/**Faster
	 * 
	 * 商品属性设置
	 */
	@Override
	public String getGoodsBuyParam(String id) throws Exception {
		//获取缓存名称
		String key =OCSKey.DIY_GOODS_BUYPARAM_ID;
		//设置缓存格式
		String cacheKey=key+id;
		//设置传入Map
		Map<String,Object> paramMap =new HashMap<String,Object>();
		//设置商品id
		paramMap.put("id", id);
		//设置缓存名称
		paramMap.put("cacheKey", cacheKey);
		//设置缓存时间
		paramMap.put("cacheTime", super.FOREVER);
		//设置图片前缀
		paramMap.put("dgurl", dgurl);
		//查询数据库，传入Map
		return this.dao.getGoodsShow(paramMap);
	}

	
	/**
	 * @author zhanglz
	 * @param page:当前页
	 * @return 首页
	 */
	@Override
	public String home(int page) throws Exception{
		String key = OCSKey.DIY_HOME_PAGE;
		page-=1;
		String cacheKey = key+page;
		Map<String,Object> paramMap =new HashMap<String,Object>();
		paramMap.put("page", page*(Integer.parseInt(homesize)));
		paramMap.put("pagesize", Integer.parseInt(homesize));
		paramMap.put("dgurl", dgurl);
		paramMap.put("cacheKey", cacheKey);
		paramMap.put("cacheTime", super.HALFDAY);
		return this.dao.home(paramMap);
	}
	
	
	/**
	 * @author zhanglz
	 * @return 精品汇
	 */
	@Override
	public String nice(int page) throws Exception{
		String key = OCSKey.DIY_NICE_PAGE;
		page-=1;
		String cacheKey = key+page;
		Map<String,Object> paramMap =new HashMap<String,Object>();
		paramMap.put("page", page*(Integer.parseInt(homesize)));
		paramMap.put("pagesize", Integer.parseInt(homesize));
		paramMap.put("dgurl", dgurl);
		paramMap.put("cacheKey", cacheKey);
		paramMap.put("cacheTime", super.HALFDAY);
		return this.dao.nice(paramMap);
	}


	@Override
	public String getGoodsChartParamById(Map<String, Object> paramMap) throws Exception {
		String id =paramMap.get("id").toString();
		String cacheKey =OCSKey.DIY_GOODS_CHART_PARAM_ID+id;
		paramMap.put("cacheKey", cacheKey);
		paramMap.put("cacheTime", super.FOREVER);
		return this.dao.getGoodsChartParamById(paramMap);
	}

	@Override
	public String getCfImgUrlList(String param) throws Exception {
		String cacheKey =OCSKey.DIY_GOODS_CF_IMG_ID+param;
		Map<String,Object> paramMap =new HashMap<String,Object>();
		paramMap.put("cacheKey", cacheKey);
		paramMap.put("cacheTime", super.FOREVER);
		paramMap.put("param", param);
		paramMap.put("dgurl", dgurl);
		return this.dao.getPreviewImg(paramMap);
	}
	
	@Override
	public String getCfImgUrlList2(String param) throws Exception {
		String cacheKey =OCSKey.DIY_GOODS_CF_IMG_ID+"param="+param;
		Map<String,Object> paramMap =new HashMap<String,Object>();
		paramMap.put("cacheKey", cacheKey);
		paramMap.put("cacheTime", super.FOREVER);
		paramMap.put("param", param);
		paramMap.put("dgurl", dgurl);
		return this.dao.getPreviewImg2(paramMap);
	}


	/*
	@Override
	public String getGoodsShowImgById(Map<String, Object> paramMap) throws Exception {
		String id=paramMap.get("id").toString();
		String cacheKey=OCSKey.DIY_GOODS_SHOWIMG+id;
		paramMap.put("cacheKey", cacheKey);
		paramMap.put("cacheTime",super.FOREVER);
		return super.q(cacheKey);
	}
	*/
	/**>>Faster
	 * 
	 * 返回首页广告数据
	 */
	@Override
	public String getADImgList() throws Exception {
		//获取首页广告缓存key
		String cacheKey=OCSKey.DIY_GOODS_ADIMG;
		//设置查询参数
		Map<String,Object> paramMap =new HashMap<String,Object>();
		//设置图片前缀时间缓存
		paramMap.put("cacheKey", cacheKey);
		paramMap.put("cacheTime", super.FOREVER);
		paramMap.put("dgurl", dgurl);
		paramMap.put("version1_0",version1_0);
		//获取广告数据
		return this.dao.getADImgList(paramMap);
	}
	
	@Override
	public String newADImgList() throws Exception {
		//获取首页广告缓存key
		String cacheKey=OCSKey.DIY_NEW_GOODS_ADIMG;
		//设置查询参数
		Map<String,Object> paramMap =new HashMap<String,Object>();
		//设置图片前缀时间缓存
		paramMap.put("cacheKey", cacheKey);
		paramMap.put("cacheTime", super.FOREVER);
		paramMap.put("dgurl", dgurl);
		paramMap.put("version2_0",version2_0);
		//获取广告数据
		return this.dao.newADImgList(paramMap);
	}

	/**
	 * 返回广告跳转页面
	 */
	@Override
	public String getADImgList(String id) throws Exception {
		String cacheKey=OCSKey.DIY_AD_IMGLIST;
		Map<String,Object> paramMap =new HashMap<String,Object>();
		//设置图片前缀时间缓存
		paramMap.put("cacheKey", cacheKey+"_"+id);
		paramMap.put("cacheTime", super.FOREVER);
		paramMap.put("dgurl", dgurl);
		paramMap.put("id",id);
		return this.dao.getADJumpImg(paramMap);
	}
	
	@Override
	public String youHome() throws Exception{
		String key = OCSKey.YOU_HOME;
		String cacheKey = key;
		Map<String,Object> paramMap =new HashMap<String,Object>();
		paramMap.put("dgurl", dgurl);
		paramMap.put("cacheKey", cacheKey);
		paramMap.put("cacheTime", super.HALFDAY);
		return this.dao.youHome(paramMap);
	}

	@Override
	public String youGoods(Integer id) throws Exception {
		String key =OCSKey.YOU_GOODSID;
		String cacheKey=key+id;
		Map<String,Object> paramMap =new HashMap<String,Object>();
		paramMap.put("id", id);
		paramMap.put("dgurl", dgurl);
		paramMap.put("cacheKey", cacheKey);
		paramMap.put("cacheTime", super.FOREVER);
		return this.dao.youGoods(paramMap);
	}

	@Override
	public String youHome2() throws Exception {
		String key = OCSKey.YOU_HOME2;
		String cacheKey = key;
		Map<String,Object>paramMap = new HashMap<String,Object>();
		paramMap.put("dgurl", dgurl);
		paramMap.put("cacheKey", cacheKey);
		paramMap.put("cacheTime",super.HALFDAY);
		return this.dao.youHome2(paramMap);
	}

	@Override
	public String homeV2() throws Exception {
		String key = OCSKey.DIY_HOME_V2;
		String cacheKey = key;
		Map<String,Object>paramMap = new HashMap<String,Object>();
		paramMap.put("dgurl", dgurl);
		paramMap.put("cacheKey", cacheKey);
		paramMap.put("cacheTime",super.HALFDAY);
		return this.dao.homeV2(paramMap);
	}
	
	@Override
	public String getGoodsChartParamByIdV2(Map<String, Object> paramMap) throws Exception {
		String id =paramMap.get("id").toString();
		String cacheKey =OCSKey.DIY_GOODS_CHART_PARAM_ID_V2+id;
		paramMap.put("cacheKey", cacheKey);
		paramMap.put("cacheTime", super.FOREVER);
		return this.dao.getGoodsChartParamByIdV2(paramMap);
	}
}
