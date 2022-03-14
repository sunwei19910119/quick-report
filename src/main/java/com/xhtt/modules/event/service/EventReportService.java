package com.xhtt.modules.event.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xhtt.common.utils.PageUtils;
import com.xhtt.common.utils.R;
import com.xhtt.modules.accident.entity.AccidentReportEntity;
import com.xhtt.modules.event.entity.EventReportEntity;

import java.util.Map;

/**
 * 事件快报（市级）
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2022-03-14 09:40:41
 */
public interface EventReportService extends IService<EventReportEntity> {

    PageUtils reportList(Map<String, Object> params);

    PageUtils signList(Map<String, Object> params);

    void deleteById(Integer id);

    void deleteBatch(Integer[] ids);

    R submit(int id,Integer status,String refuseReason);

    //区级签发上报市级
    void accidentReport(AccidentReportEntity accidentReport);

    //市级回退区级签发
    void accidentReportRefuse(EventReportEntity eventReportEntity);

}

