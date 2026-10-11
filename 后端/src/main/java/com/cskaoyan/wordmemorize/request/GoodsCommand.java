package com.cskaoyan.wordmemorize.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class GoodsCommand {

    @Schema(description = "商品id")
    String id;
    @Schema(description = "商品名称")
    String name;
    @Schema(description = "商品价格")
    BigDecimal price;
    @Schema(description = "商品描述信息")
    String desc;
    @Schema(description = "商品上架状态：0-下架，1-上架")
    Integer status;
    @Schema(description = "商品类型: 1. 单词上限 2.等级解锁 3.补签卡")
    Integer type;
    @Schema(description = "商品值，如果是等级商品则表示等级值，如果是单词上限则表示单词上限个数")
    Integer value;
}
