package com.cskaoyan.wordmemorize.converter;

import com.cskaoyan.wordmemorize.dao.entity.*;
import com.cskaoyan.wordmemorize.dto.admin.*;
import com.cskaoyan.wordmemorize.dto.app.DisplayBookVocDTO;
import com.cskaoyan.wordmemorize.dto.app.DisplayChapterDTO;
import com.cskaoyan.wordmemorize.dto.app.DisplayVocInterpretationDTO;
import com.cskaoyan.wordmemorize.dto.app.UserBookStatisticsDTO;
import com.cskaoyan.wordmemorize.request.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Mapper(componentModel = "spring")
public interface VocConverter {

    VocDTO vocDO2DTO(VocDO vocDO);

    @Mapping(source = "vocDOs", target = "pageList")
    @Mapping(source = "total", target = "totalCount")
    PageDTO<VocDTO> vocList2Page(List<VocDO> vocDOs, Long total);

    List<VocChapBookDTO>  vocChapBookDOs2DTOs(List<VocChapBookDO> vocChapBookDOs);



    VocDO command2VocDO(VocCommand command);

    VocChapBookDO command2VocChapBookDO(VocCommand vocCommand);

    VocMeaningDO command2VocMeaningDO(VocMeaningCommand command);

    List<VocMeaningDO> commands2VocMeaningDOs(List<VocMeaningCommand> command);

    List<ExampleSentenceDO> commands2ExampleSentenceDOs(List<ExampleSentenceCommand> commands);

    List<ExampleSentenceDTO> exampleSentenceDO2DTO(List<ExampleSentenceDO> exampleSentenceDO);

    List<VocChapBookDO> commands2VocChapBookDOs(List<VocChapBookCommand> commands);

    @Mapping(source = "interContent", target = "content")
    DisplayVocInterpretationDTO vocMeaningDO2DisplayVocInterpretationDTO(VocMeaningDO vocMeaningDO);
//    DisplayBookVocDTO vocDo2DisplayChapterVocDTO(VocDO vocDO);
    List<DisplayBookVocDTO> vocDos2DisplayChapterVocDTOs(List<VocDO> vocDOs);


    @Mapping(source = "vocDOs", target = "pageList")
    @Mapping(source = "total", target = "totalCount")
    PageDTO<DisplayBookVocDTO> displayBookVocList2Page(List<VocDO> vocDOs, Long total);

    @Mapping(source = "vocs", target = "displayVocs")
    DisplayChapterDTO vocChapterDO2displayChapterDTO(VocChapterDO vocChapterDO);

    List<DisplayChapterDTO> vocChapterDOs2displayChapterDTOs(List<VocChapterDO> vocChapterDOs);

    @Mapping(source = "vocChapterDOs", target = "pageList")
    @Mapping(source = "total", target = "totalCount")
    PageDTO<DisplayChapterDTO> displayChapterVocList2Page(List<VocChapterDO> vocChapterDOs, Long total);


    UserBookStatisticsDTO userBookStatisticsDO2DTO(UserBookStatisticsDO userBookStatisticsDO);

    @Mapping(source = "interpretations", target = "interpretations", ignore = true)
    VocDO vocExcelDTO2VocDO(VocExcelDTO vocExcelDTO);

    DeleteVocRelationOperateDTO toDeleteVocRelationOperateDTO(VocChapBookDO vocChapBookDO);

    @Mapping(source = "vocMeaningDO.id", target = "id")
    @Mapping(source = "vocMeaningDO.type", target = "type")
    @Mapping(source = "vocMeaningDO.vocId", target = "vocId")
    @Mapping(source = "vocMeaningDO.interContent", target = "interContent")
    @Mapping(source = "content", target = "vocName")
    DeleteInterpretationOperateDTO toDeleteInterpretationOperateDTO(VocMeaningDO vocMeaningDO, String content);

    @Mapping(source = "sentenceDO.id", target = "id")
    @Mapping(source = "sentenceDO.content", target = "sentence")
    @Mapping(source = "sentenceDO.vocId", target = "vocId")
    @Mapping(source = "content", target = "vocName")
    DeleteVocExampleOperateDTO toDeleteExampleOperateDTO(ExampleSentenceDO sentenceDO, String content);

    DeleteVocOperateDTO toDeleteVocOperateDTO(VocDO vocDO);
    List<DeleteVocOperateDTO> toDeleteVocOperateDTOs(List<VocDO> vocDOs);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "content", target = "content")
    DisplayBookVocDTO vocDOToDisplayBookVocDTO(VocDO vocDO);

//    List<DisplayBookVocDTO> vocDOsToDisplayBookVocDTOs(List<VocDO> vocDOs);

//    @Mapping(source = "id", target = "id")
//    @Mapping(source = "type", target = "type")
//    @Mapping(source = "interContent", target = "interContent")
//    DisplayVocInterpretationDTO vocMeaningDOToDisplayVocInterpretationDTO(VocMeaningDO vocMeaningDO);

    List<DisplayVocInterpretationDTO> vocMeaningDOsToDisplayVocInterpretationDTOs(List<VocMeaningDO> vocMeaningDOs);

    // 添加将 VocMeaningDO 列表转换为 VocMeaningDTO 列表的方法
    List<VocMeaningDTO> vocMeaningDOs2DTOs(List<VocMeaningDO> vocMeaningDOs);


}
