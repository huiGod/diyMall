package cn._51app.service.diy2_0.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mysql.fabric.xmlrpc.base.Data;

import cn._51app.dao.diy2_0.ICommodityDao;
import cn._51app.dao.ocs.OCSDao;
import cn._51app.service.BaseService;
import cn._51app.service.diy2_0.ICommodityService;
import cn._51app.util.JSONUtil;
import cn._51app.util.OCSKey;
import cn._51app.util.PropertiesUtil;

@Service
public class CommodityService extends BaseService implements ICommodityService {
	
	@Autowired
	private ICommodityDao iCommodityDao; 
	
	@Autowired
	private OCSDao ocsDao;
	
	//图片前缀
	private final String dgurl=PropertiesUtil.getValue("diy.goods.url");
	//限制页数
	private final String pages=PropertiesUtil.getValue("diy.home.page.size");
	//模板前缀
	private final String dmRootUrl =PropertiesUtil.getValue("diy.material.url");
	//发现列表一页数量
	private final String number=PropertiesUtil.getValue("diy.findlist.size");

	@Override
	public String getGoodsSortCat() throws Exception {
		Map<String,Object>paramMap=new HashMap<String,Object>();
		String key=OCSKey.DIY_GOODS_SORT_CAT;
		paramMap.put("dgurl",dgurl);
		paramMap.put("cacheKey",key+pages);
		paramMap.put("pages",pages);
		paramMap.put("cacheTime", super.FOREVER);
		return iCommodityDao.getGoodsSortCat(paramMap);
	}

	@Override
	public String customize(String id) throws Exception {
		Map<String,Object>paramMap=new HashMap<String,Object>();
		String key=OCSKey.DIY_GOODS_CUSTOMIZE+"_"+id;
		paramMap.put("dgurl",dgurl);
		paramMap.put("cacheKey",key);
		paramMap.put("cacheTime",super.FOREVER);
		paramMap.put("id",id);
		return iCommodityDao.getCustomize(paramMap);
	}
	
	@Override
	public String customizeTest() throws Exception {
		Map<String,Object>paramMap=new HashMap<String,Object>();
		String key=OCSKey.DIY_GOODS_CUSTOMIZE;
		paramMap.put("dgurl",dgurl);
		paramMap.put("cacheKey",key);
		paramMap.put("cacheTime",super.FOREVER);
		return iCommodityDao.getCustomizeTest(paramMap);
	}
	
	@Override
	public String materialList(String id,String module)throws Exception{
		Map<String,Object>paramMap=new HashMap<String,Object>();
		String key=OCSKey.DIY_GOODS_MATERIAL+":"+module+":"+id;
		paramMap.put("dmRootUrl",dmRootUrl);
		paramMap.put("cacheKey",key);
		paramMap.put("module",module);
		paramMap.put("id",id);
		paramMap.put("cacheTime",super.FOREVER);
		return iCommodityDao.getMaterial(paramMap);
	}

	@Override
	public String photoList(String id) throws Exception {
		Map<String,Object>paramMap=new HashMap<String,Object>();
		String key=OCSKey.DIY_GOODS_PHOTO+":"+id;
		paramMap.put("dgurl",dgurl);
		paramMap.put("cacheKey",key);
		paramMap.put("id",id);
		paramMap.put("cacheTime",super.FOREVER);
		return iCommodityDao.photoList(paramMap);
	}
	
	@Override
	public String tagsList(String id) throws Exception {
		Map<String,Object>paramMap=new HashMap<String,Object>();
		String key=OCSKey.DIY_GOODS_TAGS+":"+id;
		paramMap.put("dgurl",dgurl);
		paramMap.put("cacheKey",key);
		paramMap.put("id",id);
		paramMap.put("cacheTime",super.FOREVER);
		return iCommodityDao.tagList(paramMap);
	}
	
	@Override
	public String findList(String page) throws Exception{
		Map<String,Object>paramMap=new HashMap<String,Object>();
		String key=OCSKey.DIY_GOODS_FIND+"_"+page;
		int index=0,size=8;
		if(number!=null){
			size=Integer.parseInt(number);
		}
		if(page!=null){
			index=Integer.parseInt(page);
		}
		paramMap.put("index", index*size);
		paramMap.put("rows",size);
		paramMap.put("dgurl",dgurl);
		paramMap.put("cacheKey",key);
		paramMap.put("cacheTime", super.FOREVER);
		return iCommodityDao.findList(paramMap);
	}
	
	@Override
	public String findDetail(String id) throws Exception{
		Map<String,Object>paramMap=new HashMap<String,Object>();
		String key=OCSKey.DIY_GOODS_FIND+id;
		paramMap.put("dgurl",dgurl);
		paramMap.put("cacheKey",key);
		paramMap.put("cacheTime", super.FOREVER);
		paramMap.put("id",id);
		return iCommodityDao.findDetail(paramMap);
	}
	
	@Override
	public String goodsProperty(String id,String type) throws Exception{
		Map<String,Object>paramMap=new HashMap<String,Object>();
		String key=OCSKey.DIY_GOODS_PROPERTY+id+"_"+type;
		paramMap.put("dgurl",dgurl);
		paramMap.put("cacheKey",key);
		paramMap.put("cacheTime",super.FOREVER);
		paramMap.put("id",id);
		paramMap.put("type",type);
		return iCommodityDao.goodsProperty(paramMap);
	}
	
	@Override
	public String goodsProperty2(String id,String type) throws Exception{
		Map<String,Object>paramMap=new HashMap<String,Object>();
		String key=OCSKey.DIY_GOODS_PROPERTY2+id+"_"+type;
		paramMap.put("dgurl",dgurl);
		paramMap.put("cacheKey",key);
		paramMap.put("cacheTime",super.FOREVER);
		paramMap.put("id",id);
		paramMap.put("type",type);
		return iCommodityDao.goodsProperty2(paramMap);
	}
	
	@Override
	public String goodsProperty3(String id,String type) throws Exception{
		Map<String,Object>paramMap=new HashMap<String,Object>();
		String key=OCSKey.DIY_GOODS_PROPERTY3+id+"_"+type;
		paramMap.put("dgurl",dgurl);
		paramMap.put("cacheKey",key);
		paramMap.put("cacheTime",super.FOREVER);
		paramMap.put("id",id);
		paramMap.put("type",type);
		return iCommodityDao.goodsProperty3(paramMap);
	}
	
	@Override
	public String getGoodsChartParamById(Map<String,Object>paramMap)throws Exception{
		String id =paramMap.get("id").toString();
		String cacheKey =OCSKey.DIY_GOODS_CHART_PARAM+id;
		paramMap.put("dgurl",dgurl);
		paramMap.put("cacheKey", cacheKey);
		paramMap.put("cacheTime", super.FOREVER);
		return this.iCommodityDao.getGoodsChartParamById(paramMap);
	}
	
	@Override
	public String getGoodsTitleById(String id)throws Exception{
		//获取缓存标签
		String key =OCSKey.DIY_GOODS_TITLE;
		//缓存+商品ID作为key
		String cacheKey=key+id;
		//传入缓存key、时间、查询id
		Map<String,Object> paramMap =new HashMap<String,Object>();
		paramMap.put("id", id);
		paramMap.put("dgurl", dgurl);
		paramMap.put("cacheKey", cacheKey);
		paramMap.put("cacheTime", super.FOREVER);
		//查询id对应的标题页面
		return this.iCommodityDao.getGoodsTitleById(paramMap);
	}
	
	@Override
	public String getFontList()throws Exception{
		String key=OCSKey.DIY_FONT_LIST;
		Map<String,Object>paramMap=new HashMap<String,Object>();
		paramMap.put("dgurl",dgurl);
		paramMap.put("cacheKey",key);
		paramMap.put("cacheTime",super.FOREVER);
		return this.iCommodityDao.getFontList(paramMap);
	}
	
	@Override
	public String goods(Integer id) throws Exception {
		String key =OCSKey.DIY_GOODS_INFO;
		String cacheKey=key+id;
		Map<String,Object> paramMap =new HashMap<String,Object>();
		paramMap.put("id", id);
		paramMap.put("dgurl", dgurl);
		paramMap.put("cacheKey", cacheKey);
		paramMap.put("cacheTime", super.FOREVER);
		return this.iCommodityDao.goods(paramMap);
	}
	
	@Override
	public String production(String type,String page)throws Exception{
		String production=this.ocsDao.query(OCSKey.DIY_MAKE_HOME+"_"+type+"_"+page);
		Map<String,Object>result=new HashMap<>();
		if(StringUtils.isBlank(production)){
			Map<String,Object>paramMap=new HashMap<String,Object>();
			int index=0,size=8;
			if(number!=null){
				size=Integer.parseInt(number);
			}
			if(page!=null){
				index=Integer.parseInt(page);
			}
			paramMap.put("index", index*size);
			paramMap.put("rows",size);
			paramMap.put("type",Integer.parseInt(type));
			paramMap.put("dgurl",dgurl);
			production=this.toJson(this.iCommodityDao.production(paramMap));
			ocsDao.insert(OCSKey.DIY_MAKE_HOME, production,0);
		}
		return production;
	}
	
	@Override
	public String getMakeSort(String sortId)throws Exception{
		String key=OCSKey.DIY_MAKE_SORT+"_"+sortId;
		Map<String,Object>paramMap=new HashMap<String,Object>();
		paramMap.put("sortId",Integer.parseInt(sortId));
		paramMap.put("dgurl",dgurl);
		paramMap.put("cacheKey",key);
		paramMap.put("cacheTime",super.FOREVER);
		return this.iCommodityDao.getMakeSort(paramMap);
	}
	
	@Override
	public String getGoodsDetails(String id,String isBoutique)throws Exception{
		//获取缓存名称
		String key =OCSKey.DIY_GOODS_DETAIL;
		//设置缓存格式
		String cacheKey=key+"_"+id+"_"+isBoutique;
		//设置传入Map
		Map<String,Object> paramMap =new HashMap<String,Object>();
		//设置图片前缀
		paramMap.put("dgurl", dgurl);
		//设置商品id
		paramMap.put("id", id);
		//设置缓存名称
		paramMap.put("cacheKey", cacheKey);
		paramMap.put("isBoutique", isBoutique);
		//设置缓存时间
		paramMap.put("cacheTime", super.FOREVER);
		//查询数据库，传入Map
		return this.iCommodityDao.getGoodsDetails(paramMap);
	}

}
