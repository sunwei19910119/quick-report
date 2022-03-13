package com.xhtt.modules.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xhtt.modules.sys.dao.SysGovernmentInfoDao;
import com.xhtt.modules.sys.entity.SysGovernmentInfoEntity;
import com.xhtt.modules.sys.service.SysGovernmentInfoService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


@Service("sysGovernmentInfoService")
public class SysGovernmentInfoServiceImpl extends ServiceImpl<SysGovernmentInfoDao, SysGovernmentInfoEntity> implements SysGovernmentInfoService {

    @Override
    public SysGovernmentInfoEntity getbygovOtherID(Long govOtherId) {
        SysGovernmentInfoEntity sysGovernmentInfo = this.getOne(new LambdaQueryWrapper<SysGovernmentInfoEntity>().eq(SysGovernmentInfoEntity::getGovOtherId, govOtherId).select(SysGovernmentInfoEntity::getId));
        return sysGovernmentInfo;
    }

}