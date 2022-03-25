package com.xhtt.modules.event.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xhtt.modules.accident.controller.vo.AccidentReportSimpleVo;
import com.xhtt.modules.event.controller.vo.EventReportSimpleVo;
import com.xhtt.modules.event.entity.EventReportEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 事件快报（市级）
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2022-03-14 09:40:41
 */
@Mapper
public interface EventReportDao extends BaseMapper<EventReportEntity> {
    List<EventReportSimpleVo> reportList(Page<EventReportSimpleVo> page, @Param("ps") Map<String, Object> params);

    List<EventReportSimpleVo> signList(Page<EventReportSimpleVo> page, @Param("ps") Map<String, Object> params);

    int deleteById(Integer id);

    int deleteBatch(Integer[] ids);

    int submit(int id,Integer status,String refuseReason);

    int checkNumber(String number);

    int checkNumberExcept(String number,Integer id);

    String maxNumber();
}
