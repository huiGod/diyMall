package cn._51app.dao.impl;
import java.util.Map;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import cn._51app.dao.BaseDao;
import cn._51app.dao.IGoodsOrderDao;

@Repository
public class GoodsOrderDao extends BaseDao implements IGoodsOrderDao{

	@Override
	public int insert(Map<String, Object> paramMap) throws Exception {
		String insert ="INSERT INTO diy_order_goods(goods_id,gType,price,store_id,order_no,num,param,img_url,file_type,deviceNo,app,sys,ctime)",
				value=" VALUES(:gId,:gType,:price,:storeId,:orderNo,:num,:param,:imgUrl,:fileType,:deviceNo,:app,:sys,now())";
		String sql =insert+value;
		SqlParameterSource ps=new MapSqlParameterSource(paramMap); 
		 KeyHolder key=new GeneratedKeyHolder();
		 int result =npjt.update(sql,ps,key);
		 if(result!=0){
			 return key.getKey().intValue();
		 }else{
			 return 0;
		 }
	}

	@Override
	public String getGOMap(Map<String, Object> paramMap) throws Exception {
		String select ="SELECT dog.id AS oid,dog.order_no AS orderNo,dgi.id AS gid,dgi.name,dog.price AS nowPrice,dog.num,dog.param,dog.img_url AS pimgUrl,dog.file_type",
				from=" FROM diy_order_goods dog INNER JOIN diy_goods_info dgi ON dog.goods_id=dgi.id",
				where=" WHERE dog.id=:id";//AND dog.deviceNo=:deviceNo
		String http="http://120.26.112.213:8083/file/img/";
		
		String sql =select+from+where;
		Map<String,Object> m =null;
		try {
			m =super.npjt.queryForMap(sql, paramMap);
			String pimgUrl =m.get("pimgUrl").toString();
			String ft =m.get("file_type").toString();
			
			m.put("pimgUrl", http+pimgUrl+"@p"+ft);
			//订单号
			//String orderNo =((((imgUrl.replace(".", "#")).split("#"))[0]).split("/")[1]);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
		return super.toJson(m);
	}

}
