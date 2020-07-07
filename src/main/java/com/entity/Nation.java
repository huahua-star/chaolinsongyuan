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
@TableName("nation")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="nation", description="国籍表")
public class Nation {

    /**表id*/
    @TableId(type = IdType.UUID)
    @ApiModelProperty(value = "表id")
    private String id;

    private String code;

    private String descript1;

    private String descript;

    private String groupcode;


}
