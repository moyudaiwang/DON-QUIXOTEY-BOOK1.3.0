package com.element.hydrogen.utils.dev;

import com.element.hydrogen.entity.common.ResponseJson;
import com.element.hydrogen.utils.common.excel.ExcelUtil;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @ClassName CreateTableMysqlUtil
 * @Description 建表工具：导入Excel生成mysql建表文件
 * 1、生成备份原有表数据的SQL：DROP TABLE DON_USER_INFO_BAK;
 * CREATE TABLE DON_USER_INFO_BAK AS SELECT * FROM DON_USER_INFO;
 * 2:生成delete表结构语句；
 * 3：生成新的建表语句；
 * @Author yanyu
 * @Date 2021/7/10 17:02
 * @Version 1.0
 */
public class CreateTableMysqlUtil {

    private  static final  int LOOP_NUM =2;  //循环几个工作表（sheet）
    private  static final  String SQL_PATH ="C:\\Users\\yanyu\\Desktop\\唐吉坷德文档库\\4_Tool\\CreateTable\\resultPage.sql";  //要生成的建表SQL的路径
    private final static String XLS = "xls";
    private final static String XLSX = "xlsx";
    /**
     * 上传Excel，生成创建表结构SQL
     * @param excelFiles
     * @param module
     * @return
     */
    public static ResponseJson createTableM(MultipartFile[] excelFiles, String module) throws IOException {
        ResponseJson responseJson = new ResponseJson();
        PrintWriter writer = new PrintWriter(SQL_PATH) ;
        if (excelFiles != null && excelFiles.length > 0){
            for (int i = 0; i <excelFiles.length; i++) {
                MultipartFile excelFile = excelFiles[i];
                checkFile(excelFile);
                Workbook wb = ExcelUtil.chooseWorkbook(excelFile.getOriginalFilename(), excelFile.getInputStream());

                //循环生成备份表SQL--OK
                writer.write("-- bak Table DDL:\r\n");
                for (int j = 0; j < LOOP_NUM; j++) {
                    Sheet sheet = wb.getSheetAt(j);
                    String bakStr = bakTale(sheet);
                    writer.write(bakStr);
                }
                //循环生成表DDL
                writer.write("\r\n");
                writer.write("--create Table DDL:\r\n");
                for (int j = 0; j < LOOP_NUM; j++) {
                    Sheet sheet = wb.getSheetAt(j);
                    String createStr = createstatement(sheet);
                    writer.write(createStr);
                }
            }
        }
        writer.close();
        responseJson.setCode("200");
        responseJson.setMsg("导出成功:"+SQL_PATH);
        return responseJson;
    }

    //生成备份原有表数据ddl
    private static String bakTale(Sheet sheet) {
        StringBuffer bakStr = new StringBuffer();
        bakStr.append("DROP TABLE "+sheet.getRow(0).getCell(0).getStringCellValue()+ "_BAK;\r\n");
        bakStr.append("CREATE TABLE "+sheet.getRow(0).getCell(0).getStringCellValue()+ "_BAK AS SELECT * FROM "+sheet.getRow(0).getCell(0).getStringCellValue() + ";\r\n");
        return bakStr.toString();
    }
    //主体建表ddl
    private static String createstatement(Sheet sheet) {
        StringBuffer bodysb = new StringBuffer(); // 建表语句的表头和结尾注释不分
        StringBuffer fldsb = new StringBuffer(); // 字段部分
        StringBuffer commentsb = new StringBuffer(); // 表中文名
        StringBuffer partitionsb = new StringBuffer(); // 分区字段部分
        bodysb.append("-- "+sheet.getRow(0).getCell(0).getStringCellValue()+" DDL：\r\n");
        //bodysb.append("-- SELECT * FROM "+sheet.getCell(0, 0).getContents()+ ";\r\n");
        bodysb.append("DROP TABLE "+sheet.getRow(0).getCell(0).getStringCellValue()+ ";\r\n");
        bodysb.append("CREATE TABLE "+sheet.getRow(0).getCell(0).getStringCellValue());
        // 表头部分
        bodysb.append(" (\r\n");
        // 表中文名
        commentsb.append("ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT ='" + sheet.getRow(0).getCell(1).getStringCellValue() + "';\r\n\n");
        // 字段部分
        for (int i = 2; i < sheet.getPhysicalNumberOfRows(); i++) {
            // 首行处理
            if (i == 2) {
                fldsb.append(" " + sheet.getRow(2).getCell(0).getStringCellValue() + " " + sheet.getRow(2).getCell(1).getStringCellValue());
                String isNullVal = sheet.getRow(2).getCell(2).getStringCellValue();
                String defaultVal = sheet.getRow(2).getCell(3).getStringCellValue();
                if ("N".equals(isNullVal.toUpperCase())) { //不允许为null
                    fldsb.append(" NOT NULL");
                    if (!"".equals(defaultVal)) { //存在默认
                        fldsb.append(" DEFAULT '" + defaultVal + "'");
                    }
                } else {
                    fldsb.append(" DEFAULT NULL");
                }
                if (!"".equals(sheet.getRow(2).getCell(4).getStringCellValue())) {
                    fldsb.append(" COMMENT '" + sheet.getRow(2).getCell(4).getStringCellValue() + "',\r\n");
                }else{
                    fldsb.append(",\r\n");
                }

            } else if (i > 2 && i < sheet.getPhysicalNumberOfRows() - 1) {
                for (int j = 0; j <= 4; j++) {
                    switch (j) {
                        case 0:
                            fldsb.append(" " + sheet.getRow(i).getCell(j).getStringCellValue());
                            break;
                        case 1:
                            fldsb.append(" " + sheet.getRow(i).getCell(j).getStringCellValue());
                            break;
                        case 2:
                            String isNullVal = sheet.getRow(i).getCell(j).getStringCellValue();
                            int addj = j + 1;//将默认值一并处理
                            String defaultVal = sheet.getRow(i).getCell(addj).getStringCellValue();
                            if ("N".equals(isNullVal.toUpperCase())) { //不允许为null
                                fldsb.append(" NOT NULL");
                                if (!"".equals(defaultVal)) { //存在默认
                                    fldsb.append(" DEFAULT '" + defaultVal + "'");
                                }
                            } else {
                                fldsb.append(" DEFAULT NULL");
                            }
                            break;
                        case 3:
                            break;
                        case 4:
                            if (!"".equals(sheet.getRow(i).getCell(j).getStringCellValue())) {
                                fldsb.append(" COMMENT '" + sheet.getRow(i).getCell(j).getStringCellValue() + "',\r\n");
                            }else{
                                fldsb.append(",\r\n");
                            }
                            break;
                        default:
                            break;
                    }
                }
            } else {
                fldsb.append(" " + sheet.getRow(sheet.getPhysicalNumberOfRows() - 1).getCell(0).getStringCellValue() + " " + sheet.getRow(sheet.getPhysicalNumberOfRows() - 1).getCell(1).getStringCellValue());
                String isNullVal = sheet.getRow(sheet.getPhysicalNumberOfRows() - 1).getCell(2).getStringCellValue();
                String defaultVal = sheet.getRow(sheet.getPhysicalNumberOfRows() - 1).getCell(3).getStringCellValue();
                if ("N".equals(isNullVal.toUpperCase())) { //不允许为null
                    fldsb.append(" NOT NULL");
                    if (!"".equals(defaultVal)) { //存在默认
                        fldsb.append(" DEFAULT '" + defaultVal + "'");
                    }
                } else {
                    fldsb.append(" DEFAULT NULL");
                }
                fldsb.append(" COMMENT '" + sheet.getRow(sheet.getPhysicalNumberOfRows() - 1).getCell(4).getStringCellValue() + "',\r\n");
                fldsb.append(" PRIMARY KEY (" + sheet.getRow(sheet.getPhysicalNumberOfRows() - 1).getCell(0).getStringCellValue() + ")\r\n)");
            }
        }

        // 合并结果集
        StringBuffer result = new StringBuffer();
        result = bodysb.append(fldsb).append(commentsb).append(partitionsb);
        return result.toString();
    }


    public static void checkFile(MultipartFile file) throws IOException {
        // 判断文件是否存在
        if (null == file) {
            System.out.println("文件不存在！");
            throw new FileNotFoundException("文件不存在！");
        }
        // 获得文件名
        String fileName = file.getOriginalFilename();
        // 判断文件是否是excel文件
        if (!fileName.endsWith(XLS) && !fileName.endsWith(XLSX)) {
            System.out.println(fileName + "不是excel文件");
            throw new IOException(fileName + "不是excel文件");
        }
    }


}
