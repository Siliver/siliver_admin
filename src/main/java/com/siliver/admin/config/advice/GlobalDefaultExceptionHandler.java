package com.siliver.admin.config.advice;

import com.siliver.admin.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@Slf4j
@RestControllerAdvice("com.siliver.admin.controller")
public class GlobalDefaultExceptionHandler {

    /**
     * 全局异常
     *
     * @param e 异常信息
     * @return 异常结果
     */
    @ExceptionHandler(Exception.class)
    public Result<String> allExceptionHandler(Exception e) {
        log.error("controller异常：", e);
        return Result.failBuild("系统忙,请稍后再试!!");
    }

    /**
     * 字段验证异常过滤器
     *
     * @param exception 字段验证异常
     * @return 验证错误输出
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Void> validationBodyException(MethodArgumentNotValidException exception) {
        BindingResult result = exception.getBindingResult();
        if (result.hasErrors()) {
            List<ObjectError> errors = result.getAllErrors();
            return Result.failBuild(errors.get(0).getDefaultMessage());
        }
        return Result.failBuild("请填写正确信息");
    }
}
