package com.utils.Card;

import com.sun.jna.Library;
import com.sun.jna.Native;

public interface CLibraryUtil extends Library {
	
		CLibraryUtil INSTANCE =(CLibraryUtil) Native.
				loadLibrary(System.getProperty("user.dir")+"\\Msprintsdk.dll",CLibraryUtil.class);
		/**
		 * 打开usb接口
		 * @return
		 */
		int SetUsbportauto();
		/**
		 * 打印机初始化
		 * @return
		 */
		int SetInit();
		/**
		 * 获取打印机状态
		 * @return
		 */
		int GetStatus();
		//int PrintSelfcheck(); //打印机自走页
		//int PrintString(String strData,int iImme); //打印字符串
		//int GetStatusspecial(); //获取打印机特殊功能状态
		//int PrintCutpaper(int iMode); //打印切纸
}
