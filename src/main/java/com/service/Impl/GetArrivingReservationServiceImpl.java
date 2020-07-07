package com.service.Impl;

import com.entity.Reservation;
import com.service.GetArrivingReservationService;
import com.service.XRHotelServiceSoap;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.utils.JsonUtils.Jsontobean;
import static com.utils.StringToBean.StrToJava;

@Service
public class GetArrivingReservationServiceImpl implements GetArrivingReservationService {

    @Override
    public String XmlToJavaBean(String resno, String roomno,String name,String accnt) {
        String xmls = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<interface>\n" +
                "<item>\n" ;
                if(!StringUtils.isEmpty(name)){
                    xmls+="<name>" + name + "</name>\n" ;
                }
                if(!StringUtils.isEmpty(accnt)){
                    xmls+="<accnt>" + accnt + "</accnt>\n" ;
                }
                xmls+= "<resno>" + resno + "</resno>\n" +
                "<roomno>" + roomno + "</roomno>\n" +
                "</item>\n" +
                "</interface>";
        System.out.println("xmls:"+xmls);
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        XRHotelServiceSoap soap = context.getBean(XRHotelServiceSoap.class);
        final String inhouse = soap.getInHouseReservation(xmls);
        JSONObject object = XML.toJSONObject(inhouse);
        String jsonData = object.toString();
        System.out.println("jsonData:"+jsonData);
        com.alibaba.fastjson.JSONObject jsonObj = com.alibaba.fastjson.JSONObject.parseObject(jsonData);
        com.alibaba.fastjson.JSONObject responseObj=jsonObj.getJSONObject("response");
        String result=responseObj.getString("result");
        if (null==result || !"0".equals(result)){
            return "2";//该客人已退房
        }
        Reservation jsontobean = Jsontobean(jsonData);
        String response = jsontobean.getResponse();
        List<String> list = StrToJava(response);
        String gue = list.get(8);
        String image = gue.substring(gue.lastIndexOf(":") + 10);
        String[] arr=(image.substring(0,image.length()-1)).split("\\|");
        System.out.println("arr:"+arr.length);
        if (arr.length>1){
            return "0";//有同住客 无法退房
        }else{
            return  "1";// 单个客人可以退房
        }
    }


}
