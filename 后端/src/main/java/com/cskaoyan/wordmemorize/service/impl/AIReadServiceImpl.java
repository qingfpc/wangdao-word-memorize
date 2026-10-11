package com.cskaoyan.wordmemorize.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cskaoyan.wordmemorize.dao.entity.UserVocAISituationDO;
import com.cskaoyan.wordmemorize.dao.entity.UserVocAISituationOptionDO;
import com.cskaoyan.wordmemorize.dao.mapper.UserVocAISituationMapper;
import com.cskaoyan.wordmemorize.dao.mapper.UserVocAISituationOptionMapper;
import com.cskaoyan.wordmemorize.dto.admin.PageDTO;
import com.cskaoyan.wordmemorize.dto.admin.UserVocAISituationDTO;
import com.cskaoyan.wordmemorize.dto.admin.UserVocAISituationOptionDTO;
import com.cskaoyan.wordmemorize.dto.app.AIReviewReadDTO;
import com.cskaoyan.wordmemorize.request.AIReviewReadCommand;
import com.cskaoyan.wordmemorize.request.PageRequest;
import com.cskaoyan.wordmemorize.request.UserVocAISituationCommand;
import com.cskaoyan.wordmemorize.request.UserVocAISituationOptionCommand;
import com.cskaoyan.wordmemorize.service.AIReadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AIReadServiceImpl implements AIReadService {

    @Autowired
    UserVocAISituationMapper userVocAISituationMapper;
    @Autowired
    UserVocAISituationOptionMapper userVocAISituationOptionMapper;

    //添加ai阅读模式
    @Override
    public void addAISituation(UserVocAISituationCommand command) {
        //添加模式
        UserVocAISituationDO userVocAISituationDO = new UserVocAISituationDO();
        userVocAISituationDO.setName(command.getName());
        userVocAISituationDO.setStatus(command.getStatus());
        userVocAISituationDO.setDescription(command.getDescription());
        userVocAISituationMapper.insert(userVocAISituationDO);
        //添加选项
        List<UserVocAISituationOptionCommand> options = command.getOptions();
        if(options!=null){
            options.forEach(option -> {
                UserVocAISituationOptionDO userVocAISituationOptionDO = new UserVocAISituationOptionDO();
                userVocAISituationOptionDO.setSituationId(userVocAISituationDO.getId());
                userVocAISituationOptionDO.setName(option.getName());
                userVocAISituationOptionDO.setDescription(option.getDescription());
                userVocAISituationOptionMapper.insert(userVocAISituationOptionDO);
            });
        }
    }

    //修改指定ai阅读的模式
    @Override
    public void updateAISituation(UserVocAISituationCommand command) {
        UpdateWrapper<UserVocAISituationDO> updateWrapper=new UpdateWrapper<>();
        updateWrapper.eq("id",command.getId())
                .set("name",command.getName())
                .set("status",command.getStatus())
                .set("description",command.getDescription());
        userVocAISituationMapper.update(null,updateWrapper);
        //更新选项
        List<UserVocAISituationOptionCommand> options = command.getOptions();
        if(options!=null){
            options.forEach(option -> {
                UpdateWrapper<UserVocAISituationOptionDO> updateWrapper1 = new UpdateWrapper<>();
                updateWrapper1.eq("id", option.getId())
                        .set("name", option.getName())
                        .set("description", option.getDescription());
                userVocAISituationOptionMapper.update(null, updateWrapper1);
            });
        }
    }

    //删除指定的ai阅读模式
    @Override
    public void deleteAISituation(Long id) {
        userVocAISituationMapper.deleteById(id);
        Map<String,Object> param=new HashMap<>();
        param.put("situation_id",id);
        userVocAISituationOptionMapper.deleteByMap(param);
    }

    //删除指定的ai阅读模式的选项
    @Override
    public void deleteAISituationOption(Long id) {
        userVocAISituationOptionMapper.deleteById(id);
    }

    //批量删除指定的ai阅读模式的选项
    @Override
    public void batchDeleteAISituationOption(List<Long> ids) {
        userVocAISituationOptionMapper.deleteByIds(ids);
    }

    //获取ai阅读的场景列表
    @Override
    public PageDTO<UserVocAISituationDTO> getAISituationList(PageRequest pageRequest) {
        // 分页对象
        Page<UserVocAISituationDO> page = new Page<>(pageRequest.getPageNum(), pageRequest.getCount());

        // 执行分页查询（无筛选条件）
        Page<UserVocAISituationDO> situationPage = userVocAISituationMapper.selectPage(page, null);

        // 将分页记录转为 DTO 列表
        List<UserVocAISituationDTO> dtoList = situationPage.getRecords().stream().map(situation -> {
            UserVocAISituationDTO dto = new UserVocAISituationDTO();
            dto.setId(situation.getId().toString());
            dto.setName(situation.getName());
            dto.setStatus(situation.getStatus());
            dto.setDescription(situation.getDescription());

            // 查询该场景下所有选项
            List<UserVocAISituationOptionDO> optionDOs = userVocAISituationOptionMapper.selectBySituationId(situation.getId());

            // 转换为 DTO
            List<UserVocAISituationOptionDTO> optionDTOs = optionDOs.stream().map(optionDO -> {
                UserVocAISituationOptionDTO optionDTO = new UserVocAISituationOptionDTO();
                optionDTO.setId(optionDO.getId().toString());
                optionDTO.setSituationId(optionDO.getSituationId().toString());
                optionDTO.setName(optionDO.getName());
                optionDTO.setDescription(optionDO.getDescription());
                return optionDTO;
            }).toList();

            dto.setOptions(optionDTOs);
            return dto;
        }).toList();

        PageDTO<UserVocAISituationDTO> result = new PageDTO<>();
        result.setPageList(dtoList);
        result.setTotalCount(situationPage.getTotal());
        return result;
    }
}
