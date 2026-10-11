package com.cskaoyan.wordmemorize.request;

import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class VocMeaningCommand {

    @Schema(description = "释义id")
    String id;

    @Schema(description = "单词id")
    String vocId;
    @Schema(description = "释义词性")
    String type;
    @Schema(description = "释义")
    String interContent;
}
