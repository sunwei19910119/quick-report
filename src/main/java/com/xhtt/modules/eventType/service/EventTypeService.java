package com.xhtt.modules.eventType.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xhtt.common.utils.PageUtils;
import com.xhtt.modules.eventType.entity.EventTypeEntity;

import java.util.Map;

/**
 * 
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2022-03-11 14:06:32
 */
public interface EventTypeService extends IService<EventTypeEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

