package com.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 系统串口管理
 * @author fengjiening
 *
 */
public class ComHandleMap {
	/**
	 * 卡机com
	 */
	public static final int COMHANDLE = 3;
	/**
	 * 前台设备唯一标示
	 */
	public static final int WORKGROUP_1 =1;
	
	/**
	 * 目前没用
	 * key 工作组
	 * value 开启串口后的 handle
	 */
	public static Map<Integer,Integer> comHandleMap =new ConcurrentHashMap<Integer,Integer>();
	
	
	public static synchronized void put(Integer key,Integer value){
		comHandleMap.put(key, value);
	}
	
	public static synchronized void remove(Integer key){
		comHandleMap.remove(key);
	}
}
