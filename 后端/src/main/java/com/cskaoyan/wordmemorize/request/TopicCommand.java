package com.cskaoyan.wordmemorize.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "题目请求体")
public class TopicCommand {
    @Schema(description = "id")
    String id;
    @Schema(description = "选词填空题的小题列表(选词填空题才会有)")
    List<ReviewSubTopicCommand> subTopics;
    @Schema(description = "题目所针对的单词id(单选题才会有)")
    String vocId;
    @Schema(description = "题目所针对的单词")
    String vocabulary;
    @Schema(description = "题目类型：1.单选题 2.选词填空题")
    Integer topicType;
    @Schema(description = "题目")
    String topic;
    @Schema(description = "题目选项，单选题有四个，选词填空题有一个(就是干扰项)")
    List<TopicOptionCommand> options;
}
