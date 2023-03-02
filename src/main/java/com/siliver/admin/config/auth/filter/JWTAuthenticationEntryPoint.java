package com.siliver.admin.config.auth.filter;

import com.alibaba.fastjson2.JSON;
import com.siliver.admin.common.Result;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

/**
 * token验证失败节点
 *
 * @author siliver
 */
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        Result<String> result = new Result<>(405, "token失效或未携带token", null);
        response.getWriter().write(JSON.toJSONString(result));
    }

}
