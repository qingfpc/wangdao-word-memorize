package com.cskaoyan.wordmemorize.converter;

import com.cskaoyan.wordmemorize.dao.entity.VocPaySaleDO;
import com.cskaoyan.wordmemorize.dto.admin.PageDTO;
import com.cskaoyan.wordmemorize.dto.app.PayOrderDTO;
import com.cskaoyan.wordmemorize.dto.app.VocPaySaleDTO;
import com.cskaoyan.wordmemorize.request.VocPaySaleCommand;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PayConverter {

    VocPaySaleDO vocPaySaleCommand2DO(VocPaySaleCommand command);

    @Mapping(source = "itemName", target = "subject")
    @Mapping(source = "payPrice", target = "totalAmount")
    PayOrderDTO vocPaySaleDO2PayOrderDTO(VocPaySaleDO vocPaySaleDO);


    @Mapping(source = "createTime", target = "saleTime")
    VocPaySaleDTO vocPaySaleDO2DTO(VocPaySaleDO vocPaySaleDO);

    @Mapping(source = "list", target = "pageList")
    @Mapping(source = "total", target = "totalCount")
    PageDTO<VocPaySaleDTO> vocPaySalePage2PageDTO(List<VocPaySaleDO> list, Long total);

}
