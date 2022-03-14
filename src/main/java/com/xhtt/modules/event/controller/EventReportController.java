package com.xhtt.modules.event.controller;

import java.util.Arrays;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.xhtt.common.utils.PageUtils;
import com.xhtt.common.utils.R;
import com.xhtt.core.annotation.Login;
import com.xhtt.core.annotation.LoginUser;
import com.xhtt.modules.accident.entity.AccidentReportEntity;
import com.xhtt.modules.sys.entity.SysUserEntity;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.xhtt.modules.event.entity.EventReportEntity;
import com.xhtt.modules.event.service.EventReportService;

import javax.validation.Valid;


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
     * 市级列表（信息上报：草稿，退回,区级待上报）
     */
    @RequestMapping("/reportList")
    @Login
    public R reportList(@RequestParam Map<String, Object> params){
        PageUtils page = eventReportService.reportList(params);
        return R.ok().put("page", page);
    }


    /**
     * 市级列表（信息签发：待签发，签发完成）
     */
    @RequestMapping("/signList")
    @Login
    public R list(@RequestParam Map<String, Object> params, @LoginUser SysUserEntity sysUser){
        PageUtils page = eventReportService.signList(params);
        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("generator:eventreport:info")
    public R info(@PathVariable("id") Integer id){
		EventReportEntity eventReport = eventReportService.getById(id);

        return R.ok().put("eventReport", eventReport);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @Login
    public R save(@Valid @RequestBody EventReportEntity eventReport, @LoginUser SysUserEntity sysUser){
        eventReport.setUploadImage(JSON.toJSONString(eventReport.getUploadImageList()));
        eventReport.setUploadVideo(JSON.toJSONString(eventReport.getUploadVideoList()));
        eventReport.setUploadVoice(JSON.toJSONString(eventReport.getUploadVoiceList()));
        eventReport.setUploadFile(JSON.toJSONString(eventReport.getUploadFileList()));
        eventReportService.save(eventReport);
        //如果number为空,插入主键
        if(eventReport.getNumber() == null || eventReport.getNumber().isEmpty()){
            eventReport.setNumber(eventReport.getId().toString());
            eventReportService.updateById(eventReport);
        }
        return R.ok();
    }


    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody EventReportEntity eventReport){
		eventReportService.updateById(eventReport);

        return R.ok();
    }

    /**
     * 单个删除
     */
    @RequestMapping("/deleteById")
    @Login
    public R delete(@RequestParam Integer id){
        eventReportService.deleteById(id);
        return R.ok();
    }


    /**
     * 批量删除
     */
    @RequestMapping("/deleteBatch")
    public R deleteBatch(@RequestParam Integer[] ids){
		eventReportService.deleteBatch(ids);

        return R.ok();
    }

    @PostMapping("/submit")
    @Login
    public R submit(@RequestParam int id){
        return eventReportService.submit(id);
    }
}
