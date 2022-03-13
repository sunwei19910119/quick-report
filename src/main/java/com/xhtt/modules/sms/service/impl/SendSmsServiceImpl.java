package com.xhtt.modules.sms.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.xhtt.common.httpclient.HttpClientUtil;
import com.xhtt.modules.sms.entity.SmsMessage;
import com.xhtt.modules.sms.entity.SmsResponse;
import com.xhtt.modules.sms.service.ISendSmsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @Author SunWei
 * @Date: 2022/3/8 8:50
 * @Description:
 */
@Service
public class SendSmsServiceImpl implements ISendSmsService {

     @Value("${sms.url}")
     private String url;

     @Override
     public boolean sendSms(SmsMessage message) {
          Object obj = JSONArray.toJSON(message);
          String json = obj.toString();
          String result = HttpClientUtil.sendPostByJson(url,json,0);
          SmsResponse smsResponse = JSON.parseObject(result, SmsResponse.class);
          return smsResponse.getCode() == 200;
     }
}

