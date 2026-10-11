package com.cskaoyan.wordmemorize.dto.admin;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 *  封装删除等级操作日志
 */
@Data
@Schema(description = "删除等级操作日志")
public class DeleteLevelOperateDTO {

    @Schema(description = "等级id")
    Long id;
    @Schema(description = "等级名称")
    String levelName;
}
