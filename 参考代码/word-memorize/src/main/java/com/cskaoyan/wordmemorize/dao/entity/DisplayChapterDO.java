package com.cskaoyan.wordmemorize.dao.entity;

import lombok.Data;

import java.util.List;

@Data
public class DisplayChapterDO {
    Long id;

    String chapterName;

    List<DisplayChapterVocDO> displayVocs;
}
