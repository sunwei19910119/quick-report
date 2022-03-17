package com.xhtt.modules.eventType.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.xhtt.common.utils.PageUtils;
import com.xhtt.common.utils.R;
import com.xhtt.common.utils.RedisUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.xhtt.modules.eventType.entity.EventTypeEntity;
import com.xhtt.modules.eventType.service.EventTypeService;




/**
 * 
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2022-03-11 14:06:32
 */
@RestController
@RequestMapping("app/eventType")
public class EventTypeController {
    @Autowired
    private EventTypeService eventTypeService;
    @Autowired
    RedisUtils redisUtils;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(){
//        List result = redisUtils.get("event_type_list", ArrayList.class);

//        if(CollUtil.isNotEmpty(result)){
//            return R.ok().put("list", result);
//        }

        List<EventTypeEntity> list = eventTypeService.selectAll();
//        redisUtils.set("event_type_list",list,redisUtils.DEFAULT_EXPIRE);

        return R.ok().put("list", list);


    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Integer id){
		EventTypeEntity eventType = eventTypeService.getById(id);
        EventTypeEntity eventTypeEntityParent = null;
        if(eventType.getLevel() != 0){
             eventTypeEntityParent = eventTypeService.getById(eventType.getParentId());
        }
        return R.ok().put("eventType", eventType).put("parentEventType",eventTypeEntityParent);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody EventTypeEntity eventType){
		eventTypeService.save(eventType);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody EventTypeEntity eventType){
		eventTypeService.updateById(eventType);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Integer[] ids){
		eventTypeService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
