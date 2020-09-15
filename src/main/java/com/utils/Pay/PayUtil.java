package com.utils.Pay;

import com.alibaba.fastjson.JSONObject;
import java.io.*;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.*;

public class PayUtil {
    /*private static String opSys="2";
    private static String characterSet="00";
    private static String orgNo="518";//机构号
    private static String mercId="800290000015948";//商户号
    private static String trmNo="XB033766";//设备号
    private static String signType="MD5";//签名方式
    private static String ordinaryVersion="V1.0.5";//普通支付版本号
    private static String preVersion="V1.0.2";//预授权支付版本号
    private static String tuikuanVersion="V1.0.0";//退款版本号
    private static String signKey="C286D0E61C6989596A23B654FDCAB361";*/
    private static String gongyongurl="https://gateway.starpos.com.cn/adpservice/";
    private static String opSys="2";
    private static String characterSet="00";
    private static String orgNo="80516";//机构号
    private static String mercId="800110000082730";//商户号
    private static String trmNo="XB944063";//设备号
    private static String signType="MD5";//签名方式
    private static String ordinaryVersion="V1.0.5";//普通支付版本号
    private static String preVersion="V1.0.2";//预授权支付版本号
    private static String tuikuanVersion="V1.0.0";//退款版本号
    private static String signKey="AA83CA32E334399362CD1C19AD0A7157";



    /**
     * 测试
     */
    //商户号：800290000007906 终端号：XB006439机构号：11658
    //交易密钥：9FF13E7726C4DFEB3BED750779F59711 进件密钥：2CE5B3770D15B4E9E499A447E0EC1C95
    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url
     *      发送请求的 URL
     * @param param
     *      请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, String param,String tradeNo,String signValue,String txnTime,String version) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty("opSys",opSys);//windows sdk
            conn.setRequestProperty("characterSet",characterSet);
            conn.setRequestProperty("orgNo",orgNo);
            conn.setRequestProperty("mercId",mercId);
            conn.setRequestProperty("trmNo",trmNo);
            conn.setRequestProperty("tradeNo",tradeNo);
            conn.setRequestProperty("txnTime",txnTime);
            conn.setRequestProperty("signType",signType);
            conn.setRequestProperty("signValue",signValue);
            conn.setRequestProperty("version",version);
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！"+e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }

    /**
     *  自助机扫描客户出示的付款码付款
     * @param amount  支付金额
     * @param total_amount 订单总金额
     * @param authCode 支付授权码
     * @param payChannel 支付渠道
     * @return
     */
    public static String sdkBarcodePay(String amount,String total_amount,
                                                   String authCode,String payChannel,String url,int paytype) throws UnsupportedEncodingException {
        Map<String,String> paramMap=new HashMap<>();
        BigDecimal amountBig=(new BigDecimal(Double.valueOf(amount)).setScale(2, BigDecimal.ROUND_HALF_UP)).multiply(new BigDecimal(100));
        System.out.println("amountBig:"+amountBig);
        BigDecimal total_amountBig=(new BigDecimal(Double.valueOf(total_amount)).setScale(2, BigDecimal.ROUND_HALF_UP)).multiply(new BigDecimal(100));
        System.out.println("total_amountBig:"+total_amountBig);
        if (amountBig.toString().contains(".")){
            paramMap.put("amount",amountBig.toString().substring(0,amountBig.toString().length()-3));
        }else{
            paramMap.put("amount",amountBig.toString().substring(0,amountBig.toString().length()));
        }
        if (total_amountBig.toString().contains(".")){
            paramMap.put("total_amount",total_amountBig.toString().substring(0,total_amountBig.toString().length()-3));
        }else{
            paramMap.put("total_amount",total_amountBig.toString().substring(0,total_amountBig.toString().length()));
        }
        paramMap.put("authCode",authCode);
        paramMap.put("payChannel",payChannel);
        paramMap.put("opSys",opSys);//windows sdk
        paramMap.put("characterSet",characterSet);
        paramMap.put("orgNo",orgNo);
        paramMap.put("mercId",mercId);
        paramMap.put("trmNo",trmNo);
        String txnTime=new SimpleDateFormat("yyyyMMddHHmmsss").format(new Date());
        paramMap.put("txnTime",txnTime);
        paramMap.put("signType",signType);
        paramMap.put("version",paytype==0?ordinaryVersion:preVersion);
        String tradeNo= UUID.randomUUID().toString().replaceAll("-","");
        paramMap.put("tradeNo",tradeNo);
        String daiSign=getMapToStringOnlyValue(paramMap);
        daiSign+=signKey;
        String signValue= PreOccupancyUtil.encrypt32(daiSign);
        paramMap.put("signValue",signValue);
        String param="{"+getMapToString(paramMap)+"}";
        System.out.println("param:"+param);
        String returnResult=sendPost(url,param,tradeNo,signValue,txnTime,paytype==0?ordinaryVersion:preVersion);
        System.out.println("returnResult:"+returnResult);
        /*JSONObject jsonObj = JSONObject.parseObject(returnResult);
        String message = jsonObj.get("message").toString();
        message=URLDecoder.decode(message,"UTF-8");
        System.out.println("message:"+message);*/
        return returnResult;
    }

    /**
     *  自助机扫描客户出示的付款码付款
     * @param qryNo  第三方的流水号
     * @return
     */
    public static Map sdkQryBarcodePay(String qryNo,int paytype,String url) throws UnsupportedEncodingException {
        Map<String,String> paramMap=new HashMap<>();
        paramMap.put("qryNo",qryNo);
        paramMap.put("opSys",opSys);//windows sdk
        paramMap.put("characterSet",characterSet);
        paramMap.put("orgNo",orgNo);
        paramMap.put("mercId",mercId);
        paramMap.put("trmNo",trmNo);
        String txnTime=new SimpleDateFormat("yyyyMMddHHmmsss").format(new Date());
        paramMap.put("txnTime",txnTime);
        paramMap.put("signType",signType);
        paramMap.put("version",paytype==0?ordinaryVersion:preVersion);
        String tradeNo= UUID.randomUUID().toString().replaceAll("-","");
        paramMap.put("tradeNo",tradeNo);
        String daiSign=getMapToStringOnlyValue(paramMap);
        daiSign+=signKey;
        System.out.println("daiSign:"+daiSign);
        String signValue= PreOccupancyUtil.encrypt32(daiSign);
        System.out.println("signValue:"+signValue);
        paramMap.put("signValue",signValue);
        String param="{"+getMapToString(paramMap)+"}";
        System.out.println("param:"+param);
        String returnResult=sendPost(url,param,tradeNo,signValue,txnTime,paytype==0?ordinaryVersion:preVersion);
        System.out.println("returnResult:"+returnResult);
        JSONObject jsonObj = JSONObject.parseObject(returnResult);
        String message = jsonObj.get("message").toString();
        message=URLDecoder.decode(message,"UTF-8");
        String returnCode=jsonObj.get("returnCode").toString();
        String result=jsonObj.get("result").toString();// 值 :  S-交易成功F-交易失败A-等待授权Z-交易未知D-订单已撤销
        Map<String,String> returnMap=new HashMap<>();
        returnMap.put("message",message);
        returnMap.put("returnCode",returnCode);
        returnMap.put("result",result);
        returnMap.put("orderNo",jsonObj.get("orderNo").toString());
        return returnMap;
    }

    /**
     *  退款
     * @param orderNo  支付渠道的订单号
     * @return
     */
    public static Map sdkRefundBarcodePay(String orderNo) throws UnsupportedEncodingException {
        Map<String,String> paramMap=new HashMap<>();
        paramMap.put("orderNo",orderNo);
        paramMap.put("opSys",opSys);//windows sdk
        paramMap.put("characterSet",characterSet);
        paramMap.put("orgNo",orgNo);
        paramMap.put("mercId",mercId);
        paramMap.put("trmNo",trmNo);
        String txnTime=new SimpleDateFormat("yyyyMMddHHmmsss").format(new Date());
        paramMap.put("txnTime",txnTime);
        paramMap.put("signType",signType);
        paramMap.put("version",tuikuanVersion);
        String tradeNo= UUID.randomUUID().toString().replaceAll("-","");
        paramMap.put("tradeNo",tradeNo);
        String daiSign=getMapToStringOnlyValue(paramMap);
        daiSign+=signKey;
        System.out.println("daiSign:"+daiSign);
        String signValue= PreOccupancyUtil.encrypt32(daiSign);
        System.out.println("signValue:"+signValue);
        paramMap.put("signValue",signValue);
        String param="{"+getMapToString(paramMap)+"}";
        System.out.println("param:"+param);
        //String returnResult=sendPost("http://sandbox.starpos.com.cn/adpweb/ehpspos3/sdkRefundBarcodePay.json",param,tradeNo,signValue,txnTime,tuikuanVersion);
        String returnResult=sendPost(gongyongurl+"sdkRefundBarcodePay.json",param,tradeNo,signValue,txnTime,tuikuanVersion);
        System.out.println("returnResult:"+returnResult);
        JSONObject jsonObj = JSONObject.parseObject(returnResult);
        String message = jsonObj.get("message").toString();
        message=URLDecoder.decode(message,"UTF-8");
        String returnCode=jsonObj.get("returnCode").toString();
        String result=jsonObj.get("result").toString();// 值 :  S-交易成功F-交易失败A-等待授权Z-交易未知D-订单已撤销
        Map<String,String> returnMap=new HashMap<>();
        returnMap.put("message",message);
        returnMap.put("returnCode",returnCode);
        returnMap.put("result",result);
        return returnMap;
    }

    /**
     *  预授权完成
     * @param orderNo  预授权完成
     * @return
     */
    public static Map sdkCompleteEmp(String orderNo,String txnAmt) throws UnsupportedEncodingException {
        Map<String,String> paramMap=new HashMap<>();
        paramMap.put("orderNo",orderNo);
        txnAmt=((new BigDecimal(Double.valueOf(txnAmt)).setScale(2, BigDecimal.ROUND_HALF_UP)).multiply(new BigDecimal(100))).toString();
        if (txnAmt.contains(".")){
            paramMap.put("txnAmt",txnAmt.substring(0,txnAmt.length()-3));
        }else{
            paramMap.put("txnAmt",txnAmt.substring(0,txnAmt.length()));
        }
        paramMap.put("opSys",opSys);//windows sdk
        paramMap.put("characterSet",characterSet);
        paramMap.put("orgNo",orgNo);
        paramMap.put("mercId",mercId);
        paramMap.put("trmNo",trmNo);
        String txnTime=new SimpleDateFormat("yyyyMMddHHmmsss").format(new Date());
        paramMap.put("txnTime",txnTime);
        paramMap.put("signType",signType);
        paramMap.put("version",preVersion);
        String tradeNo= UUID.randomUUID().toString().replaceAll("-","");
        paramMap.put("tradeNo",tradeNo);
        String daiSign=getMapToStringOnlyValue(paramMap);
        daiSign+=signKey;
        System.out.println("待签名数据:"+daiSign);
        String signValue= PreOccupancyUtil.encrypt32(daiSign);
        System.out.println("签名:"+signValue);
        paramMap.put("signValue",signValue);
        String param="{"+getMapToString(paramMap)+"}";
        System.out.println("参数:"+param);
        //String returnResult=sendPost("http://sandbox.starpos.com.cn/adpservice/sdkCompleteEmp.json",param,tradeNo,signValue,txnTime,preVersion);
        String returnResult=sendPost(gongyongurl+"sdkCompleteEmp.json",param,tradeNo,signValue,txnTime,preVersion);
        System.out.println("返回值:"+returnResult);
        JSONObject jsonObj = JSONObject.parseObject(returnResult);
        String message = jsonObj.get("message").toString();
        message=URLDecoder.decode(message,"UTF-8");
        String returnCode=jsonObj.get("returnCode").toString();
        String result=jsonObj.get("result").toString();// 值 :  S-交易成功F-交易失败A-等待授权Z-交易未知D-订单已撤销
        Map<String,String> returnMap=new HashMap<>();
        returnMap.put("message",message);
        returnMap.put("returnCode",returnCode);
        returnMap.put("result",result);
        return returnMap;
    }
    /**
     *  预授权撤销
     * @return
     */
    public static Map sdkEmpCancel(String orderNo,String txnAmt) throws UnsupportedEncodingException {
        Map<String,String> paramMap=new HashMap<>();
        paramMap.put("orderNo",orderNo);
        txnAmt=((new BigDecimal(Double.valueOf(txnAmt)).setScale(2, BigDecimal.ROUND_HALF_UP)).multiply(new BigDecimal(100))).toString();
        if (txnAmt.contains(".")){
            paramMap.put("txnAmt",txnAmt.substring(0,txnAmt.length()-3));
        }else{
            paramMap.put("txnAmt",txnAmt.substring(0,txnAmt.length()));
        }
        paramMap.put("opSys",opSys);//windows sdk
        paramMap.put("characterSet",characterSet);
        paramMap.put("orgNo",orgNo);
        paramMap.put("mercId",mercId);
        paramMap.put("trmNo",trmNo);
        String txnTime=new SimpleDateFormat("yyyyMMddHHmmsss").format(new Date());
        paramMap.put("txnTime",txnTime);
        paramMap.put("signType",signType);
        paramMap.put("version",preVersion);
        String tradeNo=UUID.randomUUID().toString().replaceAll("-","");
        paramMap.put("tradeNo",tradeNo);
        String daiSign=getMapToStringOnlyValue(paramMap);
        daiSign+=signKey;
        System.out.println("待签名数据:"+daiSign);
        String signValue= PreOccupancyUtil.encrypt32(daiSign);
        System.out.println("签名:"+signValue);
        paramMap.put("signValue",signValue);
        String param="{"+getMapToString(paramMap)+"}";
        System.out.println("参数:"+param);
        //String returnResult=sendPost("http://sandbox.starpos.com.cn/adpservice/sdkEmpCancel.json",param,tradeNo,signValue,txnTime,preVersion);
        String returnResult=sendPost(gongyongurl+"sdkEmpCancel.json",param,tradeNo,signValue,txnTime,preVersion);
        System.out.println("返回值:"+returnResult);
        JSONObject jsonObj = JSONObject.parseObject(returnResult);
        String message = jsonObj.get("message").toString();
        message=URLDecoder.decode(message,"UTF-8");
        String returnCode=jsonObj.get("returnCode").toString();
        String result=jsonObj.get("result").toString();// 值 :  S-交易成功F-交易失败A-等待授权Z-交易未知D-订单已撤销
        Map<String,String> returnMap=new HashMap<>();
        returnMap.put("message",message);
        returnMap.put("returnCode",returnCode);
        returnMap.put("result",result);
        return returnMap;
    }

    /**
     *  预授权完成的撤销
     * @return
     */
    public static Map sdkComEmpCancel(String orderNo,String txnAmt) throws UnsupportedEncodingException {
        Map<String,String> paramMap=new HashMap<>();
        paramMap.put("orderNo",orderNo);
        txnAmt=((new BigDecimal(Double.valueOf(txnAmt)).setScale(2, BigDecimal.ROUND_HALF_UP)).multiply(new BigDecimal(100))).toString();
        if (txnAmt.contains(".")){
            paramMap.put("txnAmt",txnAmt.substring(0,txnAmt.length()-3));
        }else{
            paramMap.put("txnAmt",txnAmt.substring(0,txnAmt.length()));
        }
        paramMap.put("opSys",opSys);//windows sdk
        paramMap.put("characterSet",characterSet);
        paramMap.put("orgNo",orgNo);
        paramMap.put("mercId",mercId);
        paramMap.put("trmNo",trmNo);
        String txnTime=new SimpleDateFormat("yyyyMMddHHmmsss").format(new Date());
        paramMap.put("txnTime",txnTime);
        paramMap.put("signType",signType);
        paramMap.put("version",preVersion);
        String tradeNo= UUID.randomUUID().toString().replaceAll("-","");
        paramMap.put("tradeNo",tradeNo);
        String daiSign=getMapToStringOnlyValue(paramMap);
        daiSign+=signKey;
        System.out.println("待签名数据:"+daiSign);
        String signValue= PreOccupancyUtil.encrypt32(daiSign);
        System.out.println("签名:"+signValue);
        paramMap.put("signValue",signValue);
        String param="{"+getMapToString(paramMap)+"}";
        System.out.println("参数:"+param);
        String returnResult=sendPost(gongyongurl+"sdkComEmpCancel.json",param,tradeNo,signValue,txnTime,preVersion);
        System.out.println("返回值:"+returnResult);
        JSONObject jsonObj = JSONObject.parseObject(returnResult);
        String message = jsonObj.get("message").toString();
        message=URLDecoder.decode(message,"UTF-8");
        String returnCode=jsonObj.get("returnCode").toString();
        String result=jsonObj.get("result").toString();// 值 :  S-交易成功F-交易失败A-等待授权Z-交易未知D-订单已撤销
        Map<String,String> returnMap=new HashMap<>();
        returnMap.put("message",message);
        returnMap.put("returnCode",returnCode);
        returnMap.put("result",result);
        return returnMap;
    }
    /**
     * map转String
     * @param map
     * @return
     */
    public static String getMapToStringOnlyValue(Map<String,String> map){

        Set<String> keySet = map.keySet();
        //将set集合转换为数组
        String[] keyArray = keySet.toArray(new String[keySet.size()]);
        //给数组排序(升序)
        Arrays.sort(keyArray);
        //因为String拼接效率会很低的，所以转用StringBuilder。博主会在这篇博文发后不久，会更新一篇String与StringBuilder开发时的抉择的博文。
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < keyArray.length; i++) {
            // 参数值为空，则不参与签名 这个方法trim()是去空格
            if (map.get(keyArray[i]).trim().length() > 0) {
                sb.append(map.get(keyArray[i]).trim());
            }
        }
        return sb.toString();
    }
    /**
     * map转String
     * @param map
     * @return
     */
    public static String getMapToString(Map<String,String> map){

        Set<String> keySet = map.keySet();
        //将set集合转换为数组
        String[] keyArray = keySet.toArray(new String[keySet.size()]);
        //给数组排序(升序)
        Arrays.sort(keyArray);
        //因为String拼接效率会很低的，所以转用StringBuilder。博主会在这篇博文发后不久，会更新一篇String与StringBuilder开发时的抉择的博文。
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < keyArray.length; i++) {
            // 参数值为空，则不参与签名 这个方法trim()是去空格
            if (map.get(keyArray[i]).trim().length() > 0) {
                sb.append("'").append(keyArray[i]).append("'").append(":").append("'").append(map.get(keyArray[i]).trim()).append("'");
            }
            if(i != keyArray.length-1){
                sb.append(",");
            }
        }
        return sb.toString();
    }
}
