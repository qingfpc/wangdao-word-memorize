package com.cskaoyan.wordmemorize.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cskaoyan.wordmemorize.dao.entity.UserBookStatisticsDO;
import com.cskaoyan.wordmemorize.dao.entity.VocChapBookDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface VocChapBookMapper extends BaseMapper<VocChapBookDO> {
    List<VocChapBookDO> getVocRelations(@Param("vocId") Long vocId);

}
