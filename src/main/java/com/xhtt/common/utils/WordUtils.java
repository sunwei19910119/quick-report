package com.xhtt.common.utils;

/**
 * @Date 2018-10-19 下午 5:58
 */

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.Version;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

public class WordUtils {
    //配置信息,代码本身写的还是很可读的,就不过多注解了
    private static Configuration configuration = null;
    //这里注意的是利用WordUtils的类加载器动态获得模板文件的位置
    // private static final String templateFolder = WordUtils.class.getClassLoader().getResource("../../").getPath() + "WEB-INF/templetes/";
    public static String tempFilePath;

    static {
        configuration = new Configuration(new Version(2, 3, 23));
        configuration.setDefaultEncoding("utf-8");
        try {
//            configuration.setDirectoryForTemplateLoading(ResourceUtils.getFile("classpath:templates/"));
            configuration.setClassForTemplateLoading(WordUtils.class, "/templates");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Configuration getConfiguration() {
        return configuration;
    }

    private WordUtils() {
        throw new AssertionError();
    }

    public static void exportWord(HttpServletResponse response, Map map, String ftlFile, String name) throws IOException {
        File file = null;
        InputStream fin = null;
        ServletOutputStream out = null;
        try {
            Template freemarkerTemplate = configuration.getTemplate(ftlFile);
            // 调用工具类的createDoc方法生成Word文档
            file = createDoc(map, freemarkerTemplate);
            fin = new FileInputStream(file);

            response.setCharacterEncoding("utf-8");
            //response.setContentType("application/msword");
            //response.setContentType("application/octet-stream");
            response.setContentType("application/json;charset=utf-8");
            // 设置浏览器以下载的方式处理该文件名
            String fileName = name + ".doc";
            response.setHeader("Content-Disposition", "attachment;filename=".concat(String.valueOf(URLEncoder.encode(fileName, "UTF-8"))));
            out = response.getOutputStream();
            byte[] buffer = new byte[512];  // 缓冲区
            int bytesToRead = -1;
            // 通过循环将读入的Word文件的内容输出到浏览器中
            while ((bytesToRead = fin.read(buffer)) != -1) {
                out.write(buffer, 0, bytesToRead);
            }
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

    public static void createWord(Map dataMap, String templateName, String filePath, String fileName) {
        try {
            // 获取模板
            Template template = configuration.getTemplate(templateName);

            // 输出文件
            File outFile = new File(filePath + File.separator + fileName);

            // 如果输出目标文件夹不存在，则创建
            if (!outFile.getParentFile().exists()) {
                outFile.getParentFile().mkdirs();
            }

            // 将模板和数据模型合并生成文件
            Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile), "UTF-8"));

            // 生成文件
            template.process(dataMap, out);

            // 关闭流
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static File createDoc(Map<?, ?> dataMap, Template template) {
        String name = "casp.doc";
        File f = new File(name);
//        Template t = template;
        try {
            // 这个地方不能使用FileWriter因为需要指定编码类型否则生成的Word文档会因为有无法识别的编码而无法打开
            Writer w = new OutputStreamWriter(new FileOutputStream(f), StandardCharsets.UTF_8);
            template.process(dataMap, w);
            w.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
        return f;
    }

    //获得图片的base64码
    public static String getImageBase(String name) {
        if (name == null || name == "") {
            return null;
        }
        File file = new File(tempFilePath + name);
        if (!file.exists()) {
            return null;
        }
        InputStream in = null;
        byte[] data = null;
        try {
            in = new FileInputStream(file);
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
            return null;
        }
        try {
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(data);
    }
}
