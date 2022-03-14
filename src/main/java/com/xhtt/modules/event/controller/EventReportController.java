package com.xhtt.modules.event.controller;

import java.util.Arrays;
import java.util.Map;

import com.xhtt.common.utils.PageUtils;
import com.xhtt.common.utils.R;
import com.xhtt.core.annotation.Login;
import com.xhtt.core.annotation.LoginUser;
import com.xhtt.modules.sys.entity.SysUserEntity;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.xhtt.modules.event.entity.EventReportEntity;
import com.xhtt.modules.event.service.EventReportService;



/**
 * 事件快报（市级）
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2022-03-14 09:40:41
 */
@RestController
@RequestMapping("app/eventReport")
public class EventReportController {
    @Autowired
    private EventReportService eventReportService;


    /**
     * 市级列表（信息上报：草稿，退回）
     */
    @RequestMapping("/reportList")
    @Login
    public R reportList(@RequestParam Map<String, Object> params){
        PageUtils page = eventReportService.reportList(params);
        return R.ok().put("page", page);
    }


//
//    /**
//     * 信息
//     */
//    @RequestMapping("/info/{id}")
//    @RequiresPermissions("generator:eventreport:info")
//    public R info(@PathVariable("id") Integer id){
//		EventReportEntity eventReport = eventReportService.getById(id);
//
//        return R.ok().put("eventReport", eventReport);
//    }
//
//    /**
//     * 保存
//     */
//    @RequestMapping("/save")
//    @RequiresPermissions("generator:eventreport:save")
//    public R save(@RequestBody EventReportEntity eventReport){
//		eventReportService.save(eventReport);
//
//        return R.ok();
//    }
//
//    /**
//     * 修改
//     */
//    @RequestMapping("/update")
//    @RequiresPermissions("generator:eventreport:update")
//    public R update(@RequestBody EventReportEntity eventReport){
//		eventReportService.updateById(eventReport);
//
//        return R.ok();
//    }
//
//    /**
//     * 删除
//     */
//    @RequestMapping("/delete")
//    @RequiresPermissions("generator:eventreport:delete")
//    public R delete(@RequestBody Integer[] ids){
//		eventReportService.removeByIds(Arrays.asList(ids));
//
//        return R.ok();
//    }

}
