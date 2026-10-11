package com.cskaoyan.wordmemorize.dto.app;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Objects;

@Data
public class UserRemindDTO{
    @Schema(description = "id值")
    String id;
    @Schema(description = "用户id")
    String userId;
    @Schema(description = "提醒时间")
    String remindTime;
    @Schema(description = "消息通知状态: 1.开启 0.未开启")
    Integer messageStatus;
    @Schema(description = "短信通知条件描述")
    String messageRemindDescription;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserRemindDTO that = (UserRemindDTO) o;
        return Objects.equals(id, that.id) && Objects.equals(userId, that.userId) && Objects.equals(remindTime, that.remindTime) && Objects.equals(messageStatus, that.messageStatus) && Objects.equals(messageRemindDescription, that.messageRemindDescription);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, remindTime, messageStatus, messageRemindDescription);
    }
}
