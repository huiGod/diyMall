package cn._51app.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import cn._51app.dao.ICouponDao;
import cn._51app.dao.ocs.OCSDao;
import cn._51app.service.BaseService;
import cn._51app.service.ICouponService;
import cn._51app.util.OCSKey;

@Service
public class CouponService extends BaseService implements ICouponService{

	@Autowired
	private ICouponDao dao;
	@Autowired
	private OCSDao ocsDao;
	private Logger logger = Logger.getLogger(NewAddressService.class);
	private ObjectMapper mapper=new ObjectMapper();
	
	@Override
	public String drawSwitch(String deviceNo,String app) throws Exception {
		Map<String,Object> paramMap =new HashMap<String,Object>();
		paramMap.put("deviceNo", deviceNo);
		paramMap.put("app", app);
		String s = String.valueOf(this.dao.userDraw(paramMap));
		return s;
	}

	@Override
	public String getDrawLevelAndValid() throws Exception {
		String cacheKey =OCSKey.DIY_DRAW_L_V;
		del(cacheKey);
		int cacheTime =FOREVER;
		return this.dao.getDrawLevelAndValid(cacheKey,cacheTime);
	}

	@Override
	public boolean addCouponUser(String valid, String item, String mobile) throws Exception {
		Map<String,Object> paramMap =new HashMap<String,Object>();
		int cid = 0;  //没中奖则为0
		if(!item.equals("0"))
			cid = dao.getCouponIdByLevel(item);
		paramMap.put("cid", cid);
		paramMap.put("valid", valid);
		String arr[] = mobile.split(",");
		paramMap.put("deviceNo",arr[0]);
		paramMap.put("app", arr[1]);
		paramMap.put("sys", arr[2]);
		if(!this.dao.userDraw(paramMap)){
			return false;
		}
		int result =dao.addCouponUser(paramMap);
		if(result==0){
			return false;
		}
		return true;
	}

	@Override
	public String getCoupon(String deviceNo, String app) throws Exception {
		String json=null;
		List<Map<String, Object>> couponList=this.dao.queryAvaiCoupon(deviceNo,app);
		json=mapper.writeValueAsString(couponList);
		return json;
	}
	
	@Override
	public String couponTo() {
		List<Map<String, Object>> users=this.dao.getUsers();
		int index=0;
		for (int i = 0; i < users.size(); i++) {
			String deviceNo=(String)users.get(i).get("deviceNo");
			String app=(String)users.get(i).get("app");
			int result=this.dao.insertCoupon(deviceNo,app);
			index=index+result;
		}
		return index+"";
	}
}
