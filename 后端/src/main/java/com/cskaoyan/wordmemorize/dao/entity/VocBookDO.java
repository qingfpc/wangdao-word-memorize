package com.cskaoyan.wordmemorize.dao.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cskaoyan.wordmemorize.common.entity.BaseDO;
import lombok.Data;

import java.util.List;

@Data
@TableName("voc_book")
public class VocBookDO extends BaseDO {

    Long bcId;

    String name;

    String bcName;

    String author;

    String publisher;

    String isbn;

    Integer vocCount;

    String comment;

    String version;

    Integer sortBy;

    Integer chapterCount;

    @TableField(exist = false)
    List<VocChapterDO> chapters;

}
