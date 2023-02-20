package com.siliver.admin.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@Schema(title = "WeChatLoginRequest", description = "微信登录参数的")
public class WeChatLoginRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 42L;

    @NotBlank(message = "微信登录code不能为空")
    @Schema(description = "微信登录code")
    private String code;
}
