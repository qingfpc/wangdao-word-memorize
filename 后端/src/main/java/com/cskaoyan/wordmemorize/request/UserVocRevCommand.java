package com.cskaoyan.wordmemorize.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserVocRevCommand {

    @Schema(description = "id值")
    String id;
    @Schema(description = "单词id")
    String vocId;
    @Schema(description = "用户id")
    String userId;
    @Schema(description = "当前间隔因子")
    Double factor;
    @Schema(description = "当前时间间隔")
    Integer reviewInterval;
    @Schema(description = "下次复习日期")
    LocalDate nextReviewDate;
    @Schema(description = "复习标记: 0. 还未学习 1. 正常复习 2.暂停复习")
    Integer type;
    @Schema(description = "最新的熟悉程度：0.不认识 1.模糊 2.认识 4.简单")
    Integer familiarity;
    @Schema(description = "连续复习失败次数(即连续完全不认识)")
    Integer consecutiveFailCount;
}
