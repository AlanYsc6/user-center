package com.alan.service;

import com.alan.pojo.domain.User;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {
    @Autowired
    private UserService userService;

    /**
     * 测试添加用户
     */
    @Test
    void testAddUser() {
        User user = new User();

        user.setUsername("杨轩");
        user.setUserAccount("Alan");
        user.setAcatarurl("https://gitee.com/alanysc/image/raw/master/user9.jpeg");
        user.setGender(1);
        user.setUserPassword("123456");
        user.setPhone("17516569585");
        user.setEmail("alanysc@qq.com");

        boolean result = userService.save(user);
        System.out.println(user.getId());
        assertTrue(result);
    }

    /**
     * 测试修改用户
     */
    @Test
    void testUpdateUser() {
        User user = new User();
        user.setId(1L);
        user.setUserAccount("alan");
        boolean result = userService.updateById(user);
        System.out.println(result);
        assertNotNull(user);

    }

    @Test
    void userRegister() {
        String userAccount = "Alan";
        String userPassword = "";
        String checkPassword = "123456";
        long result = userService.userRegister(userAccount, userPassword, checkPassword);
        assertEquals(-1, result);
        userAccount = "A lan";
        userPassword = "123456";
        result = userService.userRegister(userAccount, userPassword, checkPassword);
        assertEquals(-2, result);
        userAccount = "Alan";
        userPassword = "12.456";
        result = userService.userRegister(userAccount, userPassword, checkPassword);
        assertEquals(-3, result);
        userPassword = "12345678";
        result = userService.userRegister(userAccount, userPassword, checkPassword);
        assertEquals(-4, result);
        userAccount = "alan";
        userPassword = "123456";
        result = userService.userRegister(userAccount, userPassword, checkPassword);
        assertEquals(-5, result);
        userAccount = "Alan";
        result = userService.userRegister(userAccount, userPassword, checkPassword);
        assertTrue(result > 0);
    }
    /**
     * 测试eq
     */
    @Test
    void testEq(){
        User userAccountRepeat = userService.getOne(new LambdaQueryWrapper<User>().eq(User::getUserAccount, "ALAN"));
        System.out.println(userAccountRepeat);
    }
}