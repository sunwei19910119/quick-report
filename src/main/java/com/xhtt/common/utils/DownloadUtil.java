package com.xhtt.common.utils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

public class DownloadUtil {

    // 字符编码格式
    private static String charsetCode = "utf-8";


    /**
     * 文件的内容类型
     */
    private static String getFileContentType(String name){
        String result = "";
        String fileType = name.toLowerCase();
        if (fileType.endsWith(".png")) {
            result = "image/png";
        } else if (fileType.endsWith(".gif")) {
            result = "image/gif";
        } else if (fileType.endsWith(".jpg") || fileType.endsWith(".jpeg")) {
            result = "image/jpeg";
        } else if(fileType.endsWith(".svg")){
            result = "image/svg+xml";
        }else if (fileType.endsWith(".doc")) {
            result = "application/msword";
        } else if (fileType.endsWith(".xls")) {
            result = "application/x-excel";
        } else if (fileType.endsWith(".zip")) {
            result = "application/zip";
        } else if (fileType.endsWith(".pdf")) {
            result = "application/pdf";
        } else {
            result = "application/octet-stream";
        }
        return result;
    }

    /**
     * 下载文件
     * @param path 文件的位置
     * @param fileName 自定义下载文件的名称
     * @param resp http响应
     * @param req http请求
     */
    public static void downloadFile(String path, String fileName, HttpServletResponse resp, HttpServletRequest req){

        try {
            File file = new File(path);
            /**
             * 中文乱码解决
             */
            String type = req.getHeader("User-Agent").toLowerCase();
            if(type.indexOf("firefox")>0 || type.indexOf("chrome")>0){
                /**
                 * 谷歌或火狐
                 */
                fileName = new String(fileName.getBytes(charsetCode), "iso8859-1");
            }else{
                /**
                 * IE
                 */
                fileName = URLEncoder.encode(fileName, charsetCode);
            }
            // 设置响应的头部信息
            resp.setHeader("content-disposition", "attachment;filename=" + fileName);
            // 设置响应内容的类型
            resp.setContentType(getFileContentType(fileName)+"; charset=" + charsetCode);
            // 设置响应内容的长度
            resp.setContentLength((int) file.length());
            // 输出
            outStream(new FileInputStream(file), resp.getOutputStream());
        } catch (Exception e) {
            System.out.println("执行downloadFile发生了异常：" + e.getMessage());
        }
    }

    private static void outStream(FileInputStream fileInputStream, ServletOutputStream outputStream) {
    }

    /**
     * 基础字节数组输出
     */
    private static void outStream(InputStream is, OutputStream os) {
        try {
            byte[] buffer = new byte[10240];
            int length = -1;
            while ((length = is.read(buffer)) != -1) {
                os.write(buffer, 0, length);
                os.flush();
            }
        } catch (Exception e) {
            System.out.println("执行 outStream 发生了异常：" + e.getMessage());
        } finally {
            try {
                os.close();
            } catch (IOException e) {
            }
            try {
                is.close();
            } catch (IOException e) {
            }
        }
    }
}
