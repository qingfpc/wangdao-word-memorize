package com.cskaoyan.wordmemorize.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cskaoyan.wordmemorize.dao.entity.UserVocBoundLogDO;
import org.apache.ibatis.annotations.Param;

public interface UserVocBoundLogMapper extends BaseMapper<UserVocBoundLogDO> {

    void addUserVocBoundLog(@Param("log") UserVocBoundLogDO userVocBoundLogDO);
}
