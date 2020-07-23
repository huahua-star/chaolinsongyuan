package com.controller;

import TTCEPackage.K7X0Util;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.entity.TblTxnp;
import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;
import com.service.ITblTxnpService;
import com.utils.DaPuWeiDa;
import com.utils.EmailUtil;
import com.utils.Http.HttpUtil;
import com.utils.ReturnNullOrKong;
import com.utils.Returned2.AutoLog;
import com.utils.Returned2.SetResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import com.utils.Returned2.Result;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

import java.util.*;

import static TTCEPackage.K7X0Util.check;


/**
 * 卡Controller
 */
@Slf4j
@Api(tags = "制读卡")
@RestController
@RequestMapping("/zzj/card")
public class CardController {

    @Value("${sdk.ComHandle}")
    private Integer comHandle;


    @Value("${print.phone}")
    private String phone;
    @Value("${print.leave}")
    private String leave;
    @Value("${dapuPort}")
    private int dapuPort;
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

    @Value("${Email.cardEmail}")
    private String cardEmail;

    @Autowired
    private RabbitHelper rabbitHelper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private final String cardQueue = "cardQueue";
    @Autowired
    private ITblTxnpService tblTxnpService;



    /**
     * 发送自助机无卡邮件
     */
    @ApiOperation(value = "发送自助机无卡邮件")
    @RequestMapping(value = "/sendEmailNoCard", method = RequestMethod.GET)
    public Result<?> sendEmailNoCard(String TO) {
        String[] TOS=TO.split(",");
        EmailUtil.send("自助机无卡，请快速补充！",
                HOST,FROM,"",AFFIXNAME,USER,PWD,"无卡通知",TOS);
        return Result.ok("成功");
    }


    @RequestMapping("/testCardRabbit")
    public void testCardRabbit() {
        String cardStr = "设备即将无卡请检测。";
        rabbitHelper.startThread(this.rabbitTemplate,cardQueue,cardStr);
    }


    @GetMapping(value = "/checks")
    public Result<Object> checks() {
        Result<Object> result = new Result<>();
        log.info("check()方法");
        try {
            // 打开发卡机
            log.info("检测发卡机是否有卡");
            K7X0Util.open(comHandle);
            // 检测发卡机是否预空
            boolean isEmpty = check(2, 0x31);
            if (isEmpty) {
                log.info("发卡机卡箱预空,即将无卡");
            }
            // 检测发卡机是否有卡
            isEmpty = check(3, 0x38);
            if (isEmpty) {
                log.info("sendCard()方法结束return:{卡箱已空}");
                sendEmailNoCard(cardEmail);
                return SetResultUtil.setErrorMsgResult(result, "发卡机卡箱已空");
            }
            log.info("check()方法结束");
            return SetResultUtil.setSuccessResult(result, "有卡");
        } catch (Exception e) {
            log.error("sendCard()出现异常error:{}", e.getMessage());
            K7X0Util.regain();
            return SetResultUtil.setExceptionResult(result, "sendCard");
        }
    }


    /**
     * 发卡
     */
    @ApiOperation(value = "发卡", notes = "发卡-sendCard")
    @GetMapping(value = "/sendCard")
    public Result<Object> sendCard(int num, String roomno, String begin_,
                                   String end_, boolean flag,String lastName) {
        Result<Object> result = new Result<>();
        log.info("进入sendCard()方法");
        try {
            // 打开发卡机
            log.info("检测发卡机是否有卡");
            K7X0Util.open(comHandle);
            // 检测发卡机是否预空
            boolean isEmpty = check(2, 0x31);
            if (isEmpty) {
                String str = "发卡机卡箱预空,即将无卡";
                log.info("发卡机卡箱预空,即将无卡");
                rabbitHelper.startThread(this.rabbitTemplate,cardQueue,str);
            }
            // 检测发卡机是否有卡
            isEmpty = check(3, 0x38);
            if (isEmpty) {
                // 发卡失败
                log.info("发卡失败失败数据加入数据库中");
                log.info("sendCard()方法结束return:{卡箱已空}");
                sendEmailNoCard(cardEmail);
                return SetResultUtil.setErrorMsgResult(result, "发卡机卡箱已空");
            }
            while (check(3, 0x31)) {
                System.out.println("##########有卡未取出");
                return SetResultUtil.setExceptionResult(result, "读卡位置有卡未取出");
            }
            for (int i = 0; i < num; i++) {
                System.out.println("发送到读卡位置");
                K7X0Util.sendToRead(comHandle);
                /**
                 * 写卡
                 */
                Result<Object> writeResult=null;
                if (i==0&&flag){
                    writeResult=NewKeyCard(roomno,begin_,end_);
                }else{
                    writeResult=DuplicateKeyCard(roomno,begin_,end_);
                }
                if (writeResult.getCode()==200){
                    //写卡成功
                    //发送到收卡位置
                    K7X0Util.sendCardToTake(comHandle);
                    log.info("打印小票需要的数据");
                    //打印小票无 早餐数据
                    DaPuWeiDa.printCheckIn(dapuPort,roomno,leave,lastName,phone,end_.substring(0,10));
                    Thread.currentThread().sleep(1000);
                }else{
                    K7X0Util.regainCard(comHandle);
                    return SetResultUtil.setExceptionResult(result, "写卡失败");
                }
            }
            log.info("sendCard()方法结束");
            return SetResultUtil.setSuccessResult(result, "发卡成功");
        } catch (Exception e) {
            log.error("sendCard()出现异常error:{}", e.getMessage());
            K7X0Util.regain();
            return SetResultUtil.setExceptionResult(result, "sendCard");
        }
    }

    /**
     * 不打印发卡
     */
    @ApiOperation(value = "发卡", notes = "发卡-sendCardNoPaper")
    @GetMapping(value = "/sendCardNoPaper")
    public Result<Object> sendCardNoPaper(int num, String roomno, String begin_, String end_,boolean flag) {
        Result<Object> result = new Result<>();
        log.info("进入sendCard()方法");
        try {
            // 打开发卡机
            log.info("检测发卡机是否有卡");
            K7X0Util.open(comHandle);
            // 检测发卡机是否预空
            boolean isEmpty = check(2, 0x31);
            if (isEmpty) {
                String str = "发卡机卡箱预空,即将无卡";
                log.info("发卡机卡箱预空,即将无卡");
                rabbitHelper.startThread(this.rabbitTemplate,cardQueue,str);
            }
            // 检测发卡机是否有卡
            isEmpty = check(3, 0x38);
            if (isEmpty) {
                // 发卡失败
                log.info("发卡失败失败数据加入数据库中");
                log.info("sendCard()方法结束return:{卡箱已空}");
                return SetResultUtil.setErrorMsgResult(result, "发卡机卡箱已空");
            }
            while (check(3, 0x31)) {
                System.out.println("##########有卡未取出");
                return SetResultUtil.setExceptionResult(result, "读卡位置有卡未取出");
            }
            for (int i = 0; i < num; i++) {
                System.out.println("发送到读卡位置");
                K7X0Util.sendToRead(comHandle);
                /**
                 * 写卡
                 */
                Result<Object> writeResult=null;
                if (i==0&&flag){
                    writeResult=NewKeyCard(roomno,begin_,end_);
                }else{
                    writeResult=DuplicateKeyCard(roomno,begin_,end_);
                }
                if (writeResult.getCode()==200){
                    //写卡成功
                    //发送到收卡位置
                    K7X0Util.sendCardToTake(comHandle);
                }else{
                    K7X0Util.regainCard(comHandle);
                    return SetResultUtil.setExceptionResult(result, "写卡失败");
                }
            }
            log.info("sendCard()方法结束");
            return SetResultUtil.setSuccessResult(result, "发卡成功");
        } catch (Exception e) {
            log.error("sendCard()出现异常error:{}", e.getMessage());
            return SetResultUtil.setExceptionResult(result, "sendCard");
        }
    }


    /**
     * 退卡
     */
    @ApiOperation(value = "退卡", notes = "退卡-Recoverycard")
    @GetMapping(value = "/Recoverycard")
    public Result<Object> Recoverycard() throws InterruptedException {
        Result<Object> result = new Result<>();
        log.info("进入sendCard()方法");
        // 回收到发卡箱
        Boolean flag = K7X0Util.regainCard(comHandle);
        if (!flag) {
            return SetResultUtil.setErrorMsgResult(result, "退卡失败");
        }
        // 将卡片发送到读卡位置
        K7X0Util.sendToRead(comHandle);
        //读卡
        result=ReadCard();
        return result;
    }

    /**
     *  从读卡位置发到取卡位置
     */
    @ApiOperation(value = "sendCardToTake", notes = "sendCardToTake")
    @GetMapping(value = "/sendCardToTake")
    public Result<Object> sendCardToTake() {
        K7X0Util.sendCardToTake(comHandle);
        return Result.ok("成功");
    }

    /**
     *  从读卡位置发送到读卡箱
     */
    @ApiOperation(value = "regain", notes = "regain")
    @GetMapping(value = "/regain")
    public Result<Object> regain() {
        if(!K7X0Util.open(comHandle)){
            System.out.println("发卡器串口打开失败+=========>com:"+comHandle);
            return Result.error("打开串口失败");
        }
        K7X0Util.regain();
        return Result.ok("成功");
    }




    @ApiOperation(value = "补卡", notes = "补卡")
    @GetMapping(value = "/Scard")
    public Result<Object> Scard() throws InterruptedException {
        Result<Object> result = new Result<>();
        // 回收到发卡箱
        log.info("进入sendCard()方法");
        boolean flag = K7X0Util.regainCardZiDong(comHandle);
        if (!flag) {
            return SetResultUtil.setErrorMsgResult(result, "失败");
        }
        Thread.sleep(1000);
        return Result.ok("成功");
    }
    /**
     * 检测发卡位置是否有卡
     */
    @ApiOperation(value = "检测发卡是否有卡", httpMethod = "GET")
    @RequestMapping(value = "/checkTureCard", method = RequestMethod.GET)
    public Result<Object> checkTureCard() {
        Result<Object> result = new Result<>();
        //打开发卡器
        K7X0Util.open(comHandle);
        boolean isEmpty = K7X0Util.check(3, 0x35);//0x35
        System.out.println("isEmpty:" + isEmpty);
        SetResultUtil.setSuccessResult(result, "检测是否有卡", isEmpty);
        return result;
    }


    @Value("${EacAddress}")
    private String EacAddress;
    @Value("${hotelID}")
    private String hotelID;
    /**
     * CISA 门锁接口调用
     *
     */
    @AutoLog(value = "制作新卡（实体卡）")
    @ApiOperation(value="制作新卡（实体卡）-NewKeyCard", notes="制作新卡（实体卡）-NewKeyCard")
    @GetMapping(value = "/NewKeyCard")
    public Result<Object> NewKeyCard(String roomNumber,String checkInTime,String checkOutTime) throws ParseException {
        Result<Object> result = new Result<Object>();
        Map<String,String> map=new HashMap<>();
        map.put("roomNumber",roomNumber);
        map.put("checkInTime",checkInTime);
        map.put("checkOutTime",checkOutTime);
        map.put("encoderNumber","01");
        map.put("clientNumber","01");
        map.put("lastName","Liu");
        map.put("hotelID",hotelID);
        Map<String,String> paramMap=map;
        String param= HttpUtil.getMapToString(paramMap);
        String url=EacAddress+"/eac/Key/NewKeyCard";
        String returnResult= HttpUtil.sendGet(url,param);
        JSONObject jsonObj = JSONObject.parseObject(returnResult);
        String returnCode = jsonObj.get("returnCode").toString();
        String returnMessage=jsonObj.get("returnMessage").toString();
        if (returnCode.equals("0")){//成功
            return SetResultUtil.setSuccessResult(result,"成功",jsonObj);
        }else{
            //失败了再次请求
            returnResult= HttpUtil.sendGet(url,param);
            jsonObj = JSONObject.parseObject(returnResult);
            returnCode = jsonObj.get("returnCode").toString();
            returnMessage=jsonObj.get("returnMessage").toString();
            if (returnCode.equals("0")){//成功
                return SetResultUtil.setSuccessResult(result,"成功",jsonObj);
            }else{
                return SetResultUtil.setErrorMsgResult(result,returnMessage);
            }
        }
    }

    @AutoLog(value = "复制卡（实体卡）")
    @ApiOperation(value="复制卡（实体卡）-DuplicateKeyCard", notes="复制卡（实体卡）-DuplicateKeyCard")
    @GetMapping(value = "/DuplicateKeyCard")
    public Result<Object> DuplicateKeyCard(String roomNumber,String checkInTime,String checkOutTime) throws ParseException {
        Result<Object> result = new Result<Object>();
        Map<String,String> map=new HashMap<>();
        map.put("roomNumber",roomNumber);
        map.put("checkInTime",checkInTime);
        map.put("checkOutTime",checkOutTime);
        map.put("encoderNumber","01");
        map.put("clientNumber","01");
        map.put("lastName","Liu");
        map.put("hotelID",hotelID);
        Map<String,String> paramMap=map;
        String param= HttpUtil.getMapToString(paramMap);
        String url=EacAddress+"/eac/Key/DuplicateKeyCard";
        String returnResult= HttpUtil.sendGet(url,param);
        JSONObject jsonObj = JSONObject.parseObject(returnResult);
        String returnCode = jsonObj.get("returnCode").toString();
        String returnMessage=jsonObj.get("returnMessage").toString();
        if (returnCode.equals("0")){//成功
            return SetResultUtil.setSuccessResult(result,"成功",jsonObj);
        }else{
            returnResult= HttpUtil.sendGet(url,param);
            jsonObj = JSONObject.parseObject(returnResult);
            returnCode = jsonObj.get("returnCode").toString();
            returnMessage=jsonObj.get("returnMessage").toString();
            if (returnCode.equals("0")){//成功
                return SetResultUtil.setSuccessResult(result,"成功",jsonObj);
            }else{
                return SetResultUtil.setErrorMsgResult(result,returnMessage);
            }
        }
    }

/*    @AutoLog(value = "读卡（实体卡）")
    @ApiOperation(value="读卡（实体卡）-ReadKeyCard", notes="读卡（实体卡）-ReadKeyCard")
    @GetMapping(value = "/ReadKeyCard")
    public Result<Object> ReadKeyCard(){
        Result<Object> result = new Result<Object>();
        Map<String,String> map=new HashMap<>();
        map.put("hotelID",hotelID);
        Map<String,String> paramMap=map;
        String param= HttpUtil.getMapToString(paramMap);
        String url=EacAddress+"/eac/Key/ReadKeyCard";
        String returnResult= HttpUtil.sendGet(url,param);
        JSONObject jsonObj = JSONObject.parseObject(returnResult);
        JSONObject resultObj=jsonObj.getJSONObject("result");
        String returnCode = resultObj.get("returnCode").toString();
        String returnMessage=resultObj.get("returnMessage").toString();
        if (returnCode.equals("0")){//成功
            JSONObject keyCard=jsonObj.getJSONObject("keyCard");
            return SetResultUtil.setSuccessResult(result,"读卡成功",keyCard);
        }else{
            return SetResultUtil.setErrorMsgResult(result,returnMessage);
        }
    }*/


    @AutoLog(value = "读卡（实体卡）")
    @ApiOperation(value="读卡（实体卡）-ReadCard", notes="读卡（实体卡）-ReadCard")
    @GetMapping(value = "/ReadCard")
    public Result<Object> ReadCard(){
        Result<Object> results = new Result<Object>();
        ComThread.InitSTA();
        int Port=4;
        String res="";
        String CardID="";
        String CardNo="";
        String str ="";//初始化
        Variant RoomNo = new Variant(str,true);//输出参数定义，必须这样，否则得不到输出参数的值
        String str1 ="";//初始化
        Variant EndDate=new Variant(str1,true);
        try {
            ActiveXComponent pp = new ActiveXComponent("CISADLL.Lock");
            Dispatch myCom = (Dispatch) pp.getObject(); //生成一个对象
            Variant result = Dispatch.call( myCom, "ReadCard",Port,CardID,CardNo,RoomNo,EndDate) ;
            res=result.toString();
            if (res.equals("0")){
                System.out.println("RoomNo:"+RoomNo.toString().substring(RoomNo.toString().length()-4));
                System.out.println("EndDate:"+EndDate.toString());
                Map<String,String> map=new HashMap<>();
                map.put("RoomNo",RoomNo.toString().substring(RoomNo.toString().length()-4));
                map.put("endTime",EndDate.toString());
                return SetResultUtil.setSuccessResult(results,"成功",map);
            }
        }catch (Exception e) {
            e.printStackTrace();
            return SetResultUtil.setExceptionResult(results,"异常");
        }
        return  SetResultUtil.setExceptionResult(results,"异常");
    }


    @AutoLog(value = "退卡（实体卡）")
    @ApiOperation(value="退卡（实体卡）-CheckOutKeyCard", notes="退卡（实体卡）-CheckOutKeyCard")
    @GetMapping(value = "/CheckOutKeyCard")
    public Result<Object> CheckOutKeyCard(String cardNumber,String roomNumber){
        Result<Object> result = new Result<Object>();
        Map<String,String> map=new HashMap<>();
        map.put("roomNumber", ReturnNullOrKong.returnNullOrKong(roomNumber));
        map.put("cardNumber",ReturnNullOrKong.returnNullOrKong(cardNumber));
        map.put("hotelID",hotelID);
        Map<String,String> paramMap=map;
        String param= HttpUtil.getMapToString(paramMap);
        String url=EacAddress+"/eac/Key/CheckOutKeyCard";
        String returnResult= HttpUtil.sendGet(url,param);
        JSONObject jsonObj = JSONObject.parseObject(returnResult);
        String returnCode = jsonObj.get("returnCode").toString();
        String returnMessage=jsonObj.get("returnMessage").toString();
        if (returnCode.equals("0")){//成功
            return SetResultUtil.setSuccessResult(result,"退卡成功");
        }else{
            return SetResultUtil.setErrorMsgResult(result,returnMessage);
        }
    }


}

