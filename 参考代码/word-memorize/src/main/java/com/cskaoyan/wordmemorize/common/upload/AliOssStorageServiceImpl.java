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



    public String upload(File file) throws IOException {
        String originalFilename = file.getName();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String objectName = UUID.randomUUID() + extension;

        // 创建 OSSClient 实例
        ClientBuilderConfiguration clientBuilderConfiguration = new ClientBuilderConfiguration();
        // 显式声明使用 V4 签名算法

        DefaultCredentialProvider credentialProvider = CredentialsProviderFactory.newDefaultCredentialProvider(aliOssProperties.getAccessKeyId()
                , aliOssProperties.getAccessKeySecret());

        String region = "beijing";

        clientBuilderConfiguration.setSignatureVersion(SignVersion.V4);
        OSS ossClient = OSSClientBuilder.create()
                .endpoint(aliOssProperties.getEndpoint())
                .credentialsProvider(credentialProvider)
                .region(region)
                .build();
        // 创建OSSClient实例。
        StringBuilder stringBuilder = new StringBuilder("https://");

        try {
            // 创建PutObject请求。
            FileInputStream fileInputStream = new FileInputStream(file);
            ossClient.putObject(aliOssProperties.getBucketName(), objectName, fileInputStream);

            //文件访问路径规则 https://BucketName.Endpoint/ObjectName
            stringBuilder.append(aliOssProperties.getBucketName())
                    .append(".")
                    .append(aliOssProperties.getEndpoint())
                    .append("/")
                    .append(objectName);
        } catch (OSSException oe) {
            log.error("阿里OSS异常，Error Message:{}", oe.getErrorMessage());
        } catch (ClientException ce) {
            log.error("阿里OSS Client异常，Error Message:{}", ce.getMessage());
        } finally {
            if (!ObjectUtils.isEmpty(ossClient)) {
                ossClient.shutdown();
            }
        }

        return stringBuilder.toString();
    }


    /**
     * 文件上传
     */
    @Override
    public String upload(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String objectName = UUID.randomUUID() + extension;

        // 创建 OSSClient 实例
        ClientBuilderConfiguration clientBuilderConfiguration = new ClientBuilderConfiguration();
        // 显式声明使用 V4 签名算法

        DefaultCredentialProvider credentialProvider = CredentialsProviderFactory.newDefaultCredentialProvider(aliOssProperties.getAccessKeyId()
                , aliOssProperties.getAccessKeySecret());

        String region = "beijing";

        clientBuilderConfiguration.setSignatureVersion(SignVersion.V4);
        OSS ossClient = OSSClientBuilder.create()
                .endpoint(aliOssProperties.getEndpoint())
                .credentialsProvider(credentialProvider)
                .region(region)
                .build();
        // 创建OSSClient实例。
        StringBuilder stringBuilder = new StringBuilder("https://");

        try {
            // 创建PutObject请求。
            ossClient.putObject(aliOssProperties.getBucketName(), objectName, file.getInputStream());

            //文件访问路径规则 https://BucketName.Endpoint/ObjectName
            stringBuilder.append(aliOssProperties.getBucketName())
                    .append(".")
                    .append(aliOssProperties.getEndpoint())
                    .append("/")
                    .append(objectName);
        } catch (OSSException oe) {
            log.error("阿里OSS异常，Error Message:{}", oe.getErrorMessage());
        } catch (ClientException ce) {
            log.error("阿里OSS Client异常，Error Message:{}", ce.getMessage());
        } finally {
            if (!ObjectUtils.isEmpty(ossClient)) {
                ossClient.shutdown();
            }
        }

        return stringBuilder.toString();
    }

    @Override
    public void deleteFile(String objectName)  {
        // 创建 OSSClient 实例
        ClientBuilderConfiguration clientBuilderConfiguration = new ClientBuilderConfiguration();
        // 显式声明使用 V4 签名算法

        DefaultCredentialProvider credentialProvider = CredentialsProviderFactory.newDefaultCredentialProvider(aliOssProperties.getAccessKeyId()
                , aliOssProperties.getAccessKeySecret());

        String region = "beijing";

        clientBuilderConfiguration.setSignatureVersion(SignVersion.V4);
        OSS ossClient = OSSClientBuilder.create()
                .endpoint(aliOssProperties.getEndpoint())
                .credentialsProvider(credentialProvider)
                .region(region)
                .build();
        try {
            // 创建PutObject请求。
            ossClient.deleteObject(aliOssProperties.getBucketName(), objectName);
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
