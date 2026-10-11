package com.cskaoyan.wordmemorize.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "角色授权请求体")
public class RolePermissionCommand {
    @Schema(description = "角色id")
    String roleId;
    @Schema(description = "角色对应的权限集合(权限id集合)")
    List<String> permissions;
}
