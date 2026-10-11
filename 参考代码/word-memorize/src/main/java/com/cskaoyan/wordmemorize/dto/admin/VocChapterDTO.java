package com.cskaoyan.wordmemorize.dto.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class VocChapterDTO {

    @Schema(description = "章节id")
    String id;
    @Schema(description = "章节所属书籍id")
    String bookId;
    @Schema(description = "章节所属书籍名称")
    String bookName;
    @Schema(description = "章节名称")
    String chapterName;
    @Schema(description = "章节包含的单词数量")
    Integer vocCount;
    @Schema(description = "章节排序字段")
    Integer sortBy;

}
