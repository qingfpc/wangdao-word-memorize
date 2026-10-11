package com.cskaoyan.wordmemorize.dto.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Objects;

@Data
public class VocMeaningDTO {
    @Schema(description = "id值")
    String id;
    @Schema(description = "单词id")
    String vocId;
    @Schema(description = "释义类型")
    String type;
    @Schema(description = "释义内容")
    String interContent;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VocMeaningDTO that = (VocMeaningDTO) o;
        return Objects.equals(id, that.id) && Objects.equals(vocId, that.vocId) && Objects.equals(type, that.type) && Objects.equals(interContent, that.interContent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, vocId, type, interContent);
    }
}
