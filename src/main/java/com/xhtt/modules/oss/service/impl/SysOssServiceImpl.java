/**
 * Copyright 2018 人人开源 http://www.renren.io
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.xhtt.modules.oss.service.impl;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import com.xhtt.common.exception.RRException;
import com.xhtt.common.utils.DateUtils;
import com.xhtt.common.utils.PageUtils;
import com.xhtt.common.utils.Query;
import com.xhtt.modules.oss.dao.SysOssDao;
import com.xhtt.modules.oss.entity.SysOssEntity;
import com.xhtt.modules.oss.service.SysOssService;
import com.xhtt.modules.sys.entity.SysUserEntity;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Date;
import java.util.Map;
import java.util.UUID;


@Service("sysOssService")
public class SysOssServiceImpl extends ServiceImpl<SysOssDao, SysOssEntity> implements SysOssService {

    @Value("${renren.file.path: null}")
    private String filePath;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<SysOssEntity> page = (Page<SysOssEntity>) this.page(
                new Query<SysOssEntity>(params).getPage()
        );

        return new PageUtils(page);
    }

    @Override
    public String uploadFile(MultipartFile file, boolean watermark, SysUserEntity user) throws IOException {
        String originalFilename = file.getOriginalFilename();
        if (!originalFilename.contains(".")) {
            throw new RRException("文件类型异常");
        }
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        if (StringUtils.isBlank(suffix)) {
            throw new RRException("文件类型异常");
        }
        String name = UUID.randomUUID().toString();
        if ("null".equals(filePath)) {
            filePath = System.getProperty("user.dir") + "/files/";
        }
        File dest = new File(filePath + name + suffix);
        dest.getParentFile().mkdirs();
        file.transferTo(dest);
        dest.setReadable(true, false);
        dest.setWritable(true, false);

        if (watermark) {
            /**开始：加水印**/
            try {
                FileInputStream in = new FileInputStream(filePath + name + suffix);
                Image src = ImageIO.read(in);
                if (null != src) {//是图片文件就加水印
                    int w = src.getWidth(null);
                    int h = src.getHeight(null);
                    float alpha = 0.4f; // 透明度
                    BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);//构建画板
                    Graphics2D g = img.createGraphics();//得到画笔
                    g.drawImage(src, 0, 0, w, h, null);//把源图片写入画板
                    Font fsib30 = new Font("微软雅黑", Font.BOLD + Font.ITALIC, img.getWidth() / 16);
                    g.setFont(fsib30);
                    g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
                    g.setColor(Color.black);
                    g.setBackground(Color.white);
//                g.rotate(Math.toRadians(-15),
//                        (double) img.getWidth() / 2, (double) img
//                                .getHeight() / 2);
//                g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
//                        RenderingHints.VALUE_ANTIALIAS_ON);
                    String pressText = DateUtils.format(new Date(), DateUtils.DATE_TIME_PATTERN);
//                g.drawString(pressText, (w - (getLength(pressText) * img.getWidth() / 16))
//                        / 2 + 10, (h - img.getWidth() / 16) / 1 + 10);
                    g.drawString(pressText, (w - (getLength(pressText) * img.getWidth() / 16))
                            / 2 + 10, (h - img.getWidth() / 16) / 1 + 10);
                    g.drawString(user.getName(), (w - (getLength(user.getName()) * img.getWidth() / 16))
                            / 2 + 10, (h - img.getWidth() / 16) / 1 - 25);
                    //设置水印的坐标
//                int x = w - 2 * getWatermarkLength(pressText, g);
//                int y = h - 2 * getWatermarkLength(pressText, g);
//                g.drawString(pressText, x, y);  //画出水印
                    g.dispose();//生成图片
                    OutputStream out = new FileOutputStream(filePath + name + suffix);
                    JPEGImageEncoder en = JPEGCodec.createJPEGEncoder(out);
                    en.encode(img);
                }
            } catch (Exception ee) {
                ee.printStackTrace();
            }
            /**结束：加水印**/
        }
        return dest.getName();
    }

    /**
     * 获取字符长度，一个汉字作为 1 个字符, 一个英文字母作为 0.5 个字符
     *
     * @param text
     * @return 字符长度，如：text="中国",返回 2；text="test",返回 2；text="中国ABC",返回 4.
     */
    public int getLength(String text) {
        int textLength = text.length();
        int length = textLength;
        for (int i = 0; i < textLength; i++) {
            if (String.valueOf(text.charAt(i)).getBytes().length > 1) {
                length++;
            }
        }
        return (length % 2 == 0) ? length / 2 : length / 2 + 1;
    }

    public int getWatermarkLength(String waterMarkContent, Graphics2D g) {
        return g.getFontMetrics(g.getFont()).charsWidth(waterMarkContent.toCharArray(), 0, waterMarkContent.length());
    }

}
