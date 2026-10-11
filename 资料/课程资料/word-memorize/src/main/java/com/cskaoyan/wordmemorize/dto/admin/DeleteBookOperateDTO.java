package com.cskaoyan.wordmemorize.dto.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 *  封装删除书籍操作日志
 */
@Data
@Schema(description = "删除书籍操作日志")
public class DeleteBookOperateDTO {

   @Schema(description = "书籍id")
    Long id;

    @Schema(description = "书籍名称")
    String name;

    @Schema(description = "书籍分类id")
    Long bcId;

    @Schema(description = "书籍分类id")
    String bcName;
}
