package com.xhtt.modules.sms.service;

import com.xhtt.modules.sms.entity.SmsMessage;

/**
 * @Author SunWei
 * @Date: 2022/3/8 8:49
 * @Description: 发送短信
 */
public interface ISendSmsService {

    public boolean sendSms(SmsMessage message);

}
