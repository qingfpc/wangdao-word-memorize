package com.cskaoyan.wordmemorize.service.impl;

import cn.dev33.satoken.secure.SaSecureUtil;
import com.alibaba.excel.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cskaoyan.wordmemorize.common.constant.ResultCodeEnum;
import com.cskaoyan.wordmemorize.common.exception.BusinessException;
import com.cskaoyan.wordmemorize.converter.EmployeeConverter;
import com.cskaoyan.wordmemorize.dao.entity.EmployeeDO;
import com.cskaoyan.wordmemorize.dao.entity.EmployeeRoleDO;
import com.cskaoyan.wordmemorize.dao.entity.RoleDO;
import com.cskaoyan.wordmemorize.dao.mapper.EmployeeMapper;
import com.cskaoyan.wordmemorize.dao.mapper.EmployeeRoleMapper;
import com.cskaoyan.wordmemorize.dao.mapper.RoleMapper;
import com.cskaoyan.wordmemorize.dto.admin.DeleteEmployeeRoleOperateDTO;
import com.cskaoyan.wordmemorize.dto.admin.EmployeeDTO;
import com.cskaoyan.wordmemorize.dto.admin.PageDTO;
import com.cskaoyan.wordmemorize.request.EmployeeCommand;
import com.cskaoyan.wordmemorize.request.PageRequest;
import com.cskaoyan.wordmemorize.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {


    @Autowired
    EmployeeMapper employeeMapper;

    @Autowired
    EmployeeConverter employeeConverter;

    @Autowired
    EmployeeRoleMapper employeeRoleMapper;



    @Autowired
    RoleMapper roleMapper;



    //登录
    @Override
    public EmployeeDTO login(String userName, String password) {

        // 1. 将明文密码变为密文密码
        String encodePassword = SaSecureUtil.md5(password);

        // 2. 根据用户名和密码查询数据库
        LambdaQueryWrapper<EmployeeDO> employeeWrapper = new LambdaQueryWrapper<>();
        employeeWrapper.eq(EmployeeDO::getName, userName)
                .eq(EmployeeDO::getPassword, encodePassword);

        EmployeeDO employeeDO = employeeMapper.selectOne(employeeWrapper);
        if (employeeDO == null) {
            throw new BusinessException(ResultCodeEnum.LOCAL_LOGIN_FAIL);
        }



        return employeeConverter.employeeDO2DTO(employeeDO);
    }

    @Override
    public EmployeeDTO getEmployeeInfo(Long employeeId) {


        EmployeeDO employeeInfo = employeeMapper.getEmployeeInfo(employeeId);
        return employeeConverter.employeeDO2DTO(employeeInfo);
    }



    //获取后台员工列表
    @Override
    public PageDTO<EmployeeDTO> getEmployeePage(PageRequest request) {

        if (request.getPageNum() == null){
            List<EmployeeDO> employeeDOS = employeeMapper.selectList(null);
            employeeDOS.forEach(a-> a.setPassword(null));
            PageDTO<EmployeeDTO> pageDTO = employeeConverter.emoloyeePage2PageDTO(employeeDOS, (long) employeeDOS.size());
            return pageDTO;
        }

        Page<EmployeeDO> page = new Page<>(request.getPageNum(), request.getCount());
        LambdaQueryWrapper<EmployeeDO> queryWrapper = new LambdaQueryWrapper<>();
        if (request.getKeyword() != null){
            queryWrapper.like(EmployeeDO::getName, request.getKeyword())
                    .or()
                    .like(EmployeeDO::getNickName, request.getKeyword());
        }
        Page<EmployeeDO> selectPage = employeeMapper.selectPage(page, queryWrapper);
        List<EmployeeDO> employeeDOS = selectPage.getRecords();
        employeeDOS.forEach(a->{
            a.setPassword(null);
            Long id = a.getId();
            LambdaQueryWrapper<EmployeeRoleDO> roleQueryWrapper = new LambdaQueryWrapper<>();
            roleQueryWrapper.eq(EmployeeRoleDO::getEmployeeId, id);
            List<EmployeeRoleDO> employeeRoleDOS = employeeRoleMapper.selectList(roleQueryWrapper);
            List<Long> roleList = employeeRoleDOS.stream()
                    .map(EmployeeRoleDO::getRoleId)
                    .collect(Collectors.toList());
            a.setRoleIds(roleList);
        });
        List<EmployeeDO> totalEmployee = employeeMapper.selectList(queryWrapper);
        PageDTO<EmployeeDTO> pageDTO = employeeConverter.emoloyeePage2PageDTO(employeeDOS, (long) totalEmployee.size());
        return pageDTO;
    }



    //添加员工
    @Override
    public void addEmployee(EmployeeCommand command) {

        LambdaQueryWrapper<EmployeeDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(EmployeeDO::getName, command.getName());

        List<EmployeeDO> employeeDOS = employeeMapper.selectList(queryWrapper);
        if (employeeDOS !=null && employeeDOS.size()>0 ){
            throw new BusinessException(ResultCodeEnum.EMPLOYEE_DUPLICATION);
        }


        EmployeeDO employeeDO = employeeConverter.command2EmployeeDO(command);
        String password = command.getPassword();
        if (password != null && password.length()<6){
            throw new BusinessException(ResultCodeEnum.PASSWORD_LENGTH_ERROR);
        }

        // 对密码进行加密
        String encryptedPassword = SaSecureUtil.md5(password);
        employeeDO.setPassword(encryptedPassword);



        employeeMapper.insert(employeeDO);

        // 在employee_role表中插入 employee 与 role 的关系
        List<Long> roleIds = employeeDO.getRoleIds();
        if (roleIds != null && !roleIds.isEmpty()){
            roleIds.forEach(a->{
                EmployeeRoleDO employeeRoleDO = new EmployeeRoleDO();
                employeeRoleDO.setEmployeeId(employeeDO.getId());
                employeeRoleDO.setRoleId(a);
                employeeRoleMapper.insert(employeeRoleDO);
            });
        }
    }






    //修改后台员工

    @Override
    public void updateEmployee(EmployeeCommand command) {

        EmployeeDO employeeDO = employeeConverter.command2EmployeeDO(command);

        LambdaQueryWrapper<EmployeeDO> preQueryWrapper = new LambdaQueryWrapper<>();
        preQueryWrapper.eq(EmployeeDO::getName, employeeDO.getName());

        EmployeeDO selectOne = employeeMapper.selectOne(preQueryWrapper);
        EmployeeDO sourceOne = employeeMapper.selectById(employeeDO.getId());

        if (!sourceOne.getName().equals(employeeDO.getName()) && selectOne != null ){
            throw new BusinessException(ResultCodeEnum.EMPLOYEE_DUPLICATION);
        }

        String password = employeeDO.getPassword();
        if (password != null && password.length()<6){
            throw new BusinessException(ResultCodeEnum.PASSWORD_LENGTH_ERROR);
        }
        if (password != null){
            String md5Password = SaSecureUtil.md5(password);
            employeeDO.setPassword(md5Password);
        }
        employeeMapper.updateById(employeeDO);

        List<Long> roleIds = employeeDO.getRoleIds();
        if (roleIds != null && !roleIds.isEmpty()){
            roleIds.forEach(a->{
                LambdaQueryWrapper<EmployeeRoleDO> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(EmployeeRoleDO::getEmployeeId, employeeDO.getId())
                        .eq(EmployeeRoleDO::getRoleId, a);
                List<EmployeeRoleDO> employeeRoleDOS = employeeRoleMapper.selectList(queryWrapper);
                if (employeeRoleDOS.isEmpty()){
                    EmployeeRoleDO employeeRoleDO = new EmployeeRoleDO();
                    employeeRoleDO.setEmployeeId(employeeDO.getId());
                    employeeRoleDO.setRoleId(a);
                    employeeRoleMapper.insert(employeeRoleDO);
                }
            });
        }
    }

    //删除员工
    @Transactional
    @Override
    public void delete(Long employeeId) {
        int i = employeeMapper.deleteById(employeeId);
        if (i == 0){
            throw new BusinessException(ResultCodeEnum.EMPLOYEE_NOT_FOUND);
        }
        LambdaQueryWrapper<EmployeeRoleDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(EmployeeRoleDO::getEmployeeId, employeeId);
        employeeRoleMapper.delete(queryWrapper);

    }

    @Override
    public DeleteEmployeeRoleOperateDTO deleteRole(Long employeeId, Long roleId) {
        LambdaQueryWrapper<EmployeeRoleDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(EmployeeRoleDO::getEmployeeId, employeeId)
                .eq(EmployeeRoleDO::getRoleId, roleId);
        employeeRoleMapper.delete(queryWrapper);

        EmployeeDO employeeDO = employeeMapper.selectById(employeeId);
        DeleteEmployeeRoleOperateDTO deleteEmployeeRoleOperateDTO = new DeleteEmployeeRoleOperateDTO();
        if(employeeDO!=null){

            RoleDO roleDO = roleMapper.selectById(roleId);
            deleteEmployeeRoleOperateDTO.setEmployeeId(employeeId);
            deleteEmployeeRoleOperateDTO.setRoleId(roleId.toString());
            deleteEmployeeRoleOperateDTO.setEmployeeName(employeeDO.getName());
            deleteEmployeeRoleOperateDTO.setRoleName(roleDO.getName());
        }
        return deleteEmployeeRoleOperateDTO;

    }

    @Override
    public void updateLoginInfo(Long employeeId, String ip, LocalDateTime time) {

        EmployeeDO employeeDO = new EmployeeDO();
        employeeDO.setId(employeeId);
        employeeDO.setLastLoginIp(ip);
        employeeDO.setLastLoginTime(time);

        employeeMapper.updateById(employeeDO);
    }

    @Override
    public Set<String> getPermissionAlias(Long employeeId) {
        Set<String> permissionAlias = employeeMapper.getPermissionAlias(employeeId);
        return permissionAlias;
    }

    @Override
    public boolean saveBatch(Collection<EmployeeDO> entityList, int batchSize) {
        return false;
    }

    @Override
    public boolean saveOrUpdateBatch(Collection<EmployeeDO> entityList, int batchSize) {
        return false;
    }

    @Override
    public boolean updateBatchById(Collection<EmployeeDO> entityList, int batchSize) {
        return false;
    }

    @Override
    public boolean saveOrUpdate(EmployeeDO entity) {
        return false;
    }

    @Override
    public EmployeeDO getOne(Wrapper<EmployeeDO> queryWrapper, boolean throwEx) {
        return null;
    }

    @Override
    public Optional<EmployeeDO> getOneOpt(Wrapper<EmployeeDO> queryWrapper, boolean throwEx) {
        return Optional.empty();
    }

    @Override
    public Map<String, Object> getMap(Wrapper<EmployeeDO> queryWrapper) {
        return null;
    }

    @Override
    public <V> V getObj(Wrapper<EmployeeDO> queryWrapper, Function<? super Object, V> mapper) {
        return null;
    }

    @Override
    public BaseMapper<EmployeeDO> getBaseMapper() {
        return null;
    }

    @Override
    public Class<EmployeeDO> getEntityClass() {
        return null;
    }
}
