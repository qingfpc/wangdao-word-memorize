package com.cskaoyan.wordmemorize.dto.app;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Objects;

@Data
public class DisplayVocInterpretationDTO {

    @Schema(description = "单词释义id")
    String id;
    @Schema(description = "单词释义词性")
    String type;
    @Schema(description = "单词释义内容")
    String content;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DisplayVocInterpretationDTO that = (DisplayVocInterpretationDTO) o;
        return Objects.equals(id, that.id) && Objects.equals(type, that.type) && Objects.equals(content, that.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, content);
    }
}
