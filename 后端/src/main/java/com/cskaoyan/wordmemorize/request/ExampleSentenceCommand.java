package com.cskaoyan.wordmemorize.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ExampleSentenceCommand {
    @Schema(description = "例句id")
    String id;
    @Schema(description = "例句对应的单词id")
    String vocId;
    @Schema(description = "例句内容")
    String content;
    @Schema(description = "例句释义")
    String meaning;
}
