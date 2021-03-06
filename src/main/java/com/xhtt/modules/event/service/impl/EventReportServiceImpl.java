package com.xhtt.modules.event.service.impl;

import cn.afterturn.easypoi.word.WordExportUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xhtt.common.exception.RRException;
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
import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.xhtt.common.utils.PageUtils;
import com.xhtt.common.utils.Query;
import com.xhtt.common.utils.R;
import com.xhtt.common.utils.RedisUtils;

import java.io.*;
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

    @Value("${renren.file.zipPath: null}")
    private String zipPath;

    @Value("${renren.file.path: null}")
    private String filePath;

    @Override
    public PageUtils reportList(Map<String, Object> params,SysUserEntity sysUser) {
//        List<String> userConnectIds = new ArrayList<>();
        //????????????????????????????????????????????????????????????????????????????????????
        //???????????????????????????????????????????????????????????????
//        boolean isManager = baseManageUserlevelService.isManager(sysUser.getBmdm(),sysUser.getUserConnectId());
//        if(isManager == false){
//            userConnectIds.add(sysUser.getUserConnectId());
//        }else{
//            //???????????????????????????????????????id
//            List<CzBaseZfyhjbxxEntity> baseZfyhjbxxEntities = baseZfyhjbxxService.selectListByBmdm(sysUser.getBmdm());
//            baseZfyhjbxxEntities.stream().forEach(a -> {userConnectIds.add(a.getUserId());});
//        }
//        userConnectIds.add(sysUser.getUserConnectId());
        params.put("userId",sysUser.getUserConnectId());
        Page<EventReportSimpleVo> page = new Query<EventReportSimpleVo>(params).getPage();
        List<EventReportSimpleVo> list = baseMapper.reportList(page, params);
        list.forEach(this::convertCounty);
        page.setRecords(list);
        return new PageUtils(page);
    }


    @Override
    public PageUtils signList(Map<String, Object> params,SysUserEntity sysUser) {
//        List<String> userConnectIds = new ArrayList<>();
        //????????????????????????????????????????????????????????????????????????????????????
        //???????????????????????????????????????????????????????????????
//        boolean isManager = baseManageUserlevelService.isManager(sysUser.getBmdm(),sysUser.getUserConnectId());
//        if(isManager == false){
//            userConnectIds.add(sysUser.getUserConnectId());
//        }else{
//            //???????????????????????????????????????id
//            List<CzBaseZfyhjbxxEntity> baseZfyhjbxxEntities = baseZfyhjbxxService.selectListByBmdm(sysUser.getBmdm());
//            baseZfyhjbxxEntities.stream().forEach(a -> {userConnectIds.add(a.getUserId());});
//        }
//        userConnectIds.add(sysUser.getUserConnectId());
        params.put("userId",sysUser.getUserConnectId());
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

        if(status == 3){
            //????????????????????????
            sendSmsService.sendEventReportSms(eventReportInfo);
        }
        return result == 1 ? R.ok() : R.error("????????????");
    }

    private void convertCounty(EventReportSimpleVo eventReportSimpleVo){
        eventReportSimpleVo.setCountyName(redis.get(eventReportSimpleVo.getCountyCode()));
    }

    //?????????????????????????????????
    @Override
    public void accidentReport(AccidentReportEntity accidentReport){
        EventReportConvert eventReportConvert = Mappers.getMapper(EventReportConvert.class);
        EventReportEntity eventReportEntity = eventReportConvert.convert(accidentReport);
        eventReportEntity.setStatus(5);
        eventReportEntity.setAccidentReportId(accidentReport.getId());
        this.save(eventReportEntity);
    }


    //??????????????????
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
            fileName = "????????????" + ".docx";
        } else {
            fileName = "????????????????????????" + ".docx";
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
        String maxNumber = eventReportDao.maxNumber();
        HashMap data = new HashMap<>();
        HashMap result = new HashMap<>();
        data.put("isRepeat",count);
        data.put("maxNumber",maxNumber);
        result.put("data",data);
        return R.ok(result);
    }






    @Override
    public String getExportMapList(List<Integer> ids, HttpServletResponse response, SysUserEntity user) throws Exception {
        if (CollectionUtils.isEmpty(ids)){
            throw new RRException("????????????");
        }

        Map<String, Object> map = new HashMap<>();
        File path = new File(filePath);
        String name= null ;
        String wordDirectoryName = "word" + System.currentTimeMillis();
        String wordDirectoryPath = path.getAbsolutePath();
        //???????????????????????????word?????????
        beforeExportWord(path,wordDirectoryName,wordDirectoryPath);
        ids.forEach( id -> {
            LocalDateTime now = LocalDateTime.now();
            EventReportEntity eventReportEntity = getById(id);
            map.put("year", now.getYear());
            map.put("moon", now.getMonthValue());
            map.put("day", DateUtils.format(new Date(),"dd"));
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
            map.put("type", eventReportEntity.getType());
            map.put("title", eventReportEntity.getTitle());

            //?????????word????????????word????????????
//            try (OutputStream fos = response.getOutputStream()) {
            try {
                String typeName = null;
                Date date = new Date();
                DateUtils.format(date, "yyyyMM");
                FileOutputStream fileOutputStream = new FileOutputStream(filePath + "\\" + wordDirectoryName+ "\\" + map.get("number") + ".docx");
                if (Integer.parseInt(map.get("type").toString()) == 0){
                    typeName = "zbkb";
                }else {
                    typeName = "tfsjxxzb";
                }
                XWPFDocument doc = WordExportUtil.exportWord07("templates/"+ typeName + ".docx", map);
                try {
                    doc.write(fileOutputStream);
//                        fileOutputStream.write(bao.toByteArray());
                    fileOutputStream.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                compressZipFile(response,wordDirectoryName);
            } catch (Exception e) {
                e.printStackTrace();
            }

        });
        File word = new File(wordDirectoryPath + "/" + wordDirectoryName);
        deleteFile(word);
        return wordDirectoryName + ".zip";
    }

    /**
     * ???????????????
     *
     */
    public void beforeExportWord(File path,String wordDirectoryName,String wordDirectoryPath) throws Exception {
        File word = new File(wordDirectoryPath, wordDirectoryName);
        //??????????????????????????????????????????????????????????????????????????????
        if (word.exists()) {
            for (File f : word.listFiles()) {
                f.delete();
            }
        } else {
            word.mkdir();
        }
        File wordZip = new File(filePath, "zip");
        //???????????????????????????,????????????????????????
        if (wordZip.exists()) {
            for (File f : wordZip.listFiles()) {
            }
        } else {
            wordZip.mkdir();
        }

    }


    /**
     * ??????word?????????
     * @param response
     * @param wordDirectoryName
     * @throws IOException
     */
    public void compressZipFile(HttpServletResponse response,String wordDirectoryName) throws IOException {
        String zipName =  wordDirectoryName + ".zip";
        try {
            //?????????E??????files????????????????????????
            FileOutputStream zip = new FileOutputStream(new File(zipPath + zipName));
            ZipUtil.toZip(filePath + "\\" + wordDirectoryName, zip , true);
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
//            out.close();
        }
    }


    /**
     * ????????????????????????????????????
     *
     * @param dirFile ?????????????????????????????????
     * @return ??????????????????true, ????????????false
     */
    public static boolean deleteFile(File dirFile) {
        // ??????dir????????????????????????????????????
        if (!dirFile.exists()) {
            return false;
        }
        if (dirFile.isFile()) {
            return dirFile.delete();
        } else {
            for (File file : dirFile.listFiles()) {
                deleteFile(file);
            }
        }
        return dirFile.delete();
    }

}