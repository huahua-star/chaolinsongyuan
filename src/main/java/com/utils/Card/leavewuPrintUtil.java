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

public class leavewuPrintUtil implements Printable {
    private int PAGES = 0;
    private String num;
    private String hname;
    private Date ktime;
    private Date jtime;
    private Date liTime;
    private String img;

    public void print(String roomNum, String imgurl, Date kstime, Date jstime, Date leaveTime) {
        num = roomNum;
        img = imgurl;
        ktime = kstime;
        jtime = jstime;
        liTime = leaveTime;
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
        g2.drawString("期待您再次光临", 35, 10);
        g2.drawString("北京宝之谷国际会议中心", 10, 30);
        font = new Font("宋体", Font.PLAIN, 15);// 创建字体
        g2.setFont(font);
        g2.drawString("------------------------------------------", 10, 50);
        g2.drawString("------------------------------------------", 10, 52);
        g2.drawString(""+num+"号房间", 63, 65);
        font = new Font("宋体", Font.PLAIN, 15);
        g2.setFont(font);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");//设置日期格式
        g2.drawString("预约入住时间:" + df.format(ktime), 15, 85);
        g2.drawString("预约离店时间:" + df.format(jtime), 15, 100);
        g2.drawString("离店时间    :" + df.format(liTime), 15, 115);
        font = new Font("宋体", Font.PLAIN, 12);
        g2.setFont(font);
        g2.drawString("------------------------------------------", 10, 125);
        g2.drawString("------------------------------------------", 10, 127);
        g2.drawString("您可以持小票去前台办理发票!", 20, 145);
        font = new Font("宋体", Font.PLAIN, 10);// 创建字体
        g2.setFont(font);
        g2.drawString("希望您度过美好的一天!", 45, 160);
        font = new Font("宋体", Font.PLAIN, 1);// 创建字体
        g2.setFont(font);
        g2.drawString("1", 15, 165);
        return Printable.PAGE_EXISTS; // 存在打印页时，继续打印工作
    }
}
