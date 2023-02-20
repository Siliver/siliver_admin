package com.siliver.admin.config.auth.interceptor;

import com.alibaba.fastjson2.JSON;
import com.siliver.admin.common.Result;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

public class JWTAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        Result<String> result = new Result<>(403, "没有权限", null);
        response.getWriter().write(JSON.toJSONString(result));
    }
}
