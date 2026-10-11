package com.cskaoyan.wordmemorize.controller.admin;

import com.cskaoyan.wordmemorize.common.aspect.operate.OperateLog;
import com.cskaoyan.wordmemorize.common.aspect.operate.OperateLogConstant;
import com.cskaoyan.wordmemorize.common.result.Result;
import com.cskaoyan.wordmemorize.dto.admin.DeleteLevelPrivilegeOperateDTO;
import com.cskaoyan.wordmemorize.dto.admin.LevelPrivilegeDTO;
import com.cskaoyan.wordmemorize.dto.admin.PageDTO;
import com.cskaoyan.wordmemorize.request.BatchDeleteCommand;
import com.cskaoyan.wordmemorize.request.LevelPrivilegeCommand;
import com.cskaoyan.wordmemorize.request.LevelPrivilegePageRequest;
import com.cskaoyan.wordmemorize.service.LevelPrivilegeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/privilege")
public class AdminPrivilegeController {

    @Autowired
    LevelPrivilegeService levelPrivilegeService;

    //    获取等级特权列表
    @GetMapping("list")
    public Result<PageDTO<LevelPrivilegeDTO>> getLevelPrivilegeList(LevelPrivilegePageRequest privilegePageRequest) {

        PageDTO<LevelPrivilegeDTO> levelPrivilegeList =
                levelPrivilegeService.getLevelPrivilegeList(privilegePageRequest);

        return Result.ok(levelPrivilegeList);
    }


    //    添加等级特权
    @OperateLog(dateType = "添加等级特权", operateType = OperateLogConstant.OPERATE_ADD)
    @PostMapping("")
    public Result addLevelPrivilege(@RequestBody LevelPrivilegeCommand levelPrivilegeCommand) {

        levelPrivilegeService.addLevelPrivilege(levelPrivilegeCommand);

        return Result.ok("等级特权已添加");
    }


    //    修改等级特权
    @OperateLog(dateType = "修改等级特权", operateType = OperateLogConstant.OPERATE_UPDATE)
    @PutMapping("")
    public Result updateLevelPrivilege(@RequestBody LevelPrivilegeCommand levelPrivilegeCommand) {

        levelPrivilegeService.updateLevelPrivilege(levelPrivilegeCommand);

        return Result.ok("等级特权已修改");
    }


    //    删除指定等级特权
    @OperateLog(dateType = "删除指定等级特权", operateType = OperateLogConstant.OPERATE_SINGLE_DELETE)
    @DeleteMapping("{id}")
    public Result deleteAppointLevelPrivilege(Long id) {

        levelPrivilegeService.deleteLevelPrivilege(id);

        return Result.ok("指定等级特权已删除");
    }


    //    批量删除等级特权
    @OperateLog(dateType = "添加商品", operateType = OperateLogConstant.OPERATE_BATCH_DELETE)
    @DeleteMapping("batch")
    public Result batchDeleteLevelPrivilege(@RequestBody BatchDeleteCommand batchDeleteCommand) {

        levelPrivilegeService.batchDeleteLevelPrivilege(batchDeleteCommand);

        return Result.ok("等级特权已批量删除");
    }
}
