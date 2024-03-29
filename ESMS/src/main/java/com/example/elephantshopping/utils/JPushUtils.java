package com.example.elephantshopping.utils;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosAlert;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;

public class JPushUtils 
{
	private static JPushClient jPushClient = new JPushClient("b6f0d2284c7c8fd6e24c2955", "3d88543dc03fb5e2bf40d7df");
	
	/**
	 * 向所有用户推送通知
	 * @param title
	 * @param content
	 * @throws APIRequestException 
	 * @throws APIConnectionException 
	 */
	public static void sendNotificationToAll(String title,String content)
	{
		//设置ios的alert
		IosAlert alert = IosAlert.newBuilder()
                .setTitleAndBody(title, "",content)
                .setActionLocKey("PLAY")
                .build();
		//设置pushpayload
		PushPayload pushPayload = PushPayload.newBuilder()
                		.setPlatform(Platform.android_ios())
                		.setAudience(Audience.all())
                		.setNotification(Notification.newBuilder()
                						.addPlatformNotification(AndroidNotification.newBuilder()
                                				.setAlert(content)
                                				.setTitle(title)
                                				.build()
                        						)
                						.addPlatformNotification(IosNotification.newBuilder()
                                				.setAlert(alert)
                                				//直接传alert
                                				//此项是指定此推送的badge自动加1
                                				.incrBadge(1)
                                				.build()
                        						)
                						.build()
                						)
                		.setOptions(Options.newBuilder()
                					//此字段的值是用来指定本推送要推送的apns环境，false表示开发，true表示生产；对android和自定义消息无意义
                					.setApnsProduction(true)
                					//此字段是给开发者自己给推送编号，方便推送者分辨推送记录
                					.setSendno(1)
                					//此字段的值是用来指定本推送的离线保存时长，如果不传此字段则默认保存一天，最多指定保留十天，单位为秒
                					.setTimeToLive(86400)
                					.build()
                					)
                		.build();
		//推送
		try {
			jPushClient.sendPush(pushPayload);
		} catch (APIConnectionException e) {
			e.printStackTrace();
		} catch (APIRequestException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 向指定用户推送通知
	 * @param registrationId
	 * @param notification_title
	 * @param msg_content
	 */
	public static void buildPushObject_all_registrationId_alertWithTitle(String registrationId,String notification_title,String msg_content) {
		 
        System.out.println("----------buildPushObject_all_all_alert");
        //创建一个IosAlert对象，可指定APNs的alert、title等字段
        //IosAlert iosAlert =  IosAlert.newBuilder().setTitleAndBody("title", "alert body").build();
 
      //设置ios的alert
      		IosAlert alert = IosAlert.newBuilder()
                      .setTitleAndBody(notification_title, "",msg_content)
                      .setActionLocKey("PLAY")
                      .build();
        PushPayload pushPayload = PushPayload.newBuilder()
                //指定要推送的平台，all代表当前应用配置了的所有平台，也可以传android等具体平台
                .setPlatform(Platform.all())
                //指定推送的接收对象，all代表所有人，也可以指定已经设置成功的tag或alias或该应应用客户端调用接口获取到的registration id
                .setAudience(Audience.registrationId(registrationId))
                //jpush的通知，android的由jpush直接下发，iOS的由apns服务器下发，Winphone的由mpns下发
                .setNotification(Notification.newBuilder()
                        //指定当前推送的android通知
                        .addPlatformNotification(AndroidNotification.newBuilder()
 
                                .setAlert(msg_content)
                                .setTitle(notification_title)
                                .build())
                        //指定当前推送的iOS通知
                        .addPlatformNotification(IosNotification.newBuilder()
                                //传一个IosAlert对象，指定apns title、title、subtitle等
                                .setAlert(alert)
                                //直接传alert
                                //此项是指定此推送的badge自动加1
                                .incrBadge(1)
                                //此字段的值default表示系统默认声音；传sound.caf表示此推送以项目里面打包的sound.caf声音来提醒，
                                // 如果系统没有此音频则以系统默认声音提醒；此字段如果传空字符串，iOS9及以上的系统是无声音提醒，以下的系统是默认声音
                                .setSound("sound.caf")
                                //此项说明此推送是一个background推送，想了解background看：http://docs.jpush.io/client/ios_tutorials/#ios-7-background-remote-notification
                                //取消此注释，消息推送时ios将无法在锁屏情况接收
                                //.setContentAvailable(true)
 
                                .build())
 
 
                        .build())
                //Platform指定了哪些平台就会像指定平台中符合推送条件的设备进行推送。 jpush的自定义消息，
                // sdk默认不做任何处理，不会有通知提示。建议看文档http://docs.jpush.io/guideline/faq/的
                // [通知与自定义消息有什么区别？]了解通知和自定义消息的区别
                .setOptions(Options.newBuilder()
                        //此字段的值是用来指定本推送要推送的apns环境，false表示开发，true表示生产；对android和自定义消息无意义
                        .setApnsProduction(true)
                        //此字段是给开发者自己给推送编号，方便推送者分辨推送记录
                        .setSendno(1)
                        //此字段的值是用来指定本推送的离线保存时长，如果不传此字段则默认保存一天，最多指定保留十天；
                        .setTimeToLive(86400)
 
                        .build())
 
                .build();
        
        try {
			jPushClient.sendPush(pushPayload);
		} catch (APIConnectionException | APIRequestException e) {
			e.printStackTrace();
		}
 
    }
}
