package com.cskaoyan.wordmemorize.dto.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "删除选题填空题选项日志")
public class DeleteSubTopicOperateDTO {

    @Schema(description = "选题填空题选项id")
    private Long subTopicId;
    @Schema(description = "选题填空题内容")
    private String subTopic;
    @Schema(description = "选题填空题对应的单词id")
    private Long vocId;
    @Schema(description = "选题填空题对应的单词名称")
    private String vocName;
}
