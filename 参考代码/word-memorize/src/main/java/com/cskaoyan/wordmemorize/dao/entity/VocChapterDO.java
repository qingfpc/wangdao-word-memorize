package com.cskaoyan.wordmemorize.dao.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cskaoyan.wordmemorize.common.entity.BaseDO;
import lombok.Data;

import java.util.List;

@TableName("voc_chapter")
@Data
public class VocChapterDO extends BaseDO {

    Long bookId;

    String bookName;

    String chapterName;

    Integer vocCount;

    Integer sortBy;

    @TableField(exist = false)
    List<VocDO> vocs;

}
