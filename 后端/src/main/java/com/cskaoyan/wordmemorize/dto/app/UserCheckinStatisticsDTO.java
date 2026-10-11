package com.cskaoyan.wordmemorize.dto.app;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.util.Objects;

@Data
public class UserCheckinStatisticsDTO {
    @Schema(description = "id值")
    String id;
    @Schema(description = "用户id")
    String userId;
    @Schema(description = "签到总天数")
    Integer totalDays;
    @Schema(description = "最大连续签到天数")
    Integer maxContinuousDays;
    @Schema(description = "当前连续签到天数")
    Integer curContinuousDays;
    @Schema(description = "历史最大签到起始日期")
    LocalDate maxContinuousStartDate;
    @Schema(description = "当前连续签到起始日期")
    LocalDate curContinuousStartDate;
    @Schema(description = "总的学习时长，以ms为单位")
    String totalTime;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserCheckinStatisticsDTO that = (UserCheckinStatisticsDTO) o;
        return Objects.equals(id, that.id) && Objects.equals(userId, that.userId) && Objects.equals(totalDays, that.totalDays) && Objects.equals(maxContinuousDays, that.maxContinuousDays) && Objects.equals(curContinuousDays, that.curContinuousDays) && Objects.equals(maxContinuousStartDate, that.maxContinuousStartDate) && Objects.equals(curContinuousStartDate, that.curContinuousStartDate) && Objects.equals(totalTime, that.totalTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, totalDays, maxContinuousDays, curContinuousDays, maxContinuousStartDate, curContinuousStartDate, totalTime);
    }
}
