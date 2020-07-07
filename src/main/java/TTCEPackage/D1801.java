package TTCEPackage;


public class D1801 {

	//此处为导入数据库
	static {
		//64bit 用的dll
		System.loadLibrary("D1801_Dll");
		//32bit 用的dll
		//System.loadLibrary("javadll");
	}
	/***************串口操作函数*******************/
	public native int CommOpen(int Port);
	public native int CommOpenWithBaud(int Port, int Baudate);
	public native int CommClose(int ComHandle);
	
	/***************获取dll版本信息*****************/
	public native int GetDllVersion(int ComHandle, byte[] Version);
	
	/***************获取设备固件版本信息*****************/
	public native int GetSysVersion(int ComHandle, byte MacAddr, byte[] Version);
	
	/***************发送命令接口函数*****************/
	public native int SendCmd(int ComHandle, byte MacAddr, byte[] p_Cmd, int Cmdlen);
	
	/***************获取设备状态信息*****************/
	public native int Query(int ComHandle, byte MacAddr, byte[] StateInfo);
	
	/***************获取设备状态信息(高级查询)*****************/
	public native int SensorQuery(int ComHandle, byte MacAddr, byte[] StateInfo);//和D1801_Query相比，多返回一个字节的状态
	
	/***************获取设备Mac地址*****************/
	public native int AutoTestMac(int ComHandle, byte MacAddr);
	
	/***************获取发卡以及回收卡计数*****************/
	public native int GetCountSum(int ComHandle, byte MacAddr, byte[] szData);
	
	/***************清除发卡计数*****************/
	public native int ClearSendCount(int ComHandle, byte MacAddr);
	
	/***************清除回收卡计数*****************/
	public native int ClearRecycleCount(int ComHandle, byte MacAddr);
	
	
	public void sleep(int time)//延迟睡眠
	{
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public String toHexString2(byte[] b,boolean Phug)
    {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < b.length; ++i)
        {
            buffer.append(toHexString2(b[i]));
            if(Phug==true)
            	buffer.append(" ");
        }
        return buffer.toString();
    }

    public String toHexString2(byte b)
    {
        char[] buffer = new char[2];
        buffer[0] = Character.forDigit((b >>> 4) & 0x0F, 16);
        buffer[1] = Character.forDigit(b & 0x0F, 16);
        return new String(buffer);
    }
    
    public void CopyByte(byte[] dest,byte[] ByteArray,int offset,int length)
    {
    	for(int i=0;i<length;i++)
    		dest[i]=ByteArray[i+offset];
    }
    
    public String ByteToString(byte[] by)
    {
    	String str="";
    	char ch='\0';
    	for(int i=0;by[i]!='\0';i++)
    	{
    		ch=(char)by[i];
    		str+=ch;
    	}
    	return str;
    }
}