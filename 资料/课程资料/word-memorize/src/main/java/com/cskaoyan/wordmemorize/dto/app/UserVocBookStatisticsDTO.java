package com.cskaoyan.wordmemorize.dto.app;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UserVocBookStatisticsDTO {

    @Schema(description = "不熟的单词数量")
    Integer unFamiliar;
    @Schema(description = "一般的单词数量")
    Integer general;
    @Schema(description = "熟悉的单词数量")
    Integer familiar;
    @Schema(description = "已选未学的单词数量")
    Integer notStarted;
    @Schema(description = "单词总数")
    Integer total;
    @Schema(description = "未选单词数量")
    Integer unSelected;
}
