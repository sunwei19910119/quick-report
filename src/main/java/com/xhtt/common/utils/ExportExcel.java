package com.xhtt.common.utils;

import com.xhtt.common.exception.RRException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 导出Excel
 */
@Component
public class ExportExcel<T> {

    /**
     * 导出模板
     *
     * @param sheetName sheets 名称
     * @param fileName  导出的文件名
     * @param headers   列头名
     * @param columns   字段名
     * @param result    数据集合
     * @param response  HttpServletResponse
     * @throws Exception
     */
    public void exportExcel(String fileName, String sheetName,
                            String[] headers, String[] columns, Collection<T> result,
                            HttpServletResponse response) throws Exception {
        //声明工作薄
        XSSFWorkbook workbook = new XSSFWorkbook();
        //创建sheet
        XSSFSheet sheet = workbook.createSheet(sheetName);
        //创建第0行，填充列头
        int index = 0;
        this.iterationHeaders(sheet, index, headers);
        //创建第1行，填充数据
        this.iterationData(sheet, index, columns, result);

        OutputStream out = response.getOutputStream();// 取得输出流
        response.reset();// 清空输出流
        response.setHeader("Content-disposition", "attachment; filename=" + URLEncoder.encode(fileName, "utf-8") + ".xlsx");// 设定输出文件头
        //response.setContentType("application/msexcel");// 定义输出类型
        workbook.write(out);
        out.flush();
        out.close();
    }


    public void exportSheetExcel(XSSFWorkbook workbook,String fileName, String placeName,String sheetName,
                            String[] headers, String[] columns, Collection<T> result,
                            HttpServletResponse response) throws Exception {
        //创建sheet
        XSSFSheet sheet = workbook.createSheet(sheetName);
        //创建第0行，填充列头
        sheet.createRow(0).createCell(0);
        sheet.createRow(1).createCell(0);
        CellRangeAddress callRangeAddress = new CellRangeAddress(0,0,0,6);//起始行,结束行,起始列,结束列
        CellRangeAddress callRangeAddress1 = new CellRangeAddress(1,1,0,6);//起始行,结束行,起始列,结束列
        sheet.addMergedRegion(callRangeAddress);
        sheet.addMergedRegion(callRangeAddress1);
        sheet.getRow(0).setHeightInPoints(60);

        XSSFCellStyle cellStyle=workbook.createCellStyle();
        cellStyle.setWrapText(true);//先设置为自动换行
        sheet.getRow(0).getCell(0).setCellStyle(cellStyle);
        sheet.getRow(0).getCell(0).setCellValue("风险点导入模板，请根据规则进行填写。\n" +
                "每一个sheet页对应一个生产经营场所地址，请根据实际情况填写所有sheet页。\n" +
                "“数据字典”页无需填写，请不要修改！请不要删除或添加sheet页！\n" +
                "导入成功确认数据无误后，请在网页上点击保存保存数据！");
        sheet.getRow(1).getCell(0).setCellValue("生产经营场所："+placeName);
        int index = 2;
        this.iterationHeaders(sheet, index, headers);
        //创建第1行，填充数据
        this.iterationData(sheet, index, columns, result);
    }

    public void exportSheetExcelhome(XSSFWorkbook workbook,String fileName,String sheetName,
                                 String[] headers, String[] columns, Collection<T> result,
                                 HttpServletResponse response) throws Exception {
        //创建sheet
        XSSFSheet sheet = workbook.createSheet(sheetName);
        //创建第0行，填充列头
        sheet.createRow(0).createCell(0);
        CellRangeAddress callRangeAddress = new CellRangeAddress(0,0,0,16);//起始行,结束行,起始列,结束列
        sheet.addMergedRegion(callRangeAddress);
        sheet.getRow(0).setHeightInPoints(30);

        XSSFCellStyle cellStyle=workbook.createCellStyle();
        cellStyle.setWrapText(true);//先设置为自动换行

        sheet.getRow(0).getCell(0).setCellStyle(cellStyle);
        sheet.getRow(0).getCell(0).setCellValue("企业报告及风险情况");
        int index = 1;
        this.iterationHeaders(sheet, index, headers);
        //创建第1行，填充数据
        this.iterationData(sheet, index, columns, result);
    }

    public void exportSheetExcelhome1(XSSFWorkbook workbook,String fileName,String sheetName,String[] columns,Collection<T> result,
                                     HttpServletResponse response) throws Exception {
        //创建sheet
        XSSFSheet sheet = workbook.getSheet(sheetName);
        int index = 2;
        //创建第1行，填充数据
        this.iterationData(sheet, index, columns, result);
    }
    /**
     * 遍历列头
     *
     * @param sheet
     * @param index
     * @param headers
     */
    private void iterationHeaders(XSSFSheet sheet, int index, String[] headers) {
        XSSFRow row = sheet.createRow(index);
        for (int i = 0; i < headers.length; i++) {
            sheet.setColumnWidth(i, 20 * 256);
            XSSFCell cell = row.createCell(i);
            XSSFRichTextString text = new XSSFRichTextString(headers[i]);
            cell.setCellValue(text);
        }
    }

    /**
     * 遍历数据
     *
     * @param sheet
     * @param index
     * @param columns
     * @param result
     * @throws Exception
     */
    private void iterationData(XSSFSheet sheet, int index, String[] columns, Collection<T> result) throws Exception {
        // 遍历集合数据，产生数据行
        if (result != null) {
            index++;
            XSSFRow row = null;
            for (T t : result) {
                row = sheet.createRow(index);
                index++;
                for (int i = 0; i < columns.length; i++) {
                    XSSFCell cell = row.createCell(i);
                    String fieldName = columns[i];
                    String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                    Object value;
                    if (t instanceof HashMap) {
                        HashMap<String, Object> m = (HashMap<String, Object>) t;
                        value = m.get(fieldName);
                    } else {
                        Class tCls = t.getClass();
                        Method getMethod = tCls.getMethod(getMethodName, new Class[]{});
                        // getMethod.getReturnType().isInstance(obj);
                        value = getMethod.invoke(t, new Object[]{});
                    }


                    this.conversionType(value, cell);
                }
            }
        }
    }
    Pattern p = Pattern.compile("^//d+(//.//d+)?$");
    /**
     * 数值转换
     *
     * @param value
     * @param cell
     */
    private void conversionType(Object value, XSSFCell cell) {
        String textValue = null;
        if (value == null) {
            textValue = "";
        } else if (value instanceof Date) {
            textValue = DateUtils.format((Date) value, DateUtils.DATE_TIME_PATTERN);
        } else if (value instanceof LocalDateTime) {
            textValue = ((LocalDateTime) value).format(DateUtils.dtfm);
        } else {
            //其它数据类型都当作字符串简单处理
            textValue = value.toString();
        }
        if (StringUtils.isNotEmpty(textValue)) {

            Matcher matcher = p.matcher(textValue);
            if (matcher.matches()) {
                //是数字当作double处理
                cell.setCellValue(Double.parseDouble(textValue));
            } else {
                XSSFRichTextString richString = new XSSFRichTextString(textValue);
                cell.setCellValue(richString);
            }
        }
    }

    public XSSFWorkbook excelExp(Integer sheetNum, Map<String, String> map, String templatePath) {
        try {
            if (null == sheetNum) {
                sheetNum = 0;
            }
            // 查询满足条件的业务建档信息
            ClassPathResource resource = new ClassPathResource("templates/" + templatePath);
            InputStream inputStream = resource.getInputStream();   //这个是我的excel模板
            return replaceModelNew(inputStream, sheetNum, map);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static XSSFWorkbook replaceModelNew(InputStream inputStream, int sheetNum, Map<String, String> map) {
        try {
//            POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(sourceFilePath));
            XSSFWorkbook wb = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = wb.getSheetAt(sheetNum);//获取第一个sheet里的内容
            //循环map中的键值对，替换excel中对应的键的值（注意，excel模板中的要替换的值必须跟map中的key值对应，不然替换不成功）
            int rowNum = sheet.getLastRowNum();//该sheet页里最多有几行内容
            for (int i = 0; i < rowNum; i++) {//循环每一行
                XSSFRow row = sheet.getRow(i);
                int colNum = row.getLastCellNum();//该行存在几列
                for (int j = 0; j < colNum; j++) {//循环每一列
                    XSSFCell cell = row.getCell(j);
                    String str = cell.getStringCellValue();//获取单元格内容  （行列定位）
                    if (map.containsKey(str)) {
                        //写入单元格内容
                        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                        cell.setCellValue(map.get(str)); //替换单元格内容
                    }
                }
            }
            return wb;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * freemarker导出excel
     *
     * @param response
     * @param map
     * @param ftlFile
     * @param fileName
     */
    public static void exportExcel(HttpServletResponse response, Map map, String ftlFile, String fileName) {
        try {
            Template freemarkerTemplate = WordUtils.getConfiguration().getTemplate(ftlFile);
            try (BufferedOutputStream bs = new BufferedOutputStream(response.getOutputStream()); Writer w = new OutputStreamWriter(bs, "utf-8")) {
                response.setCharacterEncoding("utf-8");
                response.setContentType("application/octet-stream");
                response.setHeader("Content-Disposition", "attachment;filename=".concat(String.valueOf(URLEncoder.encode(fileName, "UTF-8"))));
                freemarkerTemplate.process(map, w);
                bs.flush();
            } catch (TemplateException e) {
                throw new RRException("模版错误");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new RRException("文件格式不对");
        } catch (IOException e) {
            e.printStackTrace();
            throw new RRException("文件未找到");
        }

    }
}
