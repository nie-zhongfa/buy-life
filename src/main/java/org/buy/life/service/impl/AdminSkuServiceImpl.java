package org.buy.life.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.buy.life.entity.BuySkuEntity;
import org.buy.life.mapper.BuySkuMapper;
import org.buy.life.model.dto.ImportSkuDto;
import org.buy.life.model.dto.PriceDto;
import org.buy.life.model.request.AdminSkuRequest;
import org.buy.life.model.response.AdminSkuResponse;
import org.buy.life.service.IAdminFileService;
import org.buy.life.service.IAdminSkuService;
import org.buy.life.utils.excel.ExcelReadImageUtil;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @menu TODO
 * @Author YourJustin
 * @Date 2024/8/24 2:56 PM
 * I am a code man ^_^ !!
 */
@Slf4j
@Service
public class AdminSkuServiceImpl extends ServiceImpl<BuySkuMapper, BuySkuEntity> implements IAdminSkuService {

    @Resource
    private IAdminFileService adminFileService;

    @Override
    public PageInfo<AdminSkuResponse> querySkuPage(AdminSkuRequest adminSkuRequest) {
        Page<BuySkuEntity> adminSkuPage = getSkuPage(adminSkuRequest);
        PageInfo<AdminSkuResponse> pageInfo = BeanUtil.copyProperties(adminSkuPage, PageInfo.class);
        List<AdminSkuResponse> responses = new ArrayList<>();
        if (CollectionUtils.isEmpty(adminSkuPage.getRecords())) {
            adminSkuPage.getRecords().forEach(r -> {
                AdminSkuResponse adminSkuResponse = BeanUtil.copyProperties(r, AdminSkuResponse.class);

                responses.add(adminSkuResponse);
            });
        }
        pageInfo.setList(responses);
        return pageInfo;
    }

    @Override
    public void importSku(MultipartFile file) {
        try {
            InputStream inputStream = file.getInputStream();
            List<ImportSkuDto> doReadSync = EasyExcelFactory.read(file.getInputStream()).head(ImportSkuDto.class).sheet().doReadSync();
            ExcelReadImageUtil.readImage(inputStream, doReadSync);
            List<BuySkuEntity> buySkuEntities = new ArrayList<>();
            for (ImportSkuDto importSkuDto : doReadSync) {
                String fileUrl = uploadSkuImg(importSkuDto);
                PriceDto price = PriceDto.builder().price(importSkuDto.getPrice()).currency("CNY").build();
                BuySkuEntity buySkuEntity = BuySkuEntity.builder()
                        .skuId(importSkuDto.getSkuId())
                        .skuName(importSkuDto.getSkuName())
                        .skuType(importSkuDto.getSkuType())
                        .skuCategory(importSkuDto.getSkuCategory())
                        .price(JSON.toJSONString(price))
                        .costPrice(importSkuDto.getCostPrice())
                        .stock(importSkuDto.getStock())
                        .batchKey(fileUrl)
                        .build();
                List<BuySkuEntity> list = lambdaQuery().eq(BuySkuEntity::getSkuId, importSkuDto.getSkuId()).eq(BuySkuEntity::getIsDeleted, false).list();
                if (!CollectionUtils.isEmpty(list)) {
                    buySkuEntity.setId(list.get(0).getId());
                }
                buySkuEntities.add(buySkuEntity);
            }
            this.saveOrUpdateBatch(buySkuEntities);
        } catch (Exception ex) {
            log.error("importSku fail", ex);
        }
    }

    public Page<BuySkuEntity> getSkuPage(AdminSkuRequest adminSkuRequest) {
        Page<BuySkuEntity> page = new Page<>(adminSkuRequest.getPageNum(), adminSkuRequest.getPageSize());
        return lambdaQuery()
                .eq(BuySkuEntity::getClassification, adminSkuRequest.getClassification())
                .eq(BuySkuEntity::getIsDeleted, false)
                .orderByDesc(BuySkuEntity::getMtime)
                .page(page);
    }

    public String uploadSkuImg(ImportSkuDto importSkuDto) {
        try {
            String fileName = importSkuDto.getSkuName() + importSkuDto.getImgSuffix();
            MultipartFile imgFile = new MockMultipartFile(fileName, fileName, "application/octet-stream", importSkuDto.getFile());
            return adminFileService.uploadFile(imgFile);
        } catch (IOException e) {
            log.error("上传文件失败", e);
        }
        return null;
    }
}
