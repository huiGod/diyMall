package cn._51app.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import cn._51app.dao.BaseDao;
import cn._51app.dao.IUUMallDao;
import cn._51app.entity.EvaluationInfo;


@Repository
public class UUMallDao extends BaseDao implements IUUMallDao{
	
	@Override
	public Integer haveOpenid(String openid){
		try {
			String sql = "SELECT 1 FROM `diy_device_user` WHERE openid = ? LIMIT 1";
			return jt.queryForObject(sql, new Object[]{openid},Integer.class);
		} catch (DataAccessException e) {
			return 0;
		}
	}
	
	@Override
	public Integer haveMobile(String mobile){
		try {
			String sql = "SELECT 1 FROM `diy_device_user` WHERE mobile = ? LIMIT 1";
			return jt.queryForObject(sql, new Object[]{mobile},Integer.class);
		} catch (DataAccessException e) {
			return 0;
		}
	}
	
	@Override
	public int insertOpenid(Map<String, Object> paramMap){
		String sql = "INSERT INTO `diy_device_user` (device_no,app,ctime,openid,`name`,head_url,mobile) VALUES (:device_no,:app,NOW(),:openid,:name,:head_url,:mobile)";
		return npjt.update(sql, paramMap);
	}
	
	@Override
	public Map<String, Object> findUser4Openid(String openid){
		String sql = "SELECT id,device_no,app,`name`,mobile,openid,head_url,shop_num,order_num FROM `diy_device_user` WHERE openid = ?";
		return jt.queryForMap(sql,new Object[]{openid});
	}
	
	@Override
	public Map<String, Object> findUser4Mobile(String mobile){
		try {
			String sql = "SELECT id,`name`,device_no,app,mobile,openid,head_url,shop_num,order_num FROM `diy_device_user` WHERE mobile = ?";
			return jt.queryForMap(sql,new Object[]{mobile});
		} catch (DataAccessException e) {
			return null;
		}
	}
	
	@Override
	public int binding(String mobile,int id){
		String sql = "UPDATE `diy_device_user` SET mobile=? WHERE id=?";
		return jt.update(sql,new Object[]{});
	}
	
	@Override
	public String queryPreUrl(String infoId,String textureIds){
		try {
			return this.jt.queryForObject("SELECT `pre_url` FROM `diy_info_texture` WHERE `info_id`=? AND `texture_ids`=?",new Object[]{infoId,textureIds}, String.class);
		} catch (Exception e) {
			return null;
		}
	}
	
	@Override
	public int getShopNum(String deviceNo, String app){
		String sql = "SELECT COUNT(*) FROM `diy_shopcart` WHERE device_no=? AND app=? AND `status`=1";
		return jt.queryForObject(sql, Integer.class, new Object[]{deviceNo,app});
		
	}
	
	@Override
	public String youNav(Map<String, Object> paramMap) throws Exception {
		String cacheKey=paramMap.get("cacheKey").toString();
		int cacheTime=new Integer(paramMap.get("cacheTime").toString());
		String sql="SELECT name,id,special_id FROM diy_home_nav WHERE status=1 AND type=1 ORDER BY sort ASC";
		if(super.isCacheNull(cacheKey).equals("a") || super.isCacheNull(cacheKey).equals("c")){
			List<Map<String,Object>>list=super.jt.queryForList(sql);
			
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

	@Override
	public String youSpecial(Map<String, Object> paramMap) throws Exception {
		String cacheKey=paramMap.get("cacheKey").toString();
		String dgurl=paramMap.get("dgurl").toString();
		int cacheTime=new Integer(paramMap.get("cacheTime").toString());
		String sql="SELECT ds.name,ds.img_url,ds.text,ds.pid,ds.type,dhn.name AS navName FROM diy_special ds LEFT JOIN diy_home_nav dhn ON dhn.id=ds.nav_id WHERE ds.nav_id=? AND ds.status=1 ORDER BY ds.sort ASC";
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
	
	@Override
	public int homeOrderNum(String deviceNo, String app){
		String sql = "SELECT COUNT(*) FROM `diy_orders` WHERE device_no=? AND app=? AND `status`=1";
		return jt.queryForObject(sql, Integer.class, new Object[]{deviceNo,app});
	}
	
	@Override
	public Map<String, Object> getOrder4No(String order_no){
		String sql = "SELECT fee_total,info_ids FROM `diy_orders` WHERE order_no=? ";
		return jt.queryForMap(sql,new Object[]{order_no});
	}
	
	@Override
	public List<Map<String, Object>> queryShopList(String deviceNo, String app) {
		List<Map<String, Object>> list=this.jt.queryForList("SELECT `ds`.`shop_no` AS `shopNo`,`ds`.`texture_name` AS `textureName`,`ds`.`img_url` AS `imgUrl`,`ds`.`file_type` AS `fileType`,`ds`.`num`,`dgi`.`name`,`dgi`.`transportfee`,`dit`.`now_price` AS `nowPrice`,dgi.`isBoutique` FROM `diy_shopcart` `ds` INNER JOIN `diy_goods_info` `dgi` ON `ds`.`info_id`=`dgi`.`id` INNER JOIN `diy_info_texture` `dit` ON `dit`.`texture_ids`=`ds`.`texture_ids` WHERE `dit`.`info_id`=`ds`.`info_id` AND `ds`.`device_no`=? AND `ds`.`app`=? AND `ds`.`status`=1 ORDER BY `ds`.`creat_time` DESC",new Object[]{deviceNo,app});
		return list==null||list.size()==0?null:list;
	}
	
	@Override
	public Map<String, Object> getOrderComm(String order_no){
		String sql = "SELECT info_ids,img_url,file_type FROM `diy_orders` WHERE order_no=? AND `status`=4";
		return jt.queryForMap(sql,new Object[]{order_no});
	}
	
	@Override
	public List<Map<String, Object>> shopBuy(String shopNos,String deviceNo,String app){
		String sql = "SELECT s.shop_no,g.`name`,s.`texture_name`,s.`num`,s.`img_url`,s.`file_type`,t.`now_price`,g.`isBoutique` " +
				"FROM `diy_shopcart` s ,`diy_goods_info` g ,`diy_info_texture` t " +
				"WHERE s.`info_id`=g.`id` AND s.`texture_ids`=t.`texture_ids` AND s.`device_no`=? AND s.`app`=? AND s.info_id=t.`info_id` AND s.`shop_no` in "+shopNos ;
		return jt.queryForList(sql,new Object[]{deviceNo,app});
	}
	
	@Override
	public Integer saveComment(String goodsId,Integer evalType,String content,String deviceNo,String mobile,String texture) throws Exception{
		String sql = "insert into diy_evaluation(goodsId,evalType,content,cime,imgUrl,imgNull,deviceNo,mobile,texture,state) values (?,?,?,NOW(),?,1,?,?,?,0)";
		return jt.update(sql, new Object[]{goodsId,evalType,content,"",deviceNo,mobile,texture});
	}

	@Override
	public String youStrategy(Map<String, Object> paramMap) throws Exception {
		if(paramMap.get("specialId")==null || paramMap.get("specialId").toString().equals("")){
			return null;
		}
		
		String cacheKey=paramMap.get("cacheKey").toString();
		String dgurl=paramMap.get("dgurl").toString();
		int cacheTime=new Integer(paramMap.get("cacheTime").toString());
		String sql="SELECT name,img_url,text,pid,type FROM diy_special WHERE id=? AND status=1 ORDER BY sort ASC";
		if(super.isCacheNull(cacheKey).equals("a") || super.isCacheNull(cacheKey).equals("c")){
			List<Map<String,Object>>list=new ArrayList<Map<String,Object>>();
			
			//如果type=2,ids以逗号隔开为专题id
			String specialIds[]=paramMap.get("specialId").toString().split(",");
			//循环查询ids
			for(int i=0;i<specialIds.length;i++){
				if(super.jt.queryForList(sql,specialIds[i])==null){
					break;
				}
				Map<String,Object>map=super.jt.queryForList(sql,specialIds[i]).get(0);
				map.put("img_url",dgurl+map.get("img_url"));
				list.add(map);
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
	
	@Override
	public List<Map<String, Object>> queryAddress(String deviceNo, String app) {
		List<Map<String, Object>> list= this.jt.queryForList("SELECT `id`,`name`,`mobile`,`province`,`area` FROM `diy_user_address` WHERE `status`=1 AND `device_no`=? AND `app`=? ORDER BY `is_default` DESC,`ctime` DESC",new Object[]{deviceNo,app});
		return (list==null || list.size()==0)?null:list;
	}
	
	@Override
	public String getOpenId(String deviceNo, String app){
		String sql = "SELECT openid FROM `diy_device_user` WHERE device_no=? AND app=?";
		return jt.queryForObject(sql, String.class,new Object[]{deviceNo,app});
	}
	
	@Override
	public List<Map<String, Object>> getOrderList(String deviceNo, String app,int page,int number,Integer state) {
		String sql = "SELECT `do`.`price`,`do`.`order_no` AS `orderNo`,`do`.`pay_type` AS `payType`,`do`.`fee_total` AS `feeTotal`,`do`.`img_url` AS `imgUrl`,`do`.`file_type` AS `fileType`,`do`.`num`,`do`.`texture_names` AS `textureNames`,`do`.`status`,`do`.`express_no` AS `expressNo`,`dgi`.`name`,`de`.`code`,do.info_ids as infoIds " +
				"FROM `diy_orders` `do` INNER JOIN `diy_goods_info` `dgi` ON `do`.`info_ids`=`dgi`.`id` LEFT JOIN `diy_express` `de` ON `de`.`id`=`do`.`express_id` LEFT JOIN `diy_trade` `dt` ON `dt`.`order_no`=`do`.`order_no`  " +
				"WHERE `do`.`device_no`=? AND `do`.`app`=? AND `do`.`status`!=-1 AND `do`.`status`!=0 AND `do`.`status`!=6 ";
		String end = " ORDER BY `do`.`status` ASC ,`do`.`creat_time` DESC LIMIT ?,?";
		if(state==2)
			sql += " and `do`.`status` in (2,8) ";
		else if(state!=0)
			sql += " and `do`.`status`="+state;
		List<Map<String, Object>> list=this.jt.queryForList(sql+end,new Object[]{deviceNo,app,page*number,number});
		return (list==null || list.size()==0)?null:list;
	}
	
	@Override
	public List<Map<String, Object>> getShopInfoByShopNos(String shopNo) {
		List<Map<String, Object>> list=this.jt.queryForList("SELECT `ds`.`shop_no` AS `shopNo`,`ds`.`info_id` AS `infoId`,`ds`.`texture_ids` AS `textureIds`,`ds`.`texture_name` AS `textureName`,`ds`.`user_id` AS `userId`,`ds`.`img_url` AS `imgUrl`,`ds`.`file_type` AS `fileType`,`ds`.`num`,`dgi`.`name`,`dgi`.`transportfee`,`dit`.`now_price` AS `nowPrice`,dgi.`isBoutique` FROM `diy_shopcart` `ds` INNER JOIN `diy_goods_info` `dgi` ON `dgi`.`id`=`ds`.`info_id` INNER JOIN `diy_info_texture` `dit` ON `dit`.`info_id`=`ds`.`info_id` WHERE `dit`.`texture_ids`=`ds`.`texture_ids` AND `ds`.`status`=1 AND `ds`.`shop_no` IN "+shopNo+"");
		return (list==null||list.size()<0)?null:list;
	}
	
	@Override
	public Integer isBoutique(String id){
		return jt.queryForObject("SELECT isBoutique FROM `diy_goods_info` WHERE id="+id, Integer.class);
	}
	
	@Override
	public Integer isShopRepeat(String infoId,String textureIds, String deviceNo, String app){
		try {
			String sql = "SELECT num FROM `diy_shopcart` WHERE info_id=? AND texture_ids=? AND device_no=? AND app=? AND `status`=1";
			return jt.queryForObject(sql, Integer.class,new Object[]{infoId,textureIds,deviceNo,app});
		} catch (DataAccessException e) {
			return 0;
		}
	}
	
	@Override
	public Integer mergeShop(String infoId,String textureIds, String deviceNo, String app,int num){
		try {
			String sql = "UPDATE `diy_shopcart` SET num=num+? WHERE info_id=? AND texture_ids=? AND device_no=? AND app=? AND `status`=1";
			return jt.update(sql, new Object[]{num,infoId,textureIds,deviceNo,app});
		} catch (DataAccessException e) {
			return 0;
		}
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class)
	public double createOrderByShops(String orderNo, String shopNo, String infoId, String textureIds, String textureName,
			String userId, String imgUrl, String fileType, String num, String name, String nowPrice,String payId,double orgPrice,
			double desPrice,String couponId,double totalFee,Map<String, Object> addressMap,String deviceNo,String app,String transportFee,String remark) {
		System.out.println("remark="+remark);
		try {
			double couponMoney=0.00;
			if(!StringUtils.isBlank(couponId)){
				//将代金券失效  绑定到订单
				int result1=this.jt.update("UPDATE `diy_coupon_user` `dcu` INNER JOIN `diy_coupon` `dc` ON `dcu`.`coupon_id`=`dc`.`id` SET `dcu`.`status`=0 WHERE `dcu`.`id`=? AND `dcu`.`deviceNo`=? AND `dcu`.`app`=?",new Object[]{couponId,deviceNo,app});
				if(result1>0){
					//代金券钱查出来
					couponMoney=this.jt.queryForObject("SELECT `dc`.`des_price` FROM `diy_coupon` `dc` INNER JOIN `diy_coupon_user` `dcu` ON `dc`.`id`=`dcu`.`coupon_id`  WHERE `dcu`.`id`=?",new Object[]{couponId}, Double.class);
				}else{
					couponId=null;
				}
			}else{
				couponId=null;
			}
			int results= this.jt.update("INSERT INTO `diy_orders` (`order_no`,`shop_no`,`info_ids`,`user_ids`,`texture_ids`,`texture_names`,`img_url`,`file_type`,`pay_type`,`consignee`,`province`,`area`,`mobile`,`num`,`fee_transport`,`fee_total`,`price`,`coupon`,`coupon_id`,`des_privilege`,`org_privilege`,`creat_time`,`device_no`,`app`,`status`,`remark`) "
					+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,NOW(),?,?,1,?)",new Object[]{orderNo,shopNo,infoId,userId,textureIds,textureName,imgUrl,fileType,payId,addressMap.get("name"),addressMap.get("province"),addressMap.get("area"),addressMap.get("mobile"),num,transportFee,(totalFee-couponMoney)<0?0:(totalFee-couponMoney),nowPrice,couponMoney,couponId,desPrice,orgPrice,deviceNo,app,remark});
			if(results>0){
				return totalFee-couponMoney;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		throw new RuntimeException();
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class)
	public double activeOrder(String orderNo, String couponId, String addressId, String num, String payId,
			double totalFee, double orgPrice, double desPrice, String deviceNo, String app,Map<String, Object> addressMap,String remark) {
		try {
			double couponMoney=0.00;
			if(!StringUtils.isBlank(couponId)){
				//将代金券失效  绑定到订单
				int result1=this.jt.update("UPDATE `diy_coupon_user` `dcu` INNER JOIN `diy_coupon` `dc` ON `dcu`.`coupon_id`=`dc`.`id` SET `dcu`.`status`=0 WHERE `dcu`.`id`=? AND `dcu`.`deviceNo`=? AND `dcu`.`app`=?",new Object[]{couponId,deviceNo,app});
				if(result1>0){
					//代金券钱查出来
					couponMoney=this.jt.queryForObject("SELECT `dc`.`des_price` FROM `diy_coupon` `dc` INNER JOIN `diy_coupon_user` `dcu` ON `dc`.`id`=`dcu`.`coupon_id`  WHERE `dcu`.`id`=?",new Object[]{couponId}, Double.class);
				}else{
					couponId=null;
				}
			}else{
				couponId=null;
			}
			int result=this.jt.update("UPDATE `diy_orders` SET `consignee`=?,`province`=?,`area`=?,`mobile`=?,`pay_type`=?,`num`=?,`fee_total`=?,`creat_time`=NOW(),`coupon`=?,`coupon_id`=?,`org_privilege`=?,`des_privilege`=?,`status`=1,`remark`=?  WHERE `order_no`=? AND `device_no`=? AND `app`=?",
					new Object[]{addressMap.get("name"),addressMap.get("province"),addressMap.get("area"),addressMap.get("mobile"),payId,num,(totalFee-couponMoney)<0?0:(totalFee-couponMoney),couponMoney,couponId,orgPrice,desPrice,remark,orderNo,deviceNo,app});
			if(result>0){
				return totalFee-couponMoney;
			} 
		} catch (Exception e) {
			e.printStackTrace();
		}
		throw new RuntimeException();
	}
}
