package cn._51app.dao.diy2_0.impl;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Repository;

import cn._51app.dao.BaseDao;
import cn._51app.dao.diy2_0.IZeroGoodDao;

@Repository
public class ZeroDaoImpl extends BaseDao implements IZeroGoodDao{
	@Override
	public List<Map<String, Object>> zeroGoods() {
		return this.jt.queryForList("SELECT `dit`.`pre_url`,`dit`.`origin`,`dit`.`wh_size`,`dit`.`cover_size`,`dit`.`diy25_img` AS `coverImg`,`dtg`.`good_id` AS `goodId`,2 AS `type`,`dtg`.`org_price` AS `orgPrice`,`dtg`.`sort`,`dtg`.`img_url` AS `icoUrl` FROM `diy_today_good` `dtg` INNER JOIN `new_goods_info` `ngi` ON `dtg`.`good_id`=`ngi`.`id` INNER JOIN `diy_info_texture2` `dit` ON `dit`.`make_id`=`ngi`.`id`  WHERE `dtg`.`status`=1 AND `dtg`.`type`=2 AND `dtg`.`source`=2 AND `dit`.`status`=1 AND `dit`.`isdefault`=1 ORDER BY `dtg`.`sort` ASC");
	}
	
	@Override
	public List<Map<String, Object>> workListByPage(String type, int page,Set<String> paraTemp,Integer userId) {
		StringBuilder sql=new StringBuilder();
		sql.append("SELECT NOW() AS `nowtime`,`dwl`.*,`ddu`.`name` AS `userName`,`ddu`.`mobile`,`ddu`.`head_url` AS `headUrl` FROM `diy_works_list` `dwl` INNER JOIN `diy_device_user` `ddu` ON `dwl`.`userId`=`ddu`.`id` WHERE `dwl`.`status`=1 ");
		if("new".equals(type)){
			sql.append("AND `dwl`.`isopen`=2 ");
			sql.append("ORDER BY `dwl`.`sort` ASC,`dwl`.`ctime` DESC ");
			sql.append("LIMIT "+page*10+",10");
			//如果没有
		}else if("hot".equals(type)){
			sql.append("AND `dwl`.`isopen`=2 ");
//			sql.append("AND TIMESTAMPDIFF(DAY,DATE_FORMAT(`dwl`.`ctime`,'%Y-%m-%d'),CURDATE())<=2 ");
			if(paraTemp.isEmpty()){
				return null;
			}
			StringBuilder temp=new StringBuilder();
			for (String friendId : paraTemp) {
				temp.append(friendId+",");
			}
			sql.append("AND `dwl`.`id` IN(");
			sql.append(temp.substring(0, temp.length()-1)+")");
			sql.append("ORDER BY FIELD (`dwl`.`id`,"+temp.substring(0, temp.length()-1)+")");
		}else if("friend".equals(type)){
			if(paraTemp.isEmpty()){
				return null;
			}
			sql.append("AND (`dwl`.`isopen`=2 OR `dwl`.`isopen`=1) ");
			if(userId!=null){
				sql.append("AND `dwl`.`userId`!="+userId+" ");
			}
			sql.append("AND `dwl`.`userId` IN (");
			for (String friendId : paraTemp) {
				sql.append(friendId+",");
			}
			sql.replace(sql.length()-1, sql.length(), ") ");
			sql.append("ORDER BY `dwl`.`ctime` DESC ");
			sql.append("LIMIT "+page*10+",10");
		}
		return this.jt.queryForList(sql.toString());
	}
	
	@Override
	public Map<String, Object> goodsInfo(String id) {
		try {
			return this.jt.queryForMap("SELECT `dwl`.`status`,`dwl`.`isopen`,`ddu`.`name` AS `userName`,`ddu`.`mobile`,`ddu`.`head_url` AS `headUrl`,NOW() AS `nowtime`,`dwl`.`type`,`dwl`.`cutStatus`,`dwl`.`orgPrice`,`dwl`.`id`,`dwl`.`name`,`dwl`.`cont`,`dwl`.`imgurl` AS `imgUrl`,`dwl`.`suffix`,`dwl`.`money`,`dwl`.`userId`,`dwl`.`ctime`,`dwl`.`endtime`,`dwl`.`textureIds`,`dwl`.`textureName` FROM `diy_works_list` `dwl` INNER JOIN `diy_device_user` `ddu` ON `ddu`.`id`=`dwl`.`userId` WHERE `dwl`.`id`=?",new Object[]{id});
		} catch (Exception e) {
			return null;
		}
	}
	
	@Override
	public List<Map<String, Object>> myWorkListByPage(Integer userId, String type,int page) {
		StringBuilder sql=new StringBuilder();
		sql.append("SELECT NOW() AS `nowtime`,`dwl`.`cutStatus`,`dwl`.`orgPrice`,`dwl`.`textureName`,`dwl`.`textureIds`,`dwl`.`id`,`dwl`.`name`,`dwl`.`cont`,`dwl`.`isopen`,`dwl`.`ctime`,`dwl`.`endtime`,`dwl`.`imgurl` AS `imgUrl`,`dwl`.`suffix`,`dwl`.`goodsId`,`dwl`.`money`,`ddu`.`name` AS `userName`,`ddu`.`mobile`,`dwl`.`type` FROM `diy_works_list` `dwl` INNER JOIN `diy_device_user` `ddu` ON `dwl`.`userId`=`ddu`.`id` WHERE `dwl`.`status`=1  ");
		if("normal".equals(type)){
			sql.append("AND `dwl`.`type`!=3 ");
		}else if("zero".equals(type)){
			sql.append("AND `dwl`.`type`=3 ");
		}
		sql.append("AND `dwl`.`userId`=? ");
		sql.append("ORDER BY `ctime` DESC LIMIT ?,20");
		return this.jt.queryForList(sql.toString(),new Object[]{userId,page*20});
	}
	
	@Override
	public int delWorkList(int id, Integer userId) {
		return this.jt.update("UPDATE `diy_works_list` SET `status`=0 WHERE `id`=? AND `userId`=?",new Object[]{id,userId});
	}
	
	@Override
	public int cutPrice(int id, double randMoney) {
		return this.jt.update("UPDATE `diy_works_list` SET `money`=(CASE WHEN `money`>=? THEN `money`-? ELSE 0 END) WHERE `id`=? AND `status`=1 AND `type`=3",new Object[]{randMoney,randMoney,id});
	}
	
	@Override
	public Map<String, Object> personInfo(Integer userId) {
		return this.jt.queryForMap("SELECT `id`,`name`,`head_url` AS `headUrl`,NOW() AS `ctime`  FROM `diy_device_user` WHERE `id`=?",new Object[]{userId});
	}
	
	@Override
	public boolean checkCutTime(int id) {
		try {
			this.jt.queryForObject("SELECT `id` FROM `diy_works_list` WHERE `endtime`>=NOW() AND `id`=?",new Object[]{id}, Integer.class);
			return true;
		} catch (Exception e) {
		}
		return false;
	}
	
	@Override
	public List<Map<String, Object>> friends(Set<String> ids) {
		if(ids.isEmpty()){
			return null;
		}
		StringBuilder temp=new StringBuilder();
		for (String friendId : ids) {
			temp.append(friendId+",");
		}
		return this.jt.queryForList("SELECT `id`,`name`,`head_url` AS `headUrl` FROM `diy_device_user` WHERE `id` IN ("+temp.substring(0, temp.length()-1)+")");
	}
	
	@Override
	public boolean checkWork(int id, Integer userId) {
		try {
			this.jt.queryForObject("SELECT `id` FROM `diy_works_list` WHERE `id`=? AND `userId`=? LIMIT 1", new Object[]{id,userId},Integer.class);
			return true;
		} catch (Exception e) {
		}
		return false;
	}
	
	@Override
	public boolean checkMobile(Integer userId, String mobile) {
		try {
			this.jt.queryForObject("SELECT `id` FROM `diy_device_user` WHERE `id`=? AND `mobile`=?",new Object[]{userId,mobile}, Integer.class);
			return true;
		} catch (Exception e) {
		}
		return false;
	}
	
	@Override
	public double getOrgPrice(String goodId) {
		return this.jt.queryForObject("SELECT `org_price` FROM `diy_today_good` WHERE `source`=2 AND `type`=2 AND `good_id`=? AND `status`=1 LIMIT 1",new Object[]{goodId}, Double.class);
	}
	
	@Override
	public Map<String, Object> getPersonInfo(String id) {
		return this.jt.queryForMap("SELECT `id`,`name`,`head_url` AS `headUrl` FROM `diy_device_user` WHERE `id`=?",new Object[]{id});
	}
	
	@Override
	public boolean endCutprice(String id) {
		int result=this.jt.update("UPDATE `diy_works_list` SET `cutStatus`=2,endtime=NOW() WHERE id=?",new Object[]{id});
		if(result>0){
			return true;
		}
		return false;
	}
	
	@Override
	public Map<String, Object> getWorkInfo(String id, String userId) {
		try {
			return this.jt.queryForMap("SELECT `id`,`orgPrice`,`money`,`type` FROM `diy_works_list` WHERE `id`=? AND `userId`=? AND `status`=1 LIMIT 1",new Object[]{id,userId});
		} catch (Exception e) {
			return null;
		}
	}
	
	@Override
	public boolean editWorkList(String id, String userId, String type) {
		int result=this.jt.update("UPDATE `diy_works_list` SET  `isopen`=? WHERE `id`=? AND `userId`=?",new Object[]{type,id,userId});
		if(result>0){
			return true;
		}
		return false;
	}
	
	@Override
	public List<Map<String, Object>> queryEndList() {
		return this.jt.queryForList("SELECT `id` FROM `diy_works_list` WHERE `type`=3 AND `cutStatus`=1 AND `endtime`<=NOW()");
	}
	
	@Override
	public void endCutPriceStatus(String ids) {
		this.jt.update("UPDATE `diy_works_list` SET `cutStatus`=2 WHERE `id` IN "+ids);
	}
	
	@Override
	public Map<String, Object> userWorkById(String id) { 
		return this.jt.queryForMap("SELECT `dwl`.`id`,`dwl`.`imgurl`,`dwl`.`suffix`,`dwl`.`orgPrice`,`dwl`.`money`,`dwl`.`name` AS `workName`,`dwl`.`cont`,`ddu`.`name`,`ddu`.`head_url` AS `headUrl`,`ddu`.`mobile` FROM `diy_works_list` `dwl` INNER JOIN `diy_device_user` `ddu` ON `dwl`.`userId`=`ddu`.`id` WHERE `dwl`.`id`=?",new Object[]{id});
	}
	
	@Override
	public void updateCutStatus(String workId, String status) {
		this.jt.update("UPDATE `diy_works_list` SET `cutStatus`=? WHERE `id`=?",new Object[]{status,workId});
	}
	
	@Override
	public Integer getFriendIdByWorkId(int id) {
		return this.jt.queryForObject("SELECT `userId` FROM `diy_works_list`  WHERE `id`=?",new Object[]{id}, Integer.class);
	}
}
