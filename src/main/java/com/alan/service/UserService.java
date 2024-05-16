package com.alan.service;

import com.alan.pojo.domain.User;
import com.alan.pojo.vo.UserVO;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
* @author Alan
* @description 针对表【user(用户表)】的数据库操作Service
* @createDate 2024-05-13 21:27:27
*/
public interface UserService extends IService<User> {
    /**
     * 用户注册
     * @param userAccount 用户名
     * @param userPassword 密码
     * @param checkPassword 确认密码
     * @return 用户id
     */
    long userRegister(String userAccount, String userPassword,String checkPassword);

    /**
     * 用户登录
     * @param userAccount 用户名
     * @param userPassword 密码
     * @param request 请求信息
     * @return 用户信息(脱敏)
     */
    UserVO userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 用户注销
     * @param request 请求信息
     * @return 注销结果
     */
    int userLogout(HttpServletRequest request);
}
