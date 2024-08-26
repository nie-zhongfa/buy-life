package org.buy.life.controller;

import org.buy.life.service.IAdminFileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * @menu 文件
 * @Author YourJustin
 * @Date 2024/8/26 2:54 PM
 * I am a code man ^_^ !!
 */
@RestController
@RequestMapping("/admin/file")
public class AdminFileController {

   @Resource
   private IAdminFileService adminFileService;

    /**
     * 上传文件
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public ResponseEntity<String> login(@RequestBody MultipartFile file) {
        String fileUrl = adminFileService.uploadFile(file);
        return ResponseEntity.ok(fileUrl);
    }
}
