package com.cskaoyan.wordmemorize.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaySuccessNotifyCommand {


    @Schema(description = "支付交易id(由)")
    Long id;

    @Schema(description = "用户id")
    Long userId;

    @Schema(description = "支付交易金额")
    BigDecimal totalAmount;

    @Schema(description = "支付交易状态: TRADE_SUCCESS")
    String tradeStatus;

    @Schema(description = "通知id(我们自己给一个id值每次不重复就可以)")
    Long notifyId;


}
