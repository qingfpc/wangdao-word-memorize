package com.cskaoyan.wordmemorize.controller.admin;

import com.cskaoyan.wordmemorize.common.aspect.operate.OperateLog;
import com.cskaoyan.wordmemorize.common.aspect.operate.OperateLogConstant;
import com.cskaoyan.wordmemorize.common.result.Result;
import com.cskaoyan.wordmemorize.dto.admin.PageDTO;
import com.cskaoyan.wordmemorize.dto.admin.UserVocAISituationDTO;
import com.cskaoyan.wordmemorize.request.BatchDeleteCommand;
import com.cskaoyan.wordmemorize.request.PageRequest;
import com.cskaoyan.wordmemorize.request.UserVocAISituationCommand;
import com.cskaoyan.wordmemorize.service.AIReadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("admin/voc/ai-read")
public class AdminVocAiReadController {


    @Autowired
    AIReadService aiReadService;

    //添加ai阅读模式
    @OperateLog(dateType = "添加AI阅读模式", operateType = OperateLogConstant.OPERATE_ADD)
    @PostMapping("situation")
    public Result addAiReadSituation(@RequestBody  UserVocAISituationCommand command){
        aiReadService.addAISituation(command);
        return Result.ok("添加ai阅读模式成功");
    }

    //修改指定ai阅读的模式
    @OperateLog(dateType = "修改指定AI阅读的模式", operateType = OperateLogConstant.OPERATE_UPDATE)
    @PutMapping("situation")
    public Result updateAiReadSituation(@RequestBody UserVocAISituationCommand command){
        aiReadService.updateAISituation(command);
        return Result.ok("修改指定ai阅读的模式成功");
    }

    //批量删除ai阅读模式
    @OperateLog(dateType = "批量删除AI阅读模式", operateType = OperateLogConstant.OPERATE_BATCH_DELETE)
    @DeleteMapping("situation")
    public Result batchDeleteAiReadMode(@RequestBody BatchDeleteCommand command){
        //从command中获取String类型的ids列表
        List<String> ids = command.getIds();
        //通过stream流将List<String>转换为List<Long>
        List<Long> LongIds = ids.stream().map(Long::valueOf)
                .toList();
        aiReadService.batchDeleteAISituationOption(LongIds);
        return Result.ok("批量删除ai阅读模式成功");
    }

    //获取ai阅读的场景列表
    @GetMapping("situation/list")
    public Result<PageDTO<UserVocAISituationDTO>> getAiReadSituationList(PageRequest pageRequest){
        PageDTO<UserVocAISituationDTO> aiSituationList = aiReadService.getAISituationList(pageRequest);
        return Result.ok(aiSituationList);
    }

    //删除指定的ai阅读模式
    @OperateLog(dateType = "删除指定的AI阅读模式", operateType = OperateLogConstant.OPERATE_SINGLE_DELETE)
    @DeleteMapping("situation/{id}")
    public Result deleteAiReadMode(@PathVariable(name="id") Long id){
        aiReadService.deleteAISituation(id);
        return Result.ok("删除指定的ai阅读模式成功");
    }

    //删除指定的ai阅读模式的选项
    @OperateLog(dateType = "删除指定的AI阅读模式的选项", operateType = OperateLogConstant.OPERATE_SINGLE_DELETE)
    @DeleteMapping("option/{id}")
    public Result deleteAiReadOption(@PathVariable(name="id") Long id){
        aiReadService.deleteAISituationOption(id);
        return Result.ok("删除指定的ai阅读模式的选项成功");
    }
}
