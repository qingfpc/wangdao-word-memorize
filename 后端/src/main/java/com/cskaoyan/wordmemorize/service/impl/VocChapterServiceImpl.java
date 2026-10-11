package com.cskaoyan.wordmemorize.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cskaoyan.wordmemorize.common.constant.ResultCodeEnum;
import com.cskaoyan.wordmemorize.common.exception.BusinessException;
import com.cskaoyan.wordmemorize.converter.VocCategoryConverter;
import com.cskaoyan.wordmemorize.converter.VocChapterConverter;
import com.cskaoyan.wordmemorize.dao.entity.VocChapterDO;
import com.cskaoyan.wordmemorize.dao.mapper.VocChapterMapper;
import com.cskaoyan.wordmemorize.dto.admin.DeleteChapterOperateDTO;
import com.cskaoyan.wordmemorize.dto.admin.PageDTO;
import com.cskaoyan.wordmemorize.dto.admin.VocChapterDTO;
import com.cskaoyan.wordmemorize.request.BatchDeleteCommand;
import com.cskaoyan.wordmemorize.request.VocChapterCommand;
import com.cskaoyan.wordmemorize.request.VocChapterPageRequest;
import com.cskaoyan.wordmemorize.service.VocChapterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VocChapterServiceImpl implements VocChapterService {
    @Autowired
    VocChapterMapper vocChapterMapper;
    @Autowired
    VocChapterConverter vocChapterConverter;
    //分页查询章节列表
    @Override
    public PageDTO<VocChapterDTO> getChapterPage(VocChapterPageRequest pageQuery) {
        if (pageQuery.getPageNum() == null) {
            List<VocChapterDO> vocChapterDOS = vocChapterMapper.selectList(null);
            return vocChapterConverter.vocChapterDOs2Page(vocChapterDOS, (long) vocChapterDOS.size());
        }
        //需要分页
        Page<VocChapterDO> pageParam = new Page<>(pageQuery.getPageNum(), pageQuery.getCount());
        LambdaQueryWrapper<VocChapterDO> vocChapterWrapper = new LambdaQueryWrapper<>();
        if (pageQuery.getKeyword() != null) {
            vocChapterWrapper.like(VocChapterDO::getChapterName, pageQuery.getKeyword());
        }
        Page<VocChapterDO> vocChapterPage = vocChapterMapper.selectPage(pageParam, vocChapterWrapper);
        return vocChapterConverter.vocChapterDOs2Page(vocChapterPage.getRecords(), vocChapterPage.getTotal());
    }

    //添加章节
    @Override
    public void addChapter(VocChapterCommand command) {
        if (command == null) {
            throw new IllegalArgumentException("添加章节的请求参数不能为空");
        }
        // 检查章节是否已存在，假设通过章节名称判断
        LambdaQueryWrapper<VocChapterDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(VocChapterDO::getChapterName, command.getChapterName());
        VocChapterDO existingChapter = vocChapterMapper.selectOne(queryWrapper);
        if (existingChapter != null) {
            throw new BusinessException(ResultCodeEnum.CHAPTER_HAS_EXISTED);
        }
        VocChapterDO vocChapterDO = vocChapterConverter.vocChapterCommand2DO(command);
        try {
            vocChapterMapper.insert(vocChapterDO);
        } catch (Exception e) {
            // 记录日志并抛出业务异常
            throw new BusinessException(ResultCodeEnum.ADD_CHAPTER_FAIL);
        }
    }


    //修改章节
    @Override
    public void updateChapter(VocChapterCommand command) {
        // 参数校验
        if (command == null || command.getId() == null) {
            throw new IllegalArgumentException("修改章节的请求参数或章节 ID 不能为空");
        }
        // 检查章节是否存在
        VocChapterDO existingChapter = vocChapterMapper.selectById(command.getId());
        if (existingChapter == null) {
            throw new BusinessException(ResultCodeEnum.CHAPTER_NOT_EXISTED);
        }
        VocChapterDO vocChapterDO = vocChapterConverter.vocChapterCommand2DO(command);
        try {
            vocChapterMapper.updateById(vocChapterDO);
        } catch (Exception e) {
            // 记录日志并抛出业务异常
            throw new BusinessException(ResultCodeEnum.UPDATE_CHAPTER_FAIL);
        }
    }

    @Override
    public DeleteChapterOperateDTO deleteChapter(Long chapterId) {
        //初始化返回的日志
        DeleteChapterOperateDTO logDTO = new DeleteChapterOperateDTO();
        //记录传入的ID
        logDTO.setId(chapterId);
        //查询章节信息
        VocChapterDO vocChapterDO = vocChapterMapper.selectById(chapterId);
        //处理章节不存在的情况
        if (vocChapterDO==null){
            return logDTO;
        }
        //章节存在,填充DTO信息
        logDTO.setChapterName(vocChapterDO.getChapterName());
        logDTO.setBookId(vocChapterDO.getBookId());
        logDTO.setBookName(vocChapterDO.getBookName());
        int affectedRows = vocChapterMapper.deleteById(chapterId);
        System.out.println("affectedRows = " + affectedRows);
        return logDTO;
    }

    @Override
    public List<DeleteChapterOperateDTO> deleteChapters(BatchDeleteCommand command) {
        List<String> ids = command.getIds();
        if (ids==null||ids.isEmpty()){
            return Collections.emptyList();
        }
    //查询要删除的章节信息
        List<VocChapterDO> vocChapterDOSToDelete = vocChapterMapper.selectByIds(ids);
        //执行删除操作
        int affectedRows = vocChapterMapper.deleteByIds(ids);
        System.out.println("affectedRows = " + affectedRows);

        //转化为日志DTO并返回
        return vocChapterDOSToDelete.stream()
                .map(chapter->{
                    DeleteChapterOperateDTO dto = new DeleteChapterOperateDTO();
                    dto.setId(chapter.getId());
                    dto.setChapterName(chapter.getChapterName());
                    dto.setBookId(chapter.getBookId());
                    dto.setBookName(chapter.getBookName());
                    return dto;
                }).collect(Collectors.toList());
    }
}
