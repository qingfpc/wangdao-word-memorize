package com.cskaoyan.wordmemorize.dao.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cskaoyan.wordmemorize.common.entity.BaseDO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.ObjectInputFilter;
import java.time.LocalTime;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("user_remind")
@Builder
public class UserRemindDO extends BaseDO {
    Long userId;
    LocalTime remindTime;
    Integer messageStatus;
    String userName;
    String phoneNumber;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserRemindDO that = (UserRemindDO) o;
        return Objects.equals(userId, that.userId) && Objects.equals(remindTime, that.remindTime) && Objects.equals(messageStatus, that.messageStatus) && Objects.equals(userName, that.userName) && Objects.equals(phoneNumber, that.phoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), userId, remindTime, messageStatus, userName, phoneNumber);
    }
}
