package com.alan.service;

import com.alan.pojo.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author Alan
* @description 针对表【user(用户表)】的数据库操作Service
* @createDate 2024-05-13 21:27:27
*/
public interface UserService extends IService<User> {
    /**
     * 用户注册
     * @param username 用户名
     * @param password 密码
     * @param checkPassword 确认密码
     * @return 用户id
     */
    long userRegister(String username, String password,String checkPassword);
}
