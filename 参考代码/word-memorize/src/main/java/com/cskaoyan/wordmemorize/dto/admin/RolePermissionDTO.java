package com.cskaoyan.wordmemorize.dto.admin;

import com.cskaoyan.wordmemorize.dto.admin.PermissionDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "角色授权信息")
public class RolePermissionDTO {
    @Schema(description = "全部权限")
    List<PermissionDTO> systemPermissions;
    @Schema(description = "角色已授权的权限")
    List<String> assignedPermissions;
}
