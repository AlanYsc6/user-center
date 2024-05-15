package com.alan.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户表
 * @TableName user
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserVO implements Serializable {


    private static final long serialVersionUID = 1101506795029262301L;
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
}