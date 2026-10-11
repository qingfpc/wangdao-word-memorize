package com.cskaoyan.wordmemorize.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "等级添加请求")
public class LevelCommand {
    @Schema(description = "等级id")
    String id;
    @Schema(description = "等级所需的签到天数")
    Integer checkInDays;
    @Schema(description = "等级所需的签到类型")
    Integer checkInType;
    @Schema(description = "等级所需的解锁价格")
    BigDecimal unlockPrice;
    @Schema(description = "等级描述")
    String description;
    @Schema(description = "等级名称")
    String levelName;
    @Schema(description = "等级值")
    Integer levelValue;
}
