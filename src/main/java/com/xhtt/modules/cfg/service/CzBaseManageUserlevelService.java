package com.xhtt.modules.cfg.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xhtt.common.utils.PageUtils;
import com.xhtt.modules.cfg.entity.CzBaseManageUserlevelEntity;


import java.util.Map;

/**
 * 政府用户科室管理人员权限
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2022-03-15 09:56:31
 */
public interface CzBaseManageUserlevelService extends IService<CzBaseManageUserlevelEntity> {

    PageUtils queryPage(Map<String, Object> params);

    //判断当前用户是否为处室负责人或者分管领导
    boolean isManager(String deptCode,String userConnectId);
}

