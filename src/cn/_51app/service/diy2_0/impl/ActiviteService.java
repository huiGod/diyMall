package cn._51app.service.diy2_0.impl;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import cn._51app.dao.diy2_0.IActiviteDao;
import cn._51app.dao.ocs.OCSDao;
import cn._51app.service.BaseService;
import cn._51app.service.diy2_0.IActiviteService;
import cn._51app.util.OCSKey;

@Service
public class ActiviteService extends BaseService implements IActiviteService {
	
	@Autowired
	private OCSDao ocsDao;
	@Autowired
	private IActiviteDao iActiviteDao;
	private DecimalFormat df= new DecimalFormat("######0.00");
	private SimpleDateFormat datef=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private ObjectMapper mapper=new ObjectMapper();
	@Override
	public String activiteList(String type_) throws Exception{
		String json=ocsDao.query(OCSKey.DIY_ACTIVITE_+type_);
		Map<String, Object> result=null;
		if(StringUtils.isBlank(json)){
			Map<String, Object> map=this.iActiviteDao.activiteList(type_);
			result=new HashMap<>();
			int type=(Integer)map.get("type");   //活动类型
			String orgPrice=(String)map.get("org_price"); //满减需要的金额
			String desPrice=(String)map.get("des_price"); //满减的额度
			String param1=(String)map.get("param1");   //自定义参数
			String jphGoodIds=(String)map.get("jph_good_ds"); //精品汇商品id  ,隔开
			String dzGoodIds=(String)map.get("dz_good_ids");//定制商品id  ,隔开
			String companyId=(String)map.get("company_id");  //商户id
			int num=(Integer)map.get("num");  //商品数量
			double money=(Double)map.get("money"); //固定价格
			String about=(String)map.get("about"); //优惠活动描述
			switch (type) {
			case 1:  //满减
				result.put("orgPrice", df.format(orgPrice));
				result.put("desPrice", df.format(desPrice));
				break;
			case 2: //免单或者半价
				result.put("jphGoodIds", jphGoodIds==null?"":jphGoodIds);
				result.put("dzGoodIds", dzGoodIds==null?"":dzGoodIds);
				result.put("flag", param1);
				break;
			case 3: //优惠券
				break;
			case 4: //多件  贵的n件 固定价格
				result.put("num", num);
				result.put("money", money);
				break;
			case 5: //套装
				result.put("jphGoodIds", jphGoodIds==null?"":jphGoodIds);
				result.put("dzGoodIds", dzGoodIds==null?"":dzGoodIds);
				break;
			case 6: //限时抢购
				String startTime=datef.format(map.get("start_time"));
				String endTime=datef.format(map.get("end_time"));
				result.put("startTime", startTime);
				result.put("endTime", endTime);
				result.put("jphGoodIds", jphGoodIds==null?"":jphGoodIds);
				result.put("dzGoodIds", dzGoodIds==null?"":dzGoodIds);
				result.put("discount", param1);
				break;
			case 7: //分享下载应用领取优惠券
				break;
			case 8://打折
				result.put("num", orgPrice);  //件数
				result.put("discount", desPrice); //折扣
				break;
			default:
				break;
			}
			result.put("about", about);
			result.put("companyId", companyId==null?"":companyId);
			json=mapper.writeValueAsString(result);
			ocsDao.insert(OCSKey.DIY_ACTIVITE_+type_, json, 0);
		}
		return json;
	}
}
