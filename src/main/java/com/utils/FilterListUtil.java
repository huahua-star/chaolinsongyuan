package com.utils;

import com.entity.Xrorder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class FilterListUtil {

    public static List<Xrorder> filterList(List<Xrorder> list) throws ParseException {
        List<Xrorder> resultList=new ArrayList<>();
        if (list.size()>0){
            for(Xrorder k : list){
                Date nowDate=new Date();
                Date checkInDate=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(k.getActualCheckInDate());
                Date checkOutDate=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(k.getActualCheckOutDate());
                if (checkInDate.before(nowDate)&&nowDate.before(checkOutDate)){
                    k.setIsFlag("1");
                }else{
                    k.setIsFlag("0");
                }
                if (!k.getResvStatus().equalsIgnoreCase("CHECKOUT")&&!k.getResvStatus().equalsIgnoreCase("CANCEL")&&!k.getResvStatus().equalsIgnoreCase("NOSHOW")){
                    resultList.add(k);
                }
            }
        }
        return resultList;
    }
    //人数小于等于1的订单
    public static List<Xrorder> filterListNumber(List<Xrorder> list) throws ParseException {
        List<Xrorder> resultList=new ArrayList<>();
        if (list.size()>0) {
            for (Xrorder Xrorder : list){
//                if (StringUtil.isNullOrEmpty(Xrorder.getAccompanyId())) {
                if ((Xrorder.getAccompanyId())==null ||(Xrorder.getAccompanyId().isEmpty())) {
                    resultList.add(Xrorder);
                }
            }
        }
        return resultList;
    }
    //未入住的订单
    public static List<Xrorder> filterListNumberZero(List<Xrorder> list) throws ParseException {
        List<Xrorder> resultList=new ArrayList<>();
        if (list.size()>0) {
            for (Xrorder Xrorder : list){
//                if (StringUtil.isNullOrEmpty(Xrorder.getAccompanyId())&& "0".equals(Xrorder.getNameId())) {
                if (((Xrorder.getAccompanyId())==null||Xrorder.getAccompanyId().isEmpty()) && "0".equals(Xrorder.getNameId())) {
                    resultList.add(Xrorder);
                }
            }
        }
        return resultList;
    }
    //人数等于1的订单
    public static List<Xrorder> filterListNumberOne(List<Xrorder> list) throws ParseException {
        List<Xrorder> resultList=new ArrayList<>();
        if (list.size()>0) {
            for (Xrorder Xrorder : list){
//                if (StringUtil.isNullOrEmpty(Xrorder.getAccompanyId())&&!"0".equals(Xrorder.getNameId())) {
                if ((Xrorder.getAccompanyId()==null|| Xrorder.getAccompanyId().isEmpty())&&!"0".equals(Xrorder.getNameId())) {
                    resultList.add(Xrorder);
                }
            }
        }
        return resultList;
    }

    public static List<Xrorder> fiterCheckStateChinese(List<Xrorder> list){
        Map<String,String> map=new HashMap<>();
        map.put("CHECKIN","已入住");
        map.put("DUEIN","预抵");
        List<Xrorder> resultList=new ArrayList<>();
        if (list.size()>0){
            for (Xrorder Xrorder : list){
                Xrorder.setCheckInState(map.get(Xrorder.getResvStatus()));
                resultList.add(Xrorder);
            }
        }
        return resultList;
    }

    public static String conversion(String conversion){
        Map<String,String> map=new HashMap<>();
        map.put("ALIPAY","0");
        map.put("WXPAY","1");
        map.put("YLPAY","2");
        return map.get(conversion);
    }


}
