package com.siliver.admin.service.impl;

import com.siliver.admin.common.Result;
import com.siliver.admin.model.VerifyTokenModel;
import com.siliver.admin.response.LoginResponse;
import com.siliver.admin.service.IJwtInfoService;
import com.siliver.admin.util.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class JwtInfoServiceImpl implements IJwtInfoService {

    /**
     * token 缓存的前缀
     */
    private static final String ACCESS_TOKEN = "token:";
    /**
     * refesh_token 缓存的前缀
     */
    private static final String REFRESH_TOKEN = "refresh_token:";
    /**
     * 一天的毫秒数
     */
    private static final long ONE_DAY = 86400000L;
    /**
     * redis操作类
     */
    private final StringRedisTemplate stringRedisTemplate;

    /**
     * 进行Token的验证
     *
     * @param username 用户名称
     * @param roles    角色列表
     * @return 生成结果
     */
    @Override
    public Result<LoginResponse> createToken(String username, int userId, List<String> roles) {
        // 进行原Token的删除
        stringRedisTemplate.delete(ACCESS_TOKEN + username);
        stringRedisTemplate.delete(REFRESH_TOKEN + username);
        // 进行token的生成
        String token = JwtTokenUtils.createToken(username, roles, userId, true, false);
        // 进行refresh_token的生成
        String refreshToken = JwtTokenUtils.createToken(username, roles, userId, true, true);
        // 进行redis的保存
        stringRedisTemplate.opsForValue().set(ACCESS_TOKEN + username, token, 1, TimeUnit.DAYS);
        stringRedisTemplate.opsForValue().set(REFRESH_TOKEN + username, refreshToken, 7, TimeUnit.DAYS);
        // 返回结构的拼接
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(token);
        loginResponse.setRefreshToken(refreshToken);
        loginResponse.setCreateTime(System.currentTimeMillis());
        loginResponse.setExpiration(System.currentTimeMillis() + ONE_DAY);
        return Result.successBuild(loginResponse);
    }

    @Override
    public Result<LoginResponse> refreshToken(String refreshToken, String username, int userId, List<String> roles) {
        String saveToken = stringRedisTemplate.opsForValue().get(REFRESH_TOKEN + username);
        if (!StringUtils.hasText(saveToken)) {
            return Result.failBuild("刷新Token已经失效！");
        }
        if (!saveToken.equals(refreshToken)) {
            return Result.failBuild("刷新Token不正确！");
        }
        // 进行refreshToken的合法性
        if (!JwtTokenUtils.verifyRefreshToken(saveToken, username)) {
            return Result.failBuild("刷新Token不是当前用户！");
        }
        return createToken(username, userId, roles);
    }

    @Override
    public Result<Void> removeToken(String token) {
        // 进行token的解析
        String userName = JwtTokenUtils.getUsername(token);
        // 进行redis的查询
        if (Boolean.TRUE.equals(stringRedisTemplate.hasKey(ACCESS_TOKEN + userName))) {
            stringRedisTemplate.delete(ACCESS_TOKEN + userName);
        }
        if (Boolean.TRUE.equals(stringRedisTemplate.hasKey(REFRESH_TOKEN + userName))) {
            stringRedisTemplate.delete(REFRESH_TOKEN + userName);
        }
        return Result.successBuild();
    }

    @Override
    public Result<VerifyTokenModel> verifyToken(String token) {
        // 从token中获取用户名
        String username = JwtTokenUtils.getUsername(token);
        // 从token中获取角色
        List<String> roles = JwtTokenUtils.getUserRole(token);
        // 从token中获取角色编号
        int userId = JwtTokenUtils.getUserId(token);
        // 进行redis校验
        Boolean tokenFlag = stringRedisTemplate.hasKey(ACCESS_TOKEN + username);
        // 进行判断
        if (Boolean.TRUE.equals(tokenFlag)) {
            VerifyTokenModel verifyTokenModel = new VerifyTokenModel();
            verifyTokenModel.setUsername(username);
            verifyTokenModel.setUserId(userId);
            verifyTokenModel.setRoles(roles);
            return Result.successBuild(verifyTokenModel);
        }
        return Result.failBuild();
    }
}
