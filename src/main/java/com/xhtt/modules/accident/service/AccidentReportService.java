package com.xhtt.modules.accident.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xhtt.common.utils.PageUtils;
import com.xhtt.common.utils.R;
import com.xhtt.modules.accident.controller.vo.AccidentReportSimpleVo;
import com.xhtt.modules.accident.entity.AccidentReportEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2022-03-09 14:42:11
 */
public interface AccidentReportService extends IService<AccidentReportEntity> {


    PageUtils reportList(Map<String, Object> params);

    PageUtils signList(Map<String, Object> params);

    void deleteAccidentReportById(Integer id);

    void deleteBatch(Integer[] ids);

    R submit(int id);
}

