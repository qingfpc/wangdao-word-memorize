package com.cskaoyan.wordmemorize.service;

import com.cskaoyan.wordmemorize.dto.admin.DeleteLevelOperateDTO;
import com.cskaoyan.wordmemorize.dto.admin.LevelDTO;
import com.cskaoyan.wordmemorize.dto.admin.PageDTO;
import com.cskaoyan.wordmemorize.request.BatchDeleteCommand;
import com.cskaoyan.wordmemorize.request.LevelCommand;
import com.cskaoyan.wordmemorize.request.PageRequest;

import java.util.List;

public interface LevelService {

    // 添加等级(后台等级权限管理-等级管理)
    void addLevel(LevelCommand command);
    // 修改等级(后台等级权限管理-等级管理)
    void updateLevel(LevelCommand command);
    // 分页查询等级(后台等级权限管理-等级管理)
    PageDTO<LevelDTO> getLevelList(PageRequest request);
    // 删除等级(后台等级权限管理-等级管理)
    DeleteLevelOperateDTO deleteLevel(Long id);
    // 批量删除等级(后台等级权限管理-等级管理)
    List<DeleteLevelOperateDTO> deleteLevels(BatchDeleteCommand command);
}
