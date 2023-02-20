package com.siliver.admin.neum;

public enum LoginEnum {

    WITH_NON("无验证登录"),

    WITH_CAPTCHA("图形验证码登录");

    private final String message;

    LoginEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
