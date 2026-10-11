package com.cskaoyan.wordmemorize.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cskaoyan.wordmemorize.dao.entity.UserVocRevDO;
import com.cskaoyan.wordmemorize.request.PageRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserVocRevMapper extends BaseMapper<UserVocRevDO> {

    // 添加查询用户已选单词复习数据的方法
    List<UserVocRevDO> selectUserVocRevData(@Param("offset") int offset, @Param("count") int count, @Param("userId") Long userId);

    // 添加查询指定用户的单词复习数据总数的方法
    Long selectUserVocRevDataCount(@Param("userId") Long userId);

    UserVocRevDO selectByUserIdAndVocId(@Param("userId") Long userId, @Param("vocId") Long vocId);

    /**
     * 插入或更新用户单词复习数据
     */
    int upsertUserVocRev(@org.apache.ibatis.annotations.Param("userVocRevDO") UserVocRevDO userVocRevDO);

    int countByUserIdAndVocId(@Param("userId") Long userId, @Param("vocId") Long vocId);
}
