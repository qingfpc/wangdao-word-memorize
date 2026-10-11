package com.cskaoyan.wordmemorize.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cskaoyan.wordmemorize.common.entity.BaseDO;
import lombok.Data;

import java.time.LocalDate;
import java.util.Objects;

@TableName("user_remind_message")
@Data
public class UserRemindMessageDO extends BaseDO {

    Long userId;
    String phoneNumber;
    LocalDate sendDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserRemindMessageDO that = (UserRemindMessageDO) o;
        return Objects.equals(userId, that.userId) && Objects.equals(phoneNumber, that.phoneNumber) && Objects.equals(sendDate, that.sendDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), userId, phoneNumber, sendDate);
    }
}
