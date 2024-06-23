package com.tj.usercenter.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 通用返回对象类
 * @param -code 错误码
 * @param -data 数据
 * @paran message 提示信息
 * @author 大风歌
 */
@Data
public class BaseResponse<T> implements Serializable {
    private int code;
    private T data;
    private String message;
    private String description;

    public BaseResponse(int code, T data, String message, String description) {
        this.code = code;
        this.data = data;
        this.message = message;
        this.description = description;
    }

    public BaseResponse(int code, T data, String message) {
      this(code,data,message,"");
    }

    public BaseResponse(int code, T data) {
        this(code,data,"");
    }

    public BaseResponse(ErrorCode errorCode){
        this(errorCode.getCode(),null,errorCode.getMessage(),errorCode.getDescription());
    }
}
