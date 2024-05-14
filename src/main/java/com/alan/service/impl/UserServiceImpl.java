package com.alan.service.impl;

import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.alan.mapper.UserMapper;
import com.alan.pojo.domain.User;
import com.alan.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author Alan
 * @description 针对表【user(用户表)】的数据库操作Service实现
 * @createDate 2024-05-13 21:27:27
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    /**
     * 用户注册
     *
     * @param username      用户名
     * @param password      密码
     * @param checkPassword 确认密码
     * @return 用户id
     */
    @Override
    public long userRegister(String username, String password, String checkPassword) {
        //todo 验证码
        //判断输入内容非空
        boolean blank = StrUtil.hasBlank(username, password, checkPassword);
        if (blank) return -1;
        //判断用户名合法--数字，字母，下划线，不小于四位。
        boolean usernameGeneral = Validator.isGeneral(username, 4);
        if (!usernameGeneral) return -2;
        //判断密码合法--数字，字母，下划线，不小于六位。
        boolean passwordGeneral = Validator.isGeneral(password, 6);
        if (!passwordGeneral) return -3;
        //判断二次密码是否一致
        if (!password.equals(checkPassword)) return -4;
        //判断用户名是否重复
        boolean usernameRepeat = this.count(new LambdaQueryWrapper<User>().eq(User::getUsername, username)) > 0;
        if (usernameRepeat) return -5;
        //密码加密
        final String SALT = "ysc";
        String encryptPsw = SecureUtil.md5(SALT+password);
        //插入数据库
        User user = new User();
        user.setUsername(username);
        user.setPassword(encryptPsw);
        boolean result = this.save(user);
        return result?user.getId():-6;
    }
}




