package com.cskaoyan.wordmemorize.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cskaoyan.wordmemorize.dao.entity.UserNotRememberDO;

import org.springframework.stereotype.Service;

import org.apache.ibatis.annotations.Param;


@Service
public interface UserNotRememberMapper extends BaseMapper<UserNotRememberDO> {
    int countByUserIdAndVocId(@Param("userId") Long userId, @Param("vocId") Long vocId);
}
