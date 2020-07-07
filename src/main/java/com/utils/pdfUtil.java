package com.utils;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.Map;


public class pdfUtil {

    public static void main(String[] args) throws FileNotFoundException {
        Map<String, Object> mapJson = new HashMap<>();
        mapJson.put("name","李鸣");
        mapJson.put("company","");
        mapJson.put("team","");
        mapJson.put("confirm","20170526");
        mapJson.put("roomno","1023");
        mapJson.put("arrival","2020-07-06 14：34");
        mapJson.put("depart","2020-07-06 14：34");
        mapJson.put("total","300.00");
        mapJson.put("balance","100.00");
        mapJson.put("printtime","2020-07-06 14：34");
        mapJson.put("accnt","F20589024");

        mapJson.put("date","F20589024 F20589024");
        mapJson.put("description","F20589024");
        mapJson.put("guestname","F20589024");
        mapJson.put("billroomno","F20589024");
        mapJson.put("roomtype","F20589024");
        mapJson.put("debit","F20589024");
        mapJson.put("time","F20589024");
        mapJson.put("code","F20589024");
        mapJson.put("vat","F20589024");
        mapJson.put("paid","F20589024");



        FileOutputStream outputStream=new FileOutputStream("C:\\Users\\123\\Desktop\\test.pdf");
        pdfUtil.fillTemplate(mapJson ,outputStream,"C:\\Users\\123\\Desktop\\账单temlete.pdf");
    }

    /**
     *
     * @param o 写入的数据
     * @param out 自定义保存pdf的文件流
     * @param templatePath pdf模板路径
     */
    // 利用模板生成pdf
    public static void fillTemplate(Map<String,Object> o, OutputStream out, String templatePath) {
        PdfReader reader;
        ByteArrayOutputStream bos;
        PdfStamper stamper;
        try {
            reader = new PdfReader(templatePath);// 读取pdf模板
            bos = new ByteArrayOutputStream();
            stamper = new PdfStamper(reader, bos);
            AcroFields form = stamper.getAcroFields();

            java.util.Iterator<String> it = form.getFields().keySet().iterator();
            while (it.hasNext()) {
                String name = it.next().toString();
                System.out.println(name);
                String value = o.get(name) != null ? o.get(name).toString() : null;
                form.setField(name, value);
            }
            stamper.setFormFlattening(true);// 如果为false那么生成的PDF文件还能编辑，一定要设为true
            stamper.close();

            Document doc = new Document();
            PdfCopy copy = new PdfCopy(doc, out);
            doc.open();
            PdfImportedPage importPage = copy.getImportedPage(new PdfReader(bos.toByteArray()), 1);
            copy.addPage(importPage);
            doc.close();

        } catch (IOException e) {
            System.out.println(e);
        } catch (DocumentException e) {
            System.out.println(e);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
