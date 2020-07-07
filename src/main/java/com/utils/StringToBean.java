package com.utils;

import java.util.List;
import org.springframework.util.StringUtils;
import java.util.Arrays;

public class StringToBean {


    /**
     * String转List
     */
    public static List<String> StrToJava(String st) {
        if (StringUtils.isEmpty(st)) {
            throw new RuntimeException("StrToJava()异常");
        }
        List<String> listString = Arrays.asList(st.split(","));
        return listString;
    }




}
