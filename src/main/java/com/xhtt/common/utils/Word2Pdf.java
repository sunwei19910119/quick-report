package com.xhtt.common.utils;


import cn.hutool.core.io.resource.ClassPathResource;
import com.aspose.words.Document;
import com.aspose.words.License;
import com.aspose.words.SaveFormat;
import com.xhtt.common.exception.RRException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

@Component
public class Word2Pdf {
    private static boolean result;

    @Value("${renren.file.path: null}")
    private String filePath;

    /**
     * 获取license
     *
     * @return
     */
    static {
        try {
            InputStream license = new ClassPathResource("lic/license.xml").getStream();//new FileInputStream(ResourceUtils.getFile("classpath:lic/license.xml"));// 凭证文件
            License aposeLic = new License();
            aposeLic.setLicense(license);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void tf(String sfileFullName) {
        if (!result) {
            throw new RRException("许可证获取异常");
        }
        String sFileName = sfileFullName.substring(0, sfileFullName.lastIndexOf("."));
//        InputStream fileInput = null;
//        FileOutputStream fileOS = null;
        File outputFile = new File(filePath + sFileName + ".pdf");// 输出路径
        try (InputStream fileInput = new FileInputStream(new File(filePath + sfileFullName));// 待处理的文件;
             FileOutputStream fileOS = new FileOutputStream(outputFile)) {
//            fileInput = new FileInputStream(new File(filePath + sfileFullName));// 待处理的文件

            Document doc = new Document(fileInput);
//            fileOS = new FileOutputStream(outputFile);
            doc.save(fileOS, SaveFormat.PDF);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
