package com.alan.controller;

import cn.hutool.core.util.StrUtil;
import com.alan.common.BaseResponse;
import com.alan.common.ErrorCode;
import com.alan.exception.BusinessException;
import com.alan.pojo.domain.User;
import com.alan.pojo.dto.UserLoginDTO;
import com.alan.pojo.dto.UserRegisterDTO;
import com.alan.pojo.vo.UserVO;
import com.alan.service.UserService;
import com.alan.utils.ResultUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
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
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterDTO userRegisterDTO) {
        log.info("userRegisterDTO: {}", userRegisterDTO);
        if (userRegisterDTO == null) {
            log.info("userRegisterDTO is null");
            throw new BusinessException(ErrorCode.PARAM_ERROR,"注册信息不能为空");
        }

        String userAccount = userRegisterDTO.getUserAccount();
        String userPassword = userRegisterDTO.getUserPassword();
        String checkPassword = userRegisterDTO.getCheckPassword();
        String planetCode = userRegisterDTO.getPlanetCode();

        boolean blank = StrUtil.hasBlank(userAccount, userPassword, checkPassword, planetCode);

        if (blank) {
            log.info("userAccount or userPassword or checkPassword or planetCode is blank");
            throw new BusinessException(ErrorCode.PARAM_NULL,"注册信息不能为空");
        }
        long result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
        return ResultUtils.success(result);
    }

    /**
     * 用户登录
     *
     * @param userLoginDTO 登录信息
     * @param request      请求
     * @return 登录成功后返回用户信息
     */

    @PostMapping("/login")
    public BaseResponse<UserVO> userLogin(@RequestBody UserLoginDTO userLoginDTO, HttpServletRequest request) {
        log.info("userLoginDTO: {}", userLoginDTO);
        if (userLoginDTO == null) {
            log.info("userLoginDTO is null");
            throw new BusinessException(ErrorCode.PARAM_ERROR,"登录信息不能为空");
        }

        String userAccount = userLoginDTO.getUserAccount();
        String userPassword = userLoginDTO.getUserPassword();

        boolean blank = StrUtil.hasBlank(userAccount, userPassword);

        if (blank) {
            log.info("userAccount or userPassword is blank");
            throw new BusinessException(ErrorCode.PARAM_NULL,"登录信息不能为空");
        }

        UserVO userVO = userService.userLogin(userAccount, userPassword, request);
        return ResultUtils.success(userVO);
    }

    /**
     * 获取当前登录用户信息
     *
     * @param request 请求信息
     * @return 当前用户信息
     */
    @GetMapping("/current")
    public BaseResponse<UserVO> getCurrentUser(HttpServletRequest request) {
        if (request.getSession() == null) {
            log.info("session is null");
            throw new BusinessException(ErrorCode.PARAM_NULL,"session is null");
        }
        Object userObj = request.getSession().getAttribute(USER_STATE_LOGIN);
        if (userObj == null) {
            log.info("user is not login");
            throw new BusinessException(ErrorCode.NOT_LOGIN,"用户未登录");
        }
        UserVO userVO = (UserVO) userObj;
        long userVOId = userVO.getId();
        User user = userService.getById(userVOId);
        if (user == null) {
            log.info("user is null");
            throw new BusinessException(ErrorCode.NOT_LOGIN,"用户不存在");
        }
        BeanUtils.copyProperties(user, userVO);
        log.info("userVO: {}", userVO);
        return ResultUtils.success(userVO);
    }

    /**
     * 用户注销
     *
     * @param request 请求信息
     * @return 注销结果
     */
    @PostMapping("/logout")
    public BaseResponse<Integer> userLogout(HttpServletRequest request) {
        if (request.getSession() == null) {
            log.info("session is null");
            throw new BusinessException(ErrorCode.PARAM_NULL,"session is null");
        }

        int logout = userService.userLogout(request);
        return ResultUtils.success(logout);
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
            throw new BusinessException(ErrorCode.NOT_LOGIN,"用户未登录");
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
    public BaseResponse<List<UserVO>> userSearchByName(String username, HttpServletRequest request) {

        if (!isAdmin(request)) {
            log.info("user is not admin");
            throw new BusinessException(ErrorCode.NO_AUTH,"用户无权限");
        }
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        boolean blank = StrUtil.hasBlank(username);
        if (!blank) {
            queryWrapper.like(User::getUsername, username);
        }
        List<User> users = userService.list(queryWrapper);
        List<UserVO> userVos = new ArrayList<>();
        users.forEach(user -> {
            UserVO userVO = new UserVO();
            BeanUtils.copyProperties(user, userVO);
            userVos.add(userVO);
        });
        log.info("query succeeded");
        return ResultUtils.success(userVos);
    }

    /**
     * 删除用户
     *
     * @param id      用户id
     * @param request 请求信息
     * @return 删除结果
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> userDeleteById(@RequestBody Long id, HttpServletRequest request) {
        log.info("id: {}", id);
        if (id <= 0) {
            log.info("id is invalid");
            throw new BusinessException(ErrorCode.PARAM_ERROR,"用户id不合法");
        }
        if (!isAdmin(request)) {
            log.info("user is not admin");
            throw new BusinessException(ErrorCode.NO_AUTH,"用户无权限");
        }
        boolean remove = userService.removeById(id);
        return ResultUtils.success(remove);
    }
}
