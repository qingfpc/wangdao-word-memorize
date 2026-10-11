package com.cskaoyan.wordmemorize.service.impl;

import cn.dev33.satoken.secure.SaSecureUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cskaoyan.wordmemorize.common.constant.ResultCodeEnum;
import com.cskaoyan.wordmemorize.common.exception.BusinessException;
import com.cskaoyan.wordmemorize.converter.EmployeeConverter;
import com.cskaoyan.wordmemorize.dao.entity.EmployeeDO;
import com.cskaoyan.wordmemorize.dao.mapper.EmployeeMapper;
import com.cskaoyan.wordmemorize.dto.admin.DeleteEmployeeRoleOperateDTO;
import com.cskaoyan.wordmemorize.dto.admin.EmployeeDTO;
import com.cskaoyan.wordmemorize.dto.admin.PageDTO;
import com.cskaoyan.wordmemorize.request.EmployeeCommand;
import com.cskaoyan.wordmemorize.request.PageRequest;
import com.cskaoyan.wordmemorize.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

@Service
public class EmployeeServiceImpl implements EmployeeService {


    @Autowired
    EmployeeMapper employeeMapper;

    @Autowired
    EmployeeConverter employeeConverter;




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

    @Override
    public PageDTO<EmployeeDTO> getEmployeePage(PageRequest request) {
        return null;
    }

    @Override
    public void addEmployee(EmployeeCommand command) {

    }

    @Override
    public void updateEmployee(EmployeeCommand command) {

    }

    @Override
    public void delete(Long employeeId) {

    }

    @Override
    public DeleteEmployeeRoleOperateDTO deleteRole(Long employeeId, Long roleId) {
        return null;
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
        return null;
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
