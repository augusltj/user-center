package com.tj.usercenter.common;

/**
 * 返回结果工具类
 * @author 大风歌
 */
public class ResultUtils {
    /**
     * 成功
     * @param data
     * @param <T>
     * @return
     */
    public static <T> BaseResponse<T> success(T data){
        return new BaseResponse<>(0,data,"success");
    }

    /**
     * 失败
     * @param errorCode
     * @return
     */
    public static  BaseResponse error(ErrorCode errorCode){
        return new BaseResponse<>(errorCode);
    }

    /**
     * 失败
     * @param errorCode
     * @param message
     * @param desc
     * @return
     */
    public static  BaseResponse error(ErrorCode errorCode,String message,String desc){
        return new BaseResponse<>(errorCode.getCode(),null,message,desc);
    }

    /**
     * 失败
     * @param code
     * @param message
     * @param desc
     * @return
     */
    public static  BaseResponse error(int code,String message,String desc){
        return new BaseResponse<>(code,null,message,desc);
    }

    /**
     * 失败
     * @param errorCode
     * @param desc
     * @return
     */
    public static  BaseResponse error(ErrorCode errorCode,String desc){
        return new BaseResponse<>(errorCode.getCode(),errorCode.getMessage(),desc);
    }
}
