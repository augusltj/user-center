package com.tj.usercenter;

import com.tj.usercenter.model.domain.User;
import com.tj.usercenter.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class UserCenterApplicationTests {
    private static final String SALT = "myPassword";
    @Test
    void contextLoads() {
        String newPassword = DigestUtils.md5DigestAsHex((SALT + "464141").getBytes());
        System.out.println(newPassword);
    }

}
