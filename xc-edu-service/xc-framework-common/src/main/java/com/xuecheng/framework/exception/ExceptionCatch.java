package com.xuecheng.framework.exception;

import com.google.common.collect.ImmutableMap;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.model.response.ResultCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 统一 异常捕获类
 */
@ControllerAdvice//控制器增强注解
public class ExceptionCatch {

    private static final Logger LOGGER= LoggerFactory.getLogger(ExceptionCatch.class);

    //使用EXCEPTIONS存放异常类型和错误代码的映射，ImmutableMap的特点的一旦创建不可改变，并且线程安全
    private static ImmutableMap<Class<? extends Throwable>,ResultCode> EXCEPTIONS;
    //使用builder来构建一个异常类型和错误代码的异常
    protected static ImmutableMap.Builder<Class<? extends Throwable>,ResultCode> builder = ImmutableMap.builder();
    //加上这个注解就表示，我要捕获哪一类的异常，只要有抛出这个类的异常，我这个方法就执行
    @ExceptionHandler(CustomException.class)
    @ResponseBody//返回json字符串
    public ResponseResult customException(CustomException e) {
        //输出日志
        LOGGER.error("catch exception : {}\r\nexception: ",e.getMessage(), e);
        //获取错误代码
        ResultCode resultCode = e.getResultCode();
        return new ResponseResult(resultCode);
    }

    @ExceptionHandler(Exception.class)
    public ResponseResult exception(Exception e){
        LOGGER.error("catch exception : {}\r\nexception: ",e.getMessage(), e);
        //如果说这个map集合里面没有数据，就构建一个
        if(EXCEPTIONS==null){
            EXCEPTIONS=builder.build();
        }
        //通过给定异常区获取ResultCode
        ResultCode resultCode = EXCEPTIONS.get(e.getClass());

        //如果是空的就返回通用的异常
        if(resultCode==null){
            return new ResponseResult(CommonCode.SERVER_ERROR);
        }
        return new ResponseResult(resultCode);

    }

    static{
        //在这里加入一些基础的异常类型判断
        builder.put(HttpMessageNotReadableException.class,CommonCode.SERVER_ERROR);
    }

}
