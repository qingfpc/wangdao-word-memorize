package com.cskaoyan.wordmemorize.dto.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "删除权限操作日志")
public class DeletePermissionOperateDTO {

    @Schema(description = "权限ID")
    Long id;

    @Schema(description = "权限名称")
    String name;

    @Schema(description = "权限类型")
    String permissionType;

    @Schema(description = "权限别名")
    String permissionAlias;
}
