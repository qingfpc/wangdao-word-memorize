package com.cskaoyan.wordmemorize.controller.admin;

import com.cskaoyan.wordmemorize.common.aspect.operate.OperateLog;
import com.cskaoyan.wordmemorize.common.aspect.operate.OperateLogConstant;
import com.cskaoyan.wordmemorize.common.result.Result;
import com.cskaoyan.wordmemorize.service.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/admin/voc")
public class AdminAddAudioFile {

    @Autowired
    @Qualifier("aliOssStorageServiceImpl")
    FileUploadService fileUploadService;
    //上传音频文件

//    @OperateLog(dateType = "上传文件", operateType = OperateLogConstant.OPERATE_ADD)
    @PostMapping("file")
    public Result<String> addAudioFile(@RequestParam("file") MultipartFile file) throws IOException {
        String upload = fileUploadService.upload(file);
        return Result.ok(upload);
    }
}

