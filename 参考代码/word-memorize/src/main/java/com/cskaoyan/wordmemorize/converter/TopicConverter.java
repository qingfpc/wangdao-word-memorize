package com.cskaoyan.wordmemorize.converter;

import com.cskaoyan.wordmemorize.dao.entity.ReviewSubTopicDO;
import com.cskaoyan.wordmemorize.dao.entity.ReviewTopicDO;
import com.cskaoyan.wordmemorize.dao.entity.TopicOptionDO;
import com.cskaoyan.wordmemorize.dto.admin.*;
import com.cskaoyan.wordmemorize.dto.app.UserReviewSingleChoiceDTO;
import com.cskaoyan.wordmemorize.request.TopicCommand;
import com.cskaoyan.wordmemorize.request.ReviewSubTopicCommand;
import com.cskaoyan.wordmemorize.request.TopicOptionCommand;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TopicConverter {

    @Mapping(source = "topicList", target = "pageList")
    @Mapping(source = "total", target = "totalCount")
    PageDTO<ReviewTopicDTO> topicDOPage2PageDTO(List<ReviewTopicDO> topicList, Long total);


    ReviewTopicDO command2TopicDO(TopicCommand command);

    ReviewSubTopicDO command2ReviewSubTopicDO(ReviewSubTopicCommand command);
    List<TopicOptionDO> commands2TopicDOs(List<TopicCommand> commands);
    TopicOptionDO reviewSubTopicCommand2TopicDO(TopicOptionCommand command);


    ReviewTopicDTO reviewTopicDO2DTO(ReviewTopicDO reviewTopicDO);

    List<ReviewTopicDTO> reviewTopicDOs2DTOs(List<ReviewTopicDO> reviewTopicDOs);
    List<ReviewSubTopicDTO> reviewSubTopicDOs2DTOs(List<ReviewSubTopicDO> reviewTopicDO);

    List<ReviewSubTopicDO> commands2ReviewSubTopicDOs(List<ReviewSubTopicCommand> reviewTopicDO);

    TopicOptionDTO topicOptionDO2DTO(TopicOptionDO topicOptionDO);
    List<TopicOptionDTO> topicOptionDOs2DTOs(List<TopicOptionDO> topicOptionDO);

    UserReviewSingleChoiceDTO topicDO2UserReviewSingleChoiceDTO(ReviewTopicDO reviewTopicDO);

    List<UserReviewSingleChoiceDTO> topicDOs2UserReviewSingleChoiceDTOs(List<ReviewTopicDO> reviewTopicDOs);


    @Mapping(target = "topicType", constant = "1")
    @Mapping(source = "vocId", target = "vocId")
    ReviewTopicDO singleTopicExcelDTO2DO(SingleTopicExcelDTO singleTopicExcelDTO, Long vocId);

    DeleteTopicOperateDTO topicDO2DeleteTopicDTO(ReviewTopicDO reviewTopicDO);


    @Mapping(source = "id", target = "subTopicId")
    @Mapping(source = "vocabulary", target = "vocName")
    @Mapping(source = "topicItem", target = "subTopic")
    DeleteSubTopicOperateDTO toDeleteSubTopicDTO(ReviewSubTopicDO reviewSubTopicDO);

    @Mapping(source = "topicOptionDO.id", target = "optionId")
    @Mapping(source = "topicOptionDO.tid", target = "tid")
    @Mapping(source = "topicOptionDO.item", target = "option")
    @Mapping(source = "topic", target = "topic")
    DeleteTopicOptionOperateDTO toDeleteTopicOptionDTO(TopicOptionDO topicOptionDO, String topic);
}
