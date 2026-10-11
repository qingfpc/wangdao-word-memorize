package com.cskaoyan.wordmemorize.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "书籍请求体")
public class VocBookCommand {
    @Schema(description = "书籍id")
    String id;
    @Schema(description = "书籍所属分类id")
    String bcId;
    @Schema(description = "书籍所属分类名称")
    String bcName;
    @Schema(description = "书籍所属分类名称")
    String name;
    @Schema(description = "书籍作者")
    String author;
    @Schema(description = "书籍出版社")
    String publisher;
    @Schema(description = "书籍isbn编号")
    String isbn;
    @Schema(description = "书籍包含的单词数量")
    Integer vocCount;
    @Schema(description = "备注信息")
    String comment;
    @Schema(description = "书籍版本")
    String version;
    @Schema(description = "书籍排序字段")
    Integer sortBy;
    @Schema(description = "书籍是否包含章节：0-否，1-是")
    Integer hasChapters;
}
