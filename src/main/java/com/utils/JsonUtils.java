package com.utils;

import com.alibaba.fastjson.JSONObject;
import com.entity.Reservation;
import org.springframework.util.StringUtils;


public class JsonUtils {

    /**
     *Json数据转Java对象
     */
    public static Reservation Jsontobean(String json_2) {
        if (StringUtils.isEmpty(json_2)) {
            return null;
        }
        Reservation stud = JSONObject.parseObject(json_2, Reservation.class);
        return stud;
    }

}












