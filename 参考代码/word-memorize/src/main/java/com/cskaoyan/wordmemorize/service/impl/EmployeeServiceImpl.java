package com.cskaoyan.wordmemorize.service.impl;

import cn.dev33.satoken.secure.SaSecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cskaoyan.wordmemorize.common.aspect.operate.OperateLog;
import com.cskaoyan.wordmemorize.common.aspect.operate.OperateLogConstant;
import com.cskaoyan.wordmemorize.common.constant.ResultCodeEnum;
import com.cskaoyan.wordmemorize.common.exception.BusinessException;
import com.cskaoyan.wordmemorize.converter.EmployeeConverter;
import com.cskaoyan.wordmemorize.dao.entity.*;
import com.cskaoyan.wordmemorize.dao.mapper.EmployeeMapper;
import com.cskaoyan.wordmemorize.dao.mapper.EmployeeRoleMapper;
import com.cskaoyan.wordmemorize.dao.mapper.PermissionMapper;
import com.cskaoyan.wordmemorize.dao.mapper.RoleMapper;
import com.cskaoyan.wordmemorize.dto.admin.DeleteEmployeeRoleOperateDTO;
import com.cskaoyan.wordmemorize.dto.admin.EmployeeDTO;
import com.cskaoyan.wordmemorize.dto.admin.PageDTO;
import com.cskaoyan.wordmemorize.request.EmployeeCommand;
import com.cskaoyan.wordmemorize.request.PageRequest;
import com.cskaoyan.wordmemorize.service.EmployeeService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, EmployeeDO> implements EmployeeService {

    @Resource
    EmployeeMapper employeeMapper;

    @Resource
    EmployeeConverter employeeConverter;

    @Resource
    EmployeeRoleMapper employeeRoleMapper;

    @Resource
    PermissionMapper permissionMapper;


    @Resource
    RoleMapper roleMapper;


    @Override
    public EmployeeDTO login(String userName, String password) {
        if (!StringUtils.hasText(userName) || !StringUtils.hasText(password)) {
            throw new BusinessException(ResultCodeEnum.ILLEGAL_REQUEST);
        }

        String passwd = SaSecureUtil.md5(password);

        LambdaQueryWrapper<EmployeeDO> employeeWrapper = new LambdaQueryWrapper<>();
        employeeWrapper
                .and(wrapper ->
                        wrapper.eq(EmployeeDO::getName, userName)
                                .or()
                                .eq(EmployeeDO::getNickName, userName))
                .eq(EmployeeDO::getPassword, passwd);


        EmployeeDO employeeDO = employeeMapper.selectOne(employeeWrapper);
        if (employeeDO == null) {
            throw new BusinessException(ResultCodeEnum.LOCAL_LOGIN_FAIL);
        }

        return employeeConverter.employeeDO2DTO(employeeDO);
    }

    @Override
    public EmployeeDTO getEmployeeInfo(Long employeeId) {

        // 查询roleId集合
        EmployeeDO employeeInfo = employeeMapper.getEmployeeInfo(employeeId);


//        HashSet<String> allPerms = new HashSet<>(employeeInfo.getPerms());
//        employeeInfo.getPerms().forEach(perm -> {
//
//
//            LambdaQueryWrapper<PermissionDO> permWrapper = new LambdaQueryWrapper<>();
//            permWrapper.eq(PermissionDO::getPermissionCode, perm);
//            // 根据permissionCode查询出当前权限记录
//            PermissionDO permissionDO = permissionMapper.selectOne(permWrapper);
//
//            while (permissionDO.getParentPermission() != -1) {
//                // 不是根权限
//                LambdaQueryWrapper<PermissionDO> permissionWrapper = new LambdaQueryWrapper<>();
//                permissionWrapper.eq(PermissionDO::getId, permissionDO.getParentPermission());
//                log.info("parent permission {}",permissionDO.getParentPermission());
//                permissionDO = permissionMapper.selectOne(permissionWrapper);
//                allPerms.add(permissionDO.getPermissionCode());
//            }
//
//        });
//
//        employeeInfo.setPerms(allPerms.stream().toList());

        return employeeConverter.employeeDO2DTO(employeeInfo);
    }

    @Override
    public PageDTO<EmployeeDTO> getEmployeePage(PageRequest request) {

        if (request.getPageNum() == null) {
            List<EmployeeDO> employeeDOs = employeeMapper.selectList(null);
            PageDTO<EmployeeDO> employeeDOPageDTO = new PageDTO<>();
            employeeDOPageDTO.setTotalCount((long)employeeDOs.size());
            employeeDOPageDTO.setPageList(employeeDOs);
            return employeeConverter.emoloyeePage2PageDTO(employeeDOs, (long)employeeDOs.size());
        }


        Integer offset = (request.getPageNum() - 1) * request.getCount();
        List<EmployeeDO> employeeDOPage = employeeMapper.getEmployeePage(request.getCount(), offset,request);

        Long count = employeeMapper.selectCount(null);

        return employeeConverter.emoloyeePage2PageDTO(employeeDOPage, count);
    }

    @Override
    @Transactional
    public void addEmployee(EmployeeCommand command) {

        LambdaQueryWrapper<EmployeeDO> employeeWrapper = new LambdaQueryWrapper<>();
        employeeWrapper.eq(EmployeeDO::getPhoneNumber, command.getPhoneNumber());
        EmployeeDO oldOne = employeeMapper.selectOne(employeeWrapper);
        if (oldOne != null) {
            throw new BusinessException(ResultCodeEnum.EMPLOYEE_DUPLICATION);
        }

        // 先保存基本信息
        EmployeeDO employeeDO = employeeConverter.command2EmployeeDO(command);
        String pass = SaSecureUtil.md5(command.getPassword());
        employeeDO.setPassword(pass);
        employeeMapper.insert(employeeDO);

        // 保存员工-角色关系
        command.getRoleIds().forEach(idStr -> {
            Long roleId = Long.parseLong(idStr);
            EmployeeRoleDO employeeRoleDO = new EmployeeRoleDO();
            employeeRoleDO.setRoleId(roleId);
            employeeRoleDO.setEmployeeId(employeeDO.getId());
            employeeRoleMapper.insert(employeeRoleDO);
        });
    }


    @Override
    public void updateEmployee(EmployeeCommand command) {
        // 更新 employee本身的信息
        EmployeeDO employeeDO = employeeConverter.command2EmployeeDO(command);
        if (command.getPassword() != null) {
            String md5Passwd = SaSecureUtil.md5(command.getPassword());
            employeeDO.setPassword(md5Passwd);
        }
        employeeMapper.updateById(employeeDO);
        // 拿到当前的用户的角色集合
        List<Long> roleIdsNow = employeeDO.getRoleIds();

        // 处理角色变更
        //1. 查询数据库中的roleIds
        LambdaQueryWrapper<EmployeeRoleDO> employeeLambdaQueryWrapper = new LambdaQueryWrapper<>();
        employeeLambdaQueryWrapper.eq(EmployeeRoleDO::getEmployeeId, employeeDO.getId());
        List<EmployeeRoleDO> employeeRoleDOs = employeeRoleMapper.selectList(employeeLambdaQueryWrapper);
        // 2. 处理新增
        Set<Long> roIdsFromDb = employeeRoleDOs.stream().map(EmployeeRoleDO::getRoleId).collect(Collectors.toSet());
        roleIdsNow.stream().filter(roleId -> !roIdsFromDb.contains(roleId)).forEach(roleId -> {
            EmployeeRoleDO employeeRoleDO = new EmployeeRoleDO();
            employeeRoleDO.setEmployeeId(employeeDO.getId());
            employeeRoleDO.setRoleId(roleId);
            employeeRoleMapper.insert(employeeRoleDO);
        });
    }

    @Override
    public void delete(Long employeeId) {
        // 删除员工信息
        employeeMapper.deleteById(employeeId);

        // 删除员工绑定的角色
        LambdaQueryWrapper<EmployeeRoleDO> employeeLambdaQueryWrapper = new LambdaQueryWrapper<>();
        employeeLambdaQueryWrapper.eq(EmployeeRoleDO::getEmployeeId, employeeId);
        employeeRoleMapper.delete(employeeLambdaQueryWrapper);
    }

    @Override
    @OperateLog(dateType = "删除员工角色", operateType = 2)
    public DeleteEmployeeRoleOperateDTO deleteEmployeeRole(Long employeeId, Long roleId) {

        EmployeeDO employeeDO = employeeMapper.selectById(employeeId);
        RoleDO roleDO = roleMapper.selectById(roleId);

        DeleteEmployeeRoleOperateDTO log = employeeConverter.toDeleteEmployeeRoleOperateDTO(employeeDO, roleDO);

        LambdaQueryWrapper<EmployeeRoleDO> rolePermissionWrapper = new LambdaQueryWrapper<>();
        rolePermissionWrapper.eq(EmployeeRoleDO::getEmployeeId, employeeId)
                .eq(EmployeeRoleDO::getRoleId, roleId);

        employeeRoleMapper.delete(rolePermissionWrapper);

        return log;
    }

    @Override
    public void updateLoginInfo(Long employeeId, String ip, LocalDateTime time) {

        LambdaUpdateWrapper<EmployeeDO> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(EmployeeDO::getLastLoginIp, ip)
                .set(EmployeeDO::getLastLoginTime, time)
               .eq(EmployeeDO::getId, employeeId);

        employeeMapper.update(null, updateWrapper);
    }

    @Override
    public Set<String> getPermissionAlias(Long employeeId) {
        Set<String> employeePerms = employeeMapper.getEmployeePerms(employeeId);
        return employeePerms;
    }

}
