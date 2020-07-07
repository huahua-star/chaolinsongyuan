package com.utils;

import java.util.UUID;

/**
 * 生成UUID
 * @version 1.0
 */
public class UuidUtils {
	/**
	 * 获得一个UUID 
	 * @return String UUID
	 */
	public static String getUUID() {
		String uuid = UUID.randomUUID().toString();
		// 去掉“-”符号
		return uuid.replaceAll("-", "");
	}
}
