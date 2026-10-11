package com.cskaoyan.wordmemorize.converter;


import com.cskaoyan.wordmemorize.dao.entity.*;
import com.cskaoyan.wordmemorize.dto.admin.PageDTO;
import com.cskaoyan.wordmemorize.dto.app.*;
import com.cskaoyan.wordmemorize.request.UserCheckinCommand;
import com.cskaoyan.wordmemorize.request.UserRevLogCommand;
import com.cskaoyan.wordmemorize.request.UserReviewStatisticsCommand;
import com.cskaoyan.wordmemorize.request.UserVocRevCommand;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.sql.Timestamp;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ReviewConverter {

    @Mapping(source = "userCheckinDOs", target = "pageList")
    @Mapping(source = "total", target = "totalCount")
    PageDTO<UserCheckinDTO> userCheckinPage2PageDTO(List<UserCheckinDO> userCheckinDOs, Long total);

    UserCheckinDTO userCheckinDO2DTO(UserCheckinDO userCheckinDO);

    List<UserCheckinDO> userCheckinCommands2UserCheckinDOs(List<UserCheckinCommand> commands);

    UserCheckinDO userCheckinCommand2DO(UserCheckinCommand command);

    List<UserCheckinDO> userCheckinCommands2DOs(List<UserCheckinCommand> commands);

    UserCheckinStatisticsDTO userCheckinStatisticsDO2DTO(UserCheckinStatisticsDO userCheckinStatisticsDO);

    UserRevLogDO userRevLogCommand2DO(UserRevLogCommand command);

    List<UserRevLogDO> userRevLogsCommand2DOs(List<UserRevLogCommand> commands);

    @Mapping(source = "userRevStatisticsDos", target = "pageList")
    @Mapping(source = "total", target = "totalCount")
    PageDTO<UserReviewStatisticsDTO> userRevStatistcsPage2PageDTO(List<UserRevStatisticsDO> userRevStatisticsDos, Long total);

    UserRevStatisticsDO userRevStatisticsCommand2DO(UserReviewStatisticsCommand command);

    List<UserRevStatisticsDO> userRevStatisticsCommands2DOs(List<UserReviewStatisticsCommand> commands);

    UserVocRevDO userVocRevCommand2DO(UserVocRevCommand command);
    List<UserVocRevDO> userVocRevCommands2DOs(List<UserVocRevCommand> command);

    UserVocRevDTO userVocRevDO2DTO(UserVocRevDO userVocRevDO);
    List<UserVocRevDTO> userVocRevDOs2DTOs(List<UserVocRevDO> userVocRevDO);

    @Mapping(source = "userRevStatisticsDos", target = "pageList")
    @Mapping(source = "total", target = "totalCount")
    PageDTO<UserVocRevDTO> userVocRevPage2PageDTO(List<UserVocRevDO> userRevStatisticsDos, Long total);

    @Mapping(source = "list", target = "pageList")
    @Mapping(source = "total", target = "totalCount")
    PageDTO<UserRevLogDTO> userRevLogPage2PageDTO(List<UserRevLogDO> list, Long total);

    default Long transferTimeStampToLong(Timestamp timestamp) {
        return timestamp.getTime();
    }
    default Timestamp transferLongToTimeStamp(Long timeStamp) {
        return new Timestamp(timeStamp);
    }

}
