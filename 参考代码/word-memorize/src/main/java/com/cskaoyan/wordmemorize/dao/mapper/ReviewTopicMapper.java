package com.cskaoyan.wordmemorize.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cskaoyan.wordmemorize.dao.entity.ReviewTopicDO;
import com.cskaoyan.wordmemorize.request.VocPageRequest;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ReviewTopicMapper extends BaseMapper<ReviewTopicDO> {

    List<ReviewTopicDO> getSingleChoiceTopicPageWithoutBook(
            @Param("keyword") String keyword
            , @Param("limit") Integer limit
           , @Param("offset") Integer offset);

    Integer getSingleChoiceTopicPageWithoutBookCount(@Param("keyword") String keyword);

    List<ReviewTopicDO>  getSingleChoiceTopicPageWithBook(
             @Param("limit") Integer limit
            , @Param("offset") Integer offset
            , @Param("keyword") String keyword
            , @Param("bookId") Long bookId
            , @Param("chapterId") Long chapterId
    );

    Integer getSingleChoiceTopicPageWithBookCount(@Param("keyword") String keyword
            , @Param("bookId") Long bookId
            , @Param("chapterId") Long chapterId);

    List<Long> getFillWordTopicPageWithBook( @Param("limit") Integer limit
            , @Param("offset") Integer offset
            , @Param("keyword") String keyword
            , @Param("bookId") Long bookId
            , @Param("chapterId") Long chapterId);

    Integer getFillWordTopicPageWithBookCount( @Param("keyword") String keyword
            , @Param("bookId") Long bookId
            , @Param("chapterId") Long chapterId);

    List<Long> getFillWordTopicPageWithoutBook(@Param("limit") Integer limit
            , @Param("offset") Integer offset
            , @Param("keyword") String keyword);

    Integer getFillWordTopicPageWithoutBookCount(@Param("keyword") String keyword);
}
