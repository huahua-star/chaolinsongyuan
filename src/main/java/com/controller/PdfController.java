package com.controller;

import com.entity.Bill;
import com.entity.HotelSetTable;
import com.entity.Reservation;
import com.entity.WestSoftBill;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPageEvent;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.events.PdfPageEventForwarder;
import com.lowagie.text.HeaderFooter;
import com.utils.EmailUtil;
import com.utils.MyHeaderFooter;
import com.utils.Returned.CommonResult;
import com.utils.Returned2.Result;
import com.utils.pdfReport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileOutputStream;
import java.io.Writer;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Slf4j
@Api(tags = "pdf")
@RestController
@RequestMapping("/pdf")
public class PdfController {


    @Value("${pdfUrl}")
    private String pdfUrl;

    @Value("${logoimgUrl}")
    private String logoimgUrl;

    @Value("${Email.HOST}")
    private String HOST;

    @Value("${Email.FROM}")
    private String FROM;

    @Value("${Email.AFFIXNAME}")
    private String AFFIXNAME;

    @Value("${Email.USER}")
    private String USER;

    @Value("${Email.PWD}")
    private String PWD;

    @Value("${Email.SUBJECT}")
    private String SUBJECT;

    /**
     * 生成账单PDF
     */
    @ApiOperation(value = "生成账单PDF")
    @RequestMapping(value = "/getPdf", method = RequestMethod.GET)
    public Result<?> getPdf(String accnt) {
        //账号获取定单
        Reservation reservation=GetArrivingReservationController.GetOneReservation(accnt);
        //账号获取账单
        List<WestSoftBill> list=GetArrivingReservationController.GetBill(accnt,"0");//scope 0  所有账单 1 未结账单

        /*for (WestSoftBill westSoftBill :list){
            System.out.println(westSoftBill);
        }*/
        try {
            String filePath=pdfUrl+accnt+".pdf";
            // 1.新建document对象
            Document document = new Document(PageSize.A4);// 建立一个Document对象

            // 2.建立一个书写器(Writer)与document对象关联
            File file = new File(filePath);
            file.createNewFile();
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
            //writer.setPageEvent(new Watermark("HELLO ITEXTPDF"));// 水印
            MyHeaderFooter headerFooter=new MyHeaderFooter();
            writer.setPageEvent(headerFooter);
            int i=writer.getPageNumber();
            System.out.println("i:"+i);
            // 3.打开文档
            document.open();
           /*document.addTitle("Title@PDF-Java");// 标题
            document.addAuthor("Author@umiz");// 作者
            document.addSubject("Subject@iText pdf sample");// 主题
            document.addKeywords("Keywords@iTextpdf");// 关键字
            document.addCreator("Creator@umiz`s");// 创建者*/
            Map<String,String> map=new HashMap<>();
            map.put("mainName",reservation.getName());
            map.put("mainRoomno",reservation.getRoomno());
            map.put("mainGoup",reservation.getCompany());
            map.put("mainArrival",reservation.getArrival());
            map.put("mainGroupNo",reservation.getGroupno());
            map.put("mainDeparture",reservation.getDeparture());
            map.put("mainConfirm",reservation.getResno());
            map.put("mainBillNo",accnt);
            ArrayList<Bill> biilList=new ArrayList<>();
            BigDecimal totalDebit=new BigDecimal(0.00);
            BigDecimal totalPaid=new BigDecimal(0.00);
            BigDecimal balance=new BigDecimal(0.00);
            for (WestSoftBill westSoftBill :list){
                Bill bill=new Bill();
                Reservation billReservation=GetArrivingReservationController.GetOneReservation(westSoftBill.getAccnt());
                bill.setCode(westSoftBill.getArgcode());
                bill.setDate(westSoftBill.getDate_().substring(0,10));
                bill.setDescription(westSoftBill.getArgcode_descript());
                bill.setGuestName(billReservation.getName());
                if (Integer.parseInt(westSoftBill.getPccode())>9){
                    totalPaid=totalPaid.add(new BigDecimal(westSoftBill.getAmount()));
                    bill.setDebit("");
                    bill.setPaid(westSoftBill.getAmount());
                }else{
                    totalDebit=totalDebit.add(new BigDecimal(westSoftBill.getAmount()));
                    bill.setDebit(westSoftBill.getAmount());
                    bill.setPaid("");
                }
                bill.setRoomNo(billReservation.getRoomno());
                bill.setRoomType(billReservation.getRoomtype());
                bill.setVAT("0.00");
                bill.setTime(westSoftBill.getDate_().substring(10));
                biilList.add(bill);
            }
            // 4.向文档中添加内容
            balance=totalDebit.subtract(totalPaid);
            new pdfReport().generatePDF(document,logoimgUrl,map,biilList,
                    totalDebit.setScale(2, BigDecimal.ROUND_HALF_UP).toString(),
                    totalPaid.setScale(2, BigDecimal.ROUND_HALF_UP).toString(),
                    balance.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            // 5.关闭文档
            document.close();
            return Result.ok(filePath);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("失败");
        }
    }

    /**
     * 发送账单PDF
     */
    @ApiOperation(value = "发送账单PDF")
    @RequestMapping(value = "/sendEmailPdf", method = RequestMethod.GET)
    public Result<?> sendEmailPdf(String TO,String AFFIX) {
        String[] TOS=TO.split(",");
        EmailUtil.send("账单消费明细",HOST,FROM,AFFIX,AFFIXNAME,USER,PWD,SUBJECT,TOS);
        return Result.ok("成功");
    }

}
