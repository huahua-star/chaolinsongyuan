package com.entity;

import lombok.Data;

@Data
public class WestSoftBill {
    private String accnt;//账号
    private String serialnumber;//序号
    private String pccode;//费用码
    private String pccode_descript;//费用码描述
    private String argcode;//账单码
    private String argcode_descript;//账单码描述
    private String remark;//备注
    private String billno;//结账单号
    private String amount;//金额
    private String date_;//日期
    private String empno;//操作员工号
    private String ref1;//第三方交易编号，在线交易updateid
}
