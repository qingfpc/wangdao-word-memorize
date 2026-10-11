package com.cskaoyan.wordmemorize.dto.admin;

import lombok.Data;

@Data
public class DeleteRolePermissionOperateDTO {

    // 角色id
    Long roleId;

    // 角色名称
    String roleName;
    // 权限id
    Long permissionId;
    // 权限别名
    String permissionAlias;
}
