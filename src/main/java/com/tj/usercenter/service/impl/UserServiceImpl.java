package com.tj.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tj.usercenter.common.BaseResponse;
import com.tj.usercenter.common.ErrorCode;
import com.tj.usercenter.common.ResultUtils;
import com.tj.usercenter.exception.BusinessException;
import com.tj.usercenter.mapper.UserMapper;
import com.tj.usercenter.service.UserService;
import com.tj.usercenter.model.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.tj.usercenter.contant.UserConstant.USER_LOGIN_STATE;

/**
* @author 29484
 * 用户服务实现类
* @description 针对表【user(用户表)】的数据库操作Service实现
* @createDate 2024-05-24 19:04:58
*/
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService {

     //盐值，混淆密码
    private static final String SALT = "myPassword";

    @Resource
    private UserMapper userMapper;

    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword, String remarkCode) {
        String validPattern = "";//账户不能包含特殊字符的正则表达
        String encryptPassword = "";//加密密码
        QueryWrapper<User> queryWrapper = null;
        User user = null;
        //1.校验
        if (StringUtils.isAnyBlank(userAccount,userPassword,checkPassword,remarkCode)){
            throw new BusinessException(ErrorCode.NULL_ERROR,"请求参数为空");
        }
        if(userAccount.length() < 4){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户账号小于4位");
        }
        if(userPassword.length() < 8 || checkPassword.length() < 8){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户密码小于8位");
        }
        //remarkCode 用户备注编号自己填，长度不能大于5
        if(remarkCode.length() > 5){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户备注编号大于5位");
        }
        //账户不能包含特殊字符
        validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\\\\\[\\\\\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount.replaceAll(" ",""));
        if (matcher.find()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账户包含特殊字符");
        }
        //账户不能重复
        queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount",userAccount);
        long count = userMapper.selectCount(queryWrapper);
        if (count > 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账户重复");
        }
        //用户备注编号不能重复
        queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("remarkCode",remarkCode);
        count = userMapper.selectCount(queryWrapper);
        if (count > 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户备注编号重复");
        }
        //密码和验证密码相同
        if(!userPassword.equals(checkPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"密码和验证密码输入不一致");
        }
        //2.加密
        encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        //3.插入数据
        user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        user.setRemarkCode(remarkCode);
        boolean saveResult = this.save(user);
        if(!saveResult){
            throw new BusinessException(ErrorCode.NOT_SAVE_ERROR,"数据插入数据库保存失败");
        }
        return user.getId();
    }

    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        String validPattern = "";//账户不能包含特殊字符的正则表达
        String encryptPassword = "";//加密密码
        QueryWrapper<User> queryWrapper = null;
        User safetyUser = null;
        //1.校验
        if (StringUtils.isAnyBlank(userAccount,userPassword)){
            throw new BusinessException(ErrorCode.NULL_ERROR,"请求参数为空");
        }
        if(userAccount.length() < 4){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户账号小于4位");
        }
        if(userPassword.length() < 8){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户密码小于8位");
        }
        //账户不能包含特殊字符
        validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\\\\\[\\\\\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount.replaceAll(" ",""));
        if (matcher.find()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账户包含特殊字符");
        }
        //2.加密
        encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        //查询账户是否存在
        queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount",userAccount);
        queryWrapper.eq("userPassword",encryptPassword);
        User user = userMapper.selectOne(queryWrapper);//selectOne查询一条信息
        //用户不存在
        if (user == null){
            //用户登录失败，userAccount与userPassword不匹配
            log.info("user login failed, userAccount cannot match userPassword");
            throw new BusinessException(ErrorCode.NULL_ERROR,"请求的用户不存在");
        }
        //3.用户脱敏
        safetyUser = getSafetyUser(user);
        //4.记录用户的登录态
        request.getSession().setAttribute(USER_LOGIN_STATE,safetyUser);
        return safetyUser;
    }

    /**
     * 用户注销
     * @param request
     */
    @Override
    public int userLogout(HttpServletRequest request) {
        //移除登录态
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return 1;
    }

    /**
     * 用户脱敏
     *
     * @param originUser
     * @return
     */
    @Override
    public User getSafetyUser(User originUser){
        if(originUser == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"请求参数错误");
        }
        User safetyUser = new User();
        safetyUser.setId(originUser.getId());
        safetyUser.setUsername(originUser.getUsername());
        safetyUser.setUserAccount(originUser.getUserAccount());
        safetyUser.setAvatarUrl(originUser.getAvatarUrl());
        safetyUser.setGender(originUser.getGender());
        safetyUser.setPhone(originUser.getPhone());
        safetyUser.setEmail(originUser.getEmail());
        safetyUser.setUserRole(originUser.getUserRole());
        safetyUser.setRemarkCode(originUser.getRemarkCode());
        safetyUser.setUserStatus(originUser.getUserStatus());
        safetyUser.setCreateTime(originUser.getCreateTime());
        return safetyUser;
    }
}




