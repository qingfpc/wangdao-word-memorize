package com.cskaoyan.wordmemorize.controller.admin;

import com.cskaoyan.wordmemorize.common.aspect.operate.OperateLog;
import com.cskaoyan.wordmemorize.common.aspect.operate.OperateLogConstant;
import com.cskaoyan.wordmemorize.common.result.Result;
import com.cskaoyan.wordmemorize.dto.admin.DeleteBookOperateDTO;
import com.cskaoyan.wordmemorize.request.BatchDeleteCommand;
import com.cskaoyan.wordmemorize.service.VocBookService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("admin/voc/books")
public class AdminDeleteVocBooksController {

    @Resource
    VocBookService vocBookService;

    @OperateLog(dateType = "批量删除书籍", operateType = OperateLogConstant.OPERATE_BATCH_DELETE)
    @DeleteMapping("")
    public Result deleteVocBooks(@RequestBody BatchDeleteCommand batchDeleteCommand){
        List<DeleteBookOperateDTO> deleteBookOperateDTOS = vocBookService.deleteVocBooks(batchDeleteCommand);
        return Result.ok(deleteBookOperateDTOS);
    }
}
