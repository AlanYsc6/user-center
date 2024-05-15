package com.alan.pojo.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author Alan
 * @Date 2024/5/14 21:45
 * @Description 登录dto
 */
@Data
public class UserRegisterDTO implements Serializable {


    private static final long serialVersionUID = 9113715048574283135L;
    private String username;

    private String password;

    private String checkPassword;
}
