package com.xhtt.modules.sms.controller;

import com.xhtt.common.utils.R;
import com.xhtt.modules.sms.entity.SmsMessage;
import com.xhtt.modules.sms.service.ISendSmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author SunWei
 * @Date: 2022/3/8 9:20
 * @Description:
 */
@RestController
public class SendSmsController {
    @Autowired
    ISendSmsService sendSmsService;

    @PostMapping("/sendSms")
    public R sendSms(@RequestBody SmsMessage message){
        boolean result = sendSmsService.sendSms(message);
        return result ? R.ok() : R.error();
    }
}

