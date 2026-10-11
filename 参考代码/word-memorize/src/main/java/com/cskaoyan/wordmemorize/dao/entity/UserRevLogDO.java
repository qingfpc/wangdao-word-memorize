package com.cskaoyan.wordmemorize.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cskaoyan.wordmemorize.common.entity.BaseDO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Objects;

@Data
@TableName("user_rev_log")
public class UserRevLogDO extends BaseDO {

    Long userId;

    Long vocId;

    String vocContent;

    Integer lastReviewInterval;

    Integer reviewInterval;

    Integer reviewType;

    Long time;

    LocalDate reviewDate;

    Double factor;

    Double lastFactor;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserRevLogDO that = (UserRevLogDO) o;
        return Objects.equals(userId, that.userId) && Objects.equals(vocId, that.vocId) && Objects.equals(vocContent, that.vocContent) && Objects.equals(lastReviewInterval, that.lastReviewInterval) && Objects.equals(reviewInterval, that.reviewInterval) && Objects.equals(reviewType, that.reviewType) && Objects.equals(time, that.time) && Objects.equals(reviewDate, that.reviewDate) && Objects.equals(factor, that.factor) && Objects.equals(lastFactor, that.lastFactor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), userId, vocId, vocContent, lastReviewInterval, reviewInterval, reviewType, time, reviewDate, factor, lastFactor);
    }
}
