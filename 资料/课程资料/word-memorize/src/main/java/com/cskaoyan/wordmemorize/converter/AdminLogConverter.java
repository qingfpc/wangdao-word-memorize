package com.cskaoyan.wordmemorize.converter;

import com.cskaoyan.wordmemorize.dao.entity.OperateLogDO;
import com.cskaoyan.wordmemorize.dto.admin.OperateLogDTO;
import com.cskaoyan.wordmemorize.dto.admin.PageDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Mapper(componentModel = "spring")
public interface AdminLogConverter {

    @Mapping(source = "operateLogList", target = "pageList")
    @Mapping(source = "total", target = "totalCount")
    PageDTO<OperateLogDTO> operationLogPage2PageDTO(List<OperateLogDO> operateLogList, Long total);

    OperateLogDTO operateLogDO2DTO(OperateLogDO operateLogDO);

    // 将timeStamp转化为对应的日期字符串
    default String transferTimeStampToString(Timestamp timestamp) {
        if (timestamp == null) {
            return null;
        }
        LocalDateTime localDateTime = timestamp.toLocalDateTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
       return localDateTime.format(formatter);
    }

    default Long transferTimeStampToLong(Timestamp timestamp) {
        return timestamp.getTime();
    }
}
