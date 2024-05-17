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

import static com.alan.constant.UserConstant.USER_STATE_LOGIN;

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
    /**
     * 用户注册
     *
     * @param userAccount      用户名
     * @param userPassword      密码
     * @param checkPassword 确认密码
     * @param planetCode 星球编号
     * @return 用户id
     */
    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword,String planetCode) {
        //todo 验证码
        //判断输入内容非空
        boolean blank = StrUtil.hasBlank(userAccount, userPassword, checkPassword,planetCode);
        //todo 修改为自定义异常
        if (blank) {
            log.info("用户注册失败，输入内容为空");
            return -1;
        }
        //判断用户名合法--数字，字母，下划线，不小于四位。
        boolean userAccountGeneral = Validator.isGeneral(userAccount, 4);
        if (!userAccountGeneral) {
            log.info("用户注册失败，用户名不合法");
            return -2;
        }
        //判断密码合法--数字，字母，下划线，不小于六位。
        boolean passwordGeneral = Validator.isGeneral(userPassword, 6);
        if (!passwordGeneral) {
            log.info("用户注册失败，密码不合法");
            return -3;
        }
        //判断星球编号合法--数字，不大于5位。
        if (!(Validator.isNumber(planetCode)&&Validator.isGeneral(planetCode, 2,5))) {
            log.info("用户注册失败，星球编号不合法");
            return -4;
        }
        //判断二次密码是否一致
        if (!userPassword.equals(checkPassword)) {
            log.info("用户注册失败，二次密码不一致");
            return -5;
        }
        //判断用户名是否重复
        boolean userAccountRepeat = this.count(new LambdaQueryWrapper<User>().eq(User::getUserAccount, userAccount)) > 0;
        if (userAccountRepeat) {
            log.info("用户注册失败，用户名已存在");
            return -6;
        }
        //判断星球编号是否重复
        boolean planetCodeRepeat = this.count(new LambdaQueryWrapper<User>().eq(User::getPlanetCode, planetCode)) > 0;
        if (planetCodeRepeat) {
            log.info("用户注册失败，星球编号已存在");
            return -7;
        }
        //密码加密
        String encryptPsw = SecureUtil.md5(SALT+userPassword);
        //插入数据库
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPsw);
        user.setPlanetCode(planetCode);
        boolean result = this.save(user);
        if(result){
            log.info("用户注册成功");
            return user.getId();
        } else {
            log.info("用户注册失败");
            return -8;
        }
    }

    /**
     * 用户登录
     *
     * @param userAccount      用户名
     * @param userPassword      密码
     * @param request      请求信息
     * @return 用户信息(脱敏)
     */
    @Override
    public UserVO userLogin(String userAccount,String userPassword,HttpServletRequest request) {
        //判断输入内容非空
        boolean blank = StrUtil.hasBlank(userAccount, userPassword);
        //todo 修改为自定义异常
        if (blank) {
            log.info("用户登录失败，输入内容为空");
            return null;
        }
        //判断用户名合法--数字，字母，下划线，不小于四位。
        boolean userAccountGeneral = Validator.isGeneral(userAccount, 4);
        if (!userAccountGeneral) {
            log.info("用户登录失败，用户名不合法");
            return null;
        }
        //判断密码合法--数字，字母，下划线，不小于六位。
        boolean passwordGeneral = Validator.isGeneral(userPassword, 6);
        if (!passwordGeneral) {
            log.info("用户登录失败，密码不合法");
            return null;
        }
        //密码加密
        String encryptPsw = SecureUtil.md5(SALT+userPassword);
        //查询数据库
        User user = this.getOne(new LambdaQueryWrapper<User>().eq(User::getUserAccount, userAccount).eq(User::getUserPassword, encryptPsw));
        if(user==null){
            log.info("用户登录失败，用户名或密码错误");
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        request.getSession().setAttribute(USER_STATE_LOGIN,userVO);
        log.info("用户登录成功");
        return userVO;
    }

    /**
     * 用户注销
     *
     * @param request 请求信息
     * @return 注销结果
     */
    @Override
    public int userLogout(HttpServletRequest request) {
        request.getSession().removeAttribute(USER_STATE_LOGIN);
        return 1;
    }
}




