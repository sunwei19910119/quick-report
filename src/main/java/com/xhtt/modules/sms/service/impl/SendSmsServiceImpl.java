package com.xhtt.modules.sms.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.xhtt.common.httpclient.HttpClientUtil;
import com.xhtt.common.utils.DateUtils;
import com.xhtt.common.utils.RedisUtils;
import com.xhtt.modules.accident.entity.AccidentReportEntity;
import com.xhtt.modules.cfg.dao.CzBaseManageUserlevelDao;
import com.xhtt.modules.cfg.dao.CzBaseZfyhjbxxDao;
import com.xhtt.modules.cfg.entity.CzBaseManageUserlevelEntity;
import com.xhtt.modules.cfg.entity.CzBaseZfyhjbxxEntity;
import com.xhtt.modules.cfg.service.CzBaseManageUserlevelService;
import com.xhtt.modules.cfg.service.CzBaseZfyhjbxxService;
import com.xhtt.modules.event.entity.EventReportEntity;
import com.xhtt.modules.sms.entity.SmsMessage;
import com.xhtt.modules.sms.entity.SmsResponse;
import com.xhtt.modules.sms.service.ISendSmsService;
import org.apache.commons.lang.text.StrSubstitutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @Author SunWei
 * @Date: 2022/3/8 8:50
 * @Description:
 */
@Service
public class SendSmsServiceImpl implements ISendSmsService {

     @Autowired
     RedisUtils redis;

     @Autowired
     CzBaseManageUserlevelService baseManageUserlevelService;

     @Autowired
     CzBaseZfyhjbxxService baseZfyhjbxxService;

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
          String copyForUnitIds = accidentReportEntity.getCopyForUnitIds();
          String mobiles = getMobiles(copyForUnitIds);

          String template = "（突发事件信息）\n" +
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


          SmsMessage smsMessage = new SmsMessage(0,message,"15861183327","",9,"NORMAL");
          this.sendSms(smsMessage);
     }


     @Override
     public void sendEventReportSms(EventReportEntity eventReportEntity) {
          String copyForUnitIds = eventReportEntity.getCopyForUnitIds();
          String mobiles = getMobiles(copyForUnitIds);

          String template = "（突发事件信息）\n" +
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



          SmsMessage smsMessage = new SmsMessage(0,message,"15861183327","",9,"NORMAL");
          this.sendSms(smsMessage);
     }


     /**
      * @author: SunWei
      * @date: 2022/3/15 14:16
      * @param:
      * @return:
      * @description: 获取接收短信的领导手机号(逗号拼接)
      */
     public String getMobiles(String copyForUnitIds){
          Set<String> connectUserIds = new HashSet<>();
          Set<String> connectUserPhones = new HashSet<>();
          //胥亚伟id
          connectUserIds.add("32047191");
          //获取抄送单位部门领导id和手机号
          if(!StrUtil.hasEmpty(copyForUnitIds)){
               String[] copyForUnitIdsArray = copyForUnitIds.split(",");
               List<CzBaseManageUserlevelEntity> baseManageUserlevelEntities = baseManageUserlevelService.selectListByDeptCodes(copyForUnitIdsArray);
               baseManageUserlevelEntities.stream().forEach(
                       a -> {
                            connectUserIds.add(a.getCsUserid());
                            connectUserIds.add(a.getFgDeptCode());
                       });
          }
          List<CzBaseZfyhjbxxEntity> baseZfyhjbxxEntities = baseZfyhjbxxService.selectListByUserIds(connectUserIds);
          baseZfyhjbxxEntities.stream().forEach(
                  a -> {
                       connectUserPhones.add(a.getMobile());
                  });
          return CollUtil.join(connectUserPhones, ",");
     }
}

