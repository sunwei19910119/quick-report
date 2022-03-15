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


import com.xhtt.modules.cfg.dao.CzBaseManageUserlevelDao;
import com.xhtt.modules.cfg.entity.CzBaseManageUserlevelEntity;
import com.xhtt.modules.cfg.service.CzBaseManageUserlevelService;


@Service("czBaseManageUserlevelService")
public class CzBaseManageUserlevelServiceImpl extends ServiceImpl<CzBaseManageUserlevelDao, CzBaseManageUserlevelEntity> implements CzBaseManageUserlevelService {

    @Autowired
    CzBaseManageUserlevelDao baseManageUserlevelDao;

    @Override
    @DataSource(value = "slave1")
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CzBaseManageUserlevelEntity> page = this.page(
                new Query<CzBaseManageUserlevelEntity>(params).getPage(),
                new QueryWrapper<CzBaseManageUserlevelEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    @DataSource(value = "slave1")
    public boolean isManager(String deptCode,String userConnectId) {
        CzBaseManageUserlevelEntity baseManageUserlevel = baseManageUserlevelDao.isManager(deptCode,userConnectId);
        return baseManageUserlevel != null;
    }

    @Override
    @DataSource(value = "slave1")
    public List<CzBaseManageUserlevelEntity> selectListByDeptCodes(String[] copyForUnitIds ){
        return baseManageUserlevelDao.selectListByDeptCodes(copyForUnitIds);
    }


}