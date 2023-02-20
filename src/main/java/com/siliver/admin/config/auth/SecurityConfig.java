package com.siliver.admin.config.auth;

import com.siliver.admin.config.auth.filter.JWTAuthenticationEntryPoint;
import com.siliver.admin.config.auth.filter.JwtAuthorizationFilter;
import com.siliver.admin.config.auth.filter.XssFilter;
import com.siliver.admin.config.auth.interceptor.JWTAccessDeniedHandler;
import com.siliver.admin.config.custom.CustomSecurityConfig;
import com.siliver.admin.service.IJwtInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.header.writers.CrossOriginResourcePolicyHeaderWriter;
import org.springframework.security.web.header.writers.XXssProtectionHeaderWriter;
import org.springframework.util.CollectionUtils;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * Security配置类
 *
 * @author siliver
 */
@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomSecurityConfig customSecurityConfig;

    private final IJwtInfoService jwtInfoService;

    /**
     * 定义筛选器链
     *
     * @param http 身份验证管理器
     * @return 自定义筛选器链
     * @throws Exception 配置信息异常
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // 进行请求头的修改
        http.headers()
                .xssProtection(xXssConfig -> xXssConfig.headerValue(XXssProtectionHeaderWriter.HeaderValue.DISABLED))
                .frameOptions(HeadersConfigurer.FrameOptionsConfig::deny).crossOriginResourcePolicy(crossOriginResourcePolicyConfig -> crossOriginResourcePolicyConfig.policy(CrossOriginResourcePolicyHeaderWriter.CrossOriginResourcePolicy.CROSS_ORIGIN));
        // 进行cors的修改
        http.cors(corsConfigurer -> corsConfigurer.configurationSource(corsConfigurationSource()));
        // 进行rememberMe的修改，走前后端分离模式，纯webapi方式
        http.rememberMe().disable();
        // 前后端分离模式关闭缓存
        http.requestCache().disable();
        // 前后端分离模式，使用自定义token验证，csrf跨站点验证
        http.csrf().disable();
        // 关闭登录页面
        http.formLogin().disable();
        // 分离模式，关闭登出
        http.logout().disable();
        // 跳过验证的url
        http.authorizeHttpRequests().requestMatchers(customSecurityConfig.getSkip().toArray(new String[0])).anonymous();
        // 放行URL
        http.authorizeHttpRequests().requestMatchers(customSecurityConfig.getWhite().toArray(new String[0])).permitAll();
        // 动态角色权限验证添加
        if (!CollectionUtils.isEmpty(customSecurityConfig.getDynamic())) {
            customSecurityConfig.getDynamic().forEach(t -> {
                try {
                    http.authorizeHttpRequests().requestMatchers(t.getUrl().toArray(new String[0])).hasAuthority(t.getName());
                } catch (Exception e) {
                    log.error("动态配置凭借失败！");
                }
            });
        }
        // 其他所有请求需要身份认证
        http.authorizeHttpRequests().anyRequest().authenticated()
                .and()
                // 不需要session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                // 用户权限拦截器
                .addFilterBefore(new JwtAuthorizationFilter(jwtInfoService), BasicAuthenticationFilter.class)
                // 安全防护：开启xss过滤
                .addFilterBefore(new XssFilter(), JwtAuthorizationFilter.class)
                // 没有携带token或者token无效拦截器
                .exceptionHandling().authenticationEntryPoint(new JWTAuthenticationEntryPoint())
                // 添加无权限时的处理拦截器
                .accessDeniedHandler(new JWTAccessDeniedHandler());
        return http.build();
    }

    /**
     * 跨域配置
     *
     * @return 跨域配置
     */
    private CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(true);
        // todo 后续需要修改为接受来源的域名
        corsConfiguration.addAllowedOriginPattern(CorsConfiguration.ALL);
        corsConfiguration.addAllowedHeader(CorsConfiguration.ALL);
        corsConfiguration.addAllowedMethod(CorsConfiguration.ALL);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // todo 后续需要修改为实际需要的跨源地址
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }

}
