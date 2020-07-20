package com.utils;

import com.entity.Bill;
import com.entity.Reservation;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.draw.DottedLineSeparator;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Watermark;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class pdfReport {

        // 定义全局的字体静态变量
        private static Font titlefont;
        private static Font headfont;
        private static Font keyfont;
        public  static Font textfont;
        // 最大宽度
        private static int maxWidth = 520;
        // 静态代码块
        static {
            try {
                // 不同字体（这里定义为同一种字体：包含不同字号、不同style）
                BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
                titlefont = new Font(bfChinese, 16, Font.BOLD);
                headfont = new Font(bfChinese, 14, Font.BOLD);
                keyfont = new Font(bfChinese, 10, Font.BOLD);
                textfont = new Font(bfChinese, 10, Font.NORMAL);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // 生成订单PDF文件
        public void generateReservationPDF(Document document, ArrayList<Reservation> list, String month, String total,String imgUrl) throws Exception {
            Paragraph paragraph = new Paragraph("自助机"+month+"订单", titlefont);
            paragraph.setAlignment(1); //设置文字居中 0靠左   1，居中     2，靠右
            paragraph.setIndentationLeft(12); //设置左缩进
            paragraph.setIndentationRight(12); //设置右缩进
            paragraph.setFirstLineIndent(24); //设置首行缩进
            paragraph.setLeading(30f); //行间距
            paragraph.setSpacingAfter(15f);
            PdfPTable table2 = createTable(new float[] { 130, 160,120,120,130,120,120,120,120,120});
            //table.addCell(createCell("美好的一天", headfont, Element.ALIGN_LEFT, 6, false));
            table2.addCell(createCell("订单号", keyfont, Element.ALIGN_CENTER));
            table2.addCell(createCell("账号", keyfont, Element.ALIGN_CENTER));
            table2.addCell(createCell("姓名", keyfont, Element.ALIGN_CENTER));
            table2.addCell(createCell("入住时间", keyfont, Element.ALIGN_CENTER));
            table2.addCell(createCell("离店时间", keyfont, Element.ALIGN_CENTER));
            table2.addCell(createCell("房型", keyfont, Element.ALIGN_CENTER));
            table2.addCell(createCell("房间号", keyfont, Element.ALIGN_CENTER));
            table2.addCell(createCell("证件号码", keyfont, Element.ALIGN_CENTER));
            table2.addCell(createCell("手机号", keyfont, Element.ALIGN_CENTER));
            table2.addCell(createCell("订单状态", keyfont, Element.ALIGN_CENTER));

            for (int i=0;i<list.size();i++){
                table2.addCell( createCellBottomBoder(list.get(i).getResno(), keyfont, Element.ALIGN_CENTER,0.5f));
                table2.addCell( createCellBottomBoder(list.get(i).getAccnt(), keyfont, Element.ALIGN_CENTER,0.5f));
                table2.addCell( createCellBottomBoder(list.get(i).getName(), keyfont, Element.ALIGN_CENTER,0.5f));
                table2.addCell( createCellBottomBoder(list.get(i).getArrival(), keyfont, Element.ALIGN_CENTER,0.5f));
                table2.addCell( createCellBottomBoder(list.get(i).getDeparture(), keyfont, Element.ALIGN_CENTER,0.5f));
                table2.addCell( createCellBottomBoder(list.get(i).getRoomtypename(), keyfont, Element.ALIGN_CENTER,0.5f));
                table2.addCell( createCellBottomBoder(list.get(i).getRoomno(), keyfont, Element.ALIGN_CENTER,0.5f));
                table2.addCell( createCellBottomBoder(list.get(i).getIdno(), keyfont, Element.ALIGN_CENTER,0.5f));
                table2.addCell( createCellBottomBoder(list.get(i).getPhone(), keyfont, Element.ALIGN_CENTER,0.5f));
                table2.addCell( createCellBottomBoder(list.get(i).getSta(), keyfont, Element.ALIGN_CENTER,0.5f));
            }

            table2.addCell( createCellBottomBoderCospan("合计", keyfont, Element.ALIGN_RIGHT,0.7f,3));
            table2.addCell( createCellBottomBoderCospan("Total", keyfont, Element.ALIGN_CENTER,0.7f,2));
            table2.addCell( createCellBottomBoder(total, keyfont, Element.ALIGN_CENTER,0.7f));
            table2.addCell( createCellBottomBoderCospan("", keyfont, Element.ALIGN_LEFT,0.7f,2));
            table2.addCell( createCellBottomBoder("", keyfont, Element.ALIGN_LEFT,0.7f));
            table2.addCell( createCellBottomBoder("", keyfont, Element.ALIGN_CENTER,0.7f));


            // 添加图片
            Image image = Image.getInstance(imgUrl);
            image.setAlignment(Image.ALIGN_CENTER);
            image.scalePercent(8); //依照比例缩放
            image.setSpacingAfter(10f);

            document.add(image);
            document.add(paragraph);
            document.add(table2);



        }



        // 生成账单PDF文件
        public void generatePDF(Document document, String imgUrl, Map<String,String> map, ArrayList<Bill> list,String totalDebit,String totalPaid,String balance) throws Exception {
            // 段落
            Paragraph paragraph = new Paragraph("朝林松應酒店", titlefont);
            paragraph.setAlignment(1); //设置文字居中 0靠左   1，居中     2，靠右
            paragraph.setIndentationLeft(12); //设置左缩进
            paragraph.setIndentationRight(12); //设置右缩进
            paragraph.setFirstLineIndent(24); //设置首行缩进
            paragraph.setLeading(20f); //行间距
            /*paragraph.setSpacingBefore(5f); //设置段落上空白
            paragraph.setSpacingAfter(10f); //设置段落下空白*/
            // 段落
            Paragraph paragraph2 = new Paragraph("ZHAO LIN GRAND HOTEL", titlefont);
            paragraph2.setAlignment(1); //设置文字居中 0靠左   1，居中     2，靠右
            paragraph2.setIndentationLeft(12); //设置左缩进
            paragraph2.setIndentationRight(12); //设置右缩进
            paragraph2.setFirstLineIndent(24); //设置首行缩进
            paragraph2.setLeading(20f); //行间距
            paragraph2.setSpacingBefore(5f); //设置段落上空白
            paragraph2.setSpacingAfter(40f); //设置段落下空白

            // 添加图片
            Image image = Image.getInstance(imgUrl);
            image.setAlignment(Image.ALIGN_CENTER);
            image.scalePercent(8); //依照比例缩放
            image.setSpacingAfter(10f);
            // 表格
            PdfPTable table = createTable(new float[] { 130, 160,120,120});
            table.setSpacingBefore(20f);
            //table.addCell(createCell("美好的一天", headfont, Element.ALIGN_LEFT, 6, false));
            table.addCell(createCellNoboder("宾客姓名   Guest Name :", keyfont, Element.ALIGN_LEFT));
            table.addCell(createCellNoboder(map.get("mainName"), keyfont, Element.ALIGN_LEFT));
            table.addCell(createCellNoboder("房号       Room No", keyfont, Element.ALIGN_LEFT));
            table.addCell(createCellNoboder(map.get("mainRoomno"), keyfont, Element.ALIGN_LEFT));
            table.addCell(createCellNoboder("团体/公司  Group Name :", keyfont, Element.ALIGN_LEFT));
            table.addCell(createCellNoboder(map.get("mainGoup"), keyfont, Element.ALIGN_LEFT));
            table.addCell(createCellNoboder("到达日期   Arrival:", keyfont, Element.ALIGN_LEFT));
            table.addCell(createCellNoboder(map.get("mainArrival"), keyfont, Element.ALIGN_LEFT));
            table.addCell(createCellNoboder("团体代码   Group No.:", keyfont, Element.ALIGN_LEFT));
            table.addCell(createCellNoboder(map.get("mainGroupNo"), keyfont, Element.ALIGN_LEFT));
            table.addCell(createCellNoboder("离开日期   Departure:", keyfont, Element.ALIGN_LEFT));
            table.addCell(createCellNoboder(map.get("mainDeparture"), keyfont, Element.ALIGN_LEFT));
            table.addCell(createCellNoboder("确订单号   Confirmation No:", keyfont, Element.ALIGN_LEFT));
            table.addCell(createCellNoboder(map.get("mainConfirm"), keyfont, Element.ALIGN_LEFT));
            table.addCell(createCellNoboder("", keyfont, Element.ALIGN_LEFT));
            table.addCell(createCellNoboder("", keyfont, Element.ALIGN_LEFT));
            table.setSpacingAfter(50f);


            Paragraph paragraph3 = new Paragraph("总账单号Folio No. :"+map.get("mainBillNo"), keyfont);
            paragraph3.setAlignment(0); //设置文字居中 0靠左   1，居中     2，靠右
            paragraph3.setIndentationLeft(12); //设置左缩进
            paragraph3.setLeading(20f); //行间距
            paragraph3.setSpacingAfter(10f);

            PdfPTable table2 = createTable(new float[] { 130, 160,120,120,130,120,120,120,120,120});
            //table.addCell(createCell("美好的一天", headfont, Element.ALIGN_LEFT, 6, false));
            table2.addCell(createCell("日期   Date", keyfont, Element.ALIGN_CENTER));
            table2.addCell(createCell("摘要 Description", keyfont, Element.ALIGN_CENTER));
            table2.addCell(createCell("姓名 Guest Name", keyfont, Element.ALIGN_CENTER));
            table2.addCell(createCell("房间号 Room No", keyfont, Element.ALIGN_CENTER));
            table2.addCell(createCell("房型  Room Type", keyfont, Element.ALIGN_CENTER));
            table2.addCell(createCell("金额 Debit(CNY)", keyfont, Element.ALIGN_CENTER));
            table2.addCell(createCell("消费时间   Time", keyfont, Element.ALIGN_CENTER));
            table2.addCell(createCell("账单号 Code", keyfont, Element.ALIGN_CENTER));
            table2.addCell(createCell("税金   VAT", keyfont, Element.ALIGN_CENTER));
            table2.addCell(createCell("付款 Paid", keyfont, Element.ALIGN_CENTER));

            for (int i=0;i<list.size();i++){
                table2.addCell( createCellBottomBoder(list.get(i).getDate(), keyfont, Element.ALIGN_CENTER,0.5f));
                table2.addCell( createCellBottomBoder(list.get(i).getDescription(), keyfont, Element.ALIGN_CENTER,0.5f));
                table2.addCell( createCellBottomBoder(list.get(i).getGuestName(), keyfont, Element.ALIGN_CENTER,0.5f));
                table2.addCell( createCellBottomBoder(list.get(i).getRoomNo(), keyfont, Element.ALIGN_CENTER,0.5f));
                table2.addCell( createCellBottomBoder(list.get(i).getRoomType(), keyfont, Element.ALIGN_CENTER,0.5f));
                table2.addCell( createCellBottomBoder(list.get(i).getDebit(), keyfont, Element.ALIGN_CENTER,0.5f));
                table2.addCell( createCellBottomBoder(list.get(i).getTime(), keyfont, Element.ALIGN_CENTER,0.5f));
                table2.addCell( createCellBottomBoder(list.get(i).getCode(), keyfont, Element.ALIGN_CENTER,0.5f));
                table2.addCell( createCellBottomBoder(list.get(i).getVAT(), keyfont, Element.ALIGN_CENTER,0.5f));
                table2.addCell( createCellBottomBoder(list.get(i).getPaid(), keyfont, Element.ALIGN_CENTER,0.5f));
            }

            table2.addCell( createCellBottomBoderCospan("合计", keyfont, Element.ALIGN_RIGHT,0.7f,3));
            table2.addCell( createCellBottomBoderCospan("Total In CNY", keyfont, Element.ALIGN_CENTER,0.7f,2));
            table2.addCell( createCellBottomBoder(totalDebit, keyfont, Element.ALIGN_CENTER,0.7f));
            table2.addCell( createCellBottomBoderCospan("", keyfont, Element.ALIGN_LEFT,0.7f,2));
            table2.addCell( createCellBottomBoder("", keyfont, Element.ALIGN_LEFT,0.7f));
            table2.addCell( createCellBottomBoder(totalPaid, keyfont, Element.ALIGN_CENTER,0.7f));
            table2.addCell( createCellBottomBoderCospan("余额", keyfont, Element.ALIGN_RIGHT,0.7f,3));
            table2.addCell( createCellBottomBoderCospan("Balance In CNY", keyfont, Element.ALIGN_CENTER,0.7f,2));
            table2.addCell( createCellBottomBoder(balance, keyfont, Element.ALIGN_CENTER,0.7f));
            table2.addCell( createCellBottomBoderCospan("", keyfont, Element.ALIGN_LEFT,0.7f,4));

            table2.addCell( createCellNoboder("收款员/Cashier:", keyfont, Element.ALIGN_CENTER,2));
            table2.addCell( createCellNoboder("Cashier", keyfont, Element.ALIGN_LEFT,3));
            table2.addCell( createCellNoboder("打印时间/Print time:", keyfont, Element.ALIGN_CENTER,2));
            table2.addCell( createCellNoboder(new SimpleDateFormat("yyyy-MM-dd hh:mm").format(new Date()), keyfont, Element.ALIGN_LEFT,3));

            table2.addCell( createCellNoboder("备注 Remark:", keyfont, Element.ALIGN_CENTER,2));
            table2.addCell( createCellNoboder("", keyfont, Element.ALIGN_LEFT,3));
            table2.addCell( createCellNoboder("", keyfont, Element.ALIGN_CENTER,2));
            table2.addCell( createCellNoboder("", keyfont, Element.ALIGN_CENTER,3));


            Paragraph paragraph4 = new Paragraph("1.如有关人上、公司或团体不能支付此账单款项，本人同意负担一切所引起的債务。", keyfont);
            paragraph4.setAlignment(0); //设置文字居中 0靠左   1，居中     2，靠右
            paragraph4.setIndentationLeft(12); //设置左缩进
            paragraph4.setLeading(20f); //行间距
            paragraph4.setSpacingBefore(5f);
            paragraph4.setSpacingAfter(5f);

            Paragraph paragraph5 = new Paragraph("I agree that my liability for this bill is not waived and agree to be held personally liable in the event that the indicated person, company or association fails to pay for any part or the full amount of these charges.", keyfont);
            paragraph5.setAlignment(0); //设置文字居中 0靠左   1，居中     2，靠右
            paragraph5.setIndentationLeft(12); //设置左缩进
            paragraph5.setLeading(20f); //行间距
            paragraph5.setSpacingBefore(5f);
            paragraph5.setSpacingAfter(5f);

            Paragraph paragraph6 = new Paragraph("Zhaohn Grand Hotel,19 Ronghua Middle\n" +
                    "Road, BDA Beijing 100176. P R China", keyfont);
            paragraph6.setAlignment(2); //设置文字居中 0靠左   1，居中     2，靠右
            paragraph6.setIndentationRight(12); //设置左缩进
            paragraph6.setLeading(20f); //行间距
            paragraph6.setSpacingBefore(30f);
            paragraph6.setSpacingAfter(5f);

            document.add(image);
            //document.add(paragraph);
            //document.add(paragraph2);
            document.add(table);
            document.add(paragraph3);
            document.add(table2);
            document.add(paragraph4);
            document.add(paragraph5);
            //document.add(paragraph6);
        }

        /*// 直线
            Paragraph p1 = new Paragraph();
            p1.add(new Chunk(new LineSeparator()));

            // 点线
            Paragraph p2 = new Paragraph();
            p2.add(new Chunk(new DottedLineSeparator()));

            // 超链接
            Anchor anchor = new Anchor("baidu");
            anchor.setReference("www.baidu.com");

            // 定位
            Anchor gotoP = new Anchor("goto");
            gotoP.setReference("#top");*/


/**------------------------创建表格单元格的方法start----------------------------*/
        /**
         * 创建单元格(指定字体)
         * @param value
         * @param font
         * @return
         */
        public PdfPCell createCell(String value, Font font) {
            PdfPCell cell = new PdfPCell();
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPhrase(new Phrase(value, font));
            return cell;
        }
        /**
         * 创建单元格（指定字体、水平..）
         * @param value
         * @param font
         * @param align
         * @return
         */
        public PdfPCell createCell(String value, Font font, int align) {
            PdfPCell cell = new PdfPCell();
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(align);
            cell.setPhrase(new Phrase(value, font));
            return cell;
        }

    /**
     * 创建单元格（指定字体、水平..）
     * @param value
     * @param font
     * @param align
     * @return
     */
    public PdfPCell createCellNoboder(String value, Font font, int align) {
        PdfPCell cell = new PdfPCell();
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(align);
        cell.setBorderWidthLeft(0);
        cell.setBorderWidthRight(0);
        cell.setBorderWidthTop(0);
        cell.setBorderWidthBottom(0);
        cell.setPhrase(new Phrase(value, font));
        return cell;
    }

    /**
     * 创建单元格（指定字体、水平..）
     * @param value
     * @param font
     * @param align
     * @return
     */
    public PdfPCell createCellNoboder(String value, Font font, int align,int colspan) {
        PdfPCell cell = new PdfPCell();
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(align);
        cell.setBorderWidthLeft(0);
        cell.setBorderWidthRight(0);
        cell.setBorderWidthTop(0);
        cell.setBorderWidthBottom(0);
        cell.setColspan(colspan);
        cell.setPhrase(new Phrase(value, font));
        return cell;
    }


    /**
     * 创建单元格（指定字体、水平..）
     * @param value
     * @param font
     * @param align
     * @return
     */
    public PdfPCell createCellBottomBoder(String value, Font font, int align,float width) {
        PdfPCell cell = new PdfPCell();
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(align);
        cell.setBorderWidthLeft(0);
        cell.setBorderWidthRight(0);
        cell.setBorderWidthTop(0);
        cell.setBorderWidthBottom(width);
        cell.setPhrase(new Phrase(value, font));
        return cell;
    }
    /**
     * 创建单元格（指定字体、水平..）
     * @param value
     * @param font
     * @param align
     * @return
     */
    public PdfPCell createCellBottomBoderCospan(String value, Font font, int align,float width,int colspan) {
        PdfPCell cell = new PdfPCell();
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(align);
        cell.setBorderWidthLeft(0);
        cell.setBorderWidthRight(0);
        cell.setBorderWidthTop(0);
        cell.setBorderWidthBottom(width);
        cell.setColspan(colspan);
        cell.setPhrase(new Phrase(value, font));
        return cell;
    }




        /**
         * 创建单元格（指定字体、水平居..、单元格跨x列合并）
         * @param value
         * @param font
         * @param align
         * @param colspan
         * @return
         */
        public PdfPCell createCell(String value, Font font, int align, int colspan) {
            PdfPCell cell = new PdfPCell();
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(align);
            cell.setColspan(colspan);
            cell.setPhrase(new Phrase(value, font));
            return cell;
        }
        /**
         * 创建单元格（指定字体、水平居..、单元格跨x列合并、设置单元格内边距）
         * @param value
         * @param font
         * @param align
         * @param colspan
         * @param boderFlag
         * @return
         */
        public PdfPCell createCell(String value, Font font, int align, int colspan, boolean boderFlag) {
            PdfPCell cell = new PdfPCell();
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(align);
            cell.setColspan(colspan);
            cell.setPhrase(new Phrase(value, font));
            cell.setPadding(3.0f);
            if (!boderFlag) {
                cell.setBorder(0);
                cell.setPaddingTop(15.0f);
                cell.setPaddingBottom(8.0f);
            } else if (boderFlag) {
                cell.setBorder(0);
                cell.setPaddingTop(0.0f);
                cell.setPaddingBottom(15.0f);
            }
            return cell;
        }
        /**
         * 创建单元格（指定字体、水平..、边框宽度：0表示无边框、内边距）
         * @param value
         * @param font
         * @param align
         * @param borderWidth
         * @param paddingSize
         * @param flag
         * @return
         */
        public PdfPCell createCell(String value, Font font, int align, float[] borderWidth, float[] paddingSize, boolean flag) {
            PdfPCell cell = new PdfPCell();
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(align);
            cell.setPhrase(new Phrase(value, font));
            cell.setBorderWidthLeft(borderWidth[0]);
            cell.setBorderWidthRight(borderWidth[1]);
            cell.setBorderWidthTop(borderWidth[2]);
            cell.setBorderWidthBottom(borderWidth[3]);
            cell.setPaddingTop(paddingSize[0]);
            cell.setPaddingBottom(paddingSize[1]);
            if (flag) {
                cell.setColspan(2);
            }
            return cell;
        }
/**------------------------创建表格单元格的方法end----------------------------*/


/**--------------------------创建表格的方法start------------------- ---------*/
        /**
         * 创建默认列宽，指定列数、水平(居中、右、左)的表格
         * @param colNumber
         * @param align
         * @return
         */
        public PdfPTable createTable(int colNumber, int align) {
            PdfPTable table = new PdfPTable(colNumber);
            try {
                table.setTotalWidth(maxWidth);
                table.setLockedWidth(true);
                table.setHorizontalAlignment(align);
                table.getDefaultCell().setBorder(1);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return table;
        }
        /**
         * 创建指定列宽、列数的表格
         * @param widths
         * @return
         */
        public PdfPTable createTable(float[] widths) {
            PdfPTable table = new PdfPTable(widths);
            try {
                table.setTotalWidth(maxWidth);
                table.setLockedWidth(true);
                table.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.getDefaultCell().setBorder(1);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return table;
        }
        /**
         * 创建空白的表格
         * @return
         */
        public PdfPTable createBlankTable() {
            PdfPTable table = new PdfPTable(1);
            table.getDefaultCell().setBorder(0);
            table.addCell(createCell("", keyfont));
            table.setSpacingAfter(20.0f);
            table.setSpacingBefore(20.0f);
            return table;
        }
/**--------------------------创建表格的方法end------------------- ---------*/
    }