package com.xhtt.modules.eventType.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xhtt.modules.accident.controller.vo.AccidentReportSimpleVo;
import com.xhtt.modules.eventType.entity.EventTypeEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2022-03-11 14:06:32
 */
@Mapper
public interface EventTypeDao extends BaseMapper<EventTypeEntity> {
    List<EventTypeEntity> selectAll();

    EventTypeEntity getById(Integer id);

}
