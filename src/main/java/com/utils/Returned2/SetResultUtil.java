package com.utils.Returned2;


import java.util.List;

public class SetResultUtil {
    public static <T> Result<T> setResult(Result<T> result,String message,Integer code,T returnResult,Boolean flag){
        result.setMessage(message);
        result.setSuccess(flag);
        result.setResult(returnResult);
        result.setCode(code);
        return result;
    }
    public static <T> Result<T> setErrorResult(Result<T> result){
        result.setMessage("发生了错误。");
        result.setSuccess(false);
        result.setResult(null);
        result.setCode(500);
        return result;
    }
    public static <T> Result<T> setExceptionResult(Result<T> result){
        result.setMessage("发生了异常。");
        result.setSuccess(false);
        result.setResult(null);
        result.setCode(500);
        return result;
    }
    public static <T> Result<T> setExceptionResult(Result<T> result,String msg){
        result.setMessage(msg+"发生了异常。");
        result.setSuccess(false);
        result.setResult(null);
        result.setCode(500);
        return result;
    }
    public static <T> Result<T> setErrorMsgResult(Result<T> result,String message,T data){
        result.setMessage(message);
        result.setSuccess(false);
        result.setResult(data);
        result.setCode(500);
        return result;
    }
    public static <T> Result<T> setErrorMsgResult(Result<T> result,String message){
        result.setMessage(message);
        result.setSuccess(false);
        result.setResult(null);
        result.setCode(500);
        return result;
    }
    public static <T> Result<T> setSuccessResult(Result<T> result){
        result.setMessage(ReturnMessage.success);
        result.setSuccess(true);
        result.setResult(null);
        result.setCode(ReturnCode.getSuccess);
        return result;
    }
    public static <T> Result<T> setSuccessResult(Result<T> result,String message){
        result.setMessage(message);
        result.setSuccess(true);
        result.setResult(null);
        result.setCode(ReturnCode.getSuccess);
        return result;
    }
    public static <T> Result<T> setSuccessResult(Result<T> result, String message, List<T> list){
        result.setMessage(message);
        result.setSuccess(true);
        result.setResult(null);
        result.setList(list);
        result.setCode(ReturnCode.getSuccess);
        return result;
    }
    public static <T> Result<T> setSuccessResult(Result<T> result,String message,T data){
        result.setMessage(message);
        result.setSuccess(true);
        result.setResult(data);
        result.setCode(ReturnCode.getSuccess);
        return result;
    }
    public static <T> Result<T> setLackParamResult(Result<T> result,String message){
        result.setMessage(message);
        result.setSuccess(false);
        result.setResult(null);
        result.setCode(ReturnCode.lackParameter);
        return result;
    }
    public static <T> Result<T> setNotFoundResult(Result<T> result,String message){
        result.setMessage(message);
        result.setSuccess(false);
        result.setResult(null);
        result.setCode(ReturnCode.notFound);
        return result;
    }
}