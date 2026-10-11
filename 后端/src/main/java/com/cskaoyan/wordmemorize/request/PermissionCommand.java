package com.cskaoyan.wordmemorize.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "账号权限请求体")
public class PermissionCommand {
    @Schema(description = "id")
    String id;
    @Schema(description = "父权限id, 如果没有父权限，则该值为-1")
    String parentPermission;
    @Schema(description = "权限名称")
    String name;
    @Schema(description = "权限别名(主要方便人理解)")
    String permissionAlias;
    @Schema(description = "权限码")
    String permissionCode;
    @Schema(description = "权限类型：权限类型: 1. 页面权限(针对页面展示) 2. 访问权限(针对具体的请求url)")
    Integer permissionType;
    @Schema(description = "请求描述")
    String desc;
    @Schema(description = "请求路径")
    String path;
    @Schema(description = "请求方式: GET POST PUT DELETE等")
    String accessType;
    @Schema(description = "子权限集合")
    List<PermissionCommand> children;
}
