package com.xhtt.modules.event.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xhtt.common.utils.PageUtils;
import com.xhtt.common.utils.Query;
import com.xhtt.common.utils.R;
import com.xhtt.common.utils.RedisUtils;
import com.xhtt.modules.accident.controller.vo.AccidentReportSimpleVo;
import com.xhtt.modules.accident.dao.AccidentReportDao;
import com.xhtt.modules.accident.entity.AccidentReportEntity;
import com.xhtt.modules.accident.service.AccidentReportService;
import com.xhtt.modules.event.controller.vo.EventReportSimpleVo;
import com.xhtt.modules.event.convert.EventReportConvert;
import com.xhtt.modules.sms.service.ISendSmsService;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xhtt.modules.event.dao.EventReportDao;
import com.xhtt.modules.event.entity.EventReportEntity;
import com.xhtt.modules.event.service.EventReportService;


@Service("eventReportService")
public class EventReportServiceImpl extends ServiceImpl<EventReportDao, EventReportEntity> implements EventReportService {

    @Autowired
    EventReportDao eventReportDao;

    @Autowired
    AccidentReportDao accidentReportDao;

    @Autowired
    AccidentReportService accidentReportService;

    @Autowired
    RedisUtils redis;

    @Autowired
    ISendSmsService sendSmsService;

    @Override
    public PageUtils reportList(Map<String, Object> params) {
        Page<EventReportSimpleVo> page = new Query<EventReportSimpleVo>(params).getPage();
        List<EventReportSimpleVo> list = baseMapper.reportList(page, params);
        list.forEach(this::convertCounty);
        page.setRecords(list);
        return new PageUtils(page);
    }


    @Override
    public PageUtils signList(Map<String, Object> params) {
        Page<EventReportSimpleVo> page = new Query<EventReportSimpleVo>(params).getPage();
        List<EventReportSimpleVo> list = baseMapper.signList(page, params);
        list.forEach(this::convertCounty);
        page.setRecords(list);
        return new PageUtils(page);
    }

    @Override
    public void deleteById(Integer id) {
        eventReportDao.deleteById(id);
    }

    @Override
    public void deleteBatch(Integer[] ids){
        eventReportDao.deleteBatch(ids);
    }

    @Override
    public R submit(int id,Integer status,String refuseReason) {
        int result = eventReportDao.submit(id,status,refuseReason);
        EventReportEntity eventReportInfo = this.getById(id);
        //退回区级
        if(status == 2){
            if(eventReportInfo.getLevel() == 0 && eventReportInfo.getAccidentReportId() != 0 ){
                this.accidentReportRefuse(eventReportInfo);
            }
        }

        if(status == 3){
            //签发同时发送短信
            sendSmsService.sendEventReportSms(eventReportInfo);
        }
        return result == 1 ? R.ok() : R.error("提交失败");
    }

    private void convertCounty(EventReportSimpleVo eventReportSimpleVo){
        eventReportSimpleVo.setCountyName(redis.get(eventReportSimpleVo.getCountyCode()));
    }

    //区级签发的同时上报市级
    @Override
    public void accidentReport(AccidentReportEntity accidentReport){
        EventReportConvert eventReportConvert = Mappers.getMapper(EventReportConvert.class);
        EventReportEntity eventReportEntity = eventReportConvert.convert(accidentReport);
        eventReportEntity.setStatus(5);
        eventReportEntity.setAccidentReportId(accidentReport.getId());
        this.save(eventReportEntity);
    }


    //区级签发回退
    @Override
    public void accidentReportRefuse(EventReportEntity eventReportEntity){
        this.deleteById(eventReportEntity.getId());
        AccidentReportEntity accidentReportEntity = accidentReportService.getById(eventReportEntity.getAccidentReportId());
        accidentReportEntity.setStatus(4);
        accidentReportEntity.setRefuseReason(eventReportEntity.getRefuseReason());
        accidentReportService.updateById(accidentReportEntity);

    }
}