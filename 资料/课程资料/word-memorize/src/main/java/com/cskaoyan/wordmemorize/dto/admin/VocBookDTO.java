package com.cskaoyan.wordmemorize.dto.admin;

import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class VocBookDTO {
    @Schema(description = "id值")
    String id;
    @Schema(description = "类目id")
    String bcId;
    @Schema(description = "书籍类目名称")
    String bcName;
    @Schema(description = "书名")
    String name;
    @Schema(description = "作者")
    String author;
    @Schema(description = "出版社")
    String publisher;
    @Schema(description = "isbn序号")
    String isbn;
    @Schema(description = "包含的单词数量")
    Integer vocCount;
    @Schema(description = "备注")
    String comment;
    @Schema(description = "书籍版本")
    String version;
    @Schema(description = "排序字段")
    Integer sortBy;
    @Schema(description = "包含的章节数量")
    Integer chapterCount;

}
