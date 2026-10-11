package com.cskaoyan.wordmemorize.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cskaoyan.wordmemorize.common.entity.BaseDO;
import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Objects;

@TableName("user_checkin")
@Data
public class UserCheckinDO extends BaseDO {

    Long userId;

    LocalDate signDate;

    Long stickNo;

    Integer type;

    Timestamp updatedTime;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserCheckinDO that = (UserCheckinDO) o;
        return Objects.equals(userId, that.userId) && Objects.equals(signDate, that.signDate) && Objects.equals(stickNo, that.stickNo) && Objects.equals(type, that.type) && Objects.equals(updatedTime, that.updatedTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), userId, signDate, stickNo, type, updatedTime);
    }
}
