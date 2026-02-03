package com.mashibing.api.vo;

import lombok.Data;

@Data
public class ResultVo {
    private Integer code;
    private String msg;
    private Integer count;
    private Long free;
    private String uid;
    private String sid;

}
