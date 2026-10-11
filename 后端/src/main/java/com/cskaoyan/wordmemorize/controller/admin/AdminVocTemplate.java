package com.cskaoyan.wordmemorize.controller.admin;

import org.apache.commons.compress.utils.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;

@RestController
@RequestMapping("/admin/voc")
public class AdminVocTemplate {
    //下载excel模板
    @GetMapping("template")
    public ResponseEntity<byte[]> downloadTemplate() throws IOException {
            // 使用ClassPathResource读取资源文件

            ClassPathResource resource;
            resource = new ClassPathResource("templates/单词上传模版.xlsx");

            // 检查文件是否存在
            if (!resource.exists()) {
                return ResponseEntity.notFound().build();
            }

            // 读取文件内容
            InputStream inputStream = resource.getInputStream();
            byte[] fileBytes = IOUtils.toByteArray(inputStream);

            // 设置响应头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment",
                    URLEncoder.encode(resource.getFilename(), "UTF-8"));

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(fileBytes);
        }
}
