package com.cskaoyan.wordmemorize.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UserVocExampleSentenceCommand {

    @Schema(description = "自建例句id值")
    String id;
    @Schema(description = "用户id值")
    String userId;
    @Schema(description = "单词id值")
    String vocId;
    @Schema(description = "单词")
    String vocContent;
    @Schema(description = "例句")
    String sentence;
    @Schema(description = "例句的中文释义")
    String meaning;
}
