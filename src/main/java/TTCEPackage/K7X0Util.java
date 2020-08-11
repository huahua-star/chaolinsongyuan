package TTCEPackage;

public class K7X0Util {
    static K7X0 Reader=new K7X0();
    public static int ComHandle;
    static byte MacAddr = 0x00;
    static byte[] RecvBuf=new byte[300];
    static int[] length=new int[2];
    static boolean isOpen = false;

    public static void writeAndTake(Integer comHandle){
        //发送到写卡位置
        sendToRead(comHandle);
        int cout = 0;//循环20次
        int nRet;
        //寻卡
        while(cout <= 20){
            cout++;
            nRet = Reader.S50DetectCard(ComHandle,MacAddr);
            if(nRet == 0){
                System.out.println("寻卡成功");
                break;
            }else{
                System.out.println("正在尝试第" + cout + "次寻卡");
            }
            Reader.sleep(1000);
        }
        if(cout>=20){
            System.out.println("寻卡失败，请联系管理员");
            return ;
        }

        nRet = Reader.S50LoadSecKey(ComHandle, MacAddr,(byte)0x04, (byte)0x30, CardReadPwd.RecvBuf);
        if(nRet != 0){
            System.out.println("S50验证扇区:0x04，秘钥类型:KeyA，秘钥：FFFFFFFFFFFF。失败，请确认扇区的秘钥是否正确");
            return ;
        }

        String ids = "8f4a099db5aa46058363b0dad8682700";
        char[] chars = ids.toCharArray();
        byte[] writer = new byte[16];
        for(int i = 0; i < 16; i++){
            writer[i] = (byte)chars[i];
        }

        nRet = Reader.S50WriteBlock(ComHandle,MacAddr,(byte)0x04,(byte)0x00, writer);
        if(nRet != 0){
            System.out.println("S50写入扇区:0x04，块：0x00 数据失败，请确认卡片是否被移走");
            return ;
        }

        System.out.println("S50写入扇区:0x04，块：0x00 数据为：" + Reader.toHexString2(writer, true));
        System.out.println("-------"+Reader.ByteToString(writer));
        //发送到取卡位置
        sendCardToTake(comHandle);
    }

    public static void regainAndCheck(Integer comHandle){
        //先回收
        regainCard(comHandle);
        Reader.sleep(300);
        //发送到读卡位置
        sendToRead(comHandle);
        Reader.sleep(300);

        int cout = 0;//循环20次
        int nRet;
        //寻卡
        while(cout <= 20){
            cout++;
            nRet = Reader.S50DetectCard(ComHandle,MacAddr);
            if(nRet == 0){
                System.out.println("寻卡成功");
                break;
            }else{
                System.out.println("正在尝试第" + cout + "次寻卡");
            }
            Reader.sleep(1000);
        }
        if(cout>=20){
            System.out.println("寻卡失败，请联系管理员");
            return ;
        }
        nRet = Reader.S50LoadSecKey(ComHandle, MacAddr,(byte)0x04, (byte)0x30, CardReadPwd.RecvBuf);

        if(nRet != 0){
            System.out.println("S50验证扇区:0x04，秘钥类型:KeyA，秘钥：FFFFFFFFFFFF。失败，请确认扇区的秘钥是否正确");
            return ;
        }

        byte[] Data=new byte[16];
        nRet = Reader.S50ReadBlock(ComHandle,MacAddr,(byte)0x04,(byte)0X00,Data);
        if(nRet != 0){
            System.out.println("S50第一次读取扇区:0x04，块：0x00 数据失败，请确认卡片是否被移走");
            return ;
        }
        System.out.println("S50第一次读取扇区:0x04，块：0x00 数据为：" + Reader.toHexString2(Data, true));
        System.out.println("-------"+Reader.ByteToString(Data));
        //回收卡
        regain();
    }

    /**
     * 写卡
     * @param comHandle
     */
    public static void s50Write(Integer comHandle){
        int nRet = 0;

        sendToRead(9);

        Reader.sleep(300);
        int cout = 0;//循环20次
        while(cout <= 20){
            cout++;
            nRet = Reader.S50DetectCard(ComHandle,MacAddr);
            if(nRet == 0){
                System.out.println("寻卡成功");
                break;
            }else{
                System.out.println("正在尝试第" + cout + "次寻卡");
            }
            Reader.sleep(1000);
        }
        if(cout>=20){
            System.out.println("寻卡失败，请联系管理员");
            return ;
        }

        nRet = Reader.S50LoadSecKey(ComHandle, MacAddr,(byte)0x04, (byte)0x30, CardReadPwd.RecvBuf);

        if(nRet != 0){
            System.out.println("S50验证扇区:0x04，秘钥类型:KeyA，秘钥：FFFFFFFFFFFF。失败，请确认扇区的秘钥是否正确");
            return ;
        }

        byte[] Data=new byte[16];
        nRet = Reader.S50ReadBlock(ComHandle,MacAddr,(byte)0x04,(byte)0X00,Data);
        if(nRet != 0){
            System.out.println("S50第一次读取扇区:0x04，块：0x00 数据失败，请确认卡片是否被移走");
            return ;
        }
        System.out.println("S50第一次读取扇区:0x04，块：0x00 数据为：" + Reader.toHexString2(Data, true));

        String ids = "8f4a099db5aa46058363b0dad8682700";
        char[] chars = ids.toCharArray();
        byte[] writer = new byte[16];
        for(int i = 0; i < 16; i++){
            writer[i] = (byte)chars[i];
        }

        nRet = Reader.S50WriteBlock(ComHandle,MacAddr,(byte)0x04,(byte)0x00, writer);
        if(nRet != 0){
            System.out.println("S50写入扇区:0x04，块：0x00 数据失败，请确认卡片是否被移走");
            return ;
        }

        System.out.println("S50写入扇区:0x04，块：0x00 数据为：" + Reader.toHexString2(writer, true));
        System.out.println("-------"+Reader.ByteToString(writer));

        nRet = Reader.S50ReadBlock(ComHandle,MacAddr, (byte)0x04, (byte)0x00, Data);

        if(nRet != 0){
            System.out.println("S50第二次读取扇区:0x04，块：0x00 数据失败，请确认卡片是否被移走");
            return ;
        }
        System.out.println("S50第二次读取扇区:0x04，块：0x00 数据为：" + Reader.toHexString2(Data, true));
        reset();
        return ;
    }

    /**
     * 发送到读卡位置
     * @param comHandle
     */
    public static void sendToRead(Integer comHandle){
        Boolean flag = true;
        if(!open(comHandle)){
            System.out.println("发卡器串口打开失败+=========>com:"+comHandle);
            return;
        }
        while (flag){
            RecvBuf[0] = 0x46;//F
            RecvBuf[1] = 0x43;//C
            RecvBuf[2] = 0x37;//7
            Reader.SendCmd(ComHandle, MacAddr,RecvBuf, 3);
            System.out.println("检测读卡位置");
            flag = !check(3,0x33);
            Reader.sleep(1000);
        }
        System.out.println("发送到读卡位置成功");
    }


    /**
     * 发送到读卡位置
     * @param comHandle
     */
    public static int sendToReadToReturn(Integer comHandle){
        Boolean flag = true;
        if(!open(comHandle)){
            System.out.println("发卡器串口打开失败+=========>com:"+comHandle);
            return 1;
        }
        int k=0;
        while (flag){
            RecvBuf[0] = 0x46;//F
            RecvBuf[1] = 0x43;//C
            RecvBuf[2] = 0x37;//7
            Reader.SendCmd(ComHandle, MacAddr,RecvBuf, 3);
            System.out.println("检测读卡位置");
            flag = !check(3,0x33);
            System.out.println("while:"+flag);
            Reader.sleep(1000);
            k++;
            if (k>=10){
                break;
            }
        }
        System.out.println("跳出循环");
        flag = check(3,0x33);
        System.out.println("flag:"+flag);
        if (flag){
            System.out.println("发送到读卡位置成功");
            return 0;
        }else{
            return 2;
        }
    }

    /**
     * 发卡到取卡位置
     * @return
     */
    public static void sendCardToTake(Integer comHandle){
        Boolean flag = true;
        if(!open(comHandle)){
            System.out.println("发卡器串口打开失败+=========>com:"+comHandle);
            return;
        }

        send();
        /*while (flag){
            System.out.println("检测发卡位置");
            flag = !check(3,0x31);
            Reader.sleep(1000);
        }*/
        System.out.println("发送到取卡位置成功");
    }

    /**
     * 收回卡
     */
    public static boolean regainCard(Integer comHandle){
        if(!open(comHandle)){
            System.out.println("发卡器串口打开失败+=========>com:"+comHandle);
            return false;
        }
        Boolean flag = true;
        long t1 = System.currentTimeMillis();
        while (flag){
            long t2 = System.currentTimeMillis();
            //十秒跳出失败
            if(t2-t1 > 10*1000){
                return false;
            }
            flag = !check(3,0x31);
            System.out.println("检测收卡位置");
            regain();
        }
        System.out.println("回收卡片成功");
        return true;
    }

    /**
     * 自动收回卡
     */
    public static boolean regainCardZiDong(Integer comHandle){
        if(!open(comHandle)){
            System.out.println("发卡器串口打开失败+=========>com:"+comHandle);
            return false;
        }
        Boolean flag = false;
        long t1 = System.currentTimeMillis();
        while (!flag){
            long t2 = System.currentTimeMillis();
            //十秒跳出失败
            if(t2-t1 > 30*1000){
                return true;
            }
            regain();
        }
        System.out.println("回收卡片成功");
        return true;
    }

    /**
     * 收卡到发卡箱
     */
    public static void regain(){
        /*RecvBuf[0] = 0x43;//C
        RecvBuf[1] = 0x50;//P*/
        RecvBuf[0] = 0x44;//D
        RecvBuf[1] = 0x42;//B
        //收卡
        Reader.SendCmd(ComHandle, MacAddr,RecvBuf, 2);
    }

    /**
     * 发卡到取卡位置--命令DC
     */
    public static void send(){
        RecvBuf[0] = 0x44;//D
        RecvBuf[1] = 0x43;//C
        Reader.SendCmd(ComHandle, MacAddr,RecvBuf, 2);//发卡到取卡位置--DC
    }

    /**
     * 复位--命令RS
     */
    public static void reset(){
        RecvBuf[0] = 0x52;//R
        RecvBuf[1] = 0x53;//S
        Reader.SendCmd(ComHandle, MacAddr, RecvBuf, 2);//复位--RS
    }

    /**
     * 检测发卡机状态
     *
     * @param address 0  机器状态  1 动作状态  2 卡箱状态  3 卡片状态
     * @param chose  十六位进制状态码
     * @return 是否在指定状态
     */
    public static boolean check(int address,int chose){
        Boolean flag = false;
        Reader.SensorQuery(ComHandle, MacAddr, RecvBuf);
        if(RecvBuf[address]==chose){
            flag = true;
        }
        return flag;
    }

    /**
     * 打开串口
     * @param comHandle
     * @return
     */
    public static boolean open(Integer comHandle) {
        if(isOpen){
            return true;
        }
        int i=0, nRet = 0;
        System.out.println("com-----------------------------:"+comHandle);
        ComHandle = Reader.CommOpen("COM"+comHandle);
        if(ComHandle == 0){
            System.out.println("发卡器串口打开失败+=========>com:"+comHandle);
            return false;
        }
        System.out.println("发卡器串口打开成功+=========>com:"+comHandle);

        for(i = 0; i < 16; i++){
            nRet = Reader.AutoTestMac(ComHandle, (byte)i);
            if(nRet == 0){
                MacAddr = (byte)i;
                break;
            }
        }
        if(nRet != 0){
            System.out.println("未找到K7X0设备，请检查串口和电源线是否连接正常或者端口号是否选择正确");
            colse(3);
            return false;
        }else{
            System.out.println("设备连接成功，设备地址未：" + MacAddr);
        }


        byte[] Version=new byte[20];
        nRet = Reader.GetSysVersion(ComHandle, Version);
        if(nRet == 0){
            System.out.println("K7X0_Dll Version: " + Reader.ByteToString(Version));
        } else {
            System.out.println("Failed");
        }
        isOpen = true;
        return true;

    }

    /**
     * 关闭串口
     * @param comHandle
     */
    public static void colse(int comHandle) {
        int commClose = Reader.CommClose(comHandle);
        if(commClose != 0){
            System.out.println("遇到错误发卡机串口 --》关闭失败");
        }else {
            System.out.println("关闭发卡机串口 --》关闭成功");
        }
    }
}
