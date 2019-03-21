package com.example.elephantshopping.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.example.elephantshopping.controller.ActivityClickController;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.SMS;

public class Test {
	JPushClient jPushClient = new JPushClient("b6f0d2284c7c8fd6e24c2955", "3d88543dc03fb5e2bf40d7df");

	public static void main(String[] args) {
		ActivityClickController acc = new ActivityClickController();
		int x = 0, y = 0, z = 0, c = 0;
		for (int i = 0; i < 50; i++) {
			Map<String, Object> map = acc.startGame(0, 10, 20, "");
			System.out.println("第" + (i + 1) + "次奖品：" + map.get("lotteyName") + ";index:" + map.get("index"));
			if (map.get("lotteyName").equals("象本抱枕")) {
				x++;
			} else if (map.get("lotteyName").equals("象本钥匙扣")) {
				y++;
			} else if (map.get("lotteyName").equals("落地加湿器")) {
				z++;
			} else {
				c++;
			}
		}
		System.out.println("参数人数:50");
		System.out.println("象本抱枕:" + x);
		System.out.println("象本钥匙扣:" + y);
		System.out.println("落地加湿器:" + z);
		System.out.println("谢谢参与:" + c);
	}

	/**
	 * 概率计算
	 * 
	 * @param array：奖品对应号码
	 * @param prize：奖品数量
	 * @param number：参加人数
	 */
	public static List<Integer> probability(int[][] array, int[] prize, int number) {
		Random random = new Random();
		List<Integer> box = new ArrayList<Integer>();
		int prizecount = 0;
		for (int i = 0; i < prize.length; i++) {
			prizecount += prize[i];
		}
		for (int i = 0; i < number - prizecount; i++) {
			box.add(array[array.length - 1][random.nextInt(array.length)]);
		}
		for (int i = 0; i < prize.length; i++) {
			for (int j = 0; j < prize[i]; j++) {
				box.add(random.nextInt(), array[i][random.nextInt(array[i].length)]);
			}
		}
		return box;
	}

	public void testSendWithSMS() {
		try {
			SMS sms = SMS.content("Test SMS", 10);
			// ͨ��id������ָ������ 1507bfd3f7cc1d652a4 1a0018970aadfa597db
			PushResult r = jPushClient.sendAndroidNotificationWithRegistrationID("通知", "下班了", null,
					"1a0018970aadfa597db");
			System.out.println(r);
			System.out.println(r.statusCode);
		} catch (APIConnectionException e) {
			e.printStackTrace();
		} catch (APIRequestException e) {
			e.printStackTrace();
		}
	}
}
