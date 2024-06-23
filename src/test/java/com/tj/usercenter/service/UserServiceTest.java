package com.tj.usercenter.service;

import com.tj.usercenter.model.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {

    @Resource
    private UserService userService;

    @Test
    public void testAddUmcUser(){
        User user = new User();
        user.setUsername("ikun");
        user.setUserAccount("123");
        user.setAvatarUrl("https://tse1-mm.cn.bing.net/th/id/OIP-C.BAsxf3GvM0Y1YLTGnFBr0AAAAA?w=123&h=180&c=7&r=0&o=5&dpr=1.5&pid=1.7");
        user.setGender(0);
        user.setUserPassword("xxx");
        user.setPhone("18676442412");
        user.setEmail("123@qq.com");
        boolean result = userService.save(user);
        System.out.println(user.getId());
        assertTrue(result);


    }

    @Test
    void userRegister() {
        String userAccount = "大风歌";
        String userPassword = "12345678";
        String checkPassword = "12345678";
       /* long result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1,result);
        userAccount = "tj";
        result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1,result);
        userAccount = "tuyi";
        userPassword = "123456";
        result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1,result);*/
        userAccount = "ta ji";
        System.out.println(userAccount.replaceAll(" ",""));
        /*long result = userService.userRegister(userAccount.trim(), userPassword, checkPassword);
        Assertions.assertEquals(-1,result);*/
       /* userAccount = "dogtao";
        userPassword = "123456789";
        checkPassword = "123456789";
        result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1,result);
        userAccount = "大风歌jj";
        result = userService.userRegister(userAccount, userPassword, checkPassword);*/
        //assertTrue(result > 0);


    }
}