package com.xhtt.modules.accident.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xhtt.modules.accident.controller.vo.AccidentReportSimpleVo;
import com.xhtt.modules.accident.entity.AccidentReportEntity;
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
 * @date 2022-03-09 14:42:11
 */
@Mapper
public interface AccidentReportDao extends BaseMapper<AccidentReportEntity> {

    List<AccidentReportSimpleVo> reportList(Page<AccidentReportSimpleVo> page, @Param("ps") Map<String, Object> params);

    List<AccidentReportSimpleVo> signList(Page<AccidentReportSimpleVo> page, @Param("ps") Map<String, Object> params);

    int deleteById(Integer id);

    int deleteBatch(Integer[] ids);

    int submit(int id,Integer status,String refuseReason);

    int checkNumber(String number);

    String maxNumber();

}
