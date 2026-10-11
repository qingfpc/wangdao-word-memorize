package com.cskaoyan.wordmemorize.converter;

import com.cskaoyan.wordmemorize.dao.entity.VocBookDO;
import com.cskaoyan.wordmemorize.dto.admin.DeleteBookOperateDTO;
import com.cskaoyan.wordmemorize.dto.admin.PageDTO;
import com.cskaoyan.wordmemorize.dto.admin.VocBookDTO;
import com.cskaoyan.wordmemorize.request.VocBookCommand;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface VocBookConverter {

   VocBookDTO vocBookDO2DTO(VocBookDO vocBookDO);

   @Mapping(source = "vocBookDOs", target = "pageList")
   @Mapping(source = "total", target = "totalCount")
   PageDTO<VocBookDTO> vocBookList2Page(List<VocBookDO> vocBookDOs, Long total);

   @Mapping(target = "id", ignore = true)
   VocBookDO vocBookCommand2VocBookDO(VocBookCommand command);


   DeleteBookOperateDTO vocBookDO2DeleteBookDTO(VocBookDO vocBookDO);

   List<DeleteBookOperateDTO> vocBookDOs2DeleteBookDTOs(List<VocBookDO> vocBookDO);
}
