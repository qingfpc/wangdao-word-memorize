package com.cskaoyan.wordmemorize.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaySuccessNotifyCommand {
    @Schema(description = "支付交易id")
    Long id;

    @Schema(description = "支付交易金额")
    BigDecimal totalAmount;

    @Schema(description = "支付交易状态")
    String tradeStatus;

    @Schema(description = "通知id")
    Long notifyId;


}
