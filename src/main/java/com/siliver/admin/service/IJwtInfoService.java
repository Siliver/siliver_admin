package com.siliver.admin.service;

import com.siliver.admin.common.Result;
import com.siliver.admin.model.VerifyTokenModel;
import com.siliver.admin.response.LoginResponse;

import java.util.List;

/**
 * 用户jwt相关方法
 */
public interface IJwtInfoService {

    /**
     * 登录jwt创建方法
     *
     * @param username 用户名称
     * @param roles    用户角色列表
     * @return 生成登录token
     */
    Result<LoginResponse> createToken(String username, int userId, List<String> roles);

    /**
     * refreshToken刷新
     *
     * @param refreshToken refreshToken
     * @param username     用户姓名
     * @param roles        用户角色
     * @return 刷新结果
     */
    Result<LoginResponse> refreshToken(String refreshToken, String username, int userId, List<String> roles);

    /**
     * 登录token的删除
     *
     * @param token 登录token
     * @return 删除结果
     */
    Result<Void> removeToken(String token);

    /**
     * 进行token的验证
     *
     * @param token 需要验证的token
     * @return 验证结果
     */
    Result<VerifyTokenModel> verifyToken(String token);
}
