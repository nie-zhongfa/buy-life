package org.buy.life.service.excel;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.usermodel.*;
import org.buy.life.model.dto.ImportSkuDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @menu TODO
 * @Author YourJustin
 * @Date 2024/10/17 14:40
 * I am a code man ^_^ !!
 */
@Slf4j
public class ReadSkuImageUtil {

    public static void readExcelWithImages(MultipartFile file , List<ImportSkuDto> list) throws IOException, OpenXML4JException {
        // 读取图片
        try (InputStream inputStream = file.getInputStream();
             OPCPackage opcPackage = OPCPackage.open(inputStream);
             XSSFWorkbook workbook = new XSSFWorkbook(opcPackage)) {

            List<XSSFPictureData> pictures = workbook.getAllPictures();
            for (int i = 0; i < pictures.size(); i++) {
                ImportSkuDto importSkuDto = list.get(i);
                XSSFPictureData picture = pictures.get(i);
                String extension = picture.suggestFileExtension();
                byte[] data = picture.getData();
                InputStream imageInputStream = new ByteArrayInputStream(data);
                importSkuDto.setImgSuffix("."+extension);
                importSkuDto.setFile(imageInputStream);
                log.info("第"+(i+1)+"张" + "Image: " + extension + ", size: " + data.length + " bytes");
            }
        }
    }


    public static void readExcelWithImage(MultipartFile file , List<ImportSkuDto> list) throws IOException, OpenXML4JException {

        try (InputStream inputStream = file.getInputStream();
             OPCPackage opcPackage = OPCPackage.open(inputStream);
             XSSFWorkbook workbook = new XSSFWorkbook(opcPackage)) {

            XSSFSheet sheet = workbook.getSheetAt(0); // 获取第一个工作表

            XSSFDrawing drawing = sheet.getDrawingPatriarch();
            if (drawing != null) {
                List<XSSFShape> shapes = drawing.getShapes();
                for (XSSFShape shape : shapes) {
                    if (shape instanceof XSSFPicture) {
                        XSSFPicture picture = (XSSFPicture) shape;
                        XSSFClientAnchor anchor = picture.getClientAnchor();

                        int row = anchor.getRow1();
                        int col = anchor.getCol1();


                        log.info("开始执行第【{}】行数据", row);

                        System.out.println("Picture found at row: " + (row + 1) + ", column: " + (col + 1));

                        // 获取图片数据
                        XSSFPictureData pictureData = picture.getPictureData();

                        System.out.println("Picture type: " + pictureData.getPictureType());
                        System.out.println("Picture size: " + pictureData.getData().length + " bytes");

                        // 如果需要，你可以在这里保存图片
                        // savePicture(pictureData, "picture_" + row + "_" + col + "." + pictureData.suggestFileExtension());
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
