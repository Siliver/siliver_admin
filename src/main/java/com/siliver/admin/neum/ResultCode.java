package com.siliver.admin.neum;

/**
 * 返回结构常量
 *
 * @author siliver
 */
public enum ResultCode {

    /**
     * 成功
     */
    SUCCESS_CODE(200, "成功"),

    /**
     * 失败
     */
    FAIL_CODE(400, "失败"),

    /**
     * 锁定
     */
    LOCK_CODE(300, "失败");

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
