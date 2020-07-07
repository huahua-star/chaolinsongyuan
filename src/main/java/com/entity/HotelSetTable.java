package com.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@TableName("hotelSetTable")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="hotelSetTable对象", description="酒店配置表")
public class HotelSetTable {
    /**表id*/
    @TableId(type = IdType.UUID)
    @ApiModelProperty(value = "表id")
    private String id;
    //酒店名称
    private String hotelName;
    //机器编号
    private String machineNum;
    //表头规定 离店时间
    private String checkOutTime;
    // 是否支持钟点房
    private String isTrueHourRoom;
    // 是否支持迷你吧
    private String isTrueMini;
    // 是否支持选房
    private String isTrueChooseRoom;
    // 房价码
    private String housPrice;
    // 发布房型
    private String housType;
    // 会员房价码
    private String memberPrice;
    //自助预定
    private String selfHelpBook;
    //入住
    private String checkInName;
    //退房
    private String checkOutName;
    //续住
    private String continueLiveName;
    //自助预定 英文
    private String selfHelpBookEng;
    //入住 英文
    private String checkInNameEng;
    //退房 英文
    private String checkOutNameEng;
    //续住 英文
    private String continueLiveNameEng;
}

