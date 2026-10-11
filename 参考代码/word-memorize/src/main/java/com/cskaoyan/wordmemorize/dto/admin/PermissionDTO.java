package com.cskaoyan.wordmemorize.dto.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "权限信息")
public class PermissionDTO {
    @Schema(description = "权限id")
    String id;
    @Schema(description = "父权限id")
    String parentPermission;
    @Schema(description = "权限名称")
    String name;
    @Schema(description = "权限别名")
    String permissionAlias;
    @Schema(description = "权限类型: 1页面权限 2访问权限")
    Integer permissionType;
    @Schema(description = "权限码")
    String permissionCode;
    @Schema(description = "权限描述")
    String desc;
    @Schema(description = "访问路径")
    String path;
    @Schema(description = "GET POST PUT DELETE这样的访问方式")
    String accessType;
    @Schema(description = "子权限集合")
    List<PermissionDTO> children;
}
