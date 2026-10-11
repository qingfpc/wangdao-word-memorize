package com.cskaoyan.wordmemorize.dto.app;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class NextLevelDTO {
    // 签到类型
    @Schema(description = "签到类型 1. 连续签到  2. 累计签到")
    Integer signType;
    @Schema(description = "下一次升级剩余的天数")
    Integer remainDays;
}
