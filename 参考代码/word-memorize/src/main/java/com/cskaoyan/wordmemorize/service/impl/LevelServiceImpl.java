package com.cskaoyan.wordmemorize.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cskaoyan.wordmemorize.common.aspect.operate.OperateLog;
import com.cskaoyan.wordmemorize.converter.LevelConverter;
import com.cskaoyan.wordmemorize.dao.entity.LevelDO;
import com.cskaoyan.wordmemorize.dao.entity.LevelPrivilegeDO;
import com.cskaoyan.wordmemorize.dao.mapper.LevelMapper;
import com.cskaoyan.wordmemorize.dao.mapper.LevelPrivilegeMapper;
import com.cskaoyan.wordmemorize.dto.admin.DeleteLevelOperateDTO;
import com.cskaoyan.wordmemorize.dto.admin.LevelDTO;
import com.cskaoyan.wordmemorize.dto.admin.PageDTO;
import com.cskaoyan.wordmemorize.request.BatchDeleteCommand;
import com.cskaoyan.wordmemorize.request.LevelCommand;
import com.cskaoyan.wordmemorize.request.PageRequest;
import com.cskaoyan.wordmemorize.service.LevelService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LevelServiceImpl implements LevelService {

    @Resource
    LevelMapper levelMapper;

    @Resource
    LevelConverter levelConverter;

    @Resource
    LevelPrivilegeMapper levelPrivilegeMapper;


    @Override
    @OperateLog(dateType = "等级", operateType = 1)
    public void addLevel(LevelCommand command) {
        LevelDO levelDO = levelConverter.levelCommandToLevelDO(command);
        levelMapper.insert(levelDO);
    }

    @Override
    @OperateLog(dateType = "等级", operateType = 4)
    public void updateLevel(LevelCommand command) {
        LevelDO levelDO = levelConverter.levelCommandToLevelDO(command);
        levelMapper.updateById(levelDO);
    }

    @Override
    public PageDTO<LevelDTO> getLevelList(PageRequest request) {
        LambdaQueryWrapper<LevelDO> queryWrapper = null;
        if (request.getKeyword() != null) {
            queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.like(LevelDO::getLevelName, request.getKeyword());
        }
        if (request.getPageNum() == null) {
            // 说明是查询全部
            List<LevelDO> levelDOs = levelMapper.selectList(queryWrapper);
            PageDTO<LevelDTO> levelDTOPageDTO = levelConverter.levelDOToPageDTO(levelDOs, levelDOs.size());
            return levelDTOPageDTO;
        }

        // 分页查询
        Page<LevelDO> levelDOPage = new Page<>(request.getPageNum(), request.getCount());
        Page<LevelDO> resultPage = levelMapper.selectPage(levelDOPage, queryWrapper);
        return levelConverter.levelDOToPageDTO(resultPage.getRecords(), (int)resultPage.getTotal());
    }

    @Override
    @Transactional
    @OperateLog(dateType = "等级", operateType = 2)
    public DeleteLevelOperateDTO deleteLevel(Long id) {

        LevelDO levelDO = levelMapper.selectById(id);
        DeleteLevelOperateDTO deleteLevelOperateDTO = levelConverter.levelDOToDeleteLevelOperateDTO(levelDO);

        levelMapper.deleteById(id);

        LambdaQueryWrapper<LevelPrivilegeDO> levelPrivilegeWrapper = new LambdaQueryWrapper<>();
        levelPrivilegeWrapper.eq(LevelPrivilegeDO::getLevelId, id);
        levelPrivilegeMapper.delete(levelPrivilegeWrapper);

        return deleteLevelOperateDTO;
    }

    @Override
    @OperateLog(dateType = "等级", operateType = 2)
    public List<DeleteLevelOperateDTO> deleteLevels(BatchDeleteCommand command) {
        List<Long> ids = command.getIds().stream().map(idStr -> Long.parseLong(idStr))
                .collect(Collectors.toList());
        List<LevelDO> levelDOs = levelMapper.selectByIds(ids);
        List<DeleteLevelOperateDTO> deleteLevelOperateDTOs = levelConverter
                .levelDOsToDeleteLevelOperateDTOs(levelDOs);
        ids.forEach(id -> {
            levelMapper.deleteById(id);
        });

        LambdaQueryWrapper<LevelPrivilegeDO> levelPrivilegeWrapper = new LambdaQueryWrapper<>();
        levelPrivilegeWrapper.in(LevelPrivilegeDO::getLevelId, command.getIds());
        levelPrivilegeMapper.delete(levelPrivilegeWrapper);

        return deleteLevelOperateDTOs;
    }
}
