package com.cskaoyan.wordmemorize.dto.app;

import com.cskaoyan.wordmemorize.dto.admin.TopicOptionDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;
import java.util.Objects;

@Data
public class UserReviewSingleChoiceDTO {
    @Schema(description = "id值")
    String id;
    @Schema(description = "单词id")
    String vocId;
    @Schema(description = "单选题目")
    String topic;
    @Schema(description = "目标单词")
    String vocabulary;
    @Schema(description = "单选题的选项集合")
    List<TopicOptionDTO> options;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserReviewSingleChoiceDTO that = (UserReviewSingleChoiceDTO) o;
        return Objects.equals(id, that.id) && Objects.equals(vocId, that.vocId) && Objects.equals(topic, that.topic) && Objects.equals(vocabulary, that.vocabulary);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, vocId, topic, vocabulary);
    }
}
