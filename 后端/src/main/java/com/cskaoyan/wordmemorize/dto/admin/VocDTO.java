package com.cskaoyan.wordmemorize.dto.admin;

import com.cskaoyan.wordmemorize.dto.app.UserExampleSentenceDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;
import java.util.Objects;

@Data
public class VocDTO {
    @Schema(description = "单词id")
    String id;
    @Schema(description = "单词")
    String content;
    @Schema(description = "单词英式音标")
    String ukPhonetic;
    @Schema(description = "单词美式音标")
    String usPhonetic;
    @Schema(description = "单词美式发音文件url")
    String usPronounciation;
    @Schema(description = "单词英式发音文件url")
    String ukPronounciation;
    @Schema(description = "是否已选择复习: 0未选 1已选(移动端需要)")
    Integer reviewed;
    @Schema(description = "是否标识不背: 0正常 1标识不背(移动端需要)")
    Integer notRemember;
    @Schema(description = "标识是否收藏: 0未收藏 1已收藏(移动端需要)")
    Integer favorite;
    // 单词释义
    @Schema(description = "单词释义列表")
    List<VocMeaningDTO> interpretations;
    // 单词例句
    @Schema(description = "单词例句列表")
    List<ExampleSentenceDTO> examples;
    @Schema(description = "用户自建例句列表(移动端需要)")
    List<UserExampleSentenceDTO> userExamples;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VocDTO vocDTO = (VocDTO) o;
        return Objects.equals(id, vocDTO.id) && Objects.equals(content, vocDTO.content)&& Objects.equals(ukPhonetic, vocDTO.ukPhonetic) && Objects.equals(usPhonetic, vocDTO.usPhonetic) && Objects.equals(usPronounciation, vocDTO.usPronounciation) && Objects.equals(ukPronounciation, vocDTO.ukPronounciation) && Objects.equals(reviewed, vocDTO.reviewed) && Objects.equals(notRemember, vocDTO.notRemember);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, content, ukPhonetic, usPhonetic, usPronounciation, ukPronounciation, reviewed, notRemember);
    }
}
