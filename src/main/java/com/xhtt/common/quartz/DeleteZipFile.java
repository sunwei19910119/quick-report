package com.xhtt.common.quartz;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author SunWei
 * @Date: 2022/3/25 8:41
 * @Description:
 */
@Component
public class DeleteZipFile {
    @Value("${renren.file.zipPath: null}")
    private String zipPath;

    private static final Logger logger = LoggerFactory.getLogger(DeleteZipFile.class);

    /**
     * @author: SunWei
     * @date: 2022/3/25 9:26
     * @param: []
     * @return: void
     * @description: 每天凌晨1点删除zip目录
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void clearDataJob(){
            logger.info("---------定时任务开始执行---------"+new SimpleDateFormat("HH:mm:ss").format(new Date()));
            clearData();
            logger.info("---------定时任务执行成功---------"+new  SimpleDateFormat("HH:mm:ss").format(new Date()));
     }



     private void clearData() {
        try{
            File file = new File(zipPath);
            if(file.exists()){
                file.delete();
            }
            }catch (Exception e){
                logger.error("清理文件失败，失败原因："+e.getMessage());
             }
    }
}

