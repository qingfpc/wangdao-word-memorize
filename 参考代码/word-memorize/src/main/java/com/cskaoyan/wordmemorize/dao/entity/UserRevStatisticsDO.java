package com.cskaoyan.wordmemorize.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cskaoyan.wordmemorize.common.entity.BaseDO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Objects;

@TableName("user_rev_statistics")
@Data
public class UserRevStatisticsDO extends BaseDO {

    Long userId;

    Integer vocNewCount;

    Integer vocReviewCount;

    Integer totalCount;

    Integer forgetCount;

    Integer uncertainCount;

    Integer knownCount;

    Integer simpleCount;

    Long studyTime;

    LocalDate reviewDate;



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserRevStatisticsDO that = (UserRevStatisticsDO) o;
        return Objects.equals(userId, that.userId) && Objects.equals(vocNewCount, that.vocNewCount) && Objects.equals(vocReviewCount, that.vocReviewCount) && Objects.equals(totalCount, that.totalCount) && Objects.equals(forgetCount, that.forgetCount) && Objects.equals(uncertainCount, that.uncertainCount) && Objects.equals(knownCount, that.knownCount) && Objects.equals(simpleCount, that.simpleCount) && Objects.equals(studyTime, that.studyTime) && Objects.equals(reviewDate, that.reviewDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), userId, vocNewCount, vocReviewCount, totalCount, forgetCount, uncertainCount, knownCount, simpleCount, studyTime, reviewDate);
    }
}
