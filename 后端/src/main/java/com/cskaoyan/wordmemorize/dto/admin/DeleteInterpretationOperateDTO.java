package com.cskaoyan.wordmemorize.dto.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "删除单词-书籍-章节关系日志")
public class DeleteInterpretationOperateDTO {

    @Schema(description = "删除的id")
    Long id;

    @Schema(description = "删除的释义类型")
    String type;

    @Schema(description = "删除的释义内容")
    String interContent;

    @Schema(description = "删除的单词id")
    Long vocId;

    @Schema(description = "单词名称")
    String vocName;

}
