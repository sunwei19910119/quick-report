package com.xhtt.modules.cfg.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xhtt.modules.cfg.entity.CfgBaseRegionEntity;

import java.util.List;

/**
 * 行政区划代码信息
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2021-03-01 16:57:06
 */
public interface CfgBaseRegionService extends IService<CfgBaseRegionEntity> {
    List<CfgBaseRegionEntity> listByParentId(String parentId);

    CfgBaseRegionEntity getInfo(String id);

    CfgBaseRegionEntity getXzqy(String regionCode);

    List<CfgBaseRegionEntity> getXzqyList(String parentCode);

    List<CfgBaseRegionEntity> getXzqyAll();

    List<CfgBaseRegionEntity> getXzqyByType(String type,String parentCode);

    List<CfgBaseRegionEntity> getXzqyCountAndTown();
}

