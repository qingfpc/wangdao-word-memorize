package com.cskaoyan.wordmemorize.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserCheckinCommand {

    @Schema(description = "id值")
    String id;

    @Schema(description = "用户id")
    String userId;
    @Schema(description = "签到日期")
    LocalDate signDate;

    @Schema(description = "签到记录类型: 1.正常签到 2.补签")
    int type;
}
