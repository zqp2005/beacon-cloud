package com.mashibing.common.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StandardSubmit implements Serializable {
    /**
     * 针对当前短信的唯一标识，雪花算法
     */
    private Long sequenceId;
    private Long clientId;

    /**
     * 客户端的ip白名单，查询缓存
     */
    private List<String> ip;

    private String uid;
    private String mobile;
    private String sign;
    private String text;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime sendTime;
    /**
     * 短信费用
     */
    private Long fee;
    private Integer operatorId;
    private Integer areaCode;

    /**
     * 目标手机号的归属地  哈尔滨，  绥化~   （策略模块）
     */
    private String area;

    /**
     * 通道下发的源号码  106934985673485645  （策略模块）
     */
    private String srcNumber;
    /**
     * 通道的id信息   （策略模块）
     */
    private Long channelId;

    /**
     * 短信的发送状态， 0-等待/发送ing，1-成功，2-失败 ，默认情况就是0
     */
    private int reportState;
    /**
     * 短信发送失败的原因是什么，记录在当前属性
     */
    private String errorMsg;
    private String realIP;
    private String apikey;
    private int state;

//    /**
//     * 短信发送失败的原因是什么，记录在当前属性
//     */
//
    /**
     * 签名的id
     */
    private Long signId;
    /**
     * 是否携号转网，  isTransfer = true，代表做了携号转网的判断并且做了操作
     */
    private Boolean isTransfer = false;
private Long oneHourLimitMilli;

}
