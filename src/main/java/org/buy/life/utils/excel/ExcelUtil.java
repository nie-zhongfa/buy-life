package org.buy.life.utils.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.write.metadata.WriteSheet;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @menu TODO
 * @Author YourJustin
 * @Date 2024/8/26 4:50 PM
 * I am a code man ^_^ !!
 */
@Slf4j
public class ExcelUtil {

    /**
     * 手写校验表头
     * @param is excel文件流
     * @param clazz 模板类
     */
    public static void checkExcelHeaders(InputStream is, Class clazz) {
        List<String> inputList = new ArrayList<>();
        List<String> requiredList = new ArrayList<>();
        Field[] allFields = clazz.getDeclaredFields();
        for (int i = 0; i < allFields.length; ++i) {
            Field field = allFields[i];
            ExcelProperty attr = field.getAnnotation(ExcelProperty.class);
            if (null != attr) {
                //excel里面加入多余的字段
                requiredList.add(attr.value()[0]);
            }
        }
        try {
            Workbook wb = WorkbookFactory.create(is);
            Sheet sheet = wb.getSheetAt(0);
            Row row = sheet.getRow(0);
            for (Iterator<Cell> iter = row.cellIterator(); iter.hasNext(); ) {
                Cell cell = iter.next();
                inputList.add(cell.getStringCellValue());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (requiredList.stream().anyMatch(e -> !inputList.contains(e))
                || inputList.stream().anyMatch(e -> !requiredList.contains(e))) {
            throw new RuntimeException("Excel表头不一致");
        }
    }

    /**
     * 设置返回的头部信息
     * @param response 返回流信息
     * @param fileName 文件名称
     */
    public static void setResponse(HttpServletResponse response, String fileName){
        //设置头
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String excelFileName = null;
        try {
            excelFileName = URLEncoder.encode(fileName, String.valueOf(StandardCharsets.UTF_8)).replaceAll("\\+", "%20");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
        /**
         * attachment这个代表要下载的，如果去掉就直接打开了(attachment-作为附件下载,inline-在线打开)
         * excelFileName是文件名，另存为或者下载时，为默认的文件名
         */
        response.setHeader("Content-disposition", "attachment;filename=" + excelFileName + ".xlsx");
        response.setHeader("Content-Type", "application/octet-stream;charset=utf-8");
    }

    public static void writeExcel(HttpServletResponse response, String fileName, Class<?> tClass, List<?> objects) {
        ExcelWriter excelWriter = null;
        try {
            response.setContentType("application/octet-stream");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName +".xlsx");
            excelWriter = EasyExcel.write(response.getOutputStream(), tClass).build();
            WriteSheet writeSheet = EasyExcel.writerSheet(fileName).build();
            excelWriter.write(objects, writeSheet);
        } catch (IOException e) {
            log.error("导出文件失败", e);
        }finally {
            // 千万别忘记finish 会帮忙关闭流
            if (excelWriter != null) {
                excelWriter.finish();
            }
        }

    }
}
