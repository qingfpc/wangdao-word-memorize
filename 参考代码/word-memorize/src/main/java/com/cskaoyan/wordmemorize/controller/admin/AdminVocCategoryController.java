package com.cskaoyan.wordmemorize.controller.admin;

import com.cskaoyan.wordmemorize.auth.SaAdminCheckPermission;
import com.cskaoyan.wordmemorize.common.result.Result;
import com.cskaoyan.wordmemorize.dto.admin.PageDTO;
import com.cskaoyan.wordmemorize.dto.admin.VocCategoryDTO;
import com.cskaoyan.wordmemorize.request.BatchDeleteCommand;
import com.cskaoyan.wordmemorize.request.VocCategoryCommand;
import com.cskaoyan.wordmemorize.request.PageRequest;
import com.cskaoyan.wordmemorize.service.VocCategoryService;
//import org.apache.shiro.authz.annotation.RequiresPermissions;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@Tag(name = "后台管理-物料管理-书籍类目管理相关接口")
@RestController
@RequestMapping("admin/voc")
public class AdminVocCategoryController {

    @Resource
    VocCategoryService bookCategoryService;

    @Operation(summary = "*添加书籍类目",
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
    @PostMapping("category")
    @SaAdminCheckPermission("admin:category:add")
    public Result addCategory(@RequestBody VocCategoryCommand command) {
        bookCategoryService.addBookCategory(command);
        return Result.ok();
    }

//    @Operation(summary = "*获取指定的书籍类目",
//            description = "主要用于物料管理-书籍管理",
//            responses = {
//                    @ApiResponse(
//                            responseCode = "200",
//                            description = "登出成功"
//
//                    ),
//                    @ApiResponse(responseCode = "500",
//                            description = "请求失败"
//                    )
//            }
//
//    )
//    @GetMapping("category")
//    @SaAdminCheckPermission("admin:category:search")
//    public Result<VocCategoryDTO> getCategory(@Parameter(description = "书籍类目id") Long categoryId) {
//        VocCategoryDTO category = bookCategoryService.getBookCategory(categoryId);
//        return Result.ok(category);
//    }

    @Operation(summary = "**分页查询书籍类目",
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
    @GetMapping("category/list")
    @SaAdminCheckPermission("admin:category:search")
    public Result<PageDTO<VocCategoryDTO>> getCategories(PageRequest pageQuery) {
        PageDTO<VocCategoryDTO> categories = bookCategoryService.getBookCategories(pageQuery);
        return Result.ok(categories);
    }

    @Operation(summary = "*修改书籍类目",
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
    @PutMapping("category")
    @SaAdminCheckPermission("admin:category:update")
    public Result updateCategory(@RequestBody VocCategoryCommand command) {
        bookCategoryService.updateBookCategory(command);
        return Result.ok();
    }

    @Operation(summary = "***删除指定的书籍类目",
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
    @DeleteMapping("category/{categoryId}")
    @SaAdminCheckPermission("admin:category:delete")
    public Result deleteCategory(@PathVariable @Parameter(description = "类目id") Long categoryId) {
        bookCategoryService.deleteBookCategory(categoryId);
        return Result.ok();
    }
    @Operation(summary = "***批量删除书籍类目",
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
    @DeleteMapping("categories")
    @SaAdminCheckPermission("admin:category:batchdelete")
    public Result deleteCategories(@RequestBody BatchDeleteCommand command) {
        bookCategoryService.deleteBookCategories(command);
        return Result.ok();
    }

}
