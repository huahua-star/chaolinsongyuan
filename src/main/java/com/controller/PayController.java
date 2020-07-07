package com.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ctc.wstx.util.StringUtil;
import com.entity.TblList;
import com.entity.TblTxnp;
import com.mysql.cj.util.StringUtils;
import com.service.ITblTxnpService;
import com.utils.CheckMapUtil;
import com.utils.FilterListUtil;
import com.utils.Pay.PayUtil;
import com.utils.Pay.umsips;
import com.utils.Returned2.Result;
import com.utils.Returned2.ReturnCode;
import com.utils.Returned2.ReturnMessage;
import com.utils.Returned2.SetResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Api(tags="支付相关功能")
@RestController
@RequestMapping("/zzj/kailaipay")
public class PayController {

    @Autowired
    private ITblTxnpService tblTxnpService;

    static int iRet;

    static char[] strMemo = new char[1024];

    /**
     * 条码普通支付
     */
    @ApiOperation(value="条码支付", notes="条码支付")
    @RequestMapping(value = "/barcodePay")
    public Result<Object> barcodePay(HttpServletResponse response, String amount, String orderId, String roomNo, String total_amount,
                                     String authCode, String payChannel, String paytype) {

        Map<String,String> payUrlMap=new HashMap<>();
        payUrlMap.put("0","http://sandbox.starpos.com.cn/adpweb/ehpspos3/sdkBarcodePay.json");//普通支付地址
        payUrlMap.put("1","http://sandbox.starpos.com.cn/adpservice/sdkBarcodeEmp.json");//预授权支付地址

        System.out.println("预订单号:"+orderId);
        System.out.println("支付金额:"+amount);
        Result<Object> result = new Result<Object>();
        log.info("barcodePay()方法amount:{}orderId:{}",amount,orderId);
        try {
            String returnResult= PayUtil.sdkBarcodePay(amount,total_amount,authCode,payChannel,payUrlMap.get(paytype),Integer.parseInt(paytype));
            System.out.println("returnResult:"+returnResult);
            JSONObject jsonObj = JSONObject.parseObject(returnResult);
            String returnCode=jsonObj.get("returnCode").toString();
            if ("000000".equals(returnCode)){
                String orderNo=jsonObj.get("orderNo").toString();//支付渠道订单号
                TblTxnp tbl=new TblTxnp();
                tbl.setOrderid(orderNo);//支付渠道订单号
                tbl.setPreOrderid(orderId);
                tbl.setPaymethod(FilterListUtil.conversion(payChannel));
                tbl.setRoomnum(roomNo);
                tbl.setState("1");
                tbl.setPaytype(paytype);
                tbl.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                switch (paytype){
                    case "0":
                        tbl.setPreamount(new BigDecimal("0"));
                        tbl.setAmount(new BigDecimal(amount));
                        break;
                    case "1":
                        tbl.setPreamount(new BigDecimal(amount));
                        tbl.setAmount(new BigDecimal("0"));
                        break;
                }
                tblTxnpService.save(tbl);
                SetResultUtil.setSuccessResult(result,"success",orderNo);
            }else{
                SetResultUtil.setErrorMsgResult(result,"支付失败");
            }
            return result;
        } catch (Exception e) {
            log.error("barcodePay()方法出现异常:{}", e.getMessage());
            result.setCode(ReturnCode.parameterError);
            result.setMessage(ReturnMessage.logicError);
            result.setSuccess(false);
            return result;
        }
    }
    @ApiOperation(value="支付状态轮询", notes="支付状态轮询")
    @RequestMapping(value = "/query_order")
    public Result<TblTxnp> query_order(String orderNo,String paytype) {
        log.info("进入query_order()方法orderNo:{}",orderNo);
        Map<String,String> payQueryUrlMap=new HashMap<>();
        payQueryUrlMap.put("0","http://sandbox.starpos.com.cn/adpweb/ehpspos3/sdkQryBarcodePay.json");//普通支付地址
        payQueryUrlMap.put("1","http://sandbox.starpos.com.cn/adpservice/sdkQryBarcodePay.json");//预授权支付地址

        Result<TblTxnp> result = new Result<TblTxnp>();
        try {
            log.info("查询支付状态");
            TblTxnp tbl = tblTxnpService.getOne(new QueryWrapper<TblTxnp>().eq("orderid",orderNo));
            Map<String,String> map= PayUtil.sdkQryBarcodePay(orderNo,Integer.parseInt(paytype),payQueryUrlMap.get(paytype));
            String message = map.get("message");
            String returnCode=map.get("returnCode");
            System.out.println("message:"+message);
            if(returnCode == "000000" || returnCode.equals("000000")) {
                String resultKey=map.get("result");
                String tureOrderNo=map.get("orderNo");
                System.out.println("resultKey:"+resultKey);
                if(resultKey == "S" ||resultKey.equals("S")){
                    tbl.setOrderid(tureOrderNo);
                    tbl.setState("2");
                    tbl.setUpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                    log.info("检测到支付成功修改支付状态");
                    tblTxnpService.updateById(tbl);
                    return SetResultUtil.setSuccessResult(result,ReturnMessage.success,tbl);
                }else if(resultKey == "A" ||resultKey.equals("A")){
                    return SetResultUtil.setErrorMsgResult(result,ReturnMessage.forPay);
                }else{
                    return SetResultUtil.setErrorMsgResult(result,ReturnMessage.payFalse);
                }
            }else{
                return SetResultUtil.setErrorMsgResult(result,message);
            }
        } catch (Exception e) {
            log.error("query_order()方法出现异常:{}", e.getMessage());
            return SetResultUtil.setExceptionResult(result);
        }
    }
    @ApiOperation(value="退款", notes="退款")
    @RequestMapping(value = "/sdkRefundBarcodePay")
    public Result<TblTxnp> sdkRefundBarcodePay(String orderNo) {
        log.info("sdkRefundBarcodePay()方法orderNo:{}",orderNo);
        Result<TblTxnp> result = new Result<TblTxnp>();
        try {
            log.info("退款");
            TblTxnp tbl = tblTxnpService.getOne(new QueryWrapper<TblTxnp>().eq("orderid",orderNo));
            Map<String,String> map= PayUtil.sdkRefundBarcodePay(orderNo);
            String message = map.get("message");
            String returnCode=map.get("returnCode");
            System.out.println("message:"+message);
            if(returnCode == "000000" || returnCode.equals("000000")) {
                String resultKey=map.get("result");
                System.out.println("resultKey:"+resultKey);
                if(resultKey == "S" ||resultKey.equals("S")){
                    tbl.setState("0");
                    tbl.setUpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                    log.info("检测到退款成功修改订单状态");
                    tblTxnpService.updateById(tbl);
                    return SetResultUtil.setSuccessResult(result,ReturnMessage.success,tbl);
                }else if(resultKey == "A" ||resultKey.equals("A")){
                    return SetResultUtil.setErrorMsgResult(result,"退款失败");
                }else{
                    return SetResultUtil.setErrorMsgResult(result,"退款失败");
                }
            }else{
                return SetResultUtil.setErrorMsgResult(result,message);
            }
        } catch (Exception e) {
            log.error("sdkRefundBarcodePay()方法出现异常:{}", e.getMessage());
            return SetResultUtil.setExceptionResult(result);
        }
    }


    /**
     * 预授权完成
     */
    @ApiOperation(value="预授权完成", notes="预授权完成")
    @RequestMapping(value = "/sdkCompleteEmp")
    public Result<TblTxnp> sdkCompleteEmp(String orderNo,String txnAmt) {
        log.info("sdkCompleteEmp()方法orderNo:{}txnAmt{}",orderNo,txnAmt);
        Result<TblTxnp> result = new Result<TblTxnp>();
        try {
            log.info("预授权完成");
            TblTxnp tbl = tblTxnpService.getOne(new QueryWrapper<TblTxnp>().eq("orderid",orderNo));
            Map<String,String> map= PayUtil.sdkCompleteEmp(orderNo,txnAmt);
            String message = map.get("message");
            String returnCode=map.get("returnCode");
            System.out.println("message:"+message);
            if(returnCode == "000000" || returnCode.equals("000000")) {
                String resultKey=map.get("result");
                System.out.println("resultKey:"+resultKey);
                if(resultKey == "S" ||resultKey.equals("S")){
                    tbl.setState("3");
                    tbl.setAmount(new BigDecimal(txnAmt));
                    tbl.setUpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                    log.info("预授权完成修改流水状态");
                    tblTxnpService.updateById(tbl);
                    return SetResultUtil.setSuccessResult(result,ReturnMessage.success,tbl);
                }else {
                    return SetResultUtil.setErrorMsgResult(result,"预授权完成失败");
                }
            }else{
                return SetResultUtil.setErrorMsgResult(result,message);
            }
        } catch (Exception e) {
            log.error("sdkCompleteEmp()方法出现异常:{}", e.getMessage());
            return SetResultUtil.setExceptionResult(result);
        }
    }

    /**
     * 预授权撤销
     */
    @ApiOperation(value="预授权撤销", notes="预授权撤销")
    @RequestMapping(value = "/sdkEmpCancel")
    public Result<TblTxnp> sdkEmpCancel(String orderNo,String txnAmt) {
        log.info("sdkEmpCancel()方法orderNo:{}txnAmt{}",orderNo,txnAmt);
        Result<TblTxnp> result = new Result<TblTxnp>();
        try {
            log.info("预授权撤销");
            TblTxnp tbl = tblTxnpService.getOne(new QueryWrapper<TblTxnp>().eq("orderid",orderNo));
            Map<String,String> map= PayUtil.sdkEmpCancel(orderNo,txnAmt);
            String message = map.get("message");
            String returnCode=map.get("returnCode");
            System.out.println("message:"+message);
            if(returnCode == "000000" || returnCode.equals("000000")) {
                String resultKey=map.get("result");
                System.out.println("resultKey:"+resultKey);
                if(resultKey == "S" ||resultKey.equals("S")){
                    tbl.setState("0");
                    tbl.setUpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                    log.info("预授权撤销修改流水状态");
                    tblTxnpService.updateById(tbl);
                    return SetResultUtil.setSuccessResult(result,ReturnMessage.success,tbl);
                }else {
                    return SetResultUtil.setErrorMsgResult(result,"预授权撤销失败");
                }
            }else{
                return SetResultUtil.setErrorMsgResult(result,message);
            }
        } catch (Exception e) {
            log.error("sdkEmpCancel()方法出现异常:{}", e.getMessage());
            return SetResultUtil.setExceptionResult(result);
        }
    }

    /**
     * 预授权完成的撤销
     */
    @ApiOperation(value="预授权完成的撤销", notes="预授权完成的撤销")
    @RequestMapping(value = "/sdkComEmpCancel")
    public Result<TblTxnp> sdkComEmpCancel(String orderNo,String txnAmt) {
        log.info("sdkComEmpCancel()方法orderNo:{}txnAmt{}",orderNo,txnAmt);
        Result<TblTxnp> result = new Result<TblTxnp>();
        try {
            log.info("预授权完成的撤销");
            TblTxnp tbl = tblTxnpService.getOne(new QueryWrapper<TblTxnp>().eq("orderid",orderNo));
            Map<String,String> map= PayUtil.sdkComEmpCancel(orderNo,txnAmt);
            String message = map.get("message");
            String returnCode=map.get("returnCode");
            System.out.println("message:"+message);
            if(returnCode == "000000" || returnCode.equals("000000")) {
                String resultKey=map.get("result");
                System.out.println("resultKey:"+resultKey);
                if(resultKey == "S" ||resultKey.equals("S")){
                    tbl.setState("4");
                    tbl.setUpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                    log.info("预授权完成的撤销修改流水状态");
                    tblTxnpService.updateById(tbl);
                    return SetResultUtil.setSuccessResult(result,ReturnMessage.success,tbl);
                }else {
                    return SetResultUtil.setErrorMsgResult(result,"预授权完成的撤销失败");
                }
            }else{
                return SetResultUtil.setErrorMsgResult(result,message);
            }
        } catch (Exception e) {
            log.error("sdkComEmpCancel()方法出现异常:{}", e.getMessage());
            return SetResultUtil.setExceptionResult(result);
        }
    }

    /**
     * 根据resno获取流水"
     */
    @ApiOperation(value="根据resno获取流水", notes="根据resno获取流水")
    @RequestMapping(value = "/getTblByResno")
    public Result<?> getTblByResno(String resno) {
        log.info("getTblByResno()resNo:{}",resno);
        Result<TblTxnp> result = new Result<TblTxnp>();
        try {
            log.info("根据resno获取流水");
            List<TblTxnp> tblTxnpList = tblTxnpService.list(new QueryWrapper<TblTxnp>().eq("pre_orderid",resno)
                    .eq("state","2")
                    .eq("paytype","1")
                    .in("paymethod",0,1));
            return Result.ok(tblTxnpList);
        } catch (Exception e) {
            log.error("getTblByResno()方法出现异常:{}", e.getMessage());
            return SetResultUtil.setExceptionResult(result);
        }
    }

    /**
     * 根据resno获取流水"
     */
    @ApiOperation(value="根据订单号获取银联流水", notes="根据订单号获取银联流水")
    @RequestMapping(value = "/getTblByResnoByYinLian")
    public Result<?> getTblByResnoByYinLian(String resno) {
        log.info("getTblByResno()resNo:{}",resno);
        Result<TblTxnp> result = new Result<TblTxnp>();
        try {
            log.info("根据resno获取流水");
            List<TblTxnp> tblTxnpList = tblTxnpService.list(new QueryWrapper<TblTxnp>().eq("pre_orderid",resno)
                    .eq("state","2")
                    .eq("paytype","1")
                    .eq("paymethod","2"));//银行卡
            return Result.ok(tblTxnpList);
        } catch (Exception e) {
            log.error("getTblByResno()方法出现异常:{}", e.getMessage());
            return SetResultUtil.setExceptionResult(result);
        }
    }







    public static void main(String[] args) {
        if (new BigDecimal(0.00).compareTo(new BigDecimal(0))== 1){
            System.out.println("true");
        }else{
            System.out.println("false");
        }
    }


    /**
     * 一键 预授权完成
     */
    @ApiOperation(value="一键预授权完成", notes="一键 预授权完成")
    @PostMapping(value = "/oneKeyByPre")
    public Result<?> oneKeyByPre(@RequestBody TblList tblList) {
        log.info("oneKeyByPre()tblListSize:{},amount:{}",tblList.getTblTxnpList().size(),tblList.getAmount());
        List<TblTxnp> tblTxnpList = tblList.getTblTxnpList();
        BigDecimal amount=new BigDecimal(Double.parseDouble(tblList.getAmount()));
        BigDecimal allPreAmount=new BigDecimal(0);
        for(TblTxnp tblTxnp : tblTxnpList){
            allPreAmount=allPreAmount.add(tblTxnp.getPreamount());
        }
        if (allPreAmount.compareTo(amount) == -1)//如果 总预授权金额小于 预授权完成金额，则表示预授权不足 需要补交预授权
        {
            Result.error("预授权不足");
        }
        for (TblTxnp tblTxnp : tblTxnpList) {
            System.out.println("amount:"+amount.setScale(2,BigDecimal.ROUND_HALF_UP).toString());
            if (amount.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue()>0.00){
                if (tblTxnp.getPreamount().compareTo(amount) == -1) {
                    Result<TblTxnp> result=sdkCompleteEmp(tblTxnp.getOrderid(),tblTxnp.getPreamount().toString());
                    if (result.getCode()==200){
                        amount=amount.subtract(tblTxnp.getPreamount());
                        tblTxnp.setState("3");
                        tblTxnp.setAmount(tblTxnp.getPreamount());//真实预授权完成金额为 该金额
                        tblTxnp.setUpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                        tblTxnpService.updateById(tblTxnp);
                    }else{
                        return Result.error("一键完成预授权失败，原因:"+result.getMessage());
                    }
                } else{
                    Result<TblTxnp> result=sdkCompleteEmp(tblTxnp.getOrderid(),amount.setScale(2,BigDecimal.ROUND_HALF_UP).toString());
                    if (result.getCode()==200){
                        amount=amount.subtract(amount);
                        tblTxnp.setState("3");
                        tblTxnp.setAmount(amount);
                        tblTxnp.setUpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                        tblTxnpService.updateById(tblTxnp);
                    }else{
                        return Result.error("一键完成预授权失败，原因:"+result.getMessage());
                    }
                }
            }else{
                Result<TblTxnp> result=sdkEmpCancel(tblTxnp.getOrderid(),tblTxnp.getPreamount().toString());
                if (result.getCode()==200){
                    tblTxnp.setState("0");
                    tblTxnp.setUpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                    tblTxnpService.updateById(tblTxnp);
                }else{
                    return Result.error("一键完成预授权失败，原因:"+result.getMessage());
                }
            }
        }
        return Result.ok("成功");
    }


    /**
     * 一键 预授权撤销 //全部撤销
     */
    @ApiOperation(value="一键预授权撤销", notes="一键预授权撤销")
    @PostMapping(value = "/oneKeyByPreCancle")
    public Result<?> oneKeyByPreCancle(@RequestBody TblList tblList) {
        log.info("oneKeyByPreCancle()tblListSize:{},amount:{}",tblList.getTblTxnpList().size(),tblList.getAmount());
        List<TblTxnp> tblTxnpList = tblList.getTblTxnpList();
        for(TblTxnp tblTxnp : tblTxnpList) {
            Result<TblTxnp> result=sdkEmpCancel(tblTxnp.getOrderid(),tblTxnp.getPreamount().toString());
            if (result.getCode()==200){
                tblTxnp.setState("0");
                tblTxnp.setUpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                tblTxnpService.updateById(tblTxnp);
            }else{
                return Result.error("一键完成预授权失败，原因:"+result.getMessage());
            }
        }
        return Result.ok("成功");
    }



    //-------------------------------------------------------------


    /**
     * 第一步
     * 银联预授权 与 预授权完成
     * @param amount
     * @param dealType  0普通支付 1创建预授权订单 支付类型 2 预授权完成 3预授权撤销
     * @param originalCode  原授权号
     * @return
     */
    @ApiOperation(value="初始化", notes="初始化")
    @RequestMapping(value = "/UMS_Init")
    private Result<Object> UMS_SetReq(String amount,String dealType,String originalCode){
        Result<Object> result = new Result<Object>();
        log.info("UMS_SetReq()进入amount:{}dealType:{}originalCode{}",amount,dealType,originalCode);
        try {
            Map<String,String> map= new HashMap<>();
            map.put("amount",amount);
            map.put("dealType",dealType);
            //Todo  需要 金额 amount 转成 000000000001格式  单位为分
            if(!CheckMapUtil.checkMap(map)){
                return SetResultUtil.setLackParamResult(result,"缺少参数");
            }
            //初始化
            log.info("初始化");
            iRet = UMS_Init();
            log.info("进入UMS_Init()方法");
            if(iRet!=0){
                log.error("UMS_Init(),UMS_SetReq()方法结束初始化失败");
                return SetResultUtil.setErrorMsgResult(result,"初始化失败");
            }
            log.info("UMS_Init()结束");
            //银行卡的卡号#加上空格凑够1024
            for (int i = 0; i < 1024; i++) {
                strMemo[i] = ' ';
            }
            Date thisTime = new Date();
            SimpleDateFormat fmt=new SimpleDateFormat("yyyyMMdd");
            String cpInReq=null;
            //0普通支付 1创建预授权订单
            log.info("根据dealType字段判断支付类型dealType:{}",dealType);
            String payForm ="00"; //普通支付
            if("1".equals(dealType)){
                //预授权支付
                log.info("添加预授权");
                payForm = "08";
            }
            if("2".equals(dealType)){
                //预授权完成
                log.info("预授权完成");
                payForm = "10";
            }
            if("3".equals(dealType)){
                //预授权撤销
                log.info("预授权撤销");
                payForm = "09";
            }
            log.info("设置入参");
            Double money = Double.parseDouble(amount) * 100;
            String qian = String.format("%012d", money.intValue());
            log.info("time:"+fmt.format(thisTime));
            switch (dealType){
                case "0":
                case "1":
                    cpInReq = "00000000" + "11111111" + payForm
                            + qian + "333333" +  fmt.format(thisTime) + "555555555555"
                            + "666666" + "777777" + String.valueOf(strMemo) + "888";
                    break;
                case "2":
                case "3":
                    cpInReq = "00000000" + "11111111" + payForm
                            + qian + "333333" +  fmt.format(thisTime) + "555555555555"
                            + originalCode + "777777" + String.valueOf(strMemo) + "888";
                    break;
            }
            System.out.println("cnInReq:"+cpInReq);
            iRet = umsips.instanceDll.UMS_SetReq(cpInReq);
            if(iRet!=0){
                log.error("UMS_SetReq()结束参数传入失败iRet:{}",iRet);
                return SetResultUtil.setErrorMsgResult(result,"参数传入失败");
            }

            log.info("UMS_SetReq()结束");
            if ("200".equals(UMS_EnterCard().getCode())){
                return SetResultUtil.setSuccessResult(result,"成功","success");
            }else{
                return UMS_EnterCard();
            }
        }catch (Exception e) {
            log.error("UMS_SetReq()方法出现异常:{}", e.getMessage());
            return SetResultUtil.setExceptionResult(result,"UMS_SetReq()方法");
        }
    }

    /**
     * 进卡操作
     * @return
     */
    @RequestMapping(value = "/UMS_EnterCard")
    private Result<Object> UMS_EnterCard()  {
        Result<Object> result = new Result<Object>();
        try {
            log.info("进入UMS_EnterCard()方法");
            iRet = umsips.instanceDll.UMS_EnterCard();
            log.info("UMS_EnterCard:iRet:"+iRet);
            if(iRet>2){
                log.error("UMS_EnterCard()结束进卡操作失败iRet:{}",iRet);
                return SetResultUtil.setErrorMsgResult(result,"UMS_EnterCard()结束进卡操作失败");
            }
            log.error("UMS_EnterCard()结束");
            return SetResultUtil.setSuccessResult(result,"进卡成功");
        }catch (Exception e){
            log.error("UMS_EnterCard()方法出现异常:{}", e.getMessage());
            return SetResultUtil.setExceptionResult(result,"UMS_SetReq()方法");
        }

    }

    /**
     * 第三部
     * 卡的位置
     * @return
     */
    @ApiOperation(value="检查卡位置", notes="检查卡位置")
    @RequestMapping(value = "/UMS_CheckCard")
    public Result<Object> UMS_CheckCard() {
        Result<Object> result = new Result<Object>();
        try {
            log.info("进入UMS_CheckCard()方法");
            byte[] state_out = new byte[1];
            iRet = umsips.instanceDll.UMS_CheckCard(state_out);
            log.info("读取卡的位置iRet:{}",iRet);
            //TODO 有可能需要弹卡
            if (state_out[0] == 52) {
                log.error("UMS_CheckCard()卡在插卡器卡口位置");
                //UMS_EjectCard("卡在插卡器卡口位置,");
                return SetResultUtil.setErrorMsgResult(result,"UMS_CheckCard()结束卡在插卡器卡口位置");
            }else if (state_out[0] == 53) {
                log.error("UMS_CheckCard()结束未检测到卡");
                return SetResultUtil.setErrorMsgResult(result,"未检测到卡");
            }else if (state_out[0] == 56) {
                log.error("UMS_CheckCard()结束卡在电子现金感应器上");
                //UMS_EjectCard("卡在电子现金感应器上,");
                return SetResultUtil.setErrorMsgResult(result,"UMS_CheckCard()结束卡在电子现金感应器上");
            }else if (state_out[0] == 57){
                log.error("UMS_CheckCard()结束卡在非接联机感应器上");
                //UMS_EjectCard("卡在非接联机感应器上,");
                return SetResultUtil.setErrorMsgResult(result,"卡在非接联机感应器上");
            }
            log.info("UMS_CheckCard()结束");
            return SetResultUtil.setSuccessResult(result,"UMS_CheckCard()成功");
        } catch (Exception e){
            log.error("UMS_CheckCard()方法出现异常:{}", e.getMessage());
            return SetResultUtil.setExceptionResult(result,"UMS_CheckCard()方法");
        }

    }


    /**
     * 第四步
     * 读取卡的信息
     * @return
     */
    @ApiOperation(value="读取卡信息", notes="读取卡信息")
    @RequestMapping(value = "/UMS_ReadCard")
    public Result<Object> UMS_ReadCard() {
        Result<Object> result = new Result<Object>();
        try {
            log.info("进入UMS_ReadCard()方法");
            byte[] cpData = new byte[20];
            //读取卡的信息
            iRet = umsips.instanceDll.UMS_ReadCard(cpData);
            System.out.println("读取卡的信息iRet:"+iRet);
            log.info("读取卡信息cpData:{}",new String(cpData));
            if(iRet>3 || new String(cpData).trim().length()==0 ){
                log.error("UMS_ReadCard()结束读取卡信息失败iRet:{}",iRet);
                UMS_EjectCard("读取卡信息失败");
                return SetResultUtil.setErrorMsgResult(result,"读取卡信息失败");
            }
            //开启密码键盘
            int keyboard = UMS_StartPin().getCode();// 200成功
            if(keyboard != 200){
                UMS_EjectCard("开启密码键盘失败,");
                return SetResultUtil.setErrorMsgResult(result,"开启密码键盘失败");
            }
            String returnCpData=new String(cpData);//读卡信息
            log.info("returnCpData:"+returnCpData);
            return SetResultUtil.setSuccessResult(result,"读卡成功");
        } catch (Exception e){
            log.error("UMS_ReadCard()方法出现异常:{}", e.getMessage());
            UMS_EjectCard("读卡信息接口出现异常,");
            return SetResultUtil.setExceptionResult(result,"读卡信息接口");
        }

    }

    /**
     * 轮训客户输入的密码
     * @return
     */
    @ApiOperation(value="获取客户密码", notes="获取客户密码")
    @RequestMapping(value = "/UMS_GetOnePass")
    public Result<Object> UMS_GetOnePass() {
        Result<Object> result = new Result<Object>();
        log.info("轮训客户输入的密码");
        byte key_out[] = new byte[1];
        while (true) {
            iRet = umsips.instanceDll.UMS_GetOnePass(key_out);
            if (iRet != 0) {
                log.error("UMS_GetOnePass()结束输入密码失败");
                return SetResultUtil.setErrorMsgResult(result,"UMS_GetOnePass()结束输入密码失败");
            }
            if (key_out[0] == 13) {
                log.info("UMS_GetOnePass()结束密码输入完成");
                break;
            } else if (key_out[0] == 2) {
                log.error("UMS_GetOnePass()结束密码输入超时");
                //R.error(2,"密码输入超时");
                return SetResultUtil.setErrorMsgResult(result,"密码输入超时");
            } else if (key_out[0] == 27) {
                log.info("UMS_GetOnePass()结束用户取消");
                break;
            } else if (key_out[0] == 8) {
                log.info("退格");
            }
            try {
                Thread.sleep(600);
            } catch (InterruptedException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            // RetCode.setText("启动加密返回:"+iRet+"");
        }
        return SetResultUtil.setSuccessResult(result,"轮询密码成功");
    }

    /**
     * 第五步
     * 获取密码
     */
    @ApiOperation(value="完成交易", notes="完成交易")
    @RequestMapping(value = "/UMS_GetPin")
    public Result<Object> UMS_GetPin(String orderId,String roomNum,String dealType,String amount,String originalCode) {
        Result<Object> result = new Result<Object>();
        try {
            Map<String,String> map= new HashMap<>();
            map.put("amount",amount);
            map.put("dealType",dealType);
            map.put("roomNum",roomNum);
            //Todo  需要 金额 amount 转成 000000000001格式  单位为分
            if(!CheckMapUtil.checkMap(map)){
                return SetResultUtil.setLackParamResult(result,"缺少参数");
            }
            log.info("进入UMS_GetPin()方法");
            iRet = umsips.instanceDll.UMS_GetPin();
            System.out.println("取密文返回:" + iRet);
            if (iRet != 0){
                log.error("UMS_GetPin()方法结束读取秘钥失败");
                UMS_EjectCard("读取秘钥失败,");
                return SetResultUtil.setErrorMsgResult(result,"读取秘钥失败");
            }
            log.info("UMS_GetPin()方法结束");
            return UMS_TransCard(orderId,roomNum,dealType,amount,originalCode);
        }catch (Exception e){
            log.error("UMS_GetPin()方法出现异常:{}", e.getMessage());
            UMS_EjectCard("UMS_GetPin()方法异常,");
            return SetResultUtil.setExceptionResult(result,"UMS_GetPin");
        }
    }

    /**
     * 第六步
     * 交易
     * @return
     */
    public Result<Object> UMS_TransCard(String orderId,String roomNum,String dealType,String amount,String originalCode) throws UnsupportedEncodingException {
        Result<Object> result = new Result<Object>();
        try {
            log.info("进入UMS_TransCard()方法orderId:{}",orderId);
            byte[] strReq = new byte[3000];
            byte[] strResp = new byte[3000];
            int size = 1024 * 1024;
            log.info("确认交易");
            iRet = umsips.instanceDll.UMS_TransCard(strReq, strResp);
            log.info("UMS_TransCard:iRet:"+iRet);
            String thisTime =  new String(subBytes(new String(strResp,"gbk").getBytes(), 90, 6));//交易时间
            String warrantyNo = new String(subBytes(new String(strResp,"gbk").getBytes(), 108, 6)); // 授权号
            String cardNo = new String(subBytes(new String(strResp,"gbk").getBytes(), 42, 20)).trim(); // 卡号
            String  zong = warrantyNo+thisTime+cardNo;
            log.info("thisTime:"+thisTime);
            log.info("warrantyNo:"+warrantyNo);
            log.info("cardNo:"+cardNo);
            log.info("zong:"+zong);
            log.info("参数传入成功添加订单信息");
            if(dealType.equals("1")){
                if (null==warrantyNo.trim() || "".equals(warrantyNo.trim()))
                {
                    UMS_EjectCard("");
                    log.info("弹卡，本次交易失败。");
                    return SetResultUtil.setErrorMsgResult(result,"交易失败，可能是余额不足");
                }
            }
            System.out.println("originalCode:"+originalCode);
            String id=null;
            if (null == originalCode || "".equals(originalCode)){
                TblTxnp tbl=new TblTxnp();
                tbl.setOrderid(warrantyNo);//支付渠道订单号
                tbl.setPreOrderid(orderId);
                tbl.setPaymethod("2");
                tbl.setRoomnum(roomNum);
                tbl.setCreateTime(thisTime);
                tbl.setCardno(cardNo);
                tbl.setState("2");
                switch (dealType){
                    case "0":
                        tbl.setPaytype("0");
                        tbl.setPreamount(new BigDecimal("0"));
                        tbl.setAmount(new BigDecimal(amount));
                        break;
                    case "1":
                        tbl.setPaytype("1");
                        tbl.setPreamount(new BigDecimal(amount));
                        tbl.setAmount(new BigDecimal("0"));
                        break;
                }
                System.out.println("tbl:"+tbl);
                tblTxnpService.save(tbl);
                TblTxnp tblTxnp=tblTxnpService.getOne(new QueryWrapper<TblTxnp>().eq("orderid",warrantyNo));
                id=tblTxnp.getId();
            }else {
                TblTxnp tblTxnp=tblTxnpService.getOne(new QueryWrapper<TblTxnp>().eq("orderid",originalCode));
                switch (dealType){
                    case "2":
                        tblTxnp.setState("3");
                        tblTxnp.setAmount(new BigDecimal(amount));
                        break;
                    case "3":
                        tblTxnp.setState("0");
                        tblTxnp.setAmount(new BigDecimal(amount));
                        break;
                }
                tblTxnp.setUpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                System.out.println("tblTxnp:"+tblTxnp);
                tblTxnpService.updateById(tblTxnp);
                id=tblTxnp.getId();
            }
            log.info("调用弹卡操作");
            //交易成功弹卡
            UMS_EjectCard("");
            log.info("弹卡成功执行读卡器关闭操作");
            iRet = umsips.instanceDll.UMS_CardClose();
            if (iRet != 0){
                log.error("读卡器关闭失败UMS_TransCard()方法结束");
                return SetResultUtil.setErrorMsgResult(result,"读卡器关闭失败");
            }
            log.info("读卡器关闭成功UMS_TransCard()方法结束");
            return SetResultUtil.setSuccessResult(result,"交易成功",warrantyNo+","+id);
        } catch (Exception e){
            log.error("UMS_TransCard()方法出现异常:{}", e.getMessage());
            UMS_EjectCard("UMS_TransCard()方法出现异常,");
            return SetResultUtil.setExceptionResult(result,"UMS_TransCard");
        }
    }


    /**
     * 弹卡
     * @return
     */
    @ApiOperation(value="弹卡", notes="弹卡")
    @RequestMapping(value = "/UMS_EjectCard")
    public Result<Object> UMS_EjectCard(String shibai) {
        Result<Object> result = new Result<Object>();
        iRet = umsips.instanceDll.UMS_EjectCard();
        System.out.println("弹卡：iRet："+iRet);
        if(iRet!=0){
            //TODO 可以加入吞卡
            log.error("弹卡失败调用吞卡操作，提醒入住人找前台那银行卡");
            iRet =  umsips.instanceDll.UMS_CardSwallow();
            if(iRet!=0){
                log.error("UMS_TransCard()方法结束吞卡失败");
                return SetResultUtil.setErrorMsgResult(result,"UMS_TransCard()方法结束吞卡失败");
            }
            log.error("UMS_TransCard()方法结束弹卡失败，已吞卡");
            return SetResultUtil.setErrorMsgResult(result,"UMS_TransCard()方法结束弹卡失败，已吞卡");
        }
        return SetResultUtil.setSuccessResult(result,"弹卡成功");
    }
    //初始化
    public int UMS_Init() {
        iRet = umsips.instanceDll.UMS_Init(1);
        return iRet;
    }

    /**
     * 开启密码键盘
     * @return
     */
    public Result<Object> UMS_StartPin() {
        Result<Object> result = new Result<Object>();
        log.info("进入UMS_StartPin()方法");
        iRet = umsips.instanceDll.UMS_StartPin();
        if(iRet!=0){
            log.error("UMS_StartPin()开启密码键盘失败iRet:{}",iRet);
            return SetResultUtil.setErrorMsgResult(result,"UMS_StartPin()开启密码键盘失败");
        }
        return SetResultUtil.setSuccessResult(result,"成功开启密码键盘");
    }

    /**
     * 读取返回信息
     * @param src
     * @param begin
     * @param count
     * @return
     */
    public static byte[] subBytes(byte[] src, int begin, int count) {
        byte[] bs = new byte[count];
        System.arraycopy(src, begin, bs, 0, count);
        return bs;
    }



}
