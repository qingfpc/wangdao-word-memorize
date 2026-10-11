package com.cskaoyan.wordmemorize.dto.app;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserVocBoundLogDTO {
    @Schema(description = "id")
    String id;
    @Schema(description = "用户id值")
    String userId;
    @Schema(description = "支付交易id值，如果充值则有值，否则无值")
    String paySaleId;
    @Schema(description = "变化类型: 1. 充值  2. 签到 3. 消费 4.注册")
    Integer type;
    @Schema(description = "变化类型日期")
    LocalDate logDate;
    @Schema(description = "变化之的单词上限数量")
    Integer count;
    @Schema(description = "本次变化的描述字符串")
    String description;

}
