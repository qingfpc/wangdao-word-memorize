package com.cskaoyan.wordmemorize.dto.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Objects;

@Data
public class LevelPrivilegeDTO {

    @Schema(description = "id")
    String id;
    @Schema(description = "特权码")
    String privilegeCode;
    @Schema(description = "特权名称")
    String privilegeName;
    @Schema(description = "是否免费使用: 1免费  2. 收费   3.先免费后收费 ")
    Integer useType;
    @Schema(description = "特权所需的单词上限数量")
    Integer requireVocBound;
    @Schema(description = "特权可以免费使用的次数")
    Integer freeCount;
    @Schema(description = "支付方式: 1. 消耗单词上限 2. 根据所需单词量以及单词和单词兑换比例计算 ")
    Integer exchangeType;
    @Schema(description = "所属等级")
    Integer levelValue;
    @Schema(description = "特权描述")
    String description;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LevelPrivilegeDTO that = (LevelPrivilegeDTO) o;
        return Objects.equals(id, that.id) && Objects.equals(privilegeCode, that.privilegeCode) && Objects.equals(useType, that.useType) && Objects.equals(requireVocBound, that.requireVocBound)&& Objects.equals(freeCount, that.freeCount) && Objects.equals(exchangeType, that.exchangeType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, privilegeCode, useType, requireVocBound, freeCount, exchangeType);
    }
}
