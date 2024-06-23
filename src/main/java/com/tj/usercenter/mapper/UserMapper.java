package com.tj.usercenter.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tj.usercenter.model.domain.User;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 29484
* @description 针对表【user(用户表)】的数据库操作Mapper
* @createDate 2024-05-24 19:04:58
* @Entity generator.domain.User
*/
@Mapper
public interface UserMapper extends BaseMapper<User> {

}




