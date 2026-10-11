package com.cskaoyan.wordmemorize.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cskaoyan.wordmemorize.dao.entity.EmployeeDO;
import com.cskaoyan.wordmemorize.request.PageRequest;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

public interface EmployeeMapper extends BaseMapper<EmployeeDO> {

    List<EmployeeDO> getEmployeePage(@Param("limit") Integer limit, @Param("offset") Integer offset, @Param("pageRequest") PageRequest pageRequest);

    EmployeeDO getEmployeeInfo(@Param("employeeId") Long employeeId);

    Set<String> getEmployeePerms(@Param("employeeId") Long employeeId);
}
