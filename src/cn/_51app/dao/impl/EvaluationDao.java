package cn._51app.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import cn._51app.dao.BaseDao;
import cn._51app.dao.IEvaluationDao;
import cn._51app.entity.EvaluationInfo;
import cn._51app.util.PropertiesUtil;

@Repository
public class EvaluationDao extends BaseDao implements IEvaluationDao {
	
	private final String SYSPATH =PropertiesUtil.getValue("diy.goods.url");
	
	
	/**
	 * @author zhanglz
	 * @return 保存评论
	 */
	@Override
	public Integer saveEvaluation(EvaluationInfo info,String mobile,String texture) throws Exception{
		String sql = "insert into diy_evaluation(goodsId,evalType,content,cime,imgUrl,imgNull,deviceNo,mobile,texture,state) values (?,?,?,NOW(),?,?,?,?,?,0)";
		return jt.update(sql, new Object[]{info.getGoodsId(),info.getEvalType(),info.getContent(),info.getImgUrl(),info.getImgNull(),info.getDeviceNo(),mobile,texture});
	}
	
	@Override
	public Map<String,Object> getMobile4Order(String order){
		String sql = "SELECT mobile,texture_names FROM diy_orders WHERE order_no = ?";
		return jt.queryForList(sql, new Object[]{order}).get(0);
	}
	
	/**
	 * @author zhanglz
	 * @return 修改状态为已评论
	 */
	@Override
	public void updateEvaStatus(String orderNo){
		String sql = "UPDATE `diy_orders` SET `status`=7 WHERE `order_no`=?";
		int flag = jt.update(sql,new Object[]{orderNo});
		if(flag<1){
			throw new RuntimeException();
		}
	}
	
	/**
	 * @author zhanglz
	 * @return 查询评论类型的数量
	 */
	@Override
	public String getEvalTypeNum(Map<String,Object> paramMap) throws Exception{
		String cacheKey =paramMap.get("cacheKey").toString();
		del(cacheKey);
		String sql = "SELECT e.evalType,COUNT(*) AS number FROM diy_evaluation e , diy_goods_info i " +
				"WHERE e.`goodsId`=:goodsId AND e.`goodsId`=i.`id` AND e.`state`=1 GROUP BY e.evalType;";
		List<Map<String,Object>> m =null;
		if(super.isCacheNull(cacheKey).equals("a")||super.isCacheNull(cacheKey).equals("c")){
			//String sql ="SELECT evalType,COUNT(evalType) AS number FROM diy_evaluation where goodsId=:goodsId GROUP BY evalType";
			
			boolean isSame = false;
			try {
				int goodsId = (int) paramMap.get("goodsId");
				isSame = isSameName(goodsId);
			} catch (Exception e) {}
			
			try {
				Integer sum = 0;
				if (isSame) {
					m=npjt.queryForList(sql,paramMap);
					for (int i = 0; i < m.size(); i++) {
						Map<String,Object> map = m.get(i);
						sum += null==map.get("number") ? 0 : Integer.valueOf(map.get("number").toString());
					}
				}
				
				if (m == null) {
					m = new ArrayList<Map<String,Object>>();
				}
				
				Map<String,Object> newm = new HashMap<String, Object>();
				newm.put("evalType", "0");
				newm.put("number", sum);
				m.add(0, newm);
				if(m.size()<2){
					Map<String,Object> bu1 = new HashMap<String, Object>();
					bu1.put("evalType", "1");
					bu1.put("number", 0);
					m.add(1, bu1);
				}
				if(m.size()<3){
					Map<String,Object> bu2 = new HashMap<String, Object>();
					bu2.put("evalType", "2");
					bu2.put("number", 0);
					m.add(2, bu2);
				}
				if(m.size()<4){
					Map<String,Object> bu3 = new HashMap<String, Object>();
					bu3.put("evalType", "3");
					bu3.put("number", 0);
					m.add(3, bu3);
				}
				
				Map<String,Object> newm2 = new HashMap<String, Object>();
				newm2.put("evalType", "4");
				newm2.put("number", this.getEvalHavePic((int)paramMap.get("goodsId")));
				m.add(newm2);
			} catch (EmptyResultDataAccessException e) {}
			if(m!=null){
				if(super.isCacheNull(cacheKey).equals("a")){
					int cacheTime =new Integer(paramMap.get("cacheTime").toString());
					return super.saveAndGet(m, cacheKey, cacheTime);
				}else{
					return super.toJson(m);
				}
			}
		}else if(isCacheNull(cacheKey).equals("b")){
			return super.q(cacheKey);
		}
		return "";
	}
	
	/**
	 * 查询图片个数
	 * @param goodsId
	 * @return
	 */
	public int getEvalHavePic(int goodsId){
		int res = 0;
		try {
			if (isSameName(goodsId)) {
				String sql = "SELECT imgUrl FROM diy_evaluation WHERE imgNull=0 AND `state`=1 AND goodsId="+goodsId;
				List<Map<String,Object>> list = jt.queryForList(sql);
				for (Map<String, Object> map : list) {
					String imgUrl = null==map.get("imgUrl")?"":map.get("imgUrl").toString();
					String[] imgarr = imgUrl.split(",");
					res +=imgarr.length;
				}
			}
		} catch (Exception e) {}
		
		return res;
	}
	
	/**
	 * @author zhanglz
	 * @return 查询评论的图片
	 */
	@Override
	public String findEvalPic(Map<String,Object> paramMap) throws Exception{
		String cacheKey =paramMap.get("cacheKey").toString();
		String sql = "SELECT imgUrl FROM diy_evaluation WHERE goodsId=? and `state`=1 AND imgNull=0 ORDER BY cime desc LIMIT ?,?";
		List<Map<String,Object>> list =null;
		List<Map<String,Object>> result = null;
		if(super.isCacheNull(cacheKey).equals("a")||super.isCacheNull(cacheKey).equals("c")){
			
			try {
				list=jt.queryForList(sql,new Object[]{paramMap.get("goodsId"),paramMap.get("page"),paramMap.get("pagesize")});
				result=new ArrayList<>();
				if(list!=null && list.size()>0){
					for (int i = 0; i < list.size(); i++) {
						String imgUrls=(String)list.get(i).get("imgUrl");
						String temps[]=imgUrls.split(",");
						for (String temp : temps) {
							Map<String,Object> map = new HashMap<>();
							map.put("imgUrl", SYSPATH+temp);
							result.add(map);
						}
					}
				}
			} catch (EmptyResultDataAccessException e) {}
			if(result!=null){
				if(super.isCacheNull(cacheKey).equals("a")){
					int cacheTime =new Integer(paramMap.get("cacheTime").toString());
					return super.saveAndGet(result, cacheKey, cacheTime);
				}else{
					return super.toJson(result);
				}
			}
		}else if(isCacheNull(cacheKey).equals("b")){
			return super.q(cacheKey);
		}
		return "";
	}
	
	/**
	 * @author zhanglz
	 * @return 分页查询评论
	 */
	@Override
	public String findAllEval(Map<String,Object> paramMap) throws Exception{
		String cacheKey =paramMap.get("cacheKey").toString();
		
		String sql = "SELECT e.id as id,e.`mobile` AS mobile, e.evalType as evalType, " +
				"e.`content` as content ,e.`cime` AS creatTime,e.`imgUrl` as imgUrl,e.imgNull as imgNull,e.texture as texture "+
				"FROM diy_evaluation e , diy_goods_info i ";
		String where = "WHERE e.`goodsId`=:goodsId AND e.`goodsId`=i.`id` AND e.`state`=1 ";
		if(null!=paramMap.get("evalType")&&0!=(int)paramMap.get("evalType")){
			where += " AND evalType = :evalType ";
		}
		String end = "ORDER BY e.`cime` DESC limit :page,:pagesize ";
		List<Map<String,Object>> list =null;
		
		if(super.isCacheNull(cacheKey).equals("a")||super.isCacheNull(cacheKey).equals("c")){
			
			List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
			try {
				list = npjt.queryForList(sql+where+end,paramMap);
				for (int i = 0; i < list.size(); i++) {
					try {
						int goodsId = (int) paramMap.get("goodsId");
						if (isSameName(goodsId)) {
							Map<String,Object> map = list.get(i);
							if(!(boolean)map.get("imgNull")&&null!=map.get("imgUrl")){
								String img = (String) map.get("imgUrl");
								String imgs[]=img.split(",");
								String newimgs = "";
								for (int j = 0; j < imgs.length; j++) {
									String temp = SYSPATH + imgs[j];
									newimgs+=temp+",";
								}
								map.put("imgUrl", newimgs.substring(0, newimgs.length()-1));
							}else
								map.remove("imgUrl");
							Object old = map.get("mobile");
							if(null!=old&&
									old.toString().length()>10){
								String oldstr = old.toString();
								String mobile = oldstr.substring(0, 3)+"****"+oldstr.substring(7, 11);
								map.put("mobile", mobile);
							}else{
								map.put("mobile", "匿名");
							}
							result.add(map);
						}
					} catch (Exception e) {}
				}
			} catch (EmptyResultDataAccessException e) {}
			if(result!=null){
				if(super.isCacheNull(cacheKey).equals("a")){
					int cacheTime =new Integer(paramMap.get("cacheTime").toString());
					return super.saveAndGet(result, cacheKey, cacheTime);
				}else{
					return super.toJson(result);
				}
			}
		}else if(isCacheNull(cacheKey).equals("b")){
			return super.q(cacheKey);
		}
		return "";
	}

	private boolean isSameName(int goodsId) throws Exception{
		String sql = "SELECT `name` FROM diy_goods_info WHERE id = ?";
		String sql2 = "SELECT `name` FROM diy_goods_info2 WHERE id = ?";
		
		String name = super.jt.queryForObject(sql, new Object[]{goodsId},String.class);
		String name2 = super.jt.queryForObject(sql2, new Object[]{goodsId},String.class);
		
		if (name != null && name2!=null && name.equals(name2)) {
			return true;
		}
		return false;
	}
}
