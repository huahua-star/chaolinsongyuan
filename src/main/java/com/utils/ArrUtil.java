package com.utils;

import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ArrUtil {

    //判断集合是否为空
    public static boolean isnull(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    //判断Map是否为空
    public static boolean isnull(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    //判断数组是否为空
    public static boolean isnull(Object[] array) {
        return array == null || array.length == 0;
    }

    //判断List是否为空
    public static boolean isnull(List<Object> list) {
        return list == null || list.size() == 0;
    }

    //判断字符串是否为空
    public static boolean isnull(Object str) {
        return str == null || "".equals(str);
    }


    /**
     * 检查字符是否为 null || ""
     * 如果是 null 或者 "" 则返回false ,反则为 true
     * @param value 需要验证的多个字符，以英文逗号分隔
     * @return
     */
    public static boolean isnull(String... value) {
        int count = 0;
        for (int i = 0; i < value.length; i++) {
            //遍历字符数组所有的参数，发现某个为 null 或者 "" ,则跳出
            if (StringUtils.isEmpty(value[i])) {
                return false;
            }
            count++;
        }
        if (count == value.length) {
            return true;
        }
        return false;
    }



}
