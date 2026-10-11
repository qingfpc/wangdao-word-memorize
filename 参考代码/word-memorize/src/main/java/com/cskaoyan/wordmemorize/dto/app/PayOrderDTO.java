package com.cskaoyan.wordmemorize.dto.app;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Objects;

@Data
public class PayOrderDTO {

    @Schema(description = "支付交易id")
    String id;

    @Schema(description = "订单总金额")
    BigDecimal totalAmount;

    @Schema(description = "订单交易标题")
    String subject;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PayOrderDTO that = (PayOrderDTO) o;
        return Objects.equals(id, that.id) && Objects.equals(totalAmount, that.totalAmount) && Objects.equals(subject, that.subject);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, totalAmount, subject);
    }
}
