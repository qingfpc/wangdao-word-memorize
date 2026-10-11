package com.cskaoyan.wordmemorize.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cskaoyan.wordmemorize.converter.AIReadConverter;
import com.cskaoyan.wordmemorize.dao.entity.UserVocAISituationDO;
import com.cskaoyan.wordmemorize.dao.entity.UserVocAISituationOptionDO;
import com.cskaoyan.wordmemorize.dao.mapper.UserVocAISituationMapper;
import com.cskaoyan.wordmemorize.dao.mapper.UserVocAISituationOptionMapper;
import com.cskaoyan.wordmemorize.dto.admin.PageDTO;
import com.cskaoyan.wordmemorize.dto.admin.UserVocAISituationDTO;
import com.cskaoyan.wordmemorize.request.PageRequest;
import com.cskaoyan.wordmemorize.request.UserVocAISituationCommand;
import com.cskaoyan.wordmemorize.service.AIReadService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AIReadServiceImpl implements AIReadService {


    @Resource
    AIReadConverter readConverter;

    @Resource
    UserVocAISituationMapper userVocAISituationMapper;

    @Resource
    UserVocAISituationOptionMapper userVocAISituationOptionMapper;

    @Override
    @Transactional
    public void addAISituation(UserVocAISituationCommand command) {

        UserVocAISituationDO userVocAISituationDO
                = readConverter.userVocAISituationCommandToDO(command);

        userVocAISituationMapper.insert(userVocAISituationDO);

        List<UserVocAISituationOptionDO> options = userVocAISituationDO.getOptions();
        options.forEach(option -> {
            option.setSituationId(userVocAISituationDO.getId());
            userVocAISituationOptionMapper.insert(option);
        });
    }

    @Override
    @Transactional
    public void updateAISituation(UserVocAISituationCommand command) {
        UserVocAISituationDO userVocAISituationDO
                = readConverter.userVocAISituationCommandToDO(command);

        userVocAISituationMapper.updateById(userVocAISituationDO);

        LambdaQueryWrapper<UserVocAISituationOptionDO> userVocAISituationOptionDOWrapper
                = new LambdaQueryWrapper<>();

        userVocAISituationDO.getOptions().forEach(
                option -> {
                    if (option.getId() == null) {
                        // 新增
                        option.setSituationId(userVocAISituationDO.getId());
                        userVocAISituationOptionMapper.insert(option);
                        return;
                    }
                    // 更新
                    userVocAISituationOptionMapper.updateById(option);
                }
        );

    }

    @Override
    @Transactional
    public void deleteAISituation(Long id) {
        userVocAISituationMapper.deleteById(id);

        LambdaQueryWrapper<UserVocAISituationOptionDO> userVocAISituationOptionDOWrapper = new LambdaQueryWrapper<>();
        userVocAISituationOptionDOWrapper.eq(UserVocAISituationOptionDO::getSituationId, id);
        userVocAISituationOptionMapper.delete(userVocAISituationOptionDOWrapper);
    }

    @Override
    public void deleteAISituationOption(Long id) {
        userVocAISituationOptionMapper.deleteById(id);
    }

    @Override
    @Transactional
    public void batchDeleteAISituationOption(List<Long> ids) {
        userVocAISituationMapper.deleteBatchIds(ids);

        LambdaQueryWrapper<UserVocAISituationOptionDO> userVocAISituationOptionDOWrapper = new LambdaQueryWrapper<>();
        userVocAISituationOptionDOWrapper.in(UserVocAISituationOptionDO::getSituationId, ids);
        userVocAISituationOptionMapper.delete(userVocAISituationOptionDOWrapper);

    }

    @Override
    public PageDTO<UserVocAISituationDTO> getAISituationList(PageRequest pageRequest) {
        LambdaQueryWrapper<UserVocAISituationDO> userVocAISituationWrapper = new LambdaQueryWrapper<>();
        if (pageRequest.getKeyword() != null) {
            userVocAISituationWrapper.like(UserVocAISituationDO::getName, pageRequest.getKeyword());
        }

        Integer offset = (pageRequest.getPageNum() - 1) * pageRequest.getCount();
        List<UserVocAISituationDO> records = userVocAISituationMapper.getAISituationPage(offset, pageRequest.getCount());
        Long total = userVocAISituationMapper.selectCount(null);
        return readConverter.toUserVocAISituationPage(records, total);
    }
}
