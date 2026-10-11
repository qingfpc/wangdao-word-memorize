package com.cskaoyan.wordmemorize.common.util;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;

public class FileNameGenerator {

    public static final String nextFileName(String prefix) {
        long id = IdWorker.getId();
        return prefix + id;
    }
}
