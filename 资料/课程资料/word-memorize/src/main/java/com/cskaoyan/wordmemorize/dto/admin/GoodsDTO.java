package com.cskaoyan.wordmemorize.dto.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Objects;

@Data
public class GoodsDTO {

    @Schema(description = "id值")
    String id;
    @Schema(description = "商品名称")
    String name;
    @Schema(description = "商品价格")
    BigDecimal price;
    @Schema(description = "商品描述")
    String desc;
    @Schema(description = "上下架状态: 0-下架 1-上架")
    Integer status;
    @Schema(description = "商品值，如果是等级商品则表示等级值，如果是单词上限则表示单词上限个数")
    Integer value;
    @Schema(description = "商品类型: 1. 单词上限 2. 等级解锁 3. 补签卡")
    Integer type;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GoodsDTO goodsDTO = (GoodsDTO) o;
        return Objects.equals(name, goodsDTO.name) && Objects.equals(price, goodsDTO.price) && Objects.equals(desc, goodsDTO.desc) && Objects.equals(value, goodsDTO.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price, desc, value);
    }
}
