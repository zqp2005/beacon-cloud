package com.mashibing.api.enums;

import lombok.Getter;

@Getter
public enum SmsCodeEnum {
    PARAMETER_ERROR(-10,"参数不合法");
    private Integer code;
    private String msg;

    SmsCodeEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
