package com.cskaoyan.wordmemorize.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UserFavoriteCommand {
    @Schema(description = "用户id")
    String userId;
    @Schema(description = "单词id")
    String vocId;
    @Schema(description = "收藏的单词")
    String vocContent;
    @Schema(description = "是否已选单词")
    Integer selected;
}
