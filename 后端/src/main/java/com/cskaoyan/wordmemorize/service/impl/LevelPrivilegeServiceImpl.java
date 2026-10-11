package com.cskaoyan.wordmemorize.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
public class LevelPrivilegeServiceImpl implements LevelPrivilegeService {

    @Autowired
    LevelPrivilegeMapper levelPrivilegeMapper;

    @Autowired
    LevelConverter levelConverter;

    //    获取等级特权列表
    @Override
    public PageDTO<LevelPrivilegeDTO> getLevelPrivilegeList(LevelPrivilegePageRequest pageRequest) {

//        创建分页对象
        Page<LevelPrivilegeDO> page = new Page<>(pageRequest.getPageNum(), pageRequest.getCount());

//        创建查询条件包装器
        LambdaQueryWrapper<LevelPrivilegeDO> queryWrapper = new LambdaQueryWrapper<>();

//        若等级ID不为空，添加查询条件
        if (StringUtils.isNotBlank(pageRequest.getLevelId())) {
            queryWrapper.eq(LevelPrivilegeDO::getLevelId, Long.parseLong(pageRequest.getLevelId()));
        }

//        执行分页查询
        Page<LevelPrivilegeDO> levelPrivilegeDOPage = levelPrivilegeMapper.selectPage(page, queryWrapper);

//        将查询结果转换为DTO列表
        List<LevelPrivilegeDTO> levelPrivilegeDOList = levelPrivilegeDOPage.getRecords().stream()
                .map(levelConverter::levelPrivilegeDOToLevelPrivilegeDTO)
                .collect(Collectors.toList());

//        创建分页DTO对象
        PageDTO<LevelPrivilegeDTO> pageDTO = new PageDTO<>();
        pageDTO.setPageList(levelPrivilegeDOList);
        pageDTO.setTotalCount(levelPrivilegeDOPage.getTotal());

        return pageDTO;
    }

    //    添加等级特权
    @Transactional
    @Override
    public void addLevelPrivilege(LevelPrivilegeCommand command) {
        LambdaQueryWrapper<LevelPrivilegeDO> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.eq(LevelPrivilegeDO::getPrivilegeCode, command.getPrivilegeCode());

        if (levelPrivilegeMapper.selectCount(queryWrapper) <= 0) {

            LevelPrivilegeDO levelPrivilegeDO = levelConverter.levelPrivilegeCommandToLevelPrivilegeDO(command);

            levelPrivilegeMapper.insert(levelPrivilegeDO);
        }
    }

    //    修改等级特权
    @Override
    public void updateLevelPrivilege(LevelPrivilegeCommand command) {

//        根据id查询等级特权信息
        LevelPrivilegeDO levelPrivilegeDO = levelPrivilegeMapper.selectById(Long.parseLong(command.getId()));

        if (levelPrivilegeDO != null) {
//            判断新的特权码是否存在
            LambdaQueryWrapper<LevelPrivilegeDO> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(LevelPrivilegeDO::getPrivilegeCode, command.getPrivilegeCode())
                    .ne(LevelPrivilegeDO::getId, Long.parseLong(command.getId()));

            if (levelPrivilegeMapper.selectCount(queryWrapper) <= 0) {
                LevelPrivilegeDO updateLevelPrivilegeDO = levelConverter.levelPrivilegeCommandToLevelPrivilegeDO(command);
                updateLevelPrivilegeDO.setId(Long.parseLong(command.getId()));
                levelPrivilegeMapper.updateById(updateLevelPrivilegeDO);
            }
        }
    }

    //    删除指定等级特权
    @Override
    public DeleteLevelPrivilegeOperateDTO deleteLevelPrivilege(Long id) {

        DeleteLevelPrivilegeOperateDTO result = new DeleteLevelPrivilegeOperateDTO();
        result.setId(id);

//        根据id查询等级特权信息
        LevelPrivilegeDO levelPrivilegeDO = levelPrivilegeMapper.selectById(id);
        if (levelPrivilegeDO == null) {
            result.setPrivilegeName("特权不存在");
            result.setLevelId(null);
            result.setLevelValue(null);
            return result;
        } else {

            result.setPrivilegeName(levelPrivilegeDO.getPrivilegeName());
            result.setLevelId(levelPrivilegeDO.getLevelId());
            result.setLevelValue(String.valueOf(levelPrivilegeDO.getLevelValue()));

            int affectsRows = levelPrivilegeMapper.deleteById(id);
            if (affectsRows > 0) {
                System.out.println("删除等级特权成功，ID：{}" + id);
            } else {
                System.out.println("删除等级特权失败，ID：{}" + id);
            }
        }


        return result;
    }

    //    批量删除等级特权
    @Override
    public List<DeleteLevelPrivilegeOperateDTO> batchDeleteLevelPrivilege(BatchDeleteCommand command) {

        return command.getIds().stream()
                .map(id -> deleteLevelPrivilege(Long.parseLong(id)))
                .collect(Collectors.toList());

    }
}
