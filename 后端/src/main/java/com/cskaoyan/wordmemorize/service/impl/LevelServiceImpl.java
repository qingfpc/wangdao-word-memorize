package com.cskaoyan.wordmemorize.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cskaoyan.wordmemorize.common.constant.ResultCodeEnum;
import com.cskaoyan.wordmemorize.common.exception.BusinessException;
import com.cskaoyan.wordmemorize.converter.LevelConverter;
import com.cskaoyan.wordmemorize.dao.entity.LevelDO;
import com.cskaoyan.wordmemorize.dao.mapper.LevelMapper;
import com.cskaoyan.wordmemorize.dto.admin.DeleteLevelOperateDTO;
import com.cskaoyan.wordmemorize.dto.admin.LevelDTO;
import com.cskaoyan.wordmemorize.dto.admin.PageDTO;
import com.cskaoyan.wordmemorize.request.BatchDeleteCommand;
import com.cskaoyan.wordmemorize.request.LevelCommand;
import com.cskaoyan.wordmemorize.request.PageRequest;
import com.cskaoyan.wordmemorize.service.LevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LevelServiceImpl implements LevelService {

    @Autowired
    LevelMapper levelMapper;

    @Autowired
    LevelConverter levelConverter;

    //添加等级
    @Override
    public void addLevel(LevelCommand command) {
        // 使用LambdaQueryWrapper并明确指定查询字段
        LambdaQueryWrapper<LevelDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(LevelDO::getId, LevelDO::getLevelName) // 只查询必要的字段
                .eq(LevelDO::getLevelName, command.getLevelName())
                .eq(LevelDO::getIsDeleted, 0); // 确保检查未删除的记录

        LevelDO levelDO = levelMapper.selectOne(queryWrapper);
        //若等级存在，则抛出异常
        if (levelDO != null) {
            throw new BusinessException(ResultCodeEnum.LEVEL_EXIST);
        }
        // 转换并保存
        LevelDO target = levelConverter.levelCommandToLevelDO(command);
        // 确保target对象不包含goodsId字段
        levelMapper.insert(target);
    }


    //更新等级
    @Override
    public void updateLevel(LevelCommand command) {

        //根据ID查询等级信息
        LevelDO levelDO = levelMapper.selectById(Long.parseLong(command.getId()));

        //若等级存在，则更新
        if (levelDO != null) {
            LevelDO commandToLevelDO = levelConverter.levelCommandToLevelDO(command);
            levelMapper.updateById(commandToLevelDO);
        }

    }


    //获取等级列表
    @Override
    public PageDTO<LevelDTO> getLevelList(PageRequest request) {
        // 创建分页对象
        Page<LevelDO> page = new Page<>(request.getPageNum(), request.getCount());

        // 创建查询条件包装器并指定查询字段
        QueryWrapper<LevelDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id", "level_name", "level_value", "check_in_days",
                "check_in_type", "unlock_price", "description", "is_deleted");

        // 若关键词不为空，添加模糊查询条件
        if (StringUtils.hasText(request.getKeyword())) {
            queryWrapper.like("level_name", request.getKeyword())
                    .or()
                    .like("description", request.getKeyword());
        }

        // 按等级名称升序排列
        queryWrapper.orderByAsc("level_name");

        // 执行分页查询
        Page<LevelDO> levelDOPage = levelMapper.selectPage(page, queryWrapper);

        // 将查询结果转换为DTO对象
        List<LevelDTO> levelDTOList = levelDOPage.getRecords().stream()
                .map(levelConverter::levelDOToLevelDTO)
                .collect(Collectors.toList());

        // 创建分页DTO对象
        PageDTO<LevelDTO> pageDTO = new PageDTO<>();
        pageDTO.setPageList(levelDTOList);
        pageDTO.setTotalCount(levelDOPage.getTotal());

        return pageDTO;
    }

    @Override
    public DeleteLevelOperateDTO deleteLevel(Long id) {
        //根据ID查询等级信息
        LevelDO levelDO = levelMapper.selectById(id);
        //若等级不存在，则抛出异常
        if (levelDO == null) {
            throw new BusinessException(ResultCodeEnum.LEVEL_NOT_EXIST);
        }else {
            //若等级存在，则删除
            levelMapper.deleteById(id);
        }
        return levelConverter.levelDOToDeleteLevelOperateDTO(levelDO);
    }

    @Override
    public List<DeleteLevelOperateDTO> deleteLevels(BatchDeleteCommand command) {
        //根据ID列表查询等级信息
        List<LevelDO> levelDOs = levelMapper.selectBatchIds(command.getIds());
        //若等级存在，则删除
        if (levelDOs == null) {
            throw new BusinessException(ResultCodeEnum.LEVEL_NOT_EXIST);
        }else {
            levelMapper.deleteBatchIds(command.getIds());
        }
        return levelConverter.levelDOsToDeleteLevelOperateDTOs(levelDOs);
    }
}
