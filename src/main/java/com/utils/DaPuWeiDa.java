package com.utils;




import com.dp.printlibs.ds.NormalPrint;
import com.dp.printlibs.util.DataUtils;
import com.yang.serialport.manager.SerialPortManager;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import org.apache.commons.codec.DecoderException;


import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class DaPuWeiDa {

    public static void printCheckIn(int com,String roomNum,String leave,String wifiMiMa,String qiantaiPhone,String endTime) throws PortInUseException {
        SerialPort serialPort=SerialPortManager.openPort("COM"+com,115200);
        System.out.println("打开串口成功，"+serialPort.getName());
        String line1s="欢迎您入住";
        byte[] line1b = NormalPrint.printtext(line1s, 2, 1, 20);
        String line2s="北京朝林松源酒店";
        byte[] line2b = NormalPrint.printtext(line2s, 2, 1, 20);
        String line3s="---------------------------";
        byte[] line3b = NormalPrint.printtext(line3s, 2, 1, 20);
        String line4s="---------------------------";
        byte[] line4b = NormalPrint.printtext(line4s, 2, 1, 20);
        String line5s=roomNum+"号房间";
        byte[] line5b = NormalPrint.printtext(line5s, 2, 2, 20);
        String line6s="  WIFI用户名："+roomNum;
        byte[] line6b = NormalPrint.printtext(line6s, 1, 1, 20);
        String line7s="  WiFi密码："+wifiMiMa;
        byte[] line7b = NormalPrint.printtext(line7s, 1, 1, 20);
        String line8s="  前台总机："+qiantaiPhone;
        byte[] line8b = NormalPrint.printtext(line8s, 1, 1, 20);
        String line9s="  预定离店时间："+endTime+" 12:00";
        byte[] line9b = NormalPrint.printtext(line9s, 1, 1, 20);
        String line10s="希望您度过美好的一天!";
        byte[] line10b = NormalPrint.printtext(line10s, 2, 1, 10);
        String line11s="入住时间:"+new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
        byte[] line11b = NormalPrint.printtext(line11s, 2, 1, 10);
        byte[] guding3=new byte[]{(byte)0x0d,(byte)0x0a,
                (byte)0x0d,(byte)0x0a};
        byte[] buffer = DataUtils.byteArraysToBytes(new byte[][]{line1b, line2b, line3b, line4b, line5b , line6b,line7b,line8b,line9b,line11b,line10b,guding3});
        SerialPortManager.sendToPort(serialPort,buffer);//打印
        SerialPortManager.closePort(serialPort);
        System.out.println("关闭串口成功");
    }


    public static void printCheckOut(int com,String roomNum,String leave,String wifiMiMa,String qiantaiPhone) throws PortInUseException {
        SerialPort serialPort=SerialPortManager.openPort("COM"+com,115200);
        System.out.println("打开串口成功，"+serialPort.getName());
        String line1s="欢迎您入住";
        byte[] line1b = NormalPrint.printtext(line1s, 2, 1, 20);
        String line2s="北京朝林松源酒店";
        byte[] line2b = NormalPrint.printtext(line2s, 2, 1, 20);
        String line3s="---------------------------";
        byte[] line3b = NormalPrint.printtext(line3s, 2, 1, 20);
        String line4s="---------------------------";
        byte[] line4b = NormalPrint.printtext(line4s, 2, 1, 20);
        String line5s=roomNum+"号房间";
        byte[] line5b = NormalPrint.printtext(line5s, 2, 2, 20);
        String line6s="  WIFI用户名："+roomNum;
        byte[] line6b = NormalPrint.printtext(line6s, 1, 1, 20);
        String line7s="  WiFi密码："+wifiMiMa;
        byte[] line7b = NormalPrint.printtext(line7s, 1, 1, 20);
        String line8s="  前台总机："+qiantaiPhone;
        byte[] line8b = NormalPrint.printtext(line8s, 1, 1, 20);
        String line9s="  规定离店时间：12:00";
        byte[] line9b = NormalPrint.printtext(line9s, 1, 1, 20);
        String line10s="希望您度过美好的一天!";
        byte[] line10b = NormalPrint.printtext(line10s, 2, 1, 10);
        String line11s="  真实离店时间:"+new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
        byte[] line11b = NormalPrint.printtext(line11s, 2, 1, 10);
        byte[] guding3=new byte[]{(byte)0x0d,(byte)0x0a,
                (byte)0x0d,(byte)0x0a};
        byte[] buffer = DataUtils.byteArraysToBytes(new byte[][]{line1b, line2b, line3b, line4b, line5b , line6b,line7b,line8b,line9b,line11b,line10b,guding3});
        SerialPortManager.sendToPort(serialPort,buffer);//打印
        SerialPortManager.closePort(serialPort);
        System.out.println("关闭串口成功");
    }


    public static void printImgVoice(String str,int com,String roomNum,String wifiMiMa,String qiantaiPhone,String leave,String orderNo) throws PortInUseException, UnsupportedEncodingException, DecoderException {
        System.out.println("str:"+str);
        SerialPort serialPort=SerialPortManager.openPort("COM"+com,115200);
        System.out.println("打开串口成功，"+serialPort.getName());
        String line1s="期待您再次光临";
        byte[] line1b = NormalPrint.printtext(line1s, 2, 1, 20);
        String line2s="北京朝林松源酒店";
        byte[] line2b = NormalPrint.printtext(line2s, 2, 1, 20);
        String line3s="---------------------------";
        byte[] line3b = NormalPrint.printtext(line3s, 2, 1, 20);
        String line4s="---------------------------";
        byte[] line4b = NormalPrint.printtext(line4s, 2, 1, 20);
        String line5s=roomNum+"号房间";
        byte[] line5b = NormalPrint.printtext(line5s, 2, 2, 20);
        String line6s="  WIFI用户名："+roomNum;
        byte[] line6b = NormalPrint.printtext(line6s, 1, 1, 20);
        String line7s="  WiFi密码："+wifiMiMa;
        byte[] line7b = NormalPrint.printtext(line7s, 1, 1, 20);
        String line8s="  前台总机："+qiantaiPhone;
        byte[] line8b = NormalPrint.printtext(line8s, 1, 1, 20);
        String line19s="  规定离店时间：12:00";
        byte[] line19b = NormalPrint.printtext(line19s, 1, 1, 20);
        String line20s="  真实离店时间:"+new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
        byte[] line20b = NormalPrint.printtext(line20s, 2, 1, 10);
        byte[] buffer = DataUtils.byteArraysToBytes(new byte[][]{line1b, line2b, line3b, line4b, line5b , line6b,line7b,line8b,line19b,line20b});
        SerialPortManager.sendToPort(serialPort,buffer);//打印

        byte[] header=new byte[]{(byte)0x1b,(byte)0x40,(byte)0x1b,(byte)0x61,(byte)0x01,
                (byte)0x1d,(byte)0x28,(byte)0x6b,(byte)0x03,(byte)0x00, (byte)0x31,(byte)0x43,(byte)0x05,
                (byte)0x1d,(byte)0x28,(byte)0x6b,(byte)0x03,(byte)0x00,(byte)0x31,(byte)0x45,(byte)0x30,//可固定
                (byte)0x1d,(byte)0x28,(byte)0x6b};
        byte[] erweima=null;
        try {
            erweima = str.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("btyeToStr："+new String(erweima));
        byte[] changdu=int2byte(erweima.length+3);
        byte[] guding1=new byte[]{(byte)0x31,(byte)0x50,(byte)0x30};
        byte[] guding2=new byte[]{
                (byte)0x1d,(byte)0x28,(byte)0x6b,(byte)0x03,(byte)0x00,(byte)0x31,(byte)0x52,(byte)0x30,
                (byte)0x1d,(byte)0x28,(byte)0x6b,(byte)0x03,(byte)0x00,(byte)0x31,(byte)0x51,(byte)0x30,
                (byte)0x1b,(byte)0x40,(byte)0x1d,(byte)0x21,(byte)0x00,
                (byte)0x1b,(byte)0x61,(byte)0x01};
        byte[] textneirong=new byte[]{(byte)0xC9,(byte)0xA8,(byte)0xD2,(byte)0xBB,(byte)0xC9,(byte)0xA8,
                (byte)0xBF,(byte)0xAA,(byte)0xC6,(byte)0xB1};
        byte[] guding3=new byte[]{(byte)0x0d,(byte)0x0a,
                (byte)0x0d,(byte)0x0a};
        buffer = DataUtils.byteArraysToBytes(new byte[][]{header, changdu, guding1, erweima, guding2,textneirong,guding3});
        SerialPortManager.sendToPort(serialPort,buffer);//打印二维码
        String line9s="目前仅支持增值税普通发票,如需";
        byte[] line9b = NormalPrint.printtext(line9s, 2, 1, 20);
        String line10s="开专票可以持小票去前台办理!";
        byte[] line10b = NormalPrint.printtext(line10s, 2, 1, 20);
        String line11s="可以用微信或qq扫码开电子发票!";
        byte[] line11b = NormalPrint.printtext(line11s, 2, 1, 20);
        String line12s="电子发票二维码有效期为30天!";
        byte[] line12b = NormalPrint.printtext(line12s, 2, 1, 20);
        String line13s="请您妥善保管此小票!";
        byte[] line13b = NormalPrint.printtext(line13s, 2, 1, 10);
        String line14s="发票订单号："+orderNo;
        byte[] line14b = NormalPrint.printtext(line14s, 2, 1, 10);
        String line15s="希望您度过美好的一天!";
        byte[] line15b = NormalPrint.printtext(line15s, 2, 1, 10);
        buffer = DataUtils.byteArraysToBytes(new byte[][]{line9b, line10b, line11b, line12b, line13b,line14b,line15b,guding3});
        SerialPortManager.sendToPort(serialPort,buffer);//打印
        SerialPortManager.closePort(serialPort);
        System.out.println("关闭串口成功");
    }

    public static byte[] int2byte(int res) {
        byte[] targets = new byte[2];
        targets[0] = (byte) (res & 0xff);// 最低位
        targets[1] = (byte) ((res >> 8) & 0xff);// 次低位
        return targets;
    }

}
