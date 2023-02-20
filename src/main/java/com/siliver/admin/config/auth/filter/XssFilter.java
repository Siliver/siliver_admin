package com.siliver.admin.config.auth.filter;

import com.siliver.admin.config.http.XssHttpServletRequestWrapper;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * xss过滤器
 */
@Slf4j
public class XssFilter implements Filter {

    /**
     * 静态资源放行
     */
    private final List<String> excludes = Arrays.asList(".html", ".js", ".gif", ".jpg", ".png", ".css", ".ico", ".woff2", ".woff", ".tt", ".ttf");

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (handleExcludeURL((HttpServletRequest) request)) {
            chain.doFilter(request, response);
            return;
        }
        XssHttpServletRequestWrapper xssHttpServletRequestWrapper = new XssHttpServletRequestWrapper((HttpServletRequest) request);
        chain.doFilter(xssHttpServletRequestWrapper, response);
    }

    /**
     * 进行白名单数据排除
     *
     * @param request request
     * @return 判断结果
     */
    private boolean handleExcludeURL(HttpServletRequest request) {
        String path = request.getServletPath();
        if (!StringUtils.hasText(path)) {
            return false;
        }
        return excludes.stream().anyMatch(t -> path.endsWith(t) || path.matches(t));
    }

}
