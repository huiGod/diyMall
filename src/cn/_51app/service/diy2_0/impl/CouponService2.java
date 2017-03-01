package cn._51app.service.diy2_0.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import cn._51app.dao.diy2_0.impl.CouponDao2;
import cn._51app.service.BaseService;
import cn._51app.service.diy2_0.ICouponService2;
import cn._51app.util.JSONUtil;
import cn._51app.util.PropertiesUtil;

@Service
public class CouponService2 extends BaseService implements ICouponService2 {
	
	private ObjectMapper mapper=new ObjectMapper();
	
	//图片前缀
	private String dgurl=PropertiesUtil.getValue("diy.goods.url");
	
	@Autowired
	private CouponDao2 couponDao2;
	
	@Override
	public String couponList(String deviceNo,String app) throws Exception{
		Map<String,Object>map= this.couponDao2.couponList(deviceNo, app,dgurl);
		return super.toJson(map);
	}
	
	@Override
	public String couponListForId(String userId) throws Exception{
		Map<String,Object>map= this.couponDao2.couponListForId(userId,dgurl);
		return super.toJson(map);
	}
	
	@Override
	public String couponCenterType(String deviceNo,String app,String type)throws Exception{
		Map<String,Object>paramMap=new HashMap<String,Object>();
		paramMap.put("deviceNo",deviceNo);
		paramMap.put("app",app);
		paramMap.put("dgurl",dgurl);
		paramMap.put("type",type);
		return this.couponDao2.couponCenterType(paramMap);
	}
	
	@Override
	public String couponCenterForId(String userId)throws Exception{
		Map<String,Object>paramMap=new HashMap<String,Object>();
		paramMap.put("userId",userId);
		paramMap.put("dgurl",dgurl);
		return this.couponDao2.couponCenterForId(paramMap);
	}
	
	@Override
	public boolean addCoupon(String valid,String deviceNo,String app,String sys,String id)throws Exception{
		Map<String,Object>paramMap=new HashMap<String,Object>();
		paramMap.put("valid",valid);
		paramMap.put("deviceNo",deviceNo);
		paramMap.put("app",app);
		paramMap.put("sys",sys);
		paramMap.put("id",id);
		int result=this.couponDao2.addCoupon(paramMap);
		if(result==1){
			return true;
		}
		return false;
	}
	
	@Override
	public boolean addCouponForId(String valid,String userId,String sys,String id)throws Exception{
		Map<String,Object>paramMap=new HashMap<String,Object>();
		paramMap.put("valid",valid);
		paramMap.put("userId",userId);
		paramMap.put("sys",sys);
		paramMap.put("id",id);
		//判断是否已经领取过优惠券
		boolean flag=this.couponDao2.isGetCoupon(id,sys,userId);
		if(flag){
			return false;
		}
		int result=this.couponDao2.addCouponForId(paramMap);
		if(result==1){
			return true;
		}
		return false;
	}
	
	@Override
	public String getCodeCoupon(String code,String deviceNo,String app){
		Map<String,Object>paramMap=new HashMap<String,Object>();
		paramMap.put("app",app);
		paramMap.put("deviceNo",deviceNo);
		paramMap.put("code",code);
		String couponId="0";
		String valid="";
		 try {
			 //获取用户优惠券信息
			List<Map<String,Object>> couponList=this.couponDao2.getCodeCoupon(paramMap);
			if(couponList.isEmpty()){
				return "error";
			}
			 //判断是否领过
			 if(this.couponDao2.isCodeCoupon(paramMap)){
				 return "isGet";
			 }
			//判断是否领完
			 for(Map<String,Object>map : couponList){
				 int count=Integer.parseInt(map.get("count").toString());
				 if(count<=0){
					 return "had";
				 }
			 }
			 
			 for(Map<String,Object>couponMap : couponList){
				 couponId=couponMap.get("id").toString();
					valid=couponMap.get("valid").toString();
					paramMap.put("valid",valid);
					paramMap.put("id",couponId);
					paramMap.put("dgurl",dgurl);
					 int count=Integer.parseInt(couponMap.get("count").toString());
					
					 //添加用户优惠券
					 this.couponDao2.addCoupon(paramMap);
					 
					//优惠券总数-1
					this.couponDao2.updateCouponCount(count-1, couponId);
			 }
			 //正常返回优惠券信息
			 return this.couponDao2.getCoupon(paramMap);
		} catch (Exception e) {
			return null;
		}
	}
	
	@Override
	public String getCodeCouponForId(String code,String userId){
		Map<String,Object>paramMap=new HashMap<String,Object>();
		paramMap.put("userId",userId);
		paramMap.put("code",code);
		int couponId=0;
		String valid="";
		 try {
			 //获取用户优惠券信息
			List<Map<String,Object>> couponList=this.couponDao2.getCodeCoupon(paramMap);
			if(couponList.isEmpty()){
				return "error";
			}
			 //判断是否领过
			 if(this.couponDao2.isCodeCouponForId(paramMap)){
				 return "isGet";
			 }
			 //判断是否领完
			 for(Map<String,Object>map : couponList){
				 int count=Integer.parseInt(map.get("count").toString());
				 if(count<=0){
					 return "had";
				 }
			 }
			
			 for(Map<String,Object>couponMap : couponList){
				 couponId=Integer.parseInt(couponMap.get("id").toString());
					valid=couponMap.get("valid").toString();
					paramMap.put("valid",valid);
					paramMap.put("id",couponId);
					paramMap.put("dgurl",dgurl);
					//添加用户优惠券
					 this.couponDao2.addCouponForId(paramMap);
			 }
			 
			 //正常返回优惠券信息
			 return this.couponDao2.getCoupon(paramMap);
		} catch (Exception e) {
			return null;
		}
	}
	
	@Override
	public String getCenterCoupon(String deviceNo,String app,String couponId,String valid){
		Map<String,Object>paramMap=new HashMap<String,Object>();
		paramMap.put("deviceNo",deviceNo);
		paramMap.put("app",app);
		paramMap.put("id",couponId);
		paramMap.put("valid",valid);
		//判断用户是否领过优惠券
		if(this.couponDao2.isCachCoupon(paramMap)){
			return null;
		}
		int count=this.couponDao2.getCouponCount(couponId);
		if(count<=0){
			return null;
		}
		//记录优惠券
		try {
			int result=this.couponDao2.addCoupon(paramMap);
			if(result>0){
				//优惠券总数-1
				this.couponDao2.updateCouponCount(count-1, couponId);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return "";
	}
	
	@Override
	public String getCenterCouponType(String deviceNo,String app,String couponId,String valid,String type){
		Map<String,Object>paramMap=new HashMap<String,Object>();
		paramMap.put("deviceNo",deviceNo);
		paramMap.put("app",app);
		paramMap.put("id",couponId);
		paramMap.put("valid",valid);
		paramMap.put("type",type);
		//判断用户是否领过优惠券
		if(this.couponDao2.isCachCouponType(paramMap)){
			return null;
		}
		int count=this.couponDao2.getCouponCount(couponId);
		if(count<=0){
			return null;
		}
		//记录优惠券
		try {
			int result=this.couponDao2.addCoupon(paramMap);
			if(result>0){
				//优惠券总数-1
				this.couponDao2.updateCouponCount(count-1, couponId);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return "";
	}
	
	@Override
	public String getCenterCouponForId(String userId,String couponId,String valid){
		Map<String,Object>paramMap=new HashMap<String,Object>();
		paramMap.put("userId",userId);
		paramMap.put("id",couponId);
		paramMap.put("valid",valid);
		//判断用户是否领过优惠券
		if(this.couponDao2.isCachCouponForId(paramMap)){
			return null;
		}
		int count=this.couponDao2.getCouponCount(couponId);
		if(count<=0){
			return null;
		}
		//记录优惠券
		try {
			int result=this.couponDao2.addCouponForId(paramMap);
			if(result>0){
				//优惠券总数-1
				this.couponDao2.updateCouponCount(count-1, couponId);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return "";
	}
	
	@Override
	public String getAiSiAndOldCouponList(String type,String userId)throws Exception{
		Map<String,Object>paramMap=new HashMap<String,Object>();
		paramMap.put("userId",userId);
		paramMap.put("type",type);
		paramMap.put("dgurl",dgurl);
		return this.couponDao2.getAiSiAndOldCouponList(paramMap);
	}

	@Override
	public String couponCenter(String deviceNo, String app) throws Exception {
		Map<String,Object>paramMap=new HashMap<String,Object>();
		paramMap.put("deviceNo",deviceNo);
		paramMap.put("app",app);
		paramMap.put("dgurl",dgurl);
		return this.couponDao2.couponCenterType(paramMap);
	}

	public String getCouponList(String userId, String sys) {
		Map<String, Object> result=new HashMap<>();
		List<Map<String, Object>> list=this.couponDao2.getCouponList(userId,sys);
		if(list==null || list.size()<=0){
			return null;
		}
		List<Map<String, Object>> useList=new ArrayList<>();
		List<Map<String, Object>> validList=new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			long type_=(Long)list.get(i).get("type");
			Integer companyId=(Integer)list.get(i).get("companyId");
			String companyName=(String)list.get(i).get("companyName");
			if(companyId==null){
				companyId=-1;
				companyName="全场通用";
				list.get(i).put("companyId", companyId);
				list.get(i).put("companyName", companyName);
			}
			list.get(i).remove("type");
			if(type_==1){
				useList.add(list.get(i));
			}else{
				validList.add(list.get(i));
			}
		}
		result.put("useList", useList);
		result.put("validList", validList);
		return JSONUtil.convertObjectToJSON(result);
	}

	public String checkCouponList(String userId, String sys) {
		List<Map<String, Object>> list=this.couponDao2.checkCouponList(userId,sys);
		if(list==null || list.size()<=0){
			return null;
		}
		List<Map<String, Object>> result=new ArrayList<>();
		Map<String, Object> coupon=null;
		for (int i = 0; i < list.size(); i++) {
			Integer companyId=(Integer)list.get(i).get("companyId");
			String companyName=(String)list.get(i).get("companyName");
//			if(companyId==null){
//				companyId=-1;
//				companyName="全场通用";
//				list.get(i).put("companyId", companyId);
//				list.get(i).put("companyName", companyName);
//			}
			coupon=new HashMap<>();
			coupon.put("companyId", companyId);
			coupon.put("companyName", companyName);
			List<Map<String, Object>> temListt=this.couponDao2.getAvailableCoupon(userId,sys,companyId);
			if(temListt!=null && temListt.size()>0){
				for (int j = 0; j < temListt.size(); j++) {
					temListt.get(j).put("companyId", companyId);
					temListt.get(j).put("companyName", companyName);
				}
				coupon.put("couponList", temListt);
				result.add(coupon);
			}
		}
		coupon=new HashMap<>();
		coupon.put("companyId", -1);
		coupon.put("companyName", "全场通用");
		List<Map<String, Object>> temList=this.couponDao2.getAvailableCoupon2(userId,sys);
		if(temList!=null && temList.size()>0){
			coupon.put("couponList", temList);
			result.add(coupon);
		}
		return JSONUtil.convertArrayToJSON(result);
	}

	public boolean getCoupon(String userId, String couponId, String sys) {
		return this.couponDao2.getCoupon(userId,couponId,sys);
	}


}
