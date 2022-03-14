package com.xhtt.modules.event.convert;

import com.xhtt.modules.accident.entity.AccidentReportEntity;
import com.xhtt.modules.event.entity.EventReportEntity;
import org.mapstruct.Mapper;

/**
 * @Author SunWei
 * @Date: 2022/3/14 10:59
 * @Description:
 */
@Mapper
public interface EventReportConvert {

    EventReportEntity convert(AccidentReportEntity accident);
}

