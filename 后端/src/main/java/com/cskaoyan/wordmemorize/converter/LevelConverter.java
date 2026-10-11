package com.cskaoyan.wordmemorize.converter;


import com.cskaoyan.wordmemorize.dao.entity.LevelDO;
import com.cskaoyan.wordmemorize.dao.entity.LevelPrivilegeDO;
import com.cskaoyan.wordmemorize.dto.admin.*;
import com.cskaoyan.wordmemorize.request.LevelCommand;
import com.cskaoyan.wordmemorize.request.LevelPrivilegeCommand;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Mapper(componentModel = "spring")
public interface LevelConverter {


    LevelDO levelCommandToLevelDO(LevelCommand command);

    LevelDTO levelDOToLevelDTO(LevelDO levelDO);

    DeleteLevelOperateDTO levelDOToDeleteLevelOperateDTO(LevelDO levelDO);

    List<DeleteLevelOperateDTO> levelDOsToDeleteLevelOperateDTOs(List<LevelDO> levelDO);

    List<DeleteLevelPrivilegeOperateDTO> levelPrivilegeDOsToDeleteLevelPrivilegeOperateDTOs(List<LevelPrivilegeDO> levelPrivilegeDO);
    DeleteLevelPrivilegeOperateDTO levelPrivilegeDOToDeleteLevelPrivilegeOperateDTO(LevelPrivilegeDO levelPrivilegeDO);

    @Mapping(source = "levelDOs", target = "pageList")
    @Mapping(source = "count", target = "totalCount")
    PageDTO<LevelDTO> levelDOToPageDTO(List<LevelDO> levelDOs, Integer count);

    LevelPrivilegeDO levelPrivilegeCommandToLevelPrivilegeDO(LevelPrivilegeCommand command);
    LevelPrivilegeDTO levelPrivilegeDOToLevelPrivilegeDTO(LevelPrivilegeDO levelPrivilegeDO);
    @Mapping(source = "levelPrivilegeDTOs", target = "pageList")
    @Mapping(source = "count", target = "totalCount")
    PageDTO<LevelPrivilegeDTO> levelPrivilegeDOToPageDTO(List<LevelPrivilegeDO> levelPrivilegeDTOs, Integer count);
}
