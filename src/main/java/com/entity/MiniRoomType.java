package com.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@TableName("miniroomtype")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="MiniRoomType对象", description="房型设计表")
public class MiniRoomType implements Serializable {
    /**表id*/
    @TableId(type = IdType.UUID)
    @ApiModelProperty(value = "表id")
    private String id;
    // 码 值
    private String code;
    // 描述1
    private String descript1;
    // 类型
    private String cat;
    // 描述
    private String descript;
    // 分组代码
    private String groupcode;
    // 是否支持迷你吧
    private String isTrueMini;
    // 押金倍数
    private String multipleDeposit;
    //房型描述  酒店可添加
    private String description;
}
