package com.siliver.admin.controller;

import com.siliver.admin.common.Result;
import com.siliver.admin.model.VerifyCodeModel;
import com.siliver.admin.request.LoginRequest;
import com.siliver.admin.response.LoginResponse;
import com.siliver.admin.service.ILoginService;
import com.siliver.admin.service.IVerifyCodeGen;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@Tag(name = "LoginController", description = "登录相关")
@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
public class LoginController {

    private final ILoginService loginService;

    /**
     * 验证码相关接口
     */
    private final IVerifyCodeGen verifyCodeGen;

    /**
     * 管理端登录
     *
     * @param loginRequest 登录请求参数
     * @return 登录结果
     */
    @Operation(description = "用户登录接口", summary = "登录相关接口")
    @PostMapping("/login")
    public Result<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        return loginService.loginService(loginRequest);
    }

    /**
     * 进行token的刷新
     *
     * @param refreshToken 刷新使用的token
     * @param username     用户姓名
     * @return 刷新后的token
     */
    @Operation(description = "token刷新", summary = "登录相关接口")
    @PostMapping("/refresh_token")
    public Result<LoginResponse> refreshToken(
            @Parameter(name = "Authorization", in = ParameterIn.HEADER, required = true, description = "身份验证TOKEN") @RequestHeader("Authorization") String refreshToken,
            @Parameter(name = "username", in = ParameterIn.QUERY, required = true, description = "用户名称") @RequestParam("username") String username
    ) {
        return loginService.refreshTokenService(refreshToken, username);
    }

    /**
     * 登出逻辑
     *
     * @param token 登录token
     * @return 登出结果
     */
    @Operation(description = "用户登出接口", summary = "登录相关接口")
    @PostMapping("/loginOut")
    public Result<Void> loginOut(@Parameter(name = "Authorization", in = ParameterIn.HEADER, required = true, description = "身份验证TOKEN") @RequestHeader("Authorization") String token) {
        return loginService.loginOutService(token);
    }

    /**
     * 获取验证码
     *
     * @param response 响应
     */
    @Operation(description = "获取验证码", summary = "登录相关接口")
    @GetMapping(value = "/verify_code", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    public void verifyCode(HttpServletResponse response) {
        // 设置长宽
        VerifyCodeModel verifyCodeModel = verifyCodeGen.generate(80, 28);
        String code = verifyCodeModel.getRequestOnce();
        // 设置响应绑定验证码
        response.setHeader("requestOnce", code);
        // 设置响应头
        response.setHeader("Pragma", "no-cache");
        // 在代理服务服务器端防止缓冲
        response.setDateHeader("Expires", 0);
        try (ServletOutputStream servletOutputStream = response.getOutputStream()) {
            servletOutputStream.write(verifyCodeModel.getImgBytes());
            servletOutputStream.flush();
        } catch (IOException e) {
            log.error("验证码获取接口输出流获取异常", e);
        }
    }

}
