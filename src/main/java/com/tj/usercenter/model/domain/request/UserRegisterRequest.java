package com.tj.usercenter.model.domain.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户注册请求体
 *
 * @author 大风歌
 */
@Data
public class UserRegisterRequest implements Serializable {

    private static final long serialVersionUID = 3582374610437125212L;
    //用户账户
    private String userAccount;
    //用户密码
    private String userPassword;
    //校验密码
    private String checkPassword;
    //用户备注代码
    private String remarkCode;
}
