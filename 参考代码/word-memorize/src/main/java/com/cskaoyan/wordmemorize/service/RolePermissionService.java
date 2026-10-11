package com.cskaoyan.wordmemorize.service;

import com.cskaoyan.wordmemorize.dto.admin.*;
import com.cskaoyan.wordmemorize.request.PermissionCommand;
import com.cskaoyan.wordmemorize.request.PageRequest;
import com.cskaoyan.wordmemorize.request.RoleCommand;
import com.cskaoyan.wordmemorize.request.RolePermissionCommand;

import java.util.List;

public interface RolePermissionService {

    //  保存权限树(后台账号权限管理-权限管理)
    void savePermissions(PermissionCommand command);

    // 获取权限树(后台账号权限管理-权限管理)
    List<PermissionDTO> getPermissions();

    // 删除指定权限(后台账号权限管理-权限管理)
    DeletePermissionOperateDTO deletePermission(Long permissionId);

    // 更新权限树(后台账号权限管理-权限管理)
    void updatePermission(PermissionCommand command);
    // 分页获取角色列表(后台账号权限管理-角色管理)
    PageDTO<RoleDTO> getRoleList(PageRequest pageRequest);
    // 添加角色(后台账号权限管理-角色管理)
    void addRole(RoleCommand command);
    // 更新角色(后台账号权限管理-角色管理)
    void updateRole(RoleCommand roleCommand);
    // 更新角色权限关系(后台账号权限管理-角色管理)
    void updateRolePermission(RolePermissionCommand command);
    // 获取角色权限树(后台账号权限管理-角色管理点击授权时返回的角色授权树)
    RolePermissionDTO getRolePermissionList(Long roleId);
    // 删除角色(后台账号权限管理-角色管理)
    DeleteRoleOperateDTO deleteRole(Long roleId);
    // 删除角色(后台账号权限管理-角色管理)
    DeleteRolePermissionOperateDTO deleteRolePermission(Long roleId, Long permissionId);

}
