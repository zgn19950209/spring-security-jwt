package com.security.springsecurityjwt.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @Description:
 * @Created 2022/10/7 21:14
 **/
@Data
@TableName(value = "user")
public class UserDo {

    @TableId
    private String id;

    @TableField
    private String username;

    @TableField
    private String password;

    @TableField
    private String roleId;

}
