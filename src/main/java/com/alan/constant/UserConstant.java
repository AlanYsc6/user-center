package com.alan.constant;

/**
 * @Author Alan
 * @Date 2024/5/15 12:14
 * @Description 用户常量
 */
public interface UserConstant {
    /**
     * 用户登录状态
     */
    String USER_LOGIN_STATE = "userLoginState";
    //----------权限-------
    /**
     * 普通用户权限
     */
    Integer USER_COMMON_ROLE = 0;
    /**
     * 管理员权限
     */
    Integer USER_ADMIN_ROLE = 1;
}
