package com.cskaoyan.wordmemorize.converter;

import com.cskaoyan.wordmemorize.common.util.DateUtil;
import org.mapstruct.Mapper;

import java.time.LocalDate;

@Mapper(componentModel = "spring")
public interface LoginConverter {


    default LocalDate mapDate(String dateStr) {
        return DateUtil.getDate(dateStr);
    }
}
