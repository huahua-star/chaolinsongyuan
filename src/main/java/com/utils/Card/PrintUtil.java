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

/**
 * java定位打印，把打印内容打到指定的地方。
 *
 * @author lyb
 */

public class PrintUtil implements Printable {
    private int PAGES = 0;
    private String num;
    private String phones;
    private String name;
    private String pass;
    private String hname;
    private String city;//市
    private String area;//区
    private String road;//路
    private String community;//附近
    private String number;//几零几
    private String moneys;
    private String reservationTypes;
    private String endTime;
    private String reservationNum;
    private String sTime;

    public void print(String roomNum, String phone, String wifiname, String wifipass,
                      String hotelname, String cityaddress, String areaaddress, String roadaddress,
                      String communityaddress, String numberaddress,String money,String reservationType,String sDATE,String endDate,String reservationNumber) {
        num = roomNum;
        phones = phone;
        name = wifiname;
        pass = wifipass;
        hname = hotelname;
        city = cityaddress;
        area = areaaddress;
        road = roadaddress;
        community = communityaddress;
        number = numberaddress;
        moneys = money;
        endTime=endDate;
        sTime = sDATE;
        reservationTypes = reservationType;
        reservationNum = reservationNumber;
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

    public int print(Graphics gp, PageFormat pf, int page) {
        try{
            Graphics2D g2 = (Graphics2D) gp;
            g2.setPaint(Color.black); // 设置打印颜色为黑色
            if (page >= PAGES) // 当打印页号大于需要打印的总页数时，打印工作结束
                return Printable.NO_SUCH_PAGE;
            g2.translate(pf.getImageableX(), pf.getImageableY());// 转换坐标，确定打印边界
            Font font = new Font("宋体", Font.PLAIN, 14);// 创建字体
            Paper p = new Paper();
            g2.setFont(font);
            p.setSize(180, 180);
            p.setImageableArea(10, 10, 100, 141);
            pf.setPaper(p);
            // 打印当前页文本
            g2.drawString("欢迎您入住", 53, 5);
            g2.drawString("北京宝之谷国际会议中心", 20, 25);
            g2.drawString("------------------------------------------", 10, 40);
            g2.drawString("------------------------------------------", 10, 42);
            Font roomfont= new Font("宋体", Font.PLAIN, 22);//房间字体
            g2.setFont(roomfont);
            g2.drawString("" + num + "号房间", 35, 55);
            font = new Font("宋体", Font.PLAIN, 14);// 创建字体
            g2.setFont(font);
            System.out.println("reservationTypes:"+reservationTypes);
            if (reservationTypes!=null||!"".equals(reservationTypes)){
                g2.drawString("授权方式: " + reservationTypes + "",15, 75);
                g2.drawString("授权金额: " + moneys + "",15, 90);
            }
            g2.drawString("预 定 号: " + reservationNum + "",15,105);
            g2.drawString("WiFi名称: " + name + "", 15, 120);
            g2.drawString("WiFi密码: " + pass + "", 15, 135);
            g2.drawString("前台总机: " + phones + "", 15, 150);
            g2.drawString("------------------------------------------", 10, 162);
            g2.drawString("------------------------------------------", 10, 164);
            font = new Font("宋体", Font.PLAIN, 12);// 创建字体
            g2.setFont(font);
            g2.drawString("希望您度过美好的一天!", 15, 180);
            g2.drawString("入住时间:" + sTime, 15, 195);
            g2.drawString("离店时间:" + endTime, 15, 210);
            font = new Font("宋体", Font.PLAIN, 1);// 创建字体
            g2.setFont(font);
            g2.drawString("1", 15, 220);
            return Printable.PAGE_EXISTS; // 存在打印页时，继续打印工作
        }catch (Exception e){
            e.printStackTrace();
            return Printable.PAGE_EXISTS;
        }
    }

}
