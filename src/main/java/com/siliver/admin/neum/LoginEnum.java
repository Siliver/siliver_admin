package com.siliver.admin.neum;

/**
 * 登录枚举类
 *
 * @author siliver
 */

public enum LoginEnum {

    /**
     * 普通登录
     */
    WITH_NON("无验证登录"),

    /**
     * 验证码登录
     */
    WITH_CAPTCHA("图形验证码登录");

    private final String message;

    LoginEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
