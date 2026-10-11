package com.cskaoyan.wordmemorize.dto.app;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.util.Objects;

@Data
@Schema(description = "封装用户打卡数据")
public class UserCheckinDTO {

    @Schema(description = "id值")
    String id;
    @Schema(description = "userId值")
    String userId;
    @Schema(description = "用户签到日期")
    LocalDate signDate;
    @Schema(description = "签到类型: 1正常签到 2补签")
    Integer type;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserCheckinDTO that = (UserCheckinDTO) o;
        return Objects.equals(id, that.id) && Objects.equals(userId, that.userId) && Objects.equals(signDate, that.signDate) && Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, signDate, type);
    }
}
