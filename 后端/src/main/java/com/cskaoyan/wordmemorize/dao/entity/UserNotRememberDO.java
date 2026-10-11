package com.cskaoyan.wordmemorize.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cskaoyan.wordmemorize.common.entity.BaseDO;
import lombok.Data;

import java.util.Objects;

@TableName("user_not_remember")
@Data
public class UserNotRememberDO extends BaseDO {

    Long userId;
    Long vocId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserNotRememberDO that = (UserNotRememberDO) o;
        return Objects.equals(userId, that.userId) && Objects.equals(vocId, that.vocId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), userId, vocId);
    }
}
