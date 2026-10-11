package com.cskaoyan.wordmemorize.controller.admin;

import com.cskaoyan.wordmemorize.common.aspect.operate.OperateLog;
import com.cskaoyan.wordmemorize.common.aspect.operate.OperateLogConstant;
import com.cskaoyan.wordmemorize.common.result.Result;
import com.cskaoyan.wordmemorize.dto.admin.DeleteVocOperateDTO;
import com.cskaoyan.wordmemorize.dto.admin.PageDTO;
import com.cskaoyan.wordmemorize.dto.admin.VocDTO;
import com.cskaoyan.wordmemorize.dto.app.DisplayBookVocDTO;
import com.cskaoyan.wordmemorize.dto.app.DisplayChapterDTO;
import com.cskaoyan.wordmemorize.request.AppVocPageRequest;
import com.cskaoyan.wordmemorize.request.BatchDeleteCommand;
import com.cskaoyan.wordmemorize.request.PageRequest;
import com.cskaoyan.wordmemorize.request.VocCommand;
import com.cskaoyan.wordmemorize.service.VocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Array;
import java.util.List;

@RestController
@RequestMapping("/admin/voc")
public class AdminVocController {


    @Autowired
    VocService vocService;



    //添加单词
    @OperateLog(dateType = "添加单词", operateType = OperateLogConstant.OPERATE_ADD)
    @PostMapping("")
    public Result addVoc(@RequestBody VocCommand vocCommand) {

        vocService.addVoc(vocCommand);

        return Result.ok("添加单词成功");
    }
    //修改单词
    @OperateLog(dateType = "修改单词", operateType = OperateLogConstant.OPERATE_UPDATE)
    @PutMapping("")
    public Result updateVoc(@RequestBody VocCommand vocCommand) {

        vocService.updateVoc(vocCommand);

        return Result.ok("修改单词成功");
    }
    //删除指定单词
    @OperateLog(dateType = "删除指定单词", operateType = OperateLogConstant.OPERATE_SINGLE_DELETE)
    @DeleteMapping("/{vocId}")
    public Result deleteVoc(@PathVariable("vocId") String vocId) {
        try {
            // 检查是否为"null"字符串
            if ("null".equalsIgnoreCase(vocId)) {
                return Result.fail("单词ID不能为null");
            }

            // 转换为Long类型
            Long id = Long.parseLong(vocId);

            DeleteVocOperateDTO deleteVocOperateDTO = vocService.deleteVocabulary(id);
            return Result.ok(deleteVocOperateDTO);
        } catch (NumberFormatException e) {
            return Result.fail("单词ID必须是有效的数字");
        }
    }
    //批量删除单词
    @OperateLog(dateType = "批量删除单词", operateType = OperateLogConstant.OPERATE_BATCH_DELETE)
    @DeleteMapping("")
    public Result deleteVocs(@RequestBody BatchDeleteCommand batchDeleteCommand) {
        List<DeleteVocOperateDTO> deleteVocOperateDTOList = vocService.deleteVocabularies(batchDeleteCommand);
        return Result.ok(deleteVocOperateDTOList);

    }

}
