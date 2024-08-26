package org.buy.life.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import org.buy.life.entity.BuySkuEntity;
import org.buy.life.model.request.AdminSkuRequest;
import org.buy.life.model.response.AdminSkuResponse;
import org.springframework.web.multipart.MultipartFile;

/**
 * @menu TODO
 * @Author YourJustin
 * @Date 2024/8/24 2:55 PM
 * I am a code man ^_^ !!
 */
public interface IAdminSkuService extends IService<BuySkuEntity> {

    PageInfo<AdminSkuResponse> querySkuPage(AdminSkuRequest adminSkuRequest);

    void importSku(MultipartFile file);
}
