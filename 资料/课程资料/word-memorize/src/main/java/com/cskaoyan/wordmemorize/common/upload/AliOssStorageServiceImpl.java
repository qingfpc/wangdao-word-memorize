package com.cskaoyan.wordmemorize.common.upload;

import com.aliyun.oss.*;
import com.aliyun.oss.common.auth.CredentialsProviderFactory;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import com.aliyun.oss.common.comm.SignVersion;
import com.cskaoyan.wordmemorize.service.FileUploadService;
import io.minio.errors.*;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@Slf4j
@Service
@ConditionalOnBean(AliOssProperties.class)
public class AliOssStorageServiceImpl implements FileUploadService {
    @Resource
    private AliOssProperties aliOssProperties;


    /**
     * 文件上传
     */
    @Override
    public String upload(MultipartFile file) throws IOException {
        return "";
    }

    @Override
    public void deleteFile(String objectName)  {

    }
}
