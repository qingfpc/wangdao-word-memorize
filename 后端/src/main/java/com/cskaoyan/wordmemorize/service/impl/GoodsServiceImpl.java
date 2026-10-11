package com.cskaoyan.wordmemorize.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cskaoyan.wordmemorize.converter.GoodsConverter;
import com.cskaoyan.wordmemorize.dao.entity.GoodsDO;
import com.cskaoyan.wordmemorize.dao.mapper.GoodsMapper;
import com.cskaoyan.wordmemorize.dto.admin.DeleteGoodsOperateDTO;
import com.cskaoyan.wordmemorize.dto.admin.GoodsDTO;
import com.cskaoyan.wordmemorize.dto.admin.PageDTO;
import com.cskaoyan.wordmemorize.request.BatchDeleteCommand;
import com.cskaoyan.wordmemorize.request.GoodsCommand;
import com.cskaoyan.wordmemorize.request.GoodsPageRequest;
import com.cskaoyan.wordmemorize.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GoodsServiceImpl implements GoodsService {


    @Autowired
    GoodsMapper goodsMapper;

    @Autowired
    GoodsConverter goodsConverter;

    @Override
    public PageDTO<GoodsDTO> getGoodsPage(GoodsPageRequest pageQuery) {
        if (pageQuery.getPageNum()==null){
            // 查询所有的商品列表
            List<GoodsDO> goodsDOS = goodsMapper.selectList(null);
            return goodsConverter.goodsPage2PageDTO(goodsDOS, (long) goodsDOS.size());
        }
//需要分页
        Page<GoodsDO> pageParam = new Page<>(pageQuery.getPageNum(), pageQuery.getCount());
        LambdaQueryWrapper<GoodsDO> goodsWrapper = new LambdaQueryWrapper<>();
        if (pageQuery.getKeyword() !=null){
            goodsWrapper.like(GoodsDO::getName,pageQuery.getKeyword());
        }

        Page<GoodsDO> goodsDOPage = goodsMapper.selectPage(pageParam, goodsWrapper);
        return goodsConverter.goodsPage2PageDTO(goodsDOPage.getRecords(),goodsDOPage.getTotal());
    }

    @Override
    public void addGoods(GoodsCommand command) {
        if (command==null){
            throw new IllegalArgumentException("商品信息不能为空");
        }

        GoodsDO goodsDO = goodsConverter.command2GoodsDO(command);

        if (goodsDO==null){
            throw new IllegalStateException("书籍转换失败");
        }

        goodsMapper.insert(goodsDO);
    }

    @Override
    public void updateGoods(GoodsCommand command) {
        if (command==null){
            throw new IllegalArgumentException("商品信息不能为空");
        }
        GoodsDO goodsDO = goodsConverter.command2GoodsDO(command);
        goodsDO.setId(Long.parseLong(command.getId()));
        UpdateWrapper<GoodsDO> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id",goodsDO.getId());
        goodsMapper.update(goodsDO,updateWrapper);
    }

    @Override
    public DeleteGoodsOperateDTO deleteGoods(Long goodsId) {

//        初始化返回的日志
        DeleteGoodsOperateDTO logDTO = new DeleteGoodsOperateDTO();
//        记录传入的ID
        logDTO.setId(goodsId);
//查询商品信息
        GoodsDO goodsDO = goodsMapper.selectById(goodsId);
//        处理商品不存在的情况

        if (goodsDO==null){
            return logDTO;
        }

        //        商品存在时,填充DTO的商品信息
        logDTO.setName(goodsDO.getName());

//        执行删除操作

        int affectedRows = goodsMapper.deleteById(goodsId);
        return logDTO;
    }

    @Override
    public List<DeleteGoodsOperateDTO> deleteGoodsSelected(BatchDeleteCommand command) {
        List<String> ids = command.getIds();
        if (ids==null||ids.isEmpty()){
            return Collections.emptyList();
        }
//        查询要删除的商品信息
        List<GoodsDO> goodsDOSToDelete = goodsMapper.selectByIds(ids);

//      执行删除操作
        int affectedRows = goodsMapper.deleteByIds(ids);

        return goodsDOSToDelete.stream()
                .map(goods->{
                    DeleteGoodsOperateDTO dto = new DeleteGoodsOperateDTO();
                    dto.setId(goods.getId());
                    dto.setName(goods.getName());
                    return dto;
                }).collect(Collectors.toList());

    }

    @Override
    public void updateGoodsStatus(Long goodsId, Integer status) {
        // 1. 参数合法性校验
        if (goodsId==null){
            throw new IllegalArgumentException("商品ID不能为空");
        }

        if (status==null||(status!=0 && status != 1)){
            throw new IllegalArgumentException("状态值不合法,必修是0(下架) 或1(上架)");
        }

        // 2. 构建更新实体（只设置需要更新的字段）
        GoodsDO goods = new GoodsDO();
        goods.setStatus(status);


        // 3. 构建更新条件（根据商品ID定位）

        UpdateWrapper<GoodsDO> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id",goodsId);
        // 4. 执行更新并校验结果

        int affectedRows = goodsMapper.update(goods, updateWrapper);

        if (affectedRows==0){
            throw new RuntimeException("更新失败，未找到ID为" + goodsId + "的商品或状态未变更");
        }
    }
}
