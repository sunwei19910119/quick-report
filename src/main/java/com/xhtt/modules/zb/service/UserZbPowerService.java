package com.xhtt.modules.zb.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xhtt.common.utils.PageUtils;
import com.xhtt.modules.zb.entity.UserZbPowerEntity;

import java.util.Map;

/**
 * 用户值班权限信息表
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2022-03-17 17:09:50
 */
public interface UserZbPowerService extends IService<UserZbPowerEntity> {

    UserZbPowerEntity selectByUserConnectId(String userConnectId);
}

