package com.xhtt.common.utils;


import com.aspose.words.Document;
import com.aspose.words.License;
import com.aspose.words.SaveFormat;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

@Component
public class PdfUtils {
    @Value("${renren.file.path: null}")
    private String uploaderFloder;

//    private static InputStream license;
//    private static boolean result;

    /**
     * 获取license
     *
     * @return
     */
//    static {
//        try {
//            Resource resource = new ClassPathResource("lic/license.xml");
//            license = resource.getInputStream();
////            license = new FileInputStream(ResourceUtils.getFile("classpath:lic/license.xml"));// 凭证文件
//            License aposeLic = new License();
//            aposeLic.setLicense(license);
//            result = true;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
    public static void tf(String sfileFullName) {
        try {
            Resource resource = new ClassPathResource("lic/license.xml");
            InputStream license = resource.getInputStream();
//            license = new FileInputStream(ResourceUtils.getFile("classpath:lic/license.xml"));// 凭证文件
            License aposeLic = new License();
            aposeLic.setLicense(license);
        } catch (Exception e) {
            return;
        }
        String sFileName = sfileFullName.substring(0, sfileFullName.lastIndexOf("."));
        InputStream fileInput = null;
        FileOutputStream fileOS = null;
        try {
            fileInput = new FileInputStream(new File(sfileFullName));// 待处理的文件
            File outputFile = new File(sFileName + ".pdf");// 输出路径
            Document doc = new Document(fileInput);
            fileOS = new FileOutputStream(outputFile);
            doc.save(fileOS, SaveFormat.PDF);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != fileOS) {
                try {
                    fileOS.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != fileInput) {
                try {
                    fileInput.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void createPdf(String fileFullName) {
        try {
            Resource resource = new ClassPathResource("lic/license.xml");
            InputStream license = resource.getInputStream();
//            license = new FileInputStream(ResourceUtils.getFile("classpath:lic/license.xml"));// 凭证文件
            License aposeLic = new License();
            aposeLic.setLicense(license);
        } catch (Exception e) {
            return;
        }
//        if (!result) {
//            return;
//        }
        InputStream fileInput = null;
        FileOutputStream fileOS = null;
        try {
            fileInput = new FileInputStream(new File(fileFullName + ".doc"));// 待处理的文件
            File outputFile = new File(fileFullName + ".pdf");// 输出路径
            Document doc = new Document(fileInput);
            fileOS = new FileOutputStream(outputFile);
            doc.save(fileOS, SaveFormat.PDF);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != fileOS) {
                try {
                    fileOS.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != fileInput) {
                try {
                    fileInput.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void exportPdf(HttpServletResponse response, String fullFileName, String fileName) throws IOException {
        File file = null;
        InputStream fin = null;
        ServletOutputStream out = null;
        try {
            createPdf(fullFileName);
            // 以流的形式下载文件。
            InputStream fis = null;
            try {
                fis = new BufferedInputStream(new FileInputStream(fullFileName + ".pdf"));
            } catch (FileNotFoundException e) {
                throw new Exception("文件不存在");
            }
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            // 清空response
            response.reset();
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            //response.setContentType("application/octet-stream");
            response.setContentType("application/json;charset=utf-8");
            response.setHeader("Content-Disposition", "attachment;filename=".concat(String.valueOf(URLEncoder.encode(fileName + ".pdf", "UTF-8"))));
            //response.setHeader("Content-Disposition", "attachment;filename=" +fileName+".pdf");
            toClient.write(buffer);
            toClient.flush();
            toClient.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fin != null) {
                fin.close();
            }
            if (out != null) {
                out.close();
            }
            if (file != null) {
                file.delete(); // 删除临时文件
            }
        }
    }

    public static void exportPdf2(HttpServletResponse response, String fullFileName) throws IOException {
        File file = null;
        InputStream fin = null;
        ServletOutputStream out = null;
        try {
            createPdf(fullFileName);
            // 以流的形式下载文件。
            InputStream fis = null;
            try {
                fis = new BufferedInputStream(new FileInputStream(fullFileName + ".pdf"));
            } catch (FileNotFoundException e) {
                throw new Exception("文件不存在");
            }
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fin != null) {
                fin.close();
            }
            if (out != null) {
                out.close();
            }
            if (file != null) {
                file.delete(); // 删除临时文件
            }
        }
    }

}
