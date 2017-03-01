package cn._51app.dao.diy2_0.impl;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import cn._51app.dao.BaseDao;
import cn._51app.dao.diy2_0.IHomeNavDao;

@Repository
public class HomeNavDao extends BaseDao implements IHomeNavDao {

	private DecimalFormat df=new DecimalFormat("######0.00");
	
	@Override
	public String HomeNav(Map<String, Object> paramMap) throws Exception {
		String sql="SELECT * FROM diy_home2_nav WHERE status=1 ORDER BY sort";
		List<Map<String,Object>>result=new ArrayList<Map<String,Object>>();
		List<Map<String,Object>>list=super.jt.queryForList(sql);
		Map<String,Object>m=null;
		String dgurl=paramMap.get("dgurl").toString();
		int cacheTime = new Integer(paramMap.get("cacheTime").toString());
		String cacheKey =paramMap.get("cacheKey").toString();
		String prefix="http://api.diy.51app.cn/diyMall";
		if(super.isCacheNull(cacheKey).equals("a")||super.isCacheNull(cacheKey).equals("c")){
			for(Map<String,Object>map : list){
				m=new HashMap<String,Object>();
				String type=map.get("type")==null?"":map.get("type").toString();
				//导航条
				if(type.equals("1")){
					int id=map.get("id")==null?0:Integer.parseInt(map.get("id").toString());
					//专题接口
					String jump=prefix+"/homeNav2/toSpecial.do?navId="+id;
					m.put("name",map.get("name")==null?"":map.get("name").toString());
					m.put("type",Integer.parseInt(type));
					m.put("jump", jump);
					result.add(m);
				//精品专区
				}if(type.equals("2") || type.equals("4")){
					//跳转id(关联商品)
					String pid=map.get("pid")==null?"":map.get("pid").toString();
					String gsql =  "SELECT dgi.id,dgi.`good_id` AS goods_type,dgi.name,dgi.icoUrl,tex.now_price,ROUND(dgi.transportfee,2) AS transportfee,dgi.sell,dgi.h5url " +
							"FROM diy_goods_info2 dgi INNER JOIN diy_info_texture2 tex ON dgi.id=tex.info_id " +
							"WHERE tex.isdefault=1 AND dgi.id=? ORDER BY dgi.sort";
					List<Map<String,Object>> glist=jt.queryForList(gsql,pid);
					if(!glist.isEmpty()){
						Map<String,Object>mm=glist.get(0);
						m.put("goodId",mm.get("id"));
						m.put("type",Integer.parseInt(type));
						if(type.equals("2"))
						m.put("icoUrl", dgurl+mm.get("icoUrl"));
						else if(type.equals("4"))
						m.put("icoUrl", dgurl+mm.get("icoUrl"));
						m.put("cimg",dgurl+map.get("cimg"));
						m.put("now_price",df.format(mm.get("now_price")));
						m.put("sell",mm.get("sell"));
						m.put("name",mm.get("name"));
						m.put("describe", map.get("name"));
						result.add(m);
					}
				//广告（滚动）
				}else if(type.equals("3")){
					String pid=map.get("pid")==null?"":map.get("pid").toString();
					m=new HashMap<String,Object>();
					m.put("type",Integer.parseInt(type));
					m.put("cimg",dgurl+map.get("cimg"));
					m.put("specialId",map.get("special_id"));
					m.put("isgoods",map.get("isgoods"));
					m.put("url",map.get("url"));
					m.put("goodId",pid);
					result.add(m);
				//广告（静态）
				}else if(type.equals("6")){
					String pid=map.get("pid")==null?"":map.get("pid").toString();
					m.put("type",Integer.parseInt(type));
					m.put("cimg", dgurl+map.get("cimg"));
					m.put("url",map.get("url"));
					m.put("isgoods",map.get("isgoods"));
					m.put("goodId",pid);
					m.put("specialId",map.get("special_id"));
					result.add(m);
				//图标按钮
				}else if(type.equals("5")){
					String pid=map.get("pid")==null?"":map.get("pid").toString();
					String special_id=map.get("special_id")==null?"":map.get("special_id").toString();
					//专题接口
					String jump=prefix+"/homeNav2/toSpecial.do?navId="+special_id;
					String jump2=prefix+"/homeNav2/special.do?navId="+special_id;
					m.put("type",Integer.parseInt(type));
					m.put("name",map.get("name"));
					m.put("cimg", dgurl+map.get("cimg"));
					m.put("url",map.get("url"));
					m.put("goodId",pid);
					m.put("isgoods",map.get("isgoods"));
					m.put("specialId",map.get("special_id"));
					m.put("jump",jump);
					m.put("jump2",jump2);
					m.put("color",map.get("color"));
					result.add(m);
				//中间专题
				}else if(type.equals("7")){
					int specialid=map.get("special_id")==null?0:Integer.parseInt(map.get("special_id").toString());
					String specialSql="SELECT name,text,img_url,type AS jumpType,pid FROM diy_special2 WHERE nav_id=? ORDER BY sort";
					List<Map<String,Object>>special=super.jt.queryForList(specialSql,specialid);
					for(Map<String,Object>sp:special){
						String jumpId=sp.get("pid")==null?"":sp.get("pid").toString();
						String jump="http://192.168.1.247:8081/homeNav2/toSpecial.do?navId="+jumpId;
						sp.put("jump",jump);
						sp.put("img_url", dgurl+sp.get("img_url"));
						sp.put("type",Integer.parseInt(type));
						result.add(sp);
					}
				}
			}
			if(super.isCacheNull(cacheKey).equals("a"))
				return super.saveAndGet(result, cacheKey, cacheTime);
			else
				return super.toJson(result);
		}else if(super.isCacheNull(cacheKey).equals("b")){
			return super.q(cacheKey);
		}
		return "";
	}
	
	@Override
	public String toSpecial(Map<String,Object>paramMap)throws Exception{
		String dgurl=paramMap.get("dgurl").toString();
		int cacheTime = new Integer(paramMap.get("cacheTime").toString());
		String cacheKey =paramMap.get("cacheKey").toString();
		int navId=paramMap.get("navId")==null?0:Integer.parseInt(paramMap.get("navId").toString());
		if(super.isCacheNull(cacheKey).equals("a")||super.isCacheNull(cacheKey).equals("c")){
			String sql="SELECT id,name,img_url,text,type,pid FROM diy_special2 WHERE nav_id=? AND status=1 ORDER BY sort";
			List<Map<String,Object>> list=super.jt.queryForList(sql,navId);
			for(Map<String,Object>map:list){
				map.put("img_url",dgurl+map.get("img_url"));
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
	public String special(Map<String, Object> paramMap) throws Exception {
		String cacheKey=paramMap.get("cacheKey").toString();
		String dgurl=paramMap.get("dgurl").toString();
		int cacheTime=new Integer(paramMap.get("cacheTime").toString());
		String sql="SELECT ds.name,ds.img_url,ds.text,ds.pid,ds.type,dhn.name AS navName FROM diy_special2 ds LEFT JOIN diy_home2_nav dhn ON dhn.id=ds.nav_id WHERE ds.nav_id=? AND ds.status=1 ORDER BY ds.sort ASC";
		if(super.isCacheNull(cacheKey).equals("a") || super.isCacheNull(cacheKey).equals("c")){
			List<Map<String,Object>>list=super.jt.queryForList(sql,paramMap.get("nav_id"));
			for(Map<String,Object>map:list){
				map.put("img_url",dgurl+map.get("img_url"));
			}
			if(super.isCacheNull(cacheKey).equals("a")){
				return super.saveAndGet(list, cacheKey, cacheTime);
			}else{
				return super.toJson(list);
			}
		}else if(super.isCacheNull(cacheKey).equals("b")){
			return super.q(cacheKey);
		}
		return null;
	}

}
