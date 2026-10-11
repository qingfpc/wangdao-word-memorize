package com.cskaoyan.wordmemorize.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cskaoyan.wordmemorize.common.aspect.operate.OperateLog;
import com.cskaoyan.wordmemorize.converter.VocChapterConverter;
import com.cskaoyan.wordmemorize.dao.entity.VocBookDO;
import com.cskaoyan.wordmemorize.dao.entity.VocChapBookDO;
import com.cskaoyan.wordmemorize.dao.entity.VocChapterDO;
import com.cskaoyan.wordmemorize.dao.mapper.VocBookMapper;
import com.cskaoyan.wordmemorize.dao.mapper.VocChapBookMapper;
import com.cskaoyan.wordmemorize.dao.mapper.VocChapterMapper;
import com.cskaoyan.wordmemorize.dto.admin.DeleteChapterOperateDTO;
import com.cskaoyan.wordmemorize.dto.admin.PageDTO;
import com.cskaoyan.wordmemorize.dto.admin.VocChapterDTO;
import com.cskaoyan.wordmemorize.request.BatchDeleteCommand;
import com.cskaoyan.wordmemorize.request.VocChapterCommand;
import com.cskaoyan.wordmemorize.request.VocChapterPageRequest;
import com.cskaoyan.wordmemorize.service.VocChapterService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VocChapterServiceImpl implements VocChapterService {

    @Resource
    VocChapterMapper vocChapterMapper;

    @Resource
    VocChapterConverter vocChapterConverter;

    @Resource
    VocChapBookMapper vocChapBookMapper;

    @Resource
    VocBookMapper vocBookMapper;

    @Override
    public PageDTO<VocChapterDTO> getChapterPage(VocChapterPageRequest pageQuery) {

        LambdaQueryWrapper<VocChapterDO> queryWrapper = new LambdaQueryWrapper<>();

        if (StringUtils.hasText(pageQuery.getKeyword())) {
            queryWrapper.like(VocChapterDO::getChapterName,pageQuery.getKeyword());
        }

        if (pageQuery.getPageNum() == null) {
            queryWrapper.eq(VocChapterDO::getBookId, Long.parseLong(pageQuery.getBookId()));
            // 下拉框
            List<VocChapterDO> vocChapterDOs = vocChapterMapper.selectList(queryWrapper);
            return vocChapterConverter.vocChapterDOs2Page(vocChapterDOs, (long) vocChapterDOs.size());
        }

        //分页
        if (pageQuery.getBookId() != null) {
            queryWrapper.eq(VocChapterDO::getBookId, Long.parseLong(pageQuery.getBookId()));
        }
        Page<VocChapterDO> page = new Page<>(pageQuery.getPageNum(), pageQuery.getCount());

        queryWrapper.orderByAsc(VocChapterDO::getSortBy);
        Page<VocChapterDO> vocChapterDOPage = vocChapterMapper.selectPage(page, queryWrapper);
        return vocChapterConverter.vocChapterDOs2Page(vocChapterDOPage.getRecords(), vocChapterDOPage.getTotal());
    }

    @Override
    @Transactional
    @OperateLog(dateType = "章节", operateType = 1)
    public void addChapter(VocChapterCommand command) {
        VocChapterDO vocChapterDO = vocChapterConverter.vocChapterCommand2DO(command);
        vocChapterMapper.insert(vocChapterDO);

        // 修改对应书籍的章节数量
        UpdateWrapper<VocBookDO> vocChapterWrapper = new UpdateWrapper<>();
        String bookId = command.getBookId();
        vocChapterWrapper.eq("id", Long.parseLong(bookId));
        vocChapterWrapper.setSql("chapter_count = chapter_count + 1");
        vocBookMapper.update(vocChapterWrapper);

    }

    @Override
    @Transactional
    @OperateLog(dateType = "章节", operateType = 4)
    public void updateChapter(VocChapterCommand command) {
            // 所属书籍未发生变化
            VocChapterDO vocChapterDO = vocChapterConverter.vocChapterCommand2DO(command);
            vocChapterMapper.updateById(vocChapterDO);
    }

    @Override
    @Transactional
    @OperateLog(dateType = "章节", operateType = 2)
    public DeleteChapterOperateDTO deleteChapter(Long chapterId) {

        VocChapterDO vocChapterDO = vocChapterMapper.selectById(chapterId);

        // 删除章节
        vocChapterMapper.deleteById(chapterId);

        // 删除章节下的单词
        LambdaQueryWrapper<VocChapBookDO> vocChapBookLambdaQueryWrapper = new LambdaQueryWrapper<>();
        vocChapBookLambdaQueryWrapper.eq(VocChapBookDO::getChapterId, chapterId);

        Long count = vocChapBookMapper.selectCount(vocChapBookLambdaQueryWrapper);

        vocChapBookMapper.delete(vocChapBookLambdaQueryWrapper);

        // 更新书籍包含的章节数量
        LambdaUpdateWrapper<VocBookDO> vocBookDOWrapper = Wrappers.lambdaUpdate(VocBookDO.class)
                .eq(VocBookDO::getId, vocChapterDO.getBookId())
                .setSql("chapter_count = chapter_count - 1")
                        .setSql("voc_count = voc_count - " + count);
        vocBookMapper.update(vocBookDOWrapper);


        return vocChapterConverter.vocChapterDO2DeleteDTO(vocChapterDO);

    }

    @Override
    @Transactional
    @OperateLog(dateType = "章节", operateType = 3)
    public List<DeleteChapterOperateDTO> deleteChapters(BatchDeleteCommand command) {

        List<Long> ids = command.getIds().stream()
                .map(idStr -> Long.parseLong(idStr)).collect(Collectors.toList());

        List<VocChapterDO> vocChapterDOs = vocChapterMapper.selectBatchIds(ids);

        List<DeleteChapterOperateDTO> deleteChapterOperateDTOS
                = vocChapterConverter.vocChapterDOs2DeleteDTOs(vocChapterDOs);

        Long count = 0L;
        for ( Long id : ids) {
            vocChapterMapper.deleteById(id);

            LambdaQueryWrapper<VocChapBookDO> vocChapBookLambdaQueryWrapper = new LambdaQueryWrapper<>();
            vocChapBookLambdaQueryWrapper.eq(VocChapBookDO::getChapterId, id);

            count += vocChapBookMapper.selectCount(vocChapBookLambdaQueryWrapper);
            vocChapBookMapper.delete(vocChapBookLambdaQueryWrapper);
        }

        // 更新书籍包含的章节数量
        LambdaUpdateWrapper<VocBookDO> vocBookDOWrapper = Wrappers.lambdaUpdate(VocBookDO.class)
                .in(VocBookDO::getId, ids)
                .setSql("chapter_count = chapter_count - 1")
                        . setSql("voc_count = chapter_count - " + count);
        vocBookMapper.update(vocBookDOWrapper);


        return deleteChapterOperateDTOS;
    }
}
