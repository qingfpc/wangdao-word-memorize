package com.cskaoyan.wordmemorize.dto.app;


import com.baomidou.mybatisplus.annotation.TableName;
import com.cskaoyan.wordmemorize.common.entity.BaseDO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserResignLogDTO {

    @Schema(description = "id值")
    String id;
    @Schema(description = "用户id")
    String userId;
    @Schema(description = "补签起始日期")
    LocalDate resignDate;
    @Schema(description = "补签天数")
    LocalDate resignDays;
    @Schema(description = "连续签到编号")
    String stickNumber;
    @Schema(description = "签到前的累计签到天数")
    Integer beforeSignTotalDays;
    @Schema(description = "签到前的最大连续签到天数")
    Integer beforeMaxContinuousSignDays;
    @Schema(description = "签到后的最大连续签到天数")
    Integer afterMaxContinuousSignDays;
    @Schema(description = "签到后的累计签到天数")
    Integer afterSignTotalDays;

}
