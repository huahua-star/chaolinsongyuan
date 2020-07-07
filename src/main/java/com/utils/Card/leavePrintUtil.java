package com.utils.Card;

import javax.print.*;
import javax.print.attribute.*;
import javax.print.attribute.standard.MediaSize;
import javax.print.attribute.standard.MediaSizeName;
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

public class leavePrintUtil implements Printable {
    private int PAGES = 0;
    private String num;
    private String hname;
    private Date ktime;
    private Date jtime;
    private Date liTime;
    private String img;
    private String orderNos;

    public void print(String roomNum, String imgurl, Date kstime, Date jstime, Date leaveTime,String orderNo) {
        num = roomNum;
        img = imgurl;
        ktime = kstime;
        jtime = jstime;
        liTime = leaveTime;
        orderNos = orderNo;
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
            pras.add(ms.getMediaSizeName().ISO_A6);
            DocAttributeSet das = new HashDocAttributeSet();
            // 指定打印内容
            Doc doc = new SimpleDoc(this, flavor, das);
            // 不显示打印对话框，直接进行打印工作
            try {
              job.print(doc, pras); // 进行每一页的具体打印操作
            } catch (PrintException pe) {
                pe.printStackTrace();
            }
      
    }


    public int print(Graphics gp, PageFormat pf, int page)
            throws PrinterException {
        Graphics2D g2 = (Graphics2D) gp;
        g2.setPaint(Color.black); // 设置打印颜色为黑色
        if (page >= PAGES) // 当打印页号大于需要打印的总页数时，打印工作结束
            return Printable.NO_SUCH_PAGE;
        g2.translate(pf.getImageableX(), pf.getImageableY());// 转换坐标，确定打印边界
        Font font = new Font("宋体", Font.PLAIN, 16);// 创建字体
        Paper p = new Paper();
        g2.setFont(font);
        p.setSize(180, 180);
        p.setImageableArea(10, 10, 100, 141);
        pf.setPaper(p);
        // 打印当前页文本
        g2.drawString("期待您再次光临", 35, 15);
        g2.drawString("北京宝之谷国际会议中心" , 9, 35);
        font = new Font("宋体", Font.PLAIN, 14);// 创建字体
        g2.setFont(font);
        g2.drawString("------------------------------------------", 10, 55);
        g2.drawString("------------------------------------------", 10, 60);
        g2.drawString(""+num+"号房间", 63, 75);
        font = new Font("宋体", Font.PLAIN, 12);
        g2.setFont(font);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");//设置日期格式
        g2.drawString("预约入住时间:" + df.format(ktime), 15, 95);
        g2.drawString("预约离店时间:" + df.format(jtime), 15, 110);
        g2.drawString("离店时间    :" + df.format(liTime), 15, 125);
        font = new Font("宋体", Font.PLAIN, 12);
        g2.setFont(font);
        g2.drawString("------------------------------------------", 10, 145);
        g2.drawString("------------------------------------------", 10, 147);
        Image src = Toolkit.getDefaultToolkit().getImage(img);
        g2.drawImage(src, 55, 150, 90, 90, null);
        font = new Font("宋体", Font.PLAIN, 10);
        g2.setFont(font);

        g2.drawString("目前仅支持增值税普通发票,如需", 15, 250);
        g2.drawString("开专票可以持小票去前台办理!", 15, 260);
        g2.drawString("可以用微信或qq扫码开电子发票!", 15, 275);
        g2.drawString("电子发票二维码有效期为30天!", 15, 290);
        g2.drawString("请您妥善保管此小票!", 15, 300);
        g2.drawString("发票订单号:"+orderNos, 15, 315);
        g2.drawString("希望您度过美好的一天!", 45, 330);
        font = new Font("宋体", Font.PLAIN, 1);
        g2.setFont(font);
        g2.drawString("1", 45, 345);
        return Printable.PAGE_EXISTS; // 存在打印页时，继续打印工作
    }
}
