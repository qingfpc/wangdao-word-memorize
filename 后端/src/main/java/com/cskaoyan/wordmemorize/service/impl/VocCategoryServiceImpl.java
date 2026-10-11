package com.cskaoyan.wordmemorize.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cskaoyan.wordmemorize.converter.VocCategoryConverter;
import com.cskaoyan.wordmemorize.dao.entity.VocCategoryDO;
import com.cskaoyan.wordmemorize.dao.mapper.VocCategoryMapper;
import com.cskaoyan.wordmemorize.dto.admin.DeleteBookCategoryOperateDTO;
import com.cskaoyan.wordmemorize.dto.admin.PageDTO;
import com.cskaoyan.wordmemorize.dto.admin.VocCategoryDTO;
import com.cskaoyan.wordmemorize.request.BatchDeleteCommand;
import com.cskaoyan.wordmemorize.request.PageRequest;
import com.cskaoyan.wordmemorize.request.VocCategoryCommand;
import com.cskaoyan.wordmemorize.service.VocCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class VocCategoryServiceImpl implements VocCategoryService {


    @Autowired
    VocCategoryMapper vocCategoryMapper;


    @Autowired
    VocCategoryConverter vocCategoryConverter;

    @Override
    public PageDTO<VocCategoryDTO> getBookCategories(PageRequest pageQuery) {

        if (pageQuery.getPageNum() == null) {
            // 查询所有的书籍类目
            List<VocCategoryDO> vocCategoryDOs = vocCategoryMapper.selectList(null);

            return vocCategoryConverter.bookCategoryList2Page(vocCategoryDOs, (long) vocCategoryDOs.size());
        }

        // 需要分页
        Page<VocCategoryDO> pageParam = new Page<>(pageQuery.getPageNum(), pageQuery.getCount());
        LambdaQueryWrapper<VocCategoryDO> vocCategoryWrapper = new LambdaQueryWrapper<>();
        if (pageQuery.getKeyword() != null) {
            vocCategoryWrapper.like(VocCategoryDO::getCategoryName, pageQuery.getKeyword());
        }
        Page<VocCategoryDO> vocCategoryPage = vocCategoryMapper.selectPage(pageParam, vocCategoryWrapper);


        return vocCategoryConverter.bookCategoryList2Page(vocCategoryPage.getRecords(), vocCategoryPage.getTotal());
    }

    @Override
    public VocCategoryDTO getBookCategory(Long categoryId) {
        return null;
    }

    @Override
    public Long addBookCategory(VocCategoryCommand command) {
        //转换为实体类
        VocCategoryDO vocCategoryDO = vocCategoryConverter.bookCategoryCommand2Entity(command);
        //将新的书籍类目插入到数据库中
        vocCategoryMapper.insert(vocCategoryDO);
        //查询新插入到数据库的书籍类目的Id
        QueryWrapper<VocCategoryDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("category_name", vocCategoryDO.getCategoryName());
        VocCategoryDO targetVocCategory = vocCategoryMapper.selectOne(queryWrapper);
        //返回书籍类目的Id
        return targetVocCategory.getId();
    }

    @Override
    public void updateBookCategory(VocCategoryCommand command) {
        //转换为实体类
        VocCategoryDO vocCategoryDO = vocCategoryConverter.bookCategoryCommand2Entity(command);
        //更新数据
        UpdateWrapper<VocCategoryDO> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", vocCategoryDO.getId())
                .set("category_name", vocCategoryDO.getCategoryName())
                .set("sort_by", vocCategoryDO.getSortBy());
        vocCategoryMapper.update(null, updateWrapper);
    }

    @Override
    public DeleteBookCategoryOperateDTO deleteBookCategory(Long categoryId) {
        //获取删除的内容用于输出日志
        VocCategoryDO vocCategoryDO = vocCategoryMapper.selectById(categoryId);
        //执行删除操作
        int effectRows = vocCategoryMapper.deleteById(categoryId);
        //返回DeleteCategoryDTO
        return vocCategoryConverter.bookCategoryDOToDeleteCategoryDTO(vocCategoryDO);
    }

    @Override
    public List<DeleteBookCategoryOperateDTO> deleteBookCategories(BatchDeleteCommand command) {
        //获取删除的内容用于输出日志
        QueryWrapper<VocCategoryDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", command.getIds());
        List<VocCategoryDO> vocCategoryDOS = vocCategoryMapper.selectList(queryWrapper);
        List<DeleteBookCategoryOperateDTO> deleteBookCategoryOperateDTOS = vocCategoryConverter.bookCategoryDOsToDeleteCategoryDTOs(vocCategoryDOS);
        //批量删除书籍类目
        vocCategoryMapper.deleteByIds(command.getIds());
        //返回删除的内容1
        return deleteBookCategoryOperateDTOS;
    }
}
