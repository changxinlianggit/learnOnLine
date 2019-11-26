package com.xuecheng.framework.exception;

import com.xuecheng.framework.model.response.ResultCode;

/**
 * 抛出异常类
 * 只要调用这个类就需要给定一个错误代码
 * 在这个类中统一抛出异常
 */
public class ExceptionCast {

    public static void cast(ResultCode resultCode){
        throw new CustomException(resultCode);
    }

}
