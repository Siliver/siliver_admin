package com.siliver.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.siliver.admin.common.Result;
import com.siliver.admin.dto.UserDto;
import com.siliver.admin.request.LoginRequest;
import com.siliver.admin.response.LoginResponse;

/**
 * 用户相关方法
 */
public interface IUserService extends IService<UserDto> {

    /**
     * 登录方法
     *
     * @param loginRequest 登录参数
     * @return 登录返回
     */
    Result<LoginResponse> userLogin(LoginRequest loginRequest, UserDto userDto);

    /**
     * 用户状态验证
     *
     * @param username 用户姓名
     * @return 验证结果
     */
    Result<UserDto> judgeUserStatus(String username);
}
