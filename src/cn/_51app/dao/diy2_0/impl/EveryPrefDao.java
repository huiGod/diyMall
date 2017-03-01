package cn._51app.dao.diy2_0.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import cn._51app.dao.BaseDao;
import cn._51app.dao.diy2_0.IEveryPrefDao;

@Repository
public class EveryPrefDao extends BaseDao implements IEveryPrefDao {

	@Override
	public Map<String, Object> load() {
		String sql = "SELECT ngi.id,ngi.`name`,ngi.`sell`,ngi.`now_price`,def.imgTop,def.imgTail FROM diy_every_pref def LEFT JOIN new_goods_info ngi ON ngi.id = def.goods_id WHERE def.`status` = 1 LIMIT 1";

		try {
			return super.jt.queryForMap(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<Map<String, Object>> getActivities() {
		String sql = "SELECT `id`,`name`,`content`,`imgUrl`,`url` FROM diy_activity_manager WHERE `status` = 1 ORDER BY `sort` ASC";
		return super.jt.queryForList(sql);
	}

}
