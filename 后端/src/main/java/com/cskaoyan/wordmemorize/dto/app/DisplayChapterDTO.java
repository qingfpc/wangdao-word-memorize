package com.cskaoyan.wordmemorize.dto.app;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;
import java.util.Objects;

@Data
public class DisplayChapterDTO {

    @Schema(description = "章节id")
    String id;
    @Schema(description = "章节名称")
    String chapterName;
    @Schema(description = "章节包含的单词集合")
    List<DisplayBookVocDTO> displayVocs;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DisplayChapterDTO that = (DisplayChapterDTO) o;
        return Objects.equals(id, that.id) && Objects.equals(chapterName, that.chapterName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, chapterName);
    }
}
