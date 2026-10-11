package com.cskaoyan.wordmemorize.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileUploadService {

    // 实现文件上传到oss功能
    String upload(MultipartFile file) throws IOException;


    // 根据对象名称删除文件功能(在添加或者修改单词页面，删除发音文件)
    void deleteFile(String objectName);
}
