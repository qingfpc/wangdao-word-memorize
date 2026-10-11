package com.cskaoyan.wordmemorize.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class AIReviewReadCommand {

    @Schema(description = "单词集合")
    List<String> vocList;
    @Schema(description = "生成的场景名称")
    String situationName;
    @Schema(description = "生成的场景中的选项名称")
    String optionName;
}
