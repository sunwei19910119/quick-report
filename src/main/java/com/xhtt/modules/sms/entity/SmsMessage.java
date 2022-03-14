package com.xhtt.modules.sms.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @Author SunWei
 * @Date: 2022/3/8 8:44
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SmsMessage {

    //发送给企业的COMPANY_ID,非企业不需要
    private int companyId;

    //短信内容
    @NotBlank(message = "内容不能为空")
    private String message;

    //手机号（多个手机号用逗号拼接）
    @NotBlank(message = "手机号不能为空")
    private String mobile;

    //发送人姓名
    @NotBlank(message = "发送人姓名不能为空")
    private String name;

    //短信等级，默认0，0最低，9最高
    private int pushLevel;

    //短信类型（默认NORMAL,验证码CODE）
    private String smsType;

}

