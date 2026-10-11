package com.cskaoyan.wordmemorize.converter;

import com.cskaoyan.wordmemorize.dao.entity.*;
import com.cskaoyan.wordmemorize.dto.admin.*;
import com.cskaoyan.wordmemorize.dto.app.*;
import com.cskaoyan.wordmemorize.request.*;
import org.jetbrains.annotations.TestOnly;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Service;

import java.util.List;

@Mapper(componentModel = "spring", uses = VocConverter.class)
@Service
public interface UserPrivilegeConverter {

    UserFavoriteDO userFavoriteCommand2DO(UserFavoriteCommand command);

    List<UserFavoriteDO> userFavoriteDOs2DTOs(List<UserFavoriteCommand> commands);


    @Mapping(source = "list", target = "pageList")
    @Mapping(source = "total", target = "totalCount")
    PageDTO<UserFavoriteDTO> userFavoriteDOPage2PageDTO(List<UserFavoriteDO> list, Long total);

    UserVocRevDO userVocRevCommand2DO(UserVocRevCommand command);


    LevelPrivilegeDTO levelPrivilegeDO2DTO(LevelPrivilegeDO levelPrivilegeDO);

    UserVocExampleSentenceDO userVocExampleSentenceCommand2DO(UserVocExampleSentenceCommand command);

    UserVocSentenceDTO userVocSentenceDO2DTO(UserVocExampleSentenceDO userVocExampleSentenceDO);

    List<UserVocSentenceDTO> userVocSentenceDOs2DTOs(List<UserVocExampleSentenceDO> userVocExampleSentenceDOs);

    @Mapping(source = "list", target = "pageList")
    @Mapping(source = "total", target = "totalCount")
    PageDTO<DisplayUserVocSentenceDTO> displayUserVocSentenceDTOList2PageDTO(List<DisplayUserVocSentenceDTO> list, Long total);


    VocMeaningDTO vocMeaningDO2DTO(VocMeaningDO vocMeaningDO);
    List<VocMeaningDTO> vocMeaningDOs2DTOs(List<VocMeaningDO> vocMeaningDO);
    ReviewSubTopicDTO reviewSubTopicDO2DTO(ReviewSubTopicDO reviewSubTopicDO);

    @TestOnly
    List<ReviewSubTopicDTO> reviewSubTopicDOs2DTOs(List<ReviewSubTopicDO> reviewSubTopicDO);

    UserReviewSingleChoiceDTO reviewTopicDO2DTO(ReviewTopicDO reviewTopicDO);

    List<UserReviewSingleChoiceDTO> reviewTopicDOs2DTOs(List<ReviewTopicDO> reviewTopicDO);

    TopicOptionDTO reviewTopicOptionDO2DTO(TopicOptionDO reviewTopicOptionDO);

    @TestOnly
    List<TopicOptionDTO> reviewTopicOptionDOs2DTOs(List<TopicOptionDO> reviewTopicOptionDO);
    UserVocAISituationDTO userVocAISituationDO2DTO(UserVocAISituationDO userVocAISituationDO);

    List<UserVocAISituationDTO> userVocAISituationDOs2DTOs(List<UserVocAISituationDO> userVocAISituationDOs);
    UserVocAISituationOptionDTO userVocAISituationOptionDO2DTO(UserVocAISituationOptionDO userVocAISituationOptionDO);
    List<UserVocAISituationOptionDTO> userVocAISituationOptionDOs2DTOs(List<UserVocAISituationOptionDO> userVocAISituationOptionDO);

}
