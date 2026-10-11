package com.cskaoyan.wordmemorize.controller.admin;

import com.cskaoyan.wordmemorize.common.aspect.operate.OperateLog;
import com.cskaoyan.wordmemorize.common.aspect.operate.OperateLogConstant;
import com.cskaoyan.wordmemorize.common.result.Result;
import com.cskaoyan.wordmemorize.dto.admin.DeleteChapterOperateDTO;
import com.cskaoyan.wordmemorize.dto.admin.PageDTO;
import com.cskaoyan.wordmemorize.dto.admin.VocChapterDTO;
import com.cskaoyan.wordmemorize.request.BatchDeleteCommand;
import com.cskaoyan.wordmemorize.request.VocChapterCommand;
import com.cskaoyan.wordmemorize.request.VocChapterPageRequest;
import com.cskaoyan.wordmemorize.service.VocChapterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//章节管理
@RestController
@RequestMapping("/admin/voc")
public class AdminVocChapterController {
    //分页查询章节列表
    @Autowired
    VocChapterService vocChapterService;

    @GetMapping("chapters/list")
    public Result<PageDTO<VocChapterDTO>> getChapterPageList(VocChapterPageRequest vocChapterPageRequest) {

        PageDTO<VocChapterDTO> chapterPageList = vocChapterService.getChapterPage(vocChapterPageRequest);
        return Result.ok(chapterPageList);
    }

    //添加章节
    @OperateLog(dateType = "添加章节", operateType = OperateLogConstant.OPERATE_ADD)
    @PostMapping("/chapter")
    public Result addChapter(@RequestBody VocChapterCommand vocChapterCommand) {
        vocChapterService.addChapter(vocChapterCommand);
        return Result.ok();
    }

    //修改章节
    @OperateLog(dateType = "修改章节", operateType = OperateLogConstant.OPERATE_UPDATE)
    @PutMapping("/chapter")
    public Result updateChapter(@RequestBody VocChapterCommand vocChapterCommand) {
        vocChapterService.updateChapter(vocChapterCommand);
        return Result.ok();
    }

    //批量删除章节
    @OperateLog(dateType = "批量删除章节", operateType = OperateLogConstant.OPERATE_BATCH_DELETE)
    @DeleteMapping("chapters")
    public Result deleteVocChapters(@RequestBody BatchDeleteCommand batchDeleteCommand) {
        vocChapterService.deleteChapters(batchDeleteCommand);
        return Result.ok();
    }


    @OperateLog(dateType = "删除指定章节", operateType = OperateLogConstant.OPERATE_SINGLE_DELETE)
    @DeleteMapping("/chapter/{chapterId}")
    public Result deleteVocChapterById(@PathVariable("chapterId") Long chapterId){
        DeleteChapterOperateDTO dto = vocChapterService.deleteChapter(chapterId);
        return Result.ok(dto);
    }

}
