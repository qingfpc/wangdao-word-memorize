package com.cskaoyan.wordmemorize.dto.app;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.util.Objects;

@Data
public class UserReviewStatisticsDTO {

    @Schema(description = "id值")
    String id;
    @Schema(description = "用户id")
    String userId;
    @Schema(description = "复习的新单词数量")
    Integer vocNewCount;
    @Schema(description = "复习的旧单词数量")
    Integer vocReviewCount;
    @Schema(description = "复习的总单词数量")
    Integer totalCount;
    @Schema(description = "不认识的单词数量")
    Integer forgetCount;
    @Schema(description = "不确定的单词数量")
    Integer uncertainCount;
    @Schema(description = "认识的单词数量")
    Integer knowCount;
    @Schema(description = "简单的单词数量")
    Integer simpleCount;
    @Schema(description = "总的学习时长")
    String studyTime;
    @Schema(description = "复习日期")
    LocalDate reviewDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserReviewStatisticsDTO that = (UserReviewStatisticsDTO) o;
        return Objects.equals(id, that.id) && Objects.equals(userId, that.userId) && Objects.equals(vocNewCount, that.vocNewCount) && Objects.equals(vocReviewCount, that.vocReviewCount) && Objects.equals(totalCount, that.totalCount) && Objects.equals(forgetCount, that.forgetCount) && Objects.equals(uncertainCount, that.uncertainCount) && Objects.equals(knowCount, that.knowCount) && Objects.equals(simpleCount, that.simpleCount) && Objects.equals(studyTime, that.studyTime) && Objects.equals(reviewDate, that.reviewDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, vocNewCount, vocReviewCount, totalCount, forgetCount, uncertainCount, knowCount, simpleCount, studyTime, reviewDate);
    }
}
