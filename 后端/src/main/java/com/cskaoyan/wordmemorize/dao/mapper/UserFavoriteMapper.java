package com.cskaoyan.wordmemorize.dao.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cskaoyan.wordmemorize.dao.entity.UserFavoriteDO;
import com.cskaoyan.wordmemorize.dto.app.DisplayUserVocSentenceDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.List;


@Service

@Mapper

public interface UserFavoriteMapper extends BaseMapper<UserFavoriteDO> {
    int countByUserIdAndVocId(@Param("userId") Long userId, @Param("vocId") Long vocId);
}
