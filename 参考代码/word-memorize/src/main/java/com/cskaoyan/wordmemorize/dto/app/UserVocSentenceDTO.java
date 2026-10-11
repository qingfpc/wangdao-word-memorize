package com.cskaoyan.wordmemorize.dto.app;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UserVocSentenceDTO {
    @Schema(description = "自建例句id值")
    String id;
    @Schema(description = "例句")
    String sentence;
    @Schema(description = "例句的中文释义")
    String meaning;
}
