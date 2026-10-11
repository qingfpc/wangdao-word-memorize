package com.cskaoyan.wordmemorize.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cskaoyan.wordmemorize.common.aspect.operate.OperateLog;
import com.cskaoyan.wordmemorize.converter.LevelConverter;
import com.cskaoyan.wordmemorize.dao.entity.LevelPrivilegeDO;
import com.cskaoyan.wordmemorize.dao.mapper.LevelPrivilegeMapper;
import com.cskaoyan.wordmemorize.dto.admin.DeleteLevelPrivilegeOperateDTO;
import com.cskaoyan.wordmemorize.dto.admin.LevelPrivilegeDTO;
import com.cskaoyan.wordmemorize.dto.admin.PageDTO;
import com.cskaoyan.wordmemorize.request.BatchDeleteCommand;
import com.cskaoyan.wordmemorize.request.LevelPrivilegeCommand;
import com.cskaoyan.wordmemorize.request.LevelPrivilegePageRequest;
import com.cskaoyan.wordmemorize.service.LevelPrivilegeService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LevelPrivilegeServiceImpl implements LevelPrivilegeService {

    @Resource
    LevelPrivilegeMapper levelPrivilegeMapper;

    @Resource
    LevelConverter levelConverter;
    @Override
    public PageDTO<LevelPrivilegeDTO> getLevelPrivilegeList(LevelPrivilegePageRequest pageRequest) {
        LambdaQueryWrapper<LevelPrivilegeDO> queryWrapper = null;
        if (pageRequest.getKeyword() != null) {
            queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper
                    .like(LevelPrivilegeDO::getPrivilegeName, pageRequest.getKeyword())
                    .or()
                    .like(LevelPrivilegeDO::getPrivilegeCode, pageRequest.getKeyword());
        }

        if (pageRequest.getLevelId() != null) {
            if (queryWrapper == null) {
                queryWrapper = new LambdaQueryWrapper<>();
            }
            queryWrapper.eq(LevelPrivilegeDO::getLevelId, Long.parseLong(pageRequest.getLevelId()));
        }

        if (pageRequest.getPageNum() == null) {
            List<LevelPrivilegeDO> levelPrivilegeDOs = levelPrivilegeMapper.selectList(queryWrapper);
            return levelConverter.levelPrivilegeDOToPageDTO(levelPrivilegeDOs, levelPrivilegeDOs.size());
        }

        Page<LevelPrivilegeDO> pageParam = new Page<>(pageRequest.getPageNum(), pageRequest.getCount());
        Page<LevelPrivilegeDO> levelPrivilegeDOPage = levelPrivilegeMapper.selectPage(pageParam, queryWrapper);
        return levelConverter.levelPrivilegeDOToPageDTO(levelPrivilegeDOPage.getRecords(), (int) levelPrivilegeDOPage.getTotal());
    }

    @Override
    @OperateLog(dateType = "等级特权", operateType = 1)
    public void addLevelPrivilege(LevelPrivilegeCommand command) {
        LevelPrivilegeDO levelPrivilegeDO = levelConverter.levelPrivilegeCommandToLevelPrivilegeDO(command);
        levelPrivilegeMapper.insert(levelPrivilegeDO);
    }

    @Override
    @OperateLog(dateType = "等级特权", operateType = 4)
    public void updateLevelPrivilege(LevelPrivilegeCommand command) {
        LevelPrivilegeDO levelPrivilegeDO = levelConverter.levelPrivilegeCommandToLevelPrivilegeDO(command);
        levelPrivilegeMapper.updateById(levelPrivilegeDO);
    }

    @Override
    @OperateLog(dateType = "等级特权", operateType = 2)
    public DeleteLevelPrivilegeOperateDTO deleteLevelPrivilege(Long id) {
        LevelPrivilegeDO levelPrivilegeDO = levelPrivilegeMapper.selectById(id);
        DeleteLevelPrivilegeOperateDTO deleteLevelPrivilegeOperateDTO
                = levelConverter.levelPrivilegeDOToDeleteLevelPrivilegeOperateDTO(levelPrivilegeDO);
        levelPrivilegeMapper.deleteById(id);
        return deleteLevelPrivilegeOperateDTO;
    }

    @Override
    @OperateLog(dateType = "等级特权", operateType = 3)
    public List<DeleteLevelPrivilegeOperateDTO> batchDeleteLevelPrivilege(BatchDeleteCommand command) {
        List<Long> ids = command.getIds().stream().map(Long::parseLong).toList();
        List<LevelPrivilegeDO> levelPrivilegeDOs = levelPrivilegeMapper.selectByIds(ids);
        List<DeleteLevelPrivilegeOperateDTO> deleteLevelPrivilegeOperateDTOs
                = levelConverter.levelPrivilegeDOsToDeleteLevelPrivilegeOperateDTOs(levelPrivilegeDOs);
        LambdaQueryWrapper<LevelPrivilegeDO> queryWrapper = new LambdaQueryWrapper<LevelPrivilegeDO>()
                .in(LevelPrivilegeDO::getId, ids);
        levelPrivilegeMapper.delete(queryWrapper);

        return deleteLevelPrivilegeOperateDTOs;
    }
}
