package cn._51app.dao.impl;

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
import cn._51app.dao.INewOrderDao;
import cn._51app.dao.INewShopCartDao;

@Repository
public class NewShopCartDao extends BaseDao implements INewShopCartDao{
	@Autowired
	private INewOrderDao iNewOrderDao;
	@Override
	public List<Map<String, Object>> queryShopList(String deviceNo, String app, int page, int number) {
		List<Map<String, Object>> list=this.jt.queryForList("SELECT `ds`.`shop_no` AS `shopNo`,`ds`.`texture_name` AS `textureName`,`ds`.`img_url` AS `imgUrl`,`ds`.`file_type` AS `fileType`,`ds`.`num`,`dgi`.`name`,`dgi`.`transportfee`,`dit`.`now_price` AS `nowPrice` FROM `diy_shopcart` `ds` INNER JOIN `diy_goods_info` `dgi` ON `ds`.`info_id`=`dgi`.`id` INNER JOIN `diy_info_texture` `dit` ON `dit`.`texture_ids`=`ds`.`texture_ids` WHERE `dit`.`info_id`=`ds`.`info_id` AND `ds`.`device_no`=? AND `ds`.`app`=? AND `ds`.`status`=1 ORDER BY `ds`.`creat_time` DESC LIMIT ?,?",new Object[]{deviceNo,app,page*number,number});
		return list==null||list.size()==0?null:list;
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class)
	public int createShop(String resultPath, String infoId, String textureIds, String deviceNo, String app,
			String shopNo, String sufFormat, String num) {
		String textureName=iNewOrderDao.queryTextureById(textureIds.split("_"));
		this.jt.update("INSERT INTO `diy_shopcart` (`shop_no`,`info_id`,`texture_ids`,`texture_name`,`user_id`,`img_url`,`file_type`,`num`,`creat_time`,`status`,`device_no`,`app`)VALUES(?,?,?,?,(SELECT `user_id` FROM `diy_goods_info` WHERE `id`=?),?,?,?,NOW(),?,?,?)",new Object[]{shopNo,infoId,textureIds,textureName,infoId,resultPath,sufFormat,num,1,deviceNo,app});
		int result=this.iNewOrderDao.updateShopOrderNum(deviceNo, app, "shop", 1);
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
	public Map<String, Object> getOrderShopNum(String deviceNo, String app) {
		try {
			return this.jt.queryForMap("SELECT `shop_num` AS `shopNum`,`order_num` AS `orderNum` FROM `diy_device_user` WHERE `device_no`=? AND `app`=?",new Object[]{deviceNo,app});
		} catch (Exception e) {
		}
		return null;
	}
	
	@Override
	public List<Map<String, Object>> getShopInfoByShopNos(String shopNo) {
		List<Map<String, Object>> list=this.jt.queryForList("SELECT `ds`.`shop_no` AS `shopNo`,`ds`.`info_id` AS `infoId`,`ds`.`texture_ids` AS `textureIds`,`ds`.`texture_name` AS `textureName`,`ds`.`user_id` AS `userId`,`ds`.`img_url` AS `imgUrl`,`ds`.`file_type` AS `fileType`,`ds`.`num`,`dgi`.`name`,`dgi`.`transportfee`,`dit`.`now_price` AS `nowPrice` FROM `diy_shopcart` `ds` INNER JOIN `diy_goods_info` `dgi` ON `dgi`.`id`=`ds`.`info_id` INNER JOIN `diy_info_texture` `dit` ON `dit`.`info_id`=`ds`.`info_id` WHERE `dit`.`texture_ids`=`ds`.`texture_ids` AND `ds`.`status`=1 AND `ds`.`shop_no` IN "+shopNo+"");
		return (list==null||list.size()<0)?null:list;
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class)
	public double createOrderByShops(String orderNo, String shopNo, String infoId, String textureIds, String textureName,
			String userId, String imgUrl, String fileType, String num, String name, String nowPrice,String payId,double orgPrice,double desPrice,String couponId,double totalFee,Map<String, Object> addressMap,String deviceNo,String app,String transportFee) {
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
			int results= this.jt.update("INSERT INTO `diy_orders` (`order_no`,`shop_no`,`info_ids`,`user_ids`,`texture_ids`,`texture_names`,`img_url`,`file_type`,`pay_type`,`consignee`,`province`,`area`,`mobile`,`num`,`fee_transport`,`fee_total`,`price`,`coupon`,`coupon_id`,`des_privilege`,`org_privilege`,`creat_time`,`device_no`,`app`,`status`) "
					+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,NOW(),?,?,1)",new Object[]{orderNo,shopNo,infoId,userId,textureIds,textureName,imgUrl,fileType,payId,addressMap.get("name"),addressMap.get("province"),addressMap.get("area"),addressMap.get("mobile"),num,transportFee,(totalFee-couponMoney)<0?0:(totalFee-couponMoney),nowPrice,couponMoney,couponId,desPrice,orgPrice,deviceNo,app});
			if(results>0){
				return totalFee-couponMoney;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		throw new RuntimeException();
	}
	
	@Override
	public void invalidShop(String shopNo,String deviceNo,String app) {
		this.jt.update("UPDATE `diy_shopcart` SET `status`=0,`num`=0 WHERE `device_no`=? AND `app`=?  AND `shop_no` IN "+shopNo+"",new Object[]{deviceNo,app});
	}
	
	@Override
	public int addShopByOrder(String prices, String imgUrls, String fileTypes, String nums, String textureNamess,
			String infoIdss, String userIdss,String shopNo,String deviceNo,String app,String textureId) {
		return this.jt.update("INSERT INTO `diy_shopcart` (`shop_no`,`info_id`,`texture_ids`,`texture_name`,`user_id`,`img_url`,`file_type`,`num`,`device_no`,`app`,`creat_time`,`status`) VALUES(?,?,?,?,?,?,?,?,?,?,NOW(),1) ON DUPLICATE KEY UPDATE `num`=`num`+?,`status`=1",new Object[]{shopNo,infoIdss,textureId,textureNamess,userIdss,imgUrls,fileTypes,nums,deviceNo,app,nums});
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
}
