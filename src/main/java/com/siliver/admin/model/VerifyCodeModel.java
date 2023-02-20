package com.siliver.admin.model;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 验证码对象实体
 */
@Data
public class VerifyCodeModel implements Serializable {

    @Serial
    private static final long serialVersionUID = 42L;

    private byte[] imgBytes;

    private long expireTime;

    private String requestOnce;

}
