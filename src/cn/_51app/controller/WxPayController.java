package cn._51app.controller;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.databind.ObjectMapper;

import cn._51app.dao.INewOrderDao;
import cn._51app.dao.ocs.OCSDao;
import cn._51app.service.INewPayService;
import cn._51app.service.diy2_0.IUserWorksService;
import cn._51app.util.OCSKey;
import cn._51app.util.SMTPMailUtil;
import cn._51app.util.Signature;
import cn._51app.util.XMLParser;

@Controller
public class WxPayController extends  BaseController {
	
	@Autowired
	private INewPayService iNewPayService;
	@Autowired
	private OCSDao ocsDao;
	@Autowired
	private INewOrderDao iNewOrderDao;
	@Autowired
	private IUserWorksService iUserWorksService;
	private ObjectMapper mapper=new ObjectMapper();
	
	@RequestMapping("/wxnotify/{appid}")
	public void wxnotify(HttpServletRequest request, 
			HttpServletResponse response,
			@PathVariable("appid") String appid
			) throws Exception {
		String msg="";
		PrintWriter re = null;
		String total_fee = "", out_trade_no = "", trade_no = "", openid = "",coupon_fee="";
		// 处理接收消息
		try {
			re = response.getWriter();
			ServletInputStream in = request.getInputStream();
			SAXBuilder reader = new SAXBuilder();
			org.jdom2.Document document = reader.build(in);
			StringWriter out = null; // 输出对象
			String sReturn = ""; // 输出字符串
			XMLOutputter outputter = new XMLOutputter();
			out = new StringWriter();
			outputter.output(document, out);
			sReturn = out.toString();
			// System.err.println(sReturn);
			Map<String, Object> map = XMLParser.getMapFromXML(sReturn);
			// 总金额
			total_fee = map.get("total_fee").toString();
			// 订单号
			out_trade_no = map.get("out_trade_no").toString();//其实是返回的是pay_no
			// 交易号
			trade_no = map.get("transaction_id").toString();
			// 用户标识
			openid = map.get("openid").toString();
			//代金券或立减优惠金额
			coupon_fee=(String)map.get("coupon_fee");
			int ids = this.iNewPayService.checkOrderIsPayed(trade_no);
			if (ids > 0) {
				re.println(returnXml("SUCCESS", "OK"));
				return;
			}
			boolean flag = Signature.checkIsSignValidFromResponseString(sReturn,getWxPayById(appid, "key"));
			if(flag){
				if (map.get("result_code").toString().equalsIgnoreCase("SUCCESS")) {
					// 插入交易记录
					Map<String,Object> paramMap =new HashMap<String,Object>();
					paramMap.put("order_no", out_trade_no.split("_")[0]);
					paramMap.put("email", openid);
					paramMap.put("trade_no", trade_no);
					paramMap.put("price", String.valueOf(Double.valueOf(total_fee)/100));
					paramMap.put("type", 2);
					//记录交易记录
					boolean checkInsert=this.iNewPayService.updatePayRecord(paramMap);
					boolean resultflag=true;
					if(checkInsert){
						// 改变订单状态
						boolean b=false;
						if(iNewPayService.isH5Custom(out_trade_no.split("_")[0]))
							b=iNewPayService.changeOrderStatus4H5Custom(paramMap);
						else
							b=iNewPayService.changeOrderStatus(paramMap);
						System.err.println(b);
						if(b){
							//如果是0元购商品修改购买状态
							this.iUserWorksService.updateZeroStatus(out_trade_no.split("_")[0]);
							//拆订单
							this.iNewPayService.separateOrder(out_trade_no.split("_")[0]);
						}else{
							resultflag=false;
						}
					}else{
						//有问题发邮件
						resultflag=false;
					}
					if(!resultflag){
						if(StringUtils.isNotBlank(trade_no)){
							SMTPMailUtil.sendEmail("783878156@qq.com", "DIY订制商城订单异常通知", "异常订单!!! 订单号:"+out_trade_no+"*******"+"交易方式:微信*******交易号:"+trade_no+"."+msg);
						}
					}
					re.println(returnXml("SUCCESS", "OK"));
				}else{
					re.println(returnXml("FAIL", "FAIL"));
				}
			}else{
				re.println(returnXml("FAIL", "FAIL"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			// 订单交易号出现异常
			if(StringUtils.isNotBlank(trade_no)){
				SMTPMailUtil.sendEmail("783878156@qq.com", "DIY订制商城订单异常通知", "异常订单!!! 订单号:"+out_trade_no+"*******"+"交易方式:微信*******交易号:"+trade_no+"."+msg);
			}
		}finally {
			re.close();
		}
	}
	
	private String returnXml(String code, String msg) {
		StringBuilder xml = new StringBuilder();
		xml.append("<xml>");
		xml.append("<return_code><![CDATA[");
		xml.append(code);
		xml.append("]]></return_code>");
		xml.append("<return_msg><![CDATA[");
		xml.append(msg);
		xml.append("]]></return_msg>");
		xml.append("</xml> ");
		return xml.toString();
	}
	
	/**
	 * tengh 2016年6月7日 下午3:11:17
	 * @param appid
	 * @param key
	 * @return
	 * TODO 获取微信配置
	 */
	private String getWxPayById(String appid, String key) throws Exception{
		String json=this.ocsDao.query(OCSKey.DIY_WX_APPID_+appid);
		if(StringUtils.isBlank(json)){
			Map<String, Object> teMap=this.iNewOrderDao.getWxPayByAppId(appid);
			json=mapper.writeValueAsString(teMap);
			ocsDao.insert(OCSKey.DIY_WX_APPID_+appid, json, 0);
		}
		Map<String, Object> map=mapper.readValue(json, HashMap.class);
		return (String)map.get(key);
	}
	
	/**
	 * TODO 手动去拆单
	 */
	@RequestMapping("separateOrder")
	public ResponseEntity<String> separateOrder(String orderNo){
		String data =null;
		String msg=null;
		int code =SUCESS;
		try {
			iNewPayService.separateOrder(orderNo);
		} catch (Exception e) {
			e.printStackTrace();
			code =SERVER_ERR;
		}
		return super.resultInfo(data, code, msg);
	}
	
	
}
