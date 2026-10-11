package com.cskaoyan.wordmemorize.controller.admin;

import com.cskaoyan.wordmemorize.auth.SaAdminCheckPermission;
import com.cskaoyan.wordmemorize.common.result.Result;
import com.cskaoyan.wordmemorize.dto.admin.PageDTO;
import com.cskaoyan.wordmemorize.dto.admin.PermissionDTO;
import com.cskaoyan.wordmemorize.dto.admin.RoleDTO;
import com.cskaoyan.wordmemorize.dto.admin.RolePermissionDTO;
import com.cskaoyan.wordmemorize.request.PageRequest;
import com.cskaoyan.wordmemorize.request.PermissionCommand;
import com.cskaoyan.wordmemorize.request.RoleCommand;
import com.cskaoyan.wordmemorize.request.RolePermissionCommand;
import com.cskaoyan.wordmemorize.service.RolePermissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "后台管理-账号权限管理-权限管理")
@RestController
@RequestMapping("admin")
public class AdminPermissionController {

    @Resource
    RolePermissionService rolePermissionService;

    @Operation(summary = "*添加账号权限",
            description = "主要用于账号权限管理-权限管理",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "登出成功"

                    ),
                    @ApiResponse(responseCode = "500",
                            description = "请求失败"
                    )
            }

    )
    @PostMapping("permission")
    @SaAdminCheckPermission("admin:permission:add")
    public Result addPermissions( @RequestBody PermissionCommand command) {
        rolePermissionService.savePermissions(command);
        return Result.ok();
    }

    @Operation(summary = "*修改账号权限",
            description = "主要用于账号权限管理-权限管理",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "登出成功"

                    ),
                    @ApiResponse(responseCode = "500",
                            description = "请求失败"
                    )
            }

    )
    @PutMapping("permission")
    @SaAdminCheckPermission("admin:permission:update")
    public Result updatePermission(@RequestBody PermissionCommand command) {
        rolePermissionService.updatePermission(command);
        return Result.ok();
    }

    @Operation(summary = "****账号权限列表",
            description = "主要用于账号权限管理-权限管理",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "登出成功"

                    ),
                    @ApiResponse(responseCode = "500",
                            description = "请求失败"
                    )
            }

    )
    @GetMapping("permission/list")
    @SaAdminCheckPermission("admin:permission:search")
    public Result<List<PermissionDTO>> getPermissions() {
        List<PermissionDTO> permissions = rolePermissionService.getPermissions();
        return Result.ok(permissions);
    }

    @Operation(summary = "***删除指定账号权限",
            description = "主要用于账号权限管理-权限管理",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "登出成功"

                    ),
                    @ApiResponse(responseCode = "500",
                            description = "请求失败"
                    )
            }

    )
    @DeleteMapping("permission/{permissionId}")
    @SaAdminCheckPermission("admin:permission:delete")
    public Result deletePermission(@PathVariable Long permissionId) {
        rolePermissionService.deletePermission(permissionId);
        return Result.ok();
    }


}
