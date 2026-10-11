package com.cskaoyan.wordmemorize.dto.admin;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "删除角色操作日志")
public class DeleteRoleOperateDTO {

    @Schema(description = "角色id")
    Long id;

    @Schema(description = "角色名称")
    String name;
}
