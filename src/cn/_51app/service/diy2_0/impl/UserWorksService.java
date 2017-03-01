package cn._51app.service.diy2_0.impl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

import cn._51app.dao.diy2_0.ICommodityDao;
import cn._51app.dao.diy2_0.ICouponDao2;
import cn._51app.dao.diy2_0.IShopCartDao;
import cn._51app.dao.diy2_0.IUserWorksDao;
import cn._51app.dao.diy2_0.IZeroGoodDao;
import cn._51app.dao.diy2_0.IndentDao;
import cn._51app.entity.User;
import cn._51app.service.BaseService;
import cn._51app.service.diy2_0.IUserWorksService;
import cn._51app.util.AliSMSUtil;
import cn._51app.util.CommonUtil;
import cn._51app.util.DateUtil;
import cn._51app.util.HttpClientUtil;
import cn._51app.util.JSONUtil;
import cn._51app.util.OCSKey;
import cn._51app.util.PropertiesUtil;
import cn._51app.util.RedisUtil;
import cn._51app.util.WxLoginUtil;
import sun.net.www.content.text.plain;

@Service
public class UserWorksService extends BaseService implements IUserWorksService {
	//图片前缀
	private final String dgurl=PropertiesUtil.getValue("diy.goods.url");
	
	private final String updownloadRootDir =PropertiesUtil.getValue("uploadUrl.sys");
	
	 private static final String userInfo=PropertiesUtil.getValue("diy.user.info");
	 
	 private final String diyRootPath =PropertiesUtil.getValue("diy.root.path");
	 
	 DecimalFormat df=new DecimalFormat("######0.00");
	 
	 private ObjectMapper mapper=new ObjectMapper();
	 
	 private static Logger log=Logger.getLogger(UserWorksService.class);
	
	@Autowired
	private IUserWorksDao iUserWorksDao;
	
	@Autowired
	private IndentDao indentDao;
	
	@Autowired
	private IShopCartDao iShopCartDao;
	
	@Autowired
	private ICommodityDao iCommodityDao;
	
	@Autowired
	private ICouponDao2 iCouponDao2;
	@Autowired
	private IZeroGoodDao iZeroGoodDao;
	
	@Override
	public String userWorks(String id) throws Exception {
		Map<String,Object>paramMap=new HashMap<String,Object>();
		paramMap.put("dgurl",dgurl);
		paramMap.put("id",id);
		return iUserWorksDao.userWorks(paramMap);
	}

	@Override
	public String saveWorks(String name,String cont,String isopen,String saveImg,String goodId, String userId,String textureIds,String type) throws Exception {
		Map<String,Object>paramMap=new HashMap<String,Object>();
		String img[]=saveImg.split("#");
		paramMap.put("saveImg",img[0]);
		if(img.length>1){
			paramMap.put("suffix",img[1]);
		}
		paramMap.put("type",Integer.parseInt(type));
		paramMap.put("name",name);
		paramMap.put("cont",cont);
		paramMap.put("isopen",Integer.parseInt(isopen));
		paramMap.put("goodId",Integer.parseInt(goodId));
		paramMap.put("userId",Integer.parseInt(userId));
		double orgPrice=0;
		double money=0;
		//没有材质id 就是默认材质
		Map<String, Object> mapParam=this.iUserWorksDao.queryDefaultTextureIds(goodId,textureIds);
		textureIds=(String)mapParam.get("texture_ids");
		orgPrice=(double)mapParam.get("org_price");
		money=(double)mapParam.get("now_price");
		//只能是定制商品
		if("3".equals(type)){
			//0元购  原价 结束时间
			orgPrice=this.iZeroGoodDao.getOrgPrice(goodId);
			money=orgPrice;
		}
		String textureNames=indentDao.queryTextureById(textureIds.split("_"));
		paramMap.put("textureNames", textureNames);
		paramMap.put("textureIds",textureIds);
		paramMap.put("orgPrice", orgPrice);
		paramMap.put("money", money);
		String workId=iUserWorksDao.saveWorks(paramMap);
		Map<String, Object> temMap=(Map<String, Object>)JSONUtil.convertJSONToObject(workId, HashMap.class);
		paramMap.put("workId", String.valueOf(temMap.get("workId")));
		RedisUtil.zadd(OCSKey.DIY_WORKLIST_LIKE,0, String.valueOf(temMap.get("workId")));
		return JSONUtil.convertObjectToJSON(paramMap);
	}
	
	@Override
	public boolean delWorks(String ids) throws Exception{
		int result=iUserWorksDao.delWorks(ids);
		if(result==1){
			return true;
		}
		return false;
	}
	
	@Override
	public boolean editWorks(String workId,String cont,String name,String isopen,String textureIds)throws Exception{
		Map<String,Object>paramMap=new HashMap<String,Object>();
		paramMap.put("workId",workId);
		paramMap.put("cont",cont);
		paramMap.put("name",name);
		paramMap.put("isopen",isopen);
		paramMap.put("textureIds",textureIds);
		int result=iUserWorksDao.editWorkds(paramMap);
		if(result==1){
			return true;
		}
		return false;
	}
	
	@Override
	public String worksInfo(String workId)throws Exception{
		Map<String,Object>wokeUser=iUserWorksDao.queryWork(workId);
		String imgUrl=wokeUser.get("imgurl")!=null?wokeUser.get("imgurl").toString():"";
		String headImg=wokeUser.get("head_url")!=null?wokeUser.get("head_url").toString():"";
		String suffix=wokeUser.get("suffix")!=null?wokeUser.get("suffix").toString():"";
		List<String>list=new ArrayList<String>();
		//商品类型（1单面2双面3照片书）
		String goodsType=wokeUser.get("goodsType")!=null?wokeUser.get("goodsType").toString():"";
		String textureIds=wokeUser.get("textureIds")!=null?wokeUser.get("textureIds").toString():"";
		//以下划线分割数据
		String arrIds[]=textureIds.split("_");
		String par="";
		//根据arrIds查询选择属性逗号隔开
		for(int i=0;i<arrIds.length;i++){
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("id",arrIds[i]);
			String param=iCommodityDao.getSelectTexture(map);
			if(i==arrIds.length-1){
				par+=param;
			}else{
				par+=param+",";
			}
		}
		wokeUser.put("head_url",dgurl+headImg);
		wokeUser.put("param",par);
		if(wokeUser.get("now_price")!=null && wokeUser.get("org_price")!=null){
			wokeUser.put("now_price",df.format(Double.valueOf(wokeUser.get("now_price").toString())));
			wokeUser.put("org_price",df.format(Double.valueOf(wokeUser.get("org_price").toString())));
		}
		
		if(goodsType.equals("1")){
			list.add(dgurl+imgUrl+suffix);
			list.add(dgurl+imgUrl+"@b"+suffix);
		}
		//格式化图片
		if(goodsType.equals("2")){
			list.add(dgurl+imgUrl+suffix);
			list.add(dgurl+imgUrl+"@b"+suffix);
			list.add(dgurl+imgUrl+"@p"+suffix);
			list.add(dgurl+imgUrl+"@pb"+suffix);
		}else if(goodsType.equals("3")){
			if(imgUrl.contains("_")){
				String imgs[]=imgUrl.split("_");
				int count=Integer.parseInt(imgs[1]);
				for(int i=0;i<count;i++){
					list.add(dgurl+imgs[0]+"_"+(i+1)+"@cut"+suffix);
				}
			}
		}
		
		wokeUser.put("imgurl",list);
		return super.toJson(wokeUser);
	}
	
	/**
	 * 发送验证码
	 */
	@Override
	public boolean sendMsg(String phone,String smscode) throws Exception{
		return AliSMSUtil.sendMsg(phone, smscode);
	}
	
	@Override
	public String login(User user) {
		Map<String, Object> result=new HashMap<>();
		String imgurl=null;
		if(("0".equals(user.getMobile()) || StringUtils.isBlank(user.getMobile())) && StringUtils.isBlank(user.getOpenid()) && StringUtils.isBlank(user.getQqid())){//注册默认账号  app+deviceNo
			result=this.iUserWorksDao.createNewUser(user);
			result.put("type", "default");
		}else if(StringUtils.isNotBlank(user.getMobile()) && !"0".equals(user.getMobile())){//手机号注册/登录
			result=this.iUserWorksDao.loginUserByPhone(user);
			result.put("type", "phone");
		}else if(StringUtils.isNotBlank(user.getOpenid()) || StringUtils.isNotBlank(user.getQqid())){//微信/QQ授权登录
			result=this.iUserWorksDao.loginUserByOpenid(user);
			//是否要处理头像
			boolean flag=(Boolean)result.get("flag");
			if(flag){//首次保存图片  非首次不用处理
				try {
					String pathName=new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());
					//随机名
					String uuid=UUID.randomUUID().toString().replaceAll("-","").substring(0,8);
					byte[] bytes=HttpClientUtil.getImg(user.getImgurl());
					String path=updownloadRootDir+userInfo+pathName;
					File pathfile=new File(path);
					pathfile.mkdirs();
					File resultFile=new File(path+"/"+uuid+".jpg");
					if(!resultFile.exists()){
						resultFile.createNewFile();
					}
					FileOutputStream file=new FileOutputStream(resultFile);
					file.write(bytes);
					file.flush();
					file.close();
					//保存图片地址
					imgurl=userInfo+pathName+"/"+uuid+".jpg";
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			result.put("type", "openid");
		}
		user.setId((Integer)result.get("userId"));
		Map<String, Object> data=this.iUserWorksDao.getUserInfo(user);
		if(StringUtils.isBlank((String)data.get("headUrl"))){
			data.put("headUrl", imgurl);
		}
		String hxUserName="";
		String hxPassWord="";
		String userNameAndPass="";
		if(data!=null){
			hxUserName=(String)data.get("hxUserName");
			hxPassWord=(String)data.get("hxPassWord");
			if(StringUtils.isBlank(hxPassWord) || StringUtils.isBlank(hxUserName)){
				try {
					userNameAndPass=this.HXtoBinding();
					data.put("hxUserName", userNameAndPass.split("#")[0]);
					data.put("hxPassWord", userNameAndPass.split("#")[1]);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		//更新环信和图片地址
		this.iUserWorksDao.updateInfo(user.getId(),userNameAndPass,imgurl);
		boolean flag=(Boolean)result.get("flag");
		String type=(String)result.get("type");
		if(flag){//首次登录的账号  需要绑定之前的信息到现在的账号上
			//插入砍价券
			Map<String, String> cutCoupon=RedisUtil.hgetAll(OCSKey.DIY_CUTPRICE_ID_+12);
			cutCoupon.put("valid", DateUtil.operDay(DateUtil.date2String(new Date(), DateUtil.FORMAT_DATE), 7));
			//删除砍价券id
			//生成自己的砍价券id  用List有顺序存起来
			long myCouponId=RedisUtil.incr(OCSKey.DIY_CUTPRICE_COUPONID);
			//插入用户砍价券id列表
			RedisUtil.lpush(OCSKey.DIY_COUPONLIST_USERID_+user.getId(), myCouponId+"");
			cutCoupon.put("id", myCouponId+"");
			cutCoupon.put("islottery", "false"); //砍价券是否是抽奖获得的
			cutCoupon.put("ctime", DateUtil.date2String(DateUtil.getCurrentDate(),DateUtil.FORMAT_DATE));
			cutCoupon.put("status", "true"); //砍价券未使用
			cutCoupon.put("userId", user.getId()+""); //砍价券属于用户id
			//插入用户砍价券信息
			RedisUtil.hset(OCSKey.DIY_CUTPRICE_COUPONLIST,myCouponId+"", JSONUtil.convertObjectToJSON(cutCoupon));
		}
		if(("openid").equals(type) || ("phone").equals(type)){//绑定之前的数据
			//绑定优惠券
			//优惠券绑定操作
			List<Map<String,Object>>nl_coupon=iCouponDao2.getCouponListNotLogin2(user);
			List<Map<String,Object>>ed_coupon=iCouponDao2.getCouponListlogined2(user);
			boolean cflag=true;
			for(Map<String,Object>nl_m : nl_coupon){
				String couponId=nl_m.get("coupon_id")==null?"":nl_m.get("coupon_id").toString();
				if(!couponId.equals("")){
					for(Map<String,Object>ed_m : ed_coupon){
						String couponId2=ed_m.get("coupon_id")==null?"":ed_m.get("coupon_id").toString();
						if(!couponId2.equals("")){
							if(couponId.equals(couponId2)){
								//相同优惠券不进行绑定
								cflag=false;
							}
						}
					}
					//绑定优惠券
					if(cflag){
						iUserWorksDao.bindingCoupon2(user,couponId);
					}
				}
			}
			//绑定购物车
			int resultNum=iUserWorksDao.bindingShop2(user);
			if(resultNum>0){
				Map<String, Object> shopMap=this.iShopCartDao.getOrderShopNum(user.getDeviceNo(),user.getApp());
				if(shopMap!=null){
					int shopNum=Integer.parseInt(shopMap.get("shopNum").toString());
					resultNum=this.indentDao.updateShopOrderNum(user.getDeviceNo(), user.getApp(), "shop", -shopNum);
					resultNum=this.indentDao.updateShopOrderNumForId(user.getId()+"", "shop", shopNum);
				}
			}
			
			resultNum=iUserWorksDao.bindingAddress2(user);
			
			resultNum=iUserWorksDao.bindingOrders2(user);

			resultNum=iUserWorksDao.bindingWork2(user);
		}
		String headUrl=(String)data.get("headUrl");
		String sex=(String)data.get("sex");
		String name=(String)data.get("name");
		String headUrl_=(String)data.get("headUrl");
		String hxUserName_=(String)data.get("hxUserName");
		String hxPassWord_=(String)data.get("hxPassWord");
		String mobile=(String)data.get("mobile");
		data.put("name", name==null?"":name);
		data.put("headUrl", headUrl_==null?"":headUrl_);
		data.put("hxUserName", hxUserName_==null?"":hxUserName_);
		data.put("hxPassWord", hxPassWord_==null?"":hxPassWord_);
		if(StringUtils.isNotBlank(headUrl)){
			data.put("headUrl", "http://file.diy.51app.cn/"+headUrl);
		}
		if("1".equals(sex) || StringUtils.isBlank(sex)){
			data.put("sex", "男");
		}else{
			data.put("sex", "女");
		}
		if("0".equals(mobile)){
			data.put("mobile", "");
		}
		boolean isFirst=(Boolean)result.get("flag");
		data.put("isFirst",isFirst);
		return JSONUtil.convertObjectToJSON(data);
	}
	
	@Override
	public String editUserInfo(MultipartFile imgurl,String nickName,String phone,String sex,String openid,String app,String deviceNo,String token)throws Exception{
		String pathName=new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());
		//随机名
		String uuid=UUID.randomUUID().toString().replaceAll("-","").substring(0,8);
		Map<String,Object>paramMap=new HashMap<String,Object>();
		File file=new File(updownloadRootDir+userInfo+pathName);
		String resultPath=pathName+"/"+uuid;
		String sufFormat=".jpg";
		//生成文件夹
		if(!file.exists()){
			file.mkdirs();
		}
		if(imgurl!=null && !imgurl.isEmpty()){
			File imgFilePath=new File(updownloadRootDir+userInfo+resultPath+sufFormat);
			if(!imgFilePath.exists()){
				imgFilePath.createNewFile();
			}
			imgurl.transferTo(imgFilePath);
			paramMap.put("imgurl", userInfo+resultPath+sufFormat);
		}
		paramMap.put("nickName", nickName);
		paramMap.put("phone", phone);
		if(sex!=null)
		paramMap.put("sex", sex.equals("男")?"1":"2");
		//设置app合并openid
		
		paramMap.put("capp",app);
		if(StringUtils.isNotBlank(phone))
			paramMap.put("app",app+phone);
		else if(StringUtils.isNotBlank(openid))
			paramMap.put("app",app+openid);
		else
			paramMap.put("app", app);
		paramMap.put("deviceNo",deviceNo);
		paramMap.put("openid",openid);
		paramMap.put("dgurl", dgurl);
		int result=0;
		//操作位,1第一次授权登陆，2不一样openid但一样设备的用户注册并绑定
		boolean flag=false;
		//单个用户信息
		Map<String,Object>userMap=null;
			//查询用户信息
			Map<String,Object> infoMap=iUserWorksDao.loadUserInfoById(paramMap);
//			//无openid时候返回用户数据
//			if(StringUtils.isBlank(openid)){
//				return super.toJson(infoMap);
//			}
		if(infoMap==null){
			//插入手机号用户
			if(StringUtils.isBlank(openid) && StringUtils.isNotBlank(phone)){
				result=iUserWorksDao.insertUser(paramMap);
			}else if(StringUtils.isNotBlank(openid)){//插入授权账号
				result=iUserWorksDao.insertUser(paramMap);
			}
//			if(result!=1){
//				throw new RuntimeException();
//			}
			if(result!=1){
				flag=false;
			}else{
				flag=true;
			}
		}
	
		//用户授权，第一次进行绑定,并绑定环信账号
		if(flag){
			if(StringUtils.isNotBlank(openid)){
				//查询用户信息
				Map<String,Object> map=iUserWorksDao.loadUserInfoById(paramMap);
				Integer userId=(Integer)map.get("id");
				//插入砍价券
				Map<String, String> cutCoupon=RedisUtil.hgetAll(OCSKey.DIY_CUTPRICE_ID_+12);
				cutCoupon.put("valid", DateUtil.operDay(DateUtil.date2String(new Date(), DateUtil.FORMAT_DATE), 7));
				//删除砍价券id
				//生成自己的砍价券id  用List有顺序存起来
				long myCouponId=RedisUtil.incr(OCSKey.DIY_CUTPRICE_COUPONID);
				//插入用户砍价券id列表
				RedisUtil.lpush(OCSKey.DIY_COUPONLIST_USERID_+userId, myCouponId+"");
				cutCoupon.put("id", myCouponId+"");
				cutCoupon.put("islottery", "false"); //砍价券是否是抽奖获得的
				cutCoupon.put("ctime", DateUtil.date2String(DateUtil.getCurrentDate(),DateUtil.FORMAT_DATE));
				cutCoupon.put("status", "true"); //砍价券未使用
				cutCoupon.put("userId", userId+""); //砍价券属于用户id
				//插入用户砍价券信息
				RedisUtil.hset(OCSKey.DIY_CUTPRICE_COUPONLIST,myCouponId+"", JSONUtil.convertObjectToJSON(cutCoupon));
				userMap=map;
				//注册环信账号
				Map<String,Object>m=returnOne(Integer.parseInt(map.get("id").toString()),token);
				map.put("hxUserName", m.get("username"));
				map.put("hxPassWord", m.get("password"));
				
				//绑定之前用原来的app
				paramMap.put("capp",app);
				
				//优惠券绑定操作
				List<Map<String,Object>>nl_coupon=iCouponDao2.getCouponListNotLogin(paramMap);
				List<Map<String,Object>>ed_coupon=iCouponDao2.getCouponListlogined(paramMap);
				boolean cflag=true;
				for(Map<String,Object>nl_m : nl_coupon){
					String couponId=nl_m.get("coupon_id")==null?"":nl_m.get("coupon_id").toString();
					if(!couponId.equals("")){
						for(Map<String,Object>ed_m : ed_coupon){
							String couponId2=ed_m.get("coupon_id")==null?"":ed_m.get("coupon_id").toString();
							if(!couponId2.equals("")){
								if(couponId.equals(couponId2)){
									//相同优惠券不进行绑定
									cflag=false;
								}
							}
						}
						//绑定优惠券
						if(cflag){
							paramMap.put("coupon_id", couponId);
							result=iUserWorksDao.bindingCoupon(paramMap);
						}
					}
				}
				
				result=iUserWorksDao.bindingShop(paramMap);
				if(result>=1){
					Map<String, Object> shopMap=this.iShopCartDao.getOrderShopNum(deviceNo,app);
					if(shopMap!=null){
						int shopNum=Integer.parseInt(shopMap.get("shopNum").toString());
						result=this.indentDao.updateShopOrderNum(deviceNo, app, "shop", -shopNum);
						result=this.indentDao.updateShopOrderNum(deviceNo,app+openid, "shop", shopNum);
					}
				}
				
				result=iUserWorksDao.bindingAddress(paramMap);
				
				result=iUserWorksDao.bindingEvaluation(paramMap);
				
				result=iUserWorksDao.bindingOrders(paramMap);
	
				result=iUserWorksDao.bindingWork(paramMap);
			}
			return super.toJson(userMap);
		//修改完全指定的用户
		}else{
			//都空就是登陆
			if((imgurl!=null && !imgurl.isEmpty()) || StringUtils.isNotBlank(nickName) || StringUtils.isNotBlank(phone) || StringUtils.isNotBlank(sex)){
				result= iUserWorksDao.editUserInfoAndOpenid(paramMap);
				if(result<=0){
					throw new RuntimeException();
				}
			}
			//查询用户信息
			Map<String,Object> map=iUserWorksDao.loadUserInfoById(paramMap);
			//绑定之前用原来的app
			paramMap.put("capp",app);
			
			//优惠券绑定操作
			List<Map<String,Object>>nl_coupon=iCouponDao2.getCouponListNotLogin(paramMap);
			List<Map<String,Object>>ed_coupon=iCouponDao2.getCouponListlogined(paramMap);
			boolean cflag=true;
			for(Map<String,Object>nl_m : nl_coupon){
				String couponId=nl_m.get("coupon_id")==null?"":nl_m.get("coupon_id").toString();
				if(!couponId.equals("")){
					for(Map<String,Object>ed_m : ed_coupon){
						String couponId2=ed_m.get("coupon_id")==null?"":ed_m.get("coupon_id").toString();
						if(!couponId2.equals("")){
							if(couponId.equals(couponId2)){
								//相同优惠券不进行绑定
								cflag=false;
							}
						}
					}
					//绑定优惠券
					if(cflag){
						paramMap.put("coupon_id", couponId);
						result=iUserWorksDao.bindingCoupon(paramMap);
					}
				}
			}
			
			result=iUserWorksDao.bindingShop(paramMap);
			if(result>=1){
				Map<String, Object> shopMap=this.iShopCartDao.getOrderShopNum(deviceNo,app);
				if(shopMap!=null){
					int shopNum=Integer.parseInt(shopMap.get("shopNum").toString());
					result=this.indentDao.updateShopOrderNum(deviceNo, app, "shop", -shopNum);
					result=this.indentDao.updateShopOrderNum(deviceNo,app+openid, "shop", shopNum);
				}
			}
			
			result=iUserWorksDao.bindingAddress(paramMap);
			
			result=iUserWorksDao.bindingEvaluation(paramMap);
			
			result=iUserWorksDao.bindingOrders(paramMap);
			
			result=iUserWorksDao.bindingWork(paramMap);
			
			return super.toJson(map);
		}
	}
	
	@Override
	public String editUserInfoForAndroid(MultipartFile imgurl,String nickName,String phone,String sex,String openid,String app,String deviceNo,String state)throws Exception{
		if(StringUtils.isBlank(state)){
			return "-1";
		}
		String pathName=new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());
		//随机名
		String uuid=UUID.randomUUID().toString().replaceAll("-","").substring(0,8);
		Map<String,Object>paramMap=new HashMap<String,Object>();
		File file=new File(updownloadRootDir+userInfo+pathName);
		String resultPath=pathName+"/"+uuid;
		String sufFormat=".jpg";
		//生成文件夹
		if(!file.exists()){
			file.mkdirs();
		}
		if(imgurl!=null && !imgurl.isEmpty()){
			File imgFilePath=new File(updownloadRootDir+userInfo+resultPath+sufFormat);
			if(!imgFilePath.exists()){
				imgFilePath.createNewFile();
			}
			imgurl.transferTo(imgFilePath);
			paramMap.put("imgurl", userInfo+resultPath+sufFormat);
		}
		paramMap.put("nickName", nickName);
		paramMap.put("phone", phone);
		if(sex!=null)
		paramMap.put("sex", sex.equals("男")?"1":"2");
		//设置app,新的绑定一个app
		if(state.equals("2") || state.equals("3"))
		paramMap.put("app",app);
		else
		paramMap.put("app",app+"_accredit");
		
		paramMap.put("deviceNo",deviceNo);
		paramMap.put("openid",openid);
		paramMap.put("dgurl", dgurl);
		paramMap.put("state",state);
		
		//手机号登陆，时候可能会查到多个与这个手机号绑定过的账号,不允许注册
		if(state.equals("1")){
			//查询用户信息
			List<Map<String,Object>> infoList=iUserWorksDao.loadUserInfoByIdWithState(paramMap);
			if(infoList!=null && !infoList.isEmpty()){
				return super.toJson(infoList);
			}
			return "-1";
		}
		//qq授权登陆,查不到用户就去注册并绑定一个 //微信授权登陆     同用
		if(state.equals("2") || state.equals("3")){
			if(openid==null || openid.isEmpty()){
				return "-3";
			}
			List<Map<String,Object>> infoList=iUserWorksDao.loadUserInfoByIdWithState(paramMap);
			if(infoList!=null && !infoList.isEmpty()){
				return super.toJson(infoList);
			}else{
				String hx[]=HXtoBinding().split("#");
				int times=0;
				//循环，最多随机3次
				while(hx.length!=2 || times<2){
					hx=HXtoBinding().split("#");
					times++;
				}
				int result=iUserWorksDao.insertUser(paramMap);
				if(result>0){ 
					infoList=iUserWorksDao.loadUserInfoByIdWithState(paramMap);
					return super.toJson(infoList);
				}else{
					return "-2";
				}
			}
		}
		//修改或绑定openid或绑定qqopenid
		if(state.equals("4")){
			int result=iUserWorksDao.editUserInfo(paramMap);
			if(result<=0){
				throw new RuntimeException();
			}
		}	
		return null;
	}
	
	@Override
	public String loadUserInfo(String deviceNo,String app,String token)throws Exception{
		Map<String,Object>paramMap=new HashMap<String,Object>();
		paramMap.put("deviceNo", deviceNo);
		paramMap.put("app",app);
		paramMap.put("dgurl", dgurl);
		Map<String,Object>userMap=iUserWorksDao.loadUserInfo(paramMap);
		Map<String,Object>m=returnOne(Integer.parseInt(userMap.get("id").toString()),token);
		userMap.put("hxUserName", m.get("username"));
		userMap.put("hxPassWord", m.get("password"));
		return super.toJson(userMap);
	}
	
	@Override
	public String weixinLogin(String code)throws Exception{
		Map<String, Object> result = null;
		try {
			Map<String, Object> map = WxLoginUtil.getAccessToken(code);
			String openid = null==map.get("openid")?"":map.get("openid").toString();
			if(StringUtils.isNotEmpty(openid)){
				if(!haveOpenid(openid)){
					String access_token = null==map.get("access_token")?"":map.get("access_token").toString();
					Map<String, Object> userMap = WxLoginUtil.getUserinfo(access_token, openid);
					String nickname = null==userMap.get("nickname")?"":userMap.get("nickname").toString();
					String headimgurl = null==userMap.get("headimgurl")?"":userMap.get("headimgurl").toString();
					String re="";
			    	for (int i = 0; i < nickname.length(); i++) {
			            char  item =  nickname.charAt(i);
			            if(isMessyCode(String.valueOf(item))){//如果是乱码
			            	continue;
			            }else{
			            	re+=item;
			            }
			            
			        }
			    	nickname=re;
					insertWx(openid, nickname, headimgurl);
				}
				result = iUserWorksDao.findUser4Openid(openid);
			}else{
				log.info("WxLogin openid=null");
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.info("WxLogin error!!!");
		}
		return super.toJson(result);
	}
	
	/**
	 * 插入微信用户
	 * @author zhanglz
	 * @return 
	 */
	public boolean insertWx(String openid,String name,String head_url){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("app", WxLoginUtil.wx_user_app);
		map.put("device_no", CommonUtil.createOrderNo("U", 5)+"-"+CommonUtil.createOrderNo("U", 5));
		map.put("openid", openid);
		map.put("name", name);
		map.put("head_url", head_url);
		map.put("mobile", 0);
		return iUserWorksDao.insertOpenid(map)==1;
	}
	
	/**
	 * TODO 微信乱码转码
	 * @param strName
	 * @return
	 */
	public static boolean isMessyCode(String strName) {  
        Pattern p = Pattern.compile("\\s*|\t*|\r*|\n*");  
        Matcher m = p.matcher(strName);  
        String after = m.replaceAll("");  
        String temp = after.replaceAll("\\p{P}", "");  
        char[] ch = temp.trim().toCharArray();  
        float chLength = 0 ;  
        float count = 0;  
        for (int i = 0; i < ch.length; i++) {  
            char c = ch[i];  
            if (!Character.isLetterOrDigit(c)) {  
                if (!isChinese(c)) {  
                    count = count + 1;  
                }  
                chLength++;   
            }  
        }  
        float result = count / chLength ;  
        if (result > 0.4) {  
            return true;  
        } else {  
            return false;  
        }  
    }
	private static boolean isChinese(char c) {  
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);  
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS  
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS  
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A  
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION  
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION  
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {  
            return true;  
        }  
        return false;  
    }  
	
	@Override
	public boolean haveOpenid(String openid){
		return iUserWorksDao.haveOpenid(openid)==1;
	}
	
	@Override
	public boolean insertUser(String deviceNo,String app){
		int result=iUserWorksDao.insertUserHome(deviceNo,app);
		if(result==1){
			return true;
		}
		return false;
	}
	/*
	 * 环信需要的账号密码
	 */
	public Map<String,Object> returnOne(int id,String token) throws Exception{
		String url="https://a1.easemob.com/1717/weiyoupin/users";
		Map<String,Object> map = iUserWorksDao.returnPassword(id);
		String json = super.toJson(map);

		String username = json.substring(json.indexOf(":")+1,json.indexOf(","));

		String password = json.substring(json.lastIndexOf(":")+1, json.indexOf("}"));

		if("\"\"".equals(password)|"null".equals(password)|"null".equals(username)|"\"\"".equals(username)){
			password = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 10);
			username = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 10);
			Map<String,Object> paramMap = new HashMap<String,Object>();
			paramMap.put("id", id);
			paramMap.put("username", username);
			paramMap.put("password", password);
			HttpClientUtil.httpPostRequest(url, "{\"username\":\""+paramMap.get("username")+"\",\"password\":\""+paramMap.get("password")+"\"}", "json", token);

			iUserWorksDao.insertPassword(paramMap);
		}
		return iUserWorksDao.returnPassword(id);
	}
	
	/**
	 * TODO 随机环信号和注册环信账号（不重复）
	 * @return 用户名#密码
	 * @throws Exception
	 */
	public String HXtoBinding() throws Exception{
		String tokenUrl="https://a1.easemob.com/1717/weiyoupin/token";
		String clientId = "YXA6TgFOcGm-EeaKO5-bd8iPGg";
		String clientSecret = "YXA6lmlcc-mVxoARnQByVWClU9fjFrs";
		
		//环信注册返回token
		String tokenData=HttpClientUtil.httpPostRequest(tokenUrl, "{\"grant_type\":\"client_credentials\",\"client_id\":\""+clientId+"\",\"client_secret\":\""+clientSecret+"\"}","json");
		//获取token
		String token = tokenData.substring(tokenData.indexOf(":")+2, tokenData.indexOf(",")-1);
		//随机生成环信账号和密码
		String password = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 10);
		String username = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 10);
		
		//是否被注册过
		boolean isUsed = iUserWorksDao.checkHxUserName(username);
		
		if(!isUsed){
			String url="https://a1.easemob.com/1717/weiyoupin/users";
			//环信注册一个账号密码
			HttpClientUtil.httpPostRequest(url, "{\"username\":\""+username+"\",\"password\":\""+password+"\"}", "json", token);
		}

		return username+"#"+password;
	}
	
	@Override
	public boolean infoUser(String deviceNo, String app, String version, String deviceToken) {
		return this.iUserWorksDao.infoUser(deviceNo,app,version,deviceToken);
	}

	@Override
	public String createOrder(String workId,String num, String textureIds, String app, String deviceNo)throws Exception{
		Map<String, Object> resultMap=new HashMap<>();
		Map<String,Object> activityMap=null;
		List<Map<String,Object>> list=null;
		String imgUrl,suffix,goodId,sufFormat="";
		String orderNo=CommonUtil.createOrderNo("A", 5);
		Map<String,Object>wokeUser=this.iUserWorksDao.queryUserWork(workId);
		if(wokeUser!=null){
			imgUrl=wokeUser.get("imgurl")!=null?wokeUser.get("imgurl").toString():"";
			suffix=wokeUser.get("suffix")!=null?wokeUser.get("suffix").toString():"";
			goodId=wokeUser.get("goodsId")!=null?wokeUser.get("goodsId").toString():"";
			sufFormat=wokeUser.get("suffix")!=null?wokeUser.get("suffix").toString():"";
			//插入到订单表
			Map<String, Object> returnMap=this.indentDao.createOrder(imgUrl+suffix,goodId,textureIds,deviceNo,app,orderNo,sufFormat,num,false,workId);
			
			activityMap=new HashMap<String,Object>();
			list=new ArrayList<Map<String,Object>>();
			List<Map<String,Object>> shopList=new ArrayList<Map<String,Object>>();
			Map<String,Object> map=new HashMap<String,Object>();
			String type=returnMap.get("type")==null?"0":returnMap.get("type").toString();
			activityMap.put("Num"+type,num);
			activityMap.put("Price"+type,returnMap.get("orgPrice"));
			list.add(returnMap);
			
			map.put("feeNum", resultMap.get("feeNum"));
			map.put("feePostage",resultMap.get("feePostage"));
			map.put("postage",returnMap.get("postage"));
			
			map.put("orderNo", orderNo);
			map.put("sortName",returnMap.get("sortName"));
			map.put("textureName", returnMap.get("textureNames"));
			map.put("imgUrl", diyRootPath+imgUrl+suffix);
			map.put("fileType", sufFormat);
			map.put("num", num);
			map.put("name", returnMap.get("name"));
			map.put("transportfee",resultMap.get("transportfee"));
			map.put("nowPrice", returnMap.get("orgPrice"));
			map.put("type", returnMap.get("type"));
			
//			List<Map<String,Object>> resultList=CommonUtil.getActivityMethod(activityMap,list);
			shopList.add(map);
//			resultMap.put("activityList", resultList);
			resultMap.put("shopList",shopList);
			resultMap.put("companyName",returnMap.get("companyName"));
			resultMap.put("companyId",returnMap.get("user_id"));
			return mapper.writeValueAsString(resultMap);
		}
		return null;
	}
	
	@Override
	public String createOrderForId(String workId,String num, String textureIds, String userId,String lettering,String modId)throws Exception{
		Map<String, Object> resultMap=new HashMap<>();
		Map<String,Object> activityMap=null;
		List<Map<String,Object>> list=null;
		String imgUrl,suffix,goodId,userIds,sufFormat="";
		String orderNo=CommonUtil.createOrderNo("A", 5);
		Map<String,Object>wokeUser=this.iUserWorksDao.queryUserWork(workId);
		Map<String,Object> companyMap=new HashMap<String,Object>();
		if(wokeUser!=null){
			imgUrl=wokeUser.get("imgurl")!=null?wokeUser.get("imgurl").toString():"";
			suffix=wokeUser.get("suffix")!=null?wokeUser.get("suffix").toString():"";
			goodId=wokeUser.get("goodsId")!=null?wokeUser.get("goodsId").toString():"";
			sufFormat=wokeUser.get("suffix")!=null?wokeUser.get("suffix").toString():"";
			userIds=wokeUser.get("userId")!=null?wokeUser.get("userId").toString():"";
			
			//插入到订单表
			Map<String, Object> returnMap=this.indentDao.createOrderForId(imgUrl,goodId,textureIds,userId,orderNo,sufFormat,num,true,workId,lettering,modId);
			if(returnMap==null){
				return null;
			}
			activityMap=new HashMap<String,Object>();
			list=new ArrayList<Map<String,Object>>();
			List<Map<String,Object>> shopList=new ArrayList<Map<String,Object>>();
			Map<String,Object> map=new HashMap<String,Object>();
			
			String actId=returnMap.get("actId")==null?"0":returnMap.get("actId").toString();
			String type=returnMap.get("type")==null?"0":returnMap.get("type").toString();
			activityMap.put("Num"+type+"_"+userIds,num);
			activityMap.put("Price"+type+"_"+userIds,returnMap.get("orgPrice"));
			list.add(returnMap);
			
			//按商家把活动分开
			String act= companyMap.get(userIds)==null?"":companyMap.get(userIds).toString();
			if(act.equals("")){
				//查询活动的详情
				Map<String,Object>typeInfo=indentDao.getTypeMap(actId);
				if(typeInfo!=null)
				typeInfo.put("companyId",userIds);
				companyMap.put(userIds,typeInfo);
			}
			
//			map.put("feeNum", resultMap.get("feeNum"));
//			map.put("feePostage",resultMap.get("feePostage"));
//			map.put("postage",returnMap.get("postage"));
			
			map.put("orderNo", orderNo);
			map.put("sortName",returnMap.get("sortName"));
			map.put("textureName", returnMap.get("textureNames"));
			map.put("imgUrl", diyRootPath+imgUrl+suffix);
			map.put("fileType", sufFormat);
			map.put("num", num);
			map.put("name", returnMap.get("name"));
			map.put("transportfee",resultMap.get("transportfee"));
			map.put("nowPrice", returnMap.get("orgPrice"));
			map.put("type", returnMap.get("type"));
			map.put("isselect", 1);
			map.put("ispostage", returnMap.get("ispostage"));
			map.put("cover_size", returnMap.get("cover_size"));
			map.put("wh_size", returnMap.get("wh_size"));
			map.put("origin", returnMap.get("origin"));
			String pre_url=(String) returnMap.get("pre_url");
			String coverImg=(String) returnMap.get("coverImg");
			map.put("pre_url", pre_url==null?"":diyRootPath+pre_url);
			map.put("coverImg", coverImg==null?"":diyRootPath+coverImg);
			
			List<Map<String,Object>> resultList=CommonUtil.getActivityMethod(activityMap,companyMap);
			shopList.add(map);
			resultMap.put("activityList", resultList);
			resultMap.put("shopList",shopList);
			resultMap.put("companyId",returnMap.get("user_id"));
			resultMap.put("transportfee", returnMap.get("transportfee"));
			resultMap.put("remotefee", returnMap.get("remotefee"));
			resultMap.put("enoughMoney", returnMap.get("enoughMoney"));
			return mapper.writeValueAsString(resultMap);
		}
		return null;
	}

	@Override
	public boolean addShops(String workId, String num, String textureIds, String app, String deviceNo) throws Exception {
		String orderNo=CommonUtil.createOrderNo("G", 3);
		String imgUrl,suffix,goodId,sufFormat="";
		try {
			Map<String,Object>wokeUser=this.iUserWorksDao.queryUserWork(workId);
			if(wokeUser!=null){
				imgUrl=wokeUser.get("imgurl")!=null?wokeUser.get("imgurl").toString():"";
				suffix=wokeUser.get("suffix")!=null?wokeUser.get("suffix").toString():"";
				goodId=wokeUser.get("goodsId")!=null?wokeUser.get("goodsId").toString():"";
				sufFormat=wokeUser.get("suffix")!=null?wokeUser.get("suffix").toString():"";
				//插入到购物车
				int result=this.iShopCartDao.createShop(imgUrl+suffix,goodId,textureIds,deviceNo,app,orderNo,sufFormat,num,Integer.parseInt(workId),"","");
				if(result>0){
					return true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	@Override
	public boolean addShopsForId(String workId, String num, String textureIds, String userId,String lettering,String modId) throws Exception {
		String orderNo=CommonUtil.createOrderNo("G", 3);
		String imgUrl,goodId,sufFormat="";
		try {
			Map<String,Object>wokeUser=this.iUserWorksDao.queryUserWork(workId);
			if(wokeUser!=null){
				Integer goodsId_=(Integer)wokeUser.get("goodsId");
				String suffix_=(String)wokeUser.get("suffix");
				String imgurl_=(String)wokeUser.get("imgurl");
				Integer type=(Integer)wokeUser.get("type");
				Integer cutStatus=(Integer)wokeUser.get("cutStatus");
				goodId=goodsId_==null?"":goodsId_+"";
				imgUrl=imgurl_==null?"":imgurl_;
				sufFormat=suffix_==null?"":suffix_;
				//如果是0元购只能数量1
//				if(this.iUserWorksDao.checkIsZeroGood(workId)){
//					num="1";
//					this.iZeroGoodDao.updateCutStatus(workId,"3");
//				}
				if(type==3){
					num="1";
					//已经购买过  或者在购物车订单中有了
					boolean isShopOrOrder=this.iUserWorksDao.isShopOrOrder(userId,workId);
					if(cutStatus==3 || isShopOrOrder){ 
						workId=null;
					}
				}
				//插入到购物车
				int result=this.iShopCartDao.createShopForId(imgUrl,goodId,textureIds,userId,orderNo,sufFormat,num,workId,lettering,modId);
				if(result>0){
					return true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	@Override
	public boolean amendShop()throws Exception{
		int result=this.iShopCartDao.amendShop();
		if(result>0){
			return true;
		}
		return false;
	}
	
	@Override
	public boolean retroaction(String contact,String content)throws Exception{
		int result=this.iUserWorksDao.retroaction(contact,content);
		if(result>0){
			return true;
		}
		return false;
	}
	
	
	@Override
	public void updateZeroStatus(String orderNo) {
		this.iUserWorksDao.updateZeroStatus(orderNo);
	}
	
	@Override
	public boolean editUser(MultipartFile imgurl, String name,  String sex, String userId) {
		String resultpath="";
		if(imgurl!=null && !imgurl.isEmpty()){
			String pathName=new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());
			//随机名
			String uuid=UUID.randomUUID().toString().replaceAll("-","").substring(0,8);
			String path=updownloadRootDir+userInfo+pathName;
			File pathfile=new File(path);
			pathfile.mkdirs();
			File resultFile=new File(path+"/"+uuid+".jpg");
			if(!resultFile.exists()){
				try {
					resultFile.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			try {
				imgurl.transferTo(resultFile);
			} catch (Exception e) {
				e.printStackTrace();
			} 
			//保存图片地址
			resultpath=userInfo+pathName+"/"+uuid+".jpg";
		}
		this.iUserWorksDao.updateNewInfo(userId,name,"男".equals(sex)?"1":"2",resultpath);
		return true;
	}
	
}
