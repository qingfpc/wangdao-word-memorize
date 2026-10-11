package com.cskaoyan.wordmemorize.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cskaoyan.wordmemorize.common.business.IdBatchDeleter;
import com.cskaoyan.wordmemorize.common.constant.CommonConstant;
import com.cskaoyan.wordmemorize.common.aspect.operate.OperateLog;
import com.cskaoyan.wordmemorize.converter.VocCategoryConverter;
import com.cskaoyan.wordmemorize.dao.entity.VocBookDO;
import com.cskaoyan.wordmemorize.dao.entity.VocCategoryDO;
import com.cskaoyan.wordmemorize.dao.entity.VocChapBookDO;
import com.cskaoyan.wordmemorize.dao.entity.VocChapterDO;
import com.cskaoyan.wordmemorize.dao.mapper.VocBookMapper;
import com.cskaoyan.wordmemorize.dao.mapper.VocCategoryMapper;
import com.cskaoyan.wordmemorize.dao.mapper.VocChapBookMapper;
import com.cskaoyan.wordmemorize.dao.mapper.VocChapterMapper;
import com.cskaoyan.wordmemorize.dto.admin.DeleteBookCategoryOperateDTO;
import com.cskaoyan.wordmemorize.dto.admin.PageDTO;
import com.cskaoyan.wordmemorize.dto.admin.VocCategoryDTO;
import com.cskaoyan.wordmemorize.request.BatchDeleteCommand;
import com.cskaoyan.wordmemorize.request.VocCategoryCommand;
import com.cskaoyan.wordmemorize.request.PageRequest;
import com.cskaoyan.wordmemorize.service.VocCategoryService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class VocCategoryServiceImpl implements VocCategoryService {

    @Resource
    VocCategoryMapper bookCategoryMapper;

    @Resource
    VocCategoryConverter bookCategoryConverter;

    @Resource
    VocChapBookMapper vocChapBookMapper;

    @Resource
    VocBookMapper vocBookMapper;

    @Resource
    VocChapterMapper vocChapterMapper;

    @Override
    public PageDTO<VocCategoryDTO> getBookCategories(PageRequest pageQuery) {
        LambdaQueryWrapper<VocCategoryDO> queryWrapper = new LambdaQueryWrapper<>();
        if (pageQuery.getPageNum() == null) {
            List<VocCategoryDO> vocCategoryDOs = bookCategoryMapper.selectList(null);
            return bookCategoryConverter.bookCategoryList2Page(vocCategoryDOs, 1L);
        }


        Page<VocCategoryDO> page = new Page<>(pageQuery.getPageNum(), pageQuery.getCount());
        if (pageQuery.getKeyword() != null) {
            queryWrapper.like(VocCategoryDO::getCategoryName, pageQuery.getKeyword());
        }

        queryWrapper.orderByAsc(VocCategoryDO::getSortBy);
        bookCategoryMapper.selectPage(page, queryWrapper);
        return bookCategoryConverter.bookCategoryList2Page(page.getRecords(), page.getTotal());
    }

    @Override
    public VocCategoryDTO getBookCategory(Long categoryId) {

        VocCategoryDO bookCategoryDO = bookCategoryMapper.selectById(categoryId);
        return bookCategoryConverter.bookCategoryEntity2DTO(bookCategoryDO);
    }

    @Override
    @OperateLog(dateType = "书籍类目", operateType = 1)
    public Long addBookCategory(VocCategoryCommand command) {
        command.setId(null);
        VocCategoryDO bookCategoryDO = bookCategoryConverter.bookCategoryCommand2Entity(command);
        bookCategoryMapper.insert(bookCategoryDO);
        return bookCategoryDO.getId();
    }

    @Override
    @OperateLog(dateType = "书籍类目", operateType = 4)
    public void updateBookCategory(VocCategoryCommand command) {
        VocCategoryDO bookCategoryDO = bookCategoryConverter.bookCategoryCommand2Entity(command);
        bookCategoryMapper.updateById(bookCategoryDO);
    }

    @Override
    @OperateLog(dateType = "书籍类目", operateType = 2)
    public DeleteBookCategoryOperateDTO deleteBookCategory(Long categoryId) {

        VocCategoryDO vocCategoryDO = bookCategoryMapper.selectById(categoryId);
        DeleteBookCategoryOperateDTO deleteBookCategoryOperateDTO = bookCategoryConverter.bookCategoryDOToDeleteCategoryDTO(vocCategoryDO);

        // 删除类目
        bookCategoryMapper.deleteById(categoryId);

        // 删除类目下的书籍
        LambdaQueryWrapper<VocBookDO> vocBookDOLambdaQueryWrapper = new LambdaQueryWrapper<>();
        vocBookDOLambdaQueryWrapper.eq(VocBookDO::getBcId, categoryId);
        log.info("books start : {}",System.currentTimeMillis());
        List<Long> bookIds = vocBookMapper.selectList(vocBookDOLambdaQueryWrapper)
                .stream().map(vocBookDO -> vocBookDO.getId()).collect(Collectors.toList());
        log.info("books end : {}", System.currentTimeMillis());

        log.info("delete books start : {}",System.currentTimeMillis());
        vocBookMapper.delete(vocBookDOLambdaQueryWrapper);
        log.info("delete books end : {}", System.currentTimeMillis());

        log.info("remain start : {}", System.currentTimeMillis());

        // 删除章节
        IdBatchDeleter<VocChapterDO> chapterIdBatchDeleter
                = new IdBatchDeleter<>(CommonConstant.BATCH, vocChapterMapper);
        chapterIdBatchDeleter.delete(VocChapterDO::getBookId, bookIds);

        // 删除书籍章节关系
        IdBatchDeleter<VocChapBookDO> vocChapBookBatchDeleter
                = new IdBatchDeleter<>(CommonConstant.BATCH, vocChapBookMapper);
        vocChapBookBatchDeleter.delete(VocChapBookDO::getBookId, bookIds);

        log.info("remain end : {}", System.currentTimeMillis());

        return deleteBookCategoryOperateDTO;
    }

    @Override
    @Transactional
    @OperateLog(dateType = "书籍类目", operateType = 3)
    public List<DeleteBookCategoryOperateDTO> deleteBookCategories(BatchDeleteCommand command) {

        List<Long> categoryIds = command.getIds().stream()
                .map(s -> Long.parseLong(s)).collect(Collectors.toList());

        List<VocCategoryDO> vocCategoryDOs = bookCategoryMapper.selectByIds(categoryIds);
        List<DeleteBookCategoryOperateDTO> deleteBookCategoryOperateDTOS = bookCategoryConverter.bookCategoryDOsToDeleteCategoryDTOs(vocCategoryDOs);
        categoryIds.forEach(id -> {
            deleteBookCategory(id);
        });

        return deleteBookCategoryOperateDTOS;
    }

}
