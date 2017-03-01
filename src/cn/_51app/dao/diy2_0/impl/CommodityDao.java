package cn._51app.dao.diy2_0.impl;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import cn._51app.dao.BaseDao;
import cn._51app.dao.diy2_0.ICommodityDao;
import cn._51app.dao.ocs.OCSDao;
import cn._51app.util.OCSKey;

@Repository
public class CommodityDao extends BaseDao implements ICommodityDao {
	
	private DecimalFormat df=new DecimalFormat("######0.00");
	
	@Autowired
	private OCSDao ocsDao;

	@Override
	public String getGoodsSortCat(Map<String, Object> paramMap) throws Exception {
		int pages=Integer.parseInt(paramMap.get("pages").toString());
		//获取缓存key
		String cacheKey = paramMap.get("cacheKey").toString();
		//获取缓存时间
		int cacheTime = new Integer(paramMap.get("cacheTime").toString());
		//图片前缀
		String dgurl =paramMap.get("dgurl").toString();
		
		List<Map<String,Object>>result=new ArrayList<Map<String,Object>>();
		String sql="SELECT id,title FROM diy_goods_sort WHERE status=1 ORDER BY sort";
		
		if(isCacheNull(cacheKey).equals("b")){
			return super.q(cacheKey);
		}else if(isCacheNull(cacheKey).equals("a") || isCacheNull(cacheKey).equals("c")){
			List<Map<String,Object>>list=super.jt.queryForList(sql);
			
			for(Map<String,Object>map : list){
				int id=Integer.parseInt(map.get("id").toString());
				String name=map.get("title")!=null?map.get("title").toString():"";
				Map<String,Object>m=new HashMap<String,Object>();
				
				String sql2="SELECT dgi.id AS goodsId,dgi.name,t.now_price AS nowPrice,dgi.sell,dgi.icoUrl FROM diy_goods_sort_cat dgsc LEFT JOIN diy_goods_info2 dgi ON dgsc.info_id=dgi.id "
						+ " LEFT JOIN diy_info_texture2 t ON dgi.id = t.info_id WHERE dgi.isBoutique=1 AND t.isdefault=1 AND dgsc.sort_id=? AND dgsc.status=1 ORDER BY dgsc.sort LIMIT ?";
				
				List<Map<String,Object>>goodsList=super.jt.queryForList(sql2, id,pages);
				//格式化数据
				for(Map<String,Object> gd : goodsList){
					String icoUrl = null==gd.get("icoUrl")?"":gd.get("icoUrl").toString();
					if(StringUtils.isNotEmpty(icoUrl)){
						gd.put("icoUrl", dgurl+icoUrl);
					}
					//格式化价格
					gd.put("nowPrice",df.format(gd.get("nowPrice")));
				}
				m.put("sortName",name);
				m.put("goods",goodsList);
				result.add(m);
			}
			
			if(isCacheNull(cacheKey).equals("a"))
				return super.saveAndGet(result, cacheKey, cacheTime);
			else
				return super.toJson(result);
		}
		
		return "";
	}

	@Override
	public String getCustomize(Map<String, Object> paramMap) throws Exception {
		String dgurl=paramMap.get("dgurl").toString();
		String http2 ="http://file.diy.51app.cn/";
		
		String select ="SELECT `ngi`.`id`"
				+",`ngi`.good_id"
				+ ",`ngi`.saveSize"
				+ ",`ngi`.origin"
				+ ",`ngi`.wh_length"
				+ ",`ngi`.edit_origin"
				+ ",`ngi`.edit_wh"
				+ ",`ngi`.baseImg"
				+ ",`ngi`.beforeImg"
				+ ",`ngi`.goodsType"
				//商品正反面图片
				+ ",`ngi`.showImg"
				//定制图标题
				+",`ngi`.name"
				//商品小图
				+",`ngi`.cfImg"
				+",`ngi`.type"
				+",`ngi`.effect"
				//默认图
				+ ",`ngi`.imageUrl"
				//对应编辑位置
				+ ",`ngi`.module"
					//工具页，默认0
				+ ",`ngi`.firstPopTool"
				//是否杯子
				+ ",`ngi`.isCup"
				//关联定制商品
				+ ",`ngi`.about"
				//客服小图
				+ ",`ngi`.serviceImg"
				+ ",`dit`.origin AS `origin2`"
				+ ",`dit`.wh_size AS `wh_size2`"
				+ ",`dit`.cover_size AS `cover_size2`"
				+ ",`dit`.diy25_img AS `coverImg2`"
				+ ",`dit`.pre_url AS `pre_url2`";
		String from =" FROM new_goods_info `ngi` LEFT JOIN `diy_info_texture2` `dit` ON `ngi`.`id`=`dit`.`make_id` AND `dit`.`isdefault`=1 AND `dit`.`status`=1 ";
		String where=" WHERE `ngi`.`id`=?";
		String sql =select+from+where;
		
		List<Map<String,Object>>list=null;
		List<Map<String,Object>>aboutList=null;
		Map<String,Object>gmap=new HashMap<String,Object>();
		List<Map<String,Object>>ll=new ArrayList<Map<String,Object>>();
		
		String cacheKey=paramMap.get("cacheKey").toString();
		int cacheTime=new Integer(paramMap.get("cacheTime").toString());
		int goodId=Integer.parseInt(paramMap.get("id").toString());
		
		if(isCacheNull(cacheKey).equals("b")){
			return super.q(cacheKey);
		}else if(isCacheNull(cacheKey).equals("a") || isCacheNull(cacheKey).equals("c")){
			list=this.jt.queryForList(sql,goodId);
			//加入定制商品定制方式
			if(list!=null && !list.isEmpty()){
				String about=list.get(0).get("about")==null?"":list.get(0).get("about").toString();
				if(!about.equals("")){
					String goodsId[]=about.split(",");
					for(int i=0;i<goodsId.length;i++){
						aboutList=this.jt.queryForList(sql,goodsId[i]);
						list.addAll(aboutList);
					}
				}
			}
			
			//格式化数据
			for(Map<String,Object>m : list){	
				//商品分类id
				int goodsId=(Integer)m.get("good_id");
				//商品id
				int id=(Integer)m.get("id");
				//默认图
				String imageURL[]={};
				//底图
				String baseImg[]={};
				//前景图
				String beforeImg[]={};
				//前景图
				String showImg[]={};
				
				Object imgUrlObj =m.get("imageURL");
				Object baseUrlObj =m.get("baseImg");
				Object beforeUrlObj =m.get("beforeImg");
				Object showUrlObj =m.get("showImg");
				if(imgUrlObj!=null){
					imageURL =imgUrlObj.toString().split(",");
				}
				if(baseUrlObj!=null){
					baseImg =baseUrlObj.toString().split(",");
				}
				if(beforeUrlObj!=null){
					beforeImg =beforeUrlObj.toString().split(",");
				}
				if(showUrlObj!=null){
					showImg =showUrlObj.toString().split(",");
				}
				Integer goodsType =(Integer)m.get("goodsType");
				//保存尺寸
				String saveSize=m.get("saveSize")!=null?m.get("saveSize").toString():"";
				//定制图标题
				String name=m.get("name")==null?"":m.get("name").toString();  
				//商品小图
				String cfImg=m.get("cfImg")==null?"":m.get("cfImg").toString();
				//对应部位
				int module=Integer.parseInt(m.get("module")==null?"3":m.get("module").toString());
				//定制类型
				int type=Integer.parseInt(m.get("type")==null?"1":m.get("type").toString());
				//定制效果动画
				int effect=Integer.parseInt(m.get("effect")==null?"1":m.get("effect").toString());
				//工具页，默认0
				int firstPopTool=Integer.parseInt(m.get("firstPopTool")==null?"0":m.get("firstPopTool").toString());
				//是否是杯子
				int isCup=Integer.parseInt(m.get("isCup")==null?"0":m.get("isCup").toString());
				
				Map<String,Object>amap=new HashMap();
				
				if (goodsType == null) {
					goodsType = 1;
				}
				
				//只有正面
				if(goodsType==1){
		
					//原点
					String origin =m.get("origin").toString();
					//宽高
					String wh_length =m.get("wh_length").toString(); 
					//编辑原点
					String edit_origin =m.get("edit_origin").toString();
					//编辑宽高
					String edit_wh =m.get("edit_wh").toString(); 
					
					amap.put("origin", origin);
					amap.put("whLength", wh_length);
					amap.put("editOrigin", edit_origin);
					amap.put("editWHLength", edit_wh);
					amap.put("imageURL", imageURL[0].equals("")?"":http2+imageURL[0]);
					amap.put("baseURL", baseImg[0].equals("")?"":http2+baseImg[0]);
					amap.put("beforeURL", beforeImg[0].equals("")?"":http2+beforeImg[0]);
					amap.put("showURL", showImg[0].equals("")?"":http2+showImg[0]);
					amap.put("saveSize", saveSize);
					amap.put("cfImg",dgurl+cfImg);
					amap.put("module",module);
					ll.add(amap);		
					gmap.put("goodsType",1);
				//正反两面
				}else if(goodsType==2){
					amap=new HashMap();
					ll=new ArrayList<Map<String,Object>>();
					
					String[] origin =m.get("origin").toString().split("#");
					String[] wh_length =m.get("wh_length").toString().split("#");
					String[] edit_origin =m.get("edit_origin").toString().split("#"); 
					String[] edit_wh =m.get("edit_wh").toString().split("#"); 
					//正面
					amap.put("origin", origin[0]);
					amap.put("whLength", wh_length[0]);
					amap.put("editOrigin", edit_origin[0]);
					amap.put("editWHLength", edit_wh[0]);
					amap.put("imageURL",imageURL[0].equals("")?"":http2+imageURL[0]);
					amap.put("baseURL", baseImg[0].equals("")?"":http2+baseImg[0]);
					amap.put("beforeURL", beforeImg[0].equals("")?"":http2+beforeImg[0]);
					amap.put("showURL",showImg[0].equals("")?"":http2+showImg[0]);
					amap.put("saveSize", saveSize);
					amap.put("cfImg",dgurl+cfImg);
					ll.add(amap);
					//反面
					amap =new HashMap<String,Object>();
					if(origin.length>1){
					amap.put("origin", origin[1]);
					}
					if(wh_length.length>1){
					amap.put("whLength", wh_length[1]);
					}
					if(edit_origin.length>1){
					amap.put("editOrigin", edit_origin[1]);
					}
					if(edit_wh.length>1){
					amap.put("editWHLength", edit_wh[1]);
					}
					if(imageURL.length>1){
					amap.put("imageURL",imageURL[1].equals("")?"":http2+imageURL[1]);
					}
					if(baseImg.length>1){
						amap.put("baseURL",baseImg[1].equals("")?"":http2+baseImg[1]);
						}
					if(beforeImg.length>1){
						amap.put("beforeURL",beforeImg[1].equals("")?"":http2+beforeImg[1]);
						}
					if(showImg.length>1){
						amap.put("showURL",showImg[1].equals("")?"":http2+showImg[1]);
						}
					amap.put("saveSize", saveSize);
					amap.put("cfImg",dgurl+cfImg);
					amap.put("module",module);
					ll.add(amap);
					gmap.put("goodsType",2);
				//照片书
				}else if(goodsType==3){
					gmap.put("goodsType",3);
				}
				//主的定制商品参数
				if(id==goodId){
					gmap.put("effect", effect);
					gmap.put("type",type);
					gmap.put("saveSize",saveSize);
					//定制标题、图标
					gmap.put("name",name);
					gmap.put("cfImg",dgurl+cfImg);	
					//商品分类id
					gmap.put("sortId", goodsId);
					//商品id
					gmap.put("goodId", id);
					//商品id
					gmap.put("firstPopTool", firstPopTool);
					//是否是杯子
					gmap.put("isCup", isCup);
				}
				String pre_url2=(String)m.get("pre_url2");
				String coverImg2=(String)m.get("coverImg2");
				String origin2=(String)m.get("origin2");
				String wh_size2=(String)m.get("wh_size2");
				String cover_size2=(String)m.get("cover_size2");
				String serviceImg=(String)m.get("serviceImg");
				gmap.put("pre_url2", http2+pre_url2);
				gmap.put("coverImg2", http2+coverImg2);
				gmap.put("origin2", origin2);
				gmap.put("wh_size2", wh_size2);
				gmap.put("cover_size2", cover_size2);
				gmap.put("serviceImg", http2+serviceImg);
			}
			gmap.put("arr", ll);
			if(isCacheNull(cacheKey).equals("a"))
			return super.saveAndGet(gmap, cacheKey, cacheTime);
			else
			return super.toJson(gmap);
		}
		
		return null;
	}
	
	@Override
	public String getCustomizeTest(Map<String, Object> paramMap) throws Exception {
		String dgurl=paramMap.get("dgurl").toString();
		String http ="http://120.26.112.213:8083/file/";
		String http2 ="http://file.diy.51app.cn/";
		
		String select ="SELECT id"
				+",good_id"
				+ ",saveSize"
				+ ",origin"
				+ ",wh_length"
				+ ",edit_origin"
				+ ",edit_wh"
				+ ",baseImg"
				+ ",beforeImg"
				+ ",goodsType"
				//商品正反面图片
				+ ",showImg"
				//定制图标题
				+",name"
				//商品小图
				+",cfImg"
				+",type"
				+",effect"
				//默认图
				+ ",imageUrl";
		String from =" FROM new_goods_info";
		String sql =select+from;
		
		List<Map<String,Object>>result=new ArrayList<Map<String,Object>>();
		Map<String,Object>map=new HashMap<String,Object>();
		List<Map<String,Object>>list=null;
		
		String cacheKey=paramMap.get("cacheKey").toString();
		int cacheTime=new Integer(paramMap.get("cacheTime").toString());
		
		if(isCacheNull(cacheKey).equals("b")){
			return super.q(cacheKey);
		}else if(isCacheNull(cacheKey).equals("a") || isCacheNull(cacheKey).equals("c")){
			list=this.jt.queryForList(sql);
			
			//格式化数据
			for(Map<String,Object>m : list){	
				//商品分类id
				int goodsId=(Integer)m.get("good_id");
				//商品id
				int id=(Integer)m.get("id");
				//默认图
				String imageURL[]={};
				//底图
				String baseImg[]={};
				//前景图
				String beforeImg[]={};
				//前景图
				String showImg[]={};
				
				Object imgUrlObj =m.get("imageURL");
				Object baseUrlObj =m.get("baseImg");
				Object beforeUrlObj =m.get("beforeImg");
				Object showUrlObj =m.get("showImg");
				if(imgUrlObj!=null){
					imageURL =imgUrlObj.toString().split(",");
				}
				if(baseUrlObj!=null){
					baseImg =baseUrlObj.toString().split(",");
				}
				if(beforeUrlObj!=null){
					beforeImg =beforeUrlObj.toString().split(",");
				}
				if(showUrlObj!=null){
					showImg =showUrlObj.toString().split(",");
				}
				Map<String,Object>gmap=new HashMap<String,Object>();
				String goodsType =m.get("goodsType").toString();
				//保存尺寸
				String saveSize=m.get("saveSize")!=null?m.get("saveSize").toString():"";
				//定制图标题
				String name=m.get("name")==null?"":m.get("name").toString();  
				//商品小图
				String cfImg=m.get("cfImg")==null?"":m.get("cfImg").toString();
				//定制类型
				int type=Integer.parseInt(m.get("type")==null?"1":m.get("type").toString());
				//定制效果动画
				int effect=Integer.parseInt(m.get("effect")==null?"1":m.get("effect").toString());
				
				Map<String,Object>amap=null;
				List<Map<String,Object>>ll=null;
				//只有正面
				if(goodsType.equals("1")){
					amap=new HashMap();
					ll=new ArrayList<Map<String,Object>>();
		
					//原点
					String origin =m.get("origin").toString();
					//宽高
					String wh_length =m.get("wh_length").toString(); 
					//编辑原点
					String edit_origin =m.get("edit_origin").toString();
					//编辑宽高
					String edit_wh =m.get("edit_wh").toString(); 
					
					amap.put("origin", origin);
					amap.put("whLength", wh_length);
					amap.put("editOrigin", edit_origin);
					amap.put("editWHLength", edit_wh);
					amap.put("imageURL", imageURL[0].equals("")?"":http2+imageURL[0]);
					amap.put("baseURL", baseImg[0].equals("")?"":http2+baseImg[0]);
					amap.put("beforeURL", beforeImg[0].equals("")?"":http2+beforeImg[0]);
					amap.put("showURL", showImg[0].equals("")?"":http2+showImg[0]);
					amap.put("saveSize", saveSize);
					ll.add(amap);		
					
					gmap.put("arr", ll);
					gmap.put("goodsType",1);
				//正反两面
				}else if(goodsType.equals("2")){
					amap=new HashMap();
					ll=new ArrayList<Map<String,Object>>();
					
					String[] origin =m.get("origin").toString().split("#");
					String[] wh_length =m.get("wh_length").toString().split("#");
					String[] edit_origin =m.get("edit_origin").toString().split("#"); 
					String[] edit_wh =m.get("edit_wh").toString().split("#"); 
					//正面
					amap.put("origin", origin[0]);
					amap.put("whLength", wh_length[0]);
					amap.put("editOrigin", edit_origin[0]);
					amap.put("editWHLength", edit_wh[0]);
					amap.put("imageURL",imageURL[0].equals("")?"":http2+imageURL[0]);
					amap.put("baseURL", baseImg[0].equals("")?"":http2+baseImg[0]);
					amap.put("beforeURL", beforeImg[0].equals("")?"":http2+beforeImg[0]);
					amap.put("showURL",showImg[0].equals("")?"":http2+showImg[0]);
					amap.put("saveSize", saveSize);
					ll.add(amap);
					//反面
					amap =new HashMap<String,Object>();
					if(origin.length>1){
					amap.put("origin", origin[1]);
					}
					if(wh_length.length>1){
					amap.put("whLength", wh_length[1]);
					}
					if(edit_origin.length>1){
					amap.put("editOrigin", edit_origin[1]);
					}
					if(edit_wh.length>1){
					amap.put("editWHLength", edit_wh[1]);
					}
					if(imageURL.length>1){
					amap.put("imageURL",imageURL[1].equals("")?"":http2+imageURL[1]);
					}
					if(baseImg.length>1){
						amap.put("baseURL",baseImg[1].equals("")?"":http2+baseImg[1]);
						}
					if(beforeImg.length>1){
						amap.put("beforeURL",beforeImg[1].equals("")?"":http2+beforeImg[1]);
						}
					if(showImg.length>1){
						amap.put("showURL",showImg[1].equals("")?"":http2+showImg[1]);
						}
					amap.put("saveSize", saveSize);
					ll.add(amap);
					gmap.put("arr", ll);
					gmap.put("goodsType",2);
				//照片书
				}else if(goodsType.equals("3")){
					gmap.put("goodsType",3);
				}
				gmap.put("effect", effect);
				gmap.put("type",type);
				gmap.put("saveSize",saveSize);
				//定制标题、图标
				gmap.put("name",name);
				gmap.put("cfImg",dgurl+cfImg);	
				//商品分类id
				gmap.put("sortId", goodsId);
				//商品id
				gmap.put("goodId", id);
				result.add(gmap);
			}
			map.put("charparam", result);
			
			if(isCacheNull(cacheKey).equals("a"))
			return super.saveAndGet(result, cacheKey, cacheTime);
			else
			return super.toJson(result);
		}
		
		return null;
	}
	
	@Override
	public String getMaterial(Map<String,Object>paramMap) throws Exception{
		String cacheKey =paramMap.get("cacheKey").toString();
		int cacheTime =new Integer(paramMap.get("cacheTime").toString());
		String id = paramMap.get("id").toString();
		String module=paramMap.get("module").toString();
		if(super.isCacheNull(cacheKey).equals("b")){
			return super.q(cacheKey);
		}else{
			Map<String,Object>m=new HashMap<String,Object>();
			
			String goodSql="SELECT * FROM diy_preload WHERE module=:module AND good_id=:id";
			String materialsql = "SELECT id,`material_url`,`count`,`num`,`pid` AS typeId FROM diy_material2 WHERE `type`=? AND module like CONCAT('%',?,'%') AND publish=1 ORDER BY sort";
			String sql="SELECT id AS coutomizeId FROM new_goods_info WHERE good_id=? AND about is not null";
			List<Map<String, Object>>coutomizeId=null;
			try{
				coutomizeId=jt.queryForList(sql,id);
			}catch(Exception e){
				sql="SELECT id AS coutomizeId FROM new_goods_info WHERE good_id=?";
				coutomizeId=jt.queryForList(sql,id);
			}
		
			List<Map<String,Object>> goodList = npjt.queryForList(goodSql,paramMap);
			//预加载的模板id
			List<Integer> preload = new ArrayList<Integer>();
			
			
			if(goodList!=null){
				for(Map<String,Object>gm : goodList){
					preload.add((Integer)gm.get("material_id"));	
				}
				m.put("autoDownloadTemplateIDs", preload);
			}else{
				m.put("autoDownloadTemplateIDs", null);
			}
			
			String dmRootUrl =paramMap.get("dmRootUrl").toString();
			String pid="(";
				
					//模板列表
					List<Map<String,Object>> listl = jt.queryForList(materialsql,new Object[]{id,module});
					if(listl==null || listl.isEmpty()){
						return null;
					}
					for (Map<String, Object> map : listl) {
						String material_url = null==map.get("material_url")?"":map.get("material_url").toString();
						int mid=Integer.parseInt(map.get("typeId")==null?"0":map.get("typeId").toString());
						if(StringUtils.isNotEmpty(material_url)){
							map.put("material_url", dmRootUrl+material_url);		
						}			
						pid+=mid+",";
					}
					pid=pid.substring(0,pid.length()-1)+")";
					String typesql = "SELECT `dmt`.`title`,`dmt`.`id` AS typeId,`dmt`.`background_color`,`dmt`.`font_color` FROM `diy_material_type2` `dmt` WHERE `dmt`.`about`=? AND `dmt`.`id` in "+pid+" AND `dmt`.`status`=1 ORDER BY `dmt`.`sort`";
					//模板标签列表
					List<Map<String,Object>> typelist = jt.queryForList(typesql,new Object[]{id});
					m.put("typeList", typelist);
					
					m.put("mList", listl);
					m.put("requestTime",new Date().getTime());
					m.put("goodsId",0);
					m.put("sortId",Integer.parseInt(id));
					
			return super.saveAndGet(m, cacheKey, cacheTime);
		}
	}
	
	@Override   
	public String photoList(Map<String,Object>paramMap) throws Exception{
		String cacheKey =paramMap.get("cacheKey").toString();
		int cacheTime =new Integer(paramMap.get("cacheTime").toString());
		String id = paramMap.get("id").toString();
		if(super.isCacheNull(cacheKey).equals("b")){
			return super.q(cacheKey);
		}else{
			Map<String,Object>m=new HashMap<String,Object>();
			
			String typesql = "SELECT title,id AS typeId,`background_color`,`font_color` FROM `diy_label` WHERE `about`=? AND `status`='1' ORDER BY sort";
			String photosql = "SELECT id,`img`,`label_id` AS `typeId` FROM `diy_photo` WHERE `good_id`=? AND `publish`=1 ORDER BY `sort`";
			
			String dgurl =paramMap.get("dgurl").toString();
					//图片标签列表
					List<Map<String,Object>> typelist = jt.queryForList(typesql,new Object[]{id});
					m.put("typeList", typelist);
					//图片列表
					List<Map<String,Object>> listl = jt.queryForList(photosql,new Object[]{id});
					for (Map<String, Object> map : listl) {
						String material_url = null==map.get("img")?"":map.get("img").toString();
						if(StringUtils.isNotEmpty(material_url)){
							map.put("img", dgurl+material_url);
						}
					}
					m.put("mList", listl);
					
			return super.saveAndGet(m, cacheKey, cacheTime);
		}
	}
	
	@Override
	public String tagList(Map<String,Object>paramMap) throws Exception{
		String cacheKey =paramMap.get("cacheKey").toString();
		int cacheTime =new Integer(paramMap.get("cacheTime").toString());
		String id = paramMap.get("id").toString();
		if(super.isCacheNull(cacheKey).equals("b")){
			return super.q(cacheKey);
		}else{
			Map<String,Object>m=new HashMap<String,Object>();
			
			String typesql = "SELECT title,id AS typeId,`background_color`,`font_color` FROM `diy_tags_label` WHERE  status='1' ORDER BY sort";//`about`=? AND
			String photosql = "SELECT id,`url`,`label_id` AS typeId FROM diy_tags WHERE status=1 ORDER BY sort";//`good_id`=? AND 
			
			String dgurl =paramMap.get("dgurl").toString();
					//图片标签列表
					List<Map<String,Object>> typelist = jt.queryForList(typesql);
					m.put("typeList", typelist);
					//图片列表
					List<Map<String,Object>> listl = jt.queryForList(photosql);
					for (Map<String, Object> map : listl) {
						String material_url = null==map.get("url")?"":map.get("url").toString();
						if(StringUtils.isNotEmpty(material_url)){
							map.put("url", dgurl+material_url);
						}
					}
					m.put("mList", listl);
					
			return super.saveAndGet(m, cacheKey, cacheTime);
		}
	}
	
	@Override
	public String findList(Map<String,Object>paramMap) throws Exception{
		//获取缓存key
		String cacheKey = paramMap.get("cacheKey").toString();
		//获取缓存时间
		int cacheTime = new Integer(paramMap.get("cacheTime").toString());
		//图片前缀
		String dgurl =paramMap.get("dgurl").toString();
		
		String sql="SELECT id,title,author,count,img,head_img AS headImg,view_count AS view,type,url,IF(DATE_FORMAT(ctime,'%Y%m%d')=curdate(),DATE_FORMAT(ctime,'今天 %H:%i'),IF(DATE_FORMAT(ctime,'%Y%m%d')=curdate()-1,DATE_FORMAT(ctime,'昨天 %H:%i'),IF(DATE_FORMAT(ctime,'%Y%m%d')=curdate()-2,DATE_FORMAT(ctime,'前天 %H:%i'),DATE_FORMAT(ctime,'%m-%d %H:%i')))) AS ctime FROM diy_find_title WHERE status=1 AND type!=1 ORDER BY sort LIMIT :index,:rows";
		String sql2="SELECT id,title,author,count,img,type,url FROM diy_find_title WHERE status=1 AND type=1 ORDER BY sort";
		if(super.isCacheNull(cacheKey).equals("b")){
			return super.q(cacheKey);
		}else if(isCacheNull(cacheKey).equals("a") || isCacheNull(cacheKey).equals("c")){
			List<Map<String,Object>>list=super.npjt.queryForList(sql,paramMap);
			if(list.isEmpty())
			return "";	
				
			if(paramMap.get("index").toString().equals("0")){
				List<Map<String,Object>>list2=super.jt.queryForList(sql2);
				list.addAll(list2);
			}
			
			for(Map<String,Object>map : list){
				String img=map.get("img")==null?"":map.get("img").toString();
				String headImg=map.get("headImg")==null?"":map.get("headImg").toString();
				map.put("img", dgurl+img);
				map.put("headImg", dgurl+headImg);
			}
			
			if(isCacheNull(cacheKey).equals("a"))
			return super.saveAndGet(list, cacheKey, cacheTime);
			else
			return toJson(list);	
		}
		return "";
	}
	
	@Override
	public String findDetail(Map<String,Object>paramMap) throws Exception{
		//获取缓存key
				String cacheKey = paramMap.get("cacheKey").toString();
				//获取缓存时间
				int cacheTime = new Integer(paramMap.get("cacheTime").toString());
				//图片前缀
				String dgurl =paramMap.get("dgurl").toString();
				//发现id
				String id=paramMap.get("id").toString();
				
				String sql="SELECT `dfd`.title,`dfd`.`sort`,`dfd`.`id`,`dfd`.`img`,`dfd`.`text`,`dfd`.`type`,`dfd`.`url`,`dfd`.`good_id`,`dfd`.`isBoutique`,'' AS `org_price`,'' AS `now_price`,DATE_FORMAT(`dfd`.`ctime`,'%Y-%m-%d') AS `ctime` FROM diy_find_detail `dfd` WHERE `dfd`.`status`=1 AND (`dfd`.`isBoutique` is null or `dfd`.`good_id` is null) AND `dfd`.`find_id`=:id GROUP BY `dfd`.`id` UNION ALL SELECT `dfd`.title,`dfd`.`sort`,`dfd`.`id`,`dfd`.`img`,`dfd`.`text`,`dfd`.`type`,`dfd`.`url`,`dfd`.`good_id`,`dfd`.`isBoutique`,`dit`.`org_price`,`dit`.`now_price`,DATE_FORMAT(`dfd`.`ctime`,'%Y-%m-%d') AS `ctime` FROM diy_find_detail `dfd` LEFT JOIN diy_info_texture2 `dit` ON `dfd`.`good_id`=`dit`.`make_id` AND `dit`.`isdefault`=1 WHERE `dfd`.`status`=1 AND `dfd`.`isBoutique`=2 AND `dfd`.`find_id`=:id GROUP BY `dfd`.`id` UNION ALL SELECT `dfd`.title,`dfd`.`sort`,`dfd`.`id`,`dfd`.`img`,`dfd`.`text`,`dfd`.`type`,`dfd`.`url`,`dfd`.`good_id`,`dfd`.`isBoutique`,`dit`.`org_price`,`dit`.`now_price`,DATE_FORMAT(`dfd`.`ctime`,'%Y-%m-%d') AS `ctime` FROM diy_find_detail `dfd` LEFT JOIN diy_info_texture2 `dit` ON `dfd`.`good_id`=`dit`.`info_id` AND `dit`.`isdefault`=1 WHERE `dfd`.`status`=1 AND `dit`.`status`=1 AND `dfd`.`isBoutique`=1 AND `dfd`.`find_id`=:id GROUP BY `dfd`.`id` UNION ALL SELECT `dfd`.title,`dfd`.`sort`,`dfd`.`id`,`dfd`.`img`,`dfd`.`text`,`dfd`.`type`,`dfd`.`url`,`dfd`.`good_id`,`dfd`.`isBoutique`,`dit`.`org_price`,`dit`.`now_price`,DATE_FORMAT(`dfd`.`ctime`,'%Y-%m-%d') AS `ctime` FROM diy_find_detail `dfd` LEFT JOIN diy_info_texture2 `dit` ON `dfd`.`good_id`=`dit`.`info_id` AND `dit`.`isdefault`=1 WHERE `dfd`.`status`=1 AND `dfd`.`isBoutique`=4 AND `dfd`.`find_id`=:id GROUP BY `dfd`.`id` order by sort";
				
				String authorAql="SELECT id,title,author,count,head_img AS headImg,view_count AS view,IF(DATE_FORMAT(ctime,'%Y%m%d')=curdate(),DATE_FORMAT(ctime,'今天 %H:%i'),IF(DATE_FORMAT(ctime,'%Y%m%d')=curdate()-1,DATE_FORMAT(ctime,'昨天 %H:%i'),IF(DATE_FORMAT(ctime,'%Y%m%d')=curdate()-2,DATE_FORMAT(ctime,'前天 %H:%i'),DATE_FORMAT(ctime,'%m-%d %H:%i')))) AS ctime FROM diy_find_title WHERE status=1 AND id=:id";
				
				String auSql="SELECT view_count FROM diy_find_title WHERE id=:id";
				Map<String,Object>auMap=super.npjt.queryForMap(auSql, paramMap);
				//访问数量+1
				String view_count=auMap.get("view_count")==null?"":auMap.get("view_count").toString();
				try{
					Integer viewNum=Integer.parseInt(view_count)+1;
					String viewSql="UPDATE diy_find_title SET view_count='"+viewNum+"' WHERE id=:id";
					super.npjt.update(viewSql, paramMap);
				}catch(Exception e){
					
				}
				
				if(super.isCacheNull(cacheKey).equals("b")){
					return super.q(cacheKey);
				}else if(isCacheNull(cacheKey).equals("a") || isCacheNull(cacheKey).equals("c")){
					List<Map<String,Object>>list=super.npjt.queryForList(sql,paramMap);
					Map<String,Object>authorMap=super.npjt.queryForMap(authorAql, paramMap);
					authorMap.put("headImg",dgurl+(authorMap.get("headImg")==null?"":authorMap.get("headImg").toString()));
					Map<String,Object>resultMap=new HashMap<String,Object>();
					for(Map<String,Object>map : list){
						String img=map.get("img")==null || map.get("img").toString().equals("")?"":dgurl+map.get("img").toString();
						String type=map.get("type")!=null?map.get("type").toString():"";
						if(!type.equals("4")){
							map.remove("ctime");
						}
						map.put("img",img);
					}
					
					resultMap.put("topCont",authorMap);
					resultMap.put("centerCont",list);
					
					if(isCacheNull(cacheKey).equals("a"))
					return super.saveAndGet(resultMap, cacheKey, cacheTime);
					else
					return toJson(list);	
				}
				return "";
	}
	
	@Override
	public String goodsProperty2(Map<String,Object>paramMap) throws Exception{
		String cacheKey=paramMap.get("cacheKey").toString();
		Map<String,Object>result = new LinkedHashMap<String,Object>();
		if(super.isCacheNull(cacheKey).equals("a") || super.isCacheNull(cacheKey).equals("c")){
			int cacheTime = new Integer(paramMap.get("cacheTime").toString());
			String dgurl=paramMap.get("dgurl").toString();
			int id=new Integer(paramMap.get("id").toString());
			int type=new Integer(paramMap.get("type").toString());
			String search="info_id";
			String table="diy_goods_info2";
			//定制商品
			if(type==2){
				search="make_id";
				table="new_goods_info";
			}
			String infosql="SELECT texture_ids,org_price,now_price,pre_url,diy25_img AS coverImg,carousel,save_size,origin,wh_size,cover_size FROM diy_info_texture2 WHERE status=0 AND "+search+"=? AND isOther=1";
			String infoend=" ORDER BY sort";
			//查询所有组合属性
			List<Map<String,Object>>info = super.jt.queryForList(infosql+infoend,new Object[]{id});
			String first = "";
			String second = "";
			String third= "";
			int index = 0;
			for(Map<String,Object>map : info){
				//预览图
				String pre_url = null==map.get("pre_url")?"":map.get("pre_url").toString();
				String carousel=null==map.get("carousel")?"":map.get("carousel").toString();
				String coverImg=null==map.get("coverImg")?"":map.get("coverImg").toString();
				
				//价格格式化
				map.put("org_price",df.format(map.get("org_price")));
				map.put("now_price",df.format(map.get("now_price")));
				if(StringUtils.isNotEmpty(pre_url)){
					map.put("pre_url",dgurl+pre_url);
				}else{
					map.put("pre_url","");
				}
				if(StringUtils.isNotEmpty(coverImg)){
					map.put("coverImg",dgurl+coverImg);
				}else{
					map.put("coverImg","");
				}
				if(StringUtils.isNotEmpty(carousel)){
					map.put("carousel",dgurl+carousel);
				}else{
					map.put("carousel", "");
				}
				//获取材质
				String texture_ids = null==map.get("texture_ids")?"":map.get("texture_ids").toString();
				String []f=texture_ids.split("_");
				index=f.length;
				//属性长度决定层级
				if(index==1){
					first+=f[0]+",";
				}else if(index==2){
					first+=f[0]+",";
					second+=f[1]+",";
				}else if(index==3){
					first+=f[0]+",";
					second+=f[1]+",";
					third+=f[2]+",";
				}
			}
		String gsql="SELECT gt.id,gt.name AS gname,g.name AS title FROM diy_goods_texture2 gt INNER JOIN diy_good2 g ON gt.texture_type=g.id ";
		String end=" ORDER BY gt.sort ";
		
		Map<String,Object>tempMap=null;
		List<Map<String,Object>>layerList=new ArrayList<Map<String,Object>>();
		if(index==1){
			//查出所有要出现的属性gname
			String firstsql=gsql+ " AND gt.id in("+first.substring(0,first.length()-1)+")";
			List<Map<String,Object>>firstList = jt.queryForList(firstsql+end);
			//获取属性类型
			String title=firstList.get(0).get("title").toString();
			
			tempMap = new HashMap<String,Object>();
			tempMap.put("title",title);
			tempMap.put("list",firstList);
			layerList.add(tempMap);
		}else if(index==2){
			String firstsql=gsql+ " AND gt.id in("+first.substring(0,first.length()-1)+")";
			List<Map<String,Object>>firstList = jt.queryForList(firstsql+end);
			String title = firstList.get(0).get("title").toString();
			
			tempMap = new HashMap<String, Object>();
			tempMap.put("title", title);
			tempMap.put("list", firstList);
			layerList.add(tempMap);
			
			String secondsql=gsql+ " AND gt.id in("+second.substring(0,second.length()-1)+")";
			List<Map<String,Object>>secondList = jt.queryForList(secondsql+end);
			String titleB = secondList.get(0).get("title").toString();
			tempMap = new HashMap<String, Object>();
			tempMap.put("title", titleB);
			tempMap.put("list", secondList);	
			layerList.add(tempMap);
		}else if(index==3){
			String firstsql=gsql+ " AND gt.id in("+first.substring(0,first.length()-1)+")";
			List<Map<String,Object>>firstList = jt.queryForList(firstsql+end);
			String title = firstList.get(0).get("title").toString();
			
			tempMap = new HashMap<String, Object>();
			tempMap.put("title", title);
			tempMap.put("list", firstList);
			layerList.add(tempMap);
			
			String secondsql=gsql+ " AND gt.id in("+second.substring(0,second.length()-1)+")";
			List<Map<String,Object>>secondList = jt.queryForList(secondsql+end);
			String titleB = secondList.get(0).get("title").toString();
			tempMap = new HashMap<String, Object>();
			tempMap.put("title", titleB);
			tempMap.put("list", secondList);
			layerList.add(tempMap);
			
			String thirdsql=gsql+ " AND gt.id in("+third.substring(0,third.length()-1)+")";
			List<Map<String,Object>>thirdList = jt.queryForList(thirdsql+end);
			String titleC = secondList.get(0).get("title").toString();
			tempMap = new HashMap<String, Object>();
			tempMap.put("title", titleC);
			tempMap.put("list", thirdList);
			
			layerList.add(tempMap);
		}
		//所有组合材质
		result.put("texture",info);
		//listABC
		result.put("list", layerList);
		infosql+=" AND isdefault=1";
		//默认价格和属性
		Map<String,Object>patientia =super.jt.queryForList(infosql+infoend,new Object[]{id}).get(0);
		//获取商家信息，商品类型
		String storesql= "SELECT user_id,goodsType,activity_id FROM "+table+" WHERE id="+id;
		Map<String,Object>store = super.jt.queryForMap(storesql);
		String activity="SELECT about FROM diy_activity WHERE id=?";
		String actId=store.get("activity_id")==null?"":store.get("activity_id").toString();
		String about=null;
		try{
			about=super.jt.queryForObject(activity,String.class,actId);
		}catch(Exception e){
			
		}
		result.put("activityId",actId);
		result.put("activity",about);
		result.put("now_price",df.format(patientia.get("now_price")));
		result.put("org_price",df.format(patientia.get("org_price")));
		result.put("origin",patientia.get("origin"));
		result.put("wh_size",patientia.get("wh_size"));
		result.put("cover_size",patientia.get("cover_size"));
		result.put("pre_url", dgurl+patientia.get("pre_url"));
		result.put("id", id);
		result.put("storeId", null==store.get("user_id")?"":store.get("user_id").toString());
		if(type==2)
		result.put("goodsType", null==store.get("goodsType")?"":store.get("goodsType").toString());
		result.put("dfTexture", patientia.get("texture_ids"));
		result.put("type",type);
		if(super.isCacheNull(cacheKey).equals("a"))
			return super.saveAndGet(result, cacheKey, cacheTime);
		else{
			return super.toJson(result);
		}
	  }else if(super.isCacheNull(cacheKey).equals("b")){
			return super.q(cacheKey);
		}
		return "";
	}
	
	@Override
	public String goodsProperty3(Map<String,Object>paramMap) throws Exception{
		String cacheKey=paramMap.get("cacheKey").toString();
		Map<String,Object>result = new LinkedHashMap<String,Object>();
		if(super.isCacheNull(cacheKey).equals("a") || super.isCacheNull(cacheKey).equals("c")){
			int cacheTime = new Integer(paramMap.get("cacheTime").toString());
			String dgurl=paramMap.get("dgurl").toString();
			int id=new Integer(paramMap.get("id").toString());
			int type=new Integer(paramMap.get("type").toString());
			String search="info_id";
			String table="diy_goods_info2";
			//定制商品
			if(type==2){
				search="make_id";
				table="new_goods_info";
			}
			String infosql="SELECT texture_ids,org_price,now_price,pre_url,diy25_img AS coverImg,carousel,save_size,origin,wh_size,cover_size FROM diy_info_texture2 WHERE status=0 AND "+search+"=? AND isOther=2";
			String infoend=" ORDER BY sort";
			//查询所有组合属性
			List<Map<String,Object>>info = super.jt.queryForList(infosql+infoend,new Object[]{id});
			String first = "";
			String second = "";
			String third= "";
			int index = 0;
			for(Map<String,Object>map : info){
				//预览图
				String pre_url = null==map.get("pre_url")?"":map.get("pre_url").toString();
				String carousel=null==map.get("carousel")?"":map.get("carousel").toString();
				String coverImg=null==map.get("coverImg")?"":map.get("coverImg").toString();
				
				//价格格式化
				map.put("org_price",df.format(map.get("org_price")));
				map.put("now_price",df.format(map.get("now_price")));
				if(StringUtils.isNotEmpty(pre_url)){
					map.put("pre_url",dgurl+pre_url);
				}else{
					map.put("pre_url","");
				}
				if(StringUtils.isNotEmpty(coverImg)){
					map.put("coverImg",dgurl+coverImg);
				}else{
					map.put("coverImg","");
				}
				if(StringUtils.isNotEmpty(carousel)){
					map.put("carousel",dgurl+carousel);
				}else{
					map.put("carousel", "");
				}
				//获取材质
				String texture_ids = null==map.get("texture_ids")?"":map.get("texture_ids").toString();
				String []f=texture_ids.split("_");
				index=f.length;
				//属性长度决定层级
				if(index==1){
					first+=f[0]+",";
				}else if(index==2){
					first+=f[0]+",";
					second+=f[1]+",";
				}else if(index==3){
					first+=f[0]+",";
					second+=f[1]+",";
					third+=f[2]+",";
				}
			}
		String gsql="SELECT gt.id,gt.name AS gname,g.name AS title FROM diy_goods_texture2 gt INNER JOIN diy_good2 g ON gt.texture_type=g.id ";
		String end=" ORDER BY gt.sort ";
		
		Map<String,Object>tempMap=null;
		List<Map<String,Object>>layerList=new ArrayList<Map<String,Object>>();
		if(index==1){
			//查出所有要出现的属性gname
			String firstsql=gsql+ " AND gt.id in("+first.substring(0,first.length()-1)+")";
			List<Map<String,Object>>firstList = jt.queryForList(firstsql+end);
			//获取属性类型
			String title=firstList.get(0).get("title").toString();
			
			tempMap = new HashMap<String,Object>();
			tempMap.put("title",title);
			tempMap.put("list",firstList);
			layerList.add(tempMap);
		}else if(index==2){
			String firstsql=gsql+ " AND gt.id in("+first.substring(0,first.length()-1)+")";
			List<Map<String,Object>>firstList = jt.queryForList(firstsql+end);
			String title = firstList.get(0).get("title").toString();
			
			tempMap = new HashMap<String, Object>();
			tempMap.put("title", title);
			tempMap.put("list", firstList);
			layerList.add(tempMap);
			
			String secondsql=gsql+ " AND gt.id in("+second.substring(0,second.length()-1)+")";
			List<Map<String,Object>>secondList = jt.queryForList(secondsql+end);
			String titleB = secondList.get(0).get("title").toString();
			tempMap = new HashMap<String, Object>();
			tempMap.put("title", titleB);
			tempMap.put("list", secondList);	
			layerList.add(tempMap);
		}else if(index==3){
			String firstsql=gsql+ " AND gt.id in("+first.substring(0,first.length()-1)+")";
			List<Map<String,Object>>firstList = jt.queryForList(firstsql+end);
			String title = firstList.get(0).get("title").toString();
			
			tempMap = new HashMap<String, Object>();
			tempMap.put("title", title);
			tempMap.put("list", firstList);
			layerList.add(tempMap);
			
			String secondsql=gsql+ " AND gt.id in("+second.substring(0,second.length()-1)+")";
			List<Map<String,Object>>secondList = jt.queryForList(secondsql+end);
			String titleB = secondList.get(0).get("title").toString();
			tempMap = new HashMap<String, Object>();
			tempMap.put("title", titleB);
			tempMap.put("list", secondList);
			layerList.add(tempMap);
			
			String thirdsql=gsql+ " AND gt.id in("+third.substring(0,third.length()-1)+")";
			List<Map<String,Object>>thirdList = jt.queryForList(thirdsql+end);
			String titleC = secondList.get(0).get("title").toString();
			tempMap = new HashMap<String, Object>();
			tempMap.put("title", titleC);
			tempMap.put("list", thirdList);
			
			layerList.add(tempMap);
		}
		//所有组合材质
		result.put("texture",info);
		//listABC
		result.put("list", layerList);
		infosql+=" AND isdefault=1";
		//默认价格和属性
		Map<String,Object>patientia =super.jt.queryForList(infosql+infoend,new Object[]{id}).get(0);
		//获取商家信息，商品类型
		String storesql= "SELECT user_id,goodsType,activity_id FROM "+table+" WHERE id="+id;
		Map<String,Object>store = super.jt.queryForMap(storesql);
		String activity="SELECT about FROM diy_activity WHERE id=?";
		String actId=store.get("activity_id")==null?"":store.get("activity_id").toString();
		String about=null;
		try{
			about=super.jt.queryForObject(activity,String.class,actId);
		}catch(Exception e){
			
		}
		result.put("activityId",actId);
		result.put("activity",about);
		result.put("now_price",df.format(patientia.get("now_price")));
		result.put("org_price",df.format(patientia.get("org_price")));
		result.put("origin",patientia.get("origin"));
		result.put("wh_size",patientia.get("wh_size"));
		result.put("cover_size",patientia.get("cover_size"));
		result.put("pre_url", dgurl+patientia.get("pre_url"));
		result.put("id", id);
		result.put("storeId", null==store.get("user_id")?"":store.get("user_id").toString());
		if(type==2)
		result.put("goodsType", null==store.get("goodsType")?"":store.get("goodsType").toString());
		result.put("dfTexture", patientia.get("texture_ids"));
		result.put("type",type);
		if(super.isCacheNull(cacheKey).equals("a"))
			return super.saveAndGet(result, cacheKey, cacheTime);
		else{
			return super.toJson(result);
		}
	  }else if(super.isCacheNull(cacheKey).equals("b")){
			return super.q(cacheKey);
		}
		return "";
	}
	
	@Override
	public String goodsProperty(Map<String,Object>paramMap) throws Exception{
		String cacheKey=paramMap.get("cacheKey").toString();
		Map<String,Object>result = new LinkedHashMap<String,Object>();
		if(super.isCacheNull(cacheKey).equals("a") || super.isCacheNull(cacheKey).equals("c")){
			int cacheTime = new Integer(paramMap.get("cacheTime").toString());
			String dgurl=paramMap.get("dgurl").toString();
			int id=new Integer(paramMap.get("id").toString());
			int type=new Integer(paramMap.get("type").toString());
			String search="info_id";
			String table="diy_goods_info2";
			//定制商品
			if(type==2){
				search="make_id";
				table="new_goods_info";
			}
			String infosql="SELECT texture_ids,org_price,now_price,pre_url,diy25_img AS coverImg,carousel,save_size,origin,wh_size,cover_size FROM diy_info_texture2 WHERE status=1 AND "+search+"=? AND (isOther IS NULL OR isOther=0)";
			String infoend=" ORDER BY sort";
			//查询所有组合属性
			List<Map<String,Object>>info = super.jt.queryForList(infosql+infoend,new Object[]{id});
			String first = "";
			String second = "";
			String third= "";
			int index = 0;
			for(Map<String,Object>map : info){
				//预览图
				String pre_url = null==map.get("pre_url")?"":map.get("pre_url").toString();
				String carousel=null==map.get("carousel")?"":map.get("carousel").toString();
				String coverImg=null==map.get("coverImg")?"":map.get("coverImg").toString();
				
				//价格格式化
				map.put("org_price",df.format(map.get("org_price")));
				map.put("now_price",df.format(map.get("now_price")));
				if(StringUtils.isNotEmpty(pre_url)){
					map.put("pre_url",dgurl+pre_url);
				}else{
					map.put("pre_url","");
				}
				if(StringUtils.isNotEmpty(coverImg)){
					map.put("coverImg",dgurl+coverImg);
				}else{
					map.put("coverImg","");
				}
				if(StringUtils.isNotEmpty(carousel)){
					map.put("carousel",dgurl+carousel);
				}else{
					map.put("carousel", "");
				}
				//获取材质
				String texture_ids = null==map.get("texture_ids")?"":map.get("texture_ids").toString();
				String []f=texture_ids.split("_");
				index=f.length;
				//属性长度决定层级
				if(index==1){
					first+=f[0]+",";
				}else if(index==2){
					first+=f[0]+",";
					second+=f[1]+",";
				}else if(index==3){
					first+=f[0]+",";
					second+=f[1]+",";
					third+=f[2]+",";
				}
			}
		String gsql="SELECT gt.id,gt.name AS gname,g.name AS title FROM diy_goods_texture2 gt INNER JOIN diy_good2 g ON gt.texture_type=g.id ";
		String end=" ORDER BY gt.sort ";
		
		Map<String,Object>tempMap=null;
		List<Map<String,Object>>layerList=new ArrayList<Map<String,Object>>();
		if(index==1){
			//查出所有要出现的属性gname
			String firstsql=gsql+ " AND gt.id in("+first.substring(0,first.length()-1)+")";
			List<Map<String,Object>>firstList = jt.queryForList(firstsql+end);
			//获取属性类型
			String title=firstList.get(0).get("title").toString();
			
			tempMap = new HashMap<String,Object>();
			tempMap.put("title",title);
			tempMap.put("list",firstList);
			layerList.add(tempMap);
		}else if(index==2){
			String firstsql=gsql+ " AND gt.id in("+first.substring(0,first.length()-1)+")";
			List<Map<String,Object>>firstList = jt.queryForList(firstsql+end);
			String title = firstList.get(0).get("title").toString();
			
			tempMap = new HashMap<String, Object>();
			tempMap.put("title", title);
			tempMap.put("list", firstList);
			layerList.add(tempMap);
			
			String secondsql=gsql+ " AND gt.id in("+second.substring(0,second.length()-1)+")";
			List<Map<String,Object>>secondList = jt.queryForList(secondsql+end);
			String titleB = secondList.get(0).get("title").toString();
			tempMap = new HashMap<String, Object>();
			tempMap.put("title", titleB);
			tempMap.put("list", secondList);	
			layerList.add(tempMap);
		}else if(index==3){
			String firstsql=gsql+ " AND gt.id in("+first.substring(0,first.length()-1)+")";
			List<Map<String,Object>>firstList = jt.queryForList(firstsql+end);
			String title = firstList.get(0).get("title").toString();
			
			tempMap = new HashMap<String, Object>();
			tempMap.put("title", title);
			tempMap.put("list", firstList);
			layerList.add(tempMap);
			
			String secondsql=gsql+ " AND gt.id in("+second.substring(0,second.length()-1)+")";
			List<Map<String,Object>>secondList = jt.queryForList(secondsql+end);
			String titleB = secondList.get(0).get("title").toString();
			tempMap = new HashMap<String, Object>();
			tempMap.put("title", titleB);
			tempMap.put("list", secondList);
			layerList.add(tempMap);
			
			String thirdsql=gsql+ " AND gt.id in("+third.substring(0,third.length()-1)+")";
			List<Map<String,Object>>thirdList = jt.queryForList(thirdsql+end);
			String titleC = secondList.get(0).get("title").toString();
			tempMap = new HashMap<String, Object>();
			tempMap.put("title", titleC);
			tempMap.put("list", thirdList);
			
			layerList.add(tempMap);
		}
		//所有组合材质
		result.put("texture",info);
		//listABC
		result.put("list", layerList);
		infosql+=" AND isdefault=1";
		//默认价格和属性
		Map<String,Object>patientia =super.jt.queryForList(infosql+infoend,new Object[]{id}).get(0);
		//获取商家信息，商品类型
		String storesql= "SELECT user_id,goodsType,activity_id FROM "+table+" WHERE id="+id;
		Map<String,Object>store = super.jt.queryForMap(storesql);
		String activity="SELECT about FROM diy_activity WHERE id=?";
		String actId=store.get("activity_id")==null?"":store.get("activity_id").toString();
		String about=null;
		try{
			about=super.jt.queryForObject(activity,String.class,actId);
		}catch(Exception e){
			
		}
		result.put("activityId",actId);
		result.put("activity",about);
		result.put("now_price",df.format(patientia.get("now_price")));
		result.put("org_price",df.format(patientia.get("org_price")));
		result.put("origin",patientia.get("origin"));
		result.put("wh_size",patientia.get("wh_size"));
		result.put("cover_size",patientia.get("cover_size"));
		result.put("pre_url", dgurl+patientia.get("pre_url"));
		result.put("id", id);
		result.put("storeId", null==store.get("user_id")?"":store.get("user_id").toString());
		if(type==2)
		result.put("goodsType", null==store.get("goodsType")?"":store.get("goodsType").toString());
		result.put("dfTexture", patientia.get("texture_ids"));
		result.put("type",type);
		if(super.isCacheNull(cacheKey).equals("a"))
			return super.saveAndGet(result, cacheKey, cacheTime);
		else{
			return super.toJson(result);
		}
	  }else if(super.isCacheNull(cacheKey).equals("b")){
			return super.q(cacheKey);
		}
		return "";
	}
	
	@Override
	public String getGoodsChartParamById(Map<String,Object>paramMap)throws Exception{
String cacheKey =paramMap.get("cacheKey").toString();
		if(super.isCacheNull(cacheKey).equals("a") || super.isCacheNull(cacheKey).equals("c")){
			Map<String,Object> rm =new HashMap<String,Object>();
			int cacheTime =Integer.parseInt(paramMap.get("cacheTime").toString());
			String dgurl=paramMap.get("dgurl").toString();
			String sql="SELECT dgi.id,dgi.name,dgi.pvwImgs,dgi.edgeInsetsStr,dgi.serviceImg,dit.texture_ids,`dgi`.`newgoodid` FROM  `diy_goods_info2` `dgi` LEFT JOIN new_goods_info `ngi` ON `ngi`.`id`=`dgi`.`newgoodid` LEFT JOIN diy_info_texture2 dit ON dit.info_id=dgi.id WHERE dit.isdefault=1 AND dit.status=1 AND dgi.id=:id limit 1";
			Map<String,Object>map=super.npjt.queryForMap(sql, paramMap);
			String id =paramMap.get("id").toString();
			String pvwImgs=map.get("pvwImgs")==null?"":map.get("pvwImgs").toString();
			String imgs[]=pvwImgs.contains("#")?pvwImgs.split("#"):pvwImgs.split(",");
			String serviceImg=map.get("serviceImg")==null?"":map.get("serviceImg").toString();
			List<String>pvw=new ArrayList<String>();
			for(int i=0;i<imgs.length;i++){
				if(!imgs[i].equals("")){
					pvw.add(dgurl+imgs[i]);
				}
			}
			
			//去查询1.0的商品id作为分享id
			String sql2="SELECT dgi.id FROM diy_goods_info dgi LEFT JOIN diy_goods_info2 dgi2 ON dgi2.shareGoods=dgi.id WHERE dgi2.id=? LIMIT 1";
			Integer goodId=0;
			try{
			      goodId=super.jt.queryForObject(sql2,Integer.class,id);
			}catch(Exception e){
				
			}
			
			
			rm.put("textureId", map.get("texture_ids"));
			rm.put("imgs",pvw);
			rm.put("serviceImg",dgurl+serviceImg);
			rm.put("edgeInsetsStr", map.get("edgeInsetsStr"));
			rm.put("newgoodid",map.get("newgoodid"));
			rm.put("goodsURLStr", "commodity/goodsDetails.do?id="+id);
			rm.put("detailURLStr", "commodity/goodsDetail.do?id="+id+"&type=1");
			rm.put("appraiseURLStr","evaluation2/comment.do?id="+id+"&goodsType=0");
			rm.put("showURLStr", "commodity/goodsProperty/1/"+id+".do");
			rm.put("shareTitle", "我在唯乐购发现了一个不错的商品，很实惠哦，赶快来看看吧。");
			rm.put("shareUrl", "http://test.diy.51app.cn/diyMall2/UGoods/goodsInfo.do?id="+goodId);
			rm.put("shareLogo", dgurl+imgs[0]);
			if(super.isCacheNull(cacheKey).equals("a")){
				return saveAndGet(rm,cacheKey,cacheTime);
			}else{
				return super.toJson(rm);
			}
		}else if(super.isCacheNull(cacheKey).equals("b")){
			return super.q(cacheKey);
		}
		return "";
	}
	
	@Override
	public String getGoodsTitleById(Map<String,Object> paramMap) throws Exception {
		//获取缓存key
		String cacheKey =paramMap.get("cacheKey").toString();
		del(cacheKey);
		//内连接sql，texture_ids对应选择属性（多个），以info_id关联材质属性表
		String select ="SELECT dgi.id,dgi.title,dgi.transportfee,tex.org_price,tex.now_price,tex.texture_ids,dgi.isBoutique,dgi.recommend ";
		String from =" FROM diy_goods_info2 dgi INNER JOIN diy_info_texture2 tex ON dgi.id =tex.info_id";
		String where =" WHERE dgi.id=:id AND tex.isdefault=1 AND tex.status=1 ";
		String sql =select+from+where;
		//设置返回或放入缓存数据
		Map<String,Object> m =null;
		//缓存查不到
		if(isCacheNull(cacheKey).equals("a") || isCacheNull(cacheKey).equals("c")){
			try {
				//查询数据库数据
				m =super.npjt.queryForMap(sql, paramMap);
				//获取现价和原价
				m.put("nowPrice",m.get("now_price")!=null?df.format(m.get("now_price")):"0.00");
				m.put("originalPrice",m.get("org_price")!=null?df.format(m.get("org_price")):"0.00");
				//设置返回属性，list形式
				List<String> tl =new ArrayList<String>();
				//获取选择的材质属性
				String textureIds=m.get("texture_ids")!=null?m.get("texture_ids").toString():"未选择";
				//以下划线分割数据
				String arrIds[]=textureIds.split("_");
				//根据arrIds查询选择属性逗号隔开
				for(int i=0;i<arrIds.length;i++){
					Map<String,Object> map=new HashMap<String,Object>();
					map.put("id",arrIds[i]);
					String param=getSelectTexture(map);
					if(i==arrIds.length-1){
						tl.add(param);
					}else{
						tl.add(param+",");
					}
				}
				String dgurl =paramMap.get("dgurl").toString();
				String recommend=m.get("recommend")==null?"":m.get("recommend").toString();
				//属性放入对象
				m.put("paramList", tl);
				m.put("recommend",getGoods(recommend,dgurl));
				
				//设置运费显示
				String transportfee = null==m.get("transportfee")?"":m.get("transportfee").toString();
				if(transportfee.equals("0.0")){
					m.put("transportfee", "免运费");
				}else{
					m.put("transportfee","运费  ￥"+transportfee);
				}
				
			} catch (EmptyResultDataAccessException e) {
				return "";
			}
			int cacheTime =new Integer(paramMap.get("cacheTime").toString());
			if(super.isCacheNull(cacheKey).equals("a"))
			return super.saveAndGet(m, cacheKey, cacheTime);
			else 
			return super.toJson(m);
		}else if(isCacheNull(cacheKey).equals("b")){
			return super.q(cacheKey);
		}
		return "";
		
	}
	
	//推荐商品详情查询以及属性
		private List<Map<String,Object>>getGoods(String recommend,String dgurl){
			String arr[]=recommend.split(",");
			String sql="SELECT dgi.ispostage,dgi.sell,dgi.id,dgi.title AS `name`,dgi.`diy25_img` AS `icoUrl`,dgi.recommend,tex.org_price,tex.now_price AS `nowPrice`,tex.texture_ids ,dgi.good_id,dgi.user_id AS companyId,da.type AS act "
					+ "FROM diy_goods_info2 dgi INNER JOIN diy_info_texture2 tex ON dgi.id =tex.info_id LEFT JOIN diy_activity da ON da.id=dgi.activity_id  "
					+ "WHERE dgi.id=? AND tex.isdefault=1 AND tex.status=1 ";
			String sql2="SELECT ngi.ispostage,ngi.sell,ngi.id,ngi.`name`,ngi.`diy25_img` AS `icoUrl`,tex.org_price,tex.now_price AS `nowPrice`,tex.texture_ids ,ngi.good_id,ngi.user_id AS companyId,da.type AS act "
					+ "FROM new_goods_info ngi INNER JOIN diy_info_texture2 tex ON ngi.id =tex.make_id LEFT JOIN diy_activity da ON da.id=ngi.activity_id "
					+ "WHERE ngi.id=? AND tex.isdefault=1 AND tex.status=1 ";
			List<Map<String,Object>> reList=new ArrayList<Map<String,Object>>();
			if(arr[0].equals("")){
				 return null;
			}
			for(int i=0;i<arr.length;i++){
				//分type和商品id
				String arr_n[]=arr[i].split("_");
				if(arr_n.length!=2){
					continue;
				}
				Map<String,Object>map=null;
				//分类型查询商品
				if(arr_n[0].equals("1") || arr_n[0].equals("4")){
					if(super.jt.queryForList(sql,arr_n[1]).isEmpty()){
						continue;
					}
					map=super.jt.queryForList(sql,arr_n[1]).get(0);
					//改变图片前缀
					Object obj =map.get("icoUrl");
					if(obj!=null){
						map.put("icoUrl", dgurl+obj);
					}
					map.put("type",arr_n[0]);
					//改变价格
					map.put("nowPrice",df.format(map.get("nowPrice")));	
					
				}else{
					if(super.jt.queryForList(sql2,arr_n[1]).isEmpty()){
						continue;
					}
					map=super.jt.queryForList(sql2,arr_n[1]).get(0);
					//改变图片前缀
					Object obj =map.get("icoUrl");
					if(obj!=null){
						map.put("icoUrl", dgurl+obj);
					}
					map.put("type",arr_n[0]);
					//改变价格
					map.put("nowPrice",df.format(map.get("nowPrice")));
				}
				
				Integer companyId=(Integer)(map.get("companyId")==null?"0":map.get("companyId"));
				Integer act=(Integer)(map.get("act")==null?"":map.get("act"));
				switch(act){
				case 1:map.put("act", "满减");break;
				case 2:map.put("act", "活动");break;
				case 4:map.put("act", "活动");break;
				case 8:map.put("act", "折扣");break;
				default:map.put("act", "无");
				}
				switch(companyId){
				case 2:map.put("isSelf", "true");break;
				case 3:map.put("isSelf", "true");break;
				default:map.put("isSelf", "false");
				}
				
				reList.add(map);
			}
			return reList;
		}
		
		/**
		 * 返回材质属性
		 */
		@Override
		public String getSelectTexture(Map<String, Object> paramMap) throws Exception {
			String sql="SELECT name FROM diy_goods_texture2 WHERE id=:id";
			return super.npjt.queryForObject(sql, paramMap,String.class);
		}
		
		@Override
		public String getFontList(Map<String,Object>paramMap)throws Exception{
			String dgurl=paramMap.get("dgurl").toString();
			String sql="SELECT id,name,url FROM diy_font_info";
			List<Map<String,Object>> list=super.jt.queryForList(sql);
			for(Map<String,Object>map : list){
				map.put("url",dgurl+map.get("url"));
			}
			return super.toJson(list);
		}
		
		@Override
		public String goods(Map<String, Object> paramMap) throws Exception{
			String cacheKey = paramMap.get("cacheKey").toString();
			String sql = "SELECT dgi.`ispostage`,dgi.id,dgi.title AS `name`,dgi.`previewImgUrl` AS `icoUrl`,dgi.recommend,tex.org_price,tex.now_price AS `nowPrice`,tex.texture_ids,dgi.sell,dgi.activity_id,dgi.isBoutique,dgi.good_label AS label,tex.`pre_url`,dgi.user_id AS companyId " +
					"FROM diy_goods_info2 dgi INNER JOIN diy_info_texture2 tex ON dgi.id =tex.info_id " +
					"WHERE dgi.id=? AND tex.isdefault=1 AND tex.status=1 ";
			if(super.isCacheNull(cacheKey).equals("a")||super.isCacheNull(cacheKey).equals("c")){
				int cacheTime = new Integer(paramMap.get("cacheTime").toString());
				Map<String, Object> map = jt.queryForMap(sql,new Object[]{paramMap.get("id")});
				if(!map.isEmpty()){
					String dgurl =paramMap.get("dgurl").toString();
					//查询id
					String activityId=map.get("activity_id").toString();
					String label=map.get("label")==null?"":map.get("label").toString();
					
					Integer companyId=(Integer)(map.get("companyId")==null?"0":map.get("companyId"));
					
					switch(companyId){
					case 2:map.put("isSelf", "true");break;
					case 3:map.put("isSelf", "true");break;
					default:map.put("isSelf", "false");
					}
					
					List<String> labList=Arrays.asList(label.split(","));
					map.put("lable",labList);
					String sql2="SELECT about FROM diy_activity WHERE id=?";
					String activity="";
					try{
						activity=super.jt.queryForObject(sql2,String.class,activityId);
					}catch(Exception e){
						
					}
					//推荐商品，用逗号隔开
					String recommend=map.get("recommend")==null?"":map.get("recommend").toString();
						Object obj =map.get("icoUrl");
						if(obj!=null){
							map.put("icoUrl", dgurl+obj);
						}
						Object pre_url =map.get("pre_url");
						if(pre_url!=null){
							map.put("pre_url", dgurl+pre_url);
						}
						//格式化价格
						map.put("nowPrice",df.format(map.get("nowPrice")));
						map.put("org_price",df.format(map.get("org_price")));
						//查询材质
						List<String> lt = new ArrayList<>();
						String textureIds=map.get("texture_ids")!=null?map.get("texture_ids").toString():"未选择";
						String arrIds[]=textureIds.split("_");
						for(int i=0;i<arrIds.length;i++){
							Map<String,Object> tmap=new HashMap<String,Object>();
							tmap.put("id",arrIds[i]);
							String param=getSelectTexture(tmap);
							if(i==arrIds.length-1){
								lt.add(param+"  1件");
							}else{
								lt.add(param+"");
							}
						}
						map.put("paramList", lt);
						map.put("activity",activity);
						if(!recommend.equals(""))
						map.put("recommend",getGoods(recommend,dgurl));
						else
						map.put("recommend","");
					}
				if(super.isCacheNull(cacheKey).equals("a"))
					return super.saveAndGet(map, cacheKey, cacheTime);
				else
					return super.toJson(map);
			}else if(super.isCacheNull(cacheKey).equals("b")){
				return super.q(cacheKey);
			}
			return "";
		}
		
		@Override
		public List<Map<String,Object>> production(Map<String,Object>paramMap)throws Exception{
			String dgurl =paramMap.get("dgurl").toString();
			String sql="SELECT `dmi`.`id`,`dmi`.`name`,`dmi`.`price`,`dmi`.`img`,`dmi`.`type`,`dmi`.`goods`,`dmi`.`sort`,`ngi`.`type` AS `gType`,`dmi`.`custom_id`,`dmi`.`subtitle` FROM diy_make_interface `dmi` LEFT JOIN new_goods_info `ngi` ON `ngi`.`id`=`dmi`.`goods` WHERE `dmi`.place=:type AND `dmi`.`state`=1 AND `dmi`.`type`=2 UNION ALL SELECT `dmi`.`id`,`dmi`.`name`,`dmi`.`price`,`dmi`.`img`,`dmi`.`type`,`dmi`.`goods`,`dmi`.`sort`,'' AS `gType`,`dmi`.`custom_id`,`dmi`.`subtitle` FROM diy_make_interface `dmi` WHERE `dmi`.`place`=:type AND `dmi`.`state`=1 AND `dmi`.`type`=3 ORDER BY sort,id LIMIT :index,:rows";
			String sql2="SELECT `dmi`.`id`,`dmi`.`name`,`dmi`.`price`,`dmi`.`img`,`dmi`.`type`,`dmi`.`goods`,`dmi`.`sort`,`ngi`.`type` AS `gType` FROM diy_make_interface `dmi` LEFT JOIN new_goods_info `ngi` ON `dmi`.`goods`=`ngi`.`id` WHERE `dmi`.`place`=:type AND `dmi`.`state`=1 AND `dmi`.`type`=1 UNION ALL SELECT `dmi`.`id`,`dmi`.`name`,`dmi`.`price`,`dmi`.`img`,`dmi`.`type`,`dmi`.`goods`,`dmi`.`sort`,'' AS `gType` FROM diy_make_interface `dmi` WHERE `dmi`.`place`=:type AND `dmi`.`state`=1 AND `dmi`.`type`=4 ORDER BY sort,id";
				List<Map<String,Object>>list=this.npjt.queryForList(sql, paramMap);
				//查询type=1和4的数据
				if(paramMap.get("index").toString().equals("0")){
					List<Map<String,Object>>list2=this.npjt.queryForList(sql2, paramMap);	
					if(!list2.isEmpty()){
						list.addAll(list2);
					}
				}
				for(Map<String,Object>map : list){
					Object img =map.get("img");
					Object price=map.get("price");
					if(price!=null){
						map.put("price",df.format(price));
					}
					if(img!=null){
						map.put("img",dgurl+img);
					}
				}
				return list;
		}
		
		@Override
		public String getMakeSort(Map<String,Object>paramMap)throws Exception{
			String cacheKey = paramMap.get("cacheKey").toString();
			String dgurl =paramMap.get("dgurl").toString();
			int cacheTime = new Integer(paramMap.get("cacheTime").toString());
			String sort_goods=npjt.queryForMap("SELECT sort_goods FROM diy_make_interface WHERE id=:sortId", paramMap).get("sort_goods").toString();
			String sql="SELECT `dmi`.`id`,`dmi`.`name`,`dmi`.`price`,`dmi`.`img`,`dmi`.`type`,`dmi`.`sort`,`dmi`.`state`,`dmi`.`goods`,`ngi`.`type` AS gType,`dmi`.`custom_id` FROM diy_make_interface `dmi` LEFT JOIN new_goods_info `ngi` ON `ngi`.`id`=`dmi`.`goods` WHERE `dmi`.`id` in("+sort_goods+") ORDER BY `dmi`.`sort`";
			if(super.isCacheNull(cacheKey).equals("a")||super.isCacheNull(cacheKey).equals("c")){
				List<Map<String,Object>>list=this.jt.queryForList(sql);
				for(Map<String,Object>map : list){
					Object img =map.get("img");
					Object price=map.get("price");
					if(price!=null){
						map.put("price",df.format(price));
					}
					if(img!=null){
						map.put("img",dgurl+img);
					}
				}
				if(super.isCacheNull(cacheKey).equals("a"))
					return super.saveAndGet(list, cacheKey, cacheTime);
				else
					return super.toJson(list);
			}else if(super.isCacheNull(cacheKey).equals("b")){
				return super.q(cacheKey);
			}
			return "";
		}
		
		@Override
		public String getGoodsDetails(Map<String,Object>paramMap)throws Exception{
			//获取缓存名称
			String cacheKey =paramMap.get("cacheKey").toString();
			String isBoutique=paramMap.get("isBoutique").toString();
			
			String from =" FROM diy_goods_info2";
			if(isBoutique.equals("2"))
			from=" FROM new_goods_info";
			//sql语句
			String select ="SELECT id,name,introduce,parameter,packAfterSale,priceNote,vedio";
			String where =" WHERE id=:id";
			String sql =select+from+where;
			//设置查询sql返回容器
			Map<String,Object> m =null;
			//使用isCacheNull查看缓存空为a,异常为c,正常为b
			if(super.isCacheNull(cacheKey).equals("a") || isCacheNull(cacheKey).equals("c")){
				
				try {
					//查询数据库获取数据
					m =super.npjt.queryForMap(sql, paramMap);
					
				//无数据空查询异常
				} catch (EmptyResultDataAccessException e) {
					return null;
				}
				//获取到数据执行
				if(m!=null){
					//设置返回的Map类型容器
					Map<String,Object> responseMap =new HashMap<String,Object>();
					//获取多张定制介绍图片，逗号分割
					String[] introduceArr =(m.get("introduce").toString()).split(",");
					//以&分开，表示每一行的数据，获取商品参数
					String[] parameterArr1 =(m.get("parameter").toString()).split("&");
					//设置lmTmp2的List来存放每一行数据
					List<Map<String,Object>> lmTmp2 =new ArrayList<Map<String,Object>>();
					//循环放数据到lmTmp2
					for(int i=0;i<parameterArr1.length;i++){
						//再次拆分数据，以#分开
						String[] parameterArr1_1Arr =(parameterArr1[i]).split("#");
						//如果长度不是2个长度，就不读取这条数据
						if(parameterArr1_1Arr.length!=2)break;
						//设置title作为#分开的数据1
						String title =parameterArr1_1Arr[0];
						//设置txt作为#分开的数据2
						String txt =parameterArr1_1Arr[1];
						//封装到Map
						Map<String,Object> mTmp =new HashMap<String,Object>();
						//放到map
						mTmp.put("title", title);
						mTmp.put("txt", txt);
						//添加到List
						lmTmp2.add(mTmp);
					}
					//获取包装售后
					String[] packAfterSaleArr1 =(m.get("packAfterSale").toString()).split("&");
					List<Map<String,Object>> lmTmp3 =new ArrayList<Map<String,Object>>();
					//设置lmTmp3的List来存放每一行数据
					for(int i=0;i<packAfterSaleArr1.length;i++){
						//再次拆分数据，以#分开
						String[] packAfterSaleArr1_1Arr =(packAfterSaleArr1[i]).split("#");
						//如果长度不是2个长度，就不读取这条数据
						if(packAfterSaleArr1_1Arr.length!=2)break;
						//设置title作为#分开的数据1
						String title =packAfterSaleArr1_1Arr[0];
						//设置txt作为#分开的数据2
						String txt =packAfterSaleArr1_1Arr[1];
						//封装到Map
						Map<String,Object> mTmp =new HashMap<String,Object>();
						//放到map
						mTmp.put("title", title);
						mTmp.put("txt", txt);
						//添加到List
						lmTmp3.add(mTmp);
					}
					//设置返回id
					responseMap.put("id", paramMap.get("id").toString());
				
					//以数组形式创建list
					List<String> lstr=Arrays.asList(introduceArr);
					String dgurl =paramMap.get("dgurl").toString();
					//设置介绍图片返回容器
					List<String> lsTmp =new ArrayList<String>();
					//为每个图片url加上前缀
					for(int i=0;i<lstr.size();i++){
						lsTmp.add(dgurl+lstr.get(i));
					}
					//价格说明or申明
					String[] priceNoteArr =m.get("priceNote").toString().split("#");
					//设置关于我们说明
					Map<String,Object> mTmp =new HashMap<String,Object>();
					//返回title
					mTmp.put("title", priceNoteArr[0]);
					//返回txt
					if(priceNoteArr.length>1){
						mTmp.put("txt", priceNoteArr[1]);
					}
					String vedio=m.get("vedio")!=null?m.get("vedio").toString():"";
					String name=m.get("name")!=null?m.get("name").toString():"";
					//封装各个map类型数据设置为返回数据
					responseMap.put("name",name);
					responseMap.put("vedio",(vedio.equals("")?"":dgurl+vedio));
					responseMap.put("priceNote", mTmp);
					responseMap.put("introduceList",  lsTmp);
					responseMap.put("parameterList", lmTmp2);
					responseMap.put("packAfterSaleList", lmTmp3);
					
					int cacheTime =new Integer(paramMap.get("cacheTime").toString());
					
					if(super.isCacheNull(cacheKey).equals("a")){
						//总数据放入缓存
						return super.saveAndGet(responseMap, cacheKey, cacheTime);
					}else
						return super.toJson(responseMap);
						
				}
			//缓存有数据，读取缓存
			}else  if(super.isCacheNull(cacheKey).equals("b")){
				return super.q(cacheKey);
			//缓存异常，同第一次读取缓存一样操作
			}
			//缓存无数据，又查不出数据，也没有异常，那就返回空字符啦
			return "";
		}
	
}
