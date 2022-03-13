package com.xhtt.modules.sys.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xhtt.common.exception.RRException;
import com.xhtt.common.utils.PageUtils;
import com.xhtt.common.utils.Query;
import com.xhtt.modules.sys.dao.SysUserDao;
import com.xhtt.modules.sys.entity.SysUserEntity;
import com.xhtt.modules.sys.service.SysUserService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;


/**
 * 系统用户
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年9月18日 上午9:46:09
 */
@Service("sysUserService")
public class SysUserServiceImpl extends ServiceImpl<SysUserDao, SysUserEntity> implements SysUserService {

    @Override
    public SysUserEntity getUserName(String userName, Integer companyId) {
        return this.getOne(new LambdaQueryWrapper<SysUserEntity>().eq(SysUserEntity::getName, userName).eq(SysUserEntity::getOrgId, companyId).select(SysUserEntity::getUserId, SysUserEntity::getType, SysUserEntity::getOrgId));
    }

    @Override
    public SysUserEntity getUserConnect(String userConnectId, Integer companyId) {
        return this.getOne(new LambdaQueryWrapper<SysUserEntity>().eq(SysUserEntity::getUserConnectId, userConnectId).eq(SysUserEntity::getOrgId, companyId));
    }

    @Override
    public SysUserEntity getUserName(String userName) {
        return this.getOne(new LambdaQueryWrapper<SysUserEntity>().eq(SysUserEntity::getName, userName).last("LIMIT 1"));
    }
}
