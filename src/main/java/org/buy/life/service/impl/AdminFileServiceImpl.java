package org.buy.life.service.impl;

import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import lombok.extern.slf4j.Slf4j;
import org.buy.life.exception.BusinessException;
import org.buy.life.service.IAdminFileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @menu TODO
 * @Author YourJustin
 * @Date 2024/8/26 3:10 PM
 * I am a code man ^_^ !!
 */
@Slf4j
@Service
public class AdminFileServiceImpl implements IAdminFileService {

    @Resource
    private FastFileStorageClient storageClient;

    @Value("${fdfs.tracker-list}")
    private String trackerList;

    /**
     * 上传文件
     *
     * @param file 文件对象
     * @return 文件访问地址
     * @throws IOException
     */
    @Override
    public String uploadFile(MultipartFile file) {
        try {
            StorePath storePath = storageClient.uploadFile(
                    file.getInputStream(),
                    file.getSize(),
                    getFileExtension(file.getOriginalFilename()),
                    null);
            return getResAccessUrl(storePath);
        } catch (Exception e) {
            log.error("上传文件失败", e);
        }
        throw new BusinessException(9999, "上传文件失败");
    }

    /**
     * 删除文件
     *
     * @param fileUrl 文件访问地址，包括组名和文件路径信息
     * @throws IOException
     */
    public void deleteFile(String fileUrl) throws IOException {
        StorePath storePath = StorePath.parseFromUrl(fileUrl);
        storageClient.deleteFile(storePath.getGroup(), storePath.getPath());
    }

    /**
     * 下载文件
     *
     * @param fileUrl 文件访问地址，包括组名和文件路径信息
     * @return 文件字节流
     * @throws IOException
     */
    public byte[] downloadFile(String fileUrl) throws IOException {
        StorePath storePath = StorePath.parseFromUrl(fileUrl);
        return storageClient.downloadFile(storePath.getGroup(), storePath.getPath(),
                inputStream -> {
                    try {
                        return StreamUtils.copyToByteArray(inputStream);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    /**
     * 获取文件后缀名
     *
     * @param filename 文件名
     * @return 后缀名
     */
    private String getFileExtension(String filename) {
        if (filename.contains(".")) {
            return filename.substring(filename.lastIndexOf(".") + 1);
        }
        return null;
    }

    /**
     * 获取文件访问地址
     *
     * @param storePath 存储路径对象
     * @return 文件访问地址
     */
    private String getResAccessUrl(StorePath storePath) {
        return trackerList + storePath.getFullPath();
    }
}
