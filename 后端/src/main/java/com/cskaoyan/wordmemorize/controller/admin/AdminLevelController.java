package com.cskaoyan.wordmemorize.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cskaoyan.wordmemorize.common.aspect.operate.OperateLog;
import com.cskaoyan.wordmemorize.common.aspect.operate.OperateLogConstant;
import com.cskaoyan.wordmemorize.common.result.Result;
import com.cskaoyan.wordmemorize.dao.entity.LevelDO;
import com.cskaoyan.wordmemorize.dto.admin.LevelDTO;
import com.cskaoyan.wordmemorize.dto.admin.PageDTO;
import com.cskaoyan.wordmemorize.request.BatchDeleteCommand;
import com.cskaoyan.wordmemorize.request.LevelCommand;
import com.cskaoyan.wordmemorize.request.PageRequest;
import com.cskaoyan.wordmemorize.service.LevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/level")
public class AdminLevelController {


    @Autowired
    LevelService levelService;

    //添加等级
    @OperateLog(dateType = "添加等级", operateType = OperateLogConstant.OPERATE_ADD)
    @PostMapping("")
    public Result addLevel(@RequestBody LevelCommand levelCommand){
        levelService.addLevel(levelCommand);
        return Result.ok("添加等级成功");
    }

    //   获取等级列表
    @GetMapping("list")
    public Result<PageDTO<LevelDTO>> getLevelList(PageRequest pageRequest){
        PageDTO<LevelDTO> levelList = levelService.getLevelList(pageRequest);
        return Result.ok(levelList);
    }

    //更新等级
    @OperateLog(dateType = "更新等级", operateType = OperateLogConstant.OPERATE_UPDATE)
    @PutMapping("")
    public Result updateLevel(@RequestBody LevelCommand levelCommand){
        levelService.updateLevel(levelCommand);
        return Result.ok("等级更新成功");
    }

    //删除等级
    @OperateLog(dateType = "删除等级", operateType = OperateLogConstant.OPERATE_SINGLE_DELETE)
    @DeleteMapping("/{id}")
    public Result deleteLevel(@PathVariable("id") Long id){
        levelService.deleteLevel(id);
        return Result.ok("等级删除成功");
    }

    //批量删除等级
    @OperateLog(dateType = "批量删除等级", operateType = OperateLogConstant.OPERATE_BATCH_DELETE)
    @DeleteMapping("/batch")
    public Result deleteLevels(@RequestBody BatchDeleteCommand command){
        levelService.deleteLevels(command);
        return Result.ok("等级批量删除成功");
    }
}




























