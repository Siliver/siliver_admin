package com.siliver.admin.config;

import com.siliver.admin.config.annotation.Idempotent;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * 用于分布式锁的拦截器
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CustomHandlerInterceptor implements HandlerInterceptor {

    /**
     * 保存redisson创建的线程锁
     */
    private static final ConcurrentHashMap<String, RLock> concurrentHashMap = new ConcurrentHashMap<>();

    /**
     * 使用redisson进行分布式锁实现
     */
    private final RedissonClient redissonClient;

    /**
     * 进行接口前拦截，进行分布式锁添加
     *
     * @param request  current HTTP request 当前请求的request
     * @param response current HTTP response 当前请求的response
     * @param handler  chosen handler to execute, for type and/or instance evaluation 执行的程序
     * @return 切面结果
     */
    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
        if (handler instanceof HandlerMethod handlerMethod) {
            Idempotent idempotent = handlerMethod.getMethod().getAnnotation(Idempotent.class);
            if (Objects.nonNull(idempotent)) {
                Method oneMethod = handlerMethod.getMethod();
                RLock lock = concurrentHashMap.get(oneMethod.getName());
                if (Objects.nonNull(lock) && lock.isLocked()) {
                    return false;
                }
                lock = redissonClient.getLock(oneMethod.getName());
                try {
                    boolean lockFlag = lock.tryLock(100, TimeUnit.SECONDS);
                    if (!lockFlag) {
                        return false;
                    }
                    concurrentHashMap.put(oneMethod.getName(), lock);
                } catch (InterruptedException e) {
                    log.error(String.format("方法：%s，分布式锁加锁失败", oneMethod.getName()), e);
                }
            }
        }
        return true;
    }

    /**
     * 接口返回后解锁
     *
     * @param request      current HTTP request 当前请求的request
     * @param response     current HTTP response 当前请求的response
     * @param handler      the handler (or {@link HandlerMethod}) that started asynchronous
     *                     execution, for type and/or instance examination 执行的程序
     * @param modelAndView the {@code ModelAndView} that the handler returned 模型图像
     *                     (can also be {@code null})
     */
    @Override
    public void postHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler, @Nullable ModelAndView modelAndView) {
        if (handler instanceof HandlerMethod handlerMethod) {
            Idempotent idempotent = handlerMethod.getMethod().getAnnotation(Idempotent.class);
            if (Objects.nonNull(idempotent)) {
                Method oneMethod = handlerMethod.getMethod();
                RLock lock = concurrentHashMap.get(oneMethod.getName());
                if (Objects.nonNull(lock)) {
                    if (lock.isLocked()) {
                        lock.unlock();
                    }
                    concurrentHashMap.remove(oneMethod.getName());
                }
            }
        }
    }

}
