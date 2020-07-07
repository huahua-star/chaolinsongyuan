package com.entity;

import lombok.Data;
/**
 * 根据到店/离店日期查询房型数量
 *
 * 可用房间信息显示
 *
 */
@Data
public class Roommessage {
    private String hotalName;
    private String type;
    private String number;
    private String description;
    private String price;
    private String features;
    private String rateCode;
    private String packages;
    private String isSmoke;//是否 禁烟  0 禁烟  1 不禁烟
    private String isBreakfast;//是否包含早餐  0 不包含 1 包含
}
