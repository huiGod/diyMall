package cn._51app.dao.impl;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import cn._51app.dao.BaseDao;
import cn._51app.dao.IFocusDao;

@Repository
public class FocusDao extends BaseDao implements IFocusDao{

	@Override
	public String getPrivilege(Map<String,Object> paramMap) throws Exception {
		//只显示三条
		String cacheKey =paramMap.get("cacheKey").toString();
		
		String select ="SELECT id AS pid,about,org_price AS orgPrice,des_price AS desPrice",
				from =" FROM diy_privilege",
				where =" WHERE `status`=1",
				orderBy =" ORDER BY des_price ASC",
				limit =" LIMIT 3";
		String sql =select+from+where+orderBy+limit;
		if(super.isCacheNull(cacheKey).equals("a")){
			List<Map<String,Object>> lm =super.npjt.queryForList(sql, paramMap);
			if(!lm.isEmpty()){
				int cacheTime =new Integer(paramMap.get("cacheTime").toString());
				return super.saveAndGet(lm, cacheKey, cacheTime);
				
				
			}
			
		}else if(isCacheNull(cacheKey).equals("b")){
			
			return super.q(cacheKey);
		}else{
			List<Map<String,Object>> lm =super.npjt.queryForList(sql, paramMap);
			if(!lm.isEmpty()){
				return super.toJson(lm);
			}
		}
		return "";
	}

	@Override
	public String queryMaterialByType(Map<String, Object> paramMap) throws Exception {
		String cacheKey =paramMap.get("cacheKey").toString();
		
		//获取图片数量分组
		String sql ="SELECT num FROM diy_material GROUP BY num";
		List<Map<String,Object>> lmNum =super.jt.queryForList(sql);
		if(lmNum.isEmpty()) return null;
		
		String typeStr =paramMap.get("typeArr").toString();
		String typeArr[] = typeStr.split(",");
		
		String select ="SELECT `id`,`material_url` as `url`,`count`,`num`";
		String from =" FROM diy_material";
		String where =" WHERE `publish`=1 AND `type`=:type AND `num`=:num";// AND `num`=:num 错误是`publish`=0
		String orderBy =" ORDER BY `sort` ASC";
		
		sql =select+from+where+orderBy;		
		
		List<Map<String,Object>> lm =null;
		Map<String,Object> mm =null;
		
		
		if(super.isCacheNull(cacheKey).equals("a")){
			
			
			
			Set<Object> lmro =new HashSet<Object>();
			
				for(int i=0;i<typeArr.length;i++){
					List<Map<String,Object>> lmr =new ArrayList<Map<String,Object>>();	
					if(typeArr[i].equals("1")){
						int id =jt.queryForObject(gidSql("1"), Integer.class);
						mm =new HashMap<String,Object>();
						mm.put("name", paramMap.get("glassName"));
						mm.put("price", paramMap.get("glassPrice"));
						mm.put("color", paramMap.get("glassColor"));
						mm.put("tmp", paramMap.get("glassTmp"));
						mm.put("ico", paramMap.get("glassIco"));
						mm.put("itemSize", paramMap.get("glassItemSize"));
						mm.put("gid", id);
					}else if(typeArr[i].equals("3")){
						int id =jt.queryForObject(gidSql("3"), Integer.class);
						mm =new HashMap<String,Object>();
						mm.put("name", paramMap.get("pillowName"));
						mm.put("price", paramMap.get("pillowPrice"));
						mm.put("color", paramMap.get("pillowColor"));
						mm.put("tmp", paramMap.get("pillowTmp"));
						mm.put("ico", paramMap.get("pillowIco"));
						mm.put("itemSize", paramMap.get("pillowItemSize"));
						mm.put("gid", id);
					}else if(typeArr[i].equals("2")){
						int id =jt.queryForObject(gidSql("2"), Integer.class);
						mm =new HashMap<String,Object>();
						mm.put("name", paramMap.get("mpsName"));
						mm.put("price", paramMap.get("mpsPrice"));
						mm.put("color", paramMap.get("mpsColor"));
						mm.put("tmp", paramMap.get("mpsTmp"));
						mm.put("ico", paramMap.get("mpsIco"));
						mm.put("itemSize", paramMap.get("mpsItemSize"));
						mm.put("gid", id);
					}else if(typeArr[i].equals("4")){
						int id =jt.queryForObject(gidSql("4"), Integer.class);
						mm =new HashMap<String,Object>();
						mm.put("name", paramMap.get("TName"));
						mm.put("price", paramMap.get("TPrice"));
						mm.put("color", paramMap.get("TColor"));
						mm.put("tmp", paramMap.get("TTmp"));
						mm.put("ico", paramMap.get("TIco"));
						mm.put("itemSize", paramMap.get("TItemSize"));
						mm.put("gid", id);
						
					}
					
					for(int j=0;j<lmNum.size();j++){
						paramMap.put("type", new Integer(typeArr[i]));
						paramMap.put("num", lmNum.get(j).get("num").toString());
						lm =super.npjt.queryForList(sql, paramMap);
						if(!lm.isEmpty()){
							String dmRootUrl =paramMap.get("dmRootUrl").toString();
							Object obj =null;
							for(Map<String,Object> m:lm){
								obj =m.get("url");
								if(obj!=null){
									m.put("url", dmRootUrl+obj.toString());
								}
							}
								if(typeArr[i].equals("1")){
									mm.put("l"+lmNum.get(j).get("num").toString(), lm);
									lmr.add(mm);
								}else if(typeArr[i].equals("2")){
									mm.put("l"+lmNum.get(j).get("num").toString(), lm);
									lmr.add(mm);
								}else if(typeArr[i].equals("3")){
									mm.put("l"+lmNum.get(j).get("num").toString(), lm);
									lmr.add(mm);
								}else if(typeArr[i].equals("4")){
									mm.put("l"+lmNum.get(j).get("num").toString(), lm);
									lmr.add(mm);
								}
						}
				}
					
					if(lmr.isEmpty()) return null;
					
					if(typeArr[i].equals("1")){
							lmro.add(lmr.get(0));
					}else if(typeArr[i].equals("2")){
							lmro.add(lmr.get(0));
					}else if(typeArr[i].equals("3")){
							lmro.add(lmr.get(0));
					}else if(typeArr[i].equals("4")){
						lmro.add(lmr.get(0));
				}
		}
				
			Iterator<Object> it=lmro.iterator();
			 while (it.hasNext()) {
				 List<Map<String,Object>> rlm =new ArrayList<Map<String,Object>>();
				 Map<String,Object> mT =(Map<String,Object>)it.next();
				 String key ="";
				 for (Map.Entry<String,Object> entry : mT.entrySet()) {
					 if(entry.getValue() instanceof List){
						 key=entry.getKey();
						 Map<String,Object> rm =new HashMap<String,Object>();
						 rm.put(key, entry.getValue());
						 rlm.add(rm);
					 }
				 }
				 mT.put("mList", rlm);
			  }
			 
			 List<Object> listr = new ArrayList<Object>(lmro); 
			 for(int l=0;l<listr.size();l++){
				 Map<String,Object> mT =(Map<String,Object>)listr.get(l);
				 mT.remove("l1");
				 mT.remove("l2");
				 mT.remove("l3");
			 }
			 
			int cacheTime =new Integer(paramMap.get("cacheTime").toString());
			
			return super.saveAndGet(lmro, cacheKey, cacheTime);
			
		}else if(isCacheNull(cacheKey).equals("b")){
			return super.q(cacheKey);
		}else{

			
			
			
			Set<Object> lmro =new HashSet<Object>();
			
				for(int i=0;i<typeArr.length;i++){
					List<Map<String,Object>> lmr =new ArrayList<Map<String,Object>>();	
					if(typeArr[i].equals("1")){
						int id =jt.queryForObject(gidSql("1"), Integer.class);
						mm =new HashMap<String,Object>();
						mm.put("name", paramMap.get("glassName"));
						mm.put("price", paramMap.get("glassPrice"));
						mm.put("color", paramMap.get("glassColor"));
						mm.put("tmp", paramMap.get("glassTmp"));
						mm.put("ico", paramMap.get("glassIco"));
						mm.put("itemSize", paramMap.get("glassItemSize"));
						mm.put("gid", id);
					}else if(typeArr[i].equals("3")){
						int id =jt.queryForObject(gidSql("3"), Integer.class);
						mm =new HashMap<String,Object>();
						mm.put("name", paramMap.get("pillowName"));
						mm.put("price", paramMap.get("pillowPrice"));
						mm.put("color", paramMap.get("pillowColor"));
						mm.put("tmp", paramMap.get("pillowTmp"));
						mm.put("ico", paramMap.get("pillowIco"));
						mm.put("itemSize", paramMap.get("pillowItemSize"));
						mm.put("gid", id);
					}else if(typeArr[i].equals("2")){
						int id =jt.queryForObject(gidSql("2"), Integer.class);
						mm =new HashMap<String,Object>();
						mm.put("name", paramMap.get("mpsName"));
						mm.put("price", paramMap.get("mpsPrice"));
						mm.put("color", paramMap.get("mpsColor"));
						mm.put("tmp", paramMap.get("mpsTmp"));
						mm.put("ico", paramMap.get("mpsIco"));
						mm.put("itemSize", paramMap.get("mpsItemSize"));
						mm.put("gid", id);
					}else if(typeArr[i].equals("4")){
						int id =jt.queryForObject(gidSql("4"), Integer.class);
						mm =new HashMap<String,Object>();
						mm.put("name", paramMap.get("TName"));
						mm.put("price", paramMap.get("TPrice"));
						mm.put("color", paramMap.get("TColor"));
						mm.put("tmp", paramMap.get("TTmp"));
						mm.put("ico", paramMap.get("TIco"));
						mm.put("itemSize", paramMap.get("TItemSize"));
						mm.put("gid", id);
						
					}
					
					for(int j=0;j<lmNum.size();j++){
						paramMap.put("type", new Integer(typeArr[i]));
						paramMap.put("num", lmNum.get(j).get("num").toString());
						lm =super.npjt.queryForList(sql, paramMap);
						if(!lm.isEmpty()){
							String dmRootUrl =paramMap.get("dmRootUrl").toString();
							Object obj =null;
							for(Map<String,Object> m:lm){
								obj =m.get("url");
								if(obj!=null){
									m.put("url", dmRootUrl+obj.toString());
								}
							}
								if(typeArr[i].equals("1")){
									mm.put("l"+lmNum.get(j).get("num").toString(), lm);
									lmr.add(mm);
								}else if(typeArr[i].equals("2")){
									mm.put("l"+lmNum.get(j).get("num").toString(), lm);
									lmr.add(mm);
								}else if(typeArr[i].equals("3")){
									mm.put("l"+lmNum.get(j).get("num").toString(), lm);
									lmr.add(mm);
								}else if(typeArr[i].equals("4")){
									mm.put("l"+lmNum.get(j).get("num").toString(), lm);
									lmr.add(mm);
								}
						}
				}
					
					if(lmr.isEmpty()) return null;
					
					if(typeArr[i].equals("1")){
							lmro.add(lmr.get(0));
					}else if(typeArr[i].equals("2")){
							lmro.add(lmr.get(0));
					}else if(typeArr[i].equals("3")){
							lmro.add(lmr.get(0));
					}else if(typeArr[i].equals("4")){
						lmro.add(lmr.get(0));
				}
		}
				
			Iterator<Object> it=lmro.iterator();
			 while (it.hasNext()) {
				 List<Map<String,Object>> rlm =new ArrayList<Map<String,Object>>();
				 Map<String,Object> mT =(Map<String,Object>)it.next();
				 String key ="";
				 for (Map.Entry<String,Object> entry : mT.entrySet()) {
					 if(entry.getValue() instanceof List){
						 key=entry.getKey();
						 Map<String,Object> rm =new HashMap<String,Object>();
						 rm.put(key, entry.getValue());
						 rlm.add(rm);
					 }
				 }
				 mT.put("mList", rlm);
			  }
			 
			 List<Object> listr = new ArrayList<Object>(lmro); 
			 for(int l=0;l<listr.size();l++){
				 Map<String,Object> mT =(Map<String,Object>)listr.get(l);
				 mT.remove("l1");
				 mT.remove("l2");
				 mT.remove("l3");
			 }
			return super.toJson(lmro);
		}
		
		
	}

	private String gidSql(String gtidStr){
		return "SELECT id FROM diy_good WHERE name='"+gtidStr+"' AND type=1 LIMIT 1";
	}

	@Override
	public List<Map<String, Object>> getShopCartC(String orderNo) throws Exception {
		String sql="SELECT ds.goods_id AS goodsId,gType,ds.price,ds.store_id AS storeId, dso.num, ds.param, ds.img_url AS imgUrl,ds.file_type AS ft,ds.deviceNo,ds.sys,ds.app"
				+ " FROM diy_shopcart ds INNER JOIN diy_shopcart_order dso ON ds.id=dso.scId"
				+ " WHERE dso.after_order_no='"+orderNo+"' AND dso.state='0'";
		return super.jt.queryForList(sql);
	}

	@Override
	public int saveOrUpdateSCT(Map<String, Object> paramMap) throws Exception {
		boolean b =new Boolean((paramMap.get("add")).toString());
		if(b){
			//添加
			String sql="INSERT INTO diy_shopcart (goods_id,gType,price,store_id,param,img_url,file_type,deviceNo,sys,app,creat_time)"
					+ " VALUES(:goodsId,:gType,:price,:storeId,:param,:imgUrl,:ft,:deviceNo,:sys,:app,NOW())";//
			SqlParameterSource ps=new MapSqlParameterSource(paramMap); 
			 KeyHolder key=new GeneratedKeyHolder();
			//获取主键
			 super.npjt.update(sql, ps, key);
			 int id=key.getKey().intValue(); 
			 int storeId =new Integer(paramMap.get("storeId").toString());
			 
			String orderNo =paramMap.get("orderNo").toString();
			
			 sql="INSERT INTO diy_shopcart_order (scId,store_id,state,num,before_order_no)"
			 		+ " VALUES("+id+","+storeId+",'1',1,'"+orderNo+"')";	
			
			 int result=jt.update(sql);
			 if(result==0){
				 sql="DELETE FROM diy_shopcart WHERE id="+id;
				 jt.update(sql);
			 } 
			 return result;
		}else{
			String sql="UPDATE diy_shopcart_order"
					+ " SET num=num+1"//:num
					+ " WHERE before_order_no=:orderNo AND state IN ('1','2') AND scId IN (SELECT id FROM diy_shopcart WHERE goods_id =:goodsId)";
			return super.npjt.update(sql, paramMap);
		}
		
	}

	@Override
	public int saveOrUpdateSCC(Map<String, Object> paramMap) throws Exception {
		
		boolean b =new Boolean((paramMap.get("add")).toString());
		if(b){
			//添加
			String sql="INSERT INTO diy_shopcart (goods_id,gType,price,store_id,param,img_url,file_type,deviceNo,sys,app,creat_time)"
					+ " VALUES(:goodsId,:gType,:price,:storeId,:param,:imgUrl,:ft,:deviceNo,:sys,:app,NOW())";//
			SqlParameterSource ps=new MapSqlParameterSource(paramMap); 
			 KeyHolder key=new GeneratedKeyHolder();
			//获取主键
			 super.npjt.update(sql, ps, key);
			 int id=key.getKey().intValue(); 
			 int storeId =new Integer(paramMap.get("storeId").toString());
			 //int num =new Integer(paramMap.get("num").toString());
			String orderNo =paramMap.get("orderNo").toString();
			
			 sql="INSERT INTO diy_shopcart_order (scId,store_id,state,num,before_order_no)"
			 		+ " VALUES("+id+","+storeId+",'1',1,'"+orderNo+"')";	
			
			 int result=jt.update(sql);
			 if(result==0){
				 sql="DELETE FROM diy_shopcart WHERE id="+id;
				 jt.update(sql);
			 } 
			 return result;
		}else{
			//修改数量goodsId
			
			String sql="UPDATE diy_shopcart_order"
					+ " SET num=num+1"//:num
					+ " WHERE before_order_no=:orderNo AND state IN ('1','2') AND scId IN (SELECT id FROM diy_shopcart WHERE goods_id =:goodsId)";
			
			return super.npjt.update(sql, paramMap);
		}
		
	
		
		
	}

	@Override
	public String getCoupon(Map<String,Object> paramMap) throws Exception {
		//只显示三条
				String cacheKey =paramMap.get("cacheKey").toString();
				String select ="SELECT id AS cid,title,about,org_price AS orgPrice,des_price AS desPrice,valid",
						from =" FROM diy_coupon",
						where =" WHERE valid>=CURDATE()",
						orderBy =" ORDER BY des_price ASC",
						limit =" LIMIT 5";
				String sql =select+from+where+orderBy+limit;
				
				if(super.isCacheNull(cacheKey).equals("a")){
					
					List<Map<String,Object>> lm =super.npjt.queryForList(sql, paramMap);
					if(!lm.isEmpty()){
						int cacheTime =new Integer(paramMap.get("cacheTime").toString());
						return super.saveAndGet(lm, cacheKey, cacheTime);
						
					}
				}else if(isCacheNull(cacheKey).equals("b")){
					return super.q(cacheKey);
					
				}else{
					List<Map<String,Object>> lm =super.npjt.queryForList(sql, paramMap);
					if(!lm.isEmpty()){
						return super.toJson(lm);
					}
					
					
				}
				return "";
	}

	@Override
	public Map<String,Object> getPrivilegeId(double feeTotal) throws Exception {
		String sql ="SELECT id,des_price AS desPrice FROM diy_coupon WHERE org_price<="+feeTotal+" ORDER BY org_price DESC";
		return super.jt.queryForMap(sql);
	}
	
	@Override
	public String drawSwitch(Map<String,Object> paramMap) throws Exception {
		String cacheKey =paramMap.get("cacheKey").toString();
		
		
		//获取有效期
		String sql ="SELECT switch,valid FROM diy_draw ORDER BY creatime DESC LIMIT 1";
		Map<String,Object> m=null;
		boolean b =false;
		
		if(super.isCacheNull(cacheKey).equals("a")){
			
			try {
				m=super.jt.queryForMap(sql);
				String valid =m.get("valid").toString();
				paramMap.put("valid", valid);
				sql="SELECT des_price FROM diy_coupon_user WHERE valid=:valid AND deviceNo=:deviceNo AND app=:app";
				//查询设备有没有抽奖，没有返回true
				b =super.npjt.queryForList(sql, paramMap).isEmpty();
				//查询后台设置有没有抽奖
				b =new Boolean(m.get("switch").toString());
			} catch (EmptyResultDataAccessException e) {
				return null;
			}
			int cacheTime =new Integer(paramMap.get("cacheTime").toString());
			String json =super.saveAndGet(b, cacheKey, cacheTime);
			if(json!=null){
				return json;
			}
			return super.toJson(b);
			
		}else if(isCacheNull(cacheKey).equals("b")){
			return super.q(cacheKey);
		}else{
			try {
				m=super.jt.queryForMap(sql);
				String valid =m.get("valid").toString();
				paramMap.put("valid", valid);
				sql="SELECT des_price FROM diy_coupon_user WHERE valid=:valid AND deviceNo=:deviceNo AND app=:app";
				//查询设备有没有抽奖，没有返回true
				b =super.npjt.queryForList(sql, paramMap).isEmpty();
				//查询后台设置有没有抽奖
				b =new Boolean(m.get("switch").toString());
			} catch (EmptyResultDataAccessException e) {
				return "";
			}
			
			return super.toJson(b);
		}
		
	}

	@Override
	public String getDrawLevelAndValid(String cacheKey, int cacheTime) throws Exception {
		String sql ="SELECT level,valid FROM diy_draw ORDER BY creatime DESC LIMIT 1";
		Map<String,Object> m=null;
		if(super.isCacheNull(cacheKey).equals("a")){
			try {
				m=super.jt.queryForMap(sql);
			} catch (EmptyResultDataAccessException e) {
				return "";
			}
			return super.saveAndGet(m, cacheKey, cacheTime);
			
		}else if(isCacheNull(cacheKey).equals("b")){
			return super.q(cacheKey);
		}else{
			try {
				m=super.jt.queryForMap(sql);
			} catch (EmptyResultDataAccessException e) {
				return "";
			}
			return super.toJson(m);
			
			
		}
		
	}

	@Override
	public int getCouponIdByLevel(String levelStr) throws Exception {
		int level =new Integer(levelStr);
		String sql="SELECT id FROM diy_coupon WHERE level="+level;
		return super.jt.queryForObject(sql, Integer.class);
	}

	@Override
	public String versionListI(String cacheKey, int cacheTime) throws Exception {
		String sql ="SELECT v.version version,v.level level FROM diy_version_i AS v";
		if(super.isCacheNull(cacheKey).equals("a")){
			
			List<Map<String,Object>> lm =super.jt.queryForList(sql);
			if(!lm.isEmpty()){
				StringBuffer sb=new StringBuffer();
				for(Map<String, Object> map:lm){
					String versionString=map.get("version").toString();
					sb.append(versionString).append(",");
				}
				return super.saveAndGet(sb, cacheKey, cacheTime);
				
			}
		}else if(super.isCacheNull(cacheKey).equals("b")){
			return super.q(cacheKey);
		}else{
			List<Map<String,Object>> lm =super.jt.queryForList(sql);
			if(!lm.isEmpty()){
				StringBuffer sb=new StringBuffer();
				for(Map<String, Object> map:lm){
					String versionString=map.get("version").toString();
					sb.append(versionString).append(",");
				}
				return super.toJson(sb);
		}
		
	}
		return "";
	}

	@Override
	public int getLevelByVersion(String v) throws Exception {
		String sql = "SELECT level FROM diy_version_i WHERE version ='"+v+"'";
		int result =0;
		try {
			result=super.jt.queryForObject(sql, Integer.class);
		} catch (EmptyResultDataAccessException e) {
			return 0;
		}
		return result;
	}

	@Override
	public String getLatestVersion(String cacheKey, int cacheTime) throws Exception {
		String sql = "SELECT about,version,appleUrl,level FROM diy_version_i ORDER BY ctime DESC LIMIT 1";
		Map<String,Object> m=null;
		if(super.isCacheNull(cacheKey).equals("a")){
			try {
				m=super.jt.queryForMap(sql);
			} catch (EmptyResultDataAccessException e) {
				return null;
			}
			return super.saveAndGet(m, cacheKey, cacheTime);
			
		}else if(isCacheNull(cacheKey).equals("b")){
			return super.q(cacheKey);
		}else{
			try {
				m=super.jt.queryForMap(sql);
			} catch (EmptyResultDataAccessException e) {
				return null;
			}
			return super.toJson(m);
		}
		
	}

	

	@Override
	public int getShopCartNum(String dn, String app) throws Exception {
		// 一个设备只能有一个app
		String sql="SELECT COUNT(ds.id) FROM diy_shopcart ds INNER JOIN diy_shopcart_order dso ON ds.id=dso.scId WHERE dso.state IN ('1','2') AND ds.deviceNo ='"+dn+"' AND ds.app ='"+app+"'";
		try {
			int num =super.jt.queryForObject(sql, Integer.class);
			return num;
		} catch (EmptyResultDataAccessException e) {
			return 0;
		}
	}
	
	//无论插入结果，返回为1
	@Override
	public int insertDevice(String dn,String app) throws Exception {
		int result =0;
		try {
			String insert ="INSERT INTO diy_device_user (device_no,app,ctime)",
					value=" VALUES('"+dn+"','"+app+"',now())";
			String sql =insert+value;
			result =jt.update(sql);
		} catch (DuplicateKeyException e) {
			result =1;
		}
		return result;
	}
	
	@Override
	public String materialList(Map<String, Object> paramMap) throws Exception{
		String cacheKey =paramMap.get("cacheKey").toString();
		int cacheTime =new Integer(paramMap.get("cacheTime").toString());
		if(super.isCacheNull(cacheKey).equals("b")){
			return super.q(cacheKey);
		}else{
			List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
			
			String typesql = "SELECT title,id AS typeId,`background_color`,`font_color` FROM `diy_material_type` WHERE `about`=? AND status='1' ORDER BY sort";
			String materialsql = "SELECT id,`material_url`,`count`,`num`,`pid` AS typeId FROM diy_material WHERE `type`=? AND publish=1 ORDER BY sort";
			
			String numsql = "SELECT name,price,color,itemSize,ico,tmp,gid,t_id FROM diy_material_title WHERE status=1 ORDER BY sort";
			List<Map<String,Object>> list = jt.queryForList(numsql);
			String dmRootUrl =paramMap.get("dmRootUrl").toString();
			for (int i = 0; i < list.size(); i++) {
				String type=list.get(i).get("t_id")==null?"":list.get(i).get("t_id").toString();
				String ico=list.get(i).get("ico")==null?"":list.get(i).get("ico").toString();
				list.get(i).put("ico",dmRootUrl+ico);
				
					List<Map<String,Object>> typelist = jt.queryForList(typesql,new Object[]{type});
					list.get(i).put("typeList", typelist);
					List<Map<String,Object>> listl = jt.queryForList(materialsql,new Object[]{type});
					for (Map<String, Object> map : listl) {
						String material_url = null==map.get("material_url")?"":map.get("material_url").toString();
						if(StringUtils.isNotEmpty(material_url)){
							map.put("material_url", dmRootUrl+material_url);
						}
					}
					
					list.get(i).put("mList", listl);
					result.add(list.get(i));
					
			}
			return super.saveAndGet(result, cacheKey, cacheTime);
		}
	}
	
}
