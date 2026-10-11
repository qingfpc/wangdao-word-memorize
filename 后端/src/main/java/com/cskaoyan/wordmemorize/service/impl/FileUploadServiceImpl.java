package com.cskaoyan.wordmemorize.service.impl;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.cskaoyan.wordmemorize.service.FileUploadService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.ClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service

public class FileUploadServiceImpl implements FileUploadService {



    private static final Logger log = LoggerFactory.getLogger(FileUploadServiceImpl.class);

    private static final String ENDPOINT = "oss-cn-wuhan-lr.aliyuncs.com";
    // 此处原为个人阿里云 OSS AccessKey，已移除。使用前请替换 REDACTED。
    private static final String ACCESS_KEY_ID = "REDACTED";
    private static final String ACCESS_KEY_SECRET = "REDACTED";
    private static final String BUCKET_NAME = "wangdao-ysyword";

    @Override
    public String upload(MultipartFile file) throws IOException {
        // 构造桶中的对象名称
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String objectName = UUID.randomUUID() + extension;

        // 创建 OSSClient 实例
        OSS ossClient = new OSSClientBuilder().build(ENDPOINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET);

        StringBuilder stringBuilder = null;
        try {
            // 向桶中放入指定名称的对象
            ossClient.putObject(BUCKET_NAME, objectName, file.getInputStream());

            // 文件访问路径规则 https://BucketName.Endpoint/ObjectName
            stringBuilder = new StringBuilder("https://");
            stringBuilder.append(BUCKET_NAME)
                    .append(".")
                    .append(ENDPOINT)
                    .append("/")
                    .append(objectName);


        } catch (OSSException oe) {
            log.error("阿里OSS异常，Error Message:{}", oe.getErrorMessage());
        } catch (ClientException ce) {
            log.error("阿里OSS Client异常，Error Message:{}", ce.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
        return stringBuilder.toString();
    }

    @Override
    public void deleteFile(String objectName) {



        // 创建 OSSClient 实例
        OSS ossClient = new OSSClientBuilder().build(ENDPOINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET);
        try {
            // 删除桶中指定对象
            ossClient.deleteObject(BUCKET_NAME, objectName);
        } catch (OSSException oe) {
            log.error("阿里OSS异常，Error Message:{}", oe.getErrorMessage());
        } catch (ClientException ce) {
            log.error("阿里OSS Client异常，Error Message:{}", ce.getMessage());
        } finally {
            if (!ObjectUtils.isEmpty(ossClient)) {
                ossClient.shutdown();
            }
        }


    }

}


