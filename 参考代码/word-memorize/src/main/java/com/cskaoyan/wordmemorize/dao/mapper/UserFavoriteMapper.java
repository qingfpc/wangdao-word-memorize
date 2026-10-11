package com.cskaoyan.wordmemorize.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cskaoyan.wordmemorize.dao.entity.UserFavoriteDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserFavoriteMapper extends BaseMapper<UserFavoriteDO> {
   List<UserFavoriteDO> getUserFavoritePage(@Param("userId") Long userId, @Param("keyword") String keyword);
}
