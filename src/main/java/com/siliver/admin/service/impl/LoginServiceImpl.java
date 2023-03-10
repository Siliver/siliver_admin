package com.siliver.admin.service.impl;

import com.siliver.admin.common.Result;
import com.siliver.admin.dto.RoleDto;
import com.siliver.admin.dto.UserDto;
import com.siliver.admin.model.WeChatCreateModel;
import com.siliver.admin.neum.LoginEnum;
import com.siliver.admin.neum.ResultCode;
import com.siliver.admin.request.LoginRequest;
import com.siliver.admin.request.WeChatLoginRequest;
import com.siliver.admin.response.LoginResponse;
import com.siliver.admin.response.WeChatLoginCustomResponse;
import com.siliver.admin.service.*;
import com.siliver.admin.util.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 登录服务实现
 *
 * @author siliver
 */
@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements ILoginService {

    private final IUserService userService;

    private final IVerifyCodeGen verifyCodeGen;

    private final IJwtInfoService jwtInfoService;

    private final IRoleService roleService;

    private final IWeChatService weChatService;

    @Override
    public Result<LoginResponse> loginService(LoginRequest loginRequest) {
        // 判断是否是验证码登录，验证码登录需要进行验证码验证
        if (LoginEnum.WITH_CAPTCHA.equals(loginRequest.getLoginType())) {
            Result<Void> result = verifyCodeGen.verifyCode(loginRequest.getRequestOnce(), loginRequest.getVerifyCode());
            if (ResultCode.SUCCESS_CODE.getCode() != result.getCode()) {
                return Result.failBuild(result.getCode(), result.getMessage());
            }
        }
        // 进行用户信息的验证，并获取用户信息
        Result<UserDto> userDtoResult = userService.judgeUserStatus(loginRequest.getUsername());
        if (ResultCode.LOCK_CODE.getCode() == userDtoResult.getCode()) {
            LoginResponse loginResponse = new LoginResponse();
            loginResponse.setExpiration(userDtoResult.getData().getExpiration());
            return new Result<>(userDtoResult.getCode(), userDtoResult.getMessage(), loginResponse);
        } else if (ResultCode.SUCCESS_CODE.getCode() != userDtoResult.getCode()) {
            return Result.failBuild(userDtoResult.getCode(), userDtoResult.getMessage());
        }
        UserDto userDto = userDtoResult.getData();
        // 进行用户登录
        Result<LoginResponse> temp = userService.userLogin(loginRequest.getPassword(), userDto, loginRequest.getIp());
        if (ResultCode.SUCCESS_CODE.getCode() != temp.getCode()) {
            return temp;
        }
        // 进行用户角色的查询
        List<RoleDto> roles = roleService.getRolesByUserAndGroupService(userDto.getId());
        List<String> roleCodes = null;
        if (!CollectionUtils.isEmpty(roles)) {
            roleCodes = roles.stream().map(RoleDto::getRoleCode).toList();
        }
        return jwtInfoService.createToken(loginRequest.getUsername(), userDto.getId(), roleCodes);
    }

    @Override
    public Result<LoginResponse> refreshTokenService(String refreshToken, String username) {
        // token 修改
        refreshToken = refreshToken.replace(JwtTokenUtils.TOKEN_PREFIX, "");
        // 进行refreshToken的验证
        Boolean refreshFlag = JwtTokenUtils.verifyRefreshToken(refreshToken, username);
        if (!refreshFlag) {
            return Result.failBuild("该refreshToken不正确！");
        }
        // 进行roles的获取
        List<String> roles = JwtTokenUtils.getUserRoleByRefresh(refreshToken);
        // 进行userId的获取
        int userId = JwtTokenUtils.getUserIdByRefresh(refreshToken);
        // 验证用户信息
        Result<UserDto> userDtoResult = userService.judgeUserStatus(username);
        if (ResultCode.SUCCESS_CODE.getCode() != userDtoResult.getCode()) {
            LoginResponse loginResponse = new LoginResponse();
            loginResponse.setExpiration(userDtoResult.getData().getExpiration());
            return new Result<>(userDtoResult.getCode(), userDtoResult.getMessage(), loginResponse);
        }
        // 进行jwt的刷新
        return jwtInfoService.refreshToken(refreshToken, username, userId, roles);
    }

    @Override
    public Result<Void> loginOutService(String token) {
        token = token.replace(JwtTokenUtils.TOKEN_PREFIX, "");
        return jwtInfoService.removeToken(token);
    }

    /**
     * 小程序登录服务
     *
     * @param weChatLoginRequest 登录参数
     * @return 登录结果
     */
    @Override
    public Result<WeChatLoginCustomResponse> weChatLoginService(WeChatLoginRequest weChatLoginRequest) {
        Result<WeChatCreateModel> temp = weChatService.weChatLoginService(weChatLoginRequest);
        if (ResultCode.SUCCESS_CODE.getCode() == temp.getCode()) {
            WeChatCreateModel weChatCreateModel = temp.getData();
            LoginRequest loginRequest = new LoginRequest();
            loginRequest.setUsername(weChatCreateModel.getUsername());
            loginRequest.setPassword(weChatCreateModel.getPassword());
            loginRequest.setLoginType(LoginEnum.WITH_NON);
            Result<LoginResponse> tempResult = loginService(loginRequest);
            if (ResultCode.SUCCESS_CODE.getCode() != tempResult.getCode()) {
                return Result.failBuild(tempResult.getMessage());
            }
            WeChatLoginCustomResponse weChatLoginCustomResponse = new WeChatLoginCustomResponse();
            BeanUtils.copyProperties(tempResult.getData(), weChatLoginCustomResponse);
            weChatLoginCustomResponse.setOpenId(weChatCreateModel.getOpenId());
            weChatLoginCustomResponse.setUnionId(weChatCreateModel.getUnionId());
            return Result.successBuild(weChatLoginCustomResponse);
        }
        return Result.failBuild(temp.getMessage());
    }

}
