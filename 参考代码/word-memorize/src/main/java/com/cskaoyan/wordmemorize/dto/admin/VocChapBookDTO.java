package com.cskaoyan.wordmemorize.dto.admin;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class VocChapBookDTO {

    @Schema(description = "关系id")
    String id;
    @Schema(description = "书籍类目id")
    String categoryId;
    @Schema(description = "书籍类目名称")
    String categoryName;
    @Schema(description = "单词id")
    String vocId;
    @Schema(description = "单词")
    String vocName;
    @Schema(description = "单词所属章节id")
    String chapterId;
    @Schema(description = "单词所属章节名称")
    String chapterName;
    @Schema(description = "单词所属书籍id")
    String bookId;
    @Schema(description = "单词所属书籍名称")
    String bookName;
}
