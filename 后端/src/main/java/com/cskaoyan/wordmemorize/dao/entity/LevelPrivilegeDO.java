package com.cskaoyan.wordmemorize.dao.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cskaoyan.wordmemorize.common.entity.BaseDO;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Objects;

@TableName("level_privilege")
@Data
public class LevelPrivilegeDO extends BaseDO {

    /**
     *   等级id
     */
    Long levelId;

    Integer levelValue;

    /**
     *  特权名称
     */
    String privilegeName;

    /**
     *   特权图片名称
     */
    @TableField(exist = false)
    String imageName;

    /**
     *   特权描述
     */
    String description;

    /**
     *   特权码
     */
    String privilegeCode;

    Integer useType;

    Integer requireVocBound;

    Integer freeCount;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LevelPrivilegeDO that = (LevelPrivilegeDO) o;
        return Objects.equals(levelId, that.levelId) && Objects.equals(levelValue, that.levelValue) && Objects.equals(privilegeName, that.privilegeName) && Objects.equals(imageName, that.imageName) && Objects.equals(description, that.description) && Objects.equals(privilegeCode, that.privilegeCode) && Objects.equals(useType, that.useType) && Objects.equals(requireVocBound, that.requireVocBound);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), levelId, levelValue, privilegeName, imageName, description, privilegeCode, useType, requireVocBound, freeCount);
    }
}
