package com.tj.usercenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tj.usercenter.common.BaseResponse;
import com.tj.usercenter.common.ErrorCode;
import com.tj.usercenter.common.ResultUtils;
import com.tj.usercenter.exception.BusinessException;
import com.tj.usercenter.model.domain.User;
import com.tj.usercenter.model.domain.request.UserLoginRequest;
import com.tj.usercenter.model.domain.request.UserRegisterRequest;
import com.tj.usercenter.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.tj.usercenter.contant.UserConstant.ADMIN_ROLE;
import static com.tj.usercenter.contant.UserConstant.USER_LOGIN_STATE;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Resource
    private UserService userService;

    /**
     * 用户注册
     * @param userRegisterRequest 用户注册请求参数体
     * @return
     */
    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest){
        if (userRegisterRequest == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"请求参数错误");
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        String remarkCode = userRegisterRequest.getRemarkCode();
        //校验请求参数是否为空
        if(StringUtils.isAnyBlank(userAccount,userPassword,checkPassword,remarkCode)){
            throw new BusinessException(ErrorCode.NULL_ERROR,"请求参数为空");
        }
        long result = userService.userRegister(userAccount, userPassword, checkPassword, remarkCode);
        return ResultUtils.success(result);
    }

    /**
     * 用户登录
     * @param userLoginRequest 用户登录请求体
     * @param request 请求体
     * @return 用户信息
     */
    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request){
        if (userLoginRequest == null){
            throw new BusinessException(ErrorCode.NULL_ERROR,"请求参数为空");
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        //校验请求参数是否为空
        if(StringUtils.isAnyBlank(userAccount,userPassword)){
            throw new BusinessException(ErrorCode.NULL_ERROR,"请求参数为空");
        }
        User user = userService.userLogin(userAccount, userPassword, request);
        if(user == null)
            System.out.println("登录失败！");
        else
            System.out.println("登录成功！");
        return ResultUtils.success(user);
    }

    /**
     * 用户注销
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public BaseResponse<Integer> userLogout(HttpServletRequest request){
        if (request == null){
            throw new BusinessException(ErrorCode.NULL_ERROR,"请求参数为空");
        }
        int result = userService.userLogout(request);
        if(result == 1){
            System.out.println("注销成功！");
        }else {
            System.out.println("注销失败！");
        }
        return ResultUtils.success(result);
    }

    @GetMapping("current")
    public BaseResponse<User> getCurrentUser(HttpServletRequest request){
        //获取用户登录态
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User userCurrent = (User) userObj;
        if (userCurrent == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"请求参数错误");
        }
        Long userId = userCurrent.getId();
        //todo 校验用户是否合法
        User user = userService.getById(userId);
        User safetyUser = userService.getSafetyUser(user);
        return ResultUtils.success(safetyUser);
    }

    @GetMapping("/search")
    public BaseResponse<List<User>> searchUsers(String username,HttpServletRequest request){
        if(!isAdmin(request)){
            throw new BusinessException(ErrorCode.NOT_AUTH_ERROR,"该用户无管理员权限");
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if(StringUtils.isAnyBlank(username)){
            //todo 无法查询用户信息
            queryWrapper.like("username",username);
        }
        List<User> userList = userService.list(queryWrapper);
        List<User> list = userList.stream().map(user -> userService.getSafetyUser(user)).collect(Collectors.toList());
        return ResultUtils.success(list);
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUsers(@RequestBody long id,HttpServletRequest request){
        if(!isAdmin(request)){
            throw new BusinessException(ErrorCode.NOT_AUTH_ERROR,"该用户无管理员权限");
        }
        if(id <= 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"请求参数错误");
        }
        boolean resultFlag = userService.removeById(id);
        return ResultUtils.success(resultFlag);
    }

    /**
     * //鉴权 是否为管理员 公共方法
     * @param request
     * @return
     */
    private boolean isAdmin(HttpServletRequest request){
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userObj;
        return user !=null && user.getUserRole() == ADMIN_ROLE;
    }

}
