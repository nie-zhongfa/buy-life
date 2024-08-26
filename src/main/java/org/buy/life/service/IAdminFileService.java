package org.buy.life.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * @menu TODO
 * @Author YourJustin
 * @Date 2024/8/26 3:10 PM
 * I am a code man ^_^ !!
 */
public interface IAdminFileService {

    String uploadFile(MultipartFile file);

    String uploadFile(String fileName, InputStream inputStream);
}
