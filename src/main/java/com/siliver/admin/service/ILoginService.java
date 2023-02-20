package com.siliver.admin.service;

import com.siliver.admin.common.Result;
import com.siliver.admin.request.LoginRequest;
import com.siliver.admin.response.LoginResponse;

/**
 * 登录抽象接口
 */
public interface ILoginService {

    /**
     * 登录方法
     *
     * @param loginRequest 登录请求参数
     * @return 登录结果
     */
    Result<LoginResponse> loginService(LoginRequest loginRequest);

    /**
     * 进行token的刷新
     *
     * @param refreshToken 刷新使用的token
     * @param username     用户姓名
     * @return 刷新后的token
     */
    Result<LoginResponse> refreshTokenService(String refreshToken, String username);

    /**
     * 登出逻辑
     *
     * @param token 登录token
     * @return 登出结果
     */
    Result<Void> loginOutService(String token);

}
