package com.utils;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.io.IOException;

public class MyHeaderFooter extends PdfPageEventHelper {
    // 总页数
    PdfTemplate totalPage;
    Font hfFont;
    {
        try {
            hfFont = new Font(BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED), 6, Font.NORMAL);
            hfFont.setColor(222,223,215);
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 打开文档时，创建一个总页数的模版
    public void onOpenDocument(PdfWriter writer, Document document) {
        PdfContentByte cb =writer.getDirectContent();
        totalPage = cb.createTemplate(30, 16);
    }
    // 一页加载完成触发，写入页眉和页脚
    public void onEndPage(PdfWriter writer, Document document) {
        PdfPTable table = new PdfPTable(4);
        try {
            table.setTotalWidth(PageSize.A4.getWidth() - 100);
            table.setWidths(new int[] {65, 65, 40,40});
            table.setLockedWidth(true);
            //table.getDefaultCell().setFixedHeight(-10);
            //table.getDefaultCell().setBorder(Rectangle.BOTTOM);

            table.addCell(createCellNoboder("中 国 北 京 市 北 京 经 济 技 术 开 发 区 荣 华 中 路\n" +
                    "19 号 院 朝 林 松 源 酒 店 邮 编 100176", hfFont,0));// 可以直接使用addCell(str)，不过不能指定字体，中文无法显示
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(createCellNoboder("Zhaolin Grand Hotel.19 Ronghua Middle\n" +
                    "Road, BDA Beijing 100176, P.R. China", hfFont,0));
            table.addCell(createCellNoboder("T 86 (10) 5387 6999\n" +
                    "F 86 (10) 5387 6998", hfFont,0));
            table.addCell(createCellNoboder("www.zhaolingrandhotel.com\n" +
                    "rsvn@zhaolingrandhotel.com", hfFont,0));
            // 将页眉写到document中，位置可以指定，指定到下面就是页脚
            //table.writeSelectedRows(0, -1, 50, PageSize.A4.getHeight(), writer.getDirectContent());
            table.writeSelectedRows(0, -1, 50, 35, writer.getDirectContent());
            //document.add(table);
        } catch (Exception de) {
            throw new ExceptionConverter(de);
        }
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


    // 全部完成后，将总页数的pdf模版写到指定位置
    public void onCloseDocument(PdfWriter writer, Document document) {
        String text = "总" + (writer.getPageNumber()) + "页";
        ColumnText.showTextAligned(totalPage, Element.ALIGN_LEFT, new Paragraph(text,hfFont), 2, 2, 0);
    }
}
