package com.cskaoyan.wordmemorize.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cskaoyan.wordmemorize.dao.entity.*;
import org.apache.ibatis.annotations.Param;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface VocMapper extends BaseMapper<VocDO> {
    Page<VocDO> getVocabularyInfo(@Param("bookId") @Nullable Long bookId
            , @Param("chapterId") @Nullable Long chapterId,
                                  @Param("keyword") @Nullable String keyword,
                                  Page<VocDO> pageInfo);

    void updateBookVocCount(@Param("bookId") Long bookId, @Param("count") Integer count);

    void updateChapterVocCount(@Param("chapterId") Long chapterId, @Param("count") Integer count);



    VocDO getVocDetail(@Param("userId") Long userId, @Param("vocId") Long vocId);


    List<UserVocExampleSentenceDO> getUserVocExampleSentence(@Param("userId") Long userId, @Param("vocId") Long vocId);

}
