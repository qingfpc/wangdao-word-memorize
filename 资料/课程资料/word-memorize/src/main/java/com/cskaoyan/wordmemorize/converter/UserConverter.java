package com.cskaoyan.wordmemorize.converter;

import com.cskaoyan.wordmemorize.common.util.DateUtil;
import com.cskaoyan.wordmemorize.dao.entity.*;
import com.cskaoyan.wordmemorize.dto.admin.LevelDTO;
import com.cskaoyan.wordmemorize.dto.admin.LevelPrivilegeDTO;
import com.cskaoyan.wordmemorize.dto.admin.PageDTO;
import com.cskaoyan.wordmemorize.dto.app.*;
import com.cskaoyan.wordmemorize.request.*;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalTime;
import java.util.List;

@Mapper(componentModel = "spring",builder = @Builder(disableBuilder = true))
public interface UserConverter {
    UserDTO userDO2DTO(UserDO userDO);

    NextLevelDTO levelDO2NextLevelDTO(LevelDO levelDO);

    @Mapping(source = "command.id", target = "id")
    UserRemindDO userRemindCommand2DO(UserRemindCommand command);



    @Mapping(source = "messageRemindDescription", target = "messageRemindDescription")
    UserRemindDTO userRemindDO2DTO(UserRemindDO userRemindDO, String messageRemindDescription);

    LevelDTO levelDO2LevelDTO(LevelDO levelDO);

    LevelPageDTO levelDO2LevelPageDTO(LevelDO levelDO);
    List<LevelPageDTO> levelDOsLevelPageDTOs(List<LevelDO> levelDOs);
    List<LevelDTO> levelDOsLevelDTOs(List<LevelDO> levelDOs);

    LevelPrivilegeDTO levelPrivilegeDO2DTO(LevelPrivilegeDO levelPrivilegeDO);

    List<LevelPrivilegeDTO> levelPrivilegeDOs2DTOs(List<LevelPrivilegeDO> levelPrivilegeDO);


    @Mapping(source = "list", target = "pageList")
    @Mapping(source = "total", target = "totalCount")
    PageDTO<DisplayUserVocBoundLogDTO> userVocBoundLogPage2PageDTO(List<DisplayUserVocBoundLogDTO> list, Long total);

    @Mapping(source = "count", target = "exchangeCount")
    DisplayUserVocBoundLogDTO userVocBoundLogDTO2DisplayDTO(UserVocBoundLogDO userVocBoundLogDO);
    List<DisplayUserVocBoundLogDTO> userVocBoundLogDTOs2DisplayDTOs(List<UserVocBoundLogDO> userVocBoundLogDO);

    UserVocBoundStatisticsDTO userVocBoundStatisticsDO2DisplayDTO(
            UserVocBoundStatisticsDO userVocBoundLogStaticsDO);
    default LocalTime timeStr2LocalTime(String timeStr) {
        LocalTime time = DateUtil.getTime(timeStr);
        return time;
    }

}
