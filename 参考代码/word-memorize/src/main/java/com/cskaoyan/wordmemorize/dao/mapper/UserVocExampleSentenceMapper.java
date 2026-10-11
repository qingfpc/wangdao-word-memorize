package com.cskaoyan.wordmemorize.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cskaoyan.wordmemorize.dao.entity.UserVocExampleSentenceDO;
import io.swagger.v3.oas.annotations.Parameter;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserVocExampleSentenceMapper extends BaseMapper<UserVocExampleSentenceDO> {

    List<UserVocExampleSentenceDO> getUserVocSentencePage(@Param("userId") Long userId, @Param("limit") Integer limit, @Param("offset") Integer offset);

    Integer getUserVocSentenceCount(@Param("userId")Long userId);
}
