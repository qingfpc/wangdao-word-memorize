package com.cskaoyan.wordmemorize.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cskaoyan.wordmemorize.dao.entity.UserVocRevDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserVocRevMapper extends BaseMapper<UserVocRevDO> {

   List<UserVocRevDO> getUserVocRevWithBook(@Param("userId")Long userId, @Param("bookId")Long bookId);

   Integer getUserVocRevBookStatisticByType(@Param("userId")Long userId, @Param("bookId")Long bookId,
                                                                         @Param("type") Integer type);

   Integer getUserVocRevBookStatisticByInterval(@Param("userId")Long userId, @Param("bookId")Long bookId, @Param("isFamiliar") boolean isFamiliar, @Param("interval") Integer interval);

   Integer getUserVocRevOfBookCount(@Param("userId")Long userId, @Param("bookId")Long bookId);
}
