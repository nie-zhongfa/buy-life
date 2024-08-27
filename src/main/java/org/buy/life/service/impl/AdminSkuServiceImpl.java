package org.buy.life.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.buy.life.entity.BuySkuEntity;
import org.buy.life.entity.resp.SimplePage;
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
    public SimplePage<AdminSkuResponse> querySkuPage(AdminSkuRequest adminSkuRequest) {
        Page<BuySkuEntity> adminSkuPage = getSkuPage(adminSkuRequest);
        if (adminSkuPage == null || CollectionUtils.isEmpty(adminSkuPage.getRecords())) {
            return SimplePage.emptyPage();
        }
        SimplePage<AdminSkuResponse> pageInfo = BeanUtil.copyProperties(adminSkuPage, SimplePage.class);
        List<AdminSkuResponse> responses = new ArrayList<>();
        if (!CollectionUtils.isEmpty(adminSkuPage.getRecords())) {
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
                BuySkuEntity buySkuEntity = BeanUtil.copyProperties(importSkuDto, BuySkuEntity.class);
                buySkuEntity.setPrice(JSON.toJSONString(price));
                buySkuEntity.setBatchKey(fileUrl);
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

    @Override
    public List<BuySkuEntity> getSkuBySkuIdList(List<String> skuIds) {
        return lambdaQuery()
                .in(BuySkuEntity::getSkuId, skuIds)
                .eq(BuySkuEntity::getIsDeleted, false)
                .list();
    }

    @Override
    public BuySkuEntity getSkuBySkuId(String skuId) {
        return lambdaQuery()
                .eq(BuySkuEntity::getSkuId, skuId)
                .eq(BuySkuEntity::getIsDeleted, false)
                .one();
    }

    @Override
    public boolean updateStock(Long id, Long stock) {
        int i = this.baseMapper.updateStock(id, stock);
        return i > 0;
    }
}
