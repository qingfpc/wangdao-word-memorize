package com.cskaoyan.wordmemorize.request;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "章节请求体")
public class VocChapterCommand {
    @Parameter(description = "章节id")
    String id;
    @Parameter(description = "章节所属的书籍id")
    String bookId;
    @Parameter(description = "章节所属的书籍名称")
    String bookName;
    @Parameter(description = "章节名称")
    String chapterName;
    @Parameter(description = "章节排序字段")
    Integer sortBy;
    @Parameter(description = "章节包含的单词数量")
    Integer vocCount;
}
