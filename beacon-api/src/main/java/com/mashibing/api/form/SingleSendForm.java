package com.mashibing.api.form;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class SingleSendForm {
    @NotBlank(message = "apikey不允许为空！")
    private String apikey;
    @NotBlank(message = "手机号不能为空！")
    private String mobile;
    @NotBlank(message = "短信内容不能为空！")
    private String text;
    private String uid;
    @Range(min = 0,max = 2,message = "短信类型只能是0~2的整数！")
    @NotNull(message = "短信类型不允许非空！")
    private Integer state;
}
