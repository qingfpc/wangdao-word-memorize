package com.cskaoyan.wordmemorize.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cskaoyan.wordmemorize.dao.entity.UserVocExampleSentenceDO;
import com.cskaoyan.wordmemorize.dao.entity.VocDO;
import org.apache.ibatis.annotations.Param;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface VocMapper extends BaseMapper<VocDO> {

}
