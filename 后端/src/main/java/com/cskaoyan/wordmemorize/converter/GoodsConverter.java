package com.cskaoyan.wordmemorize.converter;

import com.cskaoyan.wordmemorize.dao.entity.GoodsDO;
import com.cskaoyan.wordmemorize.dto.admin.DeleteGoodsOperateDTO;
import com.cskaoyan.wordmemorize.dto.admin.GoodsDTO;
import com.cskaoyan.wordmemorize.dto.admin.PageDTO;
import com.cskaoyan.wordmemorize.request.GoodsCommand;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Mapper(componentModel = "spring")
public interface GoodsConverter {


    @Mapping(source = "goodsList", target = "pageList")
    @Mapping(source = "total", target = "totalCount")
    PageDTO<GoodsDTO> goodsPage2PageDTO(List<GoodsDO> goodsList, Long total);

    GoodsDO command2GoodsDO(GoodsCommand command);


    DeleteGoodsOperateDTO goodsDO2DeleteGoodsDTO(GoodsDO goodsDO);

    List<DeleteGoodsOperateDTO> goodsDOs2DeleteGoodsDTOs(List<GoodsDO> goodsDO);
}
