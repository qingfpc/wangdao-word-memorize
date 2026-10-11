package com.cskaoyan.wordmemorize.converter;

import com.cskaoyan.wordmemorize.dao.entity.VocChapterDO;
import com.cskaoyan.wordmemorize.dto.admin.DeleteChapterOperateDTO;
import com.cskaoyan.wordmemorize.dto.admin.PageDTO;
import com.cskaoyan.wordmemorize.dto.admin.VocChapterDTO;
import com.cskaoyan.wordmemorize.request.VocChapterCommand;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface VocChapterConverter {

    @Mapping(source = "chapterDOs", target = "pageList")
    @Mapping(source = "total", target = "totalCount")
    PageDTO<VocChapterDTO> vocChapterDOs2Page(List<VocChapterDO> chapterDOs, Long total);

    VocChapterDO vocChapterCommand2DO(VocChapterCommand command);

    DeleteChapterOperateDTO vocChapterDO2DeleteDTO(VocChapterDO chapterDO);
    List<DeleteChapterOperateDTO> vocChapterDOs2DeleteDTOs(List<VocChapterDO> chapterDO);
}
