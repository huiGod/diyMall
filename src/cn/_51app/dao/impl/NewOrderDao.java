package cn._51app.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import cn._51app.dao.BaseDao;
import cn._51app.dao.INewOrderDao;
@Repository
public class NewOrderDao extends BaseDao implements INewOrderDao{
	
	@Override
	public Map<String, Object> createOrder(String resultPath, String infoId, String textureIds, String deviceNumber, String app,
			String orderNo,String sufFormat,String num) throws Exception{
		Map<String, Object> result=new HashMap<>();
		try {
			String textureNames=queryTextureById(textureIds.split("_"));
			//查询价格 和运费
			Map<String, Object> fees=this.jt.queryForMap("SELECT `dgi`.`transportfee`,`dit`.`now_price` AS `price`,`dgi`.`user_id`,`dgi`.`goodsType`,`dgi`.`name` FROM `diy_goods_info` `dgi` INNER JOIN `diy_info_texture` `dit` ON `dgi`.`id`=`dit`.`info_id` WHERE `dit`.`info_id`=? AND `dit`.`texture_ids`=? LIMIT 1",new Object[]{infoId,textureIds});
			int index= this.jt.update("INSERT INTO `diy_orders` (`order_no`,`fee_transport`,`fee_total`,`status`,`device_no`,`app`,`info_ids`,`texture_ids`,`user_ids`,`price`,`img_url`,`file_type`,`num`,`texture_names`) "
					+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)",new Object[]{orderNo,fees.get("transportfee"),((double)fees.get("transportfee")+(double)fees.get("price")*Integer.parseInt(num)),-1,deviceNumber,app,infoId,textureIds,fees.get("user_id"),fees.get("price"),resultPath,sufFormat,num,textureNames});
			result.put("textureNames", textureNames);
			result.put("name", (String)fees.get("name"));
			result.put("orgPrice", (Double)fees.get("price"));
			if(index>0){
				return result;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public int updateShopOrderNum(String deviceNumber, String app, String flag,int num) {
		if("order".equalsIgnoreCase(flag)){
			return this.jt.update("UPDATE `diy_device_user` SET `order_num`=`order_num`+? WHERE `device_no`=? AND `app`=? AND `status`=1  LIMIT 1",new Object[]{num,deviceNumber,app});
		}else if("shop".equalsIgnoreCase(flag)){
			return this.jt.update("UPDATE `diy_device_user` SET `shop_num`=`shop_num`+? WHERE `device_no`=? AND `app`=? AND `status`=1  LIMIT 1",new Object[]{num,deviceNumber,app});
		}
		return 0;
	}
	
	@Override
	public List<Map<String, Object>> queryPrivilege() {
		List<Map<String, Object>> list=this.jt.queryForList("SELECT `about`,`org_price` AS `orgPrice`,`des_price` AS `desPrice` FROM `diy_privilege` WHERE `status`=1 ORDER BY `org_price` DESC");
		return (list==null || list.size()==0)?null:list;
	}
	
	@Override
	public String queryTextureById(String[] tempTextureId) {
		StringBuilder sql=new StringBuilder();
		String textureNames="";
		sql.append("SELECT `name` FROM `diy_goods_texture` WHERE `id`="+tempTextureId[0]);
		for (int i = 1; i < tempTextureId.length; i++) {
			sql.append(" OR `id`="+tempTextureId[i]);
		}
		List<Map<String, Object>> list=this.jt.queryForList(sql.toString());
		if(list!=null && list.size()>0){
			for (int j = 0; j < list.size(); j++) {
				textureNames+=(String)list.get(j).get("name")+",";
			}
		}
		return textureNames.substring(0,textureNames.length()-1);
	}

	@Override
	@Transactional(rollbackFor=Exception.class)
	public double activeOrder(String orderNo, String couponId, String addressId, String num, String payId,
			double totalFee, double orgPrice, double desPrice, String deviceNo, String app,Map<String, Object> addressMap) {
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
			}
			int result=this.jt.update("UPDATE `diy_orders` SET `consignee`=?,`province`=?,`area`=?,`mobile`=?,`pay_type`=?,`num`=?,`fee_total`=?,`creat_time`=NOW(),`coupon`=?,`coupon_id`=?,`org_privilege`=?,`des_privilege`=?,`status`=1 WHERE `order_no`=? AND `device_no`=? AND `app`=?",
					new Object[]{addressMap.get("name"),addressMap.get("province"),addressMap.get("area"),addressMap.get("mobile"),payId,num,(totalFee-couponMoney)<0?0:(totalFee-couponMoney),couponMoney,couponId,orgPrice,desPrice,orderNo,deviceNo,app});
			if(result>0){
				return totalFee-couponMoney;
			} 
		} catch (Exception e) {
			e.printStackTrace();
		}
		throw new RuntimeException();
	}
	
	@Override
	public int boundOrderPrepay(String orderNo, String prepayId, String payNo) {
		return this.jt.update("INSERT INTO `diy_trade` (`order_no`,`pay_no`,`prepay_id`,`time`,`type`) VALUES(?,?,?,NOW(),2)",new Object[]{orderNo,payNo,prepayId});
	}
	
	@Override
	public boolean checkOrder(String orderNo, String deviceNo, String app) {
		try {
			int status=this.jt.queryForObject("SELECT `status` FROM `diy_orders` WHERE `order_no`=? AND `device_no`=? AND `app`=? LIMIT 1",new Object[]{orderNo,deviceNo,app}, Integer.class);
			if(status==2||status==0){
				return true;
			}
		} catch (Exception e) {
		}
		return false;
	}
	
	@Override
	public Map<String, Object> queryOrderInfo(String orderNo, String deviceNo, String app) {
		Map<String, Object> map=new HashMap<>();
		try {
			map=this.jt.queryForMap("SELECT `do`.`shop_no` AS `shopNo`,`do`.`user_ids` AS `userIds`,`do`.`info_ids` AS `infoIds`,`do`.`price`,`do`.`fee_transport` AS `transportfee`,`do`.`order_no` AS `orderNo`,`do`.`province`,`do`.`area`,`do`.`mobile`,`do`.`pay_type` AS `payType`,`do`.`consignee`,`do`.`fee_total` AS `feeTotal`,`de`.`code`,DATE_FORMAT(`do`.`creat_time`,'%Y-%m-%d %H:%i:%s') AS `creatTime`,`do`.`img_url` AS `imgUrl`,`do`.`file_type` AS `fileType`,`do`.`num`,`do`.`texture_ids` AS `textureIds`,`do`.`texture_names` AS `textureNames`,`do`.`status`,`do`.`express_no` AS `expressNo`,`dgi`.`name`,`dt`.`prepay_id` AS `prepayId`,do.info_ids as goodsIds FROM `diy_orders` `do` INNER JOIN `diy_goods_info` `dgi` ON `do`.`info_ids`=`dgi`.`id` LEFT JOIN `diy_trade` `dt` ON `do`.`order_no`=`dt`.`order_no` LEFT JOIN `diy_express` `de` ON `de`.`id`=`do`.`express_id`  WHERE `do`.`device_no`=? AND `do`.`app`=? AND `do`.`order_no`=? LIMIT 1",new Object[]{deviceNo,app,orderNo});
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return map;
	}
	
	@Override
	public List<Map<String, Object>> getOrderList(String deviceNo, String app,int page,int number,Integer state) {
		String sql = "SELECT `do`.`price`,`do`.`order_no` AS `orderNo`,`do`.`pay_type` AS `payType`,`do`.`fee_total` AS `feeTotal`,`do`.`img_url` AS `imgUrl`,`do`.`file_type` AS `fileType`,`do`.`num`,`do`.`texture_names` AS `textureNames`,`do`.`status`,`do`.`express_no` AS `expressNo`,`dgi`.`name`,`de`.`code`,do.info_ids as infoIds " +
				"FROM `diy_orders` `do` INNER JOIN `diy_goods_info` `dgi` ON `do`.`info_ids`=`dgi`.`id` LEFT JOIN `diy_express` `de` ON `de`.`id`=`do`.`express_id` LEFT JOIN `diy_trade` `dt` ON `dt`.`order_no`=`do`.`order_no`  " +
				"WHERE `do`.`device_no`=? AND `do`.`app`=? AND `do`.`status`!=-1 AND `do`.`status`!=0 AND `do`.`status`!=6 ";
		String end = " ORDER BY `do`.`status` ASC ,`do`.`creat_time` DESC LIMIT ?,?";
		if(state!=null&&state!=0)
			sql += " and `do`.`status`="+state;
		List<Map<String, Object>> list=this.jt.queryForList(sql+end,new Object[]{deviceNo,app,page*number,number});
		return (list==null || list.size()==0)?null:list;
	}
	
	@Override
	public Map<String, Object> confirmOrderNumAndPay(String orderNo, String deviceNo, String app) {
		try {
			return this.jt.queryForMap("SELECT `do`.`num`,`do`.`info_ids` AS `infoIds`,`do`.`pay_type` AS `payType`,DATE_FORMAT(`do`.`creat_time`,'%Y-%m-%d %H:%i:%s') AS `creatTime`,`dt`.`prepay_id` AS `prepayId`,TIMESTAMPDIFF(MINUTE,`dt`.`time`,NOW()) AS `time`,`do`.`fee_total` AS `feeTotal` FROM `diy_orders` `do` LEFT JOIN `diy_trade` `dt` ON `do`.`order_no`=`dt`.`order_no` WHERE `do`.`order_no`=? AND `do`.`device_no`=? AND `app`=? ",new Object[]{orderNo,deviceNo,app});
		} catch (Exception e) {
			return null;
		}
	}
	
	@Override
	public boolean checkOrderNum(Integer num, String infoid) {
		try {
			int id=this.jt.queryForObject("SELECT `id` FROM `diy_goods_info` WHERE `stock`>=? AND `id`=?",new Object[]{num,infoid}, Integer.class);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	@Override
	public int boundPrepayNo(String orderNo, String prepayId, String payNo) {
		return this.jt.update("UPDATE  `diy_trade` SET `prepay_id`=?,`pay_no`=?,`time`=NOW() WHERE `order_no`=?",new Object[]{prepayId,payNo,orderNo});
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class)
	public boolean updateOrder(String orderNo, String deviceNo, String app, String flag) {
		int status=0;
		if("delete".equalsIgnoreCase(flag)){
			status=6;
		}else if("cancel".equalsIgnoreCase(flag)){
			status=5;
		}else if("confirm".equalsIgnoreCase(flag)){
			status=4;
		}else if("paidCancel".equalsIgnoreCase(flag)){
			status=9;
		}
		int result=this.jt.update("UPDATE `diy_orders` SET `status`=? WHERE `order_no`=? AND `device_no`=? AND `app`=? LIMIT 1",new Object[]{status,orderNo,deviceNo,app});
		if(result>0){
			if(status==6){
				//删除订单的时候更新订单数
				this.updateShopOrderNum(deviceNo, app, "order", -1);
			}
			if(status==6 || status==5){
				//将代金券激活还回去 (如果代金券还在有效期内)
				this.jt.update("UPDATE `diy_coupon_user` `dcu` INNER JOIN `diy_orders` `do` ON `do`.`coupon_id`=`dcu`.`id` SET `dcu`.`status`=1 WHERE `dcu`.`deviceNo`=? AND `dcu`.`app`=? AND `do`.`order_no`=? AND `dcu`.`valid`>=CURDATE() ",new Object[]{deviceNo,app,orderNo});
			}
			return true;
		}
		return false;
	}
	
	@Override
	public String queryImgUrl(String infoId) {
		try {
			return this.jt.queryForObject("SELECT `icoUrl` FROM `diy_goods_info` WHERE `id`=?",new Object[]{infoId}, String.class);
		} catch (Exception e) {
			return null;
		}
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
	public int deleteShop(String deviceNo, String app, String shopNo) {
		String sql="UPDATE `diy_shopcart` SET `status`=0,`num`=0 WHERE `device_no`=? AND `app`=? AND `status`!=0 ";
		if(!"('-1')".equals(shopNo)){
			sql+=" AND `shop_no` IN "+shopNo;
		}
		return this.jt.update(sql,new Object[]{deviceNo,app});
	}
	@Override
	public boolean recordUser(String deviceNo, String app, String deviceToken) {
		try {
			this.jt.update("INSERT INTO `diy_device_user` (`device_no`,`app`,`device_token`,`ctime`,`status`) VALUES(?,?,?,NOW(),1)",new Object[]{deviceNo,app,deviceToken});
		} catch (Exception e) {
			
		}
		return true;
	}
	
	@Override
	public Map<String, Object> queryOrderInfo(String out_trade_no) {
		Map<String, Object> map=new HashMap<>();
		try {
			map=this.jt.queryForMap("SELECT `do`.`shop_no` AS `shopNo`,`do`.`user_ids` AS `userIds`,`do`.`info_ids` AS `infoIds`,`do`.`price`,`do`.`fee_transport` AS `transportfee`,`do`.`order_no` AS `orderNo`,`do`.`province`,`do`.`area`,`do`.`mobile`,`do`.`pay_type` AS `payType`,`do`.`consignee`,`do`.`fee_total` AS `feeTotal`,DATE_FORMAT(`do`.`creat_time`,'%Y-%m-%d %H:%i:%s') AS `creatTime`,`do`.`img_url` AS `imgUrl`,`do`.`file_type` AS `fileType`,`do`.`num`,`do`.`texture_ids` AS `textureIds`,`do`.`texture_names` AS `textureNames`,`do`.`status`,`dgi`.`name`,`dt`.`prepay_id` AS `prepayId` FROM `diy_orders` `do` INNER JOIN `diy_goods_info` `dgi` ON `do`.`info_ids`=`dgi`.`id` LEFT JOIN `diy_trade` `dt` ON `do`.`order_no`=`dt`.`order_no`  WHERE `do`.`order_no`=? LIMIT 1",new Object[]{out_trade_no});
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return map;
	}
	
	@Override
	public Map<String, Object> queryOrders(String out_trade_no){
		Map<String, Object> map=new HashMap<>();
		try {
			map = this.jt.queryForMap("select * from diy_orders where order_no=?",new Object[]{out_trade_no});
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return map;
	}
	
	@Override
	public void separateOrder(Map<String, Object> paramMap){
		String sql = "INSERT INTO `diy_orders` (`order_no`, `porder_no`, `shop_no`, `info_ids`, `user_ids`, `texture_ids`, `texture_names`, `img_url`, `file_type`, `pay_type`, `consignee`, `province`, `area`, `mobile`, `sys`, `num`, `fee_transport`, `fee_total`, `price`, `coupon`, `coupon_id`, `org_privilege`, `des_privilege`, `express_id`, `express_no`, `express_start`, `express_end`, `paytime`, `creat_time`, `device_no`, `app`, `remark`, `gType`, `status`,`balance`,`userId`,`message`,`name`,`sort_name`,`userwork`,`paramType`,`param1`) " +
				"VALUES (:order_no, :porder_no, :shop_no, :info_ids, :user_ids, :texture_ids, :texture_names, :img_url, :file_type, :pay_type, :consignee, :province, :area, :mobile, :sys, :num, :fee_transport, :fee_total, :price, :coupon, :coupon_id, :org_privilege, :des_privilege, :express_id, :express_no, :express_start, :express_end, :paytime, :creat_time, :device_no, :app, :remark, :gType, :status,:balance,:userId,:message,:name,:sort_name,:userwork,:paramType,:param1)";
		this.npjt.update(sql, paramMap);
		
	}
	
	@Override
	public void update0status(String order_no){
		this.jt.update("update diy_orders set status=0 where order_no=?",new Object[]{order_no});
	}
	
	@Override
	public boolean infoUser(String deviceNo, String app, String version,String deviceToken) {
		try {
			this.jt.update("INSERT INTO `diy_device_user` (`device_no`,`app`,`device_token`,`version`) VALUES (?,?,?,?) ON DUPLICATE KEY UPDATE `version`=?,`device_token`=?",new Object[]{deviceNo,app,deviceToken,version,version,deviceToken});
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	@Override
	public Map<String, Object> getWxPay(String app) {
		try {
			return this.jt.queryForMap("SELECT `app`,`appid`,`key`,`app_id` AS `appIDS`,`mch_id` AS `mchId` FROM `wx_pay` WHERE `app`=? AND `status`=1",new Object[]{app});
		} catch (Exception e) {
			return null;
		}
	}
	
	@Override
	public Map<String, Object> getWxPayByAppId(String appid) {
		try {
			return this.jt.queryForMap("SELECT `app`,`appid`,`key`,`app_id` AS `appID`,`mch_id` AS `mchId` FROM `wx_pay` WHERE `appid`=? AND `status`=1 ORDER BY `id` DESC limit 1",new Object[]{appid});
		} catch (Exception e) {
			return null;
		}
	}
}

