package com.cskaoyan.wordmemorize.dto.admin;

import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "题目")
public class ReviewTopicDTO {
    @Schema(description = "题目id")
    String id;
    @Schema(description = "题目对应的单词id(仅针对单选题)")
    String vocId;
    @Schema(description = "题目类型: 1.单选 2.选词填空")
    Integer topicType;
    @Schema(description = "题目内容")
    String topic;
    @Schema(description = "题目对应的单词: 对于单选题是一个单词，对于多选题是多个单词，以逗号分隔")
    String vocabulary;
    @Schema(description = "小题(仅针对选词填空，一个选词填空有3个小题，四个选项，但是每个小题对应的正确选项和小题存储在一起，3个小题中包含了3个选项，还有一个干扰项存储在options中)")
    List<ReviewSubTopicDTO> subTopics;
    @Schema(description = "选项: 单选题对应4个选项，选词填空题只包含干扰项")
    List<TopicOptionDTO> options;
}
