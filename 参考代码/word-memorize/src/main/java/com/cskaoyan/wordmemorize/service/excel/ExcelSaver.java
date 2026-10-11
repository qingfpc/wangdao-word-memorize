package com.cskaoyan.wordmemorize.service.excel;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.function.Function;

public interface ExcelSaver<T, R> {

    /**
     * 上传Excel文件, 返回值代表处理数据的条数
     */
    Integer  upload(MultipartFile file, Class<T> clz, Function<T, R> converter) throws IOException;
}
