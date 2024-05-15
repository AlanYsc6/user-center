package com.alan.pojo.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户表
 * @TableName user
 */
@TableName(value ="user")
@Data
public class User implements Serializable {
    /**
     * 用户主键id，自增
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 昵称
     */
    private String name;

    /**
     * 用户名
     */
    private String username;

    /**
     * 头像
     */
    private String acatarurl;

    /**
     * 性别
     */
    private Integer gender;

    /**
     * 密码，非空
     */
    private String password;

    /**
     * 电话
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 用户状态，默认0：正常
     */
    private Integer status;
    /**
     * 用户角色，默认0：普通用户,1管理员
     */
    private Integer userRole;
    /**
     * 创建时间
     */
    private Date createtime;

    /**
     * 更新时间
     */
    private Date updatetime;

    /**
     * 逻辑删除，默认0：否
     */
    @TableLogic
    private Integer isdelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}