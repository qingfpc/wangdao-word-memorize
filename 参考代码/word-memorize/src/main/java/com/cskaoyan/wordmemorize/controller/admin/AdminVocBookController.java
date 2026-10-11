package com.cskaoyan.wordmemorize.controller.admin;

import com.cskaoyan.wordmemorize.auth.SaAdminCheckPermission;
import com.cskaoyan.wordmemorize.common.result.Result;
import com.cskaoyan.wordmemorize.dto.admin.PageDTO;
import com.cskaoyan.wordmemorize.dto.admin.VocBookDTO;
import com.cskaoyan.wordmemorize.request.VocBookCommand;
import com.cskaoyan.wordmemorize.request.BatchDeleteCommand;
import com.cskaoyan.wordmemorize.request.VocBookPageRequest;
import com.cskaoyan.wordmemorize.service.VocBookService;
//import org.apache.shiro.authz.annotation.RequiresPermissions;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@Tag(name = "后台管理-物料管理-书籍管理相关接口")
@RestController
@RequestMapping("admin/voc")
public class AdminVocBookController {

    @Resource
    VocBookService vocBookService;

    @Operation(summary = "**获取书籍列表",
            description = "主要用于物料管理-书籍管理",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "登出成功"

                    ),
                    @ApiResponse(responseCode = "500",
                            description = "请求失败"
                    )
            }

    )
    @GetMapping("book/list")
    @SaAdminCheckPermission("admin:book:search")
    public Result<PageDTO<VocBookDTO>> getVocBooks(VocBookPageRequest pageQuery) {
        PageDTO<VocBookDTO> bookPage = vocBookService.getBookPage(pageQuery);
        return Result.ok(bookPage);
    }

    @Operation(summary = "*添加书籍",
            description = "主要用于物料管理-书籍管理",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "登出成功"

                    ),
                    @ApiResponse(responseCode = "500",
                            description = "请求失败"
                    )
            }

    )
    @PostMapping("book")
    @SaAdminCheckPermission("admin:book:add")
    public Result addVocBook(@RequestBody VocBookCommand command) {
        vocBookService.addVocBook(command);
        return Result.ok();
    }

    @Operation(summary = "*修改书籍",
            description = "主要用于物料管理-书籍管理",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "登出成功"

                    ),
                    @ApiResponse(responseCode = "500",
                            description = "请求失败"
                    )
            }

    )
    @PutMapping("book")
    @SaAdminCheckPermission("admin:book:update")
    public Result updateBook(@RequestBody VocBookCommand command) {
        vocBookService.updateVocBook(command);
        return Result.ok();
    }

    @Operation(summary = "***删除指定书籍",
            description = "主要用于物料管理-书籍管理",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "登出成功"

                    ),
                    @ApiResponse(responseCode = "500",
                            description = "请求失败"
                    )
            }

    )
    @DeleteMapping("book/{bookId}")
    @SaAdminCheckPermission("admin:book:delete")
    public Result deleteBook(@PathVariable @Parameter(description = "书籍id") Long bookId) {
        vocBookService.deleteVocBook(bookId);
        return Result.ok();
    }

    @Operation(summary = "***批量删除书籍",
            description = "主要用于物料管理-书籍管理",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "登出成功"

                    ),
                    @ApiResponse(responseCode = "500",
                            description = "请求失败"
                    )
            }

    )

    @DeleteMapping("books")
    @SaAdminCheckPermission("admin:book:batchdelete")
    public Result deleteBooks(@RequestBody BatchDeleteCommand command) {
        vocBookService.deleteVocBooks(command);
        return Result.ok();
    }
}
