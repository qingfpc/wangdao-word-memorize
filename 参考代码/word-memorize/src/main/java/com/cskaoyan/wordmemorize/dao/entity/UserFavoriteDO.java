package com.cskaoyan.wordmemorize.dao.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cskaoyan.wordmemorize.common.entity.BaseDO;
import lombok.Data;

import java.util.List;
import java.util.Objects;

@TableName("user_favorite")
@Data
public class UserFavoriteDO extends BaseDO {
    Long userId;

    Long vocId;

    String vocContent;

    Integer selected;

    @TableField(exist = false)
    List<VocMeaningDO> interpretations;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserFavoriteDO that = (UserFavoriteDO) o;
        return Objects.equals(userId, that.userId) && Objects.equals(vocId, that.vocId) && Objects.equals(vocContent, that.vocContent) && Objects.equals(selected, that.selected);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), userId, vocId, vocContent, selected);
    }
}
