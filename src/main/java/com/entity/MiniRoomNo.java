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
@TableName("miniroomno")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="miniroomno对象", description="房间配置表")
public class MiniRoomNo implements Serializable {
    /**表id*/
    @TableId(type = IdType.UUID)
    @ApiModelProperty(value = "表id")
    private String id;

    private String roomno;

    private String isTrueMini;

    private String roomname;

    private String description;


}
