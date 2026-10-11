package com.cskaoyan.wordmemorize.dto.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 *  封装删除章节操作日志
 */
@Data
@Schema(description = "删除章节操作日志")
public class DeleteChapterOperateDTO {
    @Schema(description = "章节id")
    Long id;

    @Schema(description = "章节名称")
    String chapterName;

    @Schema(description = "所属书籍id")
    Long bookId;

    @Schema(description = "所属书籍名称")
    String bookName;


}
