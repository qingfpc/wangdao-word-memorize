package com.cskaoyan.wordmemorize.converter;

import com.cskaoyan.wordmemorize.dao.entity.TmpDO;
import com.cskaoyan.wordmemorize.dao.entity.VocDO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TmpConverter {


    TmpDO test(VocDO vocDO);
}
