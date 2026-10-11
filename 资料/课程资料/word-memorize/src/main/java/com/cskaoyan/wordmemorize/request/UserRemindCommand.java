package com.cskaoyan.wordmemorize.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UserRemindCommand {
    @Schema(description = "id值")
    String id;
    @Schema(description = "用户id")
    String userId;
    @Schema(description = "提醒时间")
    String remindTime;
    @Schema(description = "系统通知状态: 1.开启 0.未开启")
    Integer sysStatus;
    @Schema(description = "消息通知状态: 1.开启 0.未开启")
    Integer messageStatus;
}
