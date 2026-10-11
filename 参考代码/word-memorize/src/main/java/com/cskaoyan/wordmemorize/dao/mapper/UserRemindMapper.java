package com.cskaoyan.wordmemorize.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cskaoyan.wordmemorize.dao.entity.UserRemindDO;
import org.apache.ibatis.annotations.Param;

import java.time.LocalTime;
import java.util.List;

public interface UserRemindMapper extends BaseMapper<UserRemindDO> {


    UserRemindDO getUserRemindInfo(@Param("userId") Long userId);

}
