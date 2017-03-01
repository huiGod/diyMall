package cn._51app.dao.diy2_0.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import cn._51app.dao.BaseDao;
import cn._51app.dao.diy2_0.IWorkActivityDao;
import cn._51app.util.OCSKey;
import cn._51app.util.RedisUtil;

@Repository
public class WorkActivityDao extends BaseDao implements IWorkActivityDao {

	@Override
	public String getWorkList(String openid,String dgurl) throws Exception{
		Map<String,Object>result=new HashMap<String,Object>();
		String sql="SELECT id,name,cont,imgurl,goodsId,userId,heart,suffix FROM diy_works_list dwl  WHERE type=2";
		List<Map<String,Object>> list =super.jt.queryForList(sql);
		
		String search = "select count(id) from diy_user_wx where wx_id='"+openid+"'";
		Integer i = super.jt.queryForObject(search, Integer.class);
		//查不到用户表示未授权，直接退出
		if(i!=1){
			return "";
		}
		
		for(Map<String,Object>m:list){
			String img=m.get("imgurl")==null?"":m.get("imgurl").toString();
			String suffix=m.get("suffix")==null?"":m.get("suffix").toString();
			//多张照片
			if(img.contains("_")){
				List<String>imglist=new ArrayList<String>();
				String url[]=img.split("_");
				int count=Integer.parseInt(url[1]);
				for(int k=0;k<count;k++){
					imglist.add(dgurl+url[0]+"_"+(k+1)+suffix);
				}
				m.put("preImg",imglist);
			}else{
				List<String>imglist=new ArrayList<String>();
				imglist.add(dgurl+img+suffix);
				m.put("preImg",imglist);
			}
		}
		if(list!=null && !list.isEmpty()){
			for (int j = 0; j < list.size(); j++) {
				Integer woid=(Integer)list.get(j).get("id");
				list.get(j).put("heart", RedisUtil.zscore(OCSKey.DIY_WORKLIST_LIKE, woid+""));
			}
		}
		Collections.sort(list,new Comparator<Map<String, Object>>() {
			@Override
			public int compare(Map<String, Object> m1, Map<String, Object> m2) {
				double m1heart=(Double)m1.get("heart");
				double m2heart=(Double)m2.get("heart");
				if(m1heart>m2heart){
					return 1;
				}
				if(m1heart<m2heart){
					return -1;
				}
				return 0;
			}
		});
		result.put("other", list);
		return super.toJson(result);
	}
	
	@Override
	public String getWorkListForApp(String userId,String dgurl,String page) throws Exception{
		Map<String,Object>result=new HashMap<String,Object>();
		String sql2="SELECT dwl.id,dwl.name,dwl.cont,dwl.imgurl,dwl.goodsId,dwl.userId,dwl.heart,dwl.suffix FROM diy_works_list dwl LEFT JOIN diy_device_user ddu ON dwl.userId=ddu.id WHERE type=2 AND ddu.id=?";
		String sql3="select count(id) from diy_works_list where type=2 and heart>=(select heart from diy_works_list where id=?)";
		//页码
		int index=Integer.parseInt(page),rows=8;
		//第一页9个
		if(index==0){
			rows=9;
		}
		
		List<Map<String,Object>>tempASList=new ArrayList<Map<String,Object>>(); 
		int info=0,cursor=0;
		while(tempASList.size()!=rows){
			//分页查询心的排名
			Set<String> TempList=RedisUtil.zrevrangeByScore(OCSKey.DIY_WORKLIST_LIKE,1000000,0,cursor,20);
			if(TempList.isEmpty()){
				break;
			}
			String workList="";
			for(String tmp : TempList){
				workList+=tmp+",";
			}
			String sql="SELECT id,name,cont,imgurl,goodsId,userId,heart,suffix FROM diy_works_list dwl  WHERE type=2 AND id IN(";
			sql+=workList.substring(0,workList.length()-1)+") order by instr('"+workList.substring(0,workList.length()-1)+"',id)";
			List<Map<String,Object>> list =super.jt.queryForList(sql);
			if(list!=null){
				//在规定范围内累加
				if(info+list.size()>index*rows || info>index*rows){
					for(Map<String,Object> qm : list){
						//只有在范围内才加
						if(info>=index*rows && info<(index+1)*rows){
							tempASList.add(qm);
						}
						info++;
					}
				}else{
					info+=list.size();
				}
			}
			cursor++;
		}
		//查询openid的用户作品
		List<Map<String,Object>>userList=super.jt.queryForList(sql2,userId);
		
		Map<String,Object> rm = new HashMap<String,Object>();
		String search = "select count(id) from diy_device_user where id="+userId;
		Integer i = super.jt.queryForObject(search, Integer.class);
		//查不到用户表示未授权，直接退出
		if(i!=1){
			return "";
		}
		
		for(Map<String,Object>m:tempASList){
			String img=m.get("imgurl")==null?"":m.get("imgurl").toString();
			String suffix=m.get("suffix")==null?"":m.get("suffix").toString();
			//多张照片
			if(img.contains("_")){
				List<String>imglist=new ArrayList<String>();
				String url[]=img.split("_");
				int count=Integer.parseInt(url[1]);
				for(int k=0;k<count;k++){
					imglist.add(dgurl+url[0]+"_"+(k+1)+suffix);
				}
				m.put("preImg",imglist);
			}else{
				List<String>imglist=new ArrayList<String>();
				imglist.add(dgurl+img+"@b"+suffix);
				imglist.add(dgurl+img+"@pb"+suffix);
				m.put("preImg",imglist);
			}
		}
		if(tempASList!=null && !tempASList.isEmpty()){
			for (int j = 0; j < tempASList.size(); j++) {
				Integer woid=(Integer)tempASList.get(j).get("id");
				tempASList.get(j).put("heart", RedisUtil.zscore(OCSKey.DIY_WORKLIST_LIKE, woid+""));
			}
		}
		result.put("other", tempASList);
		
		if(userList.isEmpty())
		result.put("mine","");
		else{
			for(Map<String,Object>map:userList){
				int workId=map.get("id")!=null?Integer.parseInt(map.get("id").toString()):0;
				//查询我的作品排名
				int ranking=super.jt.queryForObject(sql3,Integer.class,workId);
				map.put("ranking", ranking);
				
				String img=map.get("imgurl")==null?"":map.get("imgurl").toString();
				String suffix=map.get("suffix")==null?"":map.get("suffix").toString();
				//多张照片
				if(img.contains("_")){
					List<String>imglist=new ArrayList<String>();
					String url[]=img.split("_");
					int count=Integer.parseInt(url[1]);
					for(int k=0;k<count;k++){
						imglist.add(dgurl+url[0]+"_"+(k+1)+suffix);
					}
					map.put("preImg",imglist);
				}else{
					List<String>imglist=new ArrayList<String>();
					imglist.add(dgurl+img+"@b"+suffix);
					imglist.add(dgurl+img+"@pb"+suffix);
					map.put("preImg",imglist);
				}
			}
			if(userList!=null && !userList.isEmpty()){
				for (int k = 0; k < userList.size(); k++) {
					Integer woid=(Integer)userList.get(k).get("id");
					userList.get(k).put("heart", RedisUtil.zscore(OCSKey.DIY_WORKLIST_LIKE, woid+""));
				}
			}
			result.put("mine",userList);
		}
		
		return super.toJson(result);
	}
	
	@Override
	public Map<String,Object>openIdUser(Map<String,Object>paramMap){
		String sql="SELECT * FROM diy_user_wx WHERE wx_id=:openid";
		try{
			return super.npjt.queryForMap(sql, paramMap);
		}catch(Exception e){
			return null;
		}
	}
	
	@Override
	public int insertUser(Map<String,Object>paramMap)throws Exception{
		String sql="INSERT INTO diy_user_wx (wx_id,activite) values(:openid,1)";
		return super.npjt.update(sql, paramMap);
	}
	
	@Override
	public Map<String,Object>workInfo(Map<String,Object>paramMap){
		String sql="SELECT id,name,cont,goodsId,userId,textureIds,heart,imgUrl,suffix FROM diy_works_list WHERE id=:workId";
		Map<String,Object>map= super.npjt.queryForMap(sql, paramMap);
		String dgurl=paramMap.get("dgurl")!=null?paramMap.get("dgurl").toString():"";
		//获取图片
		String img=map.get("imgurl")==null?"":map.get("imgurl").toString();
		String suffix=map.get("suffix")==null?"":map.get("suffix").toString();
		//多张照片
		if(img.contains("_")){
			List<String>imglist=new ArrayList<String>();
			String url[]=img.split("_");
			int count=Integer.parseInt(url[1]);
			for(int k=0;k<count;k++){
				imglist.add(dgurl+url[0]+"_"+(k+1)+suffix);
			}
			map.put("preImg",imglist);
		}else{
			List<String>imglist=new ArrayList<String>();
			imglist.add(dgurl+img+"@b"+suffix);
			imglist.add(dgurl+img+"@pb"+suffix);
			map.put("preImg",imglist);
		}
		double heart=RedisUtil.zscore(OCSKey.DIY_WORKLIST_LIKE, paramMap.get("workId")+"");
		map.put("heart", heart);
		
		map.put("isVote", 0);
		return map;
	}
	
	@Override
	public int userActivite(Map<String,Object>paramMap)throws Exception{
		String sql="SELECT activite FROM diy_user_wx WHERE wx_id=:openid";
		return super.npjt.queryForObject(sql,paramMap,Integer.class);
	}
	
	@Override
	public int userActiviteforUserId(Map<String,Object>paramMap)throws Exception{
		String sql="SELECT active FROM diy_device_user WHERE id=:user";
		return super.npjt.queryForObject(sql,paramMap,Integer.class);
	}
	
	@Override
	public int userActiviteApp(Map<String,Object>paramMap)throws Exception{
		String sql="SELECT id FROM diy_works_vote WHERE userId=:userId AND workId=:workId";
		return super.npjt.queryForObject(sql,paramMap,Integer.class);
	}
	
	@Override
	public int userVote(Map<String,Object>paramMap)throws Exception{
		String sql="UPDATE diy_user_wx SET activite=activite-1 WHERE wx_id=:openid";
		return super.npjt.update(sql, paramMap);
	}
	
	@Override
	public int userVoteforUserId(Map<String,Object>paramMap)throws Exception{
		String sql="UPDATE diy_device_user SET active=active-1 WHERE id=:user";
		return super.npjt.update(sql, paramMap);
	}
	
	@Override
	public int upHeart(Map<String,Object>paramMap)throws Exception{
		String sql="UPDATE diy_works_list SET heart=heart+1 WHERE id=:workId";
		return super.npjt.update(sql, paramMap);
	}
	
	@Override
	public int workHeart(Map<String,Object>paramMap)throws Exception{
		String sql="SELECT heart FROM diy_works_list WHERE id=:workId";
		return super.npjt.queryForObject(sql, paramMap, Integer.class);
	}
	
	@Override
	public int setUserHeart(String workId)throws Exception{
		String sql="UPDATE diy_works_list SET heart=heart+20 WHERE id=?";
		return super.jt.update(sql,workId);
	}

	@Override
	public int insertVoteWorks(Map<String, Object> paramMap) throws Exception{
		String sql="INSERT INTO diy_works_vote (userId,workId,type) values(:userId,:workId,:type)";
		return super.npjt.update(sql, paramMap);
	}

	@Override
	public int getWorkVoteCount(Map<String, Object> paramMap) throws Exception{
		String sql = "SELECT count(id) FROM diy_works_vote WHERE userId=:userId AND workId=:workId AND type=:type";
		return super.npjt.queryForObject(sql, paramMap,Integer.class);
	}
}
