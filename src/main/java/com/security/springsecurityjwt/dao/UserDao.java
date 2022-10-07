package com.security.springsecurityjwt.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.security.springsecurityjwt.dto.UserDetailDTO;
import com.security.springsecurityjwt.entity.UserDo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description:
 * @Created 2022/10/7 21:14
 **/
@Mapper
public interface UserDao extends BaseMapper<UserDo> {
    UserDetailDTO getUser(@Param(value = "username") String username);
}
