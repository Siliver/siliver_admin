package com.siliver.admin.request;

import com.siliver.admin.neum.LoginEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 登录请求参数
 *
 * @author siliver
 */
@Data
@Schema(title = "LoginRequest", description = "登录请求参数")
public class LoginRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 42L;

    @NotBlank(message = "用户名称不能为空")
    @Size(min = 4, max = 15, message = "长度在4-15")
    @Schema(description = "用户名称")
    private String username;

    @NotBlank(message = "用户密码不能为空")
    @Size(min = 4, max = 20, message = "长度在4-20")
    @Schema(description = "用户密码")
    private String password;

    @NotNull(message = "登录类型不能为空")
    @Schema(description = "登录类型")
    private LoginEnum loginType;

    @NotBlank(message = "验证码不能为空")
    @Schema(description = "验证码")
    private String verifyCode;

    @NotBlank(message = "验证码不能为空")
    @Schema(description = "验证码请求编号")
    private String requestOnce;
}
