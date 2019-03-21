package com.example.elephantshopping.utils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DateFormatUtils {
	// 上线时间
	public static String onlineTime = "2018-04-01 00:00:00";

	/**
	 * 从数据库中取出的时间页面显示为毫秒数，此方法将Map中的时间转换为String并返回
	 * 
	 * @param list
	 * @return
	 */
	public static List<Map<String, Object>> transforDateType(List<Map<String, Object>> list) {
		for (int i = 0; i < list.size(); i++) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String time = sdf.format(list.get(i).get("TIME"));
			list.get(i).put("TIME", time);// 转换一下时间格式，方便页面显示
		}
		return list;
	}

	/**
	 * 从数据库中取出的时间页面显示为毫秒数，此方法将Map中的时间转换为String并返回
	 * 
	 * @param list
	 * @return
	 */
	public static List<Map<String, Object>> timeforDateType(List<Map<String, Object>> list, String field) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for (Map<String, Object> map : list) {
			if (null != map.get(field) && (!map.get(field).equals(""))) {
				try {
					String time = sdf.format(map.get(field));
					map.put(field, time);// 转换一下时间格式，方便页面显示
				} catch (Exception e) {
					SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHHmmss");
					String formatstring = map.get(field).toString();
					String time = "";
					try {
						time = sdf.format(sdf1.parse(formatstring));
					} catch (ParseException e1) {
					}
					map.put(field, time);// 转换一下时间格式，方便页面显示
				}
			} else {
				map.put(field, "");// 转换一下时间格式，方便页面显示
			}
		}
		return list;
	}

	/**
	 * 获取时间段
	 * 
	 * @return
	 */
	public static Map<String, Object> betweenDateTimeMap(long time) {
		Map<String, Object> timemap = new HashMap<String, Object>();
		// 获取当前时间搓
		long nowTime = new Date().getTime();
		// 计算时间段之前的时间
		long agoTime = nowTime - time;
		String agotime = new SimpleDateFormat("yyyy-MM-dd").format(new Date(agoTime));
		String nowtime = new SimpleDateFormat("yyyy-MM-dd").format(new Date(nowTime));
		timemap.put("startTime", agotime + " 00:00:00");
		timemap.put("endTime", nowtime + " 00:00:00");
		return timemap;
	}

	/**
	 * 获取某一特点时间段
	 */
	public static Map<String, Object> betweenDaoTimeMap(int unmber, String type) {
		Map<String, Object> map = new HashMap<String, Object>();
		// 获取当前时间搓
		long nowTime = new Date().getTime();
		long starttime = 0;
		long endtime = 0;
		if (type.equals("week")) {
			// System.out.println(
			// "本周开始时间：" + new SimpleDateFormat("yyyy-MM-dd").format(getBeginDayOfWeek(new
			// Date(nowTime))));
			nowTime = getBeginDayOfWeek(new Date(nowTime)).getTime();
			starttime = nowTime - (unmber * 7 * 24 * 60 * 60 * 1000);
			endtime = nowTime - ((unmber - 1) * 7 * 24 * 60 * 60 * 1000);
		} else if (type.equals("day")) {
			starttime = nowTime - (unmber * 24 * 60 * 60 * 1000);
			endtime = nowTime - ((unmber - 1) * 24 * 60 * 60 * 1000);
		}
		String startTime = new SimpleDateFormat("yyyy-MM-dd").format(new Date(starttime));
		String endTime = new SimpleDateFormat("yyyy-MM-dd").format(new Date(endtime));
		map.put("startTime", startTime + " 00:00:00");
		map.put("endTime", endTime + " 00:00:00");
		return map;
	}

	/**
	 * 获取本周的开始时间
	 * 
	 * @return
	 */
	public static Date getBeginDayOfWeek(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int dayofweek = cal.get(Calendar.DAY_OF_WEEK);
		if (dayofweek == 1) {
			dayofweek += 7;
		}
		cal.add(Calendar.DATE, 2 - dayofweek);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(cal.getTime());
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 0,
				0, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return new Timestamp(calendar.getTimeInMillis());
	}

	/**
	 * 获取月份时间段，参数列表传int类型，表示查询几月份前的时间段收益
	 */
	public static Map<String, Object> betweenMonth(int monthago) {
		Map<String, Object> monthmap = new HashMap<String, Object>();
		// 获取当前月份
		String nowMonth = new SimpleDateFormat("yyyy-MM").format(new Date());
		String[] yearAndMonth = nowMonth.split("-");
		int year = Integer.parseInt(yearAndMonth[0]);
		int month = Integer.parseInt(yearAndMonth[1]);
		// 确定年
		if (monthago > 12) {
			int first = monthago / 12;
			year = year - first;
			monthago = monthago % 12;
		}
		// 确定月
		if (monthago < month) {
			month = month - monthago;
		} else if (monthago > month) {
			year = year - 1;
			month = 12 + month - monthago;
		} else if (monthago == month) {
			year = year - 1;
			month = 12;
		}
		// 确定计算收益月份的时间段，时间段为一个完整月
		String startTime = year + "-" + month + "-01 00:00:00";
		monthmap.put("startTime", startTime);
		String endTime = "";
		if (month == 12) {
			endTime = (year + 1) + "-01-01 00:00:00";
		} else {
			endTime = year + "-" + String.format("%02d", (month + 1)) + "-01 00:00:00";
		}
		if (monthago == 0) {
			endTime = new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + " 00:00:00";
		}
		monthmap.put("endTime", endTime);
		return monthmap;
	}

	/**
	 * 每月流水时间段数组，按目前的时间开始计算
	 * 
	 * @return
	 */
	public static String[] GetqueryMonthlyPeriod() {
		// 获取当前月份
		String nowMonth = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		String[] yearAndMonth = nowMonth.split("-");
		int year = Integer.parseInt(yearAndMonth[0]);
		int month = Integer.parseInt(yearAndMonth[1]);
		int day = Integer.parseInt(yearAndMonth[2]);
		String[] online = onlineTime.split("-");
		int y = Integer.parseInt(online[0]);
		int m = Integer.parseInt(online[1]);
		// 判断上线时间到现在有多少个月需要查询
		int number = 12 * (year - y) + (month - m);
		int start = 1;
		if (day > 1) {
			number += 1;
			start = 0;
		}
		String[] months = new String[number];
		for (int i = 0; i < number; i++) {
			Map<String, Object> map = betweenMonth(start++);
			String[] mont = map.get("startTime").toString().split("-");
			months[i] = mont[0] + "年" + mont[1] + "月";
		}
		return months;
	}

	/**
	 * 查询月份每天的日期
	 * 
	 * @param month
	 * @return
	 * @throws ParseException
	 */
	public static List<Map<String, Object>> getMonthDayMap(String month) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		String[] days = month.split(" ");
		String now = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		String nowday = now.split("-")[2];
		if (days[0].equals(now) && (!nowday.equals("01"))) {
			String[] m = now.split("-");
			int n = Integer.parseInt(m[2]);
			for (int i = 1; i < n; i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				if (i < 10) {
					String startTime = m[0] + "-" + m[1] + "-0" + i + " 00:00:00";
					map.put("startTime", startTime);
					Date date = null;
					try {
						date = new Date(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startTime).getTime()
								+ 24 * 60 * 60 * 1000);
					} catch (ParseException e) {
						e.printStackTrace();
					}
					map.put("endTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date));
					map.put("date", m[0] + "." + m[1] + ".0" + i);
				} else {
					String startTime = m[0] + "-" + m[1] + "-" + i + " 00:00:00";
					map.put("startTime", startTime);
					Date date = null;
					try {
						date = new Date(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startTime).getTime()
								+ 24 * 60 * 60 * 1000);
					} catch (ParseException e) {
						e.printStackTrace();
					}
					map.put("endTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date));
					map.put("date", m[0] + "." + m[1] + "." + i);
				}
				list.add(map);
			}
		} else {
			try {
				long d = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(month).getTime();
				String day = new SimpleDateFormat("yyyy-MM-dd").format(new Date(d - 12 * 60 * 60 * 1000));
				String[] m = day.split("-");
				int n = Integer.parseInt(m[2]);
				for (int i = 1; i <= n; i++) {
					Map<String, Object> map = new HashMap<String, Object>();
					if (i < 10) {
						String startTime = m[0] + "-" + m[1] + "-0" + i + " 00:00:00";
						map.put("startTime", startTime);
						Date date = null;
						try {
							date = new Date(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startTime).getTime()
									+ 24 * 60 * 60 * 1000);
						} catch (ParseException e) {
							e.printStackTrace();
						}
						map.put("endTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date));
						map.put("date", m[0] + "." + m[1] + ".0" + i);
					} else {
						String startTime = m[0] + "-" + m[1] + "-" + i + " 00:00:00";
						map.put("startTime", startTime);
						Date date = null;
						try {
							date = new Date(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startTime).getTime()
									+ 24 * 60 * 60 * 1000);
						} catch (ParseException e) {
							e.printStackTrace();
						}
						map.put("endTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date));
						map.put("date", m[0] + "." + m[1] + "." + i);
					}
					list.add(map);
				}

			} catch (ParseException e) {
			}
		}
		Collections.reverse(list);
		return list;
	}

	/**
	 * 获取指定时间区间里的日期集合
	 * 
	 * @param startTime("yyyy-MM-dd")
	 * @param endTime("yyyy-MM-dd")
	 * @return
	 */
	public static List<String> getDays(String startTime, String endTime) {
		List<String> days = new ArrayList<String>();
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date d1 = format.parse(startTime);
			Date d2 = format.parse(endTime);
			if (startTime.equals(endTime)) {
				days.add(startTime);
			}
			// 判断时间的先后顺序是否合理
			else if (d2.after(d1)) {
				days.add(startTime);
				Calendar cal = Calendar.getInstance();
				// 使用开始时间设置此 Calendar 的时间
				cal.setTime(d1);
				boolean c = true;
				while (c) {
					// 根据日历的规则，为给定的日历字段添加或减去指定的时间量
					cal.add(Calendar.DAY_OF_MONTH, 1);
					// 测试此日期是否在指定日期之后
					if (d2.after(cal.getTime())) {
						String day = format.format(cal.getTime());
						days.add(day);
					} else {
						break;
					}
				}
				days.add(endTime);// 把结束时间加入集合
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return days;
	}

	/**
	 * 获取指定时间区间里的月份集合
	 * 
	 * @param startTime("yyyy-MM")
	 * @param endTime("yyyy-MM")
	 * @return
	 */
	public static List<String> getMonth(String startTime, String endTime) {
		List<String> month = new ArrayList<String>();
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
			Date d1 = format.parse(startTime);
			Date d2 = format.parse(endTime);
			if (startTime.equals(endTime)) {
				month.add(startTime);
			}
			// 判断时间的先后顺序是否合理
			else if (d2.after(d1)) {
				month.add(startTime);
				Calendar cal = Calendar.getInstance();
				// 使用开始时间设置此 Calendar 的时间
				cal.setTime(d1);
				boolean c = true;
				while (c) {
					// 根据日历的规则，为给定的日历字段添加或减去指定的时间量
					cal.add(Calendar.MONTH, 1);
					// 测试此日期是否在指定日期之后
					if (d2.after(cal.getTime())) {
						String day = format.format(cal.getTime());
						month.add(day);
					} else {
						break;
					}
				}
				month.add(endTime);// 把结束时间加入集合
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return month;
	}

	/**
	 * 得到一个月的最后一天
	 * 
	 * @param month
	 * @return
	 */
	public static String getLastDayOfMonth(String month) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
			Date date = sdf.parse(month);
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			c.add(Calendar.MONTH, 1);
			c.set(Calendar.DAY_OF_MONTH, 0);

			SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
			String lastDay = sdf2.format(c.getTime());
			return lastDay;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获得指定日期的下一天
	 * 
	 * @param day
	 * @return
	 */
	public static String getNextDay(String day) {
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date d = format.parse(day);
			Calendar cal = Calendar.getInstance();
			cal.setTime(d);
			cal.add(Calendar.DAY_OF_MONTH, 1);
			String nextDay = format.format(cal.getTime());
			return nextDay;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
}
