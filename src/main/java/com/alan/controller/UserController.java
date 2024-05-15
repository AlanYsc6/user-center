package com.alan.controller;

import cn.hutool.core.util.StrUtil;
import com.alan.pojo.dto.UserLoginDTO;
import com.alan.pojo.dto.UserRegisterDTO;
import com.alan.pojo.vo.UserVO;
import com.alan.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author Alan
 * @Date 2024/5/14 22:04
 * @Description 针对表【user(用户表)】的controller
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public Long userRegister(@RequestBody UserRegisterDTO userRegisterDTO) {
        if (userRegisterDTO == null) {
            log.info("userRegisterDTO is null");
            return -7L;
        }

        String username = userRegisterDTO.getUsername();
        String password = userRegisterDTO.getPassword();
        String checkPassword = userRegisterDTO.getCheckPassword();

        boolean blank = StrUtil.hasBlank(username, password, checkPassword);
        //todo 修改为自定义异常
        if (blank) {
            log.info("username or password or checkPassword is blank");
            return -8L;
        }

        return userService.userRegister(username, password, checkPassword);
    }
    @PostMapping("/login")
    public UserVO userLogin(@RequestBody UserLoginDTO userLoginDTO, HttpServletRequest request) {
        if (userLoginDTO == null) {
            log.info("userLoginDTO is null");
            return null;
        }

        String username = userLoginDTO.getUsername();
        String password = userLoginDTO.getPassword();

        boolean blank = StrUtil.hasBlank(username, password);
        //todo 修改为自定义异常
        if (blank) {
            log.info("username or password is blank");
            return null;
        }

        return userService.userLogin(username, password,request);
    }
}
