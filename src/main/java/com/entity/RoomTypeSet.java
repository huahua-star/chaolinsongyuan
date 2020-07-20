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
@TableName("roomtypeset")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="roomtypeset对象", description="房型设置表")
public class RoomTypeSet {
    /**表id*/
    @TableId(type = IdType.UUID)
    @ApiModelProperty(value = "表id")
    private String id;
    //房型type
    private String roomType;
    // 描述
    private String descript;
    //房价码
    private String priceCode;
    //房间价格
    private String roomPrice;
    // 押金倍数
    private String multipleDeposit;

}
