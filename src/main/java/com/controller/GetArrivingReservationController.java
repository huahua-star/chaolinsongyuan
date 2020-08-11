package com.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ctc.wstx.util.StringUtil;
import com.entity.*;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.service.*;
import com.utils.Base64Img;
import com.utils.Returned.CommonResult;
import com.utils.Returned3.R;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.cxf.binding.soap.saaj.SAAJOutInterceptor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.utils.applicationContextJava.Jsons;
import static com.utils.applicationContextJava.XRHotels;


@Slf4j
@RestController
@RequestMapping("/in")
public class GetArrivingReservationController {

    @Autowired
    private GetArrivingReservationService getArrivingReservation;

    @Autowired
    private CertificateService certificateService;

    @Autowired
    private ITblTxnpService tblTxnpService;

    @Autowired
    private NationService nationService;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private RaceService raceService;

    @Autowired
    private RabbitHelper rabbitHelper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private OperationRecordService operationRecordService;

    /**
     * 修改自助机留存订单的 状态
     */
    @ApiOperation(value = "修改自助机留存订单的 状态")
    @RequestMapping(value = "/updateReservation", method = RequestMethod.GET)
    public CommonResult<?> updateReservation(String accnt,String state,String gonganstate) {
        log.info("updateReservation()方法");
        if (StringUtils.isEmpty(accnt)){
            return CommonResult.failed("缺少参数");
        }
        Reservation reservation=reservationService.getOne(new QueryWrapper<Reservation>().eq("accnt",accnt));
        if (gonganstate!=null&&!"".equals(gonganstate)){
            reservation.setGonganstate(gonganstate);
        }
        if (state!=null&&!"".equals(state)){
            reservation.setState(state);
        }
        reservationService.updateById(reservation);
        return CommonResult.success("修改成功");
    }


    /**
     * 证件  code 转换 中文描述
     */
    @ApiOperation(value = "证件  code 转换 中文描述")
    @RequestMapping(value = "/GetCertificate", method = RequestMethod.GET)
    public CommonResult<?> GetCertificate(String code) {
        log.info("GetCertificate()方法");
        if (StringUtils.isEmpty(code)){
            return CommonResult.failed("转换失败，缺少参数");
        }
        Certificate certificate=certificateService.getOne(new QueryWrapper<Certificate>().eq("code",code));
        return CommonResult.success(certificate.getDescription());
    }

    /**
     *
     * @param code 民族代码
     * @return
     */
    @ApiOperation(value = "民族代码  code 转换")
    @RequestMapping(value = "/GetRace", method = RequestMethod.GET)
    public String GetRace(String code) {
        log.info("GetRace()方法");
        if (StringUtils.isEmpty(code)){
            return "转换失败，缺少参数";
        }
        Race race=raceService.getOne(new QueryWrapper<Race>().eq("code",code));
        return race.getRaceid();
    }

    /**
     *
     *  民族保存
     * @return
     */
    @ApiOperation(value = "民族代码保存")
    @RequestMapping(value = "/GetRaceSave", method = RequestMethod.GET)
    public String GetRaceSave() {
        log.info("GetRace()方法");
        String tom = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<interface>\n" +
                "<item>\n" +
                "<cat>" + "race" + "</cat>\n" +
                "</item>\n" +
                "</interface>";
        final XRHotelServiceSoap soap = XRHotels();
        final String dictionaryGet = soap.dictionaryGet(tom);
        final String jsons = Jsons(dictionaryGet);
        com.alibaba.fastjson.JSONObject object=JSON.parseObject(jsons);
        com.alibaba.fastjson.JSONObject resultObj=object.getJSONObject("response");
        com.alibaba.fastjson.JSONArray jsonArray=resultObj.getJSONArray("items");
        List<Race> raceList = JSON.parseObject(jsonArray.toJSONString(), new TypeReference<List<Race>>() {});
        raceService.saveBatch(raceList);
        return "成功";
    }


    @RequestMapping(value = "/GetBill", method = RequestMethod.GET)
    public static List<WestSoftBill> GetBill(String accnt,String scope) {
        log.info("GetBill()方法");

        String tom = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<interface>\n" +
                "<item>\n" +
                "<accnt>" + accnt + "</accnt>\n" +
                "<scope>" + scope + "</scope>\n" +
                "</item>\n" +
                "</interface>";
        System.out.println("tom:"+tom);
        final XRHotelServiceSoap soap = XRHotels();
        final String details = soap.getInvoiceNew(tom);
        final String jsons = Jsons(details);
        System.out.println("json:"+jsons);
        com.alibaba.fastjson.JSONObject object=JSON.parseObject(jsons);
        com.alibaba.fastjson.JSONObject resultObj=object.getJSONObject("response");
        String res=resultObj.getString("result");
        if ("0".equals(res)){
            com.alibaba.fastjson.JSONArray items=resultObj.getJSONArray("items");
            List<WestSoftBill> list=JSON.parseObject(items.toJSONString(), new TypeReference<List<WestSoftBill>>() {});
            return list;
        }else{
            return null;
        }
    }



    @RequestMapping(value = "/GetOneReservation", method = RequestMethod.GET)
    public static Reservation GetOneReservation(String accnt) {
        log.info("GetOneReservation()方法");

        String tom = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<interface>\n" +
                "<item>\n" +
                "<accnt>" + accnt + "</accnt>\n" +
                "</item>\n" +
                "</interface>";
        System.out.println("tom:"+tom);
        final XRHotelServiceSoap soap = XRHotels();
        final String details = soap.getReservationDetails(tom);
        final String jsons = Jsons(details);
        com.alibaba.fastjson.JSONObject object=JSON.parseObject(jsons);
        com.alibaba.fastjson.JSONObject resultObj=object.getJSONObject("response");
        String res=resultObj.getString("result");
        if ("0".equals(res)){
            com.alibaba.fastjson.JSONObject items=resultObj.getJSONObject("items");
            Reservation reservation=items.toJavaObject(Reservation.class);
            return reservation;
        }else{
            return null;
        }
    }





    /**
     * 2.1获取预订客人信息
     *
     * @param name     客人姓
     * @param resno    预定号
     * @param fullname 客人姓名
     * @param ident    身份证号
     * @param mobile   手机号
     * @return
     */
    @ApiOperation(value = "2.1获取预订客人信息")
    @RequestMapping(value = "/GetArrivingReservation", method = RequestMethod.GET)
    public CommonResult<?> GetArrivingReservation(String name, Integer resno,
                                                  String fullname, String ident, String mobile) {
        log.info("进入GetArrivingReservation()方法");
        /*if (StringUtils.isEmpty(name) && StringUtils.isEmpty(fullname) && StringUtils.isEmpty(ident) && StringUtils.isEmpty(mobile) && resno == null) {
            log.error("异常访问GetArrivingReservation()方法");
            return CommonResult.validateFailed("请输入必须值");
        }*/
        String tom = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<interface>\n" +
                "<item>\n" ;
        if (StringUtils.isEmpty(name)){
            tom+= "<name></name>\n";
        }else{
            tom+= "<name>" + name + "</name>\n";
        }
        if (StringUtils.isEmpty(resno)){
            tom+= "<resno></resno>\n" ;
        }else{
            tom+=  "<resno>" + resno + "</resno>\n" ;
        }
        if (StringUtils.isEmpty(fullname)){
            tom+= "<fullname></fullname>\n" ;
        }else{
            tom+= "<fullname>" + fullname + "</fullname>\n" ;
        }
        if (StringUtils.isEmpty(ident)){
            tom+= "<ident></ident>\n" ;
        }else{
            tom+= "<ident>" + ident + "</ident>\n" ;
        }
        if (StringUtils.isEmpty(mobile)){
            tom+= "<mobile></mobile>\n" ;
        }else{
            tom+= "<mobile>" + mobile + "</mobile>\n" ;
        }
        tom+="</item>\n" +
        "</interface>";
        System.out.println(tom);
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        XRHotelServiceSoap soap = context.getBean(XRHotelServiceSoap.class);
        final String arrivingReservation = soap.getArrivingReservation(tom);
        System.out.println(arrivingReservation);
        JSONObject object = XML.toJSONObject(arrivingReservation);
        String jsonData = object.toString();
        System.out.println(jsonData);
        log.info("GetArrivingReservationImpl()方法执行结束");
        return CommonResult.success(jsonData);
    }


    /**
     * 2.2获取当日离店客人信息
     *
     * @param name   客人姓名
     * @param resno  预定号
     * @param accnt  账号
     * @param roomno 房间号
     * @param ident  证件号码
     * @param phone  手机号
     * @return
     */
    @ApiOperation(value = "2.2获取当日离店客人信息")
    @RequestMapping(value = "/GetDepartingReservation", method = RequestMethod.GET)
    public CommonResult<?> GetDepartingReservation(String name, Integer resno,
                                                   String accnt, String roomno, String ident, String phone) {
        log.info("进入GetDepartingReservation()方法");
        if (name == null && resno == null && accnt == null && roomno == null && ident == null && phone == null) {
            log.error("异常访问GetDepartingReservation()方法");
            return CommonResult.validateFailed("请输入必须值");
        }
        String tom = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<interface>\n" +
                "<item>\n" +
                "<name>" + name + "</name>\n" +
                "<resno>" + resno + "</resno>\n" +
                "<accnt>" + accnt + "</accnt>\n" +
                "<roomno>" + roomno + "</roomno>\n" +
                "<ident>" + ident + "</ident>\n" +
                "<phone>" + phone + "</phone>\n" +
                "</item>\n" +
                "</interface>";
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        XRHotelServiceSoap soap = context.getBean(XRHotelServiceSoap.class);
        final String departings = soap.getDepartingReservation(tom);
        System.out.println(departings);
        JSONObject object = XML.toJSONObject(departings);
        String jsonData = object.toString();
        System.out.println(jsonData);
        return CommonResult.success(jsonData);
    }


    /**
     * 2.3 获取入住客人信息
     */
    @ApiOperation(value = "2.3 获取入住客人信息")
    @RequestMapping(value = "/GetInHouseReservation", method = RequestMethod.GET)
    public CommonResult<?> GetInHouseReservation(String name, String resno, String accnt, String roomno) {
        if (name == null && resno == null && accnt == null && roomno == null) {
            log.error("异常访问GetInHouseReservation方法");
            return CommonResult.validateFailed("请输入必须值");
        }
        String tom = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<interface>\n" +
                "<item>\n" ;
                if(!StringUtils.isEmpty(name)){
                    tom+="<name>" + name + "</name>\n" ;
                }
                if(!StringUtils.isEmpty(resno)){
                    tom+="<resno>" + resno + "</resno>\n" ;
                }
                if(!StringUtils.isEmpty(accnt)){
                    tom+="<accnt>" + accnt + "</accnt>\n" ;
                }
                if(!StringUtils.isEmpty(roomno)){
                    tom+="<roomno>" + roomno + "</roomno>\n" ;
                }
                tom+="</item>\n" +
                "</interface>";
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        XRHotelServiceSoap soap = context.getBean(XRHotelServiceSoap.class);
        final String inhouse = soap.getInHouseReservation(tom);
        JSONObject object = XML.toJSONObject(inhouse);
        String jsonData = object.toString();
        return CommonResult.success(jsonData);
    }


    /**
     * 2.4分配房间
     */
    @ApiOperation(value = "2.4分配房间")
    @RequestMapping(value = "/AssignRoom", method = RequestMethod.GET)
    public CommonResult<?> AssignRoom(String roomno, String accnt) {
        log.info("进入AssignRoom()方法");
        if (StringUtils.isEmpty(roomno) || StringUtils.isEmpty(accnt)) {
            log.error("异常访问AssignRoom()方法");
            return CommonResult.failed();
        }
        String tom = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<interface>\n" +
                "<item>\n" +
                "<roomno>" + roomno + "</roomno>\n" +
                "<accnt>" + accnt + "</accnt>\n" +
                "</item>\n" +
                "</interface>";
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        XRHotelServiceSoap soap = context.getBean(XRHotelServiceSoap.class);
        final String assignRoom = soap.assignRoom(tom);
        System.out.println(assignRoom);
        JSONObject object = XML.toJSONObject(assignRoom);
        String jsonData = object.toString();
        System.out.println(jsonData);
        log.info("AssignRoom()方法执行结束");
        return CommonResult.success(jsonData);
    }


    /**
     * 2.5释放房间
     */
    @ApiOperation(value = "2.5释放房间")
    @RequestMapping(value = "/ReleaseRoom", method = RequestMethod.GET)
    public CommonResult<?> ReleaseRoom(String accnt) {
        log.info("进入ReleaseRoom()方法");
        if (StringUtils.isEmpty(accnt)) {
            log.error("异常访问ReleaseRoom()方法");
            return CommonResult.failed();
        }
        String tom = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<interface>\n" +
                "<item>\n" +
                "<accnt>" + accnt + "</accnt>\n" +
                "</item>\n" +
                "</interface>";
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        XRHotelServiceSoap soap = context.getBean(XRHotelServiceSoap.class);
        final String room = soap.releaseRoom(tom);
        System.out.println(room);
        JSONObject object = XML.toJSONObject(room);
        String jsonData = object.toString();
        System.out.println(jsonData);
        log.info("ReleaseRoom()方法执行结束");
        return CommonResult.success(jsonData);
    }


    /**
     * 2.6 入住
     */
    @ApiOperation(value = "2.6 入住")
    @RequestMapping(value = "/GuestRemoteCheckIn", method = RequestMethod.GET)
    public CommonResult<?> GuestRemoteCheckIn(String accnt, String feecode, String masterremark,
                                              String billremark, String cardno, String expiry,
                                              String pccode, String amount, String foliono, String creditno) {
        log.info("进入GuestRemoteCheckIn()方法");
        String tom = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<interface>\n" +
                "  <item>\n" +
                "<accnt>" + accnt + "</accnt>\n" +
                "<feecode>" + feecode + "</feecode> \n" +
                "<masterremark>" + masterremark + "</masterremark> \n" +
                "<billremark>" + billremark + "</billremark>\t \n" +
                "  </item>\n" +
                "  <accredits>\n" +
                "    <cardno>" + cardno + "</cardno>\n" +
                "    <expiry>" + expiry + "</expiry>\n" +
                "    <pccode>" + pccode + "</pccode>\n" +
                "<amount>" + amount + "</amount>\n" +
                "<foliono>" + foliono + "</foliono> \n" +
                "<creditno>" + creditno + "</creditno> \n" +
                "  </accredits>\n" +
                "</interface>";
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        XRHotelServiceSoap soap = context.getBean(XRHotelServiceSoap.class);
        final String checkIn = soap.guestRemoteCheckIn(tom);
        System.out.println(checkIn);
        JSONObject object = XML.toJSONObject(checkIn);
        String jsonData = object.toString();
        System.out.println(jsonData);
        log.info("GuestRemoteCheckIn()方法执行完成");
        //订单数据留存
        Reservation reservation=reservationService.getOne(new QueryWrapper<Reservation>().eq("accnt",accnt));
        if (reservation==null){
            reservation=GetOneReservation(accnt);
            if (reservation!=null){
                log.info("订单留存失败:accnt:"+accnt);
                reservationService.save(reservation);
                System.out.println(reservation);
            }
        }

        OperationRecord operationRecord=new OperationRecord();
        operationRecord.setName(reservation.getName());
        operationRecord.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        operationRecord.setOperation("CHECKIN");
        operationRecord.setOperationDes("账号为:"+reservation.getAccnt()+"，姓名为:"+reservation.getName()+"的客人，在"+operationRecord.getCreateTime()+"在自助机办理入住。");
        operationRecord.setResno(reservation.getAccnt());
        operationRecordService.save(operationRecord);

        return CommonResult.success(jsonData);

    }


    /**
     * 2.7 单个散客退房
     */
    @ApiOperation(value = "2.7 单个散客退房")
    @RequestMapping(value = "/GuestRemoteCheckOut", method = RequestMethod.GET)
    public CommonResult<?> GuestRemoteCheckOut(String accnt, String masterremark, String billremark, String cardno, String expiry,
                                               String pccode, String amount, String foliono, String resno, String roomno,String name) {
        log.info("进入GuestRemoteCheckOut()方法");
        String flag = getArrivingReservation.XmlToJavaBean(resno, roomno,name,accnt);
        if ("2".equals(flag)) {
            log.error("GuestRemoteCheckOut()方法失败");
            return CommonResult.failed("获取客人信息时,未检索到相关内容!");
        }
        if ("0".equals(flag)) {
            log.error("GuestRemoteCheckOut()方法失败");
            return CommonResult.failed("有同住人无法退房");
        }
        String tom = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<interface>\n" +
                "<item>\n" +
                "<accnt>" + accnt + "</accnt>\n";
                if(!StringUtils.isEmpty(masterremark)){
                    tom+="<masterremark>" + masterremark + "</masterremark>\n";
                }
                if(!StringUtils.isEmpty(billremark)){
                    tom+="<billremark>" + billremark + "</billremark>\n";
                }
                tom+="</item>\n";
        if(!StringUtils.isEmpty(cardno)||!StringUtils.isEmpty(expiry)||
                !StringUtils.isEmpty(pccode)||!StringUtils.isEmpty(amount)||!StringUtils.isEmpty(foliono)){
            tom+="<postings>\n" +
                    "<cardno>" + cardno + "</cardno>\n" +
                    "<expiry>" + expiry + "</expiry>\n" +
                    "<pccode>" + pccode + "</pccode>\n" +
                    "<amount>" + amount + "</amount>\n" +
                    "<foliono>" + foliono + "</foliono>\n" +
                    "</postings>\n" ;
        }
        tom+="</interface>";
        XRHotelServiceSoap soaps = XRHotels();
        final String remoteCheckOut = soaps.guestRemoteCheckOut(tom);
        System.out.println(remoteCheckOut);
        JSONObject object = XML.toJSONObject(remoteCheckOut);
        String jsonData = object.toString();
        log.info("GuestRemoteCheckOut()方法执行结束");

        //订单数据留存
        Reservation reservation=reservationService.getOne(new QueryWrapper<Reservation>().eq("accnt",accnt));
        Reservation newReservation= GetOneReservation(accnt);
        if (reservation==null){
            reservationService.save(newReservation);
            System.out.println(newReservation);
        }else{
            newReservation.setId(reservation.getId());
            reservationService.updateById(newReservation);
            System.out.println(newReservation);
        }

        OperationRecord operationRecord=new OperationRecord();
        operationRecord.setName(newReservation.getName());
        operationRecord.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        operationRecord.setOperation("CHECKOUT");
        operationRecord.setOperationDes("账号为:"+newReservation.getAccnt()+"，姓名为:"+newReservation.getName()+"的客人，在"+operationRecord.getCreateTime()+"在自助机办理退房。");
        operationRecord.setResno(newReservation.getAccnt());
        operationRecordService.save(operationRecord);
        return CommonResult.success("退房成功");
    }


    /**
     * 2.8 获取当前可用房
     */
    @ApiOperation(value = "2.8 获取当前可用房")
    @RequestMapping(value = "/GetAvailableRoomFeatures", method = RequestMethod.GET)
    public CommonResult<?> GetAvailableRoomFeatures(String ArrivalDate, String DepartureDate, String RoomType,
                                                    String RoomNumber, String RoomStates) {
        log.info("进入GetAvailableRoomFeatures()方法");
        if (StringUtils.isEmpty(ArrivalDate) || StringUtils.isEmpty(DepartureDate)) {
            log.error("异常访问GetAvailableRoomFeatures()方法");
            return CommonResult.failed();
        }
        String tom = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<interface>\n" +
                "<item>\n" +
                "<ArrivalDate>" + ArrivalDate + "</ArrivalDate>\n" +
                "<DepartureDate>" + DepartureDate + "</DepartureDate>\n" +
                "<RoomType>" + RoomType + "</RoomType>\n" +
                "<RoomNumber>" + RoomNumber + "</RoomNumber>\n" +
                "<RoomStates>" + RoomStates + "</RoomStates>\n" +
                "</item>\n" +
                "</interface>";
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        XRHotelServiceSoap soap = context.getBean(XRHotelServiceSoap.class);
        final String availableRoomFeatures = soap.getAvailableRoomFeatures(tom);
        System.out.println(availableRoomFeatures);
        JSONObject object = XML.toJSONObject(availableRoomFeatures);
        String jsonData = object.toString();
        System.out.println(jsonData);
        log.info("GetAvailableRoomFeatures()方法执行结束");
        return CommonResult.success(jsonData);
    }


    /**
     * 2.9  获取账单
     */
    @ApiOperation(value = "2.9  获取账单")
    @RequestMapping(value = "/GetInvoice", method = RequestMethod.GET)
    public CommonResult<?> GetInvoice(String accnt, Integer scope) {
        log.info("进入GetInvoice()方法");
        if (StringUtils.isEmpty(accnt) || scope == null) {
            log.error("异常访问GetInvoice()方法");
            return CommonResult.failed();
        }
        String tom = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<interface>\n" +
                "<item>\n" +
                "<accnt>" + accnt + "</accnt>\n" +
                "<scope>" + scope + "</scope>\n" +
                "</item>\n" +
                "</interface>";
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        XRHotelServiceSoap soap = context.getBean(XRHotelServiceSoap.class);
        final String soapInvoice = soap.getInvoice(tom);
        System.out.println(soapInvoice);
        JSONObject object = XML.toJSONObject(soapInvoice);
        String jsonData = object.toString();
        System.out.println(jsonData);
        log.info("结束GetInvoice()方法");
        return CommonResult.success(jsonData);
    }


    /**
     * 2.10 获取迷你吧物品
     */
    @ApiOperation(value = "2.10 获取迷你吧物品")
    @RequestMapping(value = "/GetProducts", method = RequestMethod.GET)
    public CommonResult<?> GetProducts() {
        log.info("进入GetProducts()方法");
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        XRHotelServiceSoap soap = context.getBean(XRHotelServiceSoap.class);
        final String products = soap.getProducts();
        System.out.println(products);
        JSONObject object = XML.toJSONObject(products);
        String jsonData = object.toString();
        System.out.println(jsonData);
        log.info("结束GetProducts()方法");
        return CommonResult.success(jsonData);
    }


    /**
     * 2.11 迷你吧物品入账
     */
    @ApiOperation(value = "2.11 迷你吧物品入账")
    @RequestMapping(value = "/PostCharge", method = RequestMethod.GET)
    public CommonResult<?> PostCharge(String accnt, String code, String descript, String price, Integer number, String pcid) {
        log.info("进入PostCharge()方法");
        if (StringUtils.isEmpty(accnt) || StringUtils.isEmpty(code) || StringUtils.isEmpty(descript) || StringUtils.isEmpty(price) || StringUtils.isEmpty(pcid)) {
            log.error("异常访问PostCharge()方法");
            CommonResult.failed();
        }
        String tom = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<interface>\n" +
                "<item>\n" +
                "<accnt>" + accnt + "</accnt>\n" +
                "<code>" + code + "</code>\n" +
                "<descript>" + descript + "</descript>\n" +
                "<price>" + price + "</price>\n" +
                "<number>" + number + "</number>\n" +
                "<pcid>" + pcid + "</pcid>\n" +
                "</item>\n" +
                "</interface>";
        final XRHotelServiceSoap soap = XRHotels();
        final String charge = soap.postCharge(tom);
        System.out.println(charge);
        final String jsons = Jsons(charge);
        log.info("PostCharge()方法执行结束");
        return CommonResult.success(jsons);
    }


    /**
     * 2.12 获取客人信息
     */
    @ApiOperation(value = "2.12 获取客人信息")
    @RequestMapping(value = "/GetGuests")
    public CommonResult<?> GetGuests(String no, String name, String ident) {
        log.info("进入GetGuests()方法");
        if (StringUtils.isEmpty(no) & StringUtils.isEmpty(name) & StringUtils.isEmpty(ident)) {
            log.error("异常访问GetGuests()方法");
            return CommonResult.failed();
        }
        String tom = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<interface>\n" +
                "<item>\n" +
                "<no>" + no + "</no>\n" +
                "<name>" + name + "</name>\n" +
                "<ident>" + ident + "</ident>\n" +
                "</item>\n" +
                "</interface>";

        final XRHotelServiceSoap soap = XRHotels();
        final String guests = soap.getGuests(tom);
        final String jsons = Jsons(guests);
        System.out.println(jsons);
        log.info("结束GetGuests方法");
        return CommonResult.success(jsons);
    }


    /**
     * 2.13 获取协议单位
     */
    @ApiOperation(value = "2.13 获取协议单位")
    @RequestMapping(value = "/GetCompanies")
    public CommonResult<?> GetCompanies(String no, String name) {
        log.info("进入GetCompanies()方法");
        if (StringUtils.isEmpty(no) && StringUtils.isEmpty(name)) {
            log.error("异常访问GetCompanies()方法");
            return CommonResult.failed();
        }
        String tom = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<interface>\n" +
                "<item>\n" +
                "<no>" + no + "</no>\n" +
                "<name>" + name + "</name>\n" +
                "</item>\n" +
                "</interface>";
        final XRHotelServiceSoap soap = XRHotels();
        final String companies = soap.getCompanies(tom);
        final String jsons = Jsons(companies);
        log.info("进入GetCompanies()方法");
        return CommonResult.success(jsons);

    }


    /**
     * 2.14 获取协议单位 2
     */
    @ApiOperation(value = "2.14 获取协议单位 2")
    @RequestMapping(value = "/GetCompanies2")
    public CommonResult<?> GetCompanies2(String no, String name) {
        if (StringUtils.isEmpty(no) && StringUtils.isEmpty(name)) {
            return CommonResult.failed();
        }
        String tom = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<interface>\n" +
                "<item>\n" +
                "<no>" + no + "</no><name>" + name + "</name>\n" +
                "</item>\n" +
                "</interface>";
        final XRHotelServiceSoap soap = XRHotels();
        final String companies2 = soap.getCompanies2(tom);
        final String jsons = Jsons(companies2);
        return CommonResult.success(jsons);

    }


    /**
     * 2.15 获取迷你吧消费明细
     */
    @ApiOperation(value = "2.15获取迷你吧消费明细")
    @RequestMapping(value = "/GetBosbill")
    public CommonResult<?> GetBosbill(String accnt, String roomno) {
        if (StringUtils.isEmpty(accnt) && StringUtils.isEmpty(roomno)) {
            return CommonResult.failed();
        }
        String tom = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<interface>\n" +
                "<item>\n" +
                "<accnt>" + accnt + "</accnt>\n" +
                "<roomno>" + roomno + "</roomno></item>\n" +
                "</interface>";

        final XRHotelServiceSoap soap = XRHotels();
        final String bosbill = soap.getBosbill(tom);
        final String jsons = Jsons(bosbill);
        return CommonResult.success(jsons);

    }


    /**
     * 2.16 客人证件信息更新  – 只争对预定在住客人
     */
    @ApiOperation(value = "2.16 客人证件信息更新  – 只争对预定在住客人")
    @RequestMapping(value = "/UpdateGusetIDcode")
    public CommonResult<?> UpdateGusetIDcode(String accnt, String fname, String lname, String idcode, String idno, Integer sex,
                                             String birth, String address, String idend, String race, String city) {
        if (StringUtils.isEmpty(accnt)) {
            return CommonResult.failed();
        }
        String tom = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<interface>\n" +
                "<item>\n" +
                "<accnt>" + accnt + "</accnt>\n" +
                "<fname>" + fname + "</fname>\n" +
                "<lname>" + lname + "</lname>\n" +
                "<idcode>" + idcode + "</idcode>\n" +
                "<idno>" + idno + "</idno>\n" +
                "<sex>" + sex + "</sex><birth>" + birth + "</birth>\n" +
                "<address>" + address + "</address>\n" +
                "<idend>" + idend + "</idend>\n" +
                "<race>" + race + "</race>\n" +
                "<city>" + city + "</city>\n" +
                "</item>\n" +
                "</interface>";
        final XRHotelServiceSoap soap = XRHotels();
        final String dcode = soap.updateGusetIDcode(tom);
        final String jsons = Jsons(dcode);
        return CommonResult.success(jsons);

    }


    /**
     * 2.17 查房申请及查房状态查询
     */
    @ApiOperation(value = "2.17 查房申请及查房状态查询")
    @RequestMapping(value = "/Checkroom")
    public CommonResult<?> Checkroom(Integer code, String accnt, String roomno) {
        if (StringUtils.isEmpty(accnt) || StringUtils.isEmpty(roomno)) {
            return CommonResult.failed();
        }
        String tom = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<interface>\n" +
                "<item>\n" +
                "<code>" + code + "</code>\n" +
                "<accnt>" + accnt + "</accnt><roomno>" + roomno + "</roomno>\n" +
                "</item>\n" +
                "</interface>";

        final XRHotelServiceSoap soap = XRHotels();
        final String checkroom = soap.checkroom(tom);
        final String jsons = Jsons(checkroom);
        return CommonResult.success(jsons);

    }


    /**
     * 2.18  数据字典
     */
    @ApiOperation(value = "2.18 数据字典")
    @RequestMapping(value = "/DictionaryGet")
    public CommonResult<?> DictionaryGet(String cat) {
        if (StringUtils.isEmpty(cat)) {
            return CommonResult.failed();
        }
        String tom = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<interface>\n" +
                "<item>\n" +
                "<cat>" + cat + "</cat>\n" +
                "</item>\n" +
                "</interface>";
        final XRHotelServiceSoap soap = XRHotels();
        final String dictionaryGet = soap.dictionaryGet(tom);
        final String jsons = Jsons(dictionaryGet);
        System.out.println("jsons:"+jsons);
        return CommonResult.success(jsons);

    }

    /**
     * 国籍  code 转换
     */
    @ApiOperation(value = "国籍  code 转换 ")
    @RequestMapping(value = "/GetNationByCode", method = RequestMethod.GET)
    public CommonResult<?> GetNationByCode(String code) {
        log.info("GetCertificate()方法");
        if (StringUtils.isEmpty(code)){
            return CommonResult.failed("国籍转换失败");
        }
        Nation nation=nationService.getOne(new QueryWrapper<Nation>().eq("code",code));
        return CommonResult.success(nation);
    }

    /**
     * 2.19 黑名单客人信息查询
     */
    @ApiOperation(value = "2.19 黑名单客人信息查询")
    @RequestMapping(value = "/BlackGuest")
    public CommonResult<?> BlackGuest() {

        final XRHotelServiceSoap soap = XRHotels();
        final String guest = soap.blackGuest();
        final String jsons = Jsons(guest);
        System.out.println(jsons);
        return CommonResult.success(jsons);

    }


    /**
     * 2.20 客人主单特殊要求更新
     */
    @ApiOperation(value = "2.20 客人主单特殊要求更新")
    @RequestMapping(value = "/SrqsUpdate")
    public CommonResult<?> SrqsUpdate(String accnt, String srqs) {
        if (StringUtils.isEmpty(accnt)) {
            return CommonResult.failed();
        }

        String tom = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<interface>\n" +
                "<item>\n" +
                "<accnt>" + accnt + "</accnt>\n" +
                "<srqs>" + srqs + "</srqs>\n" +
                "</item>\n" +
                "</interface>";
        final XRHotelServiceSoap soap = XRHotels();
        final String update = soap.srqsUpdate(tom);
        final String jsons = Jsons(update);
        return CommonResult.success(jsons);

    }


    /**
     * 2.21 付款,预授权增加
     */
    @ApiOperation(value = "2.21付款,预授权增加")
    @RequestMapping(value = "/AccntPosting")
    public CommonResult<?> AccntPosting(String accnt, String feecode, String cardno, String expiry, String pccode,
                                        String amount, String foliono, String creditno) {
        String tom = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<interface>\n" +
                "<item>\n" +
                "<accnt>" + accnt + "</accnt>\n" +
                "<feecode>" + feecode + "</feecode>\n" +
                "</item>\n" +
                "<accredits>\n" +
                "<cardno>" + cardno + "</cardno>\n" +
                "<expiry>" + expiry + "</expiry>\n" +
                "<pccode>" + pccode + "</pccode>\n" +
                "<amount>" + amount + "</amount>\n" +
                "<foliono>" + foliono + "</foliono>\n" +
                "<creditno>" + creditno + "</creditno>\n" +
                "</accredits>\n" +
                "</interface>";
        System.out.println("time:"+new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        final XRHotelServiceSoap soap = XRHotels();
        final String posting = soap.accntPosting(tom);
        final String jsons = Jsons(posting);
        return CommonResult.success(jsons);

    }


    /**
     * 2.22 同住客人证件信息增加
     */
    @ApiOperation(value = "2.22 同住客人证件信息增加")
    @RequestMapping(value = "/AddGuest")
    public CommonResult<?> AddGuest(String accnt, String fname, String lname, String idcode, String idno,
                                    String sex, String birth, String address, String idend, String race, String city) {
        if (StringUtils.isEmpty(accnt)) {
            return CommonResult.failed();
        }
        String tom = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<interface>\n" +
                "  <item>\n" +
                "<accnt>" + accnt + "</accnt>\n";
                if (!StringUtils.isEmpty(fname)){
                    tom+="<fname>" + fname + "</fname>\n" ;
                }
                if (!StringUtils.isEmpty(lname)){
                    tom+="<lname>" + lname + "</lname>\n" ;
                }
                if (!StringUtils.isEmpty(idcode)){
                    tom+="<idcode>" + idcode + "</idcode>\n" ;
                }
                if (!StringUtils.isEmpty(idno)){
                    tom+="<idno>" + idno + "</idno>\n" ;;
                }
                if (!StringUtils.isEmpty(sex)){
                    tom+="<sex>" + sex + "</sex>\n" ;
                }
                if (!StringUtils.isEmpty(birth)){
                    tom+="<birth>" + birth + "</birth>\n" ;
                }
                if (!StringUtils.isEmpty(address)){
                    tom+="<address>" + address + "</address>\n" ;
                }
                if (!StringUtils.isEmpty(idend)){
                    tom+="<idend>" + idend + "</idend>\n" ;
                }
                if (!StringUtils.isEmpty(race)){
                    tom+="<race>" + race + "</race>\n" ;
                }
                if (!StringUtils.isEmpty(city)){
                    tom+="<city>" + city + "</city>\n";
                }
                tom+="  </item>\n" +
                "</interface>";

        final XRHotelServiceSoap soap = XRHotels();
        final String guest = soap.addGuest(tom);
        final String jsons = Jsons(guest);
        return CommonResult.success(jsons);

    }


    /**
     * 2.23 预授权信息查询
     */
    @ApiOperation(value = "2.23 预授权信息查询")
    @RequestMapping(value = "/GetAccredit")
    public CommonResult<?> GetAccredit(String accnt, String roomno) {
        if (StringUtils.isEmpty(accnt) && StringUtils.isEmpty(roomno)) {
            return CommonResult.failed();
        }
        String tom = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<interface>\n" +
                "  <item>\n" +
                "<accnt>" + accnt + "</accnt>\n" +
                "<roomno>" + roomno + "</roomno>\n" +
                "  </item>\n" +
                "</interface>";

        final XRHotelServiceSoap soap = XRHotels();
        final String accredit = soap.getAccredit(tom);
        final String jsons = Jsons(accredit);
        return CommonResult.success(jsons);

    }


    /**
     * 2.24自助预定
     */
    @ApiOperation(value = "2.24自助预定")
    @RequestMapping(value = "/Reservation")
    public CommonResult<?> Reservation(String s_time, String e_time, String roomtype, String roomno, String rmrate, String src,
                                       String market, String restype, String channel, String ratecode, String packages, String
                                               fname, String lname, String idcode, String idno, String sex, String birth, String address,
                                       String idend, String race, String city, String country, String mobile) {
        String tom = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<interface>\n" +
                "  <item>\n" +
                "<s_time>" + s_time + "</s_time>\n" +
                "<e_time>" + e_time + "</e_time>\n" +
                "<roomtype>" + roomtype + "</roomtype>\n" +
                "<roomno>" + roomno + "</roomno>\n" +
                "<rmrate>" + rmrate + "</rmrate>\n" +
                "<src>" + src + "</src>\n" +
                "<market>" + market + "</market>\n" +
                "<restype>" + restype + "</restype>\n" +
                "<channel>" + channel + "</channel>\n" +
                "<ratecode>" + ratecode + "</ratecode>\n" +
                "<packages>" + packages + "</packages>\n" +
                "<fname>" + fname + "</fname>\n" +
                "<lname>" + lname + "</lname>\n" +
                "<idcode>" + idcode + "</idcode>\n" +
                "<idno>" + idno + "</idno>\n" +
                "<sex>" + sex + "</sex>\n" +
                "<birth>" + birth + "</birth>\n" +
                "<address>" + address + "</address>\n" +
                "<idend>" + idend + "</idend>\n" +
                "<race>" + race + "</race>\n" +
                "<city>" + city + "</city>\n" +
                "<country>" + country + "</country>\n" +
                "    <mobile>" + mobile + "</mobile>\n" +
                "  </item>\n" +
                "</interface>";

        final XRHotelServiceSoap soap = XRHotels();
        final String s = soap.reservation(tom);
        final String jsons = Jsons(s);

        com.alibaba.fastjson.JSONObject object=JSON.parseObject(jsons);
        com.alibaba.fastjson.JSONObject resultObj=object.getJSONObject("response");
        String r=resultObj.getString("result");
        if ("0".equals(r)) {
            com.alibaba.fastjson.JSONObject items = resultObj.getJSONObject("items");
            Reservation reservation = items.toJavaObject(Reservation.class);
            OperationRecord operationRecord = new OperationRecord();
            operationRecord.setName(reservation.getName());
            operationRecord.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            operationRecord.setOperation("CREATE");
            operationRecord.setOperationDes("账号为:" + reservation.getAccnt() + "，姓名为:" + reservation.getName() + "的客人，在" + operationRecord.getCreateTime() + "在自助机生成订单。");
            operationRecord.setResno(reservation.getAccnt());
            operationRecordService.save(operationRecord);
        }
        return CommonResult.success(jsons);

    }


    /**
     * 2.25 通过房价码查询区间段房价信息
     */
    @ApiOperation(value = "2.25 通过房价码查询区间段房价信息")
    @RequestMapping(value = "/RoomPrice_Query")
    public CommonResult<?> RoomPrice_Query(String s_time, String e_time, String ratecode) {
        if (StringUtils.isEmpty(ratecode)) {
            return CommonResult.failed();
        }
        String tom = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<interface>\n" +
                "  <item>\n" +
                "<s_time>" + s_time + "</s_time>\n" +
                "<e_time>" + e_time + "</e_time>\n" +
                "<ratecode>" + ratecode + "</ratecode>\n" +
                "  </item>\n" +
                "</interface>";
        final XRHotelServiceSoap soap = XRHotels();
        final String query = soap.roomPriceQuery(tom);
        final String jsons = Jsons(query);
        return CommonResult.success(jsons);
    }


    /**
     * 2.26 查询客人关联房价码
     */
    @ApiOperation(value = "2.26 查询客人关联房价码")
    @RequestMapping(value = "/Ratecode_Query")
    public CommonResult<?> Ratecode_Query(String FV, String guestno, String name, String idno, String phone, String vipcard, String passwd) {

        String tom = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<interface>\n" +
                "  <item>\n" +
                "<class>" + FV + "</class>\n" +
                "<guestno>" + guestno + "</guestno>\n" +
                "<name>" + name + "</name>\n" +
                "<idno>" + idno + "</idno>\n" +
                "<phone>" + phone + "</phone>\n" +
                "<vipcard>" + vipcard + "</vipcard>\n" +
                "<passwd>" + passwd + "</passwd>\n" +
                "  </item>\n" +
                "</interface>";
        final XRHotelServiceSoap soap = XRHotels();
        final String query = soap.ratecodeQuery(tom);
        final String jsons = Jsons(query);
        return CommonResult.success(jsons);

    }


    /**
     * 2.27 预定客人取消
     */
    @ApiOperation(value = "2.27 预定客人取消")
    @RequestMapping(value = "/Reservation_Chancel")
    public CommonResult<?> Reservation_Chancel(String accnt) {
        if (StringUtils.isEmpty(accnt)) {
            return CommonResult.failed();
        }
        String tom = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<interface>\n" +
                "  <item>\n" +
                "<accnt>" + accnt + "</accnt>\n" +
                "  </item>\n" +
                "</interface>";
        final XRHotelServiceSoap soap = XRHotels();
        final String chance = soap.reservationChancel(tom);
        final String jsons = Jsons(chance);
        return CommonResult.success(jsons);

    }


    /**
     * 2.28修改客人离店时间
     */
    @ApiOperation(value = "2.28修改客人离店时间")
    @RequestMapping(value = "/Modify_Dep")
    public CommonResult<?> Modify_Dep(String accnt, String dep, String feecode, String cardno, String expiry, String pccode,
                                      String amount, String foliono, String creditno) {
        String tom = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<interface>\n" +
                "  <item>\n" +
                "<accnt>" + accnt + "</accnt>\n" +
                "<dep>" + dep + "</dep>\n" +
                "<feecode>" + feecode + "</feecode> \n" +
                "  </item>\n" +
                "  <accredits>\n" +
                "    <cardno>" + cardno + "</cardno>\n" +
                "    <expiry>" + expiry + "</expiry>\n" +
                "    <pccode>" + pccode + "</pccode>\n" +
                "<amount>" + amount + "</amount>\n" +
                "<foliono>" + foliono + "</foliono> \n" +
                "<creditno>" + creditno + "</creditno>  \n" +
                "  </accredits>\n" +
                "</interface>";

        final XRHotelServiceSoap soap = XRHotels();
        final String s = soap.modifyDep(tom);
        final String jsons = Jsons(s);

        //订单数据留存
        Reservation reservation=reservationService.getOne(new QueryWrapper<Reservation>().eq("accnt",accnt));
        Reservation newReservation= GetOneReservation(accnt);
        if (reservation==null){
            reservationService.save(newReservation);
            System.out.println(newReservation);
        }else{
            newReservation.setId(reservation.getId());
            reservationService.updateById(newReservation);
            System.out.println(newReservation);
        }

        OperationRecord operationRecord=new OperationRecord();
        operationRecord.setName(newReservation.getName());
        operationRecord.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        operationRecord.setOperation("UPDATE");
        operationRecord.setOperationDes("账号为:"+newReservation.getAccnt()+"，姓名为:"+newReservation.getName()+"的客人，在"+operationRecord.getCreateTime()+"在自助机续住订单。");
        operationRecord.setResno(newReservation.getAccnt());
        operationRecordService.save(operationRecord);


        return CommonResult.success(jsons);

    }

    private final String stayAlertQueue = "stayAlertQueue";
    /**
     * 客人续住 提醒前台 ，某个房间续住提醒
     */
    @ApiOperation(value = "客人续住 提醒前台 ，某个房间续住提醒")
    @RequestMapping(value = "/StayAlert")
    public void StayAlert(String roomNo){
        String str = roomNo + "房间已在自助机续住,请前往房间打扫处理！";
        rabbitHelper.startThread(this.rabbitTemplate,stayAlertQueue,str);
    }



    /**
     * 2.29 钟点房信息查询
     */
    @ApiOperation(value = "2.29 钟点房信息查询")
    @RequestMapping(value = "/HrsQuerry")
    public CommonResult<?> HrsQuerry() {
        final XRHotelServiceSoap soap = XRHotels();
        final String s = soap.hrsQuerry();
        final String jsons = Jsons(s);
        return CommonResult.success(jsons);

    }


    /**
     * 2.30 钟点房  自助预定
     */
    @ApiOperation(value = "2.30 钟点房 自助预定")
    @RequestMapping(value = "/HrsReservation")
    public CommonResult<?> HrsReservation(String s_time, String e_time, String roomtype, String roomno, String fname, String lname,
                                          String idcode, String idno, String sex, String birth, String address, String idend, String race, String city,
                                          String country, String mobile) {
        String tom = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<interface>\n" +
                "  <item>\n" +
                "<s_time>" + s_time + "</s_time>\n" +
                "<e_time>" + e_time + "</e_time>\n" +
                "<roomtype>" + roomtype + "</roomtype>\n" +
                "<roomno>" + roomno + "</roomno>\n" +
                "<fname>" + fname + "</fname>\n" +
                "<lname>" + lname + "</lname>\n" +
                "<idcode>" + idcode + "</idcode>\n" +
                "<idno>" + idno + "</idno>\n" +
                "<sex>" + sex + "</sex>\n" +
                "<birth>" + birth + "</birth>\n" +
                "<address>" + address + "</address>\n" +
                "<idend>" + idend + "</idend>\n" +
                "<race>" + race + "</race>\n" +
                "<city>" + city + "</city>\n" +
                "<country>" + country + "</country>\n" +
                "    <mobile>" + mobile + "</mobile>\n" +
                "  </item>\n" +
                "</interface>";

        final XRHotelServiceSoap soap = XRHotels();
        final String reservation = soap.hrsReservation(tom);
        final String jsons = Jsons(reservation);
        return CommonResult.success(jsons);

    }


    /**
     * 2.31 钟点房退房房价查询
     */
    @ApiOperation(value = "2.31 ")
    @RequestMapping(value = "/AccntHrsRmrate")
    public CommonResult<?> AccntHrsRmrate(String flag, String accnt) {
        if (StringUtils.isEmpty(flag)) {
            return CommonResult.failed();
        }

        String tom = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<interface>\n" +
                "  <item>\n" +
                "    <flag>" + flag + "</flag>\n" +
                "    <accnt>" + accnt + "</accnt>\n" +
                "  </item>\n" +
                "</interface>";
        final XRHotelServiceSoap soap = XRHotels();
        final String rmrate = soap.accntHrsRmrate(tom);
        final String jsons = Jsons(rmrate);
        return CommonResult.success(jsons);

    }


    /**
     * 2.32 酒店完整客房资源查询
     */
    @ApiOperation(value = "2.32 酒店完整客房资源查询")
    @RequestMapping(value = "/RoomResource_Query")
    public CommonResult<?> RoomResource_Query(String flag) {
        if (StringUtils.isEmpty(flag)) {
            return CommonResult.failed();
        }
        String tom = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<interface>\n" +
                "  <item>\n" +
                "<flag>" + flag + "</flag>\n" +
                "  </item>\n" +
                "</interface>";
        final XRHotelServiceSoap soap = XRHotels();
        final String query = soap.roomResourceQuery(tom);
        final String jsons = Jsons(query);
        System.out.println(jsons);
        return CommonResult.success(jsons);

    }


    /**
     * 2.33
     */
    @ApiOperation(value = "2.33 临时锁房和解除锁房")
    @RequestMapping(value = "/Tempsta_Set")
    public CommonResult<?> Tempsta_Set(String roomno, String tag) {
        if (StringUtils.isEmpty(roomno) && StringUtils.isEmpty(tag)) {
            return CommonResult.failed();
        }
        String tom = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<interface>\n" +
                "  <item>\n" +
                "<roomno>" + roomno + "</roomno>\n" +
                "<tag>" + tag + "</tag>\n" +
                "  </item>\n" +
                "</interface>";

        final XRHotelServiceSoap soap = XRHotels();
        final String tempstaSet = soap.tempstaSet(tom);
        final String jsons = Jsons(tempstaSet);
        return CommonResult.success(jsons);
    }


    /**
     * 2.34 获取会员客人信息
     */
    @ApiOperation(value = "2.34 获取会员客人信息")
    @RequestMapping(value = "/GetVipGuests")
    public CommonResult<?> GetVipGuests(String no, String name, String ident, String mobile, String cardno) {
        if (StringUtils.isEmpty(no) && StringUtils.isEmpty(name) && StringUtils.isEmpty(ident) && StringUtils.isEmpty(mobile) && StringUtils.isEmpty(cardno)) {
            return CommonResult.failed();
        }
        String tom = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<interface>\n" +
                "  <item>\n" +
                "    <no>" + no + "</no>\n" +
                "    <name>" + name + "</name>\n" +
                "<ident>" + ident + "</ident>\n" +
                "     <mobile>" + mobile + "</mobile>\n" +
                "    <cardno>" + cardno + "</cardno>\n" +
                "  </item>\n" +
                "</interface>";
        final XRHotelServiceSoap soap = XRHotels();
        final String guests = soap.getVipGuests(tom);
        final String jsons = Jsons(guests);
        return CommonResult.success(jsons);
    }


    /**
     * 2.35 查询房间房态
     */
    @ApiOperation(value = "2.35 查询房间房态")
    @RequestMapping(value = "/RoomSta_Querry")
    public CommonResult<?> RoomSta_Querry(String RoomType, String RoomNumber) {
        if (StringUtils.isEmpty(RoomType) && StringUtils.isEmpty(RoomNumber)) {
            return CommonResult.failed();
        }

        String tom = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<interface>\n" +
                "  <item>\n" +
                "    <RoomType>" + RoomType + "</RoomType>\n" +
                "<RoomNumber>" + RoomNumber + "</RoomNumber>\n" +
                "</item>\n" +
                "</interface>";

        final XRHotelServiceSoap soap = XRHotels();
        final String querry = soap.roomStaQuerry(tom);
        final String jsons = Jsons(querry);
        return CommonResult.success(jsons);

    }


    /**
     * 2.36  发票信息绑定
     */
    @ApiOperation(value = "2.36  发票信息绑定")
    @RequestMapping(value = "/Invoice_Binging")
    public CommonResult<?> Invoice_Binging(String accnt, String purchasername, String taxno, String bankno_accnt, String add_tel, String remark) {
        if (StringUtils.isEmpty(purchasername) || StringUtils.isEmpty(taxno)) {
            return CommonResult.failed();
        }
        String tom = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<interface>\n" +
                "  <item>\n" +
                "    <accnt>" + accnt + "</ accnt >\n" +
                "\t< purchasername>" + purchasername + "</purchasername>\n" +
                "\t<taxno>" + taxno + "</taxno>\n" +
                "\t< bankno_accnt>" + bankno_accnt + "</bankno_accnt>\n" +
                "<add_tel>" + add_tel + "</add_tel>\n" +
                "\t< remark>" + remark + "</ remark>\n" +
                "</item>\n" +
                "</interface>";
        final XRHotelServiceSoap soap = XRHotels();
        final String binging = soap.invoiceBinging(tom);
        final String jsons = Jsons(binging);
        return CommonResult.success(jsons);

    }


    /**
     * 2.37 退房不结账
     */
    @ApiOperation(value = "2.37 退房不结账")
    @RequestMapping(value = "/GuestCheckOut_Nobill")
    public CommonResult<?> GuestCheckOut_Nobill(String accnt, String masterremark, String billremark, String cardno, String expiry, String pccode,
                                                String amount, String foliono) {
        if (StringUtils.isEmpty(accnt)) {
            return CommonResult.failed();
        }
        String tom = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<interface>\n" +
                "  <item>\n" +
                "<accnt>" + accnt + "</accnt>\n" +
                "<masterremark>" + masterremark + "</masterremark>\n" +
                "<billremark>" + billremark + "</billremark>\n" +
                "  </item>\n" +
                "  <postings>\n" +
                "    <cardno>" + cardno + "</cardno>\n" +
                "    <expiry>" + expiry + "</expiry>\n" +
                "    <pccode>" + pccode + "</pccode>\n" +
                "<amount>" + amount + "</amount>\n" +
                "<foliono>" + foliono + "</foliono>\n" +
                "  </postings>\n" +
                "</interface>";

        final XRHotelServiceSoap soap = XRHotels();
        final String nobill = soap.guestCheckOutNobill(tom);
        final String jsons = Jsons(nobill);

        //订单数据留存
        Reservation reservation=reservationService.getOne(new QueryWrapper<Reservation>().eq("accnt",accnt));
        Reservation newReservation= GetOneReservation(accnt);
        if (reservation==null){
            reservationService.save(newReservation);
            System.out.println(newReservation);
        }else{
            newReservation.setId(reservation.getId());
            reservationService.updateById(newReservation);
            System.out.println(newReservation);
        }

        OperationRecord operationRecord=new OperationRecord();
        operationRecord.setName(newReservation.getName());
        operationRecord.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        operationRecord.setOperation("CHECKOUT");
        operationRecord.setOperationDes("账号为:"+newReservation.getAccnt()+"，姓名为:"+newReservation.getName()+"的客人，在"+operationRecord.getCreateTime()+"在自助机办理退房。");
        operationRecord.setResno(newReservation.getAccnt());
        operationRecordService.save(operationRecord);

        return CommonResult.success(jsons);

    }


    /**
     * 2.38  获取订单信息(包含订单状态)
     */
    @ApiOperation(value = "/2.38  获取订单信息(包含订单状态)")
    @RequestMapping(value = "/GetReservation_Details")
    public CommonResult<?> GetReservation_Details(String resno, String accnt, String fullname, String ident, String mobile, String roomno) {
        String tom = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<interface>\n" +
                "<item>\n" ;
                if(!StringUtils.isEmpty(resno)){
                    tom+="<resno>" + resno + "</resno>\n" ;
                }
                if(!StringUtils.isEmpty(accnt)){
                    tom+="<accnt>" + accnt + "</accnt>\n" ;
                }
                if(!StringUtils.isEmpty(fullname)){
                    tom+="     <fullname>" + fullname + "</fullname>\n" ;
                }
                if(!StringUtils.isEmpty(ident)){
                    tom+="     <ident>" + ident + "</ident>\n" ;
                }
                if(!StringUtils.isEmpty(mobile)){
                    tom+="     <mobile>" + mobile + "</mobile>\n" ;
                }
                if(!StringUtils.isEmpty(roomno)){
                    tom+="<roomno>" + roomno + "</roomno>\n";
                }
                tom+=
                "</item>\n" +
                "</interface>";
        final XRHotelServiceSoap soap = XRHotels();
        final String details = soap.getReservationDetails(tom);
        final String jsons = Jsons(details);
        return CommonResult.success(jsons);

    }


    /**
     * 2.39 发卡记录表数据更新及查询
     */
    @ApiOperation(value = "2.39 发卡记录表数据更新及查询")
    @RequestMapping(value = "/DoorcardRecord_Chg")
    public CommonResult<?> DoorcardRecord_Chg(String sta, String accnt, String roomno, String begin_, String end_, String encoder,
                                              String card_type, String card_t, String cardno1, String cardno2, String flag1, String flag2) {
        if (StringUtils.isEmpty(accnt) && StringUtils.isEmpty(roomno)) {
            return CommonResult.failed();
        }
        String tom = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<interface>\n" +
                "<item>\n" +
                "\t<sta>" + sta + "</sta>\n" +
                "\t<accnt>" + accnt + "</accnt>\n" +
                "     <roomno>" + roomno + "</roomno>\n" +
                "     <begin_>" + begin_ + "</begin_>\n" +
                "     <end_>" + end_ + "</end_>\n" +
                "\t<encoder>" + encoder + "</encoder>\n" +
                "\t<card_type>" + card_type + "</card_type>\n" +
                "\t<card_t>" + card_t + "</card_t>\n" +
                "\t<cardno1>" + cardno1 + "</cardno1>\n" +
                "\t<cardno2>" + cardno2 + "</cardno2>\n" +
                "\t<flag1>" + flag1 + "</flag1>\n" +
                "\t<flag2>" + flag2 + "</flag2>\n" +
                "\t</item>\n" +
                "</interface>";
        final XRHotelServiceSoap soap = XRHotels();
        final String chg = soap.doorcardRecordChg(tom);
        final String jsons = Jsons(chg);
        com.alibaba.fastjson.JSONObject jj=JSON.parseObject(jsons);
        System.out.println("json:"+jsons);
        System.out.println("result:"+jj.get("result"));
        return CommonResult.success(jsons);

    }


    /**
     * 2.40  客人联系方式更新（可扩展）
     */
    @ApiOperation(value = "2.40  客人联系方式更新（可扩展）")
    @RequestMapping(value = "/ContactUpdate")
    public CommonResult<?> ContactUpdate(String accnt, String type, String values) {

        String tom = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<interface>\n" +
                "<item>\n" +
                "\t<accnt>" + accnt + "</accnt>\n" +
                "     <type>" + type + "</type>\n" +
                "     <values>" + values + "</values>\n" +
                "</item>\n" +
                "</interface>";
        final XRHotelServiceSoap soap = XRHotels();
        final String update = soap.contactUpdate(tom);
        final String jsons = Jsons(update);
        return CommonResult.success(jsons);

    }


    /**
     * 2.41 迷你吧入账(支持同账号多笔明细)
     */
    @ApiOperation(value = "2.14 迷你吧入账(支持同账号多笔明细)")
    @PostMapping(value = "/PostCharge_New")
    public CommonResult<?> PostCharge_New(@RequestBody MiniProduct miniProduct) {
        System.out.println("size:"+miniProduct.getMiniProList().size());
        String tom = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<interface>\n" +
                "  <item>\n" +
                "    <accnt>" + miniProduct.getAccnt() + "</accnt>\n" +
                "    <pccode>" + miniProduct.getPccode() + "</pccode>\n" +
                "    <pcid>" + miniProduct.getPcid() + "</pcid>\n" +
                "  </item>\n" ;
        for (int i=0;i<miniProduct.getMiniProList().size();i++){
            tom+="  <bos_items>\n" +
                    "    <code>" + miniProduct.getMiniProList().get(i).getCode() + "</code>\n" +
                    "    <descript>" + miniProduct.getMiniProList().get(i).getDescript() + "</descript>\n" +
                    "    <price>" + miniProduct.getMiniProList().get(i).getPrice() + "</price>\n" +
                    "    <number>" + miniProduct.getMiniProList().get(i).getNumber() + "</number>\n" +
                    "    <unit>" + miniProduct.getMiniProList().get(i).getUnit() + "</unit>\n" +
                    "  </bos_items>\n";
        }
        tom+="</interface>";
        System.out.println("tom:"+tom);
        final XRHotelServiceSoap soap = XRHotels();
        final String aNew = soap.postChargeNew(tom);
        final String jsons = Jsons(aNew);
        return CommonResult.success(jsons);

    }


    /**
     * 2.42 获取账单一新
     */
    @ApiOperation(value = "2.42 获取账单一新")
    @RequestMapping(value = "/GetInvoice_New")
    public CommonResult<?> GetInvoice_New(String accnt, String scope) {
        if (StringUtils.isEmpty(accnt) && StringUtils.isEmpty(scope)) {
            return CommonResult.failed();
        }
        String tom = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<interface>\n" +
                "  <item>\n" +
                "    <accnt>" + accnt + "</accnt>\n" +
                "    <scope>" + scope + "</scope>\n" +
                "  </item>\n" +
                "</interface>";

        final XRHotelServiceSoap soap = XRHotels();
        final String aNew = soap.getInvoiceNew(tom);
        final String jsons = Jsons(aNew);
        return CommonResult.success(jsons);

    }


    /**
     * 2.43 主单拆分
     */
    @ApiOperation(value = "2.43 主单拆分")
    @RequestMapping(value = "/MaterSplit")
    public CommonResult<?> MaterSplit(String accnt) {

        String tom = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<interface>\n" +
                "  <item>\n" +
                "    <accnt>" + accnt + "</accnt>\n" +
                "  </item>\n" +
                "</interface>";

        final XRHotelServiceSoap soap = XRHotels();
        final String split = soap.materSplit(tom);
        final String jsons = Jsons(split);
        return CommonResult.success(jsons);

    }


    /**
     * 2.44 同住客整体退房
     */
    @ApiOperation(value = "2.44 同住客整体退房")
    @RequestMapping(value = "/GuestRemoteCheckOut1")
    public CommonResult<?> GuestRemoteCheckOut1(String resno,String accnt,
                                                String masterremark, String billremark,
                                                String cardno,
                                                String expiry, String pccode, String
            amount, String foliono) {

        String tom = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<interface>\n" +
                "  <item>\n" +
                "<accnt>" + accnt + "</accnt>\n" +
                "<masterremark>" + masterremark + "</masterremark> \n" +
                "<billremark>" + billremark + "</billremark> \n" +
                "  </item>\n" +
                "  <postings>\n" +
                "    <cardno>" + cardno + "</cardno>\n" +
                "    <expiry>" + expiry + "</expiry>\n" +
                "    <pccode>" + pccode + "</pccode>\n" +
                "<amount>" + amount + "</amount>\n" +
                "<foliono>" + foliono + "</foliono>\n" +
                "  </postings>\n" +
                "</interface>";

        final XRHotelServiceSoap soap = XRHotels();
        final String check = soap.guestRemoteCheckOut1(tom);
        final String jsons = Jsons(check);
        //订单数据留存
        Reservation reservation=reservationService.getOne(new QueryWrapper<Reservation>().eq("accnt",accnt));
        System.out.println("reservation:"+reservation);
        Reservation newReservation= GetOneReservation(accnt);
        System.out.println("newReservation:"+newReservation);
        System.out.println();
        if (reservation==null){
            reservationService.save(newReservation);
            System.out.println(newReservation);
        }else{
            newReservation.setId(reservation.getId());
            reservationService.updateById(newReservation);
        }

        OperationRecord operationRecord=new OperationRecord();
        operationRecord.setName(newReservation.getName());
        operationRecord.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        operationRecord.setOperation("CHECKOUT");
        operationRecord.setOperationDes("账号为:"+newReservation.getAccnt()+"，姓名为:"+newReservation.getName()+"的客人，在"+operationRecord.getCreateTime()+"在自助机办理退房。");
        operationRecord.setResno(newReservation.getAccnt());
        operationRecordService.save(operationRecord);

        return CommonResult.success(jsons);

    }


    /**
     * 2.46 团队订单基本信息查询
     */
    @ApiOperation(value = "2.46 团队订单基本信息查询")
    @RequestMapping(value = "/GetGroupRes_Details")
    public CommonResult<?> GetGroupRes_Details(String resno, String accnt, String fullname, String mobile, String applname) {

        String tom = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<interface>\n" +
                "  <item>\n" +
                "    <resno>" + resno + "</resno>\n" +
                "    <accnt>" + accnt + "</accnt>\n" +
                "    <fullname>" + fullname + "</fullname>\n" +
                "    <mobile>" + mobile + "</mobile>\n" +
                "    <applname>" + applname + "</applname>\n" +
                "  </item>\n" +
                "</interface>";

        final XRHotelServiceSoap soap = XRHotels();
        final String details = soap.getGroupResDetails(tom);
        final String jsons = Jsons(details);
        com.alibaba.fastjson.JSONObject jsonObject=JSON.parseObject(jsons);
        return CommonResult.success(jsonObject);

    }


    /**
     * 2.47 拆分团队纯预留
     */
    @ApiOperation(value = "2.47 拆分团队纯预留")
    @RequestMapping(value = "/GroupRsv_Split")
    public CommonResult<?> GroupRsv_Split(String accnt, String id, String num) {

        String tom = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<interface>\n" +
                "  <item>\n" +
                "    <accnt>" + accnt + "</accnt>\n" +
                "    <id>" + id + "</id>\n" +
                "    <num>" + num + "</num>\n" +
                "  </item>\n" +
                "</interface>";

        final XRHotelServiceSoap soap = XRHotels();
        final String split = soap.groupRsvSplit(tom);
        final String jsons = Jsons(split);
        return CommonResult.success(jsons);

    }


    /**
     * 2.48 预订单客房免费升级
     */
    @ApiOperation(value = "2.48 预订单客房免费升级")
    @RequestMapping(value = "/RoomUpgrade_Free")
    public CommonResult<?> RoomUpgrade_Free(String accnt, String new_roomtype, String old_roomtype) {
        if (StringUtils.isEmpty(accnt)) {
            return CommonResult.failed();
        }
        String tom = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<interface>\n" +
                "  <item>\n" +
                "    <accnt>" + accnt + "</accnt>\n" +
                "    <new_roomtype>" + new_roomtype + "</new_roomtype>\n" +
                "    <old_roomtype>" + old_roomtype + "</old_roomtype>\n" +
                "  </item>\n" +
                "</interface>";
        final XRHotelServiceSoap soap = XRHotels();
        final String free = soap.roomUpgradeFree(tom);
        final String jsons = Jsons(free);
        return CommonResult.success(jsons);

    }


    /**
     * 2.49 查询订单挂账基本信息
     */
    @ApiOperation(value = "2.49 查询订单挂账基本信息")
    @RequestMapping(value = "/Subaccnt_Query")
    public CommonResult<?> Subaccnt_Query(String accnt) {

        String tom = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<interface>\n" +
                "  <item>\n" +
                "    <accnt>" + accnt + "</accnt>\n" +
                "  </item>\n" +
                "</interface>";

        final XRHotelServiceSoap soap = XRHotels();
        final String query = soap.subaccntQuery(tom);
        final String jsons = Jsons(query);
        return CommonResult.success(jsons);

    }


    /**
     * 2.50 城市挂账及转账
     */
    @ApiOperation(value = "2.50 城市挂账及转账")
    @RequestMapping(value = "/CityLedger_Transfer")
    public CommonResult<?> CityLedger_Transfer(String accnt, String pc_id, String numbers, String cardno, String expiry, String pccode, String
            amount, String foliono, String toaccnt) {

        String tom = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<interface>\n" +
                "  <item>\n" +
                "<accnt>" + accnt + "</accnt>\n" +
                "<pc_id>" + pc_id + "</pc_id> \n" +
                "<numbers>" + numbers + "</numbers> \n" +
                "  </item>\n" +
                "  <postings>\n" +
                "    <cardno>" + cardno + "</cardno>\n" +
                "    <expiry>" + expiry + "</expiry>\n" +
                "    <pccode>" + pccode + "</pccode>\n" +
                "<amount>" + amount + "</amount>\n" +
                "<foliono>" + foliono + "</foliono>\n" +
                "<toaccnt>" + toaccnt + "</toaccnt>  \n" +
                "  </postings> \n" +
                "</interface>";
        final XRHotelServiceSoap soap = XRHotels();
        final String transfer = soap.cityLedgerTransfer(tom);
        final String jsons = Jsons(transfer);
        return CommonResult.success(jsons);

    }


    /**
     * 2.51 客人头像,签名及附件上传
     */
    @ApiOperation(value = "2.51 客人头像,签名及附件上传")
    @RequestMapping(value = "Upload_BmpSignBill")
    public CommonResult<?> Upload_BmpSignBill(String accnt, String guestno, String filetype, String filetypedes, String datatype, String data) {
        if (StringUtils.isEmpty(data)) {
            return CommonResult.failed();
        }
        final String imageStr = Base64Img.GetImageStr(data);
        String tom = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<interface>\n" +
                "  <item>\n" +
                "<accnt>" + accnt + "</accnt>\n" +
                "<guestno>" + guestno + "</guestno> \n" +
                "<filetype>" + filetype + "</filetype>\n" +
                "    <filetypedes>" + filetypedes + "</filetypedes>\n" +
                "    <datatype>" + datatype + "</datatype>\n" +
                "<data>" + imageStr + "</data>\n" +
                "  </item>\n" +
                "</interface>";

        final XRHotelServiceSoap soap = XRHotels();
        final String s = soap.uploadBmpSignBill(tom);
        final String jsons = Jsons(s);
        return CommonResult.success(jsons);

    }


    /**
     * 2.52  扫码付款,二维码付款等在线支付
     */
    @ApiOperation(value = "2.52 扫码付款,二维码付款等在线支付")
    @RequestMapping(value = "/Pay_Online") //TODO
    public CommonResult<?> Pay_Online() {


        return null;
    }


    /**
     * 2.53 在线支付结果查询
     */
    @ApiOperation(value = "2.53 在线支付结果查询")
    @RequestMapping(value = "/Pay_Online_Querry")
    public CommonResult<?> Pay_Online_Querry(String updateid) {
        String tom = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<interface>\n" +
                "  <item>\n" +
                "    <updateid>" + updateid + "</updateid>\n" +
                "  </item>\n" +
                "</interface>";
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            log.error("Pay_Online_Querry()方法异常");
            return CommonResult.failed();
        }
        final XRHotelServiceSoap soap = XRHotels();
        final String querry = soap.payOnlineQuerry(tom);
        final String jsons = Jsons(querry);
        return CommonResult.success(jsons);

    }


    /**
     * 2.54 批量查询自助机订单
     */
    @ApiOperation(value = "2.4 批量查询自助机订单")
    @RequestMapping(value = "/GetReservation")
    public CommonResult<?> GetReservation(String querry_type, String values) {
        if (StringUtils.isEmpty(querry_type)) {
            return CommonResult.failed();
        }
        String tom = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<interface>\n" +
                "  <item>\n" +
                "<querry_type>" + querry_type + "</querry_type>\n" +
                " <values>" + values + "</values> \n" +
                "  </item>\n" +
                "</interface>";
        System.out.println(tom);
        final XRHotelServiceSoap soap = XRHotels();
        final String reservation = soap.getReservation(tom);
        final String jsons = Jsons(reservation);
        return CommonResult.success(jsons);

    }


}
