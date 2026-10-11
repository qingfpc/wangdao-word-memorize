package com.cskaoyan.wordmemorize.dao.entity;

import lombok.Data;

import java.util.List;


/*
     可能不是用来封装原始表中数据: 当我们自己做了多表查询，想要封装结果
 */
@Data
public class DisplayChapterDO {
    Long id;

    String chapterName;

    List<DisplayChapterVocDO> displayVocs;
}
