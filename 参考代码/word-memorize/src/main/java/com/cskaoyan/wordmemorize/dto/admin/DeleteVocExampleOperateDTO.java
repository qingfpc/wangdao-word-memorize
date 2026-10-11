package com.cskaoyan.wordmemorize.dto.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "删除单词例句日志")
public class DeleteVocExampleOperateDTO {

    @Schema(description = "例句id")
    Long id;
    @Schema(description = "单词例句")
    String sentence;
    @Schema(description = "例句对应的单词id")
    Long vocId;
    @Schema(description = "例句对应的单词")
    String vocName;
}
