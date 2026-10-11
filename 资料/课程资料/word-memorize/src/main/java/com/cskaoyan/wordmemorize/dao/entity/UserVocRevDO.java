package com.cskaoyan.wordmemorize.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cskaoyan.wordmemorize.common.entity.BaseDO;
import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Objects;

@Data
@TableName("user_voc_rev")
public class UserVocRevDO extends BaseDO {

    Long vocId;

    Long userId;

    Double factor;

    Integer reviewInterval;

    LocalDate nextReviewDate;

    Integer type;

    Integer familiarity;

    Integer consecutiveFailCount;

    Integer totalReview;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserVocRevDO that = (UserVocRevDO) o;
        return Objects.equals(vocId, that.vocId) && Objects.equals(userId, that.userId) && Objects.equals(factor, that.factor) && Objects.equals(reviewInterval, that.reviewInterval) && Objects.equals(nextReviewDate, that.nextReviewDate) && Objects.equals(type, that.type) && Objects.equals(familiarity, that.familiarity) && Objects.equals(consecutiveFailCount, that.consecutiveFailCount) && Objects.equals(totalReview, that.totalReview);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), vocId, userId, factor, reviewInterval, nextReviewDate, type, familiarity, consecutiveFailCount, totalReview);
    }


}
