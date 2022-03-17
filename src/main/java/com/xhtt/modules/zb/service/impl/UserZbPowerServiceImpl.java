package com.xhtt.modules.zb.service.impl;

import com.xhtt.common.utils.PageUtils;
import com.xhtt.common.utils.Query;
import com.xhtt.datasource.annotation.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.xhtt.modules.zb.dao.UserZbPowerDao;
import com.xhtt.modules.zb.entity.UserZbPowerEntity;
import com.xhtt.modules.zb.service.UserZbPowerService;


@Service("userZbPowerService")
public class UserZbPowerServiceImpl extends ServiceImpl<UserZbPowerDao, UserZbPowerEntity> implements UserZbPowerService {


    @Autowired
    UserZbPowerDao userZbPowerDao;


    @Override
    @DataSource(value = "slave2")
    public UserZbPowerEntity selectByUserConnectId(String userConnectId) {
        return userZbPowerDao.selectByUserConnectId(userConnectId);
    }
}