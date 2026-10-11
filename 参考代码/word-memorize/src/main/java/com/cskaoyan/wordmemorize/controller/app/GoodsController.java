package com.cskaoyan.wordmemorize.controller.app;

import com.cskaoyan.wordmemorize.common.result.Result;
import com.cskaoyan.wordmemorize.dto.admin.GoodsDTO;
import com.cskaoyan.wordmemorize.dto.admin.PageDTO;
import com.cskaoyan.wordmemorize.request.GoodsPageRequest;
import com.cskaoyan.wordmemorize.service.GoodsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "移动端-商品模块")
@RestController
@RequestMapping("product")
public class GoodsController {

    @Resource
    GoodsService goodsService;

    @Operation(summary = "*获取所有的商品数据",
            description = "主要用于当用户点击购买单词上限时，展示商品列表",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "成功"
                    ),
                    @ApiResponse(responseCode = "500",
                            description = "请求失败"
                    )
            }
    )
    @GetMapping
    public Result<PageDTO<GoodsDTO>> getGoodsList(GoodsPageRequest pageRequest) {
        PageDTO<GoodsDTO> goodsPage = goodsService.getGoodsPage(pageRequest);
        return Result.ok(goodsPage);
    }


}
