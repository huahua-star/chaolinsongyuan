package TTCEPackage;

public class K7X0 {

	static {
		//System.loadLibrary("K7X0_Dll");
		//需要将K7X0_DLL.dll文件放到项目的同级目录
		//System.load(System.getProperty("user.dir")+"\\K7X0_Dll.dll");
		System.load("C:\\Users\\Administrator\\Desktop\\西软\\K7X0_Dll.dll");
	}

	public static void main(String[] args) {
		System.load(System.getProperty("user.dir")+"\\K7X0_Dll.dll");
	}

	byte[] ReceBuf=new byte[500];
	public int Buflength=0;
	public int CardPosition=0;
	public int CardType=0;
	public int RCH=0;
	
	/***************串口操作函数*******************/
	public native int CommOpen(String strPort);
	public native int CommOpenWithBaud(String strPort, int Baudate);
	public native int CommClose(int ComHandle);
	
	/*S50卡操作函数*/
	public native int S50DetectCard(int ComHandle, byte MacAddr);
	public native int S50GetCardID(int ComHandle, byte MacAddr, byte[] Data);
	public native int S50LoadSecKey(int ComHandle, byte MacAddr, byte _SectorAddr, byte _KEYType, byte[] _KEY);
	public native int S50ReadBlock(int ComHandle, byte MacAddr, byte _SectorAddr, byte _Block, byte[] Data);
	public native int S50WriteBlock(int ComHandle, byte MacAddr, byte _SectorAddr, byte _Block, byte[] _BlockData);
	public native int S50InitValue(int ComHandle, byte MacAddr, byte _SectorAddr, byte _Block, byte[] _BlockData);
	public native int S50Increment(int ComHandle, byte MacAddr, byte _SectorAddr, byte _Block, byte[] _BlockData);
	public native int S50Decrement(int ComHandle, byte MacAddr, byte _SectorAddr, byte _Block, byte[] _BlockData);
	public native int S50Halt(int ComHandle, byte MacAddr);
	
	/*S70卡操作函数*/
	public native int S70DetectCard(int ComHandle, byte MacAddr);
	public native int S70GetCardID(int ComHandle, byte MacAddr, byte[] Data);
	public native int S70LoadSecKey(int ComHandle, byte MacAddr, byte _SectorAddr, byte _KEYType, byte[] _KEY);
	public native int S70ReadBlock(int ComHandle, byte MacAddr, byte _SectorAddr, byte _Block, byte[] Data);
	public native int S70WriteBlock(int ComHandle, byte MacAddr, byte _SectorAddr, byte _Block, byte[] _BlockData);
	public native int S70InitValue(int ComHandle, byte MacAddr, byte _SectorAddr, byte _Block, byte[] _BlockData);
	public native int S70Increment(int ComHandle, byte MacAddr, byte _SectorAddr, byte _Block, byte[] _BlockData);
	public native int S70Decrement(int ComHandle, byte MacAddr, byte _SectorAddr, byte _Block, byte[] _BlockData);
	public native int S70Halt(int ComHandle, byte MacAddr);
	
	/*UL卡操作函数*/
	public native int ULDetectCard(int ComHandle, byte MacAddr);
	public native int ULGetCardID(int ComHandle, byte MacAddr, byte[] Data);
	public native int ULReadBlock(int ComHandle, byte MacAddr, byte _Block, byte[] Data);
	public native int ULWriteBlock(int ComHandle, byte MacAddr, byte _Block, byte[] _BlockData);
	public native int ULHalt(int ComHandle, byte MacAddr);
	
	/*非接TypeA卡操作函数*/
	public native int CPUTypeACardPowerOn(int ComHandle, byte MacAddr, byte[] ATRData);
	public native int CPUTypeAAPDU(int ComHandle, byte MacAddr, byte SCH, byte _dataLen, byte[] _APDUData, byte[] RCH, byte[] _exData, int[] _exdataLen);
	
	/*15693卡操作函数*/
	public native int M15693GetUid(int ComHandle, byte MacAddr);
	public native int M15693ReadData(int ComHandle, byte MacAddr, boolean bUid, byte[] uid, byte _BlockAddr, byte BlockLen, byte[] ReadBlocklength, byte[] BlockData);
	public native int M15693WriteData(int ComHandle, byte MacAddr, boolean bUid, byte[] uid, byte _BlockAddr, byte BlockLen, byte[] WriteBlocklength, byte[] _BlockData);
	public native int M15693ChooseCard(int ComHandle, byte MacAddr, boolean bUid, byte[] uid);
	public native int M15693GetMessage(int ComHandle, byte MacAddr, boolean bUid, byte[] uid, byte[] message);
	public native int M15693ReadSafeBit(int ComHandle, byte MacAddr, boolean bUid, byte[] uid, byte _BlockAddr, byte BlockLen, byte[] Readlength, byte[] Data);
	public native int M15693WriteDSFID(int ComHandle, byte MacAddr, boolean bUid, byte[] uid, byte writebit);
	public native int M15693WriteAFI(int ComHandle, byte MacAddr, boolean bUid, byte[] uid, byte writebit);
	public native int M15693LockBlock(int ComHandle, byte MacAddr, boolean bUid, byte[] uid, byte LockAddress);
	public native int M15693LockAFI(int ComHandle, byte MacAddr, boolean bUid, byte[] uid);
	public native int M15693LockDSFID(int ComHandle, byte MacAddr, boolean bUid, byte[] uid);
	
	/***************获取动态库版本信息*****************/
	public native int GetSysVersion(int ComHandle, byte[] Version);
	
	/***************获取设备固件版本信息*****************/
	public native int GetVersion(int ComHandle, byte MacAddr, byte[] Version);
	
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
    	for(int i=0;i<by.length;i++)
    	{
    		ch=(char)by[i];
    		str+=ch;
    	}
    	return str;
    }
}
