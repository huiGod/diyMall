package cn._51app.dao.diy2_0.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;





import com.thoughtworks.xstream.mapper.Mapper.Null;

import cn._51app.dao.BaseDao;
import cn._51app.dao.diy2_0.IEvaluationDao2;
import cn._51app.entity.EvaluationInfo;
import cn._51app.util.PropertiesUtil;

@Repository
public class EvaluationDao2 extends BaseDao implements IEvaluationDao2 {
	
	private final String SYSPATH =PropertiesUtil.getValue("diy.goods.url");
	
	
	/**
	 * @author zhanglz
	 * @return 保存评论
	 */
	@Override
	public Integer saveEvaluation(EvaluationInfo info,String mobile,String texture) throws Exception{

		String sql = "insert into diy_evaluation(goodsId,evalType,content,cime,imgUrl,imgNull,deviceNo,mobile,texture,state,goodsType) values (?,?,?,NOW(),?,?,?,?,?,0,?)";
		return jt.update(sql, new Object[]{info.getGoodsId(),info.getEvalType(),info.getContent(),info.getImgUrl(),info.getImgNull(),info.getDeviceNo(),mobile,texture,info.getGoodsType()});
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
	 * @author yuanqi
	 * @return 查询评论类型的数量
	 */
	@Override
	public String getEvalTypeNum(Map<String,Object> paramMap) throws Exception{
		String cacheKey =paramMap.get("cacheKey").toString();
		del(cacheKey);
		List<Map<String,Object>> m =null;
		if(super.isCacheNull(cacheKey).equals("a")||super.isCacheNull(cacheKey).equals("c")){
			//默认非定制商品
			String select = "SELECT e.evalType,COUNT(evalType) AS number FROM diy_evaluation e";
			String leftJoin = " LEFT JOIN diy_goods_info2 i on e.`goodsId`=i.`id`";
			String where = " WHERE e.`goodsId`=:goodsId AND e.`state`=1 AND e.goodsType = 0";
			String groupBy = " GROUP BY e.evalType;";
		
			int goodsType = 0;	
			if (paramMap.get("goodsType") != null && (paramMap.get("goodsType").toString()).equals("1")) {
				//定制商品
				leftJoin = " LEFT JOIN new_goods_info ng ON e.`goodsId`=ng.`id`";
				where = " WHERE e.`goodsId`=:goodsId AND e.`state`=1 AND e.goodsType = 1";
				goodsType =1;
			}
			
			String sql = select + leftJoin + where + groupBy;
			int goodsId=(Integer)paramMap.get("goodsId");
			List<Map<String,Object>> list2 =null;
			if(goodsType==0){
				List<Map<String, Object>> listparam=this.jt.queryForList("SELECT `newgoodid` FROM `diy_goods_info2` WHERE `id`=?",new Object[]{goodsId});
				if(listparam!=null && !listparam.isEmpty()){
					Integer newgoodid=(Integer)listparam.get(0).get("newgoodid");
					if(newgoodid!=null){
						list2=new ArrayList<>();
						String sql2="SELECT e.evalType,COUNT(evalType) AS number FROM diy_evaluation e LEFT JOIN new_goods_info ng ON e.`goodsId`=ng.`id` WHERE e.`goodsId`=? AND e.`state`=1 AND e.goodsType = 1";
						list2=this.jt.queryForList(sql2,new Object[]{newgoodid});
					}
				}
			}
			try {
				m=npjt.queryForList(sql,paramMap);
				if(list2!=null && !list2.isEmpty()){
					m.addAll(list2);
				}
				Integer sum = 0;
				for (int i = 0; i < m.size(); i++) {
					Map<String,Object> map = m.get(i);
					sum += null==map.get("number") ? 0 : Integer.valueOf(map.get("number").toString());
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
				newm2.put("number", this.getEvalHavePic2((int)paramMap.get("goodsId"),goodsType));
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
		String sql = "SELECT imgUrl FROM diy_evaluation WHERE imgNull=0 AND `state`=1 AND goodsId="+goodsId;
		List<Map<String,Object>> list = jt.queryForList(sql);
		for (Map<String, Object> map : list) {
			String imgUrl = null==map.get("imgUrl")?"":map.get("imgUrl").toString();
			String[] imgarr = imgUrl.split(",");
			res +=imgarr.length;
		}
		return res;
	}
	

	/**
	 * 查询图片个数
	 * @param goodsId
	 * @return
	 */
	public int getEvalHavePic2(int goodsId,int goodsType){
		int res = 0;
		String sql = "SELECT imgUrl FROM diy_evaluation WHERE imgNull=0 AND `state`=1 AND goodsId="+goodsId+" AND goodsType="+goodsType;
		List<Map<String,Object>> list = jt.queryForList(sql);
		for (Map<String, Object> map : list) {
			String imgUrl = null==map.get("imgUrl")?"":map.get("imgUrl").toString();
			String[] imgarr = imgUrl.split(",");
			res +=imgarr.length;
		}
		return res;
	}
	
	/**
	 * @author zhanglz
	 * @return 查询评论的图片
	 */
	@Override
	public String findEvalPic(Map<String,Object> paramMap) throws Exception{
		String cacheKey =paramMap.get("cacheKey").toString();
		
		
		List<Map<String,Object>> list =null;
		List<Map<String,Object>> result = null;
		if(super.isCacheNull(cacheKey).equals("a")||super.isCacheNull(cacheKey).equals("c")){
			
			try {			
				if (paramMap.get("goodsType") == null || paramMap.get("goodsType").equals("")) {
					String sql = "SELECT imgUrl FROM diy_evaluation WHERE goodsId=? and `state`=1 AND imgNull=0 ORDER BY cime desc LIMIT ?,?";
					list=jt.queryForList(sql,new Object[]{paramMap.get("goodsId"),paramMap.get("page"),paramMap.get("pagesize")});
				}else{
					String sql = "SELECT imgUrl FROM diy_evaluation WHERE goodsId=? and goodsType= ? and `state`=1 AND imgNull=0 ORDER BY cime desc LIMIT ?,?";
					list=jt.queryForList(sql,new Object[]{paramMap.get("goodsId"),paramMap.get("goodsType"),paramMap.get("page"),paramMap.get("pagesize")});
				}
				
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
		String select = "SELECT e.id as id,e.`mobile` AS mobile, e.evalType as evalType, " +
				"e.`content` as content ,e.`cime` AS creatTime,e.`imgUrl` as imgUrl,e.imgNull as imgNull,"
				+ "e.userId as userId FROM diy_evaluation e";
		
		String leftJoin = " LEFT JOIN diy_goods_info2 i ON e.`goodsId`=i.`id`";
		if (paramMap.get("goodsType") != null && (paramMap.get("goodsType").toString()).equals("1")) {
			leftJoin = " LEFT JOIN new_goods_info ng ON e.`goodsId`=ng.`id`";
		}
		
		String where = " WHERE e.`goodsId`=:goodsId   and e.`state`=1 and e.goodsType=:goodsType ";
		
		
		
		//如果评论类型不为空或不为0，则查找相关类型的评论
		if(null!=paramMap.get("evalType")&&0!=(int)paramMap.get("evalType")){
			where += " and evalType = :evalType ";
		}
		//如果商品定制类型不为空，则查找类型的评论
		if (null!=paramMap.get("goodsType")) {
			where += " and e.goodsType = :goodsType ";
		}
		String end = "ORDER BY e.`isTop` DESC , e.`cime` DESC limit :page,:pagesize ";
		
		String sql = select + leftJoin + where +end;
		/**
		 * 刻字商品关联 定制非定制 评论
		 */
		int goodsId=(Integer)paramMap.get("goodsId");
		int goodsType=(Integer)paramMap.get("goodsType");
		int evalType=(Integer)paramMap.get("evalType");
		List<Map<String,Object>> list2 =null;
		if(goodsType==0){
			List<Map<String, Object>> listparam=this.jt.queryForList("SELECT `newgoodid` FROM `diy_goods_info2` WHERE `id`=?",new Object[]{goodsId});
			if(listparam!=null && !listparam.isEmpty()){
				Integer newgoodid=(Integer)listparam.get(0).get("newgoodid");
				if(newgoodid!=null){
					list2=new ArrayList<>();
					String sql2="SELECT e.id as id,e.`mobile` AS mobile, e.evalType as evalType, " +
							"e.`content` as content ,e.`cime` AS creatTime,e.`imgUrl` as imgUrl,e.imgNull as imgNull,"
							+ "e.userId as userId FROM diy_evaluation e LEFT JOIN new_goods_info ng ON e.`goodsId`=ng.`id`"
							+"WHERE e.`goodsId`=?  and e.`state`=1 and e.goodsType=1 ";
					if(evalType!=0){
						sql2+=" AND e.`evalType`="+evalType+" ";
					}
					sql2+=" ORDER BY e.`cime` DESC limit ?,?";
					list2=this.jt.queryForList(sql2,new Object[]{newgoodid,paramMap.get("page"),paramMap.get("pagesize")});
				}
			}
		}
		List<Map<String,Object>> list =null;
		
		if(super.isCacheNull(cacheKey).equals("a")||super.isCacheNull(cacheKey).equals("c")){
			//缓存中没有获取到数据
			List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
			try {
				list = npjt.queryForList(sql,paramMap);
				if(list2!=null && !list2.isEmpty()){
					list.addAll(list2);
				}
				for (int i = 0; i < list.size(); i++) {
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

}
