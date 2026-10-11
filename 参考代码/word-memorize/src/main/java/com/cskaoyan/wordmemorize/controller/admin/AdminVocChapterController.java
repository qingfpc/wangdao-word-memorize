package com.cskaoyan.wordmemorize.controller.admin;

import com.cskaoyan.wordmemorize.auth.SaAdminCheckPermission;
import com.cskaoyan.wordmemorize.common.result.Result;
import com.cskaoyan.wordmemorize.dto.admin.PageDTO;
import com.cskaoyan.wordmemorize.dto.admin.VocChapterDTO;
import com.cskaoyan.wordmemorize.request.BatchDeleteCommand;
import com.cskaoyan.wordmemorize.request.VocChapterCommand;
import com.cskaoyan.wordmemorize.request.VocChapterPageRequest;
import com.cskaoyan.wordmemorize.service.VocChapterService;
//import org.apache.shiro.authz.annotation.RequiresPermissions;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@Tag(name = "后台管理-物料管理-章节管理相关接口")
@RestController
@RequestMapping("admin/voc")
public class AdminVocChapterController {

    @Resource
    VocChapterService vocChapterService;

    @Operation(summary = "**分页查询章节列表",
            description = "主要用于物料管理-章节管理",
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
    @GetMapping("chapters/list")
    @SaAdminCheckPermission("admin:chapter:search")
    public Result<PageDTO<VocChapterDTO>> getChapters(VocChapterPageRequest pageQuery) {
        PageDTO<VocChapterDTO> chapterPage = vocChapterService.getChapterPage(pageQuery);
        return Result.ok(chapterPage);
    }

    @Operation(summary = "*添加章节",
            description = "主要用于物料管理-章节管理",
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
    @PostMapping("chapter")
    @SaAdminCheckPermission("admin:chapter:add")
    public Result addChapter(@RequestBody VocChapterCommand command) {
        vocChapterService.addChapter(command);
        return Result.ok();
    }

    @Operation(summary = "*修改章节",
            description = "主要用于物料管理-章节管理",
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
    @PutMapping("chapter")
    @SaAdminCheckPermission("admin:chapter:update")
    public Result updateChapter(@RequestBody VocChapterCommand command) {
        vocChapterService.updateChapter(command);
        return Result.ok();
    }
    @Operation(summary = "**删除指定章节",
            description = "主要用于物料管理-章节管理",
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
    @DeleteMapping("chapter/{chapterId}")
    @SaAdminCheckPermission("admin:chapter:delete")
    public Result deleteChapter(@PathVariable @Parameter(description = "章节id") Long chapterId) {
        vocChapterService.deleteChapter(chapterId);
        return Result.ok();
    }
    @Operation(summary = "***批量删除章节",
            description = "主要用于物料管理-章节管理",
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
    @DeleteMapping("chapters")
    @SaAdminCheckPermission("admin:chapter:batchdelete")
    public Result deleteChapters(@RequestBody BatchDeleteCommand command) {
        vocChapterService.deleteChapters(command);
        return Result.ok();
    }
}
