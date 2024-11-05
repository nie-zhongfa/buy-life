package org.buy.life.service;

import org.buy.life.entity.resp.SimplePage;
import org.buy.life.model.request.AdminSeriesRequest;
import org.buy.life.model.response.AdminSeriesResponse;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * @menu TODO
 * @Author YourJustin
 * @Date 2024/11/4 20:40
 * I am a code man ^_^ !!
 */
public interface IAdminSeriesService {

    SimplePage<AdminSeriesResponse> querySeriesPage(AdminSeriesRequest adminSeriesRequest);

    void importSeriesInfo(MultipartFile file);

    void downloadSeriesTemplate(HttpServletResponse response);
}
