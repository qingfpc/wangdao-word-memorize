package com.cskaoyan.wordmemorize.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cskaoyan.wordmemorize.dao.entity.UserVocBoundStatisticsDO;
import org.apache.ibatis.annotations.Param;

public interface UserVocBoundStatisticsMapper extends BaseMapper<UserVocBoundStatisticsDO> {

    void rewardUserVocBound(@Param("reward") Integer reward, @Param("userId") Long userId);

    void payUserVocBound(@Param("amount") Integer amount, @Param("userId") Long userId);

    void exchangeUserVocBound(@Param("amount") Integer amount, @Param("userId") Long userId);


    void updateOccupiedUserVocBound(@Param("occupied")Integer occupied, @Param("userId") Long userId);
}
