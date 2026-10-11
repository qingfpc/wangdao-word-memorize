package com.cskaoyan.wordmemorize.dto.app;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Data
public class VocPaySaleDTO {

    @Schema(description = "id值")
    String id;
    @Schema(description = "用户id")
    String userId;
    @Schema(description = "购买的商品或等级解锁或特权名称")
    String itemName;
    @Schema(description = "用户支付的类型: 1.单词上限支付 2.等级解锁 3.补签卡")
    Integer itemType;
    @Schema(description = "支付的金额")
    BigDecimal payPrice;
    @Schema(description = "交易时间")
    LocalDateTime saleTime;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VocPaySaleDTO that = (VocPaySaleDTO) o;
        return Objects.equals(id, that.id) && Objects.equals(userId, that.userId) && Objects.equals(itemName, that.itemName) && Objects.equals(itemType, that.itemType) && Objects.equals(payPrice, that.payPrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, itemName, itemType, payPrice);
    }
}
