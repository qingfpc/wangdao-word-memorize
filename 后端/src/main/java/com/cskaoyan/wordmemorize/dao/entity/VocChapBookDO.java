package com.cskaoyan.wordmemorize.dao.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.cskaoyan.wordmemorize.common.entity.BaseDO;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

@TableName("voc_chap_book")
@Data
public class VocChapBookDO extends BaseDO {

    Long vocId;

    @TableField(exist = false)
    Long categoryId;
    @TableField(exist = false)
    String categoryName;

    String vocName;

    Long chapterId;
    String chapterName;

    Long bookId;

    String bookName;
}
