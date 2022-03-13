package com.xhtt.modules.cfg.dao;

import com.xhtt.modules.cfg.entity.CfgBaseRegionEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 行政区划代码信息
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2021-03-01 16:57:06
 */
@Mapper
public interface CfgBaseRegionDao extends BaseMapper<CfgBaseRegionEntity> {
    List<CfgBaseRegionEntity> getXzqyCityList(@Param(value = "ps") Map<String, Object> params);

    List<CfgBaseRegionEntity> getXzqyList(String parentCode);

    List<CfgBaseRegionEntity> getXzqyAll(@Param(value = "ps") Map<String, Object> params);

    List<CfgBaseRegionEntity> getXzqyCount();

    List<CfgBaseRegionEntity> getXzqyCountAndTown();
}
