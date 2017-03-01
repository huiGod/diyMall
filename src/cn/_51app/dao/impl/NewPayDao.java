package cn._51app.dao.impl;
import java.util.Map;

import org.springframework.stereotype.Repository;

import cn._51app.dao.BaseDao;
import cn._51app.dao.INewPayDao;

@Repository
public class NewPayDao extends BaseDao implements INewPayDao{
	@Override
	public int checkOrderIsPayed(String trade_no) {
		try {
			return this.jt.queryForObject("SELECT COUNT(*) FROM `diy_trade` WHERE `trade_no`=?",new Object[]{trade_no}, Integer.class);
		} catch (Exception e) {
		}
		return 0;
	}
	
	@Override
	public boolean insertPayRecord(Map<String, Object> paramMap) {
		try {
			this.jt.update("INSERT INTO `diy_trade` (`order_no`,`email`,`trade_no`,`price`,`time`,`type`) VALUES(?,?,?,?,NOW(),?)",new Object[]{paramMap.get("order_no"),paramMap.get("email"),paramMap.get("trade_no"),paramMap.get("price"),paramMap.get("type")});
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	@Override
	public boolean changeOrderStatus(Map<String, Object> paramMap) {
		try {
			
			int result=this.jt.update("UPDATE `diy_orders` SET `status`=2,`paytime`=NOW() WHERE `order_no`=? AND (?+0.1)>=`fee_total` ",new Object[]{paramMap.get("order_no"),paramMap.get("price")});
			if(result>0){//已付款
				return true;
			}
		} catch (Exception e) {
		}
		return false;
	}
	
	@Override
	public boolean updatePayRecord(Map<String, Object> paramMap) {
		try {
			int result=this.jt.update("UPDATE `diy_trade` SET `email`=?,`trade_no`=?,`price`=?,`time`=NOW() WHERE `order_no`=? ",new Object[]{paramMap.get("email"),paramMap.get("trade_no"),paramMap.get("price"),paramMap.get("order_no")});
			if(result>0){
				return true;
			}
		} catch (Exception e) {
		}
		return true;
	}
	
	@Override
	public int isH5Custom(String orderNo){
		String sql1 = "SELECT info_ids FROM `diy_orders` WHERE order_no=?";
		String info_ids = jt.queryForObject(sql1, String.class,orderNo);
		String sql2 = "SELECT COUNT(id) FROM `diy_goods_info` WHERE id IN("+info_ids+") AND isBoutique = 2";
		return jt.queryForObject(sql2, Integer.class);
	}
	
	@Override
	public boolean changeOrderStatus4H5Custom(Map<String, Object> paramMap) {
		try {
			int result=this.jt.update("UPDATE `diy_orders` SET `status`=8,`paytime`=NOW() WHERE `order_no`=? AND (?+0.1)>=`fee_total` ",new Object[]{paramMap.get("order_no"),paramMap.get("price")});
			if(result>0){
				return true;
			}
		} catch (Exception e) {
		}
		return false;
	}
	
	@Override
	public void changeGoodsSell(String orderNo) throws Exception{
		String sql = "SELECT `info_ids`,`num`,`file_type` FROM `diy_orders` WHERE `order_no` = ?";
		Map<String,Object> map = this.jt.queryForMap(sql,orderNo);
		if (map != null) {
			String[] infoIds = map.get("info_ids").toString().split(",");
			String[] nums = map.get("num").toString().split(",");
			String[] fileTypes = map.get("file_type").toString().split(",");
			
			for (int i = 0; i < fileTypes.length; i++) {
				int id = Integer.parseInt(infoIds[i]);
				int num = Integer.parseInt(nums[i]);
				if (fileTypes[i].equals("xxx") || fileTypes[i].equals("zzz")) {//精品商品				
					String upSell = "UPDATE `diy_goods_info2` SET `sell` = `sell` + ? WHERE `id` = ?";
					this.jt.update(upSell,num,id);
				}else{
					String upSell = "UPDATE `new_goods_info` SET `sell` = `sell` + ? WHERE `id` = ?";
					this.jt.update(upSell,num,id);
				}
				
			}
		}
	}
}
