package com.cskaoyan.wordmemorize.dao.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cskaoyan.wordmemorize.common.entity.BaseDO;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@TableName("level")
@Data
public class LevelDO extends BaseDO {

    String levelName;

    Integer levelValue;

    /**
        签到天数
     */
    Integer checkInDays;

    /**
     *   签到类型
     */
    Integer checkInType;

    /**
       解锁所需的金额
     */
    BigDecimal unlockPrice;

    String description;
    // 新增字段
    @TableField(exist = false)
    private Long goodsId; // 存储关联的商品ID
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LevelDO levelDO = (LevelDO) o;
        return Objects.equals(levelName, levelDO.levelName) && Objects.equals(levelValue, levelDO.levelValue) && Objects.equals(checkInDays, levelDO.checkInDays) && Objects.equals(checkInType, levelDO.checkInType) && Objects.equals(unlockPrice, levelDO.unlockPrice) && Objects.equals(description, levelDO.description) && Objects.equals(levelPrivileges, levelDO.levelPrivileges);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), levelName, levelValue, checkInDays, checkInType, unlockPrice, description, levelPrivileges);
    }

    @TableField(exist = false)
    List<LevelPrivilegeDO> levelPrivileges;

}
