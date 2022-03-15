package com.xhtt.modules.event.service.impl;

import cn.afterturn.easypoi.word.WordExportUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xhtt.common.utils.*;
import com.xhtt.modules.accident.controller.vo.AccidentReportSimpleVo;
import com.xhtt.modules.accident.dao.AccidentReportDao;
import com.xhtt.modules.accident.entity.AccidentReportEntity;
import com.xhtt.modules.accident.service.AccidentReportService;
import com.xhtt.modules.cfg.entity.CzBaseZfyhjbxxEntity;
import com.xhtt.modules.cfg.service.CzBaseManageUserlevelService;
import com.xhtt.modules.cfg.service.CzBaseZfyhjbxxService;
import com.xhtt.modules.event.controller.vo.EventReportSimpleVo;
import com.xhtt.modules.event.convert.EventReportConvert;
import com.xhtt.modules.sms.service.ISendSmsService;
import com.xhtt.modules.sys.entity.SysUserEntity;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xhtt.common.utils.PageUtils;
import com.xhtt.common.utils.Query;
import com.xhtt.common.utils.R;
import com.xhtt.common.utils.RedisUtils;

import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xhtt.modules.event.dao.EventReportDao;
import com.xhtt.modules.event.entity.EventReportEntity;
import com.xhtt.modules.event.service.EventReportService;

import javax.servlet.http.HttpServletResponse;


@Service("eventReportService")
public class EventReportServiceImpl extends ServiceImpl<EventReportDao, EventReportEntity> implements EventReportService {

    @Autowired
    EventReportDao eventReportDao;

    @Autowired
    AccidentReportDao accidentReportDao;

    @Autowired
    AccidentReportService accidentReportService;

    @Autowired
    RedisUtils redis;

    @Autowired
    ISendSmsService sendSmsService;

    @Autowired
    private CzBaseManageUserlevelService baseManageUserlevelService;

    @Autowired
    private CzBaseZfyhjbxxService baseZfyhjbxxService;


    @Override
    public PageUtils reportList(Map<String, Object> params,SysUserEntity sysUser) {
        List<String> userConnectIds = new ArrayList<>();
        //普通用户仅可查看自己创建的，部门领导可以查看所在部门所有
        //判断当前用户是否为处室负责人，或者分管领导
        boolean isManager = baseManageUserlevelService.isManager(sysUser.getBmdm(),sysUser.getUserConnectId());
        if(isManager == false){
            userConnectIds.add(sysUser.getUserConnectId());
        }else{
            //领导查询所在部门的所有人员id
            List<CzBaseZfyhjbxxEntity> baseZfyhjbxxEntities = baseZfyhjbxxService.selectListByBmdm(sysUser.getBmdm());
            baseZfyhjbxxEntities.stream().forEach(a -> {userConnectIds.add(a.getUserId());});
        }
        params.put("list",userConnectIds);
        Page<EventReportSimpleVo> page = new Query<EventReportSimpleVo>(params).getPage();
        List<EventReportSimpleVo> list = baseMapper.reportList(page, params);
        list.forEach(this::convertCounty);
        page.setRecords(list);
        return new PageUtils(page);
    }


    @Override
    public PageUtils signList(Map<String, Object> params,SysUserEntity sysUser) {
        List<String> userConnectIds = new ArrayList<>();
        //普通用户仅可查看自己创建的，部门领导可以查看所在部门所有
        //判断当前用户是否为处室负责人，或者分管领导
        boolean isManager = baseManageUserlevelService.isManager(sysUser.getBmdm(),sysUser.getUserConnectId());
        if(isManager == false){
            userConnectIds.add(sysUser.getUserConnectId());
        }else{
            //领导查询所在部门的所有人员id
            List<CzBaseZfyhjbxxEntity> baseZfyhjbxxEntities = baseZfyhjbxxService.selectListByBmdm(sysUser.getBmdm());
            baseZfyhjbxxEntities.stream().forEach(a -> {userConnectIds.add(a.getUserId());});
        }
        params.put("list",userConnectIds);
        Page<EventReportSimpleVo> page = new Query<EventReportSimpleVo>(params).getPage();
        List<EventReportSimpleVo> list = baseMapper.signList(page, params);
        list.forEach(this::convertCounty);
        page.setRecords(list);
        return new PageUtils(page);
    }

    @Override
    public void deleteById(Integer id) {
        eventReportDao.deleteById(id);
    }

    @Override
    public void deleteBatch(Integer[] ids){
        eventReportDao.deleteBatch(ids);
    }

    @Override
    public R submit(int id,Integer status,String refuseReason) {
        int result = eventReportDao.submit(id,status,refuseReason);
        EventReportEntity eventReportInfo = this.getById(id);
        //退回区级
        if(status == 2){
            if(eventReportInfo.getLevel() == 0 && eventReportInfo.getAccidentReportId() != 0 ){
                this.accidentReportRefuse(eventReportInfo);
            }
        }

        if(status == 3){
            //签发同时发送短信
            sendSmsService.sendEventReportSms(eventReportInfo);
        }
        return result == 1 ? R.ok() : R.error("提交失败");
    }

    private void convertCounty(EventReportSimpleVo eventReportSimpleVo){
        eventReportSimpleVo.setCountyName(redis.get(eventReportSimpleVo.getCountyCode()));
    }

    //区级签发的同时上报市级
    @Override
    public void accidentReport(AccidentReportEntity accidentReport){
        EventReportConvert eventReportConvert = Mappers.getMapper(EventReportConvert.class);
        EventReportEntity eventReportEntity = eventReportConvert.convert(accidentReport);
        eventReportEntity.setStatus(5);
        eventReportEntity.setAccidentReportId(accidentReport.getId());
        this.save(eventReportEntity);
    }


    //区级签发回退
    @Override
    public void accidentReportRefuse(EventReportEntity eventReportEntity){
        this.deleteById(eventReportEntity.getId());
        AccidentReportEntity accidentReportEntity = accidentReportService.getById(eventReportEntity.getAccidentReportId());
        accidentReportEntity.setStatus(4);
        accidentReportEntity.setRefuseReason(eventReportEntity.getRefuseReason());
        accidentReportService.updateById(accidentReportEntity);

    }
    @Override
    public void getExportMap(Integer id, HttpServletResponse response, SysUserEntity user) throws UnsupportedEncodingException {
        Map<String, Object> map = new HashMap<>();
        LocalDateTime now = LocalDateTime.now();
        EventReportEntity eventReportEntity = getById(id);

        map.put("year", now.getYear());
        map.put("moon", now.getMonthValue());
        map.put("day", DateUtils.format(new Date(), "dd"));
        map.put("companyName", eventReportEntity.getCompanyName());
        map.put("accidentTime", DateUtils.format(eventReportEntity.getAccidentTime(), DateUtils.DATE_PATTERN));
        map.put("accidentSite", eventReportEntity.getAccidentSite());
        map.put("accidentDescription", eventReportEntity.getAccidentDescription());
        map.put("nickName", user.getNickName());
        map.put("number", eventReportEntity.getNumber());
        map.put("countyCode", redis.get(eventReportEntity.getCountyCode()));
        map.put("signer", eventReportEntity.getSigner());
        map.put("reportUnit", eventReportEntity.getReportUnit());
        map.put("copyForUnit", eventReportEntity.getCopyForUnit());
        map.put("receiveWay", eventReportEntity.getReceiveWay());
        map.put("issueDate", DateUtils.format(eventReportEntity.getIssueDate(), DateUtils.DATE_TIME_PATTERN));

        response.setCharacterEncoding("utf-8");
        response.setContentType("application/msword");
        String fileName = null;
        if (eventReportEntity.getType() == 0) {
            fileName = "值班快报" + ".docx";
        } else {
            fileName = "突发事件信息专报" + ".docx";
        }

        response.setHeader("Content-Disposition", "attachment;filename=".concat(String.valueOf(URLEncoder.encode(fileName, "UTF-8"))));
        //WordUtils.exportWord(response, map, map.get("ftlFile").toString(), map.get("fileName").toString());
        try (OutputStream fos = response.getOutputStream()) {
            if (eventReportEntity.getType() == 0) {
                XWPFDocument doc = WordExportUtil.exportWord07("templates/zbkb" + ".docx", map);
                doc.write(fos);
            } else {
                XWPFDocument doc = WordExportUtil.exportWord07("templates/tfsjxxzb" + ".docx", map);
                doc.write(fos);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public R checkNumber(String number) {
        int count = eventReportDao.checkNumber(number);
        return count == 0 ? R.ok() : R.error("编号不能重复");
    }
}