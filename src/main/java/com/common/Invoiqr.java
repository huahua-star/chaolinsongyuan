package com.common;

import com.alibaba.fastjson.JSON;
import com.alipay.demo.trade.utils.ZxingUtils;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("fapiao")
@Api(value = "fapiao", description = "发票入住相关接口")
public class Invoiqr {

	// 编码名称
	private static final String encode = "UTF-8";
	// 日期格式
	private static final String dateFormat = "yyyy-MM-dd HH:mm:ss";
	private static final DateFormat df = new SimpleDateFormat(dateFormat);

	/**
	 * 发票二维码生成
	 * @param amount
	 * @param qrDir
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws Exception
	 */
    @RequestMapping("fapiao")
	public Map getCheckInPerson(String amount,String qrDir,String appCode,String taxpayerCode,String keyStorePath,
                                String keyStoreAbner,String keyStorePassWord,String facadeUrl,String reservationNumber) throws UnsupportedEncodingException, Exception {
		Map map = new HashMap();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmm");
        String dateString = formatter.format(new Date());
		String orderNo = dateString+reservationNumber;
		map.put("orderNo",orderNo);
		map.put("taxpayerCode", taxpayerCode);
		map.put("scanCodeKey", orderNo);
		map.put("orderTime", df.format(new Date()));
		map.put("drawer", "机器");
		map.put("totalAmount", amount);
		List<Map> orderItems = new ArrayList<Map>();
		Map map1 = new HashMap();
		map1.put("name", "住店消费");
		map1.put("taxRate", 0.06);
		map1.put("amount", amount);
		//map1.put("catalogCode", "5010199000000000000");
		map1.put("catalogCode", "3070402000000000000");
		orderItems.add(map1);
		map.put("orderItems", orderItems);
		String param = JSON.toJSONString(map);
		System.out.println("param:"+param);
		String sign = CertificateUtils.signToBase64(param.getBytes(encode), keyStorePath, keyStoreAbner,
				keyStorePassWord);
		Map<String, String> vars = new HashMap<String, String>();
		vars.put("appCode", URLEncoder.encode(appCode, encode));
		vars.put("cmdName", URLEncoder.encode("chinaeinv.api.order.v11.kp_async", encode));
		vars.put("sign", URLEncoder.encode(sign, encode));
		String responseJson = HttpUtil.doPost(facadeUrl, vars, param, 10000, 10000);
		System.out.println("response:"+responseJson);
		ResponseData responseData = JSON.parseObject(responseJson, ResponseData.class);
		/*String urlCode = "http://www.chinaeinv.com:980/scancode/init?orderNo=" + orderNo + "&scanCodeKey=" + orderNo
				+ "&taxpayerCode=" + taxpayerCode;测试*/
		/*String urlCode = "http://www.chinaeinv.com/scancode/init?orderNo=" + orderNo + "&scanCodeKey=" + orderNo
				+ "&taxpayerCode=" + taxpayerCode;*/
		String urlCode = "http://www.chinaeinv.com/scancode/"+taxpayerCode+"/"+orderNo+"/"+orderNo;
        System.out.println("urlCode:"+urlCode);
		/*File file = new File(qrDir);
		if (!file.exists()) {
			file.mkdir();
		}
		String filePath = String.format(qrDir + "/qr-%s.png", orderNo);
		ZxingUtils.getQRCodeImge(urlCode, 256, filePath);*/
		Map resultMap = new HashMap();
		//resultMap.put("filePath", filePath);
		resultMap.put("orderNo", orderNo);
		resultMap.put("url",urlCode);
		if(resultMap!=null){
			return resultMap;
		}else{
			return null;
		}
	}

	/**
	 * 根据订单号查询发票
	 * @param orderNo
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws Exception
	 */
	public  String quiry_order(String orderNo,String appCode,String taxpayerCode,String keyStorePath,
                               String keyStoreAbner,String keyStorePassWord,String facadeUrl) throws Exception {
		Map map = new HashMap();
		map.put("orderNo", orderNo);
		map.put("taxpayerCode", taxpayerCode);
		String param = JSON.toJSONString(map);
		String sign = CertificateUtils.signToBase64(param.getBytes(encode), keyStorePath, keyStoreAbner,
				keyStorePassWord);
		Map<String, String> vars = new HashMap<String, String>();
		vars.put("appCode", URLEncoder.encode(appCode, encode));
		vars.put("cmdName", URLEncoder.encode("chinaeinv.api.order.v11.cx.orderNo", encode));
		vars.put("sign", URLEncoder.encode(sign, encode));
		String responseJson = HttpUtil.doPost(facadeUrl, vars, param, 10000, 10000);
		System.out.println(responseJson);
		ResponseData responseData = JSON.parseObject(responseJson, ResponseData.class);
		System.out.println(responseData.toString());
		return responseJson;
	}

	/**
	 * 根据订单号取消票
	 * @param orderNo
	 * @return
	 * @throws Exception
	 */
	public  String cancel_order(String orderNo,String appCode,String taxpayerCode,String keyStorePath,
                                String keyStoreAbner,String keyStorePassWord,String facadeUrl) throws Exception {
		Map map = new HashMap();
		map.put("orderNo", "db4584cf482a48b59f295a3567b33908");
		map.put("taxpayerCode", taxpayerCode);
		String param = JSON.toJSONString(map);
		String sign = CertificateUtils.signToBase64(param.getBytes(encode), keyStorePath, keyStoreAbner,
				keyStorePassWord);
		Map<String, String> vars = new HashMap<String, String>();
		vars.put("appCode", URLEncoder.encode(appCode, encode));
		vars.put("cmdName", URLEncoder.encode("chinaeinv.api.order.v11.cancel", encode));
		vars.put("sign", URLEncoder.encode(sign, encode));
		String responseJson = HttpUtil.doPost(facadeUrl, vars, param, 10000, 10000);
		System.out.println(responseJson);
		ResponseData responseData = JSON.parseObject(responseJson, ResponseData.class);
		System.out.println(responseData.toString());
		return responseJson;
	}
	//没写完的冲红
	/*public  String invalid_order(String orderNo) throws Exception {
		Map map = new HashMap();
		map.put("orderNo", "");
		map.put("taxpayerCode", taxpayerCode);
		map.put("reason","测试");
		String param = JSON.toJSONString(map);
		String sign = CertificateUtils.signToBase64(param.getBytes(encode), keyStorePath, keyStoreAbner,
				keyStorePassWord);
		Map<String, String> vars = new HashMap<String, String>();
		vars.put("appCode", URLEncoder.encode(appCode, encode));
		vars.put("cmdName", URLEncoder.encode("chinaeinv.api.order.v11.cancel", encode));
		vars.put("sign", URLEncoder.encode(sign, encode));
		String responseJson = HttpUtil.doPost(facadeUrl, vars, param, 10000, 10000);
		System.out.println(responseJson);
		ResponseData responseData = JSON.parseObject(responseJson, ResponseData.class);
		System.out.println(responseData.toString());
		return responseJson;
	}*/

	/**
	 * 根据订单号查询发票 (工具类)
	 * @param orderNo
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws Exception
	 */
	public  ResponseData quiry_order(String orderNo) throws Exception {
		Map map = new HashMap();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmm");
		String dateString = formatter.format(new Date());
		map.put("orderNo",orderNo);
		map.put("scanCodeKey", orderNo);
		map.put("taxpayerCode", "91370200264807TEST4A");
		String param = JSON.toJSONString(map);
		String sign = CertificateUtils.signToBase64(param.getBytes(encode), "D:/PTTEST17.keystore", "PTTEST17",
				"PTTEST17");
		Map<String, String> vars = new HashMap<String, String>();
		vars.put("appCode", URLEncoder.encode("PTTEST17", encode));
		vars.put("cmdName", URLEncoder.encode("chinaeinv.api.order.v11.cx.orderNo", encode));
		vars.put("sign", URLEncoder.encode(sign, encode));
		String responseJson = HttpUtil.doPost("https://www.chinaeinv.com:943/igs/api/orderApi.jspa", vars, param, 10000, 10000);
		ResponseData responseData = JSON.parseObject(responseJson, ResponseData.class);
		return responseData;
	}



}