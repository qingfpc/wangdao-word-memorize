package com.cskaoyan.wordmemorize.controller.admin;

import com.cskaoyan.wordmemorize.auth.SaAdminCheckPermission;
import com.cskaoyan.wordmemorize.common.result.Result;
import com.cskaoyan.wordmemorize.dto.admin.GoodsDTO;
import com.cskaoyan.wordmemorize.dto.admin.PageDTO;
import com.cskaoyan.wordmemorize.request.GoodsCommand;
import com.cskaoyan.wordmemorize.request.BatchDeleteCommand;
import com.cskaoyan.wordmemorize.request.GoodsPageRequest;
import com.cskaoyan.wordmemorize.request.PageRequest;
import com.cskaoyan.wordmemorize.service.GoodsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@Tag(name = "后台管理-商品相关功能")
@RestController
@RequestMapping("admin")
public class AdminGoodsController {


    @Resource
    GoodsService goodsService;

    @Operation(summary = "**获取商品分页列表",
            description = "主要用于户商品管理",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "返回商品分页列表"

                    ),
                    @ApiResponse(responseCode = "500",
                            description = "请求失败"
                    )
            }

    )
    @GetMapping("goods/list")
    public Result<PageDTO<GoodsDTO>> getGoods(GoodsPageRequest pageRequest) {
        PageDTO<GoodsDTO> goodsPage = goodsService.getGoodsPage(pageRequest);
        return Result.ok(goodsPage);
    }

    @Operation(summary = "*添加商品",
            description = "主要用户商品管理",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "返回商品分页列表"

                    ),
                    @ApiResponse(responseCode = "500",
                            description = "请求失败"
                    )
            }

    )
    @PostMapping("goods")
    public Result addGoods(@RequestBody GoodsCommand command) {
        goodsService.addGoods(command);
        return Result.ok();
    }

    @Operation(summary = "*修改商品",
            description = "主要用户商品管理",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "添加成功"

                    ),
                    @ApiResponse(responseCode = "500",
                            description = "请求失败"
                    )
            }

    )
    @PutMapping("goods")
    public Result updateGoods(@RequestBody GoodsCommand command) {
        goodsService.updateGoods(command);
        return Result.ok();
    }

    @Operation(summary = "*删除商品",
            description = "主要用于户商品管理",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "删除成功"

                    ),
                    @ApiResponse(responseCode = "500",
                            description = "请求失败"
                    )
            }

    )
    @DeleteMapping("goods/{goodsId}")
    public Result deleteGoods(@PathVariable Long goodsId) {
        goodsService.deleteGoods(goodsId);
        return Result.ok();
    }

    @Operation(summary = "*删除选中商品",
            description = "主要用于户商品管理",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "删除成功"

                    ),
                    @ApiResponse(responseCode = "500",
                            description = "请求失败"
                    )
            }

    )
    @DeleteMapping("goods/selected")
    public Result deleteGoodsSelected(@RequestBody BatchDeleteCommand command) {
        goodsService.deleteGoodsSelected(command);
        return Result.ok();
    }

    @Operation(summary = "*修改商品状态",
            description = "主要用于户商品管理",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "删除成功"

                    ),
                    @ApiResponse(responseCode = "500",
                            description = "请求失败"
                    )
            }

    )
    @PutMapping("goods/status/{goodsId}")
    public Result updateGoodsStatus(@PathVariable Long goodsId, Integer status) {
        goodsService.updateGoodsStatus(goodsId, status);
        return Result.ok();
    }
}
