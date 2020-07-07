package TTCEPackage;

public class CardReadPwd {
	
	static byte[] RecvBuf=new byte[300];
	/**
	 * 读卡密码
	 */
	static{
		RecvBuf[0] = (byte)0xFF;
		RecvBuf[1] = (byte)0xFF;
		RecvBuf[2] = (byte)0xFF;
		RecvBuf[3] = (byte)0xFF;
		RecvBuf[4] = (byte)0xFF;
		RecvBuf[5] = (byte)0xFF;
	}
}
