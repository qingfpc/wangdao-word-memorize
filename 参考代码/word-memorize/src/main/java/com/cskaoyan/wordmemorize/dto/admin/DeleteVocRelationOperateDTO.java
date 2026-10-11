package com.cskaoyan.wordmemorize.dto.admin;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "删除单词-书籍-章节关系日志")
public class DeleteVocRelationOperateDTO {
    @Schema(description = "id")
    Long id;

    @Schema(description = "单词id")
    Long vocId;

    @Schema(description = "单词名称")
    String vocName;

    @Schema(description = "书籍id")
    Long bookId;
    @Schema(description = "书籍名称")
    String bookName;

    @Schema(description = "章节id")
    Long chapterId;

    @Schema(description = "章节名称")
    String chapterName;

}
