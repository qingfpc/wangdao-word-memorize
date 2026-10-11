package com.cskaoyan.wordmemorize.dao.entity;

import com.cskaoyan.wordmemorize.dao.entity.VocChapBookDO;
import com.cskaoyan.wordmemorize.dao.entity.VocDO;
import lombok.Data;

import java.util.List;

@Data
public class VocExcelParsedDO {
    private VocDO voc;                                  // 单词基本信息
    private List<VocChapBookDO> vocChapBooks;
}
