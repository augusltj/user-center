package com.tj.usercenter.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tj.usercenter.common.BaseResponse;
import com.tj.usercenter.model.domain.User;

import javax.servlet.http.HttpServletRequest;

/**
* @author 29484
 * 用户服务
* @description 针对表【user(用户表)】的数据库操作Service
* @createDate 2024-05-24 19:04:58
*/
public interface UserService extends IService<User> {

    /**
     * 用户注册
     * @author 大风歌
     * @param userAccount 用户账户
     * @param userPassword 用户密码
     * @param checkPassword 确认密码
     * @Param remarkCode 用户备注编号
     * @return 新用户 id
     */
    long userRegister(String userAccount, String userPassword, String checkPassword, String remarkCode);

    /**
     * 用户登录
     * @author 大风歌
     * @param userAccount 用户账户
     * @param userPassword 用户密码
     * @return 脱敏后的用户信息
     */
    User userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 用户注销
     */
    int userLogout(HttpServletRequest request);

    /**
     * 用户脱敏
     *
     * @param originUser
     * @return
     */
    User getSafetyUser(User originUser);
}
