package com.siliver.admin.config.auth.filter;

import com.siliver.admin.common.Result;
import com.siliver.admin.config.http.HeaderHttpServletRequestWrapper;
import com.siliver.admin.model.VerifyTokenModel;
import com.siliver.admin.neum.ResultCode;
import com.siliver.admin.service.IJwtInfoService;
import com.siliver.admin.util.JwtTokenUtils;
import eu.bitwalker.useragentutils.DeviceType;
import eu.bitwalker.useragentutils.UserAgent;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static com.siliver.admin.neum.CommonValue.*;

/**
 * 身份验证方法，不能注册为Bean,否则或交由spring管理，而不是Security管理
 *
 * @author siliver
 */
@Slf4j
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final IJwtInfoService jwtInfoService;

    public JwtAuthorizationFilter(IJwtInfoService jwtInfoService) {
        this.jwtInfoService = jwtInfoService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain chain) {
        String tokenHeader = request.getHeader(JwtTokenUtils.TOKEN_HEADER);
        // 如果请求头中没有Authorization信息则直接放行了
        if (tokenHeader == null || !tokenHeader.startsWith(JwtTokenUtils.TOKEN_PREFIX)) {
            try {
                chain.doFilter(request, response);
            } catch (IOException | ServletException e) {
                log.error("没有身份时，继续其他过滤器，异常", e);
            }
            return;
        }
        //设置用户身份授权
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = getAuthentication(tokenHeader);
        if (Objects.nonNull(usernamePasswordAuthenticationToken)) {
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            // 进行请求头添加修改
            request = modifyHeader(request, String.valueOf(usernamePasswordAuthenticationToken.getPrincipal()), Integer.parseInt(String.valueOf(usernamePasswordAuthenticationToken.getCredentials())));
        }
        // 添加请求头信息
        try {
            chain.doFilter(request, response);
        } catch (IOException | ServletException e) {
            log.error("有身份时，继续其他过滤器，异常", e);
        }
    }

    /**
     * 这里从token中获取用户信息并新建一个token
     *
     * @param tokenHeader token请求头
     * @return 用户密码身份TOKEN
     */
    private UsernamePasswordAuthenticationToken getAuthentication(String tokenHeader) {
        String token = tokenHeader.replace(JwtTokenUtils.TOKEN_PREFIX, "");
        Result<VerifyTokenModel> verifyToken = jwtInfoService.verifyToken(token);
        if (ResultCode.SUCCESS_CODE.getCode() == verifyToken.getCode()) {
            String username = verifyToken.getData().getUsername();
            List<String> roles = verifyToken.getData().getRoles();
            int userId = verifyToken.getData().getUserId();
            // 验证token合法性，添加redis获取验证
            return new UsernamePasswordAuthenticationToken(username, userId, AuthorityUtils.createAuthorityList(roles.toArray(new String[0])));
        }
        return null;
    }

    /**
     * 请求头修改私有方法
     * 添加userAgent请求头解析
     *
     * @param request  请求头
     * @param userCode 用户编号
     */
    private HeaderHttpServletRequestWrapper modifyHeader(HttpServletRequest request, String userCode, int userId) {
        // 进行用户信息的添加
        HeaderHttpServletRequestWrapper httpServletRequest = new HeaderHttpServletRequestWrapper(request);
        if (StringUtils.hasText(USER_CODE)) {
            httpServletRequest.addHeader(USER_CODE, userCode);
        }
        if (StringUtils.hasText(USER_ID)) {
            httpServletRequest.addHeader(USER_ID, String.valueOf(userId));
        }
        // 进行userAgent 分析添加自定义请求头
        String userAgentString = httpServletRequest.getHeader(USER_AGENT);
        //解析agent字符串
        UserAgent userAgent = UserAgent.parseUserAgentString(userAgentString);
        if (Objects.nonNull(userAgent.getOperatingSystem())) {
            DeviceType deviceType = userAgent.getOperatingSystem().getDeviceType();
            switch (deviceType) {
                case MOBILE, TABLET -> httpServletRequest.addHeader(USER_CHANNEL, "1");
                case COMPUTER -> httpServletRequest.addHeader(USER_CHANNEL, "2");
                default -> httpServletRequest.addHeader(USER_CHANNEL, "0");
            }
        }
        return httpServletRequest;
    }
}
