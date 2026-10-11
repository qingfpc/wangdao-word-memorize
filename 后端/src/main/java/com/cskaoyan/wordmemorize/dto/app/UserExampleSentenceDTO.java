package com.cskaoyan.wordmemorize.dto.app;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Objects;

@Data
public class UserExampleSentenceDTO {
    @Schema(description = "用户自建例句id值")
    String id;
    @Schema(description = "用户id")
    String userId;
    @Schema(description = "单词id")
    String vocId;
    @Schema(description = "例句")
    String sentence;
    @Schema(description = "例句释义")
    String meaning;
    @Schema(description = "例句对应的单词")
    String vocContent;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserExampleSentenceDTO that = (UserExampleSentenceDTO) o;
        return Objects.equals(id, that.id) && Objects.equals(userId, that.userId) && Objects.equals(vocId, that.vocId) && Objects.equals(sentence, that.sentence) && Objects.equals(meaning, that.meaning) && Objects.equals(vocContent, that.vocContent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, vocId, sentence, meaning, vocContent);
    }
}
