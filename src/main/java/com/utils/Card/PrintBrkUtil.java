package com.utils.Card;

import javax.print.*;
import javax.print.attribute.*;
import javax.print.attribute.standard.MediaSize;
import javax.print.attribute.standard.MediaSizeName;
import javax.swing.*;
import java.awt.*;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * java定位打印，把打印内容打到指定的地方。
 *
 * @author lyb
 */

public class PrintBrkUtil implements Printable {
    private int PAGES = 0;
    private String num;
    private String phones;
    private String name;
    private String pass;
    private String hname;
    private String moneys;
    private String reservationTypes;
    private Date endTime;
    private String reservationNum;
    private Date sTime;
    private String HotelName;
    private Date cStartTimes;
    private Date cendTimes;
    private String Floor;
    public void print(String roomNum, String phone, String wifiname, String wifipass,
                      String hotelname,String money,String reservationType,
                      Date sDATE,Date endDate,String reservationNumber,
                      String HotelNames,Date cStartTime,Date cendTime,String HotelFloor) {
        num = roomNum;
        phones = phone;
        name = wifiname;
        pass = wifipass;
        hname = hotelname;
        moneys = money;
        endTime=endDate;
        sTime = sDATE;
        reservationTypes = reservationType;
        reservationNum = reservationNumber;
        HotelName = HotelNames;
        cStartTimes = cStartTime;
        cendTimes = cendTime;
        Floor = HotelFloor;
        if (num != null && num.length() > 0) { // 当打印内容不为空时
            PAGES = 1; // 获取打印总页数
            // 指定打印输出格式
            DocFlavor flavor = DocFlavor.SERVICE_FORMATTED.PRINTABLE;
            // 定位默认的打印服务
            PrintService printService = PrintServiceLookup
                    .lookupDefaultPrintService();
            // 创建打印作业
            DocPrintJob job = printService.createPrintJob();
            // 设置打印属性
            PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
            // 设置纸张大小,也可以新建MediaSize类来自定义大小
            MediaSize ms = new MediaSize(80, 100, Size2DSyntax.MM, MediaSizeName.ISO_A7);
            pras.add(ms.getMediaSizeName().ISO_A7);
            DocAttributeSet das = new HashDocAttributeSet();
            // 指定打印内容
            Doc doc = new SimpleDoc(this, flavor, das);
            // 不显示打印对话框，直接进行打印工作
            try {
                job.print(doc, pras); // 进行每一页的具体打印操作
            } catch (PrintException pe) {
                pe.printStackTrace();
            }
        } else {
            // 如果打印内容为空时，提示用户打印将取消
            JOptionPane.showConfirmDialog(null,
                    "Sorry, Printer Job is Empty, Print Cancelled!",
                    "Empty", JOptionPane.DEFAULT_OPTION,
                    JOptionPane.WARNING_MESSAGE);
        }
    }


    public int print(Graphics gp, PageFormat pf, int page)
            throws PrinterException {
        Graphics2D g2 = (Graphics2D) gp;
        g2.setPaint(Color.black); // 设置打印颜色为黑色
        if (page >= PAGES) // 当打印页号大于需要打印的总页数时，打印工作结束
            return Printable.NO_SUCH_PAGE;
        g2.translate(pf.getImageableX(), pf.getImageableY());// 转换坐标，确定打印边界
        Font font = new Font("宋体", Font.PLAIN, 18);// 创建字体
        Paper p = new Paper();
        g2.setFont(font);
        p.setSize(180, 180);
        p.setImageableArea(10, 10, 100, 141);
        pf.setPaper(p);
        // 打印当前页文本
        g2.drawString("欢迎您入住", 53, 5);
        g2.drawString("西苑饭店" + "!", 57, 25);
        font = new Font("宋体", Font.PLAIN, 14);// 创建字体
        g2.setFont(font);
        g2.drawString("------------------------------------------", 10, 40);
        g2.drawString("------------------------------------------", 10, 42);
        g2.drawString("" + num + "号房间", 60, 55);
        g2.drawString("授权方式: " + reservationTypes + "",15, 75);
        g2.drawString("授权金额: " + moneys + "",15, 90);
        g2.drawString("预 定 号: " + reservationNum + "",15,105);
        g2.drawString("WiFi名称: " + name + "", 15, 120);
        g2.drawString("WiFi密码: " + pass + "", 15, 135);
        g2.drawString("前台总机: " + phones + "", 15, 150);
        font = new Font("宋体", Font.PLAIN, 12);// 创建字体
        g2.setFont(font);
        SimpleDateFormat zc = new SimpleDateFormat("HH:mm:ss");//设置日期格式
        g2.drawString("早餐时间: " + zc.format(cStartTimes) + "-"+ zc.format(cendTimes), 15, 165);
        font = new Font("宋体", Font.PLAIN, 14);// 创建字体
        g2.setFont(font);
        g2.drawString("早餐地址: " + HotelName +  Floor + "层", 15, 180);

        g2.drawString("------------------------------------------", 10, 192);
        g2.drawString("------------------------------------------", 10, 194);
        font = new Font("宋体", Font.PLAIN, 12);// 创建字体
        g2.setFont(font);
        g2.drawString("希望您度过美好的一天!", 15, 210);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        g2.drawString("入住时间:" + df.format(sTime), 15, 225);
        g2.drawString("离店时间:" + df.format(endTime), 15, 240);
        font = new Font("宋体", Font.PLAIN, 1);// 创建字体
        g2.setFont(font);
        g2.drawString("1", 15, 245);
        return Printable.PAGE_EXISTS; // 存在打印页时，继续打印工作
    }

}
