package cn._51app.util;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;

public class WxPayUtil {
	private static Logger log=Logger.getLogger(WxPayUtil.class);
	private static Logger orderLog=Logger.getLogger("order");
	/**
	 * 
	 * tengh 2016年1月16日 下午2:41:00
	 * @param dataMap
	 * @return
	 * TODO map 转 xml
	 * @throws UnsupportedEncodingException 
	 */
	public static String mapToXml(Map<String, Object> dataMap) throws UnsupportedEncodingException{
		StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("<xml>");
        Set<String> objSet = dataMap.keySet();
        for (String key : objSet)
        {
            if (key == null)
            {
                continue;
            }
            strBuilder.append("<").append(key).append(">");
            strBuilder.append(dataMap.get(key).toString());
            strBuilder.append("</").append(key).append(">");
        }
        strBuilder.append("</xml>");
        return new String(strBuilder.toString().getBytes(), "utf-8"); 
	}

	public static Map<String,Object> unifiedorder(double fee_total,String orderNo, String key, String appid, String appIDS, String mchId) {
		Map<String, Object> resultMap=new HashMap<>();
		String prepay_id="";
		String pay_no=orderNo+"_"+CommonUtil.createOrderNo("9",3);
		Map<String, Object> params=new HashMap<>();
		params.put("appid", appIDS);
		params.put("mch_id", mchId);
		params.put("nonce_str", CommonUtil.createOrderNo("0",18));
		params.put("body", "唯优品");
		params.put("out_trade_no", pay_no);
		params.put("time_expire", DateUtil.date2String(DateUtil.add(Calendar.MINUTE, 120),DateUtil.FORMAT_DATEDSTR));
		System.err.println("订单号:"+orderNo+",微信支付价格："+fee_total);
		if(fee_total==0){
			System.err.println("商品是0元微信付款，默认到1分支付");
			fee_total=0.01;
		}
		params.put("total_fee", new BigDecimal(Double.toString(fee_total)).multiply(new BigDecimal(100)).intValue());
		params.put("spbill_create_ip", "120.26.112.213");//http://120.26.112.213:8082/diyMall/home/initHome/1/1.do
		params.put("notify_url", "http://api.diy.51app.cn/diyMall/wxnotify/"+appid+".do");	
		params.put("trade_type", "APP");
		String sign = Signature.getSign(params,key);
		params.put("sign", sign);
		try {
			String result=HttpClientUtil.httpPostRequest(Configure.UNIFIEDORDER_API,  mapToXml(params),"xml");
			Map<String, Object> map = XMLParser.getMapFromXML(result);
			String return_code=(String)map.get("return_code");
			String result_code=(String)map.get("result_code");
			String err_code=(String)map.get("err_code");
			if("SUCCESS".equals(result_code) && "SUCCESS".equals(return_code)){
				prepay_id=(String)map.get("prepay_id");
			}else{
				String return_msg=(String)map.get("return_msg");
				String err_code_des=(String)map.get("err_code_des");
				log.info("微信预下单失败:"+return_msg+",原因:"+err_code_des+",错误代码:"+err_code);
			}
			orderLog.info("发送给微信商户单号:"+pay_no+",预下单标识:"+prepay_id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		resultMap.put("prepayId", prepay_id);
		resultMap.put("payNo", pay_no);
		return resultMap;
	}
	
	
	/**
	 * H5微信下单
	 * @return
	 */
	public static Map<String,Object> H5unifiedorder(double fee_total,String orderNo, String key, String appid, String appIDS, String mchId,String openId) {
		Map<String, Object> resultMap=new HashMap<>();
		String prepay_id="";
		String pay_no=orderNo+"_"+CommonUtil.createOrderNo("9",3);
		Map<String, Object> params=new HashMap<>();
		params.put("appid", appIDS);
		params.put("mch_id", mchId);
		params.put("nonce_str", CommonUtil.createOrderNo("0",18));
		params.put("body", "唯优品");
		params.put("openid", openId);
		params.put("out_trade_no", pay_no);
		params.put("time_expire", DateUtil.date2String(DateUtil.add(Calendar.MINUTE, 120),DateUtil.FORMAT_DATEDSTR));
		params.put("total_fee", new BigDecimal(Double.toString(fee_total)).multiply(new BigDecimal(100)).intValue());
		params.put("spbill_create_ip", "120.26.112.213");//http://120.26.112.213:8082/diyMall/home/initHome/1/1.do
		params.put("notify_url", "http://api.diy.51app.cn/diyMall/wxnotify/"+appid+".do");
		params.put("trade_type", "JSAPI");
		String sign = Signature.getSign(params,key);
		params.put("sign", sign);
		try {
			String result=HttpClientUtil.httpPostRequest(Configure.UNIFIEDORDER_API,  mapToXml(params),"xml");
			Map<String, Object> map = XMLParser.getMapFromXML(result);
			String return_code=(String)map.get("return_code");
			String result_code=(String)map.get("result_code");
			String err_code=(String)map.get("err_code");
			if("SUCCESS".equals(result_code) && "SUCCESS".equals(return_code)){
				prepay_id=(String)map.get("prepay_id");
			}else{
				String return_msg=(String)map.get("return_msg");
				String err_code_des=(String)map.get("err_code_des");
				log.info("微信预下单失败:"+return_msg+",原因:"+err_code_des+",错误代码:"+err_code);
				System.out.println("微信预下单失败:"+return_msg+",原因:"+err_code_des+",错误代码:"+err_code);
			}
			orderLog.info("发送给微信商户单号:"+pay_no+",预下单标识:"+prepay_id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		resultMap.put("prepayId", prepay_id);
		resultMap.put("payNo", pay_no);
		return resultMap;
	}
	
	/**
	 * H5支付返回给页面的参数
	 */
	public static Map<String, Object> returnH5Pay(String appId,String key,String prepay_id){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("appId", appId);
		map.put("timeStamp", Long.toString(System.currentTimeMillis()));
		map.put("nonceStr", CommonUtil.createOrderNo("0",18));
		map.put("package", "prepay_id="+prepay_id);
		map.put("signType", "MD5");
		String sign = Signature.getSign(map,key);
		map.put("paySign", sign);
		return map;
	}
	
	
	public static void main(String[] args) {
		Map<String,Object> m = H5unifiedorder(59.0, CommonUtil.createOrderNo("U", 5), "54dfs2u32016hrfhasklijfhgdfgsdkl", "2982001266", "wx204459d3f9148e3b", "1356305702","oWu8buKtZhVe0fHIK3V8JQy7XLig");
		System.out.println(m);
	}
	
//	public static Map<String, Object> checkOrder(String orderNo){
//		Map<String,Object> map=new HashMap<>();
//		map.put("appid", Configure.getAppid());
//		map.put("mch_id", Configure.getMchid());
//		map.put("out_trade_no", orderNo);
//		map.put("nonce_str", CommonUtil.createOrderNo("1",18));
//		String sign = Signature.getSign(map);
//		map.put("sign", sign);
//		Map<String, Object> result=new HashMap<>();
//		try {
//			String reXMl=HttpClientUtil.httpPostRequest("https://api.mch.weixin.qq.com/pay/orderquery",  mapToXml(map),"xml");
//			return XMLParser.getMapFromXML(reXMl);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return result;
//	}
//	
//	public static void closeOrder(String orderNo){
//		Map<String,Object> map=new HashMap<>();
//		map.put("appid", Configure.getAppid());
//		map.put("mch_id", Configure.getMchid());
//		map.put("out_trade_no", orderNo);
//		map.put("nonce_str", CommonUtil.createOrderNo("4",18));
//		String sign = Signature.getSign(map);
//		map.put("sign", sign);
//		try {
//			System.err.println(HttpClientUtil.httpPostRequest("https://api.mch.weixin.qq.com/pay/closeorder",  mapToXml(map),"xml"));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
	
	//public static void main(String[] args) {
		//unifiedorder(100,"S537344869295");
		//closeOrder("S537344869295");
		//checkOrder("S537344869295");
		//System.err.println(DateUtil.date2String(DateUtil.add(Calendar.DATE, 7),DateUtil.FORMAT_DATEDSTR));
	//}
}
