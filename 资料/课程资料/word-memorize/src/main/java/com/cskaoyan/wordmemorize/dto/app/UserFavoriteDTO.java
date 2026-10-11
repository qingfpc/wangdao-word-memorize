package com.cskaoyan.wordmemorize.dto.app;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;
import java.util.Objects;

@Data
public class UserFavoriteDTO {
    @Schema(description = "单词id")
    String vocId;
    @Schema(description = "收藏的单词")
    String vocContent;
    @Schema(description = "是否已选单词")
    Integer selected;
    @Schema(description = "单词释义")
    List<DisplayVocInterpretationDTO> interpretations;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserFavoriteDTO that = (UserFavoriteDTO) o;
        return Objects.equals(vocId, that.vocId) && Objects.equals(vocContent, that.vocContent) && Objects.equals(selected, that.selected);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vocId, vocContent, selected);
    }
}
