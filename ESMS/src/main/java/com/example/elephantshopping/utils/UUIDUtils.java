package com.example.elephantshopping.utils;

import java.util.UUID;

public class UUIDUtils {
	public static String randomID() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
}
