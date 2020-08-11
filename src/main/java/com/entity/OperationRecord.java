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
@TableName("operationrecord")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="OperationRecord对象", description="客人操作记录表")
public class OperationRecord {
    /**
     * 表id
     */
    @TableId(type = IdType.UUID)
    @ApiModelProperty(value = "表id")
    private String id;
    //订单号
    private String resno;
    //客人姓名
    private String name;
    //操作名称
    private String operation;
    //操作详情
    private String operationDes;
    //操作时间
    private String createTime;
    //修改时间
    private String updateTime;
    //状态 0失败 1成功
    private String state;
}
