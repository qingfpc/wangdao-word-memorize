package com.cskaoyan.wordmemorize.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "填空题小题请求体")
public class ReviewSubTopicCommand {
    @Schema(description = "填空题小题id")
    String id;
    @Schema(description = "填空题所属的题目id")
    String tid;
    @Schema(description = "填空题小题所针对的单词id(正确答案)")
    String vocId;
    @Schema(description = "填空题小题所针对的单词(正确答案)")
    String vocabulary;
    @Schema(description = "填空题小题内容")
    String topicItem;
    @Schema(description = "填空题小题编号")
    Integer subTopicNumber;
}
