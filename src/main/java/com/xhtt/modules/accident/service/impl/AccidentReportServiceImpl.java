package com.xhtt.modules.accident.service.impl;

import cn.afterturn.easypoi.entity.ImageEntity;
import cn.afterturn.easypoi.word.WordExportUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.extra.qrcode.QrCodeUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xhtt.common.file.FileInfoModel;
import com.xhtt.common.utils.*;
import com.xhtt.modules.accident.controller.vo.AccidentReportSimpleVo;
import com.xhtt.modules.event.service.EventReportService;
import com.xhtt.modules.sms.service.ISendSmsService;
import com.xhtt.modules.sys.entity.SysUserEntity;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.xhtt.modules.accident.dao.AccidentReportDao;
import com.xhtt.modules.accident.entity.AccidentReportEntity;
import com.xhtt.modules.accident.service.AccidentReportService;

import javax.servlet.http.HttpServletResponse;


@Service("accidentReportService")
public class AccidentReportServiceImpl extends ServiceImpl<AccidentReportDao, AccidentReportEntity> implements AccidentReportService {

    @Autowired
    AccidentReportDao accidentReportDao;

    @Autowired
    EventReportService eventReportService;

    @Autowired
    private ISendSmsService sendSmsService;

    @Autowired
    RedisUtils redis;

    @Override
    public PageUtils reportList(Map<String, Object> params) {
        params.put("level","0");
        Page<AccidentReportSimpleVo> page = new Query<AccidentReportSimpleVo>(params).getPage();
        List<AccidentReportSimpleVo> list = baseMapper.reportList(page, params);
        list.forEach(this::convertRegion);
        page.setRecords(list);
        return new PageUtils(page);
    }


    @Override
    public PageUtils signList(Map<String, Object> params) {
        params.put("level","0");
        Page<AccidentReportSimpleVo> page = new Query<AccidentReportSimpleVo>(params).getPage();
        List<AccidentReportSimpleVo> list = baseMapper.signList(page, params);
        list.forEach(this::convertRegion);
        page.setRecords(list);
        return new PageUtils(page);
    }

    @Override
    public void deleteAccidentReportById(Integer id) {
        accidentReportDao.deleteById(id);
    }

    @Override
    public void deleteBatch(Integer[] ids){
        accidentReportDao.deleteBatch(ids);
    }

    @Override
    public R submit(int id,Integer status,String refuseReason) {
        int result = accidentReportDao.submit(id,status,refuseReason);
        if(result != 1){
            return  R.error("提交失败");
        }
        AccidentReportEntity accidentReport = this.getById(id);
        if(status == 3){
            //区级签发，同时上报市级
            eventReportService.accidentReport(accidentReport);
            //签发同时发送短信
            sendSmsService.sendAccidentReportSms(accidentReport);
        }
        return R.ok();
    }

    private void convertRegion(AccidentReportSimpleVo accidentReportSimpleVo){
        accidentReportSimpleVo.setTownName(redis.get(accidentReportSimpleVo.getTownCode()));
    }

    @Override
    public Map<String, Object> getExportMap(Integer id, HttpServletResponse response, SysUserEntity user) throws UnsupportedEncodingException {
        Map<String, Object> map = new HashMap<>();
        LocalDateTime now = LocalDateTime.now();
        AccidentReportEntity accidentReportEntity = getById(id);

        map.put("year", now.getYear());
        map.put("moon", now.getMonth().toString());
        map.put("day", now.toString());
        map.put("companyName", accidentReportEntity.getCompanyName());
        map.put("accidentTime", accidentReportEntity.getAccidentTime().toString());
        map.put("accidentSite", accidentReportEntity.getAccidentSite());
        map.put("accidentDescription", accidentReportEntity.getAccidentDescription());
        map.put("receiveWay", accidentReportEntity.getReceiveWay());
        map.put("receiveTime", accidentReportEntity.getReceiveTime().toString());
        map.put("copyForUnit", accidentReportEntity.getCopyForUnit());
        map.put("userName", user.getName());
        map.put("signer", accidentReportEntity.getSigner());


        response.setCharacterEncoding("utf-8");
        response.setContentType("application/msword");
        // 设置浏览器以下载的方式处理该文件名
        String fileName = map.get("fileName") + ".docx";
        response.setHeader("Content-Disposition", "attachment;filename=".concat(String.valueOf(URLEncoder.encode(fileName, "UTF-8"))));
//        WordUtils.exportWord(response, map, map.get("ftlFile").toString(), map.get("fileName").toString());
        try (OutputStream fos = response.getOutputStream()) {
            if (accidentReportEntity.getType() == 0){
                XWPFDocument doc = WordExportUtil.exportWord07("templates/值班快报" + ".docx", map);
                doc.write(fos);
            }else {
                XWPFDocument doc = WordExportUtil.exportWord07("templates/突发事件信息专报" + ".docx", map);
                doc.write(fos);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }
}