package com.cskaoyan.wordmemorize.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

@Data
@Schema(name = "", description = "啦啦啦啦")
public class UserRevLogCommand {
    @Schema(description = "id值")
    String id;
    @Schema(description = "用户id值")
    String userId;
    @Schema(description = "用户复习的单词id")
    String vocId;
    @Schema(description = "用户复习的单词")
    String vocContent;
    @Schema(description = "用户上次复习该单词的时间间隔")
    Integer lastReviewInterval;
    @Schema(description = "用户下次复习该单词的时间间隔")
    Integer reviewInterval;
    @Schema(description = "复习反馈: 0.不认识 1.模糊 2.认识 4.简单")
    Integer reviewType;
    @Schema(description = "单词复习时长")
    String time;
    @Schema(description = "单词复习日期")
    LocalDate reviewDate;
    @Schema(description = "本次的复习间隔因子")
    Double factor;
    @Schema(description = "上次的复习间隔因子")
    Double lastFactor;
}
