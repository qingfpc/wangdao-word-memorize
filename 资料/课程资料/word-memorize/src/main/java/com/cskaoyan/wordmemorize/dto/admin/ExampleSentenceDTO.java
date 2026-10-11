package com.cskaoyan.wordmemorize.dto.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Objects;

@Data
public class ExampleSentenceDTO {
    @Schema(description = "例句id")
    String id;
    @Schema(description = "单词id")
    String vocId;
    @Schema(description = "单词")
    String content;
    @Schema(description = "例句释义")
    String meaning;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExampleSentenceDTO that = (ExampleSentenceDTO) o;
        return Objects.equals(id, that.id) && Objects.equals(vocId, that.vocId) && Objects.equals(content, that.content) && Objects.equals(meaning, that.meaning);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, vocId, content, meaning);
    }
}
