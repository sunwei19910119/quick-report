package com.xhtt.modules.oss.controller;


import com.google.gson.Gson;
import com.xhtt.common.exception.RRException;
import com.xhtt.common.file.FileInfoModel;
import com.xhtt.common.utils.*;
import com.xhtt.common.validator.ValidatorUtils;
import com.xhtt.core.annotation.Login;
import com.xhtt.core.annotation.LoginUser;
import com.xhtt.modules.oss.service.SysOssService;
import com.xhtt.modules.sys.entity.SysUserEntity;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 文件上传
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-03-25 12:13:26
 */
@RestController
@RequestMapping("app/file")
public class FileController {
    @Value("${renren.file.path: null}")
    private String filePath;
    @Resource
    private Word2Pdf word2Pdf;
    @Autowired
    private SysOssService sysOssService;

    private final static String KEY = ConfigConstant.CLOUD_STORAGE_CONFIG_KEY;


    /**
     * 图片预览
     *
     * @param fileName
     * @param httpResource
     * @throws IOException
     */
    @RequestMapping("/show/{a}/{b}/{fileName}")
    @ResponseBody
    public void showImage(@PathVariable("a") String a,@PathVariable("b") String b,@PathVariable("fileName") String fileName, HttpServletResponse httpResource) throws IOException {
        fileName = a + "/" + b + "/" + fileName;
        showImage(fileName, httpResource);
    }

    /**
     * 图片预览
     *
     * @param fileName
     * @param httpResource
     * @throws IOException
     */
    @RequestMapping("/show/{fileName}")
    @ResponseBody
    public void showImage(@PathVariable("fileName") String fileName, HttpServletResponse httpResource) throws IOException {
        // 取得文件的后缀名。
        String fileType = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        String fileN = fileName.substring(0, fileName.lastIndexOf("."));
        if (!"png".equals(fileType) && !"jpg".equals(fileType) && !"jpeg".equals(fileType) && !"bmp".equals(fileType) && !"pdf".equals(fileType)
                && !"doc".equals(fileType) && !"docx".equals(fileType)) {
            //这句话的意思，是让浏览器用utf8来解析返回的数据
            httpResource.setHeader("Content-type", "text/html;charset=UTF-8");
            //这句话的意思，是告诉servlet用UTF-8转码，而不是用默认的ISO8859
            httpResource.setCharacterEncoding("UTF-8");
            httpResource.getWriter().write("不支持的类型");
            return;
        }
        if ("doc".equals(fileType) || "docx".equals(fileType)) {
            File nf = new File(filePath + fileN + ".pdf");
            if (!nf.exists()) {
                word2Pdf.tf(fileName);
            }
            httpResource.setContentType("application/pdf;charset=UTF-8");
            fileName = fileN + ".pdf";

        } else {
            if ("pdf".equals(fileType)) {
                httpResource.setContentType("application/pdf;charset=UTF-8");
            } else {
                httpResource.setContentType("image/jpeg");
            }
        }
        String fullFileName = filePath + fileName;
        FileInputStream fis;
        try {
            fis = new FileInputStream(fullFileName);
        } catch (FileNotFoundException e) {
            throw new RRException("文件不存在");
        }
        OutputStream os = httpResource.getOutputStream();
        try {
            int count;
            byte[] buffer = new byte[1024 * 1024];
            while ((count = fis.read(buffer)) != -1){
                os.write(buffer, 0, count);
            }
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (os != null) {
                os.close();
            }
            if (fis != null) {
                fis.close();
            }
        }
    }

    /**
     * 文件下载
     *
     * @param fileName
     * @param httpResource
     * @throws IOException
     */
    @RequestMapping("/down/{fileName}")
    @ResponseBody
    public void download(@PathVariable("fileName") String fileName, HttpServletResponse httpResource) throws IOException {
        if (null == fileName || "null".equals(fileName)) {
            throw new RRException("文件不存在");
        }
        String fileType = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
        String fullFileName = filePath + fileName;
        // 以流的形式下载文件。
        InputStream fis = null;
        try {
            fis = new BufferedInputStream(new FileInputStream(fullFileName));
        } catch (FileNotFoundException e) {
            throw new RRException("文件不存在");
        }
        byte[] buffer = new byte[fis.available()];
        fis.read(buffer);
        fis.close();
        // 清空response
        httpResource.reset();
        // 设置response的Header
//        httpResource.addHeader("Content-Disposition", "attachment;filename=" + new String(("下载文件" + fileType).getBytes(), "iso-8859-1"));
//        httpResource.addHeader("Content-Length", "" + file.length());
        OutputStream out = new BufferedOutputStream(httpResource.getOutputStream());
        httpResource.setContentType("application/octet-stream");
        out.write(buffer);
        out.flush();
        out.close();
//        return httpResource;
    }

    /**
     * 列表
     */
    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = sysOssService.queryPage(params);

        return R.ok().put("page", page);
    }





    /**
     * 删除
     */
    @PostMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        sysOssService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }

    /**
     * 上传文件
     *
     * @param file
     * @return
     * @throws Exception
     */
    @PostMapping("/uploadFile")
    @Login
    public R uploadFile(@RequestParam("file") MultipartFile file, @ApiIgnore @LoginUser SysUserEntity user) throws Exception {
        if (file.isEmpty()) {
            throw new RRException("文件不能为空");
        }
        String fileName = sysOssService.uploadFile(file,false, user);
        FileInfoModel fm = new FileInfoModel(fileName,file.getOriginalFilename());
        return R.ok().put(Constant.DATA, fm);
    }

    /**
     * 上传多文件
     *
     * @param fileList
     * @return
     * @throws Exception
     */
    @PostMapping("/uploadFiles")
    @Login
    public R uploadFiles(@RequestParam("files") List<MultipartFile> fileList, @ApiIgnore @LoginUser SysUserEntity user) throws Exception {
        if (CollectionUtils.isEmpty(fileList)) {
            throw new RRException("文件不能为空");
        }
        List<FileInfoModel> result = new ArrayList();
        FileInfoModel fm;
        for (MultipartFile file : fileList) {
            String fileName = sysOssService.uploadFile(file, true, user);
            fm = new FileInfoModel(fileName, file.getOriginalFilename());
            result.add(fm);
        }
        return R.ok().put(Constant.DATA, result);
    }
}
