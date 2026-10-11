package com.cskaoyan.wordmemorize.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cskaoyan.wordmemorize.common.business.IdBatchDeleter;
import com.cskaoyan.wordmemorize.common.aspect.operate.OperateLog;
import com.cskaoyan.wordmemorize.converter.GoodsConverter;
import com.cskaoyan.wordmemorize.dao.entity.GoodsDO;
import com.cskaoyan.wordmemorize.dao.mapper.GoodsMapper;
import com.cskaoyan.wordmemorize.dto.admin.DeleteGoodsOperateDTO;
import com.cskaoyan.wordmemorize.dto.admin.GoodsDTO;
import com.cskaoyan.wordmemorize.dto.admin.PageDTO;
import com.cskaoyan.wordmemorize.request.GoodsPageRequest;
import com.cskaoyan.wordmemorize.request.GoodsCommand;
import com.cskaoyan.wordmemorize.request.BatchDeleteCommand;
import com.cskaoyan.wordmemorize.service.GoodsService;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GoodsServiceImpl implements GoodsService {

    @Resource
    GoodsMapper goodsMapper;

    @Resource
    GoodsConverter converter;

    @Override
    public PageDTO<GoodsDTO> getGoodsPage(GoodsPageRequest request) {
        LambdaQueryWrapper<GoodsDO> wrapper = new LambdaQueryWrapper<>();
        if (request.getTypes() != null) {
            //  [1, 3]
            wrapper.in(GoodsDO::getType, request.getTypes());
        }

        if (StringUtils.hasText(request.getKeyword())) {
            wrapper.eq(GoodsDO::getName, request.getKeyword());
        }

        if (request.getPageNum() == null || request.getCount() == 0) {
            List<GoodsDO> goodsDOS = goodsMapper.selectList(wrapper);
            return converter.goodsPage2PageDTO(goodsDOS, (long)goodsDOS.size());
        }
        Page<GoodsDO> goodsDOPage = new Page<>(request.getPageNum(), request.getCount());
        Page<GoodsDO> result = goodsMapper.selectPage(goodsDOPage, wrapper);

        return converter.goodsPage2PageDTO(result.getRecords(), result.getTotal());
    }

    @Override
    @OperateLog(dateType = "商品", operateType = 1)
    public void addGoods(GoodsCommand command) {
        GoodsDO goodsDO = converter.command2GoodsDO(command);
        goodsMapper.insert(goodsDO);
    }


    @Override
    @Transactional
    @OperateLog(dateType = "商品", operateType = 4)
    public void updateGoods(GoodsCommand command) {
        GoodsDO goodsDO = converter.command2GoodsDO(command);
        goodsMapper.updateById(goodsDO);
    }

    @Override
    @Transactional
    @OperateLog(dateType = "商品", operateType = 2)
    public DeleteGoodsOperateDTO deleteGoods(@Parameter(description = "商品id") Long goodsId) {

        GoodsDO goodsDO = goodsMapper.selectById(goodsId);
        DeleteGoodsOperateDTO deleteGoodsOperateDTO = converter.goodsDO2DeleteGoodsDTO(goodsDO);

        goodsMapper.deleteById(goodsId);

        return deleteGoodsOperateDTO;
    }

    @Override
    @Transactional
    @OperateLog(dateType = "商品", operateType = 3)
    public List<DeleteGoodsOperateDTO> deleteGoodsSelected(@RequestBody BatchDeleteCommand command) {
        // 批量删除goods
        List<GoodsDO> goodsDOs = goodsMapper.selectByIds(command.getIds());
        List<DeleteGoodsOperateDTO> deleteGoodsOperateDTOS = converter.goodsDOs2DeleteGoodsDTOs(goodsDOs);

        List<Long> goodsIds = command.getIds()
                .stream().map(idStr -> Long.parseLong(idStr)).collect(Collectors.toList());
        IdBatchDeleter<GoodsDO> goodsDOIdBatchDeleter = new IdBatchDeleter<>(50, goodsMapper);
        goodsDOIdBatchDeleter.delete(GoodsDO::getId, goodsIds);

        return deleteGoodsOperateDTOS;
    }

    @Override
    @OperateLog(dateType = "商品", operateType = 5)
    public void updateGoodsStatus(@Parameter(description = "商品id") Long goodsId, @Parameter(description = "修改字后的状态") Integer status) {
        GoodsDO goodsDO = new GoodsDO();
        goodsDO.setId(goodsId);
        goodsDO.setStatus(status);
        goodsMapper.updateById(goodsDO);
    }
}
