package com.siliver.admin.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
@Schema(description = "登录返回实体")
public class LoginResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 42L;

    @Schema(description = "后台创建时间")
    private long createTime;

    @Schema(description = "当此生成的token")
    private String token;

    @Schema(description = "刷新使用的token")
    private String refreshToken;

    @Schema(description = "登录成功：过期时间/登录失败：解锁时间")
    private long Expiration;

    @JsonIgnore
    private List<String> roles;
}
