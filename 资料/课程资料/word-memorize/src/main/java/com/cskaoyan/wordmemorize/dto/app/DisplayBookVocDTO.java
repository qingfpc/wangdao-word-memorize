package com.cskaoyan.wordmemorize.dto.app;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;
import java.util.Objects;

@Data
public class DisplayBookVocDTO {

    @Schema(description = "单词id")
    String id;
    @Schema(description = "单词")
    String content;
    @Schema(description = "单词对应的释义集合")
    List<DisplayVocInterpretationDTO> interpretations;
    @Schema(description = "是否已选单词，1.已选 0.未选")
    Integer reviewed;
    @Schema(description = "是否已标注不背 0. 不背 1.背")
    Integer notRemember;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DisplayBookVocDTO that = (DisplayBookVocDTO) o;
        return Objects.equals(id, that.id) && Objects.equals(content, that.content) && Objects.equals(reviewed, that.reviewed) && Objects.equals(notRemember, that.notRemember);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, content, reviewed, notRemember);
    }
}
