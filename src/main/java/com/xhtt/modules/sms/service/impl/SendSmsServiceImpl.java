package com.xhtt.modules.sms.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.xhtt.common.httpclient.HttpClientUtil;
import com.xhtt.common.utils.DateUtils;
import com.xhtt.common.utils.RedisUtils;
import com.xhtt.modules.accident.entity.AccidentReportEntity;
import com.xhtt.modules.event.entity.EventReportEntity;
import com.xhtt.modules.sms.entity.SmsMessage;
import com.xhtt.modules.sms.entity.SmsResponse;
import com.xhtt.modules.sms.service.ISendSmsService;
import org.apache.commons.lang.text.StrSubstitutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author SunWei
 * @Date: 2022/3/8 8:50
 * @Description:
 */
@Service
public class SendSmsServiceImpl implements ISendSmsService {

     @Autowired
     RedisUtils redis;

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

     @Override
     public void sendAccidentReportSms(AccidentReportEntity accidentReportEntity) {
          String template = "【突发事件信息】\n" +
                  "${accidentTime}，${countyName}${accidentSite},${companyName}，${accidentDescription}。\n" +
                  "(${receiveWay}\n" +
                  "${issueDate}书面报）";

          Map<String, String> map = new HashMap<>();
          map.put("accidentTime", DateUtils.format(accidentReportEntity.getAccidentTime(),DateUtils.DATE_TIME_PATTERN));
          map.put("countyName", redis.get(accidentReportEntity.getCountyCode()));
          map.put("accidentSite",accidentReportEntity.getAccidentSite());
          map.put("companyName",accidentReportEntity.getCompanyName());
          map.put("accidentDescription",accidentReportEntity.getAccidentDescription());
          map.put("receiveWay",accidentReportEntity.getReceiveWay());
          map.put("issueDate",DateUtils.format(accidentReportEntity.getIssueDate(),DateUtils.DATE_TIME_PATTERN));

          StrSubstitutor strSubstitutor = new StrSubstitutor(map);
          String message = strSubstitutor.replace(template);

          SmsMessage smsMessage = new SmsMessage(0,message,"18551098833","",9,"NORMAL");
//          this.sendSms(smsMessage);
     }


     @Override
     public void sendEventReportSms(EventReportEntity eventReportEntity) {
          String template = "【突发事件信息】\n" +
                  "${accidentTime}，${countyName}${accidentSite},${companyName}，${accidentDescription}。\n" +
                  "(${receiveWay}\n" +
                  "${issueDate}书面报）";

          Map<String, String> map = new HashMap<>();
          map.put("accidentTime", DateUtils.format(eventReportEntity.getAccidentTime(),DateUtils.DATE_TIME_PATTERN));
          map.put("countyName", redis.get(eventReportEntity.getCountyCode()));
          map.put("accidentSite",eventReportEntity.getAccidentSite());
          map.put("companyName",eventReportEntity.getCompanyName());
          map.put("accidentDescription",eventReportEntity.getAccidentDescription());
          map.put("receiveWay",eventReportEntity.getReceiveWay());
          map.put("issueDate",DateUtils.format(eventReportEntity.getIssueDate(),DateUtils.DATE_TIME_PATTERN));

          StrSubstitutor strSubstitutor = new StrSubstitutor(map);
          String message = strSubstitutor.replace(template);

          SmsMessage smsMessage = new SmsMessage(0,message,"18551098833","",9,"NORMAL");
//          this.sendSms(smsMessage);
     }
}

