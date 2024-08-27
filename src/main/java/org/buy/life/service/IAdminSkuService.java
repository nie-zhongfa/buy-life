package org.buy.life.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.buy.life.entity.BuySkuEntity;
import org.buy.life.entity.resp.SimplePage;
import org.buy.life.model.request.AdminSkuRequest;
import org.buy.life.model.response.AdminSkuResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @menu TODO
 * @Author YourJustin
 * @Date 2024/8/24 2:55 PM
 * I am a code man ^_^ !!
 */
public interface IAdminSkuService extends IService<BuySkuEntity> {

    SimplePage<AdminSkuResponse> querySkuPage(AdminSkuRequest adminSkuRequest);

    void importSku(MultipartFile file);

    List<BuySkuEntity> getSkuBySkuIdList(List<String> skuIds);

    BuySkuEntity getSkuBySkuId(String skuId);

    boolean updateStock(Long id, Long stock);
}
