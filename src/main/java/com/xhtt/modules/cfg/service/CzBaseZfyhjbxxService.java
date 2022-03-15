package com.xhtt.modules.cfg.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xhtt.common.utils.PageUtils;
import com.xhtt.modules.cfg.entity.CzBaseZfyhjbxxEntity;

import java.util.List;
import java.util.Map;

/**
 * 政府用户基本信息
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2022-03-15 10:48:12
 */
public interface CzBaseZfyhjbxxService extends IService<CzBaseZfyhjbxxEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<CzBaseZfyhjbxxEntity> selectListByBmdm(String bmdm);
}

