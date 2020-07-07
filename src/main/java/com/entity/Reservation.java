package com.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


@Data
@TableName("reservation")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="reservation", description="流水表")
public class Reservation {

    @TableField(exist = false)
    String response;

    /**表id*/
    @TableId(type = IdType.UUID)
    @ApiModelProperty(value = "表id")
    String id;
    //订单号
    String resno;
    //网络订单号
    String crsno;
    //账号
    String accnt;
    //订单状态 I 在住 R预定 O结账 X取消  S临时挂账 D昨日结账 N预定未到
    String sta;
    //同住人信息  （格式：档案号#姓名#预定号#账号 | ......）
    String guestshare;
    //原始房价
    String rmrate;
    // 房价
    String rate;
    //房价代码
    String ratecode;
    //包价
    String packages;
    //市场码
    String market;
    //来源码
    String source;
    //渠道码
    String channel;
    //预定类型
    String restype;
    // 特殊要求码，多用逗号分隔
    String specials;
    //是否房价保密 1:保密，0:不保密
    String secrecy;
    //是否允许 挂房账， 1:允许 0:不允许
    String nopospost;
    //团队号
    String groupno;
    //公司档案号
    String company;
    //旅行社档案号
    String agency;
    //订房中心档案号
    String bcenter;
    //孩子数
    String child;
    //到日 格式 yyyy/mm/dd hh:mm
    String arrival;
    //离店日期 格式 yyyy/mm/dd hh:mm
    String departure;
    //成人数
    String adults;
    //支付方式
    String payMethod;
    //房间数量
    String roomnum;
    //房间号
    String roomno;
    //当前房间状态
    String roomsta;
    //房类码
    String roomtype;
    //房类名称
    String roomtypename;
    //档案号
    String haccnt;
    //名
    String fname;
    //姓
    String lname;
    //全名
    String name;
    //证件类型
    String idcode;
    //证件号码
    String idno;
    //民族代码
    String race;
    //性别
    String sex;
    //生日 yyyy/mm/dd
    String birth;
    //街道
    String street;
    //城市
    String city;
    //邮编
    String postalCode;
    //国籍码
    String country;
    //会员卡号
    String cardno;
    //会员卡类型
    String cardtype;
    //房间特征码，多项用逗号分隔
    String roomfeatures;
    //手机号
    String mobile;
    //主单上电话
    String phone;
    //语种
    String language;
    //客人备注信息
    String guestremark;
    //主单备注信息
    String masterremark;

    /*String billremark;
    String creditcard;
    String ratedetail;
    String totalrates;
    String packagedetail;
    String credit;
    String balance;
    String aliref;
    String is_creditckin;
    String doorcardnum;*/

}
