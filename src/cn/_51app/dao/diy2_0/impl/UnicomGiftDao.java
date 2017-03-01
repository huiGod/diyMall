package cn._51app.dao.diy2_0.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import cn._51app.dao.BaseDao;
import cn._51app.dao.diy2_0.IUnicomGiftDao;
import cn._51app.util.CommonUtil;
import cn._51app.util.PropertiesUtil;

@Repository
public class UnicomGiftDao extends BaseDao implements IUnicomGiftDao {
	
	private final String SYSPATH =PropertiesUtil.getValue("diy.goods.url");

	@Override
	public boolean addGift(String mobile,int count) throws Exception {
		String sql = "INSERT INTO diy_unicom_gift(mobile,receive_count) VALUES(?,?)";
		return jt.update(sql, mobile,count) > 0;
	}

	@Override
	public boolean isReceive(String mobile) {
		String sql = "SELECT COUNT(id) FROM diy_unicom_gift WHERE mobile=?";
		int count = jt.queryForObject(sql, new Object[]{mobile},Integer.class);
		return count > 0;
	}

	@Override
	public int receiveNum() {
		String sql = "SELECT SUM(receive_count) FROM diy_unicom_gift";
		return jt.queryForObject(sql, Integer.class);
	}

	
	
	public boolean addToShop(String userId,String goodsId,String textureIds,String textureName) throws Exception{
		String sql = "SELECT `name`, user_id, diy25_img as img_url FROM diy_goods_info2 WHERE id=?";
		Map<String, Object> shopMap = jt.queryForMap(sql,goodsId);
		if (shopMap != null) {
			shopMap.put("shop_no", CommonUtil.createOrderNo("G", 3));
			shopMap.put("info_id", goodsId);
			shopMap.put("texture_ids", textureIds);
			shopMap.put("texture_name", textureName);
			shopMap.put("userId", userId);
		}
		
		String insert = "INSERT INTO `diy_shopcart` "
				+ "(`shop_no`,`info_id`,`texture_ids`,`texture_name`,`user_id`,`img_url`,`file_type`,`num`,`creat_time`,`status`,`userId`,`isUnicom`)"
				+ " VALUES(:shop_no,:info_id,:texture_ids,:texture_name,:user_id,:img_url,'xxx',1,NOW(),1,:userId,1)";
		
		return this.npjt.update(insert,shopMap)>0;
	}

	@Override
	public String getMobileByUser(String userId) throws Exception {
		String sql = "SELECT mobile FROM diy_device_user WHERE id = ?";
		return this.jt.queryForObject(sql, new Object[]{userId},String.class);
	}

	@Override
	public boolean isLogin(String userId) throws Exception{
		String sql="SELECT id,openid,mobile,qqid FROM diy_device_user WHERE id=?";
		List<Map<String,Object>> list=jt.queryForList(sql,userId);
		
		if (list != null && list.size()>0) {
			if((list.get(0).get("openid")!=null && !list.get(0).get("openid").toString().equals("")) || (list.get(0).get("qqid")!=null && !list.get(0).get("qqid").toString().equals("")) || (list.get(0).get("mobile")!=null && !list.get(0).get("mobile").toString().equals("0"))){
				return true;
			}
		}
		return false;
	}

	@Override
	public String getUnicomGoods(Map<String, Object> paramMap) throws Exception {
		String cacheKey =paramMap.get("cacheKey").toString();
		
		if (super.isCacheNull(cacheKey).equals("a")||super.isCacheNull(cacheKey).equals("c")) {
			String sql = "SELECT ug.sort,ug.goods_id,ug.texture_ids,ug.texture_name,gi.`name`,gi.transportfee,gi.diy25_img,it.now_price,ug.goodsType FROM diy_unicom_goods ug LEFT JOIN diy_goods_info2 gi ON gi.id = ug.goods_id LEFT JOIN diy_info_texture2 it ON it.info_id = ug.goods_id AND it.texture_ids = ug.texture_ids WHERE ug.`status` = 1 AND ug.goodsType=1 UNION ALL SELECT ug.sort,ug.goods_id,ug.texture_ids,ug.texture_name,gi.`name`,gi.transportfee,gi.diy25_img,it.now_price,ug.goodsType FROM diy_unicom_goods ug LEFT JOIN new_goods_info gi ON gi.id = ug.goods_id LEFT JOIN diy_info_texture2 it ON it.make_id = ug.goods_id AND it.texture_ids = ug.texture_ids WHERE ug.`status` = 1 AND ug.goodsType=2 UNION ALL SELECT ug.sort,ug.goods_id,ug.texture_ids,ug.texture_name,gi.`name`,gi.transportfee,gi.diy25_img,it.now_price,ug.goodsType FROM diy_unicom_goods ug LEFT JOIN new_goods_info gi ON gi.id = ug.goods_id LEFT JOIN diy_info_texture2 it ON it.make_id = ug.goods_id AND it.texture_ids = ug.texture_ids WHERE ug.`status` = 1 AND ug.goodsType=2 UNION ALL SELECT ug.sort,ug.goods_id,ug.texture_ids,ug.texture_name,gi.`name`,gi.transportfee,gi.diy25_img,it.now_price,ug.goodsType FROM diy_unicom_goods ug LEFT JOIN new_goods_info gi ON gi.id = ug.goods_id LEFT JOIN diy_info_texture2 it ON it.make_id = ug.goods_id AND it.texture_ids = ug.texture_ids WHERE ug.`status` = 1 AND ug.goodsType=4 ORDER BY sort DESC";
			List<Map<String,Object>> result = this.jt.queryForList(sql);
			if (result != null) {
				
				for (Map<String, Object> map : result) {
					String icoUrl = (String) map.get("icoUrl");
					if (StringUtils.isNotBlank(icoUrl)) {
						map.put("icoUrl", SYSPATH + icoUrl);
					}			
				}
				
//				System.out.println("数据从数据库获取");
				if(super.isCacheNull(cacheKey).equals("a")){
					int cacheTime =new Integer(paramMap.get("cacheTime").toString());
					return super.saveAndGet(result, cacheKey, cacheTime);
				}else{
					return super.toJson(result);
				}
			}
		}else if(isCacheNull(cacheKey).equals("b")){
//			System.out.println("数据从缓存获取");
			return super.q(cacheKey);
		}
		return "";
	}
	
	
	@Override
	public boolean isReceiveGoods(String userId) {
		String sql = "SELECT COUNT(id) FROM diy_shopcart WHERE userId = ? AND isUnicom = 1"; 
		int count = jt.queryForObject(sql, new Object[]{userId},Integer.class);
		return count > 0;
	}
	
	@Override
	public String getReceiveGoodsId(String userId) throws Exception{
		
		String fromShopcart = "SELECT info_id FROM diy_shopcart WHERE userId = ? AND isUnicom = 1 AND `status`=1 LIMIT 1";
		String fromOrder = "SELECT info_ids,isUnicom FROM diy_orders where userId=? and isUnicom like '%1%' LIMIT 1";
		
		String id =  "";
		try {
			id = super.jt.queryForObject(fromShopcart, new Object[]{userId},String.class);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
		if (StringUtils.isBlank(id)) {
			Map<String, Object> map = jt.queryForMap(fromOrder,userId);
			if (map != null && map.get("info_ids") != null && map.get("isUnicom") != null) {
				String[] infoIds = map.get("info_ids").toString().split(",");
				String[] isUnicom = map.get("isUnicom").toString().split(",");
				
				if (infoIds.length == isUnicom.length) {
					for (int i = 0; i < isUnicom.length; i++) {
						if (isUnicom[i].equals("1")) {
							id = infoIds[i];
						}
					}
				}
			}
		}
		return id;
	}
	
	@Override
	public boolean isReceiveCoupon(String userId) {
		String sql = "select count(c.id) from diy_coupon c Left JOIN  diy_coupon_user cu ON cu.coupon_id = c.id where c.`type` = 8  and cu.userId=?";
		return super.jt.queryForObject(sql, new Object[]{userId},Integer.class)>0;
	}
}
