package com.xhtt.modules.accident.service.impl;

import cn.afterturn.easypoi.entity.ImageEntity;
import cn.afterturn.easypoi.word.WordExportUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.extra.qrcode.QrCodeUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xhtt.common.exception.RRException;
import com.xhtt.common.file.FileInfoModel;
import com.xhtt.common.utils.*;
import com.xhtt.modules.accident.controller.vo.AccidentReportSimpleVo;
import com.xhtt.modules.cfg.entity.CzBaseZfyhjbxxEntity;
import com.xhtt.modules.cfg.service.CzBaseManageUserlevelService;
import com.xhtt.modules.cfg.service.CzBaseZfyhjbxxService;
import com.xhtt.modules.event.service.EventReportService;
import com.xhtt.modules.sms.service.ISendSmsService;
import com.xhtt.modules.sys.entity.SysUserEntity;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipOutputStream;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.xhtt.modules.accident.dao.AccidentReportDao;
import com.xhtt.modules.accident.entity.AccidentReportEntity;
import com.xhtt.modules.accident.service.AccidentReportService;

import javax.servlet.http.HttpServletRequest;
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
    private CzBaseManageUserlevelService baseManageUserlevelService;

    @Autowired
    private CzBaseZfyhjbxxService baseZfyhjbxxService;

    @Autowired
    RedisUtils redis;

    @Value("${renren.file.zipPath: null}")
    private String zipPath;

    @Value("${renren.file.path: null}")
    private String filePath;

    @Override
    public PageUtils reportList(Map<String, Object> params,SysUserEntity sysUser) {
        params.put("level","0");
        Page<AccidentReportSimpleVo> page = new Query<AccidentReportSimpleVo>(params).getPage();
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
        params.put("userId",sysUser.getUserConnectId());
        List<AccidentReportSimpleVo> list  = baseMapper.reportList(page, params);
        list.forEach(this::convertRegion);
        page.setRecords(list);
        return new PageUtils(page);
    }


    @Override
    public PageUtils signList(Map<String, Object> params,SysUserEntity sysUser) {
        params.put("level","0");
        Page<AccidentReportSimpleVo> page = new Query<AccidentReportSimpleVo>(params).getPage();
//        List<String> userConnectIds = new ArrayList<>();
        //????????????????????????????????????????????????????????????????????????????????????
        //???????????????????????????????????????????????????????????????
//        boolean isManager = baseManageUserlevelService.isManager(sysUser.getBmdm(),sysUser.getUserConnectId());
//        if(isManager == false){
//            userConnectIds.add(sysUser.getUserConnectId());
//        }else{
//            ???????????????????????????????????????id
//            List<CzBaseZfyhjbxxEntity> baseZfyhjbxxEntities = baseZfyhjbxxService.selectListByBmdm(sysUser.getBmdm());
//            baseZfyhjbxxEntities.stream().forEach(a -> {userConnectIds.add(a.getUserId());});
//        }
        //????????????????????????
//        userConnectIds.add(sysUser.getUserConnectId());
        params.put("userId",sysUser.getUserConnectId());
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
            return  R.error("????????????");
        }
        AccidentReportEntity accidentReport = this.getById(id);
        if(status == 3){
            //?????????????????????????????????
            eventReportService.accidentReport(accidentReport);
            //????????????????????????
            sendSmsService.sendAccidentReportSms(accidentReport);
        }
        return R.ok();
    }

    private void convertRegion(AccidentReportSimpleVo accidentReportSimpleVo){
        accidentReportSimpleVo.setTownName(redis.get(accidentReportSimpleVo.getTownCode()));
    }

    @Override
    public void getExportMap(Integer id, HttpServletResponse response, SysUserEntity user) throws UnsupportedEncodingException {
        Map<String, Object> map = new HashMap<>();
        LocalDateTime now = LocalDateTime.now();
        AccidentReportEntity accidentReportEntity = getById(id);

        map.put("year", now.getYear());
        map.put("moon", now.getMonthValue());
        map.put("day", DateUtils.format(new Date(),"dd"));
        map.put("companyName", accidentReportEntity.getCompanyName());
        map.put("accidentTime", DateUtils.format(accidentReportEntity.getAccidentTime(), DateUtils.DATE_PATTERN));
        map.put("accidentSite", accidentReportEntity.getAccidentSite());
        map.put("accidentDescription", accidentReportEntity.getAccidentDescription());
        map.put("nickName", user.getNickName());
        map.put("number", accidentReportEntity.getNumber());
        map.put("countyCode", redis.get(accidentReportEntity.getCountyCode()));
        map.put("signer", accidentReportEntity.getSigner());
        map.put("reportUnit", accidentReportEntity.getReportUnit());
        map.put("copyForUnit", accidentReportEntity.getCopyForUnit());
        map.put("receiveWay", accidentReportEntity.getReceiveWay());
        map.put("issueDate", DateUtils.format(accidentReportEntity.getIssueDate(), DateUtils.DATE_TIME_PATTERN));
        map.put("title", accidentReportEntity.getTitle());

        response.setCharacterEncoding("utf-8");
        response.setContentType("application/msword");
        String fileName= null;
        if (accidentReportEntity.getType() == 0){
             fileName = "????????????" + ".docx";
        }else {
             fileName = "????????????????????????" + ".docx";
        }

        response.setHeader("Content-Disposition", "attachment;filename=".concat(String.valueOf(URLEncoder.encode(fileName, "UTF-8"))));
        //WordUtils.exportWord(response, map, map.get("ftlFile").toString(), map.get("fileName").toString());
        try (OutputStream fos = response.getOutputStream()) {
            if (accidentReportEntity.getType() == 0){
                XWPFDocument doc = WordExportUtil.exportWord07("templates/zbkb" + ".docx", map);
                doc.write(fos);
            }else {
                XWPFDocument doc = WordExportUtil.exportWord07("templates/tfsjxxzb" + ".docx", map);
                doc.write(fos);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
            AccidentReportEntity accidentReportEntity = getById(id);
            map.put("year", now.getYear());
            map.put("moon", now.getMonthValue());
            map.put("day", DateUtils.format(new Date(),"dd"));
            map.put("companyName", accidentReportEntity.getCompanyName());
            map.put("accidentTime", DateUtils.format(accidentReportEntity.getAccidentTime(), DateUtils.DATE_PATTERN));
            map.put("accidentSite", accidentReportEntity.getAccidentSite());
            map.put("accidentDescription", accidentReportEntity.getAccidentDescription());
            map.put("nickName", user.getNickName());
            map.put("number", accidentReportEntity.getNumber());
            map.put("countyCode", redis.get(accidentReportEntity.getCountyCode()));
            map.put("signer", accidentReportEntity.getSigner());
            map.put("reportUnit", accidentReportEntity.getReportUnit());
            map.put("copyForUnit", accidentReportEntity.getCopyForUnit());
            map.put("receiveWay", accidentReportEntity.getReceiveWay());
            map.put("issueDate", DateUtils.format(accidentReportEntity.getIssueDate(), DateUtils.DATE_TIME_PATTERN));
            map.put("type", accidentReportEntity.getType());
            map.put("title", accidentReportEntity.getTitle());

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
        if (wordZip.exists()) {
            for (File f : wordZip.listFiles()) {
                //f.delete();
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



    @Override
    public R checkNumber(String number) {
        int count = accidentReportDao.checkNumber(number);
        String maxNumber = accidentReportDao.maxNumber();
        HashMap data = new HashMap<>();
        HashMap result = new HashMap<>();
        data.put("isRepeat",count);
        data.put("maxNumber",maxNumber);

        result.put("data",data);
        return R.ok(result);
    }
}