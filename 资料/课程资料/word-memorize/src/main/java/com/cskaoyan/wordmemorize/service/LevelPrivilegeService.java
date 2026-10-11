package com.cskaoyan.wordmemorize.service;

import com.cskaoyan.wordmemorize.dto.admin.DeleteLevelPrivilegeOperateDTO;
import com.cskaoyan.wordmemorize.dto.admin.LevelPrivilegeDTO;
import com.cskaoyan.wordmemorize.dto.admin.PageDTO;
import com.cskaoyan.wordmemorize.request.BatchDeleteCommand;
import com.cskaoyan.wordmemorize.request.LevelPrivilegeCommand;
import com.cskaoyan.wordmemorize.request.LevelPrivilegePageRequest;

import java.util.List;

public interface LevelPrivilegeService {

    // 分页查询(后台等级权限管理-特权管理)
    PageDTO<LevelPrivilegeDTO> getLevelPrivilegeList(LevelPrivilegePageRequest pageRequest);
    // 添加等级特权(后台等级权限管理-特权管理)
    void addLevelPrivilege(LevelPrivilegeCommand command);
    // 更新等级特权(后台等级权限管理-特权管理)
    void updateLevelPrivilege(LevelPrivilegeCommand command);
    // 更新等级特权(后台等级权限管理-特权管理)
    DeleteLevelPrivilegeOperateDTO deleteLevelPrivilege(Long id);
    // 批量删除更新等级特权(后台等级权限管理-特权管理)
    List<DeleteLevelPrivilegeOperateDTO> batchDeleteLevelPrivilege(BatchDeleteCommand command);
}
