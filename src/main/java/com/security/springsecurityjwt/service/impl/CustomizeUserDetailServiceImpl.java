package com.security.springsecurityjwt.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.BeanUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.security.springsecurityjwt.dao.RoleDao;
import com.security.springsecurityjwt.dao.UserDao;
import com.security.springsecurityjwt.dto.UserDetailDTO;
import com.security.springsecurityjwt.entity.RoleDo;
import com.security.springsecurityjwt.entity.UserDo;
import com.security.springsecurityjwt.service.CustomizeUserDetailService;
import lombok.var;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


/**
 * @Description:
 * @Created 2022/10/7 13:58
 **/
@Service
public class CustomizeUserDetailServiceImpl extends ServiceImpl<UserDao, UserDo> implements CustomizeUserDetailService {


    @Resource
    private UserDao userDao;

    @Resource
    private RoleDao roleDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        LambdaQueryWrapper<UserDo> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(UserDo::getUsername, username);
        UserDo userInfo = getOne(queryWrapper);
        if (null == userInfo) throw new UsernameNotFoundException("账户不存在");

        UserDetailDTO userDetailDTO = BeanUtil.copyProperties(userInfo, UserDetailDTO.class);

        RoleDo roleDo = roleDao.selectById(userInfo.getId());
        if (null != roleDo) {
            userDetailDTO.setAuthorities(AuthorityUtils.createAuthorityList(roleDo.getRoleName()));
        }
        return User.builder().username(userInfo.getUsername()).password(userInfo.getPassword()).authorities(roleDo.getRoleName()).build();
    }
}
