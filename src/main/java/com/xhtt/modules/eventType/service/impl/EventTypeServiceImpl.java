package com.xhtt.modules.eventType.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xhtt.common.utils.PageUtils;
import com.xhtt.common.utils.Query;
import com.xhtt.modules.accident.controller.vo.AccidentReportSimpleVo;
import com.xhtt.modules.accident.dao.AccidentReportDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.xhtt.modules.eventType.dao.EventTypeDao;
import com.xhtt.modules.eventType.entity.EventTypeEntity;
import com.xhtt.modules.eventType.service.EventTypeService;


@Service("eventTypeService")
public class EventTypeServiceImpl extends ServiceImpl<EventTypeDao, EventTypeEntity> implements EventTypeService {

    @Autowired
    EventTypeDao eventTypeDao;

    @Override
    public List<EventTypeEntity> selectAll() {
        List<EventTypeEntity> list = eventTypeDao.selectAll();

        return list;
    }

    @Override
    public EventTypeEntity getById(Integer id) {
        return eventTypeDao.getById(id);
    }

}