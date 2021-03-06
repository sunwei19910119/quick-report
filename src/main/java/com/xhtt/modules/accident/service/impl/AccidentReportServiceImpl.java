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
        //普通用户仅可查看自己创建的，部门领导可以查看所在部门所有
        //判断当前用户是否为处室负责人，或者分管领导
//        boolean isManager = baseManageUserlevelService.isManager(sysUser.getBmdm(),sysUser.getUserConnectId());
//        if(isManager == false){
//            userConnectIds.add(sysUser.getUserConnectId());
//        }else{
//            //领导查询所在部门的所有人员id
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
        //普通用户仅可查看自己创建的，部门领导可以查看所在部门所有
        //判断当前用户是否为处室负责人，或者分管领导
//        boolean isManager = baseManageUserlevelService.isManager(sysUser.getBmdm(),sysUser.getUserConnectId());
//        if(isManager == false){
//            userConnectIds.add(sysUser.getUserConnectId());
//        }else{
//            领导查询所在部门的所有人员id
//            List<CzBaseZfyhjbxxEntity> baseZfyhjbxxEntities = baseZfyhjbxxService.selectListByBmdm(sysUser.getBmdm());
//            baseZfyhjbxxEntities.stream().forEach(a -> {userConnectIds.add(a.getUserId());});
//        }
        //草稿只有自己可见
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
             fileName = "值班快报" + ".docx";
        }else {
             fileName = "突发事件信息专报" + ".docx";
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
            throw new RRException("参数错误");
        }

        Map<String, Object> map = new HashMap<>();
        File path = new File(filePath);
        String name= null ;
        String wordDirectoryName = "word" + System.currentTimeMillis();
        String wordDirectoryPath = path.getAbsolutePath();
        //导出前的检查，生成word文件夹
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
        //判断文件夹是否存在，存在清空文件夹，不存在创建文件夹
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
     * 压缩word文件夹
     * @param response
     * @param wordDirectoryName
     * @throws IOException
     */
    public void compressZipFile(HttpServletResponse response,String wordDirectoryName) throws IOException {
          String zipName =  wordDirectoryName + ".zip";
        try {
            //输出到E盘下files文件加下，名字是
            FileOutputStream zip = new FileOutputStream(new File(zipPath + zipName));
            ZipUtil.toZip(filePath + "\\" + wordDirectoryName, zip , true);
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
//            out.close();
        }
    }


    /**
     * 先根遍历序递归删除文件夹
     *
     * @param dirFile 要被删除的文件或者目录
     * @return 删除成功返回true, 否则返回false
     */
    public static boolean deleteFile(File dirFile) {
        // 如果dir对应的文件不存在，则退出
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