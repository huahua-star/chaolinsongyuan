package com.utils.Returned2;

/**
 * 返回码
 */
public class ReturnCode {
    //缺少参数
    public static final Integer lackParameter=400;
    //参数错误
    public static final Integer parameterError=400;
    //数据库错误
    public static final Integer erorDateBase=500;
    //逻辑错误
    public static final Integer logicError=500;
    //未找到
    public static final Integer notFound=404;
    //get成功
    public static final Integer getSuccess=200;
    //post成功
    public static final Integer postSuccess=201;
    //预授权超额
    public static final Integer preExcess=202;
}
