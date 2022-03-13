package com.xhtt.modules.sms.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author SunWei
 * @Date: 2022/3/8 10:08
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SmsResponse {

    private String message;

    private int code;

    private Object data;
}

