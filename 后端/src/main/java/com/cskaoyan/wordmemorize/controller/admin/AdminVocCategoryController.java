package com.cskaoyan.wordmemorize.controller.admin;

import com.cskaoyan.wordmemorize.common.aspect.operate.OperateLog;
import com.cskaoyan.wordmemorize.common.aspect.operate.OperateLogConstant;
import com.cskaoyan.wordmemorize.common.result.Result;
import com.cskaoyan.wordmemorize.dto.admin.PageDTO;
import com.cskaoyan.wordmemorize.dto.admin.VocCategoryDTO;
import com.cskaoyan.wordmemorize.request.BatchDeleteCommand;
import com.cskaoyan.wordmemorize.request.PageRequest;
import com.cskaoyan.wordmemorize.request.VocCategoryCommand;
import com.cskaoyan.wordmemorize.service.VocCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/voc")
public class AdminVocCategoryController {

    @Autowired
    VocCategoryService vocCategoryService;

    //添加书籍类目
    @OperateLog(dateType = "添加书籍类目", operateType = OperateLogConstant.OPERATE_ADD)
    @PostMapping("category")
    public Result addCategory(@RequestBody VocCategoryCommand command) {
        //暂存添加书籍类目之后返回的书籍类目Id
        Long categoryId = vocCategoryService.addBookCategory(command);
        return Result.ok("添加书籍类目成功");
    }

    //修改书籍类目
    @OperateLog(dateType = "修改书籍类目", operateType = OperateLogConstant.OPERATE_UPDATE)
    @PutMapping("category")
    public Result updateCategory(@RequestBody VocCategoryCommand command) {
        vocCategoryService.updateBookCategory(command);
        return Result.ok("修改书籍类目成功");
    }

    //分页查询书籍类目
    @GetMapping("category/list")
    public Result<PageDTO<VocCategoryDTO>> getCategoryList(PageRequest pageRequest) {

        PageDTO<VocCategoryDTO> bookCategories
                = vocCategoryService.getBookCategories(pageRequest);
        return Result.ok(bookCategories);
    }

    //删除指定的书籍类目
    @OperateLog(dateType = "删除指定的书籍类目", operateType = OperateLogConstant.OPERATE_SINGLE_DELETE)
    @DeleteMapping("category/{categoryId}")
    public Result deleteCategory(@PathVariable(name = "categoryId") Long categoryId) {
        vocCategoryService.deleteBookCategory(categoryId);
        return Result.ok("删除指定的书籍类目成功");
    }

    //批量删除书籍类目
    @OperateLog(dateType = "批量删除书籍类目", operateType = OperateLogConstant.OPERATE_BATCH_DELETE)
    @DeleteMapping("categories")
    public Result deleteCategoryList(@RequestBody BatchDeleteCommand batchDeleteCommand) {
        vocCategoryService.deleteBookCategories(batchDeleteCommand);
        return Result.ok("批量删除书籍类目成功");
    }

}
