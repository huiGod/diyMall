package cn._51app.service.impl;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn._51app.dao.INewOrderDao;
import cn._51app.dao.INewPayDao;
import cn._51app.dao.diy2_0.IWorkActivityDao;
import cn._51app.dao.diy2_0.IndentDao;
import cn._51app.service.BaseService;
import cn._51app.service.INewPayService;
import cn._51app.util.CommonUtil;

@Service
public class NewPayService extends BaseService implements INewPayService {
	
	@Autowired
	private INewPayDao iNewPayDao;
	@Autowired
	private INewOrderDao iNewOrderDao;
	@Autowired
	private IWorkActivityDao iworkActivityDao;
	@Autowired
	private IndentDao indentDao;

	@Override
	public int checkOrderIsPayed(String trade_no) {
		return this.iNewPayDao.checkOrderIsPayed(trade_no);
	}
	
	@Override
	public boolean insertPayRecord(Map<String, Object> paramMap) {
		return this.iNewPayDao.insertPayRecord(paramMap);
	}
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean changeOrderStatus(Map<String, Object> paramMap) {
		boolean success = this.iNewPayDao.changeOrderStatus(paramMap);
		if (success) {
			//成功修改订单状态
			String orderNo = (String) paramMap.get("order_no");
			changeGoodsSell(orderNo);
		}
		
		return success;
	}
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void changeGoodsSell(String orderNo){
		if (orderNo != null) {
			try {
				iNewPayDao.changeGoodsSell(orderNo);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public boolean updatePayRecord(Map<String, Object> paramMap) {
		return this.iNewPayDao.updatePayRecord(paramMap);
	}
	
	
	/**
	 * @author zhanglz
	 * 拆分不同商家的订单
	 */
	@Override
	@Transactional(rollbackFor=Exception.class)
	public void separateOrder(String out_trade_no) {
		Map<String, Object> map=this.iNewOrderDao.queryOrders(out_trade_no);
		if(map!=null){
			Integer status=(Integer)map.get("status");
			if(status==2){
				String userIds=(String)map.get("user_ids");
				String[] userIdArr = userIds.split(",");
				String[] norepeat = array_unique(userIdArr);
				if(norepeat.length>1){
					Map<String, Object> tempMap = new HashMap<String, Object>();
					tempMap.put("porder_no", map.get("order_no"));
					tempMap.put("pay_type", map.get("pay_type"));
					tempMap.put("consignee", map.get("consignee"));
					tempMap.put("province", map.get("province"));
					tempMap.put("area", map.get("area"));
					tempMap.put("mobile", map.get("mobile"));
					tempMap.put("sys", map.get("sys"));
					tempMap.put("fee_transport", map.get("fee_transport"));
					tempMap.put("coupon", map.get("coupon"));
					tempMap.put("coupon_id", map.get("coupon_id"));
					tempMap.put("org_privilege", map.get("org_privilege"));
					tempMap.put("des_privilege", map.get("des_privilege"));
					tempMap.put("express_id", map.get("express_id"));
					tempMap.put("express_no", map.get("express_no"));
					tempMap.put("express_start", map.get("express_start"));
					tempMap.put("express_end", map.get("express_end"));
					tempMap.put("paytime", map.get("paytime"));
					tempMap.put("device_no", map.get("device_no"));
					tempMap.put("creat_time", map.get("creat_time"));
					tempMap.put("app", map.get("app"));
					tempMap.put("remark", map.get("remark"));
					tempMap.put("gType", map.get("gType"));
					
					tempMap.put("balance", map.get("balance"));
					tempMap.put("userId", map.get("userId"));
					tempMap.put("message", map.get("message"));
					
					String shop_no = null==map.get("shop_no")?"":map.get("shop_no").toString();
					String[] shop_nos = shop_no.split(",");
					String[] info_ids=((String)map.get("info_ids")).split(",");
					String[] texture_ids=((String)map.get("texture_ids")).split("\\|");
					String[] texture_names=((String)map.get("texture_names")).split("\\|");
					String[] img_url=((String)map.get("img_url")).split(",");
					String file_type=null==map.get("file_type")?"":map.get("file_type").toString();
					String[] file_types = file_type.split(",");
					String[] price=((String)map.get("price")).split(",");
					String[] num=((String)map.get("num")).split(",");
					
					String[] name=((String)map.get("name")).split(",");
					String[] sort_name=((String)map.get("sort_name")).split(",");
					String[] userwork=((String)map.get("userwork")).split(",");
					String[] paramType=((String)map.get("paramType")).split(",");
					String[] param1=((String)map.get("param1")).split(",");
					
					Map<String, String> indexMap = new HashMap<String, String>();
					for (int i = 0; i < userIdArr.length; i++) {
						String index = "";
						for (int j = 0; j < userIdArr.length; j++) {
							if(userIdArr[i].equals(userIdArr[j])){
								index+=j+",";
							}
						}
						indexMap.put(userIdArr[i], index.substring(0,index.length()-1));
					}
					for (int i = 0; i < norepeat.length; i++) {
						String user_ids_res = "";
						String shop_no_res = "";
						String info_ids_res = "";
						String texture_ids_res = "";
						String texture_names_res = "";
						String img_url_res = "";
						String file_types_res = "";
						String price_res = "";
						String num_res = "";
						String name_res = "";
						String sort_name_res = "";
						String userwork_res = "";
						String paramType_res = "";
						String param1_res = "";
						double fee_total = 0.00;
						String indexArr[] = ((String)indexMap.get(norepeat[i])).split(",");
						for (int j = 0; j < indexArr.length; j++) {
							int index = Integer.parseInt(indexArr[j]);
							user_ids_res += userIdArr[index]+",";
							shop_no_res += shop_nos[index]+",";
							info_ids_res += info_ids[index]+",";
							texture_ids_res += texture_ids[index]+"|";
							texture_names_res += texture_names[index]+"|";
							img_url_res += img_url[index]+",";
							file_types_res += file_types[index]+",";
							price_res += price[index]+",";
							num_res += num[index]+",";
							name_res += name[index]+",";
							sort_name_res += sort_name[index]+",";
							userwork_res += userwork[index]+",";
							paramType_res += paramType[index]+",";
							param1_res += (param1.length-1>=index?param1[index]:"")+",";
							double total = Double.valueOf(price[index])*Integer.parseInt(num[index]);
							fee_total +=total;
						}
						tempMap.put("user_ids", user_ids_res.substring(0, user_ids_res.length()-1));
						tempMap.put("shop_no", shop_no_res.substring(0, shop_no_res.length()-1));
						tempMap.put("info_ids", info_ids_res.substring(0, info_ids_res.length()-1));
						tempMap.put("texture_ids", texture_ids_res.substring(0, texture_ids_res.length()-1));
						tempMap.put("texture_names", texture_names_res.substring(0, texture_names_res.length()-1));
						tempMap.put("img_url", img_url_res.substring(0, img_url_res.length()-1));
						tempMap.put("file_type", file_types_res.substring(0, file_types_res.length()-1));
						tempMap.put("price", price_res.substring(0, price_res.length()-1));
						tempMap.put("num", num_res.substring(0, num_res.length()-1));
						tempMap.put("name", name_res.substring(0, name_res.length()-1));
						tempMap.put("sort_name", sort_name_res.substring(0, sort_name_res.length()-1));
						tempMap.put("userwork", userwork_res.substring(0, userwork_res.length()-1));
						tempMap.put("paramType", paramType_res.substring(0, paramType_res.length()-1));
						tempMap.put("param1", param1_res.substring(0, param1_res.length()-1));
						tempMap.put("fee_total", fee_total);
						String order_no = "";
						if(out_trade_no.indexOf("U")!=0)
							order_no = CommonUtil.createOrderNo("F", 5);
						else
							order_no = CommonUtil.createOrderNo("V", 5);
						tempMap.put("order_no", order_no);
						tempMap.put("status", 2);
						iNewOrderDao.separateOrder(tempMap);
					}
					iNewOrderDao.update0status(out_trade_no);
				}
			}
		}
	}
	
	//去除数组中重复的记录
	public static String[] array_unique(String[] a) {
	    List<String> list = new LinkedList<String>();
	    for(int i = 0; i < a.length; i++) {
	        if(!list.contains(a[i])) {
	            list.add(a[i]);
	        }
	    }
	    return (String[])list.toArray(new String[list.size()]);
	}

	@Override
	public boolean isH5Custom(String orderNo){
		if(orderNo.indexOf("U")==0||orderNo.indexOf("V")==0){
			return iNewPayDao.isH5Custom(orderNo)>0;
		}
		return false;
	}
	
	@Override
	public boolean changeOrderStatus4H5Custom(Map<String, Object> paramMap) {
		return this.iNewPayDao.changeOrderStatus4H5Custom(paramMap);
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class)
	public void makeUserHeart(String orderNo){
		Map<String,Object>map=this.indentDao.queryByOrderNo(orderNo);
		String userwork=map.get("userwork")!=null?map.get("userwork").toString():"";
		String workId[]=userwork.split(",");
		for(int i=0;i<workId.length;i++){
			if(workId[i].equals("0"))
			continue;
			//设置增加20个心
			try {
				int result=this.iworkActivityDao.setUserHeart(workId[i]);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
}
