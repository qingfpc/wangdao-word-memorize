package com.cskaoyan.wordmemorize.service;

import com.cskaoyan.wordmemorize.dto.admin.DeleteGoodsOperateDTO;
import com.cskaoyan.wordmemorize.dto.admin.GoodsDTO;
import com.cskaoyan.wordmemorize.dto.admin.PageDTO;
import com.cskaoyan.wordmemorize.request.GoodsPageRequest;
import com.cskaoyan.wordmemorize.request.GoodsCommand;
import com.cskaoyan.wordmemorize.request.BatchDeleteCommand;

import java.util.List;

public interface GoodsService {

    // 分页获取商品信息(后台商品管理)
    PageDTO<GoodsDTO> getGoodsPage(GoodsPageRequest request);

    // 添加商品(后台商品管理)
    void addGoods(GoodsCommand command);

    // 更新商品(后台商品管理)
    void updateGoods(GoodsCommand command);
    // 删除商品(后台商品管理)
    DeleteGoodsOperateDTO deleteGoods(Long goodsId);

    // 批量删除指定商品(后台商品管理)
    List<DeleteGoodsOperateDTO> deleteGoodsSelected(BatchDeleteCommand command);
    // 更新商品上下架状态(后台商品管理)
    void updateGoodsStatus(Long goodsId, Integer status);
}
