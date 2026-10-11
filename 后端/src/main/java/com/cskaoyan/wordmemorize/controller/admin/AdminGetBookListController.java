package com.cskaoyan.wordmemorize.controller.admin;


import com.cskaoyan.wordmemorize.common.aspect.operate.OperateLog;
import com.cskaoyan.wordmemorize.common.aspect.operate.OperateLogConstant;
import com.cskaoyan.wordmemorize.common.result.Result;
import com.cskaoyan.wordmemorize.dto.admin.DeleteBookOperateDTO;
import com.cskaoyan.wordmemorize.dto.admin.PageDTO;
import com.cskaoyan.wordmemorize.dto.admin.VocBookDTO;
import com.cskaoyan.wordmemorize.request.VocBookCommand;
import com.cskaoyan.wordmemorize.request.VocBookPageRequest;
import com.cskaoyan.wordmemorize.service.VocBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("admin/voc/book")
public class AdminGetBookListController {

    @Autowired
    VocBookService vocBookService;

    @GetMapping("list")
    public Result<PageDTO<VocBookDTO>> getBookList(VocBookPageRequest vocBookPageRequest){
        PageDTO<VocBookDTO> bookPage = vocBookService.getBookPage(vocBookPageRequest);
        return Result.ok(bookPage);
    }

//    http://localhost:8111/admin/voc/book
    @OperateLog(dateType = "添加书籍", operateType = OperateLogConstant.OPERATE_ADD)
    @PostMapping("")
    public Result addBook(@RequestBody VocBookCommand vocBookCommand){
        Long l = vocBookService.addVocBook(vocBookCommand);
        return Result.ok(l);
    }

    @OperateLog(dateType = "修改书籍", operateType = OperateLogConstant.OPERATE_UPDATE)
    @PutMapping("")
    public Result updateBook(@RequestBody VocBookCommand vocBookCommand){

        vocBookService.updateVocBook(vocBookCommand);

        return Result.ok();
    }

//    http://localhost:8111/admin/voc/book
//    指定Id删除书籍信息
    @OperateLog(dateType = "删除指定书籍", operateType = OperateLogConstant.OPERATE_SINGLE_DELETE)
    @DeleteMapping("{bookId}")
    public Result deleteByBookId(@PathVariable Long bookId){
        DeleteBookOperateDTO deleteBookOperateDTO = vocBookService.deleteVocBook(bookId);
        return Result.ok(deleteBookOperateDTO);
    }












}
