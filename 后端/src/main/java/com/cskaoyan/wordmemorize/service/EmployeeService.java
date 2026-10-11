package com.cskaoyan.wordmemorize.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cskaoyan.wordmemorize.dao.entity.EmployeeDO;
import com.cskaoyan.wordmemorize.dto.admin.DeleteEmployeeRoleOperateDTO;
import com.cskaoyan.wordmemorize.dto.admin.EmployeeDTO;
import com.cskaoyan.wordmemorize.dto.admin.PageDTO;
import com.cskaoyan.wordmemorize.request.EmployeeCommand;
import com.cskaoyan.wordmemorize.request.PageRequest;

import java.time.LocalDateTime;
import java.util.Set;

public interface EmployeeService extends IService<EmployeeDO> {
    /*
        后台登录接口
     */
    EmployeeDTO login(String userName, String password);

    /*
        获取当前后台员工信息(登录之后请求，其中包含员工的权限集合)
     */
    EmployeeDTO getEmployeeInfo(Long employeeId);

    /*
        分页查询员工信息(后台账号权限管理-员工管理)
     */
    PageDTO<EmployeeDTO> getEmployeePage(PageRequest request);


    /*
       添加员工信息(后台账号权限管理-员工管理)
     */
    void addEmployee(EmployeeCommand command);

    /*
        更新员工信息(后台账号权限管理-员工管理)
     */
    void updateEmployee(EmployeeCommand command);

    /*
        删除指定员工(后台账号权限管理-员工管理)
     */
    void delete(Long employeeId);

    /*
        删除指定员工的角色(后台账号权限管理-员工管理)
     */
    DeleteEmployeeRoleOperateDTO deleteRole(Long employeeId, Long roleId);

    /*
        后台登录时调用该方法更新用户最近一次登录的ip地址和时间
     */
    void updateLoginInfo(Long employeeId, String ip, LocalDateTime time);

    /*
        在StpInterfaceImpl的getPermissionList中调用，后台员工的权限列表
     */
    Set<String> getPermissionAlias(Long employeeId);
}
