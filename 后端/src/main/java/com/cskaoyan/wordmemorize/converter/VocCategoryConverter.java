package com.cskaoyan.wordmemorize.converter;


import com.cskaoyan.wordmemorize.dao.entity.VocCategoryDO;
import com.cskaoyan.wordmemorize.dto.admin.DeleteBookCategoryOperateDTO;
import com.cskaoyan.wordmemorize.dto.admin.PageDTO;
import com.cskaoyan.wordmemorize.dto.admin.VocCategoryDTO;
import com.cskaoyan.wordmemorize.request.VocCategoryCommand;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface VocCategoryConverter {


    VocCategoryDO bookCategoryCommand2Entity(VocCategoryCommand command);

    VocCategoryDTO bookCategoryEntity2DTO(VocCategoryDO bookCategoryDO);

    List<VocCategoryDTO> bookCategoryEntities2DTOs(List<VocCategoryDO> bookCategoryDOs);

    @Mapping(source = "bookCategoryDTOs", target = "pageList")
    @Mapping(source = "total", target = "totalCount")
    PageDTO<VocCategoryDTO> bookCategoryList2Page(List<VocCategoryDO> bookCategoryDTOs, Long total);

    DeleteBookCategoryOperateDTO bookCategoryDOToDeleteCategoryDTO(VocCategoryDO bookCategoryDO);

    List<DeleteBookCategoryOperateDTO> bookCategoryDOsToDeleteCategoryDTOs(List<VocCategoryDO> bookCategoryDO);
}
