package com.cskaoyan.wordmemorize.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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

            return vocCategoryConverter.bookCategoryList2Page(vocCategoryDOs, (long)vocCategoryDOs.size());
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
        return null;
    }

    @Override
    public void updateBookCategory(VocCategoryCommand command) {

    }

    @Override
    public DeleteBookCategoryOperateDTO deleteBookCategory(Long categoryId) {
        return null;
    }

    @Override
    public List<DeleteBookCategoryOperateDTO> deleteBookCategories(BatchDeleteCommand command) {
        return null;
    }
}
