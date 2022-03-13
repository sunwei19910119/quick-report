package com.xhtt.common.utils;

/**
 * @Date 2018-10-19 下午 5:58
 */

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Component
public class Base64Util {
    @Value("${renren.file.path: null}")
    private String filePath;

    public String getImageStr(String path) {
        //1、校验是否为空
        if (path == null || path.trim().length() <= 0) {
            return "";
        }

        String fullPath = filePath + path;
        //2、校验文件是否为目录或者是否存在
        File picFile = new File(fullPath);
        if (picFile.isDirectory() || (!picFile.exists())) {
            return "";
        }

        //3、校验是否为图片
        try {
            BufferedImage image = ImageIO.read(picFile);
            if (image == null) {
                return "";
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            return "";
        }

        //4、转换成base64编码
        String imageStr = "";
        try {
            byte[] data = null;
            InputStream in = new FileInputStream(fullPath);
            data = new byte[in.available()];
            in.read(data);
            BASE64Encoder encoder = new BASE64Encoder();
            imageStr = encoder.encode(data);
        } catch (Exception e) {
            imageStr = "";
            e.printStackTrace();
        }

        return imageStr;
    }
}
