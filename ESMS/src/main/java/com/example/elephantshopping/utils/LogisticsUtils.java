package com.example.elephantshopping.utils;

import java.util.HashMap;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.elephantshopping.entity.Logistics;

public class LogisticsUtils
{
	public static List<Logistics> getLogisticsInfo(String company,String awb)
	{
		String param ="{\"com\":\""+company+"\",\"num\":\""+awb+"\"}";
		String customer ="38FEF41955C4BF9CF6CA4CDA18DDB221";
		String key = "CYsKKNIs7235";
		String sign = MD5.encode(param+key+customer);
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("param",param);
		params.put("sign",sign);
		params.put("customer",customer);
		String resp;
		try {
			resp = new HttpRequest().postData("http://poll.kuaidi100.com/poll/query.do", params, "utf-8").toString();
			JSONObject jsonObject = JSON.parseObject(resp);
			List<Logistics> list = JSONArray.parseArray(jsonObject.getString("data"), Logistics.class);
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
