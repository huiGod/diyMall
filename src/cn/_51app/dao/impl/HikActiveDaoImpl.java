package cn._51app.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;



import cn._51app.dao.BaseDao;
import cn._51app.dao.diy2_0.HikActiveDao;

@Repository
public class HikActiveDaoImpl extends BaseDao implements HikActiveDao{

	@Override
	public int getUserInfo(String id){
		String sql = "select count(id) from diy_user_app where user_id='"+id+"'";
		return super.jt.queryForObject(sql,Integer.class);
	}

	@Override
	public String getInfo(Map<String, Object> paramMap) throws Exception {
		Map<String,Object> rm = new HashMap<String,Object>();
		//设置sql参数
		String ll="SET @Rank=0 and @hear=0 and @oldhear=0";
		List<Map<String,Object>> rListT = new ArrayList<Map<String,Object>>();
		super.jt.execute(ll);
		
		String id = paramMap.get("user_id").toString();
		String rankSql = "select id from diy_user_app order by heart desc";
		List<Map<String,Object>> list = super.jt.queryForList(rankSql);
		
		
		List<Map<String,Object>> rList = new ArrayList<Map<String,Object>>();
		
		
		for(int i=0;i<list.size();i++){
			Map<String,Object> map = list.get(i);
			Map<String,Object> mapOther=new HashMap<String, Object>();
			String oId = map.get("id").toString();
			
			String sql =  "select ua.user_id,ua.content,ua.title,(@hear:=ua.heart )as heart,if(@oldhear=@hear,@Rank,@Rank:=@Rank+1) as rank,@oldhear:=@hear,ng.showImg  from diy_user_app ua   left join diy_hik_active ha on ha.user_id = ua.user_id left join new_goods_info ng on ng.id= ua.newGoods_id where ua.id ="+oId+"  order by ua.heart desc ";
			mapOther= super.npjt.queryForMap(sql, paramMap);
			mapOther.remove("@oldhear:=@hear");
			if(mapOther.get("showImg")!=null){
				String showImg=mapOther.get("showImg").toString();
				if(showImg.indexOf(",")!=-1){
					showImg=showImg.substring(0,showImg.indexOf(","));
					mapOther.put("showImg", showImg);
				}
			}
			
			mapOther.put("showImg", "http://file.diy.51app.cn/"+mapOther.get("showImg"));
			if(id.equals(mapOther.get("user_id").toString())){
				mapOther.remove("user_id");
				rListT.add(mapOther);
				continue;
			}
			mapOther.remove("content");
			mapOther.remove("user_id");
			rList.add(mapOther);
		}
		rm.put("me", rListT);
		rm.put("others", rList);
		return super.toJson(rm);
	}

	@Override
	public String appHik(Map<String, Object> paramMap) throws Exception {
		Map<String,Object> rm = new HashMap<String,Object>();
		String search = "select count(id) from diy_hik_active where user_id='"+paramMap.get("user_id")+"'";
		Integer i = super.jt.queryForObject(search, Integer.class);
		//点赞用户表是否有用户存在
		if(i!=0){
			Integer count=0;
			String selectOne="select count(id) from diy_device_user where device_no='"+paramMap.get("user_id")+"'";
			count = super.jt.queryForObject(selectOne, Integer.class);
			if(count==0){
				selectOne="select count(id) from diy_device_user where openid='"+paramMap.get("user_id")+"'";
				count=super.jt.queryForObject(selectOne, Integer.class);
				if(count==0){
					return "";
				}
			}
			String select ="select active from diy_hik_active where user_id='"+paramMap.get("user_id")+"'";
			Integer index = super.jt.queryForObject(select, Integer.class);
			if(index>0){
				String update = "update diy_hik_active set active=active-1 where user_id='"+paramMap.get("user_id")+"'";
				String updateT = "update diy_user_app ua set ua.heart=ua.heart+1 , ua.coupon_active=if(ua.heart=30,1,0) where ua.newGoods_id="+paramMap.get("goods_id");
				//设置点赞用户点赞次数==0失效不能点赞
				super.jt.update(update);
				//添加优惠券
				super.jt.update(updateT);
				String selectT = "select ngi.showImg,ua.title,ua.newGoods_id,ua.content,ua.heart  from diy_user_app ua LEFT JOIN new_goods_info ngi ON ngi.id=ua.newGoods_id where newGoods_id="+paramMap.get("goods_id");
				//查询用户点赞过的作品，返回数据
				List<Map<String,Object>> rl = super.npjt.queryForList(selectT, paramMap);
				rm.put("goods", rl);
				return super.toJson(rm);
			}else{
				return super.toJson("0");
			}
		//点赞表没有记录时候
		}else{
			Integer count=0;
			String selectOne="select count(id) from diy_device_user where device_no='"+paramMap.get("user_id")+"'";
			//查询设备号为用户id
			count = super.jt.queryForObject(selectOne, Integer.class);
			//openid也是==用户id
			if(count==0){
				selectOne="select count(id) from diy_device_user where openid='"+paramMap.get("user_id")+"'";
				count=super.jt.queryForObject(selectOne, Integer.class);
				if(count==0){
					return "";
				}
			}
			//插入点赞用户表数据，设置为1个点赞机会
			String insert = "insert into diy_hik_active (id,active,user_id) values (null,1,'"+paramMap.get("user_id")+"')";
			super.jt.update(insert);
			String select ="select active from diy_hik_active where user_id='"+paramMap.get("user_id")+"'";
			//查询点赞机会
			Integer index = super.jt.queryForObject(select, Integer.class);
			if(index>0){
				String update = "update diy_hik_active set active=0 where user_id='"+paramMap.get("user_id")+"'";
				String updateT = "update diy_user_app ua set ua.heart=ua.heart+1 , ua.coupon_active=if(ua.heart=30,1,0) where ua.newGoods_id="+paramMap.get("goods_id");
				//清空优惠券
				super.jt.update(update);
				//判断用户作品点赞超过30就送id为1的优惠券
				super.jt.update(updateT);
				String selectT = "select ngi.showImg,ua.title,ua.newGoods_id,ua.content,ua.heart  from diy_user_app ua LEFT JOIN new_goods_info ngi ON ngi.id=ua.newGoods_id where newGoods_id="+paramMap.get("goods_id");
				
				List<Map<String,Object>> rl = super.npjt.queryForList(selectT, paramMap);
				rm.put("goods", rl);
				return super.toJson(rm);
			}else{
				String selectT = "select ngi.showImg,ua.title,ua.newGoods_id,ua.content,ua.heart  from diy_user_app ua LEFT JOIN new_goods_info ngi ON ngi.id=ua.newGoods_id where newGoods_id="+paramMap.get("goods_id");
				List<Map<String,Object>> rl = super.npjt.queryForList(selectT, paramMap);
				rm.put("goods", rl);
				return super.toJson("0");
			}
		}
		
	}

	@Override
	public String wxHik(Map<String, Object> paramMap) throws Exception {
		Map<String,Object> rm = new HashMap<String,Object>();
		String search = "select count(id) from diy_hik_active where user_id='"+paramMap.get("openid")+"'";
		Integer i = super.jt.queryForObject(search, Integer.class);
		if(i!=0){
			Integer count=0;
			String selectOne="select count(id) from diy_user_wx where wx_id='"+paramMap.get("openid")+"'";
			count = super.jt.queryForObject(selectOne, Integer.class);
			if(count==0){
				return "";
			}
			String select ="select active from diy_hik_active where user_id='"+paramMap.get("openid")+"'";
			Integer index = super.jt.queryForObject(select, Integer.class);
			if(index>0){
				String update = "update diy_hik_active set active=0 where user_id='"+paramMap.get("openid")+"'";
				String updateT = "update diy_user_app ua set ua.heart=ua.heart+1 , ua.coupon_active=if(ua.heart=30,1,0) where ua.newGoods_id="+paramMap.get("goods_id");
				super.jt.update(update);
				super.jt.update(updateT);
				String selectT = "select ngi.showImg,ua.title,ua.newGoods_id,ua.content,ua.heart from diy_user_app ua LEFT JOIN new_goods_info ngi ON ngi.id=ua.newGoods_id   where newGoods_id="+paramMap.get("goods_id");
				List<Map<String,Object>> rl = super.npjt.queryForList(selectT, paramMap);
				rm.put("goods", rl);
//				for(Map<String,Object> map:rl){
//					if((Integer)map.get("heart")==30){
//						update = "update diy_user_app set coupon_active="+"123451"+" where newGoods_id="+paramMap.get("goods_id");
//						super.jt.update(update);
//					}else{
//						continue;
//					}
//				}
				return super.toJson(rm);
			}else{
				return super.toJson("0");
			}
		}else{
			Integer count=0;
			String selectOne="select count(id) from diy_user_wx where wx_id='"+paramMap.get("openid")+"'";
			count = super.jt.queryForObject(selectOne, Integer.class);
			if(count==0){
				return "";
			}
			String insert = "insert into diy_hik_active (id,active,user_id) values (null,1,'"+paramMap.get("openid")+"')";
			super.jt.update(insert);
			String select ="select active from diy_hik_active where user_id='"+paramMap.get("openid")+"'";
			Integer index = super.jt.queryForObject(select, Integer.class);
			if(index>0){
				String update = "update diy_hik_active set active=0 where user_id='"+paramMap.get("openid")+"'";
				String updateT = "update diy_user_app ua set ua.heart=ua.heart+1 , ua.coupon_active=if(ua.heart=30,1,0) where ua.newGoods_id="+paramMap.get("goods_id");
				super.jt.update(update);
				super.jt.update(updateT);
				String selectT = "select ngi.showImg,ua.title,ua.newGoods_id,ua.content,ua.heart  from diy_user_app ua LEFT JOIN new_goods_info ngi ON ngi.id=ua.newGoods_id  where newGoods_id="+paramMap.get("goods_id");
				
				List<Map<String,Object>> rl = super.npjt.queryForList(selectT, paramMap);
//				for(Map<String,Object> map:rl){
//					if((Integer)map.get("heart")==30){
//						update = "update diy_user_app set coupon_active="+"123451"+" where newGoods_id="+paramMap.get("goods_id");
//						super.jt.update(update);
//					}else{
//						continue;
//					}
//				}
				rm.put("goods", rl);
				return super.toJson(rm);
			}else{
				return super.toJson("0");
			}
		}
		
	}

	@Override
	public String payGoods(Map<String, Object> paramMap) throws Exception {
		String select = "select showImg,name,parameter from new_goods_info  where id="+paramMap.get("goods_id");
	
		Map<String,Object> rm = super.jt.queryForMap(select);
		if(rm.get("parameter")!=null){
			String parameter = rm.get("parameter").toString();
			parameter=parameter.substring(parameter.indexOf("颜色分类#")+5,parameter.lastIndexOf("色")+1);
		
			rm.put("parameter", parameter);
		}
		rm.put("number", paramMap.get("number"));
		rm.put("price", paramMap.get("price"));
		
		return super.toJson(rm);
	}

	@Override
	public String wxLogin(Map<String, Object> paramMap) throws Exception {
		String login = "insert into diy_user_wx (id,wx_id) values (null,'"+paramMap.get("openid")+"')";
		super.jt.update(login);
		return super.toJson(paramMap);
	}


	
}
