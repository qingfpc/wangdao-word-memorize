package com.cskaoyan.wordmemorize.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cskaoyan.wordmemorize.converter.AdminLogConverter;
import com.cskaoyan.wordmemorize.dao.entity.OperateLogDO;
import com.cskaoyan.wordmemorize.dao.mapper.OperateLogMapper;
import com.cskaoyan.wordmemorize.dto.admin.OperateLogDTO;
import com.cskaoyan.wordmemorize.dto.admin.PageDTO;
import com.cskaoyan.wordmemorize.request.OperateLogPageRequest;
import com.cskaoyan.wordmemorize.service.AdminLogService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class AdminLogServiceImpl implements AdminLogService {

    @Resource
    OperateLogMapper operateLogMapper;
    @Resource
    AdminLogConverter adminLogConverter;

    //查询系统操作日志
    @Override
    public PageDTO<OperateLogDTO> getOperateLogPage(OperateLogPageRequest request) {
        // 创建分页对象
        Page<OperateLogDO> page = new Page<>(request.getPageNum(), request.getCount());
        // 执行无条件分页查询
        Page<OperateLogDO> resultPage = operateLogMapper.selectPage(page, null);
        return adminLogConverter.operationLogPage2PageDTO(resultPage.getRecords(), resultPage.getTotal());
    }
}
