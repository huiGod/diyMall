package cn._51app.dao.diy2_0.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import cn._51app.dao.BaseDao;
import cn._51app.dao.diy2_0.IIdexDao;

@Repository
public class IIdexDaoImpl extends BaseDao implements IIdexDao{

	@Override
	public List<Map<String, Object>> getHomeButton(int version) {
		return this.jt.queryForList("SELECT * FROM `diy_home_button` WHERE `status`=1 AND version=? ORDER BY `sort` ASC",version);
	}
	
	@Override
	public List<Map<String, Object>> getBannerBySource(int source, Integer bannerId,int version) {
		return this.jt.queryForList("SELECT `db`.`id`,`db`.`img_url` AS `imgUrl`,`db`.`type`,`db`.`about`,`dns`.title,`dns`.`cont` FROM `diy_banner` `db` LEFT JOIN `diy_new_special` `dns` ON `db`.`des_id`=`dns`.`id`  WHERE `db`.`status`=1 AND `db`.`source`=? AND `db`.`des_id`=? AND `db`.`version`=? ORDER BY `db`.`sort` ASC ",new Object[]{source,bannerId,version});
	}
	
	@Override
	public List<Map<String, Object>> getTodayGoods() {
		List<Map<String, Object>> todayList= this.jt.queryForList("SELECT `dtg`.`img_url` AS `icoUrl`,`dtg`.`good_id` AS `goodId`,`dtg`.`type`,`dtg`.`org_price` AS `orgPrice`,`dtg`.`now_price` AS `nowPrice`,`dtg`.`sort`,`dgi`.`ispostage`,`dgi`.`user_id` AS `companyId`,`da`.`type` AS `act` FROM `diy_today_good` `dtg` INNER JOIN `diy_goods_info2` `dgi` ON `dtg`.`good_id`=`dgi`.`id` LEFT JOIN `diy_activity` `da` ON `da`.`id`=`dgi`.`activity_id` WHERE `dtg`.`source`=1 AND `dtg`.`status`=1 AND `dtg`.`type`=1 AND `dtg`.`source`=1 AND `dtg`.`show_date`=CURDATE() UNION ALL SELECT `dtg`.`img_url` AS `icoUrl`,`dtg`.`good_id` AS `goodId`,`dtg`.`type`,`dtg`.`org_price` AS `orgPrice`,`dtg`.`now_price` AS `nowPrice`,`dtg`.`sort`,`dgi`.`ispostage`,`dgi`.`user_id` AS `companyId`,`da`.`type` AS `act` FROM `diy_today_good` `dtg` INNER JOIN `diy_goods_info2` `dgi` ON `dtg`.`good_id`=`dgi`.`id` LEFT JOIN `diy_activity` `da` ON `da`.`id`=`dgi`.`activity_id` WHERE `dtg`.`source`=1 AND `dtg`.`status`=1 AND `dtg`.`type`=4 AND `dtg`.`source`=1 AND `dtg`.`show_date`=CURDATE() UNION ALL SELECT `dtg`.`img_url` AS `icoUrl`,`dtg`.`good_id` AS `goodId`,`ngi`.`type`,`dtg`.`org_price` AS `orgPrice`,`dtg`.`now_price` AS `nowPrice`,`dtg`.`sort`,`ngi`.`ispostage`,`ngi`.`user_id` AS `companyId`,`da`.`type` AS `act` FROM `diy_today_good` `dtg` INNER JOIN `new_goods_info` `ngi` ON `dtg`.`good_id`=`ngi`.`id` LEFT JOIN `diy_activity` `da` ON `da`.`id`=`ngi`.`activity_id` WHERE `dtg`.`status`=1 AND `dtg`.`type`=2 AND `dtg`.`source`=1 AND `dtg`.`show_date`=CURDATE() ORDER BY `sort` ASC");
		if(todayList.size()<=0 || todayList==null){
			todayList= this.jt.queryForList("SELECT `dtg`.`good_id` AS `goodId`,`dtg`.`type`,`dtg`.`org_price` AS `orgPrice`,`dtg`.`now_price` AS `nowPrice`,`dtg`.`sort`,`dtg`.`show_date` AS `showDate`,`dtg`.`img_url` AS `icoUrl`,`dgi`.`ispostage`,`dgi`.`user_id` AS `companyId`,`da`.`type` AS `act` FROM `diy_today_good` `dtg` INNER JOIN `diy_goods_info2` `dgi` ON `dtg`.`good_id`=`dgi`.`id` LEFT JOIN `diy_activity` `da` ON `da`.`id`=`dgi`.`activity_id` WHERE `dtg`.`status`=1 AND `dtg`.`source`=1 AND `dtg`.`type`=1 UNION ALL SELECT `dtg`.`good_id` AS `goodId`,`dtg`.`type`,`dtg`.`org_price` AS `orgPrice`,`dtg`.`now_price` AS `nowPrice`,`dtg`.`sort`,`dtg`.`show_date` AS `showDate`,`dtg`.`img_url` AS `icoUrl`,`dgi`.`ispostage`,`dgi`.`user_id` AS `companyId`,`da`.`type` AS `act` FROM `diy_today_good` `dtg` INNER JOIN `diy_goods_info2` `dgi` ON `dtg`.`good_id`=`dgi`.`id` LEFT JOIN `diy_activity` `da` ON `da`.`id`=`dgi`.`activity_id` WHERE `dtg`.`status`=1 AND `dtg`.`source`=1 AND `dtg`.`type`=4 UNION ALL SELECT `dtg`.`good_id` AS `goodId`,`ngi`.`type`,`dtg`.`org_price` AS `orgPrice`,`dtg`.`now_price` AS `nowPrice`,`dtg`.`sort`,`dtg`.`show_date` AS `showDate`,`dtg`.`img_url` AS `icoUrl`,`ngi`.`ispostage`,`ngi`.`user_id` AS `companyId`,`da`.`type` AS `act` FROM `diy_today_good` `dtg` INNER JOIN `new_goods_info` `ngi` ON `dtg`.`good_id`=`ngi`.`id` LEFT JOIN `diy_activity` `da` ON `da`.`id`=`ngi`.`activity_id` WHERE `dtg`.`status`=1 AND `dtg`.`source`=1 AND `dtg`.`type`=2 ORDER BY `showDate` DESC LIMIT 3");
		}
		return todayList;

	}
	
	@Override
	public List<Map<String, Object>> getHomeSpecials() {
		return this.jt.queryForList("SELECT `id`,`title`,`img_url` AS `imgUrl`,`type`,`about` FROM `diy_new_special` WHERE `status`=1 AND `place`=1 ORDER BY `sort` ASC");
	}
	
	@Override
	public List<Map<String, Object>> getRecommendGoods(int page) {
		return this.jt.queryForList("SELECT 1 AS `type`,`dgi`.`id`,`dgi`.`name`,`dgi`.`diy25_img` AS `icoUrl`,`dit`.`now_price` AS `nowPrice`,`dgi`.`home_recommend` AS `homeRecommend`,`dgi`.`sell`,`dgi`.`ispostage` FROM `diy_goods_info2` `dgi` INNER JOIN `diy_info_texture2` `dit` ON `dgi`.`id`=`dit`.`info_id` WHERE `dgi`.`state`=1 AND `dit`.`isdefault`=1 AND `dit`.`status`=1 UNION ALL SELECT 2 AS `type`,`ngi`.`id`,`ngi`.`name`,`ngi`.`diy25_img` AS `icoUrl`,`dit`.`now_price` AS `nowPrice`,`ngi`.`home_recommend` AS `homeRecommend`,`ngi`.`sell`,`ngi`.`ispostage` FROM `new_goods_info` `ngi` INNER JOIN `diy_info_texture2` `dit` ON `ngi`.`id`=`dit`.`make_id` WHERE `ngi`.`status`=1 AND `dit`.`isdefault`=1 AND `dit`.`status`=1 ORDER BY `homeRecommend` ASC LIMIT ?,10",new Object[]{page*10} );
	}
	
	@Override
	public List<Map<String, Object>> goodRecommendBypage(int page, String storeId) {
		return this.jt.queryForList("SELECT 4 AS `type`,`dgi`.`id`,`dgi`.`name`,`dgi`.`diy25_img` AS `icoUrl`,`dit`.`now_price` AS `nowPrice`,`dgi`.`home_recommend` AS `homeRecommend`,`dgi`.`sell`,`dgi`.`ispostage`,`dgi`.`user_id` AS `companyId`,`da`.`type` AS `act` FROM `diy_goods_info2` `dgi` INNER JOIN `diy_info_texture2` `dit` ON `dgi`.`id`=`dit`.`info_id` LEFT JOIN `diy_activity` `da` ON `da`.`id`=`dgi`.`activity_id` WHERE `dgi`.`state`=1 AND `dit`.`isdefault`=1 AND `dit`.`status`=1  AND `dgi`.`user_id`=? AND (`dgi`.`newgoodid` IS NOT NULL OR `dgi`.`newgoodid`!='') UNION ALL SELECT 2 AS `type`,`ngi`.`id`,`ngi`.`name`,`ngi`.`diy25_img` AS `icoUrl`,`dit`.`now_price` AS `nowPrice`,`ngi`.`home_recommend` AS `homeRecommend`,`ngi`.`sell`,`ngi`.`ispostage`,`ngi`.`user_id` AS `companyId`,`da`.`type` AS `act` FROM `new_goods_info` `ngi` INNER JOIN `diy_info_texture2` `dit` ON `ngi`.`id`=`dit`.`make_id` LEFT JOIN `diy_activity` `da` ON `da`.`id`=`ngi`.`activity_id` WHERE `ngi`.`status`=1 AND `dit`.`isdefault`=1 AND `dit`.`status`=1 AND `ngi`.`user_id`=? AND `ngi`.`type`!=4  UNION ALL SELECT 1 AS `type`,`dgi`.`id`,`dgi`.`name`,`dgi`.`diy25_img` AS `icoUrl`,`dit`.`now_price` AS `nowPrice`,`dgi`.`home_recommend` AS `homeRecommend`,`dgi`.`sell`,`dgi`.`ispostage`,`dgi`.`user_id` AS `companyId`,`da`.`type` AS `act` FROM `diy_goods_info2` `dgi` INNER JOIN `diy_info_texture2` `dit` ON `dgi`.`id`=`dit`.`info_id` LEFT JOIN `diy_activity` `da` ON `da`.`id`=`dgi`.`activity_id` WHERE `dgi`.`state`=1 AND `dit`.`isdefault`=1 AND `dit`.`status`=1 AND `dgi`.`isBoutique`=1 AND `dgi`.`user_id`=? AND (`dgi`.`newgoodid` IS NULL OR `dgi`.`newgoodid`='') ORDER BY `homeRecommend`,`id` ASC LIMIT ?,10",new Object[]{storeId,storeId,storeId,page*10} );
	}
	
	@Override
	public List<Map<String, Object>> getGoodsByTopNavIdByPage(int id,int page) {
		return this.jt.queryForList("SELECT `dgi`.`diy25_img` AS icoUrl,`dhc`.`type`,`dgi`.`id`,`dgi`.`name`,`dit`.`now_price` AS `nowPrice`,`dgi`.`sell`,`dhc`.`sort`,`dgi`.`ispostage`,`da`.`type` AS `act`,`dsu`.`id` AS `companyId` FROM `diy_goods_info2` `dgi` INNER JOIN `diy_info_texture2` `dit` ON `dgi`.`id`=`dit`.`info_id` INNER JOIN `diy_home_classify` `dhc` ON `dhc`.`good_id`=`dgi`.`id` LEFT JOIN `diy_activity` `da` ON `da`.`id`=`dgi`.`activity_id` LEFT JOIN `diy_sys_user` `dsu` ON `dsu`.`id`=`dgi`.`user_id` WHERE `dgi`.`state`=1 AND `dit`.`isdefault`=1 AND `dit`.`status`=1 AND `dhc`.`diyhomebutton_id`=? AND `dhc`.`status`=1 AND `dhc`.`type`=1 UNION ALL SELECT `ngi`.`diy25_img` AS icoUrl,`dhc`.`type`,`ngi`.`id`,`ngi`.`name`,`dit`.`now_price` AS `nowPrice`,`ngi`.`sell`,`dhc`.`sort`,`ngi`.`ispostage`,`da`.`type` AS `act`,`dsu`.`id` AS companyId FROM `new_goods_info` `ngi` INNER JOIN `diy_info_texture2` `dit` ON `ngi`.`id`=`dit`.`make_id` INNER JOIN `diy_home_classify` `dhc` ON `dhc`.`good_id`=`ngi`.`id` LEFT JOIN `diy_activity` `da` ON `da`.`id`=`ngi`.`activity_id` LEFT JOIN `diy_sys_user` `dsu` ON `dsu`.`id`=`ngi`.`user_id` WHERE `ngi`.`status`=1 AND `dit`.`isdefault`=1 AND `dhc`.`diyhomebutton_id`=? AND `dhc`.`status`=1 AND `dhc`.`type`=2 AND `dit`.`status`=1 UNION ALL SELECT `dgi`.`diy25_img` AS icoUrl,`dhc`.`type`,`dgi`.`id`,`dgi`.`name`,`dit`.`now_price` AS `nowPrice`,`dgi`.`sell`,`dhc`.`sort`,`dgi`.`ispostage`,`da`.`type` AS `act`,`dsu`.`id` AS companyId FROM `diy_goods_info2` `dgi` INNER JOIN `diy_info_texture2` `dit` ON `dgi`.`id`=`dit`.`info_id` INNER JOIN `diy_home_classify` `dhc` ON `dhc`.`good_id`=`dgi`.`id` LEFT JOIN `diy_activity` `da` ON `da`.`id`=`dgi`.`activity_id` LEFT JOIN `diy_sys_user` `dsu` ON `dsu`.`id`=`dgi`.`user_id` WHERE `dgi`.`state`=1 AND `dit`.`isdefault`=1 AND `dhc`.`diyhomebutton_id`=? AND `dhc`.`status`=1 AND `dhc`.`type`=4 AND `dit`.`status`=1 ORDER BY `sort`,id ASC LIMIT ?,10",new Object[]{id,id,id,page*10} );
//		return this.jt.queryForList("SELECT `ngi`.`diy25_img` AS icoUrl,`dhc`.`type`,`ngi`.`id`,`ngi`.`name`,`dit`.`now_price` AS `nowPrice`,`ngi`.`sell`,`dhc`.`sort`,`ngi`.`ispostage`,`da`.`type` AS `act`,`dsu`.`id` AS companyId FROM `new_goods_info` `ngi` INNER JOIN `diy_info_texture2` `dit` ON `ngi`.`id`=`dit`.`make_id` INNER JOIN `diy_home_classify` `dhc` ON `dhc`.`good_id`=`ngi`.`id` LEFT JOIN `diy_activity` `da` ON `da`.`id`=`ngi`.`activity_id` LEFT JOIN `diy_sys_user` `dsu` ON `dsu`.`id`=`ngi`.`user_id` WHERE `ngi`.`status`=1 AND `dit`.`isdefault`=1 AND `dhc`.`diyhomebutton_id`=? AND `dhc`.`status`=1 AND `dhc`.`type`=2 AND `dit`.`status`=1 UNION ALL SELECT `dgi`.`diy25_img` AS icoUrl,`dhc`.`type`,`dgi`.`id`,`dgi`.`name`,`dit`.`now_price` AS `nowPrice`,`dgi`.`sell`,`dhc`.`sort`,`dgi`.`ispostage`,`da`.`type` AS `act`,`dsu`.`id` AS companyId FROM `diy_goods_info2` `dgi` INNER JOIN `diy_info_texture2` `dit` ON `dgi`.`id`=`dit`.`info_id` INNER JOIN `diy_home_classify` `dhc` ON `dhc`.`good_id`=`dgi`.`id` LEFT JOIN `diy_activity` `da` ON `da`.`id`=`dgi`.`activity_id` LEFT JOIN `diy_sys_user` `dsu` ON `dsu`.`id`=`dgi`.`user_id` WHERE `dgi`.`state`=1 AND `dit`.`isdefault`=1 AND `dhc`.`diyhomebutton_id`=? AND `dhc`.`status`=1 AND `dhc`.`type`=4 AND `dit`.`status`=1 ORDER BY `sort`,id ASC LIMIT ?,10",new Object[]{id,id,page*10} );
	}
	
	@Override
	public List<Map<String, Object>> getSpecialOfGoods(int id,int type){
		if(type==3){//小图片专题
			return this.jt.queryForList("SELECT `dsg`.`type`,`dgi`.`id`,`dgi`.`name`,`dgi`.`diy25_img` AS `icoUrl`,`dit`.`now_price` AS `nowPrice`,`dgi`.`sell`,`dsg`.`sort`,`dgi`.`ispostage`,`da`.`type` AS `act`,`dgi`.`user_id` AS `companyId` FROM `diy_special_good` `dsg` INNER JOIN `diy_goods_info2` `dgi` ON `dsg`.`good_id`=`dgi`.`id` INNER JOIN `diy_info_texture2` `dit` ON `dgi`.`id`=`dit`.`info_id` LEFT JOIN `diy_activity` `da` ON `da`.`id`=`dgi`.`activity_id` WHERE `dgi`.`state`=1 AND `dit`.`isdefault`=1 AND `dsg`.`special_id`=? AND `dsg`.`status`=1 AND `dsg`.`type`=1 AND `dit`.`status`=1 UNION ALL SELECT `dsg`.`type`,`dgi`.`id`,`dgi`.`name`,`dgi`.`diy25_img` AS `icoUrl`,`dit`.`now_price` AS `nowPrice`,`dgi`.`sell`,`dsg`.`sort`,`dgi`.`ispostage`,`da`.`type` AS `act`,`dgi`.`user_id` AS `companyId` FROM `diy_special_good` `dsg` INNER JOIN `diy_goods_info2` `dgi` ON `dsg`.`good_id`=`dgi`.`id` INNER JOIN `diy_info_texture2` `dit` ON `dgi`.`id`=`dit`.`info_id` LEFT JOIN `diy_activity` `da` ON `da`.`id`=`dgi`.`activity_id` WHERE `dgi`.`state`=1 AND `dit`.`isdefault`=1 AND `dsg`.`special_id`=? AND `dsg`.`status`=1 AND `dsg`.`type`=4 AND `dit`.`status`=1 UNION ALL SELECT `dsg`.`type`,`ngi`.`id`,`ngi`.`name`,`ngi`.`diy25_img` AS `icoUrl`,`dit`.`now_price` AS `nowPrice`,`ngi`.`sell`,`dsg`.`sort`,`ngi`.`ispostage`,`da`.`type` AS `act`,`ngi`.`user_id` AS `companyId` FROM `diy_special_good` `dsg` INNER JOIN `new_goods_info` `ngi` ON `dsg`.`good_id`=`ngi`.`id` INNER JOIN `diy_info_texture2` `dit` ON `ngi`.`id`=`dit`.`make_id` LEFT JOIN `diy_activity` `da` ON `da`.`id`=`ngi`.`activity_id` WHERE `ngi`.`status`=1 AND `dit`.`isdefault`=1 AND `dsg`.`special_id`=? AND `dsg`.`status`=1 AND `dsg`.`type`=2 AND `dit`.`status`=1 ORDER BY `sort`",new Object[]{id,id,id});
		}else if(type==4){
			return this.jt.queryForList("SELECT `dsg`.`type`,`dgi`.`id`,`dgi`.`name`,`dgi`.`diy25_img` AS `icoUrl`,`dsg`.`content`,`dit`.`now_price` AS `nowPrice`,`dsg`.`sort`,`dgi`.`ispostage`,`da`.`type` AS `act`,`dgi`.`user_id` AS `companyId` FROM `diy_special_good` `dsg` INNER JOIN `diy_goods_info2` `dgi` ON `dsg`.`good_id`=`dgi`.`id` INNER JOIN `diy_info_texture2` `dit` ON `dgi`.`id`=`dit`.`info_id` LEFT JOIN `diy_activity` `da` ON `da`.`id`=`dgi`.`activity_id` WHERE `dgi`.`state`=1 AND `dit`.`isdefault`=1 AND `dsg`.`special_id`=? AND `dsg`.`status`=1 AND `dsg`.`type`=1 AND `dit`.`status`=1 UNION ALL SELECT `dsg`.`type`,`dgi`.`id`,`dgi`.`name`,`dgi`.`diy25_img` AS `icoUrl`,`dsg`.`content`,`dit`.`now_price` AS `nowPrice`,`dsg`.`sort`,`dgi`.`ispostage`,`da`.`type` AS `act`,`dgi`.`user_id` AS `companyId` FROM `diy_special_good` `dsg` INNER JOIN `diy_goods_info2` `dgi` ON `dsg`.`good_id`=`dgi`.`id` INNER JOIN `diy_info_texture2` `dit` ON `dgi`.`id`=`dit`.`info_id` LEFT JOIN `diy_activity` `da` ON `da`.`id`=`dgi`.`activity_id` WHERE `dgi`.`state`=1 AND `dit`.`isdefault`=1 AND `dsg`.`special_id`=? AND `dsg`.`status`=1 AND `dsg`.`type`=4 AND `dit`.`status`=1 UNION ALL SELECT `dsg`.`type`,`ngi`.`id`,`ngi`.`name`,`ngi`.`diy25_img` AS `icoUrl`,`dsg`.`content`,`dit`.`now_price` AS `nowPrice`,`dsg`.`sort`,`ngi`.`ispostage`,`da`.`type` AS `act`,`ngi`.`user_id` AS `companyId` FROM `diy_special_good` `dsg` INNER JOIN `new_goods_info` `ngi` ON `dsg`.`good_id`=`ngi`.`id` INNER JOIN `diy_info_texture2` `dit` ON `ngi`.`id`=`dit`.`make_id` LEFT JOIN `diy_activity` `da` ON `da`.`id`=`ngi`.`activity_id` WHERE `ngi`.`status`=1 AND `dit`.`isdefault`=1 AND `dsg`.`special_id`=? AND `dsg`.`status`=1 AND `dsg`.`type`=2 AND `dit`.`status`=1 ORDER BY `sort`",new Object[]{id,id,id});
		}
		return null;
	}
	
	@Override
	public int updateMobile(String userId, String mobile) {
		try {
			this.jt.queryForObject("SELECT `id` FROM `diy_device_user` WHERE `mobile`=? LIMIT 1",new Object[]{mobile}, Integer.class);
			return 0;
		} catch (Exception e) {
			return this.jt.update("UPDATE `diy_device_user` SET `mobile`=? WHERE `id`=?",new Object[]{mobile,userId});
		}
		
	}

	@Override
	public List<Map<String, Object>> getVerionList() {
		String sql = "SELECT * FROM diy_app_version";
		return this.jt.queryForList(sql);
	}

	@Override
	public List<Map<String, Object>> handpick(int module) {
		String sql="select id,imgUrl,param1,param2,param3,`type`,pid,layout,module from diy_home_handpick where module=? ORDER BY sort,id";
		return this.jt.queryForList(sql,module);
	}
	
	@Override
	public List<Map<String, Object>> getSubGoods(Integer id) {
		String sql="select id,imgUrl,param1,param2,param3,`type`,pid,layout from diy_home_handpick where `layout`=1 and `module`=2 AND `hid`=?  ORDER BY sort,id";
		return this.jt.queryForList(sql,id);
	}
}
