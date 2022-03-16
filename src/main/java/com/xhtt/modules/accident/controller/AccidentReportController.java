package com.xhtt.modules.accident.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import cn.afterturn.easypoi.word.WordExportUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ArrayUtil;
import com.alibaba.fastjson.JSON;
import com.xhtt.common.exception.RRException;
import com.xhtt.common.utils.PageUtils;
import com.xhtt.common.utils.R;
import com.xhtt.core.annotation.Login;
import com.xhtt.core.annotation.LoginUser;
import com.xhtt.modules.dept.entity.DeptEntity;
import com.xhtt.modules.dept.service.DeptService;
import com.xhtt.modules.event.entity.EventReportEntity;
import com.xhtt.modules.event.service.EventReportService;
import com.xhtt.modules.sms.service.ISendSmsService;
import com.xhtt.modules.sys.entity.SysUserEntity;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.xhtt.modules.accident.entity.AccidentReportEntity;
import com.xhtt.modules.accident.service.AccidentReportService;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;


/**
 * 
 * 区级上报
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2022-03-09 14:42:11
 */
@RestController
@RequestMapping("app/accidentReport")
public class AccidentReportController {
    @Autowired
    private AccidentReportService accidentReportService;

    @Autowired
    private DeptService deptService;
    /**
     * 区级列表（信息上报：草稿，退回，快报退回）
     */
    @RequestMapping("/reportList")
    @Login
    public R reportList(@RequestParam Map<String, Object> params, @LoginUser SysUserEntity sysUser){
        PageUtils page = accidentReportService.reportList(params,sysUser);
        return R.ok().put("page", page);
    }

    /**
     * 区级列表（信息签发：待签发，签发完成）
     */
    @RequestMapping("/signList")
    @Login
    public R list(@RequestParam Map<String, Object> params, @LoginUser SysUserEntity sysUser){
        PageUtils page = accidentReportService.signList(params,sysUser);
        return R.ok().put("page", page);
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Integer id){
		AccidentReportEntity accidentReport = accidentReportService.getById(id);

        if(accidentReport.getCopyForUnitIdsList() == null && StringUtils.isNotBlank(accidentReport.getCopyForUnitIds())){
            accidentReport.setCopyForUnitIdsList(accidentReport.getCopyForUnitIds().split(","));
        }
        return R.ok().put("accidentReport", accidentReport);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @Login
    public R save(@Valid @RequestBody AccidentReportEntity accidentReport, @LoginUser SysUserEntity sysUser){
        //续报必须上次的已经上报（待签发）
        if(accidentReport.getParentId() != null && accidentReport.getParentId() != 0){
            AccidentReportEntity parentEntity = accidentReportService.getById(accidentReport.getParentId());
            if(parentEntity.getStatus() != 1){
                throw new RRException("首次上报提交后，才能进行续报");
            }
        }
        accidentReport.setUploadImage(JSON.toJSONString(accidentReport.getUploadImageList()));
        accidentReport.setUploadVideo(JSON.toJSONString(accidentReport.getUploadVideoList()));
        accidentReport.setUploadVoice(JSON.toJSONString(accidentReport.getUploadVoiceList()));
        accidentReport.setUploadFile(JSON.toJSONString(accidentReport.getUploadFileList()));

        //处理抄送单位IDS
        if(ArrayUtil.isNotEmpty(accidentReport.getCopyForUnitIdsList())){
            accidentReport.setCopyForUnitIds(ArrayUtil.join(accidentReport.getCopyForUnitIdsList(),","));
            List<DeptEntity> deptEntities = deptService.selectDeptListByIds(accidentReport.getCopyForUnitIdsList());
            List<String> deptNames = deptEntities.stream().map(DeptEntity::getName).collect(Collectors.toList());
            accidentReport.setCopyForUnit(String.join(",",deptNames));
        }
        //记录创建者市平台ID
        accidentReport.setCreateUserConnectId(sysUser.getUserConnectId());
        accidentReportService.save(accidentReport);
        //如果number为空,插入主键
//        if(accidentReport.getNumber() == null || accidentReport.getNumber().isEmpty()){
//            accidentReport.setNumber(accidentReport.getId().toString());
//            accidentReportService.updateById(accidentReport);
//        }
        return R.ok();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public R update(@RequestBody AccidentReportEntity accidentReport){
        //处理抄送单位IDS
        if(ArrayUtil.isNotEmpty(accidentReport.getCopyForUnitIdsList())){
            accidentReport.setCopyForUnitIds(ArrayUtil.join(accidentReport.getCopyForUnitIdsList(),","));
            List<DeptEntity> deptEntities = deptService.selectDeptListByIds(accidentReport.getCopyForUnitIdsList());
            List<String> deptNames = deptEntities.stream().map(DeptEntity::getName).collect(Collectors.toList());
            accidentReport.setCopyForUnit(String.join(",",deptNames));
        }
		accidentReportService.updateById(accidentReport);
        return R.ok();
    }

    /**
     * 单个删除
     */
    @RequestMapping("/deleteById")
    @Login
    public R delete(@RequestParam Integer id){
        accidentReportService.deleteAccidentReportById(id);
        return R.ok();
    }


    /**
     * 批量删除
     */
    @RequestMapping("/deleteBatch")
    @Login
    public R deleteBatch(@RequestParam Integer[] ids){
		accidentReportService.deleteBatch(ids);
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
        return accidentReportService.submit(id,status,refuseReason);
    }

    /**
     * 导出区级快报专报
     */
    @GetMapping("/export")
    @ApiOperation("导出风险告知卡")
    @Login
    public void export(@RequestParam Integer id, HttpServletResponse response, @LoginUser SysUserEntity user) throws IOException {
        accidentReportService.getExportMap(id, response, user);
    }

//    /**
//     * 导出区级多个快报专报
//     */
//    @GetMapping("/exportList")
//    @ApiOperation("导出风险告知卡")
//    @Login
//    public void exportList(@RequestParam List<Integer> ids, HttpServletResponse response, @LoginUser SysUserEntity user) throws IOException {
//        accidentReportService.getExportMapList(ids, response, user);
//    }


    /**
     * number校验
     */
    @GetMapping("/checkNumber")
    public R checkNumber(@RequestParam String number) {
        return accidentReportService.checkNumber(number);
    }

}
