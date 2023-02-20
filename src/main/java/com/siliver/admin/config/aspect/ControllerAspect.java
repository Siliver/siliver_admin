package com.siliver.admin.config.aspect;

import com.alibaba.fastjson2.JSON;
import com.siliver.admin.common.Result;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Aspect
@Component
@Slf4j
public class ControllerAspect {

    /**
     * 定义接口的切点
     */
    @Pointcut("execution(* com.siliver.admin.controller.*Controller.*(..))")
    public void controllerPoint() {
    }

    /**
     * 添加Around环切方法
     *
     * @param joinPoint 扩展切点
     */
    @Around("controllerPoint()")
    public Object beforeCut(ProceedingJoinPoint joinPoint) {
        // 获取当前上下文
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert requestAttributes != null;
        HttpServletRequest httpServletRequest = requestAttributes.getRequest();
        String requestId = UUID.randomUUID().toString();
        // 获取修改请求头
        Enumeration<?> headerNames = httpServletRequest.getHeaderNames();
        Map<String, Object> headersMap = new HashMap<>();
        if (headerNames.hasMoreElements()) {
            String hearName = headerNames.nextElement().toString();
            headersMap.put(hearName, httpServletRequest.getHeader(hearName));
        }
        Object[] arguments = new Object[joinPoint.getArgs().length];
        Object[] args = joinPoint.getArgs();
        for (int i = 0; i < args.length; i++) {
            if ((args[i] instanceof ServletRequest) || (args[i] instanceof ServletResponse) || (args[i] instanceof MultipartFile)) {
                //ServletRequest不能序列化，从入参里排除，否则报异常：java.lang.IllegalStateException: It is illegal to call this method if the current request is not in asynchronous mode (i.e. isAsyncStarted() returns false)
                //ServletResponse不能序列化 从入参里排除，否则报异常：java.lang.IllegalStateException: getOutputStream() has already been called for this response
                continue;
            }
            arguments[i] = args[i];
        }
        log.info("【 {} 】  IP:{}; URL:{}; ClassMethod:{}; ARGS:{}; HeaderInfo:{}", requestId, httpServletRequest.getRemoteAddr(), httpServletRequest.getRequestURI(), joinPoint.getSignature().toString(), JSON.toJSON(arguments), JSON.toJSON(headersMap));
        try {
            Object result = joinPoint.proceed();
            log.info("【 {} 】  ClassMethod:{}; result:{}", requestId, joinPoint.getSignature().toString(), JSON.toJSON(result));
            return result;
        } catch (Throwable e) {
            log.error("【 {} 】 error:{} ", requestId, e.getMessage());
            log.error("异常： ", e);
            return Result.failBuild();
        }
    }
}
