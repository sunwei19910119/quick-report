package com.xhtt.common.utils;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by feipc on 2018/4/23.
 */
public class ReadExcel {

    public static final String OFFICE_EXCEL_2003_POSTFIX = "xls";
    public static final String OFFICE_EXCEL_2010_POSTFIX = "xlsx";
    public static final String POINT = ".";
    public static final String PATTEN = "yyyy/MM/dd";

    /**
     * 获得path的后缀名
     *
     * @param path
     * @return
     */
    public static String getPostfix(String path) {
        if (path == null || StringUtils.isEmpty(path.trim())) {
            return "";
        }
        if (path.contains(POINT)) {
            return path.substring(path.lastIndexOf(POINT) + 1, path.length());
        }
        return "";
    }

    /**
     * 单元格格式
     *
     * @param hssfCell
     * @return
     */
    public static String getHValue(HSSFCell hssfCell) {
        if (hssfCell.getCellTypeEnum() == CellType.BOOLEAN) {
            return String.valueOf(hssfCell.getBooleanCellValue());
        } else if (hssfCell.getCellTypeEnum() == CellType.NUMERIC) {
            String cellValue = "";
            if (HSSFDateUtil.isCellDateFormatted(hssfCell)) {
                Date date = HSSFDateUtil.getJavaDate(hssfCell.getNumericCellValue());
                cellValue = DateUtils.format(date, PATTEN);
            } else {
                DecimalFormat df = new DecimalFormat("#.##");
                cellValue = df.format(hssfCell.getNumericCellValue());
                String strArr = cellValue.substring(cellValue.lastIndexOf(POINT) + 1, cellValue.length());
                if (strArr.equals("00")) {
                    cellValue = cellValue.substring(0, cellValue.lastIndexOf(POINT));
                }
            }
            return cellValue;
        } else {
            return String.valueOf(hssfCell.getStringCellValue());
        }
    }

    /**
     * 单元格格式
     *
     * @param xssfCell
     * @return
     */
    public static String getXValue(XSSFCell xssfCell) {
        if (xssfCell.getCellTypeEnum() == CellType.BOOLEAN) {
            return String.valueOf(xssfCell.getBooleanCellValue());
        } else if (xssfCell.getCellTypeEnum() == CellType.NUMERIC) {
            String cellValue = "";
            if (HSSFDateUtil.isCellDateFormatted(xssfCell)) {
                Date date = HSSFDateUtil.getJavaDate(xssfCell.getNumericCellValue());
                cellValue = DateUtils.format(date, PATTEN);
            } else {
                DecimalFormat df = new DecimalFormat("#.##");
                cellValue = df.format(xssfCell.getNumericCellValue());
                String strArr = cellValue.substring(cellValue.lastIndexOf(POINT) + 1, cellValue.length());
                if (strArr.equals("00")) {
                    cellValue = cellValue.substring(0, cellValue.lastIndexOf(POINT));
                }
            }
            return cellValue;
        } else {
            return String.valueOf(xssfCell.getStringCellValue());
        }
    }

    /**
     * read the Excel .xlsx,.xls
     *
     * @param file      jsp中的上传文件
     * @param startLine 开始行数 0是第一行
     * @return
     * @throws IOException
     */
    public static Map<Long, Map<Integer, String>> readExcel(MultipartFile file, int startLine) {
        if (file == null || StringUtils.isEmpty(file.getOriginalFilename().trim())) {
            return null;
        } else {
            String postfix = getPostfix(file.getOriginalFilename());
            if (StringUtils.isNotEmpty(postfix)) {
                if (OFFICE_EXCEL_2003_POSTFIX.equals(postfix)) {
                    return readXls(file, startLine);
                } else if (OFFICE_EXCEL_2010_POSTFIX.equals(postfix)) {
                    return readXlsx(file, startLine);
                } else {
                    return null;
                }
            }
        }
        return null;
    }

    /**
     * read the Excel 2010 .xlsx
     *
     * @param file
     * @return
     * @throws IOException
     */
    @SuppressWarnings("deprecation")
    public static Map<Long, Map<Integer, String>> readXlsx(MultipartFile file, int startLine) {
        Map<Long, Map<Integer, String>> result = new HashMap<>();
        Map<Integer, String> map = null;
        // IO流读取文件
        InputStream input = null;
        XSSFWorkbook wb;
//        ArrayList<String> rowList = null;
        try {
            input = file.getInputStream();
            // 创建文档
            wb = new XSSFWorkbook(input);
            //读取sheet(页)
            for (int numSheet = 0; numSheet < wb.getNumberOfSheets(); numSheet++) {
                XSSFSheet xssfSheet = wb.getSheetAt(numSheet);
                if (xssfSheet == null) {
                    continue;
                }
               if(wb.getSheetName(numSheet).equals("数据字典")){
                   continue;
               }
                int totalRows = xssfSheet.getLastRowNum();

                for (int rowNum = startLine; rowNum <= totalRows; rowNum++) {
                    XSSFRow xssfRow = xssfSheet.getRow(rowNum);
                    if (xssfRow != null ) {
                        map = new HashMap<>();
                        int totalCells = xssfRow.getLastCellNum();
                        int a=0;
                        for (int c = 0; c <= totalCells + 1; c++) {
                            XSSFCell cell = xssfRow.getCell(c);
                            if (cell != null && !getXValue(cell).trim().equals("")) {
                                a=1;
                                break;
                            }else{
                                a=0;
                            }
                        }
                        //读取列，从第一列开始
                        if(a==1){
                            for (int c = 0; c <= totalCells + 1; c++) {
                                XSSFCell cell = xssfRow.getCell(c);
                                if (cell == null) {
                                    map.put(c, "");
                                    continue;
                                }
                                map.put(c, getXValue(cell).trim());
                            }
                            result.put(rowNum + 1L, map);
                        }
                    }
                }
                /*for (int rowNum = startLine; rowNum <= totalRows; rowNum++) {
                    XSSFRow xssfRow = xssfSheet.getRow(rowNum);
                    if (xssfRow != null) {
                        map = new HashMap<>();
                        int totalCells = xssfRow.getLastCellNum();
                        //读取列，从第一列开始
                        for (int c = 0; c <= totalCells + 1; c++) {
                            XSSFCell cell = xssfRow.getCell(c);
                            if (cell == null) {
                                map.put(c, "");
                                continue;
                            }
                            map.put(c, getXValue(cell).trim());
                        }
                        result.put(rowNum + 1l, map);
                    }
                }*/
            }
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;

    }

    /**
     * read the Excel 2003-2007 .xls
     *
     * @param file
     * @return
     * @throws IOException
     */
    public static Map<Long, Map<Integer, String>> readXls(MultipartFile file, int startLine) {
        Map<Long, Map<Integer, String>> result = new HashMap<>();
        Map<Integer, String> map;
        // IO流读取文件
        InputStream input = null;
        HSSFWorkbook wb;
//        ArrayList<String> rowList;
        try {
            input = file.getInputStream();
            // 创建文档
            wb = new HSSFWorkbook(input);
            //读取sheet(页)
            for (int numSheet = 0; numSheet < wb.getNumberOfSheets(); numSheet++) {
                HSSFSheet hssfSheet = wb.getSheetAt(numSheet);
                if (hssfSheet == null) {
                    continue;
                }
                if(wb.getSheetName(numSheet).equals("数据字典")){
                    continue;
                }
                int totalRows = hssfSheet.getLastRowNum();
                //读取Row,从第二行开始
                for (int rowNum = startLine; rowNum <= totalRows; rowNum++) {
                    HSSFRow hssfRow = hssfSheet.getRow(rowNum);
                    if (hssfRow != null ) {
                        map = new HashMap<>();
                        int totalCells = hssfRow.getLastCellNum();
                        int a=0;
                        for (int c = 0; c <= totalCells + 1; c++) {
                            HSSFCell cell = hssfRow.getCell(c);
                            if (cell != null && !getHValue(cell).trim().equals("")) {
                                a=1;
                                break;
                            }else{
                                a=0;
                            }
                        }
                        //读取列，从第一列开始
                        if(a==1){
                            for (int c = 0; c <= totalCells + 1; c++) {
                                HSSFCell cell = hssfRow.getCell(c);
                                if (cell == null) {
                                    map.put(c, "");
                                    continue;
                                }
                                map.put(c, getHValue(cell).trim());
                            }
                            result.put(rowNum + 1L, map);
                        }
                    }
                }
                /*for (int rowNum = startLine; rowNum <= totalRows; rowNum++) {
                    HSSFRow hssfRow = hssfSheet.getRow(rowNum);
                    if (hssfRow != null) {
                        map = new HashMap<>();
                        int totalCells = hssfRow.getLastCellNum();
                        //读取列，从第一列开始
                        for (int c = 0; c <= totalCells + 1; c++) {
                            HSSFCell cell = hssfRow.getCell(c);
                            if (cell == null) {
                                map.put(c, "");
                                continue;
                            }
                            map.put(c, getHValue(cell).trim());
                        }
                        result.put(rowNum + 1l, map);
                    }
                }*/
            }
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
