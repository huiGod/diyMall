package cn._51app.dao.diy2_0.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import cn._51app.dao.BaseDao;
import cn._51app.dao.diy2_0.IShopCartDao;
import cn._51app.dao.diy2_0.IndentDao;
import cn._51app.entity.User;
import cn._51app.util.CommonUtil;

@Repository
public class ShopCartDao extends BaseDao implements IShopCartDao{
	@Autowired
	private IndentDao indentDao;
	@Override
	public List<Map<String, Object>> queryShopList(String deviceNo, String app, int page, int number) {
		List<Map<String, Object>> list=this.jt.queryForList("SELECT `ds`.`shop_no` AS `shopNo`,`ds`.`texture_name` AS `textureName`,`ds`.`img_url` AS `imgUrl`,`ds`.`file_type` AS `fileType`,`ds`.`num`,`ngi`.`name`,`ngi`.`transportfee`,`ngi`.`now_price` AS `nowPrice`,`ds`.`creat_time`,`ngi`.`user_id` AS `userId`,`dsu`.`name` AS `companyName` FROM `diy_shopcart` `ds` INNER JOIN `new_goods_info` `ngi` ON `ds`.`info_id`=`ngi`.`id` INNER JOIN `diy_info_texture2` `dit` ON `dit`.`texture_ids`=`ds`.`texture_ids` INNER JOIN `diy_sys_user` `dsu` ON `dsu`.`id`=`ngi`.`user_id` WHERE `dit`.`make_id`=`ds`.`info_id` AND `ds`.`device_no`=? AND `ds`.`app`=? AND `ds`.`status`=1 AND `ds`.`file_type`!='xxx' AND `dit`.`make_id`=`ds`.`info_id` UNION ALL SELECT `ds`.`shop_no` AS `shopNo`,`ds`.`texture_name` AS `textureName`,`ds`.`img_url` AS `imgUrl`,`ds`.`file_type` AS `fileType`,`ds`.`num`,`dgi`.`name`,`dgi`.`transportfee`,`dit`.`now_price` AS `nowPrice`,`ds`.`creat_time`,`dgi`.`user_id` AS `userId`,`dsu`.`name` AS `companyName` FROM `diy_shopcart` `ds` INNER JOIN `diy_goods_info2` `dgi` ON `ds`.`info_id`=`dgi`.`id` INNER JOIN `diy_info_texture2` `dit` ON `dit`.`texture_ids`=`ds`.`texture_ids` INNER JOIN `diy_sys_user` `dsu` ON `dsu`.`id`=`dgi`.`user_id` WHERE `dit`.`info_id`=`ds`.`info_id` AND `ds`.`device_no`=? AND `ds`.`app`=? AND `ds`.`status`=1 AND `ds`.`file_type`='xxx'  ORDER BY `userId` ASC,`creat_time` DESC LIMIT ?,?",new Object[]{deviceNo,app,deviceNo,app,page*number,number});
		return list==null||list.size()==0?null:list;
	}
	
	@Override
	public String queryPreUrl(String infoId, String textureIds) {
		try {
			return this.jt.queryForObject("SELECT `pre_url` FROM `diy_info_texture2` WHERE `make_id`=? AND `texture_ids`=?",new Object[]{infoId,textureIds}, String.class);
		} catch (Exception e) {
			return null;
		}
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class)
	public int createShop(String resultPath, String infoId, String textureIds, String deviceNo, String app,
			String shopNo, String sufFormat, String num,int workId,String lettering,String modId) {
		String table="`new_goods_info`";
		//区分info_id
		boolean flag=true;
		if("xxx".equals(sufFormat)){
			table="`diy_goods_info2`";
			flag=false;
		}
		String sortName=this.getSortNameById(infoId,flag);
		String textureName=indentDao.queryTextureById(textureIds.split("_"));
		this.jt.update("INSERT INTO `diy_shopcart` (`shop_no`,`info_id`,`texture_ids`,`texture_name`,`user_id`,`img_url`,`file_type`,`num`,`creat_time`,`status`,`device_no`,`app`,`sort_name`,`userwork`,`param1`,`paramType`)VALUES(?,?,?,?,(SELECT `user_id` FROM "+table+" WHERE `id`=?),?,?,?,NOW(),?,?,?,?,?,?,?)",new Object[]{shopNo,infoId,textureIds,textureName,infoId,resultPath,sufFormat,num,1,deviceNo,app,sortName,workId,lettering!=null?lettering:"",modId!=null?Integer.parseInt(modId):0});
		int result=this.indentDao.updateShopOrderNum(deviceNo, app, "shop", 1);
		if(result<=0){
			throw new RuntimeException();
		}
		return result;
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class)
	public int createShopForId(String resultPath, String infoId, String textureIds, String userId,
			String shopNo, String sufFormat, String num,String workId,String lettering,String modId) {
		String table="`new_goods_info`";
		//区分info_id
		boolean flag=true;
		if("xxx".equals(sufFormat)){
			table="`diy_goods_info2`";
			flag=false;
		}else{
			Map<String,Object>jpInfo=null;
			//验证修复材质（针对刻字商品精品定制公用同个材质不同商品id），定制查不到再去查精品会的，最终保存是使用定制的info
				jpInfo=indentDao.queryJphInfoByInfoId(infoId, textureIds);
				if(jpInfo==null || jpInfo.isEmpty())
					jpInfo=indentDao.queryJphInfoByInfoId2(infoId, textureIds);
				//传的可能是精品，查出并使用定制的商品id
				if(jpInfo!=null)
				infoId=jpInfo.get("make_id")==null?infoId:jpInfo.get("make_id").toString();
		}
			
		String sortName=this.getSortNameById(infoId,flag);
		String textureName=indentDao.queryTextureById(textureIds.split("_"));
		this.jt.update("INSERT INTO `diy_shopcart` (`shop_no`,`info_id`,`texture_ids`,`texture_name`,`user_id`,`img_url`,`file_type`,`num`,`creat_time`,`status`,`userId`,`sort_name`,`userwork`,`paramType`,`param1`)VALUES(?,?,?,?,(SELECT `user_id` FROM "+table+" WHERE `id`=?),?,?,?,NOW(),?,?,?,?,?,?)",new Object[]{shopNo,infoId,textureIds,textureName,infoId,resultPath,sufFormat,num,1,userId,sortName,workId,StringUtils.isBlank(modId)?0:modId,lettering});
		int result=this.indentDao.updateShopOrderNumForId(userId, "shop", 1);
		if(result<=0){
			throw new RuntimeException();
		}
		return result;
	}
	
	@Override
	public int updateShop(final String deviceNo, final String app, String shopNos, String nums) {
		final String[] shopNo=shopNos.split(",");
		final String[] num=nums.split(",");
		int result[]=this.jt.batchUpdate("UPDATE `diy_shopcart` SET `num`=? WHERE `shop_no`=? AND `device_no`=? AND `app`=?", new BatchPreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ps.setInt(1, Integer.parseInt(num[i]));
				ps.setString(2, shopNo[i]);
				ps.setString(3, deviceNo);
				ps.setString(4, app);
			}
			
			@Override
			public int getBatchSize() {
				return shopNo.length;
			}
		});
		return result.length;
	}
	
	@Override
	public int updateShopForId(final String userId, String shopNos, String nums) {
		final String[] shopNo=shopNos.split(",");
		final String[] num=nums.split(",");
		int result[]=this.jt.batchUpdate("UPDATE `diy_shopcart` SET `num`=?,creat_time=now() WHERE `shop_no`=? AND `userId`=?", new BatchPreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ps.setInt(1, Integer.parseInt(num[i]));
				ps.setString(2, shopNo[i]);
				ps.setString(3, userId);
			}
			
			@Override
			public int getBatchSize() {
				return shopNo.length;
			}
		});
		return result.length;
	}
	
	@Override
	public Map<String, Object> getOrderShopNum(String deviceNo, String app) {
		try {
			return this.jt.queryForMap("SELECT `shop_num` AS `shopNum`,`order_num` AS `orderNum` FROM `diy_device_user` WHERE `device_no`=? AND `app`=?",new Object[]{deviceNo,app});
		} catch (Exception e) {
		}
		return null;
	}
	
	@Override
	public Map<String, Object> getOrderShopNumForId(String userId) {
		try {
			return this.jt.queryForMap("SELECT `shop_num` AS `shopNum`,`order_num` AS `orderNum` FROM `diy_device_user` WHERE `id`=?",new Object[]{userId});
		} catch (Exception e) {
		}
		return null;
	}
	
	@Override
	public void invalidShop(String shopNo, String deviceNo, String app) {
		this.jt.update("UPDATE `diy_shopcart` SET `status`=0,`num`=0 WHERE `device_no`=? AND `app`=?  AND `shop_no` IN "+shopNo+"",new Object[]{deviceNo,app});
	}
	
	@Override
	public void invalidShopForId(String shopNo, String userId) {
		this.jt.update("UPDATE `diy_shopcart` SET `status`=0,`num`=0 WHERE `userId`=?  AND `shop_no` IN "+shopNo+"",new Object[]{userId});
	}
	
	@Override
	public List<Map<String, Object>> getShopInfoByShopNos(String shopNo) {
		List<Map<String, Object>> list=this.jt.queryForList("SELECT `ds`.`userwork`,`ds`.`shop_no` AS `shopNo`,`ds`.`sort_name` AS `sortName`,`ds`.`info_id` AS `infoId`,`ds`.`texture_ids` AS `textureIds`,`ds`.`texture_name` AS `textureName`,`ds`.`user_id` AS `userId`,`ds`.`img_url` AS `imgUrl`,`ds`.`file_type` AS `fileType`,`ds`.`num`,`dgi`.`name`,`dgi`.`transportfee`,`dit`.`now_price` AS `nowPrice`,`dg`.`name` AS `sortName`,`da`.`id` AS `actId`,`da`.`type`,`ds`.`param1`,`ds`.`paramType`,`ds`.`isUnicom` FROM `diy_shopcart` `ds` INNER JOIN `diy_goods_info2` `dgi` ON `dgi`.`id`=`ds`.`info_id` INNER JOIN `diy_info_texture2` `dit` ON `dit`.`info_id`=`ds`.`info_id` INNER JOIN `diy_good2` `dg` ON `dgi`.`good_id`=`dg`.`id` LEFT JOIN `diy_activity` `da` ON `da`.id=`dgi`.`activity_id` WHERE `dit`.`texture_ids`=`ds`.`texture_ids` AND `ds`.`status`=1 AND `ds`.`shop_no` IN "+shopNo+" AND `ds`.`file_type`='xxx'  UNION ALL SELECT `ds`.`userwork`,`ds`.`shop_no` AS `shopNo`,`ds`.`sort_name` AS `sortName`,`ds`.`info_id` AS `infoId`,`ds`.`texture_ids` AS `textureIds`,`ds`.`texture_name` AS `textureName`,`ds`.`user_id` AS `userId`,`ds`.`img_url` AS `imgUrl`,`ds`.`file_type` AS `fileType`,`ds`.`num`,`dgi`.`name`,`dgi`.`transportfee`,`dit`.`now_price` AS `nowPrice`,`dg`.`name` AS `sortName`,`da`.`id` AS `actId`,`da`.`type`,`ds`.`param1`,`ds`.`paramType`,`ds`.`isUnicom` FROM `diy_shopcart` `ds` LEFT JOIN `new_goods_info` `dgi` ON `dgi`.`id`=`ds`.`info_id` INNER JOIN `diy_info_texture2` `dit` ON `dit`.`make_id`=`ds`.`info_id` INNER JOIN `diy_good2` `dg` ON `dgi`.`good_id`=`dg`.`id` LEFT JOIN `diy_activity` `da` ON `da`.id=`dgi`.`activity_id` WHERE `dit`.`texture_ids`=`ds`.`texture_ids` AND `ds`.`status`=1 AND `ds`.`shop_no` IN "+shopNo+" AND `ds`.`file_type`!='xxx'");
		return (list==null||list.size()<0)?null:list;
	}
	
	@Override
	public List<Map<String, Object>> getShopInfoByShopNosByAllStatus(String shopNo) {
		List<Map<String, Object>> list=this.jt.queryForList("SELECT `ds`.`userwork`,`ds`.`shop_no` AS `shopNo`,`ds`.`sort_name` AS `sortName`,`ds`.`info_id` AS `infoId`,`ds`.`texture_ids` AS `textureIds`,`ds`.`texture_name` AS `textureName`,`ds`.`user_id` AS `userId`,`ds`.`img_url` AS `imgUrl`,`ds`.`file_type` AS `fileType`,`ds`.`num`,`dgi`.`name`,`dgi`.`transportfee`,`dit`.`now_price` AS `nowPrice`,`dg`.`name` AS `sortName`,`da`.`id` AS `actId`,`da`.`type`,`ds`.`param1`,`ds`.`paramType`,`ds`.`isUnicom` FROM `diy_shopcart` `ds` INNER JOIN `diy_goods_info2` `dgi` ON `dgi`.`id`=`ds`.`info_id` INNER JOIN `diy_info_texture2` `dit` ON `dit`.`info_id`=`ds`.`info_id` INNER JOIN `diy_good2` `dg` ON `dgi`.`good_id`=`dg`.`id` LEFT JOIN `diy_activity` `da` ON `da`.id=`dgi`.`activity_id` WHERE `dit`.`texture_ids`=`ds`.`texture_ids` AND `ds`.`shop_no` IN "+shopNo+" AND `ds`.`file_type`='xxx'  UNION ALL SELECT `ds`.`userwork`,`ds`.`shop_no` AS `shopNo`,`ds`.`sort_name` AS `sortName`,`ds`.`info_id` AS `infoId`,`ds`.`texture_ids` AS `textureIds`,`ds`.`texture_name` AS `textureName`,`ds`.`user_id` AS `userId`,`ds`.`img_url` AS `imgUrl`,`ds`.`file_type` AS `fileType`,`ds`.`num`,`dgi`.`name`,`dgi`.`transportfee`,`dit`.`now_price` AS `nowPrice`,`dg`.`name` AS `sortName`,`da`.`id` AS `actId`,`da`.`type`,`ds`.`param1`,`ds`.`paramType`,`ds`.`isUnicom` FROM `diy_shopcart` `ds` LEFT JOIN `new_goods_info` `dgi` ON `dgi`.`id`=`ds`.`info_id` INNER JOIN `diy_info_texture2` `dit` ON `dit`.`make_id`=`ds`.`info_id` INNER JOIN `diy_good2` `dg` ON `dgi`.`good_id`=`dg`.`id` LEFT JOIN `diy_activity` `da` ON `da`.id=`dgi`.`activity_id` WHERE `dit`.`texture_ids`=`ds`.`texture_ids` AND `ds`.`shop_no` IN "+shopNo+" AND `ds`.`file_type`!='xxx'");
		return (list==null||list.size()<0)?null:list;
	}
	
	@Override
	public List<Map<String,Object>>getActivityList(String shopNo){
		 String sql="SELECT `da`.about,`da`.`type`,`da`.`org_price`,`da`.`des_price`,`da`.`limit_num`,`da`.`count`,`da`.`param1`,`da`.`money`,`da`.`isAbout` FROM `diy_shopcart` `ds` LEFT JOIN `diy_goods_info2` `dgi` ON `dgi`.`id`=`ds`.`info_id` LEFT JOIN `diy_activity` `da` ON `da`.id=`dgi`.`activity_id` WHERE `ds`.`shop_no` IN "+shopNo+" AND `ds`.`status`=1 AND `da`.`type` is not null AND `ds`.file_type='xxx' GROUP BY `da`.`type` UNION ALL SELECT `da`.about,`da`.`type`,`da`.`org_price`,`da`.`des_price`,`da`.`limit_num`,`da`.`count`,`da`.`param1`,`da`.`money`,`da`.`isAbout` FROM `diy_shopcart` `ds` LEFT JOIN `new_goods_info` `ngi` ON `ngi`.`id`=`ds`.`info_id` LEFT JOIN `diy_activity` `da` ON `da`.id=`ngi`.`activity_id` WHERE `ds`.`shop_no` IN "+shopNo+" AND `ds`.`status`=1 AND `da`.`type` is not null AND `ds`.file_type!='xxx' GROUP BY `da`.`type`";
		 return super.jt.queryForList(sql);
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class)
	public double createOrderByShops(String orderNo, String shopNo, String infoId, String textureIds, String textureName,
			String userId, String imgUrl, String fileType, String num, String name, String nowPrice,String payId,double orgPrice,double desPrice,String couponId,double totalFee,Map<String, Object> addressMap,String deviceNo,String app,String transportFee,String sortName,double couponMoney,String types,String userwork) {
		try {
			int results= this.jt.update("INSERT INTO `diy_orders` (`order_no`,`shop_no`,`info_ids`,`user_ids`,`texture_ids`,`texture_names`,`img_url`,`file_type`,`pay_type`,`consignee`,`province`,`area`,`mobile`,`num`,`fee_transport`,`fee_total`,`price`,`coupon`,`coupon_id`,`des_privilege`,`org_privilege`,`creat_time`,`device_no`,`app`,`status`,`name`,`sort_name`,`activities`,`userwork`) "
					+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,NOW(),?,?,1,?,?,?,?)",new Object[]{orderNo,shopNo,infoId,userId,textureIds,textureName,imgUrl,fileType,payId,addressMap.get("name"),addressMap.get("province"),addressMap.get("area"),addressMap.get("mobile"),num,transportFee,(totalFee)<0?0:(totalFee),nowPrice,couponMoney,couponId,desPrice,orgPrice,deviceNo,app,name,sortName,types,userwork});
			if(results>0){
				return totalFee;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		throw new RuntimeException();
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class)
	public double createOrderByShopsForId(String orderNo, String shopNo, String infoId, String textureIds, String textureName,
			String merchantId, String imgUrl, String fileType, String num, String name, String nowPrice,String payId,double orgPrice,double desPrice,String couponId,double totalFee,Map<String, Object> addressMap,String userId,double transportFee,String sortName,double couponMoney,String types,String userwork,String message,String param1,String paramType,String isUnicom) {
		try {
			int results= this.jt.update("INSERT INTO `diy_orders` (`order_no`,`shop_no`,`info_ids`,`user_ids`,`texture_ids`,`texture_names`,`img_url`,`file_type`,`pay_type`,`consignee`,`province`,`area`,`mobile`,`num`,`fee_transport`,`fee_total`,`price`,`coupon`,`coupon_id`,`des_privilege`,`org_privilege`,`creat_time`,`userId`,`status`,`name`,`sort_name`,`activities`,`userwork`,`message`,`param1`,`paramType`,`isUnicom`) "
					+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,NOW(),?,1,?,?,?,?,?,?,?,?)",new Object[]{orderNo,shopNo,infoId,merchantId,textureIds,textureName,imgUrl,fileType,payId,addressMap.get("name"),addressMap.get("province"),addressMap.get("area"),addressMap.get("mobile"),num,transportFee,(totalFee)<0?0:(totalFee),nowPrice,couponMoney,couponId,desPrice,orgPrice,userId,name,sortName,types,userwork,message,param1,paramType,isUnicom});
			if(results>0){
				return totalFee;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		throw new RuntimeException();
	}
	
	@Override
	public int countActiveShop(String deviceNo, String app, String shopNo) {
		try {
			return this.jt.queryForObject("SELECT COUNT(*) FROM `diy_shopcart` WHERE `device_no`=? AND `app`=? AND `shop_no` IN "+shopNo+" AND `status`=0",new Object[]{deviceNo,app}, Integer.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	@Override
	public int countActiveShopForId(String userId, String shopNo) {
		try {
			return this.jt.queryForObject("SELECT COUNT(*) FROM `diy_shopcart` WHERE `userId`=? AND `shop_no` IN "+shopNo+" AND `status`=0",new Object[]{userId}, Integer.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	@Override
	public int addShopByOrder(String prices, String imgUrls, String fileTypes, String nums, String textureNamess,
			String infoIdss, String userIdss, String shopNo, String deviceNo, String app, String textureId) {
		return this.jt.update("INSERT INTO `diy_shopcart` (`shop_no`,`info_id`,`texture_ids`,`texture_name`,`user_id`,`img_url`,`file_type`,`num`,`device_no`,`app`,`creat_time`,`status`) VALUES(?,?,?,?,?,?,?,?,?,?,NOW(),1) ON DUPLICATE KEY UPDATE `num`=`num`+?,`status`=1",new Object[]{shopNo,infoIdss,textureId,textureNamess,userIdss,imgUrls,fileTypes,nums,deviceNo,app,nums});
	}
	
	@Override
	public int addShopByOrderForId(String prices, String imgUrls, String fileTypes, String nums, String textureNamess,
			String infoIdss, String userIdss, String shopNo, String userId, String textureId,String workId) {
		return this.jt.update("INSERT INTO `diy_shopcart` (`shop_no`,`info_id`,`texture_ids`,`texture_name`,`user_id`,`img_url`,`file_type`,`num`,`userId`,`creat_time`,`status`,`userwork`) VALUES(?,?,?,?,?,?,?,?,?,NOW(),1,?) ON DUPLICATE KEY UPDATE `num`=`num`+?,`status`=1",new Object[]{shopNo,infoIdss,textureId,textureNamess,userIdss,imgUrls,fileTypes,nums,userId,workId,nums});
	}
	
	@Override
	public List<Map<String, Object>> getShopByUserIds(String deviceNo, String app,int page,int number) {
		List<Map<String, Object>> list= this.jt.queryForList("SELECT `ds`.`user_id` AS `companyId`,`dsu`.`name` AS `companyName` FROM `diy_shopcart` `ds` INNER JOIN `diy_sys_user` `dsu` ON `ds`.`user_id`=`dsu`.`id` WHERE `ds`.`status`=1 AND `ds`.`device_no`=? AND `ds`.`app`=? GROUP BY `dsu`.`id` ORDER BY `ds`.`creat_time` DESC LIMIT ?,?",new Object[]{deviceNo,app,page*number,number});
		return (list==null || list.size()<=0)?null:list;
	}
	
	@Override
	public List<Map<String, Object>> getShopByUserIdsForId(String userId) {
		List<Map<String, Object>> list= this.jt.queryForList("SELECT `dsu`.`remotefee`,`ds`.`user_id` AS `companyId`,`dsu`.`name` AS `companyName`,`dsu`.`enough_money` AS `enoughMoney`,`dsu`.`transportfee` FROM `diy_shopcart` `ds` INNER JOIN `diy_sys_user` `dsu` ON `ds`.`user_id`=`dsu`.`id` WHERE `ds`.`status`=1 AND `ds`.`userId`=? GROUP BY `dsu`.`id` ORDER BY `ds`.`creat_time` DESC ",new Object[]{userId});
		return (list==null || list.size()<=0)?null:list;
	}
	
	@Override
	public List<Map<String, Object>> getShopInfoByUserId(Integer companyId,String deviceNo,String app) {
		return this.jt.queryForList("SELECT `ds`.`shop_no` AS `shopNo`,`ds`.`sort_name` AS `sortName`,`ds`.`texture_name` AS `textureName`,`ds`.`img_url` AS `imgUrl`,`ds`.`file_type` AS `fileType`,`ds`.`num`,`ngi`.`name`,`ngi`.`postage`,`ngi`.`free_count`,`dit`.`now_price` AS `nowPrice`,`ngi`.`postage`,`ngi`.`free_count`,`ds`.`creat_time`,`da`.`type` FROM `diy_shopcart` `ds` INNER JOIN `new_goods_info` `ngi` ON `ds`.`info_id`=`ngi`.`id` INNER JOIN `diy_info_texture2` `dit` ON `dit`.`texture_ids`=`ds`.`texture_ids` LEFT JOIN `diy_activity` `da` ON `da`.`id`=`ngi`.`activity_id` WHERE `dit`.`make_id`=`ds`.`info_id` AND `ds`.`device_no`=? AND `ds`.`app`=? AND `ngi`.`user_id`=? AND `ds`.`status`=1 AND `ds`.`file_type`!='xxx' AND `dit`.`make_id`=`ds`.`info_id` UNION ALL SELECT `ds`.`shop_no` AS `shopNo`,`ds`.`sort_name` AS `sortName`,`ds`.`texture_name` AS `textureName`,`ds`.`img_url` AS `imgUrl`,`ds`.`file_type` AS `fileType`,`ds`.`num`,`dgi`.`name`,`dgi`.`postage`,`dgi`.`free_count`,`dit`.`now_price` AS `nowPrice`,`dgi`.`postage`,`dgi`.`free_count`,`ds`.`creat_time`,`da`.`type` FROM `diy_shopcart` `ds` INNER JOIN `diy_goods_info2` `dgi` ON `ds`.`info_id`=`dgi`.`id` INNER JOIN `diy_info_texture2` `dit` ON `dit`.`texture_ids`=`ds`.`texture_ids` LEFT JOIN `diy_activity` `da` ON `da`.`id`=`dgi`.`activity_id` WHERE `dit`.`info_id`=`ds`.`info_id` AND `ds`.`device_no`=? AND `ds`.`app`=? AND `dgi`.`user_id`=? AND `ds`.`status`=1 `ds`.`file_type`='xxx' ORDER BY `creat_time` DESC",new Object[]{deviceNo,app,companyId,deviceNo,app,companyId});
	}
	
	@Override
	public List<Map<String, Object>> getShopInfoByUserIdForId(Integer companyId,String userId) {
		return this.jt.queryForList("SELECT `ds`.`isUnicom`,`ds`.`paramType` AS `about`,1 AS `typeId`,`dit`.`make_id` AS `flagId`,`dit`.`pre_url`,`dit`.`diy25_img` AS `coverImg`,`dit`.`origin`,`dit`.`wh_size`,`dit`.`cover_size`,`ds`.`texture_ids`,`ds`.`info_id` AS `goodsId`,`ds`.`isselect`,`ds`.`user_id`,`ds`.`shop_no` AS `shopNo`,`ds`.`sort_name` AS `sortName`,`ds`.`texture_name` AS `textureName`,`ds`.`img_url` AS `imgUrl`,`ds`.`file_type` AS `fileType`,`ds`.`num`,`ds`.`userwork`,`ngi`.`name`,`ngi`.`ispostage`,`ngi`.`free_count`,`dit`.`now_price` AS `nowPrice`,`ds`.`creat_time`,`da`.`id` AS `actId`,`da`.`type`,`ngi`.ischange_texture FROM `diy_shopcart` `ds` INNER JOIN `new_goods_info` `ngi` ON `ds`.`info_id`=`ngi`.`id` INNER JOIN `diy_info_texture2` `dit` ON `dit`.`texture_ids`=`ds`.`texture_ids` AND `dit`.`make_id`=`ds`.`info_id` LEFT JOIN `diy_activity` `da` ON `da`.`id`=`ngi`.`activity_id` WHERE `dit`.`make_id`=`ds`.`info_id` AND `ds`.`userId`=? AND `ngi`.`user_id`=? AND `ds`.`status`=1 AND `ds`.`file_type`!='xxx' AND `dit`.`make_id`=`ds`.`info_id` UNION ALL SELECT `ds`.`isUnicom`,`ds`.`paramType` AS `about`,2 AS `typeId`,`dit`.`info_id` AS `flagId`,`dit`.`pre_url`,`dit`.`diy25_img` AS `coverImg`,`dit`.`origin`,`dit`.`wh_size`,`dit`.`cover_size`,`ds`.`texture_ids`,`ds`.`info_id` AS `goodsId`,`ds`.`isselect`,`ds`.`user_id`,`ds`.`shop_no` AS `shopNo`,`ds`.`sort_name` AS `sortName`,`ds`.`texture_name` AS `textureName`,`ds`.`img_url` AS `imgUrl`,`ds`.`file_type` AS `fileType`,`ds`.`num`,`ds`.`userwork`,`dgi`.`name`,`dgi`.`ispostage`,`dgi`.`free_count`,`dit`.`now_price` AS `nowPrice`,`ds`.`creat_time`,`da`.`id` AS `actId`,`da`.`type`,`dgi`.ischange_texture FROM `diy_shopcart` `ds` INNER JOIN `diy_goods_info2` `dgi` ON `ds`.`info_id`=`dgi`.`id` INNER JOIN `diy_info_texture2` `dit` ON `dit`.`texture_ids`=`ds`.`texture_ids` AND `dit`.`info_id`=`ds`.`info_id` LEFT JOIN `diy_activity` `da` ON `da`.`id`=`dgi`.`activity_id` WHERE `dit`.`info_id`=`ds`.`info_id` AND `ds`.`userId`=? AND `dgi`.`user_id`=? AND `ds`.`status`=1 AND `ds`.`file_type`='xxx' ORDER BY `type`,`creat_time` DESC",new Object[]{userId,companyId,userId,companyId});
	}
	
	@Override
	public List<Map<String,Object>>getShopActivityByUserId(Integer companyId,String deviceNo,String app){
		return this.jt.queryForList("SELECT `da`.about,`da`.`type`,`da`.`org_price`,`da`.`des_price`,`da`.`limit_num`,`da`.`count`,`da`.`param1`,`da`.`money`,`da`.`isAbout` FROM `diy_shopcart` `ds` LEFT JOIN `diy_goods_info2` `dgi` ON `dgi`.`id`=`ds`.`info_id` LEFT JOIN `diy_activity` `da` ON `da`.id=`dgi`.`activity_id` WHERE `ds`.device_no=? AND `ds`.app=? AND `ds`.user_id=? AND `ds`.`status`=1 AND `da`.`type` is not null AND `ds`.file_type='xxx' GROUP BY `da`.`type` UNION ALL SELECT `da`.about,`da`.`type`,`da`.`org_price`,`da`.`des_price`,`da`.`limit_num`,`da`.`count`,`da`.`param1`,`da`.`money`,`da`.`isAbout` FROM `diy_shopcart` `ds` LEFT JOIN `new_goods_info` `ngi` ON `ngi`.`id`=`ds`.`info_id` LEFT JOIN `diy_activity` `da` ON `da`.id=`ngi`.`activity_id` WHERE `ds`.device_no=? AND `ds`.app=? AND `ds`.user_id=? AND `ds`.`status`=1 AND `da`.`type` is not null AND `ds`.file_type!='xxx' GROUP BY `da`.`type`",new Object[]{deviceNo,app,companyId,deviceNo,app,companyId});
	}
	
	@Override
	public List<Map<String,Object>>getShopActivityByUserIdForId(Integer companyId,String userId){
		return this.jt.queryForList("SELECT `da`.about,`da`.`type`,`da`.`org_price`,`da`.`des_price`,`da`.`limit_num`,`da`.`count`,`da`.`param1`,`da`.`money`,`da`.`isAbout` FROM `diy_shopcart` `ds` LEFT JOIN `diy_goods_info2` `dgi` ON `dgi`.`id`=`ds`.`info_id` LEFT JOIN `diy_activity` `da` ON `da`.id=`dgi`.`activity_id` WHERE `ds`.`userId`=? AND `ds`.user_id=? AND `ds`.`status`=1 AND `da`.`type` is not null AND `ds`.file_type='xxx' GROUP BY `da`.`type` UNION ALL SELECT `da`.about,`da`.`type`,`da`.`org_price`,`da`.`des_price`,`da`.`limit_num`,`da`.`count`,`da`.`param1`,`da`.`money`,`da`.`isAbout` FROM `diy_shopcart` `ds` LEFT JOIN `new_goods_info` `ngi` ON `ngi`.`id`=`ds`.`info_id` LEFT JOIN `diy_activity` `da` ON `da`.id=`ngi`.`activity_id` WHERE `ds`.`userId`=? AND `ds`.user_id=? AND `ds`.`status`=1 AND `da`.`type` is not null AND `ds`.file_type!='xxx' GROUP BY `da`.`type`",new Object[]{userId,companyId,userId,companyId});
	}
	
	@Override
	public String getSortNameById(String infoId,boolean flag){
		String sql1="SELECT `dg`.`name` FROM  `diy_goods_info2` `dgi` LEFT JOIN `diy_good2` `dg` ON `dg`.`id`=`dgi`.`good_id` WHERE `dgi`.id=? LIMIT 1";
		String sql2="SELECT `dg`.`name` FROM  `new_goods_info` `dgi` LEFT JOIN `diy_good2` `dg` ON `dg`.`id`=`dgi`.`good_id` WHERE `dgi`.id=? LIMIT 1";
		if(flag){
			return this.jt.queryForObject(sql2,String.class, infoId);
		}
		return this.jt.queryForObject(sql1,String.class, infoId);
	}
	
	@Override
	public List<Map<String,Object>> queryShopList(String deviceNo,String app){
		String sql="SELECT * FROM diy_shopcart WHERE device_no=? AND app=? AND status=1";
		return this.jt.queryForList(sql,deviceNo,app);
	}
	
	@Override
	public List<Map<String,Object>>queryShopListForId(String userId){
		String sql="SELECT * FROM diy_shopcart WHERE userId=? AND status=1";
		return this.jt.queryForList(sql,userId);
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class)
	public int amendShop()throws Exception{
		String sql="select id from diy_device_user where app=concat('com.shua.CustomMall',openid)";
		String sql2="update diy_device_user set shop_num=(select count(id) from diy_shopcart where `status`=1 and userId=?) where id=? and (select count(id) from diy_shopcart where `status`=1 and userId=?)!=0";
		List<Map<String,Object>>list=this.jt.queryForList(sql);
		int result=0;
		for(Map<String,Object>m : list){
			String id=m.get("id")==null?"":m.get("id").toString();
			result=this.jt.update(sql2, id,id,id);
		} 
		return result;
	}
	
	@Override
	public List<Map<String, Object>> togetherGoods(String id, String page) {
		return this.jt.queryForList("SELECT dgi.id AS goodsId,dgi.name,t.now_price AS nowPrice,dgi.sell,dgi.diy25_img AS icoUrl FROM `diy_goods_info2` `dgi` LEFT JOIN diy_info_texture2 t ON dgi.id = t.info_id WHERE t.isdefault=1 AND `dgi`.`isBoutique`=1 AND `dgi`.`user_id`=? AND `dgi`.`state`=1 AND `t`.`status`=1 ORDER BY t.now_price ASC  LIMIT ?,?",new Object[]{id,Integer.parseInt(page)*10,10});
	}
	
	@Override
	public List<Map<String, Object>> togetherForActivity(String id,String activityId, String page) {
		return this.jt.queryForList("SELECT dgi.id AS goodsId,dgi.name,t.now_price AS nowPrice,dgi.sell,dgi.diy25_img AS icoUrl FROM `diy_goods_info2` `dgi` LEFT JOIN diy_info_texture2 t ON dgi.id = t.info_id WHERE t.isdefault=1 AND `dgi`.`user_id`=? AND `dgi`.`activity_id`=? AND `dgi`.`state`=1 AND `t`.`status`=1 ORDER BY t.now_price ASC  LIMIT ?,?",new Object[]{id,activityId,Integer.parseInt(page)*10,10});
	}
	
	@Override
	public boolean carshopSelect(String id, String shopNoss, String type) {
		StringBuilder sql=new StringBuilder();
		sql.append("UPDATE `diy_shopcart` SET `isselect`=");
		if("add".equals(type) || ("alladd").equals(type)){
			sql.append("1 ");
		}else if("del".equals(type)||("alldel").equals(type)){
			sql.append("0 ");
		}
		if("alladd".equals(type) || ("alldel".equals(type))){
			sql.append("WHERE `userId`="+id);
		}else{
			sql.append("WHERE `shop_no` IN "+shopNoss);
		}
		try {
			this.jt.update(sql.toString());
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	@Override
	public List<Map<String, Object>> getCouponByGoodId(Integer companyId) {
		return this.jt.queryForList("SELECT `id`,`title`,`about`,`valid`,`org_price` AS `orgPrice`,`des_price` AS `desPrice` FROM `diy_coupon` WHERE `store_id`=? AND `status`=1 AND `type`=2 ORDER BY `des_price` DESC",new Object[]{companyId});
	}
	
	@Override
	public boolean addShopByGood(Map<String, Object> param) {
		int result=this.npjt.update("INSERT INTO `diy_shopcart` (`shop_no`,`info_id`,`texture_ids`,`texture_name`,`user_id`,`img_url`,`file_type`,`num`,`creat_time`,`userId`,`sort_name`) VALUES(:shop_no,:id,:texture_ids,:texture_name,:user_id,:icoUrl,:file_type,:num,NOW(),:userId,:name)", param);
		if(result>0){
			return true;
		}
		return false;
	}
	
	@Override
	public boolean checkIsGetCoupon(Integer temcouponId, String userId) {
		try {
			this.jt.queryForObject("SELECT `id` FROM `diy_coupon_user` WHERE `userId`=? AND `coupon_id`=? LIMIT 1",new Object[]{userId,temcouponId}, Integer.class);
			return false;
		} catch (Exception e) {
		}
		return true;
	}
	
	@Override
	public int getUserRealShopNum(String userId){
		String sql="SELECT COUNT(id) FROM diy_shopcart WHERE userId=? AND `status`=1"; 
		return this.jt.queryForObject(sql,Integer.class,userId);
	}
	
	@Override
	public String getUserShopFailNo(String userId){
		String sql="SELECT shop_no FROM diy_shopcart WHERE `status`=1 AND userId=? AND `shop_no` NOT IN(SELECT `ds`.`shop_no` FROM `diy_shopcart` `ds` INNER JOIN `new_goods_info` `ngi` ON `ds`.`info_id`=`ngi`.`id` INNER JOIN `diy_info_texture2` `dit` ON `dit`.`texture_ids`=`ds`.`texture_ids` LEFT JOIN `diy_activity` `da` ON `da`.`id`=`ngi`.`activity_id` WHERE `dit`.`make_id`=`ds`.`info_id` AND `ds`.`userId`=? AND `ds`.`status`=1 AND `ds`.`file_type`!='xxx' AND `dit`.`make_id`=`ds`.`info_id` UNION ALL SELECT `ds`.`shop_no` FROM `diy_shopcart` `ds` INNER JOIN `diy_goods_info2` `dgi` ON `ds`.`info_id`=`dgi`.`id` INNER JOIN `diy_info_texture2` `dit` ON `dit`.`texture_ids`=`ds`.`texture_ids` LEFT JOIN `diy_activity` `da` ON `da`.`id`=`dgi`.`activity_id` WHERE `dit`.`info_id`=`ds`.`info_id` AND `ds`.`userId`=? AND `ds`.`status`=1 AND `ds`.`file_type`='xxx' GROUP BY `ds`.`shop_no`)";
		List<Map<String,Object>>resultList=this.jt.queryForList(sql,new Object[]{userId,userId,userId});
		if(resultList==null || resultList.isEmpty()){
			return "";
		}
		StringBuilder sb=new StringBuilder();
		for(int i=0;i<resultList.size();i++){
			sb.append("'"+resultList.get(i).get("shop_no")+"'").append(",");
		}
		return sb.substring(0,sb.length()-1);
	}
	
	@Override
	public String getShopNo(String userId, Integer info_id, String texture_ids) {
		try {
			return this.jt.queryForObject("SELECT `shop_no` FROM `diy_shopcart` WHERE `info_id`=? AND `texture_ids`=? AND `userId`=? AND `status`=1 LIMIT 1",new Object[]{info_id,texture_ids,userId}, String.class);
		} catch (Exception e) {
		}
		return null;
	}
	
	@Override
	public void updateNum(String userId, String shopNo) {
		this.jt.update("UPDATE `diy_shopcart` SET `num`=`num`+1 WHERE `userId`=? AND `shop_no`=? ",new Object[]{userId,shopNo});
	}
}
