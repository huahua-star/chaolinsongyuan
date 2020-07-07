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
@TableName("race")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="Race对象", description="民族转化表")
public class Race {
    /**表id*/
    @TableId(type = IdType.UUID)
    @ApiModelProperty(value = "表id")
    private String id;

    private String code;

    private String descript;

    private String descript1;

    private String groupcode;

    private String raceid;
}
