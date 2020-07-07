package com.utils;

import com.service.XRHotelServiceSoap;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class applicationContextJava {

    public static XRHotelServiceSoap XRHotels() {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        XRHotelServiceSoap soap = context.getBean(XRHotelServiceSoap.class);
        return soap;
    }

    public static String Jsons(String parameter) {
        JSONObject object = XML.toJSONObject(parameter);
        String jsonData = object.toString();
        return jsonData;
    }


}
