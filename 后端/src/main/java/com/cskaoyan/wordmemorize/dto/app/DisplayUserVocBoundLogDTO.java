package com.cskaoyan.wordmemorize.dto.app;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DisplayUserVocBoundLogDTO {
    @Schema(description = "id值")
    String id;
    @Schema(description = "用户id")
    String userId;
    @Schema(description = "明细类型1. 充值  2. 签到 3. 消费")
    Integer type;
    @Schema(description = "明细日期")
    LocalDate logDate;
    @Schema(description = "单词上限数量")
    Integer exchangeCount;
    @Schema(description = "描述")
    String description;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DisplayUserVocBoundLogDTO that = (DisplayUserVocBoundLogDTO) o;
        return Objects.equals(id, that.id) && Objects.equals(userId, that.userId) && Objects.equals(type, that.type) && Objects.equals(logDate, that.logDate) && Objects.equals(exchangeCount, that.exchangeCount) && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, type, logDate, exchangeCount, description);
    }
}
