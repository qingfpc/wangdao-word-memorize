package com.cskaoyan.wordmemorize.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cskaoyan.wordmemorize.dao.entity.LevelDO;
import com.cskaoyan.wordmemorize.dao.entity.UserDO;
import com.cskaoyan.wordmemorize.dao.entity.VocChapterDO;
import com.cskaoyan.wordmemorize.dao.entity.VocDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

public interface UserMapper extends BaseMapper<UserDO> {

    UserDO getUserInfo(@Param("userId") Long userId);

    Set<String> getPrivilegeCodesList(@Param("userId") Long userId);
}
