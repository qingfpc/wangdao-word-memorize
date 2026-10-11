package com.cskaoyan.wordmemorize.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cskaoyan.wordmemorize.dao.entity.UserVocAISituationOptionDO;

import java.util.List;

public interface UserVocAISituationOptionMapper extends BaseMapper<UserVocAISituationOptionDO> {
    List<UserVocAISituationOptionDO> selectBySituationId(Long situationId);
}
