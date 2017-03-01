package cn._51app.util;

import java.util.Random;

import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;

public class AliSMSUtil {
	private static String appkey = "23408374";
	
	private static String secret = "ce747c2b815938b5c07ce25bff0c660f";
	
	private static String url = "http://gw.api.taobao.com/router/rest";
	
	/**
	 * 发送验证码
	 * @param mobile
	 * @param code
	 * @return
	 * @throws ApiException
	 */
	public static boolean sendMsg(String mobile,String code) throws ApiException{
		TaobaoClient client = new DefaultTaobaoClient(url, appkey, secret);
		AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
		req.setExtend("123456");
		req.setSmsType("normal");
		req.setSmsFreeSignName("深圳优启");
		req.setSmsParamString("{\"number\":\""+code+"\"}");
		req.setRecNum(mobile);
		req.setSmsTemplateCode("SMS_37120148");
		AlibabaAliqinFcSmsNumSendResponse rsp = client.execute(req);
		return rsp.isSuccess();
	}
	
	/**
	 * 生成随机数
	 * @param min
	 * @param max
	 * @return
	 */
	public static int nextInt(final int min, final int max){
		Random rand= new Random();
		int tmp = Math.abs(rand.nextInt());
		return tmp % (max - min + 1) + min;
	}
	
}
