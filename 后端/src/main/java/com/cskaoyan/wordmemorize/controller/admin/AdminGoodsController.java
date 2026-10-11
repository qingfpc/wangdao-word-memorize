package com.cskaoyan.wordmemorize.controller.admin;


import com.cskaoyan.wordmemorize.common.aspect.operate.OperateLog;
import com.cskaoyan.wordmemorize.common.aspect.operate.OperateLogConstant;
import com.cskaoyan.wordmemorize.common.result.Result;
import com.cskaoyan.wordmemorize.dto.admin.DeleteGoodsOperateDTO;
import com.cskaoyan.wordmemorize.dto.admin.GoodsDTO;
import com.cskaoyan.wordmemorize.dto.admin.PageDTO;
import com.cskaoyan.wordmemorize.request.BatchDeleteCommand;
import com.cskaoyan.wordmemorize.request.GoodsCommand;
import com.cskaoyan.wordmemorize.request.GoodsPageRequest;
import com.cskaoyan.wordmemorize.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/goods")
public class AdminGoodsController {
    @Autowired
    GoodsService goodsService;


    //    获取商品分页列表
    @GetMapping("list")
    public Result<PageDTO<GoodsDTO>> getGoodsList(GoodsPageRequest goodsPageRequest) {
        PageDTO<GoodsDTO> goodsPage = goodsService.getGoodsPage(goodsPageRequest);
        return Result.ok(goodsPage);
    }

    //    添加商品
    @OperateLog(dateType = "添加商品", operateType = OperateLogConstant.OPERATE_ADD)
    @PostMapping("")
    public Result addGoods(@RequestBody GoodsCommand goodsCommand) {
        goodsService.addGoods(goodsCommand);
        return Result.ok();
    }

    //修改商品
    @OperateLog(dateType = "修改商品", operateType = OperateLogConstant.OPERATE_UPDATE)
    @PutMapping("")
    public Result updateGoods(@RequestBody GoodsCommand goodsCommand) {
        goodsService.updateGoods(goodsCommand);
        return Result.ok();
    }


    //    修改商品状态
    @OperateLog(dateType = "修改商品状态", operateType = OperateLogConstant.OPERATE_UPDATE_STATUS)
    @PutMapping("/status/{goodsId}")
    public Result updateGoodsStatusById(@PathVariable Long goodsId, @RequestParam Integer status) {
        goodsService.updateGoodsStatus(goodsId, status);
        return Result.ok();
    }

//    删除商品


    @OperateLog(dateType = "删除商品", operateType = OperateLogConstant.OPERATE_SINGLE_DELETE)
    @DeleteMapping("{goodId}")
    public Result deleteGoodsById(@PathVariable Long goodId) {
        DeleteGoodsOperateDTO deleteGoodsOperateDTO = goodsService.deleteGoods(goodId);
        return Result.ok(deleteGoodsOperateDTO);
    }

//    删除选中的商品

    @OperateLog(dateType = "删除选中的商品", operateType = OperateLogConstant.OPERATE_BATCH_DELETE)
    @DeleteMapping("selected")
    public Result deleteSelect(@RequestBody BatchDeleteCommand batchDeleteCommand) {
        List<DeleteGoodsOperateDTO> deleteGoodsOperateDTOS =
                goodsService.deleteGoodsSelected(batchDeleteCommand);
        return Result.ok(deleteGoodsOperateDTOS);
    }


}
