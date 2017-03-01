package cn._51app.service.diy2_0.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.sql.visitor.functions.Now;

import cn._51app.dao.diy2_0.IIdexDao;
import cn._51app.dao.ocs.OCSDao;
import cn._51app.service.diy2_0.IIndexService;
import cn._51app.util.JSONUtil;
import cn._51app.util.OCSKey;
import cn._51app.util.PropertiesUtil;

@Service
public class IIdexServiceImpl implements IIndexService{

	@Autowired
	private IIdexDao iIdexDao;
	@Autowired
	private OCSDao ocsDao;
	private String preImgUrl=PropertiesUtil.getValue("diy.goods.url");
	
	@Override
	public String home() {
		SimpleDateFormat dateFormat=new SimpleDateFormat("yy-mm-dd");
		String homeData=this.ocsDao.query(OCSKey.DIY_HOME_DATA+"_"+dateFormat.format(new Date()));
		if(StringUtils.isBlank(homeData)){
			Map<String, Object> result=new HashMap<>();
			Map<String, Object> tempMap=null;
			//顶部导航
			List<Map<String, Object>> topNav=new ArrayList<>();
			//中间菜单
			List<Map<String, Object>> midNav=new ArrayList<>();
			//首页轮播图
			Integer bannerOfBetterId=0;  //精选菜单id
			List<Map<String, Object>> banners=new ArrayList<>();
			//今日特价商品
			List<Map<String, Object>> todayGoods=new ArrayList<>();
			//首页专题推荐
			List<Map<String, Object>> specials=new ArrayList<>();
			//首页推荐商品
			List<Map<String, Object>> recommendGoods=new ArrayList<>();
			/**
			 * 菜单处理
			 */
			List<Map<String, Object>> homeButton=this.iIdexDao.getHomeButton(1);
			for (Map<String, Object> map : homeButton) {
				tempMap=new HashMap<>();
				int type=(Integer)map.get("type");
				int id=(Integer)map.get("id");
				String name=(String)map.get("name");
				String imgUrl=(String)map.get("img_url");
				String key=(String)map.get("key");
				String about=(String)map.get("about");
				Integer pf=(Integer)map.get("platform");
				tempMap.put("id", id);
				tempMap.put("name", name);
				//获取到精选首页的id
				if(1==type){
					if(bannerOfBetterId==0){
						bannerOfBetterId=id;
					}
					topNav.add(tempMap);
				}else if(2==type){
					tempMap.put("key", key);
					tempMap.put("about", about);
					 if(pf!=null && pf==1)
						tempMap.put("platform","ios");
					else if(pf!=null && pf==2)
						tempMap.put("platform","android");
					else 
						tempMap.put("platform","无");
					tempMap.put("imgUrl",preImgUrl+imgUrl);
					midNav.add(tempMap);
				}
				
			}
			
			/**
			 * banner处理
			 */
			banners=this.iIdexDao.getBannerBySource(1,bannerOfBetterId,1);
			for(Map<String,Object>bmap : banners){
				String about=bmap.get("about")!=null?bmap.get("about").toString():"";
				String type=bmap.get("type")!=null?bmap.get("type").toString():"";
				if((about.equals("4") || about.equals("7")) && type.equals("2")){
					bmap.put("isAndroidHide",true);
				}else{
					bmap.put("isAndroidHide",false);
				}
			}
			for (int i = 0; i < banners.size(); i++) {
				String imgUrl=(String)banners.get(i).get("imgUrl");
				banners.get(i).put("imgUrl", preImgUrl+imgUrl);
			}
			
			/**
			 * 处理今日特价商品
			 */
			todayGoods=this.iIdexDao.getTodayGoods();
			for (int i = 0; i < todayGoods.size(); i++) {
				String icoUrl=(String)todayGoods.get(i).get("icoUrl");
				Integer companyId=(Integer)(todayGoods.get(i).get("companyId")==null?"0":todayGoods.get(i).get("companyId"));
				Integer act=(Integer)(todayGoods.get(i).get("act")==null?"":todayGoods.get(i).get("act"));
				String goodId=todayGoods.get(i).get("goodId")!=null?todayGoods.get(i).get("goodId").toString():"";
				String type=todayGoods.get(i).get("type")!=null?todayGoods.get(i).get("type").toString():"";
				
				if((goodId.equals("4") || goodId.equals("7")) && type.equals("2")){
					todayGoods.get(i).put("isAndroidHide",true);
				}else{
					todayGoods.get(i).put("isAndroidHide",false);
				}
				
				switch(act){
				case 1:todayGoods.get(i).put("act", "满减");break;
				case 2:todayGoods.get(i).put("act", "活动");break;
				case 4:todayGoods.get(i).put("act", "活动");break;
				case 8:todayGoods.get(i).put("act", "折扣");break;
				default:todayGoods.get(i).put("act", "无");
				}
				switch(companyId){
				case 2:todayGoods.get(i).put("isSelf", "true");break;
				case 3:todayGoods.get(i).put("isSelf", "true");break;
				default:todayGoods.get(i).put("isSelf", "false");
				}
				todayGoods.get(i).put("icoUrl", preImgUrl+icoUrl);
				todayGoods.get(i).remove("sort");
				todayGoods.get(i).remove("showDate");
			}
			
			/**
			 * 首页专题推荐
			 */
			specials=this.iIdexDao.getHomeSpecials();
			for (int i = 0; i < specials.size(); i++) {
				String imgUrl=(String)specials.get(i).get("imgUrl");
				specials.get(i).put("imgUrl", preImgUrl+imgUrl);
			}
			
			/**
			 * 首页推荐商品  (查询第一页)
			 */
			recommendGoods=this.iIdexDao.getGoodsByTopNavIdByPage(bannerOfBetterId,0);
			for (int i = 0; i < recommendGoods.size(); i++) {
				String icoUrl=(String)recommendGoods.get(i).get("icoUrl");
				Integer companyId=(Integer)(recommendGoods.get(i).get("companyId")==null?"0":recommendGoods.get(i).get("companyId"));
				Integer act=(Integer)(recommendGoods.get(i).get("act")==null?"":recommendGoods.get(i).get("act"));
				switch(act){
					case 1:recommendGoods.get(i).put("act", "满减");break;
					case 2:recommendGoods.get(i).put("act", "活动");break;
					case 4:recommendGoods.get(i).put("act", "活动");break;
					case 8:recommendGoods.get(i).put("act", "折扣");break;
					default:recommendGoods.get(i).put("act", "无");
				}
				switch(companyId){
				case 2:recommendGoods.get(i).put("isSelf", "true");break;
				case 3:recommendGoods.get(i).put("isSelf", "true");break;
				default:recommendGoods.get(i).put("isSelf", "false");
				}
				recommendGoods.get(i).put("icoUrl", preImgUrl+icoUrl);
				recommendGoods.get(i).remove("homeRecommend");
			}
			
			result.put("topNav", topNav);
			result.put("midNav", midNav);
			result.put("banners", banners);
			result.put("todayGoods", todayGoods);
			result.put("specials", specials);
			result.put("recommendGoods", recommendGoods);
			homeData=JSONUtil.convertObjectToJSON(result);
			ocsDao.insert(OCSKey.DIY_HOME_DATA+"_"+dateFormat.format(new Date()), homeData,0);
		}
		return homeData;
	}

	@Override
	public String homeRecommendByPage(int page) {
		String json=this.ocsDao.query(OCSKey.DIY_HOME_BETTER_+":"+page);
		if(StringUtils.isBlank(json)){
			List<Map<String, Object>> homeButton=this.iIdexDao.getHomeButton(1);
			Integer bannerOfBetterId=0;  //精选菜单id
			for (Map<String, Object> map : homeButton) {
				int type=(Integer)map.get("type");
				int id=(Integer)map.get("id");
			
				if(1==type){
					if(bannerOfBetterId==0){
						bannerOfBetterId=id;
					}
				}
			}
			List<Map<String, Object>> list=this.iIdexDao.getGoodsByTopNavIdByPage(bannerOfBetterId,page);
			if(list.size()<=0 || list==null){
				 return null;
			}
			for (int i = 0; i < list.size(); i++) {
				String icoUrl=(String)list.get(i).get("icoUrl");
				Integer companyId=(Integer)(list.get(i).get("companyId")==null?"0":list.get(i).get("companyId"));
				Integer act=(Integer)(list.get(i).get("act")==null?"":list.get(i).get("act"));
				switch(act){
				case 1:list.get(i).put("act", "满减");break;
				case 2:list.get(i).put("act", "活动");break;
				case 4:list.get(i).put("act", "活动");break;
				case 8:list.get(i).put("act", "折扣");break;
				default:list.get(i).put("act", "无");
				}
				switch(companyId){
				case 2:list.get(i).put("isSelf", "true");break;
				case 3:list.get(i).put("isSelf", "true");break;
				default:list.get(i).put("isSelf", "false");
				}
				list.get(i).put("icoUrl", preImgUrl+icoUrl);
				list.get(i).remove("homeRecommend");
			}
			json=JSONUtil.convertArrayToJSON(list);
			ocsDao.insert(OCSKey.DIY_HOME_BETTER_+":"+page, json, 0);
		}
		return json;
	}
	
	@Override
	public String goodRecommendBypage(int page,String storeId) {
		String json=this.ocsDao.query(OCSKey.DIY_STORE_BETTER_+":"+storeId+":"+page);
		if(StringUtils.isBlank(json)){
			List<Map<String, Object>> list=this.iIdexDao.goodRecommendBypage(page,storeId);
			if(list.size()<=0 || list==null){
				 return null;
			}
			for (int i = 0; i < list.size(); i++) {
				String icoUrl=(String)list.get(i).get("icoUrl");
				Integer companyId=(Integer)(list.get(i).get("companyId")==null?"0":list.get(i).get("companyId"));
				Integer act=(Integer)(list.get(i).get("act")==null?"":list.get(i).get("act"));
				switch(act){
				case 1:list.get(i).put("act", "满减");break;
				case 2:list.get(i).put("act", "活动");break;
				case 4:list.get(i).put("act", "活动");break;
				case 8:list.get(i).put("act", "折扣");break;
				default:list.get(i).put("act", "无");
				}
				switch(companyId){
				case 2:list.get(i).put("isSelf", "true");break;
				case 3:list.get(i).put("isSelf", "true");break;
				default:list.get(i).put("isSelf", "false");
				}
				list.get(i).put("icoUrl", preImgUrl+icoUrl);
				list.get(i).remove("homeRecommend");
			}
			json=JSONUtil.convertArrayToJSON(list);
			ocsDao.insert(OCSKey.DIY_STORE_BETTER_+":"+storeId+":"+page, json, 0);
		}
		return json;
	}
	
	@Override
	public String getTopNavInfoById(int id) {
		String json=this.ocsDao.query(OCSKey.DIY_HOME_TOPNAV_+":"+id);
		if(StringUtils.isBlank(json)){
			Map<String, Object> result=new HashMap<>();
			List<Map<String, Object>> banners=this.iIdexDao.getBannerBySource(1, id,1);
			for (int i = 0; i < banners.size(); i++) {
				String imgUrl=(String)banners.get(i).get("imgUrl");
				banners.get(i).put("imgUrl", preImgUrl+imgUrl);
			}
			List<Map<String, Object>> recommendGoods=this.iIdexDao.getGoodsByTopNavIdByPage(id,0);
			for (int i = 0; i < recommendGoods.size(); i++) {
				String icoUrl=(String)recommendGoods.get(i).get("icoUrl");
				recommendGoods.get(i).put("icoUrl", preImgUrl+icoUrl);
				recommendGoods.get(i).remove("sort");
			}
			result.put("banners", banners);
			result.put("recommendGoods", recommendGoods);
			json=JSONUtil.convertObjectToJSON(result);
			ocsDao.insert(OCSKey.DIY_HOME_TOPNAV_+":"+id, json, 0);
		}
		return json;
	}
	
	@Override
	public String topNavInfoByPage(int id, int page) {
		String json=this.ocsDao.query(OCSKey.DIY_HOME_TOPNAV_PAGE_+":"+id+":"+page);
		if(StringUtils.isBlank(json)){
			List<Map<String, Object>> recommendGoods=this.iIdexDao.getGoodsByTopNavIdByPage(id,page);
			for (int i = 0; i < recommendGoods.size(); i++) {
				String icoUrl=(String)recommendGoods.get(i).get("icoUrl");
				Integer companyId=(Integer)(recommendGoods.get(i).get("companyId")==null?"0":recommendGoods.get(i).get("companyId"));
				Integer act=(Integer)(recommendGoods.get(i).get("act")==null?"":recommendGoods.get(i).get("act"));
				switch(act){
				case 1:recommendGoods.get(i).put("act", "满减");break;
				case 2:recommendGoods.get(i).put("act", "活动");break;
				case 4:recommendGoods.get(i).put("act", "活动");break;
				case 8:recommendGoods.get(i).put("act", "折扣");break;
				default:recommendGoods.get(i).put("act", "无");
				}
				switch(companyId){
				case 2:recommendGoods.get(i).put("isSelf", "true");break;
				case 3:recommendGoods.get(i).put("isSelf", "true");break;
				default:recommendGoods.get(i).put("isSelf", "false");
				}
				recommendGoods.get(i).put("icoUrl", preImgUrl+icoUrl);
				recommendGoods.get(i).remove("sort");
			}
			json=JSONUtil.convertArrayToJSON(recommendGoods);
			ocsDao.insert(OCSKey.DIY_HOME_TOPNAV_PAGE_+":"+id+":"+page, json, 0);
		}
		return json;
	}
	
	@Override
	public String specialInfoById(int type, int id) {
		String json=this.ocsDao.query(OCSKey.DIY_SPECIAL_ID_+id);
		if(StringUtils.isBlank(json)){
			Map<String, Object> result=new HashMap<>();
			List<Map<String, Object>> banners=this.iIdexDao.getBannerBySource(2, id,1);
			for (int i = 0; i < banners.size(); i++) {
				String imgUrl=(String)banners.get(i).get("imgUrl");
				banners.get(i).put("imgUrl", preImgUrl+imgUrl);
			}
			List<Map<String, Object>> recommendGoods=this.iIdexDao.getSpecialOfGoods(id,type);
			for (int i = 0; i < recommendGoods.size(); i++) {
				String icoUrl=(String)recommendGoods.get(i).get("icoUrl");
				Integer companyId=(Integer)(recommendGoods.get(i).get("companyId")==null?"0":recommendGoods.get(i).get("companyId"));
				Integer act=(Integer)(recommendGoods.get(i).get("act")==null?"":recommendGoods.get(i).get("act"));
				switch(act){
				case 1:recommendGoods.get(i).put("act", "满减");break;
				case 2:recommendGoods.get(i).put("act", "活动");break;
				case 4:recommendGoods.get(i).put("act", "活动");break;
				case 8:recommendGoods.get(i).put("act", "折扣");break;
				default:recommendGoods.get(i).put("act", "无");
				}
				switch(companyId){
				case 2:recommendGoods.get(i).put("isSelf", "true");break;
				case 3:recommendGoods.get(i).put("isSelf", "true");break;
				default:recommendGoods.get(i).put("isSelf", "false");
				}
				recommendGoods.get(i).put("icoUrl", preImgUrl+icoUrl);
				recommendGoods.get(i).remove("sort");
			}
			result.put("banners", banners);
			result.put("recommendGoods", recommendGoods);
			json=JSONUtil.convertObjectToJSON(result);
			ocsDao.insert(OCSKey.DIY_SPECIAL_ID_+id, json, 0);
		}
		return json;
	}
	
	@Override
	public String sortHome(String topType){
		String json=this.ocsDao.query(OCSKey.DIY_GOODS_SORT+"_"+topType);
		if(StringUtils.isBlank(json)){
			Map<String, Object> result=new HashMap<>();
			Map<String, Object> tempMap=null;
			//顶部导航
			List<Map<String, Object>> topNav=new ArrayList<>();
			//中间菜单
			List<Map<String, Object>> midNav=new ArrayList<>();
			//首页轮播图
			Integer bannerOfBetterId=0;  //精选菜单id
			List<Map<String, Object>> banners=new ArrayList<>();
			//今日特价商品
			List<Map<String, Object>> todayGoods=new ArrayList<>();
			//首页专题推荐
			List<Map<String, Object>> specials=new ArrayList<>();
			//首页推荐商品
			List<Map<String, Object>> recommendGoods=new ArrayList<>();
			/**
			 * 菜单处理
			 */
			List<Map<String, Object>> homeButton=this.iIdexDao.getHomeButton(1);
			for (Map<String, Object> map : homeButton) {
				tempMap=new HashMap<>();
				int type=(Integer)map.get("type");
				int id=(Integer)map.get("id");
				String name=(String)map.get("name");
				String imgUrl=(String)map.get("img_url");
				String key=(String)map.get("key");
				String about=(String)map.get("about");
				tempMap.put("id", id);
				tempMap.put("name", name);
				if("topNav".equals(topType)){
					if(1==type){
						topNav.add(tempMap);
					}
				}else if("classify".equals(topType)){
					if(3==type){
						topNav.add(tempMap);
						tempMap.put("imgUrl", preImgUrl+imgUrl);
					}
				}
			}
			
			result.put("topNav", topNav);
			json=JSONUtil.convertObjectToJSON(result);
			ocsDao.insert(OCSKey.DIY_GOODS_SORT+"_"+topType, json,0);
		}
		return json;
	}
	
	@Override
	public Map<String, Object> boundMobile(String userId, String mobile) {
		Map<String, Object> result=new HashMap<>();
		String msg="";
		boolean flag=true;
		int resultNum=this.iIdexDao.updateMobile(userId,mobile);
		if(resultNum==0){
			flag=false;
			msg="手机号已绑定其他账号~";
		}
		result.put("msg", msg);
		result.put("flag", flag);
		return result;
	}
	
	@Override
	public String updateVersion() {
//		List<Map<String, Object>> list=new ArrayList<>();
//		Map<String, Object> result=new HashMap<>();
//		result.put("version", "1.1.1");
//		result.put("name", "唯乐购");
//		result.put("boundId", "com.wcl.market");
//		result.put("code", 1);
//		result.put("url", "http://img.bizhi.51app.cn/test.apk");
//		result.put("content", "更新的内容");
//		list.add(result);
		
		List<Map<String, Object>> list = iIdexDao.getVerionList();
		if (list != null) {
			for (Map<String, Object> map : list) {
				String url = (String) map.get("url");
				if (url != null && url.length()>0) {
					map.put("url", preImgUrl + map.get("url"));
				}
			}
		}
		
		return JSONUtil.convertArrayToJSON(list);
	}

	@Override
	public String homeRevision() {
		SimpleDateFormat dateFormat=new SimpleDateFormat("yy-mm-dd");
		String homeData=this.ocsDao.query(OCSKey.DIY_HOME_NEWVERSION+"_"+dateFormat.format(new Date()));
		if(StringUtils.isBlank(homeData)){
			Map<String, Object> result=new HashMap<>();
			Map<String, Object> tempMap=null;
			//顶部导航
			List<Map<String, Object>> topNav=new ArrayList<>();
			//中间菜单
			List<Map<String, Object>> midNav=new ArrayList<>();
			//首页轮播图
			Integer bannerOfBetterId=0;  //精选菜单id
			List<Map<String, Object>> banners=new ArrayList<>();
			//handpick goods
			List<Map<String, Object>> handpick=new ArrayList<>();
			//customization goods
			List<Map<String, Object>> customization=new ArrayList<>();
			//customization map
			Map<String,Object> layout=new HashMap<String,Object>();
			/**
			 * 菜单处理
			 */
			List<Map<String, Object>> homeButton=this.iIdexDao.getHomeButton(2);
			for (Map<String, Object> map : homeButton) {
				tempMap=new HashMap<>();
				int type=(Integer)map.get("type");
				int id=(Integer)map.get("id");
				String name=(String)map.get("name");
				String imgUrl=(String)map.get("img_url");
				String key=(String)map.get("key");
				String about=(String)map.get("about");
				Integer pf=(Integer)map.get("platform");
				tempMap.put("id", id);
				tempMap.put("name", name);
				//获取到精选首页的id
				if(1==type){
					if(bannerOfBetterId==0){
						bannerOfBetterId=id;
					}
					topNav.add(tempMap);
				}else if(2==type){
					tempMap.put("key", key);
					tempMap.put("about", about);
					 if(pf!=null && pf==1)
						tempMap.put("platform","ios");
					else if(pf!=null && pf==2)
						tempMap.put("platform","android");
					else 
						tempMap.put("platform","无");
					tempMap.put("imgUrl",preImgUrl+imgUrl);
					midNav.add(tempMap);
				}
				
			}
			
			/**
			 * banner处理
			 */
			banners=this.iIdexDao.getBannerBySource(1,bannerOfBetterId,2);
			for(Map<String,Object>bmap : banners){
				String about=bmap.get("about")!=null?bmap.get("about").toString():"";
				String type=bmap.get("type")!=null?bmap.get("type").toString():"";
				if((about.equals("4") || about.equals("7")) && type.equals("2")){
					bmap.put("isAndroidHide",true);
				}else{
					bmap.put("isAndroidHide",false);
				}
			}
			for (int i = 0; i < banners.size(); i++) {
				String imgUrl=(String)banners.get(i).get("imgUrl");
				banners.get(i).put("imgUrl", preImgUrl+imgUrl);
			}
			
			/**
			 * 精选商品处理
			 */
			handpick=this.iIdexDao.handpick(1);
			for(Map<String,Object>hpmap : handpick){
				//移除数据layout
				hpmap.remove("layout");
				//图片获取
				String imgUrl=(String)hpmap.get("imgUrl");
				//图片处理
				hpmap.put("imgUrl", preImgUrl+imgUrl);
			}
			
			
			/**
			 * 定制商品的数据 (广告图)
			 */
			customization=this.iIdexDao.handpick(2);
//			int lamp = 0,rlanp=0;
//			List<Map<String,Object>>ctzList=new ArrayList<Map<String,Object>>();
//			for(Map<String,Object>ctzmap : customization){
//				//图片获取
//				String imgUrl=(String)ctzmap.get("imgUrl");
//				//图片获取
//				String ly=ctzmap.get("layout")!=null?ctzmap.get("layout").toString():"1";
//				
//				//移除数据layout
//				ctzmap.remove("layout");
//				
//				//图片处理
//				ctzmap.put("imgUrl", preImgUrl+imgUrl);
//				//添加广告图到ctzList,分开大图并且移出到layout的map
//				if(ly.equals("2")){
//					ctzmap.remove("param1");
//					ctzmap.remove("param2");
//					ctzmap.remove("param3");
//					ctzList.add(ctzmap);
//					rlanp=lamp;
//				}
//				lamp++;
//			}
//			if(customization!=null && !customization.isEmpty()){
//				customization.remove(rlanp);
//			}
			//定制的：商品
//			layout.put("goods", customization);
			//定制的：广告图
			List<Map<String, Object>> customization2=new ArrayList<>();
			List<Map<String,Object>> goods=null;
			for (int j = 0; j < customization.size(); j++) {
				Integer layout_=(Integer)customization.get(j).get("layout");
				if(layout_==2){
					customization.get(j).remove("layout");
					customization.get(j).remove("param1");
					customization.get(j).remove("param2");
					customization.get(j).remove("param3");
					customization.get(j).remove("module");
					Integer id=(Integer)customization.get(j).get("id");
					String imgUrl=(String)customization.get(j).get("imgUrl");
					customization.get(j).put("imgUrl", preImgUrl+imgUrl);
					goods=new ArrayList<Map<String,Object>>();
					goods=this.iIdexDao.getSubGoods(id);
					for (int i = 0; i < goods.size(); i++) {
						String imgUrl2=(String)goods.get(i).get("imgUrl");
						goods.get(i).put("imgUrl", preImgUrl+imgUrl2);
					}
					customization.get(j).put("goods", goods);
					customization2.add(customization.get(j));
				}
				
			}
			
			result.put("topNav", topNav);
			result.put("midNav", midNav);
			result.put("banners", banners);
			result.put("handpick", handpick);
			result.put("customization", customization2);
			
			homeData=JSONUtil.convertObjectToJSON(result);
			ocsDao.insert(OCSKey.DIY_HOME_NEWVERSION+"_"+dateFormat.format(new Date()), homeData,0);
		}
		return homeData;
	}
}
