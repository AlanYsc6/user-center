package com.alan.controller;

import cn.hutool.core.util.StrUtil;
import com.alan.pojo.domain.User;
import com.alan.pojo.dto.UserLoginDTO;
import com.alan.pojo.dto.UserRegisterDTO;
import com.alan.pojo.vo.UserVO;
import com.alan.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

import static com.alan.constant.UserConstant.USER_ROLE_ADMIN;
import static com.alan.constant.UserConstant.USER_STATE_LOGIN;

/**
 * @Author Alan
 * @Date 2024/5/14 22:04
 * @Description 针对表【user(用户表)】的controller
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;

    /**
     * 用户注册
     *
     * @param userRegisterDTO 注册信息
     * @return 注册成功后返回用户id
     */
    @PostMapping("/register")
    public Long userRegister(@RequestBody UserRegisterDTO userRegisterDTO) {
        log.info("userRegisterDTO: {}", userRegisterDTO);
        if (userRegisterDTO == null) {
            log.info("userRegisterDTO is null");
            return -7L;
        }

        String userAccount = userRegisterDTO.getUserAccount();
        String userPassword = userRegisterDTO.getUserPassword();
        String checkPassword = userRegisterDTO.getCheckPassword();

        boolean blank = StrUtil.hasBlank(userAccount, userPassword, checkPassword);
        //todo 修改为自定义异常
        if (blank) {
            log.info("userAccount or userPassword or checkPassword is blank");
            return -8L;
        }
        return userService.userRegister(userAccount, userPassword, checkPassword);
    }

    /**
     * 用户登录
     *
     * @param userLoginDTO 登录信息
     * @param request      请求
     * @return 登录成功后返回用户信息
     */

    @PostMapping("/login")
    public UserVO userLogin(@RequestBody UserLoginDTO userLoginDTO, HttpServletRequest request) {
        log.info("userLoginDTO: {}", userLoginDTO);
        if (userLoginDTO == null) {
            log.info("userLoginDTO is null");
            return null;
        }

        String userAccount = userLoginDTO.getUserAccount();
        String userPassword = userLoginDTO.getUserPassword();

        boolean blank = StrUtil.hasBlank(userAccount, userPassword);
        //todo 修改为自定义异常
        if (blank) {
            log.info("userAccount or userPassword is blank");
            return null;
        }

        return userService.userLogin(userAccount, userPassword, request);
    }

    /**
     * 获取当前登录用户信息
     * @param request 请求信息
     * @return 当前用户信息
     */
    @GetMapping("/current")
    public UserVO getCurrentUser(HttpServletRequest request) {
        if (request.getSession() == null) {
            log.info("session is null");
            return null;
        }
        Object userObj = request.getSession().getAttribute(USER_STATE_LOGIN);
        if (userObj == null) {
            log.info("user is not login");
            return null;
        }
        UserVO userVO = (UserVO) userObj;
        long userVOId = userVO.getId();
        User user = userService.getById(userVOId);
        if(user == null){
            log.info("user is null");
            return null;
        }
        BeanUtils.copyProperties(user, userVO);
        log.info("userVO: {}", userVO);
        return userVO;
    }

    /**
     * 用户注销
     *
     * @param request 请求信息
     * @return 注销结果
     */
    @PostMapping("/logout")
    public Integer userLogout(HttpServletRequest request) {
        if (request.getSession() == null) {
            log.info("session is null");
            return null;
        }
        //todo 修改为自定义异常
        return userService.userLogout(request);
    }
    /**
     * 用户鉴权
     *
     * @param request 请求信息
     * @return 鉴权结果
     */
    private Boolean isAdmin(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(USER_STATE_LOGIN);
        if (userObj == null) {
            log.info("user is not login");
            return false;
        }
        UserVO userVO = (UserVO) userObj;
        return Objects.equals(userVO.getUserRole(), USER_ROLE_ADMIN);
    }


    /**
     * 查询用户
     *
     * @param username 昵称
     * @param request  请求信息
     * @return 用户集合
     */

    @GetMapping("/search")
    public List<User> userSearchByName(String username, HttpServletRequest request) {
        log.info("username: {},session: {}", username,request.getSession().getAttribute(USER_STATE_LOGIN));
        boolean blank = StrUtil.hasBlank(username);
        if (blank) {
            log.info("username is blank");
            return null;
        }
        if (!isAdmin(request)) {
            log.info("user is not admin");
            return null;
        }
        log.info("query succeeded");
        return userService.list(new LambdaQueryWrapper<User>().like(User::getUsername, username));
    }

    /**
     * 删除用户
     *
     * @param id      用户id
     * @param request 请求信息
     * @return 删除结果
     */
    @PostMapping("/delete")
    public Boolean userDeleteById(@RequestBody Long id, HttpServletRequest request) {
        log.info("id: {}", id);
        if(id<=0){
            log.info("id is invalid");
            return false;
        }
        if (!isAdmin(request)) {
            log.info("user is not admin");
            return false;
        }
        return userService.removeById(id);
    }
}
