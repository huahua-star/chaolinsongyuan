package com.entity;

import java.io.Serializable;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 流水表
 * @Author: jeecg-boot
 * @Date:   2019-09-16
 * @Version: V1.0
 */
@Data
@TableName("xr_tbl_txn_p")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="xr_tbl_txn_p对象", description="流水表")
public class TblTxnp implements Serializable{
    
	/**表id*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "表id")
	private String id;
	/**商家订单号*/
	@ApiModelProperty(value = "商家订单号")
	private String orderid;//商家订单号
	/**预定号*/
	@ApiModelProperty(value = "预订号")
	private String preOrderid;//凯莱酒店订单号
	/**支付方式 0支付宝 1微信 2银行卡"*/
    @ApiModelProperty(value = "支付方式 0支付宝 1微信 2银行卡")
	private String paymethod;
	/**支付类型 0 普通支付 1预授权支付*/
    @ApiModelProperty(value = "支付类型 0 普通支付 1预授权支付")
	private String paytype;
	/**房间号*/
	@ApiModelProperty(value = "房间号")
	private String roomnum;
	/**消费金额    当流水状态为3时，该字段为确切预授权完成字段   */
    @ApiModelProperty(value = "消费金额")
	private java.math.BigDecimal amount;
	/**预授权金额*/
    @ApiModelProperty(value = "预授权金额")
	private java.math.BigDecimal preamount;
	/**银行卡号*/
	@ApiModelProperty(value = "银行卡号")
	private java.lang.String cardno;
	/**状态 0 撤销 1 未支付 2 已支付*/
    @ApiModelProperty(value = "状态 0预授权撤销/退款 1 未支付 2 已支付 3预授权完成 4预授权完成的撤销")
	private String state;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
	private String createBy;
	/**预订入住时间*/
    @ApiModelProperty(value = "预订入住时间")
	private String createTime;
	/**修改人*/
    @ApiModelProperty(value = "修改人")
	private String updateBy;
	/**预订离店时间*/
    @ApiModelProperty(value = "预订离店时间")
	private String updateTime;

	@TableField(exist = false)
	private Xrorder Xrorder;
}
