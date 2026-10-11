package com.cskaoyan.wordmemorize.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class VocChapBookCommand {

    @Schema(description = "单词-章节-书籍关系id")
    String id;
    @Schema(description = "单词id")
    String vocId;
    @Schema(description = "单词")
    String vocName;
    @Schema(description = "章节id")
    String chapterId;
    @Schema(description = "章节名称")
    String chapterName;
    @Schema(description = "书籍id")
    String bookId;
    @Schema(description = "书籍名称")
    String bookName;
}
