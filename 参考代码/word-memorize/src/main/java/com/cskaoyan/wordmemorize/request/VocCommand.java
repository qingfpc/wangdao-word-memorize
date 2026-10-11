package com.cskaoyan.wordmemorize.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "单词请求体")
public class VocCommand {
    @Schema(description = "单词id")
    String id;
    @Schema(description = "单词")
    String content;
    @Schema(description = "单词英式音标")
    String ukPhonetic;
    @Schema(description = "单词美式音标")
    String usPhonetic;
    @Schema(description = "单词英式发音文件")
    String ukPronounciation;
    @Schema(description = "单词美式发音文件")
    String usPronounciation;
    @Schema(description = "单词释义列表")
    List<VocMeaningCommand> interpretations;
    @Schema(description = "单词例句列表")
    List<ExampleSentenceCommand> examples;
    @Schema(description = "单词-章节-书籍关系列表集合")
    List<VocChapBookCommand> relations;

}
