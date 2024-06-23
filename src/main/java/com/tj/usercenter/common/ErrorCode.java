package com.tj.usercenter.common;

/**
 * 错误码
 * @author 大风歌
 */
public enum ErrorCode {
    SUCCESS(0,"success",""),
    PARAMS_ERROR(40000,"请求参数错误",""),
    NULL_ERROR(40001,"请求参数为空",""),
    NOT_LOGIN_ERROR(40100,"未登录",""),
    NOT_AUTH_ERROR(40101,"无权限",""),
    NOT_SAVE_ERROR(40102,"数据保存失败",""),
    SYSTEM_ERROR(50000,"系统内部异常","");

    /**
     * 错误代码
     */
    private final int code;
    /**
     * 错误信息
     */
    private final String message;
    /**
     * 错误信息详情
     */
    private final String description;

    ErrorCode(int code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getDescription() {
        return description;
    }
}
