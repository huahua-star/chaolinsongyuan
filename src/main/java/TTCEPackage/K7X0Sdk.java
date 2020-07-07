package TTCEPackage;


import com.utils.Returned3.R;
import com.utils.ComHandleMap;

import java.util.HashMap;
import java.util.Map;

public class K7X0Sdk {
	static K7X0 Reader=new K7X0();
	static int ComHandle;
	static byte MacAddr = 0x00;
	static byte[] RecvBuf=new byte[300];
	static int[] length=new int[2];
	
	//方法区
	
	/**
	 * 发卡到出卡位置
	 * @param com
	 * @return
	 */
	public static Map<Integer,String> sendCardToExit()
	{	
		Map<Integer,String> map=new HashMap<>();
		int i=0, nRet = 0;

		RecvBuf[0] = 0x44;//D
		RecvBuf[1] = 0x43;//C
		nRet = Reader.SendCmd(ComHandle, MacAddr, RecvBuf,2);//发卡到取卡位置--DC
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
		nRet = Reader.SendCmd(ComHandle, MacAddr, RecvBuf, 2);//复位--RS
		if(nRet != 0)
		{
			System.out.println("复位失败");
			map.put(0, "复位失败");
			map.put(1, ComHandle+"");
			return map;
		}
		System.out.println("复位成功");
		map.put(0, "-1");
		return map;
	}
	
	/**
	 * 发卡到读卡位置
	 * @param com
	 * @return
	 */
	public static Map<Integer,String> openAndSend() {
		
		Map<Integer,String> map=new HashMap<>();
		int nRet = 0;
	
		
/*		dd
		
		查询 状态
		
		*/
		
		RecvBuf[0] = 0x46;//F
		RecvBuf[1] = 0x43;//C
		RecvBuf[2] = 0x36;//7
		nRet = Reader.SendCmd(ComHandle, MacAddr,RecvBuf, 3);//发卡到读卡位置--FC7 
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
		nRet = Reader.SendCmd(ComHandle, MacAddr, RecvBuf, 2);//复位--RS
		if(nRet != 0)
		{
			System.out.println("复位失败");
			map.put(0, "复位失败");
			map.put(1, ComHandle+"");
			return map;
		}
		System.out.println("复位成功");
		
		map.put(0, "-1");
		return map;
	}
	
	/**
	 * 收卡
	 * @param com
	 * @return
	 */
	public static Map<Integer,String> takeCard() {
		
		Map<Integer,String> map=new HashMap<>();
		int nRet = 0;
	
		
/*		dd
		
		查询 状态
		
		*/
		
		RecvBuf[0] = 0x43;//C
		RecvBuf[1] = 0x50;//P
		nRet = Reader.SendCmd(ComHandle, MacAddr,RecvBuf, 2);//收卡--CP
		if(nRet != 0)
		{
			System.out.println("收卡失败");
			map.put(0, "收卡失败");
			map.put(1, ComHandle+"");
			return map;
		}
		System.out.println("收卡成功");
		
		RecvBuf[0] = 0x52;//R
		RecvBuf[1] = 0x53;//S
		nRet = Reader.SendCmd(ComHandle, MacAddr, RecvBuf, 2);//复位--RS
		if(nRet != 0)
		{
			System.out.println("复位失败");
			map.put(0, "复位失败");
			map.put(1, ComHandle+"");
			return map;
		}
		System.out.println("复位成功");
		
		map.put(0, "-1");
		return map;
	}
	
	
	public static void colse(Integer comHandle) {
	
		int commClose = Reader.CommClose(comHandle);
		if(commClose != 0)
		{
			System.out.println("遇到错误发卡机串口 --》关闭失败");
		}
		System.out.println("遇到错误发卡机串口 --》关闭成功");
	}
	/**
	 * 查询状态
	 * 
	 * @param comHandle
	 * @return
	 */
	public static synchronized R getStatic() {
		R e =new R();
		int err=0, nRet = 0;
		boolean isTake=false,isSend=false,isEmpty=false,isFull=false;
		nRet = Reader.SensorQuery(ComHandle, MacAddr, RecvBuf);
		if(nRet != 0)
		{
			System.out.println("Check Card Position Failed");
			
			return R.error("Check Card Position Failed");
		}	
		
		System.out.println("获取设备传感器状态成功");
		
		String str="";
		switch(RecvBuf[0])
		{
		case 0x39:
			str += "机器状态：回收箱卡满/卡箱预满\r\n";
			break;
		case 0x38:
			str += "机器状态：回收箱卡满\r\n";
			isFull=true;
			break;
		case 0x34:
			str += "机器状态：命令不能执行，请点击“复位”\r\n";
			err=1;
			break;
		case 0x32:
			str += "机器状态：准备卡失败，请点击“复位”\r\n";
			err=1;
			break;
		case 0x31:
			str += "机器状态：卡箱预满\r\n";
			break;
		case 0x30:
			str += "机器状态：空闲\r\n";
			break;
		}

		switch(RecvBuf[1])
		{
		case 0x38:
			str += "动作状态：正在发卡\r\n";
			isSend=true;
			break;
		case 0x34:
			str += "动作状态：正在收卡\r\n";
			err=1;
			break;
		case 0x32:
			str += "动作状态：发卡出错，请点击“复位”\r\n";
			err=1;
			break;
		case 0x31:
			str += "动作状态：收卡出错，请点击“复位”\r\n";
			err=1;
			break;
		case 0x30:
			str += "动作状态：空闲\r\n";
			break;
		}
		switch(RecvBuf[2])
		{
		case 0x39:
			str += "卡箱状态：发卡箱已满，无法再回收到发卡箱\r\n";
			isFull=true;
			break;
		case 0x38:
			str += "卡箱状态：发卡箱已满，无法再回收到发卡箱/卡箱预空\r\n";
			break;
		case 0x34:
			str += "卡箱状态：重叠卡\r\n";
			break;
		case 0x32:
			str += "卡箱状态：卡堵塞\r\n";
			break;
		case 0x31:
			str += "卡箱状态：卡箱预空\r\n";
			break;
		case 0x30:
			str += "卡箱状态：卡箱为非预空状态\r\n";
			break;
		}

		switch(RecvBuf[3])
		{
		case 0x3E:
			str += "卡片状态：只有一张卡在传感器2-3位置\r\n";
			break;
		case 0x3B:
			str += "卡片状态：只有一张卡在传感器1-2位置\r\n";
			isTake=true;
			break;
		case 0x39:
			str += "卡片状态：只有一张卡在传感器1位置\r\n";
			isTake=true;
			break;
		case 0x38:
			isEmpty=true;
			str += "卡片状态：卡箱已空\r\n";
			break;
		case 0x37:
			str += "卡片状态：卡在传感器1-2-3的位置\r\n";
			break;
		case 0x36:
			str += "卡片状态：卡在传感器2-3的位置\r\n";
			break;
		case 0x35:
			str += "卡片状态：卡在传感器取卡位置\r\n";
			isTake=true;
			break;
		case 0x34:
			str += "卡片状态：卡在传感器3位置\r\n";
			break;
		case 0x33:
			str += "卡箱状态：卡在传感器1-2位置(读卡位置)\r\n";
			break;
		case 0x32:
			str += "卡箱状态：卡在传感器2位置\r\n";
			break;
		case 0x31:
			str += "卡箱状态：卡在传感器1位置(取卡位置)\r\n";
			isTake=true;
			break;
		case 0x30:
			str += "卡箱状态：空闲\r\n";
			break;
		}
		String vews=str.replace("\r\n","====");
		System.out.println("**********************状态************************");
		System.out.println("**                                               **");
		System.out.println("**                                               **");
		System.out.println("** "+vews+" **");
		System.out.println("**                                               **");
		System.out.println("**                                               **");
		System.out.println("************************************************");
		
		
		return R.ok(str).put("isTake",isTake).put("isSend", isSend).put("isEmpty", isEmpty).put("isFull", isFull);
	}
	
	/**
	 *  开启 卡机串口
	 * @param comHandle
	 * @return
	 */
	public static boolean open(Integer comHandle) {
		
		int i=0, nRet = 0;
		
		ComHandle = Reader.CommOpen("COM"+comHandle);
		if(ComHandle == 0)
		{
			System.out.println("发卡器串口打开失败+=========>com:"+comHandle);
			
			return false;
		}
		
		System.out.println("发卡器串口打开成功+=========>com:"+comHandle);
		
		for(i = 0; i < 16; i++)
		{
			nRet = Reader.AutoTestMac(ComHandle, (byte)i);
			if(nRet == 0)
			{
				MacAddr = (byte)i;
				break;
			}
		}
		if(nRet != 0)
		{
			System.out.println("未找到K7X0设备，请检查串口和电源线是否连接正常或者端口号是否选择正确");
			colse(ComHandleMap.COMHANDLE);
			return false;
		}
		else
		{
			System.out.println("设备连接成功，设备地址未：" + MacAddr);
		}
		
		
		byte[] Version=new byte[20];
		nRet = Reader.GetSysVersion(ComHandle, Version);
		if(nRet == 0)
		{
			System.out.println("K7X0_Dll Version: " + Reader.ByteToString(Version));
		}
		else {
			System.out.println("Failed");
		}
		return true;
		
	}
}
