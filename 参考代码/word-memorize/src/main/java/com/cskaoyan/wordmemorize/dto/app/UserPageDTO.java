package com.cskaoyan.wordmemorize.dto.app;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalTime;

@Data
public class UserPageDTO {

    @Schema(description = "每天规划单词学习量")
    Integer vocCountOfDay;
    @Schema(description = "总的单词上限数量")
    Integer totalVocBound;
    @Schema(description = "免费获取的单词上限数量")
    Integer freeVocBound;
    @Schema(description = "购买的单词上限数量")
    Integer payVocBound;
    @Schema(description = "购买的补签卡数量：只有两种取值要么0，要么1")
    Integer reSignCard;
    @Schema(description = "每日提醒学习时间, 格式为 HH:mm 24小时制")
    String remindTime;

}
