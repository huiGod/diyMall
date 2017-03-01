package cn._51app.dao.diy2_0.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import cn._51app.dao.BaseDao;
import cn._51app.dao.diy2_0.ISearchGoodsDao;

@Repository
public class SearchGoodsDao extends BaseDao implements ISearchGoodsDao {

	@Override
	public List<Map<String, Object>> getKeywords(String name) {
//		String sql = "SELECT id,`name`,`title` FROM diy_keyword";
//		if (name != null && !name.equals("")) {
//			sql += " WHERE `name` LIKE '%"+ name +"%'";
//		}
//		sql += " ORDER BY sort ASC LIMIT 10";
//
//		return super.jt.queryForList(sql);
		
		String sql = "SELECT id,`name` FROM new_goods_info ngi";
		if (name != null && !name.equals("")) {
			//过滤联通特惠商品和勿选商品
			sql += " WHERE `name` LIKE '%"+ name +"%' AND `name` NOT LIKE '%勿选%' AND user_id <=5";
		}
		sql += " ORDER BY sort ASC LIMIT 10";
		
		List<Map<String, Object>> list = super.jt.queryForList(sql);
		for (Map<String, Object> map : list) {
			Integer goodsId = (Integer) map.get("id");
			String sqlKeyword = "SELECT dk.id,dk.`name` FROM diy_goods_keyword dgk LEFT JOIN diy_keyword dk ON dk.id = dgk.keyword_id WHERE dgk.goods_id = ?";
			map.put("keywords", super.jt.queryForList(sqlKeyword,goodsId));
		}
				
		return list;
	}

	@Override
	public List<Map<String, Object>> getGoodsInfo(int page,String name,Integer goods_id,Integer keyword_id) {
		String select = "select ngi.id,ngi.`name`,ngi.`isSale`,ngi.`diy25_img` AS iconUrl,ngi.`sell`,ngi.`saleTitle` FROM new_goods_info ngi";
		
		String sql = "";
		if (goods_id == null) {
			sql = select + " WHERE ngi.`name` LIKE '%"+name+"%' AND ngi.`status` =1 ORDER BY ngi.sort ASC LIMIT ?,?";
			return super.jt.queryForList(sql,new Object[]{page*10,10});
		}else {
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			
			//查找goods_id对应的商品
			sql = select + " WHERE ngi.id = ?";
			list.addAll(super.jt.queryForList(sql,new Object[]{goods_id}));
			
			if (keyword_id == null) {
				//查找与 name 相关的商品(不包含goods_id的商品)
				sql = select + " WHERE ngi.`name` like '%"+ name +"%' AND ngi.id != ? AND ngi.`status` =1 ORDER BY ngi.sort ASC LIMIT ?,?";
				System.out.println("keyword_id为空"+sql);
				list.addAll(super.jt.queryForList(sql,new Object[]{goods_id,page*10,10}));
			} else {
				//查找关键字为keyword_id的商品（不包含goods_id)	
				sql = select + " LEFT JOIN diy_goods_keyword dgk ON dgk.goods_id = ngi.id WHERE dgk.goods_id != ? AND dgk.goods_type = 1 AND dgk.keyword_id=? ORDER BY ngi.sort ASC LIMIT ?,?";
				System.out.println("keyword_id非空"+sql);
				list.addAll(super.jt.queryForList(sql,new Object[]{goods_id,keyword_id,page*10,10}));
			}
			return list;
		}
	}

	@Override
	public List<Map<String, Object>> getSearchLog(String userId) {
		String sql = "SELECT content as name FROM diy_search_logs WHERE userId=? ORDER BY ctime DESC"; 
		return super.jt.queryForList(sql,userId);
	}

	@Override
	public void addSearchLog(String userId, String content) {
		String sql = "SELECT id FROM diy_search_logs WHERE userId=? AND content=? LIMIT 0,1";
		try {
			//找打该记录，就更改该记录的时间
			int id = super.jt.queryForObject(sql, new Object[]{userId,content},Integer.class);
			String edit = "UPDATE diy_search_logs SET ctime=NOW() WHERE id=?";
			super.jt.update(edit,id);
		} catch (Exception e) {//没有找到id则会出现异常，插入心的记录
			String insert = "INSERT INTO diy_search_logs(userId,content,ctime) VALUES(?,?,NOW())";
			super.jt.update(insert,userId,content);
		}
	}

	@Override
	public int clearLogs(String userId) {
		String sql = "DELETE FROM diy_search_logs WHERE userId = ?";
		return super.jt.update(sql,userId);
	}

	@Override
	public List<Map<String, Object>> getRecommand() {
		String sql = "SELECT id,name FROM diy_keyword WHERE isRecommand = 1 ORDER BY sort ASC";
		return super.jt.queryForList(sql);
	}

	@Override
	public List<Map<String, Object>> getGoodsInfo(Map<String, Object> paramMap) {
		String select = "select ngi.id,ngi.`name`,ngi.`type`,ngi.`isSale`,ngi.`diy25_img` AS iconUrl,ngi.`sell`,ngi.`saleTitle` FROM new_goods_info ngi";
		
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		String sql = "";
		if (paramMap.get("goodsName") == null || paramMap.get("goodsName").toString().equals("")) {
			sql = select + " WHERE ngi.`name` LIKE CONCAT('%',:searchName,'%') AND ngi.`name` NOT LIKE '%勿选%' AND ngi.user_id <=5 AND ngi.`status` =1 ORDER BY ngi.sort,ngi.id ASC LIMIT :index,:rows";
			list.addAll(super.npjt.queryForList(sql,paramMap));
			if (list == null || list.size() == 0) {
				sql = select + " LEFT JOIN diy_goods_keyword dgk ON dgk.goods_id = ngi.id LEFT JOIN diy_keyword dk ON dk.id = dgk.keyword_id WHERE dk.`name` = :searchName ORDER BY ngi.sort ASC,ngi.id ASC LIMIT :index,:rows";
				list.addAll(super.npjt.queryForList(sql,paramMap));
			}
		}else {			
			//查找goodsName对应的商品
			if (paramMap.get("index") == null || (Integer)paramMap.get("index") == 0) {//第一页查询时才有该商品信息
				sql = select + " WHERE ngi.`name` = :goodsName AND ngi.`name` NOT LIKE '%勿选%' AND ngi.user_id <=5 AND ngi.`status` =1";
				list.addAll(super.npjt.queryForList(sql,paramMap));
			}
			
			
			if (paramMap.get("keywordName") == null || paramMap.get("keywordName").toString().equals("")) {
				//查找与 searchName 相关的商品(不包含goodsName的商品)
				sql = select + " WHERE ngi.`name` LIKE CONCAT('%',:searchName,'%') AND ngi.`name` NOT LIKE '%勿选%' AND ngi.user_id <=5 AND ngi.`name` != :goodsName AND ngi.`status` =1 ORDER BY ngi.sort ASC,ngi.id ASC LIMIT :index,:rows";
				list.addAll(super.npjt.queryForList(sql,paramMap)); 
			} else {
				//查找关键字为keywordName的商品（不包含goodsName)	
				sql = select + " LEFT JOIN diy_goods_keyword dgk ON dgk.goods_id = ngi.id  LEFT JOIN diy_keyword dk ON dk.id = dgk.keyword_id WHERE dk.`name` = :keywordName AND ngi.`name` != :goodsName AND ngi.`name` NOT LIKE '%勿选%' AND ngi.user_id <=5 AND ngi.`status`=1 ORDER BY ngi.sort ASC,ngi.id ASC LIMIT :index,:rows";
				list.addAll(super.npjt.queryForList(sql,paramMap));
			}
		}
		
		if (list != null && list.size()>0) {
			return list;
		}
		return null;
	}

}
