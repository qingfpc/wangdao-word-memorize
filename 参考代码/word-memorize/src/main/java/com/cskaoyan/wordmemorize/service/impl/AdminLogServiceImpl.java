package com.cskaoyan.wordmemorize.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cskaoyan.wordmemorize.converter.AdminLogConverter;
import com.cskaoyan.wordmemorize.dao.entity.OperateLogDO;
import com.cskaoyan.wordmemorize.dao.mapper.OperateLogMapper;
import com.cskaoyan.wordmemorize.dto.admin.OperateLogDTO;
import com.cskaoyan.wordmemorize.dto.admin.PageDTO;
import com.cskaoyan.wordmemorize.request.OperateLogPageRequest;
import com.cskaoyan.wordmemorize.service.AdminLogService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class AdminLogServiceImpl implements AdminLogService {

    @Resource
    OperateLogMapper operateLogMapper;

    @Resource
    AdminLogConverter logConverter;

    @Override
    public PageDTO<OperateLogDTO> getOperateLogPage(OperateLogPageRequest request) {
        LambdaQueryWrapper<OperateLogDO> operateLogWrapper = new LambdaQueryWrapper<>();
        if (request.getEmployeeId() != null) {
            operateLogWrapper.eq(OperateLogDO::getEmployeeId, request.getEmployeeId());
        }
        if (StringUtils.hasText(request.getDataType())) {
            operateLogWrapper.eq(OperateLogDO::getDataType, request.getDataType());
        }
        Page<OperateLogDO> pageParam = new Page<>(request.getPageNum(), request.getCount());
        Page<OperateLogDO> operateLogDOPage = operateLogMapper.selectPage(pageParam, operateLogWrapper);
        PageDTO<OperateLogDTO> operateLogPage = logConverter.operationLogPage2PageDTO(operateLogDOPage.getRecords(), operateLogDOPage.getTotal());
        return operateLogPage;
    }

}
