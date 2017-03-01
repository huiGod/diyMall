package cn._51app.util;


import cn.jiguang.common.ClientConfig;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.Notification;

public class JpushUtil {

	public static void main(String[] args) throws APIConnectionException, APIRequestException {
		 ClientConfig config = ClientConfig.getInstance();
	        config.setMaxRetryTimes(5);
	        config.setConnectionTimeout(10 * 1000); // 10 seconds
	        config.setSSLVersion("TLSv1.1");        // JPush server supports SSLv3, TLSv1, TLSv1.1, TLSv1.2

	        JPushClient jPushClient = new JPushClient(PropertiesUtil.getValue("masterSecret"), PropertiesUtil.getValue("appKey"), null, config);
	        PushPayload payload =PushPayload.newBuilder()
	                .setPlatform(Platform.ios())
	                .setAudience(Audience.alias("148014"))
	                .setNotification(Notification.alert("测试推送"))
	                .setOptions(Options.newBuilder()
	                         .setApnsProduction(true)
	                         .build())
	                .build();
	        jPushClient.sendPush(payload);
	}

	public static void pushMessage(String memberId, String content) {
		ClientConfig config = ClientConfig.getInstance();
        config.setMaxRetryTimes(5);
        config.setConnectionTimeout(10 * 1000); // 10 seconds
        config.setSSLVersion("TLSv1.1");        // JPush server supports SSLv3, TLSv1, TLSv1.1, TLSv1.2

        JPushClient jPushClient = new JPushClient(PropertiesUtil.getValue("masterSecret"), PropertiesUtil.getValue("appKey"), null, config);
        PushPayload payload =PushPayload.newBuilder()
                .setPlatform(Platform.ios())
                .setAudience(Audience.alias(memberId))
                .setNotification(Notification.alert(content))
                .setOptions(Options.newBuilder()
                         .setApnsProduction(true)
                         .build())
                .build();
        try {
			jPushClient.sendPush(payload);
		} catch (APIConnectionException e) {
			e.printStackTrace();
		} catch (APIRequestException e) {
			e.printStackTrace();
		}
	}

}
