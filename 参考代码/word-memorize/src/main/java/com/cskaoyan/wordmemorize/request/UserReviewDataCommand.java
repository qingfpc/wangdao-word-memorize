package com.cskaoyan.wordmemorize.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Schema(description = "用户复习数据")
@Data
public class UserReviewDataCommand {
    @Schema(description = "用户复习统计数据(以天为单位)")
    UserReviewStatisticsCommand revStatisticsCommand;
    @Schema(description = "用户已选单词(包括单词的复习情况)")
    List<UserVocRevCommand> vocRevCommands;
}
