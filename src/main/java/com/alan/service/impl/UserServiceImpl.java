package com.alan.service.impl;

import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.alan.mapper.UserMapper;
import com.alan.pojo.domain.User;
import com.alan.pojo.vo.UserVO;
import com.alan.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Alan
 * @description 针对表【user(用户表)】的数据库操作Service实现
 * @createDate 2024-05-13 21:27:27
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    private static final String SALT = "ysc";
    private static final String USER_LOGIN_STATE = "userLoginState";
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
        //todo 修改为自定义异常
        if (blank) {
            log.info("用户注册失败，输入内容为空");
            return -1;
        }
        //判断用户名合法--数字，字母，下划线，不小于四位。
        boolean usernameGeneral = Validator.isGeneral(username, 4);
        if (!usernameGeneral) {
            log.info("用户注册失败，用户名不合法");
            return -2;
        }
        //判断密码合法--数字，字母，下划线，不小于六位。
        boolean passwordGeneral = Validator.isGeneral(password, 6);
        if (!passwordGeneral) {
            log.info("用户注册失败，密码不合法");
            return -3;
        }
        //判断二次密码是否一致
        if (!password.equals(checkPassword)) {
            log.info("用户注册失败，二次密码不一致");
            return -4;
        }
        //判断用户名是否重复
        boolean usernameRepeat = this.count(new LambdaQueryWrapper<User>().eq(User::getUsername, username)) > 0;
        if (usernameRepeat) {
            log.info("用户注册失败，用户名已存在");
            return -5;
        }
        //密码加密
        String encryptPsw = SecureUtil.md5(SALT+password);
        //插入数据库
        User user = new User();
        user.setUsername(username);
        user.setPassword(encryptPsw);
        boolean result = this.save(user);
        if(result){
            log.info("用户注册成功");
            return user.getId();
        } else {
            log.info("用户注册失败");
            return -6;
        }
    }

    /**
     * 用户登录
     *
     * @param username      用户名
     * @param password      密码
     * @param request      请求信息
     * @return 用户信息(脱敏)
     */
    @Override
    public UserVO userLogin(String username,String password,HttpServletRequest request) {
        //判断输入内容非空
        boolean blank = StrUtil.hasBlank(username, password);
        //todo 修改为自定义异常
        if (blank) {
            log.info("用户登录失败，输入内容为空");
            return null;
        }
        //判断用户名合法--数字，字母，下划线，不小于四位。
        boolean usernameGeneral = Validator.isGeneral(username, 4);
        if (!usernameGeneral) {
            log.info("用户登录失败，用户名不合法");
            return null;
        }
        //判断密码合法--数字，字母，下划线，不小于六位。
        boolean passwordGeneral = Validator.isGeneral(password, 6);
        if (!passwordGeneral) {
            log.info("用户登录失败，密码不合法");
            return null;
        }
        //密码加密
        String encryptPsw = SecureUtil.md5(SALT+password);
        //查询数据库
        User user = this.getOne(new LambdaQueryWrapper<User>().eq(User::getUsername, username).eq(User::getPassword, encryptPsw));
        if(user==null){
            log.info("用户登录失败，用户名或密码错误");
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        request.setAttribute(USER_LOGIN_STATE,userVO.getName());
        log.info("用户登录成功");
        return userVO;
    }
}




