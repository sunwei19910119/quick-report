package com.xhtt.modules.cfg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xhtt.common.utils.RedisUtils;
import com.xhtt.datasource.annotation.DataSource;
import com.xhtt.modules.cfg.dao.CfgBaseRegionDao;
import com.xhtt.modules.cfg.entity.CfgBaseRegionEntity;
import com.xhtt.modules.cfg.service.CfgBaseRegionService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("cfgBaseRegionService")
public class CfgBaseRegionServiceImpl extends ServiceImpl<CfgBaseRegionDao, CfgBaseRegionEntity> implements CfgBaseRegionService {
    @Autowired
    private RedisUtils redisUtils;

    @Override
    @DataSource(value = "slave1")
    public List<CfgBaseRegionEntity> listByParentId(String parentId) {
        List<CfgBaseRegionEntity> list = this.list(new LambdaQueryWrapper<CfgBaseRegionEntity>()
                .eq(CfgBaseRegionEntity::getParentCode, parentId).orderByAsc(CfgBaseRegionEntity::getOrderNum));
        return list;
    }

    @Override
    @DataSource(value = "slave1")
    public CfgBaseRegionEntity getInfo(String id) {
        CfgBaseRegionEntity cfgBaseRegion = null;
        if (StringUtils.isNotEmpty(id)) {
            cfgBaseRegion = this.getOne(new LambdaQueryWrapper<CfgBaseRegionEntity>().eq(CfgBaseRegionEntity::getRegionCode, id)
                    .select(CfgBaseRegionEntity::getRegionCode, CfgBaseRegionEntity::getName));
        }

        return null == cfgBaseRegion ? new CfgBaseRegionEntity("") : cfgBaseRegion;
    }

    @Override
    public List<CfgBaseRegionEntity> getXzqyList(String parentCode) {
        List<CfgBaseRegionEntity> cfgBaseRegionList = baseMapper.getXzqyList(parentCode);
        return cfgBaseRegionList;
    }

    @Override
    @DataSource(value = "slave1")
    public List<CfgBaseRegionEntity> getXzqyAll() {
        Map<String, Object> params = new HashMap<>();
        List<CfgBaseRegionEntity> cfgBaseRegionList = baseMapper.getXzqyAll(params);
        return cfgBaseRegionList;
    }

    @Override
    @DataSource(value = "slave1")
    public CfgBaseRegionEntity getXzqy(String regionCode) {
        CfgBaseRegionEntity cfgBaseRegion = this.getOne(new LambdaQueryWrapper<CfgBaseRegionEntity>()
                .eq(CfgBaseRegionEntity::getRegionCode, regionCode));

        return cfgBaseRegion;
    }


    @Override
    @DataSource(value = "slave1")
    public List<CfgBaseRegionEntity> getXzqyCount(){
        List<CfgBaseRegionEntity> cfgBaseRegionList = baseMapper.getXzqyCount();
        return cfgBaseRegionList;
    }


    @Override
    @DataSource(value = "slave1")
    public List<CfgBaseRegionEntity> getXzqyCountAndTown(){
        List<CfgBaseRegionEntity> cfgBaseRegionList = baseMapper.getXzqyCountAndTown();
        return cfgBaseRegionList;
    }

}
