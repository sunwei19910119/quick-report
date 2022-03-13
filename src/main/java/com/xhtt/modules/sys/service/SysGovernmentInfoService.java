package com.xhtt.modules.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xhtt.modules.sys.entity.SysGovernmentInfoEntity;

/**
 * 政府用户基本信息
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2021-03-01 18:31:53
 */
public interface SysGovernmentInfoService extends IService<SysGovernmentInfoEntity> {

    SysGovernmentInfoEntity getbygovOtherID(Long govOtherId);
}

