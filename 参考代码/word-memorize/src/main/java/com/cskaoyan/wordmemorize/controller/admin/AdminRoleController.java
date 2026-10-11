package com.cskaoyan.wordmemorize.controller.admin;

import com.cskaoyan.wordmemorize.auth.SaAdminCheckPermission;
import com.cskaoyan.wordmemorize.common.result.Result;
import com.cskaoyan.wordmemorize.dto.admin.PermissionDTO;
import com.cskaoyan.wordmemorize.dto.admin.PageDTO;
import com.cskaoyan.wordmemorize.dto.admin.RoleDTO;
import com.cskaoyan.wordmemorize.dto.admin.RolePermissionDTO;
import com.cskaoyan.wordmemorize.request.PermissionCommand;
import com.cskaoyan.wordmemorize.request.PageRequest;
import com.cskaoyan.wordmemorize.request.RoleCommand;
import com.cskaoyan.wordmemorize.request.RolePermissionCommand;
import com.cskaoyan.wordmemorize.service.RolePermissionService;
//import org.apache.shiro.authz.annotation.RequiresPermissions;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Tag(name = "后台管理-账号权限管理-角色管理")
@RestController
@RequestMapping("admin")
public class AdminRoleController {

    @Resource
    RolePermissionService rolePermissionService;

    @Operation(summary = "**获取账号角色列表",
            description = "主要用于账号权限管理-角色管理",
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
    @GetMapping("role/list")
    @SaAdminCheckPermission("admin:role:search")
    public Result<PageDTO<RoleDTO>> getRoles(PageRequest pageRequest) {
        PageDTO<RoleDTO> roleList = rolePermissionService.getRoleList(pageRequest);
        return Result.ok(roleList);
    }

    @Operation(summary = "*添加角色",
            description = "主要用于账号权限管理-角色管理",
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
    @PostMapping("role")
    @SaAdminCheckPermission("admin:role:add")
    public Result addRole(@RequestBody RoleCommand command) {
        rolePermissionService.addRole(command);
        return Result.ok();
    }

    @Operation(summary = "*修改账号角色",
            description = "主要用于账号权限管理-角色管理",
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
    @PutMapping("role")
    @SaAdminCheckPermission("admin:role:update")
    public Result updateRole(@RequestBody RoleCommand command) {
        rolePermissionService.updateRole(command);
        return Result.ok();
    }

    @Operation(summary = "*删除账号角色",
            description = "主要用于账号权限管理-角色管理",
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
    @DeleteMapping("role/{roleId}")
    @SaAdminCheckPermission("admin:role:delete")
    public Result deleteRole(@PathVariable Long roleId) {
        rolePermissionService.deleteRole(roleId);
        return Result.ok();
    }

    @Operation(summary = "***账号角色授权",
            description = "主要用于账号权限管理-角色管理",
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
    @PutMapping("role/permission")
    @SaAdminCheckPermission("admin:role:authorize")
    public Result roleAuthorize(@RequestBody RolePermissionCommand command) {
        rolePermissionService.updateRolePermission(command);
        return Result.ok();
    }

    @Operation(summary = "***获取角色对应的授权信息(权限树)",
            description = "主要用于账号权限管理-角色管理",
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
    @GetMapping("{roleId}/permission/list")
    @SaAdminCheckPermission("admin:role:authorize")
    public Result<RolePermissionDTO> getRolePermissions(@PathVariable @Parameter(description = "角色id") Long roleId) {
        RolePermissionDTO rolePermissionList = rolePermissionService.getRolePermissionList(roleId);
        return Result.ok(rolePermissionList);
    }

    @Operation(summary = "***在给角色授权时，删除指定角色绑定的指定权限",
            description = "主要用于账号权限管理-角色管理",
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
    @DeleteMapping("/permission/{roleId}/{permissionId}")
    @SaAdminCheckPermission("admin:role:authorize")
    public Result deleteRolePermissions(@PathVariable("roleId") @Parameter(description = "角色id") Long roleId,
                                                        @PathVariable("permissionId") @Parameter(description = "角色id") Long permissionId                                     ) {
        rolePermissionService.deleteRolePermission(roleId, permissionId);
        return Result.ok();
    }

}
