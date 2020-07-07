package TTCEPackage;

import java.util.HashMap;
import java.util.Map;

public class RunFunction1 {
	
	static RF610 Reader=new RF610();
	static int ComHandle;
	
	static int[] length=new int[2];
	static byte[] Data=new byte[16];
	
	/**
	 * 
	* @Title: CommOpenAndWrite  
	* @Description:
	* @param @param com 串口
	* @param @param Sdict 扇区
	* @param @param KeyA 密码个是（KEYa,keyb）
	* @param @return    参数  
	* @return String    返回类型  
	* @throws
	 */
	public static Map<Integer,String>  CommOpenAndWrite(String com,byte Sdict ,byte block,byte KeyType,String dataStr)
	{
		 Map<Integer,String> map=new HashMap<>();
		String StrMessage="";
		
		ComHandle = Reader.CommOpen(com);
		if(ComHandle == 0)
		{
			System.out.println("Open "+com +" Failed");
			map.put(0, "Open com "+com +"串口  Failed");
			return map;
		}
		
		byte[] Version=new byte[50];
		int nRet = Reader.GetSysVersion(ComHandle, Version);
		if(nRet == 0)
		{
			System.out.println("RF610_Dll Version: " + Reader.ByteToString(Version));
		}
		else {
			System.out.println("Failed");
		}
		
		nRet = Reader.GetHardVersion(ComHandle, Version);
		if(nRet != 0)
		{
			System.out.println("Check Machine Version Failed");
			map.put(0, "Check Machine Version Failed");
			map.put(1, ComHandle+"");
			return map;
		}
		else
		{
			System.out.println("Machine Version: " + Reader.ByteToString(Version));
		}
		Reader.sleep(300);
		int cout = 0;//循环20次
		while(cout <= 20)
		{
			cout++;
			nRet = Reader.S50DetectCard(ComHandle);
			if(nRet == 0)
			{
				System.out.println("寻卡成功");
				break;
			}
			else
			{
				System.out.println("正在尝试第" + cout + "次寻卡");
			}
			Reader.sleep(1000);
		}
		if(cout>=20){
			map.put(0, "寻卡失败，请联系管理员");
			map.put(1, ComHandle+"");
			return map;
		}
		
		
		nRet = Reader.S50LoadSecKey(ComHandle, (byte)0x04, (byte)0x30, CardReadPwd.RecvBuf);
		//nRet = Reader.S50LoadSecKey(ComHandle, Sdict, KeyType, CardReadPwd.RecvBuf);
		
		if(nRet != 0)
		{
			System.out.println("S50验证扇区:0x04，秘钥类型:KeyA，秘钥：FFFFFFFFFFFF。失败，请确认扇区的秘钥是否正确");
			map.put(0, "S50验证扇区:0x04，秘钥类型:KeyA，秘钥：FFFFFFFFFFFF。失败，请确认扇区的秘钥是否正确");
			map.put(1, ComHandle+"");
			return map;
		}
		
		
		nRet = Reader.S50ReadBlock(ComHandle,(byte)(0x04 * 0x04 + 0x00), Data);
		if(nRet != 0)
		{
			System.out.println("S50第一次读取扇区:0x04，块：0x00 数据失败，请确认卡片是否被移走");
			map.put(0, "S50第一次读取扇区:0x04，块：0x00 数据失败，请确认卡片是否被移走");
			map.put(1, ComHandle+"");
			return map;
		}
		System.out.println("S50第一次读取扇区:0x04，块：0x00 数据为：" + Reader.toHexString2(Data, true));
		
		for(int i = 0; i < 16; i++)
		{
			Data[i] = 0x22;
		}
		
		nRet = Reader.S50WriteBlock(ComHandle, (byte)(0x04 * 0x04 + 0x00), Data);
		if(nRet != 0)
		{
			System.out.println("S50写入扇区:0x04，块：0x00 数据失败，请确认卡片是否被移走");
			map.put(0, "S50写入扇区:0x04，块：0x00 数据失败，请确认卡片是否被移走");
			map.put(1, ComHandle+"");
			return map;
		}
		
		System.out.println("S50写入扇区:0x04，块：0x00 数据为：" + Reader.toHexString2(Data, true));

		nRet = Reader.S50ReadBlock(ComHandle, (byte)(0x04 * 0x04 + 0x00), Data);
		
		if(nRet != 0)
		{
			System.out.println("S50第二次读取扇区:0x04，块：0x00 数据失败，请确认卡片是否被移走");
			map.put(0, "S50第二次读取扇区:0x04，块：0x00 数据失败，请确认卡片是否被移走");
			map.put(1, ComHandle+"");
			return map;
		}
		
		System.out.println("S50第二次读取扇区:0x04，块：0x00 数据为：" + Reader.toHexString2(Data, true));
		
		//)))))))))))))))))))))))))))))))二次写卡（（（（（（（（（（（（（（（（（（（（（（（（（（（（（（（（（（（（
		

		nRet = Reader.S50LoadSecKey(ComHandle, (byte)0x04, (byte)0x30, CardReadPwd.RecvBuf);
		//nRet = Reader.S50LoadSecKey(ComHandle, Sdict, KeyType, CardReadPwd.RecvBuf);
		
		if(nRet != 0)
		{
			System.out.println("S50验证扇区:0x04，秘钥类型:KeyA，秘钥：FFFFFFFFFFFF。失败，请确认扇区的秘钥是否正确");
			map.put(0, "S50验证扇区:0x04，秘钥类型:KeyA，秘钥：FFFFFFFFFFFF。失败，请确认扇区的秘钥是否正确");
			map.put(1, ComHandle+"");
			return map;
		}
		
		
		nRet = Reader.S50ReadBlock(ComHandle,(byte)(0x04 * 0x04 + 0x00), Data);
		if(nRet != 0)
		{
			System.out.println("S50第一次读取扇区:0x04，块：0x00 数据失败，请确认卡片是否被移走");
			map.put(0, "S50第一次读取扇区:0x04，块：0x00 数据失败，请确认卡片是否被移走");
			map.put(1, ComHandle+"");
			return map;
		}
		System.out.println("S50第一次读取扇区:0x04，块：0x00 数据为：" + Reader.toHexString2(Data, true));
		
		for(int i = 0; i < 16; i++)
		{
			Data[i] = 0x33;
		}
		
		nRet = Reader.S50WriteBlock(ComHandle, (byte)(0x04 * 0x04 + 0x00), Data);
		if(nRet != 0)
		{
			System.out.println("S50写入扇区:0x04，块：0x00 数据失败，请确认卡片是否被移走");
			map.put(0, "S50写入扇区:0x04，块：0x00 数据失败，请确认卡片是否被移走");
			map.put(1, ComHandle+"");
			return map;
		}
		
		System.out.println("S50写入扇区:0x04，块：0x00 数据为：" + Reader.toHexString2(Data, true));

		nRet = Reader.S50ReadBlock(ComHandle, (byte)(0x04 * 0x04 + 0x00), Data);
		
		if(nRet != 0)
		{
			System.out.println("S50第二次读取扇区:0x04，块：0x00 数据失败，请确认卡片是否被移走");
			map.put(0, "S50第二次读取扇区:0x04，块：0x00 数据失败，请确认卡片是否被移走");
			map.put(1, ComHandle+"");
			return map;
		}
		
		System.out.println("S50第二次读取扇区:0x04，块：0x00 数据为：" + Reader.toHexString2(Data, true));
		
		
	// 关机
		int reset = Reader.Reset(ComHandle);
		if(reset == 0)
		{
			System.out.println("复位成功!");
		}
		else {
			System.out.println(Reader.ByteToString(Version) +" ==》复位 Failed");
			map.put(0, Reader.ByteToString(Version) +" ==》复位 Failed");
			map.put(1, ComHandle+"");
			return map;
		}
		
		nRet = Reader.CommClose(ComHandle);
		if(nRet != 0)
		{
			System.out.println("串口关闭失败");
			map.put(0, "串口关闭失败");
			map.put(1, ComHandle+"");
			return map;
		}
		map.put(0, "-1");
		System.out.println("串口关闭成功");
		return map;
	}
	//停机
	public static String shutDown(String com) {
		
		ComHandle = Reader.CommOpen(com);
		if(ComHandle == 0)
		{
			System.out.println("Open "+com +" Failed");
			return "Open com"+com +"串口 Failed";
		}
		
		byte[] Version=new byte[50];
		int nRet = Reader.GetSysVersion(ComHandle, Version);
		if(nRet == 0)
		{
			System.out.println("RF610_Dll Version: " + Reader.ByteToString(Version));
		}
		else {
			System.out.println("get Version Failed");
		}
		int reset = Reader.Reset(ComHandle);
		if(reset == 0)
		{
			System.out.println("复位成功!");
		}
		else {
			System.out.println(Reader.ByteToString(Version) +" ==》复位 Failed");
			return Reader.ByteToString(Version) +" ==》复位 Failed";
		}
		int s50Halt = Reader.S50Halt(ComHandle);
		if(s50Halt == 0)
		{
			System.out.println("停机成功!");
		}
		else {
			System.out.println(Reader.ByteToString(Version) +" ==》停机 Failed");
			return Reader.ByteToString(Version) +" ==》停机 Failed";
		}
		return null;
	}
	
	static byte [][] dataD =new byte[16][16];
	public void Ss(){
		String s = "";
		byte[] bytes = s.getBytes();
		int a=bytes.length,b=16;
		System.out.println(a%b==0?a/b:a/b+1);
		int size =a%b==0?a/b:a/b+1;
		int index=0;
		for(int i=0;i<size;i++ ){
			byte [] newb =new byte[16];
			for(int j=0;j<16;j++){
				if(index<a){
					newb[j]=bytes[index++];
				}
			}
			wirte(newb,i);
		}
		for(int i=0;i<dataD.length;i++){
			for(int j=0;j<dataD[i].length;j++){
			System.out.print(dataD[i][j]+"\t");
			}
			System.out.println();
		}
	
		byte [] str =new byte[a];
		index=0;
		for(int i=0;i<dataD.length;i++){
			for(int j=0;j<dataD[i].length;j++){
				if(dataD[i][j]!=(byte)0){
					str [index++] =dataD[i][j];
				}
			}
		}
		
		String srt2=new String(str);
		System.out.println(srt2);
		
	}
	private static void wirte(byte[] newb,int col) {
		for(int i =0;i<newb.length;i++){
			dataD[col][i]=newb[i];
		}
	}
	public static void colse(int ComHandle) {
		/*int ComHandle = Reader.CommOpen(string);
		if(ComHandle == 0)
		{	
			int commClose = Reader.CommClose(ComHandle);
			if(commClose != 0)
			{
				System.out.println("读卡器串口关闭失败");
			}
		}*/
		int commClose = Reader.CommClose(ComHandle);
		if(commClose != 0)
		{
			System.out.println("遇到错误读卡器串口 --》关闭失败");
		}
		System.out.println("遇到错误读卡器串口 --》关闭成功");
	}
	
}
