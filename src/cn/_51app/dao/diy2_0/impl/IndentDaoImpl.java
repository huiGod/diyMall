package cn._51app.dao.diy2_0.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import cn._51app.dao.BaseDao;
import cn._51app.dao.diy2_0.IUserWorksDao;
import cn._51app.dao.diy2_0.IndentDao;

@Repository
public class IndentDaoImpl extends BaseDao implements IndentDao {
	
	@Autowired
	private IUserWorksDao iUserWorksDao;
	
	@Override
	public Map<String, Object> createOrder(String resultPath, String infoId, String textureIds, String deviceNumber,
			String app, String orderNo, String sufFormat, String num,boolean customize,String workId) {
		Map<String,Object>result=new HashMap<String,Object>();
		String table="`diy_goods_info2`";
		String inId="`info_id`";
		if(customize){
			table="`new_goods_info`";
			inId="`make_id`";
		}
		try{
			//获取材质
			String textureNames=queryTextureById(textureIds.split("_"));
			//查询价格 和运费
			Map<String, Object> fees=this.jt.queryForMap("SELECT `dgi`.`transportfee`,`dgi`.`postage`,`dgi`.`free_count`,`dg`.`name` AS `sortName`,`dit`.`now_price` AS `price`,`dgi`.`user_id`,`dsu`.`name` AS `companyName`,`dgi`.`goodsType`,`dgi`.`name`,`da`.`type`,`da`.about,`da`.`org_price`,`da`.`des_price`,`da`.`limit_num`,`da`.`count`,`da`.`param1`,`da`.`money`,`da`.`isAbout` FROM "+table+" `dgi` INNER JOIN `diy_info_texture2` `dit` ON `dgi`.`id`=`dit`."+inId+" INNER JOIN `diy_good2` `dg` ON `dgi`.`good_id`=`dg`.`id` INNER JOIN `diy_sys_user` `dsu` ON `dsu`.`id`=`dgi`.`user_id` LEFT JOIN `diy_activity` `da` ON `da`.`id`=`dgi`.`activity_id` WHERE `dit`."+inId+"=? AND `dit`.`texture_ids`=? LIMIT 1",new Object[]{infoId,textureIds});
			double postage=fees.get("postage")==null?0.00:Double.valueOf(fees.get("postage").toString());
			//包邮需要的张/件数量
			int freeCount=fees.get("freeCount")==null?0:Integer.parseInt(fees.get("freeCount").toString());
			int nn=num.equals("")?0:Integer.parseInt(num);
			//计算邮费
			if(resultPath.contains("_")){
				String photoNum[]=resultPath.split("_");
				Integer pN=Integer.parseInt(photoNum[1]);
				result.put("feeNum",photoNum[1]);
				result.put("feePostage", freeCount*nn);
				//包邮
				if(pN>=freeCount){
					postage=0.00;
				}
			}else{
				//商品数量合格包邮
				if(freeCount<=nn){
					postage=0.00;
				}
			}
			
			int index= this.jt.update("INSERT INTO `diy_orders` (`order_no`,`fee_transport`,`fee_total`,`status`,`device_no`,`app`,`info_ids`,`texture_ids`,`user_ids`,`price`,`img_url`,`file_type`,`num`,`texture_names`,`name`,`sort_name`,`activities`,`userwork`) "
					+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",new Object[]{orderNo,postage,((double)fees.get("transportfee")+(double)fees.get("price")*Integer.parseInt(num)),-1,deviceNumber,app,infoId,textureIds,fees.get("user_id"),fees.get("price"),resultPath,sufFormat,num,textureNames,fees.get("name"),fees.get("sortName"),fees.get("type"),workId});
			
			result.put("feeNum",1);
			result.put("feePostage", 1);
			result.put("freeCount", fees.get("free_count"));
			result.put("postage", fees.get("postage"));
			result.put("textureNames", textureNames);
			result.put("name", (String)fees.get("name"));
			result.put("orgPrice", (Double)fees.get("price"));
			result.put("merchant", fees.get("user_id"));
			result.put("type", fees.get("type"));
			result.put("about", fees.get("about"));
			result.put("org_price", fees.get("org_price"));
			result.put("des_price", fees.get("des_price"));
			result.put("limit_num", fees.get("limit_num"));
			result.put("count", fees.get("count"));
			result.put("param1", fees.get("param1"));
			result.put("money", fees.get("money"));
			result.put("isAbout", fees.get("isAbout"));
			result.put("companyName", fees.get("companyName"));
			result.put("user_id", fees.get("user_id"));
			result.put("sortName",fees.get("sortName"));
			result.put("transportfee",postage);
			if(index>0){
				return result;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public Map<String, Object> createOrderForId(String resultPath, String infoId, String textureIds, String deviceNumber,
			String userId, String sufFormat, String num,boolean customize,String workId,String lettering,String modId) {
		Map<String,Object>result=new HashMap<String,Object>();
		String table="`diy_goods_info2`";
		String inId="`info_id`";
		if(customize){
			table="`new_goods_info`";
			inId="`make_id`";
			Map<String,Object>jpInfo=null;
			//验证修复材质（针对刻字商品精品定制公用同个材质不同商品id），定制查不到再去查精品会的，最终保存是使用定制的info
				jpInfo=this.queryJphInfoByInfoId(infoId, textureIds);
				if(jpInfo==null || jpInfo.isEmpty())
					jpInfo=this.queryJphInfoByInfoId2(infoId, textureIds);
				//传的可能是精品，查出并使用定制的商品id
				if(jpInfo!=null)
				infoId=jpInfo.get("make_id")==null?infoId:jpInfo.get("make_id").toString();
		}
		try{
			//获取材质
			String textureNames=queryTextureById(textureIds.split("_"));
			//查询价格 和运费
			Map<String, Object> fees=this.jt.queryForMap("SELECT `dit`.`cover_size`,`dit`.`wh_size`,`dit`.`origin`,`dit`.`pre_url`,`dit`.`diy25_img` AS `coverImg`,`dgi`.`ispostage`,`dsu`.`remotefee`,`dsu`.`enough_money` AS `enoughMoney`,`dsu`.`transportfee`,`dgi`.`ispostage`,`dg`.`name` AS `sortName`,`dit`.`now_price` AS `price`,`dgi`.`user_id`,`dsu`.`name` AS `companyName`,`dgi`.`goodsType`,`dgi`.`name`,`da`.`id` AS `actId`,`da`.`type`,`da`.about,`da`.`org_price`,`da`.`des_price`,`da`.`limit_num`,`da`.`count`,`da`.`param1`,`da`.`money`,`da`.`isAbout` FROM "+table+" `dgi` INNER JOIN `diy_info_texture2` `dit` ON `dgi`.`id`=`dit`."+inId+" INNER JOIN `diy_good2` `dg` ON `dgi`.`good_id`=`dg`.`id` INNER JOIN `diy_sys_user` `dsu` ON `dsu`.`id`=`dgi`.`user_id` LEFT JOIN `diy_activity` `da` ON `da`.`id`=`dgi`.`activity_id` WHERE `dit`."+inId+"=? AND `dit`.`texture_ids`=? LIMIT 1",new Object[]{infoId,textureIds});
			double price=fees.get("price")==null?0.00:Double.valueOf(fees.get("price").toString());
			Integer ispostage=(Integer)fees.get("ispostage");
			double enoughMoney=(Double)fees.get("enoughMoney");
			double transportfee=(Double)fees.get("transportfee");
			double remotefee=(Double)fees.get("remotefee");
			double originPrice=price;
			if(StringUtils.isNotBlank(workId) && !"0".equals(workId)){
				Map<String, Object> workInfo=this.getWorkInfo(workId);
				Integer type=(Integer)workInfo.get("type");
				Integer cutStatus=(Integer)workInfo.get("cutStatus");
				double money=(Double)workInfo.get("money");
				if(3==type){
					num="1";
					//已经购买过  或者在购物车订单中有了
					boolean isShopOrOrder=this.iUserWorksDao.isShopOrOrder(userId,workId);
					if(cutStatus==3 || isShopOrOrder){ 
						workId=null;
					}else{
						price=money;
					}
				}
			}
			//路费运算
			if(ispostage==1){//包邮
				transportfee=0;
			}else{
				double totalFees=Integer.parseInt(num)*price;
				if(totalFees>=enoughMoney){
					transportfee=0;
				}
			}
//			double postage=fees.get("postage")==null?0.00:Double.valueOf(fees.get("postage").toString());
//			//包邮需要的张/件数量
//			int freeCount=fees.get("freeCount")==null?0:Integer.parseInt(fees.get("freeCount").toString());
//			int nn=num.equals("")?0:Integer.parseInt(num);
			String reTextureNames=textureNames;
//			//计算邮费
			if(resultPath.contains("_")){
				int typeid=this.jt.queryForObject("SELECT `info_id` FROM `diy_info_texture2` WHERE `texture_ids`=?",new Object[]{textureIds}, Integer.class);
				String photoNum[]=resultPath.split("_");
				if(photoNum.length>=2){
					if(90==typeid){
						//90冲印
						Integer pN=Integer.parseInt(photoNum[1]);
		//				result.put("feeNum",photoNum[1]);
		//				result.put("feePostage", freeCount*nn);
						price=price*pN;
						reTextureNames=textureNames+"(1份)X"+pN;
						num="1";
					}
					else{
						if(photoNum.length>=2){
						Integer pN=Integer.parseInt(photoNum[1]);
		//				result.put("feeNum",photoNum[1]);
		//				result.put("feePostage", freeCount*nn);
						reTextureNames=textureNames;
					}
				}
				}
				
				//包邮
//				if(pN>=freeCount){
//					postage=0.00;
//				}
//			}else{
//				//商品数量合格包邮
//				if(freeCount<=nn){
//					postage=0.00;
//				}
			}
			
			int index= this.jt.update("INSERT INTO `diy_orders` (`userId`,`fee_transport`,`fee_total`,`status`,`info_ids`,`texture_ids`,`user_ids`,`price`,`img_url`,`file_type`,`num`,`texture_names`,`name`,`sort_name`,`activities`,`userwork`,`order_no`,`paramType`,`param1`) "
					+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",new Object[]{deviceNumber,transportfee,(transportfee+price*Integer.parseInt(num)),-1,infoId,textureIds,fees.get("user_id"),originPrice,resultPath,sufFormat,num,textureNames,fees.get("name"),fees.get("sortName"),fees.get("actId"),workId,userId,StringUtils.isBlank(modId)?0:modId,lettering});
			
//			result.put("feeNum",1);
//			result.put("freeCount", fees.get("free_count"));
//			result.put("postage", fees.get("postage"));
			result.put("textureNames",resultPath.contains("_")?reTextureNames:textureNames);
			result.put("num", num);
			result.put("name", (String)fees.get("name"));
			result.put("actId",fees.get("actId"));
			result.put("orgPrice", price);
			result.put("merchant", fees.get("user_id"));
			result.put("type", fees.get("type"));
			result.put("about", fees.get("about"));
			result.put("org_price", (Double)fees.get("org_price"));
			result.put("des_price", (Double)fees.get("des_price"));
			result.put("limit_num", fees.get("limit_num"));
			result.put("count", fees.get("count"));
			result.put("param1", fees.get("param1"));
			result.put("money", fees.get("money"));
			result.put("isAbout", fees.get("isAbout"));
			result.put("companyName", fees.get("companyName"));
			result.put("user_id", fees.get("user_id"));
			result.put("sortName",fees.get("sortName"));
			result.put("transportfee",transportfee);
			result.put("remotefee", remotefee);
			result.put("enoughMoney", enoughMoney);
			result.put("ispostage", ispostage);
			result.put("cover_size", fees.get("cover_size"));
			result.put("wh_size",fees.get("wh_size") );
			result.put("origin", fees.get("origin"));
			result.put("pre_url", fees.get("pre_url"));
			result.put("coverImg", fees.get("coverImg"));
			if(index>0){
				return result;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public Map<String, Object> getWorkInfo(String workId) {
		return this.jt.queryForMap("SELECT `id`,`cutStatus`,`orgPrice`,`money`,`type` FROM `diy_works_list` WHERE `id`=?",new Object[]{workId});
	}

	@Override
	public String queryTextureById(String[] tempTextureId) {
		StringBuilder sql=new StringBuilder();
		String textureNames="";
		sql.append("SELECT `name` FROM `diy_goods_texture2` WHERE `id`="+tempTextureId[0]);
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
	public String queryPreUrl(String infoId,String textureIds){
		try {
			return this.jt.queryForObject("SELECT `pre_url` FROM `diy_info_texture2` WHERE `info_id`=? AND `texture_ids`=?",new Object[]{infoId,textureIds}, String.class);
		} catch (Exception e) {
			return null;
		}
	}
	
	@Override
	public Map<String, Object> confirmOrderNumAndPay(String orderNo, String deviceNo, String app) {
		try {
			return this.jt.queryForMap("SELECT `do`.`userwork`,`do`.`num`,`do`.`price`,`do`.`activities`,`do`.`info_ids` AS `infoIds`,`do`.`user_ids` AS userIds,`do`.`price`,`do`.`file_type` AS `fileType`,`do`.`pay_type` AS `payType`,DATE_FORMAT(`do`.`creat_time`,'%Y-%m-%d %H:%i:%s') AS `creatTime`,`dt`.`prepay_id` AS `prepayId`,TIMESTAMPDIFF(MINUTE,`dt`.`time`,NOW()) AS `time`,`do`.`fee_total` AS `feeTotal` FROM `diy_orders` `do` LEFT JOIN `diy_trade` `dt` ON `do`.`order_no`=`dt`.`order_no` WHERE `do`.`order_no`=? AND `do`.`device_no`=? AND `app`=? ",new Object[]{orderNo,deviceNo,app});
		} catch (Exception e) {
			return null;
		}
	}
	
	@Override
	public Map<String, Object> confirmOrderNumAndPayForId(String orderNo, String userId) {
		try {
			return this.jt.queryForMap("SELECT `do`.`img_url`,`do`.`userwork`,`do`.`num`,`do`.`price`,`do`.`activities`,`do`.`info_ids` AS `infoIds`,`do`.`user_ids` AS userIds,`do`.`price`,`do`.`file_type` AS `fileType`,`do`.`pay_type` AS `payType`,DATE_FORMAT(`do`.`creat_time`,'%Y-%m-%d %H:%i:%s') AS `creatTime`,`dt`.`prepay_id` AS `prepayId`,TIMESTAMPDIFF(MINUTE,`dt`.`time`,NOW()) AS `time`,`do`.`fee_total` AS `feeTotal` FROM `diy_orders` `do` LEFT JOIN `diy_trade` `dt` ON `do`.`order_no`=`dt`.`order_no` WHERE `do`.`order_no`=? AND `do`.`userId`=?",new Object[]{orderNo,userId});
		} catch (Exception e) {
			return null;
		}
	}
	
	@Override
	public List<Map<String,Object>>getAcitvityList(String activities){
		String sql="SELECT * FROM diy_activity WHERE `type` in ("+activities+")";
		try{
			return super.jt.queryForList(sql);
		}catch(Exception e){
			return null;
		}
	}
	
	@Override
	public Map<String,Object>getTypeMap(String activities){
		String sql="SELECT * FROM diy_activity WHERE `id`=?";
		try{
			return super.jt.queryForMap(sql,activities);
		}catch(Exception e){
			return null;
		}
	}
	
	@Override
	public String getType(String info,String fileType){
		String table="`diy_goods_info2`";
		if(!fileType.equals("xxx")){
			table="`new_goods_info`";
		}
		String sql="SELECT type FROM diy_activity da LEFT JOIN "+table+" dgi ON dgi.activity_id=da.id WHERE dgi.id="+info;
		try{
			return super.jt.queryForObject(sql, String.class);
		}catch(Exception e){
			return "";
		}
	}
	
	@Override
	public List<Map<String,Object>>getActivityByOrderNo(String orderNo,String deviceNo,String app){
		return this.jt.queryForList("SELECT `da`.about,`da`.`type`,`da`.`org_price`,`da`.`des_price`,`da`.`limit_num`,`da`.`count`,`da`.`param1`,`da`.`money`,`da`.`isAbout` FROM `diy_orders` `ds` LEFT JOIN `diy_goods_info2` `dgi` ON `dgi`.`id`=`ds`.`info_id` LEFT JOIN `diy_activity` `da` ON `da`.id=`dgi`.`activity_id` WHERE `ds`.device_no=? AND `ds`.app=? AND `ds`.order_no=? AND `ds`.`status`=1 AND `da`.`type` is not null AND `ds`.file_type='xxx' GROUP BY `da`.`type` UNION ALL SELECT `da`.about,`da`.`type`,`da`.`org_price`,`da`.`des_price`,`da`.`limit_num`,`da`.`count`,`da`.`param1`,`da`.`money`,`da`.`isAbout` FROM `diy_shopcart` `ds` LEFT JOIN `new_goods_info` `ngi` ON `ngi`.`id`=`ds`.`info_id` LEFT JOIN `diy_activity` `da` ON `da`.id=`ngi`.`activity_id` WHERE `ds`.device_no=? AND `ds`.app=? AND `ds`.order_no=? AND `ds`.`status`=1 AND `da`.`type` is not null AND `ds`.file_type!='xxx' GROUP BY `da`.`type`",new Object[]{deviceNo,app,orderNo,deviceNo,app,orderNo});
	}
	
	@Override
	public boolean checkOrderNum(Integer num,String infoid){
		String info=infoid.split("_")[0];
		String fileType=infoid.split("_")[1];
		String table="`diy_goods_info2`";
		//判断是否是定制商品
		if(!fileType.equals("xxx")){
			table="`new_goods_info`";
		}
	try {
			int id=this.jt.queryForObject("SELECT `id` FROM "+table+" WHERE `stock`>=? AND `id`=?",new Object[]{num,info}, Integer.class);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	@Override
	public Map<String, Object> queryOrderInfo(String orderNo, String deviceNo, String app) {
		Map<String, Object> map=new HashMap<>();
		try {
			map=this.jt.queryForMap("SELECT `price`,`fee_transport` AS `transportfee` FROM `diy_orders`  WHERE `device_no`=? AND `app`=? AND `order_no`=? LIMIT 1",new Object[]{deviceNo,app,orderNo});
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return map;
	}
	
	@Override
	public Map<String, Object> queryOrderInfoForId(String orderNo, String userId) {
		Map<String, Object> map=new HashMap<>();
		try {
			map=this.jt.queryForMap("SELECT `price`,`fee_transport` AS `transportfee` FROM `diy_orders`  WHERE `userId`=? AND `order_no`=? LIMIT 1",new Object[]{userId,orderNo});
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return map;
	}
	
	@Override
	public Map<String,Object>queryOrderInfo2(String orderNo, String deviceNo, String app){
		Map<String, Object> map=new HashMap<>();
		try {
			map=this.jt.queryForMap("SELECT `do`.`userwork`,`do`.`shop_no` AS `shopNo`,`do`.`sort_name` AS `sortName`,`do`.`des_privilege` AS `desPrivilege`,`do`.`img_url` AS `imgUrl`,`do`.`user_ids` AS `companyId`,`dsu`.`name` AS `componyName`,`do`.`info_ids` AS `infoIds`,`do`.`price`,`do`.`fee_transport` AS `transportfee`,`do`.`order_no` AS `orderNo`,`do`.`province`,`do`.`area`,`do`.`mobile`,`do`.`pay_type` AS `payType`,`do`.`consignee`,`do`.`fee_total` AS `feeTotal`,`de`.`code`,DATE_FORMAT(`do`.`creat_time`,'%Y-%m-%d %H:%i:%s') AS `creatTime`,`do`.`img_url` AS `imgUrl`,`do`.`file_type` AS `fileType`,`do`.`num`,`do`.`texture_ids` AS `textureIds`,`do`.`texture_names` AS `textureNames`,`do`.`status`,`do`.`express_no` AS `expressNo`,`do`.`name`,`dt`.`prepay_id` AS `prepayId`,`do`.`info_ids` AS `goodsIds` FROM `diy_orders` `do` LEFT JOIN `diy_trade` `dt` ON `do`.`order_no`=`dt`.`order_no` LEFT JOIN `diy_express` `de` ON `de`.`id`=`do`.`express_id` LEFT JOIN `diy_sys_user` `dsu` ON `dsu`.`id`=`do`.`user_ids`  WHERE `do`.`device_no`=? AND `do`.`app`=? AND `do`.`order_no`=? LIMIT 1",new Object[]{deviceNo,app,orderNo});
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return map;
	}
	
	@Override
	public Map<String,Object>queryOrderInfo2ForId(String orderNo, String userId){
		Map<String, Object> map=new HashMap<>();
		try {
			map=this.jt.queryForMap("SELECT `do`.`message`,`do`.`coupon`,`do`.`userwork`,`do`.`shop_no` AS `shopNo`,`do`.`sort_name` AS `sortName`,`do`.`des_privilege` AS `desPrivilege`,`do`.`img_url` AS `imgUrl`,`do`.`user_ids` AS `companyId`,`do`.`info_ids` AS `infoIds`,`do`.`price`,`do`.`fee_transport` AS `transportfee`,`do`.`order_no` AS `orderNo`,`do`.`province`,`do`.`area`,`do`.`mobile`,`do`.`pay_type` AS `payType`,`do`.`consignee`,`do`.`fee_total` AS `feeTotal`,`de`.`code`,DATE_FORMAT(`do`.`creat_time`,'%Y-%m-%d %H:%i:%s') AS `creatTime`,`do`.`img_url` AS `imgUrl`,`do`.`file_type` AS `fileType`,`do`.`num`,`do`.`texture_ids` AS `textureIds`,`do`.`texture_names` AS `textureNames`,`do`.`status`,`do`.`express_no` AS `expressNo`,`do`.`name`,`dt`.`prepay_id` AS `prepayId`,`do`.`info_ids` AS `goodsIds`,`do`.`texture_ids` FROM `diy_orders` `do` LEFT JOIN `diy_trade` `dt` ON `do`.`order_no`=`dt`.`order_no` LEFT JOIN `diy_express` `de` ON `de`.`id`=`do`.`express_id` where `do`.`userId`=? AND `do`.`order_no`=? LIMIT 1",new Object[]{userId,orderNo});
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return map;
	}
	
	@Override
	public String getComponyName(String companyId){
		if(companyId==null){
			return "唯乐购自营店";
		}
		try{
			String userIds[]=companyId.split(",");
			String sql="SELECT name FROM  diy_sys_user WHERE id=?";
			return this.jt.queryForObject(sql,String.class,userIds[0]);
		}catch(Exception e){
			return "唯乐购自营店";
		}
	}
	
	@Override
	public List<Map<String, Object>> queryPrivilege() {
		List<Map<String, Object>> list=this.jt.queryForList("SELECT `about`,`org_price` AS `orgPrice`,`des_price` AS `desPrice` FROM `diy_privilege` WHERE `status`=1 ORDER BY `org_price` DESC");
		return (list==null || list.size()==0)?null:list;
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class)
	public double activeOrder(String orderNo, String addressId, String num, String payId,
			double totalFee, double couponMoney,String couponIds,double orgPrice, double desPrice, String deviceNo, String app,Map<String, Object> addressMap) {
		try {
			//改变订单状态为“未付款”
			int result=this.jt.update("UPDATE `diy_orders` SET `consignee`=?,`province`=?,`area`=?,`mobile`=?,`pay_type`=?,`num`=?,`fee_total`=?,`creat_time`=NOW(),`coupon`=?,`coupon_id`=?,`org_privilege`=?,`des_privilege`=?,`status`=1 WHERE `order_no`=? AND `device_no`=? AND `app`=?",
					new Object[]{addressMap.get("name"),addressMap.get("province"),addressMap.get("area"),addressMap.get("mobile"),payId,num,(totalFee)<0?0:(totalFee),couponMoney,couponIds,orgPrice,desPrice,orderNo,deviceNo,app});
			if(result>0){
				return totalFee;
			} 
		} catch (Exception e) {
			e.printStackTrace();
		}
		throw new RuntimeException();
	}

	@Override
	@Transactional(rollbackFor=Exception.class)
	public double activeOrderForId(String orderNo, String addressId, String num, String payId,
			double totalFee, double couponMoney,String couponIds,double orgPrice, double desPrice, String userId,Map<String, Object> addressMap,String message,double realTransport) {
		try {
			//改变订单状态为“未付款”
			int result=this.jt.update("UPDATE `diy_orders` SET `consignee`=?,`province`=?,`area`=?,`mobile`=?,`pay_type`=?,`num`=?,`fee_total`=?,`creat_time`=NOW(),`coupon`=?,`coupon_id`=?,`org_privilege`=?,`des_privilege`=?,`status`=1,`message`=?,`fee_transport`=? WHERE `order_no`=? AND `userId`=?",
					new Object[]{addressMap.get("name"),addressMap.get("province"),addressMap.get("area"),addressMap.get("mobile"),payId,num,(totalFee)<0?0:(totalFee),couponMoney,couponIds,orgPrice,desPrice,message,realTransport,orderNo,userId});
			if(result>0){
				return totalFee;
			} 
		} catch (Exception e) {
			e.printStackTrace();
		}
		throw new RuntimeException();
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
	public int boundOrderPrepay(String orderNo, String prepayId, String payNo) {
		return this.jt.update("INSERT INTO `diy_trade` (`order_no`,`pay_no`,`prepay_id`,`time`,`type`) VALUES(?,?,?,NOW(),2)",new Object[]{orderNo,payNo,prepayId});
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
	public int updateShopOrderNumForId(String userId, String flag,int num) {
		if("order".equalsIgnoreCase(flag)){
			return this.jt.update("UPDATE `diy_device_user` SET `order_num`=`order_num`+? WHERE `id`=? AND `status`=1  LIMIT 1",new Object[]{num,userId});
		}else if("shop".equalsIgnoreCase(flag)){
			return this.jt.update("UPDATE `diy_device_user` SET `shop_num`=`shop_num`+? WHERE `id`=? AND `status`=1  LIMIT 1",new Object[]{num,userId});
		}
		return 0;
	}
	
	@Override
	public int updateShopOrderNumForId2(String userId, String flag,int num) {
		if("order".equalsIgnoreCase(flag)){
			return this.jt.update("UPDATE `diy_device_user` SET `order_num`=? WHERE `id`=? AND `status`=1  LIMIT 1",new Object[]{num,userId});
		}else if("shop".equalsIgnoreCase(flag)){
			return this.jt.update("UPDATE `diy_device_user` SET `shop_num`=? WHERE `id`=? AND `status`=1  LIMIT 1",new Object[]{num,userId});
		}
		return 0;
	}
	
	@Override
	public Map<String,Object> getUserCoupon(String deviceNo,String app,String couponId){
		//使优惠券失效
		int result=this.jt.update("UPDATE `diy_coupon_user` `dcu` INNER JOIN `diy_coupon` `dc` ON `dcu`.`coupon_id`=`dc`.`id` SET `dcu`.`status`=0 WHERE `dcu`.`id`=? AND `dcu`.`deviceNo`=? AND `dcu`.`app`=?",new Object[]{couponId,deviceNo,app});
		if(result>0){
			String sql="SELECT `dcu`.`org_price`,`dcu`.`des_price` FROM diy_coupon_user `dcu` WHERE `dcu`.`coupon_id`=? AND `dcu`.`deviceNo`=? & `dcu`.`app`=? LIMIT 1";
			return this.jt.queryForMap(sql,couponId,deviceNo,app);
		}
		return null;
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class)
	public Map<String,Object> getUserCouponForId(String userId,String couponId){
		//使优惠券失效
		int result=this.jt.update("UPDATE `diy_coupon_user` `dcu` INNER JOIN `diy_coupon` `dc` ON `dcu`.`coupon_id`=`dc`.`id` SET `dcu`.`status`=0 WHERE `dcu`.`id`=? AND `dcu`.`userId`=?",new Object[]{couponId,userId});
		if(result>0){
			String sql="SELECT `dcu`.`org_price` AS orgPrice,`dcu`.`des_price` AS desPrice FROM diy_coupon_user `dcu` WHERE `dcu`.id=? AND `dcu`.userId=? LIMIT 1";
			return this.jt.queryForMap(sql,Integer.parseInt(couponId),Integer.parseInt(userId));
		}
		return null;
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
	public int deleteShopForId(String userId, String shopNo) {
		String sql="UPDATE `diy_shopcart` SET `status`=0,`num`=0 WHERE `userId`=? AND `status`!=0 ";
		if(!"('-1')".equals(shopNo)){
			sql+=" AND `shop_no` IN "+shopNo;
		}
		return this.jt.update(sql,new Object[]{userId});
	}
	
	@Override
	public int deleteShopForIdReal(String userId, String shopNo) {
		String sql="UPDATE `diy_shopcart` SET `status`=0,`num`=0 WHERE `userId`=? AND `status`!=0 ";
		if(!"('-1')".equals(shopNo)){
			sql+=" AND `shop_no` IN "+shopNo;
		}
		int result=this.jt.update(sql,new Object[]{userId});
		if(result==0){
			return -1;
		}
		String sql2="SELECT COUNT(id) FROM diy_shopcart WHERE userId=? AND `status`=1"; 
		return this.jt.queryForObject(sql2,Integer.class,userId)-result;
	}
	
	/**
	 * 判断是否是定制商品
	 * @param deviceNo
	 * @param app
	 * @param shopNo
	 * @return
	 */
	private boolean isCustomize(String deviceNo, String app, String orderNo){
		String sql="SELECT file_type FROM diy_orders WHERE device_no=? AND app=? AND order_no=? LIMIT 1";
		String fileType=super.jt.queryForObject(sql,String.class,deviceNo,app,orderNo);
		if(fileType.contains("xxx")){
			return false;
		}
		return true;
	}

	@Override
	public List<Map<String, Object>> getOrderList(String deviceNo, String app, int page, int number, Integer state) {
		String sql = "SELECT `do`.`price`,`do`.`fee_transport` AS `transportfee`,`do`.`name`,`do`.`user_ids` AS `companyId`,`do`.`sort_name` AS `sortName`,`do`.`order_no` AS `orderNo`,`do`.`creat_time`,`do`.`pay_type` AS `payType`,`do`.`fee_total` AS `feeTotal`,`do`.`img_url` AS `imgUrl`,`do`.`file_type` AS `fileType`,`do`.`num`,`do`.`texture_names` AS `textureNames`,`do`.`status`,`do`.`express_no` AS `expressNo`,`de`.`code`,`do`.`info_ids` as `infoIds` FROM `diy_orders` `do` LEFT JOIN `diy_express` `de` ON `de`.`id`=`do`.`express_id` LEFT JOIN `diy_trade` `dt` ON `dt`.`order_no`=`do`.`order_no` WHERE `do`.`device_no`=? AND `do`.`app`=? AND `do`.`status`!=-1 AND `do`.`status`!=0 AND `do`.`status`!=6 AND `do`.`file_type`!='xxx' UNION ALL SELECT `do`.`price`,`do`.`fee_transport` AS `transportfee`,`do`.`name`,`do`.`user_ids` AS `companyId`,`do`.`sort_name` AS `sortName`,`do`.`order_no` AS `orderNo`,`do`.`creat_time`,`do`.`pay_type` AS `payType`,`do`.`fee_total` AS `feeTotal`,`do`.`img_url` AS `imgUrl`,`do`.`file_type` AS `fileType`,`do`.`num`,`do`.`texture_names` AS `textureNames`,`do`.`status`,`do`.`express_no` AS `expressNo`,`de`.`code`,`do`.`info_ids` as `infoIds` FROM `diy_orders` `do`  LEFT JOIN `diy_express` `de` ON `de`.`id`=`do`.`express_id` LEFT JOIN `diy_trade` `dt` ON `dt`.`order_no`=`do`.`order_no` WHERE `do`.`device_no`=? AND `do`.`app`=? AND `do`.`status`!=-1 AND `do`.`status`!=0 AND `do`.`status`!=6 AND `do`.`file_type`='xxx'";
		String end = " ORDER BY `status` ASC ,`creat_time` DESC LIMIT ?,?";
		if(state!=null&&state!=0)
			sql += " and `do`.`status`="+state;
		List<Map<String, Object>> list=this.jt.queryForList(sql+end,new Object[]{deviceNo,app,deviceNo,app,page*number,number});
		return (list==null || list.size()==0)?null:list;
	}
	
	@Override
	public List<Map<String, Object>> getOrderListForId(String userId, int page, int number, Integer state) {
		String sql = "SELECT `do`.`shop_no` AS `shopNo`,`do`.`texture_ids`,`do`.`price`,`do`.`fee_transport` AS `transportfee`,`do`.`name`,`do`.`user_ids` AS `companyId`,`do`.`sort_name` AS `sortName`,`do`.`order_no` AS `orderNo`,`do`.`creat_time`,`do`.`pay_type` AS `payType`,`do`.`fee_total` AS `feeTotal`,`do`.`img_url` AS `imgUrl`,`do`.`file_type` AS `fileType`,`do`.`num`,`do`.`texture_names` AS `textureNames`,`do`.`status`,`do`.`express_no` AS `expressNo`,`de`.`code`,`do`.`info_ids` as `infoIds` FROM `diy_orders` `do` LEFT JOIN `diy_express` `de` ON `de`.`id`=`do`.`express_id` LEFT JOIN `diy_trade` `dt` ON `dt`.`order_no`=`do`.`order_no` WHERE `do`.`userId`=? AND `do`.`status`!=-1 AND `do`.`status`!=0 AND `do`.`status`!=6 AND `do`.`file_type`!='xxx' ";
		String sql2= " UNION ALL SELECT `do`.`shop_no` AS `shopNo`,`do`.`texture_ids`,`do`.`price`,`do`.`fee_transport` AS `transportfee`,`do`.`name`,`do`.`user_ids` AS `companyId`,`do`.`sort_name` AS `sortName`,`do`.`order_no` AS `orderNo`,`do`.`creat_time`,`do`.`pay_type` AS `payType`,`do`.`fee_total` AS `feeTotal`,`do`.`img_url` AS `imgUrl`,`do`.`file_type` AS `fileType`,`do`.`num`,`do`.`texture_names` AS `textureNames`,`do`.`status`,`do`.`express_no` AS `expressNo`,`de`.`code`,`do`.`info_ids` as `infoIds` FROM `diy_orders` `do`  LEFT JOIN `diy_express` `de` ON `de`.`id`=`do`.`express_id` LEFT JOIN `diy_trade` `dt` ON `dt`.`order_no`=`do`.`order_no` WHERE `do`.`userId`=? AND `do`.`status`!=-1 AND `do`.`status`!=0 AND `do`.`status`!=6 AND `do`.`file_type`='xxx'";
		String end = " ORDER BY `status` ASC ,`creat_time` DESC LIMIT ?,?";
		if(state!=null&&state!=0){
			sql += " and `do`.`status`="+state;
			sql2 += " and `do`.`status`="+state;
		}
		List<Map<String, Object>> list=this.jt.queryForList(sql+sql2+end,new Object[]{userId,userId,page*number,number});
		return (list==null || list.size()==0)?null:list;
	}
	
	@Override
	public void toDeleteOrderOutTime(String userId){
		try{
			String had="select id from diy_orders where creat_time<DATE_SUB(now(),INTERVAL 7 DAY) and userId=? and status=1";
			List<Map<String,Object>>list=jt.queryForList(had,userId);
			if(!list.isEmpty()){
				String sql="UPDATE `diy_orders` SET `status`=6 WHERE `creat_time`<DATE_SUB(NOW(),INTERVAL 7 DAY) AND `userId`=? AND `status`=1";
				jt.update(sql,userId);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
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
	public boolean checkOrderForId(String orderNo, String userId) {
		try {
			int status=this.jt.queryForObject("SELECT `status` FROM `diy_orders` WHERE `order_no`=? AND `userId`=? LIMIT 1",new Object[]{orderNo,userId}, Integer.class);
			if(status==2||status==0){
				return true;
			}
		} catch (Exception e) {
		}
		return false;
	}
	
	@Override
	public int boundPrepayNo(String orderNo, String prepayId, String payNo) {
		return this.jt.update("UPDATE  `diy_trade` SET `prepay_id`=?,`pay_no`=?,`time`=NOW() WHERE `order_no`=?",new Object[]{prepayId,payNo,orderNo});
	}
	
	@Override
	public boolean updateOrder(String orderNo, String deviceNo, String app, String flag) {
		int status=0;
		if("delete".equalsIgnoreCase(flag)){
			status=6;
		}else if("cancel".equalsIgnoreCase(flag)){
			status=5;
		}else if("confirm".equalsIgnoreCase(flag)){
			status=4;
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
	public boolean updateOrderForId(String orderNo, String userId, String flag) {
		int status=0;
		if("delete".equalsIgnoreCase(flag)){
			status=6;
		}else if("cancel".equalsIgnoreCase(flag)){
			status=5;
		}else if("confirm".equalsIgnoreCase(flag)){
			status=4;
		}
		int result=this.jt.update("UPDATE `diy_orders` SET `status`=? WHERE `order_no`=? AND `userId`=? LIMIT 1",new Object[]{status,orderNo,userId});
		if(result>0){
			if(status==6){
				//删除订单的时候更新订单数
				this.updateShopOrderNumForId(userId, "order", -1);
			}
			if(status==6 || status==5){
				Map<String,Object>couponMap=super.jt.queryForMap("SELECT coupon_id FROM diy_orders WHERE userId=? AND order_no=?",userId,orderNo);
				String couponId=couponMap.get("coupon_id")==null?"0":couponMap.get("coupon_id").toString();
				if(!couponId.equals("")){
					String cid[]=couponId.split(",");
					for(int i=0;i<cid.length;i++){
						//将代金券激活还回去 (如果代金券还在有效期内)
						this.jt.update("UPDATE `diy_coupon_user` `dcu`  SET `dcu`.`status`=1 AND `dcu`.`valid`>=CURDATE() WHERE `dcu`.`id`=?",new Object[]{cid[i]});
					}
				}
			}
			return true;
		}
		return false;
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
	public boolean recordUserForId(String userId, String deviceToken) {
		try {
			this.jt.update("INSERT INTO `diy_device_user` (`userId`,`device_token`,`ctime`,`status`) VALUES(?,?,NOW(),1)",new Object[]{userId,deviceToken});
		} catch (Exception e) {
			
		}
		return true;
	}
	
	@Override
	public String queryOrderCompanyName(String companyId){
		String sql="SELECT `name` FROM `diy_sys_user` WHERE `id`=?";
		return this.jt.queryForObject(sql,String.class,companyId);
	}
	
	@Override
	public Map<String,Object>queryByOrderNo(String orderNo){
		String sql="SELECT * FROM `diy_orders` WHERE `order_no`=?";
		return super.jt.queryForMap(sql,orderNo);
	}
	
	@Override
	public Map<String, Object> queryjphGoodInfo(String id) {
		return this.jt.queryForMap("SELECT `dgi`.`id`,`dgi`.`user_id`,`dit`.`pre_url` AS `icoUrl`,`dit`.`texture_ids`,`dg`.`name` FROM `diy_goods_info2` `dgi` INNER JOIN `diy_info_texture2` `dit` ON `dit`.`info_id`=`dgi`.`id` INNER JOIN  `diy_good2` `dg` ON `dg`.`id`=`dgi`.`good_id` WHERE `dit`.`isdefault`=1 AND `dit`.`status`=1 AND `dgi`.`id`=? LIMIT 1",new Object[]{id});
	}

	@Override
	public Map<String, Object> queryForGoods(String id, String fileType) {
		String table="new_goods_info";
		if(fileType.equals("xxx")){
			table="diy_goods_info2";
		}
		String sql="SELECT * FROM "+table+" WHERE id=?";
		return super.jt.queryForMap(sql,id);
	}

	@Override
	public Map<String, Object> queryForMerchant(String id) {
		String sql="SELECT id,enough_money,transportfee,remotefee FROM diy_sys_user WHERE id=?";
		return super.jt.queryForMap(sql,id);
	}

	
	@Override
	public boolean changeTexture(String shopNo, String userId, String textureIds) {
		String textureName=queryTextureById(textureIds.split("_"));
		Map<String,Object>shopMap=this.jt.queryForMap("SELECT file_type,info_id,`img_url` FROM diy_shopcart WHERE shop_no=?",shopNo);
		String file_type=shopMap.get("file_type")==null?"":shopMap.get("file_type").toString();
		String info_id=shopMap.get("info_id")==null?"":shopMap.get("info_id").toString();
		String imgUrl=(String)shopMap.get("img_url");
		String tex="info_id";
		if(!file_type.equals("xxx")){
			tex="make_id";
		}
		Map<String,Object>textureMap=this.jt.queryForMap("SELECT pre_url FROM diy_info_texture2 WHERE "+tex+"=? AND texture_ids=? LIMIT 1",info_id,textureIds);
		String preImg=textureMap.get("pre_url")==null?"":textureMap.get("pre_url").toString();
		int result=0;
		if(!file_type.equals("xxx") || imgUrl.contains("_")){
			//定制
			result=this.jt.update("UPDATE `diy_shopcart` SET `texture_ids`=?,`texture_name`=? WHERE `userId`=? AND `shop_no`=? LIMIT 1",new Object[]{textureIds,textureName,userId,shopNo});
		}else{
			result=this.jt.update("UPDATE `diy_shopcart` SET `img_url`=?,`texture_ids`=?,`texture_name`=? WHERE `userId`=? AND `shop_no`=? LIMIT 1",new Object[]{preImg,textureIds,textureName,userId,shopNo});
		}
		if(result>0){
			return true;
		}else{
			return false;
		}
	}

	@Override
	public Map<String, Object> queryJphInfoByInfoId(String infoId,String texture_ids) {
		try{
			return this.jt.queryForMap("SELECT `ngi`.`user_id` AS `userId`,`dit`.`now_price` AS `nowPrice`,`dit`.`org_price` AS `orgPrice`,`dit`.`make_id` FROM `new_goods_info` `ngi` INNER JOIN `diy_info_texture2` `dit` ON `dit`.`make_id`=`ngi`.`id` WHERE `ngi`.`id`=? AND `dit`.`texture_ids`=?",new Object[]{infoId,texture_ids});
		}catch(Exception e){
			return null;
		}
		
	}
	
	@Override
	public Map<String,Object>queryJphInfoByInfoId2(String infoId,String texture_ids){
		try{
			return this.jt.queryForMap("SELECT `ngi`.`user_id` AS `userId`,`dit`.`now_price` AS `nowPrice`,`dit`.`org_price` AS `orgPrice`,`dit`.`make_id` FROM `diy_goods_info2` `ngi` INNER JOIN `diy_info_texture2` `dit` ON `dit`.`info_id`=`ngi`.`id` WHERE `ngi`.`id`=? AND `dit`.`texture_ids`=?",new Object[]{infoId,texture_ids});
		}catch(Exception e){
			return null;
		}
		
	}
	
	@Override
	public Map<String,Object>queryInfoTexture(String infoId,String texutreIds,String fileType){
		String info="make_id";
		if(fileType.equals("xxx")){
			info="info_id";
		}
		try {
			String sql="SELECT * FROM diy_info_texture2 WHERE "+info+"=? AND texture_ids=?";
			return this.jt.queryForMap(sql,infoId,texutreIds);
		} catch (Exception e) {
		}
		return null;
	}
	
}
