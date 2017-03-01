package cn._51app.dao.diy2_0.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import cn._51app.dao.BaseDao;
import cn._51app.dao.diy2_0.INewPrefDao;

@Repository
public class NewPrefDao extends BaseDao implements INewPrefDao {

	@Override
	public List<Map<String,Object>> getNewPrefGoods() {
		String sql = "select id,`diy25_img` as iconUrl, `name`,`sell` from new_goods_info WHERE isNewPref = 1 AND `status` = 1";
		return super.jt.queryForList(sql);
	}

	@Override
	public boolean isGetCoupon(Integer userId){
		try {
			String sql = "select count(c.id) from diy_coupon c Left JOIN  diy_coupon_user cu ON cu.coupon_id = c.id where c.`type` = 9  and cu.userId=?";
			return super.jt.queryForObject(sql, new Object[]{userId},Integer.class)>0;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	@Transactional(rollbackFor=Exception.class)
	public int getCouponByUser(Integer userId) throws Exception {
		return 0;
	}

	@Override
	public boolean isLogin(Integer userId){
		try {
			String sql="SELECT id,openid,mobile,qqid FROM diy_device_user WHERE id=?";
			List<Map<String,Object>> list=jt.queryForList(sql,userId);
			
			if (list != null && list.size()>0) {
				if(list.get(0).get("openid")!=null && !list.get(0).get("openid").toString().equals("")){
					return true;
				}
				if (list.get(0).get("qqid")!=null && !list.get(0).get("qqid").toString().equals("")) {
					return true;
				}
				
				if (list.get(0).get("mobile")!=null && !list.get(0).get("mobile").toString().equals("0")) {
					return true;
				}
			}
		} catch (Exception e) {}
		
		return false;
	}

	@Override
	public List<Map<String, Object>> getNewPrefCoupon() {
		String sql = "SELECT `dc`.`id`,`dc`.`valid`,`dc`.`title`,`dc`.`store_id`,`dc`.`about`,`dc`.`des_price` AS desPrice,`dc`.`org_price` AS orgPrice,`dc`.`count`,`dc`.`img`,`dsu`.`name` AS `storeName` FROM `diy_coupon` `dc` LEFT JOIN `diy_sys_user` `dsu` ON `dsu`.`id`=`dc`.`store_id` WHERE type=9 AND status=1 AND valid>=CURDATE()"; 
		return this.jt.queryForList(sql);
	}
	
	@Override
	public int addCouponForId(Map<String, Object> paramMap) throws Exception{
		String sql = "INSERT INTO `diy_coupon_user` (`userId`,`valid`,`coupon_id`,`creatime`,`status`,`org_price`,`des_price`) VALUES(:userId,DATE_ADD(CURDATE(), INTERVAL 7 DAY),:id,CURDATE(),1,:orgPrice,:desPrice)"; 
		return this.npjt.update(sql, paramMap);
	}


}
