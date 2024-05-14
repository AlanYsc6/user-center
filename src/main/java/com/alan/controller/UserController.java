package com.alan.controller;

import com.alan.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author Alan
 * @Date 2024/5/14 22:04
 * @Description
 */
@RestController
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;

}
