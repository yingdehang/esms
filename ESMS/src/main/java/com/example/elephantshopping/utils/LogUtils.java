package com.example.elephantshopping.utils;

import javax.servlet.http.HttpServletRequest;
import com.example.elephantshopping.entity.Log;

public class LogUtils {

	public static Log insertlog(String uSERS_ID, String aCTION_NAME, String nOTE, HttpServletRequest request) {
		Log log = new Log();
		log.setEORID(UUIDUtils.randomID());
		log.setUSERS_ID(uSERS_ID);
		log.setACTION_NAME(aCTION_NAME);
		log.setNOTE(nOTE);
		if (request.getHeader("x-forwarded-for") == null) {
			log.setIP(request.getRemoteAddr());
		} else {
			log.setIP(request.getHeader("x-forwarded-for"));
		}
		return log;
	}
}
