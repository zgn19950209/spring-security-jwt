package com.security.springsecurityjwt.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.security.springsecurityjwt.entity.RoleDo;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description:
 * @Created 2022/10/7 21:14
 **/
@Mapper
public interface RoleDao extends BaseMapper<RoleDo> {
}
