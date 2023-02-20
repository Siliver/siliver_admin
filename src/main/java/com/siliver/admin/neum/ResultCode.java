package com.siliver.admin.neum;

public enum ResultCode {

    SUCCESS_CODE(200, "成功"),
    FAIL_CODE(400, "失败");

    private final int code;

    private final String message;

    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }
}
