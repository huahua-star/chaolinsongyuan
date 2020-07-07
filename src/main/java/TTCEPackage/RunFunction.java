package TTCEPackage;

import java.util.HashMap;
import java.util.Map;

public class RunFunction
{
	static D1801 Function = new D1801();
	static int[] length = new int[2];
	static byte[] RecvBuf = new byte[300];
	
	public static Map<Integer,String> sendCardToExit(int com)
	{	
		 Map<Integer,String> map=new HashMap<>();
		String StrMessage = "";
		int nRet;
		int i;
		byte MacAddress = -1;
		int ComHandle = 0;
		
		ComHandle = Function.CommOpen(com);//此处输入你们端口号，比如COM1,你就输入1，COM2，就输入2....
		if(ComHandle == 0)
		{
			System.out.println("串口打开失败");
			map.put(0, "串口打开失败");
			return map;
		}
		System.out.println("串口打开成功");
		
		nRet = Function.GetDllVersion(ComHandle, RecvBuf);
		if(nRet != 0)
		{
			System.out.println("获取动态库版本失败");
			map.put(0, "获取动态库版本失败");
			map.put(1, ComHandle+"");
			return map;
		}
		StrMessage = Function.ByteToString(RecvBuf);
		System.out.println("获取动态库版本成功，版本号为：" + StrMessage);
		
		for(i = 0; i < 16; i++)//此处遍历0-15的Mac地址
		{
			nRet = Function.AutoTestMac(ComHandle, (byte)i);
			if(nRet == 0)
			{
				MacAddress = (byte)i;
				break;
			}
		}
		if(MacAddress >= 0)
		{
			System.out.println("获取Mad地址成功，地址为：" + MacAddress);
		}
		else
		{
			System.out.println("获取Mad地址失败");
			map.put(0, "获取Mad地址失败");
			map.put(1, ComHandle+"");
			return map;
		}
		
		for(i = 0; i < 300; i++)
			RecvBuf[i] = 0;
		nRet = Function.GetSysVersion(ComHandle, MacAddress, RecvBuf);
		if(nRet != 0)
		{
			System.out.println("获取设备固件版本失败");
			map.put(0, "获取设备固件版本失败");
			map.put(1, ComHandle+"");
			return map;
		}
		StrMessage = Function.ByteToString(RecvBuf);
		System.out.println("获取设备固件版本成功，版本号为：" + StrMessage);

		nRet = Function.SensorQuery(ComHandle, MacAddress, RecvBuf);//高级查询
		if(nRet != 0)
		{
			System.out.println("获取设备传感器状态失败--高级查询");
			map.put(0, "获取设备传感器状态失败--高级查询");
			map.put(1, ComHandle+"");
			return map;
		}
		else
		{
			StrMessage = "";
			switch(RecvBuf[0])
			{
			case 0x38:
				StrMessage += "命令状态：回收卡箱已满\r\n";
				StrMessage += "卡箱状态：卡箱非预空\r\n";
				break;
			case 0x34://命令不能执行
				StrMessage += "命令状态：命令不能执行，请点击执行复位指令\r\n";
				break;
			case 0x32://准备卡失败
				StrMessage += "命令状态：命令不能执行，请点击执行复位指令\r\n";
				break;
			case 0x31://正在准备卡
				StrMessage += "命令状态：正在准备卡\r\n";
				break;
			case 0x30://机器空闲
				StrMessage += "命令状态：机器空闲\r\n";
				break;
			}
			
			switch(RecvBuf[1])
			{
			case 0x38://正在发卡
				StrMessage += "命令状态：正在发卡\r\n";
				break;
			case 0x34://正在收卡
				StrMessage += "命令状态：正在收卡\r\n";
				break;
			case 0x32://发卡出错
				StrMessage += "命令状态：发卡出错，请点击执行复位指令\r\n";
				break;
			case 0x31://收卡出错
				StrMessage += "命令状态：收卡出错，请点击执行复位指令\r\n";
				break;
			case 0x30://没有任何动作
				StrMessage += "命令状态：没有任何动作\r\n";
				break;
			}
			
			switch(RecvBuf[3])
			{
			case 0x39:
				StrMessage += "命令状态：回收卡箱已满\r\n";
				StrMessage += "卡箱状态：卡箱预空\r\n";
				break;
			case 0x38:
				StrMessage += "命令状态：回收卡箱已满\r\n";
				StrMessage += "卡箱状态：卡箱非预空\r\n";
				break;
			case 0x34:
				StrMessage += "卡箱状态：重叠卡\r\n";
				break;
			case 0x32:
				StrMessage += "卡箱状态：卡堵塞\r\n";
				break;
			case 0x31://正在准备卡
				StrMessage += "卡箱状态：卡箱预空\r\n";
				break;
			case 0x30://机器空闲
				StrMessage += "卡箱状态：卡箱非预空\r\n";
				break;
			}
			
			switch(RecvBuf[3])
			{
			case 0x3E:
				StrMessage += "卡片位置：只有一张卡在传感器2-3位置\r\n";
				break;
			case 0x3B:
				StrMessage += "卡片位置：只有一张卡在传感器1-2位置\r\n";
				break;
			case 0x39:
				StrMessage += "卡片位置：只有一张卡在传感器1位置\r\n";
				break;
			case 0x38:
				StrMessage += "卡片位置：发卡箱已空\r\n";
				StrMessage += "卡箱状态：卡箱空\r\n";
				break;
			case 0x36:
				StrMessage += "卡片位置：卡在传感器2-3位置\r\n";
				break;
			case 0x34:
				StrMessage += "卡片位置：卡在传感器3位置，预发卡位置\r\n";
				break;
			case 0x33:
				StrMessage += "卡片位置：卡在传感器1-2位置，读卡位置\r\n";
				break;
			case 0x32:
				StrMessage += "卡片位置：卡在传感器2位置\r\n";
				break;
			case 0x31:
				StrMessage += "卡片位置：卡在取卡位置\r\n";
				break;
			}
			System.out.println(StrMessage);
		}
		System.out.println("获取设备传感器状态成功");

		Function.sleep(1500);//此处暂停一下
		
		RecvBuf[0] = 0x46;//F
		RecvBuf[1] = 0x43;//C
		RecvBuf[2] = 0x30;//0
		nRet = Function.SendCmd(ComHandle, MacAddress, RecvBuf, 3);//发卡到取卡位置--FC0
		if(nRet != 0)
		{
			System.out.println("发卡到取卡位置失败");
			map.put(0, "发卡到取卡位置失败");
			map.put(1, ComHandle+"");
			return map;
		}
		System.out.println("发卡到取卡位置成功");
		
		RecvBuf[0] = 0x52;//R
		RecvBuf[1] = 0x53;//S
		nRet = Function.SendCmd(ComHandle, MacAddress, RecvBuf, 2);//复位--RS
		if(nRet != 0)
		{
			System.out.println("复位失败");
			map.put(0, "复位失败");
			map.put(1, ComHandle+"");
			return map;
		}
		System.out.println("复位成功");
		
		nRet = Function.CommClose(ComHandle);
		if(nRet != 0)
		{
			System.out.println("串口关闭失败");
			map.put(0, "串口关闭失败");
			map.put(1, ComHandle+"");
			return map;
		}
		System.out.println("串口关闭成功");
		map.put(0, "-1");
		return map;
	}
	
	public static Map<Integer,String> openAndSend(int com) {
		
		 Map<Integer,String> map=new HashMap<>();
		 
		String StrMessage = "";
		int nRet;
		int i;
		byte MacAddress = -1;
		int ComHandle = 0;
		
		ComHandle = Function.CommOpen(com);//此处输入你们端口号，比如COM1,你就输入1，COM2，就输入2....
		if(ComHandle == 0)
		{
			System.out.println("串口打开失败");
			map.put(0, "1");
			return map;
		}
		System.out.println("串口打开成功");
		
		nRet = Function.GetDllVersion(ComHandle, RecvBuf);
		if(nRet != 0)
		{
			System.out.println("获取动态库版本失败");
			map.put(0, "获取动态库版本失败");
			map.put(1, ComHandle+"");
			return map;
		}
		StrMessage = Function.ByteToString(RecvBuf);
		System.out.println("获取动态库版本成功，版本号为：" + StrMessage);
		
		for(i = 0; i < 16; i++)//此处遍历0-15的Mac地址
		{
			nRet = Function.AutoTestMac(ComHandle, (byte)i);
			if(nRet == 0)
			{
				MacAddress = (byte)i;
				break ;
			}
		}
		if(MacAddress >= 0)
		{
			System.out.println("获取Mad地址成功，地址为：" + MacAddress);
		}
		else
		{
			System.out.println("获取Mad地址失败");
			map.put(0, "获取Mad地址失败");
			map.put(1, ComHandle+"");
			return map;
		}
		
		for(i = 0; i < 300; i++)
			RecvBuf[i] = 0;
		nRet = Function.GetSysVersion(ComHandle, MacAddress, RecvBuf);
		if(nRet != 0)
		{
			System.out.println("获取设备固件版本失败");
			map.put(0, "获取设备固件版本失败");
			map.put(1, ComHandle+"");
			return map;
		}
		StrMessage = Function.ByteToString(RecvBuf);
		System.out.println("获取设备固件版本成功，版本号为：" + StrMessage);
		
		nRet = Function.SensorQuery(ComHandle, MacAddress, RecvBuf);//高级查询
		if(nRet != 0)
		{
			System.out.println("获取设备传感器状态失败--高级查询");
			map.put(0, "获取设备传感器状态失败--高级查询");
			map.put(1, ComHandle+"");
			return map;
		}
		else
		{
			StrMessage = "";
			switch(RecvBuf[0])
			{
			case 0x38:
				StrMessage += "命令状态：回收卡箱已满\r\n";
				StrMessage += "卡箱状态：卡箱非预空\r\n";
				break;
			case 0x34://命令不能执行
				StrMessage += "命令状态：命令不能执行，请点击执行复位指令\r\n";
				break;
			case 0x32://准备卡失败
				StrMessage += "命令状态：命令不能执行，请点击执行复位指令\r\n";
				break;
			case 0x31://正在准备卡
				StrMessage += "命令状态：正在准备卡\r\n";
				break;
			case 0x30://机器空闲
				StrMessage += "命令状态：机器空闲\r\n";
				break;
			}
			
			switch(RecvBuf[1])
			{
			case 0x38://正在发卡
				StrMessage += "命令状态：正在发卡\r\n";
				break;
			case 0x34://正在收卡
				StrMessage += "命令状态：正在收卡\r\n";
				break;
			case 0x32://发卡出错
				StrMessage += "命令状态：发卡出错，请点击执行复位指令\r\n";
				break;
			case 0x31://收卡出错
				StrMessage += "命令状态：收卡出错，请点击执行复位指令\r\n";
				break;
			case 0x30://没有任何动作
				StrMessage += "命令状态：没有任何动作\r\n";
				break;
			}
			
			switch(RecvBuf[3])
			{
			case 0x39:
				StrMessage += "命令状态：回收卡箱已满\r\n";
				StrMessage += "卡箱状态：卡箱预空\r\n";
				break;
			case 0x38:
				StrMessage += "命令状态：回收卡箱已满\r\n";
				StrMessage += "卡箱状态：卡箱非预空\r\n";
				break;
			case 0x34:
				StrMessage += "卡箱状态：重叠卡\r\n";
				break;
			case 0x32:
				StrMessage += "卡箱状态：卡堵塞\r\n";
				break;
			case 0x31://正在准备卡
				StrMessage += "卡箱状态：卡箱预空\r\n";
				break;
			case 0x30://机器空闲
				StrMessage += "卡箱状态：卡箱非预空\r\n";
				break;
			}
			
			switch(RecvBuf[3])
			{
			case 0x3E:
				StrMessage += "卡片位置：只有一张卡在传感器2-3位置\r\n";
				break;
			case 0x3B:
				StrMessage += "卡片位置：只有一张卡在传感器1-2位置\r\n";
				break;
			case 0x39:
				StrMessage += "卡片位置：只有一张卡在传感器1位置\r\n";
				break;
			case 0x38:
				StrMessage += "卡片位置：发卡箱已空\r\n";
				StrMessage += "卡箱状态：卡箱空\r\n";
				break;
			case 0x36:
				StrMessage += "卡片位置：卡在传感器2-3位置\r\n";
				break;
			case 0x34:
				StrMessage += "卡片位置：卡在传感器3位置，预发卡位置\r\n";
				break;
			case 0x33:
				StrMessage += "卡片位置：卡在传感器1-2位置，读卡位置\r\n";
				break;
			case 0x32:
				StrMessage += "卡片位置：卡在传感器2位置\r\n";
				break;
			case 0x31:
				StrMessage += "卡片位置：卡在取卡位置\r\n";
				break;
			}
			System.out.println(StrMessage);
		}
		System.out.println("获取设备传感器状态成功");
		
		RecvBuf[0] = 0x46;//F
		RecvBuf[1] = 0x43;//C
		RecvBuf[2] = 0x37;//7
		nRet = Function.SendCmd(ComHandle, MacAddress, RecvBuf, 3);//发卡到读卡位置--FC7
		if(nRet != 0)
		{
			System.out.println("发卡到读卡位置失败");
			map.put(0, "发卡到读卡位置失败");
			map.put(1, ComHandle+"");
			return map;
		}
		System.out.println("发卡到读卡位置成功");
		
		RecvBuf[0] = 0x52;//R
		RecvBuf[1] = 0x53;//S
		nRet = Function.SendCmd(ComHandle, MacAddress, RecvBuf, 2);//复位--RS
		if(nRet != 0)
		{
			System.out.println("复位失败");
			map.put(0, "复位失败");
			map.put(1, ComHandle+"");
			return map;
		}
		System.out.println("复位成功");
		
		nRet = Function.CommClose(ComHandle);
		if(nRet != 0)
		{
			System.out.println("串口关闭失败");
			map.put(0, "串口关闭失败");
			map.put(1, ComHandle+"");
			return map;
		}
		System.out.println("串口关闭成功");
		map.put(0, "-1");
		return map;
	}
	public static int returnCard(int com) {
		String StrMessage = "";
		int nRet;
		int i;
		byte MacAddress = -1;
		int ComHandle = 0;
		
		ComHandle = Function.CommOpen(com);//此处输入你们端口号，比如COM1,你就输入1，COM2，就输入2....
		if(ComHandle == 0)
		{
			System.out.println("串口打开失败");
			return 1;
		}
		System.out.println("串口打开成功");
		
		/*nRet = Function.GetDllVersion(ComHandle, RecvBuf);
		if(nRet != 0)
		{
			System.out.println("获取动态库版本失败");
			return 2;
		}
		StrMessage = Function.ByteToString(RecvBuf);
		System.out.println("获取动态库版本成功，版本号为：" + StrMessage);*/
		
		for(i = 0; i < 16; i++)//此处遍历0-15的Mac地址
		{
			nRet = Function.AutoTestMac(ComHandle, (byte)i);
			if(nRet == 0)
			{
				MacAddress = (byte)i;
				break ;
			}
		}
		if(MacAddress >= 0)
		{
			System.out.println("获取Mad地址成功，地址为：" + MacAddress);
		}
		else
		{
			System.out.println("获取Mad地址失败");
			return 3;
		}
		
		for(i = 0; i < 300; i++)
			RecvBuf[i] = 0;
		nRet = Function.GetSysVersion(ComHandle, MacAddress, RecvBuf);
		if(nRet != 0)
		{
			System.out.println("获取设备固件版本失败");
			return 4;
		}
		StrMessage = Function.ByteToString(RecvBuf);
		System.out.println("获取设备固件版本成功，版本号为：" + StrMessage);
		
		nRet = Function.SensorQuery(ComHandle, MacAddress, RecvBuf);//高级查询
		if(nRet != 0)
		{
			System.out.println("获取设备传感器状态失败--高级查询");
			return 5;
		}
		else
		{
			StrMessage = "";
			switch(RecvBuf[0])
			{
			case 0x38:
				StrMessage += "命令状态：回收卡箱已满\r\n";
				StrMessage += "卡箱状态：卡箱非预空\r\n";
				break;
			case 0x34://命令不能执行
				StrMessage += "命令状态：命令不能执行，请点击执行复位指令\r\n";
				break;
			case 0x32://准备卡失败
				StrMessage += "命令状态：命令不能执行，请点击执行复位指令\r\n";
				break;
			case 0x31://正在准备卡
				StrMessage += "命令状态：正在准备卡\r\n";
				break;
			case 0x30://机器空闲
				StrMessage += "命令状态：机器空闲\r\n";
				break;
			}
			
			switch(RecvBuf[1])
			{
			case 0x38://正在发卡
				StrMessage += "命令状态：正在发卡\r\n";
				break;
			case 0x34://正在收卡
				StrMessage += "命令状态：正在收卡\r\n";
				break;
			case 0x32://发卡出错
				StrMessage += "命令状态：发卡出错，请点击执行复位指令\r\n";
				break;
			case 0x31://收卡出错
				StrMessage += "命令状态：收卡出错，请点击执行复位指令\r\n";
				break;
			case 0x30://没有任何动作
				StrMessage += "命令状态：没有任何动作\r\n";
				break;
			}
			
			switch(RecvBuf[3])
			{
			case 0x39:
				StrMessage += "命令状态：回收卡箱已满\r\n";
				StrMessage += "卡箱状态：卡箱预空\r\n";
				break;
			case 0x38:
				StrMessage += "命令状态：回收卡箱已满\r\n";
				StrMessage += "卡箱状态：卡箱非预空\r\n";
				break;
			case 0x34:
				StrMessage += "卡箱状态：重叠卡\r\n";
				break;
			case 0x32:
				StrMessage += "卡箱状态：卡堵塞\r\n";
				break;
			case 0x31://正在准备卡
				StrMessage += "卡箱状态：卡箱预空\r\n";
				break;
			case 0x30://机器空闲
				StrMessage += "卡箱状态：卡箱非预空\r\n";
				break;
			}
			
			switch(RecvBuf[3])
			{
			case 0x3E:
				StrMessage += "卡片位置：只有一张卡在传感器2-3位置\r\n";
				break;
			case 0x3B:
				StrMessage += "卡片位置：只有一张卡在传感器1-2位置\r\n";
				break;
			case 0x39:
				StrMessage += "卡片位置：只有一张卡在传感器1位置\r\n";
				break;
			case 0x38:
				StrMessage += "卡片位置：发卡箱已空\r\n";
				StrMessage += "卡箱状态：卡箱空\r\n";
				break;
			case 0x36:
				StrMessage += "卡片位置：卡在传感器2-3位置\r\n";
				break;
			case 0x34:
				StrMessage += "卡片位置：卡在传感器3位置，预发卡位置\r\n";
				break;
			case 0x33:
				StrMessage += "卡片位置：卡在传感器1-2位置，读卡位置\r\n";
				break;
			case 0x32:
				StrMessage += "卡片位置：卡在传感器2位置\r\n";
				break;
			case 0x31:
				StrMessage += "卡片位置：卡在取卡位置\r\n";
				break;
			}
			System.out.println(StrMessage);
		}
		System.out.println("获取设备传感器状态成功");
		
		
		/*88888888888888888888888888888--修改发卡位置-start--888888888888888888888888888888888*/
		RecvBuf[0] = 0x46;//F
		RecvBuf[1] = 0x43;//C
		RecvBuf[2] = 0x37;//7
		nRet = Function.SendCmd(ComHandle, MacAddress, RecvBuf, 3);//发卡到读卡位置--FC7
		if(nRet != 0)
		{
			System.out.println("发卡到复位位置失败");
		}
		/*88888888888888888888888888888--修改发卡位置-end--888888888888888888888888888888888*/
		RecvBuf[0] = 0x52;//R
		RecvBuf[1] = 0x53;//S
		nRet = Function.SendCmd(ComHandle, MacAddress, RecvBuf, 2);//复位--RS
		if(nRet != 0)
		{
			System.out.println("复位失败");
			return 7;
		}
		System.out.println("复位成功");
		
		nRet = Function.CommClose(ComHandle);
		if(nRet != 0)
		{
			System.out.println("串口关闭失败");
			return 8;
		}
		System.out.println("串口关闭成功");
		return -1;
	}

	public static void colse(Integer comHandle) {
		/*int ComHandle = Function.CommOpen(comHandle);
		if(ComHandle == 0)
		{
			int commClose = Function.CommClose(ComHandle);
			if(commClose != 0)
			{
				System.out.println("发卡机串口关闭失败");
			}
		}*/
		int commClose = Function.CommClose(comHandle);
		if(commClose != 0)
		{
			System.out.println("遇到错误发卡机串口 --》关闭失败");
		}
		System.out.println("遇到错误发卡机串口 --》关闭成功");
	}
}

