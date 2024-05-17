package com.alan.utils;

import com.alan.common.BaseResponse;

/**
 * @Author Alan
 * @Date 2024/5/17 11:09
 * @Description 统一返回结果集
 */
public class ResultUtils {
    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(0, data, "success");
    }
}
