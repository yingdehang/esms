package com.example.elephantshopping.utils;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 实现对cookie的操作
 * 
 * @author XB
 *
 */
public class CookiesUtils {
	/**
	 * 读取cookie
	 * 
	 * @param request
	 * @return
	 */
	public static Map<String, Cookie> readCookies(HttpServletRequest request) {
		Map<String, Cookie> map = new HashMap<String, Cookie>();
		Cookie[] cookies = request.getCookies();// 这样便可以获取一个cookie数组
		if (null == cookies) {
			System.out.println("没有cookie=========");
		} else {
			for (Cookie cookie : cookies) {
				map.put(cookie.getName(), cookie);
			}
		}
		return map;
	}

	/**
	 * 添加cookie
	 */
	public static void addCookies(HttpServletResponse response, String name, String value) {
		Cookie cookie = new Cookie(name.trim(), value.trim());
		cookie.setPath("/");
		// System.out.println("添加成功=========");
		response.addCookie(cookie);
	}

}
