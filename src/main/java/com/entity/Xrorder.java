package com.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("Xrorder")
@ApiModel(value="Xrorder对象", description="订单对象")
public class Xrorder implements Serializable {
    /**表id*/
    @TableId(type = IdType.UUID)
    @ApiModelProperty(value = "表id")
    private String id;
    /*订单号   resv_name_id  */
    @ApiModelProperty(value = "订单号")
    private String resvNameId;
    /*个人信息id  name_id*/
    private String nameId;
    /* 姓名全拼         alt_name */
    private String altName;
    /* 姓         last_name*/
    private String lastName;
    /* 名      first_name*/
    private String firstName;
    /*      language*/
    private String language;
    /*
    订单状态 resv_status
    分为 checkin  入住状态
         duein  预抵达状态
    */
    private String resvStatus;
    /*      订单类型  resv_type
            记住 checkin 和 6 pm     */
    private String resvType;
    /*  该订单的房价   rate_finally    */
    private String rateFinally;
    /*    价格代码    rate_code   */
    private String rateCode;
    /*   包价   Packages      */
    private String packages;
    /*   房间类型  room_type  */
    private String roomType;
    /*   房间号  room_no  */
    private String roomNo;
    /*  层数  floor   ????  */
    private String floor;
    /*  过夜  数量   ????  nights             */
    private String nights;
    /*  预定入住时间 begin_date */
    private String beginDate;
    /*  预定离店时间 end_date */
    private String endDate;
    /*  original_end_date   */
    private String originalEndDate;
    /* 实际预定入住时间  actual_check_in_date  */
    private String actualCheckInDate;
    /* 实际预定离店时间  actual_check_out_date  */
    private String actualCheckOutDate;
    /*  手机号   phone */
    private String phone;
    /* 身份证号 identityCard */
    private String idEntityCard;
    /* 是否为同住人 isAccompany   默认 0 不是同住人  1 是同住人   */
    private String isAccompany;
    /*性别   gender   */
    private String gender;
    /* 生日 borthday    */
    private String borthday;
    /* 民族     nation   */
    private String nation;
    /*籍贯地址   address  */
    private String address;
    /* 证件类型  ：身份证 ：ID, 护照：PSP   */
    private String certificateType;
    /* 国籍  nationality*/
    private String nationality;
    /* 是否可以入住 0 不能入住 1 可以入住     是否 创建 订单成功 0 不成功 1 成功*/
    @TableField(exist = false)
    private String isFlag;
    /* 订单 备注   */
    private String message;
    /* payment_method   支付方式  */
    private String paymentMethod;
    /* no_post  是否允许挂账  1 不允许 0 可以    */
    private String noPost;
    /* chanel_code  订单来源    */
    private String chanelCode;
    /*同住人id集  accompany_id   */
    private String accompanyId;
    /* 房间名称  room_name    */
    private String roomName;
    /* crs_id   crs id  */
    private String  crsId;
    /* field135   酒店名称  */
    private String hotalName;
    /* country1  国籍  中文  */
    private String cnationality;
    /*id_type  中文证件类型   */
    private String idType;
    /*block_id 团队id*/
    private String blockId;
    /*block 团队名称*/
    private String block;
    /*不知道具体是什么 应该有用*/
    private String isBlock;
    /*中文 入住状态*/
    @TableField(exist = false)
    private String checkInState;
    /*PAD  预授权金额*/
    private String preAmount;
}
