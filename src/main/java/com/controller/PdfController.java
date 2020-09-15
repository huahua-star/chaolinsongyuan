package com.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.entity.*;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPageEvent;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.events.PdfPageEventForwarder;
import com.lowagie.text.HeaderFooter;
import com.service.OperationRecordService;
import com.service.ReservationService;
import com.utils.EmailUtil;
import com.utils.MyHeaderFooter;
import com.utils.PageFooter;
import com.utils.Returned.CommonResult;
import com.utils.Returned2.Result;
import com.utils.pdfReport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileOutputStream;
import java.io.Writer;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

@Slf4j
@Api(tags = "pdf")
@RestController
@RequestMapping("/pdf")
public class PdfController {


    @Value("${pdfUrl}")
    private String pdfUrl;

    @Value("${reservationMonthUrl}")
    private String reservationMonthUrl;

    @Value("${operRecordMonthUrl}")
    private String operRecordMonthUrl;

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

    @Value("${Email.reservationEmail}")
    private String reservationEmail;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private OperationRecordService operationRecordService;

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
        EmailUtil.send(" 尊敬的客人/Dear guest：\r\n" +
                "      您好！非常感谢您选择北京朝林松源酒店，我们很荣幸能为您提供服务，" +
                "附件是您的账单信息请查收，有任何问题请您随时与我们联系，谢谢！\r\n" +
                "      Thank you for choosing Zhaolin Grand Hotel Beijing！It is our great honor to serve you,please kindly find your detail folio as attachment,any concern please feel free to contact us,thank you!",
                HOST,FROM,AFFIX,AFFIXNAME,USER,PWD,SUBJECT,TOS);
        return Result.ok("成功");
    }


    /*
     * 生成自助机每月订单PDF
     */
    @ApiOperation(value = "生成自助机每月订单PDF")
    @RequestMapping(value = "/createReservationMonthPdf", method = RequestMethod.GET)
    public Result<?> createReservationMonthPdf() {
        //获取上月
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        Date date=new Date();
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MONTH,calendar.get(Calendar.MONTH)-1);
        date=calendar.getTime();
        String month=format.format(date);
        System.out.println("month:"+month);
        try {
            String filePath=reservationMonthUrl+month+".pdf";
            System.out.println("filePath:"+filePath);
            // 1.新建document对象
            Document document = new Document(PageSize.A4);// 建立一个Document对象
            // 2.建立一个书写器(Writer)与document对象关联
            File file = new File(filePath);
            file.createNewFile();
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
            PageFooter pageFooter=new PageFooter();
            writer.setPageEvent(pageFooter);
            // 3.打开文档
            document.open();
            month=new SimpleDateFormat("yyyy/MM").format(date);
            String beginTime=month+"/01 00:00";
            String endTime=month+"/31 24:00";
            List<Reservation> list=reservationService.list(new QueryWrapper<Reservation>().between("departure", beginTime,endTime));
            for (Reservation reservation : list){
                reservation.setSta(getStatus(reservation.getSta()));
            }
            ArrayList<Reservation> reservationList=(ArrayList<Reservation>) list;
            // 4.向文档中添加内容
            new pdfReport().generateReservationPDF(document,reservationList,month,list.size()+"",logoimgUrl);
            // 5.关闭文档
            document.close();
            return Result.ok(filePath);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("失败");
        }
    }

    /*
     * 生成自助机每月生成操作记录pdf
     */
    @ApiOperation(value = "生成自助机每月生成操作记录pdf")
    @RequestMapping(value = "/createOperRecordMonthPdf", method = RequestMethod.GET)
    public Result<?> createOperRecordMonthPdf() {
        //获取上月
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        Date date=new Date();
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MONTH,calendar.get(Calendar.MONTH)-1);
        date=calendar.getTime();
        String month=format.format(date);
        System.out.println("month:"+month);
        try {
            String filePath=operRecordMonthUrl+month+".pdf";
            System.out.println("filePath:"+filePath);
            // 1.新建document对象
            Document document = new Document(PageSize.A4);// 建立一个Document对象
            // 2.建立一个书写器(Writer)与document对象关联
            File file = new File(filePath);
            file.createNewFile();
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
            PageFooter pageFooter=new PageFooter();
            writer.setPageEvent(pageFooter);
            // 3.打开文档
            document.open();
            month=new SimpleDateFormat("yyyy-MM").format(date);
            String beginTime=month+"-01 00:00:00";
            String endTime=month+"-31 23:59:59";
            List<OperationRecord> list=operationRecordService.list(new QueryWrapper<OperationRecord>().between("create_time", beginTime,endTime));
            ArrayList<OperationRecord> operationRecords=(ArrayList<OperationRecord>) list;
            // 4.向文档中添加内容
            new pdfReport().generateOperationRecordPDF(document,operationRecords,month,list.size()+"",logoimgUrl);
            // 5.关闭文档
            document.close();
            return Result.ok(filePath);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("失败");
        }
    }



    @Scheduled(cron = "0 0 1 1 * ?")
    public void sendEmail() throws InterruptedException {
        log.info("生成订单");
        createReservationMonthPdf();
        log.info("发送邮箱");
        String[] TOS=new String[]{reservationEmail};
        //获取上月
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        Date date=new Date();
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MONTH,calendar.get(Calendar.MONTH)-1);
        date=calendar.getTime();
        String fileName=format.format(date)+".pdf";
        String filePath=reservationMonthUrl+fileName;
        EmailUtil.send(format.format(date)+"月自助机订单统计",HOST,FROM,filePath,fileName,USER,PWD,format.format(date)+"月自助机订单统计",TOS);
        Thread.sleep(5000);
        createOperRecordMonthPdf();
        filePath=operRecordMonthUrl+fileName;
        EmailUtil.send(format.format(date)+"月自助机订单统计",HOST,FROM,filePath,fileName,USER,PWD,format.format(date)+"月自助机操作记录统计",TOS);

    }





    public static String getStatus(String status){
        Map<String,String> map=new HashMap<>();
        map.put("I","在住");
        map.put("R","预定");
        map.put("O","结账");
        map.put("X","取消");
        map.put("S","临时挂账");
        map.put("D","昨日结账");
        map.put("N","预定未到");
        map.put(null,"无订单状态");
        map.put("","无订单状态");
        return map.get(status);
    }

}
