package com.compilespace.need.utils;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈Excel表格上传下载工具类〉
 *  采用的是apache.poi
 *      <dependency>
 * 			<groupId>org.apache.poi</groupId>
 * 			<artifactId>poi</artifactId>
 * 			<version>3.11</version>
 * 		</dependency>
 * 		<dependency>
 * 			<groupId>org.apache.poi</groupId>
 * 			<artifactId>poi-ooxml</artifactId>
 * 			<version>3.9</version>
 * 		</dependency>
 * @author Admin
 * @since 1.0.0
 */
public class ExcelUtil {

    /**
     * 从数据库中导出数据到Excel表格(下载)
     *
     *     在调用此方法结束后,要以流的方式输出,注意
     *     // 输出Excel文件
     * 		ServletOutputStream outputStream = response.getOutputStream();
     * 		response.reset();
     * 		//为excel文件命名
     * 		response.setHeader("Content-disposition", "attachment; filename=details.xls");
     * 		response.setContentType("application/msexcel");
     * 		workbook.write(outputStream);
     * 		outputStream.flush();
     * 		outputStream.close();
     * @param heads Excel表头信息
     * @param datas 将各个实体类取值封装到String数组里
     * @return SXSSFWorkbook
     */
    public SXSSFWorkbook downloadExcel(String[] heads, List<String[]> datas) {
        // 创建excel对象
        SXSSFWorkbook workbook = new SXSSFWorkbook();
        // 创建excel表
        Sheet sheet = workbook.createSheet();
        // 创建表头行
        Row headRow = sheet.createRow(0);
        // 给表头设置数据
        for (int i = 0; i < heads.length; i++) {
            // 创建行中的列
            Cell createCell = headRow.createCell(i);
            // 在列中设置值
            createCell.setCellValue(heads[i]);
        }
        // 给表体设置数据
        for (int j = 0; j < datas.size(); j++) {
            // 创建行
            Row bodyRow = sheet.createRow(j + 1);
            // 将集合转成数组
            String[] str = datas.get(j);
            // 给列中设置数据
            for (int i = 0; i < str.length; i++) {
                // 创建列
                Cell createCell = bodyRow.createCell(i);
                // 在列中设置值
                createCell.setCellValue(str[i]);
            }
        }
        return workbook;
    }

    /**
     * 从Excel表格导入数据库(上传)
     * 调用此方法需注意以下几项:
     * 1.Excel表格中不能有空的空格
     * 2.数字和字符请严格区分
     *
     * @param uploadFile 要上传的Excel文件
     * @return List<String                                                               [                                                               ]> 封装好的实体类数据,只要遍历将数组设置进实体类中即可
     */
    public List<String[]> uploadExcel(MultipartFile uploadFile) throws Exception {
        //准备容器
        List<String[]> datas = new ArrayList<>();
        InputStream inputStream = uploadFile.getInputStream();
        //创建一个SXSSFWorkbook
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        //拿到第一张表
        XSSFSheet sht = workbook.getSheetAt(0);
        //拿到总行数
        int lastRowNum = sht.getLastRowNum();
        for (int i = 1; i <= lastRowNum; i++) {
            //拿到某一行
            Row row = sht.getRow(i);
            //拿到这一行有多少列
            short lastCellNum = row.getLastCellNum();
            //装每一行的容器
            String[] rowData = new String[lastCellNum];
            for (int j = 0; j < lastCellNum; j++) {
                //把数据放进去
                rowData[j] = row.getCell(j).getStringCellValue();
            }
            //把这一行放到容器中
            datas.add(rowData);
        }
        inputStream.close();
        return datas;
    }
}

