package com.cskaoyan.wordmemorize.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cskaoyan.wordmemorize.dao.entity.UserRevStatisticsDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

@Service
public interface UserRevStatisticsMapper extends BaseMapper<UserRevStatisticsDO> {

    void upsertUserRevStatistics(@Param("userRevStatisticsDO") UserRevStatisticsDO userRevStatisticsDO);
}
