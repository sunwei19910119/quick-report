package com.xhtt.modules.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xhtt.common.utils.PageUtils;
import com.xhtt.modules.sys.entity.SysUserEntity;

import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * 系统用户
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年9月18日 上午9:43:39
 */
public interface SysUserService extends IService<SysUserEntity> {

    SysUserEntity getUserName(String userName, Integer companyId);

    SysUserEntity getUserConnect(String userConnectId, Integer companyId);

    SysUserEntity getUserName(String userName);

}
