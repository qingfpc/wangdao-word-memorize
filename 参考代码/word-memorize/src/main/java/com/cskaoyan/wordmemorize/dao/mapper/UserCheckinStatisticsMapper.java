package com.cskaoyan.wordmemorize.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cskaoyan.wordmemorize.dao.entity.UserCheckinDO;
import com.cskaoyan.wordmemorize.dao.entity.UserCheckinStatisticsDO;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;

public interface UserCheckinStatisticsMapper extends BaseMapper<UserCheckinStatisticsDO> {



    void updateCheckinStatistics(@Param("statisticInfo") UserCheckinStatisticsDO statisticInfo,
                                @Param("count")Integer count);

}
