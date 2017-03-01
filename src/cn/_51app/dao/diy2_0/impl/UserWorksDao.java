package cn._51app.dao.diy2_0.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import cn._51app.dao.BaseDao;
import cn._51app.dao.diy2_0.IUserWorksDao;
import cn._51app.entity.User;
import cn._51app.util.CommonUtil;

@Repository
public class UserWorksDao extends BaseDao implements IUserWorksDao{

	@Override
	public String userWorks(Map<String,Object>paramMap) throws Exception{
		Map<String,Object>resultMap=new HashMap<String,Object>();
		String dgurl=paramMap.get("dgurl").toString();
		String sql="SELECT dwl.id,dwl.name,dwl.cont,date_format(dwl.ctime,'%Y年%m月%d') AS ctime,dwl.imgurl,dwl.goodsId,dwl.suffix,dwl.textureIds,dit.org_price AS orgPrice,dit.now_price AS nowPrice FROM diy_works_list dwl LEFT JOIN diy_info_texture2 dit ON dit.texture_ids=dwl.textureIds AND dit.make_id=dwl.goodsId WHERE dwl.userId=? ORDER BY dwl.ctime DESC,dwl.sort ASC ";
		String sql2="SELECT date_format(ctime,'%Y年%m月%d') AS ctime FROM diy_works_list WHERE userId=? GROUP BY date_format(ctime,'%Y年%m月%d日') ORDER BY ctime DESC";
			int id=Integer.parseInt(paramMap.get("id").toString());
			List<Map<String,Object>>info = super.jt.queryForList(sql,new Object[]{id});
			List<Map<String,Object>>info2=super.jt.queryForList(sql2,new Object[]{id});
			
			for(Map<String,Object>map : info){
				String suffix=map.get("suffix")==null?"":map.get("suffix").toString();
				String img=map.get("imgurl")==null?"":map.get("imgurl").toString();
				String name=(String)map.get("name");
				String cont=(String)map.get("cont");
				map.put("name", StringUtils.isBlank(name)?"未命名作品":name);
				map.put("cont", StringUtils.isBlank(cont)?"这家伙很懒，什么都没写":cont);
				//处理照片书的图片显示问题
				if(img.contains("_")){
					String imgs[]=img.split("_");
					map.put("imgUrl", dgurl+imgs[0]+"_1"+suffix);
				}else{
					map.put("imgUrl", dgurl+img+"@b"+suffix);
				}
					map.remove("suffix");
			}
			List<String>dateTime=new ArrayList<String>();
			for(Map<String,Object>map2:info2){
				dateTime.add(map2.get("ctime").toString());
			}
			resultMap.put("productions",info);
			resultMap.put("productionDates", dateTime.toArray());
			return super.toJson(resultMap);
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class)
	public String saveWorks(Map<String,Object>paramMap)throws Exception{
		String sql="INSERT INTO diy_works_list (name,cont,isopen,ctime,imgurl,suffix,goodsId,userId,textureIds,type,orgPrice,endtime,textureName,money) VALUES(:name,:cont,:isopen,now(),:saveImg,:suffix,:goodId,:userId,:textureIds,:type,:orgPrice,DATE_ADD(NOW(),INTERVAL  3 DAY),:textureNames,:money)";
			int result= super.npjt.update(sql, paramMap);
			if(result<0){
				throw new RuntimeException();
			}
			String sql2="SELECT @@IDENTITY AS workId";
			return super.toJson(super.jt.queryForMap(sql2));
	}
	
	@Override
	public int delWorks(String ids)throws Exception{
		String id[]=ids.split(",");
		for(int j=0;j<id.length;j++){
			String sql="SELECT imgurl,suffix FROM diy_works_list WHERE id=?";
			//先查出图片
			Map<String,Object>map=super.jt.queryForMap(sql,id[j]);
			String imgurl=map.get("imgurl")==null?"":map.get("imgurl").toString();
			String suffix=map.get("suffix")==null?"":map.get("suffix").toString();
			String img[]=imgurl.split(",");
			String suf[]=suffix.split(",");
			//删除图片
			for(int i=0;i<img.length;i++){
				CommonUtil.delWorks(img[i],suf[i],null);
			}
			String sql2="DELETE FROM diy_works_list WHERE id=?";
			//删除数据
			int result=super.jt.update(sql2,id[j]);
			if(result!=1){
				return result;
			}
		}
		return 1;
	}
	
	@Override
	public int editWorkds(Map<String,Object>paramMap)throws Exception{
		String sql="UPDATE diy_works_list SET";
				   if(paramMap.get("cont")!=null && !paramMap.get("cont").toString().equals("")){
						sql+=" cont='"+paramMap.get("cont")+"',";
					}
					if(paramMap.get("name")!=null && !paramMap.get("name").toString().equals("")){
						sql+=" name='"+paramMap.get("name")+"',";
				    }
					if(paramMap.get("textureIds")!=null && !paramMap.get("textureIds").toString().equals("")){
						sql+=" textureIds='"+paramMap.get("textureIds")+"',";
					}
					if(paramMap.get("isopen")!=null && !paramMap.get("isopen").toString().equals("")){
						sql+=" isopen="+paramMap.get("isopen")+",";
					}
					sql=sql.substring(0,sql.length()-1);
					sql+=" WHERE id=:workId";
			return super.npjt.update(sql,paramMap);
	}
	
	@Override
	public Map<String,Object>queryWork(String workId){
		String sql="SELECT `ddu`.`name` AS `nickName`,`ddu`.`head_url`,`dit`.`now_price`,`dit`.`org_price`,`ngi`.`goodsType`,`dwl`.`id`,`dwl`.`name`,`dwl`.`cont`,`dwl`.imgurl,`dwl`.`isopen`,`dwl`.`ctime`,`dwl`.`suffix`,`dwl`.`goodsId`,`dwl`.`textureIds` FROM `diy_works_list` `dwl` INNER JOIN `new_goods_info` `ngi` ON `ngi`.`id`=`dwl`.`goodsId` INNER JOIN `diy_info_texture2` `dit` ON `dit`.`make_id`=`dwl`.`goodsId` AND `dit`.`texture_ids`=`dwl`.`textureIds` INNER JOIN diy_device_user `ddu` ON `ddu`.`id`=`dwl`.`userId` WHERE `dwl`.`id`=?";
		return super.jt.queryForMap(sql,workId);
	}
	
	@Override
	public int editUserInfo(Map<String,Object>paramMap)throws Exception{
		String sql="UPDATE diy_device_user SET";
				if(paramMap.get("phone")!=null && !paramMap.get("phone").toString().equals("")){
					sql+=" mobile='"+paramMap.get("phone")+"',";
				}
				if(paramMap.get("nickName")!=null && !paramMap.get("nickName").toString().equals("")){
					sql+=" name='"+paramMap.get("nickName")+"',";
				}
				if(paramMap.get("imgurl")!=null && !paramMap.get("imgurl").toString().equals("")){
					sql+=" head_url='"+paramMap.get("imgurl")+"',";
				}
				if(paramMap.get("sex")!=null && !paramMap.get("sex").toString().equals("")){
					sql+=" sex='"+paramMap.get("sex")+"',";
				}
				if(paramMap.get("openid")!=null && !paramMap.get("openid").toString().equals("")){
					sql+=" openid='"+paramMap.get("openid")+"',";
				}
				sql=sql.substring(0,sql.length()-1);
				sql+=" WHERE app=:app AND device_no=:deviceNo";
		return super.npjt.update(sql,paramMap);
	}
	
	@Override
	public int editUserInfoAndOpenid(Map<String,Object>paramMap)throws Exception{
		String sql="UPDATE diy_device_user SET";
		boolean flag=true;
				if(StringUtils.isNotBlank((String)paramMap.get("phone"))){
					sql+=" mobile='"+paramMap.get("phone")+"',";
					flag=false;
				}
				if(paramMap.get("nickName")!=null && !paramMap.get("nickName").toString().equals("")){
					sql+=" name='"+paramMap.get("nickName")+"',";
					flag=false;
				}
				if(paramMap.get("imgurl")!=null && !paramMap.get("imgurl").toString().equals("")){
					sql+=" head_url='"+paramMap.get("imgurl")+"',";
					flag=false;
				}
				if(paramMap.get("sex")!=null && !paramMap.get("sex").toString().equals("")){
					sql+=" sex='"+paramMap.get("sex")+"',";
					flag=false;
				}
				if(flag){
					return 1;
				}
				sql=sql.substring(0,sql.length()-1);
				if(StringUtils.isNotBlank((String)paramMap.get("openid")))
					sql+=" WHERE openid=:openid";
				else if(StringUtils.isBlank((String)paramMap.get("openid")) && StringUtils.isNotBlank((String)paramMap.get("phone")))
					sql+=" WHERE mobile=:phone";
				
				int result=super.npjt.update(sql,paramMap);
		return result;
	}
	
	@Override
	public int editUserInfoWithState(Map<String,Object>paramMap)throws Exception{
		String sql="UPDATE diy_device_user SET";
		String state=paramMap.get("state").toString();
		boolean flag=true;
				if(paramMap.get("phone")!=null && !paramMap.get("phone").toString().equals("")){
					sql+=" mobile='"+paramMap.get("phone")+"',";
					flag=false;
				}
				if(paramMap.get("nickName")!=null && !paramMap.get("nickName").toString().equals("")){
					sql+=" name='"+paramMap.get("nickName")+"',";
					flag=false;
				}
				if(paramMap.get("imgurl")!=null && !paramMap.get("imgurl").toString().equals("")){
					sql+=" head_url='"+paramMap.get("imgurl")+"',";
					flag=false;
				}
				if(paramMap.get("sex")!=null && !paramMap.get("sex").toString().equals("")){
					sql+=" sex='"+paramMap.get("sex")+"',";
					flag=false;
				}
				if(flag){
					return 1;
				}
				sql=sql.substring(0,sql.length()-1);
				sql+=" WHERE app=:app AND device_no=:deviceNo";
				if(state.equals("2")){
					sql+=" AND openid=:openid";
				}
				if(state.equals("3")){
					sql+=" AND qqid=:openid";
				}
		return super.npjt.update(sql,paramMap);
	}
	
	@Override
	public int bindingCoupon(Map<String,Object>paramMap)throws Exception{
		try{
			String capp=paramMap.get("capp")==null?"":paramMap.get("capp").toString();
			String phone=paramMap.get("phone")==null?"":paramMap.get("phone").toString();
			if(capp.contains(phone)){
				return 0;
			}
			String oldSql="SELECT id FROM diy_device_user WHERE device_no=:deviceNo AND app=:capp AND openid is null";
			Integer userId=super.npjt.queryForObject(oldSql,paramMap,Integer.class);
			String sql="";
			if(StringUtils.isNotBlank((String)paramMap.get("openid"))){
				sql="UPDATE diy_coupon_user SET userId=(SELECT id FROM diy_device_user WHERE openid=:openid AND app=:app AND device_no=:deviceNo) WHERE userId="+userId+" AND coupon_id=:coupon_id";
			}else{
				sql="UPDATE diy_coupon_user SET userId=(SELECT id FROM diy_device_user WHERE mobile=:phone AND app=:app AND device_no=:deviceNo) WHERE userId="+userId+" AND coupon_id=:coupon_id";
			}
			return super.npjt.update(sql, paramMap);
		}catch(Exception e){
			return 0;
		}
	}
	
	@Override
	public int bindingCoupon2(User user, String couponId) {
		try{
			String oldSql="SELECT id FROM diy_device_user WHERE device_no=? AND app=? ORDER BY `ctime` DESC LIMIT 1";
			Integer userId=super.jt.queryForObject(oldSql,new Object[]{user.getDeviceNo(),user.getApp()},Integer.class);
			String sql="UPDATE diy_coupon_user SET userId=? WHERE userId=? AND coupon_id=?";
			return super.jt.update(sql, new Object[]{user.getId(),userId,couponId});
		}catch(Exception e){
			return 0;
		}
	}
	
	@Override
	public int bindingShop(Map<String,Object>paramMap)throws Exception{
		try{
			String capp=paramMap.get("capp")==null?"":paramMap.get("capp").toString();
			String phone=paramMap.get("phone")==null?"":paramMap.get("phone").toString();
			if(capp.contains(phone)){
				return 0;
			}
			String oldSql="SELECT id FROM diy_device_user WHERE device_no=:deviceNo AND app=:capp AND openid is null";
			Integer userId=super.npjt.queryForObject(oldSql,paramMap,Integer.class);
			String sql="";
			if(StringUtils.isNotBlank((String)paramMap.get("openid"))){
				sql="UPDATE diy_shopcart SET userId=(SELECT id FROM diy_device_user WHERE openid=:openid AND app=:app AND device_no=:deviceNo) WHERE userId="+userId;
			}else{
				sql="UPDATE diy_shopcart SET userId=(SELECT id FROM diy_device_user WHERE mobile=:phone AND app=:app AND device_no=:deviceNo) WHERE userId="+userId;
			}
			return super.npjt.update(sql, paramMap);
		}catch(Exception e){
			return 0;
		}
	}
	
	@Override
	public int bindingShop2(User user) {
		try{
			String oldSql="SELECT id FROM diy_device_user WHERE device_no=? AND app=? ORDER BY `ctime` DESC LIMIT 1";
			Integer userId=super.jt.queryForObject(oldSql,new Object[]{user.getDeviceNo(),user.getApp()},Integer.class);
			String sql="UPDATE diy_shopcart SET userId=? WHERE userId=?";
			return super.jt.update(sql, new Object[]{user.getId(),userId});
		}catch(Exception e){
			return 0;
		}
	}
	
	@Override
	public int bindingOrders(Map<String,Object>paramMap)throws Exception{
		try{
			String capp=paramMap.get("capp")==null?"":paramMap.get("capp").toString();
			String phone=paramMap.get("phone")==null?"":paramMap.get("phone").toString();
			if(capp.contains(phone)){
				return 0;
			}
			String oldSql="SELECT id FROM diy_device_user WHERE device_no=:deviceNo AND app=:capp AND openid is null";
			Integer userId=super.npjt.queryForObject(oldSql,paramMap,Integer.class);
			String sql="";
			if(StringUtils.isNotBlank((String)paramMap.get("openid"))){
				sql="UPDATE diy_orders SET userId=(SELECT id FROM diy_device_user WHERE openid=:openid AND app=:app AND device_no=:deviceNo) WHERE userId="+userId;
			}else{
				sql="UPDATE diy_orders SET userId=(SELECT id FROM diy_device_user WHERE mobile=:phone AND app=:app AND device_no=:deviceNo) WHERE userId="+userId;
			}
			return super.npjt.update(sql, paramMap);
		}catch(Exception e){
			return 0;
		}
	}
	
	@Override
	public int bindingOrders2(User user) {
		try{
			String oldSql="SELECT id FROM diy_device_user WHERE device_no=? AND app=? ORDER BY `ctime` DESC LIMIT 1";
			Integer userId=super.jt.queryForObject(oldSql,new Object[]{user.getDeviceNo(),user.getApp()},Integer.class);
			String sql="UPDATE diy_orders SET userId=? WHERE userId=?";
			return super.jt.update(sql, new Object[]{user.getId(),userId});
		}catch(Exception e){
			return 0;
		}
	}
	
	@Override
	public int bindingAddress(Map<String,Object>paramMap)throws Exception{
		try{
			String capp=paramMap.get("capp")==null?"":paramMap.get("capp").toString();
			String phone=paramMap.get("phone")==null?"":paramMap.get("phone").toString();
			if(capp.contains(phone)){
				return 0;
			}
			String oldSql="SELECT id FROM diy_device_user WHERE device_no=:deviceNo AND app=:capp AND openid is null";
			Integer userId=super.npjt.queryForObject(oldSql,paramMap,Integer.class);
			String sql="";
			if(StringUtils.isNotBlank((String)paramMap.get("openid"))){
				sql="UPDATE diy_user_address SET userId=(SELECT id FROM diy_device_user WHERE openid=:openid AND app=:app AND device_no=:deviceNo) WHERE userId="+userId;
			}else{
				sql="UPDATE diy_user_address SET userId=(SELECT id FROM diy_device_user WHERE mobile=:phone AND app=:app AND device_no=:deviceNo) WHERE userId="+userId;
			}
			return super.npjt.update(sql, paramMap);
		}catch(Exception e){
			return 0;
		}
	}
	
	@Override
	public int bindingAddress2(User user) {
		try{
			String oldSql="SELECT id FROM diy_device_user WHERE device_no=? AND app=? ORDER BY `ctime` DESC LIMIT 1";
			Integer userId=super.jt.queryForObject(oldSql,new Object[]{user.getDeviceNo(),user.getApp()},Integer.class);
			String sql="UPDATE diy_user_address SET userId=? WHERE userId=?";
			return super.jt.update(sql, new Object[]{userId,userId});
		}catch(Exception e){
			return 0; 
		}
			
	}
	
	@Override
	public int bindingEvaluation(Map<String,Object>paramMap)throws Exception{
		try{
			String capp=paramMap.get("capp")==null?"":paramMap.get("capp").toString();
			String phone=paramMap.get("phone")==null?"":paramMap.get("phone").toString();
			if(capp.contains(phone)){
				return 0;
			}
			String oldSql="SELECT id FROM diy_device_user WHERE device_no=:deviceNo AND app=:capp AND openid is null";
			Integer userId=super.npjt.queryForObject(oldSql,paramMap,Integer.class);
			String sql="";
			if(StringUtils.isNotBlank((String)paramMap.get("openid"))){
				sql="UPDATE diy_evaluation SET userId=(SELECT id FROM diy_device_user WHERE openid=:openid AND app=:app AND device_no=:deviceNo) WHERE userId="+userId;
			}else{
				sql="UPDATE diy_evaluation SET userId=(SELECT id FROM diy_device_user WHERE mobile=:phone AND app=:app AND device_no=:deviceNo) WHERE userId="+userId;
			}
			return super.npjt.update(sql, paramMap);
		}catch(Exception e){
			return 0;
		}
	}
	
	@Override
	public int bindingWork(Map<String,Object>paramMap)throws Exception{
		try{
			String capp=paramMap.get("capp")==null?"":paramMap.get("capp").toString();
			String phone=paramMap.get("phone")==null?"":paramMap.get("phone").toString();
			if(capp.contains(phone)){
				return 0;
			}
			String oldSql="SELECT id FROM diy_device_user WHERE device_no=:deviceNo AND app=:capp AND openid is null";
			Integer userId=super.npjt.queryForObject(oldSql,paramMap,Integer.class);
			String sql="";
			if(StringUtils.isNotBlank((String)paramMap.get("openid"))){
				sql="UPDATE diy_works_list SET userId=(SELECT id FROM diy_device_user WHERE openid=:openid AND app=:app AND device_no=:deviceNo) WHERE userId="+userId;
			}else{
				sql="UPDATE diy_works_list SET userId=(SELECT id FROM diy_device_user WHERE mobile=:phone AND app=:app AND device_no=:deviceNo) WHERE userId="+userId;
			}
			return super.npjt.update(sql, paramMap);
		}catch(Exception e){
			return 0;
		}
	}
	
	@Override
	public int bindingWork2(User user) {
		try{
			String oldSql="SELECT id FROM diy_device_user WHERE device_no=? AND app=? ORDER BY `ctime` DESC LIMIT 1";
			Integer userId=super.jt.queryForObject(oldSql,new Object[]{user.getDeviceNo(),user.getApp()},Integer.class);
			String sql="UPDATE diy_works_list SET userId=? WHERE userId=?";
			return super.jt.update(sql, new Object[]{user.getId(),userId});
		}catch(Exception e){
			return 0;
		}
	}
	
	@Override
	public int insertUser(Map<String,Object>paramMap)throws Exception{
		String sql="INSERT INTO diy_device_user(";
		if(paramMap.get("phone")!=null && !paramMap.get("phone").toString().equals("")){
			sql+="mobile,";
		}
		if(paramMap.get("nickName")!=null && !paramMap.get("nickName").toString().equals("")){
			sql+="name,";
		}
		if(paramMap.get("imgurl")!=null && !paramMap.get("imgurl").toString().equals("")){
			sql+="head_url,";
		}
		if(paramMap.get("sex")!=null && !paramMap.get("sex").toString().equals("")){
			sql+="sex,";
		}
		if(paramMap.get("openid")!=null && !paramMap.get("openid").toString().equals("")){
			sql+="openid,";
		}
		if(paramMap.get("app")!=null && !paramMap.get("app").toString().equals("")){
			sql+="app,";
		}
		if(paramMap.get("deviceNo")!=null && !paramMap.get("deviceNo").toString().equals("")){
			sql+="device_no,";
		}
		if(paramMap.get("qqid")!=null && !paramMap.get("qqid").toString().equals("")){
			sql+="qqid,";
		}
		if(paramMap.get("password")!=null && !paramMap.get("password").toString().equals("")){
			sql+="password,";
		}
		if(paramMap.get("username")!=null && !paramMap.get("username").toString().equals("")){
			sql+="username,";
		}
		sql=sql.substring(0,sql.length()-1);
		sql+=") values(";
		if(paramMap.get("phone")!=null && !paramMap.get("phone").toString().equals("")){
			sql+="'"+paramMap.get("phone")+"',";
		}
		if(paramMap.get("nickName")!=null && !paramMap.get("nickName").toString().equals("")){
			sql+="'"+paramMap.get("nickName")+"',";
		}
		if(paramMap.get("imgurl")!=null && !paramMap.get("imgurl").toString().equals("")){
			sql+="'"+paramMap.get("imgurl")+"',";
		}
		if(paramMap.get("sex")!=null && !paramMap.get("sex").toString().equals("")){
			sql+="'"+paramMap.get("sex")+"',";
		}
		if(paramMap.get("openid")!=null && !paramMap.get("openid").toString().equals("")){
			sql+="'"+paramMap.get("openid")+"',";
		}
		if(paramMap.get("app")!=null && !paramMap.get("app").toString().equals("")){
			sql+="'"+paramMap.get("app")+"',";
		}
		if(paramMap.get("deviceNo")!=null && !paramMap.get("deviceNo").toString().equals("")){
			sql+="'"+paramMap.get("deviceNo")+"',";
		}
		if(paramMap.get("qqid")!=null && !paramMap.get("qqid").toString().equals("")){
			sql+="'"+paramMap.get("qqid")+"',";
		}
		if(paramMap.get("password")!=null && !paramMap.get("password").toString().equals("")){
			sql+="'"+paramMap.get("password")+"',";
		}
		if(paramMap.get("username")!=null && !paramMap.get("username").toString().equals("")){
			sql+="'"+paramMap.get("username")+"',";
		}
		sql=sql.substring(0,sql.length()-1);
		sql+=")";
		return super.jt.update(sql+" ON DUPLICATE KEY UPDATE `mobile`='"+paramMap.get("phone")+"'");
	}
	
	@Override
	public int isOpenUser(Map<String,Object>paramMap)throws Exception{
		String sql="SELECT * FROM diy_device_user WHERE openid=?";
		List<Map<String,Object>>list=super.jt.queryForList(sql,paramMap.get("openid"));
		if(list.isEmpty())
			return 0;
		return 1;
	}
	
	@Override
	public Map<String,Object>loadUserInfoById(Map<String,Object>paramMap){
		String dgurl=paramMap.get("dgurl").toString();
		String openid=(String)paramMap.get("openid");
		String phone=(String)paramMap.get("phone");
		String sql="SELECT id,mobile,name,head_url AS headUrl,sex,openid,username AS hxUserName,password AS hxPassWord FROM diy_device_user WHERE device_no=:deviceNo AND app=:app ORDER BY id";
		if(StringUtils.isBlank(openid) && StringUtils.isNotBlank(phone))
			sql="SELECT id,mobile,name,head_url AS headUrl,sex,openid,username AS hxUserName,password AS hxPassWord FROM diy_device_user WHERE mobile=:phone AND openid is null ORDER BY id LIMIT 1";
		else if(StringUtils.isNotBlank(openid))
			sql="SELECT id,mobile,name,head_url AS headUrl,sex,openid,username AS hxUserName,password AS hxPassWord FROM diy_device_user WHERE openid=:openid ORDER BY id LIMIT 1";
		else
			sql="SELECT id,mobile,name,head_url AS headUrl,sex,openid,username AS hxUserName,password AS hxPassWord FROM diy_device_user WHERE device_no=:deviceNo AND app=:app ORDER BY id LIMIT 1";
		try{
			Map<String,Object>map=super.npjt.queryForMap(sql,paramMap);
			if(map.get("name")==null){
				map.put("name","");
			}
			if(map.get("sex")!=null){
				map.put("sex", map.get("sex").toString().equals("1")?"男":"女");
			}else{
				map.put("sex","");
			}
			if(map.get("headUrl")==null){
				map.put("headUrl","");
			}
			if(map.get("hxPassWord")==null){
			map.put("hxPassWord", "");
			}
			if(map.get("hxUserName")==null){
			map.put("hxUserName", "");
			}
			if(map.get("headUrl")!=null){
				map.put("headUrl",dgurl+map.get("headUrl"));
			}else{
				map.put("headUrl","");
			}
		return map;
		}catch(Exception e){
			return null;
		}	
	}
	
	@Override
	public List<Map<String,Object>> loadUserInfoByIdWithState(Map<String,Object>paramMap){
		String dgurl=paramMap.get("dgurl").toString();
		String state=paramMap.get("state").toString();
		String sql="SELECT id,mobile,name,head_url AS headUrl,sex,openid,username AS hxUserName,password AS hxPassWord FROM diy_device_user WHERE device_no=:deviceNo AND app=:app";
		if(state.equals("1")){
			sql+=" AND mobile=:phone";
		}else if(state.equals("2")){
			sql+=" AND qqid=:openid";
		}else if(state.equals("3")){
			sql+=" AND openid=:openid";
		}
		List<Map<String,Object>> infoList=super.npjt.queryForList(sql,paramMap);
		
		for(Map<String,Object> map : infoList){
			if(map.get("name")==null){
				map.put("name","");
			}
			if(map.get("sex")!=null){
				map.put("sex", map.get("sex").toString().equals("1")?"男":"女");
			}else{
				map.put("sex","");
			}
			if(map.get("headUrl")==null){
				map.put("headUrl","");
			}
			if(map.get("hxPassWord")==null){
			map.put("hxPassWord", "");
			}
			if(map.get("hxUserName")==null){
			map.put("hxUserName", "");
			}
			if(map.get("headUrl")!=null){
				map.put("headUrl",dgurl+map.get("headUrl"));
			}else{
				map.put("headUrl","");
			}
		}
	return infoList;	
	}
	
	@Override
	public Map<String,Object> loadUserInfo(Map<String,Object>paramMap)throws Exception{
		String dgurl=paramMap.get("dgurl").toString();
		String deviceNo=paramMap.get("deviceNo").toString();
		String app=paramMap.get("app").toString();
		String sql="SELECT id,mobile,name,head_url AS headUrl,sex,openid FROM diy_device_user WHERE device_no=? AND app=?";
		Map<String,Object> map=super.jt.queryForMap(sql, deviceNo,app);
		String id=map.get("id").toString();
		//数据格式化
			if(map.get("name")==null){
				map.put("name","");
			}
			if(map.get("sex")!=null){
				map.put("sex", map.get("sex").toString().equals("1")?"男":"女");
			}else{
				map.put("sex","");
			}
			if(map.get("headUrl")==null){
				map.put("headUrl","");
			}if(map.get("openid")==null){
				map.put("openid", "");
			}
			if(map.get("headUrl")!=null){
				map.put("headUrl",dgurl+map.get("headUrl"));
			}else{
				map.put("headUrl","");
			}
			//用户查询接口
//			map.put("shop","newShopCart2/shopList.do?deviceNo="+deviceNo+"&app="+app+"&page=0");
//			map.put("order", "indent/getOrderList.do?deviceNo="+deviceNo+"&app="+app+"&page=0");
//			map.put("coupon","coupon2/couponList?deviceNo="+deviceNo+"&app="+app);
//			map.put("adress", "newAddress/getAdress.do?deviceNo="+deviceNo+"&app="+app);
//			map.put("about","homeNav2/intro.do");
		return map;
	}
	
	@Override
	public Integer haveOpenid(String openid){
		try {
			String sql = "SELECT 1 FROM `diy_device_user` WHERE openid = ? LIMIT 1";
			return jt.queryForObject(sql, new Object[]{openid},Integer.class);
		} catch (DataAccessException e) {
			return 0;
		}
	}
	
	@Override
	public int insertOpenid(Map<String, Object> paramMap){
		String sql = "INSERT INTO `diy_device_user` (device_no,app,ctime,openid,`name`,head_url,mobile) VALUES (:device_no,:app,NOW(),:openid,:name,:head_url,:mobile)";
		return npjt.update(sql, paramMap); 
	}
	
	@Override
	public Map<String, Object> findUser4Openid(String openid){
		String sql = "SELECT id,device_no,app,`name`,mobile,openid,head_url,shop_num,order_num FROM `diy_device_user` WHERE openid = ?";
		return jt.queryForMap(sql,new Object[]{openid});
	}
	
	@Override
	public int insertUserHome(String deviceNo,String app){
		int result =0;
		try {
			String insert ="INSERT INTO diy_device_user (device_no,app,ctime,active)",
					value=" VALUES('"+deviceNo+"','"+app+"',now(),1)";
			String sql =insert+value;
			result =jt.update(sql);
		} catch (DuplicateKeyException e) {
			result =1;
		}
		return result;
	}

	@Override
	public Map<String, Object> returnPassword(int id) {
		String sql = "select username,password from diy_device_user where id=?";
		return jt.queryForMap(sql,new Object[]{id});
	}
	
	@Override
	public boolean checkHxUserName(String userName){
		String sql="SELECT COUNT(id) FROM diy_device_user WHERE username=?";
		int result=jt.queryForObject(sql,Integer.class,userName);
		if(result!=0){
			return true;
		}
		return false;
	}

	@Override
	public int insertPassword(Map<String, Object> paramMap) {
		String sql = "UPDATE diy_device_user SET";// username = '"+paramMap.get("username")+"',password = '"+paramMap.get("password")+"' where id = '"+paramMap.get("id")+"'";
		if(paramMap.get("username")!=null){
			sql+=" username ='"+paramMap.get("username")+"',";
		}
		if(paramMap.get("password")!=null){
			sql+=" password = '"+paramMap.get("password")+"',";
		}
		sql = sql.substring(0,sql.length()-1);
		sql+="where id = '"+paramMap.get("id")+"'";
		return npjt.update(sql, paramMap);
	}

	@Override
	public boolean infoUser(String deviceNo, String app, String version, String deviceToken) {
		try {
			this.jt.update("INSERT INTO `diy_device_user` (`device_no`,`app`,`device_token`,`version`) VALUES (?,?,?,?) ON DUPLICATE KEY UPDATE `version`=?,`device_token`=?",new Object[]{deviceNo,app,deviceToken,version,version,deviceToken});
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public Map<String, Object> queryUserWork(String workId) {
		String sql="SELECT * FROM diy_works_list WHERE id=?";
		return super.jt.queryForMap(sql,workId);
	}
	
	@Override
	public int retroaction(String contact,String content)throws Exception{
		String insert="INSERT INTO diy_retroaction(contact,content,ctime) ";
		String value="VALUES(?,?,now())";
		String sql=insert+value;
		return super.jt.update(sql,contact,content);
	}
	
	@Override
	public Map<String, Object> queryDefaultTextureIds(String goodId,String textureIds) {
		StringBuilder sql=new StringBuilder();
		sql.append("SELECT `texture_ids`,`org_price`,`now_price` FROM `diy_info_texture2` WHERE `make_id`=? ");
		if(StringUtils.isBlank(textureIds)){
			sql.append("AND `isdefault`=1");
		}else{
			sql.append("AND `texture_ids`='"+textureIds+"'");
		}
		sql.append(" LIMIT 1");
		return this.jt.queryForMap(sql.toString(),new Object[]{goodId});
	}
	
	@Override
	public boolean checkIsZeroGood(String workId) {
		try {
			this.jt.queryForObject("SELECT `id` FROM `diy_works_list` WHERE `status`=1 AND `type`=3 AND `id`=?",new Object[]{workId}, Integer.class);
			return true;
		} catch (Exception e) {
		}
		return false;
	}
	
	@Override
	public boolean isShopOrOrder(String userId, String workId) {
		boolean flag=false;
		try {
			this.jt.queryForObject("SELECT `id` FROM `diy_shopcart` WHERE `userwork`=? LIMIT 1",new Object[]{workId}, Integer.class);
			flag=true;
		} catch (Exception e) {
		}
		if(!flag){
			try {
				this.jt.queryForObject("SELECT `id` FROM `diy_orders` WHERE `userwork`=? LIMIT 1",new Object[]{workId}, Integer.class);
				flag=true;
			} catch (Exception e) {
			}
		}
		return flag;
	}
	
	@Override
	public Map<String,Object>queryUserWorkDetailInfo(String workId){
		String sql="SELECT * FROM diy_works_list WHERE id=?";
		return this.jt.queryForMap(sql,workId);
	}
	
	
	@Override
	public void updateZeroStatus(String orderNo) {
		this.jt.update("UPDATE diy_orders `do` INNER JOIN diy_works_list  `dwi` SET `dwi`.`cutStatus`=3 WHERE `do`.`userwork`=`dwi`.`id` AND `do`.`order_no`=? AND `dwi`.`type`=3",new Object[]{orderNo});
	}

	@Override
	public Map<String, Object> createNewUser(final User user) {
		Integer userId=null;
		boolean flag=false;
		Map<String, Object> result=new HashMap<>();
		try {
			//查询是否有老账号
			userId=this.jt.queryForObject("SELECT `id` FROM `diy_device_user` WHERE `app`=? AND `device_no`=? ORDER BY `ctime` DESC LIMIT 1",new Object[]{user.getApp(),user.getDeviceNo()}, Integer.class);
		} catch (Exception e) {
			final String sql="INSERT INTO `diy_device_user` (`app`,`device_no`,`ctime`) VALUES(?,?,NOW())";
			KeyHolder holder=new GeneratedKeyHolder();
			jt.update(new PreparedStatementCreator() {
				@Override
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement ps=con.prepareStatement(sql, new String[] { "id" });
					ps.setString(1, user.getApp());
					ps.setString(2, user.getDeviceNo());
					return ps;
				}
			},holder);
			userId=holder.getKey().intValue();
			flag=true;
		}
		result.put("flag", flag);
		result.put("userId", userId);
		return result;
	}
	
	@Override
	public Map<String, Object> loginUserByPhone(final User user) {
		Integer userId=null;
		boolean flag=false;
		Map<String, Object> result=new HashMap<>();
		try {
			//查询是否有老账号
			userId=this.jt.queryForObject("SELECT `id` FROM `diy_device_user` WHERE `mobile`=? ORDER BY `ctime` DESC LIMIT 1",new Object[]{user.getMobile()}, Integer.class);
		} catch (Exception e) {
			final String sql="INSERT INTO `diy_device_user` (`app`,`device_no`,`ctime`,`mobile`,`name`) VALUES(?,?,NOW(),?,?)";
			KeyHolder holder=new GeneratedKeyHolder();
			jt.update(new PreparedStatementCreator() {
				@Override
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement ps=con.prepareStatement(sql, new String[] { "id" });
					ps.setString(1, user.getApp()+"-"+user.getMobile());
					ps.setString(2, user.getDeviceNo());
					ps.setString(3, user.getMobile());
					ps.setString(4, user.getMobile().substring(0, 3)+"****"+user.getMobile().substring(7));
					return ps;
				}
			},holder);
			userId=holder.getKey().intValue();
			flag=true;
		}
		result.put("flag", flag);
		result.put("userId", userId);
		return result;
	}
	
	@Override
	public Map<String, Object> loginUserByOpenid(final User user) {
		Integer userId=null;
		boolean flag=false;
		Map<String, Object> result=new HashMap<>();
		try {
			//查询是否有老账号
			StringBuilder sql=new StringBuilder();
			sql.append("SELECT `id` FROM `diy_device_user` WHERE");
			if(StringUtils.isNotBlank(user.getOpenid())){
				sql.append(" `openid`='"+user.getOpenid()+"'");
			}else if(StringUtils.isNotBlank(user.getQqid())){
				sql.append(" `qqid`='"+user.getQqid()+"'");
			}
			sql.append(" LIMIT 1");
			userId=this.jt.queryForObject(sql.toString(), Integer.class);
		} catch (Exception e) {
			String column="";
			String colValue="";
			if(StringUtils.isNotBlank(user.getOpenid())){
				column="`openid`";
				colValue=user.getOpenid();
			}else{
				column="`qqid`";
				colValue=user.getQqid();
			}
			final String finalColValue=colValue;
			final String sql="INSERT INTO `diy_device_user` (`app`,`device_no`,`ctime`,"+column+",`name`,`sex`) VALUES(?,?,NOW(),?,?,?)";
			KeyHolder holder=new GeneratedKeyHolder();
			jt.update(new PreparedStatementCreator() {
				@Override
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement ps=con.prepareStatement(sql, new String[] { "id" });
					if(StringUtils.isNotBlank(user.getOpenid())){
						ps.setString(1, user.getApp()+"-"+user.getOpenid());
					}else{
						ps.setString(1, user.getApp()+"-"+user.getQqid());
					}
					ps.setString(2, user.getDeviceNo());
					ps.setString(3, finalColValue);
					ps.setString(4, user.getName());
					ps.setInt(5, "男".equals(user.getSex())?1:2);
					return ps;
				}
			},holder);
			userId=holder.getKey().intValue();
			flag=true;
		}
		result.put("flag", flag);
		result.put("userId", userId);
		return result;
	}
	
	@Override
	public Map<String, Object> getUserInfo(User user) {
		try {
			return this.jt.queryForMap("SELECT `id`,`mobile`,`name`,`head_url` AS `headUrl`,`sex`,`username` AS `hxUserName`,`password` AS `hxPassWord` FROM `diy_device_user` WHERE `id`=?",new Object[]{user.getId()});
		} catch (Exception e) {
		}
		return null;
	}
	
	@Override
	public void updateInfo(Integer id, String userNameAndPass, String imgurl) {
		if(StringUtils.isBlank(userNameAndPass) && StringUtils.isBlank(imgurl)){
			
		}else{
			StringBuilder sql=new StringBuilder();
			sql.append("UPDATE `diy_device_user` SET ");
			if(StringUtils.isNotBlank(userNameAndPass)){
				sql.append("`userName`='"+userNameAndPass.split("#")[0]+"',`password`='"+userNameAndPass.split("#")[1]+"',");
			}
			if(StringUtils.isNotBlank(imgurl)){
				sql.append("`head_url`='"+imgurl+"',");
			}
			String rsql=sql.substring(0, sql.length()-1)+" WHERE `id`=?";
			this.jt.update(rsql,new Object[]{id});
		}
	}
	
	@Override
	public void updateNewInfo(String userId, String name, String sex, String resultpath) {
		StringBuilder sql=new StringBuilder();
		sql.append("UPDATE `diy_device_user` SET `name`=?,`sex`=?");
		if(StringUtils.isNotBlank(resultpath)){
			sql.append(",`head_url`='"+resultpath+"'");
		}
		sql.append(" WHERE `id`=?");
		this.jt.update(sql.toString(),new Object[]{name,sex,userId});
	}
}
