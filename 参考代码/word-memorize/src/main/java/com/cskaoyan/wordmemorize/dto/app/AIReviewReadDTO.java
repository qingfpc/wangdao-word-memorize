package com.cskaoyan.wordmemorize.dto.app;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AIReviewReadDTO {

    @Schema(description = "段落或者对话的英文内容")
    String segment;
    @Schema(description = "段落或者对话的中文翻译")
    String segmentTranslation;
}
