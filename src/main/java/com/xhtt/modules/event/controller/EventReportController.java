package com.xhtt.modules.event.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import cn.hutool.core.util.ArrayUtil;
import com.alibaba.fastjson.JSON;
import com.xhtt.common.exception.RRException;
import com.xhtt.common.exception.XhttException;
import com.xhtt.common.utils.PageUtils;
import com.xhtt.common.utils.R;
import com.xhtt.core.annotation.Login;
import com.xhtt.core.annotation.LoginUser;
import com.xhtt.modules.accident.entity.AccidentReportEntity;
import com.xhtt.modules.dept.entity.DeptEntity;
import com.xhtt.modules.dept.service.DeptService;
import com.xhtt.modules.event.dao.EventReportDao;
import com.xhtt.modules.sys.entity.SysUserEntity;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.xhtt.modules.event.entity.EventReportEntity;
import com.xhtt.modules.event.service.EventReportService;

import javax.servlet.http.HttpServletResponse;
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
    @Autowired
    private EventReportDao eventReportDao;
    @Autowired
    private DeptService deptService;
    /**
     * 市级列表（信息上报：草稿，退回,区级待上报）
     */
    @RequestMapping("/reportList")
    @Login
    public R reportList(@RequestParam Map<String, Object> params,@LoginUser SysUserEntity sysUser){
        PageUtils page = eventReportService.reportList(params,sysUser);
        return R.ok().put("page", page);
    }


    /**
     * 市级列表（信息签发：待签发，签发完成）
     */
    @RequestMapping("/signList")
    @Login
    public R list(@RequestParam Map<String, Object> params, @LoginUser SysUserEntity sysUser){
        PageUtils page = eventReportService.signList(params,sysUser);
        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Integer id){
		EventReportEntity eventReport = eventReportService.getById(id);
        if(eventReport.getCopyForUnitIdsList() == null && StringUtils.isNotBlank(eventReport.getCopyForUnitIds())){
            eventReport.setCopyForUnitIdsList(eventReport.getCopyForUnitIds().split(","));
        }
        return R.ok().put("eventReport", eventReport);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @Login
    public R save(@Valid @RequestBody EventReportEntity eventReport, @LoginUser SysUserEntity sysUser){
        //续报必须上次的已经上报（待签发）
        if(eventReport.getParentId() != null && eventReport.getParentId() != 0){
            EventReportEntity parentEntity = eventReportService.getById(eventReport.getParentId());
            if(parentEntity.getStatus() != 1){
                throw new RRException("首次上报提交后，才能进行续报");
            }
        }
        eventReport.setUploadImage(JSON.toJSONString(eventReport.getUploadImageList()));
        eventReport.setUploadVideo(JSON.toJSONString(eventReport.getUploadVideoList()));
        eventReport.setUploadVoice(JSON.toJSONString(eventReport.getUploadVoiceList()));
        eventReport.setUploadFile(JSON.toJSONString(eventReport.getUploadFileList()));


        //处理抄送单位IDS
        if(ArrayUtil.isNotEmpty(eventReport.getCopyForUnitIdsList())){
            eventReport.setCopyForUnitIds(ArrayUtil.join(eventReport.getCopyForUnitIdsList(),","));
            List<DeptEntity> deptEntities = deptService.selectDeptListByIds(eventReport.getCopyForUnitIdsList());
            List<String> deptNames = deptEntities.stream().map(DeptEntity::getName).collect(Collectors.toList());
            eventReport.setCopyForUnit(String.join(",",deptNames));
        }
        //记录创建者市平台ID
        eventReport.setCreateUserConnectId(sysUser.getUserConnectId());
        //校验number重复
        if(eventReportDao.checkNumber(eventReport.getNumber()) > 0){
            return R.error("编号不能重复");
        }
        eventReportService.save(eventReport);
        //如果number为空,插入主键
//        if(eventReport.getNumber() == null || eventReport.getNumber().isEmpty()){
//            eventReport.setNumber(eventReport.getId().toString());
//            eventReportService.updateById(eventReport);
//        }
        return R.ok();
    }


    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody EventReportEntity eventReport){
        //校验number重复
        if(eventReportDao.checkNumberExcept(eventReport.getNumber(),eventReport.getId()) > 0){
            return R.error("编号不能重复");
        }
        eventReport.setUploadImage(JSON.toJSONString(eventReport.getUploadImageList()));
        eventReport.setUploadVideo(JSON.toJSONString(eventReport.getUploadVideoList()));
        eventReport.setUploadVoice(JSON.toJSONString(eventReport.getUploadVoiceList()));
        eventReport.setUploadFile(JSON.toJSONString(eventReport.getUploadFileList()));
        //处理抄送单位IDS
        if(ArrayUtil.isNotEmpty(eventReport.getCopyForUnitIdsList())){
            eventReport.setCopyForUnitIds(ArrayUtil.join(eventReport.getCopyForUnitIdsList(),","));
            List<DeptEntity> deptEntities = deptService.selectDeptListByIds(eventReport.getCopyForUnitIdsList());
            List<String> deptNames = deptEntities.stream().map(DeptEntity::getName).collect(Collectors.toList());
            eventReport.setCopyForUnit(String.join(",",deptNames));
        }

        //退回区级
        if(eventReport.getStatus() == 4){
            if(eventReport.getLevel() == 0 && eventReport.getAccidentReportId() != 0 ){
                eventReportService.accidentReportRefuse(eventReport);
            }
        }

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

    /**
     * 签发
     */
    @PostMapping("/submit")
    @Login
    public R submit(@RequestParam int id,
                    @RequestParam Integer status,
                    @RequestParam String refuseReason){
        return eventReportService.submit(id,status,refuseReason);
    }


    /**
     * 导出市级快报专报
     */
    @GetMapping("/export")
    @ApiOperation("导出风险告知卡")
    @Login
    public void export(@RequestParam Integer id, HttpServletResponse response, @LoginUser SysUserEntity user) throws IOException {
        eventReportService.getExportMap(id, response, user);
    }



    /**
     * number校验
     */
    @GetMapping("/checkNumber")
    public R checkNumber(@RequestParam String number) {
        return eventReportService.checkNumber(number);
    }

}
