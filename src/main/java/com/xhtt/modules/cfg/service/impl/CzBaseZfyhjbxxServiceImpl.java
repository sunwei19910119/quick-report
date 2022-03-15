package com.xhtt.modules.cfg.service.impl;

import com.xhtt.common.utils.PageUtils;
import com.xhtt.common.utils.Query;
import com.xhtt.datasource.annotation.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


import com.xhtt.modules.cfg.dao.CzBaseZfyhjbxxDao;
import com.xhtt.modules.cfg.entity.CzBaseZfyhjbxxEntity;
import com.xhtt.modules.cfg.service.CzBaseZfyhjbxxService;


@Service("czBaseZfyhjbxxService")
public class CzBaseZfyhjbxxServiceImpl extends ServiceImpl<CzBaseZfyhjbxxDao, CzBaseZfyhjbxxEntity> implements CzBaseZfyhjbxxService {

    @Autowired
    CzBaseZfyhjbxxDao baseZfyhjbxxDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CzBaseZfyhjbxxEntity> page = this.page(
                new Query<CzBaseZfyhjbxxEntity>(params).getPage(),
                new QueryWrapper<CzBaseZfyhjbxxEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    @DataSource(value = "slave1")
    public List<CzBaseZfyhjbxxEntity> selectListByBmdm(String bmdm) {
        return baseZfyhjbxxDao.selectListByBmdm(bmdm);
    }

}