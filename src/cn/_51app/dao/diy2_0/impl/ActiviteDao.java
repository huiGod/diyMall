package cn._51app.dao.diy2_0.impl;

import java.util.Map;

import org.springframework.stereotype.Repository;

import cn._51app.dao.BaseDao;
import cn._51app.dao.diy2_0.IActiviteDao;

@Repository
public class ActiviteDao extends BaseDao implements IActiviteDao{
	
	@Override
	public Map<String, Object> activiteList(String type) {
		StringBuilder sql=new StringBuilder();
		sql.append("SELECT * FROM `diy_activity` WHERE ");
		if("0".equals(type)){
			sql.append("`status`=1");
		}else{
			sql.append("`type`="+type);
		}
		sql.append(" LIMIT 1");
		return this.jt.queryForMap(sql.toString());
	}
	
	@Override
	public Map<String,Object>activiterList2(String activityId){
		String sql="SELECT * FROM `diy_activity` WHERE status=1 AND company_id=? AND start_time<=now() AND end_time>=now() LIMIT 1";
		Map<String,Object>map=this.jt.queryForMap(sql,activityId);
		//查不到活动或type=7表示没有活动优惠
		if(map==null || map.get("type").equals("7")){
			return null;
		}
		return map;
	}
	
}
