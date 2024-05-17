package com.alan.common;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author Alan
 * @Date 2024/5/17 10:58
 * @Description 通用返回类
 */
@Data
public class BaseResponse<T> implements Serializable {

    private static final long serialVersionUID = -4765301762535502096L;

    private int code;

    private T data;

    private String message;

    public BaseResponse(int code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    public BaseResponse(int code, T data) {
        this(code, data, "");
    }
}
