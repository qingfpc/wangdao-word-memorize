package com.cskaoyan.wordmemorize.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cskaoyan.wordmemorize.common.entity.BaseDO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Objects;

@Data
@TableName("user_voc_bound_log")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserVocBoundLogDO extends BaseDO {

    Long userId;

    Long paySaleId;

    Integer type;

    LocalDate logDate;

    Integer count;

    String description;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserVocBoundLogDO that = (UserVocBoundLogDO) o;
        return Objects.equals(userId, that.userId) && Objects.equals(paySaleId, that.paySaleId) && Objects.equals(type, that.type) && Objects.equals(logDate, that.logDate) && Objects.equals(count, that.count) && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), userId, paySaleId, type, logDate, count, description);
    }
}
