package com.example.elephantshopping.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.example.elephantshopping.service.ActivityClickService;

@Controller
@RequestMapping("activityclick")
public class ActivityClickController {
	// 人气大赛 活动时间段2018-04-25 09:00:00 -- 2018-05-24 12:00:00
	final String startTime = "2018-04-25 09:00:00";// 开始时间
	final String endTime = "2018-05-24 12:00:00";// 结束时间

	// 幸运大转盘
	final int humidifier = 0;// 奖励落地加湿器（5名）
	final int holdPillow = 0;// 奖励象本抱枕（30名）
	final int keyChain = 0;// 奖励象本钥匙扣（50名）
	static int ranking = 0;// 参加游戏的顺序
	static List<Integer> list = null;// 奖品列表

	@Autowired
	private ActivityClickService activityService;

	/**
	 * 点击活动链接页面
	 * 
	 * @param mv
	 * @param name
	 * @return
	 */
	@RequestMapping("activityPage")
	public ModelAndView toactivity(ModelAndView mav, String name, String userId) {
		mav.addObject("userId", userId);
		switch (name) {
		// 人气王专题页
		case "rqwztym":
			mav.setViewName("/activity/" + name);
			break;
		// 人气王排行榜
		case "renqidasai":
			Map<String, Object> Info = activityService.queryRenqidasaiInfo(startTime, endTime);
			// 参赛总数
			mav.addObject("count", Info.get("count"));
			// 参赛前10名
			mav.addObject("list", Info.get("list"));
			mav.setViewName("/activity/renqidasai");
			break;
		// 上线专题页
		case "shangxianzhuantiye":
			mav.setViewName("/activity/shangxianzhuantiye");
			break;
		// 会员专属福利
		case "hyzsfl":
			mav.setViewName("/activity/" + name);
			break;
		// 幸运大转盘
		case "xingyundazhuanpan":
			// 是否在抽奖活动时间段
			mav.addObject("isTime", timeMissagef());
			// 查询用户是否参与过转盘活动
			mav.addObject("gamed", queryIsGamed(userId));
			// 查询用户是否符合抽奖条件
			mav.addObject("isLack", queryRaffle(userId));
			// 查询中奖list
			// mav.addObject("winlist", queryWinList());
			mav.setViewName("/activity/xingyundazhuanpan");
			break;
		// 活动页面跳转链接
		default:
			mav.setViewName("/activity/" + name);
			break;
		}
		return mav;
	}

	/**
	 * 查询当前时间是否是抽奖时间段内2018-05-24 12:00:00 -- 2018-05-25 00:00:00
	 * 
	 * @return
	 */
	private String timeMissagef() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			long beginTime = sdf.parse("2018-05-24 12:00:00").getTime();
			long overTime = sdf.parse("2018-05-25 00:00:00").getTime();
			long nowTime = new Date().getTime();
			if (nowTime < beginTime) {
				return "抽奖活动暂未开始";
			} else if (nowTime > overTime) {
				return "抽奖活动已经结束";
			} else {
				return "活动进行中";
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 查询中奖list
	 * 
	 * @return
	 */
	private List<Map<String, Object>> queryWinList() {
		return activityService.queryWinList();
	}

	/**
	 * 查询个人排名
	 */
	@RequestMapping("queryPersonalRanking")
	@ResponseBody
	public Map<String, Object> queryPersonalRanking(String phone) {
		return activityService.queryPersonalRanking(phone, startTime, endTime);
	}

	/**
	 * 查询用户是否参与过游戏
	 */
	public String queryIsGamed(String userId) {
		return activityService.queryIsGamed(userId);
	};

	/**
	 * 查询用户是否在可抽奖列表
	 */
	public int queryRaffle(String userId) {
		return activityService.queryRaffle(userId);
	}

	/**
	 * 幸运大转盘活动查询奖品信息
	 * 
	 * @return
	 */
	@RequestMapping("queryPrizeInfo")
	@ResponseBody
	public synchronized Map<String, Object> queryPrizeInfo(String userId) {
		// 查询被抽取礼品数量,计算出可被抽取礼品
		int oneprizeNumber = humidifier - activityService.queryPrizeNumber("落地加湿器");
		int twoprizeNumber = holdPillow - activityService.queryPrizeNumber("象本抱枕");
		int threeprizeNumber = keyChain - activityService.queryPrizeNumber("象本钥匙扣");
		return startGame(oneprizeNumber, twoprizeNumber, threeprizeNumber, userId);
	}

	/**
	 * 转盘游戏
	 * 
	 * @param threeprizeNumber
	 * @param twoprizeNumber
	 * @param oneprizeNumber
	 */
	public Map<String, Object> startGame(int oneprizeNumber, int twoprizeNumber, int threeprizeNumber, String userId) {
		String[] prize = { "象本钥匙扣", "落地加湿器", "象本抱枕", "谢谢参与", "象本钥匙扣", "象本抱枕", "落地加湿器", "象本钥匙扣", "谢谢参与" };
		// 奖品数组
		int[][] array = { { 1, 6 }// 一等奖
				, { 2, 5 }// 二等奖
				, { 0, 4, 7 } // 三等奖
				, { 3, 8 }// 谢谢参与
		};
		// 奖品数量
		int[] prizeNumber = { oneprizeNumber, twoprizeNumber, threeprizeNumber };
		if (ranking == 0) {
			// 查询未抽奖人数
			int number = activityService.queryUserNumber();
			list = probability(array, prizeNumber, number);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		int index;
		if (ranking < list.size()) {
			index = list.get(ranking++);
		} else {
			index = 3;
		}
		map.put("index", index + 1);
		if (index == 1 || index == 6) {
			map.put("msg", "恭喜获得一等奖：\"" + prize[index] + "\"一台");
			map.put("isWin", 1);
		} else if (index == 2 || index == 5) {
			map.put("msg", "恭喜获得二等奖：\"" + prize[index] + "\"一个");
			map.put("isWin", 1);
		} else if (index == 0 || index == 4 || index == 7) {
			map.put("msg", "恭喜获得三等奖：\"" + prize[index] + "\"一个");
			map.put("isWin", 1);
		} else {
			map.put("msg", "谢谢参与");
			map.put("isWin", 0);
		}
		map.put("lotteyName", prize[index]);
		map.put("userId", userId);
		activityService.updateLottery(map);
		return map;
	}

	/**
	 * 生成中奖顺序list
	 * 
	 * @param array：奖品对应号码
	 * @param prize：奖品数量
	 * @param number：人数
	 */
	public List<Integer> probability(int[][] array, int[] prize, int number) {
		Random random = new Random();
		List<Integer> box = new ArrayList<Integer>();
		int prizecount = 0;
		for (int i = 0; i < prize.length; i++) {
			prizecount += prize[i];
		}
		for (int i = 0; i < number - prizecount; i++) {
			box.add(array[array.length - 1][random.nextInt(array[array.length - 1].length)]);
		}
		for (int i = 0; i < prize.length; i++) {
			for (int j = 0; j < prize[i]; j++) {
				if (number - prizecount <= 0 && i == 0) {
					box.add(random.nextInt(box.size()), array[i][random.nextInt(array[i].length)]);
				} else {
					box.add(random.nextInt(box.size()), array[i][random.nextInt(array[i].length)]);
				}
			}
		}
		return box;
	}

}
