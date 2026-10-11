package com.cskaoyan.wordmemorize.converter;

import com.cskaoyan.wordmemorize.dao.entity.EmployeeDO;
import com.cskaoyan.wordmemorize.dao.entity.RoleDO;
import com.cskaoyan.wordmemorize.dto.admin.DeleteEmployeeRoleOperateDTO;
import com.cskaoyan.wordmemorize.dto.admin.EmployeeDTO;
import com.cskaoyan.wordmemorize.dto.admin.PageDTO;
import com.cskaoyan.wordmemorize.dto.admin.RoleDTO;
import com.cskaoyan.wordmemorize.request.EmployeeCommand;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EmployeeConverter {

    EmployeeDTO employeeDO2DTO(EmployeeDO employeeDO);

    @Mapping(source = "employeeList", target = "pageList")
    @Mapping(source = "total", target = "totalCount")
    PageDTO<EmployeeDTO> emoloyeePage2PageDTO(List<EmployeeDO> employeeList, Long total);

    EmployeeDO command2EmployeeDO(EmployeeCommand command);

    DeleteEmployeeRoleOperateDTO toDeleteEmployeeRoleOperateDTO(EmployeeDO employeeDO, RoleDO roleDO);
}
