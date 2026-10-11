package com.cskaoyan.wordmemorize.controller.app;

import com.cskaoyan.wordmemorize.common.result.Result;
import com.cskaoyan.wordmemorize.dto.admin.GoodsDTO;
import com.cskaoyan.wordmemorize.dto.admin.PageDTO;
import com.cskaoyan.wordmemorize.request.GoodsPageRequest;
import com.cskaoyan.wordmemorize.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product")
public class GoodsDataController {


    @Autowired
    GoodsService goodsService;

//    获取所有商品数据  (主要用于当用户点击购买单词上限时，展示商品列表)
    @GetMapping("")
    public Result<PageDTO<GoodsDTO>> getAllGoodsData(@RequestParam GoodsPageRequest goodsPageRequest){

        PageDTO<GoodsDTO> goodsPage = goodsService.getGoodsPage(goodsPageRequest);

        return Result.ok(goodsPage);
    }
}
