package com.siliver.admin.common;

import com.siliver.admin.neum.ResultCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 返回通用结构
 *
 * @param <T> 泛型
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "通用返回实体")
public class Result<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 42L;

    @Schema(description = "返回结果编号；200是成功")
    private int code;

    @Schema(description = "失败原因信息")
    private String message;

    @Schema(description = "实际返回结构")
    private T data;

    public static <T> Result<T> successBuild(T data) {
        Result<T> result = new Result<>();
        result.setCode(ResultCode.SUCCESS_CODE.getCode());
        result.setMessage(ResultCode.SUCCESS_CODE.getMessage());
        result.setData(data);
        return result;
    }

    public static <T> Result<T> successBuild() {
        Result<T> result = new Result<>();
        result.setCode(ResultCode.SUCCESS_CODE.getCode());
        result.setMessage(ResultCode.SUCCESS_CODE.getMessage());
        return result;
    }

    public static <T> Result<T> failBuild(String message) {
        Result<T> result = new Result<>();
        result.setCode(ResultCode.FAIL_CODE.getCode());
        result.setMessage(message);
        return result;
    }

    public static <T> Result<T> failBuild() {
        Result<T> result = new Result<>();
        result.setCode(ResultCode.FAIL_CODE.getCode());
        result.setMessage(ResultCode.FAIL_CODE.getMessage());
        return result;
    }

    public static <T> Result<T> failBuild(int code, String message) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }
}
