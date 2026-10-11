package com.cskaoyan.wordmemorize.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cskaoyan.wordmemorize.dao.entity.UserCheckStickDO;
import com.cskaoyan.wordmemorize.dao.entity.UserCheckinDO;
import com.cskaoyan.wordmemorize.dao.entity.UserCheckinStatisticsDO;
import com.cskaoyan.wordmemorize.dao.entity.UserCheckinStickDateRangeDO;
import lombok.NonNull;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

public interface UserCheckinMapper extends BaseMapper<UserCheckinDO> {

    UserCheckinStatisticsDO getMaxContinuousCheckinInfo(@Param("userId") Long userId, @NonNull @Param("preLevelDate") LocalDate preLevelDate);

    UserCheckinStatisticsDO getLatestContinuousCheckinInfo(@Param("userId") Long userId, @Param("targetDate")LocalDate targetDate);


    Integer getTotalCheckinDaysFromPrevLevel(@Param("userId") Long userId);


    LocalDate getStartDateByStickNo(@Param("userId")Long userId, @Param("stickNo")Long stickNo);

    UserCheckStickDO getPreviousContinuousInfo(@Param("userId") Long userId, @Param("targetData")LocalDate targetData);

    LocalDate getLatestSignDateWithDays(@Param("userId") Long userId, @Param("days")Integer days, @Param("fromDate")LocalDate fromDate);

    UserCheckStickDO getStickInfoContainsDate(@Param("userId") Long userId, @Param("targetDate")LocalDate targetData);


    void addResignCheckinRecord(@Param("list") List<UserCheckinDO> checkinDOs);

    List<UserCheckinStickDateRangeDO> getSticksInfoWithDateRange(@Param("userId") Long userId, @Param("startDate")LocalDate startDate, @Param("endDate")LocalDate endDate);
}
