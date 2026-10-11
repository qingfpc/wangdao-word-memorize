package com.cskaoyan.wordmemorize.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class VocPaySaleCommand {

    @Schema(description = "id值")
    String id;
    @Schema(description = "用户id")
    String userId;
    @Schema(description = "用户支付的商品(特权id)")
    String itemId;
    @Schema(description = "用户支付的商品(特权名称)")
    String itemName;
    @Schema(description = "用户支付的类型: 1.单词上限支付 2.等级解锁 3.其他(等级特权)")
    Integer itemType;
    @Schema(description = "支付价格")
    BigDecimal payPrice;
    @Schema(description = "消耗的单词上限")
    Integer exchangeVocCount;
    @Schema(description = "支付状态，移动端忽略")
    Integer status;
    @Schema(description = "是否是购买补签卡")
    Boolean isSignCard;


}
