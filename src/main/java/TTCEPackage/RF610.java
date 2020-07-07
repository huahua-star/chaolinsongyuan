package TTCEPackage;

/*************************************************************************************
注：
1.调用RF610_Dll.dll动态库需要注意一个地方，包名一定要是TTCEPackage，调用和声明的类名可以是任何类名。
2.关于接口调用说明，请参考windows标准接口调用接口说明，参数都是一致。
*************************************************************************************/

public class RF610 {

		//此处为导入数据库
		static {
			
			//64bit 用的dll
			//System.loadLibrary("RF610_Dll");
			//32bit 用的dll
			System.loadLibrary("RF610_DLL");
		}
		
	
	byte[] ReceBuf=new byte[500];
	public int Buflength=0;
	public int CardPosition=0;
	public int CardType=0;
	public int RCH=0;
	/*串口操作函数*/
	public native int CommOpen(String StrPort);
	public native int CommOpenWithBaud(String StrPort, int Baudate);
	public native int CommClose(int ComHandle);
	public native int GetSysVersion(int ComHandle, byte[] Version);
	
	public native int Reset(int ComHandle);
	public native int GetHardVersion(int ComHandle, byte[] Version);
	
	/*S50卡操作函数*/
	public native int S50DetectCard(int ComHandle);
	public native int S50GetCardID(int ComHandle, byte[] Data);
	public native int S50LoadSecKey(int ComHandle, byte _BlockAddr, byte _KEYType, byte[] _KEY);
	public native int S50ReadBlock(int ComHandle, byte _Block, byte[] Data);
	public native int S50WriteBlock(int ComHandle, byte _Block, byte[] _BlockData);
	public native int S50InitValue(int ComHandle, byte _Block, byte[] _BlockData);
	public native int S50Increment(int ComHandle, byte _Block, byte[] _BlockData);
	public native int S50Decrement(int ComHandle, byte _Block, byte[] _BlockData);
	public native int S50Halt(int ComHandle);
	
	/*S70卡操作函数*/
	public native int S70DetectCard(int ComHandle);
	public native int S70GetCardID(int ComHandle, byte[] Data);
	public native int S70LoadSecKey(int ComHandle, byte _BlockAddr, byte _KEYType, byte[] _KEY);
	public native int S70ReadBlock(int ComHandle, byte _Block, byte[] Data);
	public native int S70WriteBlock(int ComHandle, byte _Block, byte[] _BlockData);
	public native int S70InitValue(int ComHandle, byte _Block, byte[] _BlockData);
	public native int S70Increment(int ComHandle, byte _Block, byte[] _BlockData);
	public native int S70Decrement(int ComHandle, byte _Block, byte[] _BlockData);
	public native int S70Halt(int ComHandle);
	
	/*UL卡操作函数*/
	public native int ULDetectCard(int ComHandle);
	public native int ULGetCardID(int ComHandle, byte[] Data);
	public native int ULReadBlock(int ComHandle, byte _Block, byte[] Data);
	public native int ULWriteBlock(int ComHandle, byte _Block, byte[] _BlockData);
	public native int ULHalt(int ComHandle);
	
	/*15693卡操作函数*/
	public native int M15693GetUid(int ComHandle);
	public native int M15693ReadData(int ComHandle, boolean bUid, byte[] uid, byte _BlockAddr, byte BlockLen, byte[] BlockData, byte[] ReadBlocklength);
	public native int M15693WriteData(int ComHandle, boolean bUid, byte[] uid, byte _BlockAddr, byte BlockLen, byte[] _BlockData, byte[] WriteBlocklength);
	public native int M15693ChooseCard(int ComHandle, boolean bUid, byte[] uid);
	public native int M15693GetMessage(int ComHandle, boolean bUid, byte[] uid, byte[] message);
	public native int M15693ReadSafeBit(int ComHandle, boolean bUid, byte[] uid, byte _BlockAddr, byte BlockLen, byte[] Readlength, byte[] Data);
	public native int M15693WriteDSFID(int ComHandle, boolean bUid, byte[] uid, byte writebit);
	public native int M15693WriteAFI(int ComHandle, boolean bUid, byte[] uid, byte writebit);
	public native int M15693LockBlock(int ComHandle, boolean bUid, byte[] uid, byte LockAddress);
	public native int M15693LockAFI(int ComHandle, boolean bUid, byte[] uid);
	public native int M15693LockDSFID(int ComHandle, boolean bUid, byte[] uid);
	
	/*非接TypeA卡操作函数*/
	public native int CPUTypeACardPowerOn(int ComHandle, byte[] ATRData);
	public native int CPUTypeAAPDU(int ComHandle, byte SCH, byte _dataLen, byte[] _APDUData, byte[] RCH, byte[] _exData, int[] _exdataLen);
	
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

