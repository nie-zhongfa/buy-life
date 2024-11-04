package org.buy.life.service;

import org.buy.life.entity.resp.SimplePage;
import org.buy.life.model.request.AdminSeriesRequest;
import org.buy.life.model.response.AdminSeriesResponse;

/**
 * @menu TODO
 * @Author YourJustin
 * @Date 2024/11/4 20:40
 * I am a code man ^_^ !!
 */
public interface IAdminSeriesService {

    SimplePage<AdminSeriesResponse> querySeriesPage(AdminSeriesRequest adminSeriesRequest);
}
