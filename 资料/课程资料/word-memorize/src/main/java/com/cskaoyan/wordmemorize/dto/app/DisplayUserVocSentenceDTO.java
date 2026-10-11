package com.cskaoyan.wordmemorize.dto.app;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;
import java.util.Objects;

@Data
public class DisplayUserVocSentenceDTO {

    @Schema(description = "用户id值")
    String userId;
    @Schema(description = "单词id值")
    String vocId;
    @Schema(description = "单词")
    String vocContent;
    @Schema(description = "例句集合")
    List<UserVocSentenceDTO> sentences;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DisplayUserVocSentenceDTO that = (DisplayUserVocSentenceDTO) o;
        return Objects.equals(userId, that.userId) && Objects.equals(vocId, that.vocId) && Objects.equals(vocContent, that.vocContent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, vocId, vocContent);
    }
}
