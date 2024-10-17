package org.buy.life.service.excel;



import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.read.metadata.ReadSheet;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.sl.usermodel.PictureData;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFPictureData;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.buy.life.model.dto.ExcelImageProperty;
import org.buy.life.model.dto.ExcelSuffixProperty;
import org.buy.life.model.dto.ImportSkuDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.List;
import java.util.Map;

/**
 * @menu TODO
 * @Author YourJustin
 * @Date 2024/10/17 13:57
 * I am a code man ^_^ !!
 */
@Slf4j
public class SkuDataListener implements ReadListener<Map<Integer, String>> {

    @Override
    public void invoke(Map<Integer, String> data, AnalysisContext context) {
        for (Map.Entry<Integer, String> entry : data.entrySet()) {
            System.out.println("Column: " + entry.getKey() + ", Text: " + entry.getValue());
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        System.out.println("All data analyzed");
    }

    @Override
    public void invokeHead(Map<Integer, ReadCellData<?>> headMap, AnalysisContext context) {
        System.out.println("Reading header");
        // 处理表头数据
    }




}
