package cn._51app.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import cn._51app.dao.impl.UCouponDao;
import cn._51app.service.BaseService;
import cn._51app.service.IUCouponService;
import cn._51app.util.AliSMSUtil;
import cn._51app.util.OCSKey;

@Service
public class UCouponService extends BaseService implements IUCouponService {
	
	@Autowired
	private UCouponDao uCpuponDao;

	@Override
	public String getLottery(String deviceNo,String app) throws Exception {
		Map<String,Object> paramMap =new HashMap<String,Object>();
		paramMap.put("deviceNo",deviceNo);
		paramMap.put("app", app);
		if(deviceNo.isEmpty() || app.isEmpty()){
			return "";
		}
		Map<String,Object>map= this.uCpuponDao.getLottery(paramMap);
		//是否保存
		boolean isSave=addCouponUser(map.get("valid").toString(),map.get("level").toString(),deviceNo,app);
		if(isSave){
			map.put("couponId",uCpuponDao.getCouponId(paramMap));
		}
		map.put("isSave",isSave );
		return super.toJson(map);
	}
	
	@Override
	public boolean addCouponUser(String valid, String item, String deviceNo,String app) throws Exception {
		Map<String,Object> paramMap =new HashMap<String,Object>();
		int cid = 0; 
		String level="9";
		switch(item){
			//5元
			case "0":level="9";break;
			//鲜花
			case "1":level="8";break;
			//10元
			case "2":level="10";break;
			//5元
			case "3":level="9";break;
			//30元
			case "4":level="11";break;
			//diy
			case "5":level="13";break;
			//5元
			case "6":level="9";break;
			//diy壳
			case "7":level="12";break;
			default:level="9";
		}
		cid = uCpuponDao.getCouponIdByLevel(level);
		System.out.println(cid);
		paramMap.put("cid", cid);
		paramMap.put("valid", valid);
		paramMap.put("deviceNo",deviceNo);
		paramMap.put("app", app);
		if(!this.uCpuponDao.userDraw(paramMap)){
			return false;
		}
		int result =uCpuponDao.addCouponUser(paramMap);
		if(result==0){
			return false;
		}
		return true;
	}
	
	@Override
	public boolean addMobileUser(String mobile)throws Exception{
		Map<String,Object> paramMap =new HashMap<String,Object>();
		String arr[] = mobile.split(",");
		paramMap.put("deviceNo",arr[0]);
		paramMap.put("app", arr[1]);
		paramMap.put("phone", arr[2]);
		int result=uCpuponDao.addMobileUser(paramMap);
		if(result==0){
			return false;
		}
		return true;
	}
	
//	@Override
//	public String verifyUserCount(String mobile) throws DataAccessException, Exception{
//		Map<String,Object> paramMap =new HashMap<String,Object>();
//		String arr[] = mobile.split(",");
//		paramMap.put("deviceNo",arr[0]);
//		paramMap.put("app", arr[1]);
//		paramMap.put("phone", arr[2]);
//		return uCpuponDao.verifyUserCount(paramMap);
//	}
	
	@Override
	public String winnerList(String app,String deviceNo) throws DataAccessException, Exception{
		Map<String,Object>map=new HashMap<String,Object>();
		Map<String,Object> paramMap =new HashMap<String,Object>();
		List<Map<String,Object>> list= uCpuponDao.winnerList("U%");
		paramMap.put("deviceNo",deviceNo);
		paramMap.put("app", app);
		map.put("list",list);
		map.put("awards",uCpuponDao.getAwards(paramMap));
		map.put("hasDraw",uCpuponDao.verifyUserCount(paramMap));
		return super.toJson(map);
	}
	
	@Override
	public boolean upUserMobile(int id,String mobile) throws Exception{
		int result=uCpuponDao.upUserMobile(id, mobile);
		if(result==1){
			return true;
		}
		return false;
	}
	
	@Override
	public boolean delCoupon()throws Exception{
		int result=uCpuponDao.delCoupon();
		if(result==1){
			return true;
		}
		return false;
	}

}
