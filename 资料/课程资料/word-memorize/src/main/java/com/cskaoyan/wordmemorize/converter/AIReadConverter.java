package com.cskaoyan.wordmemorize.converter;

import com.cskaoyan.wordmemorize.dao.entity.UserVocAISituationDO;
import com.cskaoyan.wordmemorize.dao.entity.UserVocAISituationOptionDO;
import com.cskaoyan.wordmemorize.dto.admin.PageDTO;
import com.cskaoyan.wordmemorize.dto.admin.UserVocAISituationDTO;
import com.cskaoyan.wordmemorize.request.UserVocAISituationCommand;
import com.cskaoyan.wordmemorize.request.UserVocAISituationOptionCommand;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AIReadConverter {

    UserVocAISituationDO userVocAISituationCommandToDO(UserVocAISituationCommand command);
    UserVocAISituationOptionDO userVocAISituationOptionCommandToDO(UserVocAISituationOptionCommand command);

    @Mapping(source = "list", target = "pageList")
    @Mapping(source = "total", target = "totalCount")
    PageDTO<UserVocAISituationDTO> toUserVocAISituationPage(List<UserVocAISituationDO> list, Long total);
}
