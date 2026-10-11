package com.cskaoyan.wordmemorize.converter;

import com.cskaoyan.wordmemorize.dao.entity.PermissionDO;
import com.cskaoyan.wordmemorize.dao.entity.RoleDO;
import com.cskaoyan.wordmemorize.dao.entity.RolePermissionDO;
import com.cskaoyan.wordmemorize.dto.admin.*;
import com.cskaoyan.wordmemorize.request.PermissionCommand;
import com.cskaoyan.wordmemorize.request.RoleCommand;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RolePermissionConverter {

    List<PermissionDTO> employeePermissionDO2DTO(List<PermissionDO> permissionDO);
    PermissionDO command2PermissionDO(PermissionCommand command);

    @Mapping(source = "vocBookDOs", target = "pageList")
    @Mapping(source = "total", target = "totalCount")
    PageDTO<RoleDTO> roleList2Page(List<RoleDO> vocBookDOs, Long total);
    RoleDO command2RoleDO(RoleCommand roleCommand);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "permissionId", source = "permissionDTO.id")
    @Mapping(target = "permissionCode", source = "permissionDTO.permissionCode")
    List<RolePermissionDO> command2RolePermissionDO(List<PermissionCommand> command);


    DeletePermissionOperateDTO toDeletePermissionOperateDTO(PermissionDO permissionDO);

    DeleteRoleOperateDTO toDeleteRoleOperateDTO(RoleDO roleDO);

    @Mapping(source = "roleDO.id", target = "roleId")
    @Mapping(source = "roleDO.name", target = "roleName")
    @Mapping(source = "permissionDO.id", target = "permissionId")
    @Mapping(source = "permissionDO.permissionAlias", target = "permissionAlias")
    DeleteRolePermissionOperateDTO toDeleteRolePermissionOperateDTO(RoleDO roleDO, PermissionDO permissionDO);
}
