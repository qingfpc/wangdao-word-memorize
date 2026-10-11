package com.cskaoyan.wordmemorize.dto.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


/**
 *  封装删除商品操作日志
 */
@Data
@Schema(description = "删除商品操作日志")
public class DeleteGoodsOperateDTO {

    /**
     * 商品ID
     */
    @Schema(description = "商品ID")
    Long id;
    /**
     * 商品名称
     */
    @Schema(description = "商品名称")
    String name;
}
