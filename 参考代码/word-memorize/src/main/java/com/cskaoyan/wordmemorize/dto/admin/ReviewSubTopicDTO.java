package com.cskaoyan.wordmemorize.dto.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Objects;

@Data
public class ReviewSubTopicDTO {
    @Schema(description = "id值")
    String id;
    @Schema(description = "小题所属的题目id")
    String tid;
    @Schema(description = "单词id")
    String vocId;
    @Schema(description = "单词")
    String vocabulary;
    @Schema(description = "小题题目")
    String topicItem;
    @Schema(description = "小题编号")
    Integer subTopicNumber;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReviewSubTopicDTO that = (ReviewSubTopicDTO) o;
        return Objects.equals(id, that.id) && Objects.equals(tid, that.tid) && Objects.equals(vocId, that.vocId) && Objects.equals(vocabulary, that.vocabulary) && Objects.equals(topicItem, that.topicItem) && Objects.equals(subTopicNumber, that.subTopicNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tid, vocId, vocabulary, topicItem, subTopicNumber);
    }
}
