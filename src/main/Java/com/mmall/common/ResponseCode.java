package com.mmall.common;

/**
 * Created by Administrator on 2018/5/17.
 */
public enum ResponseCode {
    SUCCESS(0, "SUCCESS"),
    ERROR(1, "ERROR"),
    NEED_LOGIN(10, "NEED_LOGIN_IN"),
    ILLEGAL_ARGUMENT(20, "ILLEGAL_ARGUMENT");

    private final int code;
    private final String desc;

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    ResponseCode(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
