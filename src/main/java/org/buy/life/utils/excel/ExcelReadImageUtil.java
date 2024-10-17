package org.buy.life.utils.excel;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ooxml.POIXMLDocumentPart;
import org.apache.poi.ss.usermodel.PictureData;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.*;
import org.buy.life.model.dto.ExcelImageProperty;
import org.buy.life.model.dto.ExcelSuffixProperty;
import org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTMarker;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.List;

/**
 * @menu TODO
 * @Author YourJustin
 * @Date 2024/8/26 8:46 PM
 * I am a code man ^_^ !!
 */
@Slf4j
public class ExcelReadImageUtil {

    public static <T> void readImage(InputStream inputStream, List<T> list) {
        try {
            Workbook workbook = WorkbookFactory.create(inputStream);
            // 默认读取第一页
            XSSFSheet sheet = (XSSFSheet) workbook.getSheetAt(0);
            List<POIXMLDocumentPart> documentPartList = sheet.getRelations();
            Integer size = list.size();
            for (POIXMLDocumentPart part : documentPartList) {
                if (part instanceof XSSFDrawing) {
                    XSSFDrawing drawing = (XSSFDrawing) part;
                    List<XSSFShape> shapes = drawing.getShapes();
                    for (XSSFShape shape : shapes) {
                        try {
                            XSSFPicture picture = (XSSFPicture) shape;
                            XSSFClientAnchor anchor = picture.getPreferredSize();
                            CTMarker marker = anchor.getFrom();
                            int row = marker.getRow();
                            // 从第2行开始
                            if (row > 0 && row <= size) {
                                log.info("开始执行第【{}】行数据", row);
                                PictureData pictureData = picture.getPictureData();

                                int pictureType = pictureData.getPictureType();
                                org.apache.poi.sl.usermodel.PictureData.PictureType pictureTypeEum = org.apache.poi.sl.usermodel.PictureData.PictureType.forNativeID(pictureType);

                                byte[] bytes = pictureData.getData();
                                InputStream imageInputStream = new ByteArrayInputStream(bytes);
                                T item = list.get(row - 1);
                                Field[] fields = item.getClass().getDeclaredFields();
                                for (Field field : fields) {
                                    String name = field.getName();
                                    if (name.equals("skuId")) {
                                        field.setAccessible(true);
                                        log.info("skuId>>>>> {}", field.get(item));
                                    }
                                    if (field.isAnnotationPresent(ExcelSuffixProperty.class)) {
                                        field.setAccessible(true);
                                        field.set(item, pictureTypeEum.extension);
                                    }
                                    if (field.isAnnotationPresent(ExcelImageProperty.class)) {
                                        field.setAccessible(true);
                                        field.set(item, imageInputStream);
                                    }
                                }
                            }
                        } catch (Exception ex) {
                            log.error("图片文件读取失败", ex);
                        }

                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            log.error("read image error",e);
        }
    }
}
