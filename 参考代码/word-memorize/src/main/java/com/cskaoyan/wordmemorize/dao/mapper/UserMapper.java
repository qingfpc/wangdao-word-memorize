package com.cskaoyan.wordmemorize.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cskaoyan.wordmemorize.dao.entity.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper extends BaseMapper<UserDO> {

    UserDO getUserInfo(@Param("userId") Long userId);

    LevelDO getNextLevelInfo(@Param("userId") Long userId);

    List<LevelDO> getLevelInfo();


    Integer getUserChooseCountInBook(@Param("userId")Long userId, @Param("bookId")Long bookId);


    List<VocChapterDO> getChapterVocsOfUserBook(@Param("userId") Long userId, @Param("bookId") Long bookId
            , @Param("offset") Integer offset, @Param("limit") Integer limit);

    List<VocDO> getVocsOfUserBook(@Param("userId") Long userId, @Param("bookId") Long bookId
            , @Param("offset") Integer offset, @Param("limit") Integer limit);

}
