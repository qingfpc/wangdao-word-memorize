package com.cskaoyan.wordmemorize.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cskaoyan.wordmemorize.common.aspect.operate.OperateLog;
import com.cskaoyan.wordmemorize.converter.VocBookConverter;
import com.cskaoyan.wordmemorize.dao.entity.VocBookDO;
import com.cskaoyan.wordmemorize.dao.entity.VocCategoryDO;
import com.cskaoyan.wordmemorize.dao.entity.VocChapBookDO;
import com.cskaoyan.wordmemorize.dao.entity.VocChapterDO;
import com.cskaoyan.wordmemorize.dao.mapper.VocBookMapper;
import com.cskaoyan.wordmemorize.dao.mapper.VocCategoryMapper;
import com.cskaoyan.wordmemorize.dao.mapper.VocChapBookMapper;
import com.cskaoyan.wordmemorize.dao.mapper.VocChapterMapper;
import com.cskaoyan.wordmemorize.dto.admin.DeleteBookOperateDTO;
import com.cskaoyan.wordmemorize.dto.admin.PageDTO;
import com.cskaoyan.wordmemorize.dto.admin.VocBookDTO;
import com.cskaoyan.wordmemorize.request.VocBookCommand;
import com.cskaoyan.wordmemorize.request.BatchDeleteCommand;
import com.cskaoyan.wordmemorize.request.VocBookPageRequest;
import com.cskaoyan.wordmemorize.service.VocBookService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VocBookServiceImpl implements VocBookService {

    @Resource
    VocBookMapper vocBookMapper;

    @Resource
    VocBookConverter vocBookConverter;

    @Resource
    VocChapBookMapper vocChapBookMapper;

    @Resource
    VocChapterMapper vocChapterMapper;

    @Resource
    VocCategoryMapper vocCategoryMapper;

    @Override
    public PageDTO<VocBookDTO> getBookPage(VocBookPageRequest pageQuery) {
        LambdaQueryWrapper<VocBookDO> queryWrapper = new LambdaQueryWrapper<>();
        if (pageQuery.getCategoryId() != null) {
            // 查询多个目录的书籍
            queryWrapper.in(VocBookDO::getBcId, pageQuery.getCategoryId());
        }
        if (pageQuery.getPageNum() == null) {
            // 此时说明是在 章节的下拉框中的查询
            List<VocBookDO> vocBookDOs = vocBookMapper.selectList(queryWrapper);
            return vocBookConverter.vocBookList2Page(vocBookDOs, (long)vocBookDOs.size());
        }
        Page<VocBookDO> page = new Page<>(pageQuery.getPageNum(), pageQuery.getCount());
        if (pageQuery.getKeyword() != null) {
            queryWrapper.like(VocBookDO::getName, pageQuery.getKeyword());
        }

        // 排序
        queryWrapper.orderByAsc(VocBookDO::getSortBy);
        vocBookMapper.selectPage(page, queryWrapper);

        return vocBookConverter.vocBookList2Page(page.getRecords(), page.getTotal());
    }

    @Override
    @OperateLog(dateType = "书籍", operateType = 1)
    public Long addVocBook(VocBookCommand command) {
        VocBookDO vocBookDO = vocBookConverter.vocBookCommand2VocBookDO(command);
        vocBookMapper.insert(vocBookDO);

        // 修改类目下的书籍数量
        LambdaUpdateWrapper<VocCategoryDO> vocCategoryDOWrapper = Wrappers.lambdaUpdate(VocCategoryDO.class)
                .eq(VocCategoryDO::getId, Long.parseLong(command.getBcId()))
                .setSql("book_count = book_count + 1");
        vocCategoryMapper.update(vocCategoryDOWrapper);
        return vocBookDO.getId();
    }

    @Override
    @OperateLog(dateType = "书籍", operateType = 4)
    public void updateVocBook(VocBookCommand command) {
        // 查询数据库中的旧数据
        VocBookDO vocBookDO = vocBookConverter.vocBookCommand2VocBookDO(command);
        vocBookDO.setId(Long.parseLong(command.getId()));
        vocBookMapper.updateById(vocBookDO);
    }

    @Override
    @OperateLog(dateType = "书籍", operateType = 2)
    public DeleteBookOperateDTO deleteVocBook(Long bookId) {


        VocBookDO vocBookDO = vocBookMapper.selectById(bookId);
        DeleteBookOperateDTO deleteBookOperateDTO = vocBookConverter.vocBookDO2DeleteBookDTO(vocBookDO);

        // 删除书籍
        vocBookMapper.deleteById(bookId);

        // 删除书籍下的章节
        LambdaQueryWrapper<VocChapterDO> chapterQueryWrapper = new LambdaQueryWrapper<>();
        chapterQueryWrapper.eq(VocChapterDO::getBookId, bookId);
        vocChapterMapper.delete(chapterQueryWrapper);

        // 删除书籍下的章节，单词关系列表
        LambdaQueryWrapper<VocChapBookDO> vocChapBookLambdaQueryWrapper = new LambdaQueryWrapper<>();
        vocChapBookLambdaQueryWrapper.eq(VocChapBookDO::getBookId, bookId);
        vocChapBookMapper.delete(vocChapBookLambdaQueryWrapper);

        // 修改类目下的书籍数量
        LambdaUpdateWrapper<VocCategoryDO> vocCategoryDOWrapper = Wrappers.lambdaUpdate(VocCategoryDO.class)
                .eq(VocCategoryDO::getId, bookId)
                .setSql("book_count = book_count - 1");
        vocCategoryMapper.update(vocCategoryDOWrapper);

        return deleteBookOperateDTO;
    }

    @Override
    @OperateLog(dateType = "书籍", operateType = 3)
    public List<DeleteBookOperateDTO> deleteVocBooks(BatchDeleteCommand command) {
        List<Long> bookIds = command.getIds().stream()
                .map(s -> Long.parseLong(s)).collect(Collectors.toList());

        List<VocBookDO> vocBookDOs = vocBookMapper.selectBatchIds(bookIds);
        List<DeleteBookOperateDTO> deleteBookOperateDTOS
                = vocBookConverter.vocBookDOs2DeleteBookDTOs(vocBookDOs);


        bookIds.forEach( bookId -> {
            // 删除书籍
            vocBookMapper.deleteById(bookId);

            LambdaQueryWrapper<VocChapterDO> chapterQueryWrapper = new LambdaQueryWrapper<>();
            chapterQueryWrapper.eq(VocChapterDO::getBookId, bookId);
            // 删除书籍下的章节
            vocChapterMapper.delete(chapterQueryWrapper);

            // 删除书籍章节单词关系
            LambdaQueryWrapper<VocChapBookDO> vocChapBookQueryWrapper = new LambdaQueryWrapper<>();
            vocChapBookQueryWrapper.eq(VocChapBookDO::getBookId, bookId);
            vocChapBookMapper.delete(vocChapBookQueryWrapper);

        });

        // 更新类目下的书籍数量
        LambdaUpdateWrapper<VocCategoryDO> vocCategoryDOWrapper = Wrappers.lambdaUpdate(VocCategoryDO.class)
                .in(VocCategoryDO::getId, bookIds)
                .setSql("book_count = book_count - 1");
        vocCategoryMapper.update(vocCategoryDOWrapper);



        return deleteBookOperateDTOS;

    }

}
