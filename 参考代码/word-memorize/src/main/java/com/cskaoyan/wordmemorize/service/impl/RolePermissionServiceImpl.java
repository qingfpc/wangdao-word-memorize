package com.cskaoyan.wordmemorize.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cskaoyan.wordmemorize.common.constant.ResultCodeEnum;
import com.cskaoyan.wordmemorize.common.exception.BusinessException;
import com.cskaoyan.wordmemorize.common.aspect.operate.OperateLog;
import com.cskaoyan.wordmemorize.converter.RolePermissionConverter;
import com.cskaoyan.wordmemorize.dao.entity.PermissionDO;
import com.cskaoyan.wordmemorize.dao.entity.RoleDO;
import com.cskaoyan.wordmemorize.dao.entity.RolePermissionDO;
import com.cskaoyan.wordmemorize.dao.mapper.PermissionMapper;
import com.cskaoyan.wordmemorize.dao.mapper.RoleMapper;
import com.cskaoyan.wordmemorize.dao.mapper.RolePermissionMapper;
import com.cskaoyan.wordmemorize.dto.admin.*;
import com.cskaoyan.wordmemorize.request.PermissionCommand;
import com.cskaoyan.wordmemorize.request.PageRequest;
import com.cskaoyan.wordmemorize.request.RoleCommand;
import com.cskaoyan.wordmemorize.request.RolePermissionCommand;
import com.cskaoyan.wordmemorize.service.RolePermissionService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RolePermissionServiceImpl implements RolePermissionService {

    @Resource
    PermissionMapper permissionMapper;

    @Resource
    RolePermissionConverter rolePermissionConverter;

    @Resource
    RoleMapper roleMapper;

    @Resource
    RolePermissionMapper rolePermissionMapper;


    @Override
    @OperateLog(dateType = "权限", operateType = 1)
    public void savePermissions(PermissionCommand command) {
        // 假设根节点的父节点id值是-1
        PermissionDO permissionDO = rolePermissionConverter.command2PermissionDO(command);
        permissionMapper.insert(permissionDO);
    }

    @Override
    public List<PermissionDTO> getPermissions() {
        List<PermissionDO> permissionDOS = permissionMapper.selectList(null);
        List<PermissionDTO> employeePermissionDTOs = rolePermissionConverter.employeePermissionDO2DTO(permissionDOS);
        return buildPermissionTree(employeePermissionDTOs);
    }

    @Override
    @OperateLog(dateType = "权限", operateType = 2)
    @Transactional
    public DeletePermissionOperateDTO deletePermission(Long permissionId) {

        // 先查询该权限的所有子权限
//        LambdaQueryWrapper<RolePermissionDO> adminQueryWrapper = new LambdaQueryWrapper<>();
//        adminQueryWrapper.eq(RolePermissionDO::getPermissionId, permissionId);

//        Long count = rolePermissionMapper.selectCount(adminQueryWrapper);
//        if (count > 0) {
//            throw new BusinessException(ResultCodeEnum.PERMISSION_CAN_NOT_DELETE);
//        }

        PermissionDO permissionDO = permissionMapper.selectById(permissionId);

        // 先删除该权限
        permissionMapper.deleteById(permissionId);

        // 查询该权限的所有子权限
        LambdaQueryWrapper<PermissionDO> permissionQueryWrapper
                = new LambdaQueryWrapper<>();
        permissionQueryWrapper.eq(PermissionDO::getParentPermission, permissionId)
                .select(PermissionDO::getId);

        List<Long> toDeleteIds = permissionMapper.selectList(permissionQueryWrapper).stream()
                .map(PermissionDO::getId).collect(Collectors.toList());

        for (int i = 0; i < toDeleteIds.size(); i++) {
            // 递归删除子权限
            deletePermission(toDeleteIds.get(i));
        }

        return rolePermissionConverter.toDeletePermissionOperateDTO(permissionDO);
    }

    @Override
    @OperateLog(dateType = "权限", operateType = 4)
    public void updatePermission(PermissionCommand command) {
        LambdaQueryWrapper<RolePermissionDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RolePermissionDO::getPermissionId, command.getId());

        PermissionDO permissionDO = rolePermissionConverter.command2PermissionDO(command);
        permissionMapper.updateById(permissionDO);
    }

    @Override
    public PageDTO<RoleDTO> getRoleList(PageRequest pageRequest) {
        LambdaQueryWrapper<RoleDO> roleDOLambdaQueryWrapper = new LambdaQueryWrapper<>();

        if (pageRequest.getPageNum() == null) {
            List<RoleDO> roleDOs = roleMapper.selectList(null);
            return  rolePermissionConverter.roleList2Page(roleDOs, (long)roleDOs.size());
        }

        if (StringUtils.hasText(pageRequest.getKeyword())) {
            roleDOLambdaQueryWrapper.like(RoleDO::getName, pageRequest.getKeyword());
        }
        Page<RoleDO> roleDOPage = new Page<>(pageRequest.getPageNum(), pageRequest.getCount());
        Page<RoleDO> rolePage = roleMapper.selectPage(roleDOPage, roleDOLambdaQueryWrapper);
        return rolePermissionConverter.roleList2Page(rolePage.getRecords(), rolePage.getTotal());
    }

    @Override
    @OperateLog(dateType = "角色", operateType = 1)
    public void addRole(RoleCommand command) {
        RoleDO roleDO = rolePermissionConverter.command2RoleDO(command);
        roleMapper.insert(roleDO);
    }

    @Override
    @OperateLog(dateType = "角色", operateType = 4)
    public void updateRole(RoleCommand roleCommand) {
        RoleDO roleDO = rolePermissionConverter.command2RoleDO(roleCommand);
        roleMapper.updateById(roleDO);
    }

    @Override
    @OperateLog(dateType = "角色权限", operateType = 4)
    public void updateRolePermission(RolePermissionCommand command) {
        // 查询出数据库当前roleId对应的permissionId集合
        LambdaQueryWrapper<RolePermissionDO> rolePermissionLambdaQueryWrapper = new LambdaQueryWrapper<>();
        Long roleId = Long.parseLong(command.getRoleId());
        rolePermissionLambdaQueryWrapper.eq(RolePermissionDO::getRoleId, roleId)
                .select(RolePermissionDO::getPermissionId);

        Set<Long> oldPermissionSet = rolePermissionMapper.selectList(rolePermissionLambdaQueryWrapper)
                .stream().map(RolePermissionDO::getPermissionId).collect(Collectors.toSet());

        // 查询数据库中不存在的permissionId集合(就是给角色新添加的权限)
        LambdaQueryWrapper<PermissionDO> permissionLambdaQueryWrapper = new LambdaQueryWrapper<>();
        permissionLambdaQueryWrapper.in(PermissionDO::getId, command.getPermissions())
              .select(PermissionDO::getId);
        List<Long> toInsert = command.getPermissions().stream().map(Long::parseLong).filter(permissionId -> !oldPermissionSet.contains(permissionId))
                .collect(Collectors.toList());

        // 保存
        for (int i = 0; i < toInsert.size(); i++) {
            RolePermissionDO rolePermissionDO = new RolePermissionDO();
            rolePermissionDO.setRoleId(roleId);
            rolePermissionDO.setPermissionId(toInsert.get(i));
            rolePermissionMapper.insert(rolePermissionDO);
        }
    }


    @Override
    public RolePermissionDTO getRolePermissionList(Long roleId) {
        // 先获取所有的系统权限
        List<PermissionDTO> systemPermissions = getPermissions();

        // 在查询选中权限id集合
        LambdaQueryWrapper<RolePermissionDO> rolePermissionLambdaQueryWrapper = new LambdaQueryWrapper<>();
        rolePermissionLambdaQueryWrapper
                .eq(RolePermissionDO::getRoleId, roleId)
                .select(RolePermissionDO::getPermissionId);

        List<String> permissionIds = rolePermissionMapper
                .selectList(rolePermissionLambdaQueryWrapper)
                .stream()
                .map(rolePermissionDO -> rolePermissionDO.getPermissionId() + "")
                .collect(Collectors.toList());

        List<String> leafPermissionIds = new ArrayList<>();
        if (!CollectionUtils.isEmpty(permissionIds)) {
            leafPermissionIds = permissionMapper.selectBatchIds(permissionIds).stream()
                    .filter(permissionDO -> permissionDO.getPermissionType() == 2)
                    .map(permissionDO -> permissionDO.getId() + "").collect(Collectors.toList());
        }

        return new RolePermissionDTO(systemPermissions, leafPermissionIds);
    }

    @Override
    @OperateLog(dateType = "角色", operateType = 2)
    public DeleteRoleOperateDTO deleteRole(Long roleId) {

        RoleDO roleDO = roleMapper.selectById(roleId);


        roleMapper.deleteById(roleId);
        return rolePermissionConverter.toDeleteRoleOperateDTO(roleDO);
    }

    @Override
    @OperateLog(dateType = "角色权限", operateType = 2)
    public DeleteRolePermissionOperateDTO deleteRolePermission(Long roleId, Long permissionId) {
        RoleDO roleDO = roleMapper.selectById(roleId);
        PermissionDO permissionDO = permissionMapper.selectById(permissionId);

        // 待删除的权限集合
        List<Long> toDeleteIds = new ArrayList<>();
        // 初始只有一个指定的待删除权限
        toDeleteIds.add(permissionId);

        while (!CollectionUtils.isEmpty(toDeleteIds)) {
            // 删除当前待删除的权限
            LambdaQueryWrapper<RolePermissionDO> rolePermissionLambdaQueryWrapper = new LambdaQueryWrapper<>();
            rolePermissionLambdaQueryWrapper.eq(RolePermissionDO::getRoleId, roleId)
                    .in(RolePermissionDO::getPermissionId, toDeleteIds);
            rolePermissionMapper.delete(rolePermissionLambdaQueryWrapper);

            LambdaQueryWrapper<PermissionDO> permissionWrapper = new LambdaQueryWrapper<>();
            permissionWrapper.in(PermissionDO::getParentPermission, toDeleteIds)
                   .select(PermissionDO::getId);
            // 查询被删除权限的子权限
            toDeleteIds = permissionMapper.selectList(permissionWrapper)
                    .stream().map(PermissionDO::getId)
                    .collect(Collectors.toList());

        }
        return rolePermissionConverter.toDeleteRolePermissionOperateDTO(roleDO, permissionDO);
    }


    public List<PermissionDTO> buildPermissionTree(List<PermissionDTO> permissions) {

        Map<String, PermissionDTO> permissionMap = permissions.stream()
                .collect(Collectors.toMap(PermissionDTO::getId, permission -> permission));

        List<PermissionDTO> rootPermissions = new ArrayList<>();
        for (PermissionDTO permission : permissions) {
            if ("-1".equals(permission.getParentPermission())) {
                rootPermissions.add(permission);
            } else {
                PermissionDTO parent = permissionMap.get(permission.getParentPermission());
                if (parent != null) {
                    if (parent.getChildren() == null) {
                        parent.setChildren(new ArrayList<>());
                    }
                    parent.getChildren().add(permission);
                }
            }
        }
        return rootPermissions;
    }

}
