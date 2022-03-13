package com.xhtt.common.utils;

import com.xhtt.common.exception.RRException;
import org.springframework.core.io.ClassPathResource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * @author luyc
 * @date 2018/10/25
 */
public class DownloadFile {

    /**
     * 导出文件
     */
    public static void exportFile(String fileName,String path,HttpServletResponse response) {
        ClassPathResource classPathResource = new ClassPathResource(path);
        byte[] buff = new byte[1024];
        BufferedInputStream bis = null;
        OutputStream os = null;
        try {
            //获取文件流
            InputStream stream = classPathResource.getInputStream();
            response.setHeader("content-type", "application/octet-stream");
            response.setContentType("application/json; charset=utf-8");
            String name = java.net.URLEncoder.encode(fileName, "UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" +name);
            os = response.getOutputStream();
            bis = new BufferedInputStream(stream);
            int len = 0;
            while ((len = bis.read(buff)) > 0) {
                os.write(buff, 0, len);
                os.flush();
            }
           /* int i = bis.read(buff);
            while (i != -1) {
                os.write(buff, 0, buff.length);
                os.flush();
                i = bis.read(buff);
            }*/
        }catch (UnsupportedEncodingException e2) {
            e2.printStackTrace();
        } catch (FileNotFoundException e1) {
            throw new RRException("系统找不到指定的文件");
        }catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
