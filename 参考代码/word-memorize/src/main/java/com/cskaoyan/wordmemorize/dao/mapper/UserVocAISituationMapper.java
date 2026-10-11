package com.cskaoyan.wordmemorize.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cskaoyan.wordmemorize.dao.entity.UserVocAISituationDO;
import com.cskaoyan.wordmemorize.dto.admin.PageDTO;
import com.cskaoyan.wordmemorize.dto.admin.UserVocAISituationDTO;
import com.cskaoyan.wordmemorize.request.PageRequest;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserVocAISituationMapper extends BaseMapper<UserVocAISituationDO> {

    List<UserVocAISituationDO> getAIReadSituationInfo();

    List<UserVocAISituationDO> getAISituationPage(@Param("offset") Integer offset, @Param("limit") Integer limit);
}
