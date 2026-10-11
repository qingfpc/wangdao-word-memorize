package com.cskaoyan.wordmemorize.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cskaoyan.wordmemorize.common.entity.BaseDO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@TableName("user_checkin_statistics")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCheckinStatisticsDO extends BaseDO {

    Long userId;

    Integer totalDays;

    Integer maxContinuousDays = 0;
    Integer curContinuousDays = 0;

    LocalDate maxContinuousStartDate;
    LocalDate curContinuousStartDate;

    Long totalTime;

}
