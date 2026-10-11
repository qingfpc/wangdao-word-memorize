package com.cskaoyan.wordmemorize.controller.admin;


import com.cskaoyan.wordmemorize.common.aspect.operate.OperateLog;
import com.cskaoyan.wordmemorize.common.aspect.operate.OperateLogConstant;
import com.cskaoyan.wordmemorize.common.result.Result;
import com.cskaoyan.wordmemorize.dto.admin.PageDTO;
import com.cskaoyan.wordmemorize.dto.admin.RoleDTO;
import com.cskaoyan.wordmemorize.dto.admin.RolePermissionDTO;
import com.cskaoyan.wordmemorize.request.PageRequest;
import com.cskaoyan.wordmemorize.request.RoleCommand;
import com.cskaoyan.wordmemorize.request.RolePermissionCommand;
import com.cskaoyan.wordmemorize.service.RolePermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("admin")
public class AdminRoleController {


    @Autowired
    RolePermissionService rolePermissionService;

    //添加角色
    @OperateLog(dateType = "角色", operateType = OperateLogConstant.OPERATE_ADD)
    @PostMapping("role")
    public Result addRole(@RequestBody RoleCommand roleCommand) {
        rolePermissionService.addRole(roleCommand);
        return Result.ok("添加角色成功");
    }

    //修改账号角色
    @OperateLog(dateType = "修改账号角色", operateType = OperateLogConstant.OPERATE_UPDATE)
    @PutMapping("role")
    public Result updateRole(@RequestBody RoleCommand roleCommand) {
        rolePermissionService.updateRole(roleCommand);
        return Result.ok("修改账号角色成功");
    }

    //账号角色授权
    @OperateLog(dateType = "账号角色授权", operateType = OperateLogConstant.OPERATE_UPDATE_STATUS)
    @PutMapping("role/permission")
    public Result updateRolePermission(@RequestBody RolePermissionCommand rolePermissionCommand) {
        rolePermissionService.updateRolePermission(rolePermissionCommand);
        return Result.ok("账号角色授权成功");
    }

    //获取角色对应的授权信息(授权树)
    @GetMapping("{roleId}/permission/list")
    public Result<RolePermissionDTO> getRolePermissionList(@PathVariable(name = "roleId") Long roleId) {
        RolePermissionDTO rolePermissionList = rolePermissionService.getRolePermissionList(roleId);
        return Result.ok(rolePermissionList);
    }

    //获取账号角色列表
    @GetMapping("role/list")
    public Result<PageDTO<RoleDTO>> getRoleList(PageRequest pageRequest) {
        PageDTO<RoleDTO> roleList = rolePermissionService.getRoleList(pageRequest);
        return Result.ok(roleList);
    }

    //删除账号角色
    @OperateLog(dateType = "删除账号角色", operateType = OperateLogConstant.OPERATE_SINGLE_DELETE)
    @DeleteMapping("role/{roleId}")
    public Result deleteRole(@PathVariable(name = "roleId") Long roleId) {
        rolePermissionService.deleteRole(roleId);
        return Result.ok("删除账号角色成功");
    }

    //在给角色授权时，删除指定角色绑定的指定权限
    @OperateLog(dateType = "删除指定角色绑定的指定权限", operateType = OperateLogConstant.OPERATE_SINGLE_DELETE)
    @DeleteMapping("permission/{roleId}/{permissionId}")
    public Result<RolePermissionDTO> deleteRolePermissionController(
            @PathVariable Long roleId,
            @PathVariable Long permissionId) {
        // 删除操作
        rolePermissionService.deleteRolePermission(roleId, permissionId);
        // 获取最新的角色权限授权树
        RolePermissionDTO rolePermissionList = rolePermissionService.getRolePermissionList(roleId);
        return Result.ok(rolePermissionList);
    }
}
